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
package org.openempi.webapp.client;

import java.util.List;

import org.openempi.webapp.client.model.ReportWeb;
import org.openempi.webapp.client.model.ReportRequestWeb;
import org.openempi.webapp.client.model.ReportRequestEntryWeb;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportDataServiceAsync
{	
	public void getReports( AsyncCallback<List<ReportWeb>> callback);
	
	public void addReport(ReportWeb report, AsyncCallback<ReportWeb> callback);

	public void deleteReport(ReportWeb report, AsyncCallback<String> callback);
	
	public void updateReport(ReportWeb report, AsyncCallback<ReportWeb> callback);
	
	public void generateReport(ReportRequestWeb reportRequest, AsyncCallback<ReportRequestEntryWeb> callback);
	
	public void getReportRequests( AsyncCallback<List<ReportRequestEntryWeb>> callback);
}
