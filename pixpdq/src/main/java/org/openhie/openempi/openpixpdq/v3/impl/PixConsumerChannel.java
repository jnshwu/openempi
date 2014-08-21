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
package org.openhie.openempi.openpixpdq.v3.impl;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.ws.addressing.AddressingBuilder;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201302UV02;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhie.openempi.openpixpdq.v3.PIXConsumerPortType;
import org.openhie.openempi.openpixpdq.v3.PIXConsumerService;
import org.openhie.openempi.openpixpdq.v3.util.Utilities;

public class PixConsumerChannel extends HL7ChannelV3
{	
	public PixConsumerChannel(IConnectionDescription connection) {
		super(connection);
	}

	@Override
	public Object sendMessage(Object sender, Object request) {
		PRPAIN201302UV02 message = (PRPAIN201302UV02) request;
		PIXConsumerPortType port = (PIXConsumerPortType) sender;
		MCCIIN000002UV01 response = port.pixConsumerPRPAIN201302UV02(message);
		String responseString = Utilities.objectMessageToString(response);
		log.info("In response to sending an update notification, got the response: " + responseString);
//		clearWSAddressingAfterCall(port);
		return response;
	}

	public PIXConsumerPortType getServiceClient(String action) {

//		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
//		factory.setServiceClass(PIXConsumerPortType.class);
//		factory.setAddress("http://acme.come/some-service");
//		factory.getFeatures().add(new WSAddressingFeature());
//		PIXConsumerService service = (PIXConsumerService) factory.create();
//		PIXConsumerPortType port = (PIXConsumerPortType) factory.create();
		PIXConsumerService service = new PIXConsumerService();
		service.addPort(PIXConsumerService.PIXConsumerPortSoap12, SOAPBinding.SOAP12HTTP_BINDING, getConsumerUrl());
		AddressingFeature addressing = new AddressingFeature();
		PIXConsumerPortType port = service.getPIXConsumerPortSoap12(addressing);
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getConsumerUrl());
//        setupWSAddressingForCall(bp);        
		return port;
	}

	private void setupWSAddressingForCall(BindingProvider bp) {
		// get Message Addressing Properties instance
        AddressingBuilder builder = AddressingBuilder.getAddressingBuilder();
        AddressingProperties maps = builder.newAddressingProperties();

        // set MessageID property
        AttributedURIType messageID = new AttributedURIType();
        messageID.setValue("urn:uuid:" + System.currentTimeMillis());
        maps.setMessageID(messageID);

        // associate MAPs with request context
        bp.getRequestContext().put(org.apache.cxf.ws.addressing.JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES, maps);
	}

	private void clearWSAddressingAfterCall(PIXConsumerPortType port) {
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().remove(org.apache.cxf.ws.addressing.JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES);
	}
}
