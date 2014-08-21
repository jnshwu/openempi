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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.ejb.person.PersonManagerService;
import org.openhie.openempi.ejb.person.PersonQueryService;
import org.openhie.openempi.ejb.security.SecurityService;

public class ServiceLocator
{
	protected static final Log log = LogFactory.getLog(ServiceLocator.class);
	
	private static Context context;
	
	public static PersonManagerService getPersonManagerService() throws NamingException {
		log.debug("Looking up an instance of the person manager using: PersonManagerService/remote");
		PersonManagerService personManagerService = (PersonManagerService) getInitialContext().lookup("openempi/PersonManagerService");
		log.debug("Obtained an instance of the person manager service: " + personManagerService);
		return personManagerService;
	}
	
	public static SecurityService getSecurityService() throws NamingException {
		log.debug("Looking up an instance of the person using: SecurityService/remote");
		SecurityService securityService = (SecurityService) getInitialContext().lookup("openempi/SecurityService");
		log.debug("Obtained an instance of the security service: " + securityService);
		return securityService;
	}
	
	public static PersonQueryService getPersonQueryService() throws NamingException {
		log.debug("Looking up an instance of the person query using: PersonQueryService/remote");
		PersonQueryService personQueryService = (PersonQueryService) getInitialContext().lookup("openempi/PersonQueryService");
		log.debug("Obtained an instance of the person query service: " + personQueryService);
		return personQueryService;		
	}
	/**
	 * Get the initial naming context
	 */
	protected static Context getInitialContext() throws NamingException {
		if (context == null) {
			Hashtable<String,String> props = new Hashtable<String,String>();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
			props.put(Context.PROVIDER_URL, "localhost:1099");
			context = new InitialContext(props);
		}
		return context;
	}
}
