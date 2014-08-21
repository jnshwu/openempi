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
package org.openhie.openempi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierUpdateEvent;

public class IdentifierUpdateNotificationTest extends BaseServiceTestCase {
	private PersonManagerService personManagerService;
	private PersonQueryService personQueryService;

	private List<IdentifierUpdateEvent> identifierUpdateEvents = new ArrayList<IdentifierUpdateEvent>();

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		personManagerService = Context.getPersonManagerService();
		personQueryService = Context.getPersonQueryService();
	}

	public void testGetNotificationCount() {

		try {
			long count = personQueryService.getNotificationCount(Context
					.getUserContext().getUser());

			System.out.println("testGetNotificationCount:");
			System.out.println(count);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testRetrieveNotifications() {
		try {

			identifierUpdateEvents = personQueryService.retrieveNotifications(
					false, Context.getUserContext().getUser());

			System.out.println("testRetrieveNotifications:");
			for (IdentifierUpdateEvent identifierUpdateEvent : identifierUpdateEvents) {
				System.out.println(identifierUpdateEvent.toString());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testRetrieveNotificationsByDate() {

		Date dateCreated = new Date();

		try {
			identifierUpdateEvents = personQueryService
					.retrieveNotificationsByDate(dateCreated, false, Context
							.getUserContext().getUser());

			System.out.println("testRetrieveNotificationsByDate:");
			for (IdentifierUpdateEvent identifierUpdateEvent : identifierUpdateEvents) {
				System.out.println(identifierUpdateEvent.toString());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testRemoveNotifications() {

		identifierUpdateEvents = personQueryService.retrieveNotifications(0, 0,
				false, Context.getUserContext().getUser());
		assertNotNull(identifierUpdateEvents);

		try {

			int count = personManagerService
					.removeNotifications(identifierUpdateEvents);

			log.debug("testRemoveNotifications() - count: " + count);
			System.out.println("testRemoveNotifications():");
			System.out.println(count);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Exception: " + e, e);
		}
	}

}