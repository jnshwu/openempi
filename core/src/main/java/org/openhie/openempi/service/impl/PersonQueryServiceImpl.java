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
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.configuration.GlobalIdentifier;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.MatchingService;
import org.openhie.openempi.model.Criteria;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.LoggedLink;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.Race;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.model.ReviewRecordPair;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.ValidationService;
import org.openhie.openempi.util.ConvertUtil;

public class PersonQueryServiceImpl extends PersonCommonServiceImpl implements PersonQueryService
{
	public Person findPersonById(PersonIdentifier personIdentifier) {

		ValidationService validationService = Context.getValidationService();
		validationService.validate(personIdentifier);

		return personDao.getPersonById(personIdentifier);
	}

	public PersonIdentifier getGlobalIdentifierById(PersonIdentifier personIdentifier) {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(personIdentifier);

		GlobalIdentifier globalIdentifier = Context.getConfiguration().getGlobalIdentifier();
		if (!globalIdentifier.isAssignGlobalIdentifier()) {
			log.warn("The system is not configured to assign global identifiers.");
			return null;
		}
		
		IdentifierDomain globalDomain = globalIdentifier.getIdentifierDomain();
		// Check to see if the person already has a global identifier
		Person person = personDao.getPersonById(personIdentifier);
		if (person != null) {
			for (PersonIdentifier identifier : person.getPersonIdentifiers()) {
				IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
				if (identifierDomain != null && identifierDomain.equals(globalDomain)) {
					if (log.isTraceEnabled()) {
						log.trace("Person has a global identifier assigned: " + identifier);
					}
					return identifier;
				}
			}
		}
		return null;
	}
	
	public Person loadPerson(Integer personId) {
		if (personId == null || personId.intValue() == 0) {
			return null;
		}
		Person foundPerson = personDao.loadPerson(personId);
		return foundPerson;
	}
	
	public List<Person> loadPersons(List<Integer> personIds) {
		if (personIds == null || personIds.size() == 0) {
			return null;
		}
		List<Person> persons = personDao.loadPersons(personIds);
		return persons;
	}
	
	public List<PersonLink> getPersonLinks(Person person) {
		List<PersonLink> links = personLinkDao.getPersonLinks(person);		
		return links;
	}
	
	
	public List<PersonLink> getPersonLinksByLinkSource(Integer linkSourceId) {
		if (linkSourceId == null) {
			return new java.util.ArrayList<PersonLink>();
		}
		return personLinkDao.getPersonLinksByLinkSource(linkSourceId);
	}

	public List<Person> loadAllPersonsPaged(int firstRecord, int maxRecords) {
			return findPersonsByAttributesPaged(new Person(), firstRecord, maxRecords);
	}

	public List<ReviewRecordPair> loadAllUnreviewedPersonLinks() {
		return personLinkDao.getAllUnreviewedReviewRecordPairs();
	}

	public List<ReviewRecordPair> loadUnreviewedPersonLinks(int maxResults) {
		return personLinkDao.getUnreviewedReviewRecordPairs(maxResults);
	}
	
	public ReviewRecordPair loadReviewRecordPair(int reviewRecordPairId) {
		return personLinkDao.getReviewRecordPair(reviewRecordPairId);
	}
	
	public ReviewRecordPair getLoggedLink(Integer loggedLinkId) {
		if (loggedLinkId == null) {
			return null;
		}
		LoggedLink link = personLinkDao.getLoggedLink(loggedLinkId);
		ReviewRecordPair pair = buildReviewRecordPairFromLoggedLink(link);
		Person left = personDao.loadPerson(link.getLeftRecordId().intValue());
		Person right = personDao.loadPerson(link.getRightRecordId().intValue());
		pair.setPersonLeft(left);
		pair.setPersonRight(right);
		return pair;
	}
	
	public int getLoggedLinksCount(int vectorValue) {
		return personLinkDao.getLoggedLinksCount(vectorValue);
	}
	
	public List<LoggedLink> getLoggedLinks(final int vectorValue, final int start, final int maxResults) {
		return personLinkDao.getLoggedLinks(vectorValue, start, maxResults);
	}
	
	public List<Person> findPersonsById(PersonIdentifier personIdentifier) {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(personIdentifier);
		
		List<Person> persons = personDao.getPersonsByIdentifier(personIdentifier);
		return persons;
	}

	public List<Person> findLinkedPersons(PersonIdentifier identifier) {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifier);
		
		List<Person> linkedPersons = new ArrayList<Person>();
		Person person = personDao.getPersonById(identifier);
		if (person == null) {
			return linkedPersons;
		}
		List<PersonLink> links = personLinkDao.getPersonLinks(person);
		for (PersonLink link : links) {
			if (person.getPersonId().equals(link.getPersonLeft().getPersonId())) {
				linkedPersons.add(link.getPersonRight());
			} else {
				linkedPersons.add(link.getPersonLeft());
			}
		}
		return linkedPersons;
	}

	public List<Person> findLinkedPersons(Person person) {		
		List<Person> linkedPersons = new ArrayList<Person>();
		if (person == null) {
			return linkedPersons;
		}
		List<PersonLink> links = personLinkDao.getPersonLinks(person);
		for (PersonLink link : links) {
			if (person.getPersonId().equals(link.getPersonLeft().getPersonId())) {
				linkedPersons.add(link.getPersonRight());
			} else {
				linkedPersons.add(link.getPersonLeft());
			}
		}
		return linkedPersons;
	}
	
	public List<Person> findPersonsByAttributes(Person person) {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);
		
		Record record = new Record(person);
		Criteria criteria = ConvertUtil.buildCriteriaFromProperties(record);
		ConvertUtil.addIndirectCriteria(person, criteria);
		List<Person> persons;
		if (person.getPersonIdentifiers() != null && person.getPersonIdentifiers().size() > 0) {
			persons = personDao.getPersons(criteria, person.getPersonIdentifiers());
		} else {
			persons = personDao.getPersons(criteria);
		}
		return persons;
	}
	
	public List<Person> getSingleBestRecords(List<Integer> personIds) {
		if (personIds == null || Context.getSingleBestRecordService() == null) {
			return new ArrayList<Person>();
		}
		List<Person> persons = new ArrayList<Person>(personIds.size());
		for (Integer personId : personIds) {
			Person person = getSingleBestRecord(personId);
			persons.add(person);
		}
		return persons;
	}
	
	public Person getSingleBestRecord(Integer personId) {
		// First load the source record
		Person person = personDao.loadPerson(personId);
		if (person == null || Context.getSingleBestRecordService() == null) {
			// If we can't find the person, then return a blank record
			return new Person();
		}
		List<Person> linkedPersons = findLinkedPersons(person);
		if (linkedPersons == null || linkedPersons.size() == 0) {
			return person;
		}
			
		// Add the person itself to the list to complete the cluster
		linkedPersons.add(person);
		List<Record> records = new java.util.ArrayList<Record>(linkedPersons.size());
		for (Person aPerson : linkedPersons) {
			records.add(ConvertUtil.getRecordFromPerson(aPerson));
		}
		
		Record filteredRecord = Context.getSingleBestRecordService().getSingleBestRecord(records);
		Person filteredPerson = personDao.loadPersonComplete(filteredRecord.getRecordId().intValue());
		return filteredPerson;
	}
	
	public List<Person> findMatchingPersonsByAttributes(Person person) {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);
		
		populateCustomFields(person);
		Record record = ConvertUtil.getRecordFromPerson(person);
		MatchingService matchingService = Context.getMatchingService();
		List<Person> persons = new java.util.ArrayList<Person>();
		try {
			Set<RecordPair> links = matchingService.match(record);
			for (RecordPair recordPair : links) {
				Person personLinked = (Person) recordPair.getRightRecord().getObject();
				persons.add(personDao.loadPerson(personLinked.getPersonId()));
			}
		} catch (ApplicationException e) {
			log.warn("While searching for matching persons, the system encountered an issue: " + e, e);
		}
		return persons;
	}

	public IdentifierDomain findIdentifierDomain(IdentifierDomain identifierDomain) {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomain);
		
		if (identifierDomain == null ||
				(identifierDomain.getIdentifierDomainId() == null &&
				 identifierDomain.getNamespaceIdentifier() == null &&
				(identifierDomain.getUniversalIdentifier() == null || identifierDomain.getUniversalIdentifierTypeCode() == null))) {
			return null;
		}
		return super.findIdentifierDomain(identifierDomain);
	}
	
	public IdentifierDomain findIdentifierDomainByName(String identifierDomainName) {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomainName);
		
		if (identifierDomainName == null ||
				identifierDomainName.length() == 0) {
			return null;
		}
		return personDao.findIdentifierDomainByName(identifierDomainName);
	}
	
	public List<String> getPersonModelAttributeNames() {
		return getModelAttributeNames(false);
	}
	
	public List<String> getPersonModelAllAttributeNames() {
		Record record = new Record(new Person());
		return ConvertUtil.getAllModelAttributeNames(record);
	}
	
	public List<String> getPersonModelCustomAttributeNames() {
		return getModelAttributeNames(true);
	}
	
	private List<String> getModelAttributeNames(boolean needCustomFields) {
		Record record = new Record(new Person());
		return ConvertUtil.getModelAttributeNames(record, needCustomFields);
	}
	
	public List<Person> findPersonsByAttributesPaged(Person person, int firstResult, int maxResults) {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);
		
		Record record = new Record(person);
		Criteria criteria = ConvertUtil.buildCriteriaFromProperties(record);
		ConvertUtil.addIndirectCriteria(person, criteria);
		List<Person> persons;
		if (person.getPersonIdentifiers() != null && person.getPersonIdentifiers().size() > 0) {
			persons = personDao.getPersonsPaged(criteria, person.getPersonIdentifiers(), firstResult,
					maxResults, true);
		} else {
			persons = personDao.getPersonsPaged(criteria, firstResult, maxResults);
		}
		return persons;
	}
	


	public Gender findGenderByCode(String genderCode) {
		return personDao.findGenderByCode(genderCode);
	}

	public Gender findGenderByName(String genderName) {
		return personDao.findGenderByName(genderName);
	}

	public Race findRaceByCode(String raceCode) {
		return personDao.findRaceByCode(raceCode);
	}

	public Race findRaceByName(String raceName) {
		return personDao.findRaceByName(raceName);
	}

	/*
	private void hydrateObjects(List<Person> linkedPersons) {
		// This is used for materializing the hibernate proxies before returning the collection
		for (Person p : linkedPersons) {
			Hibernate.initialize(p);
			for (PersonIdentifier identifier : p.getPersonIdentifiers()) {
				identifier.toString();
				identifier.getIdentifierDomain().toString();
			}
		}
	}*/

	public IdentifierDomainAttribute getIdentifierDomainAttribute(IdentifierDomain identifierDomain,
			String attributeName) {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomain);
		
		return personDao.getIdentifierDomainAttribute(identifierDomain, attributeName);
	}

	public List<IdentifierDomainAttribute> getIdentifierDomainAttributes(IdentifierDomain identifierDomain) {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomain);
		
		return personDao.getIdentifierDomainAttributes(identifierDomain);
	}

	private ReviewRecordPair buildReviewRecordPairFromLoggedLink(LoggedLink link) {
		ReviewRecordPair pair = new ReviewRecordPair();
		pair.setReviewRecordPairId(link.getLinkId());
		pair.setDateCreated(link.getDateCreated());
		pair.setUserCreatedBy(link.getUserCreatedBy());
		pair.setWeight(link.getWeight());
		return pair;
	}

	public List<String> getIdentifierDomainTypeCodes() {
		return personDao.getIdentifierDomainTypeCodes();
	}

	public List<IdentifierDomain> getIdentifierDomains() {
		return personDao.getIdentifierDomains();
	}

	 public int getNotificationCount(User user){
		int count =  personDao.getIdentifierUpdateEventCount(user);
		 return count;
	  }

	 public List<IdentifierUpdateEvent> retrieveNotifications(int startIndex, int maxEvents, Boolean removeRecords, User eventRecipient) {

		 List<IdentifierUpdateEvent> identifierUpdateEvents = personDao.getIdentifierUpdateEvents(startIndex, maxEvents, eventRecipient);

		 if (removeRecords) {
			for (IdentifierUpdateEvent identifierUpdateEvent : identifierUpdateEvents) {
				log.trace("Deleting IdentifierUpdateEvent : " + identifierUpdateEvent.getIdentifierUpdateEventId());
				personDao.removeIdentifierUpdateEvent(identifierUpdateEvent);
			}
		 }

		 return identifierUpdateEvents;
	 }

	 public List<IdentifierUpdateEvent> retrieveNotifications(Boolean removeRecords, User eventRecipient) {
		 List<IdentifierUpdateEvent> identifierUpdateEvents = personDao.getIdentifierUpdateEvents(eventRecipient);

		 if (removeRecords) {
			for (IdentifierUpdateEvent identifierUpdateEvent : identifierUpdateEvents) {
				log.trace("Deleting IdentifierUpdateEvent : " + identifierUpdateEvent.getIdentifierUpdateEventId());
				personDao.removeIdentifierUpdateEvent(identifierUpdateEvent);
			}
		 }

		 return identifierUpdateEvents;
	 }

	 public List<IdentifierUpdateEvent> retrieveNotificationsByDate(Date startDate, Boolean removeRecords, User eventRecipient) {
		 List<IdentifierUpdateEvent> identifierUpdateEvents = personDao.getIdentifierUpdateEventsByDate(startDate, eventRecipient);

		 if (removeRecords) {
			for (IdentifierUpdateEvent identifierUpdateEvent : identifierUpdateEvents) {
				log.trace("Deleting IdentifierUpdateEvent : " + identifierUpdateEvent.getIdentifierUpdateEventId());
				personDao.removeIdentifierUpdateEvent(identifierUpdateEvent);
			}
		 }

		 return identifierUpdateEvents;
	 }

	 public IdentifierUpdateEvent findIdentifierUpdateEvent(long identifierUpdateEventId) {
	     return personDao.findIdentifierUpdateEvent(identifierUpdateEventId);
	 }
}
