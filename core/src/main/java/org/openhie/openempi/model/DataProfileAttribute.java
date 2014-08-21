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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "data_profile_attribute")
@GenericGenerator(name = "data_profile_attribute_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "data_profile_attribute_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class DataProfileAttribute extends BaseObject
{
	private static final long serialVersionUID = -5805814946755578692L;

	public static final int INTEGER_DATA_TYPE = 0;
	public static final int STRING_DATA_TYPE = 1;
	public static final int DATE_DATA_TYPE = 2;
	public static final int LONG_DATA_TYPE = 3;
	public static final int FLOAT_DATA_TYPE = 4;
	public static final int DOUBLE_DATA_TYPE = 5;
	
	private Integer attributeId;
	private String attributeName;
	private Integer datatypeId;
	private Double averageLength;
	private Integer minimumLength;
	private Integer maximumLength;
	private Double averageValue;
	private Double minimumValue;
	private Double maximumValue;
	private Double variance;
	private Double standardDeviation;
	private Integer rowCount;
	private Integer distinctCount;
	private Integer duplicateCount;
	private Integer uniqueCount;
	private Integer nullCount;
	private Double nullRate;
	private Double entropy;
	private Double maximumEntropy;
	private Double uValue;
	private Double averageTokenFrequency;
	private Integer blockingPairs;
	private Integer dataSourceId;

	private Set<DataProfileAttributeValue> attributeValues = new HashSet<DataProfileAttributeValue>();

	public DataProfileAttribute() {
	}
	
	public DataProfileAttribute(Integer attributeId) {
		this.attributeId = attributeId;
	}
	
	public DataProfileAttribute(String attributeName, int datatypeId) {
		this.attributeName = attributeName;
		this.datatypeId = dataSourceId;
	}
	
	public DataProfileAttribute(Integer attributeId, Integer datatypeId) {
		this.attributeId = attributeId;
		this.datatypeId = datatypeId;
	}
	
	@Id
	@GeneratedValue(generator="data_profile_attribute_gen")
	@Column(name = "attribute_id", unique = true, nullable = false)
	@XmlElement
	public Integer getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}

	@Column(name = "attribute_name")
	@XmlElement
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Column(name = "datatype_id")
	@XmlElement
	public Integer getDatatypeId() {
		return datatypeId;
	}

	public void setDatatypeId(Integer datatypeId) {
		this.datatypeId = datatypeId;
	}

	@Column(name = "average_length")
	@XmlElement
	public Double getAverageLength() {
		return averageLength;
	}

	public void setAverageLength(Double averageLength) {
		this.averageLength = averageLength;
	}

	@Column(name = "minimum_length")
	@XmlElement
	public Integer getMinimumLength() {
		return minimumLength;
	}

	public void setMinimumLength(Integer minimumLength) {
		this.minimumLength = minimumLength;
	}

	@Column(name = "maximum_length")
	@XmlElement
	public Integer getMaximumLength() {
		return maximumLength;
	}

	public void setMaximumLength(Integer maximumLength) {
		this.maximumLength = maximumLength;
	}

	@Column(name = "average_value")
	@XmlElement
	public Double getAverageValue() {
		return averageValue;
	}

	public void setAverageValue(Double averageValue) {
		this.averageValue = averageValue;
	}

	@Column(name = "minimum_value")
	@XmlElement
	public Double getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(Double minimumValue) {
		this.minimumValue = minimumValue;
	}

	@Column(name = "maximum_value")
	@XmlElement
	public Double getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(Double maximumValue) {
		this.maximumValue = maximumValue;
	}

	@Column(name = "variance")
	@XmlElement
	public Double getVariance() {
		return variance;
	}

	public void setVariance(Double variance) {
		this.variance = variance;
	}

	@Column(name = "standard_deviation")
	@XmlElement
	public Double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(Double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	@Column(name = "row_count")
	@XmlElement
	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	@Column(name = "distinct_count")
	@XmlElement
	public Integer getDistinctCount() {
		return distinctCount;
	}

	public void setDistinctCount(Integer distinctCount) {
		this.distinctCount = distinctCount;
	}

	@Column(name = "duplicate_count")
	@XmlElement
	public Integer getDuplicateCount() {
		return duplicateCount;
	}

	public void setDuplicateCount(Integer duplicateCount) {
		this.duplicateCount = duplicateCount;
	}

	@Column(name = "unique_count")
	@XmlElement
	public Integer getUniqueCount() {
		return uniqueCount;
	}

	public void setUniqueCount(Integer uniqueCount) {
		this.uniqueCount = uniqueCount;
	}

	@Column(name = "null_count")
	@XmlElement
	public Integer getNullCount() {
		return nullCount;
	}

	public void setNullCount(Integer nullCount) {
		this.nullCount = nullCount;
	}
	
	@Column(name = "null_rate")
	@XmlElement
	public Double getNullRate() {
		return nullRate;
	}

	public void setNullRate(Double nullRate) {
		this.nullRate = nullRate;
	}

	@Column(name = "entropy")
	@XmlElement
	public Double getEntropy() {
		return entropy;
	}

	public void setEntropy(Double entropy) {
		this.entropy = entropy;
	}

	@Column(name = "maximum_entropy")
	@XmlElement
	public Double getMaximumEntropy() {
		return maximumEntropy;
	}

	public void setMaximumEntropy(Double maximumEntropy) {
		this.maximumEntropy = maximumEntropy;
	}

	@Column(name = "u_value")
	@XmlElement
	public Double getuValue() {
		return uValue;
	}

	public void setuValue(Double uValue) {
		this.uValue = uValue;
	}

	@Column(name = "average_token_frequency")
	@XmlElement
	public Double getAverageTokenFrequency() {
		return averageTokenFrequency;
	}

	public void setAverageTokenFrequency(Double averageTokenFrequency) {
		this.averageTokenFrequency = averageTokenFrequency;
	}

	@Column(name = "blocking_pairs")
	@XmlElement
	public Integer getBlockingPairs() {
		return blockingPairs;
	}

	public void setBlockingPairs(Integer blockingPairs) {
		this.blockingPairs = blockingPairs;
	}

	@Column(name = "data_source_id")
	@XmlElement
	public Integer getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(Integer dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	
	@Transient
	public Set<DataProfileAttributeValue> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(Set<DataProfileAttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataProfileAttribute other = (DataProfileAttribute) obj;
		if (attributeId == null) {
			if (other.attributeId != null)
				return false;
		} else if (!attributeId.equals(other.attributeId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeId == null) ? 0 : attributeId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "DataProfileAttribute [attributeId=" + attributeId + "attributeName=" + attributeName + ", datatypeId=" + datatypeId + ", averageLength="
				+ averageLength + ", minimumLength=" + minimumLength + ", maximumLength=" + maximumLength
				+ ", averageValue=" + averageValue + ", minimumValue=" + minimumValue + ", maximumValue="
				+ maximumValue + ", variance=" + variance + ", standardDeviation=" + standardDeviation 
				+ ", rowCount=" + rowCount
				+ ", distinctCount=" + distinctCount + ", duplicateCount=" + duplicateCount + ", uniqueCount="
				+ uniqueCount + ", nullCount=" + nullCount + ", dataSourceId=" + dataSourceId + ", attributeValues="
				+ attributeValues + "]";
	}

}
