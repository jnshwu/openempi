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

public class ReportRequestEntryWeb extends BaseModelData
{
	
//	@SuppressWarnings("unused")
//    private UserWeb unusedUserWeb;
	
	public ReportRequestEntryWeb() {
		
	}

	public java.lang.Integer getReportRequestId() {
		return get("reportRequestId");
	}

	public void setReportRequestId(java.lang.Integer reportRequestId) {
		set("reportRequestId", reportRequestId);
	}
	/*	public ReportWeb getReport() {
		return get("report");
		}
		
		public void setReport(ReportWeb report) {
			set("report", report);
		}	
		
		public UserWeb getUserRequested() {
			return get("userRequested");
		}
		
		public void setUserRequested(UserWeb userRequested) {
			set("userRequested", userRequested);
		}	
	*/		
	public String getUserName() {
		return get("userName");
	}

	public void setUserName(String userName) {
		set("userName", userName);
	}
	
	public String getReportName() {
		return get("reportName");
	}

	public void setReportName(String reportName) {
		set("reportName", reportName);
	}
	
	public String getReportDescription() {
		return get("reportDescription");
	}

	public void setReportDescription(String reportDescription) {
		set("reportDescription", reportDescription);
	}
	

	
	public java.util.Date getDateRequested() {
		return get("dateRequested");
	}

	public void setDateRequested(java.util.Date dateRequested) {
		set("dateRequested", dateRequested);
	}
	
	public java.util.Date getDateCompleted() {
		return get("dateCompleted");
	}

	public void setDateCompleted(java.util.Date dateCompleted) {
		set("dateCompleted", dateCompleted);
	}
	
	public String getCompleted() {
		return get("completed");
	}

	public void setCompleted(String completed) {
		set("completed", completed);
	}
	
	public String getReportHandle() {
		return get("reportHandle");
	}

	public void setReportHandle(String reportHandle) {
		set("reportHandle", reportHandle);
	}

}
