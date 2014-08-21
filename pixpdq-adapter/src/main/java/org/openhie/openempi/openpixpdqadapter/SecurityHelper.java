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
package org.openhie.openempi.openpixpdqadapter;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.context.Context;

public class SecurityHelper
{
	protected static final Log log = LogFactory.getLog(SecurityHelper.class);
	public static final String USERNAME_PROPERTY = "username";
	public static final String PASSWORD_PROPERTY = "password";
	public static final String DEFAULT_USERNAME = "admin";
	public static final String DEFAULT_PASSWORD = "admin";
	
	public static String getSessionKey() throws AuthenticationException, NamingException {
		log.debug("Current user context is " + Context.getUserContext());
		if (Context.getUserContext() != null && Context.getUserContext().getSessionKey() != null) {
			return Context.getUserContext().getSessionKey();
		}
		String sessionKey = Context.authenticate(getUsername(), getPassword());
		return sessionKey;
	}
	
	public static String getUsername() {
		String username = getSystemProperty(USERNAME_PROPERTY);
		if (username == null) {
			username = DEFAULT_USERNAME;
		}
		return username;
	}
	
	public static String getPassword() {
		String password = getSystemProperty(PASSWORD_PROPERTY);
		if (password == null) {
			password = DEFAULT_PASSWORD;
		}
		return password;
	}
	
	public static String getSystemProperty(String propertyName) {
		return System.getProperty(propertyName);
	}
}
