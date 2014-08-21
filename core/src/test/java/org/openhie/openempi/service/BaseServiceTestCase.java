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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public abstract class BaseServiceTestCase extends BaseManagerTestCase
{	
	@Override
	protected void onSetUp() throws Exception {
		log.debug("Application context is: " + getApplicationContext());
		super.onSetUp();
		Context.startup();
		Context.authenticate("admin", "admin");
	}

	@Override
	protected void onTearDown() throws Exception {
		System.out.println("Before tearDown Application context is: " + getApplicationContext());
		super.onTearDown();
		System.out.println("After tearDown Application context is: " + getApplicationContext());
		Context.shutdown();
	}
	
	protected void deletePerson(Person person) throws ApplicationException {
		List<Person> persons = Context.getPersonQueryService().findPersonsByAttributes(person);
		Map<PersonIdentifier,PersonIdentifier> idsDeleted=new HashMap<PersonIdentifier,PersonIdentifier>();
		for (Person personFound : persons) {
			PersonIdentifier id = personFound.getPersonIdentifiers().iterator().next();
			if (id.getDateVoided() == null && idsDeleted.get(id) == null) {
				Context.getPersonManagerService().deletePerson(id);
				log.debug("Deleted person with identifier " + personFound.getPersonId());
				for (PersonIdentifier idDeleted : personFound.getPersonIdentifiers()) {
					idsDeleted.put(idDeleted, idDeleted);
				}
			}
		}
	}

	protected void sleep(long sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
		}
	}

	protected static PersonIdentifier buildTestPersonIdentifier(String ssn) {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier(ssn);
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);
		return pi;
	}

	protected static Person buildTestPerson(String ssn) {
		Person person = new Person();
		person.setGivenName("Odysseas");
		person.setFamilyName("Pentakalos");
		person.setAddress1("2930 Oak Shadow Drive");
		person.setCity("Herndon");
		person.setState("Virginia");
		person.setSsn(ssn);
	
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier(ssn);
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);
		person.addPersonIdentifier(pi);
		
		Nationality nation = new Nationality();
		nation.setNationalityCode("USA");
		person.setNationality(nation);
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
