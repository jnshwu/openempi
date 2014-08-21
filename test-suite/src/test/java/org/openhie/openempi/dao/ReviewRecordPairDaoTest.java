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

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.LinkSource;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.ReviewRecordPair;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;

public class ReviewRecordPairDaoTest extends BaseDaoTestCase
{
	private PersonDao personDao;
	private UserDao userDao;
	private PersonLinkDao personLinkDao;
	
	public void testAddReviewRecordPair() {
		PersonManagerService managerService = Context.getPersonManagerService();
		
		PersonIdentifier pi = buildTestIdentifier("PH2009001", "IHENA");
		Person person = buildTestPerson("PH2009001", "IHENA", pi);
		addPerson(person);			
		pi = buildTestIdentifier("PH2009007", "IHENA");
		person = buildTestPerson("PH2009007", "IHENA", pi);
		addPerson(person);	

		ReviewRecordPair pair = generateReviewRecordPair("PH2009001", "IHENA", "PH2009007", "IHENA");
		assertNotNull(pair);
		
		try {
			managerService.addReviewRecordPair(pair);
			assertNotNull(pair.getReviewRecordPairId());			
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
		
		pi = buildTestIdentifier("PH2009002", "IHENA");
		person = buildTestPerson("PH2009002", "IHENA", pi);
		addPerson(person);			
		pi = buildTestIdentifier("PH2009008", "IHENA");
		person = buildTestPerson("PH2009008", "IHENA", pi);
		addPerson(person);	
		
		pair = generateReviewRecordPair("PH2009002", "IHENA", "PH2009008", "IHENA");
		assertNotNull(pair);
		
		try {
			managerService.addReviewRecordPair(pair);
			assertNotNull(pair.getReviewRecordPairId());	
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
/*		
		pair = generateReviewRecordPair("PH2009003", "IHENA", "PH2009009", "IHENA");
		personLinkDao.addReviewRecordPair(pair);
		assertNotNull(pair.getReviewRecordPairId());

		pair = generateReviewRecordPair("PH2009004", "IHENA", "PH2009010", "IHENA");
		personLinkDao.addReviewRecordPair(pair);
		assertNotNull(pair.getReviewRecordPairId());

		pair = generateReviewRecordPair("PH2009005", "IHENA", "PH2009011", "IHENA");
		personLinkDao.addReviewRecordPair(pair);
		assertNotNull(pair.getReviewRecordPairId());

		pair = generateReviewRecordPair("PH2009006", "IHENA", "PH2009012", "IHENA");
		personLinkDao.addReviewRecordPair(pair);
		assertNotNull(pair.getReviewRecordPairId());
*/
		List<ReviewRecordPair> list = getPersonLinkDao().getAllUnreviewedReviewRecordPairs();
		for (ReviewRecordPair apair : list) {
			System.out.println(apair);
						
			apair.setRecordsMatch(false);
			try {
				managerService.matchReviewRecordPair(apair);
			} catch (Exception e) {
				log.error("Exception: " + e, e);
				e.printStackTrace();
			}
		}
		assertNotNull(list);
		assertTrue(list.size() > 0);
	}

	public void testDeletePerson() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		List<Person> personsFound = null;
		try {
			
			Person person = new Person();
			person.setGivenName("PH2009001");
			person.setFamilyName("IHENA");
			
			personsFound = queryService.findPersonsByAttributes(person);
			for (Person p : personsFound) {
				managerService.deletePersonById(p);
			}
			
			person = new Person();
			person.setGivenName("PH2009007");
			person.setFamilyName("IHENA");
			
			personsFound = queryService.findPersonsByAttributes(person);
			for (Person p : personsFound) {
				managerService.deletePersonById(p);
			}

			person = new Person();
			person.setGivenName("PH2009002");
			person.setFamilyName("IHENA");
			
			personsFound = queryService.findPersonsByAttributes(person);
			for (Person p : personsFound) {
				managerService.deletePersonById(p);
			}
			person = new Person();
			person.setGivenName("PH2009008");
			person.setFamilyName("IHENA");
			
			personsFound = queryService.findPersonsByAttributes(person);
			for (Person p : personsFound) {
				managerService.deletePersonById(p);
			}
			
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}	
	}
	
	private ReviewRecordPair generateReviewRecordPair(String leftIdentifier, String leftNamespaceIdentifier, String rightIdentifier, String rightNamespaceIdentifier) {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier(leftIdentifier);
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier(leftNamespaceIdentifier);
		pi.setIdentifierDomain(id);
		Person leftPerson = personDao.getPersonById(pi);
		
		pi.setIdentifier(rightIdentifier);
		id = new IdentifierDomain();
		id.setNamespaceIdentifier(rightNamespaceIdentifier);
		pi.setIdentifierDomain(id);
		Person rightPerson = personDao.getPersonById(pi);
		
		ReviewRecordPair pair = new ReviewRecordPair();
		pair.setPersonLeft(leftPerson);
		pair.setPersonRight(rightPerson);
		pair.setDateCreated(new java.util.Date());
		
		User user = getUserDao().get(-1L);
		if(user == null ) {
			user = getUserDao().get(1L);
			if(user == null ) {
				return null;
			}
		} 		
		pair.setUserCreatedBy(user);
		pair.setLinkSource(new LinkSource(1));
		pair.setWeight(1.0);
		return pair;
	}
	
	protected PersonIdentifier buildTestIdentifier(String identifierName, String namespaceIdentifier) {
		PersonIdentifier identifier = new PersonIdentifier();
		identifier.setIdentifier(identifierName);
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier(namespaceIdentifier);
		identifier.setIdentifierDomain(id);	
		
		return identifier;		
	}
	
	protected Person buildTestPerson(String firstName, String lastName, PersonIdentifier identifier) {
		Person person = new Person();
		person.setGivenName(firstName);
		person.setFamilyName(lastName);
		person.setAddress1("2930 Oak Shadow Drive");
		person.setCity("Herndon");
		person.setState("Virginia");
	
		person.addPersonIdentifier(identifier);
		
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
	
	public void testGetAllReviewRecordPairs() {
	}
	
	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}
	
	public PersonLinkDao getPersonLinkDao() {
		return personLinkDao;
	}

	public void setPersonLinkDao(PersonLinkDao personLinkDao) {
		this.personLinkDao = personLinkDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
