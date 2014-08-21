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

import javax.ws.rs.core.MediaType;

import org.openempi.webservices.restful.model.AuthenticationRequest;

import com.sun.jersey.api.client.ClientResponse;

public class SecurityResourceTest extends BaseRestfulServiceTestCase
{
	public void testAuthenticationSuccess() {
		AuthenticationRequest request = new AuthenticationRequest("admin", "admin");
		String sessionKey = getWebResource().path("security-resource")
    			.path("authenticate")
    			.accept(MediaType.APPLICATION_XML)
    			.accept(MediaType.APPLICATION_JSON)
    			.put(String.class, request);
		assertNotNull("Unable to authenticate with the server: " + sessionKey, sessionKey);
	}
	
	public void testAuthenticationInvalidCredentials() {
		AuthenticationRequest request = new AuthenticationRequest("admin", "xxxxxx");
		ClientResponse response = getWebResource().path("security-resource")
    			.path("authenticate")
    			.accept(MediaType.APPLICATION_XML)
    			.accept(MediaType.APPLICATION_JSON)
    			.put(ClientResponse.class, request);
		assertTrue("Should receive an unauthorized error: " + response.getClientResponseStatus(), response.getStatus() == 401);
	}
	
	public void testAuthenticationInvalidRequest() {
		AuthenticationRequest request = new AuthenticationRequest();
		ClientResponse response = getWebResource().path("security-resource")
    			.path("authenticate")
    			.accept(MediaType.APPLICATION_XML)
    			.accept(MediaType.APPLICATION_JSON)
    			.put(ClientResponse.class, request);
		assertTrue("Should receive an unauthorized error: " + response.getClientResponseStatus(), response.getStatus() == 401);
	}	
}
