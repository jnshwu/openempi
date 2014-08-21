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
package org.openhie.openempi.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.context.Context;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class BaseSpringApp
{
	private String[] DEFAULT_CONTEXT_RESOURCES = {
	        "/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml",
	        "/applicationContext-service.xml", "classpath*:/**/applicationContext.xml"
		};
    protected final Log log = LogFactory.getLog(getClass());

	
	private GenericApplicationContext applicationContext;

	public void startup() {
		Context.startup();
//		Context.authenticate("admin", "admin");
	}
	
	public void startup(String[] locations) {
		Context.startup();
//		Context.authenticate("admin", "admin");
	}
	
	public void shutdown() {
		Context.shutdownAll();
	}
	
	public final ConfigurableApplicationContext getApplicationContext() {
		// lazy load, in case startup() has not yet been called.
		if (applicationContext == null) {
			try {
				startup(DEFAULT_CONTEXT_RESOURCES);
			}
			catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug("Caught exception while retrieving the ApplicationContext for application ["
							+ getClass().getName() + "].", e);
				}
			}
		}
		return this.applicationContext;
	}
}
