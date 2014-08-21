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
package org.openhie.openempi.ejb.person;

import java.util.List;

import javax.ejb.Remote;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

@Remote
public interface PersonManagerService
{
	public Person addPerson(String sessionKey, Person person) throws ApplicationException;
	
	public Person importPerson(String sessionKey, Person person) throws ApplicationException;
	
	public void updatePerson(String sessionKey, Person person) throws ApplicationException;
	
	public void deletePerson(String sessionKey, PersonIdentifier personIdentifier) throws ApplicationException;
	
	public void mergePersons(String sessionKey, PersonIdentifier retiredIdentifier, PersonIdentifier survivingIdentifier) throws ApplicationException;
	
	public Person getPerson(String sessionKey, List<PersonIdentifier> personIdentifiers);
	public IdentifierDomain obtainUniqueIdentifierDomain(String sessionKey, String universalIdentifierTypeCode);
	
	public IdentifierDomainAttribute addIdentifierDomainAttribute(String sessionKey, IdentifierDomain identifierDomain, String attributeName, String attributeValue);
	
	public void updateIdentifierDomainAttribute(String sessionKey, IdentifierDomainAttribute identifierDomainAttribute);

	public void removeIdentifierDomainAttribute(String sessionKey, IdentifierDomainAttribute identifierDomainAttribute);	
}
