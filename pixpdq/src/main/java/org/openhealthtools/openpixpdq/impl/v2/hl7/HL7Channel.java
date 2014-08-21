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
package org.openhealthtools.openpixpdq.impl.v2.hl7;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.actorconfig.net.ConnectionFactory;
import org.openhealthtools.openexchange.actorconfig.net.IConnection;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openpixpdq.common.IHL7Channel;
import org.openhealthtools.openpixpdq.impl.v2.MessageValidation;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;

/**
 * This class represents a communication channel with an HL7 server.
 * 
 * @author Jim Firby
 * @version 2.0 - Nov 20, 2005
 */
public class HL7Channel implements IHL7Channel<Message>
{
	/* Logger for problems during HL7 exchanges */
  private static final Logger log = Logger.getLogger(HL7Channel.class);

  /* The connection description for this server */
  private IConnectionDescription connection = null;
  
  /* The actual HL7 connection, if it is still open */
  private Connection hl7Connection = null;
  
  /**
   * Creates a new HL7 communication channel to the server
   * described in the connection description.
   * 
   * @param connection the connection description
   */
	public HL7Channel(IConnectionDescription connection) {
		this.connection = connection;
	}
	
	/**
	 * Sends an HL7 message over this channel to the server and
	 * return the response.
	 * 
	 * @param message the HL7 message to be sent
	 * @return the HL7 message that came back as a response
	 * @throws IOException 
	 */
	public Message sendMessage(Message message) throws IOException {
		return sendMessage(message, false);
	}
	
	/**
	 * Sends an HL7 message over this channel to the server and
	 * return the response.
	 * 
	 * @param message the HL7 message to be sent
	 * @param keepOpen True if the connection used for this message should be kept open
	 * @return the HL7 message that came back as a response
	 * @throws IOException When there is a problem communicating with the server
	 */
	public Message sendMessage(Message message, boolean keepOpen) throws IOException {
		// Create an HL7 connection
		if ((hl7Connection == null) || !hl7Connection.isOpen()) {
			// Open the appropriate TCP connection
			IConnection conn = ConnectionFactory.getConnection(connection);
			if (!conn.isConnectionValid()) {
				throw new IOException("Cannot open connection to \"" + connection.getDescription() + "\"");
			}
			// Create the HL7 connection using this TCP connection
			try {
                PipeParser parser = new PipeParser();
                parser.setValidationContext(new MessageValidation());
				hl7Connection = new Connection(parser, new MinLowerLayerProtocol(), conn.getSocket());
			} catch (LLPException e) {
				// Error in HAPI configuration
				log.error("Cannot find HL7 low level protocol implementation", e);
				conn.closeConnection();
				return null;
			} catch (IOException e) {
				// Error communicating over socket
				conn.closeConnection();
				throw e;
			}
		}
		// Get an initator to send the message
		Initiator initiator = hl7Connection.getInitiator();
		// Okay, send the message
		Message response = null;
		try {
			response = initiator.sendAndReceive(message);
		} catch (HL7Exception e) {
			// Improper HL7 message formatting (on send or receive)
            
            log.error("Improper HL7 message formatting", e);
			hl7Connection.close();
			hl7Connection = null;
			return null;
		} catch (LLPException e) {
			// Can't use Lower Level Protocol for some reason
			log.error("HL7 protocol error", e);
			hl7Connection.close();
			hl7Connection = null;
			return null;
		} catch (IOException e) {
			// Can't communicate over socket
			hl7Connection.close();
			hl7Connection = null;
			throw e;
		}
		// Close everything
		if (!keepOpen) {
			hl7Connection.close();
			hl7Connection = null;
		}
		return response;
	}
	
	/**
	 * Closes any connections being held open by this channel
	 */
	public void close() {
		if (hl7Connection != null) {
			hl7Connection.close();
			hl7Connection = null;
		}
	}
	
	/**
	 * Sees if this channel is holding open any connections.
	 * 
	 * @return True if this channel holds an open connection
	 */
	public boolean isOpen() {
		if (hl7Connection != null) return hl7Connection.isOpen();
		return false;
	}
	
}
