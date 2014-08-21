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
package org.openempi.webapp.server.security;

import org.openempi.webapp.client.SecurityService;
import org.openempi.webapp.client.domain.Authentication;
import org.openempi.webapp.client.domain.AuthenticationException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * Default in-memory implementation of the {@link SecurityService}. Borrowed
 * Inspired by Beginning Google Web Toolkit Book
 * 
 * @author Uri Boness
 * @author Sam Brodkin
 */
public class SecurityServiceInMemoryImpl extends RemoteServiceServlet implements SecurityService {

	private static Authentication authentication = Authentication.ANONYMOUS;

	/**
	 * {@inheritDoc}
	 */
	public void login(String userName) throws AuthenticationException {
		if (userName.equals("badguy")) {
			throw new AuthenticationException();
		}
		authentication = new Authentication(userName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void logout() {
		authentication = Authentication.ANONYMOUS;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isLoggedIn() {
		return authentication != Authentication.ANONYMOUS;
	}

	/**
	 * {@inheritDoc}
	 */
	public Authentication getCurrentAuthentication() {
		return authentication;
	}

}
