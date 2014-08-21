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
 * Language entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "language")
public class Language extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -5571536094478016725L;

	private Integer languageCd;
	private String languageName;
	private String languageDescription;
	private String languageCode;
	
	/** default constructor */
	public Language() {
	}

	/** minimal constructor */
	public Language(Integer languageCd, String languageName) {
		this.languageCd = languageCd;
		this.languageName = languageName;
	}

	/** full constructor */
	public Language(Integer languageCd, String languageName, String languageDescription) {
		this.languageCd = languageCd;
		this.languageName = languageName;
		this.languageDescription = languageDescription;
	}

	@Id
	@Column(name = "language_cd", unique = true, nullable = false)
	public Integer getLanguageCd() {
		return this.languageCd;
	}

	public void setLanguageCd(Integer languageCd) {
		this.languageCd = languageCd;
	}

	@Column(name = "language_name", nullable = false, length = 64)
	public String getLanguageName() {
		return this.languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	@Column(name = "language_description")
	public String getLanguageDescription() {
		return this.languageDescription;
	}

	public void setLanguageDescription(String languageDescription) {
		this.languageDescription = languageDescription;
	}

	@Column(name = "language_code", nullable = false, length = 64)
	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Language))
			return false;
		Language castOther = (Language) other;
		return new EqualsBuilder().append(languageCd, castOther.languageCd)
				.append(languageName, castOther.languageName)
				.append(languageDescription, castOther.languageDescription)
				.append(languageCode, castOther.languageCode).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(languageCd).append(languageName).append(languageDescription).append(
				languageCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("languageCd", languageCd).append("languageName", languageName).append(
				"languageDescription", languageDescription).append("languageCode", languageCode).toString();
	}
}