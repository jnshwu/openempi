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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The class represents the default implementation of loading Spring 
 * configuration, and provides a facade to get the spring application
 * context. 
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @see PropertyFacade
 */
public class DefaultSpringConfig implements SpringConfig {

	private final static Log log = LogFactory.getLog(DefaultSpringConfig.class);
	
	private ApplicationContext applicationContext = null;

	public DefaultSpringConfig() throws ConfigurationException {
		initializeSpring(null);
	}
	
	public DefaultSpringConfig(String[] configLocations) throws ConfigurationException {
		initializeSpring(configLocations);
	}
	
	@Override
	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	private void initializeSpring(String[] configLocations) throws ConfigurationException {
			if (configLocations != null)
				this.applicationContext = new ClassPathXmlApplicationContext(configLocations);
			else
				this.applicationContext = new ClassPathXmlApplicationContext(getConfigLocations());
				
			
			//add a shutdown hook for the above context... 
			((AbstractApplicationContext)this.applicationContext).registerShutdownHook();
	}
	
	protected String[] getConfigLocations() {
        return new String[] {
               "classpath*:/applicationContext.xml" // for modular projects
            };
    }

}
