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
package org.openempi.webapp.client.mvc.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.mvc.View;
import org.openempi.webapp.client.domain.AuthenticationException;

import org.openempi.webapp.client.model.MessageLogEntryWeb;
import org.openempi.webapp.client.model.MessageLogSearchCriteriaWeb;
import org.openempi.webapp.client.model.MessageLogListWeb;
import org.openempi.webapp.client.model.MessageTypeWeb;
import org.openempi.webapp.client.ui.util.Utility;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DatePickerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.DateTimePropertyEditor;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MessageLogView extends View
{
	public static final Integer PAGE_SIZE = new Integer(10);
	
	private Grid<MessageLogEntryWeb> grid;
	private ListStore<MessageLogEntryWeb> store = new ListStore<MessageLogEntryWeb>();
	
	private TextField<String> selectedMessageTypes;
	private ListView<MessageTypeWeb> messageTypes;
	private ListStore<MessageTypeWeb> messageTypesStore = new ListStore<MessageTypeWeb>();
	private DateField startDate;
	private DateField endDate;
	
	private Status status;
	private Button searchButton;
	private Button cancelButton;

	public RpcProxy<PagingLoadResult<MessageLogEntryWeb>> proxy;
	public BasePagingLoader<PagingLoadResult<MessageLogEntryWeb>> pagingLoader;
	public PagingToolBar pagingToolBar;
	
	public MessageLogSearchCriteriaWeb searchCriteria;
	
	
	private Dialog messagedetailInfoDialog = null;
	private LayoutContainer formContainer;
	private TabPanel tabPanel;		
	
	private FormPanel leftFormPanel; 
	private FormPanel rightFormPanel;
	private TextArea  incomingMessageEdit;
	private TextArea  outgoingMessageEdit;
	
	private LayoutContainer container;
	
	public MessageLogView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.MessageLogView) {
			
			searchCriteria = null;
			initUI();
			
			if( Registry.get(Constants.MESSAGE_TYPE_CODES) != null ) {	
				List<MessageTypeWeb> messageTypes =  (List<MessageTypeWeb>)Registry.get(Constants.MESSAGE_TYPE_CODES);
				/* for (MessageTypeWeb type : messageTypes) {
					Info.display("Information", "Message Types: "+ type.getMessageTypeCd() + ", " + type.getMessageTypeName());			
				}*/
				messageTypesStore.removeAll();
				messageTypesStore.add(messageTypes);
			}
			
		}else if (event.getType() == AppEvents.Logout) {
			
  		    Dispatcher.get().dispatch(AppEvents.Logout);
			
		} else if (event.getType() == AppEvents.MessageLogReceived) {
			
			// Info.display("Information", "EventReceived");
			store.removeAll();
		
			
			grid.getSelectionModel().select(0, true);
			grid.getSelectionModel().deselect(0);
			
			status.hide();
			searchButton.unmask(); 
			
		} else if (event.getType() == AppEvents.MessageLogDetailReceived) {
			
			MessageLogEntryWeb message = (MessageLogEntryWeb) event.getData();
			
        	buildMessageetailInfoDialog(message);
        	messagedetailInfoDialog.show();
			
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  			
		}
	}

	@SuppressWarnings("unchecked")
	private FormPanel setupButtonPanel(int tabIndex) {
		FormPanel buttonPanel = new FormPanel(); 
		buttonPanel.setHeaderVisible(false);
		buttonPanel.setBodyBorder(false);
		buttonPanel.setStyleAttribute("paddingRight", "10px");
		buttonPanel.setButtonAlign(HorizontalAlignment.CENTER);		

		searchButton = new Button("Search Message", IconHelper.create("images/search_icon_16x16.png"), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
								
				searchCriteria = new MessageLogSearchCriteriaWeb();
				if( !startDate.validate() || !endDate.validate()) {
					Info.display("Warning", "You must enter valid format of date before pressing the search button.");
					return;					
				}
				searchCriteria.setStartDateTime( Utility.DateTimeToString(startDate.getValue()));
				searchCriteria.setEndDateTime( Utility.DateTimeToString(endDate.getValue()));

				Set<MessageTypeWeb> ids = new HashSet<MessageTypeWeb>();
				if (messageTypes.getSelectionModel().getSelection().size() > 0) {
					
					List<MessageTypeWeb> types =  messageTypes.getSelectionModel().getSelection();
					for( MessageTypeWeb type : types) {
						 // Info.display("Information", "Event Type: "+type.getMessageTypeName());						
					    ids.add(type);
					}	
				    searchCriteria.setMessageTypes(ids);
				} else {
					
					// if not selected any types that means select All
					List<MessageTypeWeb> messageTypes =  (List<MessageTypeWeb>)Registry.get(Constants.MESSAGE_TYPE_CODES);
					for( MessageTypeWeb type : messageTypes) {
						 // Info.display("Information", "Event Type: "+type.getMessageTypeName());						
					    ids.add(type);
					}	
				    searchCriteria.setMessageTypes(ids);			
				}
				
				searchCriteria.setFirstResult(new Integer(0));
				searchCriteria.setMaxResults(PAGE_SIZE);
				
				//// controller.handleEvent(new AppEvent(AppEvents.AuditEventRequest, searchCriteria));
				
			    PagingLoadConfig config = new BasePagingLoadConfig();
			    config.setOffset(0);
			    config.setLimit(PAGE_SIZE);
			        
			    pagingLoader.load(config);  
			    
				status.show();
				searchButton.mask();				
			}
		});
		
		cancelButton = new Button("Reset", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				selectedMessageTypes.setValue("");
				messageTypes.getSelectionModel().deselectAll();
				startDate.clear();
				endDate.clear();
				searchButton.disable();
			}
		});
			
		searchButton.setTabIndex(tabIndex++);		
		cancelButton.setTabIndex(tabIndex++);			
		searchButton.disable();
		
		status = new Status();
		status.setBusy("please wait...");
		buttonPanel.getButtonBar().add(status);
		status.hide();
		
		buttonPanel.getButtonBar().setSpacing(15);
		buttonPanel.addButton(searchButton);
		buttonPanel.addButton(cancelButton);
		
		return buttonPanel;
	}

	private PagingToolBar setupRpcProxy() {
		// Rpc Proxy
	    proxy = new RpcProxy<PagingLoadResult<MessageLogEntryWeb>>() {	
	    	
            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<MessageLogEntryWeb>> callback) {
           	
            	if( searchCriteria == null ){
  		            callback.onSuccess(new BasePagingLoadResult<MessageLogEntryWeb>( null, 0, 0 ));
            		return;
            	}
            		
                // set page offset for searchCriteria
            	searchCriteria.setFirstResult( ((PagingLoadConfig)loadConfig).getOffset() );
            	
	        	getController().getAuditEventDataService().getMessageLogsBySearch(searchCriteria, new AsyncCallback<MessageLogListWeb>() {
	    		      public void onFailure(Throwable caught) {
	    		    	  
	    		    	  if (caught instanceof AuthenticationException) {
	    		    		  Dispatcher.get().dispatch(AppEvents.Logout);
	    		    		  return;
	    		    	  }		    
	    		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
	    		      }


	    		      public void onSuccess(MessageLogListWeb result) {
	    		    	  // Info.display("Information", "The messages: " +((PagingLoadConfig)loadConfig).getOffset() +"; "+result.getTotalCount());	  
	    		    	  
	    				  // PagingLoadConfig configuration
	    		          callback.onSuccess(new BasePagingLoadResult<MessageLogEntryWeb>( result.getMessageLogs(), ((PagingLoadConfig)loadConfig).getOffset(), result.getTotalCount() ));
	    		      }
	    		    });	 
	        }
	    };
	    
	    // Page loader
	    pagingLoader = new BasePagingLoader<PagingLoadResult<MessageLogEntryWeb>>(proxy);	    
	    pagingLoader.setRemoteSort(true);
	    pagingLoader.addLoadListener(new LoadListener() {
			// After the loader be completely filled, remove the mask
			public void loaderLoad(LoadEvent le) {
				status.hide();
				searchButton.unmask();
				
				grid.unmask(); 
			}
		});		

	    store = new ListStore<MessageLogEntryWeb>(pagingLoader); 	 
	    
	    PagingToolBar pagingToolBar = new PagingToolBar(PAGE_SIZE);  
	    pagingToolBar.bind(pagingLoader);		    
	    return pagingToolBar;
	}

	private Grid<MessageLogEntryWeb> setupGrid() {
			    
		// Audit Event Grid
		ColumnConfig incomingTypeColumn = new ColumnConfig("incomingMessageType.messageTypeName", "Incoming Message Type", 150);
		ColumnConfig OutghoingypeColumn = new ColumnConfig("outgoingMessageType.messageTypeName", "Outgoing Message Type", 150);		
		ColumnConfig incomingMessageColumn = new ColumnConfig("incomingMessage", "Incoming Message", 250);
		ColumnConfig outgoingMessageColumn = new ColumnConfig("outgoingMessage", "Outgoing Message", 250);
		ColumnConfig dateTimeColumn = new ColumnConfig("dateReceived", "Date Received", 120);
					 dateTimeColumn.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());

		
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(incomingTypeColumn);
		config.add(OutghoingypeColumn);
		config.add(incomingMessageColumn);
		config.add(outgoingMessageColumn);
		config.add(dateTimeColumn);
		
		final ColumnModel cm = new ColumnModel(config);
		
		Grid<MessageLogEntryWeb>  grid = new Grid<MessageLogEntryWeb>(store, cm);
		grid.setBorders(true);
		grid.setAutoWidth(true);
		grid.setStripeRows(true); 
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		grid.setHeight(330);
		
		grid.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<MessageLogEntryWeb>>() {
			
			public void handleEvent(SelectionChangedEvent<MessageLogEntryWeb> be) {
				List<MessageLogEntryWeb> selection = be.getSelection();
			}
		});
		
		grid.addListener(Events.SortChange, new Listener<GridEvent<MessageLogEntryWeb>>() {	
			public void handleEvent(GridEvent<MessageLogEntryWeb> be) {
				MessageLogEntryWeb selectedField = be.getGrid().getSelectionModel().getSelectedItem();				
			}
		});	
		
		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<MessageLogEntryWeb>>() {	
			public void handleEvent(GridEvent<MessageLogEntryWeb> be) {
				MessageLogEntryWeb selectedMessage = be.getGrid().getSelectionModel().getSelectedItem();
				
				// Info.display("Information", "Double click: "+selectedMessage.getMessageLogId());				
				controller.handleEvent(new AppEvent(AppEvents.MessageLogDetailRequest, selectedMessage));	
			}
		});
		
		return grid;
	}
	
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
			
		container = new LayoutContainer();
		container.setLayout(new CenterLayout());
		
		// Rpc Proxy setup
		pagingToolBar = setupRpcProxy();
	    
		// Audit event grid setup
		grid = setupGrid();
	    
		// Panel
		ContentPanel cp = new ContentPanel();
		cp.setHeading("Message Log Viewer");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/search_icon_16x16.png"));
		cp.setLayout(new FormLayout());
		cp.setSize(1100, 630);

		// Search Container
		ContentPanel searchContainer = new ContentPanel();
		searchContainer.setHeaderVisible(false);
		FormLayout searchFormLayout = new FormLayout();
		searchFormLayout.setLabelWidth(140);
		searchFormLayout.setDefaultWidth(770);

		searchContainer.setLayout(searchFormLayout);

		selectedMessageTypes = new TextField<String>();
		selectedMessageTypes.setFieldLabel("Selected Message Types");
		selectedMessageTypes.setReadOnly(true);
		
		messageTypes = new ListView<MessageTypeWeb>();
		messageTypes.setDisplayProperty("messageTypeName"); 
		messageTypes.setWidth(220);
		messageTypes.setHeight(110);
		messageTypes.setStore(messageTypesStore);
		messageTypes.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<MessageTypeWeb>>() {
			
			public void handleEvent(SelectionChangedEvent<MessageTypeWeb> be) {
				List<MessageTypeWeb> selections = be.getSelection();
				String selectedTypes = "";
				for( MessageTypeWeb type : selections) {
					if(selectedTypes.isEmpty() )
						selectedTypes = type.getMessageTypeName();
					else
						selectedTypes = selectedTypes + ", "+type.getMessageTypeName();
				}	
				selectedMessageTypes.setValue(selectedTypes);	

				if( selectedMessageTypes.getValue() == null && startDate.getValue() == null && endDate.getValue() == null) {
					searchButton.disable();
				} else {
					searchButton.enable();
				}
			}
		});
		
		DateTimePropertyEditor dateFormat = new DateTimePropertyEditor("yyyy-MM-dd HH:mm");
		startDate = new DateField();
		startDate.setFieldLabel("Start Date Time");
		startDate.setToolTip("yyyy-MM-dd HH:mm");
		startDate.setPropertyEditor(dateFormat);
        startDate.getDatePicker().addListener(Events.Select, new Listener<DatePickerEvent>() {

            public void handleEvent(DatePickerEvent be) {            	
            	// Info.display("Warning", "Picker startDate: "+ startDate.getValue());
				searchButton.enable();
            }
            
        });

        startDate.addListener(Events.KeyUp, new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent p_event) {
				if( selectedMessageTypes.getValue() == null && startDate.getValue() == null && endDate.getValue() == null) {
					searchButton.disable();
				} else {
					searchButton.enable();
				}
            }
          });
        
		endDate = new DateField();
		endDate.setFieldLabel("End Date Time");
		endDate.setToolTip("yyyy-MM-dd HH:mm");
		endDate.setPropertyEditor(dateFormat);
		endDate.getDatePicker().addListener(Events.Select, new Listener<DatePickerEvent>() {

	            public void handleEvent(DatePickerEvent be) {
	               	// Info.display("Warning", "endDate: "+ endDate.getValue());
					searchButton.enable();
	            }	            
	    });

		endDate.addListener(Events.KeyUp, new Listener<FieldEvent>() {
	            public void handleEvent(FieldEvent p_event) {
					if( selectedMessageTypes.getValue() == null && startDate.getValue() == null && endDate.getValue() == null) {
						searchButton.disable();
					} else {
						searchButton.enable();
					}
	            }
	          });
	       
		LayoutContainer main = new LayoutContainer();  
		main.setLayout(new ColumnLayout());

			LayoutContainer left = new LayoutContainer();
			left.setStyleAttribute("paddingRight", "10px");
			FormLayout layout = new FormLayout();
			layout.setLabelWidth(140);
			layout.setDefaultWidth(220);
			// layout.setLabelAlign(LabelAlign.TOP);  
			left.setLayout(layout);
			left.add(startDate);
			
			LayoutContainer right = new LayoutContainer();
			right.setStyleAttribute("paddingLeft", "10px");
			layout = new FormLayout();
			// layout.setLabelAlign(LabelAlign.TOP);
			layout.setLabelWidth(140);
			layout.setDefaultWidth(220);
			right.setLayout(layout);
			right.add(endDate);
		
		main.add(left, new ColumnData(.5));  
		main.add(right, new ColumnData(.5));
		
		HBoxLayoutData dataSelectedTypes = new HBoxLayoutData(new Margins(5, 0, 0, 0));	
		searchContainer.add(selectedMessageTypes, dataSelectedTypes);
		HBoxLayoutData dataTypes = new HBoxLayoutData(new Margins(5, 0, 5, 145));		
		searchContainer.add(messageTypes, dataTypes);
		
		searchContainer.add(main);	
		HBoxLayoutData dataButtons = new HBoxLayoutData(new Margins(0, 0, 5, 0));	
		searchContainer.add(setupButtonPanel(3),dataButtons);
		
		cp.add(searchContainer);
		cp.add(grid);
		cp.setBottomComponent(pagingToolBar); 	
		
		container.add(cp);
		
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}	
	
	//  messagedetailInfoDialog
	private void buildMessageetailInfoDialog(MessageLogEntryWeb message) {		

		
		messagedetailInfoDialog = new Dialog();
		messagedetailInfoDialog.setBodyBorder(false);
		messagedetailInfoDialog.setWidth(1020);
		messagedetailInfoDialog.setHeight(560);
		messagedetailInfoDialog.setIcon(IconHelper.create("images/information.png"));
		messagedetailInfoDialog.setHeading("Message Detail Information");
		messagedetailInfoDialog.setButtons(Dialog.OK);
		messagedetailInfoDialog.setModal(true);
		messagedetailInfoDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {		        	  	 
	        	  // messagedetailInfoDialog.hide();	
	        	  messagedetailInfoDialog.close();
				}
	    });
		
		ContentPanel cp = new ContentPanel();
		cp.setFrame(false);
		cp.setHeaderVisible(false);
		cp.setLayout(new BorderLayout());
		cp.setSize(1010, 500);
		    
/*			TableLayout formlayout = new TableLayout(2);
			formlayout.setWidth("100%");
			formlayout.setCellVerticalAlign(VerticalAlignment.TOP);
		    
			
			formContainer = new LayoutContainer();
			formContainer.setLayout(formlayout);
		
				leftFormPanel = setupIncomingMessageForm("");
				rightFormPanel = setupOutgoingMessageForm("");
				
			formContainer.add(leftFormPanel);	
			formContainer.add(rightFormPanel);		
*/		
		tabPanel = new TabPanel();		
		tabPanel.setBorders(false);
		tabPanel.setBodyBorder(false);

		leftFormPanel = setupIncomingMessageForm("");
		rightFormPanel = setupOutgoingMessageForm("");
		
		TabItem attributeDataPart1Tab = new TabItem(" Incoming Message ");		
		attributeDataPart1Tab.setLayout(new FitLayout());
		attributeDataPart1Tab.add(leftFormPanel);	


		TabItem attributeDataPart2Tab = new TabItem(" Outgoing Message ");		
		attributeDataPart2Tab.setLayout(new FitLayout());
		attributeDataPart2Tab.add(rightFormPanel);
		
		tabPanel.add(attributeDataPart1Tab);
		tabPanel.add(attributeDataPart2Tab);
		
		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(4, 2, 4, 2));

//		cp.add(formContainer, data);
		cp.add(tabPanel, data);
	
		messagedetailInfoDialog.add(cp);
		
		// display
		incomingMessageEdit.setValue(message.getIncomingMessage());			
		outgoingMessageEdit.setValue(message.getOutgoingMessage());	
	}
    
	private FormPanel setupIncomingMessageForm(String title) {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setWidth(1000);
	    formPanel.setTitle(title); 
	    
		FormLayout layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP); 
		layout.setDefaultWidth(980);
		formPanel.setLayout(layout);
		
	    incomingMessageEdit = new TextArea ();
	    incomingMessageEdit.setHeight(420);	
	    incomingMessageEdit.setFieldLabel("Incoming Message");

	    incomingMessageEdit.setInputStyleAttribute("word-wrap", "normal");
	    incomingMessageEdit.setInputStyleAttribute("overflow-x", "scroll");  // for IE
	    
	    incomingMessageEdit.setReadOnly(true);
	    
		formPanel.add(incomingMessageEdit); 
		
	    return formPanel;
	}
	
	private FormPanel setupOutgoingMessageForm(String title) {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setWidth(1000);
	    formPanel.setTitle(title); 

		FormLayout layout = new FormLayout();
		layout.setLabelAlign(LabelAlign.TOP);  
		layout.setDefaultWidth(980);
		formPanel.setLayout(layout);
		
	    outgoingMessageEdit = new TextArea ();
	    outgoingMessageEdit.setHeight(420);	
	    outgoingMessageEdit.setFieldLabel("Outgoing Message");	 
	    
	    //outgoingMessageEdit.setInputStyleAttribute("white-space", "pre");
	    outgoingMessageEdit.setInputStyleAttribute("word-wrap", "normal");
	    outgoingMessageEdit.setInputStyleAttribute("overflow-x", "scroll");  // for IE
	    // outgoingMessageEdit.getElement().setAttribute("wrap", "off");
	    
	    outgoingMessageEdit.setReadOnly(true);

		formPanel.add(outgoingMessageEdit); 
		
	    return formPanel;
	}
}
