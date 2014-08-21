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

import java.util.List;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.ReportDataServiceAsync;
import org.openempi.webapp.client.domain.AuthenticationException;
import org.openempi.webapp.client.model.ReportWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReportDesignController extends Controller
{
	private ReportDesignView reportDesignView;

	public ReportDesignController() {		
		this.registerEventTypes(AppEvents.ReportDesignView);
	}

	@Override
	protected void initialize() {
		reportDesignView = new ReportDesignView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.ReportDesignView) {
			forwardToView(reportDesignView, event);
			
		} else if (type == AppEvents.ReportRequest) {
			requestReportData();
		} else if (type == AppEvents.ReportAdd) {
			addReportData(event);
		} else if (type == AppEvents.ReportUpdate) {
			editReportData(event);
		} else if (type == AppEvents.ReportDelete) {
			deleteReportData(event);
		}
	}
	
	private void requestReportData() {
		ReportDataServiceAsync reportDataService = getReportDataService();
		reportDataService.getReports(new AsyncCallback<List<ReportWeb>>() {
			
	      public void onFailure(Throwable caught) {
			// Info.display("Information", "onFailure."+caught.getMessage());
	        // Dispatcher.forwardEvent(AppEvents.Error, caught);
	    	  
	    	  if (caught instanceof AuthenticationException) {
	    		  
//	    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    	  forwardToView(reportDesignView, AppEvents.Logout,null);
	    		  return;
	    	  }
	    	  forwardToView(reportDesignView, AppEvents.Error, caught.getMessage());
	      }

	      public void onSuccess(List<ReportWeb> result) {
			// Info.display("Information", "onSuccess.");
	        forwardToView(reportDesignView, AppEvents.ReportReceived, result);
	      }
	    });
	}
	
	private void addReportData(AppEvent event) {
		ReportDataServiceAsync reportDataService = getReportDataService();
		
		ReportWeb report = (ReportWeb) event.getData();		
		
		reportDataService.addReport(report, new AsyncCallback<ReportWeb>() {
			
	      public void onFailure(Throwable caught) {
	    	// Info.display("Information", "onFailure."+caught.getMessage());
	        // Dispatcher.forwardEvent(AppEvents.Error, caught);
	    	  
	    	  if (caught instanceof AuthenticationException) {
	    		  
//	    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    	  forwardToView(reportDesignView, AppEvents.Logout,null);
	    		  return;
	    	  }
	    	forwardToView(reportDesignView, AppEvents.Error, caught.getMessage());
	      }

	      public void onSuccess(ReportWeb report) {
			// Info.display("Information", "add onSuccess.");
	        forwardToView(reportDesignView, AppEvents.ReportAddComplete, report);
	      }
	    });
	}
	
	private void editReportData(AppEvent event) {
		ReportDataServiceAsync reportDataService = getReportDataService();
		
		ReportWeb report = (ReportWeb) event.getData();		
		
		reportDataService.updateReport(report, new AsyncCallback<ReportWeb>() {
			
	      public void onFailure(Throwable caught) {
	    	// Info.display("Information", "onFailure."+caught.getMessage());
	    	  
	    	  if (caught instanceof AuthenticationException) {
	    		  
//	    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    	  forwardToView(reportDesignView, AppEvents.Logout,null);
	    		  return;
	    	  }
	    	forwardToView(reportDesignView, AppEvents.Error, caught.getMessage());
	      }

	      public void onSuccess(ReportWeb report) {
			// Info.display("Information", "edit onSuccess.");
	        forwardToView(reportDesignView, AppEvents.ReportUpdateComplete, report);
	      }
	    });
	}
	
	private void deleteReportData(AppEvent event) {
		ReportDataServiceAsync reportDataService = getReportDataService();
		
		ReportWeb report = (ReportWeb) event.getData();		
		
		reportDataService.deleteReport(report, new AsyncCallback<String>() {
			
	      public void onFailure(Throwable caught) {
			// Info.display("Information", "onFailure."+caught.getMessage());
	        // Dispatcher.forwardEvent(AppEvents.Error, caught);
	    	  
	    	  if (caught instanceof AuthenticationException) {
	    		  
//	    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    	  forwardToView(reportDesignView, AppEvents.Logout,null);
	    		  return;
	    	  }
		    forwardToView(reportDesignView, AppEvents.Error, caught.getMessage());
	      }

	      public void onSuccess(String message) {
			// Info.display("Information", "delete onSuccess.");
	        forwardToView(reportDesignView, AppEvents.ReportDeleteComplete, message);
	      }
	    });
	}
}
