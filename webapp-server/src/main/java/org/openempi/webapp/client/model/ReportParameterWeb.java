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

public class ReportParameterWeb extends BaseModelData
{ 
	public ReportParameterWeb() {
	}

	public java.lang.Integer getReportParameterId() {
		return get("reportParameterId");
	}

	public void setReportParameterId(java.lang.Integer reportParameterId) {
		set("reportParameterId", reportParameterId);
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

	public java.lang.Integer getParameterDatatype() {
		return get("parameterDatatype");
	}

	public void setParameterDatatype(java.lang.Integer parameterDatatype) {
		set("parameterDatatype", parameterDatatype);
	}
}
