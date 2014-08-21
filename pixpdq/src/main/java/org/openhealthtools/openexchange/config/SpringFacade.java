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
import org.openhealthtools.openexchange.utils.StringUtil;
import org.springframework.context.ApplicationContext;

/**
 * The facade class to get the spring application context. Be sure
 * to call the loadSpringConfig method first to load the spring
 * context before invoking the getApplicationContext method.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class SpringFacade {
	private static final Log log = LogFactory.getLog(SpringFacade.class);

	private static SpringConfig config = null;
	
	/**
	 * Loads the spring context to the spring container. This method has
	 * to called before invoking the getApplicationContext method.  
	 * 
	 * @param configLocations the application context files
	 * @return <code>true</code> if the spring config is loaded successfully.
	 * @throws ConfigurationException when there is an error with configuration.
	 */
    public synchronized static boolean loadSpringConfig(String[] configLocations)
    throws ConfigurationException {
    	
    	//Get the bootstrap property spring config class
        String springConfigClass = BootStrapProperties.getSpringConfigClass();

    	if (!StringUtil.goodString(springConfigClass) || 
    		springConfigClass.equals("org.openhealthtools.openexchange.config.DefaultSpringConfig")) {
    		//Use the default property config
			if (log.isInfoEnabled()) {
				log.info("Loading Spring Context from " + springConfigClass);
			}
    		config = new DefaultSpringConfig(configLocations);
    	} else {
	    	try {
				Class clazz = Class.forName(springConfigClass);
				if (log.isInfoEnabled()) {
					log.info("Loading Spring Context from " + springConfigClass);
				}
				config = (SpringConfig)clazz.newInstance();
			} catch (Exception e) {
				String message = "Fail to load Spring application context. Nested message is " + e.getMessage();
				log.fatal(message + e.getMessage(), e);
				throw new ConfigurationException(message, e);
			}
    	}
    	return true;
    }
    
    /**
     * Gets the Spring configuration. Be sure {@link #loadSpringConfig(String[])} is
     * invoked before calling this method.
     * 
     * @return the SpringConfig object
     * @see #loadSpringConfig(String[])  
     */
    public static SpringConfig getSpringConfig() {
    	return config;
    }
    
    /**
     * Gets the application context. Be sure {@link #loadSpringConfig(String[])} is
     * invoked before calling this method.
     * 
     * @return the application context
     */
    public static ApplicationContext getApplicationContext() {
    	if (config == null)
    		return null;
    	
    	return config.getApplicationContext();
    }
 }
