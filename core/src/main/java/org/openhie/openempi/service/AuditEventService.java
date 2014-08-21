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

import java.util.Date;
import java.util.List;

import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.MessageLogEntry;
import org.openhie.openempi.model.MessageType;
import org.openhie.openempi.model.Person;

public interface AuditEventService
{
	public AuditEventType getAuditEventTypeByCode(String eventTypeCode);
	
	public AuditEvent saveAuditEvent(AuditEvent auditEvent);
	
	public AuditEvent saveAuditEvent(String auditEventType, String auditEventDescription);

	public AuditEvent saveAuditEvent(String auditEventType, String auditEventDescription, Person refPerson);
	
	public AuditEvent saveAuditEvent(String auditEventType, String auditEventDescription, Person refPerson, Person altRefPerson);
	
	public List<AuditEvent> getAllAuditEvents();
	
	public List<AuditEventType> getAllAuditEventTypes();
	
	public int getAuditEventCount(Date startDate, Date endDate, List<Integer> auditEventTypeCodes);
	
	public List<AuditEvent> filterAuditEvents(Date startDate, Date endDate, List<Integer> auditEventTypeCodes);
	
	public List<AuditEvent> filterAuditEventsPaged(Date startDate, Date endDate, List<Integer> auditEventTypeCodes, int firstResult, int maxResults);

	public List<MessageType> getMessageTypes();
	
	public MessageType getMessageTypeByCode(String messageTypeCode);

	public MessageLogEntry getMessageLogEntry(Integer messageLogId);

	public int getMessageLogEntryCount(Date startDate, Date endDate, List<Integer> messageTypeCodes);

	public MessageLogEntry saveMessageLogEntry(MessageLogEntry messageLogEntry);
	
	public List<MessageLogEntry> filterMessageLogEntries(Date startDate, Date endDate, List<Integer> messageType, int firstResult, int maxResults);
}
