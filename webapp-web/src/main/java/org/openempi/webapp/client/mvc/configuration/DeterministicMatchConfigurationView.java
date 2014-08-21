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
package org.openempi.webapp.client.mvc.configuration;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.ExactMatchingConfigurationWeb;
import org.openempi.webapp.client.model.MatchFieldWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.ui.util.Utility;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;

public class DeterministicMatchConfigurationView extends View
{
	private static final NumberFormat nfc = NumberFormat.getFormat("#,##0.0000");
	
	private Grid<MatchFieldWeb> grid;
	private ListStore<MatchFieldWeb> store = new ListStore<MatchFieldWeb>();
	private Dialog addEditMatchFieldDialog = null;
	private Boolean addOrEditFieldMode = true;
	private int editedFieldIndex = 0;
	private MatchFieldWeb editedField;

	private ListStore<ModelPropertyWeb> attributeNameStore = new ListStore<ModelPropertyWeb>();
	private ListStore<ModelPropertyWeb> comparatorFuncNameStore = new ListStore<ModelPropertyWeb>();

	private ComboBox<ModelPropertyWeb> attributeNameCombo = new ComboBox<ModelPropertyWeb>();
	private ComboBox<ModelPropertyWeb> comparatorFuncNameCombo = new ComboBox<ModelPropertyWeb>();
	private NumberField matchThresholdEdit = new NumberField();

	private LayoutContainer container;
	
	@SuppressWarnings("unchecked")
	public DeterministicMatchConfigurationView(Controller controller) {
		super(controller);
		List<ModelPropertyWeb> attributeNames = (List<ModelPropertyWeb>) Registry.get(Constants.PERSON_MODEL_ALL_ATTRIBUTE_NAMES);
		List<ModelPropertyWeb> comparatorFuncNames = (List<ModelPropertyWeb>) Registry.get(Constants.COMPARATOR_FUNCTION_NAMES);
		
		List<ModelPropertyWeb> names = new ArrayList<ModelPropertyWeb>();
		for (ModelPropertyWeb field : attributeNames) {
			 String name = field.getName();
			 if( name.equals("personId") || name.equals("personIdentifiers") || name.equals("accountIdentifierDomain") ||
				 name.equals("userCreatedBy") || name.equals("userChangedBy") || name.equals("userVoidedBy") ||
				 name.equals("dateCreated") || name.equals("dateChanged") || name.equals("dateVoided") ) {	
				 continue;
			 } else {
				 names.add(field);
			 }				 
		}	
		
		try {
			attributeNameStore.add(names);
			comparatorFuncNameStore.add(comparatorFuncNames);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
	}

	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.DeterministicMatchConfigurationView) {
			initUI();
		} else if (event.getType() == AppEvents.DeterministicMatchConfigurationReceived) {
			store.removeAll();
			ExactMatchingConfigurationWeb config = (ExactMatchingConfigurationWeb) event.getData();
				
			List<MatchFieldWeb> fields = (List<MatchFieldWeb>) config.getMatchFields();						
			for (MatchFieldWeb matchField : fields) {		
				// Info.display("Information", "MatchThreshold: "+matchField.getMatchThreshold());
				matchField.setFieldDescription(Utility.convertToDescription(matchField.getFieldName()));
				matchField.setComparatorFunctionNameDescription(Utility.convertToDescription(matchField.getComparatorFunctionName()));
				matchField.setMatchThreshold( Float.parseFloat( nfc.format(matchField.getMatchThreshold())) );
			}
			store.add(fields);
			
			grid.getSelectionModel().select(0, true);
			grid.getSelectionModel().deselect(0);
			
		} else if (event.getType() == AppEvents.DeterministicMatchConfigurationSaveComplete) {			
			// String message = event.getData();			
	        MessageBox.alert("Information", "Deterministic Match Configuration was successfully saved", null);  	 
	        
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  			
		}
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		controller.handleEvent(new AppEvent(AppEvents.DeterministicMatchConfigurationRequest));
		
		buildAddEditFieldDialog();
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());
		
		// ColumnConfig fieldNameColumn = new ColumnConfig("fieldName", "Field Name", 130);
		ColumnConfig fieldNameColumn = new ColumnConfig("fieldDescription", "Field Name", 150);
		// ColumnConfig compFuncNameColumn = new ColumnConfig("comparatorFunctionName", "Comp.Func.Name", 170);
		ColumnConfig compFuncNameColumn = new ColumnConfig("comparatorFunctionNameDescription", "Comparator Name", 180);
		ColumnConfig matchThresholdColumn = new ColumnConfig("matchThreshold", "Match Threshold", 120);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(fieldNameColumn);
		config.add(compFuncNameColumn);
		config.add(matchThresholdColumn);
		
		final ColumnModel cm = new ColumnModel(config);

		grid = new Grid<MatchFieldWeb>(store, cm);
		grid.setBorders(true);
		grid.setAutoWidth(true);
		grid.setStripeRows(true); 
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		grid.setHeight(280);

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Deterministic Match Field Configuration");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/table_gear.png"));
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(150);
		formLayout.setDefaultWidth(280);
		cp.setLayout(formLayout);
		cp.setSize(500, 380);

		LayoutContainer buttonContainer = new LayoutContainer();
		buttonContainer.setHeight(24);
		buttonContainer.setLayout(new ColumnLayout());
		final Button addFieldButton =
			new Button("Add", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// Make sure we are starting with a clean slate
					addOrEditFieldMode = true;
					addEditMatchFieldDialog.show();

					attributeNameCombo.clearSelections();
					comparatorFuncNameCombo.clearSelections();
					matchThresholdEdit.clear();
				}
			});
		buttonContainer.add(addFieldButton);
		final Button editFieldButton =
			new Button("Edit", IconHelper.create("images/folder_edit.png"), new SelectionListener<ButtonEvent>() {
		  		@Override
		  		public void componentSelected(ButtonEvent ce) {
		  			addOrEditFieldMode = false;
					MatchFieldWeb editField = grid.getSelectionModel().getSelectedItem();
					if (editField == null) {
						Info.display("Information", "You must first select a field to be edited before pressing the \"Edit Field\" button.");
						return;
					}
					addEditMatchFieldDialog.show();
					editedFieldIndex = grid.getStore().indexOf(editField);
					editedField = editField;
					
					attributeNameCombo.setValue(new ModelPropertyWeb(editField.getFieldName(), editField.getFieldDescription()));
					comparatorFuncNameCombo.setValue(new ModelPropertyWeb(editField.getComparatorFunctionName(), editField.getComparatorFunctionNameDescription()));
					
					matchThresholdEdit.setValue(editField.getMatchThreshold());
		  		}
		    });
		buttonContainer.add(editFieldButton);
		final Button removeFieldButton =
			new Button("Remove", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					MatchFieldWeb removeField = grid.getSelectionModel().getSelectedItem();
					if (removeField == null) {
						Info.display("Information", "You must first select a field to be deleted before pressing the \"Remove Round\" button.");
						return;
					}
					grid.getStore().remove(removeField);
/*					for (MatchFieldWeb field : grid.getStore().getModels()) {
						if (field == removeField) {
							grid.getStore().remove(field);
						}
					}
*/
				}
		    });
		buttonContainer.add(removeFieldButton);
		final Button moveUpFieldButton =
			new Button("Move Up", IconHelper.create("images/arrow_up.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (grid.getStore().getCount() > 1) {
						MatchFieldWeb field = grid.getSelectionModel().getSelectedItem();
						if (field == null) {
							Info.display("Information", "You must first select a field before pressing the \"Move Up\" button.");
							return;
						}
/*						int selectionIndex = grid.getStore().indexOf(field);
						if (selectionIndex > 0) {
							grid.getStore().remove(field);
							grid.getStore().insert(field, selectionIndex - 1);
						} else {
							Info.display("Information", "Cannot move up the first field.");
						}
						
						grid.getSelectionModel().select(field, true);
*/
						grid.getSelectionModel().selectPrevious(false);						
					}
				}
		    });
		buttonContainer.add(moveUpFieldButton);
		final Button moveDownFieldButton =
			new Button("Move Down", IconHelper.create("images/arrow_down.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (grid.getStore().getCount() > 1) {
						MatchFieldWeb field = grid.getSelectionModel().getSelectedItem();
						if (field == null) {
							Info.display("Information", "You must first select a field before pressing the \"Move Down\" button.");
							return;
						}
/*						int selectionIndex = grid.getStore().indexOf(field);
						if (selectionIndex >= 0 && selectionIndex < grid.getStore().getCount() - 1) {
							grid.getStore().remove(field);
							grid.getStore().insert(field, selectionIndex + 1);
						} else {
							Info.display("Information", "Cannot move down the last field.");
						}
						
						grid.getSelectionModel().select(field, true);
*/
						grid.getSelectionModel().selectNext(false);
					}
				}
		    });
		buttonContainer.add(moveDownFieldButton);

		grid.getSelectionModel().addListener(Events.SelectionChange,
			new Listener<SelectionChangedEvent<MatchFieldWeb>>() {
				public void handleEvent(SelectionChangedEvent<MatchFieldWeb> be) {
					List<MatchFieldWeb> selection = be.getSelection();
					Boolean editFieldEnabled = true;
					Boolean removeFieldEnabled = true;
					Boolean moveUpEnabled = true;
					Boolean moveDownEnabled = true;
					if (selection != null) {
						if (selection.size() <= 0) {
							editFieldEnabled = false;
							removeFieldEnabled = false;
							moveUpEnabled = false;
							moveDownEnabled = false;
						} else {
							editFieldEnabled = true;
							removeFieldEnabled = true;
							int selectionIndex = grid.getStore().indexOf(selection.get(0));
							moveUpEnabled = (selectionIndex > 0);
							moveDownEnabled = (selectionIndex < grid.getStore().getCount() - 1);
						}
					}
					editFieldButton.setEnabled(editFieldEnabled);
					removeFieldButton.setEnabled(removeFieldEnabled);
					moveUpFieldButton.setEnabled(moveUpEnabled);
					moveDownFieldButton.setEnabled(moveDownEnabled);
				}
			});

		grid.addListener(Events.SortChange, new Listener<GridEvent>() {
			public void handleEvent(GridEvent be) {
				// Info.display("Information", "SortChange.");
				MatchFieldWeb selectField = grid.getSelectionModel().getSelectedItem();
				
				int selectionIndex = grid.getStore().indexOf(selectField);
				Boolean moveUpEnabled = (selectionIndex > 0);;
				Boolean moveDownEnabled = (selectionIndex < grid.getStore().getCount() - 1);
				moveUpFieldButton.setEnabled(moveUpEnabled);
				moveDownFieldButton.setEnabled(moveDownEnabled);
			}
		});
		
		LayoutContainer c = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.CENTER);
		c.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));

		c.add(new Button("Save Settings", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
	  		@Override
	  		public void componentSelected(ButtonEvent ce) {
	  			ExactMatchingConfigurationWeb matchConfig = new ExactMatchingConfigurationWeb();
				
				List<MatchFieldWeb> matchFieldsConfig = grid.getStore().getModels();
				matchConfig.setMatchFields(matchFieldsConfig);
				
				controller.handleEvent(new AppEvent(AppEvents.DeterministicMatchConfigurationSave, matchConfig));
	  		}
	    }), layoutData);
		
		cp.setBottomComponent(c);

		cp.add(buttonContainer);
		
		cp.add(grid);

		container.add(cp);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}	

	private void buildAddEditFieldDialog() {
		if(addEditMatchFieldDialog != null)
			return;
		
		addEditMatchFieldDialog = new Dialog();
		addEditMatchFieldDialog.setBodyBorder(false);
		addEditMatchFieldDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addEditMatchFieldDialog.setHeading("Add/Edit Match Field");
		addEditMatchFieldDialog.setWidth(460);
		addEditMatchFieldDialog.setHeight(330);
		addEditMatchFieldDialog.setButtons(Dialog.OKCANCEL);
//		addEditMatchFieldDialog.setHideOnButtonClick(true);
		addEditMatchFieldDialog.setModal(true);
		addEditMatchFieldDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<ModelPropertyWeb> attribNameSelection = attributeNameCombo.getSelection();
				List<ModelPropertyWeb> compFuncNameSelection = comparatorFuncNameCombo.getSelection();
				if (matchThresholdEdit.getValue() != null && attribNameSelection.size() > 0 && compFuncNameSelection.size() > 0) {
						ModelPropertyWeb attribNameField = attribNameSelection.get(0);
						ModelPropertyWeb compFuncNameField = compFuncNameSelection.get(0);														  
			        	  
						MatchFieldWeb matchFieldWeb = new MatchFieldWeb();
						matchFieldWeb.setFieldName(attribNameField.getName());
						matchFieldWeb.setFieldDescription(attribNameField.getDescription());
						matchFieldWeb.setComparatorFunctionName(compFuncNameField.getName());
						matchFieldWeb.setComparatorFunctionNameDescription(compFuncNameField.getDescription());
						matchFieldWeb.setMatchThreshold(matchThresholdEdit.getValue().floatValue());
						
						if (addOrEditFieldMode) {  // Add					
				        	// check duplicate Field
				        	for (MatchFieldWeb field : grid.getStore().getModels()) {
				        		  if( field.getFieldName().equals( matchFieldWeb.getFieldName() ) ) {
					        	      MessageBox.alert("Information", "There is a duplicate match field in Deterministic Match Configuration", null);  		
					        	      return;			        			  
				        		  }
				        	}				
							grid.getStore().add(matchFieldWeb);
							
						} else { // Edit
				        	for (MatchFieldWeb field : grid.getStore().getModels()) {
				        		if( field.getFieldName() != editedField.getFieldName() ) {
					        		if( field.getFieldName().equals( matchFieldWeb.getFieldName() ) ) {
						        	    MessageBox.alert("Information", "There is a duplicate match field in Deterministic Match Configuration", null);  		
						        	    return;			        			  
					        		}
				        		}
				        	}				
							grid.getStore().remove(editedField);
							grid.getStore().insert(matchFieldWeb, editedFieldIndex);
						}
						
						addEditMatchFieldDialog.close();

				} else {
					if ( attribNameSelection.size() == 0 ) {
						MessageBox.alert("Information", "Please select Attribute Name", null);  
						return;
					} 
					if ( compFuncNameSelection.size() == 0 ) {
		        	    MessageBox.alert("Information", "Please select Comparator Name", null);  
						return;		
					}	
					if( matchThresholdEdit.getValue() == null ) {
		        	    MessageBox.alert("Information", "Match Threshold is required.", null);  
						return;	
					} 
				}
			}
	    });
		
		addEditMatchFieldDialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  addEditMatchFieldDialog.close();
	          }
	    });
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Match Field");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/folder.png"));
			FormLayout formLayout = new FormLayout();
			formLayout.setLabelWidth(150);
			formLayout.setDefaultWidth(280);
		cp.setLayout(formLayout);
		cp.setSize(450, 270);
		
		attributeNameCombo.setEmptyText("Select attribute...");
		attributeNameCombo.setForceSelection(true);
//		attributeNameCombo.setDisplayField("name");
		attributeNameCombo.setDisplayField("description");
		attributeNameCombo.setStore(attributeNameStore);
		attributeNameCombo.setTypeAhead(true);
		attributeNameCombo.setTriggerAction(TriggerAction.ALL);		
		attributeNameCombo.setFieldLabel("Attribute Name");
		cp.add(attributeNameCombo);

		comparatorFuncNameCombo.setEmptyText("Select function...");
		comparatorFuncNameCombo.setForceSelection(true);
//		comparatorFuncNameCombo.setDisplayField("name");
		comparatorFuncNameCombo.setDisplayField("description");
		comparatorFuncNameCombo.setStore(comparatorFuncNameStore);
		comparatorFuncNameCombo.setTypeAhead(true);
		comparatorFuncNameCombo.setTriggerAction(TriggerAction.ALL);
		comparatorFuncNameCombo.setFieldLabel("Comparator Name");
		cp.add(comparatorFuncNameCombo);

		matchThresholdEdit.setFieldLabel("Match Threshold");
		cp.add(matchThresholdEdit);
		
		addEditMatchFieldDialog.add(cp);
	}

}
