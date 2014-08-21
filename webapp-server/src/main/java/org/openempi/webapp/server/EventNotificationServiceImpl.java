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
import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.openempi.webapp.client.EventNotificationService;
import org.openempi.webapp.client.model.NotificationEventWeb;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.notification.MergeNotificationEvent;
import org.openhie.openempi.notification.MessageHandler;
import org.openhie.openempi.notification.NotificationEvent;
import org.openhie.openempi.notification.NotificationService;

public class EventNotificationServiceImpl extends AbstractRemoteServiceServlet implements EventNotificationService
{
	// TODO Need to make this configurable by the web application
	private final static int BUFFER_SIZE = 16;
	private final static String OPENEMPI_ADMIN_WEB = "AdminWebClient";

	private CircularFifoBuffer eventBuffer;
	private EventNotificationMessageHandler handler;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		eventBuffer = new CircularFifoBuffer(BUFFER_SIZE);
		handler = new EventNotificationMessageHandler(OPENEMPI_ADMIN_WEB);
	}
	
	@SuppressWarnings("unchecked")
	public List<NotificationEventWeb> getEvents() {
		List<NotificationEventWeb> events = new ArrayList<NotificationEventWeb>();
		for (Iterator<NotificationEventWeb> iter = eventBuffer.iterator(); iter.hasNext(); ) {
			events.add(0, iter.next());
		}
		return events;
	}

	public String registerListener(String eventTypeName) {
		log.info("Registering a listener for eventType " + eventTypeName);
		String message = "";
		NotificationService service = Context.getNotificationService();
		try {
			service.registerListener(eventTypeName, handler);
		} catch (Throwable e) {
			log.error("Error while registering listener: " + e, e);
			message = e.getMessage();
		}
		return message;
	}

	public String unregisterListener(String eventTypeName) {
		log.info("Unegistering a listener for eventType " + eventTypeName);
		String message = "";
		NotificationService service = Context.getNotificationService();
		try {
			service.unregisterListener(eventTypeName, handler);
		} catch (Throwable e) {
			log.error("Error while unregistering listener: " + e, e);
			message = e.getMessage();
		}
		return message;
	}

	public class EventNotificationMessageHandler implements MessageHandler
	{
		private String clientId;
		
		public EventNotificationMessageHandler(String clientId) {
			this.clientId = clientId;
		}
		
		public String getClientId() {
			return clientId;
		}
		
		public void setClientId(String clientId) {
			this.clientId = clientId;
		}
		
		public void processMessage(Message message) {
			log.debug("Received message in the test handler: " + message);
			try {
				NotificationEventWeb event = buildEventFromMessage(message);
				log.debug("Adding message to buffer with ID: " + event.getMessageId());
				eventBuffer.add(event);
			} catch (JMSException e) {
				log.warn("Failed while processing a message received from the Event Notification service: " + e, e);
			}
		}

		private NotificationEventWeb buildEventFromMessage(Message message) throws JMSException {
			ObjectMessage objectMessage = (ObjectMessage) message;
			if (!(objectMessage.getObject() instanceof NotificationEvent)) {
				log.warn("Unable to handle notification event of type: " + objectMessage);
				return null;
			}
			NotificationEvent eventObj = (NotificationEvent) objectMessage.getObject();
			NotificationEventWeb event = new NotificationEventWeb();
			event.setEventTypeName(eventObj.getEventType().getEventTypeName());
			event.setMessageId(message.getJMSMessageID());
			event.setTimestamp(new java.util.Date(message.getJMSTimestamp()));
			event.setPersonIdentifier(getPersonIdentifier(eventObj));
			return event;
		}

		private String getPersonIdentifier(NotificationEvent event) {
			Person person;
			if (event instanceof MergeNotificationEvent) {
				MergeNotificationEvent merge = (MergeNotificationEvent) event;
				Person[] persons = (Person[]) merge.getEventData();
				person = persons[0];
			} else {
				person = (Person) event.getEventData();
			}
			
			if (person.getPersonIdentifiers() == null || person.getPersonIdentifiers().size() == 0) {
				return null;
			}
			PersonIdentifier identifier =  person.getPersonIdentifiers().iterator().next();
			return identifier.getIdentifier();
		}
	}
}
