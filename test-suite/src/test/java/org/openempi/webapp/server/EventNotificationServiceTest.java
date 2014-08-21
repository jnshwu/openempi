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
package org.openempi.webapp.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.openempi.webapp.client.EventNotificationService;
import org.openhie.openempi.notification.EventType;

public class EventNotificationServiceTest extends BaseServiceTestCase
{
	public void testNotificationService() {
		EventNotificationService notificationService = new EventNotificationServiceImpl();
		String message = notificationService.registerListener(EventType.ADD_EVENT_TYPE.getEventTypeName());
		log.debug("Got a message during registration of " + message);
		
		try {
			Thread.sleep(5000);
		} catch (Exception e) {			
		}
	}

	private void waitForInput() {
		String string = "";
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		try {
		    string = reader.readLine(); 
		} catch(Exception e) {
		}
		log.debug("You typed: " + string);
	}
}
