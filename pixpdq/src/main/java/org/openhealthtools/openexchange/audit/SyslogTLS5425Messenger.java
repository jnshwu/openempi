/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
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

package org.openhealthtools.openexchange.audit;

import java.io.OutputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.net.ConnectionFactory;
import org.openhealthtools.openexchange.actorconfig.net.IConnection;


/** 
 * Sends the message to Syslog logger using TCP/TLS protocol (RFC 5425).
 * 
 * @author <a href="mailto:anilkumar.reddy@misys.com">Anil Kumar</a>
 */
class SyslogTLS5425Messenger implements IMessageTransmitter {
	
	/**
	 * Logger instance
	 */
	private static final Log log = LogFactory.getLog(SyslogTLS5425Messenger.class);
	private static final int FACILITY = 10; // CP ATNA_157, using value of 10(security/authorization messages) 
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); //Syslog date format.

	private Severity defaultSeverity = Severity.Notice;
	
	private String localHostName;
	private String messageHostName;
	private String applicationName;
	private AuditTrailDescription desc;
	
	/**
	 * Thread that asynchronously handles delivery of messages 
	 * to the sender mechanism
	 */
	private AuditQueue auditQueue; 

	private void init() {
		auditQueue = AuditQueue.getInstance(this);
	}
	
	/** 
	 * Use to create a "connection" to a TLS Syslog audit repository.
	 * 
	 */
	public SyslogTLS5425Messenger(AuditTrailDescription description) {
		try {
			messageHostName = description.getHost();
			localHostName = description.getName();
			applicationName = description.getApplicationName();
			desc = description;			
		} catch (Exception e) {
			log.error("Problem resolving host name for UDP logging ATNA messeger.", e);
		}
		init();
	}
	
	/** 
	 * Sends syslog message using a given facility and level. 
	 */
	public void sendMessage(String message, Severity severity) {
		Date now = new Date();
		int PRI = (FACILITY * 8) + severity.value();
		//byte[] utf8_bom = "\uFEFF".getBytes("UTF-8");
		
		//strip away xml header
		String xmlheader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
		//message = message.substring(xmlheader.length());
		
		String SyslogVersion = "1";

		String bom = "\uFEFF";
		
        String date = formatter.format(now);
        // Append colon to timezone info (required for RFC 5424)
        date = date.substring(0, date.length() - 2) + ":" + date.substring(date.length() - 2);
        
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(PRI);
        sb.append(">");
        sb.append(SyslogVersion);
        sb.append(" ");
        sb.append(date);
        sb.append(" ");
        sb.append(localHostName);
        sb.append(" ");
        sb.append(applicationName);
        sb.append(" - IHE+RFC-3881 - ");
        //ITI-CP477 discourages to use bom for UTF-8 encoding
        //sb.append(bom);
        sb.append(message);
        String completeMessage = sb.toString(); 
     
        if (auditQueue != null && desc.isAsynchronous())
        	//send in async mode
        	auditQueue.addToQueue( completeMessage );
        else 
        	//send in synchronous mode 
        	transmitMessages(new String[]{completeMessage});
	}

	/** 
	 * Sends syslog message using the default facility and level. 
	 */
	public void sendMessage(String message) {
		sendMessage(message, defaultSeverity);
	}

	/** 
	 * Sets the severity to be used for non specific messages. 
	 */
	public void setDefaultSeverity(Severity severity) {
		defaultSeverity = severity;
	}

	/**
	 * Gets the audit trail description
	 */
	public AuditTrailDescription getAuditTrailDescription() {
		return desc;
	}
	
	/**
	 * Transmits the messages over the wires to the audit record repository specified 
	 * by the audit trail description.
	 * 
	 * @param msgs the messages to transmit
	 */
	public void transmitMessages(String[] messages) {
		try {
			IConnection conn = ConnectionFactory.getConnection(desc.getConnectionDescription());
            OutputStream os = conn.getSocket().getOutputStream();

			//send out each message
			for (int i=0; i<messages.length; i++) {
				if (messages[i] == null) continue;
				
				byte[] messageBytes = messages[i].getBytes("UTF-8");			
				String completemsg = messageBytes.length + " " + messages[i];			
	            os.write(completemsg.getBytes("UTF-8"));
			}
			os.flush();
			os.close();
            conn.closeConnection();
		}catch(Exception e) {
			log.error("Problem sending TCP/TLS syslog message - " + e.getMessage(), e);
		}
	}
 

	public void flush() {
		log.warn("Flushing audit queue");
		auditQueue.interrupt();
	}
	
}