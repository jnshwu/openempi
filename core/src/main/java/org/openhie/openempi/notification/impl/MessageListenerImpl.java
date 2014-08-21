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

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TopicSubscriber;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.notification.MessageHandler;

public class MessageListenerImpl implements MessageListener, org.openhie.openempi.notification.MessageListener
{
	protected final Log log = LogFactory.getLog(getClass());
	 
	private Connection connection;
	private Session session;
	private MessageHandler handler;
	private TopicSubscriber subscriber;
	
	public MessageListenerImpl(Connection connection, Session session, TopicSubscriber subscriber, MessageHandler handler) {
		this.connection = connection;
		this.session = session;
		this.subscriber = subscriber;
		this.handler = handler;
	}
	
	public void onMessage(Message message) {
		log.debug("Received message " + message);
		handler.processMessage(message);
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public MessageHandler getHandler() {
		return handler;
	}

	public void setHandler(MessageHandler handler) {
		this.handler = handler;
	}

	public TopicSubscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(TopicSubscriber subscriber) {
		this.subscriber = subscriber;
	}
}
