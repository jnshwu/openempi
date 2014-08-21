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

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

public class OpenempiAdapterComponentProxy implements AdapterComponentMpiProxy
{
	private static Service cachedService = null;
	private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi";
	private static final String SERVICE_LOCAL_PART = "AdapterComponentMpiService";
	private static final String PORT_LOCAL_PART = "AdapterComponentMpiPort";
	private static final String WSDL_FILE = "AdapterComponentMpi.wsdl";
	private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi:FindCandidatesRequest";
	private Log log = null;
	private WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();

	public OpenempiAdapterComponentProxy() {
		log = createLogger();
	}

	@Override
	public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 request, AssertionType assertion) {
		String url = null;
		PRPAIN201306UV02 response = new PRPAIN201306UV02();
		String serviceName = NhincConstants.ADAPTER_COMPONENT_MPI_SERVICE_NAME;
		
		try {
			if (request != null) {
				log.debug("Before target system URL look up.");
				url = proxyHelper.getUrlLocalHomeCommunity(serviceName);
				log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
				
				if (NullChecker.isNotNullish(url)) {
					AdapterComponentMpiPortType port = getPort(url, NhincConstants.ADAPTER_MPI_ACTION,
							WS_ADDRESSING_ACTION, assertion);
					response = (PRPAIN201306UV02) proxyHelper.invokePort(port, AdapterComponentMpiPortType.class,
							"findCandidates", request);
				} else {
					log.error("Failed to call the web service (" + serviceName + "). The URL is null.");
				}
			} else {
				log.error("Failed to call the web service (" + serviceName + "). The request parameter is null.");
			}
		} catch (Exception e) {
			log.error("Failed to call the web service (" + serviceName + "). An unexpected exception occurred: " +
					e.getMessage(), e);
		}
		return response;
	}

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected AdapterComponentMpiPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        AdapterComponentMpiPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterComponentMpiPortType.class);
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = proxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

	protected Log createLogger() {
		return ((log != null) ? log : LogFactory.getLog(getClass()));
	}
}
