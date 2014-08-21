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
package org.openempi.webapp.server;

import java.util.List;

import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.LinkedPersonWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.UserFileWeb;
import org.openhie.openempi.context.Context;

public class PersonDataServiceTest extends BaseServiceTestCase
{
	
//	public void testGetIdentifierDomainTypes() {
//		PersonDataServiceImpl personService = new PersonDataServiceImpl();
//		List<IdentifierDomain> domains = personService.getIdentifierDomains();
//		for (IdentifierDomain domain : domains) {
//			System.out.println("Found domain: " + domain);
//		}
//	}

	public void testGetPersonsByIdentifier() {
		PersonDataServiceImpl personService = new PersonDataServiceImpl();
		PersonIdentifierWeb identifier = new PersonIdentifierWeb();
		identifier.setIdentifier("c%");
		IdentifierDomainWeb domain = new IdentifierDomainWeb();
		domain.setUniversalIdentifierTypeCode("hl7");
		identifier.setIdentifierDomain(domain);
		try {
			List<PersonWeb> persons = personService.getPersonsByIdentifier(identifier);
			System.out.println("List of person has " + persons.size() + " entries");
			for (PersonWeb person : persons) {
				System.out.println("Found person: " + person.getGivenName() + " " + person.getFamilyName() + " " + person.getPostalCode() + " " + person.getState() + " " + person.getSsn());
				for (PersonIdentifierWeb id : person.getPersonIdentifiers()) {
					System.out.println("Found identifier: " + id.getIdentifier() + " " + id.getIdentifierDomain().getNamespaceIdentifier() + " " + id.getIdentifierDomain().getUniversalIdentifier() + " " + id.getIdentifierDomain().getUniversalIdentifierTypeCode());
				}
			}
		} catch (Exception e) {
			log.error("Test failed: " + e);
		}
	}

	public void testGetPersonsByAttributes() {
		PersonDataServiceImpl personService = new PersonDataServiceImpl();
		PersonWeb templatePerson = new PersonWeb();
		templatePerson.setFamilyName("Mart%");
//		templatePerson.setGivenName("CHIP");
		try {
			List<PersonWeb> persons = personService.getPersonsByAttribute(templatePerson);
			for (PersonWeb person : persons) {
				System.out.println("Found person: " + person.getGivenName() + " " + person.getFamilyName() + " " + person.getPostalCode() + " " + person.getState() + " " + person.getSsn());
				for (PersonIdentifierWeb id : person.getPersonIdentifiers()) {
					System.out.println("Found identifier: " + id.getIdentifier() + " " + id.getIdentifierDomain().getNamespaceIdentifier() + " " + id.getIdentifierDomain().getUniversalIdentifier() + " " + id.getIdentifierDomain().getUniversalIdentifierTypeCode());
				}
				if (person.getLinkedPersons() == null) {
					continue;
				}
				for (LinkedPersonWeb linked : person.getLinkedPersons()) {
					System.out.println("Found linked person: " + linked.getGivenName() + " " + linked.getFamilyName() + " " + 
							linked.getPersonIdentifier() + " " + linked.getDateOfBirth() + " " +
							linked.getAddress1() + " " + linked.getCity() + " "  + linked.getState() + " " + linked.getPostalCode());
				}
			}		
			System.out.println("List of person has " + persons.size() + " entries");
		} catch (Exception e) {
			log.error("Test failed: " + e);
		}
	}
//	private String getTemplate() {
//		return "<p><b>Company:</b> {givenName}</p><br><p><b>Summary:</b> {familyName}</p>";
//		return "<p>Name: {givenName}</p>, <p>Company: {familyName}</p>, <p>Location: {postalCode}</p>, <p>Kids:</p>, " +
//				"<tpl for=\"personIdentifiers\"> " +
//				"<p>{#}. {identifier} {this.identifierDomain}, </p> " +
//				"{ isWorking: function() { return('yes');} }</tpl>";
//	}
	
	public void testUserFileSupport() {
		UserFileWeb userFile = new UserFileWeb();
		userFile.setName("test");
		userFile.setFilename("/tmp/test.file");
		Context.startup();
		Context.authenticate("admin", "admin");
		try {
			PersonDataServiceImpl personService = new PersonDataServiceImpl();
			personService.addUserFile(userFile);
			
			List<UserFileWeb> files = personService.getUserFiles("admin");
			for (UserFileWeb aFile : files) {
				System.out.println(aFile);
				personService.removeUserFile(aFile.getUserFileId());
				System.out.println("Removed user file entry");
			}
		} catch (Exception e) {
			log.error("Test failed: " + e);
		}		
	}
}
