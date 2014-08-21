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
package org.openempi.webapp.client;

import java.util.List;

import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.LinkedPersonWeb;
import org.openempi.webapp.client.model.LoggedLinkListWeb;
import org.openempi.webapp.client.model.LoggedLinkSearchCriteriaWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.ReviewRecordPairWeb;
import org.openempi.webapp.client.model.UserFileWeb;
import org.openempi.webapp.client.model.FileLoaderConfigurationWeb;

import com.google.gwt.user.client.rpc.RemoteService;

public interface PersonDataService extends RemoteService
{
	public String addPerson(PersonWeb person) throws Exception;

	public String deletePerson(PersonIdentifierWeb identifier) throws Exception;

	public String deletePerson(PersonWeb person) throws Exception;
	
	public PersonWeb updatePerson(PersonWeb person) throws Exception;
	
	public List<IdentifierDomainWeb> getIdentifierDomains() throws Exception;
	
	public IdentifierDomainWeb addIdentifierDomain(IdentifierDomainWeb identifierDomain) throws Exception;
	
	public IdentifierDomainWeb updateIdentifierDomain(IdentifierDomainWeb identifierDomain) throws Exception;
	
	public String deleteIdentifierDomain(IdentifierDomainWeb identifierDomain) throws Exception;
	
	public List<PersonWeb> getPersonsByIdentifier(PersonIdentifierWeb identifier) throws Exception;
	
	public List<PersonWeb> getPersonsByAttribute(PersonWeb person) throws Exception;

	public List<PersonWeb> getMatchingPersons(PersonWeb person) throws Exception;
	
	public List<LinkedPersonWeb> getLinkedPersons(PersonWeb person) throws Exception;
	
	public List<PersonLinkWeb> getPersonLinks(PersonWeb person) throws Exception;
	
	public String unlinkPersons(List<PersonLinkWeb> personLinks) throws Exception;
	
	public UserFileWeb addUserFile(UserFileWeb userFile) throws Exception;
	
	public List<UserFileWeb> getUserFiles(String username) throws Exception;
	
	public List<FileLoaderConfigurationWeb> getFileLoaderConfigurations() throws Exception;
	
	public String importUserFile(UserFileWeb userFile) throws Exception;
	
	public String dataProfileUserFile(UserFileWeb userFile) throws Exception;
	
	public void removeUserFile(Integer userFileId) throws Exception;
	
	public List<ReviewRecordPairWeb> getAllUnreviewedReviewRecordPairs() throws Exception;
	
	public List<ReviewRecordPairWeb> getUnreviewedReviewRecordPairs() throws Exception;

	public List<ReviewRecordPairWeb> getUnreviewedReviewRecordPairs(Integer maxResults) throws Exception;
	
	public void matchReviewRecordPairs(ReviewRecordPairWeb reviewRecordPair) throws Exception;
	
	public List<ReviewRecordPairWeb> getLoggedLinks(Integer vectorValue) throws Exception;
	
	public LoggedLinkListWeb getLoggedLinks(LoggedLinkSearchCriteriaWeb search) throws Exception;
	
	// We need to figure out how to do the following in a non-matching algorithm specific way
	// or by pushing it out to a matching-algorithm specific GUI (more likely choice)
//	public List<PersonPairWeb> testScorePairs();
}
