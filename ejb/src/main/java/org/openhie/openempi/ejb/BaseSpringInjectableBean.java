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
package org.openhie.openempi.ejb;

import javax.ejb.CreateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.ejb.util.CustomJndiBeanFactoryLocator;
import org.openhie.openempi.model.User;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.ejb.support.AbstractStatelessSessionBean;

public abstract class BaseSpringInjectableBean extends AbstractStatelessSessionBean 
{
	private static final long serialVersionUID = 2898872461151599058L;
	private static java.util.Map<String,Boolean> initializationMap = new java.util.HashMap<String,Boolean>();
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	protected void onEjbCreate() throws CreateException {
		log.debug("onEjbCreate was invoked via SpringInjectionInterceptor. The bean factory is " + getBeanFactory());
		Context.startup();
	}
	
	public void init() {
		if (!isInitialized(getClass())) {
			log.debug("Initialization of EJB context for bean: " + getClass().getName());
			setBeanFactoryLocator(new CustomJndiBeanFactoryLocator());
			try {
				ejbCreate();
				setInitialized(getClass(), true);
			} catch (CreateException e) {
				log.error("Failed while initializing the bean: " + e, e);
			} catch (Throwable t) {
				log.error("Failed while initializing the bena: " + t, t);
			}
		}
	}
	
	
	@Override
	public void setBeanFactoryLocator(BeanFactoryLocator beanFactoryLocator) {
		log.debug("Setting the custom bean factory locator.");
		super.setBeanFactoryLocator(new CustomJndiBeanFactoryLocator());
	}

	public User isSessionValid(String sessionKey) {
		return Context.authenticate(sessionKey);
	}

	public boolean isInitialized(Class<? extends BaseSpringInjectableBean> className) {
		Boolean flag = initializationMap.get(className.getName());
		if (flag == null || !flag.booleanValue()) {
			return false;
		} else {
			return true;
		}
	}

	public synchronized void setInitialized(Class<? extends BaseSpringInjectableBean> className, boolean flag) {
		initializationMap.put(className.getName(), Boolean.TRUE);
	}
}
