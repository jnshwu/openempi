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

import java.util.List;

import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.UpdateNotificationRegistrationEntry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class IdentifierUpdateNotificationDeleteTest extends BaseServiceTestCase
{
	private List<Person> addedPersons = new java.util.ArrayList<Person>();
	
	static {
		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts-identifier-update-notification.properties");
		System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME, "mpi-config-identifier-update-notification.xml");
	}
	
	public void testIdUpdateNfnAddPerson() {
		PersonManagerService personService = Context.getPersonManagerService();
		List<UpdateNotificationRegistrationEntry> updateNotificationRegistrationEntries = Context
				.getConfiguration().getAdminConfiguration()
				.getUpdateNotificationRegistrationEntries();

		Person person1 = buildTestPersonParams("John", "Smith", "987-65-4321",
				"2.16.840.1.113883.4.1");

		try {
			personService.addPerson(person1);
			log.info("Added person with id: " + person1.getPersonId());
			addedPersons.add(person1);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}

		Person person2 = buildTestPersonParams("John", "Smith", "876-54-3219",
				"2.16.840.1.113883.4.1");
		try {
			personService.addPerson(person2);
			log.info("Added person with id: " + person2.getPersonId());
			addedPersons.add(person2);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}

		try {
			PersonIdentifier person2Identifier = person2.getPersonIdentifiers().iterator().next();
			assertNotNull(person2Identifier);
			personService.deletePerson(person2Identifier);
			log.info("Delete Person with id: " + person2.getPersonId());
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onTearDown() throws Exception {
		PersonManagerService personManager = Context.getPersonManagerService();
		for (Person person : addedPersons) {
			log.info("Deleting test person: " + person.getPersonId());
			personManager.deletePersonById(person);
		}
		super.onTearDown();
	}

	protected static Person buildTestPersonParams(String givenName, String familyName, String Id, String nameSpaceId) {
		
		Person person = new Person();
		person.setGivenName(givenName);
		person.setFamilyName(familyName);
		
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier(Id);
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier(nameSpaceId);
		pi.setIdentifierDomain(id);
		person.addPersonIdentifier(pi);	
		return person;
	}
}