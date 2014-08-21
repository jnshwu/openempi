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
package org.openhie.openempi.service;

import java.util.Date;
import java.util.List;

import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.LoggedLink;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.Race;
import org.openhie.openempi.model.ReviewRecordPair;
import org.openhie.openempi.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface PersonQueryService
{
	
	/**
	 * Returns the list of identifier domains known by the system
	 * 
	 * @return
	 */
	public List<IdentifierDomain> getIdentifierDomains();
	
	/**
	 * Returns the list of distinct identifier domain type codes
	 * 
	 */
	public List<String> getIdentifierDomainTypeCodes();
	
	/**
	 * Returns an instance of an IdentifierDomainAttribute associated with the identifier domain passed in and with the 
	 * name passed in as attributeName.
	 * 
	 * @param identifierDomain This must be an instance of an identifier domain that has been obtained
	 * either from a prior call to getIdentifierDomains() or via an association between an identifier
	 * domain and a person identifier. The only attribute that must be present is the identifierDomainId
	 * attribute which is only useful internally to an OpenEMPI instance.
	 * @param attributeName The name of the attribute.
	 * @return
	 */
	public IdentifierDomainAttribute getIdentifierDomainAttribute(IdentifierDomain identifierDomain, String attributeName);
	
	/**
	 * This method returns a list of all the identifier domain attributes associated with a given identifier domain.
	 * 
	 * @param identifierDomain This must be an instance of an identifier domain that has been obtained
	 * either from a prior call to getIdentifierDomains() or via an association between an identifier
	 * domain and a person identifier. The only attribute that must be present is the identifierDomainId
	 * attribute which is only useful internally to an OpenEMPI instance.
	 * @return List of IdentifierDomainAttributes found.
	 */
	public List<IdentifierDomainAttribute> getIdentifierDomainAttributes(IdentifierDomain identifierDomain);
	
	public Person findPersonById(PersonIdentifier identifier);
	
	/**
	 * This method returns a list of all person records that have either an exact or partial match with the
	 * identifier that was provided. The search accepts wildcards when searching on the identifier but will
	 * filter the results on an exact match on any of the identifier domain attributes that are provided.
	 * 
	 * @param identifier Expects the identifier with possible wildcards (such as 555-%) but can filter
	 * out the result set by including identifier domain attributes
	 * @return List of person records that match the search criteria
	 */
	public List<Person> findPersonsById(PersonIdentifier identifier);

	public PersonIdentifier getGlobalIdentifierById(PersonIdentifier identifier);

	public List<Person> findLinkedPersons(PersonIdentifier identifier);
	
	public List<Person> findLinkedPersons(Person person);
	
	public Person loadPerson(Integer personId);
	
	public List<Person> loadPersons(List<Integer> personIds);
	
	public List<Person> loadAllPersonsPaged(int firstRecord, int maxRecords);
	
	/**
	 * This method returns all the probable person link entries that have been identified by the
	 * matching algorithm. These entries need to be reviewed by a human operator and assigned a
	 * linked or unlinked status
	 * 
	 * @return List of unreviewed person link review entries.
	 */
	public List<ReviewRecordPair> loadAllUnreviewedPersonLinks();
	
	public List<ReviewRecordPair> loadUnreviewedPersonLinks(int maxResults);
	
	/**
	 * This method returns the specific probable review record pair entry identified by the
	 * id passed into the call.
	 * 
	 * @param reviewRecordPairId
	 * @return The populated review record pair entry or null if not found.
	 */
	public ReviewRecordPair loadReviewRecordPair(int reviewRecordPairId);
	
	/**
	 * This method returns a list of all person records that match any of the person attributes that
	 * are provided in the search object which acts as a template.
	 * 
	 * @param person Person object with any attributes provided as search criteria
	 * @return List of person records that match the search criteria
	 */
	public List<Person> findPersonsByAttributes(Person person);
	
	/**
	 * This method returns the single best representative record for each cluster
	 * of records associated with a given person record identifier. Each record
	 * may have been matched to one or more other records, forming a cluster of
	 * records. This method uses the configured Single Best Record algorithm to 
	 * select the single best representative from each cluster.
	 * 
	 * @param personIds A list of internal, key identifiers to person records.
	 * @return A list of single best records. The position of a record 
	 * in the response corresponds to the position of the key identifiers from which
	 * person and its cluster, the single best record was selected.
	 */
	public List<Person> getSingleBestRecords(List<Integer> personIds);
	
	public Person getSingleBestRecord(Integer personId);
	
	/**
	 * This method returns an identifier domain located using whichever key is provided
	 * for looking up the entity in the repository. The keys and the order in which they are used
	 * in locating the entry are as follows:
	 * <ul>
	 * <li>The internal key for the entry identifierDomainId.</li>
	 * <li>The namespace identifier alternate key</li>
	 * <li>The pair universal identifier/universal identifier type code</li>
	 * </ul>
	 * 
	 * @param identifierDomain
	 * @return
	 */
	public IdentifierDomain findIdentifierDomain(IdentifierDomain identifierDomain);
	
	public IdentifierDomain findIdentifierDomainByName(String identifierDomainName);
	
	/**
	 * This method returns all the links associated with the person whose
	 * record is passed as a parameter.
	 * 
	 * @param person The person record that will be used as a pivot to find all the links associated with it.
	 * 
	 * @return List of links associated with the person or an empty list if no records are found.
	 */
	public List<PersonLink> getPersonLinks(Person person);
	
	/**
	 * This method returns a list of all person records that match any of the person attributes that
	 * are provided in the search object which acts as a template. Unlike the findPersonsByAttributes 
	 * method, the similarity between the template person used in the search and the person records
	 * returned by the method, is based on the configuration of the matching algorithm and not on an
	 * exact match between the attributes.
	 * 
	 * @param person Person object with any attributes provided as search criteria
	 * @return List of person records that match the search criteria
	 */
	public List<Person> findMatchingPersonsByAttributes(Person person);
	
	/**
	 * This method returns a page of person records that match any of the person attributes that
	 * are provided in the search object which acts as a template.
	 * 
	 * @param person Person object with any attributes provided as search criteria
	 * @param firstResult Start index of the page
	 * @param maxResults Size of the page
	 * @return List of person records that match the search criteria
	 */
	public List<Person> findPersonsByAttributesPaged(Person person, int firstResult,
			int maxResults);
	
	/**
	 * This method returns a list of all person attributes that are supported by the
	 * current model. The list of attributes can be used for the purpose of defining the blocking
	 * algorithm, the matching algorithm, or to extract information about what the model
	 * supports.
	 * 
	 * @return A sorted list of the attributes
	 */
	public List<String> getPersonModelAllAttributeNames();
	
	/**
	 * This method returns a list of all person attributes that are supported by the
	 * current model, except those which designated to be custom field holders.
	 * The list of attributes can be used for the purpose of defining the blocking
	 * algorithm, the matching algorithm, or to extract information about what the model
	 * supports.
	 * 
	 * @return A sorted list of the attributes
	 */
	public List<String> getPersonModelAttributeNames();
	
	/**
	 * This method returns a list of all person attributes which designated to be custom
	 * field holders. The list of attributes can be used for the purpose of defining the blocking
	 * algorithm, the matching algorithm, or to extract information about what the model
	 * supports.
	 * 
	 * @return A sorted list of the custom field attributes
	 */
	public List<String> getPersonModelCustomAttributeNames();
	
	public Race findRaceByCode(String raceCode);
	
	public Race findRaceByName(String raceName);
	
	public Gender findGenderByCode(String genderCode);
	
	public Gender findGenderByName(String genderName);
	
	public List<PersonLink> getPersonLinksByLinkSource(Integer linkSourceId);

	public ReviewRecordPair getLoggedLink(Integer loggedLinkId);
	
	public int getLoggedLinksCount(int vectorValue);
	
	public List<LoggedLink> getLoggedLinks(final int vectorValue, final int start, final int maxResults);

	public int getNotificationCount(User user);

	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)	 
	public IdentifierUpdateEvent findIdentifierUpdateEvent(long identifierUpdateEventId);

	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public List<IdentifierUpdateEvent> retrieveNotifications(int startIndex, int maxEvents, Boolean removeRecords, User eventRecipient);
		 
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public List<IdentifierUpdateEvent> retrieveNotifications(Boolean removeRecords, User eventRecipient);
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)	 
	public List<IdentifierUpdateEvent> retrieveNotificationsByDate(Date startDate, Boolean removeRecords, User eventRecipient);
}
