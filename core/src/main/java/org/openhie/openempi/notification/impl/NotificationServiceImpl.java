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
package org.openhie.openempi.notification.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.broker.BrokerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.notification.EventType;
import org.openhie.openempi.notification.MessageHandler;
import org.openhie.openempi.notification.NotificationEvent;
import org.openhie.openempi.notification.NotificationService;
import org.openhie.openempi.service.impl.BaseServiceImpl;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class NotificationServiceImpl extends BaseServiceImpl implements NotificationService
{
	private JmsTemplate jmsTemplate;
	private Map<String,Topic> topicMap;
	private Map<String,MessageListenerImpl> listenerMap;
	private static BrokerService brokerService;
	private MessageHandler loggingMessageHandler = new LoggingMessageHandler();
	private boolean brokerInitialized = false;
	
	public NotificationServiceImpl() {
		listenerMap = new HashMap<String,MessageListenerImpl>();
	}

	public void fireNotificationEvent(final NotificationEvent event) {
		if (brokerService == null || !brokerInitialized) {
			log.debug("The broker service is not running in fireNotificationEvent.");
			return;
		}
		
		if (event == null || event.getEventType() == null) {
			log.warn("Request was made to generate an event but the required attributes were not present in the request.");
			return;
		}
		log.info("Fire notification for event " + event.getEventType().getEventTypeName());
		
		Topic topic = topicMap.get(event.getEventType().getEventTypeName());
		if (topic == null) {
			log.warn("Request was made to generate an event but the event type is unknown.");
			return;
		}
		
		try {
			jmsTemplate.send(topic, new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					ObjectMessage message = session.createObjectMessage();
					message.setObject(event);
					return message;
				}
			});
		} catch (RuntimeException e) {
			log.warn("Unable to send out a notification event: " + e, e);
		}
	}

	public void registerListener(String eventTypeName, MessageHandler handler) {
		if (brokerService == null || !brokerInitialized) {
			log.debug("The broker service is not running in registerListener.");			
			return;
		}
		
		log.info("Registering handler for event " + eventTypeName);
		if (!isValidHandler(handler)) {
			return;
		}
		
		Topic topic = topicMap.get(eventTypeName);
		if (topic == null) {
			log.error("Caller attempted to register interest to events of unknown type " + eventTypeName);
			throw new RuntimeException("Unknown event type specified in registration request.");
		}
		
		if (isListenerRegistered(handler, eventTypeName)) {
			log.warn("Caller attempted to register interest for the same event again.");
			return;
		}
		ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();
		try {
			TopicConnection connection = (TopicConnection) connectionFactory.createConnection();
			if (connection.getClientID() == null) {
				connection.setClientID(handler.getClientId());
			}
			log.debug("Connection is of type " + connection);
			TopicSession topicSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			TopicSubscriber subscriber = topicSession.createSubscriber(topic);
			MessageListenerImpl listener = new MessageListenerImpl(connection, topicSession, subscriber, handler);
			saveListenerRegistration(listener, eventTypeName);
			subscriber.setMessageListener(listener);
			connection.start();
		} catch (JMSException e) {
			log.error("Failed while setting up a registrant for notification events. Error: " + e, e);
			throw new RuntimeException("Unable to setup registration for event notification: " + e.getMessage());
		}		
	}

	public void registerListener(List<String> eventTypeNames, MessageHandler handler) {
		if (brokerService == null || !brokerInitialized) {
			log.debug("The broker service is not running in registerListener.");			
			return;
		}
		
		if (!isValidHandler(handler)) {
			return;
		}

		ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			connection.setClientID(handler.getClientId());
		} catch (JMSException e) {
			log.error("Failed while setting up a registrant for notification events. Error: " + e, e);
			throw new RuntimeException("Unable to setup registration for event notification: " + e.getMessage());
		}
		
		for (String eventTypeName : eventTypeNames) {
			log.info("Registering handler for event " + eventTypeName);
			Topic topic = topicMap.get(eventTypeName);
			if (topic == null) {
				log.error("Caller attempted to register interest to events of unknown type " + eventTypeName);
				throw new RuntimeException("Unknown event type specified in registration request.");
			}
			
			if (isListenerRegistered(handler, eventTypeName)) {
				log.warn("Caller attempted to register interest for the same event again.");
				return;
			}
			
			try {
				Session topicSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				TopicSubscriber subscriber = topicSession.createDurableSubscriber(topic, topic.getTopicName());
				MessageListenerImpl listener = new MessageListenerImpl(connection, topicSession, subscriber, handler);
				saveListenerRegistration(listener, eventTypeName);
				subscriber.setMessageListener(listener);
			} catch (JMSException e) {
				log.error("Failed while setting up a registrant for notification events. Error: " + e, e);
				throw new RuntimeException("Unable to setup registration for event notification: " + e.getMessage());
			}
		}
		
		try {
			connection.start();
		} catch (JMSException e) {
			log.error("Failed while setting up a registrant for notification events. Error: " + e, e);
			throw new RuntimeException("Unable to setup registration for event notification: " + e.getMessage());
		}
	}

	public void unregisterListener(String eventTypeName, MessageHandler handler) {
		if (brokerService == null || !brokerInitialized) {
			log.debug("The broker service is not running in unregisterListener.");			
			return;
		}
		
		log.info("Unregistering handler for event " + eventTypeName);
		if (!isValidHandler(handler)) {
			return;
		}
		
		Topic topic = topicMap.get(eventTypeName);
		if (topic == null) {
			log.error("Caller attempted to unregister interest to events of unknown type " + eventTypeName);
			throw new RuntimeException("Unknown event type specified in un-registration request.");
		}
		
		MessageListenerImpl listener = lookupMessageListener(handler, eventTypeName);
		if (listener == null) {
			log.warn("Caller attempted to unregister a listener that is not known to the system: " + buildMapKey(handler, eventTypeName));
			return;
		}
		
		try {
			listener.getConnection().stop();
			listener.getSubscriber().close();
			listener.getSession().unsubscribe(topic.getTopicName());
			listener.getSession().close();
		} catch (JMSException e) {
			log.error("Failed while unregistering a listener. Error: " + e, e);
			throw new RuntimeException("Unable to unregister a listener for event notification: " + e.getMessage());
		}		
	}

	public void unregisterListener(List<String> eventTypeNames, MessageHandler handler) {
		if (brokerService == null || !brokerInitialized) {
			log.debug("The broker service is not running in unregisterListener.");			
			return;
		}
		
		if (!isValidHandler(handler)) {
			return;
		}
		for (String eventTypeName : eventTypeNames) {
			unregisterListener(eventTypeName, handler);
		}
	}

	private boolean isValidHandler(MessageHandler handler) {
		if (handler == null || handler.getClientId() == null || 
				handler.getClientId().length() == 0) {
			return false;
		}
		return true;
	}
	
	public void startup() {
		if (brokerService == null) {
			log.info("The broker service is not defined so it won't be started.");
			return;
		}

		if (brokerInitialized) {
			return;
		}
		
		String openEmpiHome = Context.getOpenEmpiHome();
		System.setProperty("activemq.base", openEmpiHome);
		try {
			log.info("Starting the notification broker...");
			try {
				brokerService.start();
				String[] events = {
						EventType.ADD_EVENT_TYPE.getEventTypeName(),
						EventType.DELETE_EVENT_TYPE.getEventTypeName(),
						EventType.IMPORT_EVENT_TYPE.getEventTypeName(),
						EventType.MERGE_EVENT_TYPE.getEventTypeName(),
						EventType.UPDATE_EVENT_TYPE.getEventTypeName()
					};
				registerListener((List<String>) Arrays.asList(events), loggingMessageHandler);
				log.info("Registered the logging listener...");
			} catch (java.io.IOException e) {
				log.warn("It seems that the Notification Broker is already up and running: " + e, e);
			}
			brokerInitialized = true;
			log.info("Notification broker was started successfuly.");
		} catch (Exception e) {
			log.error("Failed while trying to start the notification broker. Error: " + e, e);
			throw new RuntimeException("Unable to start the notification broker due to: " + e.getMessage());
		}
	}

	public void shutdown() {
		if (brokerService == null || !brokerInitialized) {
			return;
		}
		
		try {
			String[] events = {
					EventType.ADD_EVENT_TYPE.getEventTypeName(),
					EventType.DELETE_EVENT_TYPE.getEventTypeName(),
					EventType.IMPORT_EVENT_TYPE.getEventTypeName(),
					EventType.MERGE_EVENT_TYPE.getEventTypeName(),
					EventType.UPDATE_EVENT_TYPE.getEventTypeName()
				};
			unregisterListener((List<String>) Arrays.asList(events), loggingMessageHandler);
			log.info("Unregistered the logging listener...");
			
			log.info("Shutting down the notification broker...");
			brokerService.stop();
			brokerInitialized = false;
			log.info("Notification broker was shutdown successfuly.");
		} catch (Exception e) {
			log.error("Failed while trying to shutdown the notification broker. Error: " + e, e);
			throw new RuntimeException("Unable to shutdown the notification broker due to: " + e.getMessage());
		}
	}
	
	private void saveListenerRegistration(MessageListenerImpl listener, String eventTypeName) {
		String key = buildMapKey(listener.getHandler(), eventTypeName);
		listenerMap.put(key, listener);
	}

	private MessageListenerImpl lookupMessageListener(MessageHandler handler, String eventTypeName) {
		String key = buildMapKey(handler, eventTypeName);
		return listenerMap.get(key);
	}
	
	private boolean isListenerRegistered(MessageHandler handler, String eventTypeName) {
		String key = buildMapKey(handler, eventTypeName);
		if (listenerMap.get(key) != null) {
			return true;
		}
		return false;
	}

	private String buildMapKey(MessageHandler handler, String eventTypeName) {
		String key = handler.getClientId() + ":" + eventTypeName;
		return key;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Map<String, Topic> getTopicMap() {
		return topicMap;
	}

	public void setTopicMap(Map<String, Topic> topicMap) {
		this.topicMap = topicMap;
	}
	
	public void setBrokerService(BrokerService brokerService) {
		NotificationServiceImpl.brokerService = brokerService;
	}
	
	public BrokerService getBrokerService() {
		return brokerService;
	}
	
	private class LoggingMessageHandler implements MessageHandler
	{
		private final static String LOGGING_MESSAGE_HANDLER_CLIENT = "loggingMessageHandler";
		private final Log log = LogFactory.getLog(getClass());
		
		public String getClientId() {
			return LOGGING_MESSAGE_HANDLER_CLIENT;
		}

		public void processMessage(Message message) {
			String topicName = "";
			try {
				Topic topic = (Topic) message.getJMSDestination();
				topicName = topic.getTopicName();
			} catch (JMSException e) {
				log.warn("While trying to process message got an error: " + e, e);
				return;
			}
			if (log.isDebugEnabled()) {
				log.debug("From destination: " + topicName + " received message: " + message);
			}
		}
	}
}
