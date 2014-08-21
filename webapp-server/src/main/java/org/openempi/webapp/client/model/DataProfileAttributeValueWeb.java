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

public class DataProfileAttributeValueWeb extends BaseModelData
{
	public DataProfileAttributeValueWeb() {
	}
	
	public java.lang.Integer getAttributeValueId() {
		return get("attributeValueId");
	}
	
	public void setAttributeValueId(java.lang.Integer attributeValueId) {
		set("attributeValueId", attributeValueId);
	}
	
	public java.lang.Integer getAttributeId() {
		return get("attributeId");
	}
	
	public void setAttributeId(java.lang.Integer attributeId) {
		set("attributeId", attributeId);
	}
	
	public java.lang.String getAttributeValue() {
		return get("attributeValue");
	}

	public void setAttributeValue(java.lang.String attributeValue) {
		set("attributeValue", attributeValue);
	}
	
	public java.lang.Integer getFrequency() {
		return get("frequency");
	}
	
	public void setFrequency(java.lang.Integer frequency) {
		set("frequency", frequency);
	}
}
