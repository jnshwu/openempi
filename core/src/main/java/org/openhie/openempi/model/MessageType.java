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

/**
 * AuditEventType entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "message_type")
public class MessageType extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = 6170253797278224912L;

	public static final String UNKNOWN_MESSAGE_TYPE_CODE = "UNKNOWN";
	
	private Integer messageTypeCd;
	private String messageTypeName;
	private String messageTypeDescription;
	private String messageTypeCode;
	
	/** default constructor */
	public MessageType() {
	}

	@Id
	@Column(name = "message_type_cd", unique = true, nullable = false)
	public Integer getMessageTypeCd() {
		return messageTypeCd;
	}

	public void setMessageTypeCd(Integer messageTypeCd) {
		this.messageTypeCd = messageTypeCd;
	}

	@Column(name = "message_type_name", nullable = false, length = 64)
	public String getMessageTypeName() {
		return messageTypeName;
	}

	public void setMessageTypeName(String messageTypeName) {
		this.messageTypeName = messageTypeName;
	}

	@Column(name = "message_type_description")
	public String getMessageTypeDescription() {
		return messageTypeDescription;
	}

	public void setMessageTypeDescription(String messageTypeDescription) {
		this.messageTypeDescription = messageTypeDescription;
	}

	@Column(name = "message_type_code", nullable = false, length = 64)
	public String getMessageTypeCode() {
		return messageTypeCode;
	}

	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageTypeCd == null) ? 0 : messageTypeCd.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageType other = (MessageType) obj;
		if (messageTypeCd == null) {
			if (other.messageTypeCd != null)
				return false;
		} else if (!messageTypeCd.equals(other.messageTypeCd))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessageType [messageTypeCd=" + messageTypeCd + ", messageTypeName=" + messageTypeName
				+ ", messageTypeDescription=" + messageTypeDescription + ", messageTypeCode=" + messageTypeCode + "]";
	}
}