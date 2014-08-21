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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.InitializationException;
import org.openhie.openempi.blocking.RecordPairIterator;
import org.openhie.openempi.blocking.RecordPairSource;
import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.MatchField;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.context.Context.ObservationEventType;
import org.openhie.openempi.dao.PersonLinkDao;
import org.openhie.openempi.matching.AbstractMatchingLifecycleObserver;
import org.openhie.openempi.matching.MatchingService;
import org.openhie.openempi.model.ComparisonVector;
import org.openhie.openempi.model.LinkSource;
import org.openhie.openempi.model.LoggedLink;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.model.ReviewRecordPair;
import org.openhie.openempi.notification.MatchingConfigurationUpdatedObservable;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.util.ConvertUtil;

public class ProbabilisticMatchingService extends AbstractMatchingLifecycleObserver implements MatchingService,
        Observer
{
    private final static String BLOCKING_ROUNDS_REGISTRY_KEY = "blockingRounds";
    private HashMap<String, MatchField> matchFieldByName;
    private PersonLinkDao personLinkDao;
    private String[] matchFieldNames;
    private MatchField[] matchFields;
    private boolean initialized = false;
    private FellegiSunterParameters fellegiSunterParams;
    private static final Double MIN_MARGINAL_VALUE = 0.0000001;
    private static final Double MAX_MARGINAL_VALUE = 0.9999999;
    private HashMap<Integer, ComparisonVector> vectorByValueMap = new HashMap<Integer, ComparisonVector>();
    private List<MatchField> fields;
    private Float falseNegativeProbability;
    private Float falsePositiveProbability;
    private String configurationDirectory;
    private boolean logByVectors;
    private double logByVectorsFraction;
    private boolean logByWeight;
    private String logDestination;
    private Set<Integer> vectorsToLog;
    private Set<String> blockingFieldList;
    private List<String> logFieldList;
    private double logWeightLowerBound;
    private double logWeightUpperBound;
    private double logByWeightFraction;

    public void startup() throws InitializationException {
        try {
            loadConfiguration();
        } catch (Throwable t) {
            log.error("Failed while initializing the probabilistic matching service: " + t, t);
            initialized = false;
            try {
                linkRecords();
                @SuppressWarnings("unchecked")
                Map<String, Object> configurationData = (Map<String, Object>) Context.getConfiguration()
                        .lookupConfigurationEntry(ConfigurationRegistry.MATCH_CONFIGURATION);
                VectorConfigurationHelper.updateVectorConfiguration(configurationData, fellegiSunterParams);
            } catch (Exception e) {
                log.error("Failed while initializing the probabilistic matching service: " + e, e);
                initialized = false;
            }
        }
        Context.registerObserver(this, ObservationEventType.MATCHING_CONFIGURATION_UPDATED_EVENT);
    }

    public Set<RecordPair> match(Record record) throws ApplicationException {
        log.trace("Looking for matches on record " + record);
        if (!initialized) {
            throw new ApplicationException("Matching service has not been initialized yet.");
        }
        List<RecordPair> pairs = Context.getBlockingService().findCandidates(record);
        Set<RecordPair> matches = new java.util.HashSet<RecordPair>();
        scoreRecordPairs(pairs);
        calculateRecordPairWeights(pairs, fellegiSunterParams);

        // Apply Fellegi-Sunter classification rule to each pair
        for (RecordPair pair : pairs) {
            classifyRecordPair(pair);
            if (pair.getMatchOutcome() == RecordPair.MATCH_OUTCOME_LINKED) {
                matches.add(pair);
            } else if (pair.getMatchOutcome() == RecordPair.MATCH_OUTCOME_POSSIBLE) {
                // This is a possible match; need to add it to the list for
                // review
                addReviewRecordPair(pair);
            }
        }

        if (isLoggingEnabled()) {
            for (RecordPair pair : pairs) {
                logRecordPair(pair);
            }
        }
        return matches;
    }

    private void addReviewRecordPair(RecordPair pair) {
        ReviewRecordPair reviewPairFound = personLinkDao.getReviewRecordPair(pair.getLeftRecord().getRecordId()
                .intValue(), pair.getRightRecord().getRecordId().intValue());
        if (reviewPairFound != null) {
            return;
        }
        ReviewRecordPair reviewRecordPair = ConvertUtil.buildReviewPair(pair);
        log.trace("Adding review record pair: " + reviewRecordPair);
        personLinkDao.addReviewRecordPair(reviewRecordPair);
    }

    public RecordPair match(RecordPair recordPair) throws ApplicationException {
        if (log.isTraceEnabled()) {
            log.trace("Looking for matches on record pair " + recordPair);
        }
        if (!initialized) {
            throw new ApplicationException("Matching service has not been initialized yet.");
        }
        scoreRecordPair(recordPair);
        calculateRecordPairWeight(recordPair, fellegiSunterParams);

        log.debug("Record pair weight (" + recordPair.getLeftRecord().getRecordId() + ","
                + recordPair.getRightRecord().getRecordId() + ") is: " + recordPair.getWeight());
        classifyRecordPair(recordPair);
        if (isLoggingEnabled()) {
            logRecordPair(recordPair);
        }
        return recordPair;
    }

    public void linkRecords() {
        log.info("Retrieving all record pairs before initializing the classification model.");
        List<RecordPair> pairs = getRecordPairs();
        log.info("Scoring all record pairs retrieved from the blocking service.");
        scoreRecordPairs(pairs);
        fellegiSunterParams = new FellegiSunterParameters(getFields().size());
        ProbabilisticMatchingConfigurationLoader.loadDefaultValues(fellegiSunterParams);
        fellegiSunterParams.setMatchingFieldNames(matchFieldNames);
        fellegiSunterParams.setMu(getFalsePositiveProbability());
        fellegiSunterParams.setLambda(getFalseNegativeProbability());
        log.info("Calculating vector frequencies.");
        calculateVectorFrequencies(pairs, fellegiSunterParams);
        log.info("Estimating model conditional distributions.");
        estimateMarginalProbabilities(fellegiSunterParams);
        adjustProbabilityValues(fellegiSunterParams);
        log.info("Calculating weights for all record pairs.");
        calculateRecordPairWeights(pairs, fellegiSunterParams);
        log.info("Ordering record pairs using weight value.");
        orderRecordPairsByWeight(pairs);
        calculateMarginalProbabilities(pairs, fellegiSunterParams);
        // calculateBoundsOnVectors(fellegiSunterParams);
        log.info("Calculating classification model lower and upper bounds.");
        calculateBounds(pairs, fellegiSunterParams);
        FellegiSunterConfigurationManager.saveParameters(getConfigurationDirectory(), fellegiSunterParams);
        initialized = true;
    }

    private void classifyRecordPair(RecordPair recordPair) {
        recordPair.setLinkSource(new LinkSource(getMatchingServiceId()));
        Integer manualClassification = lookupManualConfigurationValue(recordPair);
        if (manualClassification == null) {
            recordPair.setLinkSource(new LinkSource(getMatchingServiceId()));
            if (recordPair.getWeight() >= fellegiSunterParams.getUpperBound()) {
                recordPair.setMatchOutcome(RecordPair.MATCH_OUTCOME_LINKED);
            } else if (recordPair.getWeight() <= fellegiSunterParams.getLowerBound()) {
                recordPair.setMatchOutcome(RecordPair.MATCH_OUTCOME_UNLINKED);
            } else {
                recordPair.setMatchOutcome(RecordPair.MATCH_OUTCOME_POSSIBLE);
            }
            if (log.isTraceEnabled()) {
                log.trace("Vector with value " + recordPair.getComparisonVector().getBinaryVectorString()
                        + " was classified as " + decodeClassification(recordPair.getMatchOutcome()));
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Used manual override to classify vector "
                        + recordPair.getComparisonVector().getBinaryVectorString() + " as "
                        + decodeClassification(manualClassification));
            }
            recordPair.setMatchOutcome(manualClassification);
        }
    }

    private String decodeClassification(Integer value) {
        if (value == RecordPair.MATCH_OUTCOME_LINKED) {
            return "Link";
        } else if (value == RecordPair.MATCH_OUTCOME_POSSIBLE) {
            return "Possible Link";
        } else {
            return "Non-link";
        }
    }

    @SuppressWarnings("unchecked")
    private Integer lookupManualConfigurationValue(RecordPair recordPair) {
        Map<String, Object> configurationData = (Map<String, Object>) Context.getConfiguration()
                .lookupConfigurationEntry(ConfigurationRegistry.MATCH_CONFIGURATION);
        Map<Integer, Integer> vectorClassifications = (Map<Integer, Integer>) configurationData
                .get(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_VECTOR_CLASSIFICATIONS);
        if (vectorClassifications == null) {
            return null;
        }
        Integer manualClassification = vectorClassifications.get(recordPair.getComparisonVector()
                .getBinaryVectorValue());
        return manualClassification;
    }

    private void adjustProbabilityValues(FellegiSunterParameters params) {
        for (int i = 0; i < fellegiSunterParams.getFieldCount(); i++) {
            if (fellegiSunterParams.getMValue(i) > MAX_MARGINAL_VALUE) {
                logAdjustment("m", i, MAX_MARGINAL_VALUE, fellegiSunterParams.getMValue(i));
                fellegiSunterParams.setMValue(i, MAX_MARGINAL_VALUE);
            }
            if (fellegiSunterParams.getMValue(i) < MIN_MARGINAL_VALUE) {
                logAdjustment("m", i, MIN_MARGINAL_VALUE, fellegiSunterParams.getMValue(i));
                fellegiSunterParams.setMValue(i, MIN_MARGINAL_VALUE);
            }
            if (fellegiSunterParams.getUValue(i) > MAX_MARGINAL_VALUE) {
                logAdjustment("u", i, MAX_MARGINAL_VALUE, fellegiSunterParams.getUValue(i));
                fellegiSunterParams.setUValue(i, MAX_MARGINAL_VALUE);
            }
            if (fellegiSunterParams.getUValue(i) < MIN_MARGINAL_VALUE) {
                logAdjustment("u", i, MIN_MARGINAL_VALUE, fellegiSunterParams.getUValue(i));
                fellegiSunterParams.setUValue(i, MIN_MARGINAL_VALUE);
            }
        }
    }

    private void logAdjustment(String prob, int index, double newValue, double oldValue) {
        log.info("Adjusting " + prob + "-value for field index " + index + " to " + newValue + " from " + oldValue);
    }

    public int getMatchingServiceId() {
        return LinkSource.PROBABILISTIC_MATCHING_ALGORITHM_SOURCE;
    }

    public FellegiSunterParameters getFellegiSunterParameters() {
        return fellegiSunterParams;
    }

    public List<RecordPair> getRecordPairs() {
        RecordPairSource source = Context.getBlockingService().getRecordPairSource();
        // BlockingService blockingService = (BlockingService)
        // Context.getApplicationContext()
        // .getBean(Constants.NAIVE_BLOCKING_SERVICE);
        // RecordPairSource source = blockingService.getRecordPairSource();
        List<RecordPair> pairs = new ArrayList<RecordPair>();
        int pairCount = 0;
        for (RecordPairIterator iter = source.iterator(); iter.hasNext();) {
            pairs.add(iter.next());
            pairCount++;
            if (pairCount % 10000 == 0) {
                log.info("Loaded " + pairCount + " pairs.");
            }
        }
        return pairs;
    }

    public void scoreRecordPair(RecordPair pair) {
        StringComparisonService comparisonService = Context.getStringComparisonService();
        pair.setComparisonVector(new ComparisonVector(matchFields));
        for (int i = 0; i < matchFieldNames.length; i++) {
            String fieldName = matchFieldNames[i];
            Object value1 = pair.getLeftRecord().get(fieldName);
            Object value2 = pair.getRightRecord().get(fieldName);
            double distance = comparisonService.score(matchFields[i].getComparatorFunction().getFunctionName(),
                    matchFields[i].getComparatorFunction().getParameterMap(), value1, value2);
            pair.getComparisonVector().setScore(i, distance);
        }
        log.debug("Comparing records " + pair.getLeftRecord().getRecordId() + " and "
                + pair.getRightRecord().getRecordId() + " got vector "
                + pair.getComparisonVector().getBinaryVectorString());
    }

    public void scoreRecordPairs(List<RecordPair> pairs) {
        StringComparisonService comparisonService = Context.getStringComparisonService();
        for (RecordPair pair : pairs) {
            pair.setComparisonVector(new ComparisonVector(matchFields));
            for (int i = 0; i < matchFieldNames.length; i++) {
                String fieldName = matchFieldNames[i];
                Object value1 = pair.getLeftRecord().get(fieldName);
                Object value2 = pair.getRightRecord().get(fieldName);
                double distance = comparisonService.score(matchFields[i].getComparatorFunction().getFunctionName(),
                        matchFields[i].getComparatorFunction().getParameterMap(), value1, value2);
                pair.getComparisonVector().setScore(i, distance);
            }
            log.debug("Comparing records " + pair.getLeftRecord().getRecordId() + " and "
                    + pair.getRightRecord().getRecordId() + " got vector "
                    + pair.getComparisonVector().getBinaryVectorString());
        }
    }

    public void calculateVectorFrequencies(List<RecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
        for (int i = 0; i < fellegiSunterParams.getVectorCount(); i++) {
            fellegiSunterParams.setVectorFrequency(i, 0);
        }
        for (RecordPair pair : pairs) {
            ComparisonVector vector = pair.getComparisonVector();
            if (vector.getBinaryVectorValue() == 2) {
                log.debug(vector.getBinaryVectorString() + "=>" + vector.getScoreVectorString() + "=>"
                        + this.getRecordPairMatchFields(pair));
            }
            fellegiSunterParams.incrementVectorFrequency(vector.getBinaryVectorValue());
        }
        if (log.isDebugEnabled()) {
            fellegiSunterParams.logVectorFrequencies(log);
        }
    }

    public void calculateRecordPairWeights(List<RecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
        // First calculate the weight by vector configuration; this way we won't
        // have to
        // repeat the calculation for every record pair; there are only 2^n
        // vector configurations
        // where n is the number of match fields.
        int vectorCount = fellegiSunterParams.getVectorCount();
        for (int index = 0; index < vectorCount; index++) {
            calculateVectorWeight(index, fellegiSunterParams);
        }
        for (RecordPair pair : pairs) {
            calculateRecordPairWeight(pair, fellegiSunterParams);
        }
    }

    private void calculateVectorWeight(int vectorValue, FellegiSunterParameters fellegiSunterParams) {
        int bitPositions = fellegiSunterParams.getFieldCount();
        double weight = 0;
        for (int i = 0; i < bitPositions; i++) {
            int bitPosValue = getBitValueAtPosition(vectorValue, i);
            if (bitPosValue == 1) {
                Double numerator = fellegiSunterParams.getMValue(i);
                Double denominator = fellegiSunterParams.getUValue(i);
                numerator = adjustMinimumValue(numerator);
                denominator = adjustMinimumValue(denominator);
                weight += Math.log(numerator / denominator) / Math.log(2.0);
            } else {
                Double numerator = (1.0 - fellegiSunterParams.getMValue(i));
                Double denominator = (1.0 - fellegiSunterParams.getUValue(i));
                numerator = adjustMinimumValue(numerator);
                denominator = adjustMinimumValue(denominator);
                weight += Math.log(numerator / denominator) / Math.log(2.0);
            }
            fellegiSunterParams.setVectorWeight(vectorValue, weight);
            if (log.isTraceEnabled()) {
                log.trace("Set the weight of vector " + vectorValue + " to " + weight);
            }
        }
    }

    private static int getBitValueAtPosition(int value, int pos) {
        int mask = 1;
        for (int i = 1; i <= pos; i++) {
            mask = mask << 1;
        }
        int valueAtPosition = mask & value;
        if (valueAtPosition > 0) {
            valueAtPosition = 1;
        }
        return valueAtPosition;
    }

    private void calculateRecordPairWeight(RecordPair pair, FellegiSunterParameters fellegiSunterParams) {
        ComparisonVector vector = pair.getComparisonVector();
        int vectorValue = vector.getBinaryVectorValue();
        pair.setWeight(fellegiSunterParams.getVectorWeight(vectorValue));
    }

    private Double adjustMinimumValue(Double numerator) {
        if (numerator.doubleValue() < MIN_MARGINAL_VALUE.doubleValue()) {
            numerator = MIN_MARGINAL_VALUE;
        }
        return numerator;
    }

    public List<RecordPair> orderRecordPairsByWeight(List<RecordPair> pairs) {
        Collections.sort(pairs, new RecordPairComparator());
        return pairs;
    }

    public void calculateMarginalProbabilities(List<RecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
        for (RecordPair pair : pairs) {
            ComparisonVector vector = pair.getComparisonVector();
            Integer binaryVectorValue = vector.getBinaryVectorValue();
            if (vectorByValueMap.get(binaryVectorValue) == null) {
                log.trace("Added the vector " + vector.getBinaryVectorString() + " in map keyed by "
                        + binaryVectorValue);
                vectorByValueMap.put(binaryVectorValue, vector);
            }
            vector.calculateProbabilityGivenMatch(fellegiSunterParams.getMValues());
            vector.calculateProbabilityGivenNonmatch(fellegiSunterParams.getUValues());
        }
    }

    public void estimateMarginalProbabilities(FellegiSunterParameters fellegiSunterParams) {
        ExpectationMaximizationEstimator estimator = new ExpectationMaximizationEstimator();
        estimator.estimateMarginalProbabilities(fellegiSunterParams, getInitialMValue(), getInitialUValue(),
                getInitialPValue(), getMaxIterations());
    }

    private int getMaxIterations() {
        if (fellegiSunterParams == null || fellegiSunterParams.getMaxIterations() == 0) {
            return ProbabilisticMatchingConfigurationLoader.DEFAULT_MAX_EM_ITERATIONS;
        }
        return fellegiSunterParams.getMaxIterations();
    }

    private double getInitialPValue() {
        if (fellegiSunterParams == null || fellegiSunterParams.getPInitialValue() == 0) {
            return ProbabilisticMatchingConfigurationLoader.DEFAULT_P_INITIAL_VALUE;
        }
        return fellegiSunterParams.getPInitialValue();
    }

    private double getInitialUValue() {
        if (fellegiSunterParams == null || fellegiSunterParams.getUInitialValue() == 0) {
            return ProbabilisticMatchingConfigurationLoader.DEFAULT_U_INITIAL_VALUE;
        }
        return fellegiSunterParams.getUInitialValue();
    }

    private double getInitialMValue() {
        if (fellegiSunterParams == null || fellegiSunterParams.getMInitialValue() == 0) {
            return ProbabilisticMatchingConfigurationLoader.DEFAULT_M_INITIAL_VALUE;
        }
        return fellegiSunterParams.getMInitialValue();
    }

    public String getRecordPairMatchFields(RecordPair pair) {
        StringBuffer sb = new StringBuffer("{ ");
        for (int i = 0; i < matchFieldNames.length; i++) {
            String fieldName = matchFieldNames[i];
            Object value1 = pair.getLeftRecord().get(fieldName);
            Object value2 = pair.getRightRecord().get(fieldName);
            sb.append("[").append(value1).append(",").append(value2).append("]");
            if (i < matchFieldNames.length - 1) {
                sb.append(",");
            }
        }
        return sb.append(" }").toString();
    }

    public void calculateBoundsOnVectors(FellegiSunterParameters fellegiSunterParams) {
        List<ComparisonVector> list = new java.util.ArrayList<ComparisonVector>(vectorByValueMap.values().size());
        for (ComparisonVector vector : vectorByValueMap.values()) {
            double ratio = vector.getVectorProbGivenM() / vector.getVectorProbGivenU();
            vector.setOrderingRatio(ratio);
            double weight = Math.log(ratio) / Math.log(2.0);
            vector.setVectorWeight(weight);
            list.add(vector);
        }
        // We now order the vectors based on the decreasing ratio of agreement
        // to disagreement probability
        Collections.sort(list, new Comparator<ComparisonVector>()
        {
            @Override
            public int compare(ComparisonVector v1, ComparisonVector v2) {
                double rank = v2.getOrderingRatio() - v1.getOrderingRatio();
                int order;
                if (rank > 0) {
                    order = 1;
                } else if (rank < 0) {
                    order = -1;
                } else {
                    order = 0;
                }
                return order;
            }
        });

        double sum = 0;
        int index = 0;
        for (ComparisonVector vector : list) {
            log.trace("V[" + vector.getBinaryVectorString() + "], m(g)=" + vector.getVectorProbGivenM() + ", u(g)="
                    + vector.getVectorProbGivenU() + ", ratio=" + vector.getOrderingRatio() + ", weight="
                    + vector.getVectorWeight());
            sum += vector.getVectorProbGivenU();
            if (sum > fellegiSunterParams.getMu()) {
                break;
            }
            index++;
        }
        ComparisonVector theOne = list.get(index);
        fellegiSunterParams.setUpperBound(theOne.getVectorWeight());
        log.trace("Set the upper bound to: " + theOne.getVectorWeight());

        // We now order the vectors based on the increasing ratio of agreement
        // to disagreement probability
        Collections.sort(list, new Comparator<ComparisonVector>()
        {
            @Override
            public int compare(ComparisonVector v1, ComparisonVector v2) {
                double rank = v2.getOrderingRatio() - v1.getOrderingRatio();
                int order;
                if (rank > 0) {
                    order = -1;
                } else if (rank < 0) {
                    order = 1;
                } else {
                    order = 0;
                }
                return order;
            }
        });

        sum = 0;
        index = 0;
        for (ComparisonVector vector : list) {
            log.trace("V[" + vector.getBinaryVectorString() + "], m(g)=" + vector.getVectorProbGivenM() + ", u(g)="
                    + vector.getVectorProbGivenU() + ", ratio=" + vector.getOrderingRatio() + ", weight="
                    + vector.getVectorWeight());
            sum += vector.getVectorProbGivenM();
            if (sum > fellegiSunterParams.getLambda()) {
                break;
            }
            index++;
        }
        theOne = list.get(index);
        fellegiSunterParams.setLowerBound(theOne.getVectorWeight());
        log.trace("Set the lower bound to: " + theOne.getVectorWeight());
    }

    public void calculateBounds(List<RecordPair> pairs, FellegiSunterParameters fellegiSunterParams) {
        double sum = 0;
        int index = 0;
        for (RecordPair pair : pairs) {
            sum += pair.getComparisonVector().getVectorProbGivenM();
            if (log.isTraceEnabled()) {
                log.trace("Lambda sum for vector " + pair.getComparisonVector().getBinaryVectorString() + " is: " + sum);
            }
            index++;
            if (sum > fellegiSunterParams.getLambda()) {
                break;
            }
        }
        log.trace("Sum: " + sum + ", lambda: " + fellegiSunterParams.getLambda() + ", index: " + (index - 1)
                + ", value: " + pairs.get(index - 1).getWeight());
        fellegiSunterParams.setLowerBound(pairs.get(index - 1).getWeight());

        sum = 0;
        index = pairs.size() - 1;
        for (int i = index; i >= 0; i--) {
            sum += pairs.get(i).getComparisonVector().getVectorProbGivenU();
            if (log.isTraceEnabled()) {
                log.trace("Mu sum for vector " + pairs.get(i).getComparisonVector().getBinaryVectorString() + " is: "
                        + sum);
            }
            if (sum > fellegiSunterParams.getMu()) {
                break;
            }
        }
        log.trace("Sum: " + sum + ", mu: " + fellegiSunterParams.getMu() + ", index: " + index + ", value: "
                + pairs.get(index).getWeight());
        fellegiSunterParams.setUpperBound(pairs.get(index).getWeight());
    }

    @SuppressWarnings("unchecked")
    private void loadConfiguration() {
        Map<String, Object> configurationData = (Map<String, Object>) Context.getConfiguration()
                .lookupConfigurationEntry(ConfigurationRegistry.MATCH_CONFIGURATION);
        loadConfiguration(configurationData);
        loadLogFields();
    }

    @SuppressWarnings("unchecked")
    private void loadLogFields() {
        Map<String, Object> blockingConfData = (Map<String, Object>) Context.getConfiguration()
                .lookupConfigurationEntry(ConfigurationRegistry.BLOCKING_CONFIGURATION);
        Object obj = blockingConfData.get(BLOCKING_ROUNDS_REGISTRY_KEY);
        blockingFieldList = new java.util.HashSet<String>();
        if (obj != null) {
            List<BlockingRound> blockingRounds = (List<BlockingRound>) obj;
            for (BlockingRound round : blockingRounds) {
                for (BaseField field : round.getFields()) {
                    blockingFieldList.add(field.getFieldName());
                }
            }
            log.info("Loaded blocking fields for logging of " + blockingFieldList.toString());
        }

        List<MatchField> matchFields = getFields();
        logFieldList = new ArrayList<String>();
        for (MatchField field : matchFields) {
            logFieldList.add(field.getFieldName());
        }
        log.info("Loaded matching fields for logging of " + logFieldList.toString());
    }

    @SuppressWarnings("unchecked")
    private void loadConfiguration(Map<String, Object> configurationData) {
        fields = (List<MatchField>) configurationData.get(Constants.MATCHING_FIELDS_REGISTRY_KEY);
        falseNegativeProbability = (Float) configurationData
                .get(ProbabilisticMatchingConstants.FALSE_NEGATIVE_PROBABILITY_REGISTRY_KEY);
        falsePositiveProbability = (Float) configurationData
                .get(ProbabilisticMatchingConstants.FALSE_POSITIVE_PROBABILITY_REGISTRY_KEY);
        configurationDirectory = (String) configurationData
                .get(ProbabilisticMatchingConstants.CONFIGURATION_DIRECTORY_REGISTRY_KEY);
        if (fields == null || fields.size() == 0) {
            log.error("Probabilistic matching service has not been configured properly; no match fields have been defined.");
            throw new RuntimeException("Probabilistic maching service has not been configured properly.");
        }
        if (falseNegativeProbability == null) {
            log.warn("The false negative probability has not been configured; using default value of: "
                    + ProbabilisticMatchingConstants.DEFAULT_FALSE_NEGATIVE_PROBABILITY);
            this.falseNegativeProbability = ProbabilisticMatchingConstants.DEFAULT_FALSE_NEGATIVE_PROBABILITY;
        }
        if (falsePositiveProbability == null) {
            log.warn("The false positive probability has not been configured; using default value of: "
                    + ProbabilisticMatchingConstants.DEFAULT_FALSE_POSITIVE_PROBABILITY);
            this.falsePositiveProbability = ProbabilisticMatchingConstants.DEFAULT_FALSE_POSITIVE_PROBABILITY;
        }
        if (configurationDirectory == null) {
            log.warn("The configuration directory has not been configured; using default value of: "
                    + Context.getOpenEmpiHome());
            this.configurationDirectory = Context.getOpenEmpiHome();
        }
        matchFieldByName = new HashMap<String, MatchField>();

        matchFieldNames = new String[fields.size()];
        matchFields = new MatchField[fields.size()];
        int index = 0;
        for (MatchField field : fields) {
            matchFieldNames[index] = field.getFieldName();
            matchFields[index] = field;
            matchFieldByName.put(field.getFieldName(), field);
            index++;
        }
        log.debug("Matching service " + getClass().getName() + " will perform matching using " + toString());

        if (configurationData.containsKey(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_KEY)
                && (Boolean) configurationData
                        .get(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_KEY)) {
            logByVectors = true;
            vectorsToLog = (Set<Integer>) configurationData
                    .get(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_VECTORS_KEY);
            logByVectorsFraction = ((Double) configurationData
                    .get(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_FRACTION_KEY))
                    .doubleValue();
        } else {
            logByVectors = false;
        }

        if (configurationData.containsKey(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_KEY)
                && (Boolean) configurationData
                        .get(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_KEY)) {
            logByWeight = true;
            logWeightLowerBound = ((Double) configurationData
                    .get(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_LOWER_BOUND_KEY))
                    .doubleValue();
            logWeightUpperBound = ((Double) configurationData
                    .get(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_UPPER_BOUND_KEY))
                    .doubleValue();
            logByWeightFraction = ((Double) configurationData
                    .get(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_FRACTION_KEY))
                    .doubleValue();

        } else {
            logByWeight = false;
        }

        if (configurationData.containsKey(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_VECTORS_KEY)
                || configurationData
                        .containsKey(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_BY_WEIGHT_KEY)) {
            logDestination = ((String) configurationData
                    .get(ProbabilisticMatchingConstants.PROBABILISTIC_MATCHING_LOGGING_DESTINATION));
            if (logDestination == null) {
                logDestination = Constants.PROBABILISTIC_MATCHING_LOGGING_DESTINATION_TO_FILE;
            } else {
                logDestination = Constants.PROBABILISTIC_MATCHING_LOGGING_DESTINATION_TO_DB;
            }
        }
        fellegiSunterParams = FellegiSunterConfigurationManager.loadParameters(getConfigurationDirectory());
        ProbabilisticMatchingConfigurationLoader.loadDefaultValues(fellegiSunterParams);

        VectorConfigurationHelper.updateVectorConfiguration(configurationData, fellegiSunterParams);
        initialized = true;
    }

    private boolean isLoggingEnabled() {
        if (logByVectors || logByWeight)
            return true;
        return false;
    }

    private void logRecordPair(RecordPair pair) {
        double randomValue = Math.random();
        if (logByVectors == true && randomValue < logByVectorsFraction
                && vectorsToLog.contains(new Integer(pair.getComparisonVector().getBinaryVectorValue()))) {
            logRecordPairToDestination(pair);
        }
        if (logByWeight == true && randomValue < logByWeightFraction && pair.getWeight() >= logWeightLowerBound
                && pair.getWeight() <= logWeightUpperBound) {
            log.info("logRecordPair: " + logMatchStatus(pair) + "Weight -> " + pair.getWeight() + " -> "
                    + pair.getComparisonVector().getBinaryVectorString() + " -> " + getPairValues(pair));
            logRecordPairToDestination(pair);
        }
    }

    private void logRecordPairToDestination(RecordPair pair) {
        if (logDestination.equalsIgnoreCase(Constants.PROBABILISTIC_MATCHING_LOGGING_DESTINATION_TO_FILE)) {
            log.info("logRecordPair: " + logMatchStatus(pair) + "Weight -> " + pair.getWeight() + "Vector -> "
                    + pair.getComparisonVector().getBinaryVectorString() + " -> " + getPairValues(pair));
        } else {
            LoggedLink loggedLink = getLoggedLinkFromRecordPair(pair);
            personLinkDao.addLoggedLink(loggedLink);
        }
    }

    private LoggedLink getLoggedLinkFromRecordPair(RecordPair pair) {
        LoggedLink link = new LoggedLink();
        link.setDateCreated(new java.util.Date());
        link.setLeftRecordId(pair.getLeftRecord().getRecordId());
        link.setRightRecordId(pair.getRightRecord().getRecordId());
        link.setUserCreatedBy(Context.getUserContext().getUser());
        link.setVectorValue(pair.getComparisonVector().getBinaryVectorValue());
        link.setWeight(pair.getWeight());
        return link;
    }

    private String logMatchStatus(RecordPair pair) {
        if (pair.getMatchOutcome() == RecordPair.MATCH_OUTCOME_LINKED) {
            return "[L] ";
        } else if (pair.getMatchOutcome() == RecordPair.MATCH_OUTCOME_UNLINKED) {
            return "[U] ";
        } else if (pair.getMatchOutcome() == RecordPair.MATCH_OUTCOME_POSSIBLE) {
            return "[P] ";
        }
        return "[ ] ";
    }

    private Set<String> getBlockingLoggingFields() {
        return blockingFieldList;
    }

    private List<String> getLoggingFields() {
        return logFieldList;
    }

    private String getPairValues(RecordPair pair) {
        StringBuffer sb = new StringBuffer();
        logRecordBlockingFields(sb, pair.getLeftRecord(), getBlockingLoggingFields());
        sb.append("[");
        logRecordMatchFields(sb, pair.getLeftRecord(), getLoggingFields());
        sb.append("] versus [");
        // logRecordBlockingFields(sb, pair.getRightRecord(),
        // getBlockingLoggingFields());
        // sb.append("[");
        logRecordMatchFields(sb, pair.getRightRecord(), getLoggingFields());
        sb.append("]");
        return sb.toString();
    }

    private void logRecordBlockingFields(StringBuffer sb, Record rec, Set<String> fields) {
        if (fields.size() == 0) {
            return;
        }
        sb.append("{");
        int index = 0;
        for (String field : fields) {
            Object value = rec.get(field);
            if (value == null) {
                sb.append("null");
            } else {
                sb.append("'").append(value.toString()).append("'");
            }
            if (index < fields.size() - 1) {
                sb.append(",");
            }
            index++;
        }
        sb.append("}");
    }

    private void logRecordMatchFields(StringBuffer sb, Record rec, List<String> fields) {
        for (int i = 0; i < fields.size(); i++) {
            Object value = rec.get(fields.get(i));
            if (value == null) {
                sb.append("null");
            } else {
                sb.append("'").append(value.toString()).append("'");
            }
            if (i < fields.size() - 1) {
                sb.append(",");
            }
        }
    }

    public PersonLinkDao getPersonLinkDao() {
        return personLinkDao;
    }

    public void setPersonLinkDao(PersonLinkDao personLinkDao) {
        this.personLinkDao = personLinkDao;
    }

    public List<MatchField> getFields() {
        return fields;
    }

    public void setFields(List<MatchField> fields) {
        this.fields = fields;
    }

    public float getFalseNegativeProbability() {
        return falseNegativeProbability;
    }

    public void setFalseNegativeProbability(float falseNegativeProbability) {
        this.falseNegativeProbability = falseNegativeProbability;
    }

    public float getFalsePositiveProbability() {
        return falsePositiveProbability;
    }

    public void setFalsePositiveProbability(float falsePositiveProbability) {
        this.falsePositiveProbability = falsePositiveProbability;
    }

    public String getConfigurationDirectory() {
        return configurationDirectory;
    }

    public void setConfigurationDirectory(String configurationDirectory) {
        this.configurationDirectory = configurationDirectory;
    }

    public static void main(String[] args) {
        String baseDirectory = Context.getOpenEmpiHome();
        System.out.println(FellegiSunterConfigurationManager.loadParameters(baseDirectory + "/" + "conf"));
    }

    public void shutdown() {
        log.info("Shutting down the probabilistic matching service.");
    }

    public void initializeRepository() throws ApplicationException {
        FellegiSunterConfigurationManager.removeParametersFile(getConfigurationDirectory());
        startup();
    }

    @Override
    public void update(Observable o, Object eventData) {
        if (eventData == null || !(eventData instanceof Map) || !(o instanceof MatchingConfigurationUpdatedObservable)) {
            log.trace("Received notification for event that was not expected.");
            return;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) eventData;
        log.info("The configuration of the matching algorithm was changed by the user.");

        loadConfiguration(data);
        try {
            fellegiSunterParams = FellegiSunterConfigurationManager.loadParameters(getConfigurationDirectory());
            ProbabilisticMatchingConfigurationLoader.loadDefaultValues(fellegiSunterParams);
            initialized = true;
        } catch (Throwable t) {
            log.error("Failed while initializing the probabilistic matching service: " + t, t);
            initialized = false;
            try {
                linkRecords();
            } catch (Exception e) {
                log.error("Failed while initializing the probabilistic matching service: " + e, e);
                initialized = false;
            }
        }
    }
}
