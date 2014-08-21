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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.ui.util.InputFormData;
import org.openempi.webapp.client.ui.util.InputFormat;
import org.openempi.webapp.client.ui.util.Utility;
import org.openempi.webapp.resources.client.model.Gender;
import org.openempi.webapp.resources.client.model.State;
import org.openempi.webapp.resources.client.model.Country;
import org.openempi.webapp.resources.client.model.Confirm;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.mvc.View;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
//import com.extjs.gxt.ui.client.mvc.Controller;
//import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.DateTimePropertyEditor;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;  
import com.extjs.gxt.ui.client.widget.grid.ColumnData;  
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

public class AddPersonView extends View
{
//	private LayoutContainer container;
	private ContentPanel container;
	private LayoutContainer formButtonContainer;	
	private LayoutContainer identifierContainer;
	private LayoutContainer formContainer;
	private FormPanel leftFormPanel; 
	private FormPanel rightFormPanel; 
	private FormPanel buttonPanel; 
	
	// Name
	private TextField<String> firstName;
	private TextField<String> middleName;
	private TextField<String> lastName;
	private TextField<String> prefix;
	private TextField<String> suffix;
	
	// Birth
	private DateField dateOfBirth;
	private TextField<String> birthPlace;
	private NumberField birthOrder;
	private ComboBox<Confirm> multiBirthInd;
	
	// Address
	private TextField<String> address1;
	private TextField<String> address2;
	private TextField<String> city;
	private ComboBox<State> state;
	private TextField<String> zip;
	private ComboBox<Country> country;
	
	// Phone
	private TextField<String> phoneCountryCode;
	private TextField<String> phoneAreaCode;
	private TextField<String> phoneNumber;
	private TextField<String> phoneExt;
	
	// Other
	private ComboBox<Gender> gender;
	private TextField<String> degree;
	private TextField<String> maidenName;
	private ComboBox<Confirm> maritalStatus;
	private ComboBox<Confirm> deathInd;
	private DateField deathTime;
	private TextField<String> ssn;
	private TextField<String> email;

	
	private TextField<String> identifier;
	private ComboBox<IdentifierDomainWeb> identifierDomains;
	
	private ListStore<PersonIdentifierWeb> identifierStore = new ListStore<PersonIdentifierWeb>();	
	private Grid<PersonIdentifierWeb> identifierGrid;	
	private Button addIdentifierButton;
	private Button removeIdentifierButton;

	private Status status;
	private Button checkDuplicateButton;
	private Button submitButton;
	private Button cancelButton;

	ListStore<Gender> genders = new ListStore<Gender>();
	ListStore<State> states = new ListStore<State>();
	ListStore<Country> countries = new ListStore<Country>();
	ListStore<Confirm> confirms = new ListStore<Confirm>();
//	ListStore<IdentifierDomainWeb> domains = new ListStore<IdentifierDomainWeb>();
	
	private Dialog checkDuplicateDialog = null;
	private Grid<PersonWeb> grid;
	private ListStore<PersonWeb> personStore = new ListStore<PersonWeb>();

	private PersonWeb updatePerson = null;	
	
	public AddPersonView(Controller controller) {
		super(controller);
		genders.add(InputFormData.getGenders());
		states.add(InputFormData.getStates());
		countries.add(InputFormData.getCountry());
		confirms.add(InputFormData.getConfirm());
//		List<IdentifierDomainWeb> domainEntries = Registry.get(Constants.IDENTITY_DOMAINS);
//		domains.add(domainEntries);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		
		if (event.getType() == AppEvents.AddPersonView) {
			initUI();			
			clearFormFields();
	        
		} else if (event.getType() == AppEvents.AddPersonComplete) {			
			// String message = event.getData();
			// Info.display("Information", "Person was successfully added with message " + message);					
	        MessageBox.alert("Information", "Person was successfully added", listenInfoMsg);  
 
		} else if (event.getType() == AppEvents.CheckDuplicatePersonComplete) {			
		
			List<PersonWeb> personList =  (List<PersonWeb>) event.getData();			
			status.hide();
			checkDuplicateButton.unmask();  
			if( personList != null && personList.size() > 0 ) {				
				personStore.removeAll();
				personStore.add(personList);
				
				checkDuplicateDialog.show();
			} else {
		        MessageBox.alert("Information", "No duplicate Persons found", null);  				
			}	 	
		} else if (event.getType() == AppEvents.AddToUpdatePersonViewCancel) {		
			
			initUI();	
			if( getController().getFromCache(Constants.ADD_PERSON) != null ) {
				PersonWeb person = (PersonWeb) getController().getFromCache(Constants.ADD_PERSON);
				
				displayRecords(person);	
			}			
			checkDuplicateDialog.show();
			
		} else if (event.getType() == AppEvents.AddToUpdatePersonViewFinished) {		
			
			initUI();	
			if( getController().getFromCache(Constants.ADD_PERSON) != null ) {
				PersonWeb person = (PersonWeb) getController().getFromCache(Constants.ADD_PERSON);
				
				displayRecords(person);	
			}		
			
			// Update the person lists in Registry
	    	PersonWeb updatedperson = event.getData();	
	    	updatedperson.setLinkedPersons(updatePerson.getLinkedPersons());
	    	
	    	List<PersonWeb> basicPersonList = Registry.get(Constants.BASIC_SEARCH_LIST);
			if( basicPersonList != null ) {
				for (PersonWeb personWeb : basicPersonList) {
					if( personWeb.getPersonId().intValue() == updatePerson.getPersonId().intValue() ) {
						basicPersonList.remove(personWeb);
						basicPersonList.add(updatedperson);	
						break;
					}
				}		
				Registry.register(Constants.BASIC_SEARCH_LIST, basicPersonList);	
			}

	    	List<PersonWeb> advancedPersonList = Registry.get(Constants.ADVANCED_SEARCH_LIST);
			if( advancedPersonList != null ) {
				for (PersonWeb personWeb : advancedPersonList) {
					if( personWeb.getPersonId().intValue() == updatePerson.getPersonId().intValue() ) {
						advancedPersonList.remove(personWeb);
						advancedPersonList.add(updatedperson);	
						break;
					}
				}		
			    Registry.register(Constants.ADVANCED_SEARCH_LIST, advancedPersonList);	
			}
		   		    
		    personStore.remove(updatePerson);	       	
		    personStore.add(updatedperson);	
	       	
			checkDuplicateDialog.show();
	        
		} else if (event.getType() == AppEvents.Error) {	
			status.hide();
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, listenFailureMsg);  			
		}
	}

    final Listener<MessageBoxEvent> listenInfoMsg = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();   
          if( btn.getText().equals("OK")) {   

  			  clearFormFields();   
  			  status.hide();
  			  submitButton.unmask();  
          }
        }  
    };  

    final Listener<MessageBoxEvent> listenFailureMsg = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();   
          if( btn.getText().equals("OK")) {   
  			  status.hide();
  			  submitButton.unmask();  
          }
        }  
    };  
    
	private FormPanel setupIdentifierForm() {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setLabelWidth(150);
	    formPanel.setWidth(855);
	    
		formPanel.add( setupIdentifierfieldSet(1)); 
		
	    return formPanel;
	}
	
	private FieldSet setupIdentifierfieldSet(int tabIndex) {
		FieldSet identifierfieldSet = new FieldSet();  
		identifierfieldSet.setHeading("Identifiers");  
		identifierfieldSet.setCollapsible(true);  
		identifierfieldSet.setBorders(false);  
		FormLayout identifierlayout = new FormLayout();  
		identifierlayout.setLabelWidth(150);  
		identifierlayout.setDefaultWidth(390); // It is the real function to set the textField width
		identifierfieldSet.setLayout(identifierlayout);  	


			identifier= new TextField<String>();
			identifier.setFieldLabel("Identifier");
//			identifier.setAllowBlank(false);
			identifier.setTabIndex(tabIndex++);
			
			//Field listeners
			identifier.addListener(Events.Change, new Listener<FieldEvent>() {
		            public void handleEvent(FieldEvent fe) {
		            	// Clear invalid mark
		            	identifier.clearInvalid();
		            }
		    });
			
			ListStore<IdentifierDomainWeb> domains = new ListStore<IdentifierDomainWeb>();
			List<IdentifierDomainWeb> domainEntries = Registry.get(Constants.IDENTITY_DOMAINS);
			domains.add(domainEntries);		
			
			identifierDomains = new ComboBox<IdentifierDomainWeb>();
			identifierDomains.setFieldLabel("Identifier Domain");
//			identifierDomains.setDisplayField("namespaceIdentifier");
//			identifierDomains.setValueField("namespaceIdentifier");
			identifierDomains.setDisplayField("identifierDomainName");
			identifierDomains.setStore(domains);
//			identifierDomains.setAllowBlank(false);
			identifierDomains.setTabIndex(tabIndex++);
		
			//Field listeners
			identifierDomains.addListener(Events.Change, new Listener<FieldEvent>() {
		            public void handleEvent(FieldEvent fe) {
		            	// Clear invalid mark
		            	identifierDomains.clearInvalid();
		            }
		    });
			
			ContentPanel cp = new ContentPanel();  
			cp.setHeaderVisible(false);
			cp.setWidth(875);
			
		    ToolBar toolBar = new ToolBar();	    
			// Buttons
		    addIdentifierButton = new Button(" Add ", IconHelper.create("images/add.png"), new SelectionListener<ButtonEvent> () {
				@Override
				public void componentSelected(ButtonEvent ce) {	
					if (identifier.getValue() != null && identifierDomains.getValue() != null) {
			
						// check duplicate identifier
						for (int i=0;i<identifierStore.getCount();i++){
						     PersonIdentifierWeb identifierInStore = identifierStore.getAt(i);
						     if( identifier.getValue().equals(identifierInStore.getIdentifier())) {						    	 						    	 
						    	 identifier.markInvalid("Duplicate identifier");
						    	 return;
						     }
						     
						     // Info.display("identifierDomains:", ""+identifierDomains.getValue().getIdentifierDomainName());
						     // Info.display("identifierInStore:", ""+identifierInStore.getIdentifierDomain().getIdentifierDomainName());						     
						     if( identifierDomains.getValue().getIdentifierDomainId().equals(identifierInStore.getIdentifierDomain().getIdentifierDomainId())) {						    	 						    	 
						    	 identifierDomains.markInvalid("Duplicate identifier domain");
						    	 return;
						     }						     
						}
						
						PersonIdentifierWeb personIdentifier = new PersonIdentifierWeb();
						personIdentifier.setIdentifier(identifier.getValue());
						personIdentifier.setIdentifierDomain(identifierDomains.getValue());
						identifierStore.add(personIdentifier);
					}
	
				}
			});
		    removeIdentifierButton = new Button(" Remove ", IconHelper.create("images/delete.png"), new SelectionListener<ButtonEvent> () {
				@Override
				public void componentSelected(ButtonEvent ce) {
					PersonIdentifierWeb item = identifierGrid.getSelectionModel().getSelectedItem();
					String removedIdentifier = item.getIdentifier();
					if( item != null )
						identifierStore.remove(item);
									
					// Clear invalid mark
					if( identifier.getValue().equals(removedIdentifier)){	            						    
						identifier.clearInvalid();
					} 
					
					if( identifierDomains.getValue().getIdentifierDomainId().equals(item.getIdentifierDomain().getIdentifierDomainId())){	            						    
						identifierDomains.clearInvalid();
					} 
				}
			});			
		    addIdentifierButton.setTabIndex(tabIndex++);
		    removeIdentifierButton.setTabIndex(tabIndex++);		 
		    
		    toolBar.add(addIdentifierButton);   
		    toolBar.add(new SeparatorToolItem());  
		    toolBar.add(removeIdentifierButton);  
		   
		    cp.add(toolBar);  
		    cp.add( setupPersonIdentifierGrid(identifierStore,tabIndex) );  
		    
			identifierfieldSet.add(identifier);
			identifierfieldSet.add(identifierDomains);
			identifierfieldSet.add(cp);	
			
			return identifierfieldSet;
	}
	
	private Grid<PersonIdentifierWeb> setupPersonIdentifierGrid( ListStore<PersonIdentifierWeb> identifierStore, int tabIndex ) {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
					    
	    // Columns
		ColumnConfig column;

		column = new ColumnConfig();
		column.setId("identifier");  
		column.setHeader("Identifier");
		column.setWidth(220);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("identifierDomainName");  
		column.setHeader("Domain Name");
		column.setWidth(160);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("namespaceIdentifier");  
		column.setHeader("Namespace Identifier");
		column.setWidth(160);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("universalIdentifier");  
		column.setHeader("Universal Identifier");  
		column.setWidth(160);
		configs.add(column);
	
		column = new ColumnConfig();
		column.setId("universalIdentifierTypeCode");  
		column.setHeader("Universal Identifier Type");  
		column.setWidth(130);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);
		identifierGrid = new Grid<PersonIdentifierWeb>(identifierStore, cm);
		
		identifierGrid.setStyleAttribute("borderTop", "none");
		identifierGrid.setBorders(true);
		identifierGrid.setStripeRows(true); 
		identifierGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		identifierGrid.setWidth(875); // 850 + vertical scroll bar space
		identifierGrid.setHeight(86);
		identifierGrid.setTabIndex(tabIndex);	
		
		return identifierGrid;
	}
	
	private FormPanel setupPersonLeftForm(String title, int tabIndex) {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setLabelWidth(150);
	    formPanel.setWidth(400);
//	    formPanel.setLabelAlign(LabelAlign.RIGHT); // default is LEFT
	    formPanel.setTitle(title); 
	    
	    // Name fields
	    FieldSet namefieldSet = new FieldSet();  
	    namefieldSet.setHeading("Name");  
	    namefieldSet.setCollapsible(true);  
	    namefieldSet.setBorders(false);  
	    FormLayout namelayout = new FormLayout();  
	    namelayout.setLabelWidth(150);  
	    namefieldSet.setLayout(namelayout); 
	    
			firstName = new TextField<String>();
			firstName.setFieldLabel("First Name");
			firstName.setAllowBlank(false);
			// firstName.setLabelStyle("font-size:12px; margin-left: 20px");
			firstName.setTabIndex(tabIndex++);
			
			middleName = new TextField<String>();
			middleName.setFieldLabel("Middle Name");
			middleName.setTabIndex(tabIndex++);
			
			lastName = new TextField<String>();
			lastName.setFieldLabel("Last Name");
			lastName.setAllowBlank(false);
			lastName.setTabIndex(tabIndex++);

			prefix = new TextField<String>();
			prefix.setFieldLabel("Prefix");
			prefix.setTabIndex(tabIndex++);

			suffix = new TextField<String>();
			suffix.setFieldLabel("Suffix");
			suffix.setTabIndex(tabIndex++);

			namefieldSet.add(firstName);
			namefieldSet.add(middleName);
			namefieldSet.add(lastName);			
			namefieldSet.add(prefix);		
			namefieldSet.add(suffix);		
			
		// Address fields
		FieldSet addressfieldSet = new FieldSet();  
		addressfieldSet.setHeading("Address");  
		addressfieldSet.setCollapsible(true);  
		addressfieldSet.setBorders(false); 		
		FormLayout addresslayout = new FormLayout();  
		addresslayout.setLabelWidth(150);  
		addressfieldSet.setLayout(addresslayout);
		    		
			address1 = new TextField<String>();
			address1.setFieldLabel("Address 1");
			address1.setTabIndex(tabIndex++);
			
			address2 = new TextField<String>();
			address2.setFieldLabel("Address 2");
			address2.setTabIndex(tabIndex++);
			
			city = new TextField<String>();
			city.setFieldLabel("City");
			city.setTabIndex(tabIndex++);
			
			state = new ComboBox<State>();
			state.setFieldLabel("State");
			state.setEmptyText("Select a state...");  
			state.setDisplayField("name");  
			state.setWidth(150);  
			state.setStore(states);
			state.setTypeAhead(true);  
		    state.setTriggerAction(TriggerAction.ALL);
		    state.setTabIndex(tabIndex++);
			
		    zip = new TextField<String>();
			zip.setFieldLabel("Zip Code");
			zip.setTabIndex(tabIndex++);

			country = new ComboBox<Country>();
			country.setFieldLabel("Country");
			country.setEmptyText("Select a country...");
			country.setDisplayField("name");
			country.setTemplate( InputFormat.getFlagTemplate() );
			country.setWidth(100);
			country.setStore(countries);
			country.setTypeAhead(true);
			country.setTriggerAction(TriggerAction.ALL);
			country.setTabIndex(tabIndex++);
			
			addressfieldSet.add(address1);
			addressfieldSet.add(address2);
			addressfieldSet.add(city);
			addressfieldSet.add(state);
			addressfieldSet.add(zip);
			addressfieldSet.add(country);		

		// Birth fields	  
		FieldSet birthfieldSet = new FieldSet();  
		birthfieldSet.setHeading("Birth");  
		birthfieldSet.setCollapsible(true);  
		birthfieldSet.setBorders(false);  
		FormLayout birthlayout = new FormLayout();  
		birthlayout.setLabelWidth(150);  
		birthfieldSet.setLayout(birthlayout);  
		
			dateOfBirth = new DateField();
			dateOfBirth.setFieldLabel("Date of Birth");
			dateOfBirth.setToolTip("yyyy-MM-dd");
			dateOfBirth.setTabIndex(tabIndex++);

			birthPlace = new TextField<String>();
			birthPlace.setFieldLabel("Birth Place");
			birthPlace.setTabIndex(tabIndex++);	
			
			birthOrder = new NumberField();
			birthOrder.setFieldLabel("Birth Order");
			birthOrder.setAllowDecimals(false);
			birthOrder.setAllowNegative(false);
			birthOrder.setTabIndex(tabIndex++);	
			 
			multiBirthInd = new ComboBox<Confirm>();
			multiBirthInd.setFieldLabel("Multiple Birth Indicator");
			multiBirthInd.setEmptyText("Select a Yes or No...");
			multiBirthInd.setDisplayField("name");
			multiBirthInd.setWidth(100);
			multiBirthInd.setStore(confirms);
			multiBirthInd.setTypeAhead(true);
			multiBirthInd.setTriggerAction(TriggerAction.ALL);
			multiBirthInd.setTabIndex(tabIndex++);
			
			
			birthfieldSet.add(dateOfBirth);
			birthfieldSet.add(birthPlace);
			birthfieldSet.add(birthOrder);
			birthfieldSet.add(multiBirthInd);
			
		formPanel.add(namefieldSet); 
		formPanel.add(addressfieldSet); 
		formPanel.add(birthfieldSet); 
		
	    return formPanel;
	}
	
	private FormPanel setupPersonRightForm(String title, int tabIndex) {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setLabelWidth(150);
	    formPanel.setWidth(400);
//	    formPanel.setLabelAlign(LabelAlign.RIGHT); // default is LEFT
	    formPanel.setTitle(title);  

	    FieldSet phonefieldSet = new FieldSet();  
	    phonefieldSet.setHeading("Phone");  
	    phonefieldSet.setCollapsible(true);  
	    phonefieldSet.setBorders(false);  
	    FormLayout phonelayout = new FormLayout();  
	    phonelayout.setLabelWidth(150);  
	    phonefieldSet.setLayout(phonelayout);  

			phoneCountryCode = new TextField<String>();
			phoneCountryCode.setFieldLabel("Phone Country Code");	
			phoneCountryCode.setTabIndex(tabIndex++);	
			
			phoneAreaCode = new TextField<String>();
			phoneAreaCode.setFieldLabel("Phone Area Code");	
			phoneAreaCode.setTabIndex(tabIndex++);
			
			phoneNumber = new TextField<String>();
			phoneNumber.setFieldLabel("Phone Number");	
			phoneNumber.setToolTip("xxx-xxxx");
			phoneNumber.setTabIndex(tabIndex++);
	
			phoneExt = new TextField<String>();
			phoneExt.setFieldLabel("Phone Extension");	
			phoneExt.setTabIndex(tabIndex++);
	
			phonefieldSet.add(phoneCountryCode);
			phonefieldSet.add(phoneAreaCode);
			phonefieldSet.add(phoneNumber);
			phonefieldSet.add(phoneExt);		
		
		FieldSet otherfieldSet = new FieldSet();  
		otherfieldSet.setHeading("Other");  
		otherfieldSet.setCollapsible(true);  
		otherfieldSet.setBorders(false);  
		FormLayout otherlayout = new FormLayout();  
		otherlayout.setLabelWidth(150);  
		otherfieldSet.setLayout(otherlayout);  		
		
			gender = new ComboBox<Gender>();
			gender.setFieldLabel("Gender");
			gender.setEmptyText("Select a gender...");
			gender.setDisplayField("name");
			gender.setWidth(100);
			gender.setStore(genders);
			gender.setTypeAhead(true);
			gender.setTriggerAction(TriggerAction.ALL);
			gender.setTabIndex(tabIndex++);

			degree = new TextField<String>();
			degree.setFieldLabel("Degree");	
			degree.setTabIndex(tabIndex++);
			
			maidenName = new TextField<String>();
			maidenName.setFieldLabel("Mother's Maiden Name");	
			maidenName.setTabIndex(tabIndex++);
	
			maritalStatus = new ComboBox<Confirm>();
			maritalStatus.setFieldLabel("Marital Status");
			maritalStatus.setEmptyText("Select a Yes or No...");
			maritalStatus.setDisplayField("name");
			maritalStatus.setWidth(100);
			maritalStatus.setStore(confirms);
			maritalStatus.setTypeAhead(true);
			maritalStatus.setTriggerAction(TriggerAction.ALL);
			maritalStatus.setTabIndex(tabIndex++);
			
			deathInd = new ComboBox<Confirm>();
			deathInd.setFieldLabel("Death Indicator");
			deathInd.setEmptyText("Select a Yes or No...");
			deathInd.setDisplayField("name");
			deathInd.setWidth(100);
			deathInd.setStore(confirms);
			deathInd.setTypeAhead(true);
			deathInd.setTriggerAction(TriggerAction.ALL);
			deathInd.setTabIndex(tabIndex++);
			
			DateTimePropertyEditor dateFormat = new DateTimePropertyEditor("yyyy-MM-dd HH:mm");
			deathTime = new DateField();
			deathTime.setFieldLabel("Death Time");
			deathTime.setToolTip("yyyy-MM-dd HH:mm");
			deathTime.setPropertyEditor(dateFormat);
			deathTime.setTabIndex(tabIndex++);
			
			ssn = new TextField<String>();
			ssn.setFieldLabel("Social Security Number");	
			ssn.setTabIndex(tabIndex++);
			
			email = new TextField<String>();
			email.setFieldLabel("Email");	
			email.setRegex(InputFormat.EMAIL_FORMATS);
	        email.getMessages().setRegexText("Invalid email format");
//	        email.setAutoValidate(true);  
	        email.setToolTip("xyz@example.com");	        
			email.setTabIndex(tabIndex++);
			
			otherfieldSet.add(gender);
			otherfieldSet.add(degree);
			otherfieldSet.add(maidenName);
			otherfieldSet.add(maritalStatus);
			otherfieldSet.add(deathInd);
			otherfieldSet.add(deathTime);
			otherfieldSet.add(ssn);
			otherfieldSet.add(email);
		    
		formPanel.add(phonefieldSet); 
		formPanel.add(otherfieldSet); 
		
	    return formPanel;
	}
	
	private PersonWeb getNewPersonFromGUI() {
		PersonWeb person = new PersonWeb();		
		
		// Name
		person.setGivenName( firstName.getValue());
		person.setMiddleName(middleName.getValue());
		person.setFamilyName(lastName.getValue());
		person.setPrefix(prefix.getValue());
		person.setSuffix(suffix.getValue());
		
		// Birth
		person.setDateOfBirth( Utility.DateToString(dateOfBirth.getValue()));
		person.setBirthPlace( birthPlace.getValue());
		if( birthOrder.getValue() != null ) {
			person.setBirthOrder( birthOrder.getValue().intValue() );		
		}

		Confirm confirmValue = multiBirthInd.getValue();
		if (confirmValue != null) {
			person.setMultipleBirthInd(confirmValue.getCode());
		}
		
		// Address
		person.setAddress1(address1.getValue());
		person.setAddress2(address2.getValue());
		
		person.setCity(city.getValue());
		if (state.getValue() != null) {
			person.setState(state.getValue().getAbbr());
		}
		
		person.setPostalCode(zip.getValue());
		
		Country countryValue = country.getValue();
		if (countryValue != null) {
			person.setCountry(countryValue.getName());
			person.setCountryCode(countryValue.getCode());
		}
		
		// Phone
		person.setPhoneCountryCode(phoneCountryCode.getValue());
		person.setPhoneAreaCode(phoneAreaCode.getValue());
		person.setPhoneNumber(phoneNumber.getValue());
		person.setPhoneExt(phoneExt.getValue());	
		
		// Other
		Gender genderValue = gender.getValue();
		if (genderValue != null) {
			person.setGender(genderValue.getCode());
		}
		person.setDegree(degree.getValue());
		person.setMothersMaidenName(maidenName.getValue());

		Confirm maritalStatusValue = maritalStatus.getValue();
		if (maritalStatusValue != null) {
			person.setMaritalStatusCode(maritalStatusValue.getCode());
		}
		
		person.setDeathTime( Utility.DateTimeToString(deathTime.getValue()));
		// Info.display("Death time:", deathTime.getValue().toString());	

		Confirm deathIndValue = deathInd.getValue();
		if (deathIndValue != null) {
			person.setDeathInd(deathIndValue.getCode());
		}
		
		person.setSsn(ssn.getValue());
		person.setEmail(email.getValue());				

		
		// Identifiers
		Set<PersonIdentifierWeb> ids = new HashSet<PersonIdentifierWeb>();
		for (int i=0;i<identifierStore.getCount();i++){
		     PersonIdentifierWeb identifier = identifierStore.getAt(i);
		     ids.add(identifier);
		}
		person.setPersonIdentifiers(ids);
		
		// for testing			
		/* for( PersonIdentifierWeb identifier : person.getPersonIdentifiers()) {
			Info.display("Identifier type:", identifier.getUniversalIdentifierTypeCode());		
		}*/
		
		return 	person;
	}
	
	private void displayRecords(PersonWeb person) {
		
		// Name
		firstName.setValue(person.getGivenName());		
		middleName.setValue(person.getMiddleName());
		lastName.setValue(person.getFamilyName());
		prefix.setValue(person.getPrefix());
		suffix.setValue(person.getSuffix());
		
		// Birth
		dateOfBirth.setValue( Utility.StringToDate(person.getDateOfBirth()));	
		birthPlace.setValue(person.getBirthPlace());
		birthOrder.setValue(person.getBirthOrder( ));	
		for( Confirm cf : confirms.getModels()) {
			if(cf.getCode().equals(person.getMultipleBirthInd()))
				multiBirthInd.setValue(cf);
		}
		
		// Address
		address1.setValue(person.getAddress1());
		address2.setValue(person.getAddress2());
		city.setValue(person.getCity());
		for( State st : states.getModels()) {
			if(st.getAbbr().equals(person.getState()))
				state.setValue(st);
		}
		zip.setValue(person.getPostalCode());
		for( Country co : countries.getModels()) {
			if(co.getName().equals(person.getCountry()))
				country.setValue(co);
		}
		
		// Phone
		phoneCountryCode.setValue(person.getPhoneCountryCode());
		phoneAreaCode.setValue(person.getPhoneAreaCode());
		phoneNumber.setValue(person.getPhoneNumber());
		phoneExt.setValue(person.getPhoneExt());		
		
		// Other
		for( Gender gd : genders.getModels()) {
			if(person.getGender()!= null && person.getGender().equals(gd.getName()))
				gender.setValue(gd);
		}		

		degree.setValue(person.getDegree());
		maidenName.setValue(person.getMothersMaidenName());
		for( Confirm cf : confirms.getModels()) {
			if(cf.getCode().equals(person.getMaritalStatusCode()))
				maritalStatus.setValue(cf);
		}							
		deathTime.setValue( Utility.StringToDateTime(person.getDeathTime()));
		for( Confirm cf : confirms.getModels()) {
			if(cf.getCode().equals(person.getDeathInd()))
				deathInd.setValue(cf);
		}				
		ssn.setValue(person.getSsn());
		email.setValue(person.getEmail());	
		
		// Identifiers
		for( PersonIdentifierWeb identifier : person.getPersonIdentifiers()) {
			// Info.display("Identifiers:", identifier.getIdentifier());	
			if( identifier.getDateVoided() == null)
				identifierStore.add(identifier);			
		}		
	}
	
	private FormPanel setupButtonPanel(int tabIndex) {
		FormPanel buttonPanel = new FormPanel(); 
		buttonPanel.setHeaderVisible(false);
		buttonPanel.setBodyBorder(false);
		buttonPanel.setStyleAttribute("paddingRight", "10px");
		buttonPanel.setButtonAlign(HorizontalAlignment.CENTER);		
		 		
		submitButton = new Button("Save", new SelectionListener<ButtonEvent>() {
		
			@Override
			public void componentSelected(ButtonEvent ce) {
				// Info.display("test:", "save componentSelected");				
				if( !leftFormPanel.isValid() || !rightFormPanel.isValid() ) {
					
					Info.display("test:", "Invalid fields");	
					return;
				}

				PersonWeb person = getNewPersonFromGUI();
								
				controller.handleEvent(new AppEvent(AppEvents.AddPersonInitiate, person));
				status.show();
				submitButton.mask();
			}		
		});

		
		checkDuplicateButton = new Button("Check Duplicate", new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				// Info.display("test:", "Check Duplicate componentSelected");					
				if( !leftFormPanel.isValid() || !rightFormPanel.isValid() ) {
					
					Info.display("test:", "Invalid fields");	
					return;
				}
				
				PersonWeb person = getNewPersonFromGUI();
				
				controller.handleEvent(new AppEvent(AppEvents.CheckDuplicatePersonInitiate, person));
				status.show();
				checkDuplicateButton.mask();
			}
		});
		
		cancelButton = new Button("Reset", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				clearFormFields();
			}
		});
			
		submitButton.setTabIndex(tabIndex++);	
		checkDuplicateButton.setTabIndex(tabIndex++);		
		cancelButton.setTabIndex(tabIndex++);	
		
		status = new Status();
		status.setBusy("please wait...");
		buttonPanel.getButtonBar().add(status);
		status.hide();
		
		buttonPanel.getButtonBar().setSpacing(15);
		buttonPanel.addButton(submitButton);
		buttonPanel.addButton(checkDuplicateButton);
		buttonPanel.addButton(cancelButton);
		
		return buttonPanel;
	}
	
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		
		buildCheckDuplicateDialog();
		
//		container = new LayoutContainer();
		container = new ContentPanel ();
		container.setLayout(new BorderLayout());
		container.setHeading("Add New Person");  
		
		formButtonContainer = new LayoutContainer();
		formButtonContainer.setScrollMode(Scroll.AUTO);
		
			TableLayout identlayout = new TableLayout(2);
			identlayout.setWidth("100%");
			identlayout.setCellSpacing(5);
			identlayout.setCellVerticalAlign(VerticalAlignment.TOP);
		    
			TableLayout formlayout = new TableLayout(2);
			formlayout.setWidth("100%");
			formlayout.setCellSpacing(5);
			formlayout.setCellVerticalAlign(VerticalAlignment.TOP);
		    
			identifierContainer = new LayoutContainer();;	
			identifierContainer.setLayout(identlayout);
			identifierContainer.add( setupIdentifierForm() );
			
			formContainer = new LayoutContainer();
//			formContainer.setBorders(true);
			formContainer.setLayout(formlayout);
				leftFormPanel = setupPersonLeftForm("", 6);
				rightFormPanel = setupPersonRightForm("", 21);
				
			formContainer.add(leftFormPanel);	
			formContainer.add(rightFormPanel);		
			buttonPanel = setupButtonPanel(33);
			
		formButtonContainer.add(identifierContainer);
		formButtonContainer.add(formContainer);
		formButtonContainer.add(buttonPanel);
		
		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(4, 2, 4, 2));
		container.add(formButtonContainer, data);		
				
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
	
	private void clearFormFields() {
		firstName.clear();
		middleName.clear();
		lastName.clear();
		prefix.clear();
		suffix.clear();
		
		dateOfBirth.clear();
		birthPlace.clear();
		birthOrder.clear();
		multiBirthInd.clear();
		
		address1.clear();
		address2.clear();
		city.clear();
		state.clear();
		zip.clear();
		country.clear();

		phoneCountryCode.clear();
		phoneAreaCode.clear();
		phoneNumber.clear();
		phoneExt.clear();
		
		gender.clear();
		degree.clear();
		maidenName.clear();
		maritalStatus.clear();
		deathInd.clear();
		deathTime.clear();
		ssn.clear();
		email.clear();
		
		identifier.clear();
		identifierDomains.clear();
		
		identifierStore.removeAll();
	}
	
	private void buildCheckDuplicateDialog() {
		if(checkDuplicateDialog != null)
			return;
		
		checkDuplicateDialog = new Dialog();
		checkDuplicateDialog.setBodyBorder(false);
		checkDuplicateDialog.setIcon(IconHelper.create("images/folder_go.png"));
		checkDuplicateDialog.setHeading("Duplicate Persons");
		checkDuplicateDialog.setWidth(980);
		checkDuplicateDialog.setHeight(300);
		checkDuplicateDialog.setButtons(Dialog.OK);
		checkDuplicateDialog.setHideOnButtonClick(true);
		checkDuplicateDialog.setResizable(false);
		checkDuplicateDialog.setModal(true);
		checkDuplicateDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
			}
	    });
		
		setupPersonGrid();
		checkDuplicateDialog.add(grid);
	}
	
	private void setupPersonGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		XTemplate tpl = XTemplate.create(getTemplate());
		RowExpander expander = new RowExpander();
		expander.setTemplate(tpl);
   
		configs.add(expander);
		
		ColumnConfig column = new ColumnConfig();
		column.setId("givenName");  
		column.setHeader("First Name");
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("familyName");  
		column.setHeader("Last Name");  
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("dateOfBirth");  
		column.setHeader("Date of Birth");
		column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
		column.setWidth(80);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("gender");  
		column.setHeader("Gender");
/*	    column.setRenderer(new GridCellRenderer<PersonWeb>() {  
	        public Object render(PersonWeb model, String property, ColumnData config, int rowIndex, int colIndex,  
	            ListStore<PersonWeb> store, Grid<PersonWeb> grid) {  
		        	String gender = "";
		        	if( model.getGender() != null ) {		        	
			        	if( model.getGender().contains("Other")) {
			        		gender = "Other";	        		
			        	} else if( model.getGender().contains("Female")) {
			        		gender = "Female";	
			        	} else if( model.getGender().contains("Male")) {
			        		gender = "Male";	
			        	}
		        	}
		        	return gender;
	        	}
	      });  
*/
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
		column.setWidth(100);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("state");  
		column.setHeader("State");  
		column.setWidth(70);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("postalCode");  
		column.setHeader("Zip Code");  
		column.setWidth(70);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("country");
		column.setHeader("Country");
		column.setWidth(120);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);

		grid = new Grid<PersonWeb>(personStore, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		grid.addPlugin(expander);
		grid.setHeight(260);
		
		// Update person from check duplicate person list
	    Menu updateMenu = new Menu();
	    updateMenu.add(new MenuItem("Update", IconHelper.create("images/update.png"), new SelectionListener<MenuEvent>() {
	          @Override
	          public void componentSelected(MenuEvent ce) {
	        	  	updatePerson = grid.getSelectionModel().getSelectedItem();
					if( updatePerson != null ) {
						
						PersonWeb person = getNewPersonFromGUI();
						
						getController().setToCache(Constants.ADD_PERSON, person);	
					      
						checkDuplicateDialog.hide();
						Dispatcher.forwardEvent(AppEvents.AddToUpdatePersonView, updatePerson);								
					}									
	          }
	        }));
	    
		grid.setContextMenu(updateMenu);
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

}
