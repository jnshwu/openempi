/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */
package org.openhealthtools.openpixpdq.common;

import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.IheActor;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openexchange.log.IMesaLogger;
import org.openhealthtools.openpixpdq.api.IMessageStoreLogger;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.PipeParser;

/**
 * This class implements a number of useful methods shared by all
 * IHE Actors.
 * 
 * @author Jim Firby
 * @version 1.0 - Nov 20, 2005
 */
public abstract class HL7Actor implements IheActor {
	
	
	/* The logger used for capturing MESA test output */
	private IMesaLogger mesaLogger = null;
	
	/*The logger used to persist(store) raw inbound and outbound messages.*/
	private IMessageStoreLogger storeLogger = null;

	/**The actor description of this actor*/
	protected IActorDescription actorDescription = null;
	
	/* The IHE Audit Trail for this actor. */
	protected IheAuditTrail auditTrail = null;
 
  
	/**
	 * Creates a new HL7 Actor
	 * 
	 */
	public HL7Actor(IActorDescription actorDescription, IheAuditTrail auditTrail) 
	throws IheConfigurationException{
		this.actorDescription = actorDescription;
		this.auditTrail = auditTrail;
	}
  
	/**
	 *  Starts this actor. It must be called once for each actor when the program starts. 
	 */  
	public void start() {
		if (auditTrail != null) auditTrail.start();  
	}

	/**
	 * Stops this actor. It must be called once for each actor just before the program quits. 
	 */ 
	public void stop() {
		if (auditTrail != null) auditTrail.stop();  	  
	}

	/**
	 * Returns the actor description for this Actor. 
	 * 
	 * @returns the actor description for this Actor
	 */
	public IActorDescription getActorDescription() {
		return this.actorDescription;
	}

	/**
	 * Returns a useful name for this Actor so that it can be put into
	 * debugging and logging messages.
	 * 
	 * @returns a useful name for this Actor
	 */
	public String getName() {
		return this.actorDescription.getName();
	}

	/**
	 * Logs an HL7 message and description to the error log.
	 * 
	 * @param message the HL7 message
	 * @param description a description of the problem with the message
	 */
	public void logHL7MessageError(Logger log, Message message, String description) {
		if (mesaLogger != null) {
			// Just log the description
			mesaLogger.writeString(description);
		} else {
			// Log the description and the HL7 message itself
			log.error(description);
			try {
				log.error(PipeParser.encode(message, new EncodingCharacters('|', "^~\\&")));
			} catch (HL7Exception e) {
				log.error("Error reporting HL7 error", e);
			}
		}
	}

	/**
	 * Checks whether this actor has a MESA test logger.
	 * 
	 * @return True if there is a defined MESA test logger.
	 */
  boolean hasMesaLogger() {
  	return (mesaLogger != null);
  }
  
  /**
   * Gets the MESA test logger.
   * 
   * @return The MESA test logger.
   */
  public IMesaLogger getMesaLogger() {
  	return mesaLogger;
  }
  
	/**
	 * Sets the custom logger for MESA test messages.  This is
	 * only used by MESA testing programs.
	 * 
	 * @param stream The logger for MESA messages
	 */
	public void setMesaLogger(IMesaLogger logger) {
		mesaLogger = logger;
	}


   /**
    * Gets the Message Store logger.
    *  
    * @return The Message Store logger.
    */
	public IMessageStoreLogger getStoreLogger() {
		return storeLogger;
	}

	/**
	 * Sets the logger to store message. 
	 * 
	 * @param storeLogger the storeLogger to set
	 */
	public void setStoreLogger(IMessageStoreLogger storeLogger) {
		this.storeLogger = storeLogger;
	}

	
	/**
	 * Gets the Audit Trail of this actor.
	 * 
	 * @return the auditTrail
	 */
	public IheAuditTrail getAuditTrail() {
		return auditTrail;
	}
	
}
