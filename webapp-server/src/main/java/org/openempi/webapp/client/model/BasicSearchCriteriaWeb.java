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

public class BasicSearchCriteriaWeb extends BaseModelData
{
	public static final String IDENTIFIER_DOMAIN = "identifierDomain";
	public static final String UNIVERSAL_IDENTIFIER_TYPE_CODE = "universalIdentifierTypeCode";
	public static final String IDENTIFIER = "identifier";

	public BasicSearchCriteriaWeb() {
	}

	public BasicSearchCriteriaWeb(String identifier, IdentifierDomainWeb identifierDomain, IdentifierDomainTypeCodeWeb universalIdentifierTypeCode) {
		set(IDENTIFIER, identifier);
		set(IDENTIFIER_DOMAIN, identifierDomain);
		set(UNIVERSAL_IDENTIFIER_TYPE_CODE, universalIdentifierTypeCode);
	}
	
	public String getIdentifier() {
		return get(IDENTIFIER);
	}
	
	public IdentifierDomainWeb getIdentifierDomain() {
		return get(IDENTIFIER_DOMAIN);
	}
	
	public IdentifierDomainTypeCodeWeb getUniversalIdentifierTypeCode() {
		return get(UNIVERSAL_IDENTIFIER_TYPE_CODE);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("identifier: ").append(getIdentifier());
		sb.append(",identifier domain: ").append(getIdentifierDomain().getIdentifierDomainName());
		sb.append(",universalIdentifierTypeCode: ").append(getUniversalIdentifierTypeCode().getUniversalIdentifierTypeCode());
		return sb.toString();
	}
}
