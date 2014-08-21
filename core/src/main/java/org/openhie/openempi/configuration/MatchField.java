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
package org.openhie.openempi.configuration;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MatchField extends BaseField
{
	private static final long serialVersionUID = -4012644666481353904L;

	private float agreementProbability;
	private float disagreementProbability;
	private ComparatorFunction comparatorFunction;
	private float matchThreshold;
	
	public float getAgreementProbability() {
		return agreementProbability;
	}

	public void setAgreementProbability(float agreementProbability) {
		this.agreementProbability = agreementProbability;
	}

	public float getDisagreementProbability() {
		return disagreementProbability;
	}

	public void setDisagreementProbability(float disagreementProbability) {
		this.disagreementProbability = disagreementProbability;
	}

	public ComparatorFunction getComparatorFunction() {
		return comparatorFunction;
	}

	public void setComparatorFunction(ComparatorFunction comparatorFunction) {
		this.comparatorFunction = comparatorFunction;
	}

	public float getMatchThreshold() {
		return matchThreshold;
	}

	public void setMatchThreshold(float matchThreshold) {
		this.matchThreshold = matchThreshold;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof MatchField))
			return false;
		MatchField castOther = (MatchField) other;
		return new EqualsBuilder()
				.append(fieldName, castOther.fieldName)
				.append(agreementProbability, castOther.agreementProbability)
				.append(disagreementProbability, castOther.disagreementProbability)
				.append(comparatorFunction, castOther.comparatorFunction)
				.append(matchThreshold, castOther.matchThreshold)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(fieldName)
				.append(agreementProbability)
				.append(disagreementProbability)
				.append(comparatorFunction)
				.append(matchThreshold)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(super.toString())
				.append("agreementProbability", agreementProbability)
				.append("disagreementProbability", disagreementProbability)
				.append("comparatorFunctionName", comparatorFunction)
				.append("matchThreshold", matchThreshold)
				.toString();
	}

}
