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
import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PersonQueryTest extends BaseServiceTestCase
{
	public void testFindLinkedPersons() {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("555-55-5555");
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);

		List<PersonIdentifier> ids = new ArrayList<PersonIdentifier>();
		ids.add(pi);
		try {
			PersonQueryService queryService = Context.getPersonQueryService();
			List<Person> linkedPersons = queryService.findLinkedPersons(pi);
			for (Person p : linkedPersons) {
				log.debug("Found the linked person: " + p);
			}
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}		
	}
	
	public void testGetPersonByAttributes() {
		try {
			Person personOne = new Person();
			personOne.setCity("Herdon");
			personOne.setFamilyName("Pentakalos");
			personOne.setGivenName("Odysseas");
			PersonQueryService personService = Context.getPersonQueryService();
			List<Person> list = personService.findPersonsByAttributes(personOne);
			for (Person person : list) {
				System.out.println("Found person: " + person);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testGetPersonByExtendedAttributes() {
		try {
			Person personOne = new Person();
			personOne.setCity("Greenvile");
			personOne.setFamilyName("Moorxe");
			personOne.setGivenName("Demi");
			Gender gender = new Gender();
			gender.setGenderCode("F");
			personOne.setGender(gender);
			PersonQueryService personService = Context.getPersonQueryService();
			List<Person> list = personService.findPersonsByAttributes(personOne);
			for (Person person : list) {
				System.out.println("Found person: " + person);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testGetPersonByAccount() {
		try {
			Person personOne = new Person();
			personOne.setAccount("5819980");
			PersonQueryService personService = Context.getPersonQueryService();
			List<Person> list = personService.findPersonsByAttributes(personOne);
			for (Person person : list) {
				System.out.println("Found person: " + person);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
