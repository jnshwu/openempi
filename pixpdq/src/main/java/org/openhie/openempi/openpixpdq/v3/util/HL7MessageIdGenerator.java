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
package org.openhie.openempi.openpixpdq.v3.util;
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7MessageIdGenerator
{
    private static Log log = LogFactory.getLog(HL7MessageIdGenerator.class);
    
    public static II GenerateHL7MessageId(String myDeviceId) {
        II messageId = new II();
        
        if (Utilities.isNullish(myDeviceId)) {
            myDeviceId = getDefaultLocalDeviceId();
        }
        
        log.debug("Using local device id " + myDeviceId);
        messageId.setRoot(myDeviceId);
        messageId.setExtension(GenerateMessageId());
        return messageId;
    }
    
    public static II GenerateHL7MessageId() {
        II messageId = new II();
        messageId.setRoot(getDefaultLocalDeviceId());
        messageId.setExtension(GenerateMessageId());
        return messageId;
    }

    private static String getDefaultLocalDeviceId() {
        return HL7Constants.DEFAULT_LOCAL_DEVICE_ID;
    }
    
    public static String GenerateMessageId() {
    	java.util.UUID uuid = java.util.UUID.randomUUID();
        log.debug("generated message id=" + uuid.toString());
        return uuid.toString();
    }
}

