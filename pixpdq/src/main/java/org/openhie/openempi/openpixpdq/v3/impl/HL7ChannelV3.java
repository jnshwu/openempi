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

import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;

public abstract class HL7ChannelV3
{

	/* Logger for problems during HL7 exchanges */
	protected final Logger log = Logger.getLogger(getClass());
	
	private static final String UPDATE_ACTION = "urn:hl7-org:v3:PRPA_IN201302UV02";

	/* The connection description for this server */
	private IConnectionDescription connection = null;
	
	private String consumerUrl;

	public HL7ChannelV3(IConnectionDescription connection) {
		this.connection = connection;
		consumerUrl = this.connection.getUrlPath();
		log.debug("Set the consumerUrl to " + consumerUrl);
	}
	
	public Object sendMessage(Object request) {
		Object sender = getServiceClient(UPDATE_ACTION);
		
		return sendMessage(sender, request);
	}
	
	public abstract Object sendMessage(Object sender, Object request);

	public abstract Object getServiceClient(String action);
	
	public IConnectionDescription getConnection() {
		return connection;
	}

	public void setConnection(IConnectionDescription connection) {
		this.connection = connection;
	}

	public String getConsumerUrl() {
		return consumerUrl;
	}

	public void setConsumerUrl(String consumerUrl) {
		this.consumerUrl = consumerUrl;
	}
}
