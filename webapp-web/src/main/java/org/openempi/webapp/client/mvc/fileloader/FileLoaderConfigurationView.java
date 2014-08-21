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
package org.openempi.webapp.client.mvc.fileloader;

import java.util.ArrayList;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.LoaderConfigWeb;
import org.openempi.webapp.client.model.LoaderDataFieldWeb;
import org.openempi.webapp.client.model.LoaderFieldCompositionWeb;
import org.openempi.webapp.client.model.LoaderSubFieldWeb;
import org.openempi.webapp.client.model.ModelPropertyWeb;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
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
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;

public class FileLoaderConfigurationView extends View
{
	private Grid<LoaderDataFieldWeb> grid;
	private ListStore<LoaderDataFieldWeb> store = new ListStore<LoaderDataFieldWeb>();
	private CheckBox isHeaderPresentCheckBox = new CheckBox();
	private Dialog addEditLoaderDataFieldDialog;
	private Dialog addEditLoaderSubFieldDialog;
	private Boolean addOrEditFieldMode = true;
	private int editedFieldIndex = 0;
	private LoaderDataFieldWeb editedDataField;
	private ListStore<ModelPropertyWeb> listStore = new ListStore<ModelPropertyWeb>();
	private Grid<LoaderSubFieldWeb> editSubFieldGrid;
	private ListStore<LoaderSubFieldWeb> subFieldStore = new ListStore<LoaderSubFieldWeb>();
	private ComboBox<ModelPropertyWeb> fieldNameCombo = new ComboBox<ModelPropertyWeb>();
	private TextField<String> formatStringEdit = new TextField<String>();
	private CheckBox isPartOfCompositionCheckBox = new CheckBox();
	private NumberField compositionIndexEdit = new NumberField();
	private TextField<String> separatorEdit = new TextField<String>();
	private Boolean addOrEditSubFieldMode = true;
	private int editedSubFieldIndex = 0;
	private LoaderSubFieldWeb editedSubField;
	private ComboBox<ModelPropertyWeb> subFieldNameCombo = new ComboBox<ModelPropertyWeb>();
	private NumberField beginIndexEdit = new NumberField();
	private NumberField endIndexEdit = new NumberField();

	private LayoutContainer container;
	
	@SuppressWarnings("unchecked")
	public FileLoaderConfigurationView(Controller controller) {
		super(controller);
		List<ModelPropertyWeb> fieldNames = (List<ModelPropertyWeb>) Registry.get(Constants.PERSON_MODEL_ATTRIBUTE_NAMES);
		try {
			listStore.add(fieldNames);
		} catch (Exception e) {
			Info.display("Message", e.getMessage());
		}
	}

	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.FileLoaderConfigurationView) {
			initUI();
		} else if (event.getType() == AppEvents.FileLoaderConfigurationReceived) {
			store.removeAll();
			LoaderConfigWeb config = (LoaderConfigWeb) event.getData();
			isHeaderPresentCheckBox.setValue(config.getHeaderLinePresent());
			store.add(config.getLoaderDataFields());
		}
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

//		store.groupBy("blockingRound");
		controller.handleEvent(new AppEvent(AppEvents.FileLoaderConfigurationRequest));
		
		buildAddEditFieldDialog();
		buildAddEditSubFieldDialog();
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());
		
		ColumnConfig fieldName = new ColumnConfig("fieldName", "Field Name", 150);
		ColumnConfig formatString = new ColumnConfig("formatString", "Format String", 100);
//		ColumnConfig functionName = new ColumnConfig("functionName", "Function Name", 100);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(fieldName);
		config.add(formatString);
		
		final ColumnModel cm = new ColumnModel(config);

		grid = new Grid<LoaderDataFieldWeb>(store, cm);
		grid.setBorders(true);

		ContentPanel cp = new ContentPanel();
		cp.setHeading("File Loader Configuration");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/table_gear.png"));
		cp.setLayout(new FillLayout());
		cp.setSize(500, 350);

		ToolBar toolBar = new ToolBar();
		final Button addFieldButton =
			new Button("Add", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					// Make sure we are starting with a clean slate
					addOrEditFieldMode = true;
					addEditLoaderDataFieldDialog.show();
					editSubFieldGrid.getStore().removeAll();
					fieldNameCombo.clearSelections();
					formatStringEdit.clear();
//					functionNameEdit.clear();
					isPartOfCompositionCheckBox.setValue(false);
					compositionIndexEdit.clear();
					separatorEdit.clear();
				}
			});
		toolBar.add(addFieldButton);
		final Button editFieldButton =
			new Button("Edit", IconHelper.create("images/folder_edit.png"), new SelectionListener<ButtonEvent>() {
		  		@Override
		  		public void componentSelected(ButtonEvent ce) {
		  			addOrEditFieldMode = false;
					LoaderDataFieldWeb editField = grid.getSelectionModel().getSelectedItem();
					if (editField == null) {
						Info.display("Information", "You must first select a field to be edited before pressing the \"Edit Field\" button.");
						return;
					}
					addEditLoaderDataFieldDialog.show();
					editedFieldIndex = grid.getStore().indexOf(editField);
					editedDataField = editField;
					subFieldStore.removeAll();
					if (editField.getLoaderSubFields() != null) {
						if (editField.getLoaderSubFields().size() > 0) {
							subFieldStore.add(editField.getLoaderSubFields());
						}
					}
					fieldNameCombo.setValue(new ModelPropertyWeb(editField.getFieldName()));
					if (editField.getFormatString() != null)
						formatStringEdit.setValue(editField.getFormatString());
					else
						formatStringEdit.clear();
					isPartOfCompositionCheckBox.setValue(editField.getLoaderFieldComposition() != null);
					if (editField.getLoaderFieldComposition() != null) {
						compositionIndexEdit.setValue(editField.getLoaderFieldComposition().getIndex());
						separatorEdit.setValue(editField.getLoaderFieldComposition().getSeparator());
					} else {
						compositionIndexEdit.clear();
						separatorEdit.clear();
					}
		  		}
		    });
		toolBar.add(editFieldButton);
		final Button removeFieldButton =
			new Button("Remove", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					LoaderDataFieldWeb removeField = grid.getSelectionModel().getSelectedItem();
					if (removeField == null) {
						Info.display("Information", "You must first select a field to be deleted before pressing the \"Remove Round\" button.");
						return;
					}
					for (LoaderDataFieldWeb field : grid.getStore().getModels()) {
						if (field == removeField) {
							grid.getStore().remove(field);
						}
					}
				}
		    });
		toolBar.add(removeFieldButton);
		toolBar.add(new SeparatorToolItem());
		final Button moveUpFieldButton =
			new Button("Move Up", IconHelper.create("images/arrow_up.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (grid.getStore().getCount() > 1) {
						LoaderDataFieldWeb field = grid.getSelectionModel().getSelectedItem();
						if (field == null) {
							Info.display("Information", "You must first select a field before pressing the \"Move Up\" button.");
							return;
						}
						int selectionIndex = grid.getStore().indexOf(field);
						if (selectionIndex > 0) {
							grid.getStore().remove(field);
							grid.getStore().insert(field, selectionIndex - 1);
						} else {
							Info.display("Information", "Cannot move up the first field.");
						}
					}
				}
		    });
		toolBar.add(moveUpFieldButton);
		final Button moveDownFieldButton =
			new Button("Move Down", IconHelper.create("images/arrow_down.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (grid.getStore().getCount() > 1) {
						LoaderDataFieldWeb field = grid.getSelectionModel().getSelectedItem();
						if (field == null) {
							Info.display("Information", "You must first select a field before pressing the \"Move Down\" button.");
							return;
						}
						int selectionIndex = grid.getStore().indexOf(field);
						if (selectionIndex >= 0 && selectionIndex < grid.getStore().getCount() - 1) {
							grid.getStore().remove(field);
							grid.getStore().insert(field, selectionIndex + 1);
						} else {
							Info.display("Information", "Cannot move down the last field.");
						}
					}
				}
		    });
		toolBar.add(moveDownFieldButton);

		grid.getSelectionModel().addListener(Events.SelectionChange,
			new Listener<SelectionChangedEvent<LoaderDataFieldWeb>>() {
				public void handleEvent(SelectionChangedEvent<LoaderDataFieldWeb> be) {
					List<LoaderDataFieldWeb> selection = be.getSelection();
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

		isHeaderPresentCheckBox.setBoxLabel("Is header present in file?");
		toolBar.add(isHeaderPresentCheckBox);
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
				LoaderConfigWeb loaderConfig = new LoaderConfigWeb();
				loaderConfig.setHeaderLinePresent(isHeaderPresentCheckBox.getValue());
				List<LoaderDataFieldWeb> loaderDataFieldsConfig = grid.getStore().getModels();
				loaderConfig.setLoaderDataFields(loaderDataFieldsConfig);
				controller.handleEvent(new AppEvent(AppEvents.FileLoaderConfigurationSave, loaderConfig));
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
	
	private void buildAddEditFieldDialog() {
		addEditLoaderDataFieldDialog = new Dialog();
		addEditLoaderDataFieldDialog.setBodyBorder(false);
		addEditLoaderDataFieldDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addEditLoaderDataFieldDialog.setHeading("Add/Edit Field");
		addEditLoaderDataFieldDialog.setWidth(450);
		addEditLoaderDataFieldDialog.setHeight(500);
		addEditLoaderDataFieldDialog.setButtons(Dialog.OKCANCEL);
		addEditLoaderDataFieldDialog.setHideOnButtonClick(true);
		addEditLoaderDataFieldDialog.setModal(true);
		addEditLoaderDataFieldDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<ModelPropertyWeb> selection = fieldNameCombo.getSelection();
				if (selection != null) {
					if (selection.size() > 0) {
						ModelPropertyWeb field = selection.get(0);
						LoaderDataFieldWeb loaderDataFieldWeb = new LoaderDataFieldWeb();
						loaderDataFieldWeb.setFieldName(field.getName());
						loaderDataFieldWeb.setFormatString(formatStringEdit.getValue());
						if (isPartOfCompositionCheckBox.getValue()) {
							LoaderFieldCompositionWeb loaderFieldCompositionWeb = new LoaderFieldCompositionWeb(
									compositionIndexEdit.getValue().intValue(),
									separatorEdit.getValue());
							loaderDataFieldWeb.setLoaderFieldComposition(loaderFieldCompositionWeb);
						}
						if (editSubFieldGrid.getStore().getCount() > 0) {
							loaderDataFieldWeb.setLoaderSubFields(editSubFieldGrid.getStore().getModels());
						}
						if (addOrEditFieldMode) {
							grid.getStore().add(loaderDataFieldWeb);
						} else {
							grid.getStore().remove(editedDataField);
							grid.getStore().insert(loaderDataFieldWeb, editedFieldIndex);
						}
					}
				}
			}
	    });

		ContentPanel cp = new ContentPanel();
		cp.setHeading("File Field");
		cp.setFrame(false);
		cp.setIcon(IconHelper.create("images/folder.png"));
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(170);
		formLayout.setDefaultWidth(250);
		cp.setLayout(formLayout);
//		cp.setLayout(new FillLayout());
		cp.setSize(450, 500);
		
		fieldNameCombo.setEmptyText("Select a field...");
		fieldNameCombo.setForceSelection(true);
		fieldNameCombo.setDisplayField("name");
//		fieldNameCombo.setWidth(150);
		fieldNameCombo.setStore(listStore);
		fieldNameCombo.setTypeAhead(true);
		fieldNameCombo.setTriggerAction(TriggerAction.ALL);

		fieldNameCombo.setFieldLabel("Field Name");
		cp.add(fieldNameCombo);

		formatStringEdit.setFieldLabel("Format String");
		cp.add(formatStringEdit);

		isPartOfCompositionCheckBox.setBoxLabel("Is part of composition?");
		cp.add(isPartOfCompositionCheckBox);

		compositionIndexEdit.setFieldLabel("Index in composition");
		cp.add(compositionIndexEdit);

		separatorEdit.setFieldLabel("Composition Separator");
		cp.add(separatorEdit);

		LayoutContainer buttonContainer = new LayoutContainer();
		buttonContainer.setHeight(24);
		buttonContainer.setLayout(new ColumnLayout());
		final Button addSubFieldButton =
			new Button("Add", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
		  			addOrEditSubFieldMode = true;
					addEditLoaderSubFieldDialog.show();
					subFieldNameCombo.clearSelections();
					beginIndexEdit.clear();
					endIndexEdit.clear();
				}
		    });
		buttonContainer.add(addSubFieldButton);
		final Button editSubFieldButton =
			new Button("Edit", IconHelper.create("images/folder_edit.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
		  			addOrEditSubFieldMode = false;
		  			LoaderSubFieldWeb editField = editSubFieldGrid.getSelectionModel().getSelectedItem();
					if (editField == null) {
						Info.display("Information", "You must first select a subfield to be edited before pressing the \"Edit\" button.");
						return;
					}
					addEditLoaderSubFieldDialog.show();
					subFieldNameCombo.setValue(new ModelPropertyWeb(editField.getFieldName()));
					editedSubFieldIndex = editSubFieldGrid.getStore().indexOf(editField);
					editedSubField = editField;
					beginIndexEdit.setValue(editField.getBeginIndex());
					endIndexEdit.setValue(editField.getEndIndex());
				}
		    });
		buttonContainer.add(editSubFieldButton);
		final Button removeSubFieldButton =
			new Button("Remove", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
		  		@Override
		  		public void componentSelected(ButtonEvent ce) {
					LoaderSubFieldWeb field = editSubFieldGrid.getSelectionModel().getSelectedItem();
					if (field == null) {
						Info.display("Information", "You must first select a field before pressing the \"Remove Field\" button.");
						return;
					}
					editSubFieldGrid.getStore().remove(field);
		  		}
		    });
		buttonContainer.add(removeSubFieldButton);
		final Button moveUpSubFieldButton =
			new Button("Move", IconHelper.create("images/arrow_up.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (editSubFieldGrid.getStore().getCount() > 1) {
						LoaderSubFieldWeb field = editSubFieldGrid.getSelectionModel().getSelectedItem();
						if (field == null) {
							Info.display("Information", "You must first select a field before pressing the \"Remove Field\" button.");
							return;
						}
						int selectionIndex = editSubFieldGrid.getStore().indexOf(field);
						if (selectionIndex > 0) {
							editSubFieldGrid.getStore().remove(field);
							editSubFieldGrid.getStore().insert(field, selectionIndex - 1);
						}
					}
				}
		    });
		buttonContainer.add(moveUpSubFieldButton);
		final Button moveDownSubFieldButton =
			new Button("Move", IconHelper.create("images/arrow_down.png"), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					if (editSubFieldGrid.getStore().getCount() > 1) {
						LoaderSubFieldWeb field = editSubFieldGrid.getSelectionModel().getSelectedItem();
						if (field == null) {
							Info.display("Information", "You must first select a field before pressing the \"Remove Field\" button.");
							return;
						}
						int selectionIndex = editSubFieldGrid.getStore().indexOf(field);
						if (selectionIndex >= 0 && selectionIndex < editSubFieldGrid.getStore().getCount() - 1) {
							editSubFieldGrid.getStore().remove(field);
							editSubFieldGrid.getStore().insert(field, selectionIndex + 1);
						}
					}
				}
		    });
		buttonContainer.add(moveDownSubFieldButton);
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setId("fieldName");
		column.setHeader("Field Name");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(160);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("beginIndex");
		column.setHeader("Begin Index");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(110);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("endIndex");
		column.setHeader("End Index");
		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setWidth(110);
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		editSubFieldGrid = new Grid<LoaderSubFieldWeb>(subFieldStore, cm);
		editSubFieldGrid.setStyleAttribute("borderTop", "none");
		editSubFieldGrid.setBorders(true);
		editSubFieldGrid.setStripeRows(true);
		editSubFieldGrid.setAutoWidth(true);
		editSubFieldGrid.setHeight(340);
		editSubFieldGrid.getSelectionModel().addListener(Events.SelectionChange,
			new Listener<SelectionChangedEvent<LoaderSubFieldWeb>>() {
				public void handleEvent(SelectionChangedEvent<LoaderSubFieldWeb> be) {
					List<LoaderSubFieldWeb> selection = be.getSelection();
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
							int selectionIndex = editSubFieldGrid.getStore().indexOf(selection.get(0));
							moveUpEnabled = (selectionIndex > 0);
							moveDownEnabled = (selectionIndex < editSubFieldGrid.getStore().getCount() - 1);
						}
					}
					editSubFieldButton.setEnabled(editFieldEnabled);
					removeSubFieldButton.setEnabled(removeFieldEnabled);
					moveUpSubFieldButton.setEnabled(moveUpEnabled);
					moveDownSubFieldButton.setEnabled(moveDownEnabled);
				}
			});

		cp.add(buttonContainer);
		cp.add(editSubFieldGrid);

		addEditLoaderDataFieldDialog.add(cp);
	}

	private void buildAddEditSubFieldDialog() {
		addEditLoaderSubFieldDialog = new Dialog();
		addEditLoaderSubFieldDialog.setBodyBorder(false);
		addEditLoaderSubFieldDialog.setIcon(IconHelper.create("images/folder_go.png"));
		addEditLoaderSubFieldDialog.setHeading("Add/Edit SubField");
		addEditLoaderSubFieldDialog.setAutoWidth(true);
		addEditLoaderSubFieldDialog.setAutoHeight(true);
		addEditLoaderSubFieldDialog.setButtons(Dialog.OKCANCEL);
		addEditLoaderSubFieldDialog.setHideOnButtonClick(true);
		addEditLoaderSubFieldDialog.setModal(true);
		addEditLoaderSubFieldDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<ModelPropertyWeb> selection = subFieldNameCombo.getSelection();
				if (selection != null) {
					if (selection.size() > 0) {
						if ((endIndexEdit.getValue().intValue() >= 0 &&
							beginIndexEdit.getValue().intValue() >= endIndexEdit.getValue().intValue()) ||
							beginIndexEdit.getValue().intValue() < 0)
						{
							Info.display("Information", "Begin index must be smaller than End index.");
							return;
						}
						ModelPropertyWeb field = selection.get(0);
						LoaderSubFieldWeb loaderSubFieldWeb =
							new LoaderSubFieldWeb(field.getName(), beginIndexEdit.getValue().intValue(),
									endIndexEdit.getValue().intValue());
						if (addOrEditSubFieldMode) {
							editSubFieldGrid.getStore().add(loaderSubFieldWeb);
						} else {
							editSubFieldGrid.getStore().remove(editedSubField);
							editSubFieldGrid.getStore().insert(loaderSubFieldWeb, editedSubFieldIndex);
						}
					}
				}
			}
	    });
		
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(90);
		formLayout.setDefaultWidth(155);
		addEditLoaderSubFieldDialog.setLayout(formLayout);

		subFieldNameCombo.setEmptyText("Select a sub field...");
		subFieldNameCombo.setForceSelection(true);
		subFieldNameCombo.setDisplayField("name");
//		subFieldNameCombo.setWidth(150);
		subFieldNameCombo.setStore(listStore);
		subFieldNameCombo.setTypeAhead(true);
		subFieldNameCombo.setTriggerAction(TriggerAction.ALL);

		subFieldNameCombo.setFieldLabel("Sub Field Name");
		addEditLoaderSubFieldDialog.add(subFieldNameCombo);

		beginIndexEdit.setFieldLabel("Begin Index");
		addEditLoaderSubFieldDialog.add(beginIndexEdit);

		endIndexEdit.setFieldLabel("End Index");
		addEditLoaderSubFieldDialog.add(endIndexEdit);
	}

}
