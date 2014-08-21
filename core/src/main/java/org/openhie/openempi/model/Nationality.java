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
 * Nationality entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "nationality")
public class Nationality extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 8807251669987078189L;

	private Integer nationalityCd;
	private String nationalityName;
	private String nationalityDescription;
	private String nationalityCode;
	
	/** default constructor */
	public Nationality() {
	}

	/** minimal constructor */
	public Nationality(Integer nationalityCd, String nationalityName) {
		this.nationalityCd = nationalityCd;
		this.nationalityName = nationalityName;
	}

	/** full constructor */
	public Nationality(Integer nationalityCd, String nationalityName, String nationalityDescriiption) {
		this.nationalityCd = nationalityCd;
		this.nationalityName = nationalityName;
		this.nationalityDescription = nationalityDescriiption;
	}

	@Id
	@Column(name = "nationality_cd", unique = true, nullable = false)
	public Integer getNationalityCd() {
		return this.nationalityCd;
	}

	public void setNationalityCd(Integer nationalityCd) {
		this.nationalityCd = nationalityCd;
	}

	@Column(name = "nationality_name", nullable = false, length = 64)
	public String getNationalityName() {
		return this.nationalityName;
	}

	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}

	@Column(name = "nationality_description")
	public String getNationalityDescription() {
		return this.nationalityDescription;
	}

	public void setNationalityDescription(String nationalityDescription) {
		this.nationalityDescription = nationalityDescription;
	}

	@Column(name = "nationality_code", nullable = false, length = 64)
	public String getNationalityCode() {
		return nationalityCode;
	}

	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Nationality))
			return false;
		Nationality castOther = (Nationality) other;
		return new EqualsBuilder().append(nationalityCd, castOther.nationalityCd).append(nationalityName,
				castOther.nationalityName).append(nationalityDescription, castOther.nationalityDescription).append(
				nationalityCode, castOther.nationalityCode).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(nationalityCd).append(nationalityName).append(nationalityDescription)
				.append(nationalityCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("nationalityCd", nationalityCd).append("nationalityName",
				nationalityName).append("nationalityDescription", nationalityDescription).append("nationalityCode",
				nationalityCode).toString();
	}

}