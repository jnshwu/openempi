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

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProfileController extends Controller
{
	private ProfileView profileView;
	
	public ProfileController() {
		this.registerEventTypes(AppEvents.ProfileView);
		this.registerEventTypes(AppEvents.Error);
	}

	@Override
	protected void initialize() {
		profileView = new ProfileView(this);
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.ProfileView) {
			
			forwardToView(profileView, event);
			
	    } else if (type == AppEvents.ProfileUpdateInitiate) {
	    	
	    	UserWeb user = event.getData();
	    	updateUserProfile(user);
	    } else if (type == AppEvents.ProfileVarifyPasswordInitiate) {
	    	
	    	UserWeb user = event.getData();
	    	verifyUserPassword(user);
	    }
	}
		
	public void updateUserProfile(UserWeb user) {
		getUserDataService().saveUser(user, new AsyncCallback<UserWeb>() {
			public void onFailure(Throwable caught) {
			    //Info.display("Information", "Failure:"+caught.getMessage());
			    //Dispatcher.forwardEvent(AppEvents.Error, caught);
		    	forwardToView(profileView, AppEvents.Error, caught.getMessage());
			}

			public void onSuccess(UserWeb result) {	  
			    //Info.display("Information", "onSuccess:"+result.getId());
			    forwardToView(profileView, AppEvents.ProfileUpdateComplete, result);
			}
		});					
	}
	
	public void verifyUserPassword(UserWeb user) {
		getUserDataService().authenticateUser(user.getUsername(),user.getPassword(), true, new AsyncCallback<UserWeb>() {
		      public void onFailure(Throwable caught) {
		   
		    	  // Dispatcher.forwardEvent(AppEvents.Error, caught);
		    	  forwardToView(profileView, AppEvents.ProfileVarifyPasswordFailure, caught.getMessage());		        
		      }

		      public void onSuccess(UserWeb result) {	  
		    	  //Info.display("Information", "onSuccess:"+result.getId());
		    	  forwardToView(profileView, AppEvents.ProfileVarifyPasswordSuccess, result);
		      }
		    });						
	}
}
