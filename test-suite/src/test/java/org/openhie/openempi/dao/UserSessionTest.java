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

import org.openhie.openempi.model.User;
import org.openhie.openempi.model.UserSession;
import org.openhie.openempi.util.SessionGenerator;

public class UserSessionTest extends BaseDaoTestCase
{
	private UserDao userDao;
	private UserSessionDao userSessionDao;

	public void testAddUserSession() {
		UserSession session = new UserSession();
		User user = (User) userDao.loadUserByUsername("admin");
		session.setUser(user);
		session.setDateCreated(new java.util.Date());
		session.setSessionKey(SessionGenerator.generateSessionId());
		try {
			userSessionDao.saveUserSession(session);
			log.debug("Session is: " + session);
		} catch (Exception e) {
			log.error("Failed while saving a new session: " + e, e);
		}
	}
	
	public void testFindUserSession() {
		java.util.List<UserSession> sessions = userSessionDao.findAll();
		for (UserSession session : sessions) {
			log.debug("Found session: " + session);
			UserSession found = userSessionDao.findById(session.getSessionId());
			log.debug("Found by session id: " + found);
			found = userSessionDao.findBySessionKey(session.getSessionKey());
			log.debug("Found by session key: " + found);
		}
	}
	public UserSessionDao getUserSessionDao() {
		return userSessionDao;
	}

	public void setUserSessionDao(UserSessionDao userSessionDao) {
		this.userSessionDao = userSessionDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
