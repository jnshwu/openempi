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

import java.util.Collection;

import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openpixpdq.api.IPixManager;
import org.openhealthtools.openpixpdq.common.Constants;
import org.openhealthtools.openpixpdq.common.HL7Actor;

/**
 * This is the Patient Identifier Cross-referencing (PIX) Manager V3 actor, 
 * the server side actor of the IHE PIX V3 profile. This actor accepts HL7 v3 messages
 * such as PRPA_IN201301UV02, PRPA_IN201302UV02 and PRPA_IN201304UV02 from a PIX Source, 
 * and PRPA_IN201309UV02 from a PIX Consumer.
 *  The transactions that this actor handles include PIX Feed, PIX Update, PIX Merge, 
 *  PIX Query and PIX Update Notification.   
 *
 * @author Wenzhi Li
 * @see IPixManager
 */
public class PixManagerV3 extends HL7Actor implements IPixManager
{
    /* Logger for problems during SOAP exchanges */
    private static Logger log = Logger.getLogger(PixManagerV3.class);

	/** The XDS Registry Connection */
	private IConnectionDescription xdsRegistryConnection = null;
    /* The connections for PIX Consumers that subscribe to the PIX Update Notification*/
    private Collection<IConnectionDescription> pixConsumerConnections = null;

   /**
    * Creates a new PixManagerV3 that will talk to a PIX client over
    * the actor description supplied.
    *
    * @param actor The actor description of this PIX manager
    * @param auditTrail The audit trail for this PIX Manager
    * @throws IheConfigurationException
    */
    public PixManagerV3(IActorDescription actor, IheAuditTrail auditTrail) 
    	    throws IheConfigurationException {
        super(actor, auditTrail);
		this.xdsRegistryConnection = Configuration.getConnection(actor, Constants.XDS_REGISTRY, false);
		this.pixConsumerConnections = Configuration.getConnections(actor, Constants.PIX_CONSUMER, false);
		log.debug("The PIX Consumer Connections have been loaded.");
    }

    @Override
    public void start() {
        //call the super one to initiate standard start process
        super.start();        
        if(log.isInfoEnabled()) {
        	log.info("Started PIX Manager V3: " + this.getName() );
        }
    }

    @Override
    public void stop() {
        //call the super one to initiate standard stop process
        super.stop();

        if(log.isInfoEnabled()) {
        	log.info("Stopped PIX Manager V3: " + this.getName() );
        }
    }
    
	@Override
	public IConnectionDescription getXdsRegistryConnection() {
		return xdsRegistryConnection;
	}

	@Override
    public Collection<IConnectionDescription> getPixConsumerConnections() {
		return pixConsumerConnections;
	}

}

