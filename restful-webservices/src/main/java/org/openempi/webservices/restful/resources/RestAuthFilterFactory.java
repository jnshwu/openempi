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

import java.util.Collections;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.model.User;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class RestAuthFilterFactory implements ResourceFilterFactory
{
	private static final String OPENEMPI_SESSION_KEY_HEADER = "OPENEMPI_SESSION_KEY";
	protected final Log log = LogFactory.getLog(getClass());

	@Context
	private UriInfo uriInfo;

	@Override
	public List<ResourceFilter> create(AbstractMethod method) {
		return Collections.singletonList((ResourceFilter) new Filter());
	}

	private class Filter implements ResourceFilter, ContainerRequestFilter {

		protected Filter() {
		}

		public ContainerRequestFilter getRequestFilter() {
			return this;
		}

		public ContainerResponseFilter getResponseFilter() {
			return null;
		}

		public ContainerRequest filter(ContainerRequest request) {
			log.debug("Url invoked is: " + uriInfo.getPath());
			
			// We need to let the caller get through to the authentication call
			if (uriInfo.getPath() != null && 
					(uriInfo.getPath().equalsIgnoreCase("security-resource/authenticate") ||
					 uriInfo.getPath().equalsIgnoreCase("application.wadl"))) {
				log.debug("Request permitted due to URI being present in the exclusion list.");
				return request;
			}

			String sessionKey = request.getHeaderValue(OPENEMPI_SESSION_KEY_HEADER);
			if (sessionKey != null && sessionKey.length() > 0) {
				User user = org.openhie.openempi.context.Context.authenticate(sessionKey);
				if (user == null) {
					throw new WebApplicationException(Response.Status.UNAUTHORIZED);
				}
				return request;
			}
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
	}
}
