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

public class PersonAddWithLinkTest extends BaseServiceTestCase
{
	private PersonManagerService personManagerService;
	private Person leftPerson;
	private Person rightPerson;
	private Person thirdPerson;
	
	protected void setupTest() {
		personManagerService = Context.getPersonManagerService();

		Date dob = new Date();
		leftPerson = buildTestPerson("ALAN", "ALPHATest", dob, "testIdentifierLeft");
		rightPerson = buildTestPerson("ALAN", "ALPHATest", dob, "testIdentifierRight");
        thirdPerson = buildTestPerson("ALAN", "ALPHATest", dob, "testIdentifierThird");
	}
	
	public void testLink() {
	    setupTest();
		assertNotNull("Unable to find left person.", leftPerson);
		assertNotNull("Unable to find right person.", rightPerson);
		try {
    		leftPerson = personManagerService.addPerson(leftPerson);
    		System.out.println("Added first Person");
    		System.out.println(printPerson(leftPerson));
    		
    		leftPerson.setCity("Oakton");
    		personManagerService.updatePerson(leftPerson);
            System.out.println("Updated first Person");
            System.out.println(printPerson(leftPerson));
    		
    		rightPerson = personManagerService.addPerson(rightPerson);
            System.out.println("Added second linked Person");
            System.out.println(printPerson(rightPerson));
            
            thirdPerson = personManagerService.addPerson(thirdPerson);
            System.out.println("Added third linked Person");
            System.out.println(printPerson(thirdPerson));
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    if (leftPerson != null) {
	            try {
	                personManagerService.removePerson(leftPerson.getPersonId());
	            } catch (Exception e) {
	            }
		    }
            if (rightPerson != null) {
                try {
                    personManagerService.removePerson(rightPerson.getPersonId());
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
	
	private String printPerson(Person person) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("First Name: ").append(person.getGivenName()).append("\n");
        sb.append("Last Name: ").append(person.getGivenName()).append("\n");
        sb.append("Date of Birth: ").append(person.getGivenName()).append("\n");
        sb.append("Address: ").append(person.getAddress1()).append("\n");
        sb.append("City: ").append(person.getCity()).append("\n");
        sb.append("Zip Code: ").append(person.getPostalCode()).append("\n");
        for (PersonIdentifier id : person.getPersonIdentifiers()) {
            sb.append("\tIdentifier: ").append(id.getIdentifier()).append("->")
                .append(id.getIdentifierDomain().getIdentifierDomainName())
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
