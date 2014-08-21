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
package org.openempi.webapp.client.mvc.fileloader;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.FileLoaderDataServiceAsync;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.LoaderConfigWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FileLoaderConfigurationController extends Controller
{
	private FileLoaderConfigurationView fileLoaderConfigurationView;
	private FileLoaderDataServiceAsync fileLoaderService;

	public FileLoaderConfigurationController() {		
		this.registerEventTypes(AppEvents.FileLoaderConfigurationReceived);
		this.registerEventTypes(AppEvents.FileLoaderConfigurationRequest);
		this.registerEventTypes(AppEvents.FileLoaderConfigurationSave);
		this.registerEventTypes(AppEvents.FileLoaderConfigurationView);
	}

	@Override
	protected void initialize() {
		fileLoaderConfigurationView = new FileLoaderConfigurationView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.FileLoaderConfigurationView) {
			forwardToView(fileLoaderConfigurationView, event);
		} else if (type == AppEvents.FileLoaderConfigurationRequest) {
			requestFileLoaderConfigurationData();
		} else if (type == AppEvents.FileLoaderConfigurationSave) {
			saveFileLoaderConfiguration(event);
		}
	}

	private void saveFileLoaderConfiguration(AppEvent event) {
		FileLoaderDataServiceAsync fileLoaderService = getFileLoaderDataService();
		LoaderConfigWeb configuration = (LoaderConfigWeb) event.getData();
		fileLoaderService.saveFileLoaderConfigurationData(configuration, (new AsyncCallback<String>() {
	      public void onFailure(Throwable caught) {
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}		 	    	  
		        Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(String message) {
	    	  if (message == null || message.length() == 0) {
	    		  Info.display("Information", "File loader configuration data was saved successfully.");
	    	  } else {
	    		  Info.display("Information", "Failed to save file loader configuration data: " + message);
	    	  }
	      }
	    }));
	}

	private void requestFileLoaderConfigurationData() {
		FileLoaderDataServiceAsync fileLoaderService = getFileLoaderDataService();
		fileLoaderService.loadFileLoaderConfigurationData(new AsyncCallback<LoaderConfigWeb>() {
	      public void onFailure(Throwable caught) {
	    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}		 
				Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(LoaderConfigWeb result) {
	    	  forwardToView(fileLoaderConfigurationView, AppEvents.FileLoaderConfigurationReceived, result);
	      }
	    });
	}

	private FileLoaderDataServiceAsync getFileLoaderDataService() {
		if (fileLoaderService == null) {
			fileLoaderService = (FileLoaderDataServiceAsync) Registry.get(Constants.FILE_LOADER_DATA_SERVICE);
		}
		return fileLoaderService;
	}
}
