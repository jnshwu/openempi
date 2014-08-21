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
import java.util.Set;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.LinkedPersonWeb;
import org.openempi.webapp.client.model.PermissionWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.mvc.View;
import org.openempi.webapp.client.ui.util.InputFormData;
import org.openempi.webapp.client.ui.util.Utility;
import org.openempi.webapp.resources.client.model.Gender;
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
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
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

public class TemplateSearchView extends View
{
	private Grid<PersonWeb> grid;
	private ListStore<PersonWeb> personStore = new ListStore<PersonWeb>();
	ListStore<Gender> genders = new ListStore<Gender>();

	private LayoutContainer container;
	private LayoutContainer gridContainer;
	private Status status;
	private Button searchButton;
	private Button resetSearchButton;
	private PersonWeb updatePerson = null;	
	
	private TextField<String> givenName;
	private TextField<String> familyName;
	private DateField dateOfBirth;
	private ComboBox<Gender> gender;
	private CheckBox checkFuzzyMatch;

	private Map<String,PermissionWeb> permissions = null;
	
	public TemplateSearchView(Controller controller) {
		super(controller);
		genders.add(InputFormData.getGenders());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.AdvancedSearchView || 
			event.getType() == AppEvents.AdvancedDeletePersonCancel || event.getType() == AppEvents.AdvancedUpdatePersonCancel ||
			event.getType() == AppEvents.AdvancedDeletePersonFinished || event.getType() == AppEvents.AdvancedUpdatePersonFinished) {
			grid = null;
			initUI();
			
			// get advanced search criteria from Cache
			if( Registry.get(Constants.ADVANCED_SEARCH_CRITERIA) != null ) {
				setCriteriaToView( (PersonWeb) Registry.get(Constants.ADVANCED_SEARCH_CRITERIA) );				
			}
			if( Registry.get(Constants.ADVANCED_SEARCH_FUZZY_MATCH) != null ) {
				checkFuzzyMatch.setValue( (Boolean)Registry.get(Constants.ADVANCED_SEARCH_FUZZY_MATCH) );				
			}
			
			if (event.getType() == AppEvents.AdvancedDeletePersonFinished ) {
		    	PersonWeb person = event.getData();
				
				// remove person from Cache
//				List<PersonWeb> personList =  (List<PersonWeb>) getController().getFromCache(Constants.ADVANCED_SEARCH_LIST);
		    	List<PersonWeb> personList = Registry.get(Constants.ADVANCED_SEARCH_LIST);
				personList.remove(person);	
//				getController().setToCache(Constants.ADVANCED_SEARCH_LIST, personList);
			    Registry.register(Constants.ADVANCED_SEARCH_LIST, personList);		
				
				displayRecords( personList );								
				
				// remove person from Basic search Cache if the person in the Basic search list
//				List<PersonWeb> basicPersonList =  (List<PersonWeb>) getController().getFromCache(Constants.BASIC_SEARCH_LIST);
		    	List<PersonWeb> basicPersonList = Registry.get(Constants.BASIC_SEARCH_LIST);
				for (PersonWeb personWeb : basicPersonList) {
					if( personWeb.getPersonId().intValue() == person.getPersonId().intValue() ) {
						basicPersonList.remove(personWeb);
						break;
					}
				}
//				getController().setToCache(Constants.BASIC_SEARCH_LIST, basicPersonList);	
			    Registry.register(Constants.BASIC_SEARCH_LIST, basicPersonList);			
				
			} else if (event.getType() == AppEvents.AdvancedUpdatePersonFinished ) {
		    	PersonWeb updatedperson = event.getData();	
		    	updatedperson.setLinkedPersons(updatePerson.getLinkedPersons());
		    	
//				List<PersonWeb> personList =  (List<PersonWeb>) getController().getFromCache(Constants.ADVANCED_SEARCH_LIST);
		    	List<PersonWeb> personList = Registry.get(Constants.ADVANCED_SEARCH_LIST);
				personList.remove(updatePerson);	
				personList.add(updatedperson);	
//				getController().setToCache(Constants.ADVANCED_SEARCH_LIST, personList);
			    Registry.register(Constants.ADVANCED_SEARCH_LIST, personList);	
			    
				displayRecords( personList );		
				
				// update person in Basic search Cache if the person in the Basic search list		
//				List<PersonWeb> basicPersonList =  (List<PersonWeb>) getController().getFromCache(Constants.BASIC_SEARCH_LIST);		
		    	List<PersonWeb> basicPersonList = Registry.get(Constants.BASIC_SEARCH_LIST);
				for (PersonWeb personWeb : basicPersonList) {
					if( personWeb.getPersonId().intValue() == updatePerson.getPersonId().intValue() ) {
						basicPersonList.remove(personWeb);
						basicPersonList.add(updatedperson);	
						break;
					}
				}		
//				getController().setToCache(Constants.BASIC_SEARCH_LIST, basicPersonList);
			    Registry.register(Constants.BASIC_SEARCH_LIST, basicPersonList);	
			    
			} else if (event.getType() == AppEvents.AdvancedUpdatePersonCancel ) {
				
				if( event.getData() == null ) {  // search again
					
					Boolean fuzzyMatch = (Boolean) Registry.get(Constants.ADVANCED_SEARCH_FUZZY_MATCH);
					PersonWeb searchCriteria = (PersonWeb) Registry.get(Constants.ADVANCED_SEARCH_CRITERIA);

					if( fuzzyMatch.booleanValue() == true ) {
						controller.handleEvent(new AppEvent(AppEvents.AdvancedSearchFuzzyMatchInitiate, searchCriteria));
					} else {
						controller.handleEvent(new AppEvent(AppEvents.AdvancedSearchInitiate, searchCriteria));					
					}
					status.show();
					searchButton.mask();	
				} else {
					if(  Registry.get(Constants.ADVANCED_SEARCH_LIST) != null ) {
						displayRecords( (List<PersonWeb>) Registry.get(Constants.ADVANCED_SEARCH_LIST)  );				
					}			
				}
				
			} else {
				// get advanced person list from Cache
//				if( getController().getFromCache(Constants.ADVANCED_SEARCH_LIST) != null ) {
//					displayRecords( (List<PersonWeb>) getController().getFromCache(Constants.ADVANCED_SEARCH_LIST) );				
//				}	
				if(  Registry.get(Constants.ADVANCED_SEARCH_LIST) != null ) {
					displayRecords( (List<PersonWeb>) Registry.get(Constants.ADVANCED_SEARCH_LIST)  );				
				}			
			}
			
		} else if (event.getType() == AppEvents.AdvancedSearchRenderData) {
			grid = null;
			initUI();
			
			// get advanced search criteria from Cache
			if( Registry.get(Constants.ADVANCED_SEARCH_CRITERIA) != null ) {
				setCriteriaToView( (PersonWeb) Registry.get(Constants.ADVANCED_SEARCH_CRITERIA) );				
			}	
			
			if( Registry.get(Constants.ADVANCED_SEARCH_FUZZY_MATCH) != null ) {
				checkFuzzyMatch.setValue( (Boolean)Registry.get(Constants.ADVANCED_SEARCH_FUZZY_MATCH) );				
			}
			displayRecords((List<PersonWeb>) event.getData());
		}
	}

	private void setCriteriaToView(PersonWeb person) {
		givenName.setValue(person.getGivenName());
		familyName.setValue(person.getFamilyName());	
		dateOfBirth.setValue( Utility.StringToDate(person.getDateOfBirth()));	
		for( Gender gd : genders.getModels()) {
			if(person.getGender()!= null && person.getGender().equals(gd.getCode())) 
				gender.setValue(gd);
		}		
	}

	private void displayRecords(List<PersonWeb> persons) {
		if (grid == null) {
			setupPersonGrid();
		}
		personStore.removeAll();
		for (PersonWeb personWeb : persons) {
			Set<LinkedPersonWeb> linked = personWeb.getLinkedPersons();
			if (linked == null) {
				personWeb.setLinkedPersons(new java.util.HashSet<LinkedPersonWeb>());
			}
		}
		personStore.add(persons);
		container.layout();
		status.hide();
		searchButton.unmask();
	}

	private void clearSearch() {
		givenName.clear();
		familyName.clear();
		dateOfBirth.clear();
		gender.clear();
		checkFuzzyMatch.clear();		
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
			        	// Info.display("Information", "The person: " +updatePerson.getGivenName());
						controller.handleEvent(new AppEvent(AppEvents.AdvancedUpdatePersonView, updatePerson));									
					}									
	          }
	        });

	    MenuItem menuItemDelete = new MenuItem("Delete", IconHelper.create("images/delete.png"), new SelectionListener<MenuEvent>() {
	          @Override
	          public void componentSelected(MenuEvent ce) {
					PersonWeb person = grid.getSelectionModel().getSelectedItem();
					if( person != null ) {
						controller.handleEvent(new AppEvent(AppEvents.AdvancedDeletePersonView, person));									
					}				
	          }
	        });
	    
	    MenuItem menuItemUnlink = new MenuItem("Unlink Persons", IconHelper.create("images/link_break.png"), new SelectionListener<MenuEvent>() {
	          @Override
	          public void componentSelected(MenuEvent ce) {
	        	    updatePerson = grid.getSelectionModel().getSelectedItem();
	        	    
					if( updatePerson != null ) {
						if(updatePerson.getLinkedPersons() != null && updatePerson.getLinkedPersons().size() > 0 ) {
							
						   PersonWeb person = copyPerson(updatePerson);
						   controller.handleEvent(new AppEvent(AppEvents.UnlinkPersonsView, person));	

						} else {
							Info.display("Warning", "No persons linked with this selected person.");
						}							
					}				
	          }
	        });
	    updateMenu.add(menuItemUpdate);
	    updateMenu.add(menuItemDelete);	    
	    updateMenu.add(menuItemUnlink);	
	    
	    // check permissions
	    if( permissions != null ) {
		    PermissionWeb permission = permissions.get(Permission.RECORD_EDIT);
			if (permission == null) {
				menuItemUpdate.disable();
				menuItemUnlink.disable();
			}
	
		    permission = permissions.get(Permission.RECORD_DELETE);
			if (permission == null) {
				menuItemDelete.disable();
			}
	    }
		
		grid.setContextMenu(updateMenu);		
		gridContainer.add(grid);
	}

	private PersonWeb copyPerson(PersonWeb person) {
		PersonWeb updatingPerson = new PersonWeb();
		
		updatingPerson.setPersonId( person.getPersonId());

		// Name
		updatingPerson.setGivenName( person.getGivenName());
		updatingPerson.setMiddleName(person.getMiddleName());
		updatingPerson.setFamilyName(person.getFamilyName());
		updatingPerson.setPrefix(person.getPrefix());
		updatingPerson.setSuffix(person.getSuffix());
		
		// Birth		
		updatingPerson.setDateOfBirth(person.getDateOfBirth());
		updatingPerson.setBirthPlace( person.getBirthPlace());
		updatingPerson.setBirthOrder( person.getBirthOrder());	
		updatingPerson.setMultipleBirthInd(person.getMultipleBirthInd());

		// Address
		updatingPerson.setAddress1(person.getAddress1());
		updatingPerson.setAddress2(person.getAddress2());		
		updatingPerson.setCity(person.getCity());		
		updatingPerson.setState(person.getState());		
		updatingPerson.setPostalCode(person.getPostalCode());
		updatingPerson.setCountry(person.getCountry());
		updatingPerson.setCountryCode(person.getCountryCode());

		// Phone
		updatingPerson.setPhoneCountryCode(person.getPhoneCountryCode());
		updatingPerson.setPhoneAreaCode(person.getPhoneAreaCode());
		updatingPerson.setPhoneNumber(person.getPhoneNumber());
		updatingPerson.setPhoneExt(person.getPhoneExt());	
		
		// Other
		updatingPerson.setGender(person.getGender());
		updatingPerson.setDegree(person.getDegree());
		updatingPerson.setMothersMaidenName(person.getMothersMaidenName());
		updatingPerson.setMaritalStatusCode(person.getMaritalStatusCode());		
		updatingPerson.setDeathTime(person.getDeathTime());
		updatingPerson.setDeathInd(person.getDeathInd());
		updatingPerson.setSsn(person.getSsn());
		updatingPerson.setEmail(person.getEmail());	

		updatingPerson.setDateCreated(person.getDateCreated());			
		updatingPerson.setDateChanged(person.getDateChanged());	
		
		// updatingPerson.setPersonIdentifiers(person.getPersonIdentifiers());			
		
		return updatingPerson;
	}

	private String getTemplate() {
		return "<p class=\"identifierBlock\">" +
				"<b>Person Identifiers:</b><br>" +
				"<table class=\"identifierTable\">" +
					"<tr>" +
						"<th class=\"identifierColumn\">Identifier</th>" +
						"<th class=\"identifierDomainNameColumn\">Domain Name</th>" +
						"<th class=\"namespaceColumn\">Namespace Identifier</th>" +
						"<th class=\"universalIdentifierColumn\">Universal Identifier</th>" +
						"<th class=\"universalIdentifierTypeColumn\" >Universal Identifier Type</th>" +
					"</tr>" +
					"<tpl for=\"personIdentifiers\">" +
						"<tr>" +
						"<td>{identifier}</td><td>{identifierDomainName}</td><td>{namespaceIdentifier}</td><td>{universalIdentifier}</td><td>{universalIdentifierTypeCode}</td>" +
						"</tr>" +
					"</tpl>" +
				"</table>"+
				"<br>" +
				"<b>Linked Patients:</b><br>" +
				"<table class=\"identifierTable\">" +
					"<tr>" +
						"<th class=\"universalIdentifierTypeColumn\">Name</th>" +
						"<th class=\"linkedIdentifierColumn\">Identifier</th>" +
						"<th class=\"dateOfBirthColumn\">Date of Birth</th>" +
						"<th class=\"identifierColumn\">Address</th>" +
					"</tr>" +
					"<tpl for=\"linkedPersons\">" +
						"<tr>" +
						"<td>{givenName} {familyName}</td><td>{personIdentifier}</td><td>{dateOfBirth:date(\"yyyy-MM-dd\")}</td><td>{address1} {city} {state} {postalCode}</td>" +
						"</tr>" +
					"</tpl>" +	
				"</table>"+
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

//		final TextField<String> givenName = new TextField<String>();
		givenName = new TextField<String>();
		givenName.setFieldLabel("First Name");
		givenName.setTabIndex(1);
		left.add(givenName, formData);

//		final DateField dateOfBirth = new DateField();
		dateOfBirth = new DateField();
		dateOfBirth.setFieldLabel("Date of Birth");
		dateOfBirth.setTabIndex(3);	      
		left.add(dateOfBirth, formData);
		
		checkFuzzyMatch = new CheckBox();  
		checkFuzzyMatch.setBoxLabel("");  
		checkFuzzyMatch.setValue(false);  
	    CheckBoxGroup checkGroup = new CheckBoxGroup();  
	    checkGroup.setFieldLabel("Fuzzy Match");  	    
	    checkGroup.add(checkFuzzyMatch);  
		left.add(checkGroup, formData);
		
		LayoutContainer right = new LayoutContainer();
		right.setStyleAttribute("paddingLeft", "10px");
		layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);
		right.setLayout(layout);

//		final TextField<String> familyName = new TextField<String>();
		familyName = new TextField<String>();
		familyName.setFieldLabel("Last Name");
		familyName.setTabIndex(2);
		right.add(familyName, formData);
		
//		ComboBox<Gender> gender = new ComboBox<Gender>();
		gender = new ComboBox<Gender>();
		gender.setFieldLabel("Gender");
		gender.setEmptyText("Select a gender...");
		gender.setDisplayField("name");
		gender.setWidth(100);
		gender.setStore(genders);
		gender.setTypeAhead(true);
		gender.setTriggerAction(TriggerAction.ALL);
		gender.setTabIndex(4);
		right.add(gender, new FormData("40%"));
	    
		main.add(left, new ColumnData(.5));  
		main.add(right, new ColumnData(.5));
		panel.add(main, new FormData("60%"));
		searchButton = new Button("Search", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {
				PersonWeb template = new PersonWeb();
				template.setGivenName(givenName.getValue());
				template.setFamilyName(familyName.getValue());
				if (template.getGivenName() == null && template.getFamilyName() == null) {
					Info.display("Warning", "You must enter at least a partial first or last name before pressing the search button.");
					return;
				}				

				if ( (template.getGivenName().equals("%") && template.getFamilyName() == null) || 
					 (template.getFamilyName().equals("%") && template.getGivenName() == null) ||
					 (template.getGivenName().equals("%") && template.getFamilyName().equals("%")) ) {
					Info.display("Warning", "You must enter at least a partial first or last name before pressing the search button.");
					return;
				}			
				
				if( !dateOfBirth.validate() ) {
					Info.display("Warning", "You must enter valid format of date before pressing the search button.");
					return;					
				}
				template.setDateOfBirth( Utility.DateToString(dateOfBirth.getValue()));

				Gender genderValue = gender.getValue();
				if (genderValue != null) {
					// Info.display("Gender", genderValue.getCode());
					template.setGender(genderValue.getCode());
				} else {
					// Info.display("Gender", "null");
					template.setGender(null);					
				}
				
				Registry.register(Constants.ADVANCED_SEARCH_FUZZY_MATCH,  checkFuzzyMatch.getValue());
				if( checkFuzzyMatch.getValue() == true ) {
					controller.handleEvent(new AppEvent(AppEvents.AdvancedSearchFuzzyMatchInitiate, template));
				} else {
					controller.handleEvent(new AppEvent(AppEvents.AdvancedSearchInitiate, template));					
				}
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
		data.setMargins(new Margins(165, 2, 2, 2));
		container.add(gridContainer, data);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
}
