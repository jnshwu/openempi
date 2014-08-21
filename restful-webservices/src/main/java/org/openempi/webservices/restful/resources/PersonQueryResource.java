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
package org.openempi.webservices.restful.resources;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openempi.webservices.restful.model.IdentifierDomainAttributeRequest;
import org.openempi.webservices.restful.model.PersonPagedRequest;
import org.openempi.webservices.restful.model.StringList;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.Race;
import org.openhie.openempi.model.ReviewRecordPair;
import org.openhie.openempi.service.PersonQueryService;

@Path("/person-query-resource")
public class PersonQueryResource
{
	@GET
	@Path("/findGenderByCode")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Gender findGenderByCode(@QueryParam("genderCode") String genderCode) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.findGenderByCode(genderCode);
	}

	@GET
	@Path("/findGenderByName")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Gender findGenderByName(@QueryParam("genderName") String genderName) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.findGenderByName(genderName);
	}
	
	@GET
	@Path("/findRaceByCode")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Race findRaceByCode(@QueryParam("raceCode") String raceCode) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.findRaceByCode(raceCode);
	}

	@GET
	@Path("/findRaceByName")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Race findRaceByName(@QueryParam("raceName") String raceName) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.findRaceByName(raceName);
	}
	
	@GET
	@Path("/getIdentifierDomainTypeCodes")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public StringList getIdentifierDomainTypeCodes() {
		PersonQueryService personQueryService = getPersonQueryService(); 
		List<String> typeCodes = personQueryService.getIdentifierDomainTypeCodes();
		return new StringList(typeCodes);
	}
 
	@POST
	@Path("/findIdentifierDomain")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public IdentifierDomain findIdentifierDomain(IdentifierDomain identifierDomain) {
		PersonQueryService personQueryService = getPersonQueryService();
		identifierDomain = personQueryService.findIdentifierDomain(identifierDomain);
		return identifierDomain;
	}	

	@GET
	@Path("/getIdentifierDomains")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<IdentifierDomain> getIdentifierDomains() {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.getIdentifierDomains();
	}
	
	@POST
	@Path("/getIdentifierDomainAttributes")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<IdentifierDomainAttribute> getIdentifierDomainAttributes(IdentifierDomain identifierDomain) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.getIdentifierDomainAttributes(identifierDomain);
	}	
	
	@POST
	@Path("/getIdentifierDomainAttribute")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public IdentifierDomainAttribute getIdentifierDomainAttribute(IdentifierDomainAttributeRequest identifierDomainAttributeRequest) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.getIdentifierDomainAttribute(identifierDomainAttributeRequest.getIdentifierDomain(),
				identifierDomainAttributeRequest.getAttributeName());
	}	

	@GET
	@Path("/getPersonModelAllAttributeNames")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public StringList getPersonModelAllAttributeNames() {
		PersonQueryService personQueryService = getPersonQueryService(); 
		List<String> personAllAttributes = personQueryService.getPersonModelAllAttributeNames();
		return new StringList(personAllAttributes);
	}
	
	@GET
	@Path("/getPersonModelAttributeNames")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public StringList getPersonModelAttributeNames() {
		PersonQueryService personQueryService = getPersonQueryService(); 
		List<String> personAttributes = personQueryService.getPersonModelAttributeNames();
		return new StringList(personAttributes);
	}
	
	@GET
	@Path("/getPersonModelCustomAttributeNames")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public StringList getPersonModelCustomAttributeNames() {
		PersonQueryService personQueryService = getPersonQueryService(); 
		List<String> personCustomAttributes = personQueryService.getPersonModelCustomAttributeNames();
		return new StringList(personCustomAttributes);
	}
	
	@POST
	@Path("/findPersonById")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person findPersonById(PersonIdentifier identifier) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.findPersonById(identifier);
	}

	@POST
	@Path("/getGlobalIdentifierById")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public PersonIdentifier getGlobalIdentifierById(PersonIdentifier identifier) {
		PersonQueryService personQueryService = getPersonQueryService();
		PersonIdentifier globalIdentifier = personQueryService.getGlobalIdentifierById(identifier);
		if( globalIdentifier == null )		
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		else
			return globalIdentifier;
	}

	@GET
	@Path("/getSingleBestRecord")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person getSingleBestRecord(@QueryParam("personId") Integer personId) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.getSingleBestRecord(personId);
	}
	
	@GET
	@Path("/getSingleBestRecords")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Person> getSingleBestRecords(@QueryParam("personId") List<Integer> personIds) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.getSingleBestRecords(personIds);
	}
	
	@POST
	@Path("/findLinkedPersons")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Person> findLinkedPersons(PersonIdentifier identifier) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.findLinkedPersons(identifier);
	}

	@POST
	@Path("/getPersonLinks")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<PersonLink> getPersonLinks(Person person) {
		PersonQueryService personQueryService = getPersonQueryService();
		List<PersonLink> links = personQueryService.getPersonLinks(person);
		for (PersonLink link : links) {
			Person leftPerson = personQueryService.loadPerson(link.getPersonLeft().getPersonId());
			Person rightPerson = personQueryService.loadPerson(link.getPersonRight().getPersonId());
			link.setPersonLeft(leftPerson);
			link.setPersonRight(rightPerson);
		}
		return links; 
	}
	
	// search 
	@POST
	@Path("/findPersonsByAttributes")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Person> findPersonsByAttributes(Person person) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.findPersonsByAttributes(person);
	}
	
	@POST
	@Path("/findPersonsByAttributesPaged")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Person> findPersonsByAttributesPaged(PersonPagedRequest request) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.findPersonsByAttributesPaged(request.getPerson(),
				request.getFirstResult(), request.getMaxResults());
	}
	
	// matching search
	@POST
	@Path("/findMatchingPersonsByAttributes")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Person> findMatchingPersonsByAttributes(Person person) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.findMatchingPersonsByAttributes(person);
	}
	
	@GET
	@Path("/loadPerson")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person loadPerson(@QueryParam("personId") Integer personId) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.loadPerson(personId);
	}
	
	@GET
	@Path("/loadAllPersonsPaged")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Person> loadAllPersonsPaged(@QueryParam("firstRecord") Integer firstRecord, @QueryParam("maxRecords") Integer maxRecords) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.loadAllPersonsPaged(firstRecord,maxRecords);
	}
	
	@GET
	@Path("/loadPersons")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Person> loadPersons(@QueryParam("personId") List<Integer> personIds) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.loadPersons(personIds);
	}
	
	@GET
	@Path("/loadAllUnreviewedPersonLinks")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ReviewRecordPair> loadAllUnreviewedPersonLinks() {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.loadAllUnreviewedPersonLinks();
	}
	
	@GET
	@Path("/loadUnreviewedPersonLinks")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<ReviewRecordPair> loadUnreviewedPersonLinks( @QueryParam("maxRecords") Integer maxRecords) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.loadUnreviewedPersonLinks(maxRecords);
	}
	
	@GET
	@Path("/loadReviewRecordPair")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ReviewRecordPair loadReviewRecordPair(@QueryParam("personLinkReviewId") Integer personLinkReviewId) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.loadReviewRecordPair(personLinkReviewId);
	}
	
	@GET
	@Path("/getNotificationCount")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String getNotificationCount() {
		PersonQueryService personQueryService = getPersonQueryService();
	
		Integer count =  personQueryService.getNotificationCount(Context.getUserContext().getUser());
		
		return Integer.toString(count);
	}
	
	@GET
	@Path("/retrieveNotifications")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<IdentifierUpdateEvent> retrieveNotifications(
			@DefaultValue("0")  @QueryParam("firstRecord") Integer firstRecord, 
			@DefaultValue("0")  @QueryParam("maxRecords") Integer maxRecords, 
			@DefaultValue("false") @QueryParam("removeRecords") Boolean removeRecords) {
		PersonQueryService personQueryService = getPersonQueryService();
		if (firstRecord == 0 && maxRecords == 0) {
			return personQueryService.retrieveNotifications(removeRecords, Context.getUserContext().getUser());
		}
		else 
		{
			return personQueryService.retrieveNotifications(firstRecord, maxRecords, removeRecords, Context.getUserContext().getUser());
		}
	}
	
	/***
	@GET
	@Path("/retrieveNotifications")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<IdentifierUpdateEvent> retrieveNotifications(@QueryParam("removeRecords") Boolean removeRecords) {
		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.retrieveNotifications(removeRecords, Context.getUserContext().getUser());
	}
	***/
	
	@GET
	@Path("/retrieveNotificationsByDate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<IdentifierUpdateEvent> retrieveNotificationsByDate(
			@QueryParam("date") String date,
			@DefaultValue("false") @QueryParam("removeRecords") Boolean removeRecords) {
		
		Date dateObj;
		
		try
		{
			Calendar cal = javax.xml.bind.DatatypeConverter.parseDateTime(date);
			dateObj = cal.getTime();
			
		}
		catch(java.lang.IllegalArgumentException e)
		{
			e.printStackTrace();
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

		PersonQueryService personQueryService = getPersonQueryService();
		return personQueryService.retrieveNotificationsByDate(dateObj, removeRecords, Context.getUserContext().getUser());
	}
	
	
	private PersonQueryService getPersonQueryService() {
		return org.openhie.openempi.context.Context.getPersonQueryService();
	}
}
