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
package org.openhie.openempi.dao.hibernate;

import java.util.List;

import org.openhie.openempi.dao.UserSessionDao;
import org.openhie.openempi.model.UserSession;

public class UserSessionDaoHibernate extends UniversalDaoHibernate implements UserSessionDao
{
	public UserSession findById(Integer sessionId) {
		log.debug("Locating session by id: " + sessionId);
		UserSession session = (UserSession) getHibernateTemplate().load(UserSession.class, sessionId);
		log.debug("Found session: " + session);
		return session;
	}

	@SuppressWarnings("unchecked")
	public UserSession findBySessionKey(String sessionKey) {
		log.debug("Locating session by key: " + sessionKey);
		List<UserSession> list = (List<UserSession>) getHibernateTemplate().find("from UserSession where sessionKey = ?", new String[] { sessionKey });
		if (list.size() == 0) {
			return null;
		}
		UserSession session = list.get(0);
		log.debug("Found session: " + session);
		return session;
	}

	public void saveUserSession(UserSession session) {
		log.debug("Saving session record: " + session);
		getHibernateTemplate().saveOrUpdate(session);
		log.debug("Finished saving the session.");
	}

	@SuppressWarnings("unchecked")
	public List<UserSession> findAll() {
		List<UserSession> sessions = (List<UserSession>) this.getAll(UserSession.class);
		return sessions;
	}
}
