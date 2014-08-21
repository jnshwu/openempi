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

import org.openempi.webapp.client.model.FileLoaderConfigurationWeb;
import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.LinkedPersonWeb;
import org.openempi.webapp.client.model.LoggedLinkListWeb;
import org.openempi.webapp.client.model.LoggedLinkSearchCriteriaWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.UserFileWeb;
import org.openempi.webapp.client.model.ReviewRecordPairWeb;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PersonDataServiceAsync
{
	public void addPerson(PersonWeb person, AsyncCallback<String> callback);
	
	public void deletePerson(PersonIdentifierWeb identifier, AsyncCallback<String> callback);

	public void deletePerson(PersonWeb person, AsyncCallback<String> callback);
	
	public void updatePerson(PersonWeb person, AsyncCallback<PersonWeb> callback);
	
	public void getIdentifierDomains(AsyncCallback<List<IdentifierDomainWeb>> callback);
	
	public void addIdentifierDomain(IdentifierDomainWeb identifierDomain, AsyncCallback<IdentifierDomainWeb> callback);
	
	public void updateIdentifierDomain(IdentifierDomainWeb identifierDomain, AsyncCallback<IdentifierDomainWeb> callback);
	
	public void deleteIdentifierDomain(IdentifierDomainWeb identifierDomain, AsyncCallback<String> callback);
	
	public void getPersonsByIdentifier(PersonIdentifierWeb identifier, AsyncCallback<List<PersonWeb>> callback);
	
	public void getLinkedPersons(PersonWeb person, AsyncCallback<List<LinkedPersonWeb>> callback);

	public void getPersonLinks(PersonWeb person, AsyncCallback<List<PersonLinkWeb>> callback);
	
	public void unlinkPersons(List<PersonLinkWeb> personLinks, AsyncCallback<String> callback);

	public void getMatchingPersons(PersonWeb person, AsyncCallback<List<PersonWeb>> callback);
	
	public void getPersonsByAttribute(PersonWeb person, AsyncCallback<List<PersonWeb>> callback);

	public void addUserFile(UserFileWeb userFile, AsyncCallback<UserFileWeb> callback);
	
	public void getUserFiles(String username, AsyncCallback<List<UserFileWeb>> callback);
	
	public void getFileLoaderConfigurations(AsyncCallback<List<FileLoaderConfigurationWeb>> callback);
	
	public void importUserFile(UserFileWeb userFile, AsyncCallback<String> callback);
	
	public void dataProfileUserFile(UserFileWeb userFile, AsyncCallback<String> callback);
	
	public void removeUserFile(Integer userFileId, AsyncCallback<Void> callback);
		
	public void getAllUnreviewedReviewRecordPairs(AsyncCallback<List<ReviewRecordPairWeb>> callback);
	
	public void getUnreviewedReviewRecordPairs(AsyncCallback<List<ReviewRecordPairWeb>> callback);
	
	public void getUnreviewedReviewRecordPairs(Integer maxResults, AsyncCallback<List<ReviewRecordPairWeb>> callback);
	
	public void matchReviewRecordPairs(ReviewRecordPairWeb reviewRecordPair, AsyncCallback<Void> callback);
	
	public void getLoggedLinks(Integer vectorValue, AsyncCallback<List<ReviewRecordPairWeb>> callback);
	
	public void getLoggedLinks(LoggedLinkSearchCriteriaWeb search, AsyncCallback<LoggedLinkListWeb> callback);
}
