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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "data_profile_attribute_value")
@GenericGenerator(name = "data_profile_attribute_value_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "data_profile_attribute_value_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
public class DataProfileAttributeValue extends BaseObject
{
	private static final long serialVersionUID = -5805814946755578692L;
	
	private Integer attributeValueId;
	private Integer attributeId;
	private String attributeValue;
	private Integer frequency;
	
	public DataProfileAttributeValue() {
	}
	
	public DataProfileAttributeValue(Integer attributeValueId) {
		this.attributeValueId = attributeValueId;
	}
	
	public DataProfileAttributeValue(Integer attributeValueId, Integer attributeId, String attributeValue,
			Integer frequency) {
		this.attributeValueId = attributeValueId;
		this.attributeId = attributeId;
		this.attributeValue = attributeValue;
		this.frequency = frequency;
	}

	@Id
	@GeneratedValue(generator="data_profile_attribute_value_gen")
	@Column(name = "attribute_value_id", unique = true, nullable = false)
	public Integer getAttributeValueId() {
		return attributeValueId;
	}

	public void setAttributeValueId(Integer attributeValueId) {
		this.attributeValueId = attributeValueId;
	}
	
	@Column(name = "attribute_id")
	public Integer getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}
	
	@Column(name = "attribute_value")
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Column(name = "frequency")
	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataProfileAttributeValue other = (DataProfileAttributeValue) obj;
		if (attributeValueId == null) {
			if (other.attributeValueId != null)
				return false;
		} else if (!attributeValueId.equals(other.attributeValueId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeValueId == null) ? 0 : attributeValueId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "DataProfileAttributeValue [attributeValueId=" + attributeValueId + ", attributeId=" + attributeId
				+ ", attributeValue=" + attributeValue + ", frequency=" + frequency + "]";
	}
}
