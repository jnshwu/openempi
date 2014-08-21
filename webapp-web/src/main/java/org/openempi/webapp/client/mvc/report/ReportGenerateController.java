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
import org.openempi.webapp.client.model.ReportRequestWeb;
import org.openempi.webapp.client.model.ReportRequestEntryWeb;
import org.openempi.webapp.client.model.ReportWeb;
import org.openempi.webapp.client.mvc.Controller;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.extjs.gxt.ui.client.widget.Info;

public class ReportGenerateController extends Controller
{
	private ReportGenerateView reportGenerateView;

	public ReportGenerateController() {		
		this.registerEventTypes(AppEvents.ReportGenerateView);
	}

	@Override
	protected void initialize() {
		reportGenerateView = new ReportGenerateView(this);
	}

	@Override
	public void handleEvent(AppEvent event) {
		EventType type = event.getType();
		if (type == AppEvents.ReportGenerateView) {
			
			forwardToView(reportGenerateView, event);			
		} else if (type == AppEvents.ReportRequest) {
			
			requestReportData();
			
		} else if (type == AppEvents.ReportReloadRequest) {
			
			reloadReportData();	
			
		} else if (type == AppEvents.ReportRequestEntryRequest) {
			
			reloadReportRequestEntryData();
			
		} else if (type == AppEvents.ReportGenerate) {
			
			requestReportGenerate(event);
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
		    	  forwardToView(reportGenerateView, AppEvents.Logout,null);
	    		  return;
	    	  }
	    	forwardToView(reportGenerateView, AppEvents.Error, caught.getMessage());
	      }

	      public void onSuccess(List<ReportWeb> result) {
			// Info.display("Information", "onSuccess.");
	        forwardToView(reportGenerateView, AppEvents.ReportReceived, result);
	        
	        reloadReportRequestEntryData();
	      }
	    });
	}
	
	private void reloadReportData() {
		ReportDataServiceAsync reportDataService = getReportDataService();
		reportDataService.getReports(new AsyncCallback<List<ReportWeb>>() {
			
	      public void onFailure(Throwable caught) {
			// Info.display("Information", "onFailure."+caught.getMessage());
	        // Dispatcher.forwardEvent(AppEvents.Error, caught);
	    	  
	    	  if (caught instanceof AuthenticationException) {
//	    		  Dispatcher.get().dispatch(AppEvents.Logout);
		    	  forwardToView(reportGenerateView, AppEvents.Logout,null);
	    		  return;
	    	  }
	    	forwardToView(reportGenerateView, AppEvents.Error, caught.getMessage());
	      }

	      public void onSuccess(List<ReportWeb> result) {
			// Info.display("Information", "onSuccess.");
	        forwardToView(reportGenerateView, AppEvents.ReportReceived, result);
	      }
	    });
	}
	
	private void requestReportGenerate(AppEvent event) {
		ReportDataServiceAsync reportDataService = getReportDataService();
    	
		ReportRequestWeb reportRequestWeb = (ReportRequestWeb) event.getData();		

		reportDataService.generateReport(reportRequestWeb, new AsyncCallback<ReportRequestEntryWeb>() {
			
		      public void onFailure(Throwable caught) {
				// Info.display("Information", "onFailure."+caught.getMessage());
		        // Dispatcher.forwardEvent(AppEvents.Error, caught);
		    	  
		    	  if (caught instanceof AuthenticationException) {
//		    		  Dispatcher.get().dispatch(AppEvents.Logout);
			    	  forwardToView(reportGenerateView, AppEvents.Logout,null);
		    		  return;
		    	  }
			    forwardToView(reportGenerateView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(ReportRequestEntryWeb entry) {
				// Info.display("Information", "Generate Report onSuccess." + entry.getReportHandle());
		        forwardToView(reportGenerateView, AppEvents.ReportGenerateComplete, entry);
		      }
		 });
	}
	
	private void reloadReportRequestEntryData() {
		ReportDataServiceAsync reportDataService = getReportDataService();

		reportDataService.getReportRequests(new AsyncCallback<List<ReportRequestEntryWeb>>() {
			
		      public void onFailure(Throwable caught) {
				// Info.display("Information", "onFailure."+caught.getMessage());
		        // Dispatcher.forwardEvent(AppEvents.Error, caught);
		    	  
		    	  if (caught instanceof AuthenticationException) {
//		    		  Dispatcher.get().dispatch(AppEvents.Logout);
			    	  forwardToView(reportGenerateView, AppEvents.Logout,null);
		    		  return;
		    	  }
			    forwardToView(reportGenerateView, AppEvents.Error, caught.getMessage());
		      }

		      public void onSuccess(List<ReportRequestEntryWeb> result) {
				// Info.display("Information", "Get Report Requests onSuccess." + result.size());
		        forwardToView(reportGenerateView, AppEvents.ReportRequestEntryReceived, result);
		      }
		 });
	}
	
}
