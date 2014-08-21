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

import org.openempi.webapp.client.model.AuditEventSearchCriteriaWeb;
import org.openempi.webapp.client.model.AuditEventListWeb;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.AuditEventDataServiceAsync;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.mvc.Controller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class AuditEventController extends Controller
{
	private AuditEventView auditEventView;

	public AuditEventController() {		
		this.registerEventTypes(AppEvents.AuditEventView);
		this.registerEventTypes(AppEvents.AuditEventRequest);
	}

	@Override
	protected void initialize() {
		auditEventView = new AuditEventView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.AuditEventView) {
			
			forwardToView(auditEventView, event);
		} else if (type == AppEvents.AuditEventRequest) {
			
			AuditEventSearchCriteriaWeb searchCriteria = event.getData();
	    	search(searchCriteria);			
		} 
	}
	
	private void search(AuditEventSearchCriteriaWeb searchCriteria) {		  	  	
		AuditEventDataServiceAsync auditEventDataService = getAuditEventDataService();		
	  	// Info.display("Information", "Submitting request to search audit events");
 	  	
		auditEventDataService.getAuditEventsBySearch(searchCriteria, new AsyncCallback<AuditEventListWeb>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(AuditEventListWeb result) {
		    	  if(result.getAuditEvents() != null)
		    		 GWT.log("Result has " + result.getAuditEvents().size() + " records.", null);
		    	 
		    	  forwardToView(auditEventView, AppEvents.AuditEventReceived, result);
		      }
		    });			
	}
}
