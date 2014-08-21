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

import com.allen_sauer.gwt.log.client.Log;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.ConfigurationDataServiceAsync;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.ExactMatchingConfigurationWeb;
import org.openempi.webapp.client.model.MatchFieldWeb;
import org.openempi.webapp.client.mvc.Controller;


public class DeterministicMatchConfigurationController extends Controller
{
	private DeterministicMatchConfigurationView matchConfigurationView;

	public DeterministicMatchConfigurationController() {		
		this.registerEventTypes(AppEvents.DeterministicMatchConfigurationReceived);
		this.registerEventTypes(AppEvents.DeterministicMatchConfigurationRequest);
		this.registerEventTypes(AppEvents.DeterministicMatchConfigurationSave);
		this.registerEventTypes(AppEvents.DeterministicMatchConfigurationView);
	}

	@Override
	protected void initialize() {
		matchConfigurationView = new DeterministicMatchConfigurationView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.DeterministicMatchConfigurationView) {
			forwardToView(matchConfigurationView, event);
		} else if (type == AppEvents.DeterministicMatchConfigurationRequest) {
			requestMatchConfigurationData();
		} else if (type == AppEvents.DeterministicMatchConfigurationSave) {
			saveMatchConfiguration(event);
		}
	}

	private void saveMatchConfiguration(AppEvent event) {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		ExactMatchingConfigurationWeb configuration = (ExactMatchingConfigurationWeb) event.getData();			
		configurationDataService.saveExactMatchingConfiguration(configuration, (new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
		    	forwardToView(matchConfigurationView, AppEvents.Error, caught.getMessage());	 
	      }

	      public void onSuccess(String message) {

	    	  forwardToView(matchConfigurationView, AppEvents.DeterministicMatchConfigurationSaveComplete, message);
	      }
	    }));
	}

	private void requestMatchConfigurationData() {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		configurationDataService.loadExactMatchingConfiguration(new AsyncCallback<ExactMatchingConfigurationWeb>() {
	      public void onFailure(Throwable caught) {
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
				Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(ExactMatchingConfigurationWeb result) {
	    	  Log.debug("Received the exact matching configuration data: " + result);
	    	  for (MatchFieldWeb field : result.getMatchFields()) {
	    		  Log.debug("Match Field: " + field.getFieldName() + "," + field.getComparatorFunctionName() + "," + field.getMatchThreshold());
	    	  }
	        forwardToView(matchConfigurationView, AppEvents.DeterministicMatchConfigurationReceived, result);
	      }
	    });
	}

}
