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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ExtendedCriterion extends Criterion
{
	private static final long serialVersionUID = 7608441012044500866L;

	private String alias;
	private String associationPath;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAssociationPath() {
		return associationPath;
	}

	public void setAssociationPath(String associationPath) {
		this.associationPath = associationPath;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof ExtendedCriterion))
			return false;
		ExtendedCriterion castOther = (ExtendedCriterion) other;
		return new EqualsBuilder()
			.append(getName(), castOther.getName())
			.append(getOperation(), castOther.getOperation())
			.append(getValue(), castOther.getValue())
			.append(getAlias(), castOther.getAlias())
			.append(getAssociationPath(), castOther.getAssociationPath())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getName())
			.append(getOperation())
			.append(getValue())
			.append(getAlias())
			.append(getAssociationPath())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("name", getName())
			.append("operation", getOperation())
			.append("value", getValue())
			.append("alias", getAlias())
			.append("associationPath", getAssociationPath())
				.toString();
	}	
}
