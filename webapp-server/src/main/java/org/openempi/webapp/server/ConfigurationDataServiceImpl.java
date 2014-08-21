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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openempi.webapp.client.ConfigurationDataService;
import org.openempi.webapp.client.model.CustomFieldWeb;
import org.openempi.webapp.client.model.ExactMatchingConfigurationWeb;
import org.openempi.webapp.client.model.MatchConfigurationWeb;
import org.openempi.webapp.client.model.MatchFieldWeb;
import org.openempi.webapp.client.model.StringComparatorFunctionWeb;
import org.openempi.webapp.client.model.TransformationFunctionWeb;
import org.openempi.webapp.client.model.VectorConfigurationWeb;
import org.openhie.openempi.Constants;
import org.openhie.openempi.configuration.ComparatorFunction;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.CustomField;
import org.openhie.openempi.configuration.MatchField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.VectorConfiguration;

public class ConfigurationDataServiceImpl extends AbstractRemoteServiceServlet implements ConfigurationDataService
{
	public final static String FALSE_NEGATIVE_PROBABILITY_REGISTRY_KEY = "falseNegativeProbability";
	public final static String FALSE_POSITIVE_PROBABILITY_REGISTRY_KEY = "falsePositiveProbability";
	public final static String CONFIGURATION_DIRECTORY_REGISTRY_KEY = "configDirectory";
	
	public final static String PROBABILISTIC_MATCHING_M_VALUES_KEY = "mValues";
	public final static String PROBABILISTIC_MATCHING_U_VALUES_KEY = "uValues";
	public final static String PROBABILISTIC_MATCHING_P_VALUE_KEY = "pValue";
	public final static String PROBABILISTIC_MATCHING_INITIAL_M_VALUE_KEY = "initialMValue";
	public final static String PROBABILISTIC_MATCHING_INITIAL_U_VALUE_KEY = "initialUValue";
	public final static String PROBABILISTIC_MATCHING_INITIAL_P_VALUE_KEY = "initialPValue";
	public final static String PROBABILISTIC_MATCHING_MAX_ITERATIONS_KEY = "maxIterations";
	public final static String PROBABILISTIC_MATCHING_LOWER_BOUND_KEY = "lowerBound";
	public final static String PROBABILISTIC_MATCHING_UPPER_BOUND_KEY = "upperBound";
	
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_KEY = "loggingByVectors";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_VECTORS_KEY = "loggingVectorsIntegerSet";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_FRACTION_KEY = "loggingVectorsFraction";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_KEY = "loggingByWeight";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_LOWER_BOUND_KEY = "loggingWeightLowerBound";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_UPPER_BOUND_KEY = "loggingWeightUpperBound";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_FRACTION_KEY = "loggingWeightFraction";
	public final static String PROBABILISTIC_MATCHING_LOGGING_DESTIONATION = "loggingDestination";
	public final static String PROBABILISTIC_MATCHING_VECTOR_CONFIGURATION = "vectorConfiguration";
	
	static final int DEFAULT_MAX_EM_ITERATIONS = 100;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public List<StringComparatorFunctionWeb> getStringComparatorFunctionList() {
		log.debug("Received request to retrieve a list of string comparator function.");
		List<StringComparatorFunctionWeb> functionList = new ArrayList<StringComparatorFunctionWeb>();
		String[] functionNames = 
			Context.getApplicationContext().getBeanNamesForType(org.openhie.openempi.stringcomparison.metrics.AbstractDistanceMetric.class);
		System.out.println("String Comparator Function Names:");
		for (String functionName: functionNames) {
			StringComparatorFunctionWeb compFunc = new StringComparatorFunctionWeb();
			compFunc.setClassName(functionName);
			System.out.println(" " + functionName);
			functionList.add(compFunc);
		}
		return functionList;
	}

	public List<TransformationFunctionWeb> getTransfromationFunctionList() {
		log.debug("Received request to retrieve a list of transformation functions.");
		try {
			List<TransformationFunctionWeb> functionList = new ArrayList<TransformationFunctionWeb>();
			String[] functionNames = 
				Context.getApplicationContext().getBeanNamesForType(org.openhie.openempi.transformation.function.AbstractTransformationFunction.class);
			System.out.println("Transormation Function Names:");
			for (String functionName: functionNames) {
				TransformationFunctionWeb compFunc = new TransformationFunctionWeb();
				compFunc.setClassName(functionName);
				System.out.println(" " + functionName);
				functionList.add(compFunc);
			}
			return functionList;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<CustomFieldWeb> loadCustomFieldsConfiguration() throws Exception {
		log.debug("Received request to retrieve a list of custom fields.");
		
		authenticateCaller();	
		try {
			List<CustomField> customFields = Context.getConfiguration().getCustomFields();
			List<CustomFieldWeb> customFieldsWeb = new ArrayList<CustomFieldWeb>();
			for(CustomField customField : customFields) {
				CustomFieldWeb customFieldWeb = new CustomFieldWeb();
				customFieldWeb.setFieldName(customField.getFieldName());
				customFieldWeb.setSourceFieldName(customField.getSourceFieldName());
				customFieldWeb.setTransformationFunctionName(customField.getTransformationFunctionName());
				
				java.util.HashMap<String,String> customFieldsWebMap = new java.util.HashMap<String, String>();
				for (String key : customField.getConfigurationParameters().keySet())  {
					String value = customField.getConfigurationParameters().get(key);
					customFieldsWebMap.put(key, value);
				}	
				customFieldWeb.setConfigurationParameters(customFieldsWebMap);
				customFieldsWeb.add(customFieldWeb);
			}
			return customFieldsWeb;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	@SuppressWarnings("unchecked")
	public ExactMatchingConfigurationWeb loadExactMatchingConfiguration() throws Exception {
		log.debug("Received request to retrieve the exact matching configuration data.");
		
		authenticateCaller();	
		try {
			Map<String,Object> configurationData = (Map<String,Object>) Context.getConfiguration()
					.lookupConfigurationEntry(ConfigurationRegistry.MATCH_CONFIGURATION);
			List<MatchField> fields = (List<MatchField>) configurationData.get(Constants.MATCHING_FIELDS_REGISTRY_KEY);
			if (fields == null || fields.size() == 0) {
				log.error("The Exact Matching service has not been configured properly; no match fields have been defined.");
				throw new RuntimeException("The Exact Matching service has not been configured properly.");
			}
			List<MatchFieldWeb> webConfig = convertToClientModel(fields);
			ExactMatchingConfigurationWeb matchConfigurationWeb = new ExactMatchingConfigurationWeb();
			matchConfigurationWeb.setMatchFields(webConfig);
			return matchConfigurationWeb;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	public List<VectorConfigurationWeb> loadVectorConfiguration() throws Exception {
		log.debug("Received request to retrieve the vector configuration data.");
		
		authenticateCaller();	
		@SuppressWarnings("unchecked")
		Map<String,Object> configurationData = (Map<String,Object>) Context.getConfiguration()
				.lookupConfigurationEntry(ConfigurationRegistry.MATCH_CONFIGURATION);
		if (configurationData == null) {
			log.warn("Unable to find any vector configuration data.");
			return new ArrayList<VectorConfigurationWeb>();
		}
		@SuppressWarnings("unchecked")
		List<VectorConfiguration> vectorConfig = (List<VectorConfiguration>) configurationData.get(PROBABILISTIC_MATCHING_VECTOR_CONFIGURATION);
		if (vectorConfig == null) {
			log.warn("Unable to find any vector configuration data.");
			return new ArrayList<VectorConfigurationWeb>();
		}
		
		List<VectorConfigurationWeb> vectorConfigs = new ArrayList<VectorConfigurationWeb>();
		for (VectorConfiguration vector : vectorConfig) {
			VectorConfigurationWeb vectorWeb = new VectorConfigurationWeb();
			vectorWeb.setAlgorithmClassification(vector.getAlgorithmClassification());
			vectorWeb.setManualClassification(vector.getManualClassification());
			vectorWeb.setVectorValue(vector.getVectorValue());
			vectorWeb.setWeight(vector.getWeight());
			vectorConfigs.add(vectorWeb);
		}
		return vectorConfigs;
	}

	@SuppressWarnings("unchecked")
	public MatchConfigurationWeb loadProbabilisticMatchingConfiguration() throws Exception {
		log.debug("Received request to retrieve the probabilistic configuration.");
		
		authenticateCaller();	
		try {
			Map<String,Object> configurationData = (Map<String,Object>) Context.getConfiguration()
					.lookupConfigurationEntry(ConfigurationRegistry.MATCH_CONFIGURATION);			
			List<MatchField> fields = (List<MatchField>) configurationData.get(Constants.MATCHING_FIELDS_REGISTRY_KEY);
			if (fields == null || fields.size() == 0) {
				log.error("The Probabilistic Matching service has not been configured properly; no match fields have been defined.");
				throw new RuntimeException("The Probabilistic Matching service has not been configured properly.");
			}
			MatchConfigurationWeb matchConfigurationWeb = new MatchConfigurationWeb();
			matchConfigurationWeb.setMatchFields(convertToClientModel(fields));
			Float falseNegativeProbability = (Float) configurationData.get(FALSE_NEGATIVE_PROBABILITY_REGISTRY_KEY);
			matchConfigurationWeb.setFalseNegativeProbability(falseNegativeProbability);
			Float falsePositiveProbability = (Float) configurationData.get(FALSE_POSITIVE_PROBABILITY_REGISTRY_KEY);
			matchConfigurationWeb.setFalsePositiveProbability(falsePositiveProbability);
			String configurationDirectory = (String) configurationData.get(CONFIGURATION_DIRECTORY_REGISTRY_KEY);
			matchConfigurationWeb.setConfigFileDirectory(configurationDirectory);
			int index=0;
			double[] mValues = (double[]) configurationData.get(PROBABILISTIC_MATCHING_M_VALUES_KEY);
			double[] uValues = (double[]) configurationData.get(PROBABILISTIC_MATCHING_U_VALUES_KEY);
			for (MatchFieldWeb field : matchConfigurationWeb.getMatchFields()) {
				if (mValues != null && index < mValues.length) {
					field.setMValue(mValues[index]);
				} else {
					field.setMValue(0d);
				}
				if (uValues != null && index < uValues.length) {
					field.setUValue(uValues[index]);
				} else {
					field.setUValue(0d);
				}
				index++;
			}
			matchConfigurationWeb.setPValue((Double) configurationData.get(PROBABILISTIC_MATCHING_P_VALUE_KEY));
			matchConfigurationWeb.setInitialMValue((Double) configurationData.get(PROBABILISTIC_MATCHING_INITIAL_M_VALUE_KEY));
			matchConfigurationWeb.setInitialUValue((Double) configurationData.get(PROBABILISTIC_MATCHING_INITIAL_U_VALUE_KEY));
			matchConfigurationWeb.setInitialPValue((Double) configurationData.get(PROBABILISTIC_MATCHING_INITIAL_P_VALUE_KEY));
			matchConfigurationWeb.setLowerBound((Double) configurationData.get(PROBABILISTIC_MATCHING_LOWER_BOUND_KEY));
			matchConfigurationWeb.setUpperBound((Double) configurationData.get(PROBABILISTIC_MATCHING_UPPER_BOUND_KEY));
			Integer maxIterations = (Integer) configurationData.get(PROBABILISTIC_MATCHING_MAX_ITERATIONS_KEY);
			if (maxIterations != null) {
				matchConfigurationWeb.setMaxIterations(maxIterations);
			} else {
				matchConfigurationWeb.setMaxIterations(DEFAULT_MAX_EM_ITERATIONS);
			}
			
			Boolean logByVectors = (Boolean) configurationData.get(PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_KEY);
			if (logByVectors != null && logByVectors.booleanValue()) {
				loadLogByVectorsConfiguration(matchConfigurationWeb, configurationData);
			}
			Boolean logByWeight = (Boolean) configurationData.get(PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_KEY);
			if (logByWeight != null && logByWeight.booleanValue()) {
				loadLogByWeightConfiguration(matchConfigurationWeb, configurationData);
			}
			
			String logDestination = (String) configurationData.get(PROBABILISTIC_MATCHING_LOGGING_DESTIONATION);
			if (logDestination == null || 
					logDestination.toString().equalsIgnoreCase(Constants.PROBABILISTIC_MATCHING_LOGGING_DESTINATION_TO_FILE)) {
				matchConfigurationWeb.setLoggingDestination(MatchConfigurationWeb.LOG_TO_FILE_DESTINATION);
			} else {
				matchConfigurationWeb.setLoggingDestination(MatchConfigurationWeb.LOG_TO_DB_DESTINATION);
			}
			return matchConfigurationWeb;
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	private void loadLogByWeightConfiguration(MatchConfigurationWeb config, Map<String, Object> data) {
		config.setLoggingByWeight((Boolean) data.get(PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_KEY));	
		
		config.setLoggingByWeightFraction((Double) data.get(PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_FRACTION_KEY));		
		config.setLoggingByWeightLowerBound((Double) data.get(PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_LOWER_BOUND_KEY));		
		config.setLoggingByWeightUpperBound((Double) data.get(PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_UPPER_BOUND_KEY));		
	}

	@SuppressWarnings("unchecked")
	private void loadLogByVectorsConfiguration(MatchConfigurationWeb config, Map<String, Object> data) {		
		config.setLoggingByVectors((Boolean) data.get(PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_KEY));	
		
		config.setLoggingVectors((Set<Integer>) data.get(PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_VECTORS_KEY));
		config.setLoggingByVectorsFraction((Double) data.get(PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_FRACTION_KEY));		
	}

	public Float getCleanFloatFromDouble(Double value) {
		if (value != null) {
			return value.floatValue();
		} else {
			return 0f;
		}
	}
	
	/**
	 * TODO This needs to be moved out of here and into a service that is specific to the matching algorithm
	 * that it configures.
	 */
	public String saveProbabilisticMatchingConfiguration(MatchConfigurationWeb matchConfigurationWeb) throws Exception {
		log.debug("Received request to save match configuration.");
		
		authenticateCaller();	
		String returnMessage = "";
		try {
			Configuration configuration = Context.getConfiguration();
			ConfigurationLoader loader = configuration.getMatchingConfigurationLoader();
			List<MatchField> fields = convertMatchFieldsFromClientModel(matchConfigurationWeb.getMatchFields());
			Map<String,Object> configurationData = new java.util.HashMap<String,Object>();
			configurationData.put(Constants.MATCHING_FIELDS_REGISTRY_KEY, fields);
			configurationData.put(FALSE_NEGATIVE_PROBABILITY_REGISTRY_KEY, matchConfigurationWeb.getFalseNegativeProbability());
			configurationData.put(FALSE_POSITIVE_PROBABILITY_REGISTRY_KEY, matchConfigurationWeb.getFalsePositiveProbability());
			configurationData.put(CONFIGURATION_DIRECTORY_REGISTRY_KEY, matchConfigurationWeb.getConfigFileDirectory());
			
			configurationData.put(PROBABILISTIC_MATCHING_P_VALUE_KEY, matchConfigurationWeb.getPValue());
			configurationData.put(PROBABILISTIC_MATCHING_INITIAL_M_VALUE_KEY, matchConfigurationWeb.getInitialMValue());
			configurationData.put(PROBABILISTIC_MATCHING_INITIAL_U_VALUE_KEY, matchConfigurationWeb.getInitialUValue());
			configurationData.put(PROBABILISTIC_MATCHING_INITIAL_P_VALUE_KEY, matchConfigurationWeb.getInitialPValue());
			configurationData.put(PROBABILISTIC_MATCHING_LOWER_BOUND_KEY, matchConfigurationWeb.getLowerBound());
			configurationData.put(PROBABILISTIC_MATCHING_UPPER_BOUND_KEY, matchConfigurationWeb.getUpperBound());
			configurationData.put(PROBABILISTIC_MATCHING_MAX_ITERATIONS_KEY, matchConfigurationWeb.getMaxIterations());
			double[] mValues = new double[fields.size()];
			double[] uValues = new double[fields.size()];
			int index=0;
			for (MatchFieldWeb field : matchConfigurationWeb.getMatchFields()) {
				mValues[index] = field.getMValue().doubleValue();
				uValues[index] = field.getUValue().doubleValue();
				index++;
			}
			configurationData.put(PROBABILISTIC_MATCHING_M_VALUES_KEY, mValues);
			configurationData.put(PROBABILISTIC_MATCHING_U_VALUES_KEY, uValues);

			if (matchConfigurationWeb.getLoggingByVectors() != null && matchConfigurationWeb.getLoggingByVectors().booleanValue()) {
				saveLogByVectorsConfiguration(matchConfigurationWeb, configurationData);
			}
			if (matchConfigurationWeb.getLoggingByWeight() != null && matchConfigurationWeb.getLoggingByWeight().booleanValue()) {
				saveLogByWeightConfiguration(matchConfigurationWeb, configurationData);
			}
			
			if (matchConfigurationWeb.getLoggingDestination() != null) {
				String logDestination = null;
				if (matchConfigurationWeb.getLoggingDestination().equalsIgnoreCase(MatchConfigurationWeb.LOG_TO_DB_DESTINATION)) {
					logDestination = Constants.PROBABILISTIC_MATCHING_LOGGING_DESTINATION_TO_DB;		
				} else if (	matchConfigurationWeb.getLoggingDestination().equalsIgnoreCase(MatchConfigurationWeb.LOG_TO_FILE_DESTINATION)) {
					logDestination = Constants.PROBABILISTIC_MATCHING_LOGGING_DESTINATION_TO_FILE;
				}
				configurationData.put(PROBABILISTIC_MATCHING_LOGGING_DESTIONATION, logDestination);
			}
			
			List<VectorConfiguration> vectorConfigurations = new ArrayList<VectorConfiguration>(matchConfigurationWeb.getVectorConfigurations().size());
			for (VectorConfigurationWeb vectorConf : matchConfigurationWeb.getVectorConfigurations()) {
				 VectorConfiguration vectorConfiguration = new VectorConfiguration();
				 vectorConfiguration.setAlgorithmClassification(vectorConf.getAlgorithmClassification());
				 vectorConfiguration.setManualClassification(vectorConf.getManualClassification());
				 vectorConfiguration.setWeight(vectorConf.getWeight());
				 vectorConfiguration.setVectorValue(vectorConf.getVectorValue());
				 vectorConfigurations.add(vectorConfiguration);				 
			}			
			configurationData.put(PROBABILISTIC_MATCHING_VECTOR_CONFIGURATION, vectorConfigurations);
			
			loader.saveAndRegisterComponentConfiguration(configuration, configurationData);
		} catch (Exception e) {
			log.warn("Failed while saving the probabilistic matching configuration: " + e, e);
			throw new RuntimeException("Failed while saving the matching configuration data: " + e.getMessage());
		}		
		return returnMessage;
	}
	
	private void saveLogByWeightConfiguration(MatchConfigurationWeb config, Map<String, Object> data) {
		data.put(PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_KEY, Boolean.TRUE);
		data.put(PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_LOWER_BOUND_KEY, config.getLoggingByWeightLowerBound());
		data.put(PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_UPPER_BOUND_KEY, config.getLoggingByWeightUpperBound());
		data.put(PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_FRACTION_KEY, config.getLoggingByWeightFraction());
	}

	private void saveLogByVectorsConfiguration(MatchConfigurationWeb config, Map<String, Object> data) {
		data.put(PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_KEY, Boolean.TRUE);
		data.put(PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_VECTORS_KEY, config.getLoggingVectors());
		data.put(PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_FRACTION_KEY, config.getLoggingByVectorsFraction());
	}

	public String saveExactMatchingConfiguration(ExactMatchingConfigurationWeb matchingConfig) throws Exception {
		log.debug("Received request to save exact matching configuration.");
		
		authenticateCaller();	
		String returnMessage = "";
		try {
			Configuration configuration = Context.getConfiguration();
			ConfigurationLoader loader = configuration.getMatchingConfigurationLoader();
			List<MatchField> fields = convertMatchFieldsFromClientModel(matchingConfig.getMatchFields());
			Map<String,Object> configurationData = new java.util.HashMap<String,Object>();
			configurationData.put(Constants.MATCHING_FIELDS_REGISTRY_KEY, fields);
			loader.saveAndRegisterComponentConfiguration(configuration, configurationData);
		} catch (Exception e) {
			log.warn("Failed while saving the exact matching configuration: " + e, e);
			throw new RuntimeException("Failed while saving the matching configuration data: " + e.getMessage());
		}		
		return returnMessage;
	}

	public String saveCustomFieldsConfiguration(List<CustomFieldWeb> customFieldsWeb) throws Exception {
		log.debug("Received request to save a list of custom fields.");
		
		authenticateCaller();	
		String returnMessage = "";
		try {
			Configuration configuration = Context.getConfiguration();
			configuration.saveAndRegisterCustomFieldsConfiguration(
					convertFromClientModel(customFieldsWeb));
		} catch (Exception e) {
			log.error("Failed to execute: " + e.getMessage(), e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	private List<MatchField> convertMatchFieldsFromClientModel(List<MatchFieldWeb> matchFieldsWeb) {
		List<MatchField> matchFields = new ArrayList<MatchField>();
		for(MatchFieldWeb matchFieldWeb: matchFieldsWeb) {
			MatchField matchField = new MatchField();
			matchField.setFieldName(matchFieldWeb.getFieldName());
			if (matchFieldWeb.getDisagreementProbability() != null) {
				matchField.setAgreementProbability(matchFieldWeb.getAgreementProbability());
			}
			if (matchFieldWeb.getAgreementProbability() != null) {
				matchField.setDisagreementProbability(matchFieldWeb.getDisagreementProbability());
			}
			if (matchFieldWeb.getComparatorFunctionName() != null) {
				matchField.setComparatorFunction(new ComparatorFunction(matchFieldWeb.getComparatorFunctionName()));
			}
			if (matchFieldWeb.getMatchThreshold() != null) {
				matchField.setMatchThreshold(matchFieldWeb.getMatchThreshold());
			}
			matchFields.add(matchField);
		}
		return matchFields;
	}
	
	private List<MatchFieldWeb> convertToClientModel(List<MatchField> fields) {
		List<MatchFieldWeb> matchFieldsWeb = new ArrayList<MatchFieldWeb>();
		for(MatchField matchField : fields) {
			MatchFieldWeb matchFieldWeb = new MatchFieldWeb();
			matchFieldWeb.setFieldName(matchField.getFieldName());
			if (matchField.getComparatorFunction() != null && matchField.getComparatorFunction().getFunctionName() != null) {
				matchFieldWeb.setComparatorFunctionName(matchField.getComparatorFunction().getFunctionName());
			}
			if (matchField.getMatchThreshold() > 0) {
				float threshold = Math.round(matchField.getMatchThreshold()*1000)/1000.0f;
				matchFieldWeb.setMatchThreshold(threshold);
			}
			if (matchField.getDisagreementProbability() > 0) {
				float disagreementProbability = Math.round(matchField.getDisagreementProbability()*1000)/1000.0f;
				matchFieldWeb.setDisagreementProbability(disagreementProbability);
			}
			if (matchField.getAgreementProbability() > 0) {
				float agreementProbability = Math.round(matchField.getAgreementProbability()*1000)/1000.0f;
				matchFieldWeb.setAgreementProbability(agreementProbability);
			}
			matchFieldsWeb.add(matchFieldWeb);
		}
		return matchFieldsWeb;
	}
	private List<CustomField> convertFromClientModel(List<CustomFieldWeb> customFieldsWeb) {
		List<CustomField> customFields = new ArrayList<CustomField>();
		
		for(CustomFieldWeb customFieldWeb: customFieldsWeb) {			
			CustomField customField = new CustomField();
			customField.setFieldName(customFieldWeb.getFieldName());
			customField.setSourceFieldName(customFieldWeb.getSourceFieldName());
			customField.setTransformationFunctionName(customFieldWeb.getTransformationFunctionName());
			
			if( customFieldWeb.getConfigurationParameters() != null ) {
				java.util.HashMap<String,String> customFieldsMap = new java.util.HashMap<String, String>();
				for (String key : customFieldWeb.getConfigurationParameters().keySet())  {
					String value = customFieldWeb.getConfigurationParameters().get(key);
					customFieldsMap.put(key, value);
				}	
				customField.setConfigurationParameters(customFieldsMap);
			}			
			customFields.add(customField);
		}
		return customFields;
	}


//	private MatchConfiguration convertFromClientModel(MatchConfigurationWeb matchConfigurationWeb) {
//		MatchConfiguration matchConfiguration = new MatchConfiguration();
//		matchConfiguration.setFalseNegativeProbability(matchConfigurationWeb.getFalseNegativeProbability());
//		matchConfiguration.setFalsePositiveProbability(matchConfigurationWeb.getFalsePositiveProbability());
//		List<MatchField> matchFields = new ArrayList<MatchField>();
//		for(MatchFieldWeb matchFieldWeb: matchConfigurationWeb.getMatchFields()) {
//			MatchField matchField = new MatchField();
//			matchField.setFieldName(matchFieldWeb.getFieldName());
//			matchField.setAgreementProbability(matchFieldWeb.getAgreementProbability());
//			matchField.setDisagreementProbability(matchFieldWeb.getDisagreementProbability());
//			matchField.setComparatorFunction(new ComparatorFunction(matchFieldWeb.getComparatorFunctionName()));
//			matchField.setMatchThreshold(matchFieldWeb.getMatchThreshold());
//			matchFields.add(matchField);
//		}
//		matchConfiguration.setMatchFields(matchFields);
//		matchConfiguration.setConfigFileDirectory(matchConfigurationWeb.getConfigFileDirectory());
//		return matchConfiguration;
//	}

}
