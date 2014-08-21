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
package org.openhie.openempi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Report Request entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
public class ReportRequest extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1943429923033311936L;
	
	private Integer reportId;
	private List<ReportRequestParameter> reportParameters = new java.util.ArrayList<ReportRequestParameter>();;
	private Date requestDate;

	public ReportRequest() {
	}
	
	public ReportRequest(Integer reportId) {
//		this.reportParameters = new java.util.ArrayList<ReportRequestParameter>();
		this.reportId = reportId;
	}
	
	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	
	public void addReportRequestParameter(ReportRequestParameter parameter) {
		reportParameters.add(parameter);
	}

	public List<ReportRequestParameter> getReportParameters() {
		return reportParameters;
	}

	public void setReportParameters(List<ReportRequestParameter> reportParameters) {
		this.reportParameters = reportParameters;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportRequest other = (ReportRequest) obj;
		if (reportId == null) {
			if (other.reportId != null)
				return false;
		} else if (!reportId.equals(other.reportId))
			return false;
		if (reportParameters == null) {
			if (other.reportParameters != null)
				return false;
		} else if (!reportParameters.equals(other.reportParameters))
			return false;
		if (requestDate == null) {
			if (other.requestDate != null)
				return false;
		} else if (!requestDate.equals(other.requestDate))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reportId == null) ? 0 : reportId.hashCode());
		result = prime * result + ((reportParameters == null) ? 0 : reportParameters.hashCode());
		result = prime * result + ((requestDate == null) ? 0 : requestDate.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ReportRequest [reportId=" + reportId + ", reportParameters=" + reportParameters + ", requestDate="
				+ requestDate + "]";
	}
}
