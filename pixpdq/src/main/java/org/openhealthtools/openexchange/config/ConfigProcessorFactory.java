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

package org.openhealthtools.openexchange.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The factory class to get Config Processor.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class ConfigProcessorFactory {
	private static final Log log = LogFactory.getLog(ConfigProcessorFactory.class);

	/**The instance*/
	public static ConfigProcessor processor = null;
	
	/**
	 * Gets the config processor.
	 * 
	 * @return
	 */
	public synchronized static ConfigProcessor getConfigProcessor() {
		if (processor == null) {
			String configProcessorClass = BootStrapProperties.getConfigProcessorClass();
			try {
				Class clazz = Class.forName(configProcessorClass);

				if (log.isInfoEnabled()) {
					log.info("Loading ConfigProcessor from " + configProcessorClass);
				}
				
				processor = (ConfigProcessor)clazz.newInstance();
			} catch (Exception e) {
				String message = "Failed to load ConfigProcessor. Nested message is " + e.getMessage();
				log.error(message, e);
			}
		}
		return processor;
	}
}

