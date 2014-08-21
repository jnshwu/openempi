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
package org.openhie.openempi.webservices.restful;

import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.openempi.webservices.restful.model.IdentifierDomainAttributeRequest;
import org.openempi.webservices.restful.model.MergePersonsRequest;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.LinkSource;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.ReviewRecordPair;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

public class PersonManagerResourceTest extends BaseRestfulServiceTestCase
{	

	public void testAddPerson() {
		Person person = buildTestPerson("555-55-5566");
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, person);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
	}
	
	public void testUpdatePerson() {
    	List<Person> persons = null;
       	Person person = new Person();
       	person.setGivenName("Odysseas");
       	
    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);

       	if (persons.size() == 0) {
       		assertFalse("Unable to get a person to update", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0);           	
       	person.setAddress2("TEMP1");
       	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("updatePerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, person);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.NOT_MODIFIED.getStatusCode());
		}
	}
	
	public void testUpdatePersonById() {
    	List<Person> persons = null;
       	Person person = new Person();
       	person.setGivenName("Odysseas");
       	
    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (persons.size() == 0) {
       		assertFalse("Unable to get a person to update", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0);            	
       	person.setAddress2("TEMP2");
       	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("updatePersonById")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, person);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.NOT_MODIFIED.getStatusCode());
		}
	}

	public void testDeletePerson() {
    	List<Person> persons = null;
       	Person person = new Person();
       	person.setGivenName("Odysseas");
       	
    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (persons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0);            	
    	PersonIdentifier personIdentifier = person.getPersonIdentifiers().iterator().next();; 

     	// remove person completely
        ClientResponse response = getWebResource().path("person-manager-resource")
        .path("removePersonById")
        .queryParam("personId", person.getPersonId().toString())
        .header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        .accept(MediaType.APPLICATION_JSON)
        .post(ClientResponse.class);

        if (response.getStatus() != Status.OK.getStatusCode()) {
            assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.NOT_FOUND.getStatusCode());
        }
	}
	
	public void testImportPerson() {
		  
		Person person = buildTestPerson("111-22-3333");
		
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("importPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, person);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
	}
	
	public void testDeletePersonById() {
    	List<Person> persons = null;
       	Person person = new Person();
       	person.setGivenName("Odysseas");
       	
    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (persons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0);            	
       	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("deletePersonById")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, person);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.NOT_FOUND.getStatusCode());
		}
	}
    
	public void testAddIdentifierDomain() {
		IdentifierDomain identifierDomain = new IdentifierDomain();
						identifierDomain.setNamespaceIdentifier("TEST");
						identifierDomain.setIdentifierDomainName("TEMP-TEST"); 
						
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("addIdentifierDomain")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, identifierDomain);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
	}
	
	public void testUpdateIdentifierDomain() {
    	List<IdentifierDomain> domains = 
    			getWebResource().path("person-query-resource")
    				.path("getIdentifierDomains")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<IdentifierDomain>>(){});
    	assertTrue("Failed to retrieve domains list.",
    			domains != null && domains.size() > 0);
    	
    	IdentifierDomain identifierDomain = null;
    	for (IdentifierDomain domain : domains) {
    		if (domain.getIdentifierDomainName().startsWith("TEMP-TEST")) {
    			identifierDomain = domain;
    		}
    	}
    	if (identifierDomain == null) {
    		return;
    	}
			
    	identifierDomain.setIdentifierDomainName("TEMP1-TEST");
    	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("updateIdentifierDomain")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, identifierDomain);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.BAD_REQUEST.getStatusCode());
		}
	}
	
	public void testDeleteIdentifierDomain() {
    	List<IdentifierDomain> domains = 
    			getWebResource().path("person-query-resource")
    				.path("getIdentifierDomains")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<IdentifierDomain>>(){});
    	assertTrue("Failed to retrieve domains list.",
    			domains != null && domains.size() > 0);
    	
    	IdentifierDomain identifierDomain = null;
    	for (IdentifierDomain domain : domains) {
    		if (domain.getIdentifierDomainName().startsWith("TEMP1-TEST")) {
    			identifierDomain = domain;
    		}
    	}
    	if (identifierDomain == null) {
    		return;
    	}
			
    	identifierDomain.setIdentifierDomainName("TEMP1");
    	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("deleteIdentifierDomain")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, identifierDomain);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.BAD_REQUEST.getStatusCode());
		}
	}
	
	public void testAddIdentifierDomainAttribute() {
      	List<IdentifierDomain> domains = 
    			getWebResource().path("person-query-resource")
    				.path("getIdentifierDomains")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<IdentifierDomain>>(){});
    	assertTrue("Failed to retrieve domains list.", domains != null && domains.size() > 0);

       	IdentifierDomain id = null;
    	for (IdentifierDomain domain : domains) {
    		if (domain.getIdentifierDomainName().startsWith("IHENA")) {
    			id = domain;
    		}
    	}
    	if (id == null) {
    		// no this kind of IdentifierDomain
    		return;
    	}   
    	
    	IdentifierDomainAttributeRequest identifierDomainAttributeRequest = new IdentifierDomainAttributeRequest(id, "IHENA", "200");	
    	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("addIdentifierDomainAttribute")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, identifierDomainAttributeRequest);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, false);
		}
	}
	
	public void testUpdateIdentifierDomainAttribute() {
       	List<IdentifierDomain> domains = 
    			getWebResource().path("person-query-resource")
    				.path("getIdentifierDomains")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<IdentifierDomain>>(){});
    	assertTrue("Failed to retrieve domains list.", domains != null && domains.size() > 0);

       	IdentifierDomain id = null;
    	for (IdentifierDomain domain : domains) {
    		if (domain.getIdentifierDomainName().startsWith("IHENA")) {
    			id = domain;
    		}
    	}
    	if (id == null) {
    		return;
    	}
    
    	IdentifierDomainAttributeRequest identifierDomainAttributeRequest= new IdentifierDomainAttributeRequest(id, "IHENA");
    	
    	IdentifierDomainAttribute dentifierDomainAttribute = 
    			getWebResource().path("person-query-resource")
        			.path("getIdentifierDomainAttribute")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.post(IdentifierDomainAttribute.class, identifierDomainAttributeRequest);
    	
    	if(dentifierDomainAttribute == null) {
    		return;
    	}
    	
    	dentifierDomainAttribute.setAttributeName("IHENA1");
    	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("updateIdentifierDomainAttribute")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, dentifierDomainAttribute);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, false);
		}
	}
	
	public void testRemoveIdentifierDomainAttribute() {
       	List<IdentifierDomain> domains = 
    			getWebResource().path("person-query-resource")
    				.path("getIdentifierDomains")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<IdentifierDomain>>(){});
    	assertTrue("Failed to retrieve domains list.", domains != null && domains.size() > 0);

       	IdentifierDomain id = null;
    	for (IdentifierDomain domain : domains) {
    		if (domain.getIdentifierDomainName().startsWith("IHENA")) {
    			id = domain;
    		}
    	}
    	if (id == null) {
    		return;
    	}
    
    	IdentifierDomainAttributeRequest identifierDomainAttributeRequest= new IdentifierDomainAttributeRequest(id, "IHENA1");
    	
    	IdentifierDomainAttribute dentifierDomainAttribute = 
    			getWebResource().path("person-query-resource")
        			.path("getIdentifierDomainAttribute")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.post(IdentifierDomainAttribute.class, identifierDomainAttributeRequest);
    	
    	if(dentifierDomainAttribute == null) {
    		return;
    	}
    	
    	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("removeIdentifierDomainAttribute")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, dentifierDomainAttribute);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, false);
		}
	}

	public void testAddReviewRecordPair() {
		
		if( !addNewTestPerson("111-11-1111",  "PersonTestLeft") ) {
			assertNotNull("Unable to add a new test person", null);   
			return;
		}

		if( !addNewTestPerson("222-22-2222",  "PersonTestRight") ) {
			assertNotNull("Unable to add a new test person", null);   
			return;
		}
    	
    	// person left
       	Person personLeft = findTestPerson("PersonTestLeft");         
       	
       	// person right
       	Person personRight = findTestPerson("PersonTestRight");         
 
       	if( personLeft == null || personRight == null ) {
			assertNotNull("Unable to find test person", null);  
       		return;
       	}
		
       	boolean result = addNewTestReviewRecordPair(personLeft, personRight);
       	
       	assertFalse("Incorrect status code received of adding ReviewRecordPair", result);
	}
		
	public void testDeleteReviewRecordPair() {
    	List<ReviewRecordPair> reviewRecordPairs = 
    			getWebResource().path("person-query-resource")
    				.path("loadAllUnreviewedPersonLinks")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<ReviewRecordPair>>(){});
    	assertTrue("Failed to retrieve ReviewRecordPairs list.", reviewRecordPairs != null && reviewRecordPairs.size() > 0);
    	
    	ReviewRecordPair reviewRecordPairFound = null;
		for( ReviewRecordPair pair : reviewRecordPairs) {
			if( pair.getPersonLeft().getGivenName().equals("PersonTestLeft") )
				reviewRecordPairFound = pair;
		}

		if(reviewRecordPairFound == null ) {
		   assertNotNull("Unable to retrieve reviewRecordPair", reviewRecordPairFound);   	
		   return;
		}
		
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("deleteReviewRecordPair")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, reviewRecordPairFound);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, false);
		}
		
		// delete person left and person right
		deleteTestPerson(reviewRecordPairFound.getPersonLeft());
		deleteTestPerson(reviewRecordPairFound.getPersonRight());
	}
	
	public void testAddReviewRecordPairForLink() {
		
		// add ReviewRecordPair
		if( !addNewTestPerson("333-33-3333",  "PersonTestLeft1") ) {
			assertNotNull("Unable to add a new test person", null);   
			return;
		}
		
		if( !addNewTestPerson("444-44-4444",  "PersonTestRight1") ) {
			assertNotNull("Unable to add a new test person", null);   
			return;
		}
    	
    	// person left
       	Person personLeft = findTestPerson("PersonTestLeft1");         
       	
       	// person right
       	Person personRight = findTestPerson("PersonTestRight1");         
 
       	if( personLeft == null || personRight == null ) {
			assertNotNull("Unable to find test person", null);  
       		return;
       	}
		
       	boolean result = addNewTestReviewRecordPair(personLeft, personRight);
       	
       	assertFalse("Incorrect status code received of adding ReviewRecordPair", result);
	} 
	
	public void testMatchReviewRecordPairLink() {
       	
       	// link ReviewRecordPair
		List<ReviewRecordPair> reviewRecordPairs = 
				getWebResource().path("person-query-resource")
					.path("loadAllUnreviewedPersonLinks")
					.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<ReviewRecordPair>>(){});
		assertTrue("Failed to retrieve ReviewRecordPairs list.", reviewRecordPairs != null && reviewRecordPairs.size() > 0);
		
		ReviewRecordPair reviewRecordPairFound = null;
		for( ReviewRecordPair pair : reviewRecordPairs) {
			if( pair.getPersonLeft().getGivenName().equals("PersonTestLeft1") )
				reviewRecordPairFound = pair;
		}
	
		if(reviewRecordPairFound == null ) {
		   assertNotNull("Unable to retrieve reviewRecordPair: " + reviewRecordPairFound);   	
		   return;
		}
		
		// Link
		reviewRecordPairFound.setRecordsMatch(true);
		
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("matchReviewRecordPair")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, reviewRecordPairFound);
	
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.BAD_REQUEST.getStatusCode());
		}
		
		// delete person left and person right
		deleteTestPerson(reviewRecordPairFound.getPersonLeft());
		deleteTestPerson(reviewRecordPairFound.getPersonRight());
	}
	
	public void testAddReviewRecordPairForUnlink() {
		
		// add ReviewRecordPair
		if( !addNewTestPerson("555-55-5555",  "PersonTestLeft2") ) {
			assertNotNull("Unable to add a new test person", null);   
			return;
		}

		if( !addNewTestPerson("666-66-6666",  "PersonTestRight2") ) {
			assertNotNull("Unable to add a new test person", null);   
			return;
		}
    	
    	// person left
       	Person personLeft = findTestPerson("PersonTestLeft2");         
       	
       	// person right
       	Person personRight = findTestPerson("PersonTestRight2");         
 
       	if( personLeft == null || personRight == null ) {
			assertNotNull("Unable to find test person", null);   
       		return;
       	}
		
       	boolean result = addNewTestReviewRecordPair(personLeft, personRight);
       	
       	assertFalse("Incorrect status code received of adding ReviewRecordPair", result);
	} 
	
	public void testMatchReviewRecordPairUnlink() {
       	
       	// link ReviewRecordPair
		List<ReviewRecordPair> reviewRecordPairs = 
				getWebResource().path("person-query-resource")
					.path("loadAllUnreviewedPersonLinks")
					.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<List<ReviewRecordPair>>(){});
		assertTrue("Failed to retrieve ReviewRecordPairs list.", reviewRecordPairs != null && reviewRecordPairs.size() > 0);
		
		ReviewRecordPair reviewRecordPairFound = null;
		for( ReviewRecordPair pair : reviewRecordPairs) {
			if( pair.getPersonLeft().getGivenName().equals("PersonTestLeft2") )
				reviewRecordPairFound = pair;
		}
	
		if(reviewRecordPairFound == null ) {
		   assertNotNull("Unable to retrieve reviewRecordPair: " + reviewRecordPairFound);   	
		   return;
		}
		
		// Unlink
		reviewRecordPairFound.setRecordsMatch(false);
		
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("matchReviewRecordPair")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, reviewRecordPairFound);
	
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.BAD_REQUEST.getStatusCode());
		}
		
		// delete person left and person right
		deleteTestPerson(reviewRecordPairFound.getPersonLeft());
		deleteTestPerson(reviewRecordPairFound.getPersonRight());
	}
	
	public void testMergePersons() {
		
		if( !addNewTestPerson("777-77-7777",  "PersonTestLeft3") ) {
			assertNotNull("Unable to add a new test person", null);   	
			return;
		}

		if( !addNewTestPerson("888-88-8888",  "PersonTestRight3") ) {
			assertNotNull("Unable to add a new test person", null);   
			return;
		}
    	
    	// person retired
       	Person personRetired = findTestPerson("PersonTestLeft3");         
       	
       	// person surviving
       	Person personSurviving = findTestPerson("PersonTestRight3");         
 
       	if( personRetired == null || personSurviving == null ) {
			assertNotNull("Unable to find test person", null);   
       		return;
       	}
       	
    	PersonIdentifier personIdentifierRetired = personRetired.getPersonIdentifiers().iterator().next();; 
    	PersonIdentifier personIdentifierSurviving = personSurviving.getPersonIdentifiers().iterator().next();; 
    	
       	MergePersonsRequest mergePersonsRequest = new MergePersonsRequest(personIdentifierRetired, personIdentifierSurviving);
       	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("mergePersons")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, mergePersonsRequest);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.NOT_FOUND.getStatusCode());
		}
		
		// delete person left and person right
		deleteTestPerson(personSurviving);
	}

	public void testLinkPersons() {
		if( !addNewTestPerson("999-99-9999",  "PersonTestLeft4") ) {
			assertNotNull("Unable to add a new test person", null);   	
			return;
		}

		if( !addNewTestPerson("999-88-7777",  "PersonTestRight4") ) {
			assertNotNull("Unable to add a new test person", null);   
			return;
		}
    	
    	// person left
       	Person personLeft = findTestPerson("PersonTestLeft4");         
       	
       	// person right
       	Person personRight = findTestPerson("PersonTestRight4");         

       	PersonLink personLink = new PersonLink(); 
       	personLink.setPersonLeft(personLeft);
      	personLink.setPersonRight(personRight);
      	personLink.setDateCreated(new java.util.Date());
      	personLink.setUserCreatedBy(Context.getUserContext().getUser());
      	personLink.setLinkSource(new LinkSource(2));
      	personLink.setWeight(1.0);
		
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("linkPersons")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personLink);
	
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.BAD_REQUEST.getStatusCode());
		}
	}	

	public void testUnlinkPersons() {
		
      	Person person = findTestPerson("PersonTestLeft4");            
       	if( person == null ) {
			assertNotNull("Unable to find test person", null);  
       		return;
       	} 	
       	
       	List<PersonLink> personLinks = getWebResource().path("person-query-resource")
        			.path("getPersonLinks")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<PersonLink>>(){}, person);
       	
       	if( personLinks == null ) {
			assertNotNull("Unable to find person links", null);  
       		return;       		
       	}
 
       	PersonLink personLink = personLinks.get(0);

		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("unlinkPersons")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personLink);
	
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.BAD_REQUEST.getStatusCode());
		}

	}
/*	
	public void testDeleteReviewRecordPairs() {
		
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("deleteReviewRecordPairs")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.NOT_MODIFIED.getStatusCode());
		}
	}
*/

	public boolean addNewTestReviewRecordPair(Person personLeft, Person personRight) {
      	// ReviewRecordPair
		ReviewRecordPair recordPair = new ReviewRecordPair();
		recordPair.setPersonLeft(personLeft);
		recordPair.setPersonRight(personRight);
		recordPair.setDateCreated(new java.util.Date());
		recordPair.setUserCreatedBy(Context.getUserContext().getUser());
		recordPair.setLinkSource(new LinkSource(1));
		recordPair.setWeight(1.0);
		
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("addReviewRecordPair")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, recordPair);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			// assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.BAD_REQUEST.getStatusCode());
			return false;
		}
		return true;
	}

	public boolean addNewTestPerson(String SSN, String givenName) {
		Person person = buildTestPerson(SSN);
       	person.setGivenName(givenName);
       	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, person);
		if (response.getStatus() != Status.OK.getStatusCode()) {
			// assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.NOT_MODIFIED.getStatusCode());
			return false;
		}
		return true;
	}

	public Person findTestPerson(String givenName) {
		
    	List<Person> persons = null;
       	Person person = new Person();
       	person.setGivenName(givenName);
       	
    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (persons.size() == 0) {
    		return null;
    	}
       	
       	return persons.get(0);            	
	}

	public boolean deleteTestPerson(Person person) {
       	
    	PersonIdentifier personIdentifier = person.getPersonIdentifiers().iterator().next(); 
    	
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("deletePerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personIdentifier);

		if (response.getStatus() != Status.OK.getStatusCode()) {
			return false;
		}
		return true;
	}

	public void testRemoveNotifications() {

			List<IdentifierUpdateEvent> identifierUpdateEvents =
	    			getWebResource().path("person-query-resource")
	    				.path("retrieveNotificationsByDate")
	    				.queryParam("date", "2013-01-25") //YYYY-MM-DD
	    				.queryParam("removeRecords", "false")
	    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
	    				.accept(MediaType.APPLICATION_XML)
	    				.get(new GenericType<List<IdentifierUpdateEvent>>(){});
	    	assertTrue("Failed to retrieve identifierUpdateEvents list paged.", identifierUpdateEvents != null && identifierUpdateEvents.size() > 0);
	    	System.out.println("testRetrieveNotificationsByDate:");

			for (IdentifierUpdateEvent identifierUpdateEvent : identifierUpdateEvents) {
				System.out.println(identifierUpdateEvent.toString());

		        ClientResponse response = getWebResource().path("person-manager-resource")
		                .path("removeNotificationById")
		                .queryParam("notificationId", identifierUpdateEvent.getIdentifierUpdateEventId().toString())
		                .header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		                .accept(MediaType.APPLICATION_JSON)
		                .post(ClientResponse.class);

		    	assertTrue("Failed to remove notification.", response.getStatus() != Status.OK.getStatusCode());
			}

		}
}
