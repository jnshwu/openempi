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

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;

public class IdentifierDomainCrudTest extends BaseServiceTestCase
{
	private final static String universalIdentifierTypeCode = "RestonPCP";
	
	public void testAddIdentifierDomain() {
		PersonQueryService queryService = Context.getPersonQueryService();
		IdentifierDomain identifierDomain = new IdentifierDomain();
		identifierDomain.setIdentifierDomainName("Reston PCP");
		identifierDomain.setIdentifierDomainDescription("Reston Primary Care Provider");
		identifierDomain.setNamespaceIdentifier("RestonPCP");
		identifierDomain.setUniversalIdentifier("RestonPCP");
		identifierDomain.setUniversalIdentifierTypeCode("RestonPCP");
		IdentifierDomain idFound = queryService.findIdentifierDomain(identifierDomain);
		System.out.println("Searching for identifier domain returned: " + idFound);
		
		PersonManagerService personService = Context.getPersonManagerService();
		try {
			identifierDomain = personService.addIdentifierDomain(identifierDomain);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		idFound = queryService.findIdentifierDomain(identifierDomain);
		System.out.println("Searching for identifier domain after add returned: " + idFound);
		
		identifierDomain.setNamespaceIdentifier("RestonPCPModified");
		try {
			identifierDomain = personService.updateIdentifierDomain(identifierDomain);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		idFound = queryService.findIdentifierDomain(identifierDomain);
		System.out.println("Searching for identifier domain after update returned: " + idFound);

		try {
			personService.deleteIdentifierDomain(identifierDomain);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		idFound = queryService.findIdentifierDomain(identifierDomain);
		System.out.println("Searching for identifier domain after delete returned: " + idFound);
		
		identifierDomain = new IdentifierDomain();
		identifierDomain.setNamespaceIdentifier("IHENA");
		identifierDomain.setUniversalIdentifier("1.3.6.1.4.1.21367.2010.1.2.300");
		identifierDomain.setUniversalIdentifierTypeCode("ISO");
		idFound = queryService.findIdentifierDomain(identifierDomain);
		try {
			if (idFound != null) {
				personService.deleteIdentifierDomain(idFound);
				// This should fail because this entry is linked to person identifiers and the system should not let us delete it.
				fail();
			}
		} catch (Throwable t) {
		}
	}
}
