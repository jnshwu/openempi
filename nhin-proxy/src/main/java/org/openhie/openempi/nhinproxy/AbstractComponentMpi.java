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
package org.openhie.openempi.nhinproxy;

import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType;
import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiService;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AbstractComponentMpi
{
	protected static final String OPENEMPI_SERVICE = "openempiService";
	
	private static AdapterComponentMpiService adapterComponentMpiService;
	protected Log log;

	public AbstractComponentMpi() {
        log = createLogger();
	}

	protected String getEndpointURL() {
	    String endpointURL = null;
	    String serviceName = OPENEMPI_SERVICE;
	    try {
	        endpointURL = invokeConnectionManager(serviceName);
	        log.debug("Retrieved endpoint URL for service " + serviceName + ": " + endpointURL);
	    } catch (ConnectionManagerException ex) {
	        log.error("Error getting url for " + serviceName + " from the connection manager. Error: " + ex.getMessage(), ex);
	    }
	    return endpointURL;
	}

	protected AdapterComponentMpiService getAdapterComponentMpiService() {
	    if (adapterComponentMpiService == null) {
	    	adapterComponentMpiService = new AdapterComponentMpiService();
	    }
	    return adapterComponentMpiService;
	}

	protected AdapterComponentMpiPortType getAdapterComponentMpiPortType() {
	
		AdapterComponentMpiPortType port = null;
	
	    String endpointURL = getEndpointURL();
	
	    if ((endpointURL != null) && (!endpointURL.isEmpty())) {
	    	AdapterComponentMpiService service = getAdapterComponentMpiService();
	        if (service != null) {
	            port = service.getAdapterComponentMpiPort();
	            configurePort(port, endpointURL);
	        } else {
	            log.warn("AdapterComponentMpiService was null");
	        }
	    } else {
	        log.warn("Endpoint url was missing.");
	    }
	    return port;
	}

	@SuppressWarnings("unchecked")
	protected void configurePort(AdapterComponentMpiPortType port, String endpointURL) {
		log.debug("Begin configurePort");
	    if (port == null) {
	        log.warn("configurePort - Port was null.");
	    } else if (endpointURL == null) {
	        log.warn("configurePort - Endpoint URL was null.");
	    } else {
	        Map requestContext = ((BindingProvider) port).getRequestContext();
	        requestContext.put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
	    }
	    log.debug("End configurePort");
	}

	protected String invokeConnectionManager(String serviceName)
			throws ConnectionManagerException {
			    return ConnectionManagerCache.getLocalEndpointURLByServiceName(serviceName);
			}

	protected Log createLogger() {
	    return LogFactory.getLog(getClass());
	}

}