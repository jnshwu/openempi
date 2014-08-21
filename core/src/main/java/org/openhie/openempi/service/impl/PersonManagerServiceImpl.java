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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.ValidationException;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.configuration.GlobalIdentifier;
import org.openhie.openempi.configuration.UpdateNotificationRegistrationEntry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.context.Context.ObservationEventType;
import org.openhie.openempi.matching.MatchingService;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.LinkSource;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.model.ReviewRecordPair;
import org.openhie.openempi.model.User;
import org.openhie.openempi.notification.EventType;
import org.openhie.openempi.notification.NotificationEvent;
import org.openhie.openempi.notification.NotificationEventFactory;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.ValidationService;
import org.openhie.openempi.util.ConvertUtil;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.eaio.uuid.UUID;

public class PersonManagerServiceImpl extends PersonCommonServiceImpl implements PersonManagerService
{
	private static final int TRANSACTION_BLOCK_SIZE = 10000;
	private TransactionTemplate transactionTemplate;
	private UpdateEventNotificationGenerator notificationGenerator;
	
    private  List<UpdateNotificationRegistrationEntry> updateNotificationEntries = new java.util.ArrayList<UpdateNotificationRegistrationEntry>();
	
	/**
	 * Add a new person to the system.
	 * 1. First it attempts to locate the person in the system using the persons identifiers. If the person is already in
	 * the system then there is nothing more to do.
	 * 2. Since the person doesn't exist in the system yet, a new record is added.
	 * 3. The matching algorithm is invoked to identify matches and association links are established between the existing
	 * patient and other aliases of the patient that were identified based on the algorithm.
	 */
	public Person addPerson(Person person) throws ApplicationException {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);
		
		Person personFound = findPersonUsingIdentifiers(person);
		if (personFound != null) {
			log.warn("While attempting to add a person, found an existing record with same identifier: " + person);
			throw new ApplicationException("Person record to be added already exists in the system.");
		}

		validateNoNewGlobalIdentifier(person, personFound);
		
		// Before we save the entry we need to generate any custom
		// fields that have been requested through configuration
		populateCustomFields(person);

		savePerson(person);

		PersonState personState = new PersonState(person.getPersonId(), IdentifierUpdateEvent.ADD_SOURCE);
		// Now we need to check for matches and if any are found, establish links among the aliases
		findAndProcessAddRecordLinks(person, personState);
		getUpdateEventNotificationGenerator().generateEvents(personState);
		
		// Audit the event that a new person entry was created.
		Context.getAuditEventService().saveAuditEvent(AuditEventType.ADD_PERSON_EVENT_TYPE, "Added a new person record", person);
		
		// Generate a notification event to inform interested listeners that this event has occurred.
		NotificationEvent event = NotificationEventFactory.createNotificationEvent(EventType.ADD_EVENT_TYPE, person);
		Context.getNotificationService().fireNotificationEvent(event);
		
		// Generate a notification event to inform interested listeners via the lightweight mechanism that this event has occurred.
		Context.notifyObserver(ObservationEventType.ENTITY_ADD_EVENT, ConvertUtil.getRecordFromPerson(person));

		return person;
	}

	public void deletePerson(PersonIdentifier personIdentifier) throws ApplicationException {

		ValidationService validationService = Context.getValidationService();
		validationService.validate(personIdentifier);
		
		Person personFound = personDao.getPersonById(personIdentifier);
		if (personFound == null) {
			log.warn("While attempting to delete a person was not able to locate a record with the given identifier: " + personIdentifier);
			throw new ApplicationException("Person record to be deleted does not exist in the system.");
		}
		
		PersonState state = capturePreIdentifiers(IdentifierUpdateEvent.DELETE_SOURCE, personFound);
		findAndDeleteRecordLinks(personFound);
		deletePerson(personFound);
		getUpdateEventNotificationGenerator().generateEvents(state);

		Context.getAuditEventService().saveAuditEvent(AuditEventType.DELETE_PERSON_EVENT_TYPE, "Deleted a person record", personFound);
		
		// Generate a notification event to inform interested listeners that this event has occurred.
		NotificationEvent event = NotificationEventFactory.createNotificationEvent(EventType.DELETE_EVENT_TYPE, personFound);
		Context.getNotificationService().fireNotificationEvent(event);
		
		// Generate a notification event to inform interested listeners via the lightweight mechanism that this event has occurred.
		Context.notifyObserver(ObservationEventType.ENTITY_DELETE_EVENT, ConvertUtil.getRecordFromPerson(personFound));
	}

	public void deletePersonById(Person person) throws ApplicationException {

		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);

		if (person == null || person.getPersonId() == null) {
			log.warn("The delete operation requires that the caller provides the internal unique identifier for the person record: " + person);
			throw new ApplicationException("The delete operation requires that the caller specifies the internal unique identifier for the record.");
		}
		
		Person personFound = findPersonUsingId(person);
		if (personFound == null) {
			log.warn("While attempting to delete a person was not able to locate a record with the given internal identifier: " + person.getPersonId());
			throw new ApplicationException("Person record to be deleted does not exist in the system.");
		}		
		
		findAndDeleteRecordLinks(personFound);
		deletePerson(personFound);
		
		Context.getAuditEventService().saveAuditEvent(AuditEventType.DELETE_PERSON_EVENT_TYPE, "Deleted a person record", personFound);
		
		// Generate a notification event to inform interested listeners that this event has occurred.
		NotificationEvent event = NotificationEventFactory.createNotificationEvent(EventType.DELETE_EVENT_TYPE, personFound);
		Context.getNotificationService().fireNotificationEvent(event);
		
		// Generate a notification event to inform interested listeners via the lightweight mechanism that this event has occurred.
		Context.notifyObserver(ObservationEventType.ENTITY_DELETE_EVENT, ConvertUtil.getRecordFromPerson(personFound));
	}
	
	public PersonLink linkPersons(PersonLink personLink) throws ApplicationException {
		// Validate the input
		if (personLink == null || 
				personLink.getPersonLeft() == null || personLink.getPersonLeft().getPersonId() == null ||
				personLink.getPersonRight() == null || personLink.getPersonRight().getPersonId() == null) {
			log.warn("The link persons operation requires that the caller provides the unique identifiers " +
					"for the two person objects to be linked: " + personLink);
			throw new ApplicationException("The link persons operation requires that the caller provides " +
					"the unique identifiers for the two person objects to be linked.");
		}
		Person leftPerson = personDao.loadPerson(personLink.getPersonLeft().getPersonId());
		Person rightPerson = personDao.loadPerson(personLink.getPersonRight().getPersonId());
		if (leftPerson == null || rightPerson == null) {
			log.warn("The link persons operation requires that the caller provides unique identifiers " +
					" for the two valid records to be linked: " + personLink);
			throw new ApplicationException("The link persons operation requires that the caller provides unique identifiers " +
					" for the two valid records to be linked.");
		}

		RecordPair pair = new RecordPair(buildRecord(personLink.getPersonLeft()), buildRecord(personLink.getPersonRight()));
		if (personLink.getLinkSource() == null) {
			pair.setLinkSource(new LinkSource(LinkSource.MANUAL_MATCHING_SOURCE));
		} else {
			pair.setLinkSource(personLink.getLinkSource());
		}
		if (personLink.getWeight() == null) {
			pair.setWeight(1.0D);
		} else {
			pair.setWeight(personLink.getWeight());
		}
		List<PersonLink> links = new java.util.ArrayList<PersonLink>();
		linkNodesInPair(pair, null, null, links);
		personLinkDao.addPersonLinks(links);
		
		Context.getAuditEventService().saveAuditEvent(AuditEventType.LINK_PERSONS_EVENT_TYPE, "Linked two person records",
				leftPerson, rightPerson);

		return personLinkDao.getPersonLink(leftPerson, rightPerson);
	}

	private Record buildRecord(Person person) {
		Record record = new Record(person);
		record.setRecordId(person.getPersonId().longValue());
		return record;
	}
		
	public void unlinkPersons(PersonLink personLink) throws ApplicationException {
		if (personLink == null ||
				personLink.getPersonLeft() == null || personLink.getPersonLeft().getPersonId() == null ||
				personLink.getPersonRight() == null || personLink.getPersonRight().getPersonId() == null) {
			log.warn("The unlink persons operation requires that the caller provides the unique identifiers " +
					"for the two person objects to be unlinked: " + personLink);
			throw new ApplicationException("The unlink persons operation requires that the caller provides " +
					"the unique identifiers for the two person objects to be unlinked.");
		}
		Person leftPerson = personDao.loadPerson(personLink.getPersonLeft().getPersonId());
		Person rightPerson = personDao.loadPerson(personLink.getPersonRight().getPersonId());
		if (leftPerson == null || rightPerson == null) {
			log.warn("The unlink persons operation requires that the caller provides unique identifiers " +
					" for the two valid records to be unlinked: " + personLink);
			throw new ApplicationException("The unlink persons operation requires that the caller provides unique identifiers " +
					" for the two valid records to be unlinked.");
		}

		if (Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier()) {
			PersonState state = capturePreIdentifiers(IdentifierUpdateEvent.UNLINK_SOURCE, rightPerson);
			removeGlobalIdentifier(rightPerson, Context.getConfiguration().getGlobalIdentifierDomain());
			generateGlobalIdentifier(Context.getConfiguration().getGlobalIdentifierDomain(), rightPerson);
			capturePostIdentifiers(rightPerson, state);
			getUpdateEventNotificationGenerator().generateEvents(state);
			personDao.updatePerson(rightPerson);
		}
		
		List<PersonLink> links = personLinkDao.getPersonLinks(rightPerson);
		if (links.size() == 0) {
			log.warn("No links involving person " + rightPerson.getPersonId() + " present.");
			return;
		}
		for (PersonLink link : links) {
			log.debug("Removing link " + link + " associated with person " + rightPerson.getPersonId());
			personLinkDao.removeLink(link);
		}
		
		Context.getAuditEventService().saveAuditEvent(AuditEventType.UNLINK_PERSONS_EVENT_TYPE, "Unlinked two person records",
				leftPerson, rightPerson);
	}
	
	public void mergePersons(PersonIdentifier retiredIdentifier, PersonIdentifier survivingIdentifier) throws ApplicationException {

		ValidationService validationService = Context.getValidationService();
		validationService.validate(retiredIdentifier);
		validationService.validate(survivingIdentifier);
		
		Person personSurviving = personDao.getPersonById(survivingIdentifier);
		if (personSurviving == null) {
			log.warn("While attempting to merge two persons was not able to locate a record with the given identifier: " + survivingIdentifier);
			throw new ApplicationException("Person record to be the survivor of a merge does not exist in the system.");
		}
		
		Person personRetiring = personDao.getPersonById(retiredIdentifier);
		if (personRetiring == null) {
			log.warn("While attempting to merge two persons was not able to locate a record with the given identifier: " + retiredIdentifier);
			throw new ApplicationException("Person record to be deleted as part of a merge does not exist in the system.");
		}
		
		List<PersonLink> links = personLinkDao.getPersonLinks(personRetiring);
		for (PersonLink originalLink : links) {
			Person otherPerson = getOtherPerson(originalLink, personRetiring);
			personLinkDao.addPersonLink(getPersonLinkFromRecordPair(personSurviving, otherPerson, originalLink));
			personLinkDao.remove(PersonLink.class, originalLink.getPersonLinkId());
		}
		// Delete the retired person record
		deletePerson(personRetiring);
		
		Context.getAuditEventService().saveAuditEvent(AuditEventType.MERGE_PERSON_EVENT_TYPE, "Merged two person records", personSurviving, personRetiring);
		
		// Generate a notification event to inform interested listeners that this event has occurred.
		NotificationEvent event = NotificationEventFactory.createNotificationEvent(EventType.MERGE_EVENT_TYPE, 
				new Person[] {personSurviving, personRetiring});
		Context.getNotificationService().fireNotificationEvent(event);
	}

	public void updatePerson(Person person) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);

		if (person.getDateChanged() == null) {
			log.warn("The update operation requires that the caller specifies the time the record was last retrieved in field date changed.");
			throw new ApplicationException("The update operation requires that the caller specifies the time the record was last retrieved in field date changed.");
		}
				
		Person personFound = findPersonUsingIdentifiers(person);
		Person personOriginal = (Person) ConvertUtil.cloneBean(personFound);
		log.debug("Updated object is " + personOriginal);
		if (personFound == null) {
			log.warn("While attempting to update a person was 		PersonState state = capturePreIdentifiers(personFound);not able to locate a record with the given identifier: " + person);
			throw new ApplicationException("Person record to be updated does not exist in the system.");
		}
		
		if (personFound.getDateChanged().getTime() > person.getDateChanged().getTime()) {
			log.warn("The update record is stale as compared to the state of the record in the system: " + person);
			throw new ApplicationException("Person record has changed in the system.");
		}

		validateNoNewGlobalIdentifier(person, personFound);
		
		// Before we save the entry we need to generate any custom
		// fields that have been requested through configuration
		populateCustomFields(person);
		
		PersonState state = capturePreIdentifiers(IdentifierUpdateEvent.UPDATE_SOURCE, personFound);
		updatePerson(person, personFound);
		
		findAndUpdateRecordLinks(person, state);
		getUpdateEventNotificationGenerator().generateEvents(state);

		Context.getAuditEventService().saveAuditEvent(AuditEventType.UPDATE_PERSON_EVENT_TYPE, "Updated an existing person record", person);
		
		// Generate a notification event to inform interested listeners that this event has occurred.
		NotificationEvent event = NotificationEventFactory.createNotificationEvent(EventType.UPDATE_EVENT_TYPE, person);
		Context.getNotificationService().fireNotificationEvent(event);
		
		// Generate a notification event to inform interested listeners via the lightweight mechanism that this event has occurred.
		Record recordFound = ConvertUtil.getRecordFromPerson(personFound);
		Record recordUpdated = ConvertUtil.getRecordFromPerson(person);
		Context.notifyObserver(ObservationEventType.ENTITY_UPDATE_EVENT, new Object[]{recordFound, recordUpdated});		
	}

	private PersonState capturePreIdentifiers(String source, Person person) {
		PersonState state = new PersonState(person.getPersonId(), source);
		Set<PersonIdentifier> preUpdateIdentifiers = copyIdentifiers(person);
		state.setPreIdentifiers(preUpdateIdentifiers);
		return state;
	}

	private PersonState capturePostIdentifiers(Person person, PersonState state) {
		Set<PersonIdentifier> postIdentifiers = copyIdentifiers(person);
		state.setPostIdentifiers(postIdentifiers);
		return state;
	}
	
	private Set<PersonIdentifier> copyIdentifiers(Person person) {
		Set<PersonIdentifier> ids = new HashSet<PersonIdentifier>();
		for (PersonIdentifier id : person.getPersonIdentifiers()) {
			if (id.getDateVoided() != null) {
				continue;
			}
			PersonIdentifier newId = new PersonIdentifier();
			newId.setIdentifier(id.getIdentifier());
			newId.setIdentifierDomain(id.getIdentifierDomain());
			ids.add(newId);
		}
		return ids;
	}

	public Person updatePersonById(Person person) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);

		if (person == null || person.getPersonId() == null) {
			log.warn("The update operation requires that the caller provides the internal unique identifier for the person record: " + person);
			throw new ApplicationException("The update operation requires that the caller specifies the internal unique identifier for the record.");
		}
		
		if (person.getDateChanged() == null) {
			log.warn("The update operation requires that the caller specifies the time the record was last retrieved in field date changed.");
			throw new ApplicationException("The update operation requires that the caller specifies the time the record was last retrieved in field date changed.");
		}

		Person personFound = findPersonUsingId(person);
		if (personFound == null) {
			log.warn("While attempting to update a person was not able to locate the record to be updated: " + person);
			throw new ApplicationException("Person record to be updated does not exist in the system.");
		}
		
		if (personFound.getDateChanged().getTime() > person.getDateChanged().getTime()) {
			log.warn("The update record is stale as compared to the state of the record in the system: " + person);
			throw new ApplicationException("Person record has changed in the system.");
		}
				
		// Before we save the entry we need to generate any custom fields that have been requested through configuration
		populateCustomFields(person);

		PersonState state = capturePreIdentifiers(IdentifierUpdateEvent.UPDATE_SOURCE, personFound);
		updatePerson(person, personFound);
		
		findAndUpdateRecordLinks(person, state);
		getUpdateEventNotificationGenerator().generateEvents(state);

		Context.getAuditEventService().saveAuditEvent(AuditEventType.UPDATE_PERSON_EVENT_TYPE, "Updated an existing person record", person);
		
		// Generate a notification event to inform interested listeners that this event has occurred.
		NotificationEvent event = NotificationEventFactory.createNotificationEvent(EventType.UPDATE_EVENT_TYPE, person);
		Context.getNotificationService().fireNotificationEvent(event);
		
		// Generate a notification event to inform interested listeners via the lightweight mechanism that this event has occurred.
		Record recordFound = ConvertUtil.getRecordFromPerson(personFound);
		Record recordUpdated = ConvertUtil.getRecordFromPerson(person);
		Context.notifyObserver(ObservationEventType.ENTITY_UPDATE_EVENT,  new Object[]{recordFound, recordUpdated});	
		
		return person;
	}
	
	public IdentifierDomain addIdentifierDomain(IdentifierDomain identifierDomain) throws ApplicationException {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomain);
		
		if (identifierDomain == null || 
				(identifierDomain.getNamespaceIdentifier() == null &&
				(identifierDomain.getUniversalIdentifier() == null || identifierDomain.getUniversalIdentifierTypeCode() == null))) {
			log.warn("Attempted to add an identifier domain with insufficient attributes: " + identifierDomain);
			throw new ApplicationException("The identifier domain to be added is invalid.");
		}
		
		IdentifierDomain idFound = findIdentifierDomain(identifierDomain);
		if (idFound != null) {
			log.warn("While attempting to add an identifier domain, found an existing record in the repository: " + idFound);
			throw new ApplicationException("Identifier domain record to be added already exists in the system.");
		}
		saveIdentifierDomain(identifierDomain);
		return identifierDomain;
	}

	public void deleteIdentifierDomain(IdentifierDomain identifierDomain) throws ApplicationException {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomain);
		
		if (identifierDomain == null ||
				(identifierDomain.getIdentifierDomainId() == null &&
				 identifierDomain.getNamespaceIdentifier() == null &&
				(identifierDomain.getUniversalIdentifier() == null || identifierDomain.getUniversalIdentifierTypeCode() == null))) {
			log.warn("Attempted to delete an identifier domain with insufficient attributes: " + identifierDomain);
			throw new ApplicationException("The identifier domain to be deleted is invalid.");
		}

		IdentifierDomain idFound = findIdentifierDomain(identifierDomain);
		if (idFound == null) {
			return;
		}
		try {
			personDao.removeIdentifierDomain(idFound);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			log.warn("Attempted to delete an identifier domain that is still referenced by identifiers: " + e, e);
			throw new ApplicationException("Cannot delete identifier domain as it is still in use.");
		}
	}
	
	public IdentifierDomain updateIdentifierDomain(IdentifierDomain identifierDomain) throws ApplicationException {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomain);
		
		if (identifierDomain == null || identifierDomain.getIdentifierDomainId() == null) {
			log.warn("Attempted to update an identifier domain with insufficient attributes: " + identifierDomain);
			throw new ApplicationException("The identifier domain to be updated is invalid.");
		}
		
		if (identifierDomain.getNamespaceIdentifier() == null &&
			(identifierDomain.getUniversalIdentifier() == null || identifierDomain.getUniversalIdentifierTypeCode() == null)) {
			log.warn("Attempted to update an identifier domain with insufficient attributes: " + identifierDomain);
			throw new ApplicationException("The identifier domain to be update is invalid.");
		}
		
		IdentifierDomain idFound = findIdentifierDomain(identifierDomain);
		if (idFound != null && idFound.getIdentifierDomainId() != identifierDomain.getIdentifierDomainId()) {
			log.warn("While attempting to update an identifier domain, found an existing record in the repository with same identifiying attributes: " + idFound);
			throw new ApplicationException("Identifier domain record cannot be updated to match another entry in the repository.");
		}
		idFound.setIdentifierDomainDescription(identifierDomain.getIdentifierDomainDescription());
		idFound.setIdentifierDomainName(identifierDomain.getIdentifierDomainName());
		idFound.setNamespaceIdentifier(identifierDomain.getNamespaceIdentifier());
		idFound.setUniversalIdentifier(identifierDomain.getUniversalIdentifier());
		idFound.setUniversalIdentifierTypeCode(identifierDomain.getUniversalIdentifierTypeCode());
		saveIdentifierDomain(idFound);
		return idFound;
	}
	
	public Person getPerson(List<PersonIdentifier> personIdentifiers) {
		ValidationService validationService = Context.getValidationService();
		for (PersonIdentifier identifier : personIdentifiers) {
			validationService.validate(identifier);
			
			Person personFound = personDao.getPersonById(identifier);
			if (personFound != null) {
				return personFound;
			}
		}
		return null;
	}
	
	/**
	 * Imports person into the system.
	 * 1. First it attempts to locate the person in the system using the persons identifiers. If the person is already in
	 * the system then there is nothing more to do.
	 * 2. Since the person doesn't exist in the system yet, a new record is added.
	 * 
	 */
	public Person importPerson(Person person) throws ApplicationException {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(person);
		
		Boolean personFound = findPersonUsingIdentifiersHQL(person);
		if (personFound != false) {
			log.warn("While attempting to add a person, found an existing record with same identifier: " + person);
			throw new ApplicationException("Person record to be added already exists in the system.");
		}		
		
		// Before we save the entry we need to generate any custom
		// fields that have been requested through configuration
		populateCustomFields(person);
		
		savePerson(person);
		Context.getAuditEventService().saveAuditEvent(AuditEventType.IMPORT_PERSON_EVENT_TYPE, "Imported person record.", person);
		
		// Generate a notification event to inform interested listeners that this event has occurred.
//		NotificationEvent event = NotificationEventFactory.createNotificationEvent(EventType.IMPORT_EVENT_TYPE, person);
		//Context.getNotificationService().fireNotificationEvent(event);
		
		return person;
	}

	public void addReviewRecordPair(ReviewRecordPair reviewRecordPair) throws ApplicationException {
		validateReviewRecordPair(reviewRecordPair);
		saveReviewRecordPair(reviewRecordPair);
	}

	public void addReviewRecordPairs(List<ReviewRecordPair> reviewRecordPairs) throws ApplicationException {
		java.util.Date now = new java.util.Date();
		for (ReviewRecordPair reviewRecordPair : reviewRecordPairs) {
			validateReviewRecordPair(reviewRecordPair);
			reviewRecordPair.setUserCreatedBy(Context.getUserContext().getUser());
			reviewRecordPair.setDateCreated(now);
		}
		personLinkDao.addReviewRecordPairs(reviewRecordPairs);
	}

	private void validateReviewRecordPair(ReviewRecordPair reviewRecordPair) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(reviewRecordPair);
		
		if (reviewRecordPair.getPersonLeft() == null || 
				reviewRecordPair.getPersonRight() == null ||
				reviewRecordPair.getPersonLeft().getPersonId() == null ||
				reviewRecordPair.getPersonRight().getPersonId() == null) {
			log.info("Record pair review entry must include references to both records: " + reviewRecordPair);
			throw new ApplicationException("The record pair review entry does not reference both records.");
		}
		
		if (reviewRecordPair.getPersonLeft().getPersonId().intValue() == reviewRecordPair.getPersonRight().getPersonId().intValue()) {
			log.info("Record pair review entry has two references to the same record");
			throw new ApplicationException("The record pair review entry has two references to the same record.");
		}
	}
	
	public void matchReviewRecordPair(ReviewRecordPair recordPair) throws ApplicationException {
		ValidationService validationService = Context.getValidationService();
		validationService.validate(recordPair);

		ReviewRecordPair recordPairFound = personLinkDao.getReviewRecordPair(recordPair.getReviewRecordPairId());
		if (recordPairFound == null) {
			log.debug("Attempted to load an unknown review record pair: " + recordPair);
			return;
		}
		
		if (recordPair.getRecordsMatch() == null) {
			log.debug("Attempted to establish a match but did not specify whether the pair should be linked or not: " + recordPair);
			throw new ApplicationException("Must specify whether the record pair should be reviewed or not.");
		}
		
		// If they specify a link between the records then we need to create a person link entry.
		recordPairFound.setRecordsMatch(recordPair.getRecordsMatch());
		updateReviewRecordPair(recordPairFound);
		if (recordPairFound.getRecordsMatch() == true) {
			personLinkDao.convertReviewLinkToLink(recordPairFound);
		}
	}

	public IdentifierDomainAttribute addIdentifierDomainAttribute(IdentifierDomain identifierDomain, String attributeName, String attributeValue) {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomain);
		
		IdentifierDomainAttribute attribute = personDao.addIdentifierDomainAttribute(identifierDomain, attributeName, attributeValue);
		
		Context.getAuditEventService().saveAuditEvent(AuditEventType.ADD_IDENTIFIER_DOMAIN_ATTRIBUTE_EVENT_TYPE, "Added attribute " + attributeName + 
				" to identifier domain with ID " + identifierDomain.getIdentifierDomainId());
		
		return attribute;
	}
	
	public void updateIdentifierDomainAttribute(IdentifierDomainAttribute identifierDomainAttribute) {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomainAttribute);
		
		personDao.updateIdentifierDomainAttribute(identifierDomainAttribute);
		
		Context.getAuditEventService().saveAuditEvent(AuditEventType.UPDATE_IDENTIFIER_DOMAIN_ATTRIBUTE_EVENT_TYPE, "Updated attribute " + identifierDomainAttribute.getAttributeName() + 
				" of identifier domain with ID " + identifierDomainAttribute.getIdentifierDomainId());
	}
	
	public void removeIdentifierDomainAttribute(IdentifierDomainAttribute identifierDomainAttribute) {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(identifierDomainAttribute);
		
		personDao.removeIdentifierDomainAttribute(identifierDomainAttribute);
		Context.getAuditEventService().saveAuditEvent(AuditEventType.DELETE_IDENTIFIER_DOMAIN_ATTRIBUTE_EVENT_TYPE, "Deleted attribute " + identifierDomainAttribute.getAttributeName() + 
				" of identifier domain with ID " + identifierDomainAttribute.getIdentifierDomainId());
	}
	
	public IdentifierDomain obtainUniqueIdentifierDomain(String universalIdentifierTypeCode) {
		boolean isKnown = personDao.isKnownUniversalIdentifierTypeCode(universalIdentifierTypeCode);
		log.trace("The universlIdentifierTypeCode " + universalIdentifierTypeCode + " is known to the system exptresson is " + isKnown);
		if (!isKnown) {
			throw new ValidationException("The universalIdentifierTypeCode " + universalIdentifierTypeCode + " is not known to the system.");
		}
		IdentifierDomain identifierDomain = generateIdentifierDomainForUniversalIdentifierTypeCode(universalIdentifierTypeCode);
		personDao.addIdentifierDomain(identifierDomain);
		log.trace("Created new identifier domain " + identifierDomain);
		Context.getAuditEventService().saveAuditEvent(AuditEventType.OBTAIN_UNIQUE_IDENTIFIER_DOMAIN_EVENT_TYPE, "Obtained unique identifier domain type for type code " + universalIdentifierTypeCode);
		return identifierDomain;
	}

	public void generateCustomFields() throws ApplicationException {
		log.info("Re-generating all the custom fields in the repository.");
		personDao.clearCustomFields();
		int recordCount = personDao.getRecordCount();
		log.info("Will generate custom fields for " + recordCount + " records.");
		int start=0;
		int blockSize = TRANSACTION_BLOCK_SIZE;
		while (start < recordCount) {
			List<Person> records = personDao.getPersonsPaged(start, blockSize);
			generateCustomFields(records);
			start += records.size();
			log.info("Finished generating custom fields for " + start + " records.");
		}
	}

	public void clearCustomFields() {
		log.info("Clearing the values of all custom fields.");
		personDao.clearCustomFields();		
	}
	
	public void generateCustomFields(final List<Person> records) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					for (Person person : records) {
						populateCustomFields(person);
					}
					personDao.updatePersons(records);
				} catch (Exception e) {
					log.error("Failed while generating custom fields for a block of records: " + e);
					status.setRollbackOnly();
				}
			}
		});
	}
	
	public void initializeRepository() throws ApplicationException {
		log.info("Initialized the repository from the beginning using the underlying matching algorithm to do so.");
		MatchingService matchingService = Context.getMatchingService();
		matchingService.initializeRepository();
		linkAllRecordPairs();
	}

	public void linkAllRecordPairs() throws ApplicationException {
		clearAllLinks();
		MatchingService matchingService = Context.getMatchingService();
		BlockingService blockingService = Context.getBlockingService();
		RecordPairSource recordPairSource = blockingService.getRecordPairSource();
		int pairCount=0;
		Map<Long,Integer> clusterIdByRecordIMap = personLinkDao.getClusterIdByRecordIdMap(matchingService.getMatchingServiceId());
		Map<Integer,List<PersonLink>> linksByClusterIdMap = new HashMap<Integer,List<PersonLink>>();
		Map<ReviewRecordPair,ReviewRecordPair> mapOfReviewLinks = new HashMap<ReviewRecordPair,ReviewRecordPair>();
		List<ReviewRecordPair> reviewLinks = new java.util.ArrayList<ReviewRecordPair>();
		List<PersonLink> links = new java.util.ArrayList<PersonLink>();
		for (RecordPairIterator iter = recordPairSource.iterator(); iter.hasNext(); ) {
			RecordPair pair = iter.next();
			pairCount++;
			pair = matchingService.match(pair);
			if (pair.getMatchOutcome() == RecordPair.MATCH_OUTCOME_LINKED) {
				linkNodesInPair(pair, clusterIdByRecordIMap, linksByClusterIdMap, links);
			} else if (pair.getMatchOutcome() == RecordPair.MATCH_OUTCOME_POSSIBLE) {
				ReviewRecordPair reviewRecordPair = ConvertUtil.buildReviewPair(pair);
				log.trace("Adding review record pair: " + reviewRecordPair);
				ReviewRecordPair found = mapOfReviewLinks.get(reviewRecordPair);
				if (found == null) {
					mapOfReviewLinks.put(reviewRecordPair,reviewRecordPair);
					reviewLinks.add(reviewRecordPair);
				}
			}
			if (reviewLinks.size() == 10000) {
				log.info("Finished persisting a block of " + reviewLinks.size() + " review links out of a total of: " + mapOfReviewLinks.keySet().size());
				addReviewRecordPairs(reviewLinks);
				reviewLinks.clear();
			}
			if (links.size() == 10000) {
				log.info("Finished persisting a block of " + links.size() + " links.");
				personLinkDao.addPersonLinks(links);
				links.clear();
			}
			if (pairCount % TRANSACTION_BLOCK_SIZE == 0) {
				log.info("Finished linking " + pairCount + " record pairs.");
			}
		}
		log.info("Finished persisting a block of " + reviewLinks.size() + " review links out of a total of: " + mapOfReviewLinks.keySet().size());
		addReviewRecordPairs(reviewLinks);
		log.info("Finished persisting a block of " + links.size() + " links.");
		personLinkDao.addPersonLinks(links);
		log.info("In initializing the repository, we evaluated " + pairCount + " record pairs.");		
	}

	public void clearAllLinks() {
		MatchingService matchingService = Context.getMatchingService();
		log.info("Clearing all existing links of source: " + matchingService.getMatchingServiceId());

		// Remove all the current links in the system by the current matching algorithm.
		LinkSource linkSource = new LinkSource(matchingService.getMatchingServiceId());
		removeLinksBySource(linkSource);
		removeReviewLinksBySource(linkSource);		
	}

	private Integer getClusterId(Long recordId, Integer linkSourceId, Map<Long,Integer> clusterIdByRecordIdMap) {
		if (clusterIdByRecordIdMap == null) {
			return personLinkDao.getClusterId(new Long[] { recordId }, linkSourceId);
		}
		return clusterIdByRecordIdMap.get(recordId);
	}

	/**
	 * 
	 * @param pair
	 * @param linksByClusterIdMap 
	 * @param links 
	 * @param mapOfLinks 
	 */
	private void linkNodesInPair(RecordPair pair, Map<Long,Integer> clusterIdByRecordIdMap, Map<Integer, List<PersonLink>> linksByClusterIdMap, List<PersonLink> links) {
		Record left = pair.getLeftRecord();
		Record right = pair.getRightRecord();
		Integer leftClusterId = getClusterId(left.getRecordId(), pair.getLinkSource().getLinkSourceId(), clusterIdByRecordIdMap);
		Integer rightClusterId = getClusterId(right.getRecordId(), pair.getLinkSource().getLinkSourceId(), clusterIdByRecordIdMap);
		if (leftClusterId == null && rightClusterId == null) {
			int clusterId = personLinkDao.getNextClusterId();
			if (log.isTraceEnabled()) {
				log.trace("Case 1: Building a link between node: " + pair.getLeftRecord().getRecordId() + " and node " + pair.getRightRecord().getRecordId() + " using clusterId: " + clusterId);
			}
			updateClusterIdMap(clusterId, pair, clusterIdByRecordIdMap);
			PersonLink link = getPersonLinkFromRecordPair(pair, clusterId);
			links.add(link);
			updateLinksByClusterIdMap(linksByClusterIdMap, clusterId, link);
			
			if (Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier()) {
				synchronizeGlobalIdentifiers((Person) left.getObject(), (Person) right.getObject());
			}
		} else if (leftClusterId == null && rightClusterId != null) {
			int clusterId = rightClusterId;
			Person sourcePerson = (Person) pair.getLeftRecord().getObject();
			linkAllNodes(sourcePerson, clusterId, pair, linksByClusterIdMap, links);

			if (Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier()) {
				synchronizeGlobalIdentifiers((Person) right.getObject(), (Person) left.getObject());
			}
		} else if (leftClusterId != null && rightClusterId == null) {
			int clusterId = leftClusterId;
			Person sourcePerson = (Person) pair.getRightRecord().getObject();
			linkAllNodes(sourcePerson, clusterId, pair, linksByClusterIdMap, links);

			if (Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier()) {
				synchronizeGlobalIdentifiers((Person) left.getObject(), (Person) right.getObject());
			}
		} else if (leftClusterId.intValue() == rightClusterId.intValue()) {
			if (log.isTraceEnabled()) {
				log.trace("Skipping this link since the nodes are already connected: (" + left.getRecordId() + "," + right.getRecordId() + ")");
			}
			return;
		} else {
			if (log.isTraceEnabled()) {
				log.info("Case 3: Need to build links between two sub-graphs.");
			}
			List<PersonLink> leftLinks = getPersonLinks(linksByClusterIdMap, leftClusterId);
			Set<Person> nodesInCluster = getPersonsInCluster(leftLinks);
			StringBuffer sb = new StringBuffer("[");
			for (Person node : nodesInCluster) {
				sb.append(node.getPersonId()).append(",");
			}
			sb.append("]");
			String leftList = sb.toString();
			List<PersonLink> rightLinks = getPersonLinks(linksByClusterIdMap, rightClusterId);
			nodesInCluster = getPersonsInCluster(rightLinks);
			sb = new StringBuffer("[");
			for (Person node : nodesInCluster) {
				sb.append(node.getPersonId()).append(",");
			}
			sb.append("]");
			String rightList = sb.toString();
			if (log.isTraceEnabled()) {
				log.info("Case 3: Built links between sub-graph: " + leftList + " and subgraph: " + rightList);
			}
		}
	}

	/**
	 * Assign the same global identifier to both person entries. The global identifier should come from the left
	 * record and should be added to the right one.
	 * 
	 * @param left
	 * @param right
	 */
	private void synchronizeGlobalIdentifiers(Person left, Person right) {
		PersonIdentifier globalId = getGlobalIdentifierAndDomain(left, Context.getConfiguration().getGlobalIdentifierDomain());
		if (globalId != null) {
			right = personDao.loadPerson(right.getPersonId());
			PersonState state = capturePreIdentifiers(IdentifierUpdateEvent.LINK_SOURCE, right);
			removeGlobalIdentifier(right, Context.getConfiguration().getGlobalIdentifierDomain());
			PersonIdentifier copy = getGlobalPersonIdentifierCopy(globalId);
			copy.setPerson(right);
			right.addPersonIdentifier(copy);
			personDao.updatePerson(right);
			capturePostIdentifiers(right, state);
			getUpdateEventNotificationGenerator().generateEvents(state);
			return;
		}

		// Neither one of the have a global identifier so, we need to generate one
		left = personDao.loadPerson(left.getPersonId());
		PersonState leftState = capturePreIdentifiers(IdentifierUpdateEvent.LINK_SOURCE, left);
		globalId = generateGlobalIdentifier(left);
		personDao.updatePerson(left);
		capturePostIdentifiers(left, leftState);
		getUpdateEventNotificationGenerator().generateEvents(leftState);

		right = personDao.loadPerson(right.getPersonId());
		PersonState state = capturePreIdentifiers(IdentifierUpdateEvent.LINK_SOURCE, right);
		removeGlobalIdentifier(right, Context.getConfiguration().getGlobalIdentifierDomain());
		PersonIdentifier copy = getGlobalPersonIdentifierCopy(globalId);
		copy.setPerson(right);
		right.addPersonIdentifier(copy);
		personDao.updatePerson(right);
		capturePostIdentifiers(right, state);
		getUpdateEventNotificationGenerator().generateEvents(state);
	}

	private void updateLinksByClusterIdMap(Map<Integer, List<PersonLink>> linksByClusterIdMap, int clusterId, PersonLink link) {
		if (linksByClusterIdMap == null) {
			return;
		}
		List<PersonLink> list = linksByClusterIdMap.get(clusterId);
		if (list == null) {
			list = new java.util.ArrayList<PersonLink>();
			linksByClusterIdMap.put(clusterId, list);
		}
		list.add(link);
	}

	private void updateClusterIdMap(int clusterId, RecordPair pair, Map<Long, Integer> clusterIdByRecordIdMap) {
		if (clusterIdByRecordIdMap == null) {
			return;
		}
		clusterIdByRecordIdMap.put(pair.getLeftRecord().getRecordId(), clusterId);
		clusterIdByRecordIdMap.put(pair.getRightRecord().getRecordId(), clusterId);
	}

	private void linkAllNodes(Person sourcePerson, int clusterId, RecordPair pair, Map<Integer, List<PersonLink>> linksByClusterIdMap, List<PersonLink> links) {
		List<PersonLink> existingLinks = getPersonLinks(linksByClusterIdMap, clusterId);
		Set<Person> nodesInCluster = getPersonsInCluster(existingLinks);
		StringBuffer sb = new StringBuffer("[");
		for (Person node : nodesInCluster) {
			sb.append(node.getPersonId()).append(",");
		}
		sb.append("]");
		log.trace("Case 2: Building a link between node: " + sourcePerson.getPersonId() + " and nodes: " + sb + " using clusterId: " + clusterId);
		for (Person node : nodesInCluster) {
			if (node.getPersonId() == sourcePerson.getPersonId()) {
				continue;
			}
			PersonLink link = getPersonLink(sourcePerson, node, clusterId, pair);
			links.add(link);
			updateLinksByClusterIdMap(linksByClusterIdMap, clusterId, link);
		}
	}

	private List<PersonLink> getPersonLinks(Map<Integer, List<PersonLink>> linksByClusterIdMap, int clusterId) {
		if (linksByClusterIdMap == null) {
			return personLinkDao.getPersonLinks(clusterId);
		}
		return linksByClusterIdMap.get(clusterId);
	}

	private PersonLink getPersonLink(Person leftPerson, Person rightPerson, Integer clusterId, RecordPair pair) {
		PersonLink personLink = new PersonLink();
		personLink.setDateCreated(new java.util.Date());
		personLink.setUserCreatedBy(Context.getUserContext().getUser());
		personLink.setPersonLeft(leftPerson);
		personLink.setPersonRight(rightPerson);
		personLink.setWeight(pair.getWeight());
		personLink.setClusterId(clusterId);
		personLink.setLinkSource(pair.getLinkSource());
		return personLink;
	}
	
	private void removeLinksBySource(LinkSource linkSource) {
		personLinkDao.removeLinksBySource(linkSource);
	}
	
	private void removeReviewLinksBySource(LinkSource linkSource) {
		personLinkDao.removeReviewLinksBySource(linkSource);
	}
	
	public boolean assignGlobalIdentifier() {
		GlobalIdentifier globalIdentifier = Context.getConfiguration().getGlobalIdentifier();
		if (!globalIdentifier.isAssignGlobalIdentifier()) {
			log.info("The system is not configured for global identifiers so we will not assign global identifiers to persons without one.");
			return true;
		}
		
		IdentifierDomain domain = getPersistedIdentifierDomain(globalIdentifier.getIdentifierDomain());
		List<Integer> personIds = personDao.getPersonsWithoutIdentifierInDomain(domain, false);
		if (personIds.size() > 0) {
			assignGlobalIdentifier(domain, personIds, false);
		}
		personIds = personDao.getPersonsWithoutIdentifierInDomain(domain, true);
		if (personIds.size() == 0) {
			return true;
		}
		return assignGlobalIdentifier(domain, personIds, true);
	}

	public void deleteReviewRecordPair(ReviewRecordPair recordPair) {
		if (recordPair == null || recordPair.getReviewRecordPairId() == null) {
			log.debug("Called attempted to delete a record pair but did not provide the identifier for it.");
			return;
		}
		personLinkDao.removeReviewRecordPair(recordPair);
	}

	public void deleteReviewRecordPairs() {
		personLinkDao.removeAllReviewRecordPairs();
	}
	
	public void removePerson(Integer personId) throws ApplicationException {
		if (personId == null) {
			return;
		}
		Person personFound = personDao.loadPerson(personId);
		if (personFound == null) {
			return;
		}
		personDao.removePerson(personId);

		// Generate a notification event to inform interested listeners that this event has occurred.
		NotificationEvent event = NotificationEventFactory.createNotificationEvent(EventType.DELETE_EVENT_TYPE, personFound);
		Context.getNotificationService().fireNotificationEvent(event);

		// Generate a notification event to inform interested listeners via the lightweight mechanism that this event has occurred.
		Context.notifyObserver(ObservationEventType.ENTITY_DELETE_EVENT, ConvertUtil.getRecordFromPerson(personFound));
	}
	
	private boolean assignGlobalIdentifier(IdentifierDomain domain, List<Integer> personIds, boolean hasLinks) {
		java.util.Map<Integer,Integer> idsToProcess = buildMapOfIdsToProcess(personIds);
		Set<PersonIdentifier> identifiersToAdd = new HashSet<PersonIdentifier>();
		int totalCount = 0;
		for (Integer id : personIds) {
			// If already processed through a linked entry then skip it
			if (idsToProcess.get(id) == null) {
				continue;
			}
			Person person = new Person();
			person.setPersonId(id);
			log.debug("Assigning global identifier to person " + person.getPersonId());
			if (hasLinks) {
				List<Person> linkedPersons = findLinkedPersons(person);
				PersonIdentifier globalIdentifier = getGlobalIdentifierFromLinks(domain, linkedPersons);
				// If one of the links has a global identifier then get it and assign it to this person entry
				if (globalIdentifier != null) {
					log.debug("Assigning global identifier " + globalIdentifier.getIdentifier() + " to person " + person.getPersonId() + " obtained from linked entry.");
					globalIdentifier.setPerson(person);
					identifiersToAdd.add(globalIdentifier);
					idsToProcess.remove(id);				
				// None of them have a global identifier so generate one and assign it to every entry in the cluster
				} else {
					globalIdentifier = generateGlobalIdentifier(domain, person);
					globalIdentifier.setDateCreated(new java.util.Date());
					globalIdentifier.setUserCreatedBy(Context.getUserContext().getUser());
					log.debug("Assigning newly generated global identifier " + globalIdentifier.getIdentifier() + " to person " + person.getPersonId());
					globalIdentifier.setPerson(person);
					identifiersToAdd.add(globalIdentifier);
					idsToProcess.remove(id);
					for (Person linkedPerson : linkedPersons) {
						PersonIdentifier gid = cloneGlobalIdentifier(globalIdentifier);
						gid.setPerson(linkedPerson);
						identifiersToAdd.add(gid);
						idsToProcess.remove(linkedPerson.getPersonId());
					}
				}
			} else {
				PersonIdentifier globalIdentifier = generateGlobalIdentifier(domain, person);
				globalIdentifier.setDateCreated(new java.util.Date());
				globalIdentifier.setUserCreatedBy(Context.getUserContext().getUser());
				log.debug("Assigning newly generated global identifier " + globalIdentifier.getIdentifier() + " to person " + person.getPersonId());
				globalIdentifier.setPerson(person);
				identifiersToAdd.add(globalIdentifier);
				idsToProcess.remove(id);				
			}
			totalCount++;
			if (identifiersToAdd.size() == 5000) {
				log.info("Adding a batch of identifiers for a total of " + totalCount);
				personDao.addPersonIdentifiers(identifiersToAdd);
				identifiersToAdd.clear();
			}
		}		
		personDao.addPersonIdentifiers(identifiersToAdd);
		log.info("Finished assigning global identifiers.");
		return true;
	}

	private PersonIdentifier cloneGlobalIdentifier(PersonIdentifier globalIdentifier) {
		PersonIdentifier id = new PersonIdentifier();
		id.setIdentifier(globalIdentifier.getIdentifier());
		id.setDateCreated(globalIdentifier.getDateCreated());
		id.setUserCreatedBy(globalIdentifier.getUserCreatedBy());
		id.setIdentifierDomain(globalIdentifier.getIdentifierDomain());
		return id;
	}

	private PersonIdentifier getGlobalIdentifierFromLinks(IdentifierDomain domain, java.util.Collection<Person> linkedPersons) {
		for (Person person : linkedPersons) {
			for (PersonIdentifier identifier : person.getPersonIdentifiers()) {
				if (identifier.getIdentifierDomain().equals(domain)) {
					return identifier;
				}
			}
		}
		return null;
	}

	private java.util.Map<Integer,Integer> buildMapOfIdsToProcess(List<Integer> personIds) {
		java.util.Map<Integer,Integer> idsToProcess = new java.util.HashMap<Integer,Integer>(personIds.size());
		for (Integer id : personIds) {
			idsToProcess.put(id, id);
		}
		return idsToProcess;
	}

	private IdentifierDomain getPersistedIdentifierDomain(IdentifierDomain identifierDomain) {
		IdentifierDomain identifierDomainFound = personDao.findIdentifierDomain(identifierDomain);
		if (identifierDomainFound == null) {
			identifierDomain.setDateCreated(new Date());
			identifierDomain.setUserCreatedBy(Context.getUserContext().getUser());
			personDao.addIdentifierDomain(identifierDomain);
			return identifierDomain;
		}
		return identifierDomainFound;
	}

	private Person findPersonUsingIdentifiers(Person person) {
		Set<PersonIdentifier> identifiers = person.getPersonIdentifiers();
		for (PersonIdentifier identifier : identifiers) {
			Person personFound = personDao.getPersonById(identifier);
			if (personFound != null) {
				return personFound;
			}
		}
		return null;
	}
	

	private Boolean findPersonUsingIdentifiersHQL(Person person) {
		Set<PersonIdentifier> identifiers = person.getPersonIdentifiers();
		for (PersonIdentifier identifier : identifiers) {
			boolean personFound = personDao.getPersonByIdHQL(identifier);
			if (personFound != false) {
				return personFound;
			}
		}
		return false;
	}	

	/** 
	 * Method ensures that the caller does not add a global identifier.
	 * 
	 * @param person
	 * @param personFound
	 * @throws ApplicationException 
	 */
	private void validateNoNewGlobalIdentifier(Person person, Person personFound) throws ApplicationException {
		// If we are not handling global identifier assignment, then there is nothing to do
		if (!Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier()) {
			return;
		}
		
		String identifier = getGlobalIdentifier(person, Context.getConfiguration().getGlobalIdentifierDomain());
		if (identifier == null) {
			return;
		}
		
		if (personFound == null) {
			log.warn("Caller attempted to add a global identifier to a record.");
			throw new ApplicationException("Identifiers in the global identifier domain must be assigned only by the system itself.");
		}
		
		String identifierExisting = getGlobalIdentifier(personFound, Context.getConfiguration().getGlobalIdentifierDomain());
		if (identifierExisting == null || !identifier.equals(identifierExisting)) {
			log.warn("Caller attempted to add a global identifier to a record.");
			throw new ApplicationException("Identifiers in the global identifier domain must be assigned only by the system itself.");
		}
	}

	private PersonIdentifier getGlobalIdentifierAndDomain(Person person, IdentifierDomain domain) {
		for (PersonIdentifier identifier : person.getPersonIdentifiers()) {
			if (domain.getIdentifierDomainName().equals(identifier.getIdentifierDomain().getIdentifierDomainName())) {
				return identifier;
			}
		}
		return null;
	}

	private String getGlobalIdentifier(Person person, IdentifierDomain domain) {
		for (PersonIdentifier identifier : person.getPersonIdentifiers()) {
			if (domain.getIdentifierDomainName().equals(identifier.getIdentifierDomain().getIdentifierDomainName())) {
				return identifier.getIdentifier();
			}
		}
		return null;
	}

	private void saveIdentifierDomain(IdentifierDomain identifierDomain) {
		java.util.Date now = new java.util.Date();
		identifierDomain.setDateCreated(now);
		identifierDomain.setUserCreatedBy(Context.getUserContext().getUser());
		personDao.addIdentifierDomain(identifierDomain);
	}
	
	private Person findPersonUsingId(Person person) {
		Person personFound =  personDao.loadPersonForUpdate(person.getPersonId());
		return personFound;
	}

	private void savePerson(Person person) {
		log.debug("Current user is " + Context.getUserContext().getUser());
		java.util.Date now = new java.util.Date();
		person.setDateCreated(now);
		person.setUserCreatedBy(Context.getUserContext().getUser());
		person.setDateChanged(now);
		person.setUserChangedBy(Context.getUserContext().getUser());
		personDao.addPerson(person);
	}
	
	private IdentifierDomain generateIdentifierDomainForUniversalIdentifierTypeCode(String universalIdentifierTypeCode) {
		UUID uuid = new UUID();
		IdentifierDomain id = new IdentifierDomain();
		java.util.Date now = new java.util.Date();
		id.setDateCreated(now);
		id.setUserCreatedBy(Context.getUserContext().getUser());
		id.setNamespaceIdentifier(uuid.toString());
		id.setUniversalIdentifier(uuid.toString());
		id.setUniversalIdentifierTypeCode(universalIdentifierTypeCode);
		return id;
	}

	private void updatePerson(Person person, Person personFound) {
		person.setPersonId(personFound.getPersonId());
		person.setUserCreatedBy(personFound.getUserCreatedBy());
		
		java.util.Date now = new java.util.Date();
		person.setDateChanged(now);
		person.setUserChangedBy(Context.getUserContext().getUser());
				
		// Update DateCreated and UserCreatedBy
		java.util.Map<String,PersonIdentifier> mapOfIdentifiers = new java.util.HashMap<String,PersonIdentifier>();
		for (PersonIdentifier id : personFound.getPersonIdentifiers()) {
			mapOfIdentifiers.put(getIdentifierKey(id), id);
		}
		for (PersonIdentifier identifier : person.getPersonIdentifiers()) {
			PersonIdentifier found = mapOfIdentifiers.get(getIdentifierKey(identifier));
			if (found != null) {
				// old identifier so keep its DateCreated and UserCreatedBy
				if (identifier.getPersonIdentifierId() == null) {
					identifier.setPersonIdentifierId(found.getPersonIdentifierId());
				}
				identifier.setDateCreated(found.getDateCreated());
				identifier.setUserCreatedBy(found.getUserCreatedBy());
				continue;
			}

			identifier.setDateCreated(now);
			identifier.setUserCreatedBy(Context.getUserContext().getUser());
		}
		
		// Update deleted Identifiers
		updateDeletedIdentifiers(person, personFound);
		
		personDao.updatePerson(person);
	}
	
	/**
	 * If the updated person is missing identifiers that were present in the persisted version of the record,
	 * then we need to de-activate them rather than remove them completely from the repository.
	 * 
	 * This method locates those missing identifiers, adds them to the object to be updated, and voids them.
	 * @param person - newly updated record
	 * @param personFound - current record in persistence
	 */
	private void updateDeletedIdentifiers(Person person, Person personFound) {
		java.util.Map<String,PersonIdentifier> mapOfIdentifiers = new java.util.HashMap<String,PersonIdentifier>();
		for (PersonIdentifier id : person.getPersonIdentifiers()) {
			mapOfIdentifiers.put(getIdentifierKey(id), id);
		}
		java.util.Date now = new java.util.Date();
		for (PersonIdentifier id : personFound.getPersonIdentifiers()) {
			PersonIdentifier found = mapOfIdentifiers.get(getIdentifierKey(id));
			if (found != null) {
				if (id.getPersonIdentifierId() == null) {
					id.setPersonIdentifierId(found.getPersonIdentifierId());
				}
				continue;
			}
			// Found an identifier in the persisted person that is not in the new person
			id.setDateVoided(now);
			id.setUserVoidedBy(Context.getUserContext().getUser());
			id.setPerson(person);
			person.addPersonIdentifier(id);
		}
	}

	private String getIdentifierKey(PersonIdentifier id) {
		return id.getIdentifier() + "." + id.getIdentifierDomain().getIdentifierDomainId();
	}

	private void deletePerson(Person person) {
		java.util.Date now = new java.util.Date();
		User currUser = Context.getUserContext().getUser();
		Set<PersonIdentifier> ids = person.getPersonIdentifiers();
		for (PersonIdentifier id : ids) {
			id.setDateVoided(now);
			id.setUserVoidedBy(currUser);
		}
		person.setDateCreated(now);
		person.setUserCreatedBy(currUser);
		person.setDateChanged(now);
		person.setUserChangedBy(currUser);
		person.setDateVoided(now);
		person.setUserVoidedBy(Context.getUserContext().getUser());
		log.trace("Voiding the person record: " + person);
		personDao.updatePerson(person);
	}
	
	private void findAndDeleteRecordLinks(Person person) {
		List<PersonLink> links = personLinkDao.getPersonLinks(person);
		for (PersonLink link : links) {
			log.trace("Deleting the person link: " + link);
			personLinkDao.remove(PersonLink.class, link.getPersonLinkId());
		}
	}
	
	private Person getOtherPerson(PersonLink originalLink, Person person) {
		if (originalLink.getPersonLeft().getPersonId().equals(person.getPersonId())) {
			return originalLink.getPersonRight();
		}
		return originalLink.getPersonLeft();
	}

	private void findAndUpdateRecordLinks(Person person, PersonState state) throws ApplicationException {
		List<PersonLink> currLinks = personLinkDao.getPersonLinks(person);
		Set<PersonLink> preLinks = new HashSet<PersonLink>();
		state.setPreLinks(preLinks);
		for (PersonLink link : currLinks) {
			log.trace("Deleting the person link during an update: " + link);
			preLinks.add(link);
			personLinkDao.remove(PersonLink.class, link.getPersonLinkId());
		}
		findAndProcessAddRecordLinks(person, state);
	}

	private List<PersonLink> findAndProcessAddRecordLinks(Person person, PersonState state) throws ApplicationException {

		Record record = ConvertUtil.getRecordFromPerson(person);
		
		// Call the matching service to find any record pairs that must be linked
		MatchingService matchingService = Context.getMatchingService();
		Set<RecordPair> links = matchingService.match(record);
		
		List<PersonLink> personLinks = new java.util.ArrayList<PersonLink>(links.size());
		// If no matching records are found then return an empty list
		if (links.size() == 0) {
			if (Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier()) {
				if (state.getPreLinks().size() > 0) {
					removeGlobalIdentifier(person, Context.getConfiguration().getGlobalIdentifierDomain());
				}
				if (!hasGlobalIdentifier(person, Context.getConfiguration().getGlobalIdentifierDomain())) {
					generateGlobalIdentifier(Context.getConfiguration().getGlobalIdentifierDomain(), person);
				}
				personDao.updatePersonIdentifiers(person);
			}
			capturePostIdentifiers(person, state);
			return personLinks;
		}

		if (log.isDebugEnabled()) {
			log.debug("While adding node " + record.getRecordId() + " found links to nodes " + links);
		}
		// At this point we know that the record 'person' needs to be linked with
		// all the nodes in the list personLinks. The strategy is to identify all the nodes
		// to which the node person needs to be linked to resulting in the set 
		// called tagetNodeIds.
		// 
		Set<Long> targetNodeIds = getTargetNodeIds(links, person.getPersonId().longValue());
		if (log.isDebugEnabled()) {
			log.debug("While adding node " + record.getRecordId() + " found links to nodes " + links + 
					" and will need to connect source node to nodes: " + targetNodeIds);
		}		
		// Find the cluster ID for the group of nodes identified, their
		// links to one another, and then extract the list of nodes in the cluster
		RecordPair pair = links.iterator().next();
		Integer clusterId = personLinkDao.getClusterId(targetNodeIds, matchingService.getMatchingServiceId());
		PersonIdentifier globalIdentifier = null;
		if (clusterId != null) {
			List<PersonLink> existingLinks = personLinkDao.getPersonLinks(clusterId);
			Map<Long,Person> nodeToPersonMap = new HashMap<Long,Person>();
			targetNodeIds = buildTargetNodeSet(existingLinks, nodeToPersonMap);
			if (log.isDebugEnabled()) {
				log.debug("While adding node " + record.getRecordId() + " found cluster of nodes " + targetNodeIds);
			}
			for (Long targetNode : targetNodeIds) {
				if (isNotInExistingLinks(person.getPersonId().longValue(), targetNode, existingLinks)) {
					Person targetPerson = nodeToPersonMap.get(targetNode);
					if (globalIdentifier == null) {
						globalIdentifier = extractGlobalIdentifier(targetPerson);
					}
					PersonLink personLink = getPersonLinkFromPersonPair(person, targetPerson, clusterId, pair.getLinkSource());
					if (log.isDebugEnabled()) {
						log.debug("Adding link between nodes " + person.getPersonId() + " and " + targetPerson.getPersonId());
					}
					// TODO: We are using an arbitrary weight here but what we
					// should be doing is making a call back into the Matching Service to get a score
					// for this record pair that reflects the distance
					personLink.setWeight(pair.getWeight());
					personLinkDao.addPersonLink(personLink);
					personLinks.add(personLink);
				}
			}
			state.getPostLinks().addAll(personLinks);
			
			// If the configuration supports global identifiers, reconcile the identifier assigned to
			// the person record based on that assigned to the cluster just joined.
			//
			if (Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier()) {
				removeGlobalIdentifier(person, Context.getConfiguration().getGlobalIdentifierDomain());
				log.debug("Adding global identifier to person of " + globalIdentifier);
				PersonIdentifier globalIdCopy = getGlobalPersonIdentifierCopy(globalIdentifier);
				person.addPersonIdentifier(globalIdCopy);
				globalIdCopy.setPerson(person);
				personDao.updatePersonIdentifiers(person);
			}
		} else {
			clusterId = personLinkDao.getNextClusterId();
			Map<Long,Person> nodeToPersonMap = new HashMap<Long,Person>();
			targetNodeIds = buildTargetNodeSet(links, nodeToPersonMap);
			if (log.isDebugEnabled()) {
				log.debug("While adding node " + record.getRecordId() + " found group of nodes " + targetNodeIds);
			}
			List<Long> targetNodeList = new ArrayList<Long>();
			targetNodeList.addAll(targetNodeIds);
			globalIdentifier = extractGlobalIdentifier(person.getPersonId().longValue(), targetNodeList, nodeToPersonMap);
			for (int i=0; i < targetNodeList.size(); i++) {
				Person sourcePerson = nodeToPersonMap.get(targetNodeList.get(i));
				for (int j=i+1; j < targetNodeList.size(); j++) {
					Person targetPerson = nodeToPersonMap.get(targetNodeList.get(j));
					PersonLink personLink = getPersonLinkFromPersonPair(sourcePerson, targetPerson, clusterId, pair.getLinkSource());
					if (log.isDebugEnabled()) {
						log.debug("Adding link between nodes " + sourcePerson.getPersonId() + " and " + targetPerson.getPersonId());
					}
					// TODO: We are using an arbitrary weight here but what we
					// should be doing is making a call back into the Matching Service to get a score
					// for this record pair that reflects the distance
					personLink.setWeight(pair.getWeight());
					personLinkDao.addPersonLink(personLink);
					personLinks.add(personLink);					
				}
			}
			state.getPostLinks().addAll(personLinks);

			// If the configuration supports global identifiers, reconcile the identifier assigned to
			// the person record based on that assigned to the cluster just joined.
			//
			if (globalIdentifier == null) {
				log.warn("Found record(s) to be linked to record with ID " + person.getPersonId() + 
						" that does not have a global identifier: " + targetNodeList);
			} else if (Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier()) {
				removeGlobalIdentifier(person, globalIdentifier.getIdentifierDomain());
				log.debug("Adding global identifier to person of " + globalIdentifier);
				PersonIdentifier globalIdCopy = getGlobalPersonIdentifierCopy(globalIdentifier);
				person.addPersonIdentifier(globalIdCopy);
				globalIdCopy.setPerson(person);
				personDao.updatePersonIdentifiers(person);
			}
		}
		capturePostIdentifiers(person, state);
		return personLinks;
	}

	/**
	 * Removes from a person the identifier in the global domain if present.
	 * @param person
	 * @param globalIdentifier
	 */
	private void removeGlobalIdentifier(Person person, IdentifierDomain globalIdentifierDomain) {
		for (PersonIdentifier id : person.getPersonIdentifiers()) {
			if (id.getIdentifierDomain().getIdentifierDomainName().equalsIgnoreCase(globalIdentifierDomain.getIdentifierDomainName())) {
				id.setDateVoided(new Date());
				id.setUserVoidedBy(Context.getUserContext().getUser());
			}
		}
	}

	private PersonIdentifier extractGlobalIdentifier(Long sourceId, List<Long> idList, Map<Long, Person> idToPersonMap) {
		PersonIdentifier globalIdentifier = null;
		for (Long id : idList) {
			if (id.equals(sourceId)) {
				continue;
			}
			Person p = idToPersonMap.get(id);
			if (p == null) {
				continue;
			}
			globalIdentifier = extractGlobalIdentifier(p);
			if (globalIdentifier != null) {
				break;
			}
		}
		return globalIdentifier;
	}

    private boolean hasGlobalIdentifier(Person person, IdentifierDomain globalIdentifierDomain) {
        for (PersonIdentifier id : person.getPersonIdentifiers()) {
            if (id.getIdentifierDomain().getIdentifierDomainName()
            		.equalsIgnoreCase(globalIdentifierDomain.getIdentifierDomainName()) &&
            		id.getDateVoided() == null) {
                return true;
            }
        }
        return false;
    }

	private PersonIdentifier extractGlobalIdentifier(Person person) {
		if (!Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier()) {
			return null;
		}
		IdentifierDomain globalIdentifierDomain = Context.getConfiguration().getGlobalIdentifierDomain();
		for (PersonIdentifier id : person.getPersonIdentifiers()) {
			if (id.getIdentifierDomain().getIdentifierDomainName().equalsIgnoreCase(globalIdentifierDomain.getIdentifierDomainName())) {
				log.debug("Found global identifier in existing cluster of : " + id);
				return id;
			}
		}
		log.debug("Unable to find global identifier in existing cluster.");
		return null;
	}

	private boolean isNotInExistingLinks(Long sourceNode, Long targetNode, List<PersonLink> links) {
		if (links == null || links.size() == 0) {
			return true;
		}
		for (PersonLink link : links) {
			if (link != null && 
					link.getPersonLeft() != null && link.getPersonRight() != null &&
					link.getPersonLeft().getPersonId() != null && link.getPersonRight().getPersonId() != null &&
					((link.getPersonLeft().getPersonId().longValue() == sourceNode.longValue() && 
					  link.getPersonRight().getPersonId().longValue() == targetNode.longValue()) ||
					 (link.getPersonLeft().getPersonId().longValue() == targetNode.longValue() && 
					  link.getPersonRight().getPersonId().longValue() == sourceNode.longValue()))) {
				return false;
			}
		}
		return true;
	}

//	private void documentErrorCondition(Set<RecordPair> links, Set<Long> targetNodeIds, Set<Long> nodesInCluster) {
//		log.error("Found a case where the matching service did not identify all the nodes in the cluster to be linked together.");
//		log.error("Matching service found links:\n");
//		for (RecordPair pair : links) {
//			log.debug(pair);
//		}
//		log.error("The set of nodes identified is:\n");
//		StringBuffer sb = new StringBuffer("{");
//		for (Long nodeId : targetNodeIds) {
//			sb.append(nodeId).append(",");
//		}
//		sb.append("}");
//		log.debug(sb.toString());
//
//		log.error("The set of nodes in the cluster is:\n");
//		sb = new StringBuffer("{");
//		for (Long nodeId : nodesInCluster) {
//			sb.append(nodeId).append(",");
//		}
//		sb.append("}");
//		log.debug(sb.toString());
//	}
	
	private Set<Person> getPersonsInCluster(List<PersonLink> links) {
		Set<Person> nodesInCluster = new java.util.HashSet<Person>();
		for (PersonLink link : links) {
			nodesInCluster.add(link.getPersonLeft());
			nodesInCluster.add(link.getPersonRight());
		}
		return nodesInCluster;
	}

	private Set<Long> buildTargetNodeSet(Set<RecordPair> pairs, Map<Long, Person> nodeToPersonMap) {
		Set<Long> nodeIds = new java.util.HashSet<Long>();
		for (RecordPair pair : pairs) {
			if (pair != null) {
				if (pair.getLeftRecord() != null && pair.getLeftRecord().getRecordId() != null) {
					Long nodeId = pair.getLeftRecord().getRecordId();
					nodeIds.add(nodeId);
					nodeToPersonMap.put(nodeId, (Person) pair.getLeftRecord().getObject());
				}
				if (pair.getRightRecord() != null && pair.getRightRecord().getRecordId() != null) {
					Long nodeId = pair.getRightRecord().getRecordId();
					nodeIds.add(nodeId);
					nodeToPersonMap.put(nodeId, (Person) pair.getRightRecord().getObject());
				}
			}
		}
		return nodeIds;
	}

	private Set<Long> buildTargetNodeSet(List<PersonLink> links, Map<Long, Person> nodeToPersonMap) {
		Set<Long> nodeIds = new java.util.HashSet<Long>();
		for (PersonLink link : links) {
			if (link != null) {
				if (link.getPersonLeft() != null && link.getPersonLeft().getPersonId() != null) {
					Long nodeId = link.getPersonLeft().getPersonId().longValue();
					nodeIds.add(nodeId);
					nodeToPersonMap.put(nodeId, link.getPersonLeft());
				}
				if (link.getPersonRight() != null && link.getPersonRight().getPersonId() != null) {
					Long nodeId = link.getPersonRight().getPersonId().longValue();
					nodeIds.add(nodeId);
					nodeToPersonMap.put(nodeId, link.getPersonRight());
				}
			}
		}
		return nodeIds;
	}
	
	private Set<Long> getTargetNodeIds(Set<RecordPair> pairs, Long excludePersonId) {
		Set<Long> nodeIds = new java.util.HashSet<Long>();
		for (RecordPair pair : pairs) {
			if (pair != null) {
				if (pair.getLeftRecord() != null && pair.getLeftRecord().getRecordId() != null && 
						pair.getLeftRecord().getRecordId().longValue() != excludePersonId) {
					nodeIds.add(pair.getLeftRecord().getRecordId());
				}
				if (pair.getRightRecord() != null && pair.getRightRecord().getRecordId() != null && 
						pair.getRightRecord().getRecordId().longValue() != excludePersonId) {
					nodeIds.add(pair.getRightRecord().getRecordId());
				}
			}
		}
		return nodeIds;
	}

	private PersonLink getPersonLinkFromRecordPair(RecordPair recordPair, Integer clusterId) {
		PersonLink personLink = new PersonLink();
		personLink.setDateCreated(new java.util.Date());
		personLink.setUserCreatedBy(Context.getUserContext().getUser());
		personLink.setPersonLeft((Person) recordPair.getLeftRecord().getObject());
		personLink.setPersonRight((Person) recordPair.getRightRecord().getObject());
		personLink.setWeight(recordPair.getWeight());
		personLink.setLinkSource(recordPair.getLinkSource());
		personLink.setClusterId(clusterId);
		return personLink;
	}

	private PersonLink getPersonLinkFromPersonPair(Person leftPerson, Person rightPerson, Integer clusterId, LinkSource linkSource) {
		PersonLink personLink = new PersonLink();
		personLink.setDateCreated(new java.util.Date());
		personLink.setUserCreatedBy(Context.getUserContext().getUser());
		personLink.setPersonLeft(leftPerson);
		personLink.setPersonRight(rightPerson);
		personLink.setClusterId(clusterId);
		personLink.setLinkSource(linkSource);
		return personLink;
	}

	private PersonLink getPersonLinkFromRecordPair(Person leftPerson, Person rightPerson, PersonLink oldLink) {
		PersonLink personLink = new PersonLink();
		personLink.setDateCreated(new java.util.Date());
		personLink.setUserCreatedBy(Context.getUserContext().getUser());
		personLink.setPersonLeft(leftPerson);
		personLink.setPersonRight(rightPerson);
		personLink.setClusterId(oldLink.getClusterId());
		personLink.setLinkSource(oldLink.getLinkSource());
		personLink.setWeight(org.openhie.openempi.Constants.MERGE_RECORDS_WEIGHT);
		return personLink;
	}
	
	private boolean configurationSupportsGlobalIdentifiers() {
		return Context.getConfiguration().getGlobalIdentifier().isAssignGlobalIdentifier();
	}
	
	private void generateGlobalId(Person person, List<PersonLink> personLinks) {
		GlobalIdentifier globalId = Context.getConfiguration().getGlobalIdentifier();
		log.trace("Global Identifier Configuration is " + globalId);
		if (!configurationSupportsGlobalIdentifiers()) {
			return;
		}

		IdentifierDomain domain = globalId.getIdentifierDomain();
		
		// Check to see if the person already has a global identifier
		for (PersonIdentifier identifier : person.getPersonIdentifiers()) {
			IdentifierDomain identifierDomain = identifier.getIdentifierDomain();
			if (identifierDomain != null && identifierDomain.equals(domain)) {
				log.debug("Person already has an global identifier assigned: " + identifier);
				return;
			}
		}
		
		Set<Person> linkedPersons = loadLinkedPersons(personLinks);
		log.debug("New person record was found to have " + linkedPersons.size() + " linked entries already in the repository.");
		PersonIdentifier globalIdentifier = getGlobalIdentifierFromLinks(domain, linkedPersons);
		// If one of the links has a global identifier then get it and assign it to this person entry
		if (globalIdentifier == null) {
			globalIdentifier = generateGlobalIdentifier(domain, person);
			log.debug("Generated a new global identifier since one could not be obtained from linked person entries: " + globalIdentifier);
		} else {
			log.debug("Obtained global identifier from linked person entry: " + globalIdentifier);
			globalIdentifier = getGlobalPersonIdentifierCopy(globalIdentifier);
		}
		person.addPersonIdentifier(globalIdentifier);
	}

	private PersonIdentifier getGlobalPersonIdentifierCopy(PersonIdentifier globalIdentifier) {
		PersonIdentifier id = new PersonIdentifier();
		id.setDateCreated(globalIdentifier.getDateCreated());
		id.setDateVoided(globalIdentifier.getDateVoided());
		id.setIdentifier(globalIdentifier.getIdentifier());
		id.setIdentifierDomain(globalIdentifier.getIdentifierDomain());
		id.setUserCreatedBy(globalIdentifier.getUserCreatedBy());
		id.setUserVoidedBy(globalIdentifier.getUserVoidedBy());
		return id;
	}

	private PersonIdentifier generateGlobalIdentifier(Person person) {
		return generateGlobalIdentifier(Context.getConfiguration().getGlobalIdentifierDomain(), person);
	}
	
	private PersonIdentifier generateGlobalIdentifier(IdentifierDomain globalIdentifierDomain, Person person) {
		UUID uuid = new UUID();
		PersonIdentifier identifier = new PersonIdentifier();
		identifier.setIdentifier(uuid.toString());
		identifier.setIdentifierDomain(globalIdentifierDomain);
		identifier.setPerson(person);
		person.addPersonIdentifier(identifier);
		identifier.setDateCreated(new java.util.Date());
		identifier.setUserCreatedBy(Context.getUserContext().getUser());
		return identifier;
	}

	private void saveReviewRecordPair(ReviewRecordPair recordPair) {
		recordPair.setUserCreatedBy(Context.getUserContext().getUser());
		recordPair.setDateCreated(new java.util.Date());
		personLinkDao.addReviewRecordPair(recordPair);
	}
	
	private void updateReviewRecordPair(ReviewRecordPair recordPair) {
		recordPair.setUserReviewedBy(Context.getUserContext().getUser());
		recordPair.setDateReviewed(new java.util.Date());
		personLinkDao.updateReviewRecordPair(recordPair);
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public List<UpdateNotificationRegistrationEntry> getUpdateNotificationRegistrationEntries() {
		updateNotificationEntries = Context.getConfiguration().getAdminConfiguration()
				.getUpdateNotificationRegistrationEntries();
		return updateNotificationEntries;
	}
	
	public UpdateEventNotificationGenerator getUpdateEventNotificationGenerator() {
		if (notificationGenerator == null) {
			notificationGenerator = new UpdateEventNotificationGenerator(getPersonDao(), getUpdateNotificationRegistrationEntries());
		}
		return notificationGenerator;
	}
	
	class PersonState
	{
		
		private int personId;
		private String source;
		private String transition;
		private Set<PersonIdentifier> preIdentifiers;
		private Set<PersonLink> preLinks;
		private Set<PersonIdentifier> postIdentifiers;
		private Set<PersonLink> postLinks;
		
		public PersonState(int personId) {
			this.personId = personId;
			postLinks = new HashSet<PersonLink>();
			preLinks = new HashSet<PersonLink>();
		}
		
		public PersonState(int personId, String source) {
			this(personId);
			this.source = source;
		}
		
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getTransition() {
			return transition;
		}
		public void setTransition(String transition) {
			this.transition = transition;
		}
		public Set<PersonIdentifier> getPreIdentifiers() {
			return preIdentifiers;
		}
		public void setPreIdentifiers(Set<PersonIdentifier> preIdentifiers) {
			this.preIdentifiers = preIdentifiers;
		}
		public Set<PersonLink> getPreLinks() {
			return preLinks;
		}
		public void setPreLinks(Set<PersonLink> preLinks) {
			this.preLinks = preLinks;
		}
		public Set<PersonIdentifier> getPostIdentifiers() {
			return postIdentifiers;
		}
		public void setPostIdentifiers(Set<PersonIdentifier> postIdentifiers) {
			this.postIdentifiers = postIdentifiers;
		}
		public Set<PersonLink> getPostLinks() {
			return postLinks;
		}
		public void setPostLinks(Set<PersonLink> postLinks) {
			this.postLinks = postLinks;
		}
		public int getPersonId() {
			return personId;
		}
		public void setPersonId(int personId) {
			this.personId = personId;
		}
	}

	@Override
	public void removeNotification(IdentifierUpdateEvent identifierUpdateEvent) {
			personDao.removeIdentifierUpdateEvent(identifierUpdateEvent);
	}

	@Override
	public int removeNotifications(List<IdentifierUpdateEvent> identifierUpdateEvents) {
		int count = 0;
		for (IdentifierUpdateEvent identifierUpdateEvent : identifierUpdateEvents) {
			log.trace("Deleting the IdentifierUpdateEvent");
			personDao.removeIdentifierUpdateEvent(identifierUpdateEvent);
			count++;
		}
		return count;
	}

	@Override
	public void cleanupUpdateNotifications() {
		updateNotificationEntries = getUpdateNotificationRegistrationEntries();	
		Calendar cal = Calendar.getInstance();

		for (UpdateNotificationRegistrationEntry entry : updateNotificationEntries) {
			cal.add(Calendar.DATE, -(entry.getTimeToLive()));

			List<IdentifierUpdateEvent> identifierUpdateEvents = personDao.getIdentifierUpdateEventsBeforeDate(cal.getTime(), entry.getUser());

			for(IdentifierUpdateEvent identifierUpdateEvent : identifierUpdateEvents) {
				log.trace("Deleting IdentifierUpdateEvent : " + identifierUpdateEvent.getIdentifierUpdateEventId());
				personDao.removeIdentifierUpdateEvent(identifierUpdateEvent);
			}
		 }
	}


}