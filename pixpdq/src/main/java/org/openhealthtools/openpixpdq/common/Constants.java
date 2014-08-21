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

/**
 * This class defines all the constants used by OpenPIXPDQ.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class Constants {

	//Actor Types
	public static final String PIX_MANAGER    = "PixManager";
	public static final String PIX_MANAGER_V3 = "PixManagerV3";
	public static final String PD_SUPPLIER    = "PdSupplier";
	public static final String PD_SUPPLIER_V3 = "PdSupplierV3";
	public static final String SECURE_NODE    = "SecureNode";
	public static final String PIX_CONSUMER_V3 = "PixConsumerV3";
	
	//Connection Types
	public static final String SERVER = "Server";
	public static final String XDS_REGISTRY = "XdsRegistry";
	public static final String PIX_CONSUMER = "PixConsumer";

	public static final String RECEIVING_APPLICATION = "ReceivingApplication";
	public static final String RECEIVING_FACILITY = "ReceivingFacility";
	
	//property names
	public static final String VALIDATE_RECEIVING_APPLICATION = "validate.receiving.application";
	public static final String VALIDATE_RECEIVING_FACILITY    = "validate.receiving.facility";
	
	//Constants
	public static final String INCLUDE_OTHER_IDS = "include.other.ids";
	public static final String SSN_OID = "2.16.840.1.113883.4.1";
	
}
