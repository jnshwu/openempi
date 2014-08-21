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
 * Race entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "race", schema = "public")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Race extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -6599300349364875923L;

	private Integer raceCd;
	private String raceName;
	private String raceDescription;
	private String raceCode;
	
	/** default constructor */
	public Race() {
	}

	/** minimal constructor */
	public Race(Integer raceCd, String raceName) {
		this.raceCd = raceCd;
		this.raceName = raceName;
	}

	/** full constructor */
	public Race(Integer raceCd, String raceName, String raceDescription) {
		this.raceCd = raceCd;
		this.raceName = raceName;
		this.raceDescription = raceDescription;
	}

	@Id
	@Column(name = "race_cd", unique = true, nullable = false)
	@XmlElement
	public Integer getRaceCd() {
		return this.raceCd;
	}

	public void setRaceCd(Integer raceCd) {
		this.raceCd = raceCd;
	}

	@Column(name = "race_name", nullable = false, length = 64)
	@XmlElement
	public String getRaceName() {
		return this.raceName;
	}

	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}

	@Column(name = "race_description")
	@XmlElement
	public String getRaceDescription() {
		return this.raceDescription;
	}

	public void setRaceDescription(String raceDescription) {
		this.raceDescription = raceDescription;
	}

	@Column(name = "race_code", nullable = false, length = 64)
	@XmlElement
	public String getRaceCode() {
		return raceCode;
	}

	public void setRaceCode(String raceCode) {
		this.raceCode = raceCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Race))
			return false;
		Race castOther = (Race) other;
		return new EqualsBuilder().append(raceCd, castOther.raceCd).append(raceName, castOther.raceName).append(
				raceDescription, castOther.raceDescription).append(raceCode, castOther.raceCode).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(raceCd).append(raceName).append(raceDescription).append(raceCode)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("raceCd", raceCd).append("raceName", raceName).append(
				"raceDescription", raceDescription).append("raceCode", raceCode).toString();
	}

}