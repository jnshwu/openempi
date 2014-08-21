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


/**
 * The request interface for PIX Update Notification. This
 * is the data type placed in the blocking queue used in PIX Update
 * Notification consumer, namely {@link PixUpdateNotifier}.
 * 
 * @author Wenzhi Li
 * @version 1.0, Feb 14, 2009
 * @see PixUpdateNotifier
 */
public interface IPixUpdateNotificationRequest {
    
	/**
	 * Executes the PIX update notification of this request, which
	 * actually sends a PIX update notification to subscribed 
	 * PIX Consumers
	 */
	public void execute();
	
}
