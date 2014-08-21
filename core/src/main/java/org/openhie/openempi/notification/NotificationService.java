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

import java.util.List;

public interface NotificationService
{
	/**
	 * This method is used for staring the Notification Service. It is primarily used for hiding
	 * all implementation details relating to the broker from the rest of the application.
	 */
	public void startup();
	
	/**
	 * This method is used for stopping the Notification Service. It is primarily used for hiding
	 * all implementation details relating to the broker from the rest of the application.
	 */
	public void shutdown();
	
	/**
	 * Uses the notification mechanism to send out an event. The caller is usually core code that
	 * just completed processing the underlying event and uses this interface method to notify anyone interested
	 * that an event of a particular type has just occurred.
	 * 
	 * @param event An object that encapsulates the event type information and the payload that is associated with this particular event
	 */
	public void fireNotificationEvent(NotificationEvent event);
	
	/**
	 * Used by the caller to indicate interest in receiving notifications regarding
	 * events of interest.
	 * 
	 * @param eventTypeName Name of the event for which the caller wishes to receive notifications about
	 * @param handler The handler that will receive notifications of events for processing
	 */
	public void registerListener(String eventTypeName, MessageHandler handler);
	
	/**
	 * Used by the caller to indicate interest in receiving notifications regarding
	 * events of interest.
	 * 
	 * @param eventTypeNames List of event names for which the caller wishes to receive notifications about
	 * @param handler The handler that will receive notifications of events for processing
	 */
	public void registerListener(List<String> eventTypeNames, MessageHandler handler);
	
	/**
	 * Used by the caller to indicate that it is no longer interested in receiving notifications regarding
	 * events of interest.
	 * 
	 * @param eventTypeName Name of the event for which the caller does no longer want to receive notifications about
	 * @param handler The handler that was registered to receive notifications of events
	 */
	public void unregisterListener(String eventTypeName, MessageHandler handler);
	
	/**
	 * Used by the caller to indicate that it is no longer interested in receiving notifications regarding
	 * events of interest.
	 * 
	 * @param eventTypeName List of event names for which the caller does no longer want to receive notifications about
	 * @param handler The handler that was registered to receive notifications of events
	 */
	public void unregisterListener(List<String> eventTypeName, MessageHandler handler);
}
