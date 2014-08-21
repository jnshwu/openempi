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
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.ReportParameterWeb;
import org.openempi.webapp.client.model.ReportQueryParameterWeb;
import org.openempi.webapp.client.model.ReportQueryWeb;
import org.openempi.webapp.client.model.ReportRequestWeb;
import org.openempi.webapp.client.model.ReportRequestParameterWeb;
import org.openempi.webapp.client.model.ReportWeb;
import org.openempi.webapp.client.model.ReportRequestEntryWeb;
import org.openempi.webapp.client.ui.util.Utility;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
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
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReportGenerateView extends View
{
	public static final Integer TYPE_DATE = 0;
	public static final Integer TYPE_STRING = 1;
	public static final Integer TYPE_NUMERIC = 2;
	
	private Grid<ReportWeb> gridReport;
	private GroupingStore<ReportWeb> store = new GroupingStore<ReportWeb>();
	private Grid<ReportRequestEntryWeb> gridEntry;
	private GroupingStore<ReportRequestEntryWeb> storeEntries = new GroupingStore<ReportRequestEntryWeb>();
	
	private Button generateReportButton;
	private Button reloadReportButton;
	private Button viewReportButton;
	private Button reloadReportEntryButton;
	
	private Dialog reportGenerateDialog = null;	
	List<ReportField> widgets = null;
	private LayoutContainer container;
	
	private ReportWeb requestedReport;

	private Frame pdfViewFrame = null;
	
	public ReportGenerateView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.ReportGenerateView) {
			initUI();
			
		} else if (event.getType() == AppEvents.Logout) {
			// Info.display("Information", "Report Generate View Logout.");		
			
			if( reportGenerateDialog != null && reportGenerateDialog.isVisible() )
				reportGenerateDialog.close();
			
  		    Dispatcher.get().dispatch(AppEvents.Logout);
  		    
		} else if (event.getType() == AppEvents.ReportReceived) {
			
			// Info.display("Information", "ReportReceived.");
			store.removeAll();
				
			List<ReportWeb> reports = (List<ReportWeb>) event.getData();						
			store.add(reports);
/*
			for( ReportWeb report : reports) {		
				if(report.getReportQueries() != null ) {
					for( ReportQueryWeb reportQuery : report.getReportQueries()) {
						if(reportQuery.getReportQueryParameters() != null ) {
							for( ReportQueryParameterWeb reportQueryParameter : reportQuery.getReportQueryParameters()) {								
								 Info.display("Information", " Parameter Name: "+ reportQueryParameter.getReportParameter().getName());
							}
						}
			  	  	}	
				}
			}
*/			
			showDefaultCursor();			
			gridReport.getSelectionModel().select(0, true);
			gridReport.getSelectionModel().deselect(0);
			
		} else if (event.getType() == AppEvents.ReportRequestEntryReceived) {
			
			// Info.display("Information", "ReportRequestEntryReceived.");
			storeEntries.removeAll();
				
			List<ReportRequestEntryWeb> reports = (List<ReportRequestEntryWeb>) event.getData();						
			storeEntries.add(reports);

			showDefaultCursor();
			gridEntry.getSelectionModel().select(0, true);
			gridEntry.getSelectionModel().deselect(0);
		} else if (event.getType() == AppEvents.ReportGenerateComplete) {			
			
			ReportRequestEntryWeb entryWeb = event.getData(); 

			List<ReportRequestEntryWeb> reportEntries = new ArrayList<ReportRequestEntryWeb>(storeEntries.getCount());
			
			// put new generate report entry in the first
			reportEntries.add(entryWeb);
			
			int count = storeEntries.getCount();				
			for (int i=0; i < count; i++) {		
				reportEntries.add(storeEntries.getAt(i));				
			}	
			
			storeEntries.removeAll();			
			storeEntries.add(reportEntries);			
 			
	        MessageBox.alert("Information", "The report was successfully submitted", null);  	  	        
	        reportGenerateDialog.close();		
	        
		} else if (event.getType() == AppEvents.Error) {
			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  	
	        
			showDefaultCursor();
	        if( reportGenerateDialog != null ) {
				Button ok = reportGenerateDialog.getButtonById("ok");
				ok.setText("Generate");
				ok.unmask();	
	        }
		} 
	}

	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		controller.handleEvent(new AppEvent(AppEvents.ReportRequest));
		
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());

		LayoutContainer formContainer = new LayoutContainer();
/*			ColumnLayout layout = new ColumnLayout();
			formContainer.setLayout(layout);  
			formContainer.add( setupReportPanel(""), new ColumnData(.5));
			formContainer.add( setupReportEntryPanel(""), new ColumnData(.5));
*/
		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 0, 10, 0));
			formContainer.add( setupReportPanel(""), layoutData);
			formContainer.add( setupReportEntryPanel(""), layoutData);
		container.add( formContainer);
		
		pdfViewFrame = new Frame();
		pdfViewFrame.setSize("0", "0");
		pdfViewFrame.setVisible(false);
		container.add(pdfViewFrame);
		
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
	
	private ContentPanel setupReportPanel(String title) {
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Report Generate");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/report.png"));
		cp.setLayout(new FillLayout());
		cp.setSize(960, 280);

		XTemplate tpl = XTemplate.create(getTemplate());
		tpl.setMaxDepth(5);
		RowExpander expander = new RowExpander();
		expander.setTemplate(tpl);
		
		ColumnConfig reportName = new ColumnConfig("name", "Name", 280);
		ColumnConfig reportNameDisplayed = new ColumnConfig("nameDisplayed", "Name Displayed", 280);
		ColumnConfig reportDescription = new ColumnConfig("description", "Description", 340);
//		ColumnConfig reportTemplate = new ColumnConfig("templateName", "Template File", 200);
//		ColumnConfig generatedReport = new ColumnConfig("dataGenerator", "Data Generator", 160);
//		ColumnConfig reportURL = new ColumnConfig("reportUrl", "Report URL", 180);
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(expander);
		config.add(reportName);
		config.add(reportNameDisplayed);
		config.add(reportDescription);
//		config.add(reportTemplate);
//		config.add(generatedReport);
//		config.add(reportURL);
		
		final ColumnModel cm = new ColumnModel(config);
		gridReport = new Grid<ReportWeb>(store, cm);
		gridReport.setBorders(true);
		gridReport.setStripeRows(true);
		gridReport.addPlugin(expander);

		LayoutContainer buttonContainer = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.CENTER);
		buttonContainer.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));
		generateReportButton = new Button(" Generate Report ", IconHelper.create("images/report_disk.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  
				  ReportWeb report = gridReport.getSelectionModel().getSelectedItem();
				  if (report == null) {
						Info.display("Information", "You must first select a field to be edited before pressing the \"Generate Report\" button.");
						return;
				  }		
				  
				  requestedReport = report;
				  
				  if( report.getReportParameters() != null && report.getReportParameters().size() > 0) {
					  
					  buildGenerateReportDialog( report );						  
				  } else {
					  
					  // no parameters
		      		  ReportRequestWeb request = new ReportRequestWeb();
		      		  request.setReportId(requestedReport.getReportId());
		      		  
					  controller.handleEvent(new AppEvent(AppEvents.ReportGenerate, request));		
				  }				  
	          }
	    }); 

		reloadReportButton = new Button(" Refresh Report", IconHelper.create("images/arrow_refresh.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  
	        	  	showWaitCursor();
	        	  	
	      			controller.handleEvent(new AppEvent(AppEvents.ReportReloadRequest));
	          }
	    });
		
		buttonContainer.add(generateReportButton, layoutData);
		buttonContainer.add(reloadReportButton, layoutData);
		cp.setBottomComponent(buttonContainer);

		cp.add(gridReport);		
		return cp;
	}

	private ContentPanel setupReportEntryPanel(String title) {
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Report Entry");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/report_picture.png"));
		cp.setLayout(new FillLayout());
		cp.setSize(960, 280);

		ColumnConfig reportName = new ColumnConfig("reportName", "Report Name", 180);
		ColumnConfig reportDescription = new ColumnConfig("reportDescription", "Report Description", 180);
		ColumnConfig reportCompleted = new ColumnConfig("completed", "completed", 80);
		ColumnConfig reportDateRequested = new ColumnConfig("dateRequested", "Date Requested", 180);
					 reportDateRequested.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
		ColumnConfig reportDateCompleted = new ColumnConfig("dateCompleted", "Data Completed", 180);
					 reportDateCompleted.setDateTimeFormat(DateTimeFormat.getMediumDateTimeFormat());
		ColumnConfig userName = new ColumnConfig("userName", "User", 120);
//		ColumnConfig reportURL = new ColumnConfig("reportHandle", "Report Handle", 120);
		
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(reportName);
		config.add(reportDescription);
		config.add(reportCompleted);
		config.add(reportDateRequested);
		config.add(reportDateCompleted);
		config.add(userName);
//		config.add(reportURL);
		
		final ColumnModel cm = new ColumnModel(config);
		gridEntry = new Grid<ReportRequestEntryWeb>(storeEntries, cm);
		gridEntry.setBorders(true);
		gridEntry.setStripeRows(true);
		
		LayoutContainer buttonContainer = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.CENTER);
		buttonContainer.setLayout(layout);
	
		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));
		viewReportButton = new Button(" View Report ", IconHelper.create("images/magnifier.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
				  ReportRequestEntryWeb reportEntry = gridEntry.getSelectionModel().getSelectedItem();
				  if (reportEntry == null) {
						Info.display("Information", "You must first select a field to be edited before pressing the \"View Report\" button.");
						return;
				  }			        	  

				  if (reportEntry.getReportHandle() == null || reportEntry.getReportHandle().isEmpty()) {
						Info.display("Information", "You must generate report first then view the report. \"View Report\" button.");
						return;
				  }			        	  
				  // Info.display("Information", " View Report: "+ GWT.getHostPageBaseURL());
	        
				  String url = GWT.getHostPageBaseURL() +"reportDownloadService?reportHandle="+ reportEntry.getReportHandle();
				  pdfViewFrame.setUrl(url);

	          }
	    });
		
		reloadReportEntryButton = new Button(" Refresh Report Entries ", IconHelper.create("images/arrow_refresh.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  
	        	  	showWaitCursor();
	        	  
	      			controller.handleEvent(new AppEvent(AppEvents.ReportRequestEntryRequest));
	          }
	    });
		
		buttonContainer.add(viewReportButton, layoutData);		
		buttonContainer.add(reloadReportEntryButton, layoutData);		
		cp.setBottomComponent(buttonContainer);
		
		cp.add(gridEntry);	
		return cp;
	}
	
	private String getTemplate() {
		return "<p class=\"identifierBlock\">" +
				"<b>Report Parameters:</b><br>" +
				"<table class=\"identifierTable\"> " +
					"<tr>" +
						"<th class=\"identifierColumn\">Name</th>" +
						"<th class=\"identifierColumn\">Name Displayed</th>" +
						"<th class=\"identifierColumn\">Description</th>" +
						"<th class=\"identifierColumn\">Data type</th>" +
					"</tr> " +
					"<tpl for=\"reportParameters\"> " +
						"<tr> " +
							"<td>{name}</td>" +
							"<td>{nameDisplayed}</td>" +
							"<td>{description}</td>" +
							"<td>" +
						     "<tpl if=\"parameterDatatype == 0\"><p>Date</p></tpl>"+
						     "<tpl if=\"parameterDatatype == 1\"><p>String</p></tpl>"+
						     "<tpl if=\"parameterDatatype == 2\"><p>Numeric</p></tpl>"+
							"</td>" +
						"</tr> " +
					"</tpl>" +
				"</table>" +
				"<br>" +

				"<b>Report Queries:</b><br>" +
					"<table class=\"identifierTable\"> " +
						"<tr>" +
							"<th class=\"identifierColumn\">Name</th>" +
							"<th class=\"identifierColumn\">Query</th>" +
							"<th class=\"identifierColumn\">Query Parameters</th>" +
						"</tr> " +
						"<tr>" +
						    "<tpl if=\"typeof reportQueries !=\'undefined\'\">" +
							"<tpl for=\"reportQueries\"> " +
								"<tr> " +
									"<td>{name}</td>" +
									"<td>{query}</td>" +
									"<td>" +
										"<table class=\"identifierTable\"> " +
											"<tr>" +
												"<th class=\"identifierColumn\">Parameter</th>" +
												"<th class=\"identifierColumn\">Name</th>" +
												"<th class=\"identifierColumn\">Required</th>" +
											"</tr> " +
										    "<tpl if=\"typeof reportQueryParameters !=\'undefined\'\">" +
												"<tpl for=\"reportQueryParameters\"> " +
													"<tr> " +
														// values: The values in the current scope.
														"<td>{[values.reportParameter.name]}</td>" +
														"<td>{parameterName}</td><td>{required}</td>" +
													"</tr> " +
												"</tpl>" +
											"</tpl>" +	
										"</table>" +
										"<br>" +
									"</td>" +
								"</tr> " +	
							"</tpl>" +
							"</tpl>" +
						"</tr> " +
					"</table>" +
				"</p>";
	}
	
	//  Add Generate Report Dialog
	@SuppressWarnings("unchecked")
	private void buildGenerateReportDialog(ReportWeb report) {		

		reportGenerateDialog = new Dialog();
		reportGenerateDialog.setBodyBorder(false);
		reportGenerateDialog.setWidth(580);
		reportGenerateDialog.setHeight(360);
		reportGenerateDialog.setIcon(IconHelper.create("images/report.png"));
		reportGenerateDialog.setHeading("Generate Report");
		reportGenerateDialog.setButtons(Dialog.OKCANCEL);
		reportGenerateDialog.setModal(true);
		
	    final BorderLayout layout = new BorderLayout();  
	    reportGenerateDialog.setLayout(layout); 

		Button ok = reportGenerateDialog.getButtonById("ok");
		ok.setText("Generate");
		  		  
		reportGenerateDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  
	        	  // check valid fields
	      		  for( ReportField widget : widgets) {	  
	      			  
	    	        	if( widget.datatype == TYPE_DATE) {		    	        		
	    	        		DateField field = (DateField) widget.field;        
	    	        		if ( !field.isValid() ) 
	    	        			return;
	    	                
	    	        	} else if( widget.datatype == TYPE_STRING) {	    	        		
	    	                TextField<String> field = (TextField<String>) widget.field;               
	    	        		if ( !field.isValid() ) 
	    	        			return;	
	    	        	} else if( widget.datatype == TYPE_NUMERIC) {
	    	        		
	    	        		NumberField field = (NumberField) widget.field;
	    	        		if ( !field.isValid() ) 
	    	        			return;  
	    	        	}	            	
	      		  }
	      		
	      		  ReportRequestWeb request = new ReportRequestWeb();
	      		  request.setReportId(requestedReport.getReportId());
	      		  
	      		  List<ReportRequestParameterWeb> reportParameters = new ArrayList<ReportRequestParameterWeb>();
	      		  for( ReportField widget : widgets) {	   	      			  
	      			  	ReportRequestParameterWeb parameter = new ReportRequestParameterWeb();
	      			  
	      			  	parameter.setParameterName(widget.name);
	      			  	if( widget.datatype == TYPE_DATE) {	
	    	        		
	    	        		DateField field = (DateField) widget.field;  
	  	      			    // parameter.setParameterValue(field.getValue());		  
	  	      			    parameter.setParameterValue( Utility.DateToString(field.getValue()) );		  	      			    
	    	        	} else if( widget.datatype == TYPE_STRING) {
	    	        		
	    	                TextField<String> field = (TextField<String>) widget.field;               
	  	      			    parameter.setParameterValue(field.getValue());
	    	        	} else if( widget.datatype == TYPE_NUMERIC) {
	    	        		
	    	        		NumberField field = (NumberField) widget.field;
	  	      			    parameter.setParameterValue(field.getValue());
	    	        	}	            		      			  
	      			  	reportParameters.add(parameter);
	      		  }
	      		  request.setReportParameters(reportParameters);
	      		  
/*				
	      		  // Debug display	      		  
	      		  List<ReportRequestParameterWeb> parameters = request.getReportParameters();
				  if( parameters != null )  {
					  for (int i=0; i<parameters.size(); i++){
						   ReportRequestParameterWeb parameter =  parameters.get(i);
						   
						   Info.display("Report Parameter:", parameter.getParameterName()+": "+parameter.getParameterValue());	
					  }
				  }	 
*/   
				  controller.handleEvent(new AppEvent(AppEvents.ReportGenerate, request));		
				  Button ok = reportGenerateDialog.getButtonById("ok");
				  ok.setText("please wait...");
				  ok.mask();		  
	          }
	    });
		
		reportGenerateDialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
	        	  
	        	  reportGenerateDialog.close();
	          }
	    });

		ContentPanel leftcp = new ContentPanel();
		leftcp.setHeaderVisible(false);
		leftcp.setFrame(false);
		leftcp.add(new Image("images/GenerateReport.png"));
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Report Parameters");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/report.png"));
			FormLayout formLayout = new FormLayout();
			formLayout.setLabelWidth(80);
			formLayout.setDefaultWidth(260);
		cp.setLayout(formLayout);
		cp.setSize(520, 300);
		cp.setScrollMode(Scroll.AUTO);
		
		widgets = new ArrayList<ReportField>(report.getReportParameters().size());		
		for( ReportParameterWeb reportParameter : report.getReportParameters()) {
	
        	if( reportParameter.getParameterDatatype() != null ) {		        	
	        	if( reportParameter.getParameterDatatype() == TYPE_DATE) {	
	        		
	        		DateField field = new DateField();  
		                if( IsRequiredParameter(report,reportParameter )) {
			                field.setFieldLabel(reportParameter.getName() + " * ");	  
			                field.setAllowBlank(false);
		                } else {
			                field.setFieldLabel(reportParameter.getName());	                	
		                }
		                field.setToolTip("Type Date");
	                
	                ReportField reportField = new ReportField();
		                reportField.name = reportParameter.getName();
		                reportField.field = field;
		                reportField.datatype = reportParameter.getParameterDatatype();	                
	                widgets.add(reportField);	
	                cp.add(field);
	                
	        	} else if( reportParameter.getParameterDatatype() == TYPE_STRING) {
	        		
	                TextField<String> field = new TextField<String>();  
		                field.setFieldLabel(reportParameter.getName());
		                if( IsRequiredParameter(report,reportParameter )) {
			                field.setFieldLabel(reportParameter.getName() + " * ");	 
			                field.setAllowBlank(false);
		                } else {
			                field.setFieldLabel(reportParameter.getName());	                	
		                }
		                field.setToolTip("Type String");
	                
	                ReportField reportField = new ReportField();
		                reportField.name = reportParameter.getName();
		                reportField.field = field;
		                reportField.datatype = reportParameter.getParameterDatatype();	                
	                widgets.add(reportField);	
	                cp.add(field);
	        	} else if( reportParameter.getParameterDatatype() == TYPE_NUMERIC) {
	        		
	        		NumberField field = new NumberField();  
		                field.setFieldLabel(reportParameter.getName());	  
		                if( IsRequiredParameter(report,reportParameter )) {
			                field.setFieldLabel(reportParameter.getName() + " * ");	      
			                field.setAllowBlank(false);
		                } else {
			                field.setFieldLabel(reportParameter.getName());	                	
		                }
		                field.setToolTip("Type Numeric");
	                
	                ReportField reportField = new ReportField();
		                reportField.name = reportParameter.getName();
		                reportField.field = field;
		                reportField.datatype = reportParameter.getParameterDatatype();	                
	                widgets.add(reportField);		     
	                cp.add(field);
	        	}
        	}
        }
			
	    BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 142);  
	    westData.setSplit(true);  
	    westData.setMargins(new Margins(0,0,0,0));  
	  
	    BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);  
	    centerData.setMargins(new Margins(0));  
	    
		reportGenerateDialog.add(leftcp, westData);	    
		reportGenerateDialog.add(cp, centerData);
		
		reportGenerateDialog.show();
	}
	
	private boolean IsRequiredParameter(ReportWeb report,  ReportParameterWeb reportParameter) {
		if(report.getReportQueries() != null ) {
			for( ReportQueryWeb reportQuery : report.getReportQueries()) {
				if(reportQuery.getReportQueryParameters() != null ) {
					for( ReportQueryParameterWeb reportQueryParameter : reportQuery.getReportQueryParameters()) {
					     if( reportParameter.getName().equals( reportQueryParameter.getReportParameter().getName()) ) {
					    	 if( reportQueryParameter.getRequired().equals("Y") || reportQueryParameter.getRequired().equals("y"))
					    		 return true;
						 }
					}
				}
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

class ReportField {
	String name;	
	Widget field;	
	Integer datatype;
	boolean required;
}
