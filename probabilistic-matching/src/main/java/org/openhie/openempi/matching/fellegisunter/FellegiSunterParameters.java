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

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.logging.Log;

public class FellegiSunterParameters implements Serializable
{
	private static final long serialVersionUID = 7534673028743277151L;
	
	int fieldCount;
	private String[] matchingFieldNames;
	private int[] vectorFrequencies;
	private int vectorCount;
	private double[] vectorWeights;
	private double[] mValues;
	private double[] uValues;
	private double pValue;
	private double lowerBound;
	private double upperBound;
	private double lambda; 					// Type I (false negative) error
	private double mu; 						// Type II (false positive) error
	private double mInitialValue;
	private double uInitialValue;
	private double pInitialValue;
	private int maxIterations;
	
	public FellegiSunterParameters(int vectorCount, int[] vectorFrequencies, double[] mValues, double[] uValues, double lambda, double mu) {
		this.vectorCount = vectorCount;
		this.vectorFrequencies = vectorFrequencies;
		this.mValues = mValues;
		this.uValues = uValues;
		this.lambda = lambda;
		this.mu = mu;
	}
	
	public FellegiSunterParameters() {
	}
	
	public FellegiSunterParameters(int fieldCount) {
		this.fieldCount = fieldCount;
		mValues = new double[fieldCount];
		uValues = new double[fieldCount];
		vectorCount = (int)Math.pow(2,fieldCount);
		vectorFrequencies = new int[vectorCount];
		vectorWeights = new double[vectorCount];
	}
	
	public String[] getMatchingFieldNames() {
		return matchingFieldNames;
	}

	public void setMatchingFieldNames(String[] matchingFieldNames) {
		this.matchingFieldNames = matchingFieldNames;
	}
	
	public int getFieldCount() {
		return fieldCount;
	}
	
	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}
	
	public double getMValue(int index) {
		checkIndexBounds(index, fieldCount);
		return mValues[index];
	}
	
	public void setMValue(int index, double values) {
		checkIndexBounds(index, fieldCount);
		mValues[index] = values;
	}

	public double getUValue(int index) {
		checkIndexBounds(index, fieldCount);
		return uValues[index];
	}
	
	public void setUValue(int index, double values) {
		checkIndexBounds(index, fieldCount);
		uValues[index] = values;
	}

	public void incrementVectorFrequency(int index) {
		checkIndexBounds(index, vectorCount);
		vectorFrequencies[index]++;
	}
	
	public int getVectorFrequency(int index) {
		checkIndexBounds(index, vectorCount);
		return vectorFrequencies[index];
	}
	
	public void setVectorFrequency(int index, int frequency) {
		checkIndexBounds(index, vectorCount);
		vectorFrequencies[index] = frequency;
	}

	private void checkIndexBounds(int index, int bound) {
		if (index < 0 || index > bound-1) {
			throw new IndexOutOfBoundsException("Index value is out of valid range between 0 and " + bound);
		}
	}
	
	public double[] getMValues() {
		return mValues;
	}
	
	public void setMValues(double[] values) {
		mValues = values;
	}
	
	public double[] getUValues() {
		return uValues;
	}
	
	public void setUValues(double[] values) {
		uValues = values;
	}
	
	public double getPValue() {
		return pValue;
	}
	
	public void setPValue(double pValue) {
		this.pValue = pValue;
	}
	
	public double getLowerBound() {
		return lowerBound;
	}
	
	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}
	
	public double getUpperBound() {
		return upperBound;
	}
	
	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	public int[] getVectorFrequencies() {
		return vectorFrequencies;
	}

	public void setVectorFrequencies(int[] vectorFrequencies) {
		this.vectorFrequencies = vectorFrequencies;
	}

	public int getVectorCount() {
		return vectorCount;
	}

	public void setVectorCount(int vectorCount) {
		this.vectorCount = vectorCount;
	}

	public double[] getVectorWeights() {
		return vectorWeights;
	}

	public void setVectorWeights(double[] vectorWeights) {
		this.vectorWeights = vectorWeights;
	}

	public double getVectorWeight(int vectorValue) {
		return vectorWeights[vectorValue];
	}
	
	public void setVectorWeight(int vectorValue, double weight) {
		vectorWeights[vectorValue] = weight;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getMu() {
		return mu;
	}

	public void setMu(double mu) {
		this.mu = mu;
	}

	public double getMInitialValue() {
		return mInitialValue;
	}

	public void setMInitialValue(double mInitialValue) {
		this.mInitialValue = mInitialValue;
	}

	public double getUInitialValue() {
		return uInitialValue;
	}

	public void setUInitialValue(double uInitialValue) {
		this.uInitialValue = uInitialValue;
	}

	public double getPInitialValue() {
		return pInitialValue;
	}

	public void setPInitialValue(double pInitialValue) {
		this.pInitialValue = pInitialValue;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public void logVectorFrequencies(Log log) {
		log.trace("Vector Frequencies:");
		for (int i=0; i < vectorCount; i++) {
			log.trace(i + "=>" + vectorFrequencies[i]);
		}
	}

	@Override
	public String toString() {
		return "FellegiSunterParameters [fieldCount=" + fieldCount
				+ ", matchingFieldNames=" + Arrays.toString(matchingFieldNames)
				+ ", vectorFrequencies=" + Arrays.toString(vectorFrequencies)
				+ ", vectorCount=" + vectorCount + ", vectorWeights="
				+ Arrays.toString(vectorWeights) + ", mValues="
				+ Arrays.toString(mValues) + ", uValues="
				+ Arrays.toString(uValues) + ", pValue=" + pValue
				+ ", lowerBound=" + lowerBound + ", upperBound=" + upperBound
				+ ", lambda=" + lambda + ", mu=" + mu + ", mInitialValue="
				+ mInitialValue + ", uInitialValue=" + uInitialValue
				+ ", pInitialValue=" + pInitialValue + ", maxIterations="
				+ maxIterations + "]";
	}
}
