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

import java.util.Date;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PersonGlobalIdWithUpdatePerson3Test extends BaseServiceTestCase
{
	private PersonManagerService personManagerService;
	private PersonQueryService personQueryService;

	private Person firstPerson;
	private Person secondPerson;
	private Person thirdPerson;	
	
	protected void setupTest() {
		personManagerService = Context.getPersonManagerService();
		personQueryService = Context.getPersonQueryService();
		
		Date dob = new Date();
		firstPerson = buildTestPerson("ALAN", "ALPHA", dob, "testIdentifier1", "NIST2010");
		secondPerson = buildTestPerson("ALAN", "ALPHA", dob, "testIdentifier2", "NIST2010-2");
		thirdPerson = buildTestPerson("ALAN", "ALPHA", dob, "testIdentifier3", "NIST2010-3");
	}
	
	public void testLink() {
	    setupTest();
		assertNotNull("Unable to find first person.", firstPerson);
		assertNotNull("Unable to find second person.", secondPerson);
		assertNotNull("Unable to find third person.", thirdPerson);
		try {
			
			// (1) add first person
		    firstPerson = personManagerService.addPerson(firstPerson);
    		System.out.println("Added first Person");
    		System.out.println(printPerson(firstPerson));

			// (2) add second person
    		secondPerson = personManagerService.addPerson(secondPerson);
            System.out.println("Added second Person");
            System.out.println(printPerson(secondPerson));

			// (3) add third person
            thirdPerson = personManagerService.addPerson(thirdPerson);
            System.out.println("Added third Person");
            System.out.println(printPerson(thirdPerson));
          
			// check global identifiers    
            IdentifierDomain globalDomain = Context.getConfiguration().getGlobalIdentifierDomain();
            assertNotNull("The global identifier domain has not been configured.", globalDomain);
            
            System.out.println("The global identifier domain is: " + globalDomain.getIdentifierDomainName());
            String firstGid = getIdentifierInDomain(firstPerson, globalDomain);
            assertNotNull("The first record was not assigned a global identifier", firstGid);
            System.out.println("First record's global id is: " + firstGid);

            String secondGid = getIdentifierInDomain(secondPerson, globalDomain);
            assertNotNull("The second record was not assigned a global identifier", secondGid);
            System.out.println("Second record's global id is: " + secondGid);

            String thirdGid = getIdentifierInDomain(thirdPerson, globalDomain);
            assertNotNull("The third record was not assigned a global identifier", secondGid);
            System.out.println("Third record's global id is: " + thirdGid);
            
            assertTrue("The global identifiers should be same.", firstGid.equals(secondGid));
            assertTrue("The global identifiers should be same.", secondGid.equals(thirdGid));
            
            
            // (4) Now we will update the third person so that it does not match the first and second.            
            thirdPerson.setGivenName("BETA");
            thirdPerson = personManagerService.updatePersonById(thirdPerson);
            
            System.out.println("\nUpdated third Person not to match the first and second.");
            System.out.println(printPerson(thirdPerson));

            thirdGid = getIdentifierInDomain(thirdPerson, globalDomain);
            assertNotNull("The third record was not assigned a global identifier after the update", thirdGid);
            System.out.println("Third record's global id is: " + thirdGid);
            
            assertFalse("The global identifiers should be different.", firstGid.equals(thirdGid));
 
            
            // (5) Now we will update the third person so that it matches the first and second         
            thirdPerson.setGivenName("ALAN");
            thirdPerson = personManagerService.updatePersonById(thirdPerson);
            System.out.println("\nUpdated third Person to match the first and second.");
            System.out.println(printPerson(thirdPerson));

            thirdGid = getIdentifierInDomain(thirdPerson, globalDomain);
            assertNotNull("The third record was not assigned a global identifier after the second update", thirdGid);
            System.out.println("Third record's global id is: " + thirdGid);
            
            assertTrue("The global identifiers should be same.", firstGid.equals(thirdGid));                        
            
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if (firstPerson != null) {
	            try {
	                personManagerService.removePerson(firstPerson.getPersonId());
	            } catch (Exception e) {
	            }
		    }
            if (secondPerson != null) {
                try {
                    personManagerService.removePerson(secondPerson.getPersonId());
                } catch (Exception e) {
                }
            }
            if (thirdPerson != null) {
                try {
                    personManagerService.removePerson(thirdPerson.getPersonId());
                } catch (Exception e) {
                }
            }
		}
		
	}
	
	private String getIdentifierInDomain(Person person, IdentifierDomain globalDomain) {
	    for (PersonIdentifier id : person.getPersonIdentifiers()) {
	        if (id.getIdentifierDomain().getIdentifierDomainId().equals(globalDomain.getIdentifierDomainId()) &&
	                id.getDateVoided() == null) {
	            return id.getIdentifier();
	        }
	    }
        return null;
    }

    private String printPerson(Person person) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("First Name: ").append(person.getGivenName()).append("\n");
        sb.append("Last Name: ").append(person.getFamilyName()).append("\n");
        sb.append("Date of Birth: ").append(person.getDateOfBirth()).append("\n");
        sb.append("Address: ").append(person.getAddress1()).append("\n");
        sb.append("City: ").append(person.getCity()).append("\n");
        sb.append("Zip Code: ").append(person.getPostalCode()).append("\n");
        for (PersonIdentifier id : person.getPersonIdentifiers()) {
            sb.append("\tIdentifier: ").append(id.getIdentifier()).append("->")
                .append(id.getIdentifierDomain().getIdentifierDomainName()).append("->")
                .append(id.getDateVoided())                
                .append("\n");
        }
        return sb.toString();
    }

    protected Person buildTestPerson(String firstName, String lastName, Date dateOfBirth, String identifierName, String domainName) {
		Person person = new Person();
		person.setGivenName(firstName);
		person.setFamilyName(lastName);
		person.setDateOfBirth(dateOfBirth);
		person.setAddress1("2930 Oak Shadow Drive");
		person.setCity("Herndon");
		person.setState("Virginia");
	
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier(identifierName);
		IdentifierDomain id = new IdentifierDomain();
		id.setIdentifierDomainName(domainName);
		id.setNamespaceIdentifier(domainName);
		pi.setIdentifierDomain(id);
		person.addPersonIdentifier(pi);
		
		Nationality nation = new Nationality();
		nation.setNationalityCode("USA");
		person.setNationality(nation);
		return person;
	}
}
