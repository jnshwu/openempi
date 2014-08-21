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
package org.openhie.openempi.dao;

import java.util.List;

import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;

public class IdentifierDomainAttributeTest extends BaseDaoTestCase
{
	private PersonDao personDao;

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public void testAddIdentifierDomainAttribute() {
		try {
			List<IdentifierDomain> domains = personDao.getIdentifierDomains();
			
			IdentifierDomain domain = domains.get(0);
			IdentifierDomainAttribute attrib = personDao.addIdentifierDomainAttribute(domain, "name", "Some Name");
			System.out.println("Attrib: " + attrib);
			
			IdentifierDomainAttribute foundAttrib = personDao.getIdentifierDomainAttribute(domain, "name");
			System.out.println("Found attribute: " + foundAttrib);
			
			foundAttrib.setAttributeValue("Updated test");
			System.out.println("Updated attribute: " + personDao.getIdentifierDomainAttribute(domain, "name"));
			
			personDao.removeIdentifierDomainAttribute(foundAttrib);
			System.out.println("Removed the attribute: " + foundAttrib);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
