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
package org.openhie.openempi.openpixpdqadapter;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openpixpdq.api.IMessageStoreLogger;
import org.openhealthtools.openpixpdq.api.MessageStore;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.MessageLogEntry;
import org.openhie.openempi.model.MessageType;
import org.openhie.openempi.service.AuditEventService;

public class MessageStoreLogger extends BasePixPdqAdapter implements IMessageStoreLogger
{
	protected final Log log = LogFactory.getLog(getClass());
	
	public MessageStoreLogger() {
		super();
	}
	
	@Override
	public void saveLog(MessageStore messageLog) {
		log.debug("Received a message to log: " + messageLog);
		AuditEventService auditService = Context.getAuditEventService();
		if (auditService == null) {
			log.warn("Unable to obtain a reference to the audit service in order to persist the message pair in the repository.");
			return;
		}
		
		MessageLogEntry entry = new MessageLogEntry();
		entry.setDateReceived(new Date());
		entry.setIncomingMessage(messageLog.getInMessage());
		entry.setOutgoingMessage(messageLog.getOutMessage());
		MessageType type = new MessageType();
		type.setMessageTypeCode(messageLog.getTriggerEvent());
		entry.setIncomingMessageType(type);
		if (log.isTraceEnabled()) {
			log.trace("Logging the message: " + entry);
		}
		auditService.saveMessageLogEntry(entry);
	}
}
