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
package org.openhie.openempi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.MessageLogEntry;
import org.openhie.openempi.model.MessageType;

public class MessageLogTest extends BaseServiceTestCase
{
	public void testFilterMessageLogEntries() {
		try {
			AuditEventService auditService = Context.getAuditEventService();
			MessageType type = auditService.getMessageTypeByCode("ADT_A01");
			assertNotNull("Unable to locate a message type of type ADT_A01", type);
			List<Integer> types = new ArrayList<Integer>();
			types.add(type.getMessageTypeCd());
			int count = auditService.getMessageLogEntryCount(null, null, types);
			log.debug("There are " + count + " message log entries that fit our criteria.");
			
			List<MessageLogEntry> entries = auditService.filterMessageLogEntries(null,  null, types, 0, count);
			for (MessageLogEntry entry : entries) {
				log.debug("Found message log entry: " + entry);
				MessageLogEntry singleEntry = auditService.getMessageLogEntry(entry.getMessageLogId());
				log.debug("Found the single entry: " + entry);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testAddMessageLogEntry() {
		try {
			AuditEventService auditService = Context.getAuditEventService();
			MessageLogEntry entry = new MessageLogEntry();
			entry.setDateReceived(new Date());
			String sourceMessage = 
					"MSH|^~\\&|SendingApplication|SendingFacility|ReceivingApplication|ReceivingFacility|20101004144709||ADT^A01^ADT_A01|NIST-101004144709009|P|2.3.1\n" +
					"EVN||20101004144709\n" +
					"PID|||PIX^^^NIST2010&2.16.840.1.113883.3.72.5.9.1&ISO||ALPHA^ALAN^^^^^L|BARNES^^^^^^L|19781208|M|||820 JORIE BLVD^^NEW YORK CITY^NY^10503||||||||153-12-5432\n" +
					"PV1||I";
			entry.setIncomingMessage(sourceMessage);
			MessageType type = auditService.getMessageTypeByCode("ADT_A01");
			assertNotNull("Unable to locate a message type of type ADT_A01", type);
			entry.setIncomingMessageType(type);
			String responseMessage = 
					"MSH|^~\\&|OPENEMPI_XREF|SYSNET|NIST_SENDER|NIST|20130115225123+0000||ACK^A01|d8e0b77b13c4068ec263|P|2.3.1\n" +
					"MSA|AA|NIST-101101160358190";
			entry.setOutgoingMessage(responseMessage);
			type = auditService.getMessageTypeByCode("ACK");
			assertNotNull("Unable to locate a message type of type ACK", type);
			entry.setOutgoingMessageType(type);
			log.debug("Generated the message log entry: " + entry);
			entry = auditService.saveMessageLogEntry(entry);
			log.debug("Got an entry of " + entry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
