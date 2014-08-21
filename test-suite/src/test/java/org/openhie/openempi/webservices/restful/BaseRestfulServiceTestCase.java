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
package org.openhie.openempi.webservices.restful;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.openempi.webapp.server.BaseServiceTestCase;
import org.openempi.webservices.restful.model.AuthenticationRequest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class BaseRestfulServiceTestCase extends BaseServiceTestCase
{
	protected static final String OPENEMPI_SESSION_KEY_HEADER = "OPENEMPI_SESSION_KEY";

	private Client client;
	private WebResource resource;
	private String sessionKey;
	
	@Override
	protected void onSetUp() throws Exception {
		client = Client.create();
		resource = client.resource(getBaseURI(null));
		AuthenticationRequest request = new AuthenticationRequest("admin", "admin");
		sessionKey = resource.path("security-resource")
			.path("authenticate")
			.accept(MediaType.APPLICATION_JSON)
			.put(String.class, request);
		log.debug("Obtained a session key of : " + sessionKey);
	}
    
	@Override
	protected void onTearDown() throws Exception {
		System.out.println("Before tearDown Application context is: " + getApplicationContext());
	}
	
    protected static URI getBaseURI(String resourceName) {
    	String uri = "http://localhost/openempi-admin/openempi-ws-rest/";
    	if (resourceName != null && resourceName.length() > 0) {
    		uri = uri + resourceName;
    	}
        return UriBuilder.fromUri(uri).port(8080).build();
    }
    
	protected WebResource getWebResource() {
		return resource;
	}
    
	protected Client getClient() {
		return client;
	}
    
	protected String getSessionKey() {
		return sessionKey;
	}
}
