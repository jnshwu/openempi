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
import org.openempi.webapp.client.model.CustomFieldWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CustomFieldsConfigurationController extends Controller
{
	private CustomFieldsConfigurationView customFieldsConfigurationView;

	public CustomFieldsConfigurationController() {		
		this.registerEventTypes(AppEvents.CustomFieldsConfigurationReceived);
		this.registerEventTypes(AppEvents.CustomFieldsConfigurationRequest);
		this.registerEventTypes(AppEvents.CustomFieldsConfigurationSave);
		this.registerEventTypes(AppEvents.CustomFieldsConfigurationView);
	}

	@Override
	protected void initialize() {
		customFieldsConfigurationView = new CustomFieldsConfigurationView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.CustomFieldsConfigurationView) {
			forwardToView(customFieldsConfigurationView, event);
		} else if (type == AppEvents.CustomFieldsConfigurationRequest) {
			requestCustomFieldsConfigurationData();
		} else if (type == AppEvents.CustomFieldsConfigurationSave) {
			saveCustomFieldsConfiguration(event);
		}
	}

	@SuppressWarnings("unchecked")
	private void saveCustomFieldsConfiguration(AppEvent event) {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		List<CustomFieldWeb> configuration = (List<CustomFieldWeb>) event.getData();
		configurationDataService.saveCustomFieldsConfiguration(configuration, (new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
		    	forwardToView(customFieldsConfigurationView, AppEvents.Error, caught.getMessage());	    	  	    	  
	      }

	      public void onSuccess(String message) {
	    	  forwardToView(customFieldsConfigurationView, AppEvents.CustomFieldsConfigurationSaveComplete, message);
	      }
	    }));
	}

	private void requestCustomFieldsConfigurationData() {
		ConfigurationDataServiceAsync configurationDataService = getConfigurationDataService();
		configurationDataService.loadCustomFieldsConfiguration(new AsyncCallback<List<CustomFieldWeb>>() {
	      public void onFailure(Throwable caught) {

				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
				Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(List<CustomFieldWeb> result) {
	        forwardToView(customFieldsConfigurationView, AppEvents.CustomFieldsConfigurationReceived, result);
	      }
	    });
	}

}
