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

import javax.jws.WebService;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201302UV02;
import org.openhie.openempi.openpixpdq.v3.PIXConsumerPortType;
import org.openhie.openempi.openpixpdq.v3.util.HL7AckTransforms;


@WebService(endpointInterface = "org.openhie.openempi.openpixpdq.v3.PIXConsumerPortType")
public class PixConsumerServiceImpl extends AbstractIheService implements PIXConsumerPortType
{
	public void init() {
		super.init();
		log.info("Initializing the service.");
	}
	
	@Override
	public MCCIIN000002UV01 pixConsumerPRPAIN201302UV02(PRPAIN201302UV02 message) {
		log.info("Received the update notification message: " + message);
		Hl7HeaderAddress addr = extractHeaderAddress(message.getReceiver(), message.getSender());
		MCCIIN000002UV01 ack = HL7AckTransforms.createAckMessage(message.getId(), "", addr.getSenderFacility(), addr.getSenderApplication(),
				addr.getReceiverFacility(), addr.getReceiverApplication(), null);
		return ack;
	}

}
