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
package org.openempi.webapp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openempi.webapp.client.AuditEventDataService;
import org.openempi.webapp.client.model.AuditEventSearchCriteriaWeb;
import org.openempi.webapp.client.model.AuditEventTypeWeb;
import org.openempi.webapp.client.model.AuditEventListWeb;
import org.openempi.webapp.client.model.AuditEventWeb;
import org.openempi.webapp.client.model.MessageLogSearchCriteriaWeb;
import org.openempi.webapp.client.model.MessageLogListWeb;
import org.openempi.webapp.client.model.MessageLogEntryWeb;
import org.openempi.webapp.client.model.MessageTypeWeb;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.MessageLogEntry;
import org.openhie.openempi.service.AuditEventService;

public class AuditEventDataServiceImpl extends AbstractRemoteServiceServlet implements AuditEventDataService
{
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuditEventListWeb getAuditEventsBySearch( AuditEventSearchCriteriaWeb searchCriteria) throws Exception {
		log.debug("Get Audit Events By Search");
		
		authenticateCaller();
		try {			
			AuditEventService auditEventService = Context.getAuditEventService();		
			
			List<AuditEventTypeWeb> auditTypes = new ArrayList( searchCriteria.getAuditEventTypes() );
			List<Integer> auditEventTypeCodes = new java.util.ArrayList<Integer>(auditTypes.size());
			for( AuditEventTypeWeb type : auditTypes) {
				auditEventTypeCodes.add(type.getAuditEventTypeCd());
			}
			
			// 
			Date startDate = ModelTransformer.StringToDateTime(searchCriteria.getStartDateTime());
			Date endDate = ModelTransformer.StringToDateTime(searchCriteria.getEndDateTime());
			int offset = searchCriteria.getFirstResult();
			int pageSize = searchCriteria.getMaxResults();
			
			// Get total count
			int totalCount = auditEventService.getAuditEventCount(startDate, endDate, auditEventTypeCodes);
			
			// get List of AuditEvent
			List<AuditEvent> types = auditEventService.filterAuditEventsPaged(startDate, endDate, auditEventTypeCodes, offset, pageSize );			
			List<AuditEventWeb> typeWebs = new java.util.ArrayList<AuditEventWeb>(types.size());
			for (AuditEvent ts : types) {

				// AuditEventWeb tw = ModelTransformer.map( ts, AuditEventWeb.class);
				AuditEventWeb tw = ModelTransformer.mapAuditEvent( ts, AuditEventWeb.class);
				typeWebs.add(tw);
			}
			
			
			AuditEventListWeb auditList = new AuditEventListWeb();			
			auditList.setTotalCount(totalCount);
			auditList.setAuditEventTypes(typeWebs);	
			
			return auditList;
			
		} catch (Exception e) {
			log.error("Failed while trying to get audit events: " + e, e);
		}		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public MessageLogListWeb getMessageLogsBySearch( MessageLogSearchCriteriaWeb searchCriteria) throws Exception {
		log.debug("Get Message Log By Search");
		
		authenticateCaller();
		try {			
			AuditEventService auditEventService = Context.getAuditEventService();		

			List<MessageTypeWeb> messageTypes = new ArrayList( searchCriteria.getMessageTypes() );
			List<Integer> messageTypeCodes = new java.util.ArrayList<Integer>(messageTypes.size());
			for( MessageTypeWeb type : messageTypes) {
				messageTypeCodes.add(type.getMessageTypeCd());
			}
			
			Date startDate = ModelTransformer.StringToDateTime(searchCriteria.getStartDateTime());
			Date endDate = ModelTransformer.StringToDateTime(searchCriteria.getEndDateTime());
			int offset = searchCriteria.getFirstResult();
			int pageSize = searchCriteria.getMaxResults();
			
			// Get total count
			int totalCount = auditEventService.getMessageLogEntryCount(startDate, endDate, messageTypeCodes);
			
			// get List of MessageLogs
			List<MessageLogEntry> logs = auditEventService.filterMessageLogEntries(startDate, endDate, messageTypeCodes, offset, pageSize );			
			List<MessageLogEntryWeb> logWebs = new java.util.ArrayList<MessageLogEntryWeb>(logs.size());
			
			for (MessageLogEntry ml : logs) {
				MessageLogEntryWeb mlw = ModelTransformer.mapMessage( ml, MessageLogEntryWeb.class);
				logWebs.add(mlw);
			}						
			
			MessageLogListWeb messageList = new MessageLogListWeb();							
			messageList.setTotalCount(totalCount);	
			messageList.setMessageLogs(logWebs);	
			
			return messageList;
			
		} catch (Exception e) {
			log.error("Failed while trying to get list of message logs: " + e, e);
		}		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public MessageLogEntryWeb getMessageLogEntry( Integer messageLogId ) throws Exception {
		log.debug("Get Message Log Entry");
		
		authenticateCaller();
		try {			
			AuditEventService auditEventService = Context.getAuditEventService();		
			
			// get MessageLogs
			MessageLogEntry messageLog = auditEventService.getMessageLogEntry( messageLogId );						
			MessageLogEntryWeb messageLogWeb = ModelTransformer.mapMessage( messageLog, MessageLogEntryWeb.class);
			
			return messageLogWeb;
			
		} catch (Exception e) {
			log.error("Failed while trying to get message log: " + e, e);
		}		
		return null;
	}
}
