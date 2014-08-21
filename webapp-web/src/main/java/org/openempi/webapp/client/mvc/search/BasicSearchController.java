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
package org.openempi.webapp.client.mvc.search;

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.BasicSearchCriteriaWeb;
import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonLinkWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.extjs.gxt.ui.client.widget.Info;

public class BasicSearchController extends Controller
{
	private BasicSearchView basicSearchView;
	private TemplateSearchView templateSearchView;
	private UpdatePersonView updatePersonView;
	private DeletePersonView deletePersonView;
	private UnlinkPersonsView unlinkPersonsView;
	
	public BasicSearchController() {
		this.registerEventTypes(AppEvents.AdvancedSearchView);
		this.registerEventTypes(AppEvents.AdvancedSearchInitiate);
		this.registerEventTypes(AppEvents.AdvancedUpdatePersonView);
		this.registerEventTypes(AppEvents.AdvancedDeletePersonView);
		this.registerEventTypes(AppEvents.BasicSearchView);
		this.registerEventTypes(AppEvents.BasicSearchInitiate);
		this.registerEventTypes(AppEvents.BasicUpdatePersonView);
		this.registerEventTypes(AppEvents.BasicDeletePersonView);
		
		this.registerEventTypes(AppEvents.AddToUpdatePersonView);
		
		this.registerEventTypes(AppEvents.UnlinkPersonsView);
	}
	
	public void initialize() {
		basicSearchView = new BasicSearchView(this);
		templateSearchView = new TemplateSearchView(this);
		updatePersonView = new UpdatePersonView(this);
		deletePersonView =  new DeletePersonView(this);
		unlinkPersonsView =  new UnlinkPersonsView(this);
	}
	
	public void search(PersonWeb searchPerson) {
  	  	// set advanced person search Criteria to Cache
		Registry.register(Constants.ADVANCED_SEARCH_CRITERIA, searchPerson);

	    getPersonDataService().getPersonsByAttribute(searchPerson, new AsyncCallback<List<PersonWeb>>() {
	      public void onFailure(Throwable caught) {
//	  		setToCache(Constants.ADVANCED_SEARCH_LIST, null);
	    	  
	    	  if (caught instanceof AuthenticationException) {
	    		  Dispatcher.get().dispatch(AppEvents.Logout);
	    		  return;
	    	  }		    
	    	  Registry.register(Constants.ADVANCED_SEARCH_LIST, null);	
	    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(List<PersonWeb> result) {
	    	  GWT.log("Result has " + result.size() + " records.", null);
	    	  
	    	  // set advanced person list to Cache
//	    	  setToCache(Constants.ADVANCED_SEARCH_LIST, result);
			  Registry.register(Constants.ADVANCED_SEARCH_LIST, result);	
	    	  
/*	    	  for (PersonWeb person : result) {
				GWT.log("Found a person " + person.getGivenName() +  " state: " + person.getState() + ", zip-code: " + person.getPostalCode(), null);
				for (PersonIdentifierWeb pi : person.getPersonIdentifiers()) {
					GWT.log("Person identifier: " + pi.getIdentifier() + ", " + pi.getNamespaceIdentifier() + ", " + pi.getUniversalIdentifier(), null);
				}
	    	  }
*/
	    	  forwardToView(templateSearchView, AppEvents.AdvancedSearchRenderData, result);
	      }
	    });	
	}

	public void searchFuzzyMatch(PersonWeb searchPerson) {
  	  	// set advanced person search Criteria to Cache
		Registry.register(Constants.ADVANCED_SEARCH_CRITERIA, searchPerson);
		  
	    getPersonDataService().getMatchingPersons(searchPerson, new AsyncCallback<List<PersonWeb>>() {
	      public void onFailure(Throwable caught) {
//	  		setToCache(Constants.ADVANCED_SEARCH_LIST, null);
	    
	    	  if (caught instanceof AuthenticationException) {
	    		  Dispatcher.get().dispatch(AppEvents.Logout);
	    		  return;
	    	  }		    
	    	  Registry.register(Constants.ADVANCED_SEARCH_LIST, null);	
	    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(List<PersonWeb> result) {
	    	  GWT.log("Result has " + result.size() + " records.", null);
	    	  
	    	  // set advanced person list to Cache
//	    	  setToCache(Constants.ADVANCED_SEARCH_LIST, result);
		      Registry.register(Constants.ADVANCED_SEARCH_LIST, result);	
	    	  forwardToView(templateSearchView, AppEvents.AdvancedSearchRenderData, result);
	      }
	    });		
	}
	
	public void search(BasicSearchCriteriaWeb search) {
 	  	// set basic person search Criteria to Cache
		// setToCache(Constants.BASIC_SEARCH_CRITERIA, search);
		Registry.register(Constants.BASIC_SEARCH_CRITERIA, search);
		
		final PersonIdentifierWeb personIdentifier = new PersonIdentifierWeb();
		personIdentifier.setIdentifier(search.getIdentifier());
		if (search.getUniversalIdentifierTypeCode() != null && search.getUniversalIdentifierTypeCode().getUniversalIdentifierTypeCode().length() > 0 ||
			search.getIdentifierDomain() != null && search.getIdentifierDomain().getIdentifierDomainName().length() > 0) {
						
			IdentifierDomainWeb domain = new IdentifierDomainWeb();
			if( search.getIdentifierDomain() != null && search.getIdentifierDomain().getIdentifierDomainName().length() >0 )
				domain.setIdentifierDomainName(search.getIdentifierDomain().getIdentifierDomainName());
			
			if( search.getUniversalIdentifierTypeCode() != null && search.getUniversalIdentifierTypeCode().getUniversalIdentifierTypeCode().length() > 0 )
				domain.setUniversalIdentifierTypeCode(search.getUniversalIdentifierTypeCode().getUniversalIdentifierTypeCode());
			
			personIdentifier.setIdentifierDomain(domain);
		}
		
	    getPersonDataService().getPersonsByIdentifier(personIdentifier, new AsyncCallback<List<PersonWeb>>() {
	      public void onFailure(Throwable caught) {
//		  	setToCache(Constants.BASIC_SEARCH_LIST, null);
	    	  
	    	  if (caught instanceof AuthenticationException) {
	    		  Dispatcher.get().dispatch(AppEvents.Logout);
	    		  return;
	    	  }		    
	    	  Registry.register(Constants.BASIC_SEARCH_LIST, null);
	    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
	      }

	      public void onSuccess(List<PersonWeb> result) {
	    	  GWT.log("Result has " + result.size() + " records.", null);
	    	  
	    	  // set basic person list to Cache
//	  		  setToCache(Constants.BASIC_SEARCH_LIST, result);
		      Registry.register(Constants.BASIC_SEARCH_LIST, result);	  		  
	    	  for (PersonWeb person : result) {
				GWT.log("Found a person " + person.getGivenName() +  " state: " + person.getState() + ", zip-code: " + person.getPostalCode(), null);
				for (PersonIdentifierWeb pi : person.getPersonIdentifiers()) {
					GWT.log("Person identifier: " + pi.getIdentifier() + ", " + pi.getNamespaceIdentifier() + ", " + pi.getUniversalIdentifier(), null);
				}
	    	  }
	    	  forwardToView(basicSearchView, AppEvents.BasicSearchRenderData, result);
//	    	  Info.display("Information", "We've got the persons: " + result);
	      }
	    });		
	}

	public void basicUpdatePersonRenderData(PersonWeb person) {
		forwardToView(updatePersonView, AppEvents.BasicUpdatePersonRenderData, person);	
	}
	
	public void basicUpdate(PersonWeb person) {
	    getPersonDataService().updatePerson(person, new AsyncCallback<PersonWeb> () {
		      public void onFailure(Throwable caught) {
		    	//Info.display("Information", "Failure adding person to repository.");		
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;	
		    	  }		    
		    	  forwardToView(updatePersonView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(PersonWeb result) {
		    	  // Info.display("Information", "Result from add operation is message: " + result);		    	  
		    	  forwardToView(updatePersonView, AppEvents.BasicUpdatePersonComplete, result);
		      }
		    });	
	}
	
	public void basicDeletePersonRenderData(PersonWeb person) {
		forwardToView(deletePersonView, AppEvents.BasicDeletePersonRenderData, person);	
	}

	public void basicDelete(PersonWeb person) {	
	    getPersonDataService().deletePerson(person, new AsyncCallback<String> () {
		      public void onFailure(Throwable caught) {
		    	//Info.display("Information", "Failure delete person.");	
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  forwardToView(deletePersonView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(String result) {
		    	  // Info.display("Information", "Result from delete operation is message: " + result);		    	  
		    	  forwardToView(deletePersonView, AppEvents.BasicDeletePersonComplete, result);
		      }
		    });	
	}
	
	public void advancedUpdatePersonRenderData(PersonWeb person) {
		forwardToView(updatePersonView, AppEvents.AdvancedUpdatePersonRenderData, person);	
	}
	
	public void advancedUpdate(PersonWeb person) {	
	    getPersonDataService().updatePerson(person, new AsyncCallback<PersonWeb> () {
		      public void onFailure(Throwable caught) {
		    	//Info.display("Information", "Failure adding person to repository.");	
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  forwardToView(updatePersonView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(PersonWeb result) {
		    	  // Info.display("Information", "Result from add operation is message: " + result);		    	  
		    	  forwardToView(updatePersonView, AppEvents.AdvancedUpdatePersonComplete, result);
		      }
		    });	
	}
	
	public void advancedDeletePersonRenderData(PersonWeb person) {
		forwardToView(deletePersonView, AppEvents.AdvancedDeletePersonRenderData, person);	
	}
	
	public void advancedDelete(PersonWeb person) {	
	    getPersonDataService().deletePerson(person, new AsyncCallback<String> () {
		      public void onFailure(Throwable caught) {
		    	//Info.display("Information", "Failure delete person.");
		    	  
		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  forwardToView(deletePersonView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(String result) {
		    	  // Info.display("Information", "Result from delete operation is message: " + result);		    	  
		    	  forwardToView(deletePersonView, AppEvents.AdvancedDeletePersonComplete, result);
		      }
		    });	
	}
	
	public void checkPersonDuplicate(PersonWeb person) {
		// Info.display("Information", "Submitting request to check person duplicate.");
	    getPersonDataService().getMatchingPersons(person, new AsyncCallback<List<PersonWeb>>() {
		      public void onFailure(Throwable caught) {

		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(List<PersonWeb> result) {
		    	  forwardToView(updatePersonView, AppEvents.CheckDuplicatePersonComplete, result);
		      }
		    });		
	}
	
	public void getPersonLinks(PersonWeb person) {
		// Info.display("Information", "Submitting request to get person links: " + person.getGivenName());
	    getPersonDataService().getPersonLinks(person, new AsyncCallback<List<PersonLinkWeb>>() {
		      public void onFailure(Throwable caught) {

		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
		      }

		      public void onSuccess(List<PersonLinkWeb> result) {
				  forwardToView(unlinkPersonsView, AppEvents.UnlinkPersonsView, result);
		      }
		    });		
	}

	public void unlinkPersons(List<PersonLinkWeb> personLinks) {
		// Info.display("Information", "Submitting request to get person links: " + person.getGivenName());
	    getPersonDataService().unlinkPersons(personLinks, new AsyncCallback<String>() {
		      public void onFailure(Throwable caught) {

		    	  if (caught instanceof AuthenticationException) {
		    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    		  return;
		    	  }		    
		    	  forwardToView(unlinkPersonsView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(String result) {
				  forwardToView(unlinkPersonsView, AppEvents.UnlinkPersonsComplete, result);
		      }
		    });		
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.BasicSearchView) {
			forwardToView(basicSearchView, event);			
/*			if( getFromCache(Constants.BASIC_SEARCH_CRITERIA) == null ) {		   
				forwardToView(basicSearchView, event);
			} else {
				BasicSearchCriteriaWeb searchCriteria = (BasicSearchCriteriaWeb) getFromCache(Constants.BASIC_SEARCH_CRITERIA);					
		    	search(searchCriteria);
			}
*/
		} else if (type == AppEvents.AdvancedSearchView) {
			forwardToView(templateSearchView, event);
/*			if( getFromCache(Constants.ADVANCED_SEARCH_CRITERIA) == null ) {		 
				forwardToView(templateSearchView, event);
			} else {
				PersonWeb searchCriteria = (PersonWeb) getFromCache(Constants.ADVANCED_SEARCH_CRITERIA);					
		    	search(searchCriteria);
			}
*/
		} else if (type == AppEvents.BasicSearchInitiate) {
	    	BasicSearchCriteriaWeb searchCriteria = event.getData();
	    	search(searchCriteria);
		} else if (type == AppEvents.AdvancedSearchInitiate) {
	    	PersonWeb searchCriteria = event.getData();
	    	search(searchCriteria);
		} else if (type == AppEvents.AdvancedSearchFuzzyMatchInitiate) {
	    	PersonWeb searchCriteria = event.getData();
	    	searchFuzzyMatch(searchCriteria);	    	
	    } else if (type == AppEvents.BasicUpdatePersonView) {
	    	PersonWeb person = event.getData();
	    	basicUpdatePersonRenderData(person);
	    } else if (type == AppEvents.BasicUpdatePersonInitiate) {
	    	PersonWeb person = event.getData();
	    	basicUpdate(person);
	    } else if (type == AppEvents.BasicDeletePersonView) {
	    	PersonWeb person = event.getData();
	    	basicDeletePersonRenderData(person);
	    } else if (type == AppEvents.BasicDeletePersonInitiate) {
	    	PersonWeb person = event.getData();
	    	basicDelete(person);	    	
	    } else if (type == AppEvents.BasicDeletePersonFinished || type == AppEvents.BasicUpdatePersonFinished) {
	    	// PersonWeb person = event.getData();
			forwardToView(basicSearchView, event);	    	
	    } else if (type == AppEvents.BasicUpdatePersonCancel || type == AppEvents.BasicDeletePersonCancel) {	    	
/*			// get basic search criteria from Cache, search again
			if( getFromCache(Constants.BASIC_SEARCH_CRITERIA) != null ) {	
				BasicSearchCriteriaWeb searchCriteria = (BasicSearchCriteriaWeb) getFromCache(Constants.BASIC_SEARCH_CRITERIA);					
		    	search(searchCriteria);
			}
*/
			forwardToView(basicSearchView, event);	  
	    } else if (type == AppEvents.AdvancedUpdatePersonView) {
	    	PersonWeb person = event.getData();
	    	advancedUpdatePersonRenderData(person);
	    } else if (type == AppEvents.AdvancedUpdatePersonInitiate) {
	    	PersonWeb person = event.getData();
	    	advancedUpdate(person);
	    } else if (type == AppEvents.AdvancedDeletePersonView) {
	    	PersonWeb person = event.getData();
	    	advancedDeletePersonRenderData(person);
	    } else if (type == AppEvents.AdvancedDeletePersonInitiate) {
	    	PersonWeb person = event.getData();
	    	advancedDelete(person);
	    } else if (type == AppEvents.AdvancedDeletePersonFinished || type == AppEvents.AdvancedUpdatePersonFinished) {
	    	// PersonWeb person = event.getData();
			forwardToView(templateSearchView, event);	    	
	    } else if (type == AppEvents.AdvancedUpdatePersonCancel || type == AppEvents.AdvancedDeletePersonCancel) {

/*			// get advanced search criteria from Cache, search again
			if( getFromCache(Constants.ADVANCED_SEARCH_CRITERIA) != null ) {	
				PersonWeb searchCriteria = (PersonWeb) getFromCache(Constants.ADVANCED_SEARCH_CRITERIA);					
		    	search(searchCriteria);
			}
*/
			forwardToView(templateSearchView, event);	
			
		} else if (type == AppEvents.CheckDuplicatePersonInitiate) {
			
	    	PersonWeb personWeb = event.getData();
	    	checkPersonDuplicate(personWeb);
	    	
		} else if (type == AppEvents.AddToUpdatePersonView) {
			
	    	PersonWeb personWeb = event.getData();
			forwardToView(updatePersonView, AppEvents.AddToUpdatePersonRenderData, personWeb);	
			
		} else if (type == AppEvents.UnlinkPersonsView) {
			
	    	PersonWeb person = event.getData();
	    	getPersonLinks(person);
	} else if (type == AppEvents.UnlinkPersons) {
			
			List<PersonLinkWeb> personLinks = event.getData();
	    	unlinkPersons(personLinks);
	    }
	}
}
