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

import java.util.List;
import java.util.Map;

import org.openhie.openempi.InitializationException;
import org.openhie.openempi.blocking.AbstractBlockingLifecycleObserver;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.CustomField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.BlockingDao;
import org.openhie.openempi.model.Criteria;
import org.openhie.openempi.model.Criterion;
import org.openhie.openempi.model.Operation;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.transformation.TransformationService;
import org.openhie.openempi.util.ConvertUtil;

public class BlockingServiceImpl extends AbstractBlockingLifecycleObserver implements BlockingService
{
	private BlockingDao blockingDao;
	private List<BlockingRound> blockingRounds;

	public void startup() throws InitializationException {
		log.trace("Starting the Traditional Blocking Service");
	}

	public void shutdown() {
		log.trace("Stopping the Traditional Blocking Service");
	}

	public void rebuildIndex() throws InitializationException {
		// TODO Auto-generated method stub
		
	}
	
	public RecordPairSource getRecordPairSource(Object parameters) {
		if (!(parameters instanceof List<?>)) {
			log.trace("Invalid parameter for this implementation of the blocking interface: " + parameters.getClass());
			throw new RuntimeException("Invalid parameter type provided for this blocking algorithm implementation.");
		}
		@SuppressWarnings("unchecked")
		List<BlockingRound> blockingRounds = (List<BlockingRound>) parameters;
		BasicRecordPairSource recordPairSource = new BasicRecordPairSource();
		recordPairSource.setBlockingDao(getBlockingDao());
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
		int count = 0;
		List<Record> records = new java.util.ArrayList<Record>();
		for (BlockingRound round : blockingRounds) {
			List<BaseField> fields = round.getFields();
			Criteria criteria = new Criteria();
			for (BaseField field : fields) {
				Criterion criterion = buildCriterion(field, record);
				if (criterion != null) {
					log.debug("In round " + count + " added criterion: " + criterion);
					criteria.addCriterion(criterion);
				}
			}
			if (criteria.getCriteria().size() > 0) {
				records.addAll(blockingDao.blockRecords(criteria));
			}
			count++;
		}
		return ConvertUtil.generateRecordPairs(record, records);
	}

	@Override
	public List<Long> getRecordPairCount() {
		throw new RuntimeException("Method has not been implemented yet for this blocking algorithm.");
	}

	private Criterion buildCriterion(BaseField field, Record record) {
		Criterion criterion = new Criterion();
		criterion.setName(field.getFieldName());
		criterion.setOperation(Operation.EQ);
		if (field instanceof CustomField) {
			CustomField custom = (CustomField) field;
			String sourceValue = record.getAsString(custom.getSourceFieldName());
			if (sourceValue == null) {
				return null;
			}
			Object value = generateTransformedValue(custom, sourceValue);
			if (value == null) {
				return null;
			}
			criterion.setValue(value);
		} else {
			Object value = record.get(field.getFieldName());
			if (value == null) {
				return null;
			}
			criterion.setValue(value);
		}
		return criterion;
	}

	private Object generateTransformedValue(CustomField custom, String sourceValue) {
		TransformationService transformationService = Context.getTransformationService();
		try {
			return transformationService.transform(custom.getTransformationFunctionName(), sourceValue);
		} catch (Exception e) {
			log.error("Failed while trying to create a transform for generation of a blocking field: " + e, e);
			return null;
		}
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
}
