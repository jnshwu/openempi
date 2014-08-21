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

public class MessageTypeWeb extends BaseModelData implements Serializable
{ 	
	public MessageTypeWeb() {
	}

	public MessageTypeWeb(Integer messageTypeCd, String messageTypeName, String messageTypeDescription, String messageTypeCode) {
		set("messageTypeCd", messageTypeCd);
		set("messageTypeName", messageTypeName);
		set("messageTypeDescription", messageTypeDescription);
		set("messageTypeCode", messageTypeCode);
	}

	public Integer getMessageTypeCd() {
		return get("messageTypeCd");
	}

	public void setMessageTypeCd(Integer messageTypeCd) {
		set("messageTypeCd", messageTypeCd);
	}
	
	public java.lang.String getMessageTypeName() {
		return get("messageTypeName");
	}

	public void setMessageTypeName(java.lang.String messageTypeName) {
		set("messageTypeName", messageTypeName);
	}

	public java.lang.String getMessageTypeDescription() {
		return get("messageTypeDescription");
	}

	public void setMessageTypeDescription(java.lang.String messageTypeDescription) {
		set("messageTypeDescription", messageTypeDescription);
	}
	
	public java.lang.String getMessageTypeCode() {
		return get("messageTypeCode");
	}

	public void setMessageTypeCode(java.lang.String messageTypeCode) {
		set("messageTypeCode", messageTypeCode);
	}
}
