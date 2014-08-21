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

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.util.ConvertingWrapDynaBean;
import org.openhie.openempi.util.DateUtil;

public class Record implements Serializable
{
	private static final long serialVersionUID = -7069549464547186749L;
	private Long recordId;
	private Object object;
	private ConvertingWrapDynaBean dynaBean;
	private RecordTypeDef recordTypeDefinition;
	
	public Record() {
	}
	
	public Record(Object object) {
		this.object = object;
		this.dynaBean = new ConvertingWrapDynaBean(object); 
	}
	
	public synchronized RecordTypeDef getRecordDef() {
		if (recordTypeDefinition == null) {
			recordTypeDefinition = new RecordTypeDef(object);
		}
		return recordTypeDefinition;
	}

	public String getAsString(String fieldName) {
		Object obj = dynaBean.get(fieldName);
		if (obj == null) {
			return null;
		}
		if (obj instanceof java.util.Date) {
			return DateUtil.getDate((java.util.Date) obj);
		}
		return obj.toString();
	}

	public Object get(String fieldName) {
		return dynaBean.get(fieldName);
	}
	
	public void set(String fieldName, Object value) {
		dynaBean.set(fieldName, value);
	}
	
	public Object getObject() {
		return dynaBean.getInstance();
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	
	public Set<String> getPropertyNames() {
		return dynaBean.getPropertyNames();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Record))
			return false;
		Record castOther = (Record) other;
		return new EqualsBuilder().append(recordId, castOther.recordId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(recordId).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("recordId", recordId).append("dynaBean", dynaBean).append(
				"recordTypeDefinition", recordTypeDefinition).toString();
	}	
}
