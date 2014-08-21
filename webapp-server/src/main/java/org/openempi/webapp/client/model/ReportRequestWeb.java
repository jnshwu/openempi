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

import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ReportRequestWeb extends BaseModelData
{ 
	@SuppressWarnings("unused")
    private ReportRequestParameterWeb unusedReportRequestParameterWeb;
	
	public ReportRequestWeb() {
	}

	public java.lang.Integer getReportId() {
		return get("reportId");
	}

	public void setReportId(java.lang.Integer reportId) {
		set("reportId", reportId);
	}

	public List<ReportRequestParameterWeb> getReportParameters() {
		return get("reportParameters");
	}

	public void setReportParameters(List<ReportRequestParameterWeb> reportParameters) {
		set("reportParameters", reportParameters);
	}	

	public Date getRequestDate() {
		return get("requestDate");
	}

	public void setRequestDate(Date requestDate) {
		set("requestDate", requestDate);
	}	
}
