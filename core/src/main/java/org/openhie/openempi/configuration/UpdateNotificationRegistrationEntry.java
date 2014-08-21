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
package org.openhie.openempi.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.model.BaseObject;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.User;

public class UpdateNotificationRegistrationEntry extends BaseObject
{
	private static final long serialVersionUID = 6836460519679990976L;
	protected final Log log = LogFactory.getLog(getClass());

	private User user;
	private IdentifierDomain identifierDomain;
	private int timeToLive;

	public UpdateNotificationRegistrationEntry() {
	}
	
	public UpdateNotificationRegistrationEntry(User user, IdentifierDomain identifierDomain, int timeToLive) {
		this.user = user;
		this.identifierDomain = identifierDomain;
		this.timeToLive = timeToLive;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public IdentifierDomain getIdentifierDomain() {
		return identifierDomain;
	}

	public void setIdentifierDomain(IdentifierDomain identifierDomain) {
		this.identifierDomain = identifierDomain;
	}

	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}

	@Override
	public String toString() {
		return "UpdateNotificationRegistrationEntry [user=" + user + ", identifierDomain=" + identifierDomain
				+ ", timeToLive=" + timeToLive + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpdateNotificationRegistrationEntry other = (UpdateNotificationRegistrationEntry) obj;
		if (identifierDomain == null) {
			if (other.identifierDomain != null)
				return false;
		} else if (!identifierDomain.equals(other.identifierDomain))
			return false;
		if (timeToLive != other.timeToLive)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifierDomain == null) ? 0 : identifierDomain.hashCode());
		result = prime * result + timeToLive;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
}
