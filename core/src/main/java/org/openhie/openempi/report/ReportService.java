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

import java.io.File;
import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportRequest;
import org.openhie.openempi.model.ReportRequestEntry;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ReportService
{
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Report addReport(Report report) throws ApplicationException;
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void deleteReport(Report report) throws ApplicationException;

	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public ReportRequestEntry generateReport(ReportRequest reportRequest) throws ApplicationException;

	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public List<Report> getReports();
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public Report getReportByName(String reportName);
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public Report getReportById(Integer reportId);
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public File getReportDataDirectory();
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Report updateReport(Report report) throws ApplicationException;
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public List<ReportRequestEntry> getReportRequests();
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED) 
	public void reassignReportRequestsToReport(Integer oldReportId, Integer newReportId);
}
