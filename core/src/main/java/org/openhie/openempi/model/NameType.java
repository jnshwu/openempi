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
 * NameType entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "name_type")
public class NameType extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 6170253797278224912L;

	private Integer nameTypeCd;
	private String nameTypeName;
	private String nameTypeDescription;
	private String nameTypeCode;
	
	/** default constructor */
	public NameType() {
	}

	/** minimal constructor */
	public NameType(Integer nameTypeCd, String nameTypeName) {
		this.nameTypeCd = nameTypeCd;
		this.nameTypeName = nameTypeName;
	}

	/** full constructor */
	public NameType(Integer nameTypeCd, String nameTypeName, String nameTypeDescription) {
		this.nameTypeCd = nameTypeCd;
		this.nameTypeName = nameTypeName;
		this.nameTypeDescription = nameTypeDescription;
	}

	@Id
	@Column(name = "name_type_cd", unique = true, nullable = false)
	public Integer getNameTypeCd() {
		return this.nameTypeCd;
	}

	public void setNameTypeCd(Integer nameTypeCd) {
		this.nameTypeCd = nameTypeCd;
	}

	@Column(name = "name_type_name", nullable = false, length = 64)
	public String getNameTypeName() {
		return this.nameTypeName;
	}

	public void setNameTypeName(String nameTypeName) {
		this.nameTypeName = nameTypeName;
	}

	@Column(name = "name_type_description")
	public String getNameTypeDescription() {
		return this.nameTypeDescription;
	}

	public void setNameTypeDescription(String nameTypeDescription) {
		this.nameTypeDescription = nameTypeDescription;
	}

	@Column(name = "name_type_code", nullable = false, length = 64)
	public String getNameTypeCode() {
		return nameTypeCode;
	}

	public void setNameTypeCode(String nameTypeCode) {
		this.nameTypeCode = nameTypeCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof NameType))
			return false;
		NameType castOther = (NameType) other;
		return new EqualsBuilder().append(nameTypeCd, castOther.nameTypeCd)
				.append(nameTypeName, castOther.nameTypeName)
				.append(nameTypeDescription, castOther.nameTypeDescription)
				.append(nameTypeCode, castOther.nameTypeCode).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(nameTypeCd).append(nameTypeName).append(nameTypeDescription).append(
				nameTypeCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("nameTypeCd", nameTypeCd).append("nameTypeName", nameTypeName).append(
				"nameTypeDescription", nameTypeDescription).append("nameTypeCode", nameTypeCode).toString();
	}
}