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
package gov.hhs.fha.nhinc.adaptercomponentmpi;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;


@WebService(name = "AdapterComponentMpiPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    org.xmlsoap.schemas.ws._2004._08.addressing.ObjectFactory.class,
    gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory.class,
    org.hl7.v3.ObjectFactory.class
})
public interface AdapterComponentMpiPortType
{
    /**
     * 
     * @param findCandidatesRequest
     * @return
     *     returns org.hl7.v3.PRPAIN201306UV02
     */
    @WebMethod(operationName = "FindCandidates", action = "FindCandidates")
    @WebResult(name = "PRPA_IN201306UV02", targetNamespace = "urn:hl7-org:v3", partName = "FindCandidatesResponse")
    public PRPAIN201306UV02 findCandidates(
        @WebParam(name = "PRPA_IN201305UV02", targetNamespace = "urn:hl7-org:v3", partName = "FindCandidatesRequest")
        PRPAIN201305UV02 findCandidatesRequest);

}
