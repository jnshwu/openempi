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
package org.openempi.webapp.client.mvc.blocking;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.BaseFieldWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;
import org.openempi.webapp.client.ui.util.Utility;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.GroupingStore;
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
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;

public class BlockingConfigurationView extends View
{
	private Grid<BaseFieldWeb> grid;
	private GroupingStore<BaseFieldWeb> store = new GroupingStore<BaseFieldWeb>();
	private Dialog addBlockingRoundDialog = null;
	private ListStore<ModelPropertyWeb> listStore = new ListStore<ModelPropertyWeb>();
	private Grid<BaseFieldWeb> addRoundGrid;
	
	private LayoutContainer container;
	
	@SuppressWarnings("unchecked")
	public BlockingConfigurationView(Controller controller) {
		super(controller);
		List<ModelPropertyWeb> fieldNames = (List<ModelPropertyWeb>) Registry.get(Constants.PERSON_MODEL_ALL_ATTRIBUTE_NAMES);		
		
		List<ModelPropertyWeb> names = new ArrayList<ModelPropertyWeb>();
		for (ModelPropertyWeb field : fieldNames) {
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
			listStore.add(names);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.BlockingConfigurationView) {
			initUI();
		} else if (event.getType() == AppEvents.BlockingConfigurationReceived) {
			store.removeAll();
			List<BaseFieldWeb> fields = (List<BaseFieldWeb>) event.getData();
			for (BaseFieldWeb baseField : fields) {				
				baseField.setFieldDescription(Utility.convertToDescription(baseField.getFieldName()));
				store.add(baseField);
			}
		} else if (event.getType() == AppEvents.BlockingConfigurationSaveComplete) {			
			// String message = event.getData();
			// Info.display("Information", "Person was successfully added with message " + message);					
	        MessageBox.alert("Information", "Traditional Blocking Configuration was successfully saved", null);  	 
	        
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  			
		}
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		store.groupBy("blockingRound");
		controller.handleEvent(new AppEvent(AppEvents.BlockingConfigurationRequest));
		
		buildAddBlockingRoundDialog();
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());
		
		ColumnConfig blockingRound = new ColumnConfig("blockingRound", "Blocking Round", 60);
		ColumnConfig fieldIndex = new ColumnConfig("fieldIndex", "Field Index", 60);
//		ColumnConfig fieldName = new ColumnConfig("fieldName", "Field Name", 100);
		ColumnConfig fieldName = new ColumnConfig("fieldDescription", "Field Name", 100);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(blockingRound);
		config.add(fieldIndex);
		config.add(fieldName);
		
		final ColumnModel cm = new ColumnModel(config);

		GroupingView view = new GroupingView();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		view.setGroupRenderer(new GridGroupRenderer() {
			public String render(GroupColumnData data) {
				String f = cm.getColumnById(data.field).getHeader();
				String l = data.models.size() == 1 ? "Field" : "Fields";
				return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
			}});

		grid = new Grid<BaseFieldWeb>(store, cm);
		grid.setView(view);
		grid.setBorders(true);

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Traditional Blocking Configuration");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/folder.png"));
		cp.setLayout(new FillLayout());
		cp.setSize(500, 350);

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button("Add Round", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  // Make sure we are starting with a clean slate
	        	  addRoundGrid.getStore().removeAll();
	        	  addBlockingRoundDialog.show();
	          }
	    }));
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Remove Round", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  BaseFieldWeb removeField = grid.getSelectionModel().getSelectedItem();
	        	  if (removeField == null) {
	        		  Info.display("Information","You must first select a field in the round to be deleted before pressing the \"Remove Round\" button.");
	        		  return;
	        	  }
	        	  for (BaseFieldWeb field : grid.getStore().getModels()) {
	        		  if (field.getBlockingRound() == removeField.getBlockingRound()) {
	        			  store.remove(field);
	        		  } else if (field.getBlockingRound() > removeField.getBlockingRound()) {
	        			  BaseFieldWeb theField = field;
	        			  store.remove(field);
	        			  theField.setBlockingRound(theField.getBlockingRound() - 1);
	        			  store.add(theField);
	        		  }
	        	  }
	          }
	    }));
		cp.setTopComponent(toolBar);
		
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
	        	  List<BaseFieldWeb> configuration = grid.getStore().getModels();
	        	  controller.handleEvent(new AppEvent(AppEvents.BlockingConfigurationSave, configuration));
	          }
	    }), layoutData); 
		cp.setBottomComponent(c);
		cp.add(grid);

		container.add(cp);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}

    final Listener<MessageBoxEvent> listenInfoMsg = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();   
          if( btn.getText().equals("OK")) {   
        	  return;
          }
        }  
    };  
    
	private void buildAddBlockingRoundDialog() {		
		if(addBlockingRoundDialog != null)
			return;
		
		addBlockingRoundDialog = new Dialog();
		addBlockingRoundDialog.setBodyBorder(false);
		addBlockingRoundDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addBlockingRoundDialog.setHeading("Add Blocking Round");
		addBlockingRoundDialog.setWidth(382);
		addBlockingRoundDialog.setHeight(300);
		addBlockingRoundDialog.setButtons(Dialog.OKCANCEL);
//		addBlockingRoundDialog.setHideOnButtonClick(true);
		addBlockingRoundDialog.setModal(true);
		addBlockingRoundDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  
	        	  // check duplicate Round
				  int roundCount = getCurrentRoundCount(store);
				  for (int i =1; i<=roundCount; i++) {					  
					  // get round with index i
					  List<BaseFieldWeb> round =new ArrayList<BaseFieldWeb>();
					  for (BaseFieldWeb fieldInRound : store.getModels()) {
						   if( fieldInRound.getBlockingRound() == i) {	
							   round.add(fieldInRound);
						   }
					  }	
					  
					  // check round same as round added in Grid
					  boolean sameRound = true;
					  if( round.size() ==  addRoundGrid.getStore().getModels().size()) {
						  int roundIndex=0;
			        	  for (BaseFieldWeb field : addRoundGrid.getStore().getModels()) {
			        		  if( !field.getFieldName().equals(round.get(roundIndex).getFieldName()) ) {
			        			  sameRound = false;
			        		  }		
			        		  roundIndex++;
			        	  }						  
					  } else {
						  sameRound = false;
					  }
					  
					  if( sameRound ) {
		        		  // Info.display("Information", "same round.");
		        	      MessageBox.alert("Information", "There is a duplicate blocking round in Blocking Configuration", listenInfoMsg);  		
		        	      return;
					  }
				  }	
	        	  
	        	  int roundIndex = getCurrentRoundCount(store) + 1;
	        	  for (BaseFieldWeb field : addRoundGrid.getStore().getModels()) {
	        		  store.add(new BaseFieldWeb(roundIndex, field.getFieldIndex(), field.getFieldName(), field.getFieldDescription()));
	        	  }
	        	  
	        	  addBlockingRoundDialog.hide();
	          }

			  private int getCurrentRoundCount(GroupingStore<BaseFieldWeb> store) {
				int roundCount = 0;
				for (BaseFieldWeb field : store.getModels()) {
					if (field.getBlockingRound() > roundCount) {
						roundCount = field.getBlockingRound();
					}
				}
				return roundCount;
			  }
	    });

		addBlockingRoundDialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  addBlockingRoundDialog.hide();
	          }
	    });
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Blocking Round");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/folder.png"));
		cp.setLayout(new FillLayout());
		cp.setSize(370, 300);
		
		ToolBar toolBar = new ToolBar();
		
		final ComboBox<ModelPropertyWeb> combo = new ComboBox<ModelPropertyWeb>();
		combo.setEmptyText("Select a field...");
		combo.setForceSelection(true);
//		combo.setDisplayField("name");
		combo.setDisplayField("description");
		combo.setWidth(150);
		combo.setStore(listStore);
		combo.setTypeAhead(true);
		combo.setTriggerAction(TriggerAction.ALL);

		toolBar.add(combo);
		toolBar.add(new Button("Add Field", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  List<ModelPropertyWeb> selection = combo.getSelection();
	        	  if (selection == null || selection.size() == 0) {	        		
	        		  Info.display("Information", "Please select a field before pressing the \"Add Field\" button.");
	        		  return;
	        	  }
	        	  ModelPropertyWeb field = selection.get(0);
	        	  if (!fieldInList(field, addRoundGrid.getStore())) {
	        		  addRoundGrid.getStore().add(new BaseFieldWeb(1, addRoundGrid.getStore().getCount()+1, field.getName(), field.getDescription()));
	        	  }
	          }

			private boolean fieldInList(ModelPropertyWeb field, ListStore<BaseFieldWeb> listStore) {
				for (BaseFieldWeb item : listStore.getModels()) {
					if (item.getFieldName().equalsIgnoreCase(field.getName())) {
						return true;
					}
				}
				return false;
			}
	    }));
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Remove Field", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  BaseFieldWeb field = addRoundGrid.getSelectionModel().getSelectedItem();
	        	  if (field == null) {
	        		  Info.display("Information","You must first select a field before pressing the \"Remove Field\" button.");
	        		  return;
	        	  }
	        	  addRoundGrid.getStore().remove(field);
	          }
	    }));
		cp.setTopComponent(toolBar);
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("fieldIndex");
		column.setHeader("Field Index");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(100);
		configs.add(column);

		column = new ColumnConfig();
//		column.setId("fieldName");
		column.setId("fieldDescription");
		column.setHeader("Field Name");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(150);
		configs.add(column);

		ListStore<BaseFieldWeb> store = new ListStore<BaseFieldWeb>();

		ColumnModel cm = new ColumnModel(configs);

		addRoundGrid = new Grid<BaseFieldWeb>(store, cm);
		addRoundGrid.setStyleAttribute("borderTop", "none");
//		addRoundGrid.setAutoExpandColumn("fieldName");
		addRoundGrid.setBorders(true);
		addRoundGrid.setStripeRows(true);
		cp.add(addRoundGrid);

		addBlockingRoundDialog.add(cp);
	}


}
