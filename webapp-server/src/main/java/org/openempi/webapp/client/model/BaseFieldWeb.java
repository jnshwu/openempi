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

import com.extjs.gxt.ui.client.data.BaseModelData;

public class BaseFieldWeb extends BaseModelData
{
	public static final String FIELD_NAME = "fieldName";
	public static final String FIELD_DESCRIPTION = "fieldDescription";
	public static final String FIELD_INDEX = "fieldIndex";
	public static final String BLOCKING_ROUND = "blockingRound";

	public BaseFieldWeb() {
	}

	public BaseFieldWeb(Integer blockingRound, Integer fieldIndex, String fieldName) {
		set(BLOCKING_ROUND, blockingRound);
		set(FIELD_INDEX, fieldIndex);
		set(FIELD_NAME, fieldName);
	}

	public BaseFieldWeb(Integer blockingRound, Integer fieldIndex, String fieldName, String fieldDescription) {
		set(BLOCKING_ROUND, blockingRound);
		set(FIELD_INDEX, fieldIndex);
		set(FIELD_NAME, fieldName);
		set(FIELD_DESCRIPTION, fieldDescription);
	}
	
	public Integer getBlockingRound() {
		return get(BLOCKING_ROUND);
	}

	public void setBlockingRound(Integer blockingRound) {
		set(BLOCKING_ROUND, blockingRound);
	}
	
	public Integer getFieldIndex() {
		return get(FIELD_INDEX);
	}

	public void setFieldIndex(Integer fieldIndex) {
		set(FIELD_INDEX, fieldIndex);
	}
	
	public String getFieldName() {
		return get(FIELD_NAME);
	}

	public void setFieldName(String fieldName) {
		set(FIELD_NAME, fieldName);
	}
	
	public String getFieldDescription() {
		return get(FIELD_DESCRIPTION);
	}

	public void setFieldDescription(String fieldDescription) {
		set(FIELD_DESCRIPTION, fieldDescription);
	}
}
