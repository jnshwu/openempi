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

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PersonServiceTest extends BaseServiceTestCase
{
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
	}

	public void testAddPerson() {
		PersonManagerService personService = Context.getPersonManagerService();
		Person person = buildTestPerson("555-55-5555");
		try {
			personService.addPerson(person);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
		
		person = buildTestPerson("555-55-5556");
		try {
			personService.addPerson(person);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
		
		person = buildTestPerson("555-55-5557");
		try {
			personService.addPerson(person);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}		
	}
	
	public void testDeletePerson() {
		PersonManagerService personService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
		PersonIdentifier pi = buildTestPersonIdentifier("555-55-5555");
		Person personOne=null;
		try {
			personOne = queryService.findPersonById(pi);
			if (personOne == null) {
				assertTrue("Can't find a person that was just created.", false);
			}
			
			java.util.List<Person> linked = queryService.findLinkedPersons(pi);
			log.debug("Before deletion here are the links:");
			for (Person person : linked) {
				log.debug("Person with id " + personOne.getPersonId() + " is currently liked to person " + person.getPersonId());
			}
			for (Person pToDelete : linked) {
				PersonIdentifier ssnPi = getSsn(pToDelete);
				if (ssnPi == null) {
					log.debug("Person doesn't have an SSN ID: " + pToDelete.getPersonId());
					continue;
				}
				personService.deletePerson(ssnPi);
				java.util.List<Person> linksLeft = queryService.findLinkedPersons(pi);
				for (Person person : linksLeft) {
					log.debug("Person with id " + personOne.getPersonId() + " is currently liked to person " + person.getPersonId());
				}
			}
			personService.deletePerson(pi);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}

	private PersonIdentifier getSsn(Person pToDelete) {
		for (PersonIdentifier pi : pToDelete.getPersonIdentifiers()) {
			if (pi.getIdentifierDomain().getIdentifierDomainName().equals("SSN")) {
				return pi;
			}
		}
		return null;
	}
	
/*	
	public void testNewPersonFields() {
		PersonManagerService personService = Context.getPersonManagerService();
		Person person = new Person();
		person.setGivenName("Odysseas");
		person.setFamilyName("Pentakalos");
		person.setAddress1("2930 Oak Shadow Drive");
		person.setCity("Oak Hill");
		person.setState("Virginia");

		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("555-55-5555");
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);
		person.addPersonIdentifier(pi);
		
		Nationality nation = new Nationality();
		nation.setNationalityCode("USA");
		person.setNationality(nation);

		person.setGroupNumber("testGroup1");
		person.setAccount("5819980");
		person.setAccountIdentifierDomain(id);
		person.setCustom11("5819980");
		try {
			personService.addPerson(person);
			log.debug("Added person with id: " + person.getPersonId());
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}		
	}
	
	public void testGetPerson() {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("555-55-5555");
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);

		List<PersonIdentifier> ids = new ArrayList<PersonIdentifier>();
		ids.add(pi);
		PersonManagerService personService = Context.getPersonManagerService();
		try {
			Person personFound = personService.getPerson(ids);
			log.debug("Found the person: " + personFound);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}

	public void testUpdatePerson() {

		List<PersonIdentifier> ids = new ArrayList<PersonIdentifier>();
		{
			PersonIdentifier pi = new PersonIdentifier();
			pi.setIdentifier("555-55-5555");
			IdentifierDomain id = new IdentifierDomain();
			id.setNamespaceIdentifier("SSN");
			pi.setIdentifierDomain(id);			
			ids.add(pi);
		}
		PersonManagerService personService = Context.getPersonManagerService();
		try {
			Person personFound = personService.getPerson(ids);
			log.debug("Before update found the person: " + personFound);
			personFound.setCity("Fairfax");
			if (personFound.getNationality() != null) {
				personFound.getNationality().setNationalityCd(null);
				personFound.getNationality().setNationalityCode("GRC");
			}
			personService.updatePerson(personFound);
			
//			ids = new ArrayList<PersonIdentifier>();
//			{
//				PersonIdentifier pi = new PersonIdentifier();
//				pi.setIdentifier("555-55-5555");
//				IdentifierDomain id = new IdentifierDomain();
//				id.setNamespaceIdentifier("SSN");
//				pi.setIdentifierDomain(id);			
//				ids.add(pi);
//			}
//			Person updatedPerson = personService.getPerson(ids);
//			log.debug("Updated person is: " + updatedPerson);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}

	public void testGetUpdatedPerson() {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("555-55-5555");
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);

		List<PersonIdentifier> ids = new ArrayList<PersonIdentifier>();
		ids.add(pi);
		PersonManagerService personService = Context.getPersonManagerService();
		try {
			Person personFound = personService.getPerson(ids);
			log.debug("Found the person: " + personFound);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
*/

}
