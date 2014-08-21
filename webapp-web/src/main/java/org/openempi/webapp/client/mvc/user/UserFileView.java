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
import org.openempi.webapp.client.model.FileLoaderConfigurationWeb;
import org.openempi.webapp.client.model.ParameterTypeWeb;
import org.openempi.webapp.client.model.UserFileWeb;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class UserFileView extends View
{
	private ListStore<UserFileWeb> store;
	private Grid<UserFileWeb> grid;
	private LayoutContainer container;

//	private CheckBox importOnly;
//	private CheckBox skipHeaderLine;
	
	List<FileLoaderConfigurationWeb> fileLoaderConfigurations;
	FileLoaderConfigurationWeb currentFileLoader = null;
	
	private Dialog fileLoaderConfigurationsDialog = null;	
	private ContentPanel fileLoaderConfigurationsPanel = null;
	private ContentPanel dynamicFieldsPanel = null;
	
		private ComboBox<FileLoaderConfigurationWeb> fileLoadersCombo = new ComboBox<FileLoaderConfigurationWeb>();
		private ListStore<FileLoaderConfigurationWeb> fileLoadersStroe = new GroupingStore<FileLoaderConfigurationWeb>();
	
	private List<UserFileWeb> userFileEntries;
		
	public UserFileView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.FileListView) {
			grid = null;
			initUI();
		} else if (event.getType() == AppEvents.FileLoaderConfigurations) {

			fileLoaderConfigurations = event.getData();
	      	
			controller.handleEvent(new AppEvent(AppEvents.FileListUpdate, null));
			
		} else if (event.getType() == AppEvents.FileListRender) {
			displayRecords((List<UserFileWeb>) event.getData());
			
			// File Loader Configurations
			fileLoadersStroe.removeAll();
		    for (FileLoaderConfigurationWeb fileLoaderConfig : fileLoaderConfigurations) {
		    	// Info.display("File Loader :", fileLoaderConfig.getLoaderName()); 	 
		    	if( currentFileLoader == null ) {
		    		currentFileLoader = fileLoaderConfig;
		    	}
 			    fileLoadersStroe.add(fileLoaderConfig);			  
		    }  
		    
		    showDefaultCursor();

		} else if (event.getType() == AppEvents.FileListRenderDataProfile) {
			displayRecords((List<UserFileWeb>) event.getData());	
			
		    showDefaultCursor();

    		List<UserFileWeb> fileList = store.getModels();
  		    for (UserFileWeb userFile : fileList) {
  			     if (userFile.getProfileProcessed().equals("In Processing")) {
  				     MessageBox.alert("Information", "The existing Data Profile operation is not finished yet. Please wait until the operation is finished", null);  
  				     return;
  			     }
  		    }
		    		  
      	    // controller.handleEvent(new AppEvent(AppEvents.FileEntryDataProfile, userFileEntries));
      	    MessageBox.confirm("Confirm", "Data Profile operation will take a while to finish. Are you sure you want to continue?", listenConfirm); 		
      	    return;				    

		} else if (event.getType() == AppEvents.FileEntryImportSuccess) {	
			
			showDefaultCursor();
			
			String message = event.getData();
	        MessageBox.alert("Information", "" + message, null);  		
	    
		} else if (event.getType() == AppEvents.FileEntryDataProfileSuccess) {	
			
			showDefaultCursor();
			
			String message = event.getData();
	        MessageBox.alert("Information", "" + message, null);  		
		} else if (event.getType() == AppEvents.Error) {	
			
			showDefaultCursor();
			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  			
		}
	}

	private void displayRecords(List<UserFileWeb> userFiles) {
		store.removeAll();
		store.add(userFiles);
		container.layout();
	}

	private void initUI() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		CheckBoxSelectionModel<UserFileWeb> sm = new CheckBoxSelectionModel<UserFileWeb>();
		configs.add(sm.getColumn());

		ColumnConfig column = new ColumnConfig();
		column.setId("name");
		column.setHeader("File Name");
		column.setWidth(150);
		configs.add(column);

		column = new ColumnConfig("dateCreated", "Date Created", 250);
//		column.setAlignment(HorizontalAlignment.RIGHT);
		column.setDateTimeFormat(DateTimeFormat.getFullDateTimeFormat());
		configs.add(column);
		
		column = new ColumnConfig("imported", "Imported?", 70);
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		
		column = new ColumnConfig("rowsImported", "Rows Imported", 115);
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);

		column = new ColumnConfig("rowsProcessed", "Rows Processed", 115);
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);

		column = new ColumnConfig("profiled", "Data Profiled?", 100);
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		
		column = new ColumnConfig("profileProcessed", "Data Profile Processed", 150);
		column.setAlignment(HorizontalAlignment.RIGHT);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);

		container = new LayoutContainer();
		container.setLayout(new CenterLayout());

		ContentPanel cp = new ContentPanel();
		cp.setHeading("User File Management");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/folder.png"));
		cp.setLayout(new FillLayout());
		cp.setSize(1000, 400);

		ToolBar toolBar = new ToolBar();
		
		Label space = new Label("");	
/*		importOnly = new CheckBox();  
		importOnly.setBoxLabel("Import Only");  
		importOnly.setValue(false);  		
		skipHeaderLine = new CheckBox();  
		skipHeaderLine.setBoxLabel("Skip Header Line");  
		skipHeaderLine.setValue(true);  
*/
		toolBar.add(space);
/*		toolBar.add(importOnly);
		toolBar.add(skipHeaderLine);
*/	
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Import", IconHelper.create("images/folder_go.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  if(fileLoaderConfigurations == null || fileLoaderConfigurations.size()==0) {
					  MessageBox.alert("Information", "No any file loader is found.  Please check file loader configuration.", null); 
	        		  return;
	        	  }
	        	  
	        	  List<UserFileWeb> userFileEntries = grid.getSelectionModel().getSelectedItems();
	        	  if (userFileEntries == null || userFileEntries.size() == 0) {
	        		  Info.display("Information","You must first select an entry before pressing the Import button.");
	        		  return;
	        	  }
	        	  
/*	        	  showWaitCursor();
	        	  
	        	  // set values for importOnly and skipHeaderLine
	        	  for (UserFileWeb userFile : userFileEntries) {
	        		  userFile.setImportOnly(importOnly.getValue());
	        		  userFile.setSkipHeaderLine(skipHeaderLine.getValue());		      			
	        	  }
	        	  controller.handleEvent(new AppEvent(AppEvents.FileEntryImport, userFileEntries));
*/
	  			  dynamicFieldsPanel = null;
				  buildFileLoaderConfigurationsDialog();
				  fileLoaderConfigurationsDialog.setHeading("Configure File Loader");
		          Button ok = fileLoaderConfigurationsDialog.getButtonById("ok");
		          ok.setText("Import");
		        	  				
		          fileLoaderConfigurationsDialog.show();
		          
		          fileLoadersCombo.setValue(currentFileLoader);

	          }
	    }));
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Remove", IconHelper.create("images/folder_delete.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  List<UserFileWeb> userFileEntries = grid.getSelectionModel().getSelectedItems();
	        	  if (userFileEntries == null || userFileEntries.size() == 0) {
	        		  Info.display("Information","You must first select an entry before pressing the Remove button.");
	        		  return;
	        	  }
	        	  controller.handleEvent(new AppEvent(AppEvents.FileEntryRemove, userFileEntries));
	          }
	    }));
		
		toolBar.add(new SeparatorToolItem());
		toolBar.add(space);
		toolBar.add(new Button("Data Profile", IconHelper.create("images/script.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  userFileEntries = grid.getSelectionModel().getSelectedItems();
	        	  if (userFileEntries == null || userFileEntries.size() == 0 || userFileEntries.size() > 1) {
	        		  Info.display("Information","You must select one entry before pressing the Data Profile button.");
	        		  return;
	        	  }

				  controller.handleEvent(new AppEvent(AppEvents.FileListUpdateDataProfile, null));
	          }
	    }));

		toolBar.add(new SeparatorToolItem());
		toolBar.add(space);
		toolBar.add(new Button(" Refresh Uploaded Files", IconHelper.create("images/arrow_refresh.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {		        	  
		        	  showWaitCursor();
					  controller.handleEvent(new AppEvent(AppEvents.FileListUpdate, null));
	          }
	    }));
		
		cp.setTopComponent(toolBar);

		store = new ListStore<UserFileWeb>();
		grid = new Grid<UserFileWeb>(store, cm);
		grid.setSelectionModel(sm);
		grid.setAutoExpandColumn("name");
		grid.setBorders(true);
		grid.addPlugin(sm);
		cp.add(grid);
		
		container.add(cp);
		
		final FormPanel panel = new FormPanel();
		panel.setFrame(true);
		String url = GWT.getModuleBaseURL() + "upload";
		panel.setAction(url);
		panel.setEncoding(Encoding.MULTIPART);
		panel.setMethod(Method.POST);
		panel.setFrame(true);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setWidth(350);

		final TextField<String> name = new TextField<String>();
		name.setFieldLabel("Name");
		name.setName("name");
		name.setAllowBlank(false);
		panel.add(name);

		final FileUploadField file = new FileUploadField();
		file.setAllowBlank(false);
		file.setFieldLabel("File");
		file.setName("filename");
		panel.add(file);
		
		panel.addListener(Events.Submit, new Listener<FormEvent>() {
			public void handleEvent(FormEvent be) {
				GWT.log("Event is " + be, null);
				if (!be.getResultHtml().equals("success")) {
					Info.display("Information", be.getResultHtml());
					return;
				}
				controller.handleEvent(new AppEvent(AppEvents.FileListUpdate, be.getResultHtml()));
			}			
		});
		panel.addListener(Events.BeforeSubmit, new Listener<FormEvent>() {
			public void handleEvent(FormEvent be) {
				GWT.log("Event is " + be, null);
			}			
		});
		
		panel.addButton(new Button("Upload", new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  if (name.getValue() == null || file.getValue() == null) {
	        		  Info.display("Information", "You must enter a name and filename before pressing the 'Upload' button");
	        		  return;
	        	  }
	        	  if (existsFileName(name.getValue())) {
	        		  Info.display("Information", "You already have a file with this name. Please choose another name.");
	        		  return;
	        	  }
	        	  panel.submit();
	          }
		}));
		cp.add(panel);
		
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();		
	}

	final Listener<MessageBoxEvent> listenConfirm = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();  
          if( btn.getText().equals("Yes")) {       	  
        	  controller.handleEvent(new AppEvent(AppEvents.FileEntryDataProfile, userFileEntries));
        	  
        	  showWaitCursor();
			  controller.handleEvent(new AppEvent(AppEvents.FileListUpdate, null));
          }
        }  
	};

	//  FileLoaderConfigurationsDialog
	private void buildFileLoaderConfigurationsDialog() {		
/*		if(fileLoaderConfigurationsDialog != null) {
			return;	
		}
*/
		
		fileLoaderConfigurationsDialog = new Dialog();
		fileLoaderConfigurationsDialog.setBodyBorder(false);
		fileLoaderConfigurationsDialog.setWidth(515);
		fileLoaderConfigurationsDialog.setHeight(320);
		fileLoaderConfigurationsDialog.setIcon(IconHelper.create("images/basket.png"));
		fileLoaderConfigurationsDialog.setHeading("File Loader configuration");
		fileLoaderConfigurationsDialog.setButtons(Dialog.OKCANCEL);
		fileLoaderConfigurationsDialog.setModal(true);
		
		
		fileLoaderConfigurationsDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {		
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	String loaderName = currentFileLoader.getLoaderName();
	        	// Info.display("Information", "Loader Name: "+loaderName);
	        	
	    		List<ParameterTypeWeb> types = currentFileLoader.getParameterTypes();	
	    		List<Field<?>> fields = currentFileLoader.getFields();
	          	// Info.display("Information", "Fields size: "+types.size());
	    		
	  			java.util.HashMap<String,Object> map = new java.util.HashMap<String,Object>();
				for (int i=0; i<types.size(); i++){
					 ParameterTypeWeb type = types.get(i);
					 Field<?> field = fields.get(i);
					 
		          	 // Info.display("Information", "Field Name: "+type.getName());
		          	 // Info.display("Information", "Field Type: "+type.getDisplayType());

		     		if(type.getDisplayType().equals("CHECKBOX")) {
		     		   CheckBoxGroup checkGroup = (CheckBoxGroup) field;
		     	       if (checkGroup.getValue() == null) {
					       // Info.display("Information", "checkbox string; value: "+type.getName() + "; null");
		     	    	   map.put(type.getName(),false);

		     	       } else {
		     	    	   // Info.display("Information", "checkbox string; value: "+type.getName()+"; "+checkGroup.getValue().getValue());
		     	    	   map.put(type.getName(),checkGroup.getValue().getValue());
		     	       }
		     		}
		     		
		     		if(type.getDisplayType().equals("TEXTFIELD")) {
		     			// Info.display("Information", "text field string; value: "+ type.getName()+"; " +field.getValue());
		     			map.put(type.getName(),field.getValue());
		     		}			          	 
				}
				
				
	     		showWaitCursor();
	     		
	        	List<UserFileWeb> userFileEntries = grid.getSelectionModel().getSelectedItems();	
	        	  
	        	// set values for importOnly and skipHeaderLine and etc.
	        	for (UserFileWeb userFile : userFileEntries) {
	        		  userFile.setFileLoaderName(loaderName);
	        		  userFile.setFileLoaderMap(map);		      			
	        	}
	        	controller.handleEvent(new AppEvent(AppEvents.FileEntryImport, userFileEntries));
 
	        	  
	        	fileLoaderConfigurationsDialog.hide();					        	  	 
	          }
	    });

		fileLoaderConfigurationsDialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {	        	  
	        	  fileLoaderConfigurationsDialog.hide();
	          }
	    });
		
		fileLoaderConfigurationsPanel = new ContentPanel();
		fileLoaderConfigurationsPanel.setHeading("File Loader");
		fileLoaderConfigurationsPanel.setFrame(true);
		fileLoaderConfigurationsPanel.setIcon(IconHelper.create("images/database.png"));
			FormLayout formLayout = new FormLayout();
			formLayout.setLabelWidth(80);
			formLayout.setDefaultWidth(380);
			fileLoaderConfigurationsPanel.setLayout(formLayout);
			fileLoaderConfigurationsPanel.setSize(510, 260);
		
		fileLoadersCombo = new ComboBox<FileLoaderConfigurationWeb>();
//		fileLoadersCombo.setEmptyText("Select a field...");
		fileLoadersCombo.setAllowBlank(false);
		fileLoadersCombo.setEditable(false);
		fileLoadersCombo.setForceSelection(true);
		fileLoadersCombo.setDisplayField("loaderName");
		fileLoadersCombo.setWidth(150);
		fileLoadersCombo.setStore(fileLoadersStroe);
		fileLoadersCombo.setTypeAhead(true);
		fileLoadersCombo.setTriggerAction(TriggerAction.ALL);
		fileLoadersCombo.setFieldLabel("File Loader");

		fileLoadersCombo.addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<FileLoaderConfigurationWeb>>(){
			public void handleEvent(SelectionChangedEvent<FileLoaderConfigurationWeb> sce)
			{
				FileLoaderConfigurationWeb item = sce.getSelectedItem();
				// Info.display("test", (String) item.get("loaderName"));
		
				if( dynamicFieldsPanel == null ) {
					fileLoaderConfigurationsPanel.add(setupDynamicFieldsPanel((String)item.get("loaderName")));
					fileLoaderConfigurationsPanel.layout();
					return;
				} else if(!item.getLoaderName().equals(currentFileLoader.getLoaderName()) ) {
					currentFileLoader = item;
					fileLoaderConfigurationsPanel.remove(dynamicFieldsPanel);
					fileLoaderConfigurationsPanel.add( setupDynamicFieldsPanel((String)item.get("loaderName")) );
					fileLoaderConfigurationsPanel.layout();					
				}
			}
		});
		
		fileLoaderConfigurationsPanel.add(fileLoadersCombo);		
		
		fileLoaderConfigurationsDialog.add(fileLoaderConfigurationsPanel);
	}
	
	private ContentPanel setupDynamicFieldsPanel(String fileLoaderName) {
		dynamicFieldsPanel = new ContentPanel(); 
		// Info.display("File Loader:", fileLoaderName);
		
		dynamicFieldsPanel.setFrame(true);
		dynamicFieldsPanel.setHeaderVisible(false);
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(120);
		formLayout.setDefaultWidth(340);
		dynamicFieldsPanel.setLayout(formLayout);
		dynamicFieldsPanel.setSize(490, 190);
		
		FileLoaderConfigurationWeb fileLoaderConfig = findFileLoaderConfiguration(fileLoaderName);
		List<ParameterTypeWeb> types = fileLoaderConfig.getParameterTypes();	
		List<Field<?>> dtos = new java.util.ArrayList<Field<?>>(types.size());
      	for (ParameterTypeWeb type : types) {	
      		Field<?> field = CreateDynamicField(type);
      		if(field != null ) {
      			dynamicFieldsPanel.add( field);
      			dtos.add(field);
      		}
      	}	
      	fileLoaderConfig.setFields(dtos);
				
		return dynamicFieldsPanel;
	}
	
	private  Field<?> CreateDynamicField(ParameterTypeWeb type) {
		Field<?> field;
		if(type.getDisplayType().equals("CHECKBOX")) {
			
		   CheckBox checkBox = new CheckBox();  ;
		   checkBox.setBoxLabel("");  		   
		   checkBox.setValue(false); 
		   if(type.getName().equals("skipHeaderLine")) {
			   checkBox.setValue(true); 			   
		   }
		   CheckBoxGroup checkGroup = new CheckBoxGroup();  
		   checkGroup.setFieldLabel(type.getDisplayName());  
		   checkGroup.add(checkBox);  
		   field = checkGroup;
		   return field;
		}
		
		if(type.getDisplayType().equals("TEXTFIELD")) {
			   TextField<String> textEdit = new TextField<String>();
			   textEdit.setFieldLabel(type.getDisplayName());
			   field = textEdit;
			   return field;			
		}	
		return null;
	}
    
    private FileLoaderConfigurationWeb findFileLoaderConfiguration(String fileLoaderName) {
	    for (FileLoaderConfigurationWeb fileLoaderConfig : fileLoaderConfigurations) {
	    	// Info.display("File Loader :", fileLoaderConfig.getLoaderName()); 	 
	    	if( fileLoaderName.equals(fileLoaderConfig.getLoaderName())) {
	    		return fileLoaderConfig;
	    	}		  
	    }
	    return null;
    }
    
	protected boolean existsFileName(String value) {
		List<UserFileWeb> fileList = store.getModels();
		for (UserFileWeb userFile : fileList) {
			if (userFile.getName().equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public static void showWaitCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
	}
	 
	public static void showDefaultCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}
}
