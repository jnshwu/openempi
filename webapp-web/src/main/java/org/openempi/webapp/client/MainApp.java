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
package org.openempi.webapp.client;

import org.openempi.webapp.client.mvc.AppController;
import org.openempi.webapp.client.mvc.MenuToolbarController;
import org.openempi.webapp.client.mvc.admin.AdminController;
import org.openempi.webapp.client.mvc.blocking.BlockingConfigurationController;
import org.openempi.webapp.client.mvc.blocking.SortedNeighborhoodBlockingConfigurationController;
import org.openempi.webapp.client.mvc.blocking.SuffixArrayBlockingConfigurationController;
import org.openempi.webapp.client.mvc.configuration.CustomFieldsConfigurationController;
import org.openempi.webapp.client.mvc.configuration.MatchConfigurationController;
import org.openempi.webapp.client.mvc.configuration.DeterministicMatchConfigurationController;
import org.openempi.webapp.client.mvc.manage.ManageIdentifierDomainController;
import org.openempi.webapp.client.mvc.edit.EditController;
import org.openempi.webapp.client.mvc.fileloader.FileLoaderConfigurationController;
import org.openempi.webapp.client.mvc.notification.EventNotificationController;
import org.openempi.webapp.client.mvc.process.ProcessLinkController;
import org.openempi.webapp.client.mvc.search.BasicSearchController;
import org.openempi.webapp.client.mvc.user.MatchController;
import org.openempi.webapp.client.mvc.user.UserFileController;
import org.openempi.webapp.client.mvc.report.ReportDesignController;
import org.openempi.webapp.client.mvc.report.ReportGenerateController;
import org.openempi.webapp.client.mvc.security.ProfileController;
import org.openempi.webapp.client.mvc.security.ManageUserController;
import org.openempi.webapp.client.mvc.security.ManageRoleController;
import org.openempi.webapp.client.mvc.dataprofile.DataProfileController;
import org.openempi.webapp.client.mvc.event.AuditEventController;
import org.openempi.webapp.client.mvc.event.MessageLogController;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.Theme;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class MainApp implements EntryPoint
{
	public void onModuleLoad() {
		GXT.setDefaultTheme(Theme.GRAY, true);
		
		AdminServiceAsync adminService = (AdminServiceAsync) GWT.create(AdminService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) adminService;
		String moduleRelativeURL = Constants.ADMIN_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.ADMIN_SERVICE, adminService);
		
		BlockingDataServiceAsync blockingDataService = (BlockingDataServiceAsync) GWT.create(BlockingDataService.class);
		endpoint = (ServiceDefTarget) blockingDataService;
		moduleRelativeURL = Constants.BLOCKING_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.BLOCKING_DATA_SERVICE, blockingDataService);
		
		ConfigurationDataServiceAsync configuartionDataService = (ConfigurationDataServiceAsync) GWT.create(ConfigurationDataService.class);
		endpoint = (ServiceDefTarget) configuartionDataService;
		moduleRelativeURL = Constants.CONFIGURATION_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.CONFIGURATION_DATA_SERVICE, configuartionDataService);
		
		EventNotificationServiceAsync eventNotificationService = (EventNotificationServiceAsync) GWT.create(EventNotificationService.class);
		endpoint = (ServiceDefTarget) eventNotificationService;
		moduleRelativeURL = Constants.EVENT_NOTIFICATION_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.EVENT_NOTIFICATION_SERVICE, eventNotificationService);
		
		FileLoaderDataServiceAsync fileLoaderDataService = (FileLoaderDataServiceAsync) GWT.create(FileLoaderDataService.class);
		endpoint = (ServiceDefTarget) fileLoaderDataService;
		moduleRelativeURL = Constants.FILE_LOADER_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.FILE_LOADER_DATA_SERVICE, fileLoaderDataService);

		PersonDataServiceAsync personDataService = (PersonDataServiceAsync) GWT.create(PersonDataService.class);
		endpoint = (ServiceDefTarget) personDataService;
		moduleRelativeURL = Constants.PERSON_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.PERSON_DATA_SERVICE, personDataService);

		ReportDataServiceAsync reportDataService = (ReportDataServiceAsync) GWT.create(ReportDataService.class);
		endpoint = (ServiceDefTarget) reportDataService;
		moduleRelativeURL = Constants.REPORT_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.REPORT_DATA_SERVICE, reportDataService);

		ProfileDataServiceAsync profileDataService = (ProfileDataServiceAsync) GWT.create(ProfileDataService.class);
		endpoint = (ServiceDefTarget) profileDataService;
		moduleRelativeURL = Constants.PROFILE_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.PROFILE_DATA_SERVICE, profileDataService);

		AuditEventDataServiceAsync auditEventDataService = (AuditEventDataServiceAsync) GWT.create(AuditEventDataService.class);
		endpoint = (ServiceDefTarget) auditEventDataService;
		moduleRelativeURL = Constants.AUDIT_EVENT_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.AUDIT_EVENT_SERVICE, auditEventDataService);
		
		UserDataServiceAsync userDataService = (UserDataServiceAsync) GWT.create(UserDataService.class);
		endpoint = (ServiceDefTarget) userDataService;
		moduleRelativeURL = Constants.USER_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.USER_DATA_SERVICE, userDataService);
		
		ReferenceDataServiceAsync refDataService = (ReferenceDataServiceAsync) GWT.create(ReferenceDataService.class);
		endpoint = (ServiceDefTarget) refDataService;
		moduleRelativeURL = Constants.REF_DATA_SERVICE;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + moduleRelativeURL);
		Registry.register(Constants.REF_DATA_SERVICE, refDataService);
		
		Dispatcher dispatcher = Dispatcher.get();
		dispatcher.addController(new AppController());
		dispatcher.addController(new AdminController());
		dispatcher.addController(new BasicSearchController());
		dispatcher.addController(new ProcessLinkController());
		dispatcher.addController(new EditController());
		dispatcher.addController(new MenuToolbarController());
		dispatcher.addController(new UserFileController());
		dispatcher.addController(new BlockingConfigurationController());
		dispatcher.addController(new SortedNeighborhoodBlockingConfigurationController());
		dispatcher.addController(new SuffixArrayBlockingConfigurationController());
		dispatcher.addController(new FileLoaderConfigurationController());
		dispatcher.addController(new CustomFieldsConfigurationController());
		dispatcher.addController(new MatchConfigurationController());
		dispatcher.addController(new DeterministicMatchConfigurationController());
		dispatcher.addController(new ManageIdentifierDomainController());
		dispatcher.addController(new MatchController());
		dispatcher.addController(new EventNotificationController());
		dispatcher.addController(new ReportDesignController());
		dispatcher.addController(new ReportGenerateController());
		dispatcher.addController(new ProfileController());
		dispatcher.addController(new ManageUserController());
		dispatcher.addController(new ManageRoleController());
		dispatcher.addController(new DataProfileController());
		dispatcher.addController(new AuditEventController());		
		dispatcher.addController(new MessageLogController());		
		dispatcher.dispatch(AppEvents.Login);

		GXT.hideLoadingPanel("loading");
	}

}
