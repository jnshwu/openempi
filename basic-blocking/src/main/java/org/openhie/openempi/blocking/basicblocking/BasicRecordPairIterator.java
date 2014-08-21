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
package org.openhie.openempi.blocking.basicblocking;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.model.Criteria;
import org.openhie.openempi.model.Criterion;
import org.openhie.openempi.model.NameValuePair;
import org.openhie.openempi.model.Operation;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;

public class BasicRecordPairIterator implements RecordPairIterator
{
	protected final Log log = LogFactory.getLog(getClass());

	private BasicRecordPairSource recordPairSource;
	private boolean initialized = false;
	private int lastNameValuePairsSet;
	private List<List<NameValuePair>> nameValuePairsList;
	private List<RecordPair> recordPairs;
	private int lastRecordPair;
	private RecordPair nextPair;

	public BasicRecordPairIterator(BasicRecordPairSource recordPairSource) {
		this.recordPairSource = recordPairSource;
	}
	
	private synchronized void initialize() {
		nameValuePairsList = recordPairSource.getBlockingValueList();
		loadRecordPairList(nameValuePairsList.get(0));
		lastNameValuePairsSet = 1;
		lastRecordPair = 0;
		initialized = true;
	}

	public RecordPair next() {
		return nextPair;
	}

	public boolean hasNext() {
		RecordPair pair = null;
		if (!isInitialized()) {
			initialize();
		}
		if (lastRecordPair < recordPairs.size()) {
			pair = recordPairs.get(lastRecordPair);
			lastRecordPair++;
			nextPair = pair;			
			return true;
		}
		if (lastNameValuePairsSet < nameValuePairsList.size()) {
			do {
				List<NameValuePair> nameValuePairs = nameValuePairsList.get(lastNameValuePairsSet);
				log.debug("Loading pairs with blocking value of " + nameValuePairs);
				lastNameValuePairsSet++;
				loadRecordPairList(nameValuePairs);
				lastRecordPair = 1;
			} while (lastNameValuePairsSet < nameValuePairsList.size() && recordPairs.size() == 0);
			if (recordPairs == null || recordPairs.size() == 0) {
				return false;
			}
			nextPair = recordPairs.get(0);			
			return true;
		}
		return false;
	}

	private void loadRecordPairList(List<NameValuePair> pairs) {
		Criteria criteria = getCriteria(pairs);
		List<Record> records = recordPairSource.getBlockingDao().blockRecords(criteria);
		recordPairs = new ArrayList<RecordPair>();
		// If we don't find at least two records then there are no pairs to construct
		if (records.size() < 2) {
			return;
		}
		for (int i=0; i < records.size() - 1; i++) {
			for (int j = i+1; j < records.size(); j++) {
				log.debug("Building record pairs using indices " + i + " and " + j);
				RecordPair recordPair = new RecordPair(records.get(i), records.get(j));
				recordPairs.add(recordPair);
			}
		}
	}

	private Criteria getCriteria(List<NameValuePair> pairs) {
		Criteria criteria = new Criteria();
		for (NameValuePair pair : pairs) {
			Criterion criterion = new Criterion();
			criterion.setName(pair.getName());
			if (pair.getValue() == null) {
				criterion.setOperation(Operation.ISNULL);
			} else {
				criterion.setOperation(Operation.EQ);
				criterion.setValue(pair.getValue());				
			}
			criteria.addCriterion(criterion);
		}
		return criteria;
	}
	
	public void remove() {
		// This is an optional method of the interface and doesn't do
		// anything in this implementation. This is a read-only iterator.
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
}
