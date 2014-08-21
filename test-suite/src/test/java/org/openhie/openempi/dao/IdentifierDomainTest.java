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

import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;

public class IdentifierDomainTest extends BaseDaoTestCase
{
	private PersonDao personDao;

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public void testGetIdentifierDomain() {
		try {
			Context.startup();
			Context.authenticate("admin", "admin");
			Configuration configuration = Context.getConfiguration();
			log.debug("Global identifier configuration is: " + configuration.getGlobalIdentifier());
			IdentifierDomain domain = configuration.getGlobalIdentifierDomain();
			IdentifierDomain identifierDomain = personDao.findIdentifierDomain(domain);
			log.debug("Found global identifier domain in the repository: " + identifierDomain);
			if (identifierDomain != null && identifierDomain.getIdentifierDomainId() != null) {
				log.debug("Deleting existing global identifier domain in repository.");
				personDao.removeIdentifierDomain(identifierDomain);
			}
			log.debug("Adding global identifier domain to the repository.");
			domain.setDateCreated(new java.util.Date());
			domain.setUserCreatedBy(Context.getUserContext().getUser());
			personDao.addIdentifierDomain(domain);
			
			identifierDomain = personDao.findIdentifierDomain(domain);
			log.debug("Found newly created identifier domain: " + identifierDomain);
			
			log.debug("Deleting newly created identifier domain.");
			personDao.removeIdentifierDomain(identifierDomain);
			
			identifierDomain = personDao.findIdentifierDomain(domain);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			Context.shutdown();
		}
	}

	public void testGetPersonsWithoutIdentifierInDomain() {
		try {
			Context.startup();
			Context.authenticate("admin", "admin");
			Configuration configuration = Context.getConfiguration();
			log.debug("Global identifier configuration is: " + configuration.getGlobalIdentifier());
			IdentifierDomain domain = configuration.getGlobalIdentifierDomain();
			IdentifierDomain identifierDomain = personDao.findIdentifierDomain(domain);
			java.util.List<Integer> personIds = personDao.getPersonsWithoutIdentifierInDomain(identifierDomain, false);
			log.debug("There are " + personIds.size() + " person records without an identifier in domain " + identifierDomain);
//			for (Integer id : personIds) {
//				log.debug("Person with id " + id + " does not have an identifier in domain with identifier " + identifierDomain.getIdentifierDomainId());
//			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			Context.shutdown();			
		}		
	}
}
