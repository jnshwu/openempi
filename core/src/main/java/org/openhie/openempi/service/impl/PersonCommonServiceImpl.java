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
package org.openhie.openempi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.openhie.openempi.configuration.CustomField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.dao.PersonLinkDao;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.transformation.TransformationService;

public class PersonCommonServiceImpl extends BaseServiceImpl 
{
	protected PersonDao personDao;
	protected PersonLinkDao personLinkDao;
	
	public List<Person> findLinkedPersons(Person person) {
		List<Person> linkedPersons = new ArrayList<Person>();
		if (person == null || person.getPersonId() == null) {
			return linkedPersons;
		}
		List<PersonLink> links = personLinkDao.getPersonLinks(person);
		for (PersonLink link : links) {
			loadPersons(link);
			if (person.getPersonId().equals(link.getPersonLeft().getPersonId())) {
				linkedPersons.add(link.getPersonRight());
			} else {
				linkedPersons.add(link.getPersonLeft());
			}
		}
		return linkedPersons;
	}
	
	public Set<Person> loadLinkedPersons(List<PersonLink> personLinks) {
		Set<Person> linkedPersons = new java.util.HashSet<Person>();
		if (personLinks == null || personLinks.size() == 0) {
			return linkedPersons;
		}
		for (PersonLink link : personLinks) {
			loadPersons(link);
			linkedPersons.add(link.getPersonLeft());
			linkedPersons.add(link.getPersonRight());
		}
		return linkedPersons;
	}

	public void populateCustomFields(Person person) {
		List<CustomField> customFields = Context.getConfiguration().getCustomFields();
		TransformationService transformationService = Context.getTransformationService();
		for (CustomField customField : customFields) {
			log.trace("Need to generate a value for field " + customField.getSourceFieldName() + " using function " +
					customField.getTransformationFunctionName() + " and save it as field " + customField.getFieldName());
			try {
				Object value = PropertyUtils.getProperty(person, customField.getSourceFieldName());
				log.debug("Obtained a value of " + value + " for field " + customField.getSourceFieldName());
				if (value != null) {
					Object transformedValue = transformationService.transform(customField.getTransformationFunctionName(), value, customField.getConfigurationParameters());
					PropertyUtils.setProperty(person, customField.getFieldName(), transformedValue);
					log.debug("Custom field " + customField.getFieldName() + " has value " + BeanUtils.getProperty(person,
							customField.getFieldName()));
				}
			} catch (Exception e) {
				log.error("Failed while trying to obtain property for field " + customField.getSourceFieldName() + ":" + e.getMessage(), e);
			}
		}
	}

	protected IdentifierDomain findIdentifierDomain(IdentifierDomain identifierDomain) {
		IdentifierDomain idFound = personDao.findIdentifierDomain(identifierDomain);
		return idFound;
	}
	
	private void loadPersons(PersonLink link) {
		Person leftPerson = personDao.loadPerson(link.getPersonLeft().getPersonId());
		link.setPersonLeft(leftPerson);
		Person rightPerson = personDao.loadPerson(link.getPersonRight().getPersonId());
		link.setPersonRight(rightPerson);
	}
	
	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public PersonLinkDao getPersonLinkDao() {
		return personLinkDao;
	}

	public void setPersonLinkDao(PersonLinkDao personLinkDao) {
		this.personLinkDao = personLinkDao;
	}
}
