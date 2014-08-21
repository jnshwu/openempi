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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.config.ConfigurationException;
import org.openhealthtools.openexchange.config.SpringFacade;
import org.openhealthtools.openpixpdq.api.IMessageStoreLogger;
import org.openhealthtools.openpixpdq.api.IPdSupplierAdapter;
import org.openhealthtools.openpixpdq.api.IPixManagerAdapter;
import org.springframework.context.ApplicationContext;

/**
 * This class manages each module Spring initialization.
 *
 * @author Wenzhi Li
 *
 */
public class PixPdqFactory {
	
	private static final Log log = LogFactory.getLog(PixPdqFactory.class);

	private static final PixPdqFactory SINGLETON = new PixPdqFactory();

	private ApplicationContext applicationContext;

	private PixPdqFactory() {
		super();
		loadApplicationContext();
	}

	/**
	 * Singleton 
	 *
	 * @return the Singleton of this Factory
	 */
	public static PixPdqFactory getInstance() {
		return SINGLETON;
	}
	
	private String[] getConfigLocations() {
        return new String[] {
               "classpath*:/*applicationContext.xml" // for modular projects
            };
    }

	/**
	 * Gets spring beans object of the given bean name.
	 * 
	 * @param beanName the bean name
	 * @return the bean object
	 */
	public Object getBean(String beanName) {
		return this.applicationContext.getBean(beanName);
	}

	/**
	 * The factory method to get {@link IPixManagerAdapter}
	 * 
	 * @return the singleton {@link IPixManagerAdapter} instance
	 */
	public static IPixManagerAdapter getPixManagerAdapter() {
		return (IPixManagerAdapter)getInstance().getBean("pixManagerAdapter");
	}

	private void loadApplicationContext() {
		try {
			SpringFacade.loadSpringConfig( getConfigLocations() );
			applicationContext = SpringFacade.getApplicationContext();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The factory method to get {@link IPdSupplierAdapter}
	 * 
	 * @return the singleton {@link IPdSupplierAdapter} instance
	 */
	public static IPdSupplierAdapter getPdSupplierAdapter() {
		return (IPdSupplierAdapter)getInstance().getBean("pdSupplierAdapter");
	}
	
	public static IMessageStoreLogger getMessageStoreLogger() {
		return (IMessageStoreLogger) getInstance().getBean("messageStoreLogger");
	}
}
