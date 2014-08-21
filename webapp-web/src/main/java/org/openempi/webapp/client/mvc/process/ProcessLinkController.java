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
package org.openempi.webapp.client.mvc.process;

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.ReviewRecordPairWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProcessLinkController extends Controller
{
	private ProcessLinkView processLinkView;
	
	private Integer maxRecords = new Integer(15);
	
	public ProcessLinkController() {
		this.registerEventTypes(AppEvents.ProcessLinkView);
		this.registerEventTypes(AppEvents.ProcessLink);
		this.registerEventTypes(AppEvents.ProcessUnlink);
		this.registerEventTypes(AppEvents.ProcessPairLinkedView);
		this.registerEventTypes(AppEvents.ProcessPairUnlinkedView);
	}
	
	public void initialize() {
		processLinkView = new ProcessLinkView(this);
	}
	
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.ProcessLinkView) {			
			// retrieveAllUnreviewedRecordPairs();
			
			if( Registry.get(Constants.MAX_RECORD_DISPLAING) != null ) {				
				retrieveUnreviewedRecordPairs(((Integer)Registry.get(Constants.MAX_RECORD_DISPLAING)).intValue());					
			} else {
				Registry.register(Constants.MAX_RECORD_DISPLAING, maxRecords);
				retrieveUnreviewedRecordPairs(maxRecords.intValue());
			}
			
		} else if (type == AppEvents.ProcessLink) {
			ReviewRecordPairWeb pair = event.getData();
			linkPair(pair);
			
		} else if (type == AppEvents.ProcessUnlink) {
			ReviewRecordPairWeb pair = event.getData();
			UnlinkPair(pair);
		}
	}
	
	public void linkPair(ReviewRecordPairWeb pair) {
		pair.setRecordsMatch(true);
		
		getPersonDataService().matchReviewRecordPairs(pair, new AsyncCallback<Void>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(Void value) {
		  		 forwardToView(processLinkView, AppEvents.ProcessPairLinkedView, value);				    	  
		      }
		    });		
	}
	
	public void UnlinkPair(ReviewRecordPairWeb pair) {
		pair.setRecordsMatch(false);
 	    
		getPersonDataService().matchReviewRecordPairs(pair, new AsyncCallback<Void>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);		
		      }

		      public void onSuccess(Void value) {
		  		 forwardToView(processLinkView, AppEvents.ProcessPairUnlinkedView, value);				    	  
		      }
		    });
	}
	
	public void retrieveAllUnreviewedRecordPairs() {
	    getPersonDataService().getAllUnreviewedReviewRecordPairs( new AsyncCallback<List<ReviewRecordPairWeb>>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    		 
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(List<ReviewRecordPairWeb> result) {	   
		    	  forwardToView(processLinkView, AppEvents.ProcessLinkView, result);
		      }
		    });					
	}
	
	public void retrieveUnreviewedRecordPairs(int maxResults) {
	    getPersonDataService().getUnreviewedReviewRecordPairs( maxResults, new AsyncCallback<List<ReviewRecordPairWeb>>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    		 
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(List<ReviewRecordPairWeb> result) {	   
		    	  forwardToView(processLinkView, AppEvents.ProcessLinkView, result);
		      }
		    });					
	}
}
