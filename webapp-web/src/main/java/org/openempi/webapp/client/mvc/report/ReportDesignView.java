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
package org.openempi.webapp.client.mvc.report;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.ReportParameterWeb;
import org.openempi.webapp.client.model.ReportQueryParameterWeb;
import org.openempi.webapp.client.model.ReportQueryWeb;
import org.openempi.webapp.client.model.ReportWeb;
import org.openempi.webapp.client.ui.util.InputFormData;
import org.openempi.webapp.resources.client.model.ParameterType;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;  
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import com.google.gwt.core.client.GWT;

public class ReportDesignView extends View
{
	public static final String ADD_REPORT = "Add";
	public static final String EDIT_REPORT = "Edit";
	public static final String DELETE_REPORT = "Delete";
	
	private Grid<ReportWeb> grid;
	private GroupingStore<ReportWeb> store = new GroupingStore<ReportWeb>();
	
	private Dialog reportDesignDialog = null;	
	private String addOrEditOrDeleteMode = "";
		private TextField<String> reportNameEdit = new TextField<String>();
		private TextField<String> reportNameDisplayedEdit = new TextField<String>();
		private TextField<String> reportDescriptionEdit = new TextField<String>();
		private TextField<String> reportTemplateEdit = new TextField<String>();
		private TextField<String> reportDataGeneratorEdit = new TextField<String>();
		
		private Grid<ReportParameterWeb> parametersGrid;
		private ListStore<ReportParameterWeb> parameterStore = new GroupingStore<ReportParameterWeb>(); 
		
		private Grid<ReportQueryWeb> queryGrid;
		private ListStore<ReportQueryWeb> queryStore = new GroupingStore<ReportQueryWeb>();	
		
	private Dialog parameterDialog = null;	
	private String addOrEditParameterMode = "";
		private TextField<String> parameterNameEdit = new TextField<String>();
		private TextField<String> parameterNameDisplayedEdit = new TextField<String>();
		private TextField<String> parameterDescriptionEdit = new TextField<String>();		
		
		private ListStore<ParameterType> parameterDataTypeStore = new ListStore<ParameterType>();
		private ComboBox<ParameterType> parameterDataTypeCombo = new ComboBox<ParameterType>();
		
		
	private Dialog queryDialog = null;
	private String addOrEditQueryMode = "";
		private TextField<String> queryNameEdit = new TextField<String>();
//		private TextField<String> queryQueryEdit = new TextField<String>();
		private TextArea queryQueryEdit = new TextArea();
		
		private ComboBox<ReportParameterWeb> reportParameterCombo = new ComboBox<ReportParameterWeb>();
		private ListStore<ReportParameterWeb> reportParameterStore = new GroupingStore<ReportParameterWeb>();
		
		private Grid<ReportQueryParameterWeb> queryParameterGrid;
		private ListStore<ReportQueryParameterWeb> queryParameterStore = new GroupingStore<ReportQueryParameterWeb>();
		
	private ReportWeb editedReport;
	private ReportParameterWeb editedParameter;
	private ReportQueryWeb editedQuery;
	
	private LayoutContainer container;
	

	
	@SuppressWarnings("unchecked")
	public ReportDesignView(Controller controller) {
		super(controller);
		
		parameterDataTypeStore.add(InputFormData.getParameterType());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.ReportDesignView) {
			initUI();
		} else if (event.getType() == AppEvents.Logout) {
			// Info.display("Information", "Report View Logout.");		
			
			if( reportDesignDialog.isVisible() )
				reportDesignDialog.close();
			
  		    Dispatcher.get().dispatch(AppEvents.Logout);
			
		} else if (event.getType() == AppEvents.ReportReceived) {
			
			// Info.display("Information", "ReportReceived.");
			store.removeAll();
			parameterStore.removeAll();
			queryStore.removeAll();
						
			List<ReportWeb> reports = (List<ReportWeb>) event.getData();						
			store.add(reports);
			
			grid.getSelectionModel().select(0, true);
			grid.getSelectionModel().deselect(0);
		} else if (event.getType() == AppEvents.ReportAddComplete) {	
			
			ReportWeb report = event.getData();
	      	store.add(report);	      	
	        MessageBox.alert("Information", "The report was successfully added", null); 
	        
        	reportDesignDialog.close();
	        
		} else if (event.getType() == AppEvents.ReportUpdateComplete) {	
			
			store.remove(editedReport);
			ReportWeb report = event.getData();
	      	store.add(report);	      	
	        MessageBox.alert("Information", "The report was successfully updated", null);  	
	        
        	reportDesignDialog.close();
	        
		} else if (event.getType() == AppEvents.ReportDeleteComplete) {			
			
        	store.remove(editedReport);
	        MessageBox.alert("Information", "The report was successfully deleted", null);  	  
	        
	        reportDesignDialog.close();
			
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  			
		}
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		controller.handleEvent(new AppEvent(AppEvents.ReportRequest));
		
		buildReportDesignDialog();
		
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());
		
		ColumnConfig reportName = new ColumnConfig("name", "Name", 180);
		ColumnConfig reportNameDisplayed = new ColumnConfig("nameDisplayed", "Name Displayed", 180);
		ColumnConfig reportDescription = new ColumnConfig("description", "Description", 180);
		ColumnConfig reportTemplate = new ColumnConfig("templateName", "Template File", 180);
		ColumnConfig generatedReport = new ColumnConfig("dataGenerator", "Data Generator", 160);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(reportName);
		config.add(reportNameDisplayed);
		config.add(reportDescription);
		config.add(reportTemplate);
		config.add(generatedReport);
		
		final ColumnModel cm = new ColumnModel(config);
		grid = new Grid<ReportWeb>(store, cm);
		grid.setBorders(true);

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Report Design");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/report.png"));
		cp.setLayout(new FillLayout());
		cp.setSize(900, 350);

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(" Add Report ", IconHelper.create("images/report_go.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  // Make sure we are starting with a clean slate
	      		  reportDesignDialog.setIcon(IconHelper.create("images/report_go.png"));
	      		  reportDesignDialog.setHeading("Add Report Design");	      	
				  Button ok = reportDesignDialog.getButtonById("ok");
				  ok.setText("Add Report");

				  addOrEditOrDeleteMode = ADD_REPORT;
	      		  reportDesignDialog.show();
	      		  
				  readOnlyFields(false);
	      		  reportNameEdit.clear();
	      		  reportNameDisplayedEdit.clear();
	      		  reportDescriptionEdit.clear();
	      		  reportTemplateEdit.clear();
	      		  reportDataGeneratorEdit.clear();	
	      		  
	      		  parameterStore.removeAll();	      		       		  
	      		  queryStore.removeAll();
	          }
	    }));
		
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button(" Edit Report ", IconHelper.create("images/report_edit.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	      		  reportDesignDialog.setIcon(IconHelper.create("images/report_edit.png"));
	      		  reportDesignDialog.setHeading("Edit Report Design");
				  Button ok = reportDesignDialog.getButtonById("ok");
				  ok.setText("Update Report");
				 
				  ReportWeb editReport = grid.getSelectionModel().getSelectedItem();
				  if (editReport == null) {
						Info.display("Information", "You must first select a field to be edited before pressing the \"Edit Report\" button.");
						return;
				  }			
				  addOrEditOrDeleteMode = EDIT_REPORT;
	      		  reportDesignDialog.show();
	      		  
				  editedReport = editReport;
				  
				  readOnlyFields(false);
	      		  reportNameEdit.setValue(editReport.getName());
	      		  reportNameDisplayedEdit.setValue(editReport.getNameDisplayed());
	      		  reportDescriptionEdit.setValue(editReport.getDescription());
	      		  reportTemplateEdit.setValue(editReport.getTemplateName());
	      		  reportDataGeneratorEdit.setValue(editReport.getDataGenerator());
	      		  
	      		  parameterStore.removeAll();
	      		  if( editReport.getReportParameters() != null) {	   
		      		  for( ReportParameterWeb reportParameter : editReport.getReportParameters()) {	      			  
			    			// Info.display("Report Parameter:", reportParameter.getName());	
			    			parameterStore.add(reportParameter);			
		      		  }		
	      		  }
	      		  parametersGrid.getSelectionModel().select(0, true);
	      		  parametersGrid.getSelectionModel().deselect(0);	
	      		       		  
	      		  queryStore.removeAll();
	      		  if( editReport.getReportQueries() != null ) {	      			  	      		  
		      		  for( ReportQueryWeb reportQuery : editReport.getReportQueries()) {
		      			  
			      		  // for( ReportQueryParameterWeb reportQueryParameter : reportQuery.getReportQueryParameters()) {
				      	  //	  Info.display("Report Parameter:", reportQueryParameter.getReportParameter().getName());	
			      		  // }
			      		  // Info.display("Report Query:", reportQuery.getName());	
			      		  queryStore.add(reportQuery);			
		      		  }		
	      		  }
	      		  queryGrid.getSelectionModel().select(0, true);
	      		  queryGrid.getSelectionModel().deselect(0);	   
	          }
	    }));
		
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button(" Remove Report ", IconHelper.create("images/report_delete.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	      		  reportDesignDialog.setIcon(IconHelper.create("images/report_delete.png"));
	      		  reportDesignDialog.setHeading("Delete Report Design");	      		  
				  Button ok = reportDesignDialog.getButtonById("ok");
				  ok.setText("Delete Report");
				  
				  ReportWeb removeReport = grid.getSelectionModel().getSelectedItem();
				  if (removeReport == null) {
						Info.display("Information", "You must first select a field before pressing the \"Remove Report\" button.");
						return;
				  }					  
				  addOrEditOrDeleteMode = DELETE_REPORT;
	      		  reportDesignDialog.show();	   	

				  editedReport = removeReport;
					
				  readOnlyFields(true);
	      		  reportNameEdit.setValue(removeReport.getName());
	      		  reportNameDisplayedEdit.setValue(removeReport.getNameDisplayed());
	      		  reportDescriptionEdit.setValue(removeReport.getDescription());
	      		  reportTemplateEdit.setValue(removeReport.getTemplateName());
	      		  reportDataGeneratorEdit.setValue(removeReport.getDataGenerator());
	      		  
	      		  parameterStore.removeAll();
	      		  if( removeReport.getReportParameters() != null) {	   
		      		  for( ReportParameterWeb reportParameter : removeReport.getReportParameters()) {	      			  
			    			// Info.display("Report Parameter:", reportParameter.getName());	
			    			parameterStore.add(reportParameter);			
		      		  }	
	      		  }
	      		  parametersGrid.getSelectionModel().select(0, true);
	      		  parametersGrid.getSelectionModel().deselect(0);	
	      		       		  
	      		  queryStore.removeAll();
	      		  if( removeReport.getReportQueries() != null) {	   
		      		  for( ReportQueryWeb reportQuery : removeReport.getReportQueries()) {
			    			// Info.display("Report Parameter:", reportQuery.getName());	
			    			queryStore.add(reportQuery);			
		      		  }		
	      		  }
	      		  queryGrid.getSelectionModel().select(0, true);
	      		  queryGrid.getSelectionModel().deselect(0);	   
	      				  
	          }
	    }));
		cp.setTopComponent(toolBar);
		
/*		LayoutContainer c = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(15));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.CENTER);
		c.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));
		c.add(new Button(" Generate Report ", IconHelper.create("images/report_disk.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {

	          }
	    }), layoutData); 
		
		c.add(new Button(" View Report ", IconHelper.create("images/magnifier.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {

	          }
	    }), layoutData); 
		cp.setBottomComponent(c);
*/
		cp.add(grid);

		container.add(cp);

		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
 
	private void readOnlyFields(boolean enable) {
		  reportNameEdit.setReadOnly(enable);
		  reportNameDisplayedEdit.setReadOnly(enable);
		  reportDescriptionEdit.setReadOnly(enable);
		  reportTemplateEdit.setReadOnly(enable);
		  reportDataGeneratorEdit.setReadOnly(enable);
	}
	
	final Listener<MessageBoxEvent> listenConfirmDelete = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();  
          if( btn.getText().equals("Yes")) {
        	  
			  controller.handleEvent(new AppEvent(AppEvents.ReportDelete, editedReport));	
          }
        }  
	};
	
	// Add/Edit/Delete Report Dialog
	private void buildReportDesignDialog() {		
		if(reportDesignDialog != null)
			return;
		
		reportDesignDialog = new Dialog();
		reportDesignDialog.setBodyBorder(false);
		reportDesignDialog.setWidth(960);
		reportDesignDialog.setHeight(535);
		reportDesignDialog.setButtons(Dialog.OKCANCEL);
		reportDesignDialog.setModal(true);
		reportDesignDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  	        	  
				if (addOrEditOrDeleteMode.equals(ADD_REPORT)) {  // Add	
					
					if ( !reportNameEdit.isValid() || !reportNameDisplayedEdit.isValid() || !reportTemplateEdit.isValid() || !reportDataGeneratorEdit.isValid() ) {
						 Info.display("Add Report:", "Invalid fields");	
						 return;
					}
												
					// check duplicate report name
					for (int i=0;i<store.getCount();i++){
					     ReportWeb report = store.getAt(i);
					     if( reportNameEdit.getValue().equals(report.getName())) {						    	 						    	 
					    	 reportNameEdit.markInvalid("Duplicate report name");
					    	 return;
					     }
					}
				
					ReportWeb addingReport = copyReportFromGUI(new ReportWeb());
							
					controller.handleEvent(new AppEvent(AppEvents.ReportAdd, addingReport));		
									
				} else if (addOrEditOrDeleteMode.equals(EDIT_REPORT)) { // Edit
					if ( !reportNameEdit.isValid() || !reportNameDisplayedEdit.isValid() || !reportTemplateEdit.isValid() || !reportDataGeneratorEdit.isValid() ) {
						 Info.display("Add Report:", "Invalid fields");	
						 return;
					}
												
					// check duplicate report name
					for (int i=0;i<store.getCount();i++){
					     ReportWeb report = store.getAt(i);					     
			        	 if( report.getName() != editedReport.getName() ) { // not itself
						     if( reportNameEdit.getValue().equals(report.getName())) {						    	 						    	 
						    	 reportNameEdit.markInvalid("Duplicate report name");
						    	 return;
						     }			        		 
			        	 }
					}
					
					ReportWeb updatingReport = copyReportFromGUI(editedReport);						
							
					controller.handleEvent(new AppEvent(AppEvents.ReportUpdate, updatingReport));											
					
				} else if (addOrEditOrDeleteMode.equals(DELETE_REPORT)) { // Delete
					
	        	  	MessageBox.confirm("Confirm", "Delete operation cannot be undone. Are you sure you want to delete this report?", listenConfirmDelete); 		
	        	  	return;										
				}				
	          }
	    });
		
		reportDesignDialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  reportDesignDialog.hide();
	          }
	    });
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Report Design");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/report.png"));
		// cp.setLayout(new FillLayout());
			FormLayout formLayout = new FormLayout();
			formLayout.setLabelWidth(120);
			formLayout.setDefaultWidth(260);
		cp.setLayout(formLayout);
		cp.setSize(950, 510);
		
		reportNameEdit.setFieldLabel("Name");
		reportNameDisplayedEdit.setFieldLabel("Name Displayed");
		reportDescriptionEdit.setFieldLabel("Description");
		reportTemplateEdit.setFieldLabel("Template File");
		reportDataGeneratorEdit.setFieldLabel("Data Generator");		
		reportNameEdit.setAllowBlank(false);
		reportNameDisplayedEdit.setAllowBlank(false);
		reportTemplateEdit.setAllowBlank(false);
		reportDataGeneratorEdit.setAllowBlank(false);
		
		cp.add(reportNameEdit);
		cp.add(reportNameDisplayedEdit);
		cp.add(reportDescriptionEdit);
		cp.add(reportTemplateEdit);
		cp.add(reportDataGeneratorEdit);
			
		LayoutContainer formContainer = new LayoutContainer();
			ColumnLayout columnLayout = new ColumnLayout();
			formContainer.setLayout(columnLayout);  
			formContainer.add( setupParameterPanel(""), new ColumnData(.5));
			formContainer.add( setupQueryPanel(""), new ColumnData(.5));
			
		cp.add( formContainer );
	
		reportDesignDialog.add(cp);
	}
	
	private ReportWeb copyReport(ReportWeb report) {
		ReportWeb updatingReport = new ReportWeb();	
		
		updatingReport.setReportId(report.getReportId());
		updatingReport.setName(report.getName());
		updatingReport.setNameDisplayed(report.getNameDisplayed());
		updatingReport.setDescription(report.getDescription());
		updatingReport.setTemplateName(report.getTemplateName());
		updatingReport.setDataGenerator(report.getDataGenerator());		
		
		return updatingReport;
	}
	
	private ReportWeb copyReportFromGUI(ReportWeb updateReport) {
		ReportWeb report = copyReport(updateReport);	

		report.setName(reportNameEdit.getValue());
		report.setNameDisplayed(reportNameDisplayedEdit.getValue());
		report.setDescription(reportDescriptionEdit.getValue());
		report.setTemplateName(reportTemplateEdit.getValue());
		report.setDataGenerator(reportDataGeneratorEdit.getValue());
					
		// Parameters
		Set<ReportParameterWeb> rps = new HashSet<ReportParameterWeb>();
		for (int i=0;i<parameterStore.getCount();i++){
			ReportParameterWeb parameter = parameterStore.getAt(i);
			rps.add(parameter);
		}
		report.setReportParameters(rps);

		// Queries
		Set<ReportQueryWeb> rqs = new HashSet<ReportQueryWeb>();
		for (int i=0;i<queryStore.getCount();i++){
			ReportQueryWeb query = queryStore.getAt(i);
			rqs.add(query);
		}
		report.setReportQueries(rqs);
		
		return report;
	}
	
	//	Add/Edit/Delete View Parameter Panel
	private ContentPanel setupParameterPanel(String title) {
		ContentPanel cp = new ContentPanel(); 
		
		cp.setFrame(true);
		cp.setHeaderVisible(false);
		cp.setLayout(new FillLayout());
		cp.setSize(470, 300);
		
		ToolBar toolBar = new ToolBar();

		toolBar.add(new Button("Add Parameter", IconHelper.create("images/basket_add.png"), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if (addOrEditOrDeleteMode.equals(DELETE_REPORT)) {
					return;					
				}
				
				buildAddParameterDialog();
	        	addOrEditParameterMode = ADD_REPORT;
	        	
	        	parameterDialog.setHeading("Add Report Parameter");
	        	Button ok = parameterDialog.getButtonById("ok");
	        	ok.setText("Add Parameter");
	        	  
				parameterNameEdit.clear();
				parameterNameDisplayedEdit.clear();
				parameterDescriptionEdit.clear();				
				parameterDataTypeCombo.clear();
			
				parameterDialog.show();
			}
	    }));

		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Edit Parameter", IconHelper.create("images/basket_edit.png"), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if (addOrEditOrDeleteMode.equals(DELETE_REPORT)) {
					return;					
				}
				
	        	ReportParameterWeb editField = parametersGrid.getSelectionModel().getSelectedItem();
	        	if (editField == null) {
	        		Info.display("Information","You must first select a field before pressing the \"Edit Parameter\" button.");
	        		return;
	        	}
				buildAddParameterDialog();				
	        	addOrEditParameterMode = EDIT_REPORT;
	        	
	        	parameterDialog.setHeading("Edit Report Parameter");
	        	Button ok = parameterDialog.getButtonById("ok");
	        	ok.setText("Update Parameter");
	        	parameterDialog.show();
	        	
	        	editedParameter = editField;
	        	
				parameterNameEdit.setValue(editField.getName());
				parameterNameDisplayedEdit.setValue(editField.getNameDisplayed());
				parameterDescriptionEdit.setValue(editField.getDescription());								
				for( ParameterType paraType : parameterDataTypeStore.getModels()) {
					if(paraType.getCode().equals(editField.getParameterDatatype()))
						parameterDataTypeCombo.setValue(paraType);
				}
				
			}
	    }));
		

		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Remove Parameter", IconHelper.create("images/basket_delete.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  if (addOrEditOrDeleteMode.equals(DELETE_REPORT)) {
	        		  return;					
	        	  }
	        	  
	        	  ReportParameterWeb field = parametersGrid.getSelectionModel().getSelectedItem();
	        	  if (field == null) {
	        		  Info.display("Information","You must first select a field before pressing the \"Remove Parameter\" button.");
	        		  return;
	        	  }
	        	  
	
	        	  // check if parameter is used in the report Query
	        	  for (int i=0; i<queryStore.getCount(); i++){
					   ReportQueryWeb reportQuery = queryStore.getAt(i);
					   if( reportQuery.getReportQueryParameters() != null )  {
						   for( ReportQueryParameterWeb reportQueryParameter : reportQuery.getReportQueryParameters()) {
					      	  	// Info.display("Report Parameter:", reportQueryParameter.getReportParameter().getName());	
					      	  	if( field.getName().equals( reportQueryParameter.getReportParameter().getName()) ) {
					      	  		
								    MessageBox.alert("Information", "This paremeter is used by Query '"+reportQuery.getName() +"' and cannot be removed", null); 
							    	return;
							     }
	
						   }
					   }
	        	  }	        	  
	        	  parametersGrid.getStore().remove(field);
	          }
	    }));
		cp.setTopComponent(toolBar);
		
		ColumnConfig pName = new ColumnConfig("name", "Name", 100);
		ColumnConfig pNameDisplayed = new ColumnConfig("nameDisplayed", "Name Displayed", 150);		
		ColumnConfig pDescription = new ColumnConfig("description", "Description", 120);
		
		ColumnConfig pDataType = new ColumnConfig("parameterDatatype", "Data Type", 80);
		pDataType.setRenderer(new GridCellRenderer<ReportParameterWeb>() {  
			public Object render(ReportParameterWeb model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config, int rowIndex, int colIndex,
					ListStore<ReportParameterWeb> store, Grid<ReportParameterWeb> grid) {			
			        	String type = "";
			        	if( model.getParameterDatatype() != null ) {		        	
				        	if( model.getParameterDatatype() == 0) {
				        		type = "Date";	        		
				        	} else if( model.getParameterDatatype() == 1) {
					        	type = "String";	     
				        	} else if( model.getParameterDatatype() == 2) {
						        type = "Numeric";	 
				        	}
			        	}
			        	return type;
			}
		});  
	      
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(pName);
		config.add(pNameDisplayed);
		config.add(pDescription);
		config.add(pDataType);
		

		ColumnModel cm = new ColumnModel(config);	    
		parametersGrid = new Grid<ReportParameterWeb>(parameterStore, cm);
		parametersGrid.setStyleAttribute("borderTop", "none");
		parametersGrid.setBorders(true);
		parametersGrid.setStripeRows(true); 
		cp.add(parametersGrid);
	
		return cp;
	}
	
	// Add/Edit/Delete View Query Panel 
	private ContentPanel setupQueryPanel(String title) {
		ContentPanel cp = new ContentPanel(); 
		
		cp.setFrame(true);
		cp.setHeaderVisible(false);
		cp.setLayout(new FillLayout());
		cp.setSize(470, 300);
		
		ToolBar toolBar = new ToolBar();

		toolBar.add(new Button("Add Query", IconHelper.create("images/database_add.png"), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
	        	if (addOrEditOrDeleteMode.equals(DELETE_REPORT)) {
	        		return;					
	        	}
	        	
				if( parameterStore.getCount() > 0 ) {
					buildAddQueryDialog();	
		        	addOrEditQueryMode = ADD_REPORT;
		        	
		        	queryDialog.setHeading("Add Report Query");
		        	Button ok = queryDialog.getButtonById("ok");
		        	ok.setText("Add Query");
		        	queryDialog.show();
		        	
					queryNameEdit.clear();
					queryQueryEdit.clear();
					
					// Fill the report parameters to the combo
					reportParameterCombo.clear();
					reportParameterStore.removeAll();
					for (int i=0;i<parameterStore.getCount();i++){
						ReportParameterWeb parameter = parameterStore.getAt(i);
						reportParameterStore.add(parameter);
					}	
					
					queryParameterStore.removeAll();
				} else {
					Info.display("Information", "There is no parameters in this report yet.");
				}
			}
	    }));
		
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Edit Query", IconHelper.create("images/database_edit.png"), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
	        	  if (addOrEditOrDeleteMode.equals(DELETE_REPORT)) {
	        		  return;					
	        	  }				
	        	  ReportQueryWeb editField = queryGrid.getSelectionModel().getSelectedItem();
	        	  if (editField == null) {
	        		  Info.display("Information","You must first select a field before pressing the \"Edit Query\" button.");
	        		  return;
	        	  }
	        	  
	        	  buildAddQueryDialog();
	        	  addOrEditQueryMode = EDIT_REPORT;
	        	  
	        	  queryDialog.setHeading("Edit Report Query");
	        	  Button ok = queryDialog.getButtonById("ok");
	        	  ok.setText("Update Query");
	        	  queryDialog.show();

	        	  editedQuery = editField;
					
				  queryNameEdit.setValue(editField.getName());
				  queryQueryEdit.setValue(editField.getQuery());
				  
					// Fill the report parameters to the combo
					reportParameterCombo.clear();
					reportParameterStore.removeAll();
					for (int i=0;i<parameterStore.getCount();i++){
						ReportParameterWeb parameter = parameterStore.getAt(i);
						reportParameterStore.add(parameter);
					}	
					
					queryParameterStore.removeAll();
		      		for( ReportQueryParameterWeb reportQueryParameter : editField.getReportQueryParameters()) {
		      			queryParameterStore.add(reportQueryParameter);
		      		}
			}
	    }));
		
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Remove Query", IconHelper.create("images/database_delete.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  if (addOrEditOrDeleteMode.equals(DELETE_REPORT)) {
	        		  return;					
	        	  }
	        	  
	        	  ReportQueryWeb field = queryGrid.getSelectionModel().getSelectedItem();
	        	  if (field == null) {
	        		  Info.display("Information","You must first select a field before pressing the \"Remove Query\" button.");
	        		  return;
	        	  }
	        	  queryGrid.getStore().remove(field);
	          }
	    }));
		cp.setTopComponent(toolBar);
		
		XTemplate tpl = XTemplate.create(getTemplate());
		RowExpander expander = new RowExpander();
		expander.setTemplate(tpl);
		
		ColumnConfig pName = new ColumnConfig("name", "Name", 80);
		ColumnConfig pQuery = new ColumnConfig("query", "Query", 350);
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		configs.add(expander);
		configs.add(pName);
		configs.add(pQuery);

		ColumnModel cm = new ColumnModel(configs);
		
		queryGrid = new Grid<ReportQueryWeb>(queryStore, cm);
		queryGrid.setStyleAttribute("borderTop", "none");
		queryGrid.setBorders(true);
		queryGrid.setStripeRows(true);
		queryGrid.addPlugin(expander);
		cp.add(queryGrid);
		
		return cp;
	}
	
	private String getTemplate() {
		return "<p class=\"identifierBlock\">" +
				"<b>Query Parameters:</b><br>" +
				"<table class=\"identifierTable\"> " +
				"<tr>" +
					"<th class=\"identifierColumn\">Report Parameter</th>" +
					"<th class=\"identifierColumn\">Name</th>" +
					"<th class=\"identifierColumn\">Required</th>" +
					"<th class=\"identifierColumn\">Substitution Key</th>" +
				"</tr> " +
				"<tpl if=\"typeof reportQueryParameters !=\'undefined\'\">" +
					"<tpl for=\"reportQueryParameters\"> " +
						"<tr> " +
							// "<td>{reportParameter.name}</td> " + 
							"<td> <tpl for=\"reportParameter\"> "+
									"<p>{name}</p>"+
								  "</tpl>"+
							"</td>" +
							"<td>{parameterName}</td><td>{required}</td><td>{substitutionKey}</td>" +
						"</tr> " +
					"</tpl>" +
				"</tpl>" +
				"</table>" +
				"</p>";
	}
	
	//  AddParameterDialog
	private void buildAddParameterDialog() {		
		if(parameterDialog != null)
			return;
		
		parameterDialog = new Dialog();
		parameterDialog.setBodyBorder(false);
		parameterDialog.setWidth(500);
		parameterDialog.setHeight(220);
		parameterDialog.setIcon(IconHelper.create("images/basket_add.png"));
		parameterDialog.setHeading("Add Parameter");
		parameterDialog.setButtons(Dialog.OKCANCEL);
		parameterDialog.setModal(true);
		parameterDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {

					if ( !parameterNameEdit.isValid() || !parameterNameDisplayedEdit.isValid() || !parameterDataTypeCombo.isValid()) {
						 Info.display("Add Report Parameter:", "Invalid fields");	
						 return;
					}
					
					if (addOrEditParameterMode.equals(ADD_REPORT)) {  // Add													
						// check duplicate report parameter
						for (int i=0;i<parameterStore.getCount();i++){
						     ReportParameterWeb parameter = parameterStore.getAt(i);
						     if( parameterNameEdit.getValue().equals(parameter.getName())) {						    	 						    	 
						    	 parameterNameEdit.markInvalid("Duplicate parameter name");
						    	 return;
						     }
						}
						
			        	 ReportParameterWeb reportParameter = new ReportParameterWeb();
			        	 reportParameter.setName(parameterNameEdit.getValue());
			        	 reportParameter.setNameDisplayed(parameterNameDisplayedEdit.getValue());	   
			        	 reportParameter.setDescription(parameterDescriptionEdit.getValue());	 
			        	 
			     		 ParameterType parameterType = parameterDataTypeCombo.getValue();
			    		 if (parameterType != null) {
				        	 reportParameter.setParameterDatatype(parameterType.getCode());
			    		 }
		        	 		        	  
			        	 parameterStore.add(reportParameter);
		     
					} else if (addOrEditParameterMode.equals(EDIT_REPORT)) { // Edit	
						// check duplicate report parameter
						for (int i=0;i<parameterStore.getCount();i++){
						     ReportParameterWeb parameter = parameterStore.getAt(i);
				        	 if( parameter.getName() != editedParameter.getName()) {
							     if( parameterNameEdit.getValue().equals(parameter.getName())) {						    	 						    	 
							    	 parameterNameEdit.markInvalid("Duplicate parameter name");
							    	 return;
							     }
				        	 }
						}
						
			        	 ReportParameterWeb reportParameter = new ReportParameterWeb();
			        	 reportParameter.setName(parameterNameEdit.getValue());
			        	 reportParameter.setNameDisplayed(parameterNameDisplayedEdit.getValue());	   
			        	 reportParameter.setDescription(parameterDescriptionEdit.getValue());	 
			        	 
			     		 ParameterType parameterType = parameterDataTypeCombo.getValue();
			    		 if (parameterType != null) {
				        	 reportParameter.setParameterDatatype(parameterType.getCode());
			    		 }
		        	 	
			    		 // check if name is changed
			        	 if( !editedParameter.getName().equals(reportParameter.getName()) ) { 
			        		 
				        	  // check if edited parameter is used in the report Query
				        	  for (int i=0; i<queryStore.getCount(); i++){
								   ReportQueryWeb reportQuery = queryStore.getAt(i);
								   if( reportQuery.getReportQueryParameters() != null )  {
									   for( ReportQueryParameterWeb reportQueryParameter : reportQuery.getReportQueryParameters()) {
								      	  	if( editedParameter.getName().equals( reportQueryParameter.getReportParameter().getName()) ) {
								      	  		reportQueryParameter.setReportParameter(reportParameter);
										     }
				
									   }
								   }
				        	  }	        	  
			        	 }
			        	 
			    		 parameterStore.remove(editedParameter);	
			        	 parameterStore.add(reportParameter);
			        	 

					}
		        	  	 
					parameterDialog.hide();					        	  	 
				}
	    });
		parameterDialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {	        	  
	        	  parameterDialog.hide();
	          }
	    });
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Parameter");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/basket.png"));
			FormLayout formLayout = new FormLayout();
			formLayout.setLabelWidth(120);
			formLayout.setDefaultWidth(260);
		cp.setLayout(formLayout);
		cp.setSize(500, 220);
		
		parameterNameEdit.setFieldLabel("Name");
		parameterNameDisplayedEdit.setFieldLabel("Name Displayed");
		parameterDescriptionEdit.setFieldLabel("Description");
		
		parameterDataTypeCombo.setEmptyText("Select data type field...");
		parameterDataTypeCombo.setForceSelection(true);
		parameterDataTypeCombo.setDisplayField("name");
		parameterDataTypeCombo.setStore(parameterDataTypeStore);
		parameterDataTypeCombo.setTypeAhead(true);
		parameterDataTypeCombo.setTriggerAction(TriggerAction.ALL);
		parameterDataTypeCombo.setFieldLabel("Data Type");
		
		parameterNameEdit.setAllowBlank(false);
		parameterNameDisplayedEdit.setAllowBlank(false);
		parameterDataTypeCombo.setAllowBlank(false);
		
		cp.add(parameterNameEdit);
		cp.add(parameterNameDisplayedEdit);
		cp.add(parameterDescriptionEdit);
		cp.add(parameterDataTypeCombo);
	
		parameterDialog.add(cp);
	}
	
	//  AddQueryDialog
	private void buildAddQueryDialog() {		
		if(queryDialog != null)
			return;
		
		queryDialog = new Dialog();
		queryDialog.setBodyBorder(false);
		queryDialog.setWidth(525);
		queryDialog.setHeight(460);
		queryDialog.setIcon(IconHelper.create("images/database_add.png"));
		queryDialog.setHeading("Add Query");
		queryDialog.setButtons(Dialog.OKCANCEL);
		queryDialog.setModal(true);
		queryDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  
					if ( !queryNameEdit.isValid() || !queryQueryEdit.isValid() ) {
						 Info.display("Add Report Query:", "Invalid fields");	
						 return;
					}
							
					if (addOrEditQueryMode.equals(ADD_REPORT)) {  // Add	
						// check duplicate report query
						for (int i=0;i<queryStore.getCount();i++){
						     ReportQueryWeb query = queryStore.getAt(i);
						     if( queryNameEdit.getValue().equals(query.getName())) {						    	 						    	 
						    	 queryNameEdit.markInvalid("Duplicate query name");
						    	 return;
						     }
						}
						// check empty for "Query Parameter Name" and "Required" fields
						for (int i=0;i<queryParameterStore.getCount();i++){
							 ReportQueryParameterWeb queryParameter = queryParameterStore.getAt(i);
						     if(queryParameter.getParameterName() == null || queryParameter.getRequired() == null || 
						    	queryParameter.getParameterName().isEmpty() || queryParameter.getRequired().isEmpty() ) {
							    MessageBox.alert("Information", "'Query Parameter Name' and 'Required' fields cannot be empty\n. " +
							    				 				"Please double click the Query Parameter in the list grid and add values to those fields", null); 
						    	return;
						     }
						}
						
						
			        	ReportQueryWeb reportQuery = new ReportQueryWeb();
			        	reportQuery.setName(queryNameEdit.getValue());
			        	reportQuery.setQuery(queryQueryEdit.getValue());	   
						   
						Set<ReportQueryParameterWeb> qps = new HashSet<ReportQueryParameterWeb>();
						for (int i=0;i<queryParameterStore.getCount();i++){
							 ReportQueryParameterWeb parameter = queryParameterStore.getAt(i);
							 qps.add(parameter);
						 }
						 reportQuery.setReportQueryParameters(qps);
						
			        	 queryStore.add(reportQuery);
		        	  
					} else if (addOrEditQueryMode.equals(EDIT_REPORT)) { // Edit		
						
						 // check duplicate report query
						 for (int i=0;i<queryStore.getCount();i++){
						     ReportQueryWeb query = queryStore.getAt(i);
				        	 if( query.getName() != editedQuery.getName()) {
							     if( queryNameEdit.getValue().equals(query.getName())) {						    	 						    	 
							    	 queryNameEdit.markInvalid("Duplicate query name");
							    	 return;
							     }
				        	 }
						 }
						 // check empty for "Query Parameter Name" and "Required" fields
						 for (int i=0;i<queryParameterStore.getCount();i++){
							 ReportQueryParameterWeb queryParameter = queryParameterStore.getAt(i);
						     if(queryParameter.getParameterName() == null || queryParameter.getRequired() == null || 
						    	queryParameter.getParameterName().isEmpty() || queryParameter.getRequired().isEmpty() ) {
							    MessageBox.alert("Information", "'Query Parameter Name' and 'Required' fields cannot be empty\n. " +
							    				 				"Please double click the Query Parameter in the list grid and add values to those fields", null); 
						    	return;
						     }
						  }

			        	  ReportQueryWeb reportQuery = new ReportQueryWeb();
			        	  reportQuery.setName(queryNameEdit.getValue());
			        	  reportQuery.setQuery(queryQueryEdit.getValue());
			        	  
						  Set<ReportQueryParameterWeb> qps = new HashSet<ReportQueryParameterWeb>();
						  for (int i=0;i<queryParameterStore.getCount();i++){
									ReportQueryParameterWeb parameter = queryParameterStore.getAt(i);
								    qps.add(parameter);
						  }
						  reportQuery.setReportQueryParameters(qps);
						  
						  queryStore.remove(editedQuery);						
			        	  queryStore.add(reportQuery);
					}
	        	  
					queryDialog.hide();
	          }
	    });
		queryDialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  
	        	  queryDialog.hide();
	          }
	    });
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Query");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/database.png"));
			FormLayout formLayout = new FormLayout();
			formLayout.setLabelWidth(50);
			formLayout.setDefaultWidth(420);
		cp.setLayout(formLayout);
		cp.setSize(520, 390);
		
		queryNameEdit.setFieldLabel("Name");
		queryQueryEdit.setFieldLabel("Query");
		queryQueryEdit.setHeight(100);
		queryNameEdit.setAllowBlank(false);
		queryQueryEdit.setAllowBlank(false);
		
		cp.add(queryNameEdit);
		cp.add(queryQueryEdit);
		cp.add(setupQueryParameterPanel(""));
	
		queryDialog.add(cp);
	}
	
	private ContentPanel setupQueryParameterPanel(String title) {
		ContentPanel cp = new ContentPanel(); 
		
		cp.setFrame(true);
		cp.setHeaderVisible(false);
		cp.setLayout(new FillLayout());
		cp.setSize(500, 220);
		
		ToolBar toolBar = new ToolBar();

		reportParameterCombo = new ComboBox<ReportParameterWeb>();
		reportParameterCombo.setEmptyText("Select a field...");
		reportParameterCombo.setForceSelection(true);
		reportParameterCombo.setDisplayField("name");
		reportParameterCombo.setWidth(150);
		reportParameterCombo.setStore(reportParameterStore);
		reportParameterCombo.setTypeAhead(true);
		reportParameterCombo.setTriggerAction(TriggerAction.ALL);

		toolBar.add(reportParameterCombo);
		toolBar.add(new Button("Add Query Parameter", IconHelper.create("images/database_add.png"), new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
	        	  List<ReportParameterWeb> selection = reportParameterCombo.getSelection();
	        	  if (selection == null || selection.size() == 0) {	        		
	        		  Info.display("Information", "Please select a field before pressing the \"Add Query Parameter\" button.");
	        		  return;
	        	  }
	        	  
	        	  ReportParameterWeb field = selection.get(0);
	        	  
	        	  // check duplicate report parameter
	        	  //if (!fieldInList(field, queryParameterGrid.getStore())) {
		        	  ReportQueryParameterWeb reportQueryParameter = new ReportQueryParameterWeb();
		        	  reportQueryParameter.setReportParameter(field);
		        	  
		        	  queryParameterGrid.getStore().add(reportQueryParameter);
	        	  //} else {
		        	  //Info.display("Add Report Query Parameter:", "Selected parameter is already added to the query"); 
	        	  //}
			}
			
			private boolean fieldInList(ReportParameterWeb field, ListStore<ReportQueryParameterWeb> listStore) {
				for (ReportQueryParameterWeb item : listStore.getModels()) {
					if (item.getReportParameter().getName().equalsIgnoreCase(field.getName())) {
						return true;
					}
				}
				return false;
			}
			
	    }));
		
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new Button("Remove Query Parameter", IconHelper.create("images/database_delete.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  ReportQueryParameterWeb field = queryParameterGrid.getSelectionModel().getSelectedItem();
	        	  if (field == null) {
	        		  Info.display("Information","You must first select a field before pressing the \"Remove Query Parameter\" button.");
	        		  return;
	        	  }
	        	  queryParameterGrid.getStore().remove(field);
	          }
	    }));
		cp.setTopComponent(toolBar);
		
		ColumnConfig pParameter = new ColumnConfig("reportParameter.name", "Parameter", 120);
		ColumnConfig pQueryParameter = new ColumnConfig("parameterName", "Query Parameter Name", 200);
		TextField<String> queryParameterText = new TextField<String>();  
		queryParameterText.setValidator(new Validator() {	    	
				public String validate(Field<?> field, String value) {	
					   return null;
				}
	    });
		pQueryParameter.setEditor(new CellEditor(queryParameterText)); 
		
		ColumnConfig pRequired = new ColumnConfig("required", "Required", 60);
		TextField<String> requiredText = new TextField<String>();  
		requiredText.setValidator(new Validator() {	    	
				public String validate(Field<?> field, String value) {		
				     if( !value.equals("Y") && !value.equals("N") && !value.equals("y") && !value.equals("n")) {
				    	 return "Required field should be 'Y' or 'N'";
				     }
					 return null;
				}
	    });
		pRequired.setEditor(new CellEditor(requiredText));
	
		ColumnConfig pSubstitutionKey = new ColumnConfig("substitutionKey", "Substitution Key", 100);
		TextField<String> substitutionKeyText = new TextField<String>();  
		substitutionKeyText.setValidator(new Validator() {	    	
				public String validate(Field<?> field, String value) {	
					   return null;
				}
	    });
		pSubstitutionKey.setEditor(new CellEditor(substitutionKeyText));
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		configs.add(pParameter);
		configs.add(pQueryParameter);
		configs.add(pRequired);
		configs.add(pSubstitutionKey);
		
		ColumnModel cm = new ColumnModel(configs);
	    RowEditor<ReportQueryParameterWeb> rowEditor = new RowEditor<ReportQueryParameterWeb>(); 
	    rowEditor.setClicksToEdit(ClicksToEdit.TWO);
	    
		queryParameterGrid = new Grid<ReportQueryParameterWeb>(queryParameterStore, cm);
		queryParameterGrid.setStyleAttribute("borderTop", "none");
		queryParameterGrid.setBorders(true);
		queryParameterGrid.setStripeRows(true);
		queryParameterGrid.addPlugin(rowEditor); 
		cp.add(queryParameterGrid);
		
		return cp;
	}
}
