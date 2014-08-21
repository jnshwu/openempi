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

import java.util.List;

import org.openhie.openempi.matching.MatchingLifecycleObserver;
import org.openhie.openempi.model.RecordPair;
import org.openhie.openempi.service.BaseServiceTestCase;

public class ProbabilisticMatchingServiceTest extends BaseServiceTestCase
{
	public void testScorePairs() {
		try {
			ProbabilisticMatchingService matchingService = (ProbabilisticMatchingService) getApplicationContext().getBean("probabilisticMatchingService");
			MatchingLifecycleObserver lifecycle = (MatchingLifecycleObserver) matchingService;
			matchingService.startup();
//			MatchConfiguration matchConfig = Context.getConfiguration().getMatchConfiguration();
//			List<RecordPair> pairs = matchingService.getRecordPairs();
//			matchingService.scoreRecordPairs(pairs);
//			FellegiSunterParameters params = new FellegiSunterParameters(matchConfig.getMatchFields().size());
//			params.setMu(matchConfig.getFalsePositiveProbability());
//			params.setLambda(matchConfig.getFalseNegativeProbability());
//			matchingService.calculateVectorFrequencies(pairs, params);
//			matchingService.estimateMarginalProbabilities(params);
//			System.out.println("Fellegi Sunter Parameters:\n" + params);
//			matchingService.calculateRecordPairWeights(pairs, params);
//			matchingService.orderRecordPairsByWeight(pairs);
//			matchingService.calculateMarginalProbabilities(pairs, params);
//			matchingService.calculateBounds(pairs, params);
			matchingService.linkRecords();
			FellegiSunterParameters params = matchingService.getFellegiSunterParameters();
			List<RecordPair> pairs = matchingService.getRecordPairs();
			int countMatched=0, countUnmatched=0, countUndecided=0;
			for (RecordPair pair : pairs) {
//				System.out.println("M[r] = " + pair.getComparisonVector().getVectorProbGivenM() +
//						", U[r] = " + pair.getComparisonVector().getVectorProbGivenU() +
//						", Vector = " + pair.getComparisonVector().getBinaryVectorString() +
//						", Weight = " + pair.getWeight());
				if (pair.getWeight() <= params.getLowerBound()) {
					countUnmatched++;
				} else if (pair.getWeight() > params.getLowerBound() && pair.getWeight() <  params.getUpperBound()) {
					countUndecided++;
//					System.out.println("Following pair couldn't be classified with certainty: " + matchingService.getRecordPairMatchFields(pair));
				} else {
					countMatched++;
				}
			}
			System.out.println("We automatically matched " + countMatched + ", unmatched " + countUnmatched + " and have undecided: " + countUndecided);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
