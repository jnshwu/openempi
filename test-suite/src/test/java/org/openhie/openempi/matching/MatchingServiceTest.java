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
package org.openhie.openempi.matching;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.service.BaseServiceTestCase;

public class MatchingServiceTest extends BaseServiceTestCase
{
	static {
		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts.properties");
		System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME, "mpi-config.xml");
	}
	
	private static java.util.List<Integer> personIds = new java.util.ArrayList<Integer>();

	public void testMatchingService() {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		Person personOne = new Person();
		personOne.setAddress1("49 Applecross Road");
		personOne.setCity("Palo Alto");
		personOne.setState("CA");
		personOne.setPostalCode("94301");
		personOne.setFamilyName("Martinez");
		personOne.setGivenName("Javier");
		Nationality nationality = new Nationality();
		nationality.setNationalityCd(100);
		personOne.setNationality(nationality);
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(1977, 2, 14);
		java.util.Date birthDate = calendar.getTime();
		personOne.setDateOfBirth(birthDate);

		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("ABCDE");
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("HIMSS2005");
		pi.setIdentifierDomain(id);
		personOne.addPersonIdentifier(pi);
		
		try {
			Context.getPersonManagerService().addPerson(personOne);
			personIds.add(personOne.getPersonId());
		} catch (ApplicationException e) {
			System.out.println("Person record already exists; Skip this.");
		}
		
		Person personTwo = new Person();
		personTwo.setAddress1("49 Applecross Road");
		personTwo.setCity("Palo Alto");
		personTwo.setState("CA");
		personTwo.setPostalCode("94301");
		personTwo.setFamilyName("Martinez");
		personTwo.setGivenName("Javier");
		personTwo.setNationality(nationality);
		personTwo.setDateOfBirth(birthDate);
		
		pi = new PersonIdentifier();
		pi.setIdentifier("AB12345");
		id = new IdentifierDomain();
		id.setNamespaceIdentifier("XREF2005");
		pi.setIdentifierDomain(id);
		personTwo.addPersonIdentifier(pi);		
		
		try {
			Context.getPersonManagerService().addPerson(personTwo);
			personIds.add(personTwo.getPersonId());
		} catch (ApplicationException e) {
			System.out.println("Person record already exists; Skip this.");
		}
	}
	
	public void testSomeClearData() throws Exception {
		try {
			for (Integer personId : personIds) {
				log.debug("Deleting person with id: " + personId);
				Context.getPersonManagerService().removePerson(personId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			personIds.clear();
		}
	}	

	public void testMatchingServiceThreeLinks() {
		System.out.println("Transactional support for this test has rollback set to " + this.isDefaultRollback());
		this.setDefaultRollback(true);
		
		Person personOne = new Person();
		personOne.setAddress1("49 Applecross Road");
		personOne.setCity("Palo Alto");
		personOne.setState("CA");
		personOne.setPostalCode("94301");
		personOne.setFamilyName("Martinez");
		personOne.setGivenName("Javier");
		Nationality nationality = new Nationality();
		nationality.setNationalityCd(100);
		personOne.setNationality(nationality);
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(1977, 2, 14);
		java.util.Date birthDate = calendar.getTime();
		personOne.setDateOfBirth(birthDate);

		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("ABCDE");
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("HIMSS2005");
		pi.setIdentifierDomain(id);
		personOne.addPersonIdentifier(pi);
		
		try {
			Context.getPersonManagerService().addPerson(personOne);
			personIds.add(personOne.getPersonId());
		} catch (ApplicationException e) {
			System.out.println("Person record already exists; Skip this.");
		}
		
		Person personTwo = new Person();
		personTwo.setAddress1("49 Applecross Road");
		personTwo.setCity("Palo Alto");
		personTwo.setState("CA");
		personTwo.setPostalCode("94301");
		personTwo.setFamilyName("Martinez");
		personTwo.setGivenName("Javier");
		personTwo.setNationality(nationality);
		personTwo.setDateOfBirth(birthDate);
		
		pi = new PersonIdentifier();
		pi.setIdentifier("AB12345");
		id = new IdentifierDomain();
		id.setNamespaceIdentifier("XREF2005");
		pi.setIdentifierDomain(id);
		personTwo.addPersonIdentifier(pi);		
		
		try {
			Context.getPersonManagerService().addPerson(personTwo);
			personIds.add(personTwo.getPersonId());
		} catch (ApplicationException e) {
			System.out.println("Person record already exists; Skip this.");
		}		
		
		Person personThree = new Person();
		personThree.setAddress1("49personId Applecross Road");
		personThree.setCity("Palo Alto");
		personThree.setState("CA");
		personThree.setPostalCode("94301");
		personThree.setFamilyName("Martinez");
		personThree.setGivenName("Javier");
		personThree.setNationality(nationality);
		personThree.setDateOfBirth(birthDate);
		
		pi = new PersonIdentifier();
		pi.setIdentifier("A1A1A1A1");
		id = new IdentifierDomain();
		id.setNamespaceIdentifier("XREF2005");
		pi.setIdentifierDomain(id);
		personThree.addPersonIdentifier(pi);		
		
		try {
			Context.getPersonManagerService().addPerson(personThree);
			personIds.add(personThree.getPersonId());
		} catch (ApplicationException e) {
			System.out.println("Person record already exists; Skip this.");
		}
	}
	
	public void testClearData() throws Exception {
		try {
			for (Integer personId : personIds) {
				log.debug("Deleting person with id: " + personId);
				Context.getPersonManagerService().removePerson(personId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			personIds.clear();
		}
	}
}