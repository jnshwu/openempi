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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.actorconfig.IBrokerController;
import org.openhealthtools.openexchange.actorconfig.IheActor;
import org.openhealthtools.openpixpdq.api.IPdSupplier;
import org.openhealthtools.openpixpdq.api.IPixManager;

/**
 * This class presents a single global <code>PatientBroker</code>
 * instance to the OpenPIXPDQ code.  That way it need
 * not be passed around in global web state.  It can simply be
 * initialized and then requested from any code whenever necessary.
 * 
 * @author Jim Firby
 * @version 1.0 - Oct 22, 2005
 */
public class PatientBroker
{
	
	/** A single instance for this class */
	private static PatientBroker singleton = null;
	
	/** Log for problems encountered by the PatientBroker */
	private static final Logger log = Logger.getLogger(PatientBroker.class);
		
    /** A list of all known pix manager */
    private Vector<IPixManager> pixManagers = new Vector<IPixManager>();
    /** A list of all known pd supplier */
    private Vector<IPdSupplier> pdSuppliers = new Vector<IPdSupplier>();
    
    private Map<String,IheActor> actorMap = new HashMap<String,IheActor>();

	/**
	 * A private constructor for creating the singleton instance.
	 */
	private PatientBroker() {
		super();
	}
	
	/**
	 * Gets the single global instance of the <code>PatientBroker</code>.
	 * 
	 * @return the patient broker
	 */
	public static synchronized PatientBroker getInstance() {
		if (singleton == null) {
			singleton = new PatientBroker();
		}
		return singleton;
	}
 	
	public IheActor getActorByName(String name) {
		return actorMap.get(name);
	}
	
    /**
	 * Registers a new PIX manager.  This method
	 * is typically called when a PIX manager server is started.
	 *
	 * @param pixManager a PIX manager
	 * @return <code>true</code> if this PIX manager was successfully added
	 */
    public synchronized boolean registerPixManager(IPixManager pixManager) {
		// If the pixManager is new, add it to the list
		if ((pixManager != null) && (!pixManagers.contains(pixManager))) {
			pixManager.start();
			pixManagers.add(pixManager);
			actorMap.put(pixManager.getName(), pixManager);
			return true;
		} else {
			return false;
		}
	}

    /**
	 * Registers a new patient demographics supplier.  This method
	 * is typically called when a PD supplier server is started.
	 *
	 * @param pdSupplier a patient demographics supplier
	 * @return <code>true</code> if this patient demographics supplier was successfully added
	 */
    public synchronized boolean registerPdSupplier(IPdSupplier pdSupplier) {
		// If the pdqSupplier is new, add it to the list
		if ((pdSupplier != null) && (!pdSuppliers.contains(pdSupplier))) {
			pdSupplier.start();
			pdSuppliers.add(pdSupplier);
			actorMap.put(pdSupplier.getName(), pdSupplier);
			return true;
		} else {
			return false;
		}
	}

    /**
	 * Unregisters the active PIX managers specified by the controller.  If the
	 * controller is null unregister all PIX managers.  A PIX manager is stopped 
	 * when it is unregistered.
	 *
	 * @param controller the controller specifying which PIX manager to unregister. All PIX
	 * managers will be unregistered if it is null. 
	 * @return <code>true</code> if any PIX manager were actually unregistered
	 */
     public synchronized boolean unregisterPixManagers(IBrokerController controller) {
		ArrayList<IPixManager> removed = new ArrayList<IPixManager>();
		// Find all the sources to remove
		for (IPixManager actor: pixManagers) {
			if ((controller == null) || controller.shouldUnregister(actor)) {
				removed.add(actor);
			}
		}
		if (removed.isEmpty()) return false;
		// Remove them
		pixManagers.removeAll(removed);
		
 		ArrayList<IheActor> actorsRemoved = new ArrayList<IheActor>();
		for (String name : actorMap.keySet()) {
			IheActor actor = actorMap.get(name);
			if (actor.getActorDescription().getType() != null &&
					actor.getActorDescription().getType().startsWith("PixManager")) {
				actorsRemoved.add(actor);
			}
		}
		// Remove them
		if (actorsRemoved.size() > 0) {
			for (IheActor actor : actorsRemoved) {
				actorMap.remove(actor.getName());
			}
		}

		// Stop them all too
		for (IPixManager actor: removed) actor.stop();
		return true;
	}

     /**
 	 * Unregisters the active patient demographics supplier specified by the controller.  If the
 	 * controller is null unregister all patient demographics supplier.  A patient demographics 
 	 * supplier is stopped when it is unregistered.
 	 *
 	 * @param controller the controller specifying which patient demographics supplier to unregister.
 	 * All patient demographics suppliers will be unregistered if it is null. 
 	 * @return <code>true</code> if any patient demographics suppliers were actually unregistered
 	 */
      public synchronized boolean unregisterPdSuppliers(IBrokerController controller) {
 		ArrayList<IPdSupplier> removed = new ArrayList<IPdSupplier>();
 		// Find all the sources to remove
 		for (IPdSupplier actor: pdSuppliers) {
 			if ((controller == null) || controller.shouldUnregister(actor)) {
 				removed.add(actor);
 			}
 		}
 		if (removed.isEmpty()) return false;
 		// Remove them
 		pdSuppliers.removeAll(removed);
 		
 		ArrayList<IheActor> actorsRemoved = new ArrayList<IheActor>();
		for (String name : actorMap.keySet()) {
			IheActor actor = actorMap.get(name);
			if (actor.getActorDescription().getType() != null &&
					actor.getActorDescription().getType().startsWith("PdSupplier")) {
				actorsRemoved.add(actor);
			}
		}
		// Remove them
		if (actorsRemoved.size() > 0) {
			for (IheActor actor : actorsRemoved) {
				actorMap.remove(actor.getName());
			}
		}
 		// Stop them all too
 		for (IPdSupplier actor: removed) actor.stop();
 		return true;
 	}
	
}
