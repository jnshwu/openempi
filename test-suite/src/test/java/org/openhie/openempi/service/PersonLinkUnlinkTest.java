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
import org.openhie.openempi.configuration.GlobalIdentifier;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;

public class PersonLinkUnlinkTest extends BaseServiceTestCase
{
	private PersonManagerService personManagerService;
	private PersonQueryService personQueryService;
	private Person leftPerson;
	private Person rightPerson;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		personManagerService = Context.getPersonManagerService();
		personQueryService = Context.getPersonQueryService();

		Person personLeft = buildTestPerson("ALAN", "ALPHA-Left", "testIdentifierLeft");
		Person personRight = buildTestPerson("ALAN", "ALPHA-Right", "testIdentifierRight");
		addPerson(personLeft);	
		addPerson(personRight);	
		
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("testIdentifierLeft");
		IdentifierDomain id = new IdentifierDomain();
		id.setIdentifierDomainName("SSN");
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);
		leftPerson = personQueryService.findPersonById(pi);
		
		pi = new PersonIdentifier();
		pi.setIdentifier("testIdentifierRight");
		pi.setIdentifierDomain(id);
		rightPerson = personQueryService.findPersonById(pi);
	}
	
	public void testLink() {
		System.out.println(leftPerson);
		System.out.println(rightPerson);
		assertNotNull("Unable to find left person.", leftPerson);
		assertNotNull("Unable to find right person.", rightPerson);

		GlobalIdentifier globalIdentifier = Context.getConfiguration().getGlobalIdentifier();
		
		PersonLink link = new PersonLink();
		link.setPersonLeft(leftPerson);
		link.setPersonRight(rightPerson);
		try {
			// link leftPerson and rightPerson
			link = personManagerService.linkPersons(link);
			System.out.println(link);
			
		    endTransaction();

		    
			Person left = personQueryService.loadPerson(leftPerson.getPersonId());
			PersonIdentifier leftPersonGlobalIdentifier = null;
			for (PersonIdentifier identifier : left.getPersonIdentifiers()) {
				IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
				if (identifier.getDateVoided() == null && 
					identifierDomain != null && identifierDomain.equals(globalIdentifier.getIdentifierDomain())) {
					leftPersonGlobalIdentifier =  identifier;
				}			
			}	
			Person right = personQueryService.loadPerson(rightPerson.getPersonId());
			PersonIdentifier rightPersonGlobalIdentifier = null;
			for (PersonIdentifier identifier : right.getPersonIdentifiers()) {
				IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
				if (identifier.getDateVoided() == null && 
					identifierDomain != null && identifierDomain.equals(globalIdentifier.getIdentifierDomain())) {
					rightPersonGlobalIdentifier =  identifier;
				}			
			}			
			assertNotNull("leftPerson global identifier", leftPersonGlobalIdentifier);   
			assertNotNull("rightPerson global identifier", rightPersonGlobalIdentifier);   
			assertTrue("Same global identifiers " + leftPersonGlobalIdentifier.getIdentifier() + "; " + rightPersonGlobalIdentifier.getIdentifier(), 
					leftPersonGlobalIdentifier.getIdentifier().equals(rightPersonGlobalIdentifier.getIdentifier()));			
			
			// unlink leftPerson and rightPerson			
			personManagerService.unlinkPersons(link);
			System.out.println(link);

		    endTransaction();
		    
		    
			left = personQueryService.loadPerson(leftPerson.getPersonId());
			leftPersonGlobalIdentifier = null;
			for (PersonIdentifier identifier : left.getPersonIdentifiers()) {
				IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
				if (identifier.getDateVoided() == null && 
					identifierDomain != null && identifierDomain.equals(globalIdentifier.getIdentifierDomain())) {
					leftPersonGlobalIdentifier =  identifier;
				}			
			}			
			right = personQueryService.loadPerson(rightPerson.getPersonId());
			rightPersonGlobalIdentifier = null;
			for (PersonIdentifier identifier : right.getPersonIdentifiers()) {
				IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
				if (identifier.getDateVoided() == null && 
					identifierDomain != null && identifierDomain.equals(globalIdentifier.getIdentifierDomain())) {
					rightPersonGlobalIdentifier =  identifier;
				}			
			}			
			assertNotNull("leftPerson global identifier", leftPersonGlobalIdentifier);   
			assertNotNull("rightPerson global identifier", rightPersonGlobalIdentifier);   
			assertFalse("Different global identifiers " + leftPersonGlobalIdentifier.getIdentifier() + "; " + rightPersonGlobalIdentifier.getIdentifier(), 
					leftPersonGlobalIdentifier.getIdentifier().equals(rightPersonGlobalIdentifier.getIdentifier()));	
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void testDeletePerson() {
		PersonManagerService managerService = Context.getPersonManagerService();
		PersonQueryService queryService = Context.getPersonQueryService();
		
		Person personLeft = new Person();
		personLeft.setGivenName("ALAN");
		personLeft.setFamilyName("ALPHA-Left");
		
		Person personRight = new Person();
		personRight.setGivenName("ALAN");
		personRight.setFamilyName("ALPHA-Right");
		try {
			Person personFound = findPerson(queryService, personLeft, leftPerson.getPersonId());
			managerService.deletePersonById(personFound);
			
			personFound = findPerson(queryService, personRight, rightPerson.getPersonId());
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
}
