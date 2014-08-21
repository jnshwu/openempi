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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * MessageLogEntry entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "message_log")
@GenericGenerator(name = "message_log_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "message_log_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class MessageLogEntry extends BaseObject implements Serializable
{
	private static final long serialVersionUID = -6061320465621019356L;

	private Integer messageLogId;
	private Date dateReceived;
	private String incomingMessage;
	private String outgoingMessage;
	private MessageType incomingMessageType;
	private MessageType outgoingMessageType;

	/** default constructor */
	public MessageLogEntry() {
	}

	@Id
	@GeneratedValue(generator="message_log_gen")
	@Column(name = "message_log_id", unique = true, nullable = false)
	public Integer getMessageLogId() {
		return messageLogId;
	}

	public void setMessageLogId(Integer messageLogId) {
		this.messageLogId = messageLogId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_received", nullable = false, length = 8)
	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	
	@Column(name = "incoming_message", nullable = true)
	public String getIncomingMessage() {
		return incomingMessage;
	}

	public void setIncomingMessage(String incomingMessage) {
		this.incomingMessage = incomingMessage;
	}

	@Column(name = "outgoing_message", nullable = true)
	public String getOutgoingMessage() {
		return outgoingMessage;
	}

	public void setOutgoingMessage(String outgoingMessage) {
		this.outgoingMessage = outgoingMessage;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "incoming_message_type_cd")
	public MessageType getIncomingMessageType() {
		return incomingMessageType;
	}

	public void setIncomingMessageType(MessageType incomingMessageType) {
		this.incomingMessageType = incomingMessageType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "outgoing_message_type_cd")
	public MessageType getOutgoingMessageType() {
		return outgoingMessageType;
	}

	public void setOutgoingMessageType(MessageType outgoingMessageType) {
		this.outgoingMessageType = outgoingMessageType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageLogId == null) ? 0 : messageLogId.hashCode());
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
		MessageLogEntry other = (MessageLogEntry) obj;
		if (messageLogId == null) {
			if (other.messageLogId != null)
				return false;
		} else if (!messageLogId.equals(other.messageLogId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MessageLogEntry [messageLogId=" + messageLogId + ", dateReceived=" + dateReceived
				+ ", incomingMessage=" + incomingMessage + ", outgoingMessage=" + outgoingMessage
				+ ", incomingMessageType=" + incomingMessageType + ", outgoingMessageType=" + outgoingMessageType + "]";
	}
}
