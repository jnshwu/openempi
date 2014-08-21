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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.BasicSearchCriteriaWeb;
import org.openempi.webapp.client.model.IdentifierDomainTypeCodeWeb;
import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.PermissionWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.mvc.View;
import org.openhie.openempi.model.Permission;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

public class BasicSearchView extends View
{
	private Grid<PersonWeb> grid;
	private ListStore<PersonWeb> personStore = new ListStore<PersonWeb>();

	private LayoutContainer container;
	private LayoutContainer gridContainer;
	private Status status;
	private Button searchButton;
	private Button resetSearchButton;

	private PersonWeb updatePerson = null;	
	
	private TextField<String> identifier;
	private ComboBox<IdentifierDomainWeb> identifierDomains;
	private ComboBox<IdentifierDomainTypeCodeWeb> listIdentifierTypes;

	private Map<String,PermissionWeb> permissions = null;
	
	public BasicSearchView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.BasicSearchView ||
			event.getType() == AppEvents.BasicDeletePersonCancel || event.getType() == AppEvents.BasicUpdatePersonCancel ||
			event.getType() == AppEvents.BasicDeletePersonFinished || event.getType() == AppEvents.BasicUpdatePersonFinished ) {
			grid = null;
			initUI();
			
			// get basic search criteria from Cache
			if( Registry.get(Constants.BASIC_SEARCH_CRITERIA) != null ) {
				setCriteriaToView( (BasicSearchCriteriaWeb) Registry.get(Constants.BASIC_SEARCH_CRITERIA) );				
			}
			
			if (event.getType() == AppEvents.BasicDeletePersonFinished ) {
		    	PersonWeb person = event.getData();
				
				// remove person from Cache				
//				List<PersonWeb> personList =  (List<PersonWeb>) getController().getFromCache(Constants.BASIC_SEARCH_LIST);
		    	List<PersonWeb> personList = Registry.get(Constants.BASIC_SEARCH_LIST);
				personList.remove(person);	
//				getController().setToCache(Constants.BASIC_SEARCH_LIST, personList);
			    Registry.register(Constants.BASIC_SEARCH_LIST, personList);				
				displayRecords( personList );
				
				// remove person from Advanced search Cache if the person in the Advanced search list
//				List<PersonWeb> advancedPersonList =  (List<PersonWeb>) getController().getFromCache(Constants.ADVANCED_SEARCH_LIST);
		    	List<PersonWeb> advancedPersonList = Registry.get(Constants.ADVANCED_SEARCH_LIST);
				for (PersonWeb personWeb : advancedPersonList) {
					if( personWeb.getPersonId().intValue() == person.getPersonId().intValue() ) {
						advancedPersonList.remove(personWeb);
						break;
					}
				}
//				getController().setToCache(Constants.ADVANCED_SEARCH_LIST, advancedPersonList);	
			    Registry.register(Constants.ADVANCED_SEARCH_LIST, advancedPersonList);		
				
			} else if (event.getType() == AppEvents.BasicUpdatePersonFinished ) {				
		    	PersonWeb updatedperson = event.getData();	
				
//				List<PersonWeb> personList =  (List<PersonWeb>) getController().getFromCache(Constants.BASIC_SEARCH_LIST);
		    	List<PersonWeb> personList = Registry.get(Constants.BASIC_SEARCH_LIST);
				personList.remove(updatePerson);	
				personList.add(updatedperson);	
//				getController().setToCache(Constants.BASIC_SEARCH_LIST, personList);
			    Registry.register(Constants.BASIC_SEARCH_LIST, personList);						
				displayRecords( personList );
				
				// update person in Advanced search Cache if the person in the Advanced search list				
//				List<PersonWeb> advancedPersonList =  (List<PersonWeb>) getController().getFromCache(Constants.ADVANCED_SEARCH_LIST);
		    	List<PersonWeb> advancedPersonList = Registry.get(Constants.ADVANCED_SEARCH_LIST);
				for (PersonWeb personWeb : advancedPersonList) {
					if( personWeb.getPersonId().intValue() == updatePerson.getPersonId().intValue() ) {
						advancedPersonList.remove(personWeb);
						advancedPersonList.add(updatedperson);	
						break;
					}
				}		
//				getController().setToCache(Constants.ADVANCED_SEARCH_LIST, advancedPersonList);
			    Registry.register(Constants.ADVANCED_SEARCH_LIST, advancedPersonList);	
			    
			} else {
				// get basic person list from Cache
//				if( getController().getFromCache(Constants.BASIC_SEARCH_LIST) != null ) {
//					displayRecords( (List<PersonWeb>) getController().getFromCache(Constants.BASIC_SEARCH_LIST) );				
//				}		
				if( Registry.get(Constants.BASIC_SEARCH_LIST) != null ) {
					displayRecords( (List<PersonWeb>) Registry.get(Constants.BASIC_SEARCH_LIST) );						
				}
			}
			
		} else if (event.getType() == AppEvents.BasicSearchRenderData) {
			grid = null;
			initUI();
			
			// get basic search criteria from Cache
			if( Registry.get(Constants.BASIC_SEARCH_CRITERIA) != null ) {
				setCriteriaToView( (BasicSearchCriteriaWeb) Registry.get(Constants.BASIC_SEARCH_CRITERIA) );				
			}
			
			displayRecords((List<PersonWeb>) event.getData());
		}
	}

	private void setCriteriaToView(BasicSearchCriteriaWeb searchCriteria) {
		identifier.setValue(searchCriteria.getIdentifier());
		identifierDomains.setValue(searchCriteria.getIdentifierDomain());
		listIdentifierTypes.setValue(searchCriteria.getUniversalIdentifierTypeCode());	
	}
	
	private void displayRecords(List<PersonWeb> persons) {
		if (grid == null) {
			setupPersonGrid();
		}
		personStore.removeAll();
		personStore.add(persons);
		container.layout();
		status.hide();
		searchButton.unmask();
	}

	private void clearSearch() {
		identifier.clear();
		identifierDomains.clear();
		listIdentifierTypes.clear();
	}
	
	private void setupPersonGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		XTemplate tpl = XTemplate.create(getTemplate());
		GWT.log("Maximum depth is " + tpl.getMaxDepth(), null);
		RowExpander expander = new RowExpander();
		expander.setTemplate(tpl);
   
		configs.add(expander);
		
		ColumnConfig column = new ColumnConfig();
		column.setId("givenName");  
		column.setHeader("First Name");
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("familyName");  
		column.setHeader("Last Name");  
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("dateOfBirth");  
		column.setHeader("Date of Birth");
		column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
		column.setWidth(100);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("gender");  
		column.setHeader("Gender");
		column.setWidth(60);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("address1");  
		column.setHeader("Address");  
		column.setWidth(200);
		configs.add(column);
		
		
		column = new ColumnConfig();
		column.setId("city");  
		column.setHeader("City");  
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("state");  
		column.setHeader("State");  
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("postalCode");  
		column.setHeader("Zip Code");  
		column.setWidth(100);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("country");
		column.setHeader("Country");
		column.setWidth(150);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);

		grid = new Grid<PersonWeb>(personStore, cm);
		grid.setStyleAttribute("borderTop", "none");
//		grid.setAutoExpandColumn("givenName");
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		grid.addPlugin(expander);
		
	    Menu updateMenu = new Menu();
	    MenuItem menuItemUpdate = new MenuItem("Update", IconHelper.create("images/update.png"), new SelectionListener<MenuEvent>() {
	          @Override
	          public void componentSelected(MenuEvent ce) {
	        	  	updatePerson = grid.getSelectionModel().getSelectedItem();
					if( updatePerson != null ) {
	
						controller.handleEvent(new AppEvent(AppEvents.BasicUpdatePersonView, updatePerson));									
					}									
	          }
	        });
	    updateMenu.add(menuItemUpdate);

	    MenuItem menuItemDelete = new MenuItem("Delete", IconHelper.create("images/delete.png"), new SelectionListener<MenuEvent>() {
	          @Override
	          public void componentSelected(MenuEvent ce) {
					PersonWeb person = grid.getSelectionModel().getSelectedItem();
					if( person != null ) {
						controller.handleEvent(new AppEvent(AppEvents.BasicDeletePersonView, person));									
					}				
	          }
	        });
	    updateMenu.add(menuItemUpdate);
	    updateMenu.add(menuItemDelete);	    

	    // check permissions
	    if( permissions != null ) {
		    PermissionWeb permission = permissions.get(Permission.RECORD_EDIT);
			if (permission == null) {
				menuItemUpdate.disable();
			}
	
		    permission = permissions.get(Permission.RECORD_DELETE);
			if (permission == null) {
				menuItemDelete.disable();
			}
	    }
		
		grid.setContextMenu(updateMenu);
		
		gridContainer.add(grid);
	}

	private String getTemplate() {
		return "<p class=\"identifierBlock\">" +
				"<b>Person Identifiers:</b><br>" +
				"<table class=\"identifierTable\"> " +
				"<tr>" +
					"<th class=\"identifierColumn\">Identifier</th>" +
					"<th class=\"identifierDomainNameColumn\">Domain Name</th>" +
					"<th class=\"namespaceColumn\">Namespace Identifier</th>" +
					"<th class=\"universalIdentifierColumn\">Universal Identifier</th>" +
					"<th class=\"universalIdentifierTypeColumn\">Universal Identifier Type</th>" +
				"</tr> " +
				"<tpl for=\"personIdentifiers\"> " +
					"<tr> " +
					"<td>{identifier}</td><td>{identifierDomainName}</td><td>{namespaceIdentifier}</td><td>{universalIdentifier}</td><td>{universalIdentifierTypeCode}</td>" +
					"</tr> " +
				"</tpl>" +
				"<table" +
				"</p>";
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		FormData formData = new FormData("100%");
		
		permissions = Registry.get(Constants.LOGIN_USER_PERMISSIONS);   
		
		container = new LayoutContainer();
		container.setLayout(new BorderLayout());
		
		FormPanel panel = new FormPanel();
		panel.setFrame(true);  
		panel.setHeaderVisible(false);
		panel.setLabelAlign(LabelAlign.TOP);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setBorders(true);
   
		LayoutContainer main = new LayoutContainer();  
		main.setLayout(new ColumnLayout());

		LayoutContainer left = new LayoutContainer();
		left.setStyleAttribute("paddingRight", "10px");
		FormLayout layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);  
		left.setLayout(layout);

//		final TextField<String> identifier = new TextField<String>();
		identifier = new TextField<String>();
		identifier.setFieldLabel("Identifier");
		left.add(identifier, formData);

		LayoutContainer right = new LayoutContainer();
		right.setStyleAttribute("paddingLeft", "10px");
		layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);
		right.setLayout(layout);

		ListStore<IdentifierDomainWeb> domains = new ListStore<IdentifierDomainWeb>();
		List<IdentifierDomainWeb> domainEntries = Registry.get(Constants.IDENTITY_DOMAINS);
		domains.add(domainEntries);		
		
		identifierDomains = new ComboBox<IdentifierDomainWeb>();
		identifierDomains.setFieldLabel("Identifier Domain");
		identifierDomains.setEmptyText("Select a identifier domain name...");				
		identifierDomains.setDisplayField("identifierDomainName");
		identifierDomains.setValueField("identifierDomainName");
		identifierDomains.setStore(domains);
		right.add(identifierDomains, formData);		

		ListStore<IdentifierDomainTypeCodeWeb> store = new ListStore<IdentifierDomainTypeCodeWeb>();
		List<IdentifierDomainTypeCodeWeb> idTypeCodes = Registry.get(Constants.IDENTITY_DOMAIN_TYPE_CODES);
		store.add(idTypeCodes);
		
		listIdentifierTypes = new ComboBox<IdentifierDomainTypeCodeWeb>();
		listIdentifierTypes.setFieldLabel("Identifier Type");
		listIdentifierTypes.setEmptyText("Select a universal identifier type...");				
		listIdentifierTypes.setDisplayField("universalIdentifierTypeCode");
		listIdentifierTypes.setValueField("universalIdentifierTypeCode");
		listIdentifierTypes.setStore(store);
		right.add(listIdentifierTypes, formData);		
		
		main.add(left, new ColumnData(.5));  
		main.add(right, new ColumnData(.5));
		panel.add(main, new FormData("60%"));
		searchButton = new Button("Search", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {
				String id = identifier.getValue();
				IdentifierDomainTypeCodeWeb idType = null;
				if (listIdentifierTypes.getSelection().size() > 0) {
					idType = listIdentifierTypes.getSelection().get(0);
				}
				
				IdentifierDomainWeb domain = null;
				if (identifierDomains.getSelection().size() > 0) {
					domain = identifierDomains.getSelection().get(0);
				}				

				BasicSearchCriteriaWeb searchCriteria = new BasicSearchCriteriaWeb(id, domain, idType);
				if (id == null) {
					Info.display("Warning", "You must enter at least a partial identifier before pressing the search button.");
					return;
				}

				if (id.trim().equals("%")) {
					Info.display("Warning", "You must provide at least a partial identifier in a search.");
					return;
				}		
				
				// Info.display("Information", "The basic search criteria is: " + searchCriteria);
				controller.handleEvent(new AppEvent(AppEvents.BasicSearchInitiate, searchCriteria));
				status.show();
				searchButton.mask();
			}
		});
		
		resetSearchButton = new Button("Reset", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {
				clearSearch();
			}
		});
		
		status = new Status();
		status.setBusy("please wait...");
		panel.getButtonBar().add(status);
		status.hide();
		
		panel.getButtonBar().setSpacing(15);
	    panel.getButtonBar().add(new FillToolItem());
		panel.getButtonBar().add(searchButton);
		panel.getButtonBar().add(resetSearchButton);
		container.add(panel, new BorderLayoutData(LayoutRegion.NORTH, 39));
		
		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(122, 2, 2, 2));
		container.add(gridContainer, data);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
}
