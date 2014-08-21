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

import java.util.List;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.ReviewRecordPair;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Primary interface for the OpenEMPI. It provides access to most of the functionality
 * that is typically available by an EMPI.
 * 
 * @author Odysseas Pentakalos
 * @version $Revision:  $ $Date:  $
 */
public interface PersonManagerService
{
	/**
	 * Adds a person record to the EMPI. The system will first check to see if a user with the same identifier is already known to the system. If the person
	 * is known already then nothing further will be done. If the person record is new, then the system will first add the person to the system and will
	 * then invoke the matching algorithm to determine if the person is already known through other aliases.
	 * 
	 * @param person
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Person addPerson(Person person) throws ApplicationException;
	
	/**
	 * Updates the attributes maintained in the EMPI about the person. The system will locate the person record using the person identifiers as
	 * search criteria. If the record is not found, an exception is thrown. The attributes provided by the caller are used to update the person's record
	 * and then the matching algorithm is invoked to adjust the associations between person records based on the modifications that were made to the
	 * person's attributes.
	 * 
	 * @param person
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void updatePerson(Person person) throws ApplicationException;

	/**
	 * Updates the attributes maintained in the EMPI about the person. The system will locate the person record using the internal person identifier.
	 * If the record is not found, an exception is thrown. The attributes provided by the caller are used to update the person's record
	 * and then the matching algorithm is invoked to adjust the associations between person records based on the modifications that were made to the
	 * person's attributes.
	 * 
	 * @param person
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Person updatePersonById(Person person) throws ApplicationException;
	
	/**
	 * Deletes a person from the EMPI. The system locates the person record using the person identifiers as search criteria. If the record is not
	 * found an unchecked exception is thrown to notify the caller that this record does not exist in the system. If the record is found, the record
	 * is voided from the system rather than deleted to preserve a history.
	 *  
	 *  @param personIdentifier
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void deletePerson(PersonIdentifier personIdentifier) throws ApplicationException;
	
	/**
	 * Deletes a person from the EMPI. The system locates the person record using the person's internal unique identifier. If the record is not
	 * found an unchecked exception is thrown to notify the caller that this record does not exist in the system. If the record is found, the record
	 * is voided from the system rather than deleted to preserve a history.
	 *  
	 *  @param personIdentifier
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void deletePersonById(Person person) throws ApplicationException;
	
	/**
	 * Merges a person into another person record. This operation is usually the result of the creation of two duplicate records in the EMPI by one of the
	 * participating identifier domains. Once the error is identified at the source, it is corrected by merging the two patient records into a single
	 * record.
	 * @param retiredIdentifier to be retired
	 * @param survivingIdentifer to be preserved
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void mergePersons(PersonIdentifier retiredIdentifier, PersonIdentifier survivingIdentifer) throws ApplicationException;
	
	/**
	 * Links two person records together as identified by the parameters PersonLink. The PersonLink parameter provides
	 * references to the two individual person records that must be linked together.
	 * 
	 * @param PersonLink must provide references to the unique identifiers of the two person records that must be linked together.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public PersonLink linkPersons(PersonLink personLink) throws ApplicationException;
		
	/**
	 * Removes the link between the two records that are currently linked together into a single cluster. The second entry referenced in the
	 * link is removed from the cluster. If the cluster only has the two records identified by the link parameter than the operation of the method
	 * is simple. If the cluster consists of more than two records, then the first record in the pair continues to be associated with the
	 * other records in the cluster and only the edges that keep the second node linked to the complete graph are removed in addition to the
	 * link explicitly specified in the parameter.
	 * 
	 * For example, say we have records A, B, and C linked together. This implies that there are three links in the repository that maintain this
	 * relationship {(A,B), (A,C), (B,C)}. If the caller specifies that link (A,B) must be removed. then edge (A,C) will remain after the operation
	 * but links (A,B) and (B,C) will be eliminated to make sure that node B is removed from the cluster.
	 * 
	 * @param PersonLink must provide either the unique id of the link that must be removed or references to the two person records that must be unlinked
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void unlinkPersons(PersonLink personLink) throws ApplicationException;
	
	
	/**
	 * Locates a person record using the person's identifier(s)
	 * 
	 * @param person
	 */
	public Person getPerson(List<PersonIdentifier> personIdentifiers);
	
	/**
	 * Imports a person record to the EMPI. The system will first check to see if a user with the same identifier is already known to the system. If the person
	 * is known already then nothing further will be done. If the person record is new, then the system will add the person to the system.
	 * Unlike the addPerson method, the importPerson method does not invoke the matching logic and is intended for use by 
	 * data loaders that initialize the EMPI with records from another MPI before identifying duplicates.
	 *  
	 * @param person
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public Person importPerson(Person person) throws ApplicationException;
	
	/**
	 * Adds a new identifier domain to the EMPI repository. The system will first check to see if the identifier domain is already known to the
	 * system. If the identifier domain is known already then nothing further will be done. If the identifier domain is new, then the new identifier
	 * domain will be added to the repository.
	 * 
	 * @param identifierDomain
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public IdentifierDomain addIdentifierDomain(IdentifierDomain identifierDomain) throws ApplicationException;
	
	/**
	 * Update an existing identifier domain. The existing entry is located using the internal identifier.
	 * 
	 * @param identifierDomain
	 */
	public IdentifierDomain updateIdentifierDomain(IdentifierDomain identifierDomain) throws ApplicationException;
	
	/**
	 * Deletes an identifier domain from the repository. The caller must provide the internal primary key that identifier the identifier domain,
	 * the namespace identifier (another unique identifier for an identifier domain), or the pair universal identifier/university identifier type code.
	 *  
	 *  @param identifierDomain
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void deleteIdentifierDomain(IdentifierDomain identifierDomain) throws ApplicationException;
	
	/**
	 * This method finds person entries that do not have a global identifier assigned to them and for each cluster
	 * of person entries, it generates and assigns a new and unique global identifier. The installation must have
	 * a global identifier defined in the configuration file since that is where the method obtains the definition of
	 * the identifier domain of the global identifier from. Also, the system must be configured to use global identifiers
	 * otherwise the method will not assign identifiers.
	 *  
	 * @return Returns true if it successfully assigned identifiers in the global identifier domain to each entry or
	 * false otherwise
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public boolean assignGlobalIdentifier();
	
	/**
	 * This method generates a unique identifier domain identifier within the given universalIdentifierTypeCode. This method
	 * is intended for cases where new affinity domains are registering with OpenEMPI to join the group of affinity domains
	 * for which OpenEMPI collects person identity information for.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public IdentifierDomain obtainUniqueIdentifierDomain(String universalIdentifierTypeCode);
	
	/**
	 * The addIdentifierDomainAttribute method allows the caller to associate with a given identifier
	 * domain an attribute. The attribute consists of a name-value pair. This functionality is useful
	 * when OpenEMPI is used to support a Record Locator Service-type EHR and then each identifier domain
	 * corresponds to a site that provides patient services so it is useful to be able to associate
	 * arbitrary attributes to an identifier domain. Those attributes can be used to provide
	 * more user-friendly information about a health care provider site or institution.
	 *  
	 * @param identifierDomain This must be an instance of an identifier domain that has been obtained
	 * either from a prior call to getIdentifierDomains() or via an association between an identifier
	 * domain and a person identifier. The only attribute that must be present is the identifierDomainId
	 * attribute which is only useful internally to an OpenEMPI instance.
	 * 
	 * @param attributeName The name portion of the attribute
	 * @param attributeValue The value portion of the attribute
	 * @return Returns an instance of an IdentifierDomainAttribute object that was persisted after a successful
	 * add operation completion or null if the add operation fails.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public IdentifierDomainAttribute addIdentifierDomainAttribute(IdentifierDomain identifierDomain, String attributeName, String attributeValue);
	
	/**
	 * This method updates an existing identifier domain attribute with the name and value specified.
	 * 
	 * @param identifierDomainAttribute
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void updateIdentifierDomainAttribute(IdentifierDomainAttribute identifierDomainAttribute);

	/**
	 * This method removes an existing identifier domain attribute. The identifier domain attribute id
	 * attribute must be populated.
	 * 
	 * @param identifierDomainAttribute
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void removeIdentifierDomainAttribute(IdentifierDomainAttribute identifierDomainAttribute);
	
	/**
	 * This method invokes the underlying matching algorithm of the system and requests that
	 * all associations between records that point to the same physical entity should
	 * be deleted and new ones should be defined again from scratch. This operation is time
	 * consuming since it will cause the matching algorithm to attempt to match all the duplicate
	 * records in the repository.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void linkAllRecordPairs() throws ApplicationException;
	
	/**
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void initializeRepository() throws ApplicationException;
	
	/**
	 * This method iterates over all records in the repository and generates the custom
	 * fields that have been defined in the configuration file. It first clears all the existing
	 * values from their current settings so that there is no confusing overlap of values
	 * left in the repository.
	 */
	public void generateCustomFields() throws ApplicationException;
	
	/**
	 * This method clears the values of all custom fields. It is usually used before
	 * recalculating the values of custom fields using new configuration settings to prevent
	 * having a repository with mixed results in the custom fields from two different
	 * configuration settings.
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void clearCustomFields();
	
	/**
	 * This method removes all the links in the repository that have been generated by
	 * the current matching algorithm. This is usually done before applying a new matching
	 * algorithm configuration to the matching algorithm and before running the matching
	 * algorithm using the new settings against the entire repository.
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void clearAllLinks();
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void generateCustomFields(final List<Person> records);
	
	/**
	 * This method will add a new review record pair entry into the repository. This entry
	 * indicates that the two referenced records in the entry are a probable match but the
	 * determination cannot be made conclusively by the matching algorithm. A human operator
	 * must be involved to resolve with certainty the status of the probably match.
	 * 
	 * @param recordPair The review record pair entry with references to the two records that
	 * are a probable match, the source of the probable link, and the calculated match weight.
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void addReviewRecordPair(ReviewRecordPair recordPair) throws ApplicationException;

	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void addReviewRecordPairs(List<ReviewRecordPair> recordPairs) throws ApplicationException;

	/**
	 * This method manually creates a link between the two referenced records or marks thems
	 * as unlinked, depending on the field value recordsMatched. If the value of the field
	 * recordsMatched is true, then a link is created in the system between the two records
	 * and the review entry is implicitly removed from the work queue. If the values of recordsMatched is
	 * false, then the information on the decision is recorded and the entry is logically removed
	 * from the work queue.
	 * 
	 * @param recordPair The record review entry previously loaded.
	 * @exception ApplicationException This exception is thrown if the caller does not specify
	 * whether the link should be made or not
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void matchReviewRecordPair(ReviewRecordPair recordPair) throws ApplicationException;
	
	/**
	 * This method deletes the specified review record pair entry from the repository. This
	 * method should not be used as the review entries that have been reviewed, are no
	 * longer visible through the API.
	 * 
	 * @param recordPair The record review entry identified by the reviewRecordPairId attribute.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void deleteReviewRecordPair(ReviewRecordPair recordPair);
	
	/**
	 * This method deletes all the review record pair entries from the repository. This
	 * method should not be used as it is preferrable that reviewed record pair entries
	 * are preserved in the repository for historical reporting purposes.
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void deleteReviewRecordPairs();
	
	/**
	 * This method completely wipes out all data associated with a person record from
	 * the repository. This method is intended to be used to support testing and load
	 * testing efforts and should not be used in an production environment within an
	 * integration scenario. An EMPI is expected to maintain a historical trail of
	 * patient records and this method eliminates elements of that trail.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void removePerson(Integer personId) throws ApplicationException;
	
	/**
	 *The removeNotification method is used for permanently removing update notification entries from the system.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	 public void removeNotification(IdentifierUpdateEvent identifierUpdateEvent);
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	 public int removeNotifications(List<IdentifierUpdateEvent> identifierUpdateEvents);
	
	/**
	 *The cleanupUpdateNotifications method is used for permanently removing update notification entries from the system as per configured 
	 *  <time-to-live> value.
	 */
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void cleanupUpdateNotifications();
}
