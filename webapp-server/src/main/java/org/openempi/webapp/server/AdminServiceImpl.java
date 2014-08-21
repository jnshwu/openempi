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
package org.openempi.webapp.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openempi.webapp.client.AdminService;
import org.openhealthtools.openpixpdq.common.PixPdqConfigurationLoader;
import org.openhie.openempi.blocking.BlockingLifecycleObserver;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.service.PersonManagerService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class AdminServiceImpl extends AbstractRemoteServiceServlet implements AdminService
{
	private static boolean initialized = false;
	private PixPdqConfigurationLoader loader;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (!Context.isInitialized()) {
			log.error("The context did not initialize properly.");
			return;
		}
		if (Context.getConfiguration().getAdminConfiguration().isAutoStartPIXPDQ()) {
			startPixPdqServerLocal();
		}
	}

	@Override
	public void destroy() {
		stopPixPdqServerLocal();
		super.destroy();
	}

	public String startPixPdqServer() {
		log.debug("Starting the PIX/PDQ Service");		
		authenticateCaller();
		String message = startPixPdqServerLocal();
		return message;
	}

	private String startPixPdqServerLocal() {
		String message=null;
		loader = PixPdqConfigurationLoader.getInstance();
		loader.destroyAllActors();
		try {
			String[] propertyFiles = { "openpixpdq.properties" };
			loader.loadProperties(propertyFiles);
			File configurationFile = getConfigurationFile();
			log.debug("Using as configuration the file " + configurationFile.getAbsolutePath());
			loader.loadConfiguration(configurationFile.getAbsolutePath(), true);
			setInitialized(true);
		} catch (Exception e) {
			log.error("Unable to start the PIX/PDQ server due to : " + e, e);
			message = e.getMessage();
		}
		return message;
	}

	public String assignGlobalIdentifiers() {
		log.debug("Assign global identifiers for person entries without one.");
		
		authenticateCaller();
		String message=null;
		try {
			PersonManagerService personManagerService = Context.getPersonManagerService();
			personManagerService.assignGlobalIdentifier();
		} catch (Exception e) {
			log.error("Failed while trying to assign global identifiers to entries in the repository: " + e, e);
			message = e.getMessage();
		}
		return message;
	}

	private File getConfigurationFile() throws IOException {
		String openEmpiHome = Context.getOpenEmpiHome();
		if (openEmpiHome != null && openEmpiHome.length() > 0) {
			String filename = openEmpiHome + "/conf/IheActors.xml";
			log.info("Looking for PIX/PDQ configuration file at " + filename);
			File configFile = new File(filename);
			if (configFile != null && configFile.exists() && configFile.canRead()) {
				log.info("Using for PIX/PDQ configuration file at " + configFile.getAbsolutePath());
				return configFile;
			}			
		}
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("classpath:/IheActors.xml");
		File configurationFile = resource.getFile();
		log.info("Using for PIX/PDQ configuration file at " + configurationFile.getAbsolutePath());
		return configurationFile;
	}

	public String stopPixPdqServer() {
		log.debug("Stopping the PIX/PDQ Service");
		
		authenticateCaller();
		String message = stopPixPdqServerLocal();
		return message;
	}

	private String stopPixPdqServerLocal() {
		String message=null;
		if (loader != null) {
			loader.destroyAllActors();
		}
		return message;
	}

	public String initializeRepository() {
		log.debug("Initialize Repository");
		
		authenticateCaller();		
		String message=null;
		try {
			PersonManagerService personManagerService = Context.getPersonManagerService();
			personManagerService.initializeRepository();
		} catch (Exception e) {
			log.error("Failed while trying to initialize repository: " + e, e);
			message = e.getMessage();
		}
		return message;
	}

	public String linkAllRecordPairs() {
		log.debug("Linking all record pairs in the repository");
		
		authenticateCaller();		
		String message=null;
		try {
			Context.getPersonManagerService().linkAllRecordPairs();
		} catch (Exception e) {
			log.error("Failed while trying to initialize repository: " + e, e);
			message = e.getMessage();
		}
		return message;
	}

	public String initializeCustomConfiguration() {
		log.debug("Initialize Custom Configuration");
		
		authenticateCaller();		
		String message=null;
		try {
			PersonManagerService personManagerService = Context.getPersonManagerService();
			personManagerService.generateCustomFields();
		} catch (Exception e) {
			log.error("Failed while trying to initialize custom configuration: " + e, e);
			message = e.getMessage();
		}
		return message;
	}
	
	public String rebuildBlockingIndex() {
		log.debug("Rebuild Blocking Index");
		
		authenticateCaller();
		String message=null;
		try {			
			BlockingService blockingService = Context.getBlockingService();
			BlockingLifecycleObserver blockingLifecycle = (BlockingLifecycleObserver) blockingService;
			blockingLifecycle.rebuildIndex();
		} catch (Exception e) {
			log.error("Failed while trying to initialize repository: " + e, e);
			message = e.getMessage();
		}
		return message;
	}
	
	public static boolean isInitialized() {
		return initialized;
	}

	public static void setInitialized(boolean initialized) {
		AdminServiceImpl.initialized = initialized;
	}
}
