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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;

import org.openempi.webapp.client.model.PermissionWeb;
import org.openempi.webapp.client.model.RoleWeb;
import org.openhie.openempi.model.Permission;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

import com.google.gwt.core.client.GWT;

public class ManageRoleView extends View
{
	private Grid<RoleWeb> grid;
	private ListStore<RoleWeb> store = new ListStore<RoleWeb>();
	
	private Dialog  roleDialog = null;
	private String  addEditDeleteMode = "ADD";
	
	private RoleWeb editedRole;

	private TextField<String> RoleName = new TextField<String>();
	private TextField<String> RoleDescription = new TextField<String>();

	CheckBoxListView<PermissionWeb> permissionCheckBoxList;
	private ListStore<PermissionWeb> permissionStore = new ListStore<PermissionWeb>();;
	
	private LayoutContainer container;
	
	private Button addRoleButton;
	private Button updateRoleButton;
	private Button removeRoleButton;
	
	private Map<String,PermissionWeb> permissions = null;
	
	@SuppressWarnings("unchecked")
	public ManageRoleView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.ManageRoleView) {
			initUI();
		} else if (event.getType() == AppEvents.ManageRoleReceived) {
			
			// Info.display("Information", "ManageRoleReceived");
			List<RoleWeb> roles = (List<RoleWeb>) event.getData();			
			store.removeAll();		
			store.add(roles);
			
			grid.getSelectionModel().select(0, true);
			grid.getSelectionModel().deselect(0);
			
		} else if (event.getType() == AppEvents.RoleUpdateRenderData) {
			
			RoleWeb renderRole = (RoleWeb) event.getData();
			// Info.display("Information", "Role: "+ editedRole.getName());
			
	    	List<PermissionWeb> permissionList = Registry.get(Constants.PERMISSION_LIST);
			permissionStore.removeAll();
			permissionStore.add(permissionList);	
			
			roleDialog.setHeading("Edit Role");
			Button ok = roleDialog.getButtonById("ok");
			ok.setText("Update");
			roleDialog.show();
			
			readOnlyFields(false);
			displayRecords(renderRole);
			
		} else if (event.getType() == AppEvents.RoleDeleteRenderData) {
			
			RoleWeb renderRole = (RoleWeb) event.getData();
			// Info.display("Information", "Role: "+ editedRole.getName());
			
	    	List<PermissionWeb> permissionList = Registry.get(Constants.PERMISSION_LIST);
			permissionStore.removeAll();
			permissionStore.add(permissionList);	
			
			roleDialog.setHeading("Delete Role");
			Button ok = roleDialog.getButtonById("ok");
			ok.setText("Delete");
			roleDialog.show();
			
			readOnlyFields(true);
			displayRecords(renderRole);
						
		} else if (event.getType() == AppEvents.ManageRoleAddComplete) {			 
	        
	        RoleWeb newRole = (RoleWeb) event.getData();	
	       	store.add(newRole);	 
	       	
			List<RoleWeb> roleEntries = Registry.get(Constants.ROLE_LIST);								
			roleEntries.add(newRole);
			Registry.register(Constants.ROLE_LIST, roleEntries);
			
	        MessageBox.alert("Information", "A new role was successfully saved", null);  	 
			
		} else if (event.getType() == AppEvents.ManageRoleUpdateComplete) {			
			
	       	store.remove(editedRole);	       	
	        RoleWeb updateRole = (RoleWeb) event.getData();	
	       	store.add(updateRole);	   
	       	
			List<RoleWeb> roleEntries = Registry.get(Constants.ROLE_LIST);		
			roleEntries.remove(editedRole);			
			roleEntries.add(updateRole);
			Registry.register(Constants.ROLE_LIST, roleEntries);
			
	        MessageBox.alert("Information", "The role was successfully updated", null); 
	        
		} else if (event.getType() == AppEvents.ManageRoleDeleteComplete) {			
			
        	store.remove(editedRole);
        	
			List<RoleWeb> domainEntries = Registry.get(Constants.ROLE_LIST);								
			domainEntries.remove(editedRole);
			Registry.register(Constants.ROLE_LIST, domainEntries);	
			
	        MessageBox.alert("Information", "The role was successfully deleted", null);  	  
			
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  			
		}
	}

	private RoleWeb getNewRoleFromGUI() {
		RoleWeb role = new RoleWeb();
		
		// Names
		role.setName("");
		role.setDescription("");

		
		return 	copyRoleFromGUI( role);
	}
	
	private RoleWeb copyRole(RoleWeb role) {
		RoleWeb updatingRole = new RoleWeb();
		
		updatingRole.setId(role.getId());

		// Name
		updatingRole.setName( role.getName());
		updatingRole.setDescription(role.getDescription());

		return updatingRole;
	}
	
	private RoleWeb copyRoleFromGUI(RoleWeb updateRole) {
		RoleWeb role = copyRole(updateRole);		
		
		// Name
		role.setName( RoleName.getValue());
		role.setDescription( RoleDescription.getValue());
		
		// Assign Permissions
		Set<PermissionWeb> permissionsWeb = new java.util.HashSet<PermissionWeb>(permissionCheckBoxList.getChecked().size());
		for (PermissionWeb permission : permissionCheckBoxList.getChecked()) {			
			// Info.display("permission:", "permission:"+permission.getName());
			permissionsWeb.add(permission);
		}
		role.setPermissions(permissionsWeb);
	
		return 	role;
	}
	
	private void displayRecords(RoleWeb role) {
		RoleName.setValue(role.getName());
		RoleDescription.setValue(role.getDescription());
		
		if (role.getPermissions() != null && role.getPermissions().size() > 0 ) {			
			for (PermissionWeb permission : role.getPermissions()) {	
				
				// Info.display("Permission:", "Permission:"+permission.getName());					
				for(PermissionWeb m : permissionStore.getModels()) {
					if(m.getName().equals(permission.getName())) {
						permissionCheckBoxList.setChecked(m, true);
					}
				} 
			}
		}			
	}

	private void checkPermissins() {
	    if( permissions != null ) {
		    PermissionWeb permission = permissions.get(Permission.USER_ADD);
			if (permission == null) {
				addRoleButton.disable();
			}
	
		    permission = permissions.get(Permission.USER_EDIT);
			if (permission == null) {
				updateRoleButton.disable();
			}
			
		    permission = permissions.get(Permission.USER_DELETE);
			if (permission == null) {
				removeRoleButton.disable();
			}
	    }	
	}
	
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		permissions = Registry.get(Constants.LOGIN_USER_PERMISSIONS);   
		
		controller.handleEvent(new AppEvent(AppEvents.ManageRoleRequest));
		
		buildAddEditDeleteDomainDialog();
		
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());
		
		ColumnConfig nameColumn = new ColumnConfig("name", "Name", 180);
		ColumnConfig descriptionColumn = new ColumnConfig("description", "Description", 520);
		
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(nameColumn);
		config.add(descriptionColumn);
		final ColumnModel cm = new ColumnModel(config);

		grid = new Grid<RoleWeb>(store, cm);
		grid.setBorders(true);
		grid.setAutoWidth(true);
		grid.setStripeRows(true); 
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		grid.setHeight(320);

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Manage Roles");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/user_role.png"));
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(150);
		formLayout.setDefaultWidth(280);
		cp.setLayout(formLayout);
		cp.setSize(720, 360);

		LayoutContainer buttonContainer = new LayoutContainer();
		buttonContainer.setHeight(24);
		buttonContainer.setLayout(new ColumnLayout());
		addRoleButton =
			new Button("Add Role", IconHelper.create("images/user_add.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// Make sure we are starting with a clean slate
					addEditDeleteMode = "ADD";			
					
					roleDialog.setHeading("Add Role");
					Button ok = roleDialog.getButtonById("ok");
					ok.setText("Add");
					roleDialog.show();
					
					readOnlyFields(false);
					RoleName.clear();
					RoleDescription.clear();
					
			    	List<PermissionWeb> permissionList = Registry.get(Constants.PERMISSION_LIST);
					permissionStore.removeAll();
					permissionStore.add(permissionList);	
				}
			});
		
		updateRoleButton =
			new Button("Edit Role", IconHelper.create("images/user_edit.png"), new SelectionListener<ButtonEvent>() {
		  		@Override
		  		public void componentSelected(ButtonEvent ce) {		  			
					addEditDeleteMode = "EDIT";
					
		  			editedRole = grid.getSelectionModel().getSelectedItem();
					if (editedRole == null) {
						Info.display("Information", "You must first select a field to be edited before pressing the \"Edit Role\" button.");
						return;
					} else{
			        	// Info.display("Information", "The role: " +editedRole.getName());
						controller.handleEvent(new AppEvent(AppEvents.ManageGetUpdateRole, editedRole));								
					}	  

		  		}
		    });
		
		removeRoleButton =
			new Button("Delete Role", IconHelper.create("images/user_delete.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					addEditDeleteMode = "DELETE";
					
					editedRole = grid.getSelectionModel().getSelectedItem();
					if (editedRole == null) {
						Info.display("Information", "You must first select a field to be deleted before pressing the \"Delete Role\" button.");
						return;
					} else{
			        	// Info.display("Information", "The role: " +editedRole.getName());
						controller.handleEvent(new AppEvent(AppEvents.ManageGetDeleteRole, editedRole));								
					}	  
				}
		    });
		
		buttonContainer.add(addRoleButton);
		buttonContainer.add(updateRoleButton);
		buttonContainer.add(removeRoleButton);

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
	
	private void readOnlyFields(boolean enable) {
		RoleName.setReadOnly(enable);
		RoleDescription.setReadOnly(enable);
		permissionCheckBoxList.setEnabled(!enable);
	}
	
	final Listener<MessageBoxEvent> listenConfirmDelete = new Listener<MessageBoxEvent>() {  
	        public void handleEvent(MessageBoxEvent ce) {  
	          Button btn = ce.getButtonClicked();  
	          if( btn.getText().equals("Yes")) {
	        	  
				  controller.handleEvent(new AppEvent(AppEvents.ManageRoleDelete, editedRole));	
				  if (roleDialog.isVisible())
					  roleDialog.close();
	          }
	        }  
	};  
	    
	private void buildAddEditDeleteDomainDialog() {
		if(roleDialog != null)
			return;
		
		roleDialog = new Dialog();
		roleDialog.setBodyBorder(false);
		roleDialog.setIcon(IconHelper.create("images/user_role.png"));
		roleDialog.setWidth(430);
		roleDialog.setHeight(400);
		roleDialog.setButtons(Dialog.OKCANCEL);
//		addEditMatchFieldDialog.setHideOnButtonClick(true);
		roleDialog.setModal(true);
		roleDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				
					if( !RoleName.isValid() ) {					
						Info.display("test:", "Invalid fields");	
						return;
					}
					
					if (addEditDeleteMode.equals("ADD")) {  // Add	
						RoleWeb newRole = getNewRoleFromGUI();		 
						
			        	// check duplicate role name
				    	List<RoleWeb> roleList = Registry.get(Constants.ROLE_LIST);
			        	for (RoleWeb role : roleList) {
			        		  if( role.getName().equals( newRole.getName() ) ) {
				        	      MessageBox.alert("Information", "There is a duplicate role name in existing roles", null);  		
				        	      return;			        			  
			        		  }
			        	}
			        			
						controller.handleEvent(new AppEvent(AppEvents.ManageRoleAdd, newRole));		
						
					} else if (addEditDeleteMode.equals("EDIT")) { // Edit
						RoleWeb updatingRole = copyRoleFromGUI(editedRole);	
						
			        	// check duplicate role name except itself
				    	List<RoleWeb> roleList = Registry.get(Constants.ROLE_LIST);
			        	for (RoleWeb role : roleList) {
			        		if( role.getId() != updatingRole.getId() ) {	        			
				        		  if( role.getName().equals( updatingRole.getName() ) ) {
					        	      MessageBox.alert("Information", "There is a duplicate role name in existing users", null);  		
					        	      return;			        			  
				        		  }
			        		}
			        	}
						controller.handleEvent(new AppEvent(AppEvents.ManageRoleUpdate, updatingRole));	
						
					} else if (addEditDeleteMode.equals("DELETE")) { // Delete
						
		        	  	MessageBox.confirm("Confirm", "Delete operation cannot be undone. Are you sure you want to delete this role?", listenConfirmDelete); 		
		        	  	return;
					}				
		        	roleDialog.close();
			}
	    });
		
		roleDialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  roleDialog.close();
	          }
	    });
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Role");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/user.png"));
			FormLayout formLayout = new FormLayout();
			formLayout.setLabelWidth(150);
			formLayout.setDefaultWidth(220);
		cp.setLayout(formLayout);
		cp.setSize(420, 340);

		RoleName.setFieldLabel("Name");
		RoleName.setAllowBlank(false);
		cp.add(RoleName);
		
		RoleDescription.setFieldLabel("Description");
		cp.add(RoleDescription);
		
		LabelField  headingLabel = new LabelField("");	
		headingLabel.setLabelStyle("font-weight:bold");
		headingLabel.setFieldLabel("Permissions:");
		cp.add(headingLabel);	
		
		permissionCheckBoxList =  new CheckBoxListView<PermissionWeb>();
		permissionCheckBoxList.setTitle("test");
		permissionCheckBoxList.setDisplayProperty("name"); 
		permissionCheckBoxList.setStore(permissionStore);
		permissionCheckBoxList.setHeight(220);	
		cp.add(permissionCheckBoxList);
		
		roleDialog.add(cp);
	}
}
