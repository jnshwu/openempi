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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class DataProfileAttributeWeb extends BaseModelData
{
	@SuppressWarnings("unused")
    private DataProfileAttributeValueWeb unusedDataProfileAttributeValueWeb;

	public DataProfileAttributeWeb() {
	}
	
	public java.lang.Integer getAttributeId() {
		return get("attributeId");
	}

	public void setAttributeId(java.lang.Integer attributeId) {
		set("attributeId", attributeId);
	}
	
	public java.lang.String getAttributeName() {
		return get("attributeName");
	}

	public void setAttributeName(java.lang.String attributeName) {
		set("attributeName", attributeName);
	}
	
	public java.lang.Integer getDatatypeId() {
		return get("datatypeId");
	}

	public void setDatatypeId(java.lang.Integer datatypeId) {
		set("datatypeId", datatypeId);
	}
	
	public java.lang.Double getAverageLength() {
		return get("averageLength");
	}
	
	public void setAverageLength(java.lang.Double averageLength) {
		set("averageLength", averageLength);
	}
	
	public java.lang.Integer getMinimumLength() {
		return get("minimumLength");
	}

	public void setMinimumLength(java.lang.Integer minimumLength) {
		set("minimumLength", minimumLength);
	}
	
	public java.lang.Integer getMaximumLength() {
		return get("maximumLength");
	}

	public void setMaximumLength(java.lang.Integer maximumLength) {
		set("maximumLength", maximumLength);
	}
	
	public java.lang.Double getAverageValue() {
		return get("averageValue");
	}
	
	public void setAverageValue(java.lang.Double averageValue) {
		set("averageValue", averageValue);
	}
	
	public java.lang.Double getMinimumValue() {
		return get("minimumValue");
	}
	
	public void setMinimumValue(java.lang.Double minimumValue) {
		set("minimumValue", minimumValue);
	}
	
	public java.lang.Double getMaximumValue() {
		return get("maximumValue");
	}
	
	public void setMaximumValue(java.lang.Double maximumValue) {
		set("maximumValue", maximumValue);
	}
	
	public java.lang.Double getVariance() {
		return get("variance");
	}
	
	public void setVariance(java.lang.Double variance) {
		set("variance", variance);
	}
	
	public java.lang.Double getStandardDeviation() {
		return get("standardDeviation");
	}
	
	public void setStandardDeviation(java.lang.Double standardDeviation) {
		set("standardDeviation", standardDeviation);
	}
	
	public java.lang.Integer getRowCount() {
		return get("rowCount");
	}

	public void setRowCount(java.lang.Integer rowCount) {
		set("rowCount", rowCount);
	}
	
	public java.lang.Integer getDistinctCount() {
		return get("distinctCount");
	}

	public void setDistinctCount(java.lang.Integer distinctCount) {
		set("distinctCount", distinctCount);
	}
	
	public java.lang.Integer getDuplicateCount() {
		return get("duplicateCount");
	}

	public void setDuplicateCount(java.lang.Integer duplicateCount) {
		set("duplicateCount", duplicateCount);
	}
	
	public java.lang.Integer getUniqueCount() {
		return get("uniqueCount");
	}

	public void setUniqueCount(java.lang.Integer uniqueCount) {
		set("uniqueCount", uniqueCount);
	}
	
	public java.lang.Integer getNullCount() {
		return get("nullCount");
	}

	public void setNullCount(java.lang.Integer nullCount) {
		set("nullCount", nullCount);
	}

	public java.lang.Double getNullRate() {
		return get("nullRate");
	}
	
	public void setNullRate(java.lang.Double nullRate) {
		set("nullRate", nullRate);
	}

	public java.lang.Double getEntropy() {
		return get("entropy");
	}
	
	public void setEntropy(java.lang.Double entropy) {
		set("entropy", entropy);
	}
	
	public java.lang.Double getMaximumEntropy() {
		return get("maximumEntropy");
	}
	
	public void setMaximumEntropy(java.lang.Double maximumEntropy) {
		set("maximumEntropy", maximumEntropy);
	}

	public java.lang.Double getUValue() {
		return get("uValue");
	}
	
	public void setUValue(java.lang.Double uValue) {
		set("uValue", uValue);
	}

	public java.lang.Double getAverageTokenFrequency() {
		return get("averageTokenFrequency");
	}
	
	public void setAverageTokenFrequency(java.lang.Double averageTokenFrequency) {
		set("averageTokenFrequency", averageTokenFrequency);
	}
	
	public java.lang.Integer getBlockingPairs() {
		return get("blockingPairs");
	}

	public void setBlockingPairs(java.lang.Integer blockingPairs) {
		set("blockingPairs", blockingPairs);
	}
	
	public java.lang.Integer getDataSourceId() {
		return get("dataSourceId");
	}

	public void setDataSourceId(java.lang.Integer dataSourceId) {
		set("dataSourceId", dataSourceId);
	}
	
	public java.util.Set<DataProfileAttributeValueWeb> getAttributeValues() {
		return get("attributeValues");
	}

	public void setAttributeValues(java.util.Set<DataProfileAttributeValueWeb> attributeValues) {
		set("attributeValues", attributeValues);
	}
}
