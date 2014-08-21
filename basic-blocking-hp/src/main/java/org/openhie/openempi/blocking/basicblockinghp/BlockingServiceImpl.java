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
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.openhie.openempi.InitializationException;
import org.openhie.openempi.blocking.AbstractBlockingLifecycleObserver;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.blocking.basicblockinghp.cache.BlockingServiceCache;
import org.openhie.openempi.blocking.basicblockinghp.dao.BlockingDao;
import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.context.Context.ObservationEventType;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.notification.EntityAddObservable;
import org.openhie.openempi.notification.EntityDeleteObservable;
import org.openhie.openempi.notification.EntityUpdateObservable;
import org.openhie.openempi.util.ConvertUtil;

public class BlockingServiceImpl extends AbstractBlockingLifecycleObserver implements BlockingService, Observer
{
	private BlockingDao blockingDao;
	private List<BlockingRound> blockingRounds;
	private BlockingServiceCache blockingServiceCache;

	public void startup() throws InitializationException {
		log.info("Starting the Traditional Blocking Service");
		blockingRounds = getBlockingRounds();
		blockingServiceCache.init(blockingRounds);
		Context.registerObserver(this, ObservationEventType.ENTITY_ADD_EVENT);
		Context.registerObserver(this, ObservationEventType.ENTITY_DELETE_EVENT);
		Context.registerObserver(this, ObservationEventType.ENTITY_UPDATE_EVENT);
		log.info("Finished building the index for the Traditional Blocking Service");
	}

	public void shutdown() {
		blockingRounds = getBlockingRounds();
		blockingServiceCache.removeCaches(blockingRounds);
		blockingServiceCache.shutdown();
		log.info("Stopping the Traditional Blocking Service");
	}

	public void rebuildIndex() throws InitializationException {
		log.info("Rebuilding the index for the Traditional Blocking Service");
		blockingRounds = getBlockingRounds();
		blockingServiceCache.removeCaches(blockingRounds);
		blockingServiceCache.init(blockingRounds);
		log.info("Finished building the index for the Traditional Blocking Service");
	}
	
	public void update(Observable o, Object eventData) {
		if (eventData == null || !(eventData instanceof Record)) {
			return;
		}
		if (o instanceof EntityAddObservable) {
			Record record = (Record) eventData;
			log.debug("A new record was added; we need to update the index: " + record.getRecordId());
			blockingRounds = getBlockingRounds();
			for (BlockingRound round : blockingRounds) {
				List<BaseField> fields = round.getFields();
				String blockingKeyValue = BlockingKeyValueGenerator.generateBlockingKeyValue(fields, record);
				if (log.isDebugEnabled()) {
					log.debug("Adding to cache: (" + blockingKeyValue + "," + record.getRecordId().intValue() + ")");
				}
				blockingServiceCache.addRecordToIndex(round, blockingKeyValue, record.getRecordId().intValue());
			}
			blockingServiceCache.updateRecordCache(o, record);
		} else	if (o instanceof EntityDeleteObservable) {
			Record record = (Record) eventData;
			log.debug("A new record was deleted; we need to update the index: " + record.getRecordId());
			blockingRounds = getBlockingRounds();
			for (BlockingRound round : blockingRounds) {
				List<BaseField> fields = round.getFields();
				String blockingKeyValue = BlockingKeyValueGenerator.generateBlockingKeyValue(fields, record);
				if (log.isDebugEnabled()) {
					log.debug("Removing from cache: (" + blockingKeyValue + "," + record.getRecordId().intValue() + ")");
				}
				blockingServiceCache.deleteRecordFromIndex(round, blockingKeyValue, record.getRecordId().intValue());
			}
			blockingServiceCache.updateRecordCache(o, record);
		} else	if (o instanceof EntityUpdateObservable) {
			Object[] records = (Object[]) eventData;
			Record beforeRecord = (Record) records[0];
			Record afterRecord = (Record) records[1];
			log.debug("A new record was updated; we need to update the index: " + afterRecord.getRecordId());
			blockingRounds = getBlockingRounds();
			for (BlockingRound round : blockingRounds) {
				List<BaseField> fields = round.getFields();
				String blockingKeyValue = BlockingKeyValueGenerator.generateBlockingKeyValue(fields, beforeRecord);
				if (log.isDebugEnabled()) {
					log.debug("Removing from cache: (" + blockingKeyValue + "," + beforeRecord.getRecordId().intValue() + ")");
				}				
				blockingServiceCache.deleteRecordFromIndex(round, blockingKeyValue, beforeRecord.getRecordId().intValue());
				blockingKeyValue = BlockingKeyValueGenerator.generateBlockingKeyValue(fields, afterRecord);
				if (log.isDebugEnabled()) {
					log.debug("Adding to cache: (" + blockingKeyValue + "," + afterRecord.getRecordId().intValue() + ")");
				}				
				blockingServiceCache.addRecordToIndex(round, blockingKeyValue, afterRecord.getRecordId().intValue());
			}
			blockingServiceCache.updateRecordCache(o, afterRecord);
		}
	}
	
	public RecordPairSource getRecordPairSource(Object parameters) {
		if (!(parameters instanceof List<?>)) {
			log.trace("Invalid parameter for this implementation of the blocking interface: " + parameters.getClass());
			throw new RuntimeException("Invalid parameter type provided for this blocking algorithm implementation.");
		}
		@SuppressWarnings("unchecked")
		List<BlockingRound> blockingRounds = (List<BlockingRound>) parameters;
		BasicRecordPairSource recordPairSource = new BasicRecordPairSource();
		recordPairSource.setCache(getBlockingServiceCache());
		recordPairSource.setBlockingRounds(blockingRounds);
		recordPairSource.init();
		return recordPairSource;
	}
	
	public RecordPairSource getRecordPairSource() {
		return getRecordPairSource(getBlockingRounds());
	}
	
	/**
	 * Iterates over the list of blocking rounds that have been
	 * defined and accumulates patients that match the search criteria
	 * configured for the specific values present in the record
	 * provided.
	 * 
	 */
	public List<RecordPair> findCandidates(Record record) {
		blockingRounds = getBlockingRounds();
		List<Record> records = new java.util.ArrayList<Record>();
		for (BlockingRound round : blockingRounds) {
			List<BaseField> fields = round.getFields();
			String blockingKeyValue = BlockingKeyValueGenerator.generateBlockingKeyValue(fields, record);
			log.debug("Blocking using key: " + blockingKeyValue);
			records.addAll(blockingServiceCache.loadCandidateRecords(blockingKeyValue));
		}
		log.debug("Found a total of " + records.size() + " candidate records.");
		return ConvertUtil.generateRecordPairs(record, records);
	}

	@Override
	public List<Long> getRecordPairCount() {
		blockingRounds = getBlockingRounds();
		List<Long> counts = new ArrayList<Long>(blockingRounds.size());
		for (BlockingRound round : blockingRounds) {
			counts.add(blockingDao.getRecordPairCount(round));
		}
		return counts;
	}

	@SuppressWarnings("unchecked")
	private List<BlockingRound> getBlockingRounds() {
		if (blockingRounds == null) {
			Map<String,Object> configurationData = (Map<String,Object>) Context.getConfiguration()
					.lookupConfigurationEntry(ConfigurationRegistry.BLOCKING_CONFIGURATION);
			blockingRounds = (List<BlockingRound>) configurationData.get(BasicBlockingConstants.BLOCKING_ROUNDS_REGISTRY_KEY);
		}
		return blockingRounds;
	}

	public BlockingDao getBlockingDao() {
		return blockingDao;
	}

	public void setBlockingDao(BlockingDao blockingDao) {
		this.blockingDao = blockingDao;
	}

	public BlockingServiceCache getBlockingServiceCache() {
		return blockingServiceCache;
	}

	public void setBlockingServiceCache(BlockingServiceCache blockingServiceCache) {
		this.blockingServiceCache = blockingServiceCache;
	}
}
