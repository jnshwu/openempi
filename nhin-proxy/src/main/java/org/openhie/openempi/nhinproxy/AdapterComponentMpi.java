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


import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;


@WebService(serviceName = "AdapterComponentMpiService", portName = "AdapterComponentMpiPort", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi", wsdlLocation = "META-INF/wsdl/AdapterComponentMpi/AdapterComponentMpi.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless(name = "OpenEMPI")
public class AdapterComponentMpi extends AbstractComponentMpi
{
	public AdapterComponentMpi() {
		super();
	}
	
    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest) {
    	log.debug("Received a findCandidates request message: " + findCandidatesRequest);
        org.hl7.v3.PRPAIN201306UV02 response = null;
        try {
        	AdapterComponentMpiPortType port = getAdapterComponentMpiPortType();
        	
            if (findCandidatesRequest == null) {
            	log.error("The request message was null");
            } else if (port == null) {
            	log.error("Unable to obtain a proxy to the actual service.");
            } else {
                response = port.findCandidates(findCandidatesRequest);
            }
        } catch (Exception ex) {
            log.error("Error calling findCandidates: " + ex.getMessage(), ex);
        }
        log.debug("Returning response message: " + response);
        return response;
    }

}
