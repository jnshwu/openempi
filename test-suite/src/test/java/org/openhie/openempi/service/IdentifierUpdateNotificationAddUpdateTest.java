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
import org.openhie.openempi.configuration.GlobalIdentifier;
import org.openhie.openempi.configuration.UpdateNotificationRegistrationEntry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class IdentifierUpdateNotificationAddUpdateTest extends BaseServiceTestCase
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

		GlobalIdentifier globalIdentifier = Context.getConfiguration().getGlobalIdentifier();
		
		// add person1
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

		PersonIdentifier person1GlobalIdentifier = null;
		for (PersonIdentifier identifier : person1.getPersonIdentifiers()) {
			IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
			if (identifierDomain != null && identifierDomain.equals(globalIdentifier.getIdentifierDomain())) {
				person1GlobalIdentifier =  identifier;
			}			
		}
		
		// add person2
		Person person2 = buildTestPersonParams("Jon", "Smith", "876-54-3219",
				"2.16.840.1.113883.4.1");
		try {
			personService.addPerson(person2);
			log.info("Added person with id: " + person2.getPersonId());
			addedPersons.add(person2);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}

		PersonIdentifier person2GlobalIdentifier = null;
		for (PersonIdentifier identifier : person2.getPersonIdentifiers()) {
			IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
			if (identifierDomain != null && identifierDomain.equals(globalIdentifier.getIdentifierDomain())) {
				person2GlobalIdentifier =  identifier;
			}			
		}

		assertNotNull("person1 global identifier", person1GlobalIdentifier);   
		assertNotNull("person2 global identifier", person2GlobalIdentifier);   
		assertFalse("Different global identifiers " + person1GlobalIdentifier.getIdentifier() + "; " + person2GlobalIdentifier.getIdentifier(), 
				person1GlobalIdentifier.getIdentifier().equals(person2GlobalIdentifier.getIdentifier()));
		
		// updating person2 result in link with person1 		
		try {
			person2.setGivenName("John");
			personService.updatePersonById(person2);
			log.info("Updated Person with id: " + person2.getPersonId());
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}

		person2GlobalIdentifier = null;
		for (PersonIdentifier identifier : person2.getPersonIdentifiers()) {
			IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
			if (identifierDomain != null && identifierDomain.equals(globalIdentifier.getIdentifierDomain())) {
				person2GlobalIdentifier =  identifier;
			}			
		}
		assertNotNull("person2 global identifier", person2GlobalIdentifier);   
		assertTrue("Same global identifiers " + person1GlobalIdentifier.getIdentifier() + "; " + person2GlobalIdentifier.getIdentifier(), 
				person1GlobalIdentifier.getIdentifier().equals(person2GlobalIdentifier.getIdentifier()));
		
		// updating person2 result in unlink with person1 	
		try {
			person2.setGivenName("Jon");
			personService.updatePersonById(person2);
			log.info("Updated Person with id: " + person2.getPersonId());
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
		
		person2GlobalIdentifier = null;
		for (PersonIdentifier identifier : person2.getPersonIdentifiers()) {
			IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
			if (identifierDomain != null && identifierDomain.equals(globalIdentifier.getIdentifierDomain())) {
				person2GlobalIdentifier =  identifier;
			}			
		}
		assertNotNull("person2 global identifier", person2GlobalIdentifier);   		
		assertFalse("Different global identifiers " + person1GlobalIdentifier.getIdentifier() + "; " + person2GlobalIdentifier.getIdentifier(), 
				person1GlobalIdentifier.getIdentifier().equals(person2GlobalIdentifier.getIdentifier()));
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

	
}
