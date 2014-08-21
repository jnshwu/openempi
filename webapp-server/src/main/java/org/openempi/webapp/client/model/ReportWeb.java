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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ReportWeb extends BaseModelData
{ 
	@SuppressWarnings("unused")
    private ReportParameterWeb unusedReportParameterWeb;
	
	@SuppressWarnings("unused")
    private ReportQueryWeb unusedReportQueryWeb;
	
	public ReportWeb() {
	}

	public java.lang.Integer getReportId() {
		return get("reportId");
	}

	public void setReportId(java.lang.Integer reportId) {
		set("reportId", reportId);
	}
	
	public java.lang.String getName() {
		return get("name");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getNameDisplayed() {
		return get("nameDisplayed");
	}

	public void setNameDisplayed(java.lang.String nameDisplayed) {
		set("nameDisplayed", nameDisplayed);
	}
	
	public java.lang.String getDescription() {
		return get("description");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}
	
	public java.lang.String getTemplateName() {
		return get("templateName");
	}

	public void setTemplateName(java.lang.String templateName) {
		set("templateName", templateName);
	}

	public java.lang.String getDataGenerator() {
		return get("dataGenerator");
	}

	public void setDataGenerator(java.lang.String dataGenerator) {
		set("dataGenerator", dataGenerator);
	}
	
	public java.lang.String getReportUrl() {
		return get("reportUrl");
	}

	public void setReportUrl(java.lang.String reportUrl) {
		set("reportUrl", reportUrl);
	}
	
	public java.util.Set<ReportParameterWeb> getReportParameters() {
		return get("reportParameters");
	}

	public void setReportParameters(java.util.Set<ReportParameterWeb> reportParameters) {
		set("reportParameters", reportParameters);
	}
	
	public java.util.Set<ReportQueryWeb> getReportQueries() {
		return get("reportQueries");
	}

	public void setReportQueries(java.util.Set<ReportQueryWeb> reportQueries) {
		set("reportQueries", reportQueries);
	}
}
