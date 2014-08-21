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
package org.openempi.webservices.restful.resources;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openempi.webservices.restful.model.AuthenticationRequest;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.context.Context;

@Path("/security-resource")
public class SecurityResource
{
	@PUT
	@Path("/authenticate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String authenticate(AuthenticationRequest authRequest) {
		String sessionKey = null;
		try {
			sessionKey = Context.authenticate(authRequest.getUsername(), authRequest.getPassword());
		} catch (AuthenticationException e) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		return sessionKey;
	}
}
