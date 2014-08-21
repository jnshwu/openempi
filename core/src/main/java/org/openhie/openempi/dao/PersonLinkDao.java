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
package org.openhie.openempi.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhie.openempi.model.LinkSource;
import org.openhie.openempi.model.LoggedLink;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.ReviewRecordPair;

public interface PersonLinkDao extends UniversalDao
{
	public void addPersonLink(PersonLink personLink);
	
	public void addPersonLinks(List<PersonLink> personLinks);

	public List<PersonLink> getPersonLinks(Person person);
	
	public Map<Long,Integer> getClusterIdByRecordIdMap(Integer sourceId);
	
	public Integer getClusterId(Long[] recordIds, Integer sourceId);
	
	public Integer getClusterId(Set<Long> recordIds, Integer sourceId);
	
	public void convertReviewLinkToLink(ReviewRecordPair recordPair);
	
	public List<PersonLink> getPersonLinksByLinkSource(Integer linkSourceId);
	
	public List<PersonLink> getPersonLinks(Integer clusterId);
	
	public List<PersonLink> getPersonLinks(Set<Long> recordIds);
	
	public PersonLink getPersonLink(Person leftPerson, Person rightPerson);
	
	public void removeLink(PersonLink personLink);
	
	public int removeAllLinks();
	
	public int removeLinksBySource(LinkSource linkSource);
	
	public void addReviewRecordPair(ReviewRecordPair reviewRecordPair);
	
	public void addReviewRecordPairs(List<ReviewRecordPair> reviewRecordPairs);
	
	public List<ReviewRecordPair> getAllUnreviewedReviewRecordPairs();
	
	public List<ReviewRecordPair> getUnreviewedReviewRecordPairs(int maxResults);
	
	public ReviewRecordPair getReviewRecordPair(int reviewRecordPairId);
	
	public ReviewRecordPair getReviewRecordPair(int leftPersonId, int rightPersonId);
	
	public void updateReviewRecordPair(ReviewRecordPair reviewRecordPair);
	
	public void removeReviewRecordPair(ReviewRecordPair reviewRecordPair);
	
	public void removeAllReviewRecordPairs();
	
	public int removeReviewLinksBySource(LinkSource linkSource);
	
	public Integer getNextClusterId();
	
	public void addLoggedLink(LoggedLink loggedLink);
	
	public LoggedLink getLoggedLink(Integer loggedLinkId);
	
	public int getLoggedLinksCount(int vectorValue);
	
	public List<LoggedLink> getLoggedLinks(final int vectorValue, final int start, final int maxResults);
}
