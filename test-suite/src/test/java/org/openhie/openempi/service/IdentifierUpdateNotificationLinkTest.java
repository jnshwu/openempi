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

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;

public class IdentifierUpdateNotificationLinkTest extends BaseServiceTestCase
{
    private PersonManagerService personManagerService;
    private PersonQueryService personQueryService;
    private static java.util.Map<String, Person> addedPersons = new java.util.HashMap<String, Person>();
    static {
	System.setProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME,
		"openempi-extension-contexts-identifier-update-notification.properties");
	System.setProperty(Constants.OPENEMPI_CONFIGURATION_FILENAME,
		"mpi-config-identifier-update-notification.xml");
    }

    protected void onSetUp() throws Exception {
	super.onSetUp();
	System.out.println("The size of the patient list is "
		+ addedPersons.size());
	if (addedPersons.size() > 0) {
	    return;
	}
	personManagerService = Context.getPersonManagerService();
	personQueryService = Context.getPersonQueryService();

	Person person1 = buildTestPersonParams("Jon", "Smith",
		"9e4539b0-a045-11e1-a160-005056c00008",
		"2.16.840.1.113883.4.357");

	try {
	    personManagerService.addPerson(person1);
	    log.info("Added person with id: " + person1.getPersonId());
	} catch (Exception e) {
	    log.error("Exception: " + e, e);
	    e.printStackTrace();
	    throw e;
	}

	PersonIdentifier pi = new PersonIdentifier();
	pi.setIdentifier("9e4539b0-a045-11e1-a160-005056c00008");
	IdentifierDomain id = new IdentifierDomain();
	id.setNamespaceIdentifier("2.16.840.1.113883.4.357");
	pi.setIdentifierDomain(id);

	Person leftPerson = personQueryService.findPersonById(pi);
	addedPersons.put("leftPerson", leftPerson);

	Person person2 = buildTestPersonParams("John", "Smith",
		"b5195770-a045-11e1-a160-005056c00008",
		"2.16.840.1.113883.4.357");

	try {
	    personManagerService.addPerson(person2);
	    log.info("Added person with id: " + person2.getPersonId());
	} catch (Exception e) {
	    log.error("Exception: " + e, e);
	    e.printStackTrace();
	    throw e;
	}

	pi = new PersonIdentifier();
	pi.setIdentifier("b5195770-a045-11e1-a160-005056c00008");
	pi.setIdentifierDomain(id);

	Person rightPerson = personQueryService.findPersonById(pi);
	addedPersons.put("rightPerson", rightPerson);
    }

    public void testLinkTwoRecords() {
	Person leftPerson = addedPersons.get("leftPerson");
	Person rightPerson = addedPersons.get("rightPerson");
	System.out.println(leftPerson);
	System.out.println(rightPerson);
	assertNotNull("Unable to find left person.", leftPerson);
	assertNotNull("Unable to find right person.", rightPerson);
	PersonLink link = new PersonLink();
	link.setPersonLeft(leftPerson);
	link.setPersonRight(rightPerson);
	try {
	    System.out.println("Before link:");
	    Person left = personQueryService.loadPerson(leftPerson
		    .getPersonId());
	    printPersonIdentifiers("Left person's identifiers", left);
	    Person right = personQueryService.loadPerson(rightPerson
		    .getPersonId());
	    printPersonIdentifiers("Right person's identifiers", right);

	    link = personManagerService.linkPersons(link);
	    System.out.println(link);

	    endTransaction();

	    System.out.println("After link:");
	    left = personQueryService.loadPerson(leftPerson.getPersonId());
	    printPersonIdentifiers("Left person's identifiers", left);
	    right = personQueryService.loadPerson(rightPerson.getPersonId());
	    printPersonIdentifiers("Right person's identifiers", right);
	    System.out.println("-----------------");

	    personManagerService.unlinkPersons(link);
	    System.out.println(link);

	    endTransaction();

	    System.out.println("After Unlink:");
	    left = personQueryService.loadPerson(leftPerson.getPersonId());
	    printPersonIdentifiers("Left person's identifiers", left);
	    right = personQueryService.loadPerson(rightPerson.getPersonId());
	    printPersonIdentifiers("Right person's identifiers", right);
	    System.out.println("-----------------");

	} catch (ApplicationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    protected void onTearDown() throws Exception {
	for (Person person : addedPersons.values()) {
	    log.info("Deleting test person: " + person.getPersonId());
	    personManagerService.deletePersonById(person);
	}
	super.onTearDown();
    }

    private void printPersonIdentifiers(String msg, Person person) {
	System.out.println("For person with id: " + person.getPersonId() + " "
		+ msg);
	for (PersonIdentifier id : person.getPersonIdentifiers()) {
	    System.out
		    .println(id.getIdentifier()
			    + ":"
			    + id.getIdentifierDomain().getNamespaceIdentifier()
			    + ":"
			    + id.getIdentifierDomain().getUniversalIdentifier()
			    + ":"
			    + id.getIdentifierDomain()
				    .getUniversalIdentifierTypeCode());
	}
    }

}
