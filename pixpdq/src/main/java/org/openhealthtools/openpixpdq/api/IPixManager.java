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

package org.openhealthtools.openpixpdq.api;

import java.util.Collection;

import org.openhealthtools.openexchange.actorconfig.IheActor;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;

/**
 * This interface defines a patient Identifier cross reference manager (PIX Manager)actor. 
 * PIX Manager is a server side actor specified by IHE PIX profile. 
 * See Section 3.8, 3.9 and 3.10 of <a href="http://www.ihe.net/Technical_Framework/index.cfm#IT">Vol. 2 (ITI TF-2): Transactions</a>, 
 * available on the IHE site.   
 *
 * @author Wenzhi Li
 * @version 1.0, Mar 1, 2007
 */
public interface IPixManager extends IheActor {
	
	/**
	 * Gets the connection for the XDS Registry. The connect provides the details such as host name 
	 * and port etc which are needed for this PIX Manager to talk to the XDS Registry.
	 * 
	 * @return the connection of XDS Registry
	 */
	public IConnectionDescription getXdsRegistryConnection(); 

	/**
     * Gets a collection of all PIX Consumers who have subscribed to
     * the PIX Update Notification transaction.
     *  
	 * @return the pixConsumerConnections
	 */
    Collection<IConnectionDescription> getPixConsumerConnections();
	
}
