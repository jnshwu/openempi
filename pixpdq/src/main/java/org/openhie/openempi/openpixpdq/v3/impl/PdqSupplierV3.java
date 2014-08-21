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
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openpixpdq.api.IPdSupplier;
import org.openhealthtools.openpixpdq.common.HL7Actor;

public class PdqSupplierV3 extends HL7Actor implements IPdSupplier
{
    /* Logger for problems during SOAP exchanges */
    private static Logger log = Logger.getLogger(PdqSupplierV3.class);
    
	public PdqSupplierV3(IActorDescription actorDescription, IheAuditTrail auditTrail) throws IheConfigurationException {
		super(actorDescription, auditTrail);
	}

    @Override
    public void start() {
        super.start();        
        if(log.isInfoEnabled()) {
        	log.info("Started PDQ Supplier V3: " + this.getName() );
        }
    }

    @Override
    public void stop() {
        //call the super one to initiate standard stop process
        super.stop();

        if(log.isInfoEnabled()) {
        	log.info("Stopped PDQ Supplier V3: " + this.getName() );
        }
    }
}
