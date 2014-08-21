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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.openempi.webservices.restful.model.IdentifierDomainAttributeRequest;
import org.openempi.webservices.restful.model.PersonPagedRequest;
import org.openempi.webservices.restful.model.StringList;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.Race;
import org.openhie.openempi.model.ReviewRecordPair;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.header.MediaTypes;

public class PersonQueryResourceTest extends BaseRestfulServiceTestCase
{
	
    public void testApplicationWadl() {
        String serviceWadl = getWebResource()
        		.path("application.wadl")
        		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        		.accept(MediaTypes.WADL)
        		.get(String.class);
        assertTrue("Looks like the expected wadl is not generated",
                serviceWadl.length() > 0);
    }
    
    public void testFindIdentifierDomainById() {
    	List<IdentifierDomain> domains = 
    			getWebResource().path("person-query-resource")
    				.path("getIdentifierDomains")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<IdentifierDomain>>(){});
    	assertTrue("Failed to retrieve domains list.",
    			domains != null && domains.size() > 0);
    	
    	Integer id = null;
    	for (IdentifierDomain domain : domains) {
    		if (domain.getIdentifierDomainName().startsWith("IHENA")) {
    			id = domain.getIdentifierDomainId();
    		}
    		if( id == null ) {
	    		if (domain.getIdentifierDomainName().startsWith("IHELOCAL")) {
	    			id = domain.getIdentifierDomainId();
	    		}
    		}
    	}
    	IdentifierDomain domain = new IdentifierDomain(id, null, null);
        IdentifierDomain identifierDomain = getWebResource().path("person-query-resource")
        			.path("findIdentifierDomain")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(IdentifierDomain.class, domain);
        assertNotNull("Unable to retrieve domain using the id: " + id, identifierDomain);
    }
    
    public void testFindGenderByCode() {
    	String genderCode="M";
        Gender gender = getWebResource().path("person-query-resource")
        			.queryParam("genderCode", genderCode)
        			.path("findGenderByCode")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.get(Gender.class);
        assertNotNull("The known gender code " + genderCode + " was not found", gender);
    }
    
    public void testFindGenderByName() {
    	String genderName="Male";
        Gender gender = getWebResource().path("person-query-resource")
        			.queryParam("genderName", genderName)
        			.path("findGenderByName")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.get(Gender.class);
        assertNotNull("The known gender name " + genderName + " was not found", gender);
    }
    
    public void testFindGenderByCodeUnknown() {
    	String genderCode="Unknown";
        ClientResponse clientResponse = getWebResource().path("person-query-resource")
        			.queryParam("genderCode", genderCode)
        			.path("findGenderByCode")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.get(ClientResponse.class);
        System.out.println(clientResponse.getStatus());
        assertTrue("The unknown gender code " + genderCode + " was found", clientResponse.getStatus() == 204);
    }
    
    public void testFindRaceByCode() {
    	String raceCode="1002-5";
        Race race = getWebResource().path("person-query-resource")
        			.queryParam("raceCode", raceCode)
        			.path("findRaceByCode")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.get(Race.class);
        assertNotNull("The known race code " + raceCode + " was not found", race);
    }
    
    public void testFindRaceByName() {
    	String raceName="American Indian";
        Race race = getWebResource().path("person-query-resource")
        			.queryParam("raceName", raceName)
        			.path("findRaceByName")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.get(Race.class);
        assertNotNull("The known race name " + raceName + " was not found", race);
    }
    
    public void testFindIdentifierDomainByNamespaceIdentifier() {
    	List<IdentifierDomain> domains = 
    			getWebResource().path("person-query-resource")
    				.path("getIdentifierDomains")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<IdentifierDomain>>(){});
    	assertTrue("Failed to retrieve domains list.",
    			domains != null && domains.size() > 0);
    	
    	IdentifierDomain id = null;
    	for (IdentifierDomain domain : domains) {
    		if (domain.getIdentifierDomainName().startsWith("OpenEMPI")) {
    			id = domain;
    		}
    	}
    	if (id == null) {
    		return;
    	}
    	
    	IdentifierDomain domain = new IdentifierDomain();
    	domain.setNamespaceIdentifier(id.getNamespaceIdentifier());
        IdentifierDomain identifierDomain = getWebResource().path("person-query-resource")
        			.path("findIdentifierDomain")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.post(IdentifierDomain.class, domain);
        assertNotNull("Unable to retrieve domain using the namespace identifier: " + id.getNamespaceIdentifier(),
        		identifierDomain);
    }
 
    public void testGetIdentifierDomainAttribute() {
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
    	
    	assertNotNull("Unable to retrieve IdentifierDomainAttribute list using the namespace identifier: " + id.getNamespaceIdentifier(), dentifierDomainAttribute);
    }
    
    public void testGetIdentifierDomainAttributes() {
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
    	
    	List<IdentifierDomainAttribute> dentifierDomainAttributes = 
    			getWebResource().path("person-query-resource")
        			.path("getIdentifierDomainAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.post(new GenericType<List<IdentifierDomainAttribute>>(){}, id);
    	
    	assertTrue("Unable to retrieve IdentifierDomainAttribute list using the namespace identifier: " + id.getNamespaceIdentifier(), dentifierDomainAttributes.size() > 0);
    }
    
    public void testGetPersonModelAllAttributeNames() {
    	StringList data = 
    			getWebResource().path("person-query-resource")
    				.path("getPersonModelAllAttributeNames")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(StringList.class);	
    	log.debug("Response is : " + data);
    	assertTrue("Failed to retrieve All Attribute Names.", data != null && data.getData() != null && data.getData().size() > 0);
    }

    public void testGetPersonModelAttributeNames() {
    	StringList data = 
    			getWebResource().path("person-query-resource")
    				.path("getPersonModelAttributeNames")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(StringList.class);	
    	log.debug("Response is : " + data);
    	assertTrue("Failed to retrieve Attribute Names.", data != null && data.getData() != null && data.getData().size() > 0);
    }
    
    public void testGetPersonModelCustomAttributeNames() {
    	StringList data = 
    			getWebResource().path("person-query-resource")
    				.path("getPersonModelCustomAttributeNames")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(StringList.class);	
    	log.debug("Response is : " + data);
    	assertTrue("Failed to retrieve Custom Attribute Names.", data != null && data.getData() != null && data.getData().size() > 0);
    }  
   
    
	
    public void testFindPersonById() {
/*    	List<Person> persons = 
    			getWebResource().path("person-query-resource")
    				.path("loadPersons")
    				.queryParam("personId", "8114")
    				.queryParam("personId", "8115")
    				.queryParam("personId", "49162")
    				.queryParam("personId", "45099")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<Person>>(){});
    	assertTrue("Failed to retrieve person list.",
    			persons != null && persons.size() > 0);
*/

       	Person personOne = new Person();
       	personOne.setGivenName("CampbellTest");
       	personOne.setFamilyName("WhiteTest");

       	Person personTwo = new Person();
       	personTwo.setGivenName("CampbellTest2");
       	personTwo.setFamilyName("WhiteTest2");
       	
       	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");       	
       	Date date;
       	try {
       		date = sdf.parse("1988-06-30");
       		personOne.setDateOfBirth(date);
     		personTwo.setDateOfBirth(date);
		} catch (ParseException e) {
			// This should never happen because the date is hard-coded.
		}        	
       	addTestPerson(personOne);
      	addTestPerson(personTwo);
      	
       	List<Person> persons = null;
       	persons = getWebResource().path("person-query-resource")
        			.path("findMatchingPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personOne);
       	
       	Person person = persons.get(0);       	
    	PersonIdentifier personIdentifier = person.getPersonIdentifiers().iterator().next();; 
 
        person = getWebResource().path("person-query-resource")
        			.path("findPersonById")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(Person.class, personIdentifier);
        assertNotNull("Unable to retrieve person using the personIdentifier: "+personIdentifier, person);
    }

 
    public void testgetGlobalIdentifierById() {
    	
       	List<Person> persons = null;
       	Person personCriteria = new Person();
       	personCriteria.setGivenName("CampbellTest");
       	personCriteria.setFamilyName("WhiteTest");
       	
       	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");       	
       	Date date;
       	try {
       		date = sdf.parse("1988-06-30");
       		personCriteria.setDateOfBirth(date);
		} catch (ParseException e) {
			// This should never happen because the date is hard-coded.
		}      	
       	persons = getWebResource().path("person-query-resource")
        			.path("findMatchingPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personCriteria);
       	
       	Person person = persons.get(0);       	
    	PersonIdentifier personIdentifier = person.getPersonIdentifiers().iterator().next();; 
 
    	PersonIdentifier globalIdentifier = getWebResource().path("person-query-resource")
        			.path("getGlobalIdentifierById")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(PersonIdentifier.class, personIdentifier);
        assertNotNull("Unable to get global identifier using the personIdentifier: "+globalIdentifier.getIdentifier(), personIdentifier);
    }
    
    public void testFindLinkedPersons() {

       	List<Person> persons = null;
       	Person personCriteria = new Person();
       	personCriteria.setGivenName("CampbellTest");
       	personCriteria.setFamilyName("WhiteTest");
       	
       	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");       	
       	Date date;
       	try {
       		date = sdf.parse("1988-06-30");
       		personCriteria.setDateOfBirth(date);
		} catch (ParseException e) {
			// This should never happen because the date is hard-coded.
		}      	
       	persons = getWebResource().path("person-query-resource")
        			.path("findMatchingPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personCriteria);
       	
       	Person person = persons.get(0);       	
    	PersonIdentifier personIdentifier = person.getPersonIdentifiers().iterator().next();; 
 
    	persons = getWebResource().path("person-query-resource")
        			.path("findLinkedPersons")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personIdentifier);
        assertNotNull("Unable to retrieve persons using the personIdentifier: "+personIdentifier, persons);
    }
 
    public void testGetPersonLinks() {
 
       	List<Person> persons = null;
       	Person personCriteria = new Person();
       	personCriteria.setGivenName("CampbellTest");
       	personCriteria.setFamilyName("WhiteTest");
       	
       	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");       	
       	Date date;
       	try {
       		date = sdf.parse("1988-06-30");
       		personCriteria.setDateOfBirth(date);
		} catch (ParseException e) {
			// This should never happen because the date is hard-coded.
		}      	
       	persons = getWebResource().path("person-query-resource")
        			.path("findMatchingPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personCriteria);
       	
       	Person person = persons.get(0);       	
 
       	List<PersonLink> personLinks = getWebResource().path("person-query-resource")
        			.path("getPersonLinks")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<PersonLink>>(){}, person);
        assertNotNull("Unable to retrieve person links using the person: ", personLinks);
    }
    
    public void testFindPersonsByAttributes() {
    	List<Person> persons = null;
       	Person person = new Person();
       	person.setGivenName("Cam%");
       	
    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
        assertNotNull("Unable to retrieve persons using the person as search: "+person, persons);
    }

    public void testFindPersonsByAttributesPaged() {
    	List<Person> persons = null;
       	Person person = new Person();
       	person.setGivenName("Cam%");
   
       	PersonPagedRequest personPagedRequest= new PersonPagedRequest(person, 1, 10);

    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributesPaged")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personPagedRequest);
    	
        assertNotNull("Unable to retrieve paged persons using the person as search: "+person, persons);
    }
    
    public void testFindMatchingPersonsByAttributes() {
    	List<Person> persons = null;
       	Person person = new Person();
       	person.setGivenName("CampbellTest");
       	person.setFamilyName("WhiteTest");
       	
       	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");       	
       	Date date;
       	try {
       		date = sdf.parse("1988-06-30");
           	person.setDateOfBirth(date);
		} catch (ParseException e) {
			// This should never happen because the date is hard-coded.
		}
        	
    	persons = getWebResource().path("person-query-resource")
        			.path("findMatchingPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
        assertNotNull("Unable to retrieve matching persons using the person as search: "+person, persons);
    }
    
    public void testLoadPerson() {

       	List<Person> persons = null;
       	Person personCriteria = new Person();
       	personCriteria.setGivenName("CampbellTest%");
       	personCriteria.setFamilyName("WhiteTest%");
       	
       	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");       	
       	Date date;
       	try {
       		date = sdf.parse("1988-06-30");
       		personCriteria.setDateOfBirth(date);
		} catch (ParseException e) {
			// This should never happen because the date is hard-coded.
		}      	
       	persons = getWebResource().path("person-query-resource")
        			.path("findMatchingPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personCriteria);

       	if (persons.size() == 0) {
       		assertFalse("Unable to get persons", persons.size() == 0);
    		return;
    	}
       	
    	Person person = persons.get(0);
        Person personFound = getWebResource().path("person-query-resource")
        			.path("loadPerson")
        			.queryParam("personId", person.getPersonId().toString())
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.get(Person.class);
        assertNotNull("Unable to retrieve person using the id: " + personFound);
 
		for (Person personDelete : persons) {
			deleteTestPerson(personDelete);
		}
    }
    
    public void testLoadAllPersonsPaged() {
    	List<Person> persons = 
    			getWebResource().path("person-query-resource")
    				.path("loadAllPersonsPaged")
    				.queryParam("firstRecord", "1")
    				.queryParam("maxRecords", "20")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<Person>>(){});
    	assertTrue("Failed to retrieve person list paged.", persons != null && persons.size() > 0);
    }
    
    public void testLoadAllUnreviewedPersonLinks() {
    	List<ReviewRecordPair> reviewRecordPairs = 
    			getWebResource().path("person-query-resource")
    				.path("loadAllUnreviewedPersonLinks")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<ReviewRecordPair>>(){});
    	if( reviewRecordPairs != null && reviewRecordPairs.size() > 0)
    		assertTrue("Failed to retrieve ReviewRecordPairs list.", reviewRecordPairs != null && reviewRecordPairs.size() > 0);
    }

   
    public void testLoadReviewRecordPair() {
    	List<ReviewRecordPair> reviewRecordPairs = 
    			getWebResource().path("person-query-resource")
    				.path("loadAllUnreviewedPersonLinks")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<ReviewRecordPair>>(){});
    	if(reviewRecordPairs != null && reviewRecordPairs.size() > 0) {
	    	assertTrue("Failed to retrieve ReviewRecordPairs list.", reviewRecordPairs != null && reviewRecordPairs.size() > 0);
	    	
	    	ReviewRecordPair reviewRecordPair = reviewRecordPairs.get(0);
	    	ReviewRecordPair reviewRecordPairFound = 
	    			getWebResource().path("person-query-resource")
	        			.path("loadReviewRecordPair")
	        			.queryParam("personLinkReviewId", reviewRecordPair.getReviewRecordPairId().toString())
	        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
	        			.accept(MediaType.APPLICATION_JSON)
	        			.get(ReviewRecordPair.class);
	    	
	        assertNotNull("Unable to retrieve reviewRecordPair using the id: " + reviewRecordPairFound);   
    	}
    }

    public void testGetIdentifierDomainTypeCodes() {
    	StringList data = 
    			getWebResource().path("person-query-resource")
    				.path("getIdentifierDomainTypeCodes")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(StringList.class);	
    	log.debug("Response is : " + data);
    	assertTrue("Failed to retrieve domain type code list.", data != null && data.getData() != null && data.getData().size() > 0);
    }
    
    public void testGetSingleBestRecordServiceRuleOne() {
		Person personOne = new Person();
		personOne.setAddress1("1st record");
		personOne.setCity("Palo Alto");
		personOne.setState("CA");
		personOne.setPostalCode("94301");
		personOne.setFamilyName("Record");
		personOne.setGivenName("Single");		
			PersonIdentifier pi = new PersonIdentifier();
			pi.setIdentifier("1234");
			IdentifierDomain id = new IdentifierDomain();
			id.setIdentifierDomainName("NID");
			id.setNamespaceIdentifier("NID");
			pi.setIdentifierDomain(id);		
		personOne.addPersonIdentifier(pi);
		
		// add personOne
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personOne);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
		
		Person personTwo = new Person();
		personTwo.setAddress1("2nd record");
		personTwo.setCity("Palo Alto");
		personTwo.setState("CA");
		personTwo.setPostalCode("94301");
		personTwo.setFamilyName("Recard");
		personTwo.setGivenName("Single");

		// add personTwo
		response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personTwo);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
		
		// Search: findPersonsByAttributes
		Person personQuery = new Person();
		personQuery.setFamilyName("Recard");
		personQuery.setGivenName("Single");
		
    	List<Person> persons = null;      	
    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personQuery);
        assertNotNull("Unable to retrieve persons using the person as search: "+personQuery, persons);
        
        
        // Get Single Best Record
		for (Person p : persons) {

	        Person personSBR = getWebResource().path("person-query-resource")
        			.path("getSingleBestRecord")
        			.queryParam("personId", p.getPersonId().toString())
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.get(Person.class);
	        // assertNotNull("Unable to retrieve person using the id: " + personSBR);        
			assertEquals("Last name must be Record", personSBR.getFamilyName(), "Record");
		}	
		
		// Clean test Persons (vioded)
    	List<Person> deletePersons = null;
    	
       	// delete Record
       	Person person = new Person();
       	person.setFamilyName("Record");
       	person.setGivenName("Single");
       	
       	deletePersons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (deletePersons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0); 
       	deleteTestPerson(person);
       	
       	// delete Recard
       	person = new Person();
       	person.setFamilyName("Recard");
       	person.setGivenName("Single");
       	
       	deletePersons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (deletePersons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0); 
       	deleteTestPerson(person);

    }
 
    public void testGetSingleBestRecordServiceRuleTwo() {
		Person personOne = new Person();
		personOne.setAddress1("1st record");
		personOne.setCity("Palo Alto");
		personOne.setState("CA");
		personOne.setPostalCode("94301");
		personOne.setFamilyName("Record");
		personOne.setGivenName("Best");
		
		// add personOne
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personOne);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
		
		Person personTwo = new Person();
		personTwo.setAddress1("2nd record");
		personTwo.setCity("Palo Alto");
		personTwo.setState("CA");
		personTwo.setPostalCode("94301");
		personTwo.setFamilyName("Record");
		personTwo.setGivenName("Bestt");

		// add personTwo
		response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personTwo);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
		
		Person personThree = new Person();
		personThree.setAddress1("2nd record");
		personThree.setCity("Palo Alto");
		personThree.setState("CA");
		personThree.setPostalCode("94301");
		personThree.setFamilyName("Record");
		personThree.setGivenName("Bests");

		// add personThree
		response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personThree);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
		
		Person personQuery = new Person();
		personQuery.setFamilyName("Record");
		personQuery.setGivenName("Best");

	   	List<Person> persons = null;      	
    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personQuery);
        assertNotNull("Unable to retrieve persons using the person as search: "+personQuery, persons);
        
        // Get Single Best Record
		for (Person p : persons) {

	        Person personSBR = getWebResource().path("person-query-resource")
        			.path("getSingleBestRecord")
        			.queryParam("personId", p.getPersonId().toString())
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_JSON)
        			.get(Person.class);
	        // assertNotNull("Unable to retrieve person using the id: " + personSBR);        
			assertEquals("First name must be Beste", personSBR.getGivenName(), "Bests");
		}	
		
		// Clean test Persons (vioded)
    	List<Person> deletePersons = null;
    	
       	// delete Best
       	Person person = new Person();
       	person.setFamilyName("Record");
       	person.setGivenName("Best");
       	
       	deletePersons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (deletePersons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0); 
       	deleteTestPerson(person);
       	
       	// delete Bestt
       	person = new Person();
       	person.setFamilyName("Record");
       	person.setGivenName("Bestt");
       	
       	deletePersons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (deletePersons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0); 
       	deleteTestPerson(person);
       	
       	// delete Bests
       	person = new Person();
       	person.setFamilyName("Record");
       	person.setGivenName("Bests");
       	
       	deletePersons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (deletePersons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0); 
       	deleteTestPerson(person);
    }
    
    
    public void testGetSingleBestRecords() {
		Person personOne = new Person();
		personOne.setAddress1("1st record");
		personOne.setCity("Palo Alto");
		personOne.setState("CA");
		personOne.setPostalCode("94301");
		personOne.setFamilyName("Record");
		personOne.setGivenName("Best");
		
		// add personOne
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personOne);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
		
		Person personTwo = new Person();
		personTwo.setAddress1("2nd record");
		personTwo.setCity("Palo Alto");
		personTwo.setState("CA");
		personTwo.setPostalCode("94301");
		personTwo.setFamilyName("Record");
		personTwo.setGivenName("Bestt");

		// add personTwo
		response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personTwo);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}
		
		Person personThree = new Person();
		personThree.setAddress1("2nd record");
		personThree.setCity("Palo Alto");
		personThree.setState("CA");
		personThree.setPostalCode("94301");
		personThree.setFamilyName("Record");
		personThree.setGivenName("Bests");

		// add personThree
		response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, personThree);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.CONFLICT.getStatusCode());
		}  
        
		Person personQuery = new Person();
		personQuery.setFamilyName("Record");

	   	List<Person> persons = null;      	
    	persons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, personQuery);
        assertNotNull("Unable to retrieve persons using the person as search: "+personQuery, persons);
        
        // Get list person ids
        List<Integer> personIds = new java.util.ArrayList<Integer>();
		for (Person p : persons) {
			personIds.add(p.getPersonId());
		}	
 
	   	persons = getWebResource().path("person-query-resource")
    				.path("getSingleBestRecords")
    				.queryParam("personId", personIds.get(0).toString())
    				.queryParam("personId", personIds.get(1).toString())
    				 .queryParam("personId", personIds.get(2).toString())
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<Person>>(){});
    	assertTrue("Failed to retrieve person list.", persons != null && persons.size() > 0);			
    	assertEquals("First name must be Beste", persons.get(0).getGivenName(), "Bests");
       	assertEquals("First name must be Beste", persons.get(1).getGivenName(), "Bests");
       	assertEquals("First name must be Beste", persons.get(2).getGivenName(), "Bests");
       	
		// Clean test Persons (vioded)
    	List<Person> deletePersons = null;
    	
       	// delete Best
       	Person person = new Person();
       	person.setFamilyName("Record");
       	person.setGivenName("Best");
       	
       	deletePersons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (deletePersons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0); 
       	deleteTestPerson(person);
       	
       	// delete Bestt
       	person = new Person();
       	person.setFamilyName("Record");
       	person.setGivenName("Bestt");
       	
       	deletePersons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (deletePersons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0); 
       	deleteTestPerson(person);
       	
       	// delete Bests
       	person = new Person();
       	person.setFamilyName("Record");
       	person.setGivenName("Bests");
       	
       	deletePersons = getWebResource().path("person-query-resource")
        			.path("findPersonsByAttributes")
        			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        			.accept(MediaType.APPLICATION_XML)
        			.post(new GenericType<List<Person>>(){}, person);
 
       	if (deletePersons.size() == 0) {
       		assertFalse("Unable to get a person to delete", persons.size() == 0);
    		return;
    	}
       	
       	person = persons.get(0); 
       	deleteTestPerson(person);
    }

	public boolean addTestPerson(Person person) {
       	
		// add personOne
		ClientResponse response = getWebResource().path("person-manager-resource")
		.path("addPerson")
		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
		.accept(MediaType.APPLICATION_JSON)
		.put(ClientResponse.class, person);
		// If this is not a success then the only other correct behavior is that
		// the person already existed and was not added, in which case we should
		// get back a NOT_MODIFIED status code.
		if (response.getStatus() != Status.OK.getStatusCode()) {
			return false;
		}
		return true;
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
	public void testGetNotificationCount() {
    	Integer count;
    	count =
    			getWebResource().path("person-query-resource")
    				.path("getNotificationCount")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_XML)
    				.get(Integer.class);
    	assertTrue("Failed to get notification count.", count != null);
    	System.out.println("testGetNotificationCount:");
		System.out.println(count);
    }
	
	

	

	
	public void testRetrieveNotifications() {
		List<IdentifierUpdateEvent> identifierUpdateEvents = 
    			getWebResource().path("person-query-resource")
    				.path("retrieveNotifications")
    				.queryParam("firstRecord", "0")
    				.queryParam("maxRecords", "1")
    				.queryParam("removeRecords", "true")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_XML)
    				.get(new GenericType<List<IdentifierUpdateEvent>>(){});
    	assertTrue("Failed to retrieve identifierUpdateEvents list paged.", identifierUpdateEvents != null && identifierUpdateEvents.size() > 0);
    	System.out.println("testRetrieveNotifications:");
		for (IdentifierUpdateEvent identifierUpdateEvent : identifierUpdateEvents) {
		System.out.println(identifierUpdateEvent.toString());
		}
    }
		
	public void testRetrieveNotificationsByDate() {
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
		}
    }
    
}
