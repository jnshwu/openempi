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
package org.openempi.webapp.client.mvc.notification;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.NotificationEventWeb;
import org.openempi.webapp.client.model.PersonWeb;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

public class EventNotificationView extends View
{
	private Grid<NotificationEventWeb> grid;
	private ListStore<NotificationEventWeb> eventStore = new ListStore<NotificationEventWeb>();

	private LayoutContainer container;
	private LayoutContainer gridContainer;
	
	public EventNotificationView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.EventNotificationView) {
//			grid = null;
			initUI();
		} else if (event.getType() == AppEvents.EventNotificationEventsComplete) {
			displayRecords((List<NotificationEventWeb>) event.getData());			
		}
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		
		container = new LayoutContainer();
		container.setLayout(new BorderLayout());
		
		FormPanel regForm = new FormPanel();
		regForm.setHeading("Event Registration Panel");
		regForm.setFrame(true);
		regForm.setWidth(350);

		Listener<BaseEvent> listener = new CheckboxListener();
		CheckBoxGroup checkGroup = new CheckBoxGroup();
		checkGroup.setFieldLabel("Events");
		CheckBox addCheck = new CheckBox();
		addCheck.setBoxLabel("Add");
		addCheck.setName(NotificationEventWeb.ADD_EVENT_TYPE);
		addCheck.addListener(Events.OnClick, listener);
		checkGroup.add(addCheck);

		CheckBox deleteCheck = new CheckBox();
		deleteCheck.setBoxLabel("Delete");
		deleteCheck.setName(NotificationEventWeb.DELETE_EVENT_TYPE);
		deleteCheck.addListener(Events.OnClick, listener);
		checkGroup.add(deleteCheck);
		
		CheckBox importCheck = new CheckBox();
		importCheck.setBoxLabel("Import");
		importCheck.setName(NotificationEventWeb.IMPORT_EVENT_TYPE);
		importCheck.addListener(Events.OnClick, listener);
		checkGroup.add(importCheck);
		
		CheckBox mergeCheck = new CheckBox();
		mergeCheck.setBoxLabel("Merge");
		mergeCheck.setName(NotificationEventWeb.MERGE_EVENT_TYPE);
		mergeCheck.addListener(Events.OnClick, listener);
		checkGroup.add(mergeCheck);
		
		CheckBox updateCheck = new CheckBox();
		updateCheck.setBoxLabel("Update");
		updateCheck.setName(NotificationEventWeb.UPDATE_EVENT_TYPE);
		updateCheck.addListener(Events.OnClick, listener);
		checkGroup.add(updateCheck);		
		regForm.add(checkGroup, new FormData("100%"));
		
		Button button = new Button();
		button.setToolTip("Refresh event list");
	    button.setIcon(IconHelper.create("images/arrow_refresh.png"));
	    button.setText("Refresh");
	    button.addSelectionListener( new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  GWT.log("Dispatched event notification view event.", null);
	        	  getController().handleEvent(new AppEvent(AppEvents.EventNotificationEventsInitiate));
	          }
	    });
	    
	    regForm.addButton(button);
		container.add(regForm, new BorderLayoutData(LayoutRegion.NORTH, 35));
		
		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(85, 0, 2, 0));
		container.add(gridContainer, data);
		
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);		
	}


	private void displayRecords(List<NotificationEventWeb> events) {
		if (grid == null) {
			setupEventGrid();
		}
		eventStore.removeAll();
		eventStore.add(events);
		container.layout();
//		status.hide();
//		searchButton.unmask();
	}
	
	private void setupEventGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		ColumnConfig column = new ColumnConfig();
		column.setId("messageId");  
		column.setHeader("Message Id");
		column.setWidth(250);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("timestamp");
		column.setHeader("Timestamp");
		column.setWidth(180);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("eventTypeName");
		column.setHeader("Event Type");
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("personIdentifier");
		column.setHeader("Identifier");
		column.setWidth(150);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);
		grid = new Grid<NotificationEventWeb>(eventStore, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);
		
		gridContainer.add(grid);
	}


	public class CheckboxListener implements Listener<BaseEvent>
	{
		public void handleEvent(BaseEvent be) {
			CheckBox checkBox = (CheckBox) be.getSource();
			if (checkBox.getValue()) {
				Info.display("Information", "Checked box: " + checkBox.getName());
				getController().handleEvent(new AppEvent(AppEvents.EventNotificationRegistrationInitiate, checkBox.getName()));
			} else {
				Info.display("Information", "Unchecked box: " + checkBox.getName());
				getController().handleEvent(new AppEvent(AppEvents.EventNotificationUnregistrationInitiate, checkBox.getName()));
			}
		}
	}	
}
