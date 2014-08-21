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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.header.MediaTypes;

public class RHEAIntegrationTest extends BaseRestfulServiceTestCase
{
    public void testApplicationWadl() {
        String serviceWadl = getWebResource()
        		.path("application.wadl")
        		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        		.accept(MediaTypes.WADL)
        		.get(String.class);
        assertTrue("Looks like the expected wadl is not generated", serviceWadl.length() > 0);
    }

    public void testRHEAItegration() {
    	
    	// 1. Create a new patient registration
		Person personOne = new Person();
		personOne.setFamilyName("Patient");
		personOne.setGivenName("Some");
		Gender gender = new Gender();
		gender.setGenderCode("M");
		personOne.setGender(gender);
		personOne.setDateOfBirth(StringToDate("1977-02-15"));		
		personOne.setDistrict("Rwamagana");
		personOne.setSector("Fumbwe");
		personOne.setCell("Mununu");
		personOne.setVillage("Cyingara");

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
		
		
		// 2. Locate the patient's registration record using the ID		
		PersonIdentifier personIdentifier = new PersonIdentifier();
		personIdentifier.setIdentifier("1234");
		IdentifierDomain identifierDomain = new IdentifierDomain();
		identifierDomain.setIdentifierDomainName("NID");
		identifierDomain.setNamespaceIdentifier("NID");
		personIdentifier.setIdentifierDomain(identifierDomain);		
	    
        Person person = getWebResource().path("person-query-resource")
			.path("findPersonById")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(Person.class, personIdentifier);
        assertNotNull("Unable to retrieve person using the personIdentifier: "+personIdentifier, person);

        // Get the single best record for the patient record. The record should be returned.
	    Person personSBR1 = getWebResource().path("person-query-resource")
			.path("getSingleBestRecord")
			.queryParam("personId", person.getPersonId().toString())
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_JSON)
			.get(Person.class);
	    // assertNotNull("Unable to retrieve person using the id: " + personSBR);        
		assertTrue("Person one is returned.  The IDs are same", personSBR1.getPersonId().equals(person.getPersonId()));    
        
		// 3. Search for the record using the demographics attributes
		Person personQuery1 = new Person();
		personQuery1.setFamilyName("Patient");
		personQuery1.setDateOfBirth(StringToDate("1977-02-15"));
			Gender genderQuery = new Gender();
			genderQuery.setGenderCode("M");
		personQuery1.setGender(genderQuery);
		
	   	List<Person> persons = null;      	
    	persons = getWebResource().path("person-query-resource")
			.path("findPersonsByAttributes")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(new GenericType<List<Person>>(){}, personQuery1);
        assertNotNull("Unable to retrieve persons using the person as search: "+personQuery1, persons);   
       
        
        // 4. Search for the record using the demographics attributes
		Person personQuery2 = new Person();
		personQuery2.setFamilyName("Patient");
		// Custom field Custom6: 
		// name as DateofBirth, function as DateTransformationFunction and parameter name dateFormat and value yyyy are configured in mpi-config.xml
		personQuery2.setCustom6("1977"); 
		Gender genderQuery2 = new Gender();
		genderQuery2.setGenderCode("M");
		personQuery2.setGender(genderQuery2);
		
		persons = null;      	
    	persons = getWebResource().path("person-query-resource")
			.path("findPersonsByAttributes")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(new GenericType<List<Person>>(){}, personQuery2);
        assertNotNull("Unable to retrieve persons using the person as search: "+personQuery2, persons);   
        
        
        // 5. Get the patient's global identifier domain   
        // person = persons.get(0);       	
    	// personIdentifier = person.getPersonIdentifiers().iterator().next();
	    PersonIdentifier globalIdentifier = getWebResource().path("person-query-resource")
			.path("getGlobalIdentifierById")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(PersonIdentifier.class, personIdentifier);
	    assertNotNull("Unable to get global identifier using the personIdentifier: "+globalIdentifier.getIdentifier(), personIdentifier);     
	   
	    
	    // 6. Create a new patient registration 	    
		Person personTwo = new Person();
		personTwo.setFamilyName("Patient");
		personTwo.setGivenName("Some");
		Gender genderTwo = new Gender();
		genderTwo.setGenderCode("M");
		personTwo.setGender(genderTwo);
		personTwo.setDateOfBirth(StringToDate("1977-02-15"));		
		personTwo.setDistrict("Rwamagana");
		personTwo.setSector("Fumbwe");
		personTwo.setCell("Mununu");
		personTwo.setVillage("Cyingara");

		PersonIdentifier piTwo = new PersonIdentifier();
		piTwo.setIdentifier("2345");
		IdentifierDomain idTwo = new IdentifierDomain();
		idTwo.setIdentifierDomainName("OMRS-Rwamagana");
		idTwo.setNamespaceIdentifier("OMRS364");
		piTwo.setIdentifierDomain(idTwo);		
		personTwo.addPersonIdentifier(piTwo);
	
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
		
		
		// 7. Locate the patient's registration record using the ID	
		personIdentifier = new PersonIdentifier();
		personIdentifier.setIdentifier("2345");
		identifierDomain = new IdentifierDomain();
		identifierDomain.setIdentifierDomainName("OMRS-Rwamagana");
		identifierDomain.setNamespaceIdentifier("OMRS364");
		personIdentifier.setIdentifierDomain(identifierDomain);		
	    
        person = getWebResource().path("person-query-resource")
			.path("findPersonById")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(Person.class, personIdentifier);
        assertNotNull("Unable to retrieve person using the personIdentifier: "+personIdentifier, person); 
       
        
        // 8. Get the patient's global identifier domain
	    globalIdentifier = getWebResource().path("person-query-resource")
			.path("getGlobalIdentifierById")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(PersonIdentifier.class, personIdentifier);
	    assertNotNull("Unable to get global identifier using the personIdentifier: "+globalIdentifier.getIdentifier(), personIdentifier);
 
		
	    // 9. Get the single best record for the patient record. The first record should be returned.
	    Person personSBR = getWebResource().path("person-query-resource")
			.path("getSingleBestRecord")
			.queryParam("personId", person.getPersonId().toString())
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_JSON)
			.get(Person.class);
	    // assertNotNull("Unable to retrieve person using the id: " + personSBR);    
	    // This check below is dependent on the configuration of the matching algorithm
	    // we cannot always make sure that the matching algorithm is setup to match this scenario.
		assertTrue("Person one is returned.  The IDs are different", personSBR.getPersonId().equals(personSBR1.getPersonId()));    
	
		
	// 10. Update the first record and add an identifier to the record
		
		// Get already exist Identifier Domain "RAM"
	   	List<IdentifierDomain> domains = 
    			getWebResource().path("person-query-resource")
    				.path("getIdentifierDomains")
    				.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    				.accept(MediaType.APPLICATION_JSON)
    				.get(new GenericType<List<IdentifierDomain>>(){});
    	assertTrue("Failed to retrieve domains list.", domains != null && domains.size() > 0);
    	
    	IdentifierDomain idDomain = null;
    	for (IdentifierDomain domain : domains) {
    		if (domain.getIdentifierDomainName().startsWith("RAM")) {
    			idDomain = domain;
    		}
    	}
    	if (idDomain == null) {
    		return;
    	}
    	
    	IdentifierDomain domain = new IdentifierDomain();
    	domain.setNamespaceIdentifier(id.getNamespaceIdentifier());
        IdentifierDomain identifierDomainExist = getWebResource().path("person-query-resource")
			.path("findIdentifierDomain")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_JSON)
			.post(IdentifierDomain.class, domain);
        assertNotNull("Unable to retrieve domain using the namespace identifier: " + id.getNamespaceIdentifier(), identifierDomain);
      	
        // Create new Person Identifier with domain "RAM"
        PersonIdentifier piUpdate = new PersonIdentifier();
		piUpdate.setIdentifier("666");
		piUpdate.setIdentifierDomain(identifierDomainExist);	
		
		// Update the person by adding a new Person Identifier
		person.addPersonIdentifier(piUpdate);        
        
		response = getWebResource().path("person-manager-resource")
			.path("updatePerson")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_JSON)
			.put(ClientResponse.class, person);

		if (response.getStatus() != Status.OK.getStatusCode()  && response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
			assertFalse("Incorrect status code received of " + response, response.getStatus() == Status.NOT_MODIFIED.getStatusCode());
		}		
		
		
		// 11. Search for the two records using the demographics attributes. Should return both records
		Person personQuery3 = new Person();
		personQuery3.setFamilyName("Patient%");
		// Custom field Custom6: 
		// name as DateofBirth, function as DateTransformationFunction and parameter name dateFormat and value yyyy are configured in mpi-config.xml
		personQuery2.setCustom6("1977"); 
		Gender genderQuery3 = new Gender();
		genderQuery3.setGenderCode("M");
		personQuery3.setGender(genderQuery3);
		
	    persons = null;      	
	   	persons = getWebResource().path("person-query-resource")
			.path("findPersonsByAttributes")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(new GenericType<List<Person>>(){}, personQuery3);
        assertNotNull("Unable to retrieve persons using the person as search: "+personQuery3, persons);          				
    }
    
    
/*    public void testDeleteTestPersonById() {
    	PersonIdentifier personIdentifier = new PersonIdentifier();
		personIdentifier.setIdentifier("1234");
		IdentifierDomain identifierDomain = new IdentifierDomain();
		identifierDomain.setIdentifierDomainName("NID");
		identifierDomain.setNamespaceIdentifier("NID");
		personIdentifier.setIdentifierDomain(identifierDomain);		
	    
        Person person = getWebResource().path("person-query-resource")
			.path("findPersonById")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(Person.class, personIdentifier);
        assertNotNull("Unable to retrieve person using the personIdentifier: "+personIdentifier, person);
        
        ClientResponse response = getWebResource().path("person-manager-resource")
    		.path("deletePerson")
    		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    		.accept(MediaType.APPLICATION_JSON)
    		.put(ClientResponse.class, personIdentifier);
        log.debug("The status code from the first person delete was: " + response.getStatus());
        
        PersonIdentifier piTwo = new PersonIdentifier();
		piTwo.setIdentifier("2345");
		IdentifierDomain idTwo = new IdentifierDomain();
		idTwo.setIdentifierDomainName("OMRS-Rwamagana");
		idTwo.setNamespaceIdentifier("OMRS364");
		piTwo.setIdentifierDomain(idTwo);
		
		person = getWebResource().path("person-query-resource")
			.path("findPersonById")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(Person.class, piTwo);
		assertNotNull("Unable to retrieve person using the personIdentifier: " + piTwo, person);
    
        response = getWebResource().path("person-manager-resource")
    		.path("deletePerson")
    		.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
    		.accept(MediaType.APPLICATION_JSON)
    		.put(ClientResponse.class, piTwo);
        log.debug("The status code from the second person delete was: " + response.getStatus());
    }    
*/
	// Remove person by Id completely
    public void testRemovePersonById() {
    	PersonIdentifier personIdentifier = new PersonIdentifier();
		personIdentifier.setIdentifier("1234");
		IdentifierDomain identifierDomain = new IdentifierDomain();
		identifierDomain.setIdentifierDomainName("NID");
		identifierDomain.setNamespaceIdentifier("NID");
		personIdentifier.setIdentifierDomain(identifierDomain);		
	    
        Person person = getWebResource().path("person-query-resource")
			.path("findPersonById")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(Person.class, personIdentifier);
        assertNotNull("Unable to retrieve person using the personIdentifier: "+personIdentifier, person);
       
        ClientResponse response = getWebResource().path("person-manager-resource")
        .path("removePersonById")
        .queryParam("personId", person.getPersonId().toString())
        .header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
        .accept(MediaType.APPLICATION_JSON)
        .post(ClientResponse.class);
        log.debug("The status code from the first person remove was: " + response.getStatus());       
        
                     
        PersonIdentifier piTwo = new PersonIdentifier();
		piTwo.setIdentifier("2345");
		IdentifierDomain idTwo = new IdentifierDomain();
		idTwo.setIdentifierDomainName("OMRS-Rwamagana");
		idTwo.setNamespaceIdentifier("OMRS364");
		piTwo.setIdentifierDomain(idTwo);
		
		person = getWebResource().path("person-query-resource")
			.path("findPersonById")
			.header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
			.accept(MediaType.APPLICATION_XML)
			.post(Person.class, piTwo);
		assertNotNull("Unable to retrieve person using the personIdentifier: " + piTwo, person);
    
	    response = getWebResource().path("person-manager-resource")
	    	        .path("removePersonById")
	    	        .queryParam("personId", person.getPersonId().toString())
	    	        .header(OPENEMPI_SESSION_KEY_HEADER, getSessionKey())
	    	        .accept(MediaType.APPLICATION_JSON)
	    	        .post(ClientResponse.class);
        log.debug("The status code from the second person remove was: " + response.getStatus());
    }        
    
	public static Date StringToDate(String strDate){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		if( strDate != null && !strDate.isEmpty() ) {
			try {
				date = df.parse(strDate);
				// System.out.println("Today = " + df.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
}