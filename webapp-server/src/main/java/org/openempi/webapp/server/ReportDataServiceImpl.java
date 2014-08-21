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
package org.openempi.webapp.server;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openempi.webapp.client.ReportDataService;
import org.openempi.webapp.client.model.ReportRequestEntryWeb;
import org.openempi.webapp.client.model.ReportRequestWeb;
import org.openempi.webapp.client.model.ReportWeb;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportRequestEntry;
import org.openhie.openempi.report.ReportService;

public class ReportDataServiceImpl extends AbstractRemoteServiceServlet implements ReportDataService
{
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	public List<ReportWeb> getReports() throws Exception {
		
		authenticateCaller();	
		try {
			ReportService reportService = Context.getReportService();
			List<org.openhie.openempi.model.Report> reports = reportService.getReports();
			List<ReportWeb> dtos = new java.util.ArrayList<ReportWeb>(reports.size());
			for (Report rs : reports) {
				ReportWeb rw = ModelTransformer.mapToReport( rs, ReportWeb.class);
				dtos.add(rw);
			}
			
			return dtos;			
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	public ReportWeb addReport(ReportWeb reportWeb) throws Exception {
		log.debug("Received request to add a new report entry to the repository.");
		
		authenticateCaller();	
		try {
			ReportService reportService = Context.getReportService();
			org.openhie.openempi.model.Report report = ModelTransformer.mapToReport(reportWeb, org.openhie.openempi.model.Report.class);
			Report addedReport = reportService.addReport(report);
			
			return ModelTransformer.mapToReport( addedReport, ReportWeb.class);
			
		} catch (Throwable t) {	
			throw new Exception(t.getMessage());
		}
	}

	public String deleteReport(ReportWeb reportWeb) throws Exception {
		log.debug("Received request to delete report entry to the repository.");
		
		authenticateCaller();	
		String msg = "";
		try {
			ReportService reportService = Context.getReportService();
			org.openhie.openempi.model.Report report = ModelTransformer.mapToReport(reportWeb, org.openhie.openempi.model.Report.class);
			reportService.deleteReport(report);
		} catch (Throwable t) {
			log.error("Failed while deleting a report entry: " + t, t);
			msg = t.getMessage();
			throw new Exception(t.getMessage());
		}
		return msg;
	}
	
	public ReportWeb updateReport(ReportWeb reportWeb) throws Exception {
		log.debug("Received request to update the person entry in the repository.");
		
		authenticateCaller();	
		try {
			ReportService reportService = Context.getReportService();
			org.openhie.openempi.model.Report report = ModelTransformer.mapToReport(reportWeb, org.openhie.openempi.model.Report.class);
			int oldReportId = report.getReportId();
			reportService.deleteReport(report);
			// Report updatedReport = reportService.updateReport(report);
			
			report.setReportId(null);
			for (org.openhie.openempi.model.ReportQuery query : report.getReportQueries()) {
				query.setReportQueryId(null);
				for (org.openhie.openempi.model.ReportQueryParameter queryParam : query.getReportQueryParameters()) {
					queryParam.setReportQueryParameterId(null);
				}
			}
			for (org.openhie.openempi.model.ReportParameter param : report.getReportParameters()) {
				param.setReportParameterId(null);
			}
			org.openhie.openempi.model.Report updatedReport = reportService.addReport(report);
			int newReportId = updatedReport.getReportId();
			reportService.reassignReportRequestsToReport(oldReportId, newReportId);
			return ModelTransformer.mapToReport( updatedReport, ReportWeb.class);

		} catch (Throwable t) {
			log.error("Failed while updating a person entry: " + t, t);
			throw new Exception(t.getMessage());
		}
	}
	
	public ReportRequestEntryWeb generateReport(ReportRequestWeb reportRequestWeb) throws Exception {
		
		authenticateCaller();	
		try {
			ReportService reportService = Context.getReportService();
			org.openhie.openempi.model.ReportRequest reportRequest = ModelTransformer.mapToReportRequest(reportRequestWeb, org.openhie.openempi.model.ReportRequest.class);
			
			org.openhie.openempi.model.ReportRequestEntry reportRequestEntry = reportService.generateReport(reportRequest);
			
			ReportRequestEntryWeb reportRequestEntryWeb = ModelTransformer.mapToReportRequestEntry( reportRequestEntry, ReportRequestEntryWeb.class);
			
//			String reportHandle = reportRequestEntryWeb.getReportHandle();
			
			return reportRequestEntryWeb;

		} catch (Throwable t) {
			log.error("Failed while generating report: " + t, t);
			throw new Exception(t.getMessage());
		}				
	}
	
	public List<ReportRequestEntryWeb> getReportRequests()  throws Exception {
		
		authenticateCaller();	
		try {
			ReportService reportService = Context.getReportService();
			List<org.openhie.openempi.model.ReportRequestEntry> reportEntries = reportService.getReportRequests();
			List<ReportRequestEntryWeb> dtos = new java.util.ArrayList<ReportRequestEntryWeb>(reportEntries.size());
			for (ReportRequestEntry rs : reportEntries) {
				ReportRequestEntryWeb rw = ModelTransformer.mapToReportRequestEntry( rs, ReportRequestEntryWeb.class);
				dtos.add(rw);
			}			
			return dtos;			
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
}
