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
package org.openhie.openempi.singlebestrecord;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Record;

public class SingleBestRecordServiceImpl implements SingleBestRecordService
{
	protected final Log log = LogFactory.getLog(getClass());
	
	private SingleBestRecordConfiguration configuration;
	
	@Override
	public Record getSingleBestRecord(List<Record> records) {
		if (records == null) {
			return null;
		}
		if (records.size() == 1) {
			return records.get(0);
		}
		SingleBestRecordConfiguration config = getConfiguration();
		if (log.isDebugEnabled()) {
			log.debug("Selecting single best record using implementation: " + config.getName());
		}
		for (SingleBestRecordRule rule : config.getRules()) {
			Record found = evaluateRuleOnRecords(rule, records);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	private Record evaluateRuleOnRecords(SingleBestRecordRule rule, List<Record> records) {
		Record match;
		String field = rule.getFieldName();
		switch(rule.getCondition()) {
			case NOT_NULL:
				match = evaluateNotNullRule(field, records);
				break;
			case NULL:
				match = evaluateNullRule(field, records);
				break;
			case MAXIMUM:
				match = evaluateMaximum(field, records);
				break;
			case MINIMUM:
				match = evaluateMinimum(field, records);
				break;
			default:
				log.warn("The rule specified is not yet supported: " + rule);
				match = null;
		}
		return match;
	}

	private Record evaluateMinimum(String field, List<Record> records) {
		Record minRecord = null;
		Object minimum=null;
		for (Record record : records) {
			Object value = record.get(field);
			if (value == null) {
				continue;
			}
			if (minimum == null) {
				minimum = value;
				minRecord = record;
			}
			if (value instanceof String &&
					((String) value).compareTo((String) minimum) < 0) {
				minimum = value;
				minRecord = record;
			} else if (value instanceof java.util.Date &&
					((java.util.Date) value).compareTo((java.util.Date) minimum) < 0) {
				minimum = value;
				minRecord = record;
			} else if (value instanceof Integer &&
					((Integer) value).compareTo((Integer) minimum) < 0) {
				minimum = value;
				minRecord = record;				
			} else if (value instanceof Long &&
					((Long) value).compareTo((Long) minimum) < 0) {
				minimum = value;
				minRecord = record;				
			}
		}
		return minRecord;
	}

	private Record evaluateMaximum(String field, List<Record> records) {
		Record maxRecord = null;
		Object maximum=null;
		for (Record record : records) {
			Object value = record.get(field);
			if (value == null) {
				continue;
			}
			if (maximum == null) {
				maximum = value;
				maxRecord = record;
				continue;
			}
			if (value instanceof String &&
					((String) value).compareTo((String) maximum) > 0) {
				maximum = value;
				maxRecord = record;
			} else if (value instanceof java.util.Date &&
					((java.util.Date) value).compareTo((java.util.Date) maximum) > 0) {
				maximum = value;
				maxRecord = record;
			} else if (value instanceof Integer &&
					((Integer) value).compareTo((Integer) maximum) > 0) {
				maximum = value;
				maxRecord = record;				
			} else if (value instanceof Long &&
					((Long) value).compareTo((Long) maximum) > 0) {
				maximum = value;
				maxRecord = record;				
			}
		}
		return maxRecord;
	}

	private Record evaluateNullRule(String field, List<Record> records) {
		for (Record record : records) {
			Object value = record.get(field);
			if (value == null) {
				return record;
			}
		}
		return null;
	}

	private Record evaluateNotNullRule(String field, List<Record> records) {
		for (Record record : records) {
			Object value = record.get(field);
			if (value != null) {
				return record;
			}
		}
		return null;
	}

	private SingleBestRecordConfiguration getConfiguration() {
		if (configuration == null) {
			configuration = (SingleBestRecordConfiguration) Context.getConfiguration()
				.lookupConfigurationEntry(ConfigurationRegistry.SINGLE_BEST_RECORD_CONFIGURATION);
		}
		return configuration;
	}
}
