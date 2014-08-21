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


import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.openhie.openempi.ejb.person.PersonManagerService;
import org.openhie.openempi.ejb.person.PersonQueryService;
import org.openhie.openempi.ejb.security.SecurityService;

public abstract class AbstractEJBTestCase extends TestCase
 {
	protected Logger log = Logger.getLogger(AbstractEJBTestCase.class);
	
	public AbstractEJBTestCase() {
		super();
	}

	public AbstractEJBTestCase(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected PersonManagerService getPersonManagerService() throws NamingException {
		return ServiceLocator.getPersonManagerService();
	}

	protected PersonQueryService getPersonQueryService() throws NamingException {
		return ServiceLocator.getPersonQueryService();
	}

	protected SecurityService getSecurityService() throws NamingException {
		return ServiceLocator.getSecurityService();
	}
}
