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
package org.openhie.openempi.dao;

import java.util.List;

import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PersonDaoTest extends BaseDaoTestCase
{
	private PersonDao personDao;

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public void testGetAllPersons() {
		try {
			List<Integer> personIds = personDao.getAllPersons();
			for (Integer id : personIds) {
				System.out.println("Person Id: " + id);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}	
	}

	public void testGetIdentifierDomains() {
		try {
			List<IdentifierDomain> domains = personDao.getIdentifierDomains();
			for (IdentifierDomain domain : domains) {
				System.out.println("Domain: " + domain);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void testGetIdentifierTypeCodes() {
		try {
			List<String> codes = personDao.getIdentifierDomainTypeCodes();
			for (String code : codes) {
				System.out.println("Universal Identifier Type Code: " + code);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void testGetPersonsByPersonIdentifier() {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("c%");
		IdentifierDomain domain = new IdentifierDomain();
		domain.setUniversalIdentifierTypeCode("hl7");
		pi.setIdentifierDomain(domain);
		try {
			List<Person> persons = personDao.getPersonsByIdentifier(pi);
			for (Person person : persons) {
				System.out.println("Person Is: " + person);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testGetPersonByPersonIdentifier() {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("100");
		IdentifierDomain domain = new IdentifierDomain();
		domain.setNamespaceIdentifier("100");
		pi.setIdentifierDomain(domain);
		try {
			Person person = personDao.getPersonById(pi);
			System.out.println("Found person: " + person);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testGetGenderByCode() {
		String codes[] = { "Male", "Female", "Other" };
		for (String code : codes) {
			System.out.println("Found gender " + personDao.findGenderByName(code) + " for code " + code);
		}
	}
	
	public void testGetRaceByCode() {
		String codes[] = { "White", "Black", "Other Race", "Unknown"};
		for (String code : codes) {
			System.out.println("Found race " + personDao.findRaceByName(code) + " for code " + code);
		}
	}
}
