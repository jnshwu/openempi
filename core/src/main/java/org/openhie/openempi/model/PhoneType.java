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
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * PhoneType entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "phone_type")
public class PhoneType extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 22709846722492358L;

	private Integer phoneTypeCd;
	private String phoneTypeName;
	private String phoneTypeDescription;
	private String phoneTypeCode;
	
	/** default constructor */
	public PhoneType() {
	}

	/** minimal constructor */
	public PhoneType(Integer phoneTypeCd, String phoneTypeName) {
		this.phoneTypeCd = phoneTypeCd;
		this.phoneTypeName = phoneTypeName;
	}

	/** full constructor */
	public PhoneType(Integer phoneTypeCd, String phoneTypeName, String phoneTypeDescription) {
		this.phoneTypeCd = phoneTypeCd;
		this.phoneTypeName = phoneTypeName;
		this.phoneTypeDescription = phoneTypeDescription;
	}

	@Id
	@Column(name = "phone_type_cd", unique = true, nullable = false)
	public Integer getPhoneTypeCd() {
		return this.phoneTypeCd;
	}

	public void setPhoneTypeCd(Integer phoneTypeCd) {
		this.phoneTypeCd = phoneTypeCd;
	}

	@Column(name = "phone_type_name", nullable = false, length = 64)
	public String getPhoneTypeName() {
		return this.phoneTypeName;
	}

	public void setPhoneTypeName(String phoneTypeName) {
		this.phoneTypeName = phoneTypeName;
	}

	@Column(name = "phone_type_description")
	public String getPhoneTypeDescription() {
		return this.phoneTypeDescription;
	}

	public void setPhoneTypeDescription(String phoneTypeDescription) {
		this.phoneTypeDescription = phoneTypeDescription;
	}

	@Column(name = "phone_type_code", nullable = false, length = 64)
	public String getPhoneTypeCode() {
		return phoneTypeCode;
	}

	public void setPhoneTypeCode(String phoneTypeCode) {
		this.phoneTypeCode = phoneTypeCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PhoneType))
			return false;
		PhoneType castOther = (PhoneType) other;
		return new EqualsBuilder().append(phoneTypeCd, castOther.phoneTypeCd)
				.append(phoneTypeName, castOther.phoneTypeName)
				.append(phoneTypeDescription, castOther.phoneTypeDescription)
				.append(phoneTypeCode, castOther.phoneTypeCode).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(phoneTypeCd).append(phoneTypeName).append(phoneTypeDescription).append(
				phoneTypeCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("phoneTypeCd", phoneTypeCd).append("phoneTypeName", phoneTypeName).append(
				"phoneTypeDescription", phoneTypeDescription).append("phoneTypeCode", phoneTypeCode).toString();
	}
}