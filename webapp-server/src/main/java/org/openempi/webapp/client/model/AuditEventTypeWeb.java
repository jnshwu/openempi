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

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class AuditEventTypeWeb extends BaseModelData implements Serializable
{ 	
	public AuditEventTypeWeb() {
	}

	public AuditEventTypeWeb(Integer auditEventTypeCd, String auditEventTypeName, String auditEventTypeDescription, String auditEventTypeCode) {
		set("auditEventTypeCd", auditEventTypeCd);
		set("auditEventTypeName", auditEventTypeName);
		set("auditEventTypeDescription", auditEventTypeDescription);
		set("auditEventTypeCode", auditEventTypeCode);
	}

	public Integer getAuditEventTypeCd() {
		return get("auditEventTypeCd");
	}

	public void setAuditEventTypeCd(Integer auditEventTypeCd) {
		set("auditEventTypeCd", auditEventTypeCd);
	}
	
	public java.lang.String getAuditEventTypeName() {
		return get("auditEventTypeName");
	}

	public void setAuditEventTypeName(java.lang.String auditEventTypeName) {
		set("auditEventTypeName", auditEventTypeName);
	}

	public java.lang.String getAuditEventTypeDescription() {
		return get("auditEventTypeDescription");
	}

	public void setAuditEventTypeDescription(java.lang.String auditEventTypeDescription) {
		set("auditEventTypeDescription", auditEventTypeDescription);
	}
	
	public java.lang.String getAuditEventTypeCode() {
		return get("auditEventTypeCode");
	}

	public void setAuditEventTypeCode(java.lang.String auditEventTypeCode) {
		set("auditEventTypeCode", auditEventTypeCode);
	}
}
