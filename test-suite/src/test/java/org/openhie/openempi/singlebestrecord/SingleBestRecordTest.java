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
package org.openhie.openempi.singlebestrecord;

import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.service.BaseServiceTestCase;

public class SingleBestRecordTest extends BaseServiceTestCase
{
	static {
		System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME, "openempi-extension-contexts-single-best-record.properties");
		System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME, "mpi-config-single-best-record.xml");
	}

	/**
	 * Here we test the case where the SBR should return the record with the NID. We create two patient records, one with a NID
	 * and one without. The records are close enough that they are linked to one another. We then intentionally provide the record
	 * without the NID to the service and it should return the one with the NID.
	 * 
	 */
	public void testSingleBestRecordService() {
		Person personOne = new Person();
		personOne.setAddress1("1st record");
		personOne.setCity("Palo Alto");
		personOne.setState("CA");
		personOne.setPostalCode("94301");
		personOne.setFamilyName("Record");
		personOne.setGivenName("Single");
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("1234");
		IdentifierDomain id = new IdentifierDomain();
		id.setIdentifierDomainName("NID");
		id.setNamespaceIdentifier("NID");
		pi.setIdentifierDomain(id);
		personOne.addPersonIdentifier(pi);
		try {
			personOne = Context.getPersonManagerService().addPerson(personOne);
		} catch (ApplicationException e) {
			log.error("Failed while adding the first person: " + e, e);
		}
		
		Person personTwo = new Person();
		personTwo.setAddress1("2nd record");
		personTwo.setCity("Palo Alto");
		personTwo.setState("CA");
		personTwo.setPostalCode("94301");
		personTwo.setFamilyName("Recard");
		personTwo.setGivenName("Single");
		try {
			personTwo = Context.getPersonManagerService().addPerson(personTwo);
		} catch (ApplicationException e) {
			log.error("Failed while adding the first person: " + e, e);
		}
		
		Person personQuery = new Person();
		personQuery.setFamilyName("Recard");
		personQuery.setGivenName("Single");
		
		List<Person> found = Context.getPersonQueryService().findPersonsByAttributes(personQuery);
		for (Person p : found) {
			System.out.println("Found: " + found);
			Person sbr = Context.getPersonQueryService().getSingleBestRecord(p.getPersonId());
			assertEquals("Last name must be Record", sbr.getFamilyName(), "Record");
		}
	}

	/**
	 * Here we test the case where the SBR should return the record with the NID. We create two patient records, one with a NID
	 * and one without. The records are close enough that they are linked to one another. We then intentionally provide the record
	 * without the NID to the service and it should return the one with the NID.
	 * 
	 */
	public void testSingleBestRecordServiceRuleTwo() {
		Person personOne = new Person();
		personOne.setAddress1("1st record");
		personOne.setCity("Palo Alto");
		personOne.setState("CA");
		Gender gender = new Gender();
		gender.setGenderCode("M");
		personOne.setGender(gender);
		personOne.setPostalCode("94301");
		personOne.setFamilyName("Record");
		personOne.setGivenName("Best");
		try {
			personOne = Context.getPersonManagerService().addPerson(personOne);
		} catch (ApplicationException e) {
			log.error("Failed while adding the first person: " + e, e);
		}
		
		sleep(20000);
		
		Person personTwo = new Person();
		personTwo.setAddress1("2nd record");
		personTwo.setCity("Palo Alto");
		personTwo.setState("CA");
		personTwo.setPostalCode("94301");
		personTwo.setFamilyName("Record");
		personTwo.setGivenName("Bestt");
		gender = new Gender();
		gender.setGenderCode("M");
		personTwo.setGender(gender);
		try {
			personTwo = Context.getPersonManagerService().addPerson(personTwo);
		} catch (ApplicationException e) {
			log.error("Failed while adding the second person: " + e, e);
		}
		
		Person personThree = new Person();
		personThree.setAddress1("2nd record");
		personThree.setCity("Palo Alto");
		personThree.setState("CA");
		personThree.setPostalCode("94301");
		personThree.setFamilyName("Record");
		personThree.setGivenName("Bests");
		gender = new Gender();
		gender.setGenderCode("M");
		personThree.setGender(gender);
		try {
			personThree = Context.getPersonManagerService().addPerson(personThree);
		} catch (ApplicationException e) {
			log.error("Failed while adding the third person: " + e, e);
		}
		
		Person personQuery = new Person();
		personQuery.setFamilyName("Record");
		personQuery.setGivenName("Best");
		
		List<Person> found = Context.getPersonQueryService().findPersonsByAttributes(personQuery);
		for (Person p : found) {
			System.out.println("Found: " + found);
			Person sbr = Context.getPersonQueryService().getSingleBestRecord(p.getPersonId());
			assertEquals("First name must be Bast", sbr.getGivenName(), "Bests");
			System.out.println("The gender is: " + sbr.getGender());
			assertEquals("Gender must be male", sbr.getGender().getGenderCode(), "M");
		}
	}

	@Override
	protected void onTearDown() throws Exception {
		try {
			Person person = new Person();
			person.setGivenName("Single");
			person.setFamilyName("Record");
			deletePerson(person);
			
			person = new Person();
			person.setGivenName("Single");
			person.setFamilyName("Recard");
			deletePerson(person);

			person = new Person();
			person.setGivenName("Best");
			person.setFamilyName("Record");
			deletePerson(person);
			
			person = new Person();
			person.setGivenName("Bests");
			person.setFamilyName("Record");
			deletePerson(person);

			person = new Person();
			person.setGivenName("Bestt");
			person.setFamilyName("Record");
			deletePerson(person);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onTearDown();
	}	
}
