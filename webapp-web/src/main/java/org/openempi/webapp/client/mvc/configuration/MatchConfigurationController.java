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
package org.openempi.webapp.client.mvc.configuration;

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.ConfigurationDataServiceAsync;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.MatchConfigurationWeb;
import org.openempi.webapp.client.model.ReviewRecordPairWeb;
import org.openempi.webapp.client.model.VectorConfigurationWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MatchConfigurationController extends Controller
{
	private MatchConfigurationView matchConfigurationView;

	public MatchConfigurationController() {		
		this.registerEventTypes(AppEvents.MatchConfigurationReceived);
		this.registerEventTypes(AppEvents.MatchConfigurationRequest);
		this.registerEventTypes(AppEvents.MatchConfigurationSave);
		this.registerEventTypes(AppEvents.MatchConfigurationGetLoggedLinksForVector);
		this.registerEventTypes(AppEvents.MatchConfigurationView);
	}

	@Override
	protected void initialize() {
		matchConfigurationView = new MatchConfigurationView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.MatchConfigurationView) {
			forwardToView(matchConfigurationView, event);
		} else if (type == AppEvents.MatchConfigurationRequest) {
			requestMatchConfigurationData();
		} else if (type == AppEvents.MatchVectorConfigurationRequest) {
			requestMatchVectorConfigurationData();
		} else if (type == AppEvents.MatchConfigurationSave) {
			saveMatchConfiguration(event);
		} else if (type == AppEvents.MatchConfigurationGetLoggedLinksForVector) {
			getLoggedLinksForVector(event);
		}
	}

	private void saveMatchConfiguration(AppEvent event) {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		MatchConfigurationWeb configuration = (MatchConfigurationWeb) event.getData();
		configurationDataService.saveProbabilisticMatchingConfiguration(configuration, (new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
		    	forwardToView(matchConfigurationView, AppEvents.Error, caught.getMessage());
	      }

	      public void onSuccess(String message) {
/*	    	  if (message == null || message.length() == 0) {
	    		  Info.display("Information", "Match configuration data was saved successfully.");
	    	  } else {
	    		  Info.display("Information", "Failed to save match configuration data: " + message);
	    	  }
*/	    	  forwardToView(matchConfigurationView, AppEvents.MatchConfigurationSaveComplete, message);
	      }
	    }));
	}

	private void requestMatchConfigurationData() {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		configurationDataService.loadProbabilisticMatchingConfiguration(new AsyncCallback<MatchConfigurationWeb>() {
	      public void onFailure(Throwable caught) {
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
				Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(MatchConfigurationWeb result) {
	        forwardToView(matchConfigurationView, AppEvents.MatchConfigurationReceived, result);
	      }
	    });
	}
	
	private void requestMatchVectorConfigurationData() {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		configurationDataService.loadVectorConfiguration(new AsyncCallback<List<VectorConfigurationWeb>>() {
	      public void onFailure(Throwable caught) {
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
				Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(List<VectorConfigurationWeb> result) {
	        forwardToView(matchConfigurationView, AppEvents.MatchVectorConfigurationReceived, result);
	      }
	    });
	}

	private void getLoggedLinksForVector(AppEvent event) {
		Integer vector = (Integer) event.getData();
	    getPersonDataService().getLoggedLinks( vector, new AsyncCallback<List<ReviewRecordPairWeb>>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    		 
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(List<ReviewRecordPairWeb> result) {	   
		    	  forwardToView(matchConfigurationView, AppEvents.MatchConfigurationGetLoggedLinksForVectorReceived, result);
		      }
		    });					
	}
}
