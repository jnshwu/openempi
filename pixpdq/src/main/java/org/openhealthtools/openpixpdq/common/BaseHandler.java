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

import java.net.InetAddress;

import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.IBaseDescription;
import org.openhealthtools.openexchange.datamodel.Identifier;


/**
 * The base class of all handlers. It provides some common 
 * methods available to all handlers.
 * 
 * @author Wenzhi Li
 * @version 1.0, Dec 26, 2008
 */
public class BaseHandler {
	
	/**
	 * Default constructor
	 */
	public BaseHandler() {
	}
	
	/**
	 * Gets the application name of this PIX/PDQ server. If the server receives messages from
	 * a source application, then the application name is the same as ReceivingApplication, configurable
	 * through the XML actor configuration files.
	 * 
	 * @param description the actor or connection description where this server application is defined.
	 * @return the application name of this PIX/PDQ server
	 * @throws PixPdqException
	 */
	protected Identifier getServerApplication(IBaseDescription description) throws PixPdqException {
		Identifier ret = null;
		try {
			ret = Configuration.getIdentifier(description,
					"ReceivingApplication", true);
		} catch (IheConfigurationException e) {
			throw new PixPdqException(
					"Missing ReceivingApplication in Actor or Connection "
							+ description.getDescription(), e);
		}
		return ret;		
	}
    
	/**
	 * Gets the facility name of this PIX/PDQ server. If the server receives messages from
	 * a source application, then the facility name is the same as ReceivingFacility, configurable
	 * through the XML actor configuration files.
	 * 
	 * @param description the actor or conenction description where this server facility is defined.
	 * @return the facility name of this PIX/PDQ server
	 * @throws PixPdqException
	 */
	protected Identifier getServerFacility(IBaseDescription description) throws PixPdqException {
		Identifier ret = null;
		try {
			ret = Configuration.getIdentifier(description,
					"ReceivingFacility", true);
		} catch (IheConfigurationException e) {
			throw new PixPdqException(
					"Missing ReceivingFacility in Actor or Connection "
							+ description.getDescription(), e);
		}
		return ret;				
	}
	
	private static String ip = null;
	static {
		try {
			InetAddress addr = InetAddress.getLocalHost();
		    ip = addr.getHostAddress();
		}catch(Exception e) {
			//just ignore it.
		}
	}
	
	private static int staticCounter=0;
	private static final int nBits=4;
	
	/**
	 * Generates a new unique message id.
	 * 
	 * @return a message id
	 */
	public static synchronized String getMessageControlId() {
		String prefix = ""; 
		if (ip != null)	prefix += toHex(ip);
		long temp = (System.currentTimeMillis() << nBits) | (staticCounter++ & 2^nBits-1);
		String hex = Long.toHexString(temp);
		String id = prefix + hex;
		return id;
	}
	
    /**
     * Converts an IP address to a hex string.
     *         
     * @param ipAddress an IP address 
     * 
     * @return the IP address in a hex form
     */  
     protected static String toHex(String ipAddress) {                
    	 return Long.toHexString(toLong(ipAddress));        
     }  
     /**
      * Converts an IP address to a long
      * 
      * @param ipAddress the IP address
      * 
      * @return the IP address as a long        
      */   
     protected static long toLong(String ipAddress) {     
    	 long result = 0;           
    	 String[] atoms = ipAddress.split("\\.");     
    	 for (int i = 3; i >= 0; i--) {  
    		 result |= (Long.parseLong(atoms[3 - i]) << (i * 8)); 
    	 }
    	 return result & 0xFFFFFFFF;        
     }

}
