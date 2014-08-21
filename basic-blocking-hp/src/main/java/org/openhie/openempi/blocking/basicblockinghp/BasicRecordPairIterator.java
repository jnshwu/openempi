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
package org.openhie.openempi.blocking.basicblockinghp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;

public class BasicRecordPairIterator implements RecordPairIterator
{
	protected final Log log = LogFactory.getLog(getClass());

	private BasicRecordPairSource recordPairSource;
	private boolean initialized = false;
	private List<Long> recordIds;
	private List<RecordPair> recordPairs;
	private List<BlockingRound> blockingRounds;

	private int currentRecordId;
	private int currentRecordPair;
	private RecordPair nextPair;

	public BasicRecordPairIterator(BasicRecordPairSource recordPairSource) {
		this.recordPairSource = recordPairSource;
	}
	
	private synchronized void initialize() {
		blockingRounds = recordPairSource.getBlockingRounds();
		// If there are not records then there is nothing to do
		if (recordIds == null || recordIds.size() == 0) {
			return;
		}
		currentRecordId = 0;
		Set<RecordPair> allPairs = new HashSet<RecordPair>();
		recordPairs = new ArrayList<RecordPair>();
		do {
			boolean foundRecordPairs = loadRecordPairsForRecord(allPairs, currentRecordId);
			if (!foundRecordPairs) {
				return;
			}
			currentRecordId++;
		} while (currentRecordId < recordIds.size());
		recordPairs = new ArrayList<RecordPair>(allPairs.size());
		recordPairs.addAll(allPairs);
		currentRecordPair = 0;
		initialized = true;
	}

	public boolean hasNext() {
		RecordPair pair = null;
		if (!isInitialized()) {
			initialize();
		}
		if (currentRecordPair < recordPairs.size()) {
			pair = recordPairs.get(currentRecordPair);
			currentRecordPair++;
			nextPair = pair;			
			return true;
		}
		return false;
	}

	private boolean loadRecordPairsForRecord(Set<RecordPair> allPairs, int recordIndex) {
		// If we exhausted all records, then there is nothing more to do
		if (recordIndex == recordIds.size()) {
			return false;
		}
		Long recordId = recordIds.get(recordIndex);
		Record record = recordPairSource.getCache().loadRecord(recordId);
		if (record == null) {
			return false;
		}
		for (BlockingRound round : blockingRounds) {
			String blockingKeyValue = BlockingKeyValueGenerator.generateBlockingKeyValue(round.getFields(), record);
			List<Record> records = recordPairSource.getCache().loadCandidateRecords(round, blockingKeyValue);
			List<RecordPair> pairs = generateRecordPairs(records);
			if (pairs.size() > 0) {
				allPairs.addAll(pairs);
			}
		}
		return true;
	}

	public RecordPair next() {
		return nextPair;
	}

	private List<RecordPair> generateRecordPairs(List<Record> records) {
		List<RecordPair> pairs = new ArrayList<RecordPair>();
		for (int i=0; i < records.size()-1; i++) {
			for (int j=i+1; j < records.size(); j++) {
				RecordPair recordPair = new RecordPair(records.get(i), records.get(j));
				pairs.add(recordPair);
			}
		}
		return pairs;
	}
	
	public void remove() {
		// This is an optional method of the interface and doesn't do
		// anything in this implementation. This is a read-only iterator.
	}

	public void setRecordIds(List<Long> recordIds) {
		this.recordIds = recordIds;
	}
	
	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public List<BlockingRound> getBlockingRounds() {
		return blockingRounds;
	}

	public void setBlockingRounds(List<BlockingRound> blockingRounds) {
		this.blockingRounds = blockingRounds;
	}	
}
