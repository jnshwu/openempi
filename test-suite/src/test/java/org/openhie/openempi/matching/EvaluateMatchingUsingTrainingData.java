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
package org.openhie.openempi.matching;

import java.util.List;
import java.util.Map;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.LinkSource;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.service.BaseServiceTestCase;
import org.openhie.openempi.service.PersonQueryService;

public class EvaluateMatchingUsingTrainingData extends BaseServiceTestCase
{
	public static final Integer GOLD_STANDARD_SOURCE = 1;
	public static final Integer EXACT_MATCHING_SOURCE = 2;
	private java.util.Map<Link,Link> actualLinks = new java.util.HashMap<Link,Link>();
	private java.util.Map<Link,Link> matchingLinks = new java.util.HashMap<Link,Link>();
	
	public void testLoadLinksBySource() {
		double recordCount=5000;
		double totalPairs = recordCount*(recordCount-1)/2.0;
		PersonQueryService queryService = Context.getPersonQueryService();
		List<PersonLink> links = queryService.getPersonLinksByLinkSource(GOLD_STANDARD_SOURCE);
		log.debug("Found " + links.size() + " links for source: " + GOLD_STANDARD_SOURCE);
		for (PersonLink pLink : links) {
			addLinkToMap(pLink, actualLinks);
		}
		links = queryService.getPersonLinksByLinkSource(EXACT_MATCHING_SOURCE);
		log.debug("Found " + links.size() + " links for source: " + EXACT_MATCHING_SOURCE);
		for (PersonLink pLink : links) {
			addLinkToMap(pLink, matchingLinks);
		}
		
		double truePositivesCount = 0;
		double falsePositivesCount = 0;
		double falseNegativesCount = 0;
		java.util.List<Link> truePositives = new java.util.ArrayList<Link>();
		java.util.List<Link> falsePositives = new java.util.ArrayList<Link>();
		java.util.List<Link> falseNegatives = new java.util.ArrayList<Link>();
		for (Link link : matchingLinks.keySet()) {
			Link found = actualLinks.get(link);
			if (found != null) {
				truePositivesCount++;
				truePositives.add(link);
			} else {
				falsePositivesCount++;
				scoreLink(link);
				falsePositives.add(link);
			}
		}
		for (Link link : actualLinks.keySet()) {
			Link found = matchingLinks.get(link);
			if (found == null) {
				falseNegativesCount++;
				scoreLink(link);
				falseNegatives.add(link);
			}
		}
		log.debug("Number of True Positives: " + truePositivesCount);
		for (Link link : truePositives) {
			log.debug(link);
		}
		log.debug("Number of False Positives: " + falsePositivesCount);
		for (Link link : falsePositives) {
			log.debug(link);
			logPairAttributes(link);
		}
		log.debug("Number of False Negatives: " + falseNegativesCount);
		for (Link link : falseNegatives) {
			log.debug(link);
			logPairAttributes(link);
		}
		double trueNegativesCount = totalPairs - truePositivesCount - falsePositivesCount - falseNegativesCount;
		log.debug("Number of True Negatives: " + trueNegativesCount);
		
		double sensitivity = (truePositivesCount/(truePositivesCount + falseNegativesCount)) * 100.0;
		double specificity = (trueNegativesCount/(trueNegativesCount + falsePositivesCount)) * 100.0;
		double precision = (truePositivesCount/(truePositivesCount + falsePositivesCount)) * 100.0;
		log.debug("True Positives:\t" + truePositivesCount);
		log.debug("False Positives:\t" + falsePositivesCount);
		log.debug("False Negatives:\t" + falseNegativesCount);
		log.debug("True Negatives:\t" + trueNegativesCount);
		log.debug("Sensitivity (Recall):\t" + sensitivity);
		log.debug("Precision:\t" + precision);
		log.debug("Specificity:\t" + specificity);
	}

	private void scoreLink(Link link) {
		RecordPair pair = buildPairFromLink(link);
		try {
			pair = Context.getMatchingService().match(pair);
		} catch (ApplicationException e) {
			log.warn("Unable to score the record pair: " + e, e);
		}
	}

	private RecordPair buildPairFromLink(Link link) {
		Record lrec = new Record(link.getLeftPerson());
		lrec.setRecordId(link.getLeftId().longValue());
		Record rrec = new Record(link.getRightPerson());
		rrec.setRecordId(link.getRightId().longValue());
		RecordPair pair = new RecordPair(lrec, rrec);
		pair.setLinkSource(new LinkSource(LinkSource.MANUAL_MATCHING_SOURCE));
		return pair;
	}

	private void logPairAttributes(Link link) {
		Person left = link.getLeftPerson();
		logPersonAttributes(left);
		Person right = link.getRightPerson();
		logPersonAttributes(right);
	}

	private void logPersonAttributes(Person p) {
		log.debug(p.getGivenName() + "," + p.getFamilyName() + "," + p.getAddress1() + "," + p.getCity() + "," + p.getState() + "," + p.getPostalCode() + "," + p.getPhoneNumber() + "," + p.getSsn());
	}

	private void addLinkToMap(PersonLink pLink, Map<Link, Link> map) {
		Link link = null;
		if (pLink.getPersonLeft().getPersonId() < pLink.getPersonRight().getPersonId()) {
			link = new Link(pLink.getPersonLeft(), pLink.getPersonRight());
		} else {
			link = new Link(pLink.getPersonRight(), pLink.getPersonLeft());
		}
		map.put(link, link);
	}

	public class Link
	{
		private Integer leftId;
		private Integer rightId;
		private Person leftPerson;
		private Person rightPerson;
		private double weight;
		
		public Link(Person leftPerson, Person rightPerson) {
			this.leftPerson = leftPerson;
			this.rightPerson = rightPerson;
			this.leftId = leftPerson.getPersonId();
			this.rightId = rightPerson.getPersonId();
		}

		public Integer getLeftId() {
			return leftId;
		}

		public void setLeftId(Integer leftId) {
			this.leftId = leftId;
		}

		public Integer getRightId() {
			return rightId;
		}

		public void setRightId(Integer rightId) {
			this.rightId = rightId;
		}

		public Person getLeftPerson() {
			return leftPerson;
		}

		public void setLeftPerson(Person leftPerson) {
			this.leftPerson = leftPerson;
		}

		public Person getRightPerson() {
			return rightPerson;
		}

		public void setRightPerson(Person rightPerson) {
			this.rightPerson = rightPerson;
		}

		public double getWeight() {
			return weight;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		private EvaluateMatchingUsingTrainingData getOuterType() {
			return EvaluateMatchingUsingTrainingData.this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((leftId == null) ? 0 : leftId.hashCode());
			result = prime * result
					+ ((rightId == null) ? 0 : rightId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Link other = (Link) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (leftId == null) {
				if (other.leftId != null)
					return false;
			} else if (!leftId.equals(other.leftId))
				return false;
			if (rightId == null) {
				if (other.rightId != null)
					return false;
			} else if (!rightId.equals(other.rightId))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Link [leftId=" + leftId + ", rightId=" + rightId
					+ ", weight=" + weight + "]";
		}
	}
}
