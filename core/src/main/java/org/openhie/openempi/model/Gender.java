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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Gender entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "gender")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Gender extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 2361596403749617326L;

	private Integer genderCd;
	private String genderName;
	private String genderDescription;
	private String genderCode;
	
	/** default constructor */
	public Gender() {
	}

	/** minimal constructor */
	public Gender(Integer genderCd, String genderName) {
		this.genderCd = genderCd;
		this.genderName = genderName;
	}

	/** full constructor */
	public Gender(Integer genderCd, String genderName, String genderDescription) {
		this.genderCd = genderCd;
		this.genderName = genderName;
		this.genderDescription = genderDescription;
	}

	@Id
	@Column(name = "gender_cd", unique = true, nullable = false)
	@XmlElement
	public Integer getGenderCd() {
		return this.genderCd;
	}

	public void setGenderCd(Integer genderCd) {
		this.genderCd = genderCd;
	}

	@Column(name = "gender_name", nullable = false, length = 64)
	@XmlElement
	public String getGenderName() {
		return this.genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	@Column(name = "gender_description")
	@XmlElement
	public String getGenderDescription() {
		return this.genderDescription;
	}

	public void setGenderDescription(String genderDescription) {
		this.genderDescription = genderDescription;
	}

	@Column(name = "gender_code", nullable = false, length = 64)
	@XmlElement
	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Gender))
			return false;
		Gender castOther = (Gender) other;
		return new EqualsBuilder().append(genderCd, castOther.genderCd).append(genderName, castOther.genderName)
				.append(genderDescription, castOther.genderDescription).append(genderCode, castOther.genderCode)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(genderCd).append(genderName).append(genderDescription).append(genderCode)
				.toHashCode();
	}

	@Override
	public String toString() {
		return (genderName == null) ? "null" : genderName;
//		return new ToStringBuilder(this).append("genderCd", genderCd).append("genderName", genderName).append(
//				"genderDescription", genderDescription).append("genderCode", genderCode).toString();
	}

}