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
package org.openempi.webapp.server;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openempi.webapp.client.BlockingDataService;
import org.openempi.webapp.client.model.BaseFieldWeb;
import org.openempi.webapp.client.model.SortedNeighborhoodConfigurationWeb;
import org.openempi.webapp.client.model.SuffixArrayBlockingConfigurationWeb;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.context.Context;

public class BlockingDataServiceImpl extends AbstractRemoteServiceServlet implements BlockingDataService
{
	public final static String BLOCKING_ROUNDS_REGISTRY_KEY = "blockingRounds";
	public final static String SN_BLOCKING_ROUNDS_REGISTRY_KEY = "sortedNeighborhood.blockingRounds";
	public final static String WINDOW_SIZE_REGISTRY_KEY = "sortedNeighborhood.windowSize";
	public final static String SA_BLOCKING_ROUNDS_REGISTRY_KEY = "suffixArray.blockingRounds";
	public final static String MINIMUM_SUFFIX_LENGTH_REGISTRY_KEY = "suffixArray.minimumSuffixLength";
	public final static String MAXIMUM_BLOCK_SIZE_REGISTRY_KEY = "suffixArray.maximumBlockSize";
	public final static String SIMILARITY_METRIC_REGISTRY_KEY = "suffixArray.similarityMetric";
	public final static String THRESHOLD_REGISTRY_KEY = "suffixArray.threshold";	

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	@SuppressWarnings("unchecked")
	public SortedNeighborhoodConfigurationWeb loadSortedNeighborhoodBlockingConfigurationData() throws Exception {
		log.debug("Received request to load the sorted neighborhood blocking configuration data.");
		
		authenticateCaller();	
		try {
			Map<String,Object> configurationData = (Map<String,Object>) Context.getConfiguration()
					.lookupConfigurationEntry(ConfigurationRegistry.BLOCKING_CONFIGURATION);
			List<BlockingRound> rounds = (List<BlockingRound>) configurationData.get(SN_BLOCKING_ROUNDS_REGISTRY_KEY);
			List<BaseFieldWeb> webRounds = convertToClientModel(rounds);
			Integer windowSize = (Integer) configurationData.get(WINDOW_SIZE_REGISTRY_KEY);
			SortedNeighborhoodConfigurationWeb webConfig = 
					new SortedNeighborhoodConfigurationWeb(webRounds, windowSize);
			return webConfig;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	@SuppressWarnings("unchecked")
	public SuffixArrayBlockingConfigurationWeb loadSuffixArrayBlockingConfigurationData() throws Exception {
		log.debug("Received request to load the sorted neighborhood blocking configuration data.");
		
		authenticateCaller();	
		try {
			Map<String,Object> configurationData = (Map<String,Object>) Context.getConfiguration()
					.lookupConfigurationEntry(ConfigurationRegistry.BLOCKING_CONFIGURATION);
			List<BlockingRound> rounds = (List<BlockingRound>) configurationData.get(SA_BLOCKING_ROUNDS_REGISTRY_KEY);
			List<BaseFieldWeb> webRounds = convertToClientModel(rounds);
			Integer minimumSuffixLength = (Integer) configurationData.get(MINIMUM_SUFFIX_LENGTH_REGISTRY_KEY);
			Integer maximumBlockSize = (Integer) configurationData.get(MAXIMUM_BLOCK_SIZE_REGISTRY_KEY);
			String similarityMetric = (String) configurationData.get(SIMILARITY_METRIC_REGISTRY_KEY);
			Float similarityThreshold = (Float) configurationData.get(THRESHOLD_REGISTRY_KEY);
			SuffixArrayBlockingConfigurationWeb webConfig = 
					new SuffixArrayBlockingConfigurationWeb(webRounds, minimumSuffixLength, maximumBlockSize,
							similarityMetric, similarityThreshold);
			return webConfig;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BaseFieldWeb> loadTraditionalBlockingConfigurationData() throws Exception {
		log.debug("Received request to load the blocking configuration data.");
		
		authenticateCaller();	
		try {
			Map<String,Object> configurationData = (Map<String,Object>) Context.getConfiguration()
					.lookupConfigurationEntry(ConfigurationRegistry.BLOCKING_CONFIGURATION);
			List<BlockingRound> rounds = (List<BlockingRound>) configurationData.get(BLOCKING_ROUNDS_REGISTRY_KEY);
			return convertToClientModel(rounds);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	private List<BaseFieldWeb> convertToClientModel(List<BlockingRound> rounds) {
		List<BaseFieldWeb> fields = new java.util.ArrayList<BaseFieldWeb>(rounds.size());
		int blockingRoundIndex=1;
		for (BlockingRound blockingRound : rounds) {
			int blockingFieldIndex=1;
			for (org.openhie.openempi.configuration.BaseField baseField : blockingRound.getFields()) {
				BaseFieldWeb clientField = new BaseFieldWeb(blockingRoundIndex, blockingFieldIndex, baseField.getFieldName());
				fields.add(clientField);
				blockingFieldIndex++;
			}
			blockingRoundIndex++;
		}
		return fields;
	}

	public String saveTraditionalBlockingConfigurationData(List<BaseFieldWeb> blockingConfiguration) throws Exception {
		
		authenticateCaller();	
		
		Configuration configuration = Context.getConfiguration();
		String returnMessage = "";
		try {
			ConfigurationLoader loader = configuration.getBlockingConfigurationLoader();
			List<BlockingRound> rounds = convertFromClientModel(blockingConfiguration);
			Map<String,Object> configurationData = new java.util.HashMap<String,Object>();
			configurationData.put(BLOCKING_ROUNDS_REGISTRY_KEY, rounds);
			loader.saveAndRegisterComponentConfiguration(configuration, configurationData);
		} catch (Exception e) {
			log.warn("Failed while saving the blocking configuration: " + e, e);
			returnMessage = e.getMessage();
			throw new Exception(returnMessage);
		}
		return returnMessage;
	}
	
	public String saveSortedNeighborhoodBlockingConfigurationData(SortedNeighborhoodConfigurationWeb blockingConfiguration) throws Exception {
		
		authenticateCaller();	
		
		Configuration configuration = Context.getConfiguration();
		String returnMessage = "";
		try {
			ConfigurationLoader loader = configuration.getBlockingConfigurationLoader();
			List<BlockingRound> rounds = convertFromClientModel(blockingConfiguration.getBlockingRounds());
			Map<String,Object> configurationData = new java.util.HashMap<String,Object>();
			configurationData.put(SN_BLOCKING_ROUNDS_REGISTRY_KEY, rounds);
			configurationData.put(WINDOW_SIZE_REGISTRY_KEY, blockingConfiguration.getWindowSize());
			loader.saveAndRegisterComponentConfiguration(configuration, configurationData);
		} catch (Exception e) {
			log.warn("Failed while saving the blocking configuration: " + e, e);
			returnMessage = e.getMessage();
			throw new Exception(returnMessage);
		}
		return returnMessage;
	}
	
	public String saveSuffixArrayBlockingConfigurationData(SuffixArrayBlockingConfigurationWeb blockingConfiguration) throws Exception {
		
		authenticateCaller();	
		
		Configuration configuration = Context.getConfiguration();
		String returnMessage = "";
		try {
			ConfigurationLoader loader = configuration.getBlockingConfigurationLoader();
			List<BlockingRound> rounds = convertFromClientModel(blockingConfiguration.getBlockingRounds());
			Map<String,Object> configurationData = new java.util.HashMap<String,Object>();
			configurationData.put(SA_BLOCKING_ROUNDS_REGISTRY_KEY, rounds);
			configurationData.put(MINIMUM_SUFFIX_LENGTH_REGISTRY_KEY, blockingConfiguration.getMinimumSuffixLength());
			configurationData.put(MAXIMUM_BLOCK_SIZE_REGISTRY_KEY, blockingConfiguration.getMaximumBlockSize());
			configurationData.put(SIMILARITY_METRIC_REGISTRY_KEY, blockingConfiguration.getSimilarityMetric());
			configurationData.put(THRESHOLD_REGISTRY_KEY, blockingConfiguration.getSimilarityThreshold());
			loader.saveAndRegisterComponentConfiguration(configuration, configurationData);
		} catch (Exception e) {
			log.warn("Failed while saving the blocking configuration: " + e, e);
			returnMessage = e.getMessage();
			throw new Exception(returnMessage);
		}
		return returnMessage;
	}

	private List<BlockingRound> convertFromClientModel(List<BaseFieldWeb> blockingConfiguration) {
		int roundsCount = 0;
		for (BaseFieldWeb baseField : blockingConfiguration) {
			if (baseField.getBlockingRound() > roundsCount) {
				roundsCount = baseField.getBlockingRound();
			}
		}
		List<BlockingRound> rounds = new java.util.ArrayList<BlockingRound>(roundsCount);
		for (int currRound=1; currRound <= roundsCount; currRound++) {
			BlockingRound round = new BlockingRound();
			for (BaseFieldWeb baseField : blockingConfiguration) {
				if (baseField.getBlockingRound() == currRound) {
					round.addField(new org.openhie.openempi.configuration.BaseField(baseField.getFieldName()));
				}
			}
			rounds.add(round);
		}
		return rounds;
	}

}
