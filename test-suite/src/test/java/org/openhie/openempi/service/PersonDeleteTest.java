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

import java.util.Calendar;
import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PersonDeleteTest extends BaseServiceTestCase
{
	public void testAddPerson() {
		Person person = new Person();
		person.setGivenName("Delete");
		person.setFamilyName("Person");
		person.setAddress1("100 Deletion Lane");
		person.setCity("Erased");
		person.setState("North Dakota");
		person.setPhoneNumber("000-0000");
		person.setPhoneAreaCode("000");
		Gender gender = new Gender();
		gender.setGenderCode("M");
		person.setGender(gender);
		person.setSsn("000-00-0000");
		Calendar dob = Calendar.getInstance();
		dob.set(1992, 5, 6);
		person.setDateOfBirth(dob.getTime());
		
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("deletePersonIdentifier");
		IdentifierDomain id = new IdentifierDomain();
		id.setIdentifierDomainName("SSN");
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);
		person.addPersonIdentifier(pi);
		
		PersonManagerService managerService = Context.getPersonManagerService();
		try {
			managerService.addPerson(person);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	public void testDeletePersonById() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
		Person person = new Person();
		person.setGivenName("Delete");
		person.setFamilyName("Person");
		person.setSsn("000-00-0000");
		
		try {
			List<Person> personsFound = queryService.findPersonsByAttributes(person);
			assertNotNull(personsFound);
			assertTrue(personsFound.size() > 0);
			Person personFound = personsFound.get(0);
			System.out.println("Will now delete the following person:");
			printPerson(personFound);
			managerService.deletePersonById(personFound);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	private void printPerson(Person personFound) {
		System.out.println("Found the person: " + personFound.getGivenName() + "," + personFound.getFamilyName() + "," + personFound.getPhoneNumber() + " with IDs:");
		for (PersonIdentifier id : personFound.getPersonIdentifiers()) {
			System.out.println("\t<" + id.getIdentifier() + "," + id.getIdentifierDomain().getNamespaceIdentifier() + ">");
		}
	}
}
