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
 * AddressType entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "address_type")
public class AddressType extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 7714813422902045544L;

	private Integer addressTypeCd;
	private String addressTypeName;
	private String addressTypeDescription;
	private String addressTypeCode;

	/** default constructor */
	public AddressType() {
	}

	/** minimal constructor */
	public AddressType(Integer addressTypeCd, String addressTypeName) {
		this.addressTypeCd = addressTypeCd;
		this.addressTypeName = addressTypeName;
	}

	/** full constructor */
	public AddressType(Integer addressTypeCd, String addressTypeName, String addressTypeDescription, String addressTypeCode) {
		this.addressTypeCd = addressTypeCd;
		this.addressTypeName = addressTypeName;
		this.addressTypeDescription = addressTypeDescription;
		this.addressTypeCode = addressTypeCode;
	}

	@Id
	@Column(name = "address_type_cd", unique = true, nullable = false)
	public Integer getAddressTypeCd() {
		return this.addressTypeCd;
	}

	public void setAddressTypeCd(Integer addressTypeCd) {
		this.addressTypeCd = addressTypeCd;
	}

	@Column(name = "address_type_name", nullable = false, length = 64)
	public String getAddressTypeName() {
		return this.addressTypeName;
	}

	public void setAddressTypeName(String addressTypeName) {
		this.addressTypeName = addressTypeName;
	}

	@Column(name = "address_type_description")
	public String getAddressTypeDescription() {
		return this.addressTypeDescription;
	}

	public void setAddressTypeDescription(String addressTypeDescription) {
		this.addressTypeDescription = addressTypeDescription;
	}

	@Column(name = "address_type_code", nullable = false, length = 64)
	public String getAddressTypeCode() {
		return addressTypeCode;
	}

	public void setAddressTypeCode(String addressTypeCode) {
		this.addressTypeCode = addressTypeCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof AddressType))
			return false;
		AddressType castOther = (AddressType) other;
		return new EqualsBuilder().append(addressTypeCd, castOther.addressTypeCd).append(addressTypeName,
				castOther.addressTypeName).append(addressTypeDescription, castOther.addressTypeDescription).append(
				addressTypeCode, castOther.addressTypeCode).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(addressTypeCd).append(addressTypeName).append(addressTypeDescription)
				.append(addressTypeCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("addressTypeCd", addressTypeCd).append("addressTypeName",
				addressTypeName).append("addressTypeDescription", addressTypeDescription).append("addressTypeCode",
				addressTypeCode).toString();
	}

}