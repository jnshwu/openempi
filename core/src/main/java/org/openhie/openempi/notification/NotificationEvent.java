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

import java.io.Serializable;

public class NotificationEvent implements Serializable
{
	private static final long serialVersionUID = 5892803507302813189L;
	
	private EventType eventType;
	private Serializable eventData;
	
	public NotificationEvent(EventType eventType, Serializable eventData) {
		this.eventType = eventType;
		this.eventData = eventData;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	public Serializable getEventData() {
		return eventData;
	}
	
	public void setEventData(Serializable eventData) {
		this.eventData = eventData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventData == null) ? 0 : eventData.hashCode());
		result = prime * result + ((eventType.getEventTypeName() == null) ? 0 : eventType.getEventTypeName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationEvent other = (NotificationEvent) obj;
		if (eventData == null) {
			if (other.eventData != null)
				return false;
		} else if (!eventData.equals(other.eventData))
			return false;
		if (eventType == null) {
			if (other.eventType != null)
				return false;
		} else if (!eventType.getEventTypeName().equals(other.eventType.getEventTypeName()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NotificationEvent [eventTypeName=" + eventType.getEventTypeName() + ", eventData=" + eventData + "]";
	}
	
}
