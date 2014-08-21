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
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PersonUpdateTest extends BaseServiceTestCase
{
	private static Integer personId;

	public void testUpdatePersonById() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
/*		Person person = new Person();
		person.setGivenName("ALAN");
		person.setFamilyName("ALPHA");
*/
		Person person = buildTestPerson("ALAN", "ALPHATest", "testIdentifier");
		addPerson(person);	
		
		
		try {
			List<Person> personsFound = queryService.findPersonsByAttributes(person);
			assertNotNull(personsFound);
			assertTrue(personsFound.size() > 0);
			Person personFound = personsFound.get(0);
			personId = personFound.getPersonId();
			System.out.println("Before update operation: ");
			printPerson(personFound);
			personFound.setPhoneNumber("555-5656");
			managerService.updatePersonById(personFound);
			
			
/*			personFound = findPerson(queryService, person, personId);
			System.out.println("After update operation: ");
			printPerson(personFound);
*/
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	public void testUpdatePersonByIdAddIdentifier() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
		Person person = new Person();
		person.setGivenName("ALAN");
		person.setFamilyName("ALPHATest");
		try {
			Person personFound = findPerson(queryService, person, personId);
			System.out.println("After update operation: ");
			printPerson(personFound);
			
			// Let's now modify the identifiers by adding one
			PersonIdentifier foundIdentifier = null;
			for (PersonIdentifier id : personFound.getPersonIdentifiers()) {
				if (id.getIdentifierDomain().getNamespaceIdentifier().equalsIgnoreCase("SSN")) {
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
	
	public void testUpdatePersonByIdRemoveIdentifier() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
		Person person = new Person();
		person.setGivenName("ALAN");
		person.setFamilyName("ALPHATest");
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

	public void testDeletePerson() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
		Person person = new Person();
		person.setGivenName("ALAN");
		person.setFamilyName("ALPHATest");
		try {
			Person personFound = findPerson(queryService, person, personId);
			managerService.deletePersonById(personFound);
			
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}	
	}
	
	protected Person buildTestPerson(String firstName, String lastName, String identifierName) {
		Person person = new Person();
		person.setGivenName(firstName);
		person.setFamilyName(lastName);
		person.setAddress1("2930 Oak Shadow Drive");
		person.setCity("Herndon");
		person.setState("Virginia");
	
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier(identifierName);
		IdentifierDomain id = new IdentifierDomain();
		id.setIdentifierDomainName("SSN");
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);
		person.addPersonIdentifier(pi);
		
		Nationality nation = new Nationality();
		nation.setNationalityCode("USA");
		person.setNationality(nation);
		return person;
	}
	
	private Person addPerson( Person person) {
		PersonManagerService personService = Context.getPersonManagerService();
		Person personAdded = null; 
		try {
			personAdded = personService.addPerson(person);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
		return personAdded;
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
