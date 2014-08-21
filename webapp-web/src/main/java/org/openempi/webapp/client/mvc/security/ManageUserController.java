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
package org.openempi.webapp.client.mvc.security;

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.UserWeb;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.domain.AuthenticationException;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;




public class ManageUserController extends Controller
{
	private ManageUserView manageUserView;
	private UpdateUserView updateUserView;
	
	public ManageUserController() {

		this.registerEventTypes(AppEvents.ManageUserView);
	}
	
	public void initialize() {
		manageUserView = new ManageUserView(this);
		updateUserView = new UpdateUserView(this);
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.ManageUserView) {
			
			retrieveUserRecords();
			
	    } else if (type == AppEvents.UserAddView) {
	    	UserWeb user = event.getData();
	    	addUserRenderData(user);

	    } else if (type == AppEvents.UserAddInitiate) {
	    	
	    	UserWeb user = event.getData();
	    	addUser(user);
	    	
	    } else if (type == AppEvents.UserUpdateView) {
	    	
	    	UserWeb user = event.getData();
	    	updateUserRenderData(user);
	    	
	    } else if (type == AppEvents.UserUpdateInitiate) {
	    	
	    	UserWeb user = event.getData();
	    	updateUser(user);
	    	
	    } else if (type == AppEvents.UserDeleteView) {    	
	    	
	    	UserWeb user = event.getData();
	    	deleteUserRenderData(user);
	    	
	    } else if (type == AppEvents.UserDeleteInitiate) {
	    	
	    	UserWeb user = event.getData();
	    	deleteUser(user);
	    	
	    } else if (type == AppEvents.UserAddFinished || type == AppEvents.UserUpdateFinished || type == AppEvents.UserDeleteFinished ||
	    		   type == AppEvents.UserUpdateViewCancel ) {
	    	
			forwardToView(manageUserView, event);	
	    }
	}
	
	public void retrieveUserRecords() {
		getUserDataService().getUsers( new AsyncCallback<List<UserWeb>>() {
		      public void onFailure(Throwable caught) {
		    	//Info.display("Information", "Failure:"+caught.getMessage());
		        //Dispatcher.forwardEvent(AppEvents.Error, caught);
		    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
				Registry.register(Constants.MANAGE_USER_LIST, null);
				forwardToView(manageUserView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(List<UserWeb> result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.size());
				  Registry.register(Constants.MANAGE_USER_LIST, result);
		    	  forwardToView(manageUserView, AppEvents.ManageUserView, result);
		      }
		    });					
	}

	public void addUserRenderData(UserWeb user) {
		forwardToView(updateUserView, AppEvents.UserAddRenderData, user);	
	}
	
	public void addUser(UserWeb user) {
		getUserDataService().saveUser(user, new AsyncCallback<UserWeb>() {
		      public void onFailure(Throwable caught) {
		    	//Info.display("Information", "Failure:"+caught.getMessage());
		        //Dispatcher.forwardEvent(AppEvents.Error, caught);
		    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
				forwardToView(updateUserView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(UserWeb result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(updateUserView, AppEvents.UserAddComplete, result);
		      }
		    });					
	}
	
	public void updateUserRenderData(UserWeb user) {
		
		// forwardToView(updateUserView, AppEvents.UserUpdateRenderData, user);			
		getUserDataService().getUserByUsername(user.getUsername(), new AsyncCallback<UserWeb>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  	if (caught instanceof AuthenticationException) {
						Dispatcher.get().dispatch(AppEvents.Logout);
						return;
					}
		    	  	forwardToView(updateUserView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(UserWeb result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(updateUserView, AppEvents.UserUpdateRenderData, result);
		      }
		});					
	}
	
	public void updateUser(UserWeb user) {
		getUserDataService().saveUser(user, new AsyncCallback<UserWeb>() {
		      public void onFailure(Throwable caught) {
		    	//Info.display("Information", "Failure:"+caught.getMessage());
		        //Dispatcher.forwardEvent(AppEvents.Error, caught);
		    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}
				forwardToView(updateUserView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(UserWeb result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(updateUserView, AppEvents.UserUpdateComplete, result);
		      }
		    });					
	}
	
	public void deleteUserRenderData(UserWeb user) {
		// forwardToView(updateUserView, AppEvents.UserDeleteRenderData, user);	
		getUserDataService().getUserByUsername(user.getUsername(), new AsyncCallback<UserWeb>() {
		      public void onFailure(Throwable caught) {
		    	  
					if (caught instanceof AuthenticationException) {
						Dispatcher.get().dispatch(AppEvents.Logout);
						return;
					}
					forwardToView(updateUserView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(UserWeb result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(updateUserView, AppEvents.UserDeleteRenderData, result);
		      }
		});					
	}
	
	public void deleteUser(UserWeb user) {
		getUserDataService().deleteUser(user, new AsyncCallback<String>() {
		      public void onFailure(Throwable caught) {
		    	//Info.display("Information", "Failure:"+caught.getMessage());
		        //Dispatcher.forwardEvent(AppEvents.Error, caught);
		    
					if (caught instanceof AuthenticationException) {
						Dispatcher.get().dispatch(AppEvents.Logout);
						return;
					}
					forwardToView(updateUserView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(String result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(updateUserView, AppEvents.UserDeleteComplete, result);
		      }
		    });					
	}
}
