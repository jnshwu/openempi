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

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.PermissionWeb;
import org.openempi.webapp.client.model.UserWeb;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.mvc.View;
import org.openhie.openempi.model.Permission;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.data.BasePagingLoader;  
import com.extjs.gxt.ui.client.data.ModelData;  
import com.extjs.gxt.ui.client.data.PagingLoadResult;  
import com.extjs.gxt.ui.client.data.PagingLoader;  
import com.extjs.gxt.ui.client.data.PagingModelMemoryProxy;

import com.google.gwt.core.client.GWT;

public class ManageUserView extends View
{
	private Grid<UserWeb> grid;
	private ListStore<UserWeb> userStore = new ListStore<UserWeb>();
	private ListStore<UserWeb> userPageStore;
	private PagingToolBar toolBar;
	
	private LayoutContainer container;
	private LayoutContainer gridContainer;
	private Status status;

	private Button addUserButton;
	private Button updateUserButton;
	private Button removeUserButton;
	
	private UserWeb updateUser = null;
	
	private Map<String,PermissionWeb> permissions = null;
	
	public ManageUserView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.ManageUserView ) {
			grid = null;
			initUI();
			
			displayRecords((List<UserWeb>) event.getData());
		} else if (type == AppEvents.UserAddFinished ) {	
			grid = null;
			initUI();			
	    	UserWeb user = event.getData();	
	    	List<UserWeb> userList = Registry.get(Constants.MANAGE_USER_LIST);
	    	
	    	userList.add(user);	

		    Registry.register(Constants.MANAGE_USER_LIST, userList);						
			displayRecords( userList );	    	
	    	
		} else if (type == AppEvents.UserUpdateFinished ) {			
			grid = null;
			initUI();
	    	UserWeb user = event.getData();	
	    	
	    	List<UserWeb> userList = Registry.get(Constants.MANAGE_USER_LIST);
	    	
	    	userList.remove(updateUser);	
	    	userList.add(user);	

		    Registry.register(Constants.MANAGE_USER_LIST, userList);						
			displayRecords( userList );
	    	
		} else if (type == AppEvents.UserDeleteFinished ) {
			grid = null;
			initUI();
	    	List<UserWeb> userList = Registry.get(Constants.MANAGE_USER_LIST);
	    	
	    	userList.remove(updateUser);	

		    Registry.register(Constants.MANAGE_USER_LIST, userList);						
			displayRecords( userList );	
			
		} else if (type == AppEvents.UserUpdateViewCancel ) {
			grid = null;
			initUI();
			
	    	UserWeb user = event.getData();		    	
	    	List<UserWeb> userList = Registry.get(Constants.MANAGE_USER_LIST);					
			displayRecords( userList );

			if(user != null ) {
				for( UserWeb userWeb : userList) {
					if( user.getUsername() == userWeb.getUsername() ) {
						grid.getSelectionModel().select(userWeb, true);
						break;
					}
				}
			}
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  			
		}
	}
	
	
	private void displayRecords(List<UserWeb> users) {
		
		if( grid == null ) {
			setupUserGrid();
		}
		
		// Info.display("Warning", "size: "+users.size());			
		userStore.removeAll();
		userStore.add(users);		
		container.layout();
	}	
	
	private void setupUserGrid() {
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Users");
		cp.setHeaderVisible(false);
		cp.setBodyBorder(false);
		cp.setLayout(new FillLayout());
/*
	    // Add paging support for a local collection of models  
	    PagingModelMemoryProxy proxy = new PagingModelMemoryProxy(userStore.);  
	  
	    // loader  
	    PagingLoader<PagingLoadResult<ModelData>> loader = new BasePagingLoader<PagingLoadResult<ModelData>>(proxy);  
	    loader.setRemoteSort(true);  

	    userPageStore = new ListStore<UserWeb>(loader);
	    
	    toolBar = new PagingToolBar(30);  
	    toolBar.bind(loader);  
	  
	    loader.load(0, 30);  
*/	    
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		ColumnConfig column = new ColumnConfig();
		
		column.setId("username");  
		column.setHeader("Username");
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("firstName");  
		column.setHeader("First Name");
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("lastName");  
		column.setHeader("Last Name");  
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("address");  
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
		column.setWidth(80);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("postalCode");  
		column.setHeader("Zip Code");  
		column.setWidth(80);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("country");
		column.setHeader("Country");
		column.setWidth(120);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("phoneNumber");
		column.setHeader("Phone Number");
		column.setWidth(120);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("email");
		column.setHeader("Email");
		column.setWidth(150);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("website");
		column.setHeader("Website");
		column.setWidth(150);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);

		grid = new Grid<UserWeb>(userStore, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		
		LayoutContainer buttonContainer = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.START);
		buttonContainer.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 3, 0, 0));

		addUserButton = new Button(" Add User", IconHelper.create("images/user_add.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  	updateUser = grid.getSelectionModel().getSelectedItem();
					controller.handleEvent(new AppEvent(AppEvents.UserAddView, updateUser));								  
	          }
	    });

		updateUserButton = new Button(" Update User", IconHelper.create("images/user_edit.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  	updateUser = grid.getSelectionModel().getSelectedItem();
	        	  	
					if (updateUser == null) {
						Info.display("Information", "You must first select a field to be edited before pressing the \"Update User\" button.");
						return;
					} else {		
			        	// Info.display("Information", "The user: " +updateUser.getFirstName());
						controller.handleEvent(new AppEvent(AppEvents.UserUpdateView, updateUser));						
					}						        	  

	          }
	    });

		removeUserButton = new Button(" Remove User", IconHelper.create("images/user_delete.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  	updateUser = grid.getSelectionModel().getSelectedItem();
	        	  	
					if (updateUser == null) {
						Info.display("Information", "You must first select a field to be removed before pressing the \"Remove User\" button.");
						return;
					} else {		 
			        	// Info.display("Information", "The user: " +updateUser.getFirstName());
						controller.handleEvent(new AppEvent(AppEvents.UserDeleteView, updateUser));						
					}					        	  
	          }
	    });
		
		buttonContainer.add(addUserButton, layoutData);
		buttonContainer.add(updateUserButton, layoutData);
		buttonContainer.add(removeUserButton, layoutData);

	    // check permissions
		checkPermissins();

		cp.setTopComponent(buttonContainer);
		cp.add(grid);
//	    cp.setBottomComponent(toolBar);
		gridContainer.add(cp);
	}

	private void checkPermissins() {
	    if( permissions != null ) {
		    PermissionWeb permission = permissions.get(Permission.USER_ADD);
			if (permission == null) {
				addUserButton.disable();
			}
	
		    permission = permissions.get(Permission.USER_EDIT);
			if (permission == null) {
				updateUserButton.disable();
			}
			
		    permission = permissions.get(Permission.USER_DELETE);
			if (permission == null) {
				removeUserButton.disable();
			}
	    }	
	}
	
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
	
		permissions = Registry.get(Constants.LOGIN_USER_PERMISSIONS);   
		
		container = new LayoutContainer();
		container.setLayout(new BorderLayout());		
		
		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(2, 2, 2, 2));
		container.add(gridContainer, data);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
}
