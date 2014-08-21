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

public class ReportQueryParameterWeb extends BaseModelData
{ 
	public ReportQueryParameterWeb() {
	}

	public java.lang.Integer getReportQueryParameterId() {
		return get("reportQueryParameterId");
	}

	public void setReportQueryParameterId(java.lang.Integer reportQueryParameterId) {
		set("reportQueryParameterId", reportQueryParameterId);
	}
	
	public java.lang.String getParameterName() {
		return get("parameterName");
	}

	public void setParameterName(java.lang.String name) {
		set("parameterName", name);
	}
	
	public java.lang.String getRequired() {
		return get("required");
	}

	public void setRequired(java.lang.String required) {
		set("required", required);
	}
	
	public ReportParameterWeb getReportParameter() {
		return get("reportParameter");
	}

	public void setReportParameter(ReportParameterWeb reportParameter) {
		set("reportParameter", reportParameter);
	}
	
	public java.lang.String getSubstitutionKey() {
		return get("substitutionKey");
	}

	public void setSubstitutionKey(java.lang.String key) {
		set("substitutionKey", key);
	}
}
