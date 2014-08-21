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
package org.openhie.openempi.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.UserManager;

/**
 * Implementation of UserContext
 *
 * @author Odysseas Pentakalos
 */
public class UserContext
{
	private static final Log log = LogFactory.getLog(UserContext.class);
	
	private UserManager userManager;
	
	private User user;
	private String sessionKey;
	
	public String authenticate(String username, String password) throws AuthenticationException {
		User user = userManager.authenticate(username, password);
		sessionKey = userManager.createSession(user);
		log.debug("Authentication request succeeded for user " + username);
		this.user = user;
		return sessionKey;
 	}

	public User authenticate(String sessionKey) throws AuthenticationException {
		User user = userManager.authenticate(sessionKey);
		this.sessionKey = sessionKey;
		this.user = user;
		return user;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
}
