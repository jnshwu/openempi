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
 * EthnicGroup entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "ethnic_group")
public class EthnicGroup extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 8162563656465936003L;

	private Integer ethnicGroupCd;
	private String ethnicGroupName;
	private String ethnicGroupDescription;
	private String ethnicGroupCode;

	/** default constructor */
	public EthnicGroup() {
	}

	/** minimal constructor */
	public EthnicGroup(Integer ethnicGroupCd, String ethnicGroupName) {
		this.ethnicGroupCd = ethnicGroupCd;
		this.ethnicGroupName = ethnicGroupName;
	}

	/** full constructor */
	public EthnicGroup(Integer ethnicGroupCd, String ethnicGroupName, String ethnicGroupDescription) {
		this.ethnicGroupCd = ethnicGroupCd;
		this.ethnicGroupName = ethnicGroupName;
		this.ethnicGroupDescription = ethnicGroupDescription;
	}

	@Id
	@Column(name = "ethnic_group_cd", unique = true, nullable = false)
	public Integer getEthnicGroupCd() {
		return this.ethnicGroupCd;
	}

	public void setEthnicGroupCd(Integer ethnicGroupCd) {
		this.ethnicGroupCd = ethnicGroupCd;
	}

	@Column(name = "ethnic_group_name", nullable = false, length = 64)
	public String getEthnicGroupName() {
		return this.ethnicGroupName;
	}

	public void setEthnicGroupName(String ethnicGroupName) {
		this.ethnicGroupName = ethnicGroupName;
	}

	@Column(name = "ethnic_group_description")
	public String getEthnicGroupDescription() {
		return this.ethnicGroupDescription;
	}

	public void setEthnicGroupDescription(String ethnicGroupDescription) {
		this.ethnicGroupDescription = ethnicGroupDescription;
	}

	@Column(name = "ethnic_group_code", nullable = false, length = 64)
	public String getEthnicGroupCode() {
		return ethnicGroupCode;
	}

	public void setEthnicGroupCode(String ethnicGroupCode) {
		this.ethnicGroupCode = ethnicGroupCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof EthnicGroup))
			return false;
		EthnicGroup castOther = (EthnicGroup) other;
		return new EqualsBuilder().append(ethnicGroupCd, castOther.ethnicGroupCd).append(ethnicGroupName,
				castOther.ethnicGroupName).append(ethnicGroupDescription, castOther.ethnicGroupDescription).append(
				ethnicGroupCode, castOther.ethnicGroupCode).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(ethnicGroupCd).append(ethnicGroupName).append(ethnicGroupDescription)
				.append(ethnicGroupCode).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("ethnicGroupCd", ethnicGroupCd).append("ethnicGroupName",
				ethnicGroupName).append("ethnicGroupDescription", ethnicGroupDescription).append("ethnicGroupCode",
				ethnicGroupCode).toString();
	}

}