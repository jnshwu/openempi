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

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.BaseSpringInjectableBean;
import org.openhie.openempi.ejb.SpringInjectionInterceptor;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

@Stateless(name="PersonManagerService")
@Interceptors ({SpringInjectionInterceptor.class})
public class PersonManagerServiceBean extends BaseSpringInjectableBean implements PersonManagerService {
	private static final long serialVersionUID = 6181663173021377758L;

	public Person addPerson(String sessionKey, Person person) throws ApplicationException {
		log.trace("In addPerson method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		return personService.addPerson(person);
	}	
	
	public Person importPerson(String sessionKey, Person person) throws ApplicationException {
		log.trace("In importPerson method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		return personService.importPerson(person);
	}	
	
	public void updatePerson(String sessionKey, Person person) throws ApplicationException {
		log.trace("In updatePerson method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		personService.updatePerson(person);
	}
	
	public void deletePerson(String sessionKey, PersonIdentifier personIdentifier) throws ApplicationException {
		log.trace("In deletePerson method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		personService.deletePerson(personIdentifier);
	}
	
	public void mergePersons(String sessionKey, PersonIdentifier retiredIdentifier, PersonIdentifier survivingIdentifier) throws ApplicationException {
		log.trace("In mergePersons method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		personService.mergePersons(retiredIdentifier, survivingIdentifier);
	}
	
	public Person getPerson(String sessionKey, List<PersonIdentifier> personIdentifiers) {
		log.trace("In getPerson method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		return personService.getPerson(personIdentifiers);
	}
	public IdentifierDomain obtainUniqueIdentifierDomain(String sessionKey, String universalIdentifierTypeCode) {
		log.trace("In obtainUniqueIdentifierDomain method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		return personService.obtainUniqueIdentifierDomain(universalIdentifierTypeCode);
	}
	
	public IdentifierDomainAttribute addIdentifierDomainAttribute(String sessionKey, IdentifierDomain identifierDomain, String attributeName, String attributeValue) {
		log.trace("In addIdentifierDomainAttribute method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		return personService.addIdentifierDomainAttribute(identifierDomain, attributeName, attributeValue);
	}
	
	public void updateIdentifierDomainAttribute(String sessionKey, IdentifierDomainAttribute identifierDomainAttribute) {
		log.trace("In updateIdentifierDomainAttribute method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		personService.updateIdentifierDomainAttribute(identifierDomainAttribute);
	}

	public void removeIdentifierDomainAttribute(String sessionKey, IdentifierDomainAttribute identifierDomainAttribute) {	
		log.trace("In removeIdentifierDomainAttribute method.");
		Context.authenticate(sessionKey);
		org.openhie.openempi.service.PersonManagerService personService = Context.getPersonManagerService();
		personService.removeIdentifierDomainAttribute(identifierDomainAttribute);
	}
}