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

import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

@Remote
public interface PersonQueryService
{
	public Person findPersonById(String sessionKey, PersonIdentifier identifier);

	public List<Person> findPersonsById(String sessionKey, PersonIdentifier identifier);
	
	public List<Person> findLinkedPersons(String sessionKey, PersonIdentifier identifier);

	public Person loadPerson(String sessionKey, Integer personId);

	public List<Person> loadPersons(String sessionKey, List<Integer> personIds);

	public List<Person> loadAllPersonsPaged(String sessionKey, int firstResult, int maxResults);

	public List<Person> findPersonsByAttributes(String sessionKey, Person person);	
	
	public List<Person> findPersonsByAttributesPaged(String sessionKey, Person person, int firstResult, int maxResults);
	
	public List<IdentifierDomain> getIdentifierDomains(String sessionKey);
	
	public List<String> getIdentifierDomainTypeCodes(String sessionKey);
		
	public IdentifierDomainAttribute getIdentifierDomainAttribute(String sessionKey, IdentifierDomain identifierDomain, String attributeName);
	
	public List<IdentifierDomainAttribute> getIdentifierDomainAttributes(String sessionKey, IdentifierDomain identifierDomain);
	
}
