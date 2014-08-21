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
package org.openhie.openempi.dao;

import java.util.List;

import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportParameter;
import org.openhie.openempi.model.ReportQuery;
import org.openhie.openempi.model.ReportQueryParameter;

public class ReportDaoTest extends BaseDaoTestCase
{
	private ReportDao reportDao;

	public void testAddReport() {
		try {
			Report report = new Report();
			report.setDescription("Sample Test Report");
			report.setName("test-report");
			report.setNameDisplayed("Test Report");
			report.setTemplateName("test-report.xml");
			report.setDataGenerator("testDataGenerator");
			
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
	
			ReportQuery reportQuery = new ReportQuery();
			reportQuery.setName("query-one");
			reportQuery.setQuery("select p.* from person p where date_voided is null");
			reportQuery.setReport(report);
			report.addReportQuery(reportQuery);
			
			ReportQueryParameter reportQueryParameter = new ReportQueryParameter();
			reportQueryParameter.setReportQuery(reportQuery);
			reportQueryParameter.setParameterName("p.date_created");
			reportQueryParameter.setRequired(ReportQueryParameter.REQUIRED_PARAMETER);
			reportQueryParameter.setReportParameter(startDateParam);
			reportQuery.addReportQueryParameter(reportQueryParameter);
			reportDao.addReport(report);
			
		} catch (Throwable t) {
			t.printStackTrace();
		}

		try {
			List<Report> reports = reportDao.getReports();
			for (Report report : reports) {
				System.out.println("ReportId: " + report.getReportId() + ", name: " + report.getName() + ", Description: " + report.getDescription() + ", Name Displayed: " + report.getNameDisplayed());
				System.out.println("Report Parameters:");
				for (ReportParameter param : report.getReportParameters()) {
					System.out.println("\tReportParamId: " + param.getReportParameterId() + ", name: " + param.getName() + ", Description: " + param.getDescription() + ", Name Displayed: " + param.getNameDisplayed());
				}
				System.out.println("Report Queries:");
				for (ReportQuery query : report.getReportQueries()) {
					System.out.println("\tReportQueryId: " + query.getReportQueryId() + ", name: " + query.getName() + ", Query: " + query.getQuery());
					System.out.println("\tReport Query Parameters:");
					for (ReportQueryParameter queryParam : query.getReportQueryParameters()) {
						System.out.println("\tName: " + queryParam.getParameterName() + ", Parameter: " + queryParam.getReportParameter());
					}
				}
				System.out.println("============================================================================");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	public ReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}
}
