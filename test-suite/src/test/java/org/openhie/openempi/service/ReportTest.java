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
package org.openhie.openempi.service;

import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportParameter;
import org.openhie.openempi.model.ReportQuery;
import org.openhie.openempi.model.ReportQueryParameter;
import org.openhie.openempi.report.ReportService;

public class ReportTest extends BaseServiceTestCase
{

	public void testAddReport() {
		
		ReportService reportService = null; 
		try {
			reportService = Context.getReportService();
			Report report = buildReportObject();
			reportService.addReport(report);
			
		} catch (Throwable t) {
			t.printStackTrace();
		}

		try {
			List<Report> reports = reportService.getReports();
			for (Report report : reports) {
				displayReport(report);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testUpdateDeleteReport() {
		
		ReportService reportService = Context.getReportService(); 
		try {
			List<Report> reports = reportService.getReports();
			if (reports == null || reports.size() == 0) {
				return;
			}
			Report report = getReportByName(reports);
			if (report == null) {
				return;
			}
			
			ReportQuery query = report.getReportQueryByName("query-one");
			if (query != null) {
				query.setQuery("select p.* from person_identifier p where date_voided is null");
			}
			ReportParameter param = report.getReportParameterByName("start-date");
			if (param != null) {
				param.setNameDisplayed("Staring Date");
			}
			report.setNameDisplayed("Person Identifiers List");
			
			ReportParameter tempParam = report.getReportParameterByName("temp-param");
			if (tempParam != null) {
				report.removeReportParameter(tempParam);
			}
			log.debug("Updating the selected report: " + report.getName());
			reportService.updateReport(report);
			displayReport(report);
			
			reportService.deleteReport(report);
			reports = reportService.getReports();
			if (reports == null || reports.size() == 0) {
				log.debug("No reports were found after the delete operation.");
			}
			
			report = buildReportObject();
			reportService.addReport(report);
			log.debug("Added another report");
			displayReport(report);
			
			log.debug("Deleting the second report.");
			reportService.deleteReport(report);
			
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private Report buildReportObject() {
		Report report = new Report();
		report.setDescription("Sample Test Report");
		report.setName("test-report");
		report.setNameDisplayed("Test Report");
		report.setTemplateName("test-report.jrxml");
		report.setDataGenerator("testReportDataGenerator");
		
		ReportParameter startDateParam = new ReportParameter();
		startDateParam.setDescription("Start Date Parameter");
		startDateParam.setName("start-date");
		startDateParam.setNameDisplayed("Start Date");
		startDateParam.setReport(report);
		startDateParam.setParameterDatatype(ReportParameter.DATE_DATATYPE);
		report.addReportParameter(startDateParam);

		ReportParameter endDateParam = new ReportParameter();
		endDateParam.setDescription("End Date Parameter");
		endDateParam.setName("end-date");
		endDateParam.setNameDisplayed("End Date");
		endDateParam.setReport(report);
		endDateParam.setParameterDatatype(ReportParameter.DATE_DATATYPE);
		report.addReportParameter(endDateParam);
		
		ReportParameter tempParam = new ReportParameter();
		tempParam.setDescription("Temporary Parameter");
		tempParam.setName("temp-param");
		tempParam.setNameDisplayed("Temp Param");
		tempParam.setReport(report);
		tempParam.setParameterDatatype(ReportParameter.STRING_DATATYPE);
		report.addReportParameter(tempParam);

		ReportQuery reportQuery = new ReportQuery();
		reportQuery.setName("query-one");
		reportQuery.setQuery("select p.* from person p where date_voided is null");
		reportQuery.setReport(report);
		report.addReportQuery(reportQuery);
		
		ReportQueryParameter reportQueryParameter = new ReportQueryParameter();
		reportQueryParameter.setReportQuery(reportQuery);
		reportQueryParameter.setParameterName("p.date_created");
		reportQueryParameter.setReportParameter(startDateParam);
		reportQuery.addReportQueryParameter(reportQueryParameter);
		
		reportQueryParameter = new ReportQueryParameter();
		reportQueryParameter.setReportQuery(reportQuery);
		reportQueryParameter.setParameterName("p.date_voided");
		reportQueryParameter.setReportParameter(endDateParam);
		reportQueryParameter.setRequired(ReportQueryParameter.NOT_REQUIRED_PARAMETER);
		reportQuery.addReportQueryParameter(reportQueryParameter);
		log.debug("Adding a report: " +  report.getName());
		return report;
	}
	
	private Report getReportByName(List<Report> reports) {
		if (reports == null || reports.size() == 0) {
			return null;
		}
		for (Report report : reports) {
			if (report.getName().equalsIgnoreCase("test-report")) {
				return report;
			}
		}
		return null;
	}

	private void displayReport(Report report) {
		System.out.println("ReportId: " + report.getReportId() + ", name: " + report.getName() + ", Description: " + report.getDescription() + 
				", Name Displayed: " + report.getNameDisplayed() +
				", Template Name: " + report.getTemplateName() +
				", Data Generator: " + report.getDataGenerator());
		System.out.println("Report Parameters:");
		for (ReportParameter param : report.getReportParameters()) {
			System.out.println("\tReportParamId: " + param.getReportParameterId() + ", name: " + param.getName() + ", Description: " + param.getDescription() + ", Name Displayed: " + param.getNameDisplayed());
		}
		System.out.println("Report Queries:");
		for (ReportQuery query : report.getReportQueries()) {
			System.out.println("\tReportQueryId: " + query.getReportQueryId() + ", name: " + query.getName() + ", Query: " + query.getQuery());
			System.out.println("\tReport Query Parameters:");
			for (ReportQueryParameter queryParam : query.getReportQueryParameters()) {
				System.out.println("\tName: " + queryParam.getParameterName() + ", Required: '" + queryParam.getRequired() + "', Parameter: " + queryParam.getReportParameter());
			}
		}
		System.out.println("============================================================================");
	}
}
