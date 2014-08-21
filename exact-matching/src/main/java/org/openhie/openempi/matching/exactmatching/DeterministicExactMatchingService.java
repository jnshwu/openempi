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
package org.openhie.openempi.matching.exactmatching;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.MatchField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.matching.AbstractMatchingLifecycleObserver;
import org.openhie.openempi.matching.MatchingService;
import org.openhie.openempi.model.LinkSource;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.stringcomparison.StringComparisonService;

public class DeterministicExactMatchingService extends AbstractMatchingLifecycleObserver implements MatchingService
{
	private List<MatchField> matchFields;
	private StringComparisonService comparisonService;

	public void startup() throws InitializationException {
		@SuppressWarnings("unchecked")
		Map<String,Object> configurationData = (Map<String,Object>) Context.getConfiguration()
				.lookupConfigurationEntry(ConfigurationRegistry.MATCH_CONFIGURATION);
		Object obj = configurationData.get(Constants.MATCHING_FIELDS_REGISTRY_KEY);
		if (obj == null) {
			log.error("Deterministic exact matching service has not been configured properly; no match fields have been defined.");
			throw new RuntimeException("Deterministic exact maching service has not been configured properly.");
		}
		@SuppressWarnings("unchecked")
		List<MatchField> matchFields = (List<MatchField>) obj;
		for (MatchField field : matchFields) {
			log.debug("Matching service " + getClass().getName() + " will perform matching using " + field);
			if (field.getComparatorFunction() != null && comparisonService == null) {
				comparisonService = Context.getStringComparisonService();
			}
		}
		this.matchFields = matchFields;
	}
	
	public int getMatchingServiceId() {
		return LinkSource.EXACT_MATCHING_ALGORITHM_SOURCE;
	}

	public Set<RecordPair> match(Record record) {
		log.debug("Looking for matches on record " + record);
		List<RecordPair> candidates = Context.getBlockingService().findCandidates(record);
		Set<RecordPair> matches = new java.util.HashSet<RecordPair>();
		for (RecordPair entry : candidates) {
			// No need to compare a record pair that consists of two references to the same record.
			if (entry.getLeftRecord().getRecordId() != null && entry.getLeftRecord().getRecordId().longValue() == entry.getRightRecord().getRecordId().longValue()) {
				continue;
			}
			log.debug("Potential matching record pair found: " + entry);
			boolean overallMatch = true;
			for (MatchField matchField : getMatchFields()) {
				boolean fieldsMatch = isExactMatch(matchField, entry.getLeftRecord(), entry.getRightRecord());
				log.debug("Comparison of records on field " + matchField + " returned " + fieldsMatch);
				if (!fieldsMatch) {
					overallMatch = false;
					break;
				}
			}
			
			if (overallMatch) {
				log.debug("Adding to matches entry: " + entry);
				entry.setWeight(1.0);
				entry.setMatchOutcome(RecordPair.MATCH_OUTCOME_LINKED);
				entry.setLinkSource(new LinkSource(getMatchingServiceId()));
				matches.add(entry);
			}
		}
		return matches;
	}

	public RecordPair match(RecordPair recordPair) {
		if (log.isTraceEnabled()) {
			log.trace("Looking for matches on record pair " + recordPair);
		}
		if (recordPair == null || recordPair.getLeftRecord() == null || recordPair.getRightRecord() == null) {
			return recordPair;
		}
		
		recordPair.setLinkSource(new LinkSource(getMatchingServiceId()));
		
		// No need to compare a record pair that consists of two references to the same record.
		if (recordPair.getLeftRecord().getRecordId() != null && 
				recordPair.getLeftRecord().getRecordId().longValue() == recordPair.getRightRecord().getRecordId().longValue()) {
				recordPair.setMatchOutcome(RecordPair.MATCH_OUTCOME_LINKED);
				return recordPair;
		}
		boolean overallMatch = true;
		for (MatchField matchField : getMatchFields()) {
			boolean fieldsMatch = isExactMatch(matchField, recordPair.getLeftRecord(), recordPair.getRightRecord());
			if (log.isTraceEnabled()) {
				log.debug("Comparison of records on field " + matchField + " returned " + fieldsMatch);
			}
			if (!fieldsMatch) {
				overallMatch = false;
				recordPair.setWeight(0D);
				recordPair.setLinkSource(new LinkSource(getMatchingServiceId()));
				recordPair.setMatchOutcome(RecordPair.MATCH_OUTCOME_UNLINKED);
				return recordPair;
			}
		}
			
		if (overallMatch) {
			log.debug("Record pair should create a link: " + recordPair);
			recordPair.setWeight(1.0);
			recordPair.setMatchOutcome(RecordPair.MATCH_OUTCOME_LINKED);
		} else {
			log.debug("Record pair should not create a link: " + recordPair);
			recordPair.setWeight(0.0);
			recordPair.setMatchOutcome(RecordPair.MATCH_OUTCOME_UNLINKED);			
		}
		return recordPair;
	}

	public void linkRecords() {
		BlockingService blockingService = Context.getBlockingService();
		RecordPairSource source = blockingService.getRecordPairSource();
		for (RecordPairIterator iter = source.iterator(); iter.hasNext(); ) {
			
		}
	}

	private boolean isExactMatch(MatchField matchField, Record left, Record right) {
		String lVal = left.getAsString(matchField.getFieldName());
		String rVal = right.getAsString(matchField.getFieldName());
		if (lVal == null) {
			if (rVal == null) {
				return true;
			}
			return false;
		}
		if (matchField.getComparatorFunction() == null) {
			return lVal.equals(rVal);	
		}
		String functionName = matchField.getComparatorFunction().getFunctionName();
		double distance = comparisonService.score(functionName, lVal, rVal);
		if (log.isTraceEnabled()) {
			log.debug("Distance between values " + lVal + " and " + rVal + " computed using comparison function " + 
					functionName + " was found to be " + distance + " as compared to threshold " + 
					matchField.getMatchThreshold());
		}
		if (distance >= matchField.getMatchThreshold()) {
			return true;
		}
		return false;
	}

	public List<MatchField> getMatchFields() {
		return matchFields;
	}

	public void setMatchFieldNames(List<MatchField> matchFields) {
		this.matchFields = matchFields;
	}

	public void shutdown() {
		log.info("Shutting down deterministic matching service.");
	}

	public void initializeRepository() throws ApplicationException {
		log.info("The deterministic matching service is initializing the repository.");
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("matchFields", matchFields).toString();
	}
}
