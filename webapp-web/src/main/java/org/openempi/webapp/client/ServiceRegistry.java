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

import org.openempi.webapp.client.SampleRemoteService;
import org.openempi.webapp.client.SampleRemoteServiceAsync;
import org.openempi.webapp.client.SecurityService;
import org.openempi.webapp.client.SecurityServiceAsync;
import org.openempi.webapp.client.TestService;
import org.openempi.webapp.client.TestServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * 
 * Make and hold a singleton (per app user) reference to service proxy.
 * Pass this to every screen
 *
 */
public class ServiceRegistry {
	//proxies to our services
	private TestServiceAsync testService;
	private SampleRemoteServiceAsync sampleRemoteService;
	private SecurityServiceAsync securityService;

	
	public TestServiceAsync getTestService() {
		if(testService == null) {
			testService = GWT.create(TestService.class);
			((ServiceDefTarget)testService).setServiceEntryPoint(GWT.getModuleBaseURL() + "testService");
		}
		return testService;
	}
	
	public SampleRemoteServiceAsync getSampleRemoteService() {
		if(sampleRemoteService == null) {
			sampleRemoteService = GWT.create(SampleRemoteService.class);
			((ServiceDefTarget)sampleRemoteService).setServiceEntryPoint(GWT.getModuleBaseURL() + "sampleRemoteService");
		}
		return sampleRemoteService;
	}
	
	public SecurityServiceAsync getSecurityService() {
		if(securityService == null) {
			securityService = GWT.create(SecurityService.class);
			((ServiceDefTarget)securityService).setServiceEntryPoint(GWT.getModuleBaseURL() + "securityService");
		}
		return securityService;
	}
	
}
