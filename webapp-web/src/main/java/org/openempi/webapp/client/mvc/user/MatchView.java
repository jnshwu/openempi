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
package org.openempi.webapp.client.mvc.user;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.PersonPairWeb;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
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
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;

public class MatchView extends View
{
	private Grid<PersonPairWeb> grid;
	private ListStore<PersonPairWeb> pairStore = new ListStore<PersonPairWeb>();

	private LayoutContainer container;
	private LayoutContainer gridContainer;
	private Status status;
	private Button matchButton;
	
	public MatchView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.MatchView) {
			grid = null;
			initUI();
		} else if (event.getType() == AppEvents.MatchRenderData) {
			displayUndecided((List<PersonPairWeb>) event.getData());
		}
	}

	private void displayUndecided(List<PersonPairWeb> undecided) {
		if (grid == null) {
			setupPersonGrid();
		}

		pairStore.removeAll();
		pairStore.add(undecided);
		container.layout();
		status.hide();
		matchButton.unmask();
	}

	private void setupPersonGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		XTemplate tpl = XTemplate.create(getTemplate());
		GWT.log("Maximum depth is " + tpl.getMaxDepth(), null);
		RowExpander expander = new RowExpander();
		expander.setTemplate(tpl);

		configs.add(expander);

		ColumnConfig column = new ColumnConfig();
		column.setId("m");
		column.setHeader("M[r]");
		column.setWidth(140);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("u");
		column.setHeader("U[r]");
		column.setWidth(140);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("binaryVector");
		column.setHeader("Binary Vector");
		column.setWidth(140);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("continousVector");
		column.setHeader("Continous Vector");
		column.setWidth(550);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("weight");
		column.setHeader("Weight");
		column.setWidth(120);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		grid = new Grid<PersonPairWeb>(pairStore, cm);
		grid.setStyleAttribute("borderTop", "none");
//		grid.setAutoExpandColumn("givenName");
		grid.setBorders(true);
		grid.setStripeRows(true);
		grid.addPlugin(expander);

		gridContainer.add(grid);
	}

	private String getTemplate() {
		return "<p class=\"personPairBlock\">" +
				"<b>Left and Right Persons:</b><br>" +
				"<table class=\"personTable\" border=\"1\">" +
				"<tr>" +
				"<td class=\"identifierColumn\">Id</td>" +
				"<td class=\"givenNameColumn\">Given Name</td>" +
				"<td class=\"familyNameColumn\">Family Name</td>" +
				"<td class=\"dateOfBirthColumn\">Date of birth</td>" +
				"<td class=\"cityColumn\">City</td>" +
				"<td class=\"phoneNumberColumn\">Phone</td>" +
				"<td class=\"ssnColumn\">SSN</td>" +
				"<td class=\"addressColumn\">Address</td>" +
				"<td class=\"zipCodeColumn\">Zip Code</td>" +
				"</tr>" +

				"<tr>" +
				"<td>{leftID}</td>" +
				"<td>{leftGivenName}</td>" +
				"<td>{leftFamilyName}</td>" +
				"<td>{leftDob}</td>" +
				"<td>{leftCity}</td>" +
				"<td>{leftPhoneNumber}</td>" +
				"<td>{leftSsn}</td>" +
				"<td>{leftAddress}</td>" +
				"<td>{leftZipCode}</td>" +
				"</tr>" +

				"<tr>" +
				"<td>{rightID}</td>" +
				"<td>{rightGivenName}</td>" +
				"<td>{rightFamilyName}</td>" +
				"<td>{rightDob}</td>" +
				"<td>{rightCity}</td>" +
				"<td>{rightPhoneNumber}</td>" +
				"<td>{rightSsn}</td>" +
				"<td>{rightAddress}</td>" +
				"<td>{rightZipCode}</td>" +
				"</tr>" +

				"</p>";
/*		return "<p class=\"personPairBlock\">" +
				"<b>Left and Right Persons:</b><br>" +
				"<table class=\"personTable\" border=\"1\">" +
				"<tr>" +
				"<td class=\"dbIdentifierColumn\">DBId</td>" +
				"<td class=\"recordIdentifierColumn\">recId</td>" +
				"<td class=\"givenNameColumn\">GivenN.</td>" +
				"<td class=\"familyNameColumn\">FamilyN.</td>" +
				"<td class=\"middleNameColumn\">MiddleN.</td>" +
				"<td class=\"stateOfBirthColumn\">SOB</td>" +
				"<td class=\"cityColumn\">City</td>" +
				"<td class=\"stateColumn\">State</td>" +
				"<td class=\"streetNameColumn\">Street</td>" +
				"<td class=\"streetTypeColumn\">Typ</td>" +
				"<td class=\"streetDirColumn\">Dir</td>" +
				"<td class=\"raceColumn\">Rac</td>" +
				"<td class=\"genderColumn\">Gen</td>" +
				"</tr>" +
		
				"<tr>" +
				"<td>{leftID}</td>" +
				"<td>{leftPhoneNumber}</td>" +
				"<td>{leftGivenName}</td>" +
				"<td>{leftFamilyName}</td>" +
				"<td>{leftMiddleName}</td>" +
				"<td>{leftBirthState}</td>" +
				"<td>{leftCity}</td>" +
				"<td>{leftState}</td>" +
				"<td>{leftStreetName}</td>" +
				"<td>{leftStreetType}</td>" +
				"<td>{leftStreetDir}</td>" +
				"<td>{leftRace}</td>" +
				"<td>{leftGender}</td>" +
				"</tr>" +
		
				"<tr>" +
				"<td>{rightID}</td>" +
				"<td>{rightPhoneNumber}</td>" +
				"<td>{rightGivenName}</td>" +
				"<td>{rightFamilyName}</td>" +
				"<td>{rightMiddleName}</td>" +
				"<td>{rightBirthState}</td>" +
				"<td>{rightCity}</td>" +
				"<td>{rightState}</td>" +
				"<td>{rightStreetName}</td>" +
				"<td>{rightStreetType}</td>" +
				"<td>{rightStreetDir}</td>" +
				"<td>{rightRace}</td>" +
				"<td>{rightGender}</td>" +
				"</tr>" +
		
				"</p>";*/
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		FormData formData = new FormData("100%");
		
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
		left.setStyleAttribute("paddingLeft", "10px");
		FormLayout layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);  
		left.setLayout(layout);

		final TextField<String> undecidedCount = new TextField<String>();
		undecidedCount.setFieldLabel("Number of undecided ");
		left.add(undecidedCount, formData);

		main.add(left, new ColumnData(1.0));
		panel.add(main, new FormData("20%"));
		matchButton = new Button("Match", new SelectionListener<ButtonEvent> () {
			@Override
			public void componentSelected(ButtonEvent ce) {
				controller.handleEvent(new AppEvent(AppEvents.MatchInitiate));
				status.show();
				matchButton.mask();
			}
		});
		status = new Status();
		status.setBusy("please wait...");
		panel.getButtonBar().add(status);
		status.hide();
	    panel.getButtonBar().add(new FillToolItem());
		panel.getButtonBar().add(matchButton);

		container.add(panel, new BorderLayoutData(LayoutRegion.NORTH, 39));
		
		gridContainer = new LayoutContainer();
		gridContainer.setBorders(true);
		gridContainer.setLayout(new FitLayout());

		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(80, 2, 2, 2));
		container.add(gridContainer, data);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
}
