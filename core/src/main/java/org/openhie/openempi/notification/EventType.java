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
package org.openhie.openempi.notification;

public enum EventType
{
	ADD_EVENT_TYPE("addNotificationEventType"),
	DELETE_EVENT_TYPE("deleteNotificationEventType"),
	IMPORT_EVENT_TYPE("importNotificationEventType"),
	MERGE_EVENT_TYPE("mergeNotificationEventType"),
	UPDATE_EVENT_TYPE("updateNotificationEventType");
	
	private String eventTypeName;
	
	EventType(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}
}
