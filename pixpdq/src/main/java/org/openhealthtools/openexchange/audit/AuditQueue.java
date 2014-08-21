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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The audit queue for sending audit trail messages asynchronously. An 
 * audit queue is created for each audit record repository that requires queue
 * functionality. 
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
class AuditQueue extends Thread {
	/* The common log*/
	private final static Log log = LogFactory.getLog(AuditQueue.class); 
	/* The queue for storing the audit messages */
	private final List<String> queue = Collections.synchronizedList(new ArrayList<String>());
	/* The Message Transmitter */
	IMessageTransmitter messenger;
    /* The audit trail (i.e. audit repository that this queue is associated with */
	AuditTrailDescription auditTrail;
    /* An indicator whether to shut down this queue thread*/	
	private boolean shutdown = false;
	
	/* A map of audit repository name and its assigned audit queue. 
	 * Structure: Map<String(audit repository name (i.e. connection name)), AuditQueue> */
	private static Map<String, AuditQueue> queueMap = Collections.synchronizedMap(new HashMap<String, AuditQueue>());
	
	/**
	 * Factory method to get an instance of AuditQueue.
	 * 
	 * @param auditTrail
	 * @return an AuditQueue instance
	 */
    public static synchronized AuditQueue getInstance(IMessageTransmitter messenger) {
    	
    	AuditTrailDescription auditTrail = messenger.getAuditTrailDescription();
    	if (auditTrail == null || auditTrail.getConnectionDescription() == null)
    		return null;
    	
    	String connectionName = auditTrail.getConnectionDescription().getName();
    	if ( connectionName == null)
    		return null;
    	
		if (queueMap.containsKey( connectionName.toLowerCase() ) ) {
			return queueMap.get(connectionName.toLowerCase());
		}
		else {
			//create a new auditQueue
			AuditQueue auditQueue = new AuditQueue(messenger);
			auditQueue.start();
			//register shutdown hook
			Runtime.getRuntime().addShutdownHook(new Thread(auditQueue) {
				private AuditQueue auditQueue;
				
				public void run() {
					auditQueue.shutdown();
				}
			});			
			//Add the queue to the map
			queueMap.put(connectionName.toLowerCase(), auditQueue);
			return auditQueue;
		}
    }
    
    /**
     * private constructor
     * 
     * @param messenger the message transmitter
     */
    private AuditQueue(IMessageTransmitter messenger) {
    	this.messenger = messenger;
    }

    /**
     * Adds an audit message to this queue.
     * 
     * @param message the audit message to add
     */
	void addToQueue(String message) {
		synchronized (queue) {
			queue.add( message );
		}
	}
	
	public void run() {
		boolean endNow = false;
		while (!endNow)  {
			if (shutdown) {
				endNow = true;
			}
			try {
				int timeInterval = messenger.getAuditTrailDescription().getTransmissionInterval();
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {}
			
			if (!queue.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("Preparing to send " + queue.size() + " audit messages to " + 
							auditTrail.getHost() + ":" + auditTrail.getPort());
				}
				try {
					String[] messages;
					synchronized (queue) {
						messages = queue.toArray(new String[queue.size()]);
						queue.clear();
					}
					messenger.transmitMessages(messages);
				} catch (Exception e) {
					if (log.isDebugEnabled()) {
						log.debug("Error sending - " + e.getMessage(), e);
					}
				}
			}
		} 
		log.info("Clean shutdown of audit queue finished");
	}

	/**
	 * Shuts down this audit queue.
	 * 
	 */
	public void shutdown(){
		try {
			log.info("Shuting down audit queue for " + auditTrail.getConnectionDescription().getName());
			shutdown = true;
			
			this.join();
		} catch (InterruptedException e) {
			log.warn("Interrupted while waiting for queue shutdown, may not have completed cleanly - " + e.getMessage(), e);
		}
	}
  
  
}
