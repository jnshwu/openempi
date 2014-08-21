/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhie.openempi.openpixpdqadapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;

public class BasePixPdqAdapter
{
	protected final Log log = LogFactory.getLog(getClass());

	// The initialization of the context should preferably be done
	// during a lifecycle event from the PixPdqManager but currently the start() event
	// does not get propagated out to the adapter.
	//
	public BasePixPdqAdapter() {
		try {
			if (!Context.isInitialized()) {
				Context.startup();
			}
		} catch (Throwable e) {
			log.error("Failed while initializing the Pix/Pdq Adapter due to: " + e, e);
		}
	}
	
	public Set<Identifier> getDomainIdentifiers(Set<Identifier> defaultDomains) {
		Set<Identifier> idSet = new HashSet<Identifier>();
		idSet.addAll(defaultDomains);
		log.debug("Loaded from the configuration file " + idSet.size() + " entries.");
		
		List<IdentifierDomain> domains = Context.getPersonQueryService().getIdentifierDomains();
		for (IdentifierDomain domain : domains) {
			idSet.add(ConversionHelper.convertIdentifierDomain(domain));
		}
		
		log.debug("Loaded another " + domains.size() + " from the repository for a total of " + idSet.size() + 
				" unique entries.");
		return idSet;
	}
	
	public Identifier getGlobalDomainIdentifier(Identifier defaultGlobalDomain) {
		//TODO: either to return the default domain configured in OpenPIXPDQ, or 
		//Overwrite it with the global domain configured in EMPI.
		return defaultGlobalDomain;
	}
	
}
