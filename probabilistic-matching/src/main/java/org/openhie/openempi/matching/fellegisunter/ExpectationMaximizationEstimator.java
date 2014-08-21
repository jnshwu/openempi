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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExpectationMaximizationEstimator
{
	protected Log log = LogFactory.getLog(ExpectationMaximizationEstimator.class);
	private static final Double MIN_VALUE = Double.MIN_VALUE;

	
	private double[] gOfM;
	private double[] gOfU;
	private double[] mOfI;
	private double[] uOfI;
	private double p;
	// Gamma[J][i]
	private int[][] gamma;
	
	private final static double CONVERGENCE_ERROR = 0.00001;
	
	public synchronized void estimateMarginalProbabilities(FellegiSunterParameters params, double mInitial, double uInitial, double pInitial, int maxIterations) {
		initializeAlgorithm(params, mInitial, uInitial, pInitial);
		
		double error = 1.0;
		int iteration = 1;
		do {
			// Expectation Step
			log.trace("Iteration: " + iteration);
			estimateGammaOfM(params.getVectorCount());
			estimateGammaOfU(params.getVectorCount());
			
			// Maximization Step
			estimateMOfI(params.getFieldCount(), params.getVectorCount(), params.getVectorFrequencies());
			estimateUOfI(params.getFieldCount(), params.getVectorCount(), params.getVectorFrequencies());
			double pPrevious = p;
			estimateProbability(params.getVectorCount(), params.getVectorFrequencies());
			error = Math.abs(pPrevious-p);
			log.trace("Error at iteration " + iteration + " is " + error);			
			iteration++;
		} while (error > CONVERGENCE_ERROR && iteration < maxIterations);
		
		params.setMValues(mOfI);
		params.setUValues(uOfI);
		params.setPValue(p);
	}

	private void estimateGammaOfM(int vectorCount) {
		StringBuffer sb = new StringBuffer("gOfM[");
		for (int j=0; j < vectorCount; j++) {
			double mProduct = p * calculateProduct(j, mOfI);
			double uProduct = (1.0-p) * calculateProduct(j, uOfI);
			gOfM[j] = mProduct / (mProduct + uProduct);
			if (log.isTraceEnabled()) {
				sb.append(j).append("=>").append(gOfM[j]).append(", ");
			}
		}
		if (log.isTraceEnabled()) {
			log.trace(sb.toString());
		}
	}
	
	private void estimateGammaOfU(int vectorCount) {
		StringBuffer sb = new StringBuffer("gOfU[");
		for (int j=0; j < vectorCount; j++) {
			double mProduct = p * calculateProduct(j, mOfI);
			double uProduct = (1.0-p) * calculateProduct(j, uOfI);
			gOfU[j] = uProduct / (mProduct + uProduct);
			if (log.isTraceEnabled()) {
				sb.append(j).append("=>").append(gOfU[j]).append(", ");
			}
		}		
		if (log.isTraceEnabled()) {
			log.trace(sb.toString());
		}
	}
	
	private void estimateMOfI(int fieldCount, int vectorCount, int[] fOfJ) {
		for (int i=0; i < fieldCount; i++) {
			double numeratorSumOfI = 0.0;
			double denominatorSumOfI = 0.0;
			for (int j=0; j < vectorCount; j++) {
				numeratorSumOfI += gamma[j][i] *  fOfJ[j] * gOfM[j];
				denominatorSumOfI += fOfJ[j] * gOfM[j];
			}
			mOfI[i] = numeratorSumOfI/denominatorSumOfI;
		}
	}
	
	private void estimateUOfI(int fieldCount, int vectorCount, int[] fOfJ) {
		for (int i=0; i < fieldCount; i++) {
			double numeratorSumOfI = 0.0;
			double denominatorSumOfI = 0.0;
			for (int j=0; j < vectorCount; j++) {
				numeratorSumOfI += gamma[j][i] *  fOfJ[j] * gOfU[j];
				denominatorSumOfI += fOfJ[j] * gOfU[j];
			}
			uOfI[i] = numeratorSumOfI/denominatorSumOfI;
		}
	}
	
	private void estimateProbability(int vectorCount, int[] fOfJ) {
		double numeratorSum = 0.0;
		double denominatorSum = 0.0;
		for (int j=0; j < vectorCount; j++) {
			numeratorSum += gOfM[j] * fOfJ[j];
			denominatorSum += fOfJ[j];
		}
		p = numeratorSum/denominatorSum;
	}

	private double calculateProduct(int cVectorIndex, double[] vector) {
		double product = 1.0;
		for (int i=0; i < vector.length; i++) {
			if (gamma[cVectorIndex][i] == 0) {
				product *= (1.0 - vector[i]);
			} else {
				product *= vector[i];
			}
		}
		if (product < MIN_VALUE) {
			product = MIN_VALUE;
		}
		return product;
	}
	
	private void initializeAlgorithm(FellegiSunterParameters params, double mInitial, double uInitial, double pInitial) {
		gOfM = new double[params.getVectorCount()];
		gOfU = new double[params.getVectorCount()];
		mOfI = new double[params.getFieldCount()];
		initializeMarginalsVector(mOfI, mInitial);
		uOfI = new double[params.getFieldCount()];
		initializeMarginalsVector(uOfI, uInitial);
		p = pInitial;
		gamma = new int[params.getVectorCount()][params.getFieldCount()];
		for (int j=0; j < params.getVectorCount(); j++) {
			for (int i=0; i < params.getFieldCount(); i++) {
				gamma[j][i] = getBitPosition(j, i);
			}
		}
	}

	private void initializeMarginalsVector(double[] vector, double initial) {
		for (int i=0; i < vector.length; i++) {
			vector[i] = initial;
		}
	}
	
	public int getBitPosition(int number, int position) {
		return (number >> position) & 1;
	}
	
	public void main(String[] args) {
		int[] numbers = { 7, 3, 8, 9, 12 };
		for (int j=0; j < numbers.length; j++) {
			for (int i = 0; i < 8; i++) {
				System.out.println("Position " + i + " of number " + numbers[j] + " is " + getBitPosition(numbers[j], i));
			}
		}
	}
}
