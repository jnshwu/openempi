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

import java.util.Set;

import org.openhealthtools.openexchange.datamodel.Identifier;




/** 
 * This is the super interface for both IPixManagerAdapter and 
 * IPdSupplierAdapater.
 *
 * @author Wenzhi Li
 * @version 1.0.1, Oct 29, 2009
 */
public interface IPixPdqAdapter {

    /**
     * Gets the domains (or assigning authorities) supported by this PIX Manager/PD Supplier.
     * The domains can be configured in either OpenPIXPDQ or eMPI. This method takes in a set of
     * default domains configured in OpenPIXPDQ configuration files. The adapter can choose to
     * return the default domains, or retrieve domains configured in eMPI, or simply merge the
     * domains configured in both OpenPIXPDQ and eMPI. 
     * 
     * @defaultDomains the default domains configured in the OpenPIXPDQ configuration file.
     * @return A set of domain identifiers.
     */
	public Set<Identifier> getDomainIdentifiers(Set<Identifier> defaultDomains);
	
	/**
	 * Gets the global domain (or global assigning authority) supported by this PIX Manager/PD
	 * Supplier. The global domain can be configured in either 
	 * 
	 * @param defaultGlobalDomain the default global domain configured in the OpenPIXPDQ 
	 * 		   configuration file
	 * @return the global domain identifier
	 */
	public Identifier getGlobalDomainIdentifier(Identifier defaultGlobalDomain);

}
