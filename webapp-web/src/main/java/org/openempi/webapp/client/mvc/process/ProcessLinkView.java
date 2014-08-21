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
package org.openempi.webapp.client.mvc.process;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.ReviewRecordPairWeb;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.mvc.View;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldSetEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.HeaderGroupConfig;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class ProcessLinkView extends View
{
	private Grid<ReviewRecordPairWeb> grid;
	private ListStore<ReviewRecordPairWeb> pairStore = new ListStore<ReviewRecordPairWeb>();
	
//	private Grid<PersonIdentifierWeb> identifierGrid;
	private ListStore<PersonIdentifierWeb> leftIdentifierStore = new ListStore<PersonIdentifierWeb>();
	private ListStore<PersonIdentifierWeb> rightIdentifierStore = new ListStore<PersonIdentifierWeb>();
	
//	private LayoutContainer container;
	private ContentPanel container;	
	private LayoutContainer gridContainer;
	private LayoutContainer formButtonContainer;
	
	private LayoutContainer formContainer;
	private FormPanel leftFormPanel; 
	private FormPanel rightFormPanel; 

	private FieldSet leftNamefieldSet;  
	private FieldSet leftAddressfieldSet;  
	private FieldSet leftAddressOtherfieldSet;  
	private FieldSet leftPhonefieldSet;  
	private FieldSet leftBirthfieldSet;  
	private FieldSet leftOtherfieldSet;  
	private FieldSet rightNamefieldSet;  
	private FieldSet rightAddressfieldSet;
	private FieldSet rightAddressOtherfieldSet;  
	private FieldSet rightPhonefieldSet;  
	private FieldSet rightBirthfieldSet;  
	private FieldSet rightOtherfieldSet;  
    
	private FormPanel buttonPanel; 
	private Status status;
	private Button linkButton;
	private Button unlinkButton;
	private Button reloadLinkPersonButton;
    
	private FormBinding leftFormBindings;  
	private FormBinding rightFormBindings;
	
	private ReviewRecordPairWeb selectedPair;
	private NumberField maxRecords;
	
	public ProcessLinkView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.ProcessLinkView) {
			grid = null;
			initUI();
				
			showDefaultCursor();
			
			displayRecords((List<ReviewRecordPairWeb>) event.getData());
			
			if( Registry.get(Constants.MAX_RECORD_DISPLAING) != null ) {				
				maxRecords.setValue(((Integer)Registry.get(Constants.MAX_RECORD_DISPLAING)).intValue());	
			}
					    
		} else if (event.getType() == AppEvents.ProcessPairLinkedView) {	
			
				int selectionIndex = grid.getStore().indexOf(selectedPair);
				
				// remove selected pair
				pairStore.remove(selectedPair);
				
				// switch to the next item on the grid and display
				grid.getSelectionModel().select(selectionIndex, false);
				ReviewRecordPairWeb field = grid.getSelectionModel().getSelectedItem();
				if (field != null) {
				    selectedPair = field; 
				    
				    leftIdentifierStore.removeAll();
					leftIdentifierStore.add( getListIdentifiers(field.getLeftPerson()));	
					
				    rightIdentifierStore.removeAll();
					rightIdentifierStore.add( getListIdentifiers(field.getRightPerson()));	
					
				    leftFormBindings.bind((ModelData) field);  		
				    rightFormBindings.bind((ModelData) field);
				   
					linkButton.enable();
					unlinkButton.enable();		
				} else {
					selectedPair = null;
					leftFormBindings.unbind();  
					rightFormBindings.unbind(); 
	
					
				    leftIdentifierStore.removeAll();    						
				    rightIdentifierStore.removeAll();
				    
					linkButton.disable();
					unlinkButton.disable();		
				}				
		        Info.display("Confirm :", " Successfully linked"); 
		        
		} else if (event.getType() == AppEvents.ProcessPairUnlinkedView) {
			
				int selectionIndex = grid.getStore().indexOf(selectedPair);		
				
				pairStore.remove(selectedPair);

				grid.getSelectionModel().select(selectionIndex, false);
				ReviewRecordPairWeb field = grid.getSelectionModel().getSelectedItem();
				if (field != null) {
				    selectedPair = field; 
				    
				    leftIdentifierStore.removeAll();
					leftIdentifierStore.add( getListIdentifiers(field.getLeftPerson()));	
					
				    rightIdentifierStore.removeAll();
					rightIdentifierStore.add( getListIdentifiers(field.getRightPerson()));	
					
				    leftFormBindings.bind((ModelData) field);  		
				    rightFormBindings.bind((ModelData) field);
				   
					linkButton.enable();
					unlinkButton.enable();		
				} else {
					selectedPair = null;
					leftFormBindings.unbind();  
					rightFormBindings.unbind(); 
				
					
				    leftIdentifierStore.removeAll();    						
				    rightIdentifierStore.removeAll();
				    
					linkButton.disable();
					unlinkButton.disable();		
				}				
		        Info.display("Confirm :", " Successfully unlinked"); 
		        
		} else if (event.getType() == AppEvents.Error) {
			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  	
	        
			showDefaultCursor();
		} 
		status.hide();
		linkButton.unmask();  
		unlinkButton.unmask();   
		container.layout();
	}

	private void displayRecords(List<ReviewRecordPairWeb> pairs) {
		if( grid == null ) {
			setupPersonGrid();
		}
		// Info.display("Warning", "size: "+pairs.size());	
		
		pairStore.removeAll();
		pairStore.add(pairs);		
		container.layout();
	}
	
	private void setupPersonGrid() {
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Link Persons");
		cp.setHeaderVisible(false);
		cp.setBodyBorder(false);
		cp.setLayout(new FillLayout());
//		cp.setSize(1050, 200);
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
	    
	    // Columns
		ColumnConfig column;

		column = new ColumnConfig();
		column.setId("dateCreated");  
		column.setHeader("Date Created");
//		column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
		column.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
		column.setWidth(150);
		configs.add(column);

		// Left person
		column = new ColumnConfig();
		column.setId("leftPerson.givenName");  
		column.setHeader("First Name");
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("leftPerson.familyName");  
		column.setHeader("Last Name");  
		column.setWidth(120);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("leftPerson.dateOfBirth");  
		column.setHeader("Date of Birth");
		column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
		column.setWidth(120);
		configs.add(column);
		
		// Right person
		column = new ColumnConfig();
		column.setId("rightPerson.givenName");  
		column.setHeader("First Name");
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("rightPerson.familyName");  
		column.setHeader("Last Name");  
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("rightPerson.dateOfBirth");  
		column.setHeader("Date of Birth");
		column.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
		column.setWidth(120);
		configs.add(column);
		
		// Link Source
		column = new ColumnConfig();
		column.setId("linkSource.sourceName");  
		column.setHeader("Link Source");
		column.setWidth(150);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);
	    cm.addHeaderGroup(0, 1, new HeaderGroupConfig("Left Person", 1, 3));  	      
	    cm.addHeaderGroup(0, 4, new HeaderGroupConfig("Right Person", 1, 3));  
	    
		grid = new Grid<ReviewRecordPairWeb>(pairStore, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true); 
		// set single selection
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		// selection event
		grid.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<ReviewRecordPairWeb>>() {
					public void handleEvent(SelectionChangedEvent<ReviewRecordPairWeb> be) {	
						ReviewRecordPairWeb field = be.getSelectedItem();
						if (field != null) {										
						    // Info.display("test", field.getLeftPerson().getGivenName()+" "+field.getLeftPerson().getMiddleName()+" "+field.getLeftPerson().getFamilyName());		
						    
						    selectedPair = field; 
						    
						    leftIdentifierStore.removeAll();
    						leftIdentifierStore.add( getListIdentifiers(field.getLeftPerson()));	
    						
						    rightIdentifierStore.removeAll();
    						rightIdentifierStore.add( getListIdentifiers(field.getRightPerson()));	
							
				            leftFormBindings.bind((ModelData) field);  		
				            rightFormBindings.bind((ModelData) field);
				           
				    		linkButton.enable();
				    		unlinkButton.enable();		
						} else { 
							selectedPair = null;
							leftFormBindings.unbind();  
							rightFormBindings.unbind(); 
							
						    leftIdentifierStore.removeAll();    						
						    rightIdentifierStore.removeAll();
						    
							linkButton.disable();
							unlinkButton.disable();		
						}
					}
				});

		LayoutContainer buttonContainer = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.END);
		buttonContainer.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 3, 0, 2));
		HBoxLayoutData textLayoutData = new HBoxLayoutData(new Margins(0, 18, 0, 2));

		Label display = new Label("Displaying: ");
		display.setStyleAttribute("font-size","11px");
		
		maxRecords = new NumberField();
		maxRecords.setAllowDecimals(false);
		maxRecords.setAllowNegative(false);
		maxRecords.setMaxValue(200);
		maxRecords.setWidth(30);
		maxRecords.setToolTip("Displaying maximum records");
		
		reloadLinkPersonButton = new Button(" Refresh Link Persons", IconHelper.create("images/arrow_refresh.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  
	        	  if( maxRecords.isValid() ) {
		        	  showWaitCursor();
		        	  
					  Registry.register(Constants.MAX_RECORD_DISPLAING, new Integer(maxRecords.getValue().intValue()));
					  controller.handleEvent(new AppEvent(AppEvents.ProcessLinkView, null));
	        	  }
	          }
	    });
		buttonContainer.add(display);
		buttonContainer.add(maxRecords, textLayoutData);
		buttonContainer.add(reloadLinkPersonButton, layoutData);
		
		cp.setTopComponent(buttonContainer);
		cp.add(grid);
		gridContainer.add(cp);
	}
	
	public List<PersonIdentifierWeb> getListIdentifiers(PersonWeb person) {
		List<PersonIdentifierWeb> stocks = new ArrayList<PersonIdentifierWeb>();

		if(person.getPersonIdentifiers() != null) {
			for( PersonIdentifierWeb identifier : person.getPersonIdentifiers()) {
				 stocks.add(identifier);			
			}
		}
		return stocks;
	}
	
	private Grid<PersonIdentifierWeb> setupPersonIdentifierGrid( ListStore<PersonIdentifierWeb> identifierStore, String person  ) {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
					    
	    // Columns
		ColumnConfig column;

		column = new ColumnConfig();
		column.setId("identifier");  
		column.setHeader("Identifier");
		column.setWidth(110);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("namespaceIdentifier");  
		column.setHeader("Namespace Identifier");
		column.setWidth(120);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("universalIdentifier");  
		column.setHeader("Universal Identifier");  
		column.setWidth(120);
		configs.add(column);
	
		column = new ColumnConfig();
		column.setId("universalIdentifierTypeCode");  
		column.setHeader("Universal Identifier Type");  
		column.setWidth(130);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);
		cm.addHeaderGroup(0, 0, new HeaderGroupConfig( person, 1, 4)); 
		Grid<PersonIdentifierWeb> identifierGrid = new Grid<PersonIdentifierWeb>(identifierStore, cm);
		
		identifierGrid.setStyleAttribute("borderTop", "none");
		identifierGrid.setBorders(true);
		identifierGrid.setBorders(true);
		identifierGrid.setStripeRows(true); 
		identifierGrid.setWidth(505); // 480 + vertical scroll bar space
		identifierGrid.setHeight(86);
		
		return identifierGrid;
	}
	
	private FormPanel setupLeftPersonForm(String title, String person) {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setFrame(true);  
	    formPanel.setLabelWidth(150);
//	    formPanel.setLabelAlign(LabelAlign.RIGHT); // default is LEFT
	    formPanel.setTitle(title);  

	    
	    // Name fields
	    leftNamefieldSet = new FieldSet();  
	    leftNamefieldSet.setHeading("Name");  
	    leftNamefieldSet.setCollapsible(true);  
	    leftNamefieldSet.setBorders(false);  
	    FormLayout namelayout = new FormLayout();  
	    namelayout.setLabelWidth(150);  
	    leftNamefieldSet.setLayout(namelayout);  
	    
			TextField<String> firstName = new TextField<String>();
			firstName.setName( person+"givenName");  
			firstName.setFieldLabel("First Name");
			firstName.setReadOnly(true);
	
			TextField<String> middleName = new TextField<String>();
			middleName.setName( person+"middleName");  
			middleName.setFieldLabel("Middle Name");
			middleName.setReadOnly(true);
			
			TextField<String> lastName = new TextField<String>();
			lastName.setName( person+"familyName");  
			lastName.setFieldLabel("Last Name");	
			lastName.setReadOnly(true);

			TextField<String> prefix = new TextField<String>();
			prefix.setName( person+"prefix");  
			prefix.setFieldLabel("Prefix");	
			prefix.setReadOnly(true);
		
			TextField<String> suffix = new TextField<String>();
			suffix.setName( person+"suffix");  
			suffix.setFieldLabel("Suffix");	
			suffix.setReadOnly(true);
			
			leftNamefieldSet.add(firstName);
			leftNamefieldSet.add(middleName);
			leftNamefieldSet.add(lastName);			
			leftNamefieldSet.add(prefix);		
			leftNamefieldSet.add(suffix);		
			
			//Field listeners
			leftNamefieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {	
					rightNamefieldSet.collapse();
					// Info.display("test", "right Collapse");							
				}	
			});
			
			leftNamefieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {		
					rightNamefieldSet.expand();
					// Info.display("test", "right Expanded");							
				}	
			});		
			
	    // Address fields
	    leftAddressfieldSet = new FieldSet();  
	    leftAddressfieldSet.setHeading("Address");  
	    leftAddressfieldSet.setCollapsible(true);  
	    leftAddressfieldSet.setBorders(false);  
	    FormLayout addresslayout = new FormLayout();  
	    addresslayout.setLabelWidth(150);  
	    leftAddressfieldSet.setLayout(addresslayout);  
	    
			TextField<String> address1 = new TextField<String>();
			address1.setName( person+"address1");  
			address1.setFieldLabel("Address 1");	
			address1.setReadOnly(true);

			TextField<String> address2 = new TextField<String>();
			address2.setName( person+"address2");  
			address2.setFieldLabel("Address 2");	
			address2.setReadOnly(true);
			
			TextField<String> city = new TextField<String>();
			city.setName( person+"city");  
			city.setFieldLabel("City");	
			city.setReadOnly(true);
	
			TextField<String> state = new TextField<String>();
			state.setName( person+"state");  
			state.setFieldLabel("State");	
			state.setReadOnly(true);
	
			TextField<String> postalCode = new TextField<String>();
			postalCode.setName( person+"postalCode");  
			postalCode.setFieldLabel("Zip Code");	
			postalCode.setReadOnly(true);
	
			TextField<String> country = new TextField<String>();
			country.setName( person+"country");  
			country.setFieldLabel("Country");	
			country.setReadOnly(true);
			
			leftAddressfieldSet.add(address1);
			leftAddressfieldSet.add(address2);
			leftAddressfieldSet.add(city);
			leftAddressfieldSet.add(state);
			leftAddressfieldSet.add(postalCode);
			leftAddressfieldSet.add(country);		

			//Field listeners
			leftAddressfieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {	
					rightAddressfieldSet.collapse();						
				}	
			});
			
			leftAddressfieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {		
					rightAddressfieldSet.expand();					
				}	
			});		

		// Address Other fields
		leftAddressOtherfieldSet = new FieldSet();  
		leftAddressOtherfieldSet.setHeading("Address Other");  
		leftAddressOtherfieldSet.setCollapsible(true);  
		leftAddressOtherfieldSet.setBorders(false);  
		FormLayout addressOtherlayout = new FormLayout();  
		addressOtherlayout.setLabelWidth(150);  
		leftAddressOtherfieldSet.setLayout(addressOtherlayout);  
		    
			TextField<String> village = new TextField<String>();
			village.setName( person+"village");  
			village.setFieldLabel("Village");	
			village.setReadOnly(true);

			TextField<String> villageId = new TextField<String>();
			villageId.setName( person+"villageId");  
			villageId.setFieldLabel("Village ID");	
			villageId.setReadOnly(true);
			
			TextField<String> sector = new TextField<String>();
			sector.setName( person+"sector");  
			sector.setFieldLabel("Sector");	
			sector.setReadOnly(true);

			TextField<String> sectorId= new TextField<String>();
			sectorId.setName( person+"sectorId");  
			sectorId.setFieldLabel("Sector ID");	
			sectorId.setReadOnly(true);
			
			TextField<String> cell = new TextField<String>();
			cell.setName( person+"cell");  
			cell.setFieldLabel("Cell");	
			cell.setReadOnly(true);

			TextField<String> cellId = new TextField<String>();
			cellId.setName( person+"cellId");  
			cellId.setFieldLabel("Cell ID");	
			cellId.setReadOnly(true);
			
			TextField<String> district = new TextField<String>();
			district.setName( person+"district");  
			district.setFieldLabel("District");	
			district.setReadOnly(true);

			TextField<String> districtId = new TextField<String>();
			districtId.setName( person+"districtId");  
			districtId.setFieldLabel("District ID");	
			districtId.setReadOnly(true);
			
			TextField<String> province = new TextField<String>();
			province.setName( person+"province");  
			province.setFieldLabel("Province");	
			province.setReadOnly(true);
				
			leftAddressOtherfieldSet.add(village);
			leftAddressOtherfieldSet.add(villageId);
			leftAddressOtherfieldSet.add(sector);
			leftAddressOtherfieldSet.add(sectorId);
			leftAddressOtherfieldSet.add(cell);
			leftAddressOtherfieldSet.add(cellId);
			leftAddressOtherfieldSet.add(district);
			leftAddressOtherfieldSet.add(districtId);
			leftAddressOtherfieldSet.add(province);

			//Field listeners
			leftAddressOtherfieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
					public void handleEvent(FieldSetEvent be) {	
						rightAddressOtherfieldSet.collapse();						
					}	
			});
				
			leftAddressOtherfieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
					public void handleEvent(FieldSetEvent be) {		
						rightAddressOtherfieldSet.expand();					
					}	
			});		
				
	    // Phone fields
	    leftPhonefieldSet = new FieldSet();  
	    leftPhonefieldSet.setHeading("Phone");  
	    leftPhonefieldSet.setCollapsible(true);  
	    leftPhonefieldSet.setBorders(false);  
	    FormLayout phonelayout = new FormLayout();  
	    phonelayout.setLabelWidth(150);  
	    leftPhonefieldSet.setLayout(phonelayout);  

			TextField<String> phoneCountryCode = new TextField<String>();
			phoneCountryCode.setName( person+"phoneCountryCode");  
			phoneCountryCode.setFieldLabel("Phone Country Code");	
			phoneCountryCode.setReadOnly(true);
			
			TextField<String> phoneAreaCode = new TextField<String>();
			phoneAreaCode.setName( person+"phoneAreaCode");  
			phoneAreaCode.setFieldLabel("Phone Area Code");	
			phoneAreaCode.setReadOnly(true);
			
			TextField<String> phoneNumber = new TextField<String>();
			phoneNumber.setName( person+"phoneNumber");  
			phoneNumber.setFieldLabel("Phone Number");	
			phoneNumber.setReadOnly(true);
	
			TextField<String> phoneExt = new TextField<String>();
			phoneExt.setName( person+"phoneExt");  
			phoneExt.setFieldLabel("Phone Extension");	
			phoneExt.setReadOnly(true);

			leftPhonefieldSet.add(phoneCountryCode);
			leftPhonefieldSet.add(phoneAreaCode);
			leftPhonefieldSet.add(phoneNumber);
			leftPhonefieldSet.add(phoneExt);		

			//Field listeners
			leftPhonefieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {	
					rightPhonefieldSet.collapse();	
				}	
			});
			
			leftPhonefieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {		
					rightPhonefieldSet.expand();				
				}	
			});		
			
		// Birth fields	  
		leftBirthfieldSet = new FieldSet();  
		leftBirthfieldSet.setHeading("Birth");  
		leftBirthfieldSet.setCollapsible(true);  
		leftBirthfieldSet.setBorders(false);  
		FormLayout birthlayout = new FormLayout();  
		birthlayout.setLabelWidth(150);  
		leftBirthfieldSet.setLayout(birthlayout);  

///				DateField dateOfBirth = new DateField();
				TextField<String> dateOfBirth = new TextField<String>();
				dateOfBirth.setName( person+"dateOfBirth");  
				dateOfBirth.setFieldLabel("Date of Birth");
				dateOfBirth.setReadOnly(true);	
//				dateOfBirth.getPropertyEditor().setFormat(DateTimeFormat.getFormat("MM/dd/yyyy"));  //date format as MM/dd/yyyy
//				dateOfBirth.setHideTrigger(true);													//Hide trigger button
//				dateOfBirth.setReadOnly(true);														//Text field read only and trigger button disabled	
///				dateOfBirth.setEditable(false);
///				dateOfBirth.getDatePicker().disableEvents(true);
				
				TextField<String> birthOrder = new TextField<String>();
				birthOrder.setName( person+"birthOrder");  
				birthOrder.setFieldLabel("Birth Order");
				birthOrder.setReadOnly(true);	
				
				TextField<String> birthPlace = new TextField<String>();
				birthPlace.setName( person+"birthPlace");  
				birthPlace.setFieldLabel("Birth Place");
				birthPlace.setReadOnly(true);	

				TextField<String> multiBirthInd = new TextField<String>();
				multiBirthInd.setName( person+"multipleBirthInd");  
				multiBirthInd.setFieldLabel("Multiple Birth Indicator");
				multiBirthInd.setReadOnly(true);	
				
				leftBirthfieldSet.add(dateOfBirth);
				leftBirthfieldSet.add(birthPlace);
				leftBirthfieldSet.add(birthOrder);
				leftBirthfieldSet.add(multiBirthInd);
				
				//Field listeners
				leftBirthfieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
					public void handleEvent(FieldSetEvent be) {	
						rightBirthfieldSet.collapse();	
					}	
				});
				
				leftBirthfieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
					public void handleEvent(FieldSetEvent be) {		
						rightBirthfieldSet.expand();				
					}	
				});		
				
	    // Other fields	   
		leftOtherfieldSet = new FieldSet();  
		leftOtherfieldSet.setHeading("Other");  
		leftOtherfieldSet.setCollapsible(true);  
		leftOtherfieldSet.setBorders(false);  
		FormLayout otherlayout = new FormLayout();  
		otherlayout.setLabelWidth(150);  
		leftOtherfieldSet.setLayout(otherlayout);  

			TextField<String> gender = new TextField<String>();
			gender.setName( person+"gender");  
			gender.setFieldLabel("Gender");	
			gender.setReadOnly(true);

			TextField<String> degree = new TextField<String>();
			degree.setName( person+"degree");  
			degree.setFieldLabel("Degree");	
			degree.setReadOnly(true);

			TextField<String> fatherName = new TextField<String>();
			fatherName.setName( person+"fatherName");  
			fatherName.setFieldLabel("Father Name");	
			fatherName.setReadOnly(true);

			TextField<String> motherName = new TextField<String>();
			motherName.setName( person+"motherName");  
			motherName.setFieldLabel("Mother Name");	
			motherName.setReadOnly(true);
			
			TextField<String> maidenName = new TextField<String>();
			maidenName.setName( person+"mothersMaidenName");  
			maidenName.setFieldLabel("Mother's Maiden Name");	
			maidenName.setReadOnly(true);
	
			TextField<String> maritalStatus = new TextField<String>();
			maritalStatus.setName( person+"maritalStatusCode");  
			maritalStatus.setFieldLabel("Marital Status");	
			maritalStatus.setReadOnly(true);

			TextField<String> deathInd = new TextField<String>();
			deathInd.setName( person+"deathInd");  
			deathInd.setFieldLabel("Death Indicator");	
			deathInd.setReadOnly(true);

//			DateField deathTime = new DateField();
			TextField<String> deathTime = new TextField<String>();
			deathTime.setName( person+"deathTime");  
			deathTime.setFieldLabel("Death Time");
			deathTime.setReadOnly(true);
//			deathTime.setEditable(false);
//			deathTime.getDatePicker().disableEvents(true);
			
			TextField<String> ssn = new TextField<String>();
			ssn.setName( person+"ssn");  
			ssn.setFieldLabel("Social Security Number");	
			ssn.setReadOnly(true);
			
			TextField<String> email = new TextField<String>();
			email.setName( person+"email");  
			email.setFieldLabel("Email");	
			email.setReadOnly(true);
			
			leftOtherfieldSet.add(gender);	
			leftOtherfieldSet.add(degree);			
			leftOtherfieldSet.add(maritalStatus);	
			leftOtherfieldSet.add(fatherName);
			leftOtherfieldSet.add(motherName);
			leftOtherfieldSet.add(maidenName);
			leftOtherfieldSet.add(deathInd);
			leftOtherfieldSet.add(deathTime);
			leftOtherfieldSet.add(ssn);
			leftOtherfieldSet.add(email);

			//Field listeners
			leftOtherfieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {	
					rightOtherfieldSet.collapse();	
				}	
			});
			
			leftOtherfieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {		
					rightOtherfieldSet.expand();				
				}	
			});		
			
		// add fields to Panel
/*		if( person.equals("leftPerson.") )
			identifierGrid = setupPersonIdentifierGrid(leftIdentifierStore, "Left Person");
		else
			identifierGrid = setupPersonIdentifierGrid(rightIdentifierStore, "Right Person");	
			
		formPanel.add(identifierGrid);
*/		
		formPanel.add(leftNamefieldSet); 
		formPanel.add(leftAddressfieldSet);  
		formPanel.add(leftAddressOtherfieldSet);
		formPanel.add(leftPhonefieldSet);  
		formPanel.add(leftBirthfieldSet);  
		formPanel.add(leftOtherfieldSet);  		
	    return formPanel;  
	}
	
	private FormPanel setupRightPersonForm(String title, String person) {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setFrame(true);  
	    formPanel.setLabelWidth(150);
//	    formPanel.setLabelAlign(LabelAlign.RIGHT); // default is LEFT
	    formPanel.setTitle(title);  

	    
	    // Name fields
	    rightNamefieldSet = new FieldSet();  
	    rightNamefieldSet.setHeading("Name");  
	    rightNamefieldSet.setCollapsible(true);  
	    rightNamefieldSet.setBorders(false);  
	    FormLayout namelayout = new FormLayout();  
	    namelayout.setLabelWidth(150);  
	    rightNamefieldSet.setLayout(namelayout);  
	    
			TextField<String> firstName = new TextField<String>();
			firstName.setName( person+"givenName");  
			firstName.setFieldLabel("First Name");
			firstName.setReadOnly(true);
	
			TextField<String> middleName = new TextField<String>();
			middleName.setName( person+"middleName");  
			middleName.setFieldLabel("Middle Name");
			middleName.setReadOnly(true);
			
			TextField<String> lastName = new TextField<String>();
			lastName.setName( person+"familyName");  
			lastName.setFieldLabel("Last Name");	
			lastName.setReadOnly(true);

			TextField<String> prefix = new TextField<String>();
			prefix.setName( person+"prefix");  
			prefix.setFieldLabel("Prefix");	
			prefix.setReadOnly(true);
		
			TextField<String> suffix = new TextField<String>();
			suffix.setName( person+"suffix");  
			suffix.setFieldLabel("Suffix");	
			suffix.setReadOnly(true);
			
			rightNamefieldSet.add(firstName);
			rightNamefieldSet.add(middleName);
			rightNamefieldSet.add(lastName);			
			rightNamefieldSet.add(prefix);		
			rightNamefieldSet.add(suffix);		
			
			//Field listeners
			rightNamefieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {	
					leftNamefieldSet.collapse();
					// Info.display("test", "left Collapse");							
				}	
			});
			
			rightNamefieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {		
					leftNamefieldSet.expand();
					// Info.display("test", "left Expanded");							
				}	
			});		
			
	    // Address fields
	    rightAddressfieldSet = new FieldSet();  
	    rightAddressfieldSet.setHeading("Address");  
	    rightAddressfieldSet.setCollapsible(true);  
	    rightAddressfieldSet.setBorders(false);  
	    FormLayout addresslayout = new FormLayout();  
	    addresslayout.setLabelWidth(150);  
	    rightAddressfieldSet.setLayout(addresslayout);  
	    
			TextField<String> address1 = new TextField<String>();
			address1.setName( person+"address1");  
			address1.setFieldLabel("Address 1");	
			address1.setReadOnly(true);

			TextField<String> address2 = new TextField<String>();
			address2.setName( person+"address2");  
			address2.setFieldLabel("Address 2");	
			address2.setReadOnly(true);
			
			TextField<String> city = new TextField<String>();
			city.setName( person+"city");  
			city.setFieldLabel("City");	
			city.setReadOnly(true);
	
			TextField<String> state = new TextField<String>();
			state.setName( person+"state");  
			state.setFieldLabel("State");	
			state.setReadOnly(true);
	
			TextField<String> postalCode = new TextField<String>();
			postalCode.setName( person+"postalCode");  
			postalCode.setFieldLabel("Zip Code");	
			postalCode.setReadOnly(true);
	
			TextField<String> country = new TextField<String>();
			country.setName( person+"country");  
			country.setFieldLabel("Country");	
			country.setReadOnly(true);
			
			rightAddressfieldSet.add(address1);
			rightAddressfieldSet.add(address2);
			rightAddressfieldSet.add(city);
			rightAddressfieldSet.add(state);
			rightAddressfieldSet.add(postalCode);
			rightAddressfieldSet.add(country);		

			//Field listeners
			rightAddressfieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {	
					leftAddressfieldSet.collapse();	
				}	
			});
			
			rightAddressfieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {		
					leftAddressfieldSet.expand();				
				}	
			});		

		// Address Other fields
		rightAddressOtherfieldSet = new FieldSet();  
		rightAddressOtherfieldSet.setHeading("Address Other");  
		rightAddressOtherfieldSet.setCollapsible(true);  
		rightAddressOtherfieldSet.setBorders(false);  
		FormLayout addressOtherlayout = new FormLayout();  
		addressOtherlayout.setLabelWidth(150);  
		rightAddressOtherfieldSet.setLayout(addressOtherlayout);  
		    
			TextField<String> village = new TextField<String>();
			village.setName( person+"village");  
			village.setFieldLabel("Village");	
			village.setReadOnly(true);

			TextField<String> villageId = new TextField<String>();
			villageId.setName( person+"villageId");  
			villageId.setFieldLabel("Village ID");	
			villageId.setReadOnly(true);
				
			TextField<String> sector = new TextField<String>();
			sector.setName( person+"sector");  
			sector.setFieldLabel("Sector");	
			sector.setReadOnly(true);

			TextField<String> sectorId= new TextField<String>();
			sectorId.setName( person+"sectorId");  
			sectorId.setFieldLabel("Sector ID");	
			sectorId.setReadOnly(true);
				
			TextField<String> cell = new TextField<String>();
			cell.setName( person+"cell");  
			cell.setFieldLabel("Cell");	
			cell.setReadOnly(true);
	
			TextField<String> cellId = new TextField<String>();
			cellId.setName( person+"cellId");  
			cellId.setFieldLabel("Cell ID");	
			cellId.setReadOnly(true);
				
			TextField<String> district = new TextField<String>();
			district.setName( person+"district");  
			district.setFieldLabel("District");	
			district.setReadOnly(true);

			TextField<String> districtId = new TextField<String>();
			districtId.setName( person+"districtId");  
			districtId.setFieldLabel("District ID");	
			districtId.setReadOnly(true);
				
			TextField<String> province = new TextField<String>();
			province.setName( person+"province");  
			province.setFieldLabel("Province");	
			province.setReadOnly(true);
	
				
			rightAddressOtherfieldSet.add(village);
			rightAddressOtherfieldSet.add(villageId);
			rightAddressOtherfieldSet.add(sector);
			rightAddressOtherfieldSet.add(sectorId);
			rightAddressOtherfieldSet.add(cell);
			rightAddressOtherfieldSet.add(cellId);
			rightAddressOtherfieldSet.add(district);
			rightAddressOtherfieldSet.add(districtId);
			rightAddressOtherfieldSet.add(province);

			//Field listeners
			rightAddressOtherfieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
					public void handleEvent(FieldSetEvent be) {	
						leftAddressOtherfieldSet.collapse();						
					}	
			});
				
			rightAddressOtherfieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
					public void handleEvent(FieldSetEvent be) {		
						leftAddressOtherfieldSet.expand();					
					}	
			});		
				
	    // Phone fields
	    rightPhonefieldSet = new FieldSet();  
	    rightPhonefieldSet.setHeading("Phone");  
	    rightPhonefieldSet.setCollapsible(true);  
	    rightPhonefieldSet.setBorders(false);  
	    FormLayout phonelayout = new FormLayout();  
	    phonelayout.setLabelWidth(150);  
	    rightPhonefieldSet.setLayout(phonelayout);  

			TextField<String> phoneCountryCode = new TextField<String>();
			phoneCountryCode.setName( person+"phoneCountryCode");  
			phoneCountryCode.setFieldLabel("Phone Country Code");	
			phoneCountryCode.setReadOnly(true);
			
			TextField<String> phoneAreaCode = new TextField<String>();
			phoneAreaCode.setName( person+"phoneAreaCode");  
			phoneAreaCode.setFieldLabel("Phone Area Code");	
			phoneAreaCode.setReadOnly(true);
			
			TextField<String> phoneNumber = new TextField<String>();
			phoneNumber.setName( person+"phoneNumber");  
			phoneNumber.setFieldLabel("Phone Number");	
			phoneNumber.setReadOnly(true);
	
			TextField<String> phoneExt = new TextField<String>();
			phoneExt.setName( person+"phoneExt");  
			phoneExt.setFieldLabel("Phone Extension");	
			phoneExt.setReadOnly(true);

			rightPhonefieldSet.add(phoneCountryCode);
			rightPhonefieldSet.add(phoneAreaCode);
			rightPhonefieldSet.add(phoneNumber);
			rightPhonefieldSet.add(phoneExt);		

			//Field listeners
			rightPhonefieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {	
					leftPhonefieldSet.collapse();	
				}	
			});
			
			rightPhonefieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {		
					leftPhonefieldSet.expand();				
				}	
			});	
			
		// Birth fields	  
		rightBirthfieldSet = new FieldSet();  
		rightBirthfieldSet.setHeading("Birth");  
		rightBirthfieldSet.setCollapsible(true);  
		rightBirthfieldSet.setBorders(false);  
		FormLayout birthlayout = new FormLayout();  
		birthlayout.setLabelWidth(150);  
		rightBirthfieldSet.setLayout(birthlayout);  

//				DateField dateOfBirth = new DateField();
				TextField<String> dateOfBirth = new TextField<String>();
				dateOfBirth.setName( person+"dateOfBirth");  
				dateOfBirth.setFieldLabel("Date of Birth");
				dateOfBirth.setReadOnly(true);	
//				dateOfBirth.setEditable(false);
//				dateOfBirth.getDatePicker().disableEvents(true);
		
				TextField<String> birthOrder = new TextField<String>();
				birthOrder.setName( person+"birthOrder");  
				birthOrder.setFieldLabel("Birth Order");
				birthOrder.setReadOnly(true);	
				
				TextField<String> birthPlace = new TextField<String>();
				birthPlace.setName( person+"birthPlace");  
				birthPlace.setFieldLabel("Birth Place");
				birthPlace.setReadOnly(true);	

				TextField<String> multiBirthInd = new TextField<String>();
				multiBirthInd.setName( person+"multipleBirthInd");  
				multiBirthInd.setFieldLabel("Multiple Birth Indicator");
				
				rightBirthfieldSet.add(dateOfBirth);
				rightBirthfieldSet.add(birthPlace);
				rightBirthfieldSet.add(birthOrder);
				rightBirthfieldSet.add(multiBirthInd);
				
				//Field listeners
				rightBirthfieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
					public void handleEvent(FieldSetEvent be) {	
						leftBirthfieldSet.collapse();	
					}	
				});
				
				rightBirthfieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
					public void handleEvent(FieldSetEvent be) {		
						leftBirthfieldSet.expand();				
					}	
				});	
				
	    // Other fields	   
		rightOtherfieldSet = new FieldSet();  
		rightOtherfieldSet.setHeading("Other");  
		rightOtherfieldSet.setCollapsible(true);  
		rightOtherfieldSet.setBorders(false);  
		FormLayout otherlayout = new FormLayout();  
		otherlayout.setLabelWidth(150);  
		rightOtherfieldSet.setLayout(otherlayout);  

			TextField<String> gender = new TextField<String>();
			gender.setName( person+"gender");  
			gender.setFieldLabel("Gender");	
			gender.setReadOnly(true);

			TextField<String> degree = new TextField<String>();
			degree.setName( person+"degree");  
			degree.setFieldLabel("Degree");	
			degree.setReadOnly(true);

			TextField<String> fatherName = new TextField<String>();
			fatherName.setName( person+"fatherName");  
			fatherName.setFieldLabel("Father Name");	
			fatherName.setReadOnly(true);

			TextField<String> motherName = new TextField<String>();
			motherName.setName( person+"motherName");  
			motherName.setFieldLabel("Mother Name");	
			motherName.setReadOnly(true);
			
			TextField<String> maidenName = new TextField<String>();
			maidenName.setName( person+"mothersMaidenName");  
			maidenName.setFieldLabel("Mother's Maiden Name");	
			maidenName.setReadOnly(true);
	
			TextField<String> maritalStatus = new TextField<String>();
			maritalStatus.setName( person+"maritalStatusCode");  
			maritalStatus.setFieldLabel("Marital Status");	
			maritalStatus.setReadOnly(true);

			TextField<String> deathInd = new TextField<String>();
			deathInd.setName( person+"deathInd");  
			deathInd.setFieldLabel("Death Indicator");	
			deathInd.setReadOnly(true);

//			DateField deathTime = new DateField();
			TextField<String> deathTime = new TextField<String>();
			deathTime.setName( person+"deathTime");  
			deathTime.setFieldLabel("Death Time");
			deathTime.setReadOnly(true);
//			deathTime.setEditable(false);
//			deathTime.getDatePicker().disableEvents(true);
			
			TextField<String> ssn = new TextField<String>();
			ssn.setName( person+"ssn");  
			ssn.setFieldLabel("Social Security Number");	
			ssn.setReadOnly(true);
			
			TextField<String> email = new TextField<String>();
			email.setName( person+"email");  
			email.setFieldLabel("Email");	
			email.setReadOnly(true);
			
			rightOtherfieldSet.add(gender);	
			rightOtherfieldSet.add(degree);			
			rightOtherfieldSet.add(maritalStatus);	
			rightOtherfieldSet.add(fatherName);
			rightOtherfieldSet.add(motherName);
			rightOtherfieldSet.add(maidenName);
			rightOtherfieldSet.add(deathInd);
			rightOtherfieldSet.add(deathTime);
			rightOtherfieldSet.add(ssn);
			rightOtherfieldSet.add(email);

			//Field listeners
			rightOtherfieldSet.addListener(Events.Collapse, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {	
					leftOtherfieldSet.collapse();	
				}	
			});
			
			rightOtherfieldSet.addListener(Events.Expand, new Listener<FieldSetEvent>() {
				public void handleEvent(FieldSetEvent be) {		
					leftOtherfieldSet.expand();				
				}	
			});	
			
		// add fields to Panel
/*		if( person.equals("leftPerson.") )
			identifierGrid = setupPersonIdentifierGrid(leftIdentifierStore, "Left Person");
		else
			identifierGrid = setupPersonIdentifierGrid(rightIdentifierStore, "Right Person");	
			
		formPanel.add(identifierGrid);
*/		
		formPanel.add(rightNamefieldSet); 
		formPanel.add(rightAddressfieldSet); 
		formPanel.add(rightAddressOtherfieldSet); 
		formPanel.add(rightPhonefieldSet);  
		formPanel.add(rightBirthfieldSet);  
		formPanel.add(rightOtherfieldSet);  		
	    return formPanel;  
	}
	
    final Listener<MessageBoxEvent> listenLink = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();  
          // Info.display("MessageBox1 ", "The '{0}' button was pressed", btn.getText());  
          if( btn.getText().equals("Yes")) {  
        	  
			  status.show();
			  linkButton.mask();  
			  controller.handleEvent(new AppEvent(AppEvents.ProcessLink, selectedPair));
          }

        }  
    };  
      
    final Listener<MessageBoxEvent> listenUnlink = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();  
          // Info.display("MessageBox2 ", "The '{0}' button was pressed", btn.getText());  
          if( btn.getText().equals("Yes")) {
        	  
			  status.show();
			  unlinkButton.mask();    
			  controller.handleEvent(new AppEvent(AppEvents.ProcessUnlink, selectedPair));
          }
        }  
    };  
    
	private FormPanel setupButtonPanel() {
		FormPanel buttonPanel = new FormPanel(); 
		buttonPanel.setHeaderVisible(false);
		buttonPanel.setBodyBorder(false);
		buttonPanel.setButtonAlign(HorizontalAlignment.CENTER);		
		
		// Buttons
		linkButton = new Button("Link", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {		
        	  	if( selectedPair != null ) {
        	  		MessageBox.confirm("Confirm", "Are you sure you want to link those two persons?", listenLink); 
        	  	}
			}
		});
		unlinkButton = new Button("Unlink", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {
        	  	if( selectedPair != null ) {
        	  		MessageBox.confirm("Confirm", "Are you sure you want to unlink those two persons?", listenUnlink); 
        	  	}
			}
		});
		status = new Status();
		status.setBusy("please wait...");
		status.hide();
		linkButton.disable();
		unlinkButton.disable();		
		buttonPanel.getButtonBar().setSpacing(5);
		buttonPanel.getButtonBar().add(new FillToolItem());
		buttonPanel.getButtonBar().add(status);
		buttonPanel.getButtonBar().add(linkButton);
		buttonPanel.getButtonBar().add(unlinkButton);
		
		return buttonPanel;
	}
	
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		
//		container = new LayoutContainer();
		container = new ContentPanel ();
		container.setLayout(new BorderLayout());
		container.setHeading("Review Links");  
		
		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());
		
		formButtonContainer = new LayoutContainer();
		formButtonContainer.setBorders(false);
		formButtonContainer.setScrollMode(Scroll.AUTO);
		
			formContainer = new LayoutContainer();
			formContainer.setBorders(false);
			TableLayout layout = new TableLayout(2);
			layout.setWidth("100%");
		    layout.setCellSpacing(5);
		    layout.setCellVerticalAlign(VerticalAlignment.TOP);
			formContainer.setLayout(layout);
		
				leftFormPanel = setupLeftPersonForm("Left person to link", "leftPerson.");
					// TextField's name bindings grid column id
			    	leftFormBindings = new FormBinding(leftFormPanel, true);  
			    
				rightFormPanel = setupRightPersonForm("Right person to link", "rightPerson.");
					// TextField's name bindings grid column id
			    	rightFormBindings = new FormBinding(rightFormPanel, true);  
			        
			formContainer.add(setupPersonIdentifierGrid(leftIdentifierStore, "Left Person"));		
			formContainer.add(setupPersonIdentifierGrid(rightIdentifierStore, "Right Person"));		
			
			formContainer.add(leftFormPanel);	
			formContainer.add(rightFormPanel);			
	
			buttonPanel = setupButtonPanel();
		
		formButtonContainer.add(formContainer);
		formButtonContainer.add(buttonPanel);
		
		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(4, 2, 4, 2));
		container.add(formButtonContainer, data);		
		container.add(gridContainer, new BorderLayoutData(LayoutRegion.NORTH, 250));
		
		
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
	
	public static void showWaitCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
	}
	 
	public static void showDefaultCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}
}
