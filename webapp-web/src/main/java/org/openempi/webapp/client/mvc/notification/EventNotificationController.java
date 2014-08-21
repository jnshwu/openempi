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
package org.openempi.webapp.client.mvc.notification;

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.model.NotificationEventWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EventNotificationController extends Controller
{
	private EventNotificationView notificationView;
	
	public EventNotificationController() {
		this.registerEventTypes(AppEvents.EventNotificationView);
		this.registerEventTypes(AppEvents.EventNotificationEventsInitiate);
		this.registerEventTypes(AppEvents.EventNotificationRegistrationInitiate);
		this.registerEventTypes(AppEvents.EventNotificationUnregistrationInitiate);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		notificationView = new EventNotificationView(this);
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.EventNotificationView) {
			forwardToView(notificationView, event);
		} else if (type == AppEvents.EventNotificationRegistrationInitiate) {
			String eventTypeName = event.getData();
			registerListener(eventTypeName);
		} else if (type == AppEvents.EventNotificationUnregistrationInitiate) {
			String eventTypeName = event.getData();
			unregisterListener(eventTypeName);
		} else if (type == AppEvents.EventNotificationEventsInitiate) {
			getEvents();
		}
	}
	
	public void getEvents() {
		Info.display("Information", "Initiating a request for event notification data.");
	    getEventNotificationService().getEvents(new AsyncCallback<List<NotificationEventWeb>>() {
	      public void onFailure(Throwable caught) {
	    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(List<NotificationEventWeb> events) {
	    	  GWT.log("Event count received is: " + events.size(), null);
	    	  forwardToView(notificationView, AppEvents.EventNotificationEventsComplete, events);
	      }
	    });		
	}
	
	public void unregisterListener(String eventTypeName) {
		Info.display("Information", "Initiating an unregistration event for event type :" + eventTypeName);
	    getEventNotificationService().unregisterListener(eventTypeName, new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(String message) {
	    	  GWT.log("Result message from unregister notification is: " + message, null);
	    	  forwardToView(notificationView, AppEvents.EventNotificationUnregistrationComplete, message);
	      }
	    });		
	}
	
	public void registerListener(String eventTypeName) {
		Info.display("Information", "Initiating a registration event for event type :" + eventTypeName);
	    getEventNotificationService().registerListener(eventTypeName, new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(String message) {
	    	  GWT.log("Result message from register notification is: " + message, null);
	    	  forwardToView(notificationView, AppEvents.EventNotificationRegistrationComplete, message);
	      }
	    });		
	}
}
