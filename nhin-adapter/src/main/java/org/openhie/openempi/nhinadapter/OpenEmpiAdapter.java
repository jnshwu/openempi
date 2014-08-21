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
package org.openhie.openempi.nhinadapter;


import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.nhinadapter.hl7.Hl7ConversionHelper;
import org.openhie.openempi.service.PersonQueryService;


@WebService(serviceName = "AdapterComponentMpiService", portName = "AdapterComponentMpiPort", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi", wsdlLocation = "META-INF/wsdl/MuralMPI/AdapterComponentMpi.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class OpenEmpiAdapter
{
	private static final Log log = LogFactory.getLog(OpenEmpiAdapter.class);
	private PersonQueryService queryService;
	private Hl7ConversionHelper hl7ConversionHelper;

    @PostConstruct
    public void initializeService() {
    	log.info("Initializing the OpenEMPI NHIN Adapter.");
		try {
			Context.startup();
			queryService = Context.getPersonQueryService();
			hl7ConversionHelper = (Hl7ConversionHelper) Context.getApplicationContext().getBean("hl7ConversionHelper");
			log.info("Completed initialization of the OpenEMPI NHIN adapter.");
		} catch (Throwable e) {
			log.error("Failed while initializing the NHIN Adapter due to: " + e, e);
		}
    }

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest) {
    	log.debug("Received findCandidates request message: " + findCandidatesRequest);
    	Person person = hl7ConversionHelper.extractQueryPerson(findCandidatesRequest);

    	Set<PersonIdentifier> identifiers = person.getPersonIdentifiers();
    	person.setPersonIdentifiers(null);
    	org.hl7.v3.PRPAIN201306UV02 response = null;
    	try {
    		List<Person> candidates = null;
    		log.debug("With allowPartionMatches set to: " + hl7ConversionHelper.isAllowPartialMatches() + " searching for person: " + person);
    		if (hl7ConversionHelper.isAllowPartialMatches()) {
    			candidates = queryService.findMatchingPersonsByAttributes(person);
    		} else {
    			candidates = queryService.findPersonsByAttributes(person);
    		}
    		filterIdentifiers(candidates, identifiers);
    		response = hl7ConversionHelper.generateResponseMessage(candidates, findCandidatesRequest, false);
    	} catch (Throwable e) {
    		log.error("Encountered a problem while processing the patient discovery request: " + e, e);
    		response = hl7ConversionHelper.generateResponseMessage(null, findCandidatesRequest, true);
    	}
        return response; 
    }

    /**
     * 
     * TODO The adapter needs to filter the returned patient identifiers based on the assigning authority used by the requesting
     * gateway. If the requesting gateway specifies an identifier for the patient in the query request, then if the patient is located
     * in the MPI using the same identifier then there is no need to return this patient back since the requesting gateway already
     * knows this patient.
     * 
     * @param findCandidatesRequest
     * @return
     */
	private void filterIdentifiers(List<Person> candidates, Set<PersonIdentifier> identifiers) {
		// TODO Auto-generated method stub
	}
}
