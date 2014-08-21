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
package org.openhie.openempi.singlebestrecord;

import java.io.Serializable;

public class SingleBestRecordRule implements Serializable
{
	private static final long serialVersionUID = -1183069022782754301L;

	private String fieldName;
	private RuleCondition condition;
	
	public SingleBestRecordRule(String fieldName, RuleCondition condition) {
		this.fieldName = fieldName;
		this.condition = condition;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public RuleCondition getCondition() {
		return condition;
	}
	public void setCondition(RuleCondition condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "SingleBestRecordRule [fieldName=" + fieldName + ", condition="
				+ condition + "]";
	}
}
