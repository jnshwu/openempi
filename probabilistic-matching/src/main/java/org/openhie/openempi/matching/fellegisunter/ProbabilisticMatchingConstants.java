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
package org.openhie.openempi.matching.fellegisunter;

public class ProbabilisticMatchingConstants
{
	public final static Integer CLASSIFICATION_MATCH = 1;
	public final static Integer CLASSIFICATION_NON_MATCH = 2;
	public final static Integer CLASSIFICATION_PROBABLE_MATCH = 3;
	public final static String CONFIGURATION_DIRECTORY_REGISTRY_KEY = "configDirectory";
	public final static float DEFAULT_FALSE_NEGATIVE_PROBABILITY = 0.9f;
	public final static float DEFAULT_FALSE_POSITIVE_PROBABILITY = 0.9f;
	public final static float DEFAULT_UPPER_BOUND = 15.0f;
	public final static float DEFAULT_LOWER_BOUND = -5.0f;
	public final static String FALSE_NEGATIVE_PROBABILITY_REGISTRY_KEY = "falseNegativeProbability";
	public final static String FALSE_POSITIVE_PROBABILITY_REGISTRY_KEY = "falsePositiveProbability";
	public final static String PROBABILISTIC_MATCHING_ALGORITHM_NAME = "Probabilistic Matching Algorithm";
	public final static String PROBABILISTIC_MATCHING_M_VALUES_KEY = "mValues";
	public final static String PROBABILISTIC_MATCHING_U_VALUES_KEY = "uValues";
	public final static String PROBABILISTIC_MATCHING_P_VALUE_KEY = "pValue";
	public final static String PROBABILISTIC_MATCHING_INITIAL_M_VALUE_KEY = "initialMValue";
	public final static String PROBABILISTIC_MATCHING_INITIAL_U_VALUE_KEY = "initialUValue";
	public final static String PROBABILISTIC_MATCHING_INITIAL_P_VALUE_KEY = "initialPValue";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_KEY = "loggingByVectors";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_VECTORS_KEY = "loggingVectorsIntegerSet";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_FRACTION_KEY = "loggingVectorsFraction";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_KEY = "loggingByWeight";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_LOWER_BOUND_KEY = "loggingWeightLowerBound";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_UPPER_BOUND_KEY = "loggingWeightUpperBound";
	public final static String PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_FRACTION_KEY = "loggingWeightFraction";
	public final static String PROBABILISTIC_MATCHING_LOGGING_DESTINATION = "loggingDestination";
	public final static String PROBABILISTIC_MATCHING_LOWER_BOUND_KEY = "lowerBound";
	public final static String PROBABILISTIC_MATCHING_MAX_ITERATIONS_KEY = "maxIterations";
	public final static String PROBABILISTIC_MATCHING_UPPER_BOUND_KEY = "upperBound";
	public final static String PROBABILISTIC_MATCHING_VECTOR_CLASSIFICATIONS = "vectorClassifications";
	public final static String PROBABILISTIC_MATCHING_VECTOR_CONFIGURATION = "vectorConfiguration";
}
