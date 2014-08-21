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

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.RoleWeb;
import org.openempi.webapp.client.mvc.Controller;


public class ManageRoleController extends Controller
{
	private ManageRoleView manageRoleView;

	public ManageRoleController() {		
		this.registerEventTypes(AppEvents.ManageRoleView);
	}

	@Override
	protected void initialize() {
		manageRoleView = new ManageRoleView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.ManageRoleView) {
			
			forwardToView(manageRoleView, event);
			
		} else if (type == AppEvents.ManageRoleRequest) {
			
			requestRolesData();
	
		} else if (type == AppEvents.ManageGetUpdateRole) {
			
	    	RoleWeb role = event.getData();
	    	getUpdateRole(role);			

		} else if (type == AppEvents.ManageGetDeleteRole) {
			
	    	RoleWeb role = event.getData();
	    	getDeleteRole(role);
	    	
		} else if (type == AppEvents.ManageRoleAdd) {
			
	    	RoleWeb role = event.getData();
	    	addRole(role);	
		} else if (type == AppEvents.ManageRoleUpdate) {
			
	    	RoleWeb role = event.getData();
	    	updateRole(role);	
		} else if (type == AppEvents.ManageRoleDelete) {
			
	    	RoleWeb role = event.getData();
	    	deleteRole(role);	
		}	    	    	    	
	}

	private void requestRolesData() {
		getUserDataService().getRoles( new AsyncCallback<List<RoleWeb>>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }
				  Registry.register(Constants.ROLE_LIST, null);
		    	  forwardToView(manageRoleView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(List<RoleWeb> result) {	
		    	    //Info.display("Information", "onSuccess:"+result.size());
				  Registry.register(Constants.ROLE_LIST, result);
		    	  forwardToView(manageRoleView, AppEvents.ManageRoleReceived, result);
		      }
		});					
	}
	
	private void getUpdateRole(RoleWeb role) {	
		getUserDataService().getRole(role.getId(), new AsyncCallback<RoleWeb>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }
		    	  forwardToView(manageRoleView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(RoleWeb result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(manageRoleView, AppEvents.RoleUpdateRenderData, result);
		      }
		});					
	}
	
	private void getDeleteRole(RoleWeb role) {
		getUserDataService().getRole(role.getId(), new AsyncCallback<RoleWeb>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }
		    	  forwardToView(manageRoleView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(RoleWeb result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(manageRoleView, AppEvents.RoleDeleteRenderData, result);
		      }
		});					
	}
	
	private void addRole(RoleWeb role) {
		getUserDataService().saveRole(role, new AsyncCallback<RoleWeb>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }	
		    	  forwardToView(manageRoleView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(RoleWeb result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(manageRoleView, AppEvents.ManageRoleAddComplete, result);
		      }
		    });					
	}
	
	private void updateRole(RoleWeb role) {
		getUserDataService().saveRole(role, new AsyncCallback<RoleWeb>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }
		    	  forwardToView(manageRoleView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(RoleWeb result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(manageRoleView, AppEvents.ManageRoleUpdateComplete, result);
		      }
		    });					
	}
	
	private void deleteRole(RoleWeb role) {
		getUserDataService().deleteRole(role, new AsyncCallback<String>() {
		      public void onFailure(Throwable caught) {
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }
		    	  forwardToView(manageRoleView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(String result) {	  
		    	  forwardToView(manageRoleView, AppEvents.ManageRoleDeleteComplete, result);
		      }
		    });					
	}
}
