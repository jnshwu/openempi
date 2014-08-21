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
import java.util.Date;
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
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.RowEditorEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
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
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

public class UpdatePersonView extends View
{
	public static final String BASIC_SEARCH = "Basic Search";
	public static final String ADVANCED_SEARCH = "Advanced Search";
	public static final String ADD_PERSON = "Add Person";
	
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
	private int rowSelected;
	private ListStore<PersonIdentifierWeb> identifierStore = new ListStore<PersonIdentifierWeb>();	
	private Grid<PersonIdentifierWeb> identifierGrid;	
	private TextField<String> universalIdentifierText;
	private TextField<String> universalIdentifierTypeCodeText;
	private Button addIdentifierButton;
	private Button removeIdentifierButton;
	
	private Status status;
	private Button checkDuplicateButton;
	private Button submitButton;
	private Button cancelButton;
    
	private PersonWeb updatePerson = null;
	private String fromPage;
	
	ListStore<Gender> genders = new ListStore<Gender>();
	ListStore<State> states = new ListStore<State>();
	ListStore<Country> countries = new ListStore<Country>();
	ListStore<Confirm> confirms = new ListStore<Confirm>();
	
	private Dialog checkDuplicateDialog;
	private Grid<PersonWeb> grid;
	private ListStore<PersonWeb> personStore = new ListStore<PersonWeb>();
	
	public UpdatePersonView(Controller controller) {
		super(controller);
		genders.add(InputFormData.getGenders());
		states.add(InputFormData.getStates());
		countries.add(InputFormData.getCountry());
		confirms.add(InputFormData.getConfirm());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		
		if (event.getType() == AppEvents.BasicUpdatePersonRenderData || event.getType() == AppEvents.AdvancedUpdatePersonRenderData ||
			event.getType() == AppEvents.AddToUpdatePersonRenderData) {
			if( event.getType() == AppEvents.BasicUpdatePersonRenderData ) {
				fromPage = BASIC_SEARCH;
			} else  if (event.getType() == AppEvents.AdvancedUpdatePersonRenderData ){
				fromPage = ADVANCED_SEARCH;	
			} else {
				// Info.display("Information: ", "From ADD_PERSON");
				fromPage = ADD_PERSON;					
			}
			
			initUI();	
				
			clearFormFields();	
			updatePerson = (PersonWeb) event.getData();
			// Info.display("Person IDS:", ""+ updatePerson.getPersonId());	
			displayRecords(updatePerson);	
		} else if (event.getType() == AppEvents.BasicUpdatePersonComplete || event.getType() == AppEvents.AdvancedUpdatePersonComplete) {	
			
			updatePerson = (PersonWeb) event.getData();		
	        MessageBox.alert("Information", "Person was successfully updated", listenInfoMsg);  	        
			// Info.display("Person Date changed:", ""+ updatePerson.getDateChanged());		  
	        
		} else if (event.getType() == AppEvents.CheckDuplicatePersonComplete) {			
			
			List<PersonWeb> personList =  (List<PersonWeb>) event.getData();			
			status.hide();
			checkDuplicateButton.unmask();  
			
			if( personList != null && personList.size() > 0 ) {	
				for (PersonWeb personWeb : personList) {
					if( personWeb.getPersonId().intValue() == updatePerson.getPersonId().intValue() ) {
						personList.remove(personWeb);
						break;
					}
				}
				
				if( personList.size() > 0 ) {				
					personStore.removeAll();
					personStore.add(personList);
					
					checkDuplicateDialog.show();
				} else {
			        MessageBox.alert("Information", "No duplicate Persons found", null);  						
				}
			} else {
		        MessageBox.alert("Information", "No duplicate Persons found", null);  				
			}	 	
	        	        
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, listenFailureMsg);  			
		}
	}

    final Listener<MessageBoxEvent> listenInfoMsg = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();   
          if( btn.getText().equals("OK")) {           	  
  			  // clearFormFields();    		
 			  status.hide();
  			  submitButton.unmask();  
  			  
        	  // back to search page
			  if ( fromPage.equals(ADVANCED_SEARCH) ) {
					controller.handleEvent(new AppEvent(AppEvents.AdvancedUpdatePersonFinished, updatePerson));
			  } else if ( fromPage.equals(BASIC_SEARCH) ) {
					controller.handleEvent(new AppEvent(AppEvents.BasicUpdatePersonFinished, updatePerson));	
  			  } else {		
					Dispatcher.forwardEvent(AppEvents.AddToUpdatePersonViewFinished, updatePerson);	
  			  }   
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
	
	private Grid<PersonIdentifierWeb> setupPersonIdentifierGrid( final ListStore<PersonIdentifierWeb> identifierStore, int tabIndex ) {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
					    
	    // Columns
		ColumnConfig column;

		column = new ColumnConfig();
		column.setId("identifier");  
		column.setHeader("Identifier");
		column.setWidth(220);		
	    TextField<String> identifierText = new TextField<String>();  
	    identifierText.setAllowBlank(false);
	    identifierText.setValidator(new Validator() {	    	
				public String validate(Field<?> field, String value) {					
					   // check value duplicated
					   for (int i=0; i<identifierStore.getCount(); i++){
						    if( i != rowSelected ) {							   
						        PersonIdentifierWeb identifierInStore = identifierStore.getAt(i);	
							     if( value.equals(identifierInStore.getIdentifier())) {
							    	 return "Duplicate identifier.";
							     }
							}					        
					   }
					   return null;
				}
	    });
	    
	    column.setEditor(new CellEditor(identifierText));  	    
		configs.add(column);

		column = new ColumnConfig();
		column.setId("identifierDomainName");  
		column.setHeader("Domain Name");
		column.setWidth(160);
	    TextField<String> identifierDomainNameText = new TextField<String>();  
	    identifierDomainNameText.setValidator(new Validator() {	    	
				public String validate(Field<?> field, String value) {					
					   // check value duplicated
					   for (int i=0; i<identifierStore.getCount(); i++){
						    if( i != rowSelected ) {							   
						        PersonIdentifierWeb identifierInStore = identifierStore.getAt(i);	
							     if( value.equals(identifierInStore.getIdentifierDomainName())) {
							    	 return "Duplicate domain name.";
							     }
							}					        
					   }
					   return null;
				}
	    });
	    column.setEditor(new CellEditor(identifierDomainNameText));  	
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("namespaceIdentifier");  
		column.setHeader("Namespace Identifier");
		column.setWidth(160);
	    TextField<String> namespaceIdentifierText = new TextField<String>();  
	    namespaceIdentifierText.setValidator(new Validator() {	    	
				public String validate(Field<?> field, String value) {					
					   // check value duplicated
					   for (int i=0; i<identifierStore.getCount(); i++){
						    if( i != rowSelected ) {							   
						        PersonIdentifierWeb identifierInStore = identifierStore.getAt(i);	
							     if( value.equals(identifierInStore.getNamespaceIdentifier())) {
							    	 return "Duplicate namespace identifier.";
							     }
							}					        
					   }
					   return null;
				}
	    });
	    column.setEditor(new CellEditor(namespaceIdentifierText));  	
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("universalIdentifier");  
		column.setHeader("Universal Identifier");  
		column.setWidth(160);
	    universalIdentifierText = new TextField<String>();  
	    universalIdentifierText.setValidator(new Validator() {	    	
				public String validate(Field<?> field, String value) {					
					   // check value duplicated
					   for (int i=0; i<identifierStore.getCount(); i++){
						    if( i != rowSelected ) {							   
						        PersonIdentifierWeb identifierInStore = identifierStore.getAt(i);		
						        // checking unique for combining universal identifier and type
							    if( value.equals(identifierInStore.getUniversalIdentifier())) {
									String typeStr = universalIdentifierTypeCodeText.getValue();									
									if( typeStr.equals(identifierInStore.getUniversalIdentifierTypeCode())) {
										return "Duplicate universal identifier and type.";
									}
							    }
							}					        
					   }
					   return null;
				}
	    });
	    column.setEditor(new CellEditor(universalIdentifierText));  	
		configs.add(column);
	
		column = new ColumnConfig();
		column.setId("universalIdentifierTypeCode");  
		column.setHeader("Universal Identifier Type");  
		column.setWidth(130);
	    universalIdentifierTypeCodeText = new TextField<String>(); 
	    column.setEditor(new CellEditor(universalIdentifierTypeCodeText));  	
		configs.add(column);

		column = new ColumnConfig();
		column.setId("dateCreated");  
		column.setHeader("Date Created");
		column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
		column.setWidth(120);
		column.setHidden(true);
		configs.add(column);
		
		
		ColumnModel cm = new ColumnModel(configs);
	    RowEditor<PersonIdentifierWeb> rowEditor = new RowEditor<PersonIdentifierWeb>(); 
	    rowEditor.setClicksToEdit(ClicksToEdit.TWO);
	    
	    rowEditor.addListener(Events.BeforeEdit, 
	    		new Listener<RowEditorEvent>() {
				   public void handleEvent(final RowEditorEvent be) {	
					   
					   // get selected row index
					   rowSelected = be.getRowIndex();
	               }
           		}
        );
	    
		identifierGrid = new Grid<PersonIdentifierWeb>(identifierStore, cm);
		
		identifierGrid.setStyleAttribute("borderTop", "none");
		identifierGrid.setBorders(true);
		identifierGrid.setStripeRows(true); 
		identifierGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		identifierGrid.addPlugin(rowEditor);  
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

	private PersonWeb copyPersonFromGUI(PersonWeb updatePerson) {
		PersonWeb person = copyPerson(updatePerson);		
		
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
		} else {
			person.setMultipleBirthInd(null);
		}
		
		// Address
		person.setAddress1(address1.getValue());
		person.setAddress2(address2.getValue());
		
		person.setCity(city.getValue());
		if (state.getValue() != null) {
			person.setState(state.getValue().getAbbr());
		} else {
			person.setState(null);					
		}
		
		person.setPostalCode(zip.getValue());
		
		Country countryValue = country.getValue();
		if (countryValue != null) {
			person.setCountry(countryValue.getName());
			person.setCountryCode(countryValue.getCode());
		} else {
			person.setCountry(null);
			person.setCountryCode(null);					
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
		} else {
			person.setGender(null);					
		}
		person.setDegree(degree.getValue());
		person.setMothersMaidenName(maidenName.getValue());

		Confirm maritalStatusValue = maritalStatus.getValue();
		if (maritalStatusValue != null) {
			person.setMaritalStatusCode(maritalStatusValue.getCode());
		} else {
			person.setMaritalStatusCode(null);					
		}
		
		person.setDeathTime( Utility.DateTimeToString(deathTime.getValue()));
		// Info.display("Death time:", deathTime.getValue().toString());	

		Confirm deathIndValue = deathInd.getValue();
		if (deathIndValue != null) {
			person.setDeathInd(deathIndValue.getCode());
		} else {
			person.setDeathInd(null);					
		}
		
		person.setSsn(ssn.getValue());
		person.setEmail(email.getValue());				

		
		// Identifiers
		// updatePerson.getPersonIdentifiers().clear();
		Set<PersonIdentifierWeb> ids = new HashSet<PersonIdentifierWeb>();
		for (int i=0;i<identifierStore.getCount();i++){
		     PersonIdentifierWeb identifier = identifierStore.getAt(i);
			 if( identifier.getDateCreated() == null ) {
				 identifier.setDateCreated( new Date());
			 }
		     ids.add(identifier);
		}
		person.setPersonIdentifiers(ids);
		
		return 	person;
	}
	
	private FormPanel setupButtonPanel(int tabIndex) {
		FormPanel buttonPanel = new FormPanel(); 
		buttonPanel.setHeaderVisible(false);
		buttonPanel.setBodyBorder(false);
		buttonPanel.setStyleAttribute("paddingRight", "10px");
		buttonPanel.setButtonAlign(HorizontalAlignment.CENTER);		

		submitButton = new Button("Update", new SelectionListener<ButtonEvent>() {
		
			@Override
			public void componentSelected(ButtonEvent ce) {
				// Info.display("test:", "save componentSelected");	
				if( updatePerson == null )
					updatePerson = new PersonWeb();
				
				if( !leftFormPanel.isValid() || !rightFormPanel.isValid() ) {
					
					// Info.display("test:", "Invalid fields");	
					return;
				}
				
				PersonWeb updatingPerson = copyPersonFromGUI(updatePerson);
				
				if ( fromPage.equals(ADVANCED_SEARCH) ) {
					// Info.display("Information:", "Advanced Update");	
					updatingPerson.setLinkedPersons(null);
					controller.handleEvent(new AppEvent(AppEvents.AdvancedUpdatePersonInitiate, updatingPerson));			
				} else {					
					// Info.display("Information:", "Basic Update");						
 					controller.handleEvent(new AppEvent(AppEvents.BasicUpdatePersonInitiate, updatingPerson));		
				}
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
				
				PersonWeb person = copyPersonFromGUI(updatePerson);
				
				controller.handleEvent(new AppEvent(AppEvents.CheckDuplicatePersonInitiate, person));
				status.show();
				checkDuplicateButton.mask();
			}
		});
		
		cancelButton = new Button("Cancel", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if ( fromPage.equals(ADVANCED_SEARCH) ) {
					controller.handleEvent(new AppEvent(AppEvents.AdvancedUpdatePersonCancel, updatePerson));
				} else if ( fromPage.equals(BASIC_SEARCH) ){
					controller.handleEvent(new AppEvent(AppEvents.BasicUpdatePersonCancel, updatePerson));			
				} else {
					Dispatcher.forwardEvent(AppEvents.AddToUpdatePersonViewCancel, updatePerson);						
				}

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
		container.setHeading("Update Person");  
		
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
