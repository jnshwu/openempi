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
package org.openhie.openempi.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhie.openempi.ValidationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.AuditEventDao;
import org.openhie.openempi.dao.MessageLogDao;
import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.MessageLogEntry;
import org.openhie.openempi.model.MessageType;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.AuditEventService;
import org.openhie.openempi.service.ValidationService;

public class AuditEventServiceImpl extends BaseServiceImpl implements AuditEventService
{
	private AuditEventDao auditEventDao;
	private MessageLogDao messageLogDao;
	
	private Map<String, AuditEventType> eventTypeMap;
	private Map<String, MessageType> messageTypeMap;
	private List<MessageType> messageTypes;
	
	public AuditEventServiceImpl() {
		
	}
	
	@SuppressWarnings("unchecked")
	public void init() {
		log.info("Initializing the Audit Event Service.");
		eventTypeMap = new HashMap<String, AuditEventType>();
		List<AuditEventType> types = (List<AuditEventType>) auditEventDao.getAll(AuditEventType.class);
		for (AuditEventType auditEventType : types) {
			eventTypeMap.put(auditEventType.getAuditEventTypeCode(), auditEventType);
		}
		log.debug("Built the map of audit event types with count of " + types.size());
		
		messageTypeMap = new HashMap<String, MessageType>();
		messageTypes = messageLogDao.getMessageTypes();
		for (MessageType messageType : messageTypes) {
			messageTypeMap.put(messageType.getMessageTypeCode(), messageType);
		}
		log.debug("Built the map of message types with count of " + messageTypes.size());
	}

	@SuppressWarnings("unchecked")
	public List<AuditEvent> getAllAuditEvents() {
		return (List<AuditEvent>) auditEventDao.getAll(AuditEvent.class);
	}

	@SuppressWarnings("unchecked")
	public List<AuditEventType> getAllAuditEventTypes() {
		return (List<AuditEventType>) auditEventDao.getAll(AuditEventType.class);
	}
	
	public List<AuditEvent> filterAuditEvents(Date startDate, Date endDate, List<Integer> auditEventTypeCodes) {
		return (List<AuditEvent>) auditEventDao.filterAuditEvents(startDate, endDate, auditEventTypeCodes);
	}

	public int getAuditEventCount(Date startDate, Date endDate, List<Integer> auditEventTypeCodes) {
		return auditEventDao.getAuditEventCount(startDate, endDate, auditEventTypeCodes);
	}
	
	public List<AuditEvent> filterAuditEventsPaged(Date startDate, Date endDate, List<Integer> auditEventTypeCodes, int firstResult, int maxResults) {
		return auditEventDao.filterAuditEventsPaged(startDate, endDate, auditEventTypeCodes, firstResult, maxResults);
	}

	public AuditEventType getAuditEventTypeByCode(String eventTypeCode) {
		return eventTypeMap.get(eventTypeCode);
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription) {
		return saveAuditEvent(auditEventTypeCode, auditEventDescription, null, null);
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription, Person refPerson) {
		return saveAuditEvent(auditEventTypeCode, auditEventDescription, refPerson, null);
	}

	public AuditEvent saveAuditEvent(String auditEventTypeCode, String auditEventDescription, Person refPerson,
			Person altRefPerson) {
		
		AuditEventType auditEventType = getAuditEventTypeByCode(auditEventTypeCode);
		if (auditEventType == null) {
			log.error("Attempted to audit an event with unknown audit event type: " + auditEventTypeCode);
			throw new ValidationException("Invalid audit event type code " + auditEventTypeCode);
		}
		
		AuditEvent auditEvent = new AuditEvent(new java.util.Date(), auditEventType, auditEventDescription, Context.getUserContext().getUser());
		auditEvent.setRefPerson(refPerson);
		auditEvent.setAltRefPerson(altRefPerson);
		
		log.debug("About to audit the event " + auditEvent);
		auditEvent = (AuditEvent) auditEventDao.save(auditEvent);
		return auditEvent;
	}

	public AuditEvent saveAuditEvent(AuditEvent auditEvent) {
		
		ValidationService validationService = Context.getValidationService();
		validationService.validate(auditEvent);
		
		return (AuditEvent) auditEventDao.save(auditEvent);
	}

	public List<MessageType> getMessageTypes() {
		return messageTypes;
	}	

	public int getMessageLogEntryCount(Date startDate, Date endDate, List<Integer> messageTypeCodes) {
		return messageLogDao.getMessageLogEntryCount(startDate, endDate, messageTypeCodes);
	}

	public MessageType getMessageTypeByCode(String messageTypeCode) {
		return messageTypeMap.get(messageTypeCode);
	}
	
	public MessageLogEntry getMessageLogEntry(Integer messageLogId) {
		return (MessageLogEntry) messageLogDao.getMessageLogEntry(messageLogId);
	}
	
	public MessageLogEntry saveMessageLogEntry(MessageLogEntry messageLogEntry) {
		MessageType msgType = loadMessageType(messageLogEntry.getIncomingMessageType());
		messageLogEntry.setIncomingMessageType(msgType);
		msgType = loadMessageType(messageLogEntry.getOutgoingMessageType());
		messageLogEntry.setOutgoingMessageType(msgType);
		return (MessageLogEntry) messageLogDao.saveMessageLogEntry(messageLogEntry);
	}
	
	private MessageType loadMessageType(MessageType msgType) {
		if (msgType == null || (msgType.getMessageTypeCd() == null && msgType.getMessageTypeCode() == null)) {
			msgType = getMessageTypeByCode(MessageType.UNKNOWN_MESSAGE_TYPE_CODE);
			return msgType;
		}
		if (msgType.getMessageTypeCd() != null) {
			return msgType;
		}
		msgType = getMessageTypeByCode(msgType.getMessageTypeCode());
		if (msgType == null) {
			msgType = getMessageTypeByCode(MessageType.UNKNOWN_MESSAGE_TYPE_CODE);
		}
		return msgType;
	}

	
	public List<MessageLogEntry> filterMessageLogEntries(Date startDate, Date endDate, List<Integer> messageType, int firstResult, int maxResults) {
		return messageLogDao.filterMessageLogEntries(startDate, endDate, messageType, firstResult, maxResults);
	}
	
	public AuditEventDao getAuditEventDao() {
		return auditEventDao;
	}

	public void setAuditEventDao(AuditEventDao auditEventDao) {
		this.auditEventDao = auditEventDao;
	}

	public MessageLogDao getMessageLogDao() {
		return messageLogDao;
	}

	public void setMessageLogDao(MessageLogDao messageLogDao) {
		this.messageLogDao = messageLogDao;
	}
}
