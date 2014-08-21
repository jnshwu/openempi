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

import java.util.HashSet;

import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.model.Person;

public class ModelTransformerTest extends BaseServiceTestCase
{
	public void testMapPersonWebToPerson() {
		PersonWeb personWeb = new PersonWeb();
		personWeb.setGivenName("John");
		personWeb.setFamilyName("Doe");
		personWeb.setAddress1("1000 Someplace Drive");
		personWeb.setAddress2("Suite 2000");
		personWeb.setCity("Palo Alto");
		personWeb.setState("CA");
		personWeb.setPostalCode("94301");
		personWeb.setDateOfBirth("1982-08-10");
		personWeb.setGender("M");
		IdentifierDomainWeb domain = new IdentifierDomainWeb(null, "HIMSS2005", "1.3.6.1.4.1.21367.2005.1.1", "ISO");
		PersonIdentifierWeb identifier = new PersonIdentifierWeb();
		identifier.setIdentifier("LPDQ113XX01");
		identifier.setIdentifierDomain(domain);
		HashSet<PersonIdentifierWeb> set = new HashSet<PersonIdentifierWeb>();
		set.add(identifier);
		personWeb.setPersonIdentifiers(set);
		Person person = ModelTransformer.mapToPerson(personWeb, org.openhie.openempi.model.Person.class);
		log.debug("Transformed object is " + person);
	}
}
