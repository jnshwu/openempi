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

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.ui.util.InputFormData;
import org.openempi.webapp.client.ui.util.InputFormat;
import org.openempi.webapp.client.ui.util.Utility;
import org.openempi.webapp.resources.client.model.Confirm;
import org.openempi.webapp.resources.client.model.Country;
import org.openempi.webapp.resources.client.model.Gender;
import org.openempi.webapp.resources.client.model.State;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
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
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;

public class DeletePersonView extends View
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

		
	private ListStore<PersonIdentifierWeb> identifierStore = new ListStore<PersonIdentifierWeb>();	
	private Grid<PersonIdentifierWeb> identifierGrid;	
	
	private Button submitButton;
	private Button cancelButton;

	private PersonWeb updatePerson = null;
	private boolean advancedSearch;
	
	ListStore<Gender> genders = new ListStore<Gender>();
	ListStore<State> states = new ListStore<State>();
	ListStore<Country> countries = new ListStore<Country>();
	ListStore<Confirm> confirms = new ListStore<Confirm>();
	ListStore<IdentifierDomainWeb> domains = new ListStore<IdentifierDomainWeb>();
	
	public DeletePersonView(Controller controller) {
		super(controller);
		genders.add(InputFormData.getGenders());
		states.add(InputFormData.getStates());
		countries.add(InputFormData.getCountry());
		confirms.add(InputFormData.getConfirm());
		List<IdentifierDomainWeb> domainEntries = Registry.get(Constants.IDENTITY_DOMAINS);
		domains.add(domainEntries);
	}
	
	@Override
	protected void handleEvent(AppEvent event) {
		
		if (event.getType() == AppEvents.BasicDeletePersonRenderData || event.getType() == AppEvents.AdvancedDeletePersonRenderData) {
			if( event.getType() == AppEvents.BasicDeletePersonRenderData )
				advancedSearch = false;
			else
				advancedSearch = true;				
			
			initUI();	
				
			updatePerson = (PersonWeb) event.getData();

			identifierStore.removeAll();			
			displayRecords(updatePerson);	
		} else if (event.getType() == AppEvents.BasicDeletePersonComplete || event.getType() == AppEvents.AdvancedDeletePersonComplete) {			
				
	        MessageBox.alert("Information", "Person was successfully deleted", listenInfoMsg);  
	        
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, listenFailureMsg);  			
		}
	}

    final Listener<MessageBoxEvent> listenInfoMsg = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();   
          if( btn.getText().equals("OK")) {   

  			  submitButton.unmask();  
  			  
        	  // back to search page
  			  if ( advancedSearch ) {
					controller.handleEvent(new AppEvent(AppEvents.AdvancedDeletePersonFinished, updatePerson));
  			  } else {
					controller.handleEvent(new AppEvent(AppEvents.BasicDeletePersonFinished, updatePerson));			
  			  }        	  
          }
        }  
    };  

    final Listener<MessageBoxEvent> listenFailureMsg = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();   
          if( btn.getText().equals("OK")) {   
        	  
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

			
			ContentPanel cp = new ContentPanel();  
			cp.setHeaderVisible(false);
			cp.setWidth(875);
					    
		    cp.add( setupPersonIdentifierGrid(identifierStore,tabIndex) );  
		    
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
			firstName.setReadOnly(true);
			firstName.setTabIndex(tabIndex++);
			
			middleName = new TextField<String>();
			middleName.setFieldLabel("Middle Name");
			middleName.setReadOnly(true);
			middleName.setTabIndex(tabIndex++);
			
			lastName = new TextField<String>();
			lastName.setFieldLabel("Last Name");
			lastName.setReadOnly(true);
			lastName.setTabIndex(tabIndex++);

			prefix = new TextField<String>();
			prefix.setFieldLabel("Prefix");
			prefix.setReadOnly(true);
			prefix.setTabIndex(tabIndex++);

			suffix = new TextField<String>();
			suffix.setFieldLabel("Suffix");
			suffix.setReadOnly(true);
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
			address1.setReadOnly(true);
			address1.setTabIndex(tabIndex++);
			
			address2 = new TextField<String>();
			address2.setFieldLabel("Address 2");
			address2.setReadOnly(true);
			address2.setTabIndex(tabIndex++);
			
			city = new TextField<String>();
			city.setFieldLabel("City");
			city.setReadOnly(true);
			city.setTabIndex(tabIndex++);
			
			state = new ComboBox<State>();
			state.setFieldLabel("State");
			state.setEmptyText("Select a state...");  
			state.setDisplayField("name");  
			state.setWidth(150);  
			state.setStore(states);
			state.setTypeAhead(true);  
		    state.setTriggerAction(TriggerAction.ALL);
		    state.setEditable(false);
		    state.getListView().disableEvents(true);
		    state.setTabIndex(tabIndex++);
			
		    zip = new TextField<String>();
			zip.setFieldLabel("Zip Code");
		    zip.setReadOnly(true);
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
			country.setEditable(false);
			country.getListView().disableEvents(true);
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
			dateOfBirth.setEditable(false);
			dateOfBirth.getDatePicker().disableEvents(true);
			dateOfBirth.setTabIndex(tabIndex++);

			birthPlace = new TextField<String>();
			birthPlace.setFieldLabel("Birth Place");
			birthPlace.setReadOnly(true);	
			birthPlace.setTabIndex(tabIndex++);	
			
			birthOrder = new NumberField();
			birthOrder.setFieldLabel("Birth Order");
			birthOrder.setAllowDecimals(false);
			birthOrder.setAllowNegative(false);
			birthOrder.setReadOnly(true);	
			birthOrder.setTabIndex(tabIndex++);	
			 
			multiBirthInd = new ComboBox<Confirm>();
			multiBirthInd.setFieldLabel("Multiple Birth Indicator");
			multiBirthInd.setEmptyText("Select a Yes or No...");
			multiBirthInd.setDisplayField("name");
			multiBirthInd.setWidth(100);
			multiBirthInd.setStore(confirms);
			multiBirthInd.setTypeAhead(true);
			multiBirthInd.setTriggerAction(TriggerAction.ALL);
			multiBirthInd.setEditable(false);
			multiBirthInd.getListView().disableEvents(true);	
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
			phoneCountryCode.setReadOnly(true);			
			phoneCountryCode.setTabIndex(tabIndex++);	
			
			phoneAreaCode = new TextField<String>();
			phoneAreaCode.setFieldLabel("Phone Area Code");	
			phoneAreaCode.setReadOnly(true);
			phoneAreaCode.setTabIndex(tabIndex++);
			
			phoneNumber = new TextField<String>();
			phoneNumber.setFieldLabel("Phone Number");	
			phoneNumber.setToolTip("xxx-xxxx");
			phoneNumber.setReadOnly(true);
			phoneNumber.setTabIndex(tabIndex++);
	
			phoneExt = new TextField<String>();
			phoneExt.setFieldLabel("Phone Extension");	
			phoneExt.setReadOnly(true);
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
			gender.setEditable(false);
			gender.getListView().disableEvents(true);	
			gender.setTabIndex(tabIndex++);

			degree = new TextField<String>();
			degree.setFieldLabel("Degree");	
			degree.setReadOnly(true);
			degree.setTabIndex(tabIndex++);
			
			maidenName = new TextField<String>();
			maidenName.setFieldLabel("Mother's Maiden Name");	
			maidenName.setReadOnly(true);
			maidenName.setTabIndex(tabIndex++);
	
			maritalStatus = new ComboBox<Confirm>();
			maritalStatus.setFieldLabel("Marital Status");
			maritalStatus.setEmptyText("Select a Yes or No...");
			maritalStatus.setDisplayField("name");
			maritalStatus.setWidth(100);
			maritalStatus.setStore(confirms);
			maritalStatus.setTypeAhead(true);
			maritalStatus.setTriggerAction(TriggerAction.ALL);
			maritalStatus.setEditable(false);
			maritalStatus.getListView().disableEvents(true);	
			maritalStatus.setTabIndex(tabIndex++);
			
			deathInd = new ComboBox<Confirm>();
			deathInd.setFieldLabel("Death Indicator");
			deathInd.setEmptyText("Select a Yes or No...");
			deathInd.setDisplayField("name");
			deathInd.setWidth(100);
			deathInd.setStore(confirms);
			deathInd.setTypeAhead(true);
			deathInd.setTriggerAction(TriggerAction.ALL);
			deathInd.setEditable(false);
			deathInd.getListView().disableEvents(true);	
			deathInd.setTabIndex(tabIndex++);
			
			DateTimePropertyEditor dateFormat = new DateTimePropertyEditor("yyyy-MM-dd HH:mm");
			deathTime = new DateField();
			deathTime.setFieldLabel("Death Time");
			deathTime.setToolTip("yyyy-MM-dd HH:mm");
			deathTime.setPropertyEditor(dateFormat);
			deathTime.setEditable(false);
			deathTime.getDatePicker().disableEvents(true);
			deathTime.setTabIndex(tabIndex++);
			
			ssn = new TextField<String>();
			ssn.setFieldLabel("Social Security Number");
			ssn.setReadOnly(true);
			ssn.setTabIndex(tabIndex++);
			
			email = new TextField<String>();
			email.setFieldLabel("Email");	
			email.setRegex(InputFormat.EMAIL_FORMATS);
	        email.getMessages().setRegexText("Invalid email format");
	        email.setToolTip("xyz@example.com");	
			email.setReadOnly(true);
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
	
    final Listener<MessageBoxEvent> listenConfirmDelete = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();  
          if( btn.getText().equals("Yes")) {
        	  if ( advancedSearch ) {
				  // Info.display("Information:", "Advanced Update");	
        		  updatePerson.setLinkedPersons(null);
				  controller.handleEvent(new AppEvent(AppEvents.AdvancedDeletePersonInitiate, updatePerson));			
			  } else {					
				  // Info.display("Information:", "Basic Update");						
				  controller.handleEvent(new AppEvent(AppEvents.BasicDeletePersonInitiate, updatePerson));		
			  }
			  submitButton.mask();       	  
          }
        }  
    };  
    
	private FormPanel setupButtonPanel(int tabIndex) {
		FormPanel buttonPanel = new FormPanel(); 
		buttonPanel.setHeaderVisible(false);
		buttonPanel.setBodyBorder(false);
		buttonPanel.setStyleAttribute("paddingRight", "10px");
		buttonPanel.setButtonAlign(HorizontalAlignment.CENTER);		

		submitButton = new Button("Confirm Delete", new SelectionListener<ButtonEvent>() {
		
			@Override
			public void componentSelected(ButtonEvent ce) {
				
        	  	MessageBox.confirm("Confirm", "Delete operation cannot be undone. Are you sure you want to delete this person?", listenConfirmDelete); 
			}		
		});
		
		cancelButton = new Button("Cancel", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if ( advancedSearch ) {
					controller.handleEvent(new AppEvent(AppEvents.AdvancedDeletePersonCancel, updatePerson));
				} else {
					controller.handleEvent(new AppEvent(AppEvents.BasicDeletePersonCancel, updatePerson));			
				}
			}
		});
		
		submitButton.setTabIndex(tabIndex++);	
		cancelButton.setTabIndex(tabIndex++);	
		
		buttonPanel.getButtonBar().setSpacing(15);
		buttonPanel.addButton(submitButton);
		buttonPanel.addButton(cancelButton);
		
		return buttonPanel;
	}
	
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		
//		container = new LayoutContainer();
		container = new ContentPanel ();
		container.setLayout(new BorderLayout());
		container.setHeading("Delete Person");  
		
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
		
				leftFormPanel = setupPersonLeftForm("", 2);
				rightFormPanel = setupPersonRightForm("", 17);
				
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
}
