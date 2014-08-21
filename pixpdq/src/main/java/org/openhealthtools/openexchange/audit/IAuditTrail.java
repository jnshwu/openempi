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

import org.openhealthtools.openexchange.actorconfig.IAuditTrailLifeCycle;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.AuditCodeMappings.SuccessCode;

/**
 *  The base interface of an audit message class.
 *
 * @author Michael Traum
 */
public interface IAuditTrail extends IAuditTrailLifeCycle {
	
	/** Call when a user authenticates himself. 
	 * 
	 * Described in DICOM Supp95 A 1.3.15 as User Authentication.
	 * Described in ITI TF-2 p. 172 as Node-authentication-failure.
	 */
	public void userLogin(SuccessCode success, ActiveParticipant user);
	

	/** Call when a user logs out. 
	 * 
	 * Described in DICOM Supp95 A 1.3.15 as User Authentication.
	 * Described in ITI TF-2 p. 172 as Node-authentication-failure.
	 */
	public void userLogout(SuccessCode success, ActiveParticipant user);

	/** Call when the node fails to authenticate itself with another node. 
	 * 
	 * Generally you don't log successes since there can be many of those.
	 * 
	 * Described in DICOM Supp95 A 1.3.14 as Security Alert.
	 * Described in ITI TF-2 p. 172 as Node-authentication-failure.
	 */
	public void nodeAuthenticationFailure(SuccessCode success, IConnectionDescription otherServer);
}
