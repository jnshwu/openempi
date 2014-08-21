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
package org.openempi.webapp.client.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class MatchConfigurationWeb extends BaseModelData implements Serializable
{
	public static final String FALSE_NEGATIVE_PROBABILITY = "falseNegativeProbability";
	public static final String FALSE_POSITIVE_PROBABILITY = "falsePositiveProbability";
	public final static String P_VALUE_KEY = "pValue";
	public final static String INITIAL_M_VALUE_KEY = "initialMValue";
	public final static String INITIAL_U_VALUE_KEY = "initialUValue";
	public final static String INITIAL_P_VALUE_KEY = "initialPValue";
	public final static String MAX_ITERATIONS_KEY = "maxIterations";
	public final static String LOWER_BOUND_KEY = "lowerBound";
	public final static String UPPER_BOUND_KEY = "upperBound";
	public static final String MATCH_FIELDS = "matchFields";
	public static final String CONFIG_FILE_DIRECTORY = "configFileDirectory";
	public final static String LOGGING_BY_VECTORS_KEY = "loggingByVectors";
	public final static String LOGGING_BY_VECTORS_VECTORS_KEY = "loggingVectorsIntegerSet";
	public final static String LOGGING_BY_VECTORS_FRACTION_KEY = "loggingVectorsFraction";
	public final static String LOGGING_BY_WEIGHT_KEY = "loggingByWeight";
	public final static String LOGGING_BY_WEIGHT_LOWER_BOUND_KEY = "loggingWeightLowerBound";
	public final static String LOGGING_BY_WEIGHT_UPPER_BOUND_KEY = "loggingWeightUpperBound";
	public final static String LOGGING_BY_WEIGHT_FRACTION_KEY = "loggingWeightFraction";
	public final static String LOGGING_DESTINATION = "loggingDestination";

	public final static String LOG_TO_FILE_DESTINATION = "Log to File";
	public final static String LOG_TO_DB_DESTINATION = "Log to DB";

	public final static String VECTOR_CONFIGURATIONS = "vectorConfigurations";	

	@SuppressWarnings("unused")
    private VectorConfigurationWeb unusedVectorConfigurationWeb;
	
	public MatchFieldWeb dummyField;	// This is need so MatchFieldWeb will be included as serializable class in the SerializationPolicy in the GWT compiled RPC file
	
	public MatchConfigurationWeb() {
	}

	public java.lang.Float getFalseNegativeProbability() {
		return get(FALSE_NEGATIVE_PROBABILITY);
	}

	public void setFalseNegativeProbability(java.lang.Float falseNegativeProbability) {
		set(FALSE_NEGATIVE_PROBABILITY, falseNegativeProbability);
	}

	public java.lang.Float getFalsePositiveProbability() {
		return get(FALSE_POSITIVE_PROBABILITY);
	}

	public void setFalsePositiveProbability(java.lang.Float falsePositiveProbability) {
		set(FALSE_POSITIVE_PROBABILITY, falsePositiveProbability);
	}

	public List<org.openempi.webapp.client.model.MatchFieldWeb> getMatchFields() {
		return get(MATCH_FIELDS);
	}

	public void setMatchFields(List<org.openempi.webapp.client.model.MatchFieldWeb> matchFields) {
		set(MATCH_FIELDS, matchFields);
	}

	public java.lang.String getConfigFileDirectory() {
		return get(CONFIG_FILE_DIRECTORY);
	}

	public void setConfigFileDirectory(java.lang.String configFileDirectory) {
		set(CONFIG_FILE_DIRECTORY, configFileDirectory);
	}

	public java.lang.Double getInitialMValue() {
		return get(INITIAL_M_VALUE_KEY);
	}
	
	public void setInitialMValue(java.lang.Double initialMValue) {
		set(INITIAL_M_VALUE_KEY, initialMValue);
	}
	
	public java.lang.Double getInitialUValue() {
		return get(INITIAL_U_VALUE_KEY);
	}
	
	public void setInitialUValue(java.lang.Double initialUValue) {
		set(INITIAL_U_VALUE_KEY, initialUValue);
	}

	public java.lang.Double getInitialPValue() {
		return get(INITIAL_P_VALUE_KEY);
	}
	
	public void setInitialPValue(java.lang.Double initialPValue) {
		set(INITIAL_P_VALUE_KEY, initialPValue);
	}

	public java.lang.Double getPValue() {
		return get(P_VALUE_KEY);
	}
	
	public void setPValue(java.lang.Double pValue) {
		set(P_VALUE_KEY, pValue);
	}
	
	public java.lang.Integer getMaxIterations() {
		return get(MAX_ITERATIONS_KEY);
	}
	
	public void setMaxIterations(java.lang.Integer maxIterations) {
		set(MAX_ITERATIONS_KEY, maxIterations);
	}

	public java.lang.Double getLowerBound() {
		return get(LOWER_BOUND_KEY);
	}
	
	public void setLowerBound(java.lang.Double lowerBound) {
		set(LOWER_BOUND_KEY, lowerBound);
	}
	
	public java.lang.Double getUpperBound() {
		return get(UPPER_BOUND_KEY);
	}
	
	public void setUpperBound(java.lang.Double upperBound) {
		set(UPPER_BOUND_KEY, upperBound);
	}

	public java.lang.Boolean getLoggingByVectors() {
		return get(LOGGING_BY_VECTORS_KEY);
	}
	
	public void setLoggingByVectors(java.lang.Boolean loggingByVectors) {
		set(LOGGING_BY_VECTORS_KEY, loggingByVectors);
	}
	
	public java.lang.Boolean getLoggingByWeight() {
		return get(LOGGING_BY_WEIGHT_KEY);
	}
	
	public void setLoggingByWeight(java.lang.Boolean loggingByWeight) {
		set(LOGGING_BY_WEIGHT_KEY, loggingByWeight);
	}
	
	public java.lang.Double getLoggingByVectorsFraction() {
		return get(LOGGING_BY_VECTORS_FRACTION_KEY);
	}
	
	public void setLoggingByVectorsFraction(java.lang.Double loggingByVectorsFraction) {
		set(LOGGING_BY_VECTORS_FRACTION_KEY, loggingByVectorsFraction);
	}
	
	public java.lang.Double getLoggingByWeightFraction() {
		return get(LOGGING_BY_WEIGHT_FRACTION_KEY);
	}
	
	public void setLoggingByWeightFraction(java.lang.Double loggingByWeightFraction) {
		set(LOGGING_BY_WEIGHT_FRACTION_KEY, loggingByWeightFraction);
	}
	
	public Set<java.lang.Integer> getLoggingVectors() {
		return get(LOGGING_BY_VECTORS_VECTORS_KEY);
	}
	
	public void setLoggingVectors(Set<java.lang.Integer> loggingVectors) {
		set(LOGGING_BY_VECTORS_VECTORS_KEY, loggingVectors);
	}
	
	public java.lang.Double getLoggingByWeightLowerBound() {
		return get(LOGGING_BY_WEIGHT_LOWER_BOUND_KEY);
	}
	
	public void setLoggingByWeightLowerBound(java.lang.Double loggingByWeightLowerBound) {
		set(LOGGING_BY_WEIGHT_LOWER_BOUND_KEY, loggingByWeightLowerBound);
	}
	
	public java.lang.Double getLoggingByWeightUpperBound() {
		return get(LOGGING_BY_WEIGHT_UPPER_BOUND_KEY);
	}
	
	public void setLoggingByWeightUpperBound(java.lang.Double loggingByWeightUpperBound) {
		set(LOGGING_BY_WEIGHT_UPPER_BOUND_KEY, loggingByWeightUpperBound);
	}

	public java.lang.String getLoggingDestination() {
		String logDestination = get(LOGGING_DESTINATION);
		if (logDestination == null ) {
			return LOG_TO_FILE_DESTINATION;
		}
		return logDestination;
	}

	public void setLoggingDestination(java.lang.String logDestination) {
		if (logDestination == null ) {
			set(LOGGING_DESTINATION, LOG_TO_FILE_DESTINATION);
			return;
		}
		set(LOGGING_DESTINATION, logDestination);
	}
	
	public List<VectorConfigurationWeb> getVectorConfigurations() {
		return get("VECTOR_CONFIGURATIONS");
	}

	public void setVectorConfigurations(List<VectorConfigurationWeb> vectorConfigurations) {
		set("VECTOR_CONFIGURATIONS", vectorConfigurations);
	}	
}
