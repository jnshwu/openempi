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
package org.openempi.webapp.client.mvc.edit;

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EditController extends Controller
{
	private AddPersonView addPersonView;
	
	public EditController() {
		this.registerEventTypes(AppEvents.AddPersonView);
		this.registerEventTypes(AppEvents.AddPersonInitiate);
		this.registerEventTypes(AppEvents.AddToUpdatePersonViewCancel);
		this.registerEventTypes(AppEvents.AddToUpdatePersonViewFinished);
		this.registerEventTypes(AppEvents.Error);
	}

	@Override
	protected void initialize() {
		addPersonView = new AddPersonView(this);
	}
	
	public void addPersonToRepository(PersonWeb person) {
		// Info.display("Information", "Submitting request to add person to repository.");
	    getPersonDataService().addPerson(person, new AsyncCallback<String> () {
		      public void onFailure(Throwable caught) {
		    	//Info.display("Information", "Failure adding person to repository.");
		        //Dispatcher.forwardEvent(AppEvents.Error, caught);
		    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}		    	  
		    	forwardToView(addPersonView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(String result) {
		    	  // GWT.log("Result from add operation is message :" + result + ":", null);
		    	  // Info.display("Information", "Result from add operation is message: " + result);
		    	  
		    	  forwardToView(addPersonView, AppEvents.AddPersonComplete, result);
		      }
		    });		
	}

	public void checkPersonDuplicate(PersonWeb person) {
		// Info.display("Information", "Submitting request to check person duplicate.");
/*	    getPersonDataService().getPersonsByAttribute(person, new AsyncCallback<List<PersonWeb>>() {
		      public void onFailure(Throwable caught) {
		        Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(List<PersonWeb> result) {
		    	  // GWT.log("Result has " + result.size() + " records.", null);
		    	  forwardToView(addPersonView, AppEvents.CheckDuplicatePersonComplete, result);
		      }
		    });		
*/
	    getPersonDataService().getMatchingPersons(person, new AsyncCallback<List<PersonWeb>>() {
		      public void onFailure(Throwable caught) {
		    	  
				if (caught instanceof AuthenticationException) {
					Dispatcher.get().dispatch(AppEvents.Logout);
					return;
				}		    
		        Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(List<PersonWeb> result) {
		    	  forwardToView(addPersonView, AppEvents.CheckDuplicatePersonComplete, result);
		      }
		    });		
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.AddPersonView) {
			forwardToView(addPersonView, event);
		} else if (type == AppEvents.AddPersonInitiate) {
	    	PersonWeb personWeb = event.getData();
	    	addPersonToRepository(personWeb);
		} else if (type == AppEvents.CheckDuplicatePersonInitiate) {
	    	PersonWeb personWeb = event.getData();
	    	checkPersonDuplicate(personWeb);
		} else if (type == AppEvents.AddToUpdatePersonViewCancel || type == AppEvents.AddToUpdatePersonViewFinished) {
			forwardToView(addPersonView, event);
	    }	
	}
}
