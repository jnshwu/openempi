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
package org.openempi.webapp.client.mvc.event;

//import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;

import org.openempi.webapp.client.model.MessageLogEntryWeb;
import org.openempi.webapp.client.model.MessageLogSearchCriteriaWeb;
import org.openempi.webapp.client.model.MessageLogListWeb;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.AuditEventDataServiceAsync;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.mvc.Controller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class MessageLogController extends Controller
{
	private MessageLogView messageLogView;

	public MessageLogController() {		
		this.registerEventTypes(AppEvents.MessageLogView);
		this.registerEventTypes(AppEvents.MessageLogRequest);
	}

	@Override
	protected void initialize() {
		messageLogView = new MessageLogView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.MessageLogView) {
			
			forwardToView(messageLogView, event);
		} else if (type == AppEvents.MessageLogRequest) {
			
			MessageLogSearchCriteriaWeb searchCriteria = event.getData();
	    	search(searchCriteria);		
			
		} else if (type == AppEvents.MessageLogDetailRequest) {
			MessageLogEntryWeb message = event.getData();
			getMessageLogEntry(message);				
		}
	}
	
	private void search(MessageLogSearchCriteriaWeb searchCriteria) {		  	  	
		AuditEventDataServiceAsync auditEventDataService = getAuditEventDataService();		
	  	// Info.display("Information", "Submitting request to search audit events");
 	  	
		auditEventDataService.getMessageLogsBySearch(searchCriteria, new AsyncCallback<MessageLogListWeb>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(MessageLogListWeb result) {
		    	  if(result.getMessageLogs() != null)
		    		 GWT.log("Result has " + result.getMessageLogs().size() + " records.", null);
		    	 
		    	  forwardToView(messageLogView, AppEvents.MessageLogReceived, result);
		      }
		    });			
	}
	
	private void getMessageLogEntry(MessageLogEntryWeb message) {		  	  	
		AuditEventDataServiceAsync auditEventDataService = getAuditEventDataService();		
	  	// Info.display("Information", "Submitting request to search audit events");
 	  	
		auditEventDataService.getMessageLogEntry(message.getMessageLogId(), new AsyncCallback<MessageLogEntryWeb>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(MessageLogEntryWeb result) {
		    	 
		    	  forwardToView(messageLogView, AppEvents.MessageLogDetailReceived, result);
		      }
		    });			
	}
}
