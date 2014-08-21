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
package org.openempi.webapp.client.model;

import java.io.Serializable;

public class MatchFieldWeb extends BaseFieldWeb implements Serializable
{
	public static final String AGREEMENT_PROBABILITY = "agreementProbability";
	public static final String DISAGREEMENT_PROBABILITY = "disagreementProbability";
	public static final String COMPARATOR_FUNCTION_NAME = "comparatorFunctionName";
	public static final String COMPARATOR_FUNCTION_NAME_DESCRIPTION = "comparatorFunctionNameDescription";
	public static final String MATCH_THRESHOLD = "matchThreshold";
	public static final String M_VALUE = "mValue";
	public static final String U_VALUE = "uValue";
	
	public MatchFieldWeb() {
	}

	public java.lang.Float getAgreementProbability() {
		return get(AGREEMENT_PROBABILITY);
	}

	public void setAgreementProbability(java.lang.Float agreementProbability) {
		set(AGREEMENT_PROBABILITY, agreementProbability);
	}

	public java.lang.Float getDisagreementProbability() {
		return get(DISAGREEMENT_PROBABILITY);
	}

	public void setDisagreementProbability(java.lang.Float disagreementProbability) {
		set(DISAGREEMENT_PROBABILITY, disagreementProbability);
	}

	public java.lang.String getComparatorFunctionName() {
		return get(COMPARATOR_FUNCTION_NAME);
	}

	public void setComparatorFunctionName(java.lang.String comparatorFunctionName) {
		set(COMPARATOR_FUNCTION_NAME, comparatorFunctionName);
	}

	public java.lang.String getComparatorFunctionNameDescription() {
		return get(COMPARATOR_FUNCTION_NAME_DESCRIPTION);
	}

	public void setComparatorFunctionNameDescription(java.lang.String comparatorFunctionNameDescription) {
		set(COMPARATOR_FUNCTION_NAME_DESCRIPTION, comparatorFunctionNameDescription);
	}
	
	public java.lang.Float getMatchThreshold() {
		return get(MATCH_THRESHOLD);
	}

	public void setMatchThreshold(java.lang.Float matchThreshold) {
		set(MATCH_THRESHOLD, matchThreshold);
	}

	public java.lang.Double getMValue() {
		return get(M_VALUE);
	}

	public void setMValue(java.lang.Double mValue) {
		set(M_VALUE, mValue);
	}

	public java.lang.Double getUValue() {
		return get(U_VALUE);
	}

	public void setUValue(java.lang.Double uValue) {
		set(U_VALUE, uValue);
	}
}
