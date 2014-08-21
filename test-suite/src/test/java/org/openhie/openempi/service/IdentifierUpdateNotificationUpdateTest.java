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

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class IdentifierUpdateNotificationUpdateTest extends BaseServiceTestCase
{
	private static Integer personId;

	public void testIdUpdateNfnUpdatePersonById() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
		Person person = new Person();
		person.setGivenName("ALAN");
		person.setFamilyName("ALPHA");
		
		try {
			List<Person> personsFound = queryService.findPersonsByAttributes(person);
			assertNotNull(personsFound);
			assertTrue(personsFound.size() > 0);
			Person personFound = personsFound.get(0);
			personId = personFound.getPersonId();
			System.out.println("Before update operation: ");
			printPerson(personFound);
			String phoneNumber = personFound.getPhoneNumber();
			personFound.setPhoneNumber("555-5656");
			managerService.updatePersonById(personFound);
			
/**			
			personFound = findPerson(queryService, person, personId);
			System.out.println("After removing identifier operation: ");
			printPerson(personFound);

/**
			personsFound = queryService.findPersonsByAttributes(person);
			personFound = personsFound.get(0);
			System.out.println("After identifier update operation: ");
			printPerson(personFound);*/
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	public void testIdUpdateNfnUpdatePersonByIdAddIdentifier() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
		Person person = new Person();
		person.setGivenName("ALAN");
		person.setFamilyName("ALPHA");
		try {
			Person personFound = findPerson(queryService, person, personId);
			System.out.println("After update operation: ");
			printPerson(personFound);
			
			// Let's now modify the identifiers by adding one
			PersonIdentifier foundIdentifier = null;
			for (PersonIdentifier id : personFound.getPersonIdentifiers()) {
				if (id.getIdentifierDomain().getNamespaceIdentifier().equalsIgnoreCase("HIMSS2005")) {
					foundIdentifier = id;
				}
			}
			PersonIdentifier newIdentifier = new PersonIdentifier();
			newIdentifier.setIdentifier("PHI10501");
			newIdentifier.setIdentifierDomain(foundIdentifier.getIdentifierDomain());
			personFound.addPersonIdentifier(newIdentifier);
			managerService.updatePersonById(personFound);
			
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	public void testIdUpdateNfnUpdatePersonByIdRemoveIdentifier() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
		Person person = new Person();
		person.setGivenName("ALAN");
		person.setFamilyName("ALPHA");
		try {
			Person personFound = findPerson(queryService, person, personId);
			System.out.println("After update operation: ");
			printPerson(personFound);

			// Let's now delete the identifier we added
			PersonIdentifier foundIdentifier = null;
			for (PersonIdentifier id : personFound.getPersonIdentifiers()) {
				if (id.getIdentifier().equalsIgnoreCase("PHI10501")) {
					foundIdentifier = id;
				}
			}
			java.util.Set<PersonIdentifier> ids = personFound.getPersonIdentifiers();
			java.util.Set<PersonIdentifier> newIds = new java.util.HashSet<PersonIdentifier>();
			for (PersonIdentifier id : ids) {
				if (!id.getPersonIdentifierId().equals(foundIdentifier.getPersonIdentifierId())) {
					newIds.add(id);
				} else {
					System.out.println("Will delete this one: " + id);
				}
			}
			personFound.setPersonIdentifiers(newIds);
			managerService.updatePersonById(personFound);
			
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	private Person findPerson(PersonQueryService queryService, Person personQuery, Integer personId) {
		List<Person> personsFound = queryService.findPersonsByAttributes(personQuery);
		for (Person p : personsFound) {
			if (p.getPersonId().equals(personId)) {
				return p;
			}
		}
		return null;
	}
	
	private void printPerson(Person personFound) {
		System.out.println("Found the person: " + personFound.getGivenName() + "," + personFound.getFamilyName() + "," + personFound.getPhoneNumber() + " with IDs:");
		for (PersonIdentifier id : personFound.getPersonIdentifiers()) {
			System.out.println("\t<" + id.getIdentifier() + "," + id.getIdentifierDomain().getNamespaceIdentifier() + ">");
		}
	}
}
