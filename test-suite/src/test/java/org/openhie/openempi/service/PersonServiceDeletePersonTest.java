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

import java.util.ArrayList;
import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

public class PersonServiceDeletePersonTest extends BaseServiceTestCase
{
	public void testUpdatePerson() {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("555-55-5555");
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("SSN");
		pi.setIdentifierDomain(id);

		PersonManagerService personService = Context.getPersonManagerService();
		List<PersonIdentifier> ids = new ArrayList<PersonIdentifier>();
		ids.add(pi);		
		try {
			Person person = personService.getPerson(ids);
			personService.deletePerson(pi);
			log.debug("Deleted person id is: " + person);
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}
}
