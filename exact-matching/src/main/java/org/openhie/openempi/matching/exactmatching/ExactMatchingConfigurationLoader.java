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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.Constants;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.configuration.ComparatorFunction;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.MatchField;
import org.openhie.openempi.configuration.xml.MatchingConfigurationType;
import org.openhie.openempi.configuration.xml.exactmatching.ExactMatchingType;
import org.openhie.openempi.context.Context;

public class ExactMatchingConfigurationLoader implements ConfigurationLoader
{
	private Log log = LogFactory.getLog(ExactMatchingConfigurationLoader.class);
	
	public void loadAndRegisterComponentConfiguration(ConfigurationRegistry registry, Object configurationFragment) throws InitializationException {

		// This loader only knows how to process configuration information specifically
		// for the exact matching service
		//
		if (!(configurationFragment instanceof ExactMatchingType)) {
			log.error("Custom configuration loader " + getClass().getName() + " is unable to process the configuration fragment " + configurationFragment);
			throw new InitializationException("Custom configuration loader is unable to load this configuration fragment.");
		}
		
		// Register the configuration information with the Configuration Registry so that
		// it is available for the matching service to use when needed.
		//
		ArrayList<MatchField> matchFields = new ArrayList<MatchField>();
		Map<String,Object> configurationData = new java.util.HashMap<String,Object>();
		configurationData.put(Constants.MATCHING_FIELDS_REGISTRY_KEY, matchFields);
		registry.registerConfigurationEntry(ConfigurationRegistry.MATCH_CONFIGURATION, configurationData);
		
		ExactMatchingType matchingConfig = (ExactMatchingType) configurationFragment;
		log.debug("Received xml fragment to parse: " + matchingConfig);
		if (matchingConfig == null || matchingConfig.getMatchFields().sizeOfMatchFieldArray() == 0) {
			log.warn("No matching fields were configured; probably a configuration issue.");
			return;
		}
		
		for (int i=0; i < matchingConfig.getMatchFields().sizeOfMatchFieldArray(); i++) {
			org.openhie.openempi.configuration.xml.exactmatching.MatchField matchField = matchingConfig.getMatchFields().getMatchFieldArray(i);
			matchFields.add(buildMatchFieldFromXml(matchField));
		}
		registry.registerConfigurationEntry(ConfigurationRegistry.MATCHING_ALGORITHM_NAME_KEY, ExactMatchingConstants.EXACT_MATCHING_ALGORITHM_NAME);
	}

	@SuppressWarnings("unchecked")
	public void saveAndRegisterComponentConfiguration(ConfigurationRegistry registry, Map<String,Object> configurationData)
			throws InitializationException {		
		List<MatchField> fields = (List<MatchField>) configurationData.get(Constants.MATCHING_FIELDS_REGISTRY_KEY);
		MatchingConfigurationType xmlConfigurationFragment = buildMatchingConfigurationFragment(fields);
		log.debug("Saving matching info xml configuration fragment: " + xmlConfigurationFragment);
		Context.getConfiguration().saveMatchingConfiguration(xmlConfigurationFragment);
		Context.getConfiguration().saveConfiguration();
		log.debug("Storing updated matching configuration in configuration registry: " + fields);
		registry.registerConfigurationEntry(ConfigurationRegistry.MATCH_CONFIGURATION, configurationData);
	}
	
	private MatchField buildMatchFieldFromXml(org.openhie.openempi.configuration.xml.exactmatching.MatchField field) {
		MatchField matchField = new MatchField();
		matchField.setFieldName(field.getFieldName());
		if (field.getComparatorFunction() != null) {
			matchField.setComparatorFunction(buildComparatorFunctionFromXml(field.getComparatorFunction()));
			matchField.setMatchThreshold(field.getMatchThreshold());
		}
		return matchField;
	}

	private ComparatorFunction buildComparatorFunctionFromXml(org.openhie.openempi.configuration.xml.ComparatorFunction comparatorFunction) {
		ComparatorFunction function = new ComparatorFunction();
		function.setFunctionName(comparatorFunction.getFunctionName());
		if (comparatorFunction.isSetParameters() && comparatorFunction.getParameters().sizeOfParameterArray() > 0) {
			for (org.openhie.openempi.configuration.xml.Parameter parameter : comparatorFunction.getParameters().getParameterArray()) {
				log.debug("Adding parameter (" + parameter.getName() + "," + parameter.getValue() + ") to comparator function " + 
						function.getFunctionName());
				function.addParameter(parameter.getName(), parameter.getValue());
			}
		}
		return function;
	}
	
	private org.openhie.openempi.configuration.xml.MatchingConfigurationType buildMatchingConfigurationFragment(List<MatchField> fields) {
		org.openhie.openempi.configuration.xml.exactmatching.ExactMatchingType matchingType =
			org.openhie.openempi.configuration.xml.exactmatching.ExactMatchingType.Factory.newInstance();
		org.openhie.openempi.configuration.xml.exactmatching.MatchFields matchFieldsXml = matchingType.addNewMatchFields();
		for (MatchField matchField : fields) {
			org.openhie.openempi.configuration.xml.exactmatching.MatchField matchFieldXml = matchFieldsXml.addNewMatchField();
			matchFieldXml.setFieldName(matchField.getFieldName());
			org.openhie.openempi.configuration.xml.ComparatorFunction function =
					org.openhie.openempi.configuration.xml.ComparatorFunction.Factory.newInstance();
			function.setFunctionName(matchField.getComparatorFunction().getFunctionName());
			matchFieldXml.setComparatorFunction(function);
			matchFieldXml.setMatchThreshold(matchField.getMatchThreshold());
		}
		return matchingType;
	}	
}
