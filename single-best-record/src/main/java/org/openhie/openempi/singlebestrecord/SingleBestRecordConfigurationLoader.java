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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.xml.singlebestrecord.Rule;
import org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType;

public class SingleBestRecordConfigurationLoader implements ConfigurationLoader
{
	private Log log = LogFactory.getLog(getClass());
	
	@Override
	public void loadAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationFragment) throws InitializationException {
		
		if (!(configurationFragment instanceof SingleBestRecordType)) {
			log.error("Custom configuration loader " + getClass().getName() + " is unable to process the configuration fragment " + configurationFragment);
			throw new InitializationException("Custom configuration loader is unable to load this configuration fragment.");
		}
		
		SingleBestRecordType sbr = (SingleBestRecordType) configurationFragment;
		SingleBestRecordConfiguration conf = new SingleBestRecordConfiguration(sbr.getImplementationName(),
				sbr.getImplementationDescription());
		for (Rule rule : sbr.getRuleset().getRuleArray()) {
			RuleCondition condition = getRuleCondition(rule);
			if (condition == null) {
				log.warn("While parsing single best record rules, came across unexpected condition; rule will be ignored: " + conf);
				continue;
			}
			SingleBestRecordRule sbrRule = new SingleBestRecordRule(rule.getFieldName(), condition);
			conf.addRule(sbrRule);
		}
		log.debug("Loaded configuration: " + conf);
		registry.registerConfigurationEntry(ConfigurationRegistry.SINGLE_BEST_RECORD_CONFIGURATION, conf);
	}

	private RuleCondition getRuleCondition(Rule rule) {
		if (rule.getCondition().toString().equalsIgnoreCase(RuleCondition.NOT_NULL.toString())) {
			return RuleCondition.NOT_NULL;
		} else if (rule.getCondition().toString().equalsIgnoreCase(RuleCondition.NULL.toString())) {
			return RuleCondition.NULL;
		} else if (rule.getCondition().toString().equalsIgnoreCase(RuleCondition.MINIMUM.toString())) {
			return RuleCondition.MINIMUM;
		} else if (rule.getCondition().toString().equalsIgnoreCase(RuleCondition.MAXIMUM.toString())) {
			return RuleCondition.MAXIMUM;
		}
		return null;
	}

	@Override
	public void saveAndRegisterComponentConfiguration(ConfigurationRegistry registry, Map<String, Object> configurationData) throws InitializationException {
		// TODO Auto-generated method stub

	}

}
