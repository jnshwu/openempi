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
package org.openempi.webapp.server;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openhie.openempi.context.Context;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AbstractRemoteServiceServlet extends RemoteServiceServlet
{
	public final static String SESSION_KEY = "sessionKey";
	
	protected Logger log = Logger.getLogger(getClass());

	protected void storeSessionKeyInSession(String sessionKey) {
		HttpSession session = this.getThreadLocalRequest().getSession();
		session.setAttribute(SESSION_KEY, sessionKey);
	}

	protected String getSessionKeyFromSession() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		return (String) session.getAttribute(SESSION_KEY);
	}

	protected void authenticateCaller() {
		String sessionKey = getSessionKeyFromSession();
		if (sessionKey == null || sessionKey.equals("")) {
			throw new AuthenticationException("User is not logged in or the session has expired.");
		}
		
		try {
			Context.authenticate(sessionKey);			
		} catch (Throwable t) {
			throw new AuthenticationException("User is not logged in or the session has expired.");			
		}
	}
}
