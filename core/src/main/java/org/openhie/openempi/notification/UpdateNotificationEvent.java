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

import org.openhie.openempi.model.Person;

public class UpdateNotificationEvent extends NotificationEvent
{
	private static final long serialVersionUID = 5892803507302813189L;
	
	public UpdateNotificationEvent(EventType eventType, Serializable eventData) {
		super(eventType, eventData);
		if (eventData != null && !(eventData instanceof Person)) {
			throw new RuntimeException("Invalid event data provided to notification event of type " + eventType);
		}
	}
}
