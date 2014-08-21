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
package org.openempi.webapp.client.mvc.blocking;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.BlockingDataServiceAsync;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.SortedNeighborhoodConfigurationWeb;
import org.openempi.webapp.client.model.SuffixArrayBlockingConfigurationWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SuffixArrayBlockingConfigurationController extends Controller
{
	private SuffixArrayBlockingConfigurationView suffixArrayBlockingView;

	public SuffixArrayBlockingConfigurationController() {		
		this.registerEventTypes(AppEvents.SuffixArrayBlockingConfigurationReceived);
		this.registerEventTypes(AppEvents.SuffixArrayBlockingConfigurationRequest);
		this.registerEventTypes(AppEvents.SuffixArrayBlockingConfigurationSave);
		
		this.registerEventTypes(AppEvents.SuffixArrayBlockingConfigurationView);
	}

	@Override
	protected void initialize() {
		suffixArrayBlockingView = new SuffixArrayBlockingConfigurationView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.SuffixArrayBlockingConfigurationView) {
			forwardToView(suffixArrayBlockingView, event);
		} else if (type == AppEvents.SuffixArrayBlockingConfigurationRequest) {
			requestBlockingConfigurationData();
		} else if (type == AppEvents.SuffixArrayBlockingConfigurationSave) {
			saveBlockingConfiguration(event);
		}
	}

	private void saveBlockingConfiguration(AppEvent event) {
		BlockingDataServiceAsync blockingService = getBlockingDataService();
		
		SuffixArrayBlockingConfigurationWeb configuration = (SuffixArrayBlockingConfigurationWeb) event.getData();		
		blockingService.saveSuffixArrayBlockingConfigurationData(configuration, (new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) { 
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
		    	forwardToView(suffixArrayBlockingView, AppEvents.Error, caught.getMessage());
	      }

	      public void onSuccess(String message) {
	    	  forwardToView(suffixArrayBlockingView, AppEvents.SuffixArrayBlockingConfigurationSaveComplete, message);
	      }
	    }));
	}

	private void requestBlockingConfigurationData() {
		BlockingDataServiceAsync blockingService = getBlockingDataService();
		blockingService.loadSuffixArrayBlockingConfigurationData(new AsyncCallback<SuffixArrayBlockingConfigurationWeb>() {
	      public void onFailure(Throwable caught) {
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
				Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(SuffixArrayBlockingConfigurationWeb result) {
	        forwardToView(suffixArrayBlockingView, AppEvents.SuffixArrayBlockingConfigurationReceived, result);
	      }
	    });
	}
}
