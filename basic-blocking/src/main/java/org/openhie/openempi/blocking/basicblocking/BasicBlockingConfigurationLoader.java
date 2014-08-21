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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.CustomField;
import org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingField;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingFields;
import org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds;
import org.openhie.openempi.context.Context;

/**

 * @author Odysseas Pentakalos 
 * @version $Revision: $ $Date:  $
 */
public class BasicBlockingConfigurationLoader implements ConfigurationLoader
{
	private Log log = LogFactory.getLog(BasicBlockingConfigurationLoader.class);
	
	@SuppressWarnings("unchecked")
	public void loadAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationFragment) throws InitializationException {

		// This loader only knows how to process configuration information specifically
		// for the basic blocking service
		//
		if (!(configurationFragment instanceof BasicBlockingType)) {
			log.error("Custom configuration loader " + getClass().getName() + " is unable to process the configuration fragment " + configurationFragment);
			throw new InitializationException("Custom configuration loader is unable to load this configuration fragment.");
		}
		
		// Register the configuration information with the Configuration Registry so that
		// it is available for the blocking service to use when needed.
		//
		ArrayList<BlockingRound> blockingRounds = new ArrayList<BlockingRound>();
		Map<String,Object> configurationData = new java.util.HashMap<String,Object>();
		configurationData.put(BasicBlockingConstants.BLOCKING_ROUNDS_REGISTRY_KEY, blockingRounds);
		registry.registerConfigurationEntry(ConfigurationRegistry.BLOCKING_CONFIGURATION, configurationData);
		
		BasicBlockingType blockingConfig = (BasicBlockingType) configurationFragment;
		log.debug("Received xml fragment to parse: " + blockingConfig);
		if (blockingConfig == null || blockingConfig.getBlockingRounds().sizeOfBlockingRoundArray() == 0) {
			log.warn("No blocking rounds were configured; probably a configuration issue.");
			return;
		}
		
		for (int i=0; i < blockingConfig.getBlockingRounds().sizeOfBlockingRoundArray(); i++) {
			org.openhie.openempi.configuration.xml.basicblocking.BlockingRound round = blockingConfig.getBlockingRounds().getBlockingRoundArray(i);
			BlockingRound blockingRound = new BlockingRound();
			Map<String, CustomField> customFieldsByName = (Map<String, CustomField>)
				registry.lookupConfigurationEntry(ConfigurationRegistry.CUSTOM_FIELDS_MAP);
			for (int j=0; j < round.getBlockingFields().sizeOfBlockingFieldArray(); j++) {
				org.openhie.openempi.configuration.xml.basicblocking.BlockingField field = round.getBlockingFields().getBlockingFieldArray(j);
				log.trace("Looking for blocking field named " + field.getFieldName());
				CustomField custom = customFieldsByName.get(field.getFieldName());
				if (custom == null) {
					blockingRound.addField(new BaseField(field.getFieldName()));
				} else {
					blockingRound.addField(custom);
				}
			}
			blockingRounds.add(blockingRound);
		}
		registry.registerConfigurationEntry(ConfigurationRegistry.BLOCKING_ALGORITHM_NAME_KEY, BasicBlockingConstants.BLOCKING_ALGORITHM_NAME);
	}

	@SuppressWarnings("unchecked")
	public void saveAndRegisterComponentConfiguration(ConfigurationRegistry registry, Map<String,Object> configurationData)
			throws InitializationException {
		Object obj = configurationData.get(BasicBlockingConstants.BLOCKING_ROUNDS_REGISTRY_KEY);
		if (obj == null || !(obj instanceof List<?>)) {
			log.warn("Invalid configuration data passed to traditional blocking algorithm.");
			throw new InitializationException("Unable to save nvalid configuration data for the traditional blocking algorithm.");
		}
		List<BlockingRound> rounds = (List<BlockingRound>) obj; 
		BasicBlockingType xmlConfigurationFragment = buildConfigurationFileFragment(rounds);
		log.debug("Saving blocking info xml configuration fragment: " + xmlConfigurationFragment);
		Context.getConfiguration().saveBlockingConfiguration(xmlConfigurationFragment);
		Context.getConfiguration().saveConfiguration();
		log.debug("Storing updated blocking configuration in configuration registry: " + rounds);
		registry.registerConfigurationEntry(ConfigurationRegistry.BLOCKING_CONFIGURATION, configurationData);
	}

	private BasicBlockingType buildConfigurationFileFragment(List<BlockingRound> rounds) {
		BasicBlockingType newBasicBlocking = BasicBlockingType.Factory.newInstance();
		BlockingRounds roundsNode = newBasicBlocking.addNewBlockingRounds();
		for (BlockingRound blockingRound : rounds) {
			org.openhie.openempi.configuration.xml.basicblocking.BlockingRound roundNode = roundsNode.addNewBlockingRound();
			BlockingFields blockingFields = roundNode.addNewBlockingFields();
			for (org.openhie.openempi.configuration.BaseField field : blockingRound.getFields()) {
				BlockingField xmlField = blockingFields.addNewBlockingField();
				xmlField.setFieldName(field.getFieldName());
			}
		}
		return newBasicBlocking;
	}
}
