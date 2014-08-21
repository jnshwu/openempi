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

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.BaseSpringInjectableBean;
import org.openhie.openempi.ejb.SpringInjectionInterceptor;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

@Stateless(name="PersonQueryService")
@Interceptors ({SpringInjectionInterceptor.class})
public class PersonQueryServiceBean extends BaseSpringInjectableBean implements PersonQueryService
{
	private static final long serialVersionUID = -2349252197740764907L;
	
	public Person findPersonById(String sessionKey, PersonIdentifier identifier) {
		log.trace("In findPersonById method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.findPersonById(identifier);
	}

	public List<Person> findPersonsById(String sessionKey, PersonIdentifier identifier) {
		log.trace("In findPersonsById method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.findPersonsById(identifier);
	}

	public List<Person> findPersonsByAttributes(String sessionKey, Person person) {
		log.trace("In findPersonsByAttributes method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.findPersonsByAttributes(person);
	}
	
	public List<Person> findPersonsByAttributesPaged(String sessionKey, Person person,
			int firstResult, int maxResults)
	{
		log.trace("In findPersonsByAttributesPaged method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.findPersonsByAttributesPaged(person, firstResult, maxResults);		
	}

	public List<Person> loadAllPersonsPaged(String sessionKey, int firstResult, int maxResults)
	{
		log.trace("In loadPersonsPaged method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.loadAllPersonsPaged(firstResult, maxResults);		
	}

	public List<Person> findLinkedPersons(String sessionKey, PersonIdentifier identifier) {
		log.trace("In findLinkedPersons method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.findLinkedPersons(identifier);		
	}
	
	public Person loadPerson(String sessionKey, Integer personId)
	{
		log.trace("In loadPerson method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.loadPerson(personId);		
	}

	public List<Person> loadPersons(String sessionKey, List<Integer> personIds)
	{
		log.trace("In loadPersons method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.loadPersons(personIds);		
	}
	
	public List<IdentifierDomain> getIdentifierDomains(String sessionKey) {
		log.trace("In getIdentifierDomains method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.getIdentifierDomains();
	}
	
	public List<String> getIdentifierDomainTypeCodes(String sessionKey) {
		log.trace("In getIdentifierDomainTypeCodes method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.getIdentifierDomainTypeCodes();
	}
	
	public IdentifierDomainAttribute getIdentifierDomainAttribute(String sessionKey, IdentifierDomain identifierDomain, String attributeName) {
		log.trace("In getIdentifierDomainAttribute method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.getIdentifierDomainAttribute(identifierDomain, attributeName);
	}
	
	public List<IdentifierDomainAttribute> getIdentifierDomainAttributes(String sessionKey, IdentifierDomain identifierDomain) {
		log.trace("In getIdentifierDomainAttributes method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonQueryService personService = Context.getPersonQueryService();
		return personService.getIdentifierDomainAttributes(identifierDomain);
	}
}
