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
package org.openhie.openempi.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportParameter;
import org.openhie.openempi.model.ReportRequest;
import org.openhie.openempi.model.ReportRequestEntry;
import org.openhie.openempi.model.ReportRequestParameter;
import org.openhie.openempi.service.BaseServiceTestCase;

public class ReportServiceTest extends BaseServiceTestCase
{
	public void testReportGeneration() {
		try {
			ReportService reportService = Context.getReportService();
			Report report = reportService.getReportByName("event-activity");
			
			List<Report> reports = null;
			if( report == null ) {
				reports = reportService.getReports();
				if( reports == null || reports.size() == 0) {
					
					log.debug("No any report design in the database ");	
					return;
				} else {
					report = reports.get(0);
				}
			}
			
			ReportRequest request = new ReportRequest(report.getReportId());
			ReportParameter param = report.getReportParameterByName("start-date");
			log.debug("Is required parameter returns: " + report.isRequiredParameter("start-date"));
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2012);
			cal.set(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ReportRequestParameter reqParam = new ReportRequestParameter(param.getName(), sdf.format(cal.getTime()));
			request.addReportRequestParameter(reqParam);
			ReportRequestEntry entry = reportService.generateReport(request);
			log.debug("Obtained report handle: " + entry);

		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testReportRequests() {
		try {
			ReportService reportService = Context.getReportService();
			List<ReportRequestEntry> reportRequests = reportService.getReportRequests();
			for (ReportRequestEntry entry : reportRequests) {
				log.debug("Obtained report request: " + entry);
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
}
