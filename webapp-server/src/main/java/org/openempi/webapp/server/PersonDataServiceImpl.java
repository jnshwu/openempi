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
package org.openempi.webapp.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openempi.webapp.client.PersonDataService;
import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.LinkedPersonWeb;
import org.openempi.webapp.client.model.LoggedLinkListWeb;
import org.openempi.webapp.client.model.LoggedLinkSearchCriteriaWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.ReviewRecordPairWeb;
import org.openempi.webapp.client.model.UserFileWeb;
import org.openempi.webapp.client.model.FileLoaderConfigurationWeb;
import org.openempi.webapp.client.model.ParameterTypeWeb;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.FileLoader;
import org.openhie.openempi.loader.FileLoaderFactory;
import org.openhie.openempi.loader.FileLoaderManager;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.LoggedLink;
import org.openhie.openempi.model.ParameterType;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.ReviewRecordPair;
import org.openhie.openempi.model.User;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.UserManager;

public class PersonDataServiceImpl extends AbstractRemoteServiceServlet implements PersonDataService
{
	private final static String NOMINAL_FILE_LOADER = "nominalSetDataLoader";
	private final static String CONCURRENT_FILE_LOADER = "concurrentDataLoader";
	private final static String FLEXIBLE_FILE_LOADER = "flexibleDataLoader";
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	public String addPerson(PersonWeb personWeb) throws Exception {
		log.debug("Received request to add a new person entry to the repository.");
		
		authenticateCaller();	
		String msg = "";
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			org.openhie.openempi.model.Person person = ModelTransformer.mapToPerson(personWeb, org.openhie.openempi.model.Person.class);
			personService.addPerson(person);
		} catch (Throwable t) {
			// log.error("Failed to add person entry: " + t, t);
			msg = t.getMessage();			
			throw new Exception(msg);
		}
		return msg;
	}
	
	public String deletePerson(PersonIdentifierWeb personIdentifier) throws Exception {
		log.debug("Received request to delete person entry to the repository.");
		
		authenticateCaller();	
		String msg = "";
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			org.openhie.openempi.model.PersonIdentifier personId = ModelTransformer.map(personIdentifier, org.openhie.openempi.model.PersonIdentifier.class);
			personService.deletePerson(personId);
		} catch (Throwable t) {
			log.error("Failed to delete person entry: " + t, t);
			msg = t.getMessage();
		}
		return msg;
	}

	public String deletePerson(PersonWeb personWeb) throws Exception {
		log.debug("Received request to delete person entry to the repository.");
		
		authenticateCaller();
		String msg = "";
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			org.openhie.openempi.model.Person person = ModelTransformer.mapToPerson(personWeb, org.openhie.openempi.model.Person.class);
			personService.deletePersonById(person);
		} catch (Throwable t) {
			log.error("Failed while updating a person entry: " + t, t);
			msg = t.getMessage();
			throw new Exception(t.getMessage());
		}
		return msg;
	}
	
	public PersonWeb updatePerson(PersonWeb personWeb) throws Exception {
		log.debug("Received request to update the person entry in the repository.");
		
		authenticateCaller();
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			org.openhie.openempi.model.Person person = ModelTransformer.mapToPerson(personWeb, org.openhie.openempi.model.Person.class);
//			personService.updatePerson(person);			
			Person uopdatedPerson = personService.updatePersonById(person);
			
			return ModelTransformer.mapToPerson( uopdatedPerson, PersonWeb.class);

		} catch (Throwable t) {
			log.error("Failed while updating a person entry: " + t, t);
			throw new Exception(t.getMessage());
		}
	}

	public List<PersonWeb> getPersonsByIdentifier(PersonIdentifierWeb personIdentifier) throws Exception {
		log.debug("Received request to retrieve a filtered list of person records.");
		
		authenticateCaller();
		try {
			PersonQueryService personQueryService = Context.getPersonQueryService();
			org.openhie.openempi.model.PersonIdentifier personId = ModelTransformer.map(personIdentifier, org.openhie.openempi.model.PersonIdentifier.class);
			List<org.openhie.openempi.model.Person> persons = personQueryService.findPersonsById(personId);
			formatAllIdentifiersForPresentation(persons);
//			List<PersonWeb> dtos = ModelTransformer.mapList(persons, PersonWeb.class);
			List<PersonWeb> dtos = new java.util.ArrayList<PersonWeb>(persons.size());
			for (Person ps : persons) {
				PersonWeb pw = ModelTransformer.mapToPerson( ps, PersonWeb.class);
				dtos.add(pw);
			}
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public List<ReviewRecordPairWeb> getAllUnreviewedReviewRecordPairs()  throws Exception {
		log.debug("Received request to retrieve all the unreviewed record pair entries.");
		
		authenticateCaller();
		try {
			PersonQueryService personQueryService = Context.getPersonQueryService();
			List<ReviewRecordPair> recordPairs = personQueryService.loadAllUnreviewedPersonLinks();
			List<ReviewRecordPairWeb> dtos = new java.util.ArrayList<ReviewRecordPairWeb>(recordPairs.size());
			for (ReviewRecordPair pair : recordPairs) {
				ReviewRecordPairWeb dto = ModelTransformer.mapReviewRecordPair(pair, ReviewRecordPairWeb.class);
				dtos.add(dto);
			}
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<ReviewRecordPairWeb> getUnreviewedReviewRecordPairs()  throws Exception {
		log.debug("Received request to retrieve all the unreviewed record pair entries.");
		return getUnreviewedReviewRecordPairs(null);
	}
	
	public List<ReviewRecordPairWeb> getUnreviewedReviewRecordPairs(Integer maxResults)  throws Exception {
		log.debug("Received request to retrieve all the unreviewed record pair entries.");
		
		authenticateCaller();
		try {
			PersonQueryService personQueryService = Context.getPersonQueryService();
			List<ReviewRecordPair> recordPairs;
			if (maxResults == null) {
				recordPairs = personQueryService.loadAllUnreviewedPersonLinks();
			} else {
				recordPairs = personQueryService.loadUnreviewedPersonLinks(maxResults.intValue());
			}
			List<ReviewRecordPairWeb> dtos = new java.util.ArrayList<ReviewRecordPairWeb>(recordPairs.size());
			for (ReviewRecordPair pair : recordPairs) {
				ReviewRecordPairWeb dto = ModelTransformer.mapReviewRecordPair(pair, ReviewRecordPairWeb.class);
				dtos.add(dto);
			}
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public void matchReviewRecordPairs(ReviewRecordPairWeb reviewRecordPair)  throws Exception {
		log.debug("Received request to resolve an unreviewed record pair entry: " + reviewRecordPair);
		
		authenticateCaller();
		if (reviewRecordPair == null || reviewRecordPair.getReviewRecordPairId() == null) {
			throw new RuntimeException("The identifier of the review record pair entry must be provided.");
		}
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			ReviewRecordPair pair = Context.getPersonQueryService().loadReviewRecordPair(reviewRecordPair.getReviewRecordPairId());
			if (pair == null) {
				throw new RuntimeException("No review record pair entry found by this identifier.");
			}
			pair.setRecordsMatch(reviewRecordPair.getRecordsMatch());
			personService.matchReviewRecordPair(pair);
			log.debug("Completed recording the resolution of the unreviewed record pair entry.");
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public List<LinkedPersonWeb> getLinkedPersons(PersonWeb personParam) {
		return new ArrayList<LinkedPersonWeb>();
	}
	
	public List<PersonWeb> getMatchingPersons(PersonWeb person)  throws Exception {
		log.debug("Received request to retrieve a list of person records that match the person specified as a parameter.");
		
		authenticateCaller();
		try {
			PersonQueryService personQueryService = Context.getPersonQueryService();
			org.openhie.openempi.model.Person personTransformed = ModelTransformer.mapToPerson(person, org.openhie.openempi.model.Person.class);		
			List<org.openhie.openempi.model.Person> persons = personQueryService.findMatchingPersonsByAttributes(personTransformed);
			formatAllIdentifiersForPresentation(persons);
//			List<PersonWeb> dtos = ModelTransformer.mapList(persons, PersonWeb.class);
			List<PersonWeb> dtos = new java.util.ArrayList<PersonWeb>(persons.size());
			for (Person ps : persons) {
				PersonWeb pw = ModelTransformer.mapToPerson( ps, PersonWeb.class);
				dtos.add(pw);
			}
			for (PersonWeb personWeb : dtos) {
				attachLinkedPersons(personWeb);
			}
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public List<ReviewRecordPairWeb> getLoggedLinks(Integer vectorValue) {
		authenticateCaller();
		try {		
			PersonQueryService queryService = Context.getPersonQueryService();
			int count = queryService.getLoggedLinksCount(vectorValue);
			if (count == 0) {
				return new java.util.ArrayList<ReviewRecordPairWeb>();
			}
			List<LoggedLink> links = queryService.getLoggedLinks( vectorValue, 0, count );
			
			List<ReviewRecordPairWeb> reviewLinks = new java.util.ArrayList<ReviewRecordPairWeb>();
			for (LoggedLink link : links) {
				
				ReviewRecordPair pair = queryService.getLoggedLink(link.getLinkId());
				ReviewRecordPairWeb dto = ModelTransformer.mapReviewRecordPair(pair, ReviewRecordPairWeb.class);
				reviewLinks.add(dto);
			}
			return reviewLinks;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}	

	@Override
	public LoggedLinkListWeb getLoggedLinks( LoggedLinkSearchCriteriaWeb search) throws Exception {
		authenticateCaller();
		try {		
			PersonQueryService queryService = Context.getPersonQueryService();	
			
			int totalCount = queryService.getLoggedLinksCount(search.getVector());
			if (totalCount == 0) {
				
				LoggedLinkListWeb loggedLinkList = new LoggedLinkListWeb();		
				loggedLinkList.setTotalCount(0);
				loggedLinkList.setReviewRecordPairs(new java.util.ArrayList<ReviewRecordPairWeb>());	
				return loggedLinkList;
			}
			List<LoggedLink> links = queryService.getLoggedLinks( search.getVector(), search.getFirstResult(), search.getMaxResults() );
			
			List<ReviewRecordPairWeb> reviewLinks = new java.util.ArrayList<ReviewRecordPairWeb>();
			for (LoggedLink link : links) {
				
				ReviewRecordPair pair = queryService.getLoggedLink(link.getLinkId());
				ReviewRecordPairWeb dto = ModelTransformer.mapReviewRecordPair(pair, ReviewRecordPairWeb.class);
				reviewLinks.add(dto);
			}
			
			LoggedLinkListWeb loggedLinkList = new LoggedLinkListWeb();			
			loggedLinkList.setTotalCount(totalCount);
			loggedLinkList.setReviewRecordPairs(reviewLinks);	
			
			return loggedLinkList;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}	
	
	private void printPersons(List<PersonWeb> dtos) {
		for (PersonWeb person: dtos) {
			log.debug("Found person: " + person.getGivenName() + " " + person.getFamilyName() + " who is linked to: ");
			Set<LinkedPersonWeb> linkedSet = person.getLinkedPersons();
			for (LinkedPersonWeb link : linkedSet) {
				log.debug("\t" + link.getGivenName() + " " + link.getFamilyName());
			}
		}
	}

	public List<PersonWeb> getPersonsByAttribute(PersonWeb personParam) throws Exception {
		log.debug("Received request to retrieve a filtered list of person records by attributes.");
			
		authenticateCaller();		
		try {			
			PersonQueryService personQueryService = Context.getPersonQueryService();
			org.openhie.openempi.model.Person personTransformed = ModelTransformer.mapToPerson(personParam, org.openhie.openempi.model.Person.class);		
			List<org.openhie.openempi.model.Person> persons = personQueryService.findPersonsByAttributes(personTransformed);
			formatAllIdentifiersForPresentation(persons);
//			List<PersonWeb> dtos = ModelTransformer.mapList(persons, PersonWeb.class);
			List<PersonWeb> dtos = new java.util.ArrayList<PersonWeb>(persons.size());
			for (Person ps : persons) {
				PersonWeb pw = ModelTransformer.mapToPerson( ps, PersonWeb.class);
				dtos.add(pw);
			}
			for (PersonWeb personWeb : dtos) {
				attachLinkedPersons(personWeb);
			}
			
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	private void attachLinkedPersons(PersonWeb personWeb) {
		// Locate all linked persons
		PersonQueryService personQueryService = Context.getPersonQueryService();
		java.util.Set<Person> linkedPersons = new java.util.HashSet<Person>();
//		PersonIdentifierWeb idWeb = personWeb.getPersonIdentifiers().iterator().next();
//		log.debug("Searching for linked persons using identifier: " + idWeb);
//		PersonIdentifier identifier = ModelTransformer.map(idWeb, PersonIdentifier.class);
		org.openhie.openempi.model.Person personMaped = ModelTransformer.mapToPerson(personWeb, org.openhie.openempi.model.Person.class);
		Collection<Person> foundPersons = personQueryService.findLinkedPersons(personMaped);
		if (foundPersons == null || foundPersons.size() == 0) {
			return;
		}
		linkedPersons.addAll(foundPersons);
		
		// Convert them and attach them to the person object.
		formatAllIdentifiersForPresentation(linkedPersons);
		Set<LinkedPersonWeb> transformed = new HashSet<LinkedPersonWeb>();
		for (Person person : linkedPersons) {
			LinkedPersonWeb linkedPerson = ModelTransformer.map(person, LinkedPersonWeb.class);
			if (person.getPersonIdentifiers().size() > 0) {
				PersonIdentifier personId = person.getPersonIdentifiers().iterator().next();
				linkedPerson.setPersonIdentifier(personId.getIdentifier() + ":" + personId.getIdentifierDomain().getNamespaceIdentifier() + 
						":" + personId.getIdentifierDomain().getUniversalIdentifier() + ":" + personId.getIdentifierDomain().getUniversalIdentifierTypeCode());
			}
			transformed.add(linkedPerson);
		}
		personWeb.setLinkedPersons(transformed);
	}

	public List<PersonLinkWeb> getPersonLinks(PersonWeb personWeb) throws Exception {
		log.debug("Received request to return list of PersonLinkWeb: " + personWeb.getGivenName());
		
		authenticateCaller();
		
		try {
			PersonQueryService personService = Context.getPersonQueryService();
			org.openhie.openempi.model.Person person = ModelTransformer.mapToPerson(personWeb, org.openhie.openempi.model.Person.class);
			List<org.openhie.openempi.model.PersonLink> personLinks = personService.getPersonLinks(person);
			
			List<PersonLinkWeb> dtos = new java.util.ArrayList<PersonLinkWeb>(personLinks.size());
			for (PersonLink personLink : personLinks) {
				PersonLinkWeb dto = ModelTransformer.map(personLink, PersonLinkWeb.class);
				
				Person leftPerson = personService.loadPerson(personLink.getPersonLeft().getPersonId());
				Person rightPerson = personService.loadPerson(personLink.getPersonRight().getPersonId());
				
				dto.setLeftPerson(ModelTransformer.mapToPerson(leftPerson, PersonWeb.class));
				dto.setRightPerson(ModelTransformer.mapToPerson(rightPerson, PersonWeb.class));
				dtos.add(dto);
			}
			return dtos;			
						
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public String unlinkPersons(List<PersonLinkWeb> personLinks) throws Exception {
		log.debug("Received request to unlink persons: " + personLinks.size());
		
		authenticateCaller();
		String msg = "";			
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			
			for (PersonLinkWeb personLinkWeb : personLinks) {
				PersonLink personLink = ModelTransformer.map(personLinkWeb, org.openhie.openempi.model.PersonLink.class);
				personLink.setPersonLeft(ModelTransformer.mapToPerson(personLinkWeb.getLeftPerson(), org.openhie.openempi.model.Person.class));
				personLink.setPersonRight(ModelTransformer.mapToPerson(personLinkWeb.getRightPerson(), org.openhie.openempi.model.Person.class));	
				
				personService.unlinkPersons(personLink);
			}
			
		} catch (Throwable t) {
			log.error("Failed while unlink a personLink entry: " + t, t);
			msg = t.getMessage();
			throw new Exception(t.getMessage());
		}
		return msg;			
	}
	
	private void formatAllIdentifiersForPresentation(Collection<org.openhie.openempi.model.Person> persons) {
		for (org.openhie.openempi.model.Person person : persons) {
			log.trace("Found person: " + person.getGivenName() + " " + person.getFamilyName() + " " + person.getPostalCode() + " " + person.getState());
			for (org.openhie.openempi.model.PersonIdentifier identifier : person.getPersonIdentifiers()) {
				if (identifier.getIdentifierDomain().getNamespaceIdentifier() == null) {
					identifier.getIdentifierDomain().setNamespaceIdentifier("");
				}
				if (identifier.getIdentifierDomain().getUniversalIdentifier() == null) {
					identifier.getIdentifierDomain().setUniversalIdentifier("");
				}
				if (identifier.getIdentifierDomain().getUniversalIdentifierTypeCode() == null) {
					identifier.getIdentifierDomain().setUniversalIdentifierTypeCode("");
				}
			}
		}
	}
	
	public List<IdentifierDomainWeb> getIdentifierDomains() throws Exception {
		log.debug("Received request to retrieve the list of identifier domains.");
		
		authenticateCaller();
		try {
			PersonQueryService personQueryService = Context.getPersonQueryService();
			List<org.openhie.openempi.model.IdentifierDomain> domains = personQueryService.getIdentifierDomains();
			List<IdentifierDomainWeb> dtos = ModelTransformer.mapList(domains, IdentifierDomainWeb.class);
			return dtos;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}		
	}

	public IdentifierDomainWeb addIdentifierDomain(IdentifierDomainWeb identifierDomain) throws Exception {
		log.debug("Received request to add a new Identifier Domain entry to the repository.");
		
		authenticateCaller();
		String msg = "";
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			org.openhie.openempi.model.IdentifierDomain domain = ModelTransformer.map(identifierDomain, org.openhie.openempi.model.IdentifierDomain.class);
// 			personService.addIdentifierDomain(domain);
			IdentifierDomain newDomain = personService.addIdentifierDomain(domain);
			
			return ModelTransformer.map( newDomain, IdentifierDomainWeb.class);
		} catch (Throwable t) {
			// log.error("Failed to add person entry: " + t, t);
			msg = t.getMessage();			
			throw new Exception(msg);
		}
	}
	
	public IdentifierDomainWeb updateIdentifierDomain(IdentifierDomainWeb identifierDomain) throws Exception {
		log.debug("Received request to add a new Identifier Domain entry to the repository.");
		
		authenticateCaller();
		String msg = "";
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			org.openhie.openempi.model.IdentifierDomain domain = ModelTransformer.map(identifierDomain, org.openhie.openempi.model.IdentifierDomain.class);
// 			personService.updateIdentifierDomain(domain);
			
			IdentifierDomain updateDomain = personService.updateIdentifierDomain(domain);
			
			return ModelTransformer.map( updateDomain, IdentifierDomainWeb.class);
		} catch (Throwable t) {
			// log.error("Failed to add person entry: " + t, t);
			msg = t.getMessage();			
			throw new Exception(msg);
		}
	}
	
	public String deleteIdentifierDomain(IdentifierDomainWeb identifierDomain) throws Exception {
		log.debug("Received request to add a new Identifier Domain entry to the repository.");
		
		authenticateCaller();
		String msg = "";
		try {
			PersonManagerService personService = Context.getPersonManagerService();
			org.openhie.openempi.model.IdentifierDomain domain = ModelTransformer.map(identifierDomain, org.openhie.openempi.model.IdentifierDomain.class);
			personService.deleteIdentifierDomain(domain);
		} catch (Throwable t) {
			// log.error("Failed to add person entry: " + t, t);
			msg = t.getMessage();			
			throw new Exception(msg);
		}
		return msg;
	}
	
	public UserFileWeb addUserFile(UserFileWeb userFile) throws Exception {
		log.debug("Received request to add a new user file entry: " + userFile);
		
		authenticateCaller();
		try {
			UserManager userService = Context.getUserManager();
			org.openhie.openempi.model.UserFile theFile = ModelTransformer.map(userFile, org.openhie.openempi.model.UserFile.class);
			theFile = userService.saveUserFile(theFile);
			return ModelTransformer.map(theFile, UserFileWeb.class);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<UserFileWeb> getUserFiles(String username) throws Exception {
		log.debug("Received request to return list of files for user: " + username);
		
		authenticateCaller();
		try {
			UserManager userService = Context.getUserManager();
			User user = userService.getUserByUsername(username);
			List<org.openhie.openempi.model.UserFile> files = userService.getUserFiles(user);
			return ModelTransformer.mapList(files, UserFileWeb.class);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public void removeUserFile(Integer userFileId) {
		log.debug("Received request to remove user file entry: " + userFileId);
		
		authenticateCaller();
		try {
			UserManager userService = Context.getUserManager();
			org.openhie.openempi.model.UserFile userFile = new org.openhie.openempi.model.UserFile();
			userFile.setUserFileId(userFileId);
			userService.removeUserFile(userFile);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	public String importUserFile(UserFileWeb userFile) throws Exception {
		
		authenticateCaller();
		String msg = "";
		// log.debug("Received request to import the contents of file entry: " + userFile);
		if (userFile == null || userFile.getUserFileId() == null || userFile.getFilename() == null) {
        	msg = "Unable to import file with insufficient identifying attributes.";
			throw new Exception(msg);
		}

		try {
			String number = importFileEntry(userFile);		
			int dashIndex =  number.indexOf("-");
			String rowsProcessedStr= number.substring(0, dashIndex);
			String rowsImportedStr = number.substring(dashIndex+1, number.length());			
			
			UserManager userService = Context.getUserManager();
			org.openhie.openempi.model.UserFile userFileFound = userService.getUserFile(userFile.getUserFileId());
			userFileFound.setImported("Y");
			userFileFound.setRowsImported(Integer.parseInt(rowsImportedStr));
			userFileFound.setRowsProcessed(Integer.parseInt(rowsProcessedStr));
			userService.saveUserFile(userFileFound);
			msg = "File successfully imported";
		} catch (Throwable t) {
			msg = t.getMessage();			
			throw new Exception(msg);
		}
		return msg;
	}

	private String importFileEntry(UserFileWeb userFile)  throws Exception {
		
		authenticateCaller();
		String msg = "";
        try {
	        FileLoaderManager fileLoaderManager = new FileLoaderManager();
	        java.util.HashMap<String,Object> fileLoaderMap = userFile.getFileLoaderMap();

	        // set map values for importOnly and skipHeaderLine	      
	        java.util.HashMap<String,Object> map = new java.util.HashMap<String,Object>();
			map.put("context", Context.getApplicationContext());
/*			// map.put("isImport", true);
						
			if( userFile.getImportOnly() ) {
				fileLoaderManager.setUp(map);
			} else {
				fileLoaderManager.setUp();
			}
	        
			fileLoaderManager.setSkipHeaderLine(userFile.getSkipHeaderLine());
			return fileLoaderManager.loadFile(userFile.getFilename(), getFileLoaderAlias());
*/
			
			// file loader configuration
			fileLoaderManager.setSkipHeaderLine(false);
			for (String key : fileLoaderMap.keySet()) {
				if(key.equals("skipHeaderLine")) {
				   fileLoaderManager.setSkipHeaderLine( (Boolean)fileLoaderMap.get(key) );
				}
				map.put(key, fileLoaderMap.get(key));
			}
			
			if( map.size() > 1 ) {
				fileLoaderManager.setUp(map);
			} else {
				fileLoaderManager.setUp();
			}
			return fileLoaderManager.loadFile(userFile.getFilename(), userFile.getFileLoaderName());
			
        } catch (Exception e) {
        	log.error("Failed to parse and upload the file " + userFile.getFilename() + " due to " + e.getMessage());
        	msg = "Failed to import the file " + userFile.getFilename() + " due to file format or file does not exist";
			throw new Exception(msg);
        }
	}
	
	@Override
	public String dataProfileUserFile(UserFileWeb userFile) throws Exception {
		
			authenticateCaller();
			String msg = "";
	        try {
		        FileLoaderManager fileLoaderManager = new FileLoaderManager();
		        
		        fileLoaderManager.dataProfile(userFile.getFilename(), userFile.getUserFileId());		 

				UserManager userService = Context.getUserManager();
				org.openhie.openempi.model.UserFile userFileFound = userService.getUserFile(userFile.getUserFileId());
				userFileFound.setProfiled("Y");
				userFileFound.setProfileProcessed("In Processing");
				userService.saveUserFile(userFileFound);				
				msg = "Data profile operation successfully launched";	
				
				return msg;
	        } catch (Exception e) {
	        	log.error("Failed to process data profile  " + userFile.getFilename() + " due to " + e.getMessage());
	        	msg = "Failed to process data profile" + userFile.getFilename() + " due to file format or file does not exist";
				throw new Exception(msg);
	        }
	}
	
	public List<FileLoaderConfigurationWeb> getFileLoaderConfigurations() {
		Object obj;
		FileLoader fileLoader;	
		
		List<FileLoaderConfigurationWeb> fileLoaderConfigurations = new ArrayList<FileLoaderConfigurationWeb>();
		
		try {			
			obj =  Context.getApplicationContext().getBean(NOMINAL_FILE_LOADER); // file loader				
			fileLoader = FileLoaderFactory.getFileLoader(Context.getApplicationContext(), NOMINAL_FILE_LOADER);
			
			FileLoaderConfigurationWeb theLoader = new FileLoaderConfigurationWeb();
			theLoader.setLoaderName(NOMINAL_FILE_LOADER);
			
			ParameterType[] types = fileLoader.getParameterTypes();			
			List<ParameterType> list = Arrays.asList(types);
			
			List<ParameterTypeWeb> dtos = new java.util.ArrayList<ParameterTypeWeb>(list.size());
			for (ParameterType pt : list) {
				ParameterTypeWeb pw = ModelTransformer.mapToParameterType( pt, ParameterTypeWeb.class);
				dtos.add(pw);
			}
			theLoader.setParameterTypes(dtos);			
			fileLoaderConfigurations.add(theLoader);

		} catch (Throwable t) {}
				
		try {			
			obj =  Context.getApplicationContext().getBean(CONCURRENT_FILE_LOADER); // file loader hp				
			fileLoader = FileLoaderFactory.getFileLoader(Context.getApplicationContext(), CONCURRENT_FILE_LOADER);
			
			FileLoaderConfigurationWeb theLoader = new FileLoaderConfigurationWeb();
			theLoader.setLoaderName(CONCURRENT_FILE_LOADER);
			
			ParameterType[] types = fileLoader.getParameterTypes();			
			List<ParameterType> list = Arrays.asList(types);
			
			List<ParameterTypeWeb> dtos = new java.util.ArrayList<ParameterTypeWeb>(list.size());
			for (ParameterType pt : list) {
				ParameterTypeWeb pw = ModelTransformer.mapToParameterType( pt, ParameterTypeWeb.class);
				dtos.add(pw);
			}
			theLoader.setParameterTypes(dtos);			
			fileLoaderConfigurations.add(theLoader);
			
		} catch (Throwable t) {}
		
		try {			
			obj =  Context.getApplicationContext().getBean(FLEXIBLE_FILE_LOADER); // file loader map				
			fileLoader = FileLoaderFactory.getFileLoader(Context.getApplicationContext(), FLEXIBLE_FILE_LOADER);
			
			FileLoaderConfigurationWeb theLoader = new FileLoaderConfigurationWeb();
			theLoader.setLoaderName(FLEXIBLE_FILE_LOADER);
			
			ParameterType[] types = fileLoader.getParameterTypes();			
			List<ParameterType> list = Arrays.asList(types);
			
			List<ParameterTypeWeb> dtos = new java.util.ArrayList<ParameterTypeWeb>(list.size());
			for (ParameterType pt : list) {
				ParameterTypeWeb pw = ModelTransformer.mapToParameterType( pt, ParameterTypeWeb.class);
				dtos.add(pw);
			}
			theLoader.setParameterTypes(dtos);						
			fileLoaderConfigurations.add(theLoader);
			
		} catch (Throwable t) {}
		
		return fileLoaderConfigurations;
	}
	
	private String getFileLoaderAlias() {
		try {			
			Object obj =  Context.getApplicationContext().getBean(CONCURRENT_FILE_LOADER);
			if (obj != null) {
				return CONCURRENT_FILE_LOADER;
			}
			return NOMINAL_FILE_LOADER;
		} catch (Throwable t) {
			return NOMINAL_FILE_LOADER;
		}
	}
}
