/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhie.openempi.report.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.ReportDao;
import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportRequest;
import org.openhie.openempi.model.ReportRequestEntry;
import org.openhie.openempi.model.ReportRequestParameter;
import org.openhie.openempi.report.ReportGenerator;
import org.openhie.openempi.report.ReportService;
import org.openhie.openempi.service.ValidationService;
import org.openhie.openempi.service.impl.BaseServiceImpl;
import org.openhie.openempi.util.SessionGenerator;

import com.ibm.icu.text.SimpleDateFormat;

public class ReportServiceImpl extends BaseServiceImpl implements ReportService
{
	private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MMMMMMMM dd yyyy");
	private ReportDao reportDao;
    private static File reportDataDirectory;
	
	public Report addReport(Report report) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(report);
		
		return reportDao.addReport(report);
	}

	public void deleteReport(Report report) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(report);

		reportDao.removeReport(report);
	}

	public List<Report> getReports() {
		return reportDao.getReports();
	}
	
	public void reassignReportRequestsToReport(Integer oldReportId, Integer newReportId) {
		reportDao.reassignReportRequestsToReport(oldReportId, newReportId);
	}
	
	public ReportRequestEntry generateReport(ReportRequest reportRequest) throws ApplicationException {
		if (reportRequest == null || reportRequest.getReportId() == null) {
			log.error("Received a request to generate a report without the appropriate report identifier: " + reportRequest);
			throw new ApplicationException("Received an invalid report generation request.");
		}
		
		Report report = reportDao.getReportById(reportRequest.getReportId());
		if (report == null) {
			log.error("Received a request to generate a report with an unknown report identifier: " + reportRequest);
			throw new ApplicationException("Received a report request for an unknown report.");
		}
		
		ReportGenerator generator = retrieveReportDataGenerator(report.getDataGenerator());
		reportRequest.setRequestDate(new java.util.Date());
		ReportRequestEntry request = buildReportRequest(reportRequest, report);
		reportDao.addReportRequest(request);
		
		ReportGeneratorTask reportGeneratorTask = new ReportGeneratorTask(report, reportRequest, request, reportDao, generator);
		reportGeneratorTask.setReportDataDirectory(getReportDataDirectory());
		Future<Object> future = Context.scheduleTask(reportGeneratorTask);
		log.debug("Scheduled a report generation request: " + future);
		return request;
	}

	public File getReportDataDirectory() {
		if (reportDataDirectory != null) {
			return reportDataDirectory;
		}
		reportDataDirectory = new File(Context.getOpenEmpiHome() + "/" + Constants.REPORT_DATA_DIRECTORY);
		if (!reportDataDirectory.exists()) {
			boolean createDirectory = reportDataDirectory.mkdir();
			log.debug("Creating the report data directory at " + reportDataDirectory.getAbsolutePath() + " returned " + createDirectory);
		}
		return reportDataDirectory;
	}
	
	private ReportRequestEntry buildReportRequest(ReportRequest reportRequest, Report report) {
		ReportRequestEntry entry = new ReportRequestEntry();
		entry.setReport(report);
		entry.setCompleted(ReportRequestEntry.NOT_COMPLETED_PARAMETER);
		entry.setDateRequested(reportRequest.getRequestDate());
		entry.setUserRequested(Context.getUserContext().getUser());
		return entry;
	}
	
	private ReportGenerator retrieveReportDataGenerator(String dataGenerator) throws ApplicationException {
		ReportGenerator generator = (ReportGenerator) Context.getApplicationContext().getBean(dataGenerator);
		if (generator == null) {
			log.warn("Retrieved a request to generate a report using an unknown data generator: " + dataGenerator);
			throw new ApplicationException("The report configuration is invalid. Please check with your system administrator.");
		}
		return generator;
	}

	public Report getReportByName(String reportName) {
		return reportDao.getReportByName(reportName);
	}

	public Report getReportById(Integer reportId) {
		return reportDao.getReportById(reportId);
	}

	public Report updateReport(Report report) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(report);
		
		return reportDao.updateReport(report);
	}
	
	public List<ReportRequestEntry> getReportRequests() {
		return reportDao.getReportRequests();
	}

	public ReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}
	
	private static class ReportGeneratorTask implements Callable<Object>
	{
		protected final Log log = LogFactory.getLog(getClass());
		
		private Report report;
		private ReportRequest reportRequest;
		private ReportRequestEntry requestEntry;
		private ReportDao reportDao;
		private File reportDataDirectory;
		private ReportGenerator dataGenerator;

		public ReportGeneratorTask(Report report, ReportRequest reportRequest, ReportRequestEntry requestEntry, ReportDao reportDao, ReportGenerator dataGenerator) {
			this.report = report;
			this.reportRequest = reportRequest;
			this.requestEntry = requestEntry;
			this.reportDao = reportDao;
			this.dataGenerator = dataGenerator;
		}
		
		public Object call() throws Exception {
			long start = System.currentTimeMillis();
			Map<String,Object> params = new HashMap<String,Object>();
			try {
				JRXmlDataSource dataSource = generateData(reportRequest, report);
				prepareReportParameters(params, reportRequest);
				
				File templateFile = new File(generateReportTemplateName(report));
				JasperPrint reportOutput = JasperFillManager.fillReport(new FileInputStream(templateFile), params, dataSource);
				String reportHandle = generateReportHandle(report, reportRequest);
				String outputFilename = generateReportOutputFilename(reportHandle);
				JasperExportManager.exportReportToPdfFile(reportOutput, outputFilename);
				requestEntry.setReportHandle(reportHandle);
				requestEntry.setCompleted(ReportRequestEntry.COMPLETED_PARAMETER);
				requestEntry.setDateCompleted(new java.util.Date());
				reportDao.updateReportRequest(requestEntry);
				log.debug("Filling time : " + (System.currentTimeMillis() - start));
				return requestEntry;
			} catch (JRException e) {
				log.error("Failed while generating the report: " + e, e);
				throw new ApplicationException("Report generation failed: " + e.getMessage());
			} catch (FileNotFoundException e) {
				log.error("Unable to locate the template file named " + report.getTemplateName(), e);
				throw new ApplicationException("Unable to locate the template for the report: " + report.getNameDisplayed());
			}
		}

		private void prepareReportParameters(Map<String, Object> params, ReportRequest reportRequest) {
			params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
			params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
			params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
			params.put("SUBREPORT_DIR", Context.getOpenEmpiHome() + "/conf/report-templates/");
			params.put(JRParameter.REPORT_LOCALE, Locale.US);
			
			for (ReportRequestParameter requestParam : reportRequest.getReportParameters()) {
				String paramName = transformParamNameForReport(requestParam.getParameterName());
				String paramValue = transformParamValueForReport(requestParam.getParameterValue());
				log.debug("Adding to report parameter: " + paramName + " with value " + paramValue);
				params.put(paramName, paramValue);
			}
		}		
		
		private JRXmlDataSource generateData(ReportRequest reportRequest, Report report) throws ApplicationException {
			log.debug("Retrieving data generator " + report.getDataGenerator() + " for report named: " + report.getName());
			JRXmlDataSource dataSource = dataGenerator.generateReportDataSource(reportRequest, report);
			return dataSource;
		}

		private String generateReportHandle(Report report, ReportRequest reportRequest) {
			String sessionId = SessionGenerator.generateSessionId();
			StringBuffer outputFilename = new StringBuffer(report.getName())
				.append("-").append(sessionId)
				.append("-").append(reportRequest.getRequestDate().getTime())
				.append(".pdf");
			return outputFilename.toString();
		}

		private String generateReportOutputFilename(String reportHandle) {
			File outputFile = new File(getReportDataDirectory(), reportHandle);
			log.debug("Will generate report output in: " + outputFile.getAbsolutePath());
			return outputFile.getAbsolutePath();
		}
		
		private String transformParamValueForReport(Serializable parameterValue) {
			String value = null;
			if (parameterValue instanceof java.util.Date) {
				value = DATE_FORMATTER.format(parameterValue);
			} else {
				value = parameterValue.toString();
			}
			return value;
		}
		
		public File getReportDataDirectory() {
			return reportDataDirectory;
		}

		public void setReportDataDirectory(File reportDataDirectory) {
			this.reportDataDirectory = reportDataDirectory;
		}

		// Replaces '-' characters with underscores and upper-cases the name
		//
		private String transformParamNameForReport(String parameterName) {
			String name = parameterName.replace('-', '_');
			return name.toUpperCase();
		}

		private String generateReportTemplateName(Report report) {
			String reportTemplateFilename = Context.getOpenEmpiHome() + 
					"/conf/report-templates/" + report.getTemplateName() + ".jasper";
			log.debug("Retrieving report template for report " + report.getName() + " from " + reportTemplateFilename);
			return reportTemplateFilename;
		}
	}
}
