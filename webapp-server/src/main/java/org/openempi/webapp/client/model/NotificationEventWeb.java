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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class NotificationEventWeb extends BaseModelData
{
	public final static String ADD_EVENT_TYPE = "addNotificationEventType";
	public final static String DELETE_EVENT_TYPE = "deleteNotificationEventType";
	public final static String IMPORT_EVENT_TYPE = "importNotificationEventType";
	public final static String MERGE_EVENT_TYPE = "mergeNotificationEventType";
	public final static String UPDATE_EVENT_TYPE = "updateNotificationEventType";
	
	private final static String EVENT_TYPE_NAME = "eventTypeName";
	private final static String MESSAGE_ID = "messageId";
	private final static String PERSON_IDENTIFIER = "personIdentifier";
	private final static String TIMESTAMP = "timestamp";
	
	private static java.util.HashMap<String,String> eventTypeNameDisplayMap = new java.util.HashMap<String, String>();
	
	static {
		eventTypeNameDisplayMap.put(ADD_EVENT_TYPE, "Add Event");
		eventTypeNameDisplayMap.put(DELETE_EVENT_TYPE, "Delete Event");
		eventTypeNameDisplayMap.put(IMPORT_EVENT_TYPE, "Import Event");
		eventTypeNameDisplayMap.put(MERGE_EVENT_TYPE, "Merge Event");
		eventTypeNameDisplayMap.put(UPDATE_EVENT_TYPE, "Update Event");
	}

	public NotificationEventWeb() {
	}
	
	public String getEventTypeName() {
		return getEventTypeNameForDisplay(EVENT_TYPE_NAME);
	}

	public void setEventTypeName(String eventTypeName) {
		set(EVENT_TYPE_NAME, eventTypeName);
	}	
	
	public String getMessageId() {
		return get(MESSAGE_ID);
	}
	
	public void setMessageId(String messageId) {
		set(MESSAGE_ID, messageId);
	}
	
	public String getPersonIdentifier() {
		return get(PERSON_IDENTIFIER);
	}
	
	public void setPersonIdentifier(String personIdentifier) {
		set(PERSON_IDENTIFIER, personIdentifier);
	}

	public Date getTimestamp() {
		return get(TIMESTAMP);
	}
	
	public void setTimestamp(Date timestamp) {
		set(TIMESTAMP, timestamp);
	}
	
	private String getEventTypeNameForDisplay(String eventTypeName) {
		return eventTypeNameDisplayMap.get(eventTypeName);
	}
}
