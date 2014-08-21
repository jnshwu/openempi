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
package org.openempi.webapp.client.mvc.user;

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.UserFileWeb;
import org.openempi.webapp.client.model.FileLoaderConfigurationWeb;
import org.openempi.webapp.client.model.UserWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserFileController extends Controller
{
	private UserFileView userFileView;
	
	public UserFileController() {
		this.registerEventTypes(AppEvents.FileListView);
		this.registerEventTypes(AppEvents.FileListUpdate);
		this.registerEventTypes(AppEvents.FileEntryRemove);
		this.registerEventTypes(AppEvents.FileEntryImport);
	}

	public void initialize() {
		userFileView = new UserFileView(this);
	}	

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.FileListView) {
//			updateUserFileData();
			getFileLoaderConfigurations();
			forwardToView(userFileView, event);
		} else if (type == AppEvents.FileListUpdate) {
			updateUserFileData();			
		} else if (type == AppEvents.FileListUpdateDataProfile) {
			updateUserFileDataProfile();			
		} else if (type == AppEvents.FileEntryRemove) {
			List<UserFileWeb> fileList = event.getData();
			deleteUserFileEntries(fileList);
		} else if (type == AppEvents.FileEntryImport) {
			List<UserFileWeb> fileList = event.getData();
			importUserFileEntries(fileList);
		} else if (type == AppEvents.FileEntryDataProfile) {
			List<UserFileWeb> fileList = event.getData();
			dataProfileFileEntries(fileList);
		}
	}
	
	private void deleteUserFileEntries(List<UserFileWeb> fileEntries) {
		final int deleteTotal = fileEntries.size();

		for (UserFileWeb userFile : fileEntries) {
			getPersonDataService().removeUserFile(userFile.getUserFileId(), new AsyncCallback<Void>() {
				  int count = 0;
			      public void onFailure(Throwable caught) {
//			    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
			    	  
			    	  if (caught instanceof AuthenticationException) {
			    		  Dispatcher.get().dispatch(AppEvents.Logout);
			    		  return;
			    	  }		    
				      forwardToView(userFileView, AppEvents.Error, caught.getMessage());
			      }

			      public void onSuccess(Void value) {
			    	  count++;
			    	  if( count == deleteTotal ) {
			    		  updateUserFileData();
			    	  }
			      }
			    });
		}
//		updateUserFileData();
	}
	
	
	private void importUserFileEntries(List<UserFileWeb> fileEntries) {
  	  	final int fileEntrySize = fileEntries.size();	  	  	
  	  	final int[] failureCount = {0};	  
  	  	final int[] processCount = {0};	
  	  	
		for (UserFileWeb userFile : fileEntries) {
			getPersonDataService().importUserFile(userFile, new AsyncCallback<String>() {

			      public void onFailure(Throwable caught) {	    			  
				      //forwardToView(userFileView, AppEvents.Error, caught.getMessage());
			    	  
			    	  if (caught instanceof AuthenticationException) {
			    		  Dispatcher.get().dispatch(AppEvents.Logout);
			    		  return;
			    	  }		    
			    	  
			    	  failureCount[0]++;
		    		  processCount[0]++;
					  if( processCount[0] == fileEntrySize && failureCount[0] > 0) {
						  String error;
						  if(failureCount[0] == 1 )
							  error = "There are one file import failure.";
						  else
							  error = "There are " +  failureCount[0] +" files import failure.";	
						  
						  forwardToView(userFileView, AppEvents.Error, error);
					  }
			      }

			      public void onSuccess(String value) {
	
		    		  updateUserFileData();
		    		  
		    		  processCount[0]++;
					  if( processCount[0] == fileEntrySize ) {
						  if(failureCount[0] > 0) {
							  if( processCount[0] == fileEntrySize && failureCount[0] > 0) {
								  String error;
								  if(failureCount[0] == 1 )
									  error = "There are one file import failure.";
								  else
									  error = "There are " +  failureCount[0] +" files import failure.";	
								  
								  forwardToView(userFileView, AppEvents.Error, error);
							  }
						  } else {
							 Info.display("Information", value);
							 if( fileEntrySize == 1 )
								 forwardToView(userFileView, AppEvents.FileEntryImportSuccess, "File successfully imported");
							 else
								 forwardToView(userFileView, AppEvents.FileEntryImportSuccess, "Files successfully imported");
						  }
					  }
			      }
			    });
		}
	}
	
	private void dataProfileFileEntries(List<UserFileWeb> fileEntries) {
  	  	
  	  	UserFileWeb userFile = fileEntries.get(0);
  	  	getPersonDataService().dataProfileUserFile(userFile, new AsyncCallback<String>() {

			      public void onFailure(Throwable caught) {	    			  
			    	  
			    	  if (caught instanceof AuthenticationException) {
			    		  Dispatcher.get().dispatch(AppEvents.Logout);
			    		  return;
			    	  }		    
			    	  
				      forwardToView(userFileView, AppEvents.Error, caught.getMessage());
			      }

			      public void onSuccess(String value) {
			    	  
		    		  updateUserFileData();
		    		  
					  forwardToView(userFileView, AppEvents.FileEntryDataProfileSuccess, "Data Profile operation successfully launched");
			      }
			    });
	}
	
	private void getFileLoaderConfigurations() {
	    getPersonDataService().getFileLoaderConfigurations( new AsyncCallback<List<FileLoaderConfigurationWeb>>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
			      forwardToView(userFileView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(List<FileLoaderConfigurationWeb> result) {
		    	  forwardToView(userFileView, AppEvents.FileLoaderConfigurations, result);
		      }
		    });		
		
	}
	
	private void updateUserFileData() {	
		UserWeb loginUser = Registry.get(Constants.LOGIN_USER);	
		if( loginUser != null ) {
		    getPersonDataService().getUserFiles(loginUser.getUsername(), new AsyncCallback<List<UserFileWeb>>() {
			      public void onFailure(Throwable caught) {
	//		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
			    	  
			    	  if (caught instanceof AuthenticationException) {
			    		  Dispatcher.get().dispatch(AppEvents.Logout);
			    		  return;
			    	  }		    
				      forwardToView(userFileView, AppEvents.Error, caught.getMessage());
			      }
	
			      public void onSuccess(List<UserFileWeb> result) {
			    	  forwardToView(userFileView, AppEvents.FileListRender, result);
			      }
			    });		
		}
		
	}
	
	private void updateUserFileDataProfile() {	
		UserWeb loginUser = Registry.get(Constants.LOGIN_USER);	
		if( loginUser != null ) {
		    getPersonDataService().getUserFiles(loginUser.getUsername(), new AsyncCallback<List<UserFileWeb>>() {
			      public void onFailure(Throwable caught) {
			    	  
			    	  if (caught instanceof AuthenticationException) {
			    		  Dispatcher.get().dispatch(AppEvents.Logout);
			    		  return;
			    	  }		    
				      forwardToView(userFileView, AppEvents.Error, caught.getMessage());
			      }
	
			      public void onSuccess(List<UserFileWeb> result) {
			    	  forwardToView(userFileView, AppEvents.FileListRenderDataProfile, result);
			      }
			    });		
		}
		
	}
	
}
