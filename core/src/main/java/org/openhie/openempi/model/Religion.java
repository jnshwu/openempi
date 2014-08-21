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
 * Religion entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "religion", schema = "public")
public class Religion extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -8484009651057411655L;

	private Integer religionCd;
	private String religionName;
	private String religionDescription;
	private String religionCode;
	
	/** default constructor */
	public Religion() {
	}

	/** minimal constructor */
	public Religion(Integer religionCd, String religionName) {
		this.religionCd = religionCd;
		this.religionName = religionName;
	}

	/** full constructor */
	public Religion(Integer religionCd, String religionName, String religionDescription) {
		this.religionCd = religionCd;
		this.religionName = religionName;
		this.religionDescription = religionDescription;
	}

	@Id
	@Column(name = "religion_cd", unique = true, nullable = false)
	public Integer getReligionCd() {
		return this.religionCd;
	}

	public void setReligionCd(Integer religionCd) {
		this.religionCd = religionCd;
	}

	@Column(name = "religion_name", nullable = false, length = 64)
	public String getReligionName() {
		return this.religionName;
	}

	public void setReligionName(String religionName) {
		this.religionName = religionName;
	}

	@Column(name = "religion_description")
	public String getReligionDescription() {
		return this.religionDescription;
	}

	public void setReligionDescription(String religionDescription) {
		this.religionDescription = religionDescription;
	}

	@Column(name = "religion_code", nullable = false, length = 64)
	public String getReligionCode() {
		return religionCode;
	}

	public void setReligionCode(String religionCode) {
		this.religionCode = religionCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Religion))
			return false;
		Religion castOther = (Religion) other;
		return new EqualsBuilder().append(religionCd, castOther.religionCd)
				.append(religionName, castOther.religionName)
				.append(religionDescription, castOther.religionDescription)
				.append(religionCode, castOther.religionCode).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(religionCd).append(religionName).append(religionDescription).append(
				religionCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("religionCd", religionCd).append("religionName", religionName).append(
				"religionDescription", religionDescription).append("religionCode", religionCode).toString();
	}

}