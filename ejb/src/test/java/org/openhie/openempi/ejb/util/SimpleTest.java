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
package org.openhie.openempi.ejb.util;

import javax.ejb.EJBException;
import javax.naming.NamingException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.openhie.openempi.ejb.person.PersonManagerService;
import org.openhie.openempi.ejb.security.SecurityService;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class SimpleTest extends AbstractEJBTestCase
{
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public void testHello() {
		try {
			SecurityService securityService = getSecurityService();
			String sessionKey = securityService.authenticate("admin", "admin");
			System.out.println("Obtained a session key of " + sessionKey);
			
			PersonManagerService personService = getPersonManagerService();
//			System.out.println(personService.sayHello(sessionKey));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public void testAddPerson() {
		try {
			String sessionKey = getSessionKey();
			
			PersonManagerService personService = getPersonManagerService();
			Person person = new Person();
			person.setGivenName("Odysseas");
			person.setFamilyName("Pentakalos");
			person.setAddress1("2930 Oak Shadow Drive");
			person.setCity("Herndon");
			person.setState("Virginia");
	
			PersonIdentifier pi = new PersonIdentifier();
			pi.setIdentifier("555-55-5555");
			IdentifierDomain id = new IdentifierDomain();
			id.setNamespaceIdentifier("SSN");
			pi.setIdentifierDomain(id);
			person.addPersonIdentifier(pi);
		
			try {
				personService.addPerson(sessionKey, person);
			} catch (EJBException e) {
				log.warn("Person already exists in the system: " + e);
			}
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
	
	private String getSessionKey() {
		String sessionKey=null;
		try {
			SecurityService securityService = getSecurityService();
			sessionKey = securityService.authenticate("admin", "admin");
			System.out.println("Obtained a session key of " + sessionKey);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
		return sessionKey;
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTestSuite(SimpleTest.class);
		return testSuite;
	}
}
