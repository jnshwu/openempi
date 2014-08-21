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

import org.openempi.webapp.client.model.AuditEventWeb;
import org.openempi.webapp.client.model.AuditEventListWeb;
import org.openempi.webapp.client.model.AuditEventTypeWeb;
import org.openempi.webapp.client.model.AuditEventSearchCriteriaWeb;
import org.openempi.webapp.client.model.IdentifierDomainWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.ui.util.InputFormat;
import org.openempi.webapp.client.ui.util.Utility;
import org.openempi.webapp.resources.client.model.Confirm;
import org.openempi.webapp.resources.client.model.Country;
import org.openempi.webapp.resources.client.model.Gender;
import org.openempi.webapp.resources.client.model.State;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
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

import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.DateTimePropertyEditor;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AuditEventView extends View
{
	public static final Integer PAGE_SIZE = new Integer(10);
	
	private Grid<AuditEventWeb> grid;
	private ListStore<AuditEventWeb> store = new ListStore<AuditEventWeb>();
	
	private TextField<String> selectedEvenyTypes;
	private ListView<AuditEventTypeWeb> evenyTypes;
	private ListStore<AuditEventTypeWeb> eventTypesStore = new ListStore<AuditEventTypeWeb>();
	private DateField startDate;
	private DateField endDate;
	
	private Status status;
	private Button searchButton;
	private Button cancelButton;

	public RpcProxy<PagingLoadResult<AuditEventWeb>> proxy;
	public BasePagingLoader<PagingLoadResult<AuditEventWeb>> pagingLoader;
	public PagingToolBar pagingToolBar;
	
	public AuditEventSearchCriteriaWeb searchCriteria;
	
	private Dialog refPersonInfoDialog = null;	
	private LayoutContainer formButtonContainer;	
	private LayoutContainer identifierContainer;
	private LayoutContainer formContainer;
	private FormPanel leftFormPanel; 
	private FormPanel rightFormPanel; 
	// Name
	private TextField<String> firstName;
	private TextField<String> middleName;
	private TextField<String> lastName;
	private TextField<String> prefix;
	private TextField<String> suffix;
	
	// Birth
	private DateField dateOfBirth;
	private TextField<String> birthPlace;
	private NumberField birthOrder;
	private ComboBox<Confirm> multiBirthInd;
	
	// Address
	private TextField<String> address1;
	private TextField<String> address2;
	private TextField<String> city;
	private ComboBox<State> state;
	private TextField<String> zip;
	private ComboBox<Country> country;

	// Address Other
	private TextField<String> village;
	private TextField<String> villageId;
	private TextField<String> sector;
	private TextField<String> sectorId;
	private TextField<String> cell;
	private TextField<String> cellId;
	private TextField<String> district;
	private TextField<String> districtId;
	private TextField<String> province;
	
	// Phone
	private TextField<String> phoneCountryCode;
	private TextField<String> phoneAreaCode;
	private TextField<String> phoneNumber;
	private TextField<String> phoneExt;
	
	// Other
	private ComboBox<Gender> gender;
	private TextField<String> degree;
	private TextField<String> fatherName;
	private TextField<String> motherName;
	private TextField<String> maidenName;
	private ComboBox<Confirm> maritalStatus;
	private ComboBox<Confirm> deathInd;
	private DateField deathTime;
	private TextField<String> ssn;
	private TextField<String> email;

	private ListStore<PersonIdentifierWeb> identifierStore = new ListStore<PersonIdentifierWeb>();	
	private Grid<PersonIdentifierWeb> identifierGrid;	
	
	ListStore<Gender> genders = new ListStore<Gender>();
	ListStore<State> states = new ListStore<State>();
	ListStore<Country> countries = new ListStore<Country>();
	ListStore<Confirm> confirms = new ListStore<Confirm>();
	ListStore<IdentifierDomainWeb> domains = new ListStore<IdentifierDomainWeb>();
	
	private LayoutContainer container;
	
	@SuppressWarnings("unchecked")
	public AuditEventView(Controller controller) {
		super(controller);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.AuditEventView) {
			
			searchCriteria = null;
			initUI();
			
			if( Registry.get(Constants.AUDIT_EVENT_TYPE_CODES) != null ) {	
				List<AuditEventTypeWeb> auditEventTypes =  (List<AuditEventTypeWeb>)Registry.get(Constants.AUDIT_EVENT_TYPE_CODES);
				/*for (AuditEventTypeWeb type : auditEventTypes) {
					Info.display("Information", "Event Types: "+ type.getAuditEventTypeCd() + ", " + type.getAuditEventTypeName());			
				}*/
				eventTypesStore.removeAll();
				eventTypesStore.add(auditEventTypes);
			}
			
		}else if (event.getType() == AppEvents.Logout) {
			
  		    Dispatcher.get().dispatch(AppEvents.Logout);
			
		} else if (event.getType() == AppEvents.AuditEventReceived) {
			
			// Info.display("Information", "EventReceived");
			store.removeAll();
				
			AuditEventListWeb events = (AuditEventListWeb) event.getData();	
			if( events.getAuditEvents() != null )
				store.add(events.getAuditEvents());
			
			grid.getSelectionModel().select(0, true);
			grid.getSelectionModel().deselect(0);
			
			status.hide();
			searchButton.unmask();  
			
		} else if (event.getType() == AppEvents.Error) {			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, null);  			
		}
	}

	private FormPanel setupButtonPanel(int tabIndex) {
		FormPanel buttonPanel = new FormPanel(); 
		buttonPanel.setHeaderVisible(false);
		buttonPanel.setBodyBorder(false);
		buttonPanel.setStyleAttribute("paddingRight", "10px");
		buttonPanel.setButtonAlign(HorizontalAlignment.CENTER);		

		searchButton = new Button("Search Event", IconHelper.create("images/search_icon_16x16.png"), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
								
				searchCriteria = new AuditEventSearchCriteriaWeb();
				if( !startDate.validate() || !endDate.validate()) {
					Info.display("Warning", "You must enter valid format of date before pressing the search button.");
					return;					
				}
				searchCriteria.setStartDateTime( Utility.DateTimeToString(startDate.getValue()));
				searchCriteria.setEndDateTime( Utility.DateTimeToString(endDate.getValue()));
							
				Set<AuditEventTypeWeb> ids = new HashSet<AuditEventTypeWeb>();
				if (evenyTypes.getSelectionModel().getSelection().size() > 0) {
					List<AuditEventTypeWeb> types =  evenyTypes.getSelectionModel().getSelection();
					for( AuditEventTypeWeb type : types) {
						// Info.display("Information", "Event Type: "+type.getAuditEventTypeName());						
					    ids.add(type);
					}	
				    searchCriteria.setAuditEventTypes(ids);
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
				selectedEvenyTypes.setValue("");
				evenyTypes.getSelectionModel().deselectAll();
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
	    proxy = new RpcProxy<PagingLoadResult<AuditEventWeb>>() {	
	    	
            @Override
            public void load(final Object loadConfig, final AsyncCallback<PagingLoadResult<AuditEventWeb>> callback) {
           	
            	if( searchCriteria == null ){
  		            callback.onSuccess(new BasePagingLoadResult<AuditEventWeb>( null, 0, 0 ));
            		return;
            	}
            		
                // set page offset for searchCriteria
            	searchCriteria.setFirstResult( ((PagingLoadConfig)loadConfig).getOffset() );
            	
	        	getController().getAuditEventDataService().getAuditEventsBySearch(searchCriteria, new AsyncCallback<AuditEventListWeb>() {
	    		      public void onFailure(Throwable caught) {
	    		    	  
	    		    	  if (caught instanceof AuthenticationException) {
	    		    		  Dispatcher.get().dispatch(AppEvents.Logout);
	    		    		  return;
	    		    	  }		    
	    		    	  Dispatcher.forwardEvent(AppEvents.Error, caught);
	    		      }


	    		      public void onSuccess(AuditEventListWeb result) {
	    		    	  // Info.display("Information", "The persons: " +((PagingLoadConfig)loadConfig).getOffset());	  
	    		    	  
	    				  // PagingLoadConfig configuration
	    		          callback.onSuccess(new BasePagingLoadResult<AuditEventWeb>( result.getAuditEvents(), ((PagingLoadConfig)loadConfig).getOffset(), result.getTotalCount() ));
	    		      }
	    		    });	 
	        }
	    };
	    
	    // Page loader
	    pagingLoader = new BasePagingLoader<PagingLoadResult<AuditEventWeb>>(proxy);	    
	    pagingLoader.setRemoteSort(true);
	    pagingLoader.addLoadListener(new LoadListener() {
			// After the loader be completely filled, remove the mask
			public void loaderLoad(LoadEvent le) {
				status.hide();
				searchButton.unmask();
				
				grid.unmask(); 
			}
		});		

	    store = new ListStore<AuditEventWeb>(pagingLoader); 	 
	    
	    PagingToolBar pagingToolBar = new PagingToolBar(PAGE_SIZE);  
	    pagingToolBar.bind(pagingLoader);		    
	    return pagingToolBar;
	}

	private Grid<AuditEventWeb> setupGrid() {
		
		GridCellRenderer<AuditEventWeb> refButtonRenderer = new GridCellRenderer<AuditEventWeb>() {
	      	private boolean init;
	      
			@Override
			public Object render(final AuditEventWeb model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config,
					int rowIndex, int colIndex,
					ListStore<AuditEventWeb> store, Grid<AuditEventWeb> grid) {
				
		        	if (!init) {
			            init = true;
			            grid.addListener(Events.ColumnResize, new Listener<GridEvent<AuditEventWeb>>() {

			            public void handleEvent(GridEvent<AuditEventWeb> be) {
			              for (int i = 0; i < be.getGrid().getStore().getCount(); i++) {
			                if (be.getGrid().getView().getWidget(i, be.getColIndex()) != null
			                    && be.getGrid().getView().getWidget(i, be.getColIndex()) instanceof BoxComponent) {
			                  ((BoxComponent) be.getGrid().getView().getWidget(i, be.getColIndex())).setWidth(be.getWidth() - 10);
			                }
			              }
			            }
			          });
			        }

	            	AuditEventWeb auditEvent = model;
	            	PersonWeb person = auditEvent.getRefPerson();
	            	Button b = null;
	            	if( person != null ) {
				        b = new Button(person.getGivenName() + " "+person.getFamilyName(), new SelectionListener<ButtonEvent>() {
				            @Override
				            public void componentSelected(ButtonEvent ce) {
				            	
				            	AuditEventWeb auditEvent = model;
				            	
				            	buildRefPersonInfoDialog(auditEvent.getRefPerson());
				            	refPersonInfoDialog.show();
				            }
				        });
				        b.setWidth(grid.getColumnModel().getColumnWidth(colIndex) - 10);
				        b.setToolTip("Click for more information");
	            	}
			        return b;
			}
	    };

		GridCellRenderer<AuditEventWeb> altButtonRenderer = new GridCellRenderer<AuditEventWeb>() {
	      	private boolean init;
	      
			@Override
			public Object render(final AuditEventWeb model, String property,
					com.extjs.gxt.ui.client.widget.grid.ColumnData config,
					int rowIndex, int colIndex,
					ListStore<AuditEventWeb> store, Grid<AuditEventWeb> grid) {
				
		        	if (!init) {
			            init = true;
			            grid.addListener(Events.ColumnResize, new Listener<GridEvent<AuditEventWeb>>() {

			            public void handleEvent(GridEvent<AuditEventWeb> be) {
			              for (int i = 0; i < be.getGrid().getStore().getCount(); i++) {
			                if (be.getGrid().getView().getWidget(i, be.getColIndex()) != null
			                    && be.getGrid().getView().getWidget(i, be.getColIndex()) instanceof BoxComponent) {
			                  ((BoxComponent) be.getGrid().getView().getWidget(i, be.getColIndex())).setWidth(be.getWidth() - 10);
			                }
			              }
			            }
			          });
			        }

	            	AuditEventWeb auditEvent = model;
	            	PersonWeb person = auditEvent.getAltRefPerson();
	            	Button b = null;
	            	if( person != null ) {
				        b = new Button(person.getGivenName() + " "+person.getFamilyName(), new SelectionListener<ButtonEvent>() {
				            @Override
				            public void componentSelected(ButtonEvent ce) {
				            	
				            	AuditEventWeb auditEvent = model;
				            	
				            	buildRefPersonInfoDialog(auditEvent.getAltRefPerson());
				            	refPersonInfoDialog.show();
				            }
				        });
				        b.setWidth(grid.getColumnModel().getColumnWidth(colIndex) - 10);
				        b.setToolTip("Click for more information");
	            	}
			        return b;
			}
	    };
	    
		// Audit Event Grid
		ColumnConfig typeColumn = new ColumnConfig("auditEventType.auditEventTypeName", "Event Type", 150);
		ColumnConfig descriptionColumn = new ColumnConfig("auditEventDescription", "Description", 350);
		ColumnConfig dateTimeColumn = new ColumnConfig("dateCreated", "Date Created", 120);
					 dateTimeColumn.setDateTimeFormat(DateTimeFormat.getShortDateTimeFormat());
		ColumnConfig refPersonColumn = new ColumnConfig("refPerson.givenName", "Reference Person", 150);
					 refPersonColumn.setRenderer(refButtonRenderer);
		ColumnConfig altRefPersonColumn = new ColumnConfig("altRefPerson.givenName", "Alt Reference Person", 150);
					 altRefPersonColumn.setRenderer(altButtonRenderer);
		ColumnConfig userCreatedByColumn = new ColumnConfig("userCreatedBy.username", "Created By", 150);
		
		List<ColumnConfig> config = new ArrayList<ColumnConfig>();
		config.add(typeColumn);
		config.add(descriptionColumn);
		config.add(dateTimeColumn);
		config.add(refPersonColumn);
		config.add(altRefPersonColumn);
		config.add(userCreatedByColumn);	
		
		final ColumnModel cm = new ColumnModel(config);
		
		Grid<AuditEventWeb>  grid = new Grid<AuditEventWeb>(store, cm);
		grid.setBorders(true);
		grid.setAutoWidth(true);
		grid.setStripeRows(true); 
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		grid.setHeight(330);
		
		grid.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<AuditEventWeb>>() {
			
			public void handleEvent(SelectionChangedEvent<AuditEventWeb> be) {
				List<AuditEventWeb> selection = be.getSelection();
			}
		});
		
		grid.addListener(Events.SortChange, new Listener<GridEvent<AuditEventWeb>>() {	
			public void handleEvent(GridEvent<AuditEventWeb> be) {
				AuditEventWeb selectField = be.getGrid().getSelectionModel().getSelectedItem();				
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
		cp.setHeading("Event Viewer");
		cp.setFrame(true);
		cp.setIcon(IconHelper.create("images/search_icon_16x16.png"));
		cp.setLayout(new FormLayout());
		cp.setSize(1100, 630);

		// Search Container
		ContentPanel searchContainer = new ContentPanel();
		searchContainer.setHeaderVisible(false);
		FormLayout searchFormLayout = new FormLayout();
		searchFormLayout.setLabelWidth(130);
		searchFormLayout.setDefaultWidth(770);

		searchContainer.setLayout(searchFormLayout);

		selectedEvenyTypes = new TextField<String>();
		selectedEvenyTypes.setFieldLabel("Selected Event Types");
		selectedEvenyTypes.setReadOnly(true);
		
		evenyTypes = new ListView<AuditEventTypeWeb>();
		evenyTypes.setDisplayProperty("auditEventTypeName"); 
		evenyTypes.setWidth(220);
		evenyTypes.setHeight(110);
		evenyTypes.setStore(eventTypesStore);
		evenyTypes.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<AuditEventTypeWeb>>() {
			
			public void handleEvent(SelectionChangedEvent<AuditEventTypeWeb> be) {
				List<AuditEventTypeWeb> selections = be.getSelection();
				String selectedTypes = "";
				for( AuditEventTypeWeb type : selections) {
					if(selectedTypes.isEmpty() )
						selectedTypes = type.getAuditEventTypeName();
					else
						selectedTypes = selectedTypes + ", "+type.getAuditEventTypeName();
				}	
				selectedEvenyTypes.setValue(selectedTypes);	
				
				if(selectedTypes.isEmpty() ) {
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
		
		endDate = new DateField();
		endDate.setFieldLabel("End Date Time");
		endDate.setToolTip("yyyy-MM-dd HH:mm");
		endDate.setPropertyEditor(dateFormat);
		
		LayoutContainer main = new LayoutContainer();  
		main.setLayout(new ColumnLayout());

			LayoutContainer left = new LayoutContainer();
			left.setStyleAttribute("paddingRight", "10px");
			FormLayout layout = new FormLayout();
			layout.setLabelWidth(130);
			layout.setDefaultWidth(220);
			// layout.setLabelAlign(LabelAlign.TOP);  
			left.setLayout(layout);
			left.add(startDate);
			
			LayoutContainer right = new LayoutContainer();
			right.setStyleAttribute("paddingLeft", "10px");
			layout = new FormLayout();
			// layout.setLabelAlign(LabelAlign.TOP);
			layout.setLabelWidth(130);
			layout.setDefaultWidth(220);
			right.setLayout(layout);
			right.add(endDate);
		
		main.add(left, new ColumnData(.5));  
		main.add(right, new ColumnData(.5));
		
		HBoxLayoutData dataSelectedTypes = new HBoxLayoutData(new Margins(5, 0, 0, 0));	
		searchContainer.add(selectedEvenyTypes, dataSelectedTypes);
		HBoxLayoutData dataTypes = new HBoxLayoutData(new Margins(5, 0, 5, 135));		
		searchContainer.add(evenyTypes, dataTypes);
		
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
	
	//  RefPersonInfoDialog
	private void buildRefPersonInfoDialog(PersonWeb person) {		
//		if(refPersonInfoDialog != null) {
//			displayRecord(person);
//		    return;
//		}
		
		refPersonInfoDialog = new Dialog();
		refPersonInfoDialog.setBodyBorder(false);
		refPersonInfoDialog.setWidth(920);
		refPersonInfoDialog.setHeight(640);
		refPersonInfoDialog.setIcon(IconHelper.create("images/information.png"));
		refPersonInfoDialog.setHeading("Reference Person Information");
		refPersonInfoDialog.setButtons(Dialog.OK);
		refPersonInfoDialog.setModal(true);
		refPersonInfoDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
	          @Override
	          public void componentSelected(ButtonEvent ce) {
		        	  	 
	        	  // refPersonInfoDialog.hide();	
	        	  refPersonInfoDialog.close();
				}
	    });
		
		ContentPanel cp = new ContentPanel();
		cp.setFrame(true);
		cp.setLayout(new BorderLayout());
		cp.setSize(920, 600);

		formButtonContainer = new LayoutContainer();
		formButtonContainer.setScrollMode(Scroll.AUTO);
		
			TableLayout identlayout = new TableLayout(2);
			identlayout.setWidth("100%");
			//identlayout.setCellSpacing(5);
			identlayout.setCellVerticalAlign(VerticalAlignment.TOP);
		    
			TableLayout formlayout = new TableLayout(2);
			formlayout.setWidth("100%");
			//formlayout.setCellSpacing(5);
			formlayout.setCellVerticalAlign(VerticalAlignment.TOP);
		    
			identifierContainer = new LayoutContainer();;	
			identifierContainer.setLayout(identlayout);
			identifierContainer.add( setupIdentifierForm() );
			
			formContainer = new LayoutContainer();
//			formContainer.setBorders(true);
			formContainer.setLayout(formlayout);
		
				leftFormPanel = setupPersonLeftForm("", 2);
				rightFormPanel = setupPersonRightForm("", 17);
				
			formContainer.add(leftFormPanel);	
			formContainer.add(rightFormPanel);		
			
		formButtonContainer.add(identifierContainer);
		formButtonContainer.add(formContainer);
		
		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(4, 2, 4, 2));
		
		cp.add(formButtonContainer, data);
	
		refPersonInfoDialog.add(cp);
		
		displayRecord(person);
	}
	
	private FormPanel setupIdentifierForm() {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setLabelWidth(150);
	    formPanel.setWidth(855);
	    
		formPanel.add( setupIdentifierfieldSet(1)); 
		
	    return formPanel;
	}
	
	private FieldSet setupIdentifierfieldSet(int tabIndex) {
		FieldSet identifierfieldSet = new FieldSet();  
		identifierfieldSet.setHeading("Identifiers");  
		identifierfieldSet.setCollapsible(true);  
		identifierfieldSet.setBorders(false);  
		FormLayout identifierlayout = new FormLayout();  
		identifierlayout.setLabelWidth(150);  
		identifierlayout.setDefaultWidth(390); // It is the real function to set the textField width
		identifierfieldSet.setLayout(identifierlayout);  	

			
			ContentPanel cp = new ContentPanel();  
			cp.setHeaderVisible(false);
			cp.setWidth(875);
					    
		    cp.add( setupPersonIdentifierGrid(identifierStore,tabIndex) );  
		    
			identifierfieldSet.add(cp);	
			
			return identifierfieldSet;
	}
	
	private Grid<PersonIdentifierWeb> setupPersonIdentifierGrid( ListStore<PersonIdentifierWeb> identifierStore, int tabIndex ) {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
					    
	    // Columns
		ColumnConfig column;

		column = new ColumnConfig();
		column.setId("identifier");  
		column.setHeader("Identifier");
		column.setWidth(220);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("identifierDomainName");  
		column.setHeader("Domain Name");
		column.setWidth(160);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("namespaceIdentifier");  
		column.setHeader("Namespace Identifier");
		column.setWidth(160);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setId("universalIdentifier");  
		column.setHeader("Universal Identifier");  
		column.setWidth(160);
		configs.add(column);
	
		column = new ColumnConfig();
		column.setId("universalIdentifierTypeCode");  
		column.setHeader("Universal Identifier Type");  
		column.setWidth(130);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);
		identifierGrid = new Grid<PersonIdentifierWeb>(identifierStore, cm);
		
		identifierGrid.setStyleAttribute("borderTop", "none");
		identifierGrid.setBorders(true);
		identifierGrid.setStripeRows(true); 
		identifierGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		identifierGrid.setWidth(875); // 850 + vertical scroll bar space
		identifierGrid.setHeight(86);
		identifierGrid.setTabIndex(tabIndex);	
		
		return identifierGrid;
	}
	
	private FormPanel setupPersonLeftForm(String title, int tabIndex) {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setLabelWidth(150);
	    formPanel.setWidth(400);
//	    formPanel.setLabelAlign(LabelAlign.RIGHT); // default is LEFT
	    formPanel.setTitle(title); 
	    
	    // Name fields
	    FieldSet namefieldSet = new FieldSet();  
	    namefieldSet.setHeading("Name");  
	    namefieldSet.setCollapsible(true);  
	    namefieldSet.setBorders(false);  
	    FormLayout namelayout = new FormLayout();  
	    namelayout.setLabelWidth(150);  
	    namefieldSet.setLayout(namelayout); 
	    
			firstName = new TextField<String>();
			firstName.setFieldLabel("First Name");
			firstName.setReadOnly(true);
			firstName.setTabIndex(tabIndex++);
			
			middleName = new TextField<String>();
			middleName.setFieldLabel("Middle Name");
			middleName.setReadOnly(true);
			middleName.setTabIndex(tabIndex++);
			
			lastName = new TextField<String>();
			lastName.setFieldLabel("Last Name");
			lastName.setReadOnly(true);
			lastName.setTabIndex(tabIndex++);

			prefix = new TextField<String>();
			prefix.setFieldLabel("Prefix");
			prefix.setReadOnly(true);
			prefix.setTabIndex(tabIndex++);

			suffix = new TextField<String>();
			suffix.setFieldLabel("Suffix");
			suffix.setReadOnly(true);
			suffix.setTabIndex(tabIndex++);

			namefieldSet.add(firstName);
			namefieldSet.add(middleName);
			namefieldSet.add(lastName);			
			namefieldSet.add(prefix);		
			namefieldSet.add(suffix);		
			
		// Address fields
		FieldSet addressfieldSet = new FieldSet();  
		addressfieldSet.setHeading("Address");  
		addressfieldSet.setCollapsible(true);  
		addressfieldSet.setBorders(false); 		
		FormLayout addresslayout = new FormLayout();  
		addresslayout.setLabelWidth(150);  
		addressfieldSet.setLayout(addresslayout);
		    		
			address1 = new TextField<String>();
			address1.setFieldLabel("Address 1");
			address1.setReadOnly(true);
			address1.setTabIndex(tabIndex++);
			
			address2 = new TextField<String>();
			address2.setFieldLabel("Address 2");
			address2.setReadOnly(true);
			address2.setTabIndex(tabIndex++);
			
			city = new TextField<String>();
			city.setFieldLabel("City");
			city.setReadOnly(true);
			city.setTabIndex(tabIndex++);
			
			state = new ComboBox<State>();
			state.setFieldLabel("State");
			state.setEmptyText("Select a state...");  
			state.setDisplayField("name");  
			state.setWidth(150);  
			state.setStore(states);
			state.setTypeAhead(true);  
		    state.setTriggerAction(TriggerAction.ALL);
		    state.setEditable(false);
		    state.getListView().disableEvents(true);
		    state.setTabIndex(tabIndex++);
			
		    zip = new TextField<String>();
			zip.setFieldLabel("Zip Code");
		    zip.setReadOnly(true);
			zip.setTabIndex(tabIndex++);

			country = new ComboBox<Country>();
			country.setFieldLabel("Country");
			country.setEmptyText("Select a country...");
			country.setDisplayField("name");
			country.setTemplate( InputFormat.getFlagTemplate() );
			country.setWidth(100);
			country.setStore(countries);
			country.setTypeAhead(true);
			country.setTriggerAction(TriggerAction.ALL);
			country.setEditable(false);
			country.getListView().disableEvents(true);
			country.setTabIndex(tabIndex++);
			
			addressfieldSet.add(address1);
			addressfieldSet.add(address2);
			addressfieldSet.add(city);
			addressfieldSet.add(state);
			addressfieldSet.add(zip);
			addressfieldSet.add(country);					

		// Address Other fields
		FieldSet addressOtherfieldSet = new FieldSet();  
		addressOtherfieldSet.setHeading("Address Other");  
		addressOtherfieldSet.setCollapsible(true);  
		addressOtherfieldSet.setBorders(false); 		
		FormLayout addressOtherlayout = new FormLayout();  
		addressOtherlayout.setLabelWidth(150);  
		addressOtherfieldSet.setLayout(addressOtherlayout);
			    		
			village = new TextField<String>();
			village.setFieldLabel("Village");
			village.setReadOnly(true);
			village.setTabIndex(tabIndex++);

			villageId = new TextField<String>();
			villageId.setFieldLabel("Village ID");
			villageId.setReadOnly(true);
			villageId.setTabIndex(tabIndex++);

			sector = new TextField<String>();
			sector.setFieldLabel("Sector");
			sector.setReadOnly(true);
			sector.setTabIndex(tabIndex++);

			sectorId = new TextField<String>();
			sectorId.setFieldLabel("Sector ID");
			sectorId.setReadOnly(true);
			sectorId.setTabIndex(tabIndex++);

			cell = new TextField<String>();
			cell.setFieldLabel("Cell");
			cell.setReadOnly(true);
			cell.setTabIndex(tabIndex++);

			cellId = new TextField<String>();
			cellId.setFieldLabel("Cell ID");
			cellId.setReadOnly(true);
			cellId.setTabIndex(tabIndex++);

			district = new TextField<String>();
			district.setFieldLabel("District");
			district.setReadOnly(true);
			district.setTabIndex(tabIndex++);

			districtId = new TextField<String>();
			districtId.setFieldLabel("District ID");
			districtId.setReadOnly(true);
			districtId.setTabIndex(tabIndex++);

			province = new TextField<String>();
			province.setFieldLabel("Province");
			province.setReadOnly(true);
			province.setTabIndex(tabIndex++);
			
		addressOtherfieldSet.add(village);
		addressOtherfieldSet.add(villageId);
		addressOtherfieldSet.add(sector);
		addressOtherfieldSet.add(sectorId);
		addressOtherfieldSet.add(district);
		addressOtherfieldSet.add(districtId);
		addressOtherfieldSet.add(province);
		
		formPanel.add(namefieldSet); 
		formPanel.add(addressfieldSet); 
		formPanel.add(addressOtherfieldSet); 
		
	    return formPanel;
	}
	
	private FormPanel setupPersonRightForm(String title, int tabIndex) {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setLabelWidth(150);
	    formPanel.setWidth(400);
//	    formPanel.setLabelAlign(LabelAlign.RIGHT); // default is LEFT
	    formPanel.setTitle(title);  

	    FieldSet phonefieldSet = new FieldSet();  
	    phonefieldSet.setHeading("Phone");  
	    phonefieldSet.setCollapsible(true);  
	    phonefieldSet.setBorders(false);  
	    FormLayout phonelayout = new FormLayout();  
	    phonelayout.setLabelWidth(150);  
	    phonefieldSet.setLayout(phonelayout);  

			phoneCountryCode = new TextField<String>();
			phoneCountryCode.setFieldLabel("Phone Country Code");	
			phoneCountryCode.setReadOnly(true);			
			phoneCountryCode.setTabIndex(tabIndex++);	
			
			phoneAreaCode = new TextField<String>();
			phoneAreaCode.setFieldLabel("Phone Area Code");	
			phoneAreaCode.setReadOnly(true);
			phoneAreaCode.setTabIndex(tabIndex++);
			
			phoneNumber = new TextField<String>();
			phoneNumber.setFieldLabel("Phone Number");	
			phoneNumber.setToolTip("xxx-xxxx");
			phoneNumber.setReadOnly(true);
			phoneNumber.setTabIndex(tabIndex++);
	
			phoneExt = new TextField<String>();
			phoneExt.setFieldLabel("Phone Extension");	
			phoneExt.setReadOnly(true);
			phoneExt.setTabIndex(tabIndex++);
	
			phonefieldSet.add(phoneCountryCode);
			phonefieldSet.add(phoneAreaCode);
			phonefieldSet.add(phoneNumber);
			phonefieldSet.add(phoneExt);		

			// Birth fields	  
			FieldSet birthfieldSet = new FieldSet();  
			birthfieldSet.setHeading("Birth");  
			birthfieldSet.setCollapsible(true);  
			birthfieldSet.setBorders(false);  
			FormLayout birthlayout = new FormLayout();  
			birthlayout.setLabelWidth(150);  
			birthfieldSet.setLayout(birthlayout);  
			
				dateOfBirth = new DateField();
				dateOfBirth.setFieldLabel("Date of Birth");
				dateOfBirth.setToolTip("yyyy-MM-dd");
				dateOfBirth.setEditable(false);
				dateOfBirth.getDatePicker().disableEvents(true);
				dateOfBirth.setTabIndex(tabIndex++);

				birthPlace = new TextField<String>();
				birthPlace.setFieldLabel("Birth Place");
				birthPlace.setReadOnly(true);	
				birthPlace.setTabIndex(tabIndex++);	
				
				birthOrder = new NumberField();
				birthOrder.setFieldLabel("Birth Order");
				birthOrder.setAllowDecimals(false);
				birthOrder.setAllowNegative(false);
				birthOrder.setReadOnly(true);	
				birthOrder.setTabIndex(tabIndex++);	
				 
				multiBirthInd = new ComboBox<Confirm>();
				multiBirthInd.setFieldLabel("Multiple Birth Indicator");
				multiBirthInd.setEmptyText("Select a Yes or No...");
				multiBirthInd.setDisplayField("name");
				multiBirthInd.setWidth(100);
				multiBirthInd.setStore(confirms);
				multiBirthInd.setTypeAhead(true);
				multiBirthInd.setTriggerAction(TriggerAction.ALL);
				multiBirthInd.setEditable(false);
				multiBirthInd.getListView().disableEvents(true);	
				multiBirthInd.setTabIndex(tabIndex++);
				
				
				birthfieldSet.add(dateOfBirth);
				birthfieldSet.add(birthPlace);
				birthfieldSet.add(birthOrder);
				birthfieldSet.add(multiBirthInd);
				
		FieldSet otherfieldSet = new FieldSet();  
		otherfieldSet.setHeading("Other");  
		otherfieldSet.setCollapsible(true);  
		otherfieldSet.setBorders(false);  
		FormLayout otherlayout = new FormLayout();  
		otherlayout.setLabelWidth(150);  
		otherfieldSet.setLayout(otherlayout);  		
		
			gender = new ComboBox<Gender>();
			gender.setFieldLabel("Gender");
			gender.setEmptyText("Select a gender...");
			gender.setDisplayField("name");
			gender.setWidth(100);
			gender.setStore(genders);
			gender.setTypeAhead(true);
			gender.setTriggerAction(TriggerAction.ALL);
			gender.setEditable(false);
			gender.getListView().disableEvents(true);	
			gender.setTabIndex(tabIndex++);

			degree = new TextField<String>();
			degree.setFieldLabel("Degree");	
			degree.setReadOnly(true);
			degree.setTabIndex(tabIndex++);

			fatherName = new TextField<String>();
			fatherName.setFieldLabel("Father's Name");	
			fatherName.setReadOnly(true);
			fatherName.setTabIndex(tabIndex++);
			
			motherName = new TextField<String>();
			motherName.setFieldLabel("Mother's Name");	
			motherName.setReadOnly(true);
			motherName.setTabIndex(tabIndex++);
			
			maidenName = new TextField<String>();
			maidenName.setFieldLabel("Mother's Maiden Name");	
			maidenName.setReadOnly(true);
			maidenName.setTabIndex(tabIndex++);
	
			maritalStatus = new ComboBox<Confirm>();
			maritalStatus.setFieldLabel("Marital Status");
			maritalStatus.setEmptyText("Select a Yes or No...");
			maritalStatus.setDisplayField("name");
			maritalStatus.setWidth(100);
			maritalStatus.setStore(confirms);
			maritalStatus.setTypeAhead(true);
			maritalStatus.setTriggerAction(TriggerAction.ALL);
			maritalStatus.setEditable(false);
			maritalStatus.getListView().disableEvents(true);	
			maritalStatus.setTabIndex(tabIndex++);
			
			deathInd = new ComboBox<Confirm>();
			deathInd.setFieldLabel("Death Indicator");
			deathInd.setEmptyText("Select a Yes or No...");
			deathInd.setDisplayField("name");
			deathInd.setWidth(100);
			deathInd.setStore(confirms);
			deathInd.setTypeAhead(true);
			deathInd.setTriggerAction(TriggerAction.ALL);
			deathInd.setEditable(false);
			deathInd.getListView().disableEvents(true);	
			deathInd.setTabIndex(tabIndex++);
			
			DateTimePropertyEditor dateFormat = new DateTimePropertyEditor("yyyy-MM-dd HH:mm");
			deathTime = new DateField();
			deathTime.setFieldLabel("Death Time");
			deathTime.setToolTip("yyyy-MM-dd HH:mm");
			deathTime.setPropertyEditor(dateFormat);
			deathTime.setEditable(false);
			deathTime.getDatePicker().disableEvents(true);
			deathTime.setTabIndex(tabIndex++);
			
			ssn = new TextField<String>();
			ssn.setFieldLabel("Social Security Number");
			ssn.setReadOnly(true);
			ssn.setTabIndex(tabIndex++);
			
			email = new TextField<String>();
			email.setFieldLabel("Email");	
			email.setRegex(InputFormat.EMAIL_FORMATS);
	        email.getMessages().setRegexText("Invalid email format");
	        email.setToolTip("xyz@example.com");	
			email.setReadOnly(true);
			email.setTabIndex(tabIndex++);
			
			otherfieldSet.add(gender);
			otherfieldSet.add(degree);
			otherfieldSet.add(fatherName);
			otherfieldSet.add(motherName);
			otherfieldSet.add(maidenName);
			otherfieldSet.add(maritalStatus);
			otherfieldSet.add(deathInd);
			otherfieldSet.add(deathTime);
			otherfieldSet.add(ssn);
			otherfieldSet.add(email);
		    
		formPanel.add(phonefieldSet); 
		formPanel.add(birthfieldSet); 
		formPanel.add(otherfieldSet); 
			
	    return formPanel;
	}
	
	private void displayRecord(PersonWeb person) {
		
		// Name
		firstName.setValue(person.getGivenName());		
		middleName.setValue(person.getMiddleName());
		lastName.setValue(person.getFamilyName());
		prefix.setValue(person.getPrefix());
		suffix.setValue(person.getSuffix());
		
		// Birth
		dateOfBirth.setValue( Utility.StringToDate(person.getDateOfBirth()));	
		birthPlace.setValue(person.getBirthPlace());
		birthOrder.setValue(person.getBirthOrder( ));	
		for( Confirm cf : confirms.getModels()) {
			if(cf.getCode().equals(person.getMultipleBirthInd()))
				multiBirthInd.setValue(cf);
		}
		
		// Address
		address1.setValue(person.getAddress1());
		address2.setValue(person.getAddress2());
		city.setValue(person.getCity());
		for( State st : states.getModels()) {
			if(st.getAbbr().equals(person.getState()))
				state.setValue(st);
		}
		zip.setValue(person.getPostalCode());
		for( Country co : countries.getModels()) {
			if(co.getName().equals(person.getCountry()))
				country.setValue(co);
		}

		// Address Other
		village.setValue(person.getVillage());
		villageId.setValue(person.getVillageId());
		sector.setValue(person.getSector());
		sectorId.setValue(person.getSectorId());
		cell.setValue(person.getCell());
		cellId.setValue(person.getCellId());
		district.setValue(person.getDistrict());
		districtId.setValue(person.getDistrictId());
		province.setValue(person.getProvince());
		
		// Phone
		phoneCountryCode.setValue(person.getPhoneCountryCode());
		phoneAreaCode.setValue(person.getPhoneAreaCode());
		phoneNumber.setValue(person.getPhoneNumber());
		phoneExt.setValue(person.getPhoneExt());		
		
		// Other
		for( Gender gd : genders.getModels()) {
			if(person.getGender()!= null && person.getGender().equals(gd.getName()))
				gender.setValue(gd);
		}		
		degree.setValue(person.getDegree());
		fatherName.setValue(person.getFatherName());
		motherName.setValue(person.getMotherName());
		maidenName.setValue(person.getMothersMaidenName());
		for( Confirm cf : confirms.getModels()) {
			if(cf.getCode().equals(person.getMaritalStatusCode()))
				maritalStatus.setValue(cf);
		}							
		deathTime.setValue( Utility.StringToDateTime(person.getDeathTime()));
		for( Confirm cf : confirms.getModels()) {
			if(cf.getCode().equals(person.getDeathInd()))
				deathInd.setValue(cf);
		}				
		ssn.setValue(person.getSsn());
		email.setValue(person.getEmail());	
		
		// Identifiers
		identifierStore.removeAll();
		for( PersonIdentifierWeb identifier : person.getPersonIdentifiers()) {
			// Info.display("Identifiers:", identifier.getIdentifier());	
			if( identifier.getDateVoided() == null)
				identifierStore.add(identifier);			
		}	
	}
}
