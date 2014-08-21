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
package org.openhie.openempi.notification;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.BaseServiceTestCase;

public class NotificationTest extends BaseServiceTestCase
{
	
	public void testSingleEventRegistration() {
		NotificationService notificationService = Context.getNotificationService();
		MessageHandler handler = new TestMessageHandler("singleEventMessageHandler");
		notificationService.registerListener(EventType.IMPORT_EVENT_TYPE.getEventTypeName(), handler);
/*		
		NotificationEvent event = new NotificationEvent(EventType.IMPORT_EVENT_TYPE, "add event testData");
		notificationService.fireNotificationEvent(event);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
		
		notificationService.unregisterListener(EventType.IMPORT_EVENT_TYPE.getEventTypeName(), handler);
//		Context.shutdown();*/
	}
	
/*
	public void testMultipleEventRegistration() {
		NotificationService notificationService = Context.getNotificationService();
		java.util.List<String> events = new java.util.ArrayList<String>();
		events.add(EventType.ADD_EVENT_TYPE.getEventTypeName());
		events.add(EventType.DELETE_EVENT_TYPE.getEventTypeName());
		events.add(EventType.IMPORT_EVENT_TYPE.getEventTypeName());
		
		notificationService.registerListener(events, new TestMessageHandler("multipleEventMessageHandler"));
		
		NotificationEvent event = new NotificationEvent(EventType.ADD_EVENT_TYPE, "add event testData");
		notificationService.fireNotificationEvent(event);
		
		event = new NotificationEvent(EventType.DELETE_EVENT_TYPE, "delete event testData");
		notificationService.fireNotificationEvent(event);
		
		event = new NotificationEvent(EventType.IMPORT_EVENT_TYPE, "import event testData");
		notificationService.fireNotificationEvent(event);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
//		Context.shutdown();
	}
	*/
	
	public class TestMessageHandler implements MessageHandler
	{
		private String clientId;
		
		public TestMessageHandler(String clientId) {
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
			ObjectMessage objectMessage = (ObjectMessage) message;
			try {
				log.debug("Contents of message are: " + objectMessage.getObject());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}
 