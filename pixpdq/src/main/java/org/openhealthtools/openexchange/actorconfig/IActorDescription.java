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

package org.openhealthtools.openexchange.actorconfig;

import java.util.Collection;

import org.openhealthtools.openexchange.actorconfig.net.IBaseDescription;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;


/**
 * The basic actor description interface.
 * <p/>
 * It should be used to get information about a specific
 * actor.  You can get descriptions from the
 * actor factory, or from a specific actor.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @see ActorFactory, ActorDescription
 */
public interface IActorDescription extends IBaseDescription {

    /**
     * Whether this actor is installed or not. By install it means
     * the actor is started and registered with its broker.
     * 
     * @return <code>true</code> if this actor is installed; 
     * otherwise return <code>false</code>
     */
	public boolean isInstalled();
    
    /**
     * Sets this actor to be installed. By install it means
     * the actor is started and registered with its broker. 
     */
	public void setInstalled();
	
	/**
	 * Gets a human-readable string naming type of this actor
	 * (ie. "PDQ Server", "PIX Manager", "Audit Repository").  All
	 * actors of the same type will return the same string.  The
	 * type is designed to be used in a GUI.
	 * 
	 * @return the human-readable type 
	 */
	public String getHumanReadableType();
	
	/**
	 * Sets a human-readable string naming type of this actor
	 * (ie. "PDQ Server", "PIX Manager", "Audit Repository").  All
	 * actors of the same type will return the same string.  The
	 * type is designed to be used in a GUI.
	 * 
	 * @return the human-readable type 
	 */
	public void setHumanReadableType(String type);
	
	/**
	 * Gets a connection description of a given connection name.
	 * 
	 * @param name the connection name
	 * @return the {@link IConnectionDescription} matching the given name
	 */
    public IConnectionDescription getConnectionDescription(String name);
    
	/**
	 * Looks up a collection of connection descriptions given the connection type
	 * 
	 * @param type the connection description type
	 * @return a collection of connection descriptions. If the given type is not 
	 * found, an empty collection will be returned.
	 */
	public Collection<IConnectionDescription> getConnectionDescriptionsByType(String type);
 
	/**
	 * Looks up a connection descriptions given the connection type
	 * 
	 * @param type the connection description type
	 * @return a connection description. If the given type is not 
	 * found, null will be returned.
	 */
	public IConnectionDescription getConnectionDescriptionByType(String type);
	
    /**
     * Gets a set of transactions of a given type
     * 
     * @param type the type of the transactions to get
     * @return the {@link TransactionsSet} 
     */
    public TransactionsSet getTransactionSet(String type);

    /**
	 * Gets a collection of audit trail log connections for this actor.
	 * 
	 * @return a collection of audit trail log connections. If no audit log is defined, 
	 * an empty collection will be returned.
	 */
	public Collection<IConnectionDescription> getAuditLogConnection(); 

}
