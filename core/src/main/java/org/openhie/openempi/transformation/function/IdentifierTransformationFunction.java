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
package org.openhie.openempi.transformation.function;

import org.openhie.openempi.model.PersonIdentifier;

public class IdentifierTransformationFunction extends AbstractTransformationFunction
{
	public final static String IDENTIFIER_DOMAIN_NAME = "identifierDomainName";

	public IdentifierTransformationFunction() {
		super();
	}
	
	@Override
	public Object transform(Object field) {
		if (field == null || !(field instanceof java.util.Set)) {
			log.warn("Field " + field + " is not of type java.util.Set.");
			return field;
		}
		
		String identifierDomainName = getParameter(IDENTIFIER_DOMAIN_NAME);
		if (identifierDomainName == null || identifierDomainName.length() == 0) {
			log.warn("Transformation function requires parameter: " + IDENTIFIER_DOMAIN_NAME);
			return null;
		}
		@SuppressWarnings("unchecked")
		java.util.Set<PersonIdentifier> setOfIdentifiers = (java.util.Set<PersonIdentifier>) field;
		for (PersonIdentifier identifier : setOfIdentifiers) {
			if (identifier.getIdentifierDomain() != null &&
					identifier.getIdentifierDomain().getIdentifierDomainName() != null &&
					identifier.getIdentifierDomain().getIdentifierDomainName().equalsIgnoreCase(identifierDomainName)) {
				return identifier.getIdentifier();
			}
		}
		return null;
	}
	
	@Override
	public String[] getParameterNames() {
		return new String[] { IDENTIFIER_DOMAIN_NAME };
	}
}
