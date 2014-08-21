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

package org.openhealthtools.openpixpdq.common;

import java.io.File;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader;
import org.openhealthtools.openexchange.actorconfig.ActorDescriptionLoader;
import org.openhealthtools.openexchange.actorconfig.AuditBroker;
import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.IBrokerController;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openexchange.config.ConfigurationException;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.log.IMesaLogger;
import org.openhealthtools.openpixpdq.api.IMessageStoreLogger;
import org.openhealthtools.openpixpdq.api.IPdSupplier;
import org.openhealthtools.openpixpdq.api.IPixManager;
import org.openhealthtools.openpixpdq.impl.v2.PdSupplier;
import org.openhealthtools.openpixpdq.impl.v2.PixManager;
import org.openhie.openempi.openpixpdq.v3.impl.PdqSupplierV3;
import org.openhie.openempi.openpixpdq.v3.impl.PixManagerV3;

/**
 * This class loads an Actor configuration file and initializes all of the
 * appropriate OpenPIXPDQ actors within the PatientBroker and AuditBroker.
 * 
 * @author Wenzhi Li
 */
public class PixPdqConfigurationLoader extends ActorConfigurationLoader {

	/* Logger for debugging messages */
	private static final Log log = LogFactory.getLog(PixPdqConfigurationLoader.class);
	
	/* Singleton instance */
	private static PixPdqConfigurationLoader instance = null;
	
	/**
	 * Gets the singleton instance for this class.
	 * 
	 * @return the singleton ConfigurationLoader
	 */
	public static synchronized PixPdqConfigurationLoader getInstance() {
		if (instance == null) {
			instance = new PixPdqConfigurationLoader();
		}
		return instance;
	}
	
	public void loadProperties(String[] propertyFiles) {
        try {
            PropertyFacade.loadProperties(propertyFiles);
        } catch (ConfigurationException e) {
            log.error("Failed to load openpixpdq.properties", e);
        }
	}
	
	@Override
	public void destroyAllActors() {
		// Create a controller that will reset all IHE actors
		IheBrokerController controller = new IheBrokerController();
		// Apply it to the AuditBroker
		AuditBroker abroker = AuditBroker.getInstance();
		abroker.unregisterAuditSources(controller);
		// Apply it to the PatientBroker
		PatientBroker pbroker = PatientBroker.getInstance();
        pbroker.unregisterPixManagers(controller);
        pbroker.unregisterPdSuppliers(controller);
        // Okay, nothing is installed
		actorsInstalled.clear();
	}
	
	@Override
	protected boolean validateActor(IActorDescription actor, File configFile) throws IheConfigurationException {
		String actorName = actor.getName();
		// Make sure we got out a valid definition
		if (actor.getType().equalsIgnoreCase(Constants.PIX_MANAGER)) {
			IConnectionDescription connection = Configuration.getConnection(actor, Constants.SERVER, true);
			assert connection != null;
			
			IConnectionDescription xdsRegistryConnection = Configuration.getConnection(actor, Constants.XDS_REGISTRY, false);
			if (xdsRegistryConnection != null) {
				//Global Domain is required for XDS Registry
				validateGlobalDomain(actor);
			}
		} 
		else if (actor.getType().equalsIgnoreCase(Constants.PIX_MANAGER_V3)) {
			IConnectionDescription xdsRegistryConnection = Configuration.getConnection(actor, Constants.XDS_REGISTRY, false);
			if (xdsRegistryConnection != null) {
				//Global Domain is required for XDS Registry
				validateGlobalDomain(actor);
			}
			
		} 
		else if (actor.getType().equalsIgnoreCase(Constants.PD_SUPPLIER)) {
			IConnectionDescription connection = Configuration.getConnection(actor, Constants.SERVER, true);
			assert connection != null;
		}
		else if (actor.getType().equalsIgnoreCase(Constants.PD_SUPPLIER_V3)) {
			log.info("Initialized an actor of type " + actor.getType());
		}
		else if (actor.getType().equalsIgnoreCase(Constants.PIX_CONSUMER_V3)) {
			log.info("Initialized an actor of type " + actor.getType());
		}
		else if (actor.getType().equalsIgnoreCase(SECURENODE)) {
			if (actor.getAuditLogConnection().isEmpty())
				throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid '" + AUDITTRAIL + "' element");
		}
		else {
			throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid actor type");			
		}
		
		return true;
	}

	private void validateGlobalDomain(IActorDescription actor) throws IheConfigurationException {
		Identifier defaultDomain = Configuration.getGlobalDomain(actor, false);	
		Identifier globalDomain = PixPdqFactory.getPixManagerAdapter().getGlobalDomainIdentifier(defaultDomain);
		if (null == globalDomain) {
			throw new IheConfigurationException("Global Domain is not defined");
		}
	}
	
	@Override
	protected boolean createIheActor(IActorDescription actor, Collection<IConnectionDescription> auditLogs, IMesaLogger logger, File configFile) 
	throws IheConfigurationException {
		boolean okay = false;
		IheAuditTrail auditTrail = null;
		
		// Build a new audit trail if there are any connections to audit repositories.
		if (!auditLogs.isEmpty()) { 
			auditTrail = new IheAuditTrail(actor.getName(), auditLogs);
		}
		// Actually create the actor
		if (actor.getType().equalsIgnoreCase(SECURENODE)) {
			if (auditTrail != null) {
				AuditBroker broker = AuditBroker.getInstance();
				broker.registerAuditSource(auditTrail);
				okay = true;
			}
		}
		else if (actor.getType().equalsIgnoreCase(Constants.PIX_MANAGER)) {
	        PixManager pixMan =  new PixManager(actor, auditTrail);
	        if (pixMan != null) { 
	        	pixMan.setStoreLogger(getMessageStore(pixMan.getConnection(), actor.getType(), configFile));
	            pixMan.setMesaLogger(logger);
//	            pixMan.setPixEvent(eventBean);
	            PatientBroker broker = PatientBroker.getInstance();
	            broker.registerPixManager(pixMan);
	            okay = true;
	        }
		}
		else if (actor.getType().equalsIgnoreCase(Constants.PIX_MANAGER_V3)) {
	        PixManagerV3 pixMan =  new PixManagerV3(actor, auditTrail);
	        if (pixMan != null) { 
	            pixMan.setMesaLogger(logger);
	            PatientBroker broker = PatientBroker.getInstance();
	            broker.registerPixManager(pixMan);
	            okay = true;
	        }
		}
		else if (actor.getType().equalsIgnoreCase(Constants.PD_SUPPLIER)) {
            PdSupplier pdSup = new PdSupplier(actor, auditTrail);
            if (pdSup != null) {
                pdSup.setStoreLogger(getMessageStore(pdSup.getConnection(), actor.getType(), configFile));
                pdSup.setMesaLogger(logger);
                PatientBroker broker = PatientBroker.getInstance();
                broker.registerPdSupplier(pdSup);
                okay = true;
            }
		}
		else if (actor.getType().equalsIgnoreCase(Constants.PD_SUPPLIER_V3)) {
            PdqSupplierV3 pdSup = new PdqSupplierV3(actor, auditTrail);
            if (pdSup != null) {
                pdSup.setMesaLogger(logger);
                PatientBroker broker = PatientBroker.getInstance();
                broker.registerPdSupplier(pdSup);
                okay = true;
            }
		}
		else if (actor.getType().equalsIgnoreCase(Constants.PIX_CONSUMER_V3)) {
			log.debug("Nothing to do for this one.");
		}
		else {
			ActorDescriptionLoader.throwIheConfigurationException("Invalid actor type '" + actor.getType() + "'", configFile);
		}
		
        // Record this installation, if it succeeded
		if (okay) actorsInstalled.add(actor.getName()); 
		return okay;
	}

	    /**
	     * Gets the {@link IMessageStoreLogger} from the storeLogger XML configuration.
	     * 
	     * @param connection the connection description of this actor
	     * @param type the type of this actor
	     * @param configFile the configuration file
	     * @return the {@link IMessageStoreLogger} as in the configuration. Otherwise, return
	     *         null if storeLogger is not configured.
	     */
	    private IMessageStoreLogger getMessageStore(IConnectionDescription connection, String type, 
	    		File configFile) throws IheConfigurationException {
	        String storeLogBeanName = Configuration.getPropertyValue(connection, "storeLogger", false);
	        IMessageStoreLogger storeLogger = null;
	        if (storeLogBeanName != null) {
	            try {
	            	Object obj = PixPdqFactory.getInstance().getBean(storeLogBeanName);
	            	if (obj != null && obj instanceof IMessageStoreLogger) {
	            		storeLogger = (IMessageStoreLogger) obj;
	            	} else {
	            		log.warn("The message store logger bean configuration is invalid: " + storeLogBeanName);
	            	}
	            } catch (Exception e) {
	                String message = "Could not load StoreLogger in actor type '"+ type +"' in config file " + configFile;
	                log.warn(message);
	            }
	        }
	    	return storeLogger;
	    }
		
		
	@Override
	protected String getHumanReadableActorType(String type, File configFile) throws IheConfigurationException {
		if (type.equalsIgnoreCase("SecureNode")) {
			return "Audit Record Repository";
		} else if (type.equals(Constants.PIX_MANAGER)) {
            return "OpenPIXPDQ PIX Manager";
		} else if (type.equals(Constants.PIX_MANAGER_V3)) {
            return "OpenPIXPDQ PIX Manager V3";
        } else if (type.equals(Constants.PD_SUPPLIER)) {
            return "OpenPIXPDQ Patient Demographics Supplier";
        } else if (type.equals(Constants.PD_SUPPLIER_V3)) {
            return "OpenPIXPDQ Patient Demographics Supplier V3";
        } else if (type.equals(Constants.PIX_CONSUMER_V3)) {
            return "OpenPIXPDQ Patient Identifier Consumer V3";
	    }
		else {
			ActorDescriptionLoader.throwIheConfigurationException("Invalid actor type '" + type + "'", configFile);
			return null;
		}
	}
	
	/**
	 * An implementation of a broker controller that will unregister and IHE actor.
	 * 
	 */
	public class IheBrokerController implements IBrokerController {

		/**
		 * Whether to unregister of a give actor or not
		 */
		public boolean shouldUnregister(Object actor) {
			// Unregister any IHE Actor
			if (actor instanceof IheAuditTrail) return true;
            if (actor instanceof IPixManager) return true;
            if (actor instanceof IPdSupplier) return true;

            return false;
		}
	}	
}
