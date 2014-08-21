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

import com.extjs.gxt.ui.client.data.BaseModel;

public class MessageLogEntryWeb extends BaseModel implements Serializable
{
	@SuppressWarnings("unused")
    private MessageTypeWeb unusedMessageTypeWeb;
	
	public MessageLogEntryWeb() {
	}

	public java.lang.Integer getMessageLogId() {
		return get("messageLogId");
	}
	
	public void setMessageLogId(java.lang.Integer messageLogId) {
		set("messageLogId", messageLogId);
	}

	public java.util.Date getDateReceived() {
		return get("dateReceived");
	}
	
	public void setDateReceived(java.util.Date dateReceived) {
		set("dateReceived", dateReceived);
	}
	
	public String getOutgoingMessage() {
		return get("outgoingMessage");
	}

	public void setOutgoingMessage(String outgoingMessage) {
		set("outgoingMessage", outgoingMessage);
	}	
			
	public String getIncomingMessage() {
		return get("incomingMessage");
	}

	public void setIncomingMessage(String incomingMessage) {
		set("incomingMessage", incomingMessage);
	}	

	public MessageTypeWeb getIncomingMessageType() {
		return get("incomingMessageType");
	}

	public void setIncomingMessageType(MessageTypeWeb incomingMessageType) {
		set("incomingMessageType", incomingMessageType);
	}	
	
	public MessageTypeWeb getOutgoingMessageType() {
		return get("outgoingMessageType");
	}

	public void setOutgoingMessageType(MessageTypeWeb outgoingMessageType) {
		set("outgoingMessageType", outgoingMessageType);
	}		
}
