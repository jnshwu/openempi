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

public class PersonGlobalIdWithUpdatesTest extends BaseServiceTestCase
{
	private PersonManagerService personManagerService;
	private Person firstPerson;
	private Person secondPerson;
	
	protected void setupTest() {
		personManagerService = Context.getPersonManagerService();

		Date dob = new Date();
		firstPerson = buildTestPerson("ALAN", "ALPHA", dob, "testIdentifierLeft");
		secondPerson = buildTestPerson("ALAN", "BETA", dob, "testIdentifierRight");
	}
	
	public void testLink() {
	    setupTest();
		assertNotNull("Unable to find first person.", firstPerson);
		assertNotNull("Unable to find second person.", firstPerson);
		try {
		    firstPerson = personManagerService.addPerson(firstPerson);
    		System.out.println("Added first Person");
    		System.out.println(printPerson(firstPerson));

    		secondPerson = personManagerService.addPerson(secondPerson);
            System.out.println("Added second Person");
            System.out.println(printPerson(secondPerson));
            
            IdentifierDomain globalDomain = Context.getConfiguration().getGlobalIdentifierDomain();
            assertNotNull("The global identifier domain has not been configured.", globalDomain);
            
            System.out.println("The global identifier domain is: " + globalDomain.getIdentifierDomainName());
            String firstGid = getIdentifierInDomain(firstPerson, globalDomain);
            assertNotNull("The first record was not assigned a global identifier", firstGid);
            System.out.println("First record's global id is: " + firstGid);

            String secondGid = getIdentifierInDomain(secondPerson, globalDomain);
            assertNotNull("The second record was not assigned a global identifier", secondGid);
            System.out.println("Second record's global id is: " + secondGid);
            
            assertFalse("The global identifiers should be different.", firstGid.equals(secondGid));
            
            // Now we will update the second person so that it matches the first one.            
    		secondPerson.setFamilyName("ALPHA");
    		personManagerService.updatePersonById(secondPerson);
            System.out.println("\nUpdated second Person to match the first one.");
            System.out.println(printPerson(secondPerson));

            secondGid = getIdentifierInDomain(secondPerson, globalDomain);
            assertNotNull("The second record was not assigned a global identifier after the update", secondGid);
            System.out.println("Second record's global id is: " + secondGid);
            
            assertTrue("The global identifiers should be the same.", firstGid.equals(secondGid));
            
            // Now we will update the second person so that it does not match the first one.            
            secondPerson.setFamilyName("BETA");
            personManagerService.updatePersonById(secondPerson);
            System.out.println("\nUpdated second Person not to match the first one.");
            System.out.println(printPerson(secondPerson));

            secondGid = getIdentifierInDomain(secondPerson, globalDomain);
            assertNotNull("The second record was not assigned a global identifier after the second update", secondGid);
            System.out.println("Second record's global id is: " + secondGid);
            
            assertFalse("The global identifiers should be different.", firstGid.equals(secondGid));
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

    protected Person buildTestPerson(String firstName, String lastName, Date dateOfBirth, String identifierName) {
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
		id.setIdentifierDomainName("SSN");
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);
		person.addPersonIdentifier(pi);
		
		Nationality nation = new Nationality();
		nation.setNationalityCode("USA");
		person.setNationality(nation);
		return person;
	}
}
