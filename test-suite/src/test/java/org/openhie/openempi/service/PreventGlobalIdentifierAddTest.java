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

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PreventGlobalIdentifierAddTest extends BaseServiceTestCase
{
	private static final String GLOBAL_IDENTIFIER_DOMAIN_NAME = "ECID";
	private List<Person> addedPersons = new java.util.ArrayList<Person>();
	
	static {
		System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME, "mpi-config-prevent-global-identifier-add.xml");
	}
	
	public void testAddPerson() {
		PersonManagerService personService = Context.getPersonManagerService();
		Person person1 = buildTestPerson("John", "SmithNoGlobal", "987-65-4321", GLOBAL_IDENTIFIER_DOMAIN_NAME, GLOBAL_IDENTIFIER_DOMAIN_NAME);
		try {
			personService.addPerson(person1);
			assertTrue("System should not allow a record with a global identifier to be added.", false);
			addedPersons.add(person1);
		} catch (ApplicationException e) {
			assertTrue("It is correct that this was prevented.", true);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	public void testUpdatePerson() {
		PersonManagerService personService = Context.getPersonManagerService();
		Person person = buildTestPerson("John", "SmithNoGlobal", "987-65-4321", "NID", "NID");
		try {
			person = personService.addPerson(person);
			addedPersons.add(person);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
			assertFalse("The record should had been added.", true);
		}
		
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("11111111111");
		IdentifierDomain id = new IdentifierDomain();
		id.setIdentifierDomainName(GLOBAL_IDENTIFIER_DOMAIN_NAME);
		pi.setIdentifierDomain(id);
		person.addPersonIdentifier(pi);
		try {
			personService.updatePerson(person);
			assertTrue("System should not allow a record with a global identifier to be updated.", false);
		} catch (ApplicationException e) {
			assertTrue("It is correct that this was prevented.", true);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
			assertTrue(false);
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

	protected static Person buildTestPerson(String givenName, String familyName, String Id, String identifierDomainName, String namespaceIdentifier) {
		
		Person person = new Person();
		person.setGivenName(givenName);
		person.setFamilyName(familyName);
		
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier(Id);
		IdentifierDomain id = new IdentifierDomain();
		id.setIdentifierDomainName(identifierDomainName);
		id.setNamespaceIdentifier(namespaceIdentifier);
		pi.setIdentifierDomain(id);
		person.addPersonIdentifier(pi);	
		return person;
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
