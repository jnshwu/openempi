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

import org.openhie.openempi.model.BaseObject;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * BaseField encapsulates a base field that contains information about the person object
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
public class BaseField extends BaseObject
{
	private static final long serialVersionUID = -4012644666481353904L;

	protected String fieldName;
	
	public BaseField() {
		
	}
	
	public BaseField(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof BaseField))
			return false;
		BaseField castOther = (BaseField) other;
		return new EqualsBuilder().append(fieldName, castOther.fieldName).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(fieldName).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("fieldName", fieldName).toString();
	}

}
