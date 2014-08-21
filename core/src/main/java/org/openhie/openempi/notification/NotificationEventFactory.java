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

import org.openhie.openempi.model.Person;

public class NotificationEventFactory
{
	public static NotificationEvent createNotificationEvent(EventType eventType, Object eventData) {
		if (eventType == null) {
			return null;
		}
		
		NotificationEvent event = null;
		switch (eventType) {
			case ADD_EVENT_TYPE:
				if (eventData == null || !(eventData instanceof Person)) {
					break;
				}
				event = new AddNotificationEvent(EventType.ADD_EVENT_TYPE, (Person) eventData);
				break;
				
			case DELETE_EVENT_TYPE:
				if (eventData == null || !(eventData instanceof Person)) {
					break;
				}
				event = new DeleteNotificationEvent(EventType.DELETE_EVENT_TYPE, (Person) eventData);
				break;
			
			case IMPORT_EVENT_TYPE:
				if (eventData == null || !(eventData instanceof Person)) {
					break;
				}
				event = new ImportNotificationEvent(EventType.IMPORT_EVENT_TYPE, (Person) eventData);
				break;

				
			case UPDATE_EVENT_TYPE:
				if (eventData == null || !(eventData instanceof Person)) {
					break;
				}
				event = new ImportNotificationEvent(EventType.UPDATE_EVENT_TYPE, (Person) eventData);
				break;
				
			default:
				throw new RuntimeException("Unable to handle creation of a notification event of this type: " + eventType);
		}
		return event;
	}
	
	public static NotificationEvent createNotificationEvent(EventType eventType, Object[] eventData) {
		if (eventType == null) {
			return null;
		}
		
		NotificationEvent event = null;
		switch (eventType) {
		
			case MERGE_EVENT_TYPE:
				if (eventData == null || eventData.length != 2 || eventData[0] == null || 
						eventData[1]== null || !(eventData[0] instanceof Person) ||
						!(eventData[1] instanceof Person)) {
					break;
				}
				event = new MergeNotificationEvent(EventType.IMPORT_EVENT_TYPE, new Person[] { (Person) eventData[0], (Person) eventData[1]});
				break;
				
			default:
				throw new RuntimeException("Unable to handle creation of a notification event of this type: " + eventType);
		}
		return event;
	}
}
