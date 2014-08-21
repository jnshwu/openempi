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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.net.BaseDescription;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.utils.StringUtil;


/**
 * An implementation of the IActorDescription to describe a 
 * particular actor (e.g. the XDS Repository Actor). 
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class ActorDescription extends BaseDescription implements IActorDescription {

	private static final Log log = LogFactory.getLog(ActorDescription.class);

    /* The actor description  */
    private String description = null;
    /* Whether this actor is installed */
    private boolean isInstalled = false;
    /* The human-readable actor type*/
    private String humanReadableType = null;
    /* A map of all the Connections */
    private Hashtable<String, IConnectionDescription> connections = null;
    /* A map of all the TransactionsSet */
    private Hashtable<String, TransactionsSet> transactionsSets = null;
    /* A collection of audit log connections */
    private Collection<IConnectionDescription> auditLogConnections = null; 


	/* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.net.IBaseDescription#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	

    /* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.IActorDescription#getHumanReadableType()
	 */
	@Override
	public String getHumanReadableType() {
		return humanReadableType;
	}

    /* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.IActorDescription#setHumanReadableType(java.lang.String)
	 */
	@Override
	public void setHumanReadableType(String humanReadableType) {
		this.humanReadableType = humanReadableType;
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.IActorDescription#isInstalled()
	 */
	@Override
	public boolean isInstalled() {
		return this.isInstalled;
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.IActorDescription#setInstalled()
	 */
	@Override
	public void setInstalled() {
		this.isInstalled = true;
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.IActorDescription#getConnectionDescription(java.lang.String)
	 */
	@Override
	public IConnectionDescription getConnectionDescription(String name) {
        if (!StringUtil.goodString(name)) {
            return null;
        }
        String key = name.toLowerCase();
        if ((connections != null) && (connections.containsKey(key))) {
            return connections.get(key);
        } else {
            log.debug("Looking up non-existent connection description: " + name);
            return null;
        }
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.IActorDescription#getConnectionDescriptionsByType(java.lang.String)
	 */
	@Override
	public Collection<IConnectionDescription> getConnectionDescriptionsByType(String type) {
		Collection<IConnectionDescription> ret = new ArrayList<IConnectionDescription>(); 
		if (!StringUtil.goodString(type)) return ret;
		
		if (connections == null) return ret;
		
		for (IConnectionDescription connection: connections.values()) {
			if (type.equalsIgnoreCase(connection.getType())) {
				ret.add( connection );
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.IActorDescription#getConnectionDescriptionByType(java.lang.String)
	 */
	@Override
	public IConnectionDescription getConnectionDescriptionByType(String type) {
		Collection<IConnectionDescription> descriptions = getConnectionDescriptionsByType( type );
		
		if (descriptions.size() > 1) {
			log.warn("Multiple Connections of type " + type + " is found in Actor " + this.getName() + ", but one is expected.");
		}
		
		for (IConnectionDescription description : descriptions) {
			//Will return just the first one in case there are multiple
			return description;
		}
		return null;
	}
	
	
    /**
     * Adds a new connection description to this actor description.  
     * This method is used when loading configuration files.
     *
     * @param set The connection description to add
     */
    public void addConnectionDescription(IConnectionDescription connection) {
        if (connection != null) {
            String connectionName = connection.getName();
            if (connectionName != null) {
                if (connections == null) {
                	connections = new Hashtable<String, IConnectionDescription>();
                }
                connections.put(connectionName.toLowerCase(), connection);
            } else {
                log.debug("Adding Connection with no name to actor: " + name);
            }
        }
    }

    /* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.IActorDescription#getTransactionSet(java.lang.String)
	 */
	@Override
	public TransactionsSet getTransactionSet(String type) {
        if (!StringUtil.goodString(type)) {
            return null;
        }
        String key = type.toLowerCase();
        if ((transactionsSets != null) && (transactionsSets.containsKey(key))) {
            return transactionsSets.get(key);
        } else {
            log.debug("Looking up non-existent transaction set: " + type);
            return null;
        }    	
	}

	/**
     * Adds a new transactions set to this actor description.  This method 
     * is used when loading configuration files.
     *
     * @param set The {@link TransactionsSet} to add
     */
    public void addTransactionsSet(TransactionsSet set) {
        if (set != null) {
            String type = set.getType();
            if (type != null) {
                if (transactionsSets == null) {
                	transactionsSets = new Hashtable<String, TransactionsSet>();
                }
                transactionsSets.put(type.toLowerCase(), set);
            } else {
                log.debug("Adding transactions set with no name to actor: " + name);
            }
        }
    }

	/* (non-Javadoc)
	 * @see org.openhealthtools.openexchange.actorconfig.IActorDescription#getAuditLogConnection()
	 */
	@Override
	public Collection<IConnectionDescription> getAuditLogConnection() {
        if (auditLogConnections == null) {
            auditLogConnections = new ArrayList<IConnectionDescription>();
        } 
        if (auditLogConnections.size() == 0 )
            log.debug("No aduit log connection is defined for the actor: " + name);
    
    	return auditLogConnections;
	}

    /**
     * Adds a new audit log connection to this actor description.  This method 
     * is used when loading configuration files.
     *
     * @param set The {@link TransactionsSet} to add
     */
    public void addAuditLogConnection(IConnectionDescription connection) {
        if (connection != null) {
            if (auditLogConnections == null) {
            	auditLogConnections = new ArrayList<IConnectionDescription>();
            }
        	auditLogConnections.add( connection );
        }
    }
}
