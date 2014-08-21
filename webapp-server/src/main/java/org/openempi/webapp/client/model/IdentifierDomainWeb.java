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
package org.openempi.webapp.client.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

@SuppressWarnings("serial")
public class IdentifierDomainWeb extends BaseModelData
{
	public IdentifierDomainWeb() {
	}
	
	public IdentifierDomainWeb(Integer identifierDomainId, String identifierDomainName, String identifierDomainDescription, String namespaceIdentifier,
			String universalIdentifier, String universalIdentifierTypeCode) {
		set("identifierDomainId", identifierDomainId);
		set("identifierDomainName", identifierDomainName);
		set("identifierDomainDescription", identifierDomainDescription);
		set("namespaceIdentifier", namespaceIdentifier);
		set("universalIdentifier", universalIdentifier);
		set("universalIdentifierTypeCode", universalIdentifierTypeCode);
	}
	
	public IdentifierDomainWeb(Integer identifierDomainId, String namespaceIdentifier, String universalIdentifier, String universalIdentifierTypeCode) {
		set("identifierDomainId", identifierDomainId);
		set("namespaceIdentifier", namespaceIdentifier);
		set("universalIdentifier", universalIdentifier);
		set("universalIdentifierTypeCode", universalIdentifierTypeCode);
	}
	
	public Integer getIdentifierDomainId() {
		return get("identifierDomainId");
	}
	
	public void setIdentifierDomainId(Integer identifierDomainId) {
		set("identifierDomainId", identifierDomainId);
	}
	
	public String getIdentifierDomainName() {
		return get("identifierDomainName");
	}
	
	public void setIdentifierDomainName(String identifierDomainName) {
		set("identifierDomainName", identifierDomainName);
	}
	
	public String getIdentifierDomainDescription() {
		return get("identifierDomainDescription");
	}
	
	public void setIdentifierDomainDescription(String identifierDomainDescription) {
		set("identifierDomainDescription", identifierDomainDescription);
	}
	
	public String getNamespaceIdentifier() {
		return get("namespaceIdentifier");
	}
	
	public void setNamespaceIdentifier(String namespaceIdentifier) {
		set("namespaceIdentifier", namespaceIdentifier);
	}
	
	public String getUniversalIdentifier() {
		return get("universalIdentifier");
	}
	
	public void setUniversalIdentifier(String universalIdentifier) {
		set("universalIdentifier", universalIdentifier);
	}
	
	public String getUniversalIdentifierTypeCode() {
		return get("universalIdentifierTypeCode");
	}
	
	public void setUniversalIdentifierTypeCode(String universalIdentifierTypeCode) {
		set("universalIdentifierTypeCode", universalIdentifierTypeCode);
	}
	
	public String toString() {
		String value = get("namespaceIdentifier");
		if (value == null || value.length() == 0) {
			return get("universalIdentifier") + ":" + ("universalIdentifierTypeCode");
		}
		return value;
	}
}
