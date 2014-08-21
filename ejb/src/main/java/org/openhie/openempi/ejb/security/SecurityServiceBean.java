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
package org.openhie.openempi.ejb.security;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.BaseSpringInjectableBean;
import org.openhie.openempi.ejb.SpringInjectionInterceptor;

@Stateless(name="SecurityService")
@Interceptors ({SpringInjectionInterceptor.class})
public class SecurityServiceBean extends BaseSpringInjectableBean implements SecurityService
{
	private static final long serialVersionUID = -407957125655403090L;

	public String authenticate(String username, String password)
			throws AuthenticationException {
		log.debug("Received an authentication request from user " + username);
		String sessionKey = Context.authenticate(username, password);
		return sessionKey;
	}

}
