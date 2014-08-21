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
package org.openempi.webapp.client.mvc.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;

import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.PermissionWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openhie.openempi.model.Permission;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

import com.google.gwt.core.client.GWT;

public class ManageIdentifierDomainView extends View
{
	private Grid<IdentifierDomainWeb> grid;
	private ListStore<IdentifierDomainWeb> store = new ListStore<IdentifierDomainWeb>();
	
	private Dialog  addIdentifierDomainDialog = null;
	private String  addEditDeleteMode = "ADD";
	
	private IdentifierDomainWeb editedIdentifierDomain;

	private TextField<String> IdentifierName = new TextField<String>();
	private TextField<String> IdentifierDescription = new TextField<String>();
	private TextField<String> namespaceIdentifier = new TextField<String>();
	private TextField<String> universalIdentifier = new TextField<String>();
	private TextField<String> universalIdentifierType = new TextField<String>();

	private Button addDomainButton;
	private Button updateDomainButton;
	private Button removeDomainButton;
	
	private Map<String,PermissionWeb> permissions = null;
	
	private LayoutContainer container;
	
	@SuppressWarnings("unchecked")
	public ManageIdentifierDomainView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.ManageIdentifierDomainView) {
			initUI();
		}else if (event.getType() == AppEvents.Logout) {
			
			if( addIdentifierDomainDialog.isVisible() )
				addIdentifierDomainDialog.close();
			
  		    Dispatcher.get().dispatch(AppEvents.Logout);
			
		} else if (event.getType() == AppEvents.ManageIdentifierDomainReceived) {
			
			// Info.display("Information", "ManageIdentifierDomainReceived");
			store.removeAll();
				
			List<IdentifierDomainWeb> identifierDomains = (List<IdentifierDomainWeb>) event.getData();					
			store.add(identifierDomains);
			
			grid.getSelectionModel().select(0, true);
			grid.getSelectionModel().deselect(0);
			
		} else if (event.getType() == AppEvents.ManageIdentifierDomainAddComplete) {		
			
	        MessageBox.alert("Information", "A new Identifier Domain was successfully saved", null);  	 	 
	        
	        IdentifierDomainWeb newDomain = (IdentifierDomainWeb) event.getData();	
	        // Info.display("Information", "New Domain ID: " + newDomain.getIdentifierDomainId());
	       	store.add(newDomain);	 
	       	
			// controller.handleEvent(new AppEvent(AppEvents.ManageIdentifierDomainRequest));
			List<IdentifierDomainWeb> domainEntries = Registry.get(Constants.IDENTITY_DOMAINS);								
			domainEntries.add(newDomain);
			Registry.register(Constants.IDENTITY_DOMAINS, domainEntries);
			
		} else if (event.getType() == AppEvents.ManageIdentifierDomainUpdateComplete) {			
			
	       	store.remove(editedIdentifierDomain);	       	
	        IdentifierDomainWeb updateDomain = (IdentifierDomainWeb) event.getData();	
	       	store.add(updateDomain);	   
	       	
			//controller.handleEvent(new AppEvent(AppEvents.ManageIdentifierDomainRequest));
			List<IdentifierDomainWeb> domainEntries = Registry.get(Constants.IDENTITY_DOMAINS);		
			domainEntries.remove(editedIdentifierDomain);			
			domainEntries.add(updateDomain);
			Registry.register(Constants.IDENTITY_DOMAINS, domainEntries);
			
			// Basic search list
			if( Registry.get(Constants.BASIC_SEARCH_LIST) != null ) {
				List<PersonWeb> basicPersonList = Registry.get(Constants.BASIC_SEARCH_LIST);
				// Info.display("Information", "basicPersonList: "+basicPersonList.size());	
				
				for (PersonWeb personWeb : basicPersonList ) {
					for( PersonIdentifierWeb identifier : personWeb.getPersonIdentifiers() ) {
						if( identifier.getIdentifierDomain().getIdentifierDomainId().intValue() == updateDomain.getIdentifierDomainId().intValue() ) {
							identifier.setIdentifierDomain(updateDomain);
						}			
					}		
				}		
			    Registry.register(Constants.BASIC_SEARCH_LIST, basicPersonList);
			    
			} 

			// Advanced search list
			if( Registry.get(Constants.ADVANCED_SEARCH_LIST) != null ) {
				List<PersonWeb> advancedPersonList = Registry.get(Constants.ADVANCED_SEARCH_LIST);
				// Info.display("Information", "basicPersonList: "+advancedPersonList.size());	
				
				for (PersonWeb personWeb : advancedPersonList ) {
					for( PersonIdentifierWeb identifier : personWeb.getPersonIdentifiers() ) {
						if( identifier.getIdentifierDomain().getIdentifierDomainId().intValue() == updateDomain.getIdentifierDomainId().intValue() ) {
							identifier.setIdentifierDomain(updateDomain);
						}			
					}		
				}		
			    Registry.register(Constants.ADVANCED_SEARCH_LIST, advancedPersonList);
			    
			} 
			
	        MessageBox.alert("Information", "The Identifier Domain was successfully updated", null); 
	        
		} else if (event.getType() == AppEvents.ManageIdentifierDomainDeleteComplete) {			
			
	        MessageBox.alert("Information", "The Identifier Domain was successfully deleted", null);  	  
        	store.remove(editedIdentifierDomain);
        	
			// controller.handleEvent(new AppEvent(AppEvents.ManageIdentifierDomainRequest));
			List<IdentifierDomainWeb> domainEntries = Registry.get(Constants.IDENTITY_DOMAINS);								
			domainEntries.remove(editedIdentifierDomain);
			Registry.register(Constants.IDENTITY_DOMAINS, domainEntries);	
			
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  			
		}
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		permissions = Registry.get(Constants.LOGIN_USER_PERMISSIONS);   
		
		controller.handleEvent(new AppEvent(AppEvents.ManageIdentifierDomainRequest));
		
		buildAddEditDeleteDomainDialog();
		
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());
		
		ColumnConfig nameColumn = new ColumnConfig("identifierDomainName", "Name", 110);
		ColumnConfig descriptionColumn = new ColumnConfig("identifierDomainDescription", "Description", 120);
		ColumnConfig namespaceIdentifierColumn = new ColumnConfig("namespaceIdentifier", "Namespace Identifier", 150);
		ColumnConfig universalIdentifierColumn = new ColumnConfig("universalIdentifier", "Universal Identifier", 150);
		ColumnConfig universalIdentifierTypeColumn = new ColumnConfig("universalIdentifierTypeCode", "Universal Identifier Type", 150);
		
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(nameColumn);
		config.add(descriptionColumn);
		config.add(namespaceIdentifierColumn);
		config.add(universalIdentifierColumn);
		config.add(universalIdentifierTypeColumn);		
		final ColumnModel cm = new ColumnModel(config);

		grid = new Grid<IdentifierDomainWeb>(store, cm);
		grid.setBorders(true);
		grid.setAutoWidth(true);
		grid.setStripeRows(true); 
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		grid.setHeight(320);

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Manage Identifier Domains");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/key.png"));
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(150);
		formLayout.setDefaultWidth(280);
		cp.setLayout(formLayout);
		cp.setSize(720, 390);

		LayoutContainer buttonContainer = new LayoutContainer();
		buttonContainer.setHeight(24);
		buttonContainer.setLayout(new ColumnLayout());
		addDomainButton =
			new Button("Add Identifier Domain", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// Make sure we are starting with a clean slate
					addEditDeleteMode = "ADD";
					addIdentifierDomainDialog.setHeading("Add Identifier Domain");
					Button ok = addIdentifierDomainDialog.getButtonById("ok");
					ok.setText("Add");
					addIdentifierDomainDialog.show();
					
					readOnlyFields(false);
					IdentifierName.clear();
					IdentifierDescription.clear();
					namespaceIdentifier.clear();
					universalIdentifier.clear();
					universalIdentifierType.clear();
				}
			});
		
		updateDomainButton =
			new Button("Edit Identifier Domain", IconHelper.create("images/folder_edit.png"), new SelectionListener<ButtonEvent>() {
		  		@Override
		  		public void componentSelected(ButtonEvent ce) {
		  			IdentifierDomainWeb editField = grid.getSelectionModel().getSelectedItem();
					if (editField == null) {
						Info.display("Information", "You must first select a field to be edited before pressing the \"Edit Field\" button.");
						return;
					}
					addEditDeleteMode = "EDIT";
					addIdentifierDomainDialog.setHeading("Edit Identifier Domain");
					Button ok = addIdentifierDomainDialog.getButtonById("ok");
					ok.setText("Update");
					addIdentifierDomainDialog.show();
				
					editedIdentifierDomain = editField;
					
					readOnlyFields(false);
					IdentifierName.setValue(editField.getIdentifierDomainName());
					IdentifierDescription.setValue(editField.getIdentifierDomainDescription());
					namespaceIdentifier.setValue(editField.getNamespaceIdentifier());
					universalIdentifier.setValue(editField.getUniversalIdentifier());
					universalIdentifierType.setValue(editField.getUniversalIdentifierTypeCode());
					
		  		}
		    });
		
		removeDomainButton =
			new Button("Delete Identifier Domain", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
		  			IdentifierDomainWeb removeField = grid.getSelectionModel().getSelectedItem();
					if (removeField == null) {
						Info.display("Information", "You must first select a field to be deleted before pressing the \"Remove Round\" button.");
						return;
					}
					addEditDeleteMode = "DELETE";
					addIdentifierDomainDialog.setHeading("Delete Identifier Domain");
					Button ok = addIdentifierDomainDialog.getButtonById("ok");
					ok.setText("Delete");
					addIdentifierDomainDialog.show();	
					
					editedIdentifierDomain = removeField;
					
					readOnlyFields(true);
					IdentifierName.setValue(removeField.getIdentifierDomainName());
					IdentifierDescription.setValue(removeField.getIdentifierDomainDescription());
					namespaceIdentifier.setValue(removeField.getNamespaceIdentifier());
					universalIdentifier.setValue(removeField.getUniversalIdentifier());
					universalIdentifierType.setValue(removeField.getUniversalIdentifierTypeCode());
					
				}
		    });
		buttonContainer.add(addDomainButton);
		buttonContainer.add(updateDomainButton);
		buttonContainer.add(removeDomainButton);


		grid.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<IdentifierDomainWeb>>() {
			
				public void handleEvent(SelectionChangedEvent<IdentifierDomainWeb> be) {
					List<IdentifierDomainWeb> selection = be.getSelection();

				}
			});

		grid.addListener(Events.SortChange, new Listener<GridEvent<IdentifierDomainWeb>>() {

			public void handleEvent(GridEvent<IdentifierDomainWeb> be) {
				IdentifierDomainWeb selectField = grid.getSelectionModel().getSelectedItem();
				
			}
		});
		

	    // check permissions
		checkPermissins();
		
		cp.add(buttonContainer);
		
		cp.add(grid);

		container.add(cp);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}	

	private void checkPermissins() {
	    // check permissions
	    if( permissions != null ) {
		    PermissionWeb permission = permissions.get(Permission.IDENTIFIER_DOMAIN_ADD);
			if (permission == null) {
				addDomainButton.disable();
			}
	
		    permission = permissions.get(Permission.IDENTIFIER_DOMAIN_EDIT);
			if (permission == null) {
				updateDomainButton.disable();
			}
			
		    permission = permissions.get(Permission.IDENTIFIER_DOMAIN_DELETE);
			if (permission == null) {
				removeDomainButton.disable();
			}
	    }
	}
	
	private void readOnlyFields(boolean enable) {
		IdentifierName.setReadOnly(enable);
		IdentifierDescription.setReadOnly(enable);
		namespaceIdentifier.setReadOnly(enable);
		universalIdentifier.setReadOnly(enable);
		universalIdentifierType.setReadOnly(enable);
	}
	
	final Listener<MessageBoxEvent> listenConfirmDelete = new Listener<MessageBoxEvent>() {  
	        public void handleEvent(MessageBoxEvent ce) {  
	          Button btn = ce.getButtonClicked();  
	          if( btn.getText().equals("Yes")) {
	        	  
				  controller.handleEvent(new AppEvent(AppEvents.ManageIdentifierDomainDelete, editedIdentifierDomain));	
				  if (addIdentifierDomainDialog.isVisible())
					  addIdentifierDomainDialog.close();
	          }
	        }  
	};  
	    
	private void buildAddEditDeleteDomainDialog() {
		if(addIdentifierDomainDialog != null)
			return;
		
		addIdentifierDomainDialog = new Dialog();
		addIdentifierDomainDialog.setBodyBorder(false);
		addIdentifierDomainDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addIdentifierDomainDialog.setWidth(430);
		addIdentifierDomainDialog.setHeight(330);
		addIdentifierDomainDialog.setButtons(Dialog.OKCANCEL);
//		addEditMatchFieldDialog.setHideOnButtonClick(true);
		addIdentifierDomainDialog.setModal(true);
		addIdentifierDomainDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if ( (IdentifierName.getValue() != null && namespaceIdentifier.getValue() != null ) ||
					 (IdentifierName.getValue() != null && universalIdentifier.getValue() != null && universalIdentifierType.getValue() != null) ) {
					
					IdentifierDomainWeb newIdentifierDomain = new IdentifierDomainWeb();
					
					newIdentifierDomain.setIdentifierDomainName(IdentifierName.getValue());
					newIdentifierDomain.setIdentifierDomainDescription(IdentifierDescription.getValue());
					newIdentifierDomain.setNamespaceIdentifier(namespaceIdentifier.getValue());
					newIdentifierDomain.setUniversalIdentifier(universalIdentifier.getValue());
					newIdentifierDomain.setUniversalIdentifierTypeCode(universalIdentifierType.getValue());
					
					if (addEditDeleteMode.equals("ADD")) {  // Add	
			        	// check duplicate domain
			        	for (IdentifierDomainWeb domain : grid.getStore().getModels()) {
			        		  if( domain.getIdentifierDomainName().equals( newIdentifierDomain.getIdentifierDomainName() ) ) {
				        	      MessageBox.alert("Information", "There is a duplicate domain name in Identifier Domain List", null);  		
				        	      return;			        			  
			        		  }
			        		  if( domain.getNamespaceIdentifier().equals( newIdentifierDomain.getNamespaceIdentifier() ) ) {
				        	      MessageBox.alert("Information", "There is a duplicate Namespace Identifier in Identifier Domain List", null);  		
				        	      return;			        			  
			        		  }
			        		  
			        		  if( domain.getUniversalIdentifier().equals( newIdentifierDomain.getUniversalIdentifier()) && 
			        			  domain.getUniversalIdentifierTypeCode().equals( newIdentifierDomain.getUniversalIdentifierTypeCode()) ) {
				        	      MessageBox.alert("Information", "There is a duplicate Universal Identifier with Universal Identifier Type in Identifier Domain List", null);  		
				        	      return;			        			  
			        		  }
			        	}										
						controller.handleEvent(new AppEvent(AppEvents.ManageIdentifierDomainAdd, newIdentifierDomain));					
					} else if (addEditDeleteMode.equals("EDIT")) { // Edit
						
			        	for (IdentifierDomainWeb domain : grid.getStore().getModels()) {			        		
			        		if( domain.getIdentifierDomainName() != editedIdentifierDomain.getIdentifierDomainName() ) {
			        			
				        		  if( domain.getIdentifierDomainName().equals( newIdentifierDomain.getIdentifierDomainName() ) ) {
					        	      MessageBox.alert("Information", "There is a duplicate domain name in Identifier Domain List", null);  		
					        	      return;			        			  
				        		  }
				        		  if( domain.getNamespaceIdentifier().equals( newIdentifierDomain.getNamespaceIdentifier() ) ) {
					        	      MessageBox.alert("Information", "There is a duplicate Namespace Identifier in Identifier Domain List", null);  		
					        	      return;			        			  
				        		  }
				        		  
				        		  if( domain.getUniversalIdentifier().equals( newIdentifierDomain.getUniversalIdentifier()) && 
				        			  domain.getUniversalIdentifierTypeCode().equals( newIdentifierDomain.getUniversalIdentifierTypeCode()) ) {
					        	      MessageBox.alert("Information", "There is a duplicate Universal Identifier with Universal Identifier Type in Identifier Domain List", null);  		
					        	      return;			        			  
				        		  }
			        		}
			        	}										
						newIdentifierDomain.setIdentifierDomainId(editedIdentifierDomain.getIdentifierDomainId());	
						controller.handleEvent(new AppEvent(AppEvents.ManageIdentifierDomainUpdate, newIdentifierDomain));	
					} else if (addEditDeleteMode.equals("DELETE")) { // Delete
						
		        	  	MessageBox.confirm("Confirm", "Delete operation cannot be undone. Are you sure you want to delete this domain?", listenConfirmDelete); 		
		        	  	return;
					}				
		        	addIdentifierDomainDialog.close();
		        	
				} else {
					if( IdentifierName.getValue() == null ) {
		        	    MessageBox.alert("Information", "Identifier Name is required.", null);  
						return;	
					} 	
					if( namespaceIdentifier.getValue() == null && (universalIdentifier.getValue() == null || universalIdentifierType.getValue() == null)) {
		        	    MessageBox.alert("Information", "Ether Namespace Identifier is required or Universal Identifier with Universal Identifier Type are required", null);  
						return;	
					} 											
				}
			}
	    });
		
		addIdentifierDomainDialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  addIdentifierDomainDialog.close();
	          }
	    });
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Identifier Domain");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/folder.png"));
			FormLayout formLayout = new FormLayout();
			formLayout.setLabelWidth(150);
			formLayout.setDefaultWidth(220);
		cp.setLayout(formLayout);
		cp.setSize(420, 270);

		IdentifierName.setFieldLabel("Name");
		cp.add(IdentifierName);
		
		IdentifierDescription.setFieldLabel("Description");
		cp.add(IdentifierDescription);
		
		namespaceIdentifier.setFieldLabel("Namespace Identifier");
		universalIdentifier.setFieldLabel("Universal Identifier");
		universalIdentifierType.setFieldLabel("Universal Identifier Type");

	    FormPanel namespace = new FormPanel(); 
	    namespace.setHeaderVisible(false);
	    namespace.setFrame(true);
	    namespace.setLabelWidth(140);
	    namespace.setWidth(390);
	    namespace.setTitle("Namespace Identifier");  

	    namespace.add(namespaceIdentifier);
	
	    FormPanel universal = new FormPanel(); 
	    universal.setHeaderVisible(false);
	    universal.setFrame(true);

	    universal.setLabelWidth(140);
	    universal.setWidth(390);
	    universal.setTitle("Universal Identifier");  
		
	    universal.add(universalIdentifier);
	    universal.add(universalIdentifierType);
	    
	    LabelField space = new LabelField("");
	    
		cp.add(space);
		cp.add(namespace);
		cp.add(universal);		
		
		addIdentifierDomainDialog.add(cp);
	}
}
