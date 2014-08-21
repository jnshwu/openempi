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
package org.openempi.webapp.client.model;

import java.util.Set;

import org.openhie.openempi.model.ReportQueryParameter;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ReportQueryWeb extends BaseModelData
{ 
	@SuppressWarnings("unused")
    private ReportQueryParameterWeb unusedReportQueryParameterWeb;
	
	public ReportQueryWeb() {
	}

	public java.lang.Integer getReportQueryId() {
		return get("reportQueryId");
	}

	public void setReportQueryId(java.lang.Integer reportQueryId) {
		set("reportQueryId", reportQueryId);
	}
	
	public java.lang.String getName() {
		return get("name");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getQuery() {
		return get("query");
	}

	public void setQuery(java.lang.String query) {
		set("query", query);
	}
	
	public java.util.Set<ReportQueryParameterWeb> getReportQueryParameters() {
		return get("reportQueryParameters");
	}

	public void setReportQueryParameters(java.util.Set<ReportQueryParameterWeb> reportQueryParameters) {
		set("reportQueryParameters", reportQueryParameters);
	}
}
