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

import org.openhie.openempi.model.BaseObject;
import org.openhie.openempi.model.IdentifierDomain;

public class GlobalIdentifier extends BaseObject
{
	private static final long serialVersionUID = -4959981332457798934L;

	private boolean assignGlobalIdentifier;
	private String identifierDomainName;
	private String identifierDomainDescription;
	private String namespaceIdentifier;
	private String universalIdentifier;
	private String universalIdentifierType;
	private IdentifierDomain identifierDomain;
	
	public boolean isAssignGlobalIdentifier() {
		return assignGlobalIdentifier;
	}

	public void setAssignGlobalIdentifier(boolean assignGlobalIdentifier) {
		this.assignGlobalIdentifier = assignGlobalIdentifier;
	}

	public String getNamespaceIdentifier() {
		return namespaceIdentifier;
	}

	public void setNamespaceIdentifier(String namespaceIdentifier) {
		this.namespaceIdentifier = namespaceIdentifier;
	}

	public String getUniversalIdentifier() {
		return universalIdentifier;
	}

	public void setUniversalIdentifier(String universalIdentifier) {
		this.universalIdentifier = universalIdentifier;
	}

	public String getUniversalIdentifierType() {
		return universalIdentifierType;
	}

	public void setUniversalIdentifierType(String universalIdentifierType) {
		this.universalIdentifierType = universalIdentifierType;
	}

	public String getIdentifierDomainName() {
		return identifierDomainName;
	}

	public void setIdentifierDomainName(String identifierDomainName) {
		this.identifierDomainName = identifierDomainName;
	}

	public String getIdentifierDomainDescription() {
		return identifierDomainDescription;
	}

	public void setIdentifierDomainDescription(String identifierDomainDescription) {
		this.identifierDomainDescription = identifierDomainDescription;
	}

	public IdentifierDomain getIdentifierDomain() {
		return identifierDomain;
	}

	public void setIdentifierDomain(IdentifierDomain identifierDomain) {
		this.identifierDomain = identifierDomain;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GlobalIdentifier other = (GlobalIdentifier) obj;
		if (assignGlobalIdentifier != other.assignGlobalIdentifier)
			return false;
		if (namespaceIdentifier == null) {
			if (other.namespaceIdentifier != null)
				return false;
		} else if (!namespaceIdentifier.equals(other.namespaceIdentifier))
			return false;
		if (universalIdentifier == null) {
			if (other.universalIdentifier != null)
				return false;
		} else if (!universalIdentifier.equals(other.universalIdentifier))
			return false;
		if (universalIdentifierType == null) {
			if (other.universalIdentifierType != null)
				return false;
		} else if (!universalIdentifierType.equals(other.universalIdentifierType))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (assignGlobalIdentifier ? 1231 : 1237);
		result = prime * result + ((namespaceIdentifier == null) ? 0 : namespaceIdentifier.hashCode());
		result = prime * result + ((universalIdentifier == null) ? 0 : universalIdentifier.hashCode());
		result = prime * result + ((universalIdentifierType == null) ? 0 : universalIdentifierType.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "GlobalIdentifier [assignGlobalIdentifier=" + assignGlobalIdentifier + ", identifierDomainName="
				+ identifierDomainName + ", identifierDomainDescription=" + identifierDomainDescription
				+ ", namespaceIdentifier=" + namespaceIdentifier + ", universalIdentifier=" + universalIdentifier
				+ ", universalIdentifierType=" + universalIdentifierType + "]";
	}
}
