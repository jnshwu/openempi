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

import java.util.Collection;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.User;

public class PersonLinkDaoTest extends BaseDaoTestCase
{
	private PersonDao personDao;
	private PersonLinkDao personLinkDao;
	private UserDao userDao;

	public void testGetPersonLinksFromIdList() {
		java.util.Set<Long> ids = new java.util.HashSet<Long>();
		ids.add(6822L);
		ids.add(6827L);
		java.util.List<PersonLink> links = personLinkDao.getPersonLinks(ids);
		for (PersonLink link : links) {
			log.debug("Link: " + link);
		}
		log.debug("Found: " + links.size() + " links.");

		ids = new java.util.HashSet<Long>();
		ids.add(4550L);
		Integer clusterId = personLinkDao.getClusterId(ids, Context.getMatchingService().getMatchingServiceId());
		log.debug("The shared cluster ID is: " + clusterId);
		
		links = personLinkDao.getPersonLinks(clusterId);
		for (PersonLink link : links) {
			log.debug("Link: " + link);
		}
		log.debug("Found: " + links.size() + " links.");		
	}
	
	public void testAddPersonLink() {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("LPH2009002");
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("IHELOCAL");
		pi.setIdentifierDomain(id);
		Person leftPerson = personDao.getPersonById(pi);

		pi = new PersonIdentifier();
		pi.setIdentifier("PH2009002");
		id = new IdentifierDomain();
		id.setNamespaceIdentifier("IHENA");
		pi.setIdentifierDomain(id);
		Person rightPerson = personDao.getPersonById(pi);

		PersonLink personLink = new PersonLink();
		personLink.setPersonLeft(leftPerson);
		personLink.setPersonRight(rightPerson);

		User user = (User) userDao.loadUserByUsername("admin");
		personLink.setUserCreatedBy(user);
		personLink.setDateCreated(new java.util.Date());
		personLink.setWeight(1.0);

		try {
			personLinkDao.addPersonLink(personLink);
			PersonLink link = personLinkDao.getPersonLink(leftPerson, rightPerson);
			System.out.println("Found a link: " + link);

			java.util.List<PersonLink> links = personLinkDao.getPersonLinks(leftPerson);
			for (PersonLink alink : links) {
				System.out.println("Found a link using left person: " + alink);
			}

			links = personLinkDao.getPersonLinks(rightPerson);
			for (PersonLink alink : links) {
				System.out.println("Found a link using right person: " + alink);
			}
			
//			personLinkDao.removeAllLinks();
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}

	public void testGetPersonLinks() {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier("PH2009015");
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier("IHENA");
		pi.setIdentifierDomain(id);
		Person person = personDao.getPersonById(pi);
		try {
			java.util.List<PersonLink> links = personLinkDao.getPersonLinks(person);
			for (PersonLink link : links) {
				System.out.println("Found a link: " + link);
			}
		} catch (Exception e) {
			log.error("Exception: " + e, e);
			e.printStackTrace();
		}
	}

	public PersonLinkDao getPersonLinkDao() {
		return personLinkDao;
	}

	public void setPersonLinkDao(PersonLinkDao personLinkDao) {
		this.personLinkDao = personLinkDao;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
