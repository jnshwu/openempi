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
package org.openempi.webapp.client.mvc.dataprofile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.DataProfileAttributeWeb;
import org.openempi.webapp.client.model.DataProfileAttributeValueWeb;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.mvc.View;
import org.openempi.webapp.client.ui.util.Utility;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.ToolTip;
import com.extjs.gxt.charts.client.model.ToolTip.MouseStyle;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.BarChart;
import com.extjs.gxt.charts.client.model.charts.BarChart.BarStyle;
import com.extjs.gxt.charts.client.model.charts.PieChart;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
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
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class DataProfileView extends View
{	
//	private LayoutContainer container;
	private ContentPanel container;	
	private LayoutContainer gridContainer;
	private ContentPanel formContainer;
	private ContentPanel barChartContainer1;  
	private ContentPanel barChartContainer2;  
	private ContentPanel pieChartContainer;  
	private LayoutContainer frequencyContainer;  	 	
	private ContentPanel frequencyValuePanel;
	
	private Button reloadDataProfileButton;
	private Grid<DataProfileAttributeWeb> grid;
	private Grid<DataProfileAttributeWeb> grid2;
	private ListStore<DataProfileAttributeWeb> profileAttributeStore = new ListStore<DataProfileAttributeWeb>();
	
	private Grid<DataProfileAttributeValueWeb> gridAttributeValue;
	private ListStore<DataProfileAttributeValueWeb> profileAttributeValueStore = new ListStore<DataProfileAttributeValueWeb>();
	
	private Chart barChartCount;
	private Chart barChartLength;
	private Chart pieChart;
	
	private DataProfileAttributeWeb currentAttribute;

//	private List<ModelPropertyWeb> fieldNames;
	
/*	private ChartListener listener = new ChartListener() {
	    
	    public void chartClick(ChartEvent ce) {
	      Info.display("Chart Clicked", "You selected ", "" + ce.getValue());
	    }
	};
*/
	
	@SuppressWarnings("unchecked")
	public DataProfileView(Controller controller) {
		super(controller);
		// fieldNames = (List<ModelPropertyWeb>) Registry.get(Constants.PERSON_MODEL_ALL_ATTRIBUTE_NAMES);		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.DataProfileView) {
			grid = null;
			grid2 = null;
			gridAttributeValue = null;
			initUI();
			
			showWaitCursor();	
			
		} else if (event.getType() == AppEvents.DataProfileAttributeReceived) {	
			
	    	List<DataProfileAttributeWeb> attributeList = (List<DataProfileAttributeWeb>) event.getData();
/*			for (DataProfileAttributeWeb attributeWeb : attributeList) {
				 Info.display("Information", ""+attributeWeb.getAttributeName());
			}	
*/   
			displayRecords(attributeList);
			
			showDefaultCursor();				
			
		} else if (event.getType() == AppEvents.DataProfileAttributeValueReceived) {	
			
	    	List<DataProfileAttributeValueWeb> attributeValueList = (List<DataProfileAttributeValueWeb>) event.getData();
/*			for (DataProfileAttributeValueWeb attributeValueWeb : attributeValueList) {
				 Info.display("Information", ""+attributeValueWeb.getAttributeValue());
			}	 
*/
			displayValues(attributeValueList);
	
		} else if (event.getType() == AppEvents.Error) {
			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  	
	        
			showDefaultCursor();
		} 
		
		container.layout();
	}

	private void displayRecords(List<DataProfileAttributeWeb> profileAttributes) {
		if( grid == null ) {
			setupProfileAttributesGrid();
		}

		if( gridAttributeValue == null ) {
			setupProfileAttributeValueGrid();
		}

		profileAttributeStore.removeAll();
		profileAttributeStore.add(profileAttributes);		
		container.layout();
	}
	
	private void displayValues(List<DataProfileAttributeValueWeb> AttributeValues) {
		
		if( gridAttributeValue == null ) {
			setupProfileAttributeValueGrid();
		}
	
		frequencyValuePanel.setHeading(currentAttribute.getAttributeName());
		
		profileAttributeValueStore.removeAll();
		profileAttributeValueStore.add(AttributeValues);	
		
		displayPieChart(AttributeValues);
		
		container.layout();
	}
	
	private void setupProfileAttributesGrid() {
		
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Profile Attributes");
		cp.setHeaderVisible(false);
		cp.setBodyBorder(false);
		cp.setLayout(new FillLayout());
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
	    
	    // Columns
		ColumnConfig column;

		// Name
		column = new ColumnConfig();
		column.setId("attributeName");  
		column.setHeader("Attribute Name");
		column.setWidth(150);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("rowCount");  
		column.setHeader("Row Count");  
		column.setWidth(90);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("distinctCount");  
		column.setHeader("Distinct Count");
		column.setWidth(90);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("duplicateCount");  
		column.setHeader("Duplicate Count");
		column.setWidth(90);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("uniqueCount");  
		column.setHeader("Unique Count");  
		column.setWidth(90);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("nullCount");  
		column.setHeader("Null Count");
		column.setWidth(90);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("averageLength");  
		column.setHeader("Average Length");
		column.setWidth(90);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("minimumLength");  
		column.setHeader("Minimum Length");
		column.setWidth(90);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("maximumLength");  
		column.setHeader("Maximum Length");
		column.setWidth(90);
		configs.add(column);
/*		
		column = new ColumnConfig();
		column.setId("averageValue");  
		column.setHeader("Average Value");
		column.setWidth(90);
		configs.add(column);
		 
 		column = new ColumnConfig();
		column.setId("minimumValue");  
		column.setHeader("Minimum Value");
		column.setWidth(90);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("maximumValue");  
		column.setHeader("Maximum Value");
		column.setWidth(90);
		configs.add(column);
*/
		ColumnConfig averageValueColumn = new ColumnConfig("averageValue", "Average Value", 100);
		averageValueColumn.setRenderer(new GridCellRenderer<DataProfileAttributeWeb>() {  
			public Object render(DataProfileAttributeWeb model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config, int rowIndex, int colIndex,
					ListStore<DataProfileAttributeWeb> store, Grid<DataProfileAttributeWeb> grid) {		
        				Double averageValue = model.getAverageValue();	
        				String attributeName = model.getAttributeName();
			        	if( attributeName.equals("dateOfBirth") || 
			        		attributeName.equals("dateVoided") || 
			        		attributeName.equals("dateCreated") || 
			        		attributeName.equals("dateChanged")) {
			        		if( averageValue == null ) {
			        			return "";
			        		}
			                Date d = new Date(averageValue.longValue()); 
			                if( attributeName.equals("dateOfBirth") )
			                	return Utility.DateToString(d);
			                else
			                	return Utility.DateTimeToString(d);
			        	}
			        	return averageValue;
			}
		});  
		configs.add(averageValueColumn);
		
		ColumnConfig minimumValueColumn = new ColumnConfig("minimumValue", "Minimum Value", 100);
		minimumValueColumn.setRenderer(new GridCellRenderer<DataProfileAttributeWeb>() {  
			public Object render(DataProfileAttributeWeb model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config, int rowIndex, int colIndex,
					ListStore<DataProfileAttributeWeb> store, Grid<DataProfileAttributeWeb> grid) {		
        				Double minimumValue = model.getMinimumValue();	
        				String attributeName = model.getAttributeName();
			        	if( attributeName.equals("dateOfBirth") || 
			        		attributeName.equals("dateVoided") || 
			        		attributeName.equals("dateCreated") || 
			        		attributeName.equals("dateChanged")) {	
			        		if( minimumValue == null ) {
			        			return "";
			        		}
			                Date d = new Date(minimumValue.longValue()); 
			                if( attributeName.equals("dateOfBirth") )
			                	return Utility.DateToString(d);
			                else
			                	return Utility.DateTimeToString(d);
			        	}
			        	return minimumValue;
			}
		});  
		configs.add(minimumValueColumn);
		
		ColumnConfig maximumValueColumn = new ColumnConfig("maximumValue", "Maximum Value", 100);
		maximumValueColumn.setRenderer(new GridCellRenderer<DataProfileAttributeWeb>() {  
			public Object render(DataProfileAttributeWeb model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config, int rowIndex, int colIndex,
					ListStore<DataProfileAttributeWeb> store, Grid<DataProfileAttributeWeb> grid) {		
        				Double maximumValue = model.getMaximumValue();	
        				String attributeName = model.getAttributeName();
			        	if( attributeName.equals("dateOfBirth") || 
			        		attributeName.equals("dateVoided") || 
			        		attributeName.equals("dateCreated") || 
			        		attributeName.equals("dateChanged")) {	
			        		if( maximumValue == null ) {
			        			return "";
			        		}
			                Date d = new Date(maximumValue.longValue()); 
			                if( attributeName.equals("dateOfBirth") )
			                	return Utility.DateToString(d);
			                else
			                	return Utility.DateTimeToString(d);
			        	}
			        	return maximumValue;
			}
		});  
		configs.add(maximumValueColumn);


		
		ColumnModel cm = new ColumnModel(configs);	    
		grid = new Grid<DataProfileAttributeWeb>(profileAttributeStore, cm);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);
		grid.setStripeRows(true);  		
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		
		// selection event
		grid.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<DataProfileAttributeWeb>>() {
					public void handleEvent(SelectionChangedEvent<DataProfileAttributeWeb> be) {	
						currentAttribute = be.getSelectedItem();
						if (currentAttribute != null) {	
							
							displayBarChart(currentAttribute);
							
							controller.handleEvent(new AppEvent(AppEvents.DataProfileAttributeValueRequest, currentAttribute));
							
							grid2.getSelectionModel().select(currentAttribute, false);
							
						} else {
							displayNone();		
							grid2.getSelectionModel().deselectAll();
						}
					}
				});

		// For Attribute Data, Part 2
		List<ColumnConfig> configs2 = new ArrayList<ColumnConfig>();
	    
	    // Columns
		ColumnConfig column2;

		// Name
		column2 = new ColumnConfig();
		column2.setId("attributeName");  
		column2.setHeader("Attribute Name");
		column2.setWidth(150);
		configs2.add(column2);

		column2 = new ColumnConfig();
		column2.setId("variance");  
		column2.setHeader("Variance");
		column2.setWidth(90);
		configs2.add(column2);

		column2 = new ColumnConfig();
		column2.setId("standardDeviation");  
		column2.setHeader("Standard Deviation");
		column2.setWidth(120);
		configs2.add(column2);
		
		column2 = new ColumnConfig();
		column2.setId("nullRate");  
		column2.setHeader("Null Rate");
		column2.setWidth(90);
		configs2.add(column2);
		
		column2 = new ColumnConfig();
		column2.setId("entropy");  
		column2.setHeader("Entropy");  
		column2.setWidth(90);
		configs2.add(column2);

		column2 = new ColumnConfig();
		column2.setId("maximumEntropy");  
		column2.setHeader("Maximum Entropy");  
		column2.setWidth(120);
		configs2.add(column2);
		
		column2 = new ColumnConfig();
		column2.setId("uValue");  
		column2.setHeader("U Value");  
		column2.setWidth(90);
		configs2.add(column2);
		
		column2 = new ColumnConfig();
		column2.setId("averageTokenFrequency");  
		column2.setHeader("Average Token Frequency");  
		column2.setWidth(150);
		configs2.add(column2);

		column2 = new ColumnConfig();
		column2.setId("blockingPairs");  
		column2.setHeader("Blocking Pairs");  
		column2.setWidth(90);
		configs2.add(column2);
		
		ColumnModel cm2 = new ColumnModel(configs2);	    		
		grid2 = new Grid<DataProfileAttributeWeb>(profileAttributeStore, cm2);
		grid2.setStyleAttribute("borderTop", "none");
		grid2.setBorders(true);
		grid2.setStripeRows(true);  
		grid2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	

		// selection event
		grid2.getSelectionModel().addListener(Events.SelectionChange,
				new Listener<SelectionChangedEvent<DataProfileAttributeWeb>>() {
					public void handleEvent(SelectionChangedEvent<DataProfileAttributeWeb> be) {	
						currentAttribute = be.getSelectedItem();
						if (currentAttribute != null) {	
							
							displayBarChart(currentAttribute);
							
							controller.handleEvent(new AppEvent(AppEvents.DataProfileAttributeValueRequest, currentAttribute));
							
							grid.getSelectionModel().select(currentAttribute, false);
							
						} else {
							displayNone();	
							
							grid.getSelectionModel().deselectAll();
						}
					}
				});
		
		TabPanel tabPanel = new TabPanel();		
		tabPanel.setBorders(false);
		tabPanel.setBodyBorder(false);
		
		TabItem attributeDataPart1Tab = new TabItem("Attribute Data, Part1");		
		attributeDataPart1Tab.setLayout(new FitLayout());
		attributeDataPart1Tab.add(grid);	
		attributeDataPart1Tab.addListener(Events.Select, new Listener<ComponentEvent>() {
		      public void handleEvent(ComponentEvent be) {
		    	   if(currentAttribute != null)
					  grid.getView().getRow(currentAttribute).scrollIntoView();
		      }
		    });

		TabItem attributeDataPart2Tab = new TabItem("Attribute Data, Part 2");		
		attributeDataPart2Tab.setLayout(new FitLayout());
		attributeDataPart2Tab.add(grid2);
		attributeDataPart2Tab.addListener(Events.Select, new Listener<ComponentEvent>() {
		      public void handleEvent(ComponentEvent be) {
		    	   if(currentAttribute != null)
					  grid2.getView().getRow(currentAttribute).scrollIntoView();
		      }
		    });
	
		
		tabPanel.add(attributeDataPart1Tab);
		tabPanel.add(attributeDataPart2Tab);

		LayoutContainer buttonContainer = new LayoutContainer();
		HBoxLayout layout = new HBoxLayout();
		layout.setPadding(new Padding(5));
		layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layout.setPack(BoxLayoutPack.END);
		buttonContainer.setLayout(layout);

		HBoxLayoutData layoutData = new HBoxLayoutData(new Margins(0, 3, 0, 2));
		
		reloadDataProfileButton = new Button(" Refresh Data Profile", IconHelper.create("images/arrow_refresh.png"), new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {		        	  
		        	  showWaitCursor();
					  controller.handleEvent(new AppEvent(AppEvents.DataProfileView, null));
	          }
	    });
		
		buttonContainer.add(reloadDataProfileButton, layoutData);

		cp.setTopComponent(buttonContainer);
		cp.add(tabPanel);
		gridContainer.add(cp);
	}
	
	private void setupProfileAttributeValueGrid() {
		
		frequencyValuePanel = new ContentPanel();
		frequencyValuePanel.setHeading("");
		frequencyValuePanel.setHeaderVisible(true);
		frequencyValuePanel.setBodyBorder(false);
		frequencyValuePanel.setLayout(new FillLayout());
		
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
	    
	    // Columns
		ColumnConfig column;

		// Name
		column = new ColumnConfig();
		column.setId("attributeValue");  
		column.setHeader("Attribute Value");
		column.setWidth(200);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("frequency");  
		column.setHeader("Frequency");  
		column.setWidth(150);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);	    
		gridAttributeValue = new Grid<DataProfileAttributeValueWeb>(profileAttributeValueStore, cm);
		gridAttributeValue.setStyleAttribute("borderTop", "none");
		gridAttributeValue.setBorders(true);
		gridAttributeValue.setStripeRows(true);  
		
		frequencyValuePanel.add(gridAttributeValue);
		frequencyContainer.add(frequencyValuePanel);
	}
	
	private ChartModel getPieChartModel( List<DataProfileAttributeValueWeb> attributeValueList ) {
	    ChartModel cm = new ChartModel( currentAttribute.getAttributeName(), "font-size: 12px; font-family: Verdana; text-align: center;");
	    cm.setBackgroundColour("#fffff5");
	    Legend lg = new Legend(Position.RIGHT, true);
	    lg.setPadding(10);
	    cm.setLegend(lg);
	    
	    PieChart pie = new PieChart();
	    pie.setAlpha(0.5f);
	    pie.setNoLabels(true);
	    pie.setTooltip("#label# #val# <br>#percent#");
	    pie.setColours("#ff0000", "#00ffff", "#0000ff", "#add8e6", "#800080", "#808080", "#00ff00", "#ff00ff", "#800000", "#ffa500", "#c0c0c0", "#F080F0"); 
	    			 /* red,      cyan,       blue,      lightblue, purple     grey     lime       fuchsia    maroon     orange      silver    violet*/
	    
	    int frequencies = 0;
	    for (DataProfileAttributeValueWeb attributeValueWeb : attributeValueList) {
	    	// Info.display("Information", ""+attributeValueWeb.getAttributeValue());
		    pie.addSlices(new PieChart.Slice(attributeValueWeb.getFrequency(), attributeValueWeb.getAttributeValue(), attributeValueWeb.getAttributeValue()));	 
		    frequencies +=  attributeValueWeb.getFrequency();		 
	    }
	    if( frequencies != 0 ) {
	    	if(currentAttribute.getNullCount() != null  && currentAttribute.getNullCount() > 0) {
		    	pie.addSlices(new PieChart.Slice( currentAttribute.getNullCount(), "Null", "Null"));	
		    	if(currentAttribute.getRowCount()-frequencies-currentAttribute.getNullCount() > 0)
		    	   pie.addSlices(new PieChart.Slice( currentAttribute.getRowCount()-frequencies-currentAttribute.getNullCount(), "Other", "Other"));	
	    	} else {
		    	pie.addSlices(new PieChart.Slice( currentAttribute.getRowCount()-frequencies, "Other", "Other"));	
	    	}
	    		
	    } else {
	    	pie.addSlices(new PieChart.Slice( currentAttribute.getNullCount(), "Null", "Null"));		    	
	    }
	    
//	    pie.addChartListener(listener);

	    cm.addChartConfig(pie);
	    return cm;
	}
	
    private ChartModel getBarChartCountModel(DataProfileAttributeWeb attribute) 
    { 
      //Create a ChartModel with the Chart Title and some style attributes
/*      String attributeFullName = "";
      for (ModelPropertyWeb nameField : fieldNames) {
    	  if(nameField.getName().equals(attribute.getAttributeName()) ) {
    		  attributeFullName = nameField.getDescription();
    	  }  		  
      }
      ChartModel cm = new ChartModel(attributeFullName, "font-size: 12px; font-family:      Verdana; text-align: center;");
*/
      ChartModel cm = new ChartModel(attribute.getAttributeName(), "font-size: 12px; font-family:      Verdana; text-align: center;");     
     
      XAxis xa = new XAxis();
      //Add the labels to the X axis  
      xa.addLabels("Row Count", "Null Count", "Distinct Count", "Unique Count","Duplicate Count");  
      xa.setOffset(true);  
      xa.setZDepth3D(15);
      cm.setXAxis(xa);
      
      YAxis ya = new YAxis();
      int step = 10;
      if( attribute.getRowCount() > 200 && attribute.getRowCount() < 500) {
    	  step = 50;
      } else if( attribute.getRowCount() > 500 && attribute.getRowCount() < 2000) {
    	  step = 100;
      } else if( attribute.getRowCount() > 2000 && attribute.getRowCount() < 5000) {
    	  step = 500;
      } else if( attribute.getRowCount() > 5000 && attribute.getRowCount() < 20000) {
    	  step = 1000;
      } else if( attribute.getRowCount() > 20000 && attribute.getRowCount() < 50000) {
    	  step = 5000;
      } else if( attribute.getRowCount() > 50000 && attribute.getRowCount() < 200000) {
    	  step = 10000;   
      } else if( attribute.getRowCount() > 200000 ) {
    	  step = 50000;       	  
      }
      
      //set the maximum, minimum and the step value for the Y axis      
      ya.setRange(0, attribute.getRowCount()+step/2, step);  
      cm.setYAxis(ya);

      //create a Bar Chart object and add bars to the object  
      BarChart bchart = new BarChart(BarStyle.THREED);  
      bchart.setTooltip("#val# count");  
 
      //different color for different bars 
      bchart.addBars(new BarChart.Bar( (attribute.getRowCount() != null)? attribute.getRowCount() : 0, "#ffff00")); 
      bchart.addBars(new BarChart.Bar( (attribute.getNullCount() != null)? attribute.getNullCount() : 0,  "#0000ff"));  
      bchart.addBars(new BarChart.Bar( (attribute.getDistinctCount() != null)? attribute.getDistinctCount() : 0, "#00ff00")); 
      bchart.addBars(new BarChart.Bar( (attribute.getUniqueCount() != null)? attribute.getUniqueCount() : 0, "#ff0000"));
      bchart.addBars(new BarChart.Bar( (attribute.getDuplicateCount() != null)? attribute.getDuplicateCount() : 0,  "#333ccc"));

      //add the bchart as the Chart Config of the ChartModel
      cm.addChartConfig(bchart);             
      cm.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));  
      return cm;  
    }

    private ChartModel getBarChartLengthModel(DataProfileAttributeWeb attribute) 
    { 
      ChartModel cm = new ChartModel(attribute.getAttributeName(), "font-size: 12px; font-family:      Verdana; text-align: center;");     
      
      XAxis xa = new XAxis();
      //Add the labels to the X axis  
      xa.addLabels("Average Length", "Minimum Length", "Maximum Length");  
      xa.setOffset(true);  
      xa.setZDepth3D(15);
      cm.setXAxis(xa);
      
      YAxis ya = new YAxis();
      int step = 10;
      
      //set the maximum, minimum and the step value for the Y axis      
      ya.setRange(0, ((attribute.getAverageLength() != null)? attribute.getAverageLength() : 0) +step/2, step);  
      cm.setYAxis(ya);

      //create a Bar Chart object and add bars to the object  
      BarChart bchart = new BarChart(BarStyle.THREED);  
      bchart.setTooltip("#val# length");  
 
      //different color for different bars 
      bchart.addBars(new BarChart.Bar( (attribute.getAverageLength() != null)? attribute.getAverageLength() : 0, "#ffff00")); 
      bchart.addBars(new BarChart.Bar( (attribute.getMinimumLength() != null)? attribute.getMinimumLength() : 0,  "#0000ff"));  
      bchart.addBars(new BarChart.Bar( (attribute.getMaximumLength() != null)? attribute.getMaximumLength() : 0, "#00ff00")); 


      //add the bchart as the Chart Config of the ChartModel
      cm.addChartConfig(bchart);             
      cm.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));  
      return cm;  
    }
    
	private void displayBarChart(DataProfileAttributeWeb attribute) {
		if( barChartCount == null  || barChartLength == null) {
			String url = "gxt/chart/open-flash-chart.swf";  
			barChartCount = new Chart(url);	        
			barChartCount.setBorders(true);  
			
			barChartLength = new Chart(url);	        
			barChartLength.setBorders(true);  
		}
		barChartCount.setChartModel(getBarChartCountModel(attribute));          
	    barChartContainer1.add(barChartCount);
		
	    barChartLength.setChartModel(getBarChartLengthModel(attribute));          
	    barChartContainer2.add(barChartLength); 
        container.layout();
	}

	private void displayPieChart(List<DataProfileAttributeValueWeb> attributeValueList ) {
		if( pieChart == null ) {
			String url = "gxt/chart/open-flash-chart.swf";  
			pieChart = new Chart(url);	        
			pieChart.setBorders(true);  
		}
		
		pieChart.setChartModel( getPieChartModel(attributeValueList) );          
        pieChartContainer.add(pieChart);      
        container.layout();
	}
	
	private void displayNone() {        
		barChartContainer1.removeAll();  
		barChartContainer2.removeAll();  
		pieChartContainer.removeAll();   
		
		frequencyValuePanel.setHeading("");	
		profileAttributeValueStore.removeAll();
		
        container.layout();
	}
	
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);

		controller.handleEvent(new AppEvent(AppEvents.DataProfileAttributeRequest));
		
//		container = new LayoutContainer();
		container = new ContentPanel ();
		container.setLayout(new BorderLayout());
		container.setHeading("Data Profiles");  
		
			gridContainer = new LayoutContainer();
			gridContainer.setBorders(false);
			gridContainer.setLayout(new FitLayout());		
		
			formContainer = new ContentPanel();
			formContainer.setBorders(false);
			formContainer.setHeaderVisible(false);
			formContainer.setLayout(new BorderLayout());   

				barChartContainer1 = new ContentPanel();
				barChartContainer1.setBorders(false);
				barChartContainer1.setHeaderVisible(false);
				barChartContainer1.setLayout(new FitLayout());   
				
				barChartContainer2 = new ContentPanel();
				barChartContainer2.setBorders(false);
				barChartContainer2.setHeaderVisible(false);
				barChartContainer2.setLayout(new FitLayout());   

				pieChartContainer = new ContentPanel();
				pieChartContainer.setBorders(false);
				pieChartContainer.setHeaderVisible(false);
				pieChartContainer.setLayout(new FitLayout());   
				
				frequencyContainer = new LayoutContainer();
				frequencyContainer.setBorders(true);
				frequencyContainer.setLayout(new FitLayout());		

				TabPanel tabPanel = new TabPanel();		
				tabPanel.setBorders(false);
				tabPanel.setBodyBorder(false);
				
				TabItem barChart1Tab = new TabItem("Bar Chart Count");		
				barChart1Tab.setLayout(new FitLayout());
				barChart1Tab.add(barChartContainer1);	

				TabItem barChart2Tab = new TabItem("Bar Chart Length");		
				barChart2Tab.setLayout(new FitLayout());
				barChart2Tab.add(barChartContainer2);
			
				
				tabPanel.add(barChart1Tab);
				tabPanel.add(barChart2Tab);
				
			formContainer.add(tabPanel, new BorderLayoutData(LayoutRegion.WEST, 550));
			formContainer.add(frequencyContainer, new BorderLayoutData(LayoutRegion.CENTER));
			formContainer.add(pieChartContainer, new BorderLayoutData(LayoutRegion.EAST, 500));
		
		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		container.add(gridContainer, new BorderLayoutData(LayoutRegion.NORTH, 300));
		data.setMargins(new Margins(4, 2, 4, 2));
		container.add(formContainer, data);	

        
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
	
	public static void showWaitCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
	}
	 
	public static void showDefaultCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}
}
