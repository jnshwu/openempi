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
package org.openhie.openempi.model;

import java.io.Serializable;

public class VectorConfiguration implements Serializable
{
	private static final long serialVersionUID = -3807840695640112044L;

	private int vectorValue;
	private double weight;
	private int algorithmClassification;
	private int manualClassification;
	
	public int getVectorValue() {
		return vectorValue;
	}
	
	public void setVectorValue(int vectorValue) {
		this.vectorValue = vectorValue;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public int getAlgorithmClassification() {
		return algorithmClassification;
	}
	
	public void setAlgorithmClassification(int algorithmClassification) {
		this.algorithmClassification = algorithmClassification;
	}
	
	public int getManualClassification() {
		return manualClassification;
	}
	
	public void setManualClassification(int manualClassification) {
		this.manualClassification = manualClassification;
	}

	@Override
	public String toString() {
		return "VectorConfiguration [vectorValue=" + vectorValue + ", weight=" + weight + ", algorithmClassification="
				+ algorithmClassification + ", manualClassification=" + manualClassification + "]";
	}
}
