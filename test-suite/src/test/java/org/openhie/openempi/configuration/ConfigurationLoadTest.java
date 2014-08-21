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
package org.openhie.openempi.configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Test;
import org.openhie.openempi.configuration.xml.MpiConfigDocument;
import org.openhie.openempi.configuration.xml.impl.MpiConfigDocumentImpl.MpiConfigImpl;
import org.openhie.openempi.context.Context;

public class ConfigurationLoadTest
{
	protected static final Log log = LogFactory.getLog(Configuration.class);
	
	@Test
	public void testLoadingOfConfigurationFile() {
		try {
			MpiConfigDocument configDocument = loadConfigurationFromSource();
			MpiConfigImpl mpiconfig = (MpiConfigImpl) configDocument.getMpiConfig();
			Object obj = mpiconfig.getFileLoaderConfiguration();
			System.out.println("Type is " + mpiconfig.getClass());
			log.debug("Admin Configuration is: " + configDocument.getMpiConfig().getAdminConfiguration());
			log.debug("Blocking Configuration is: " + configDocument.getMpiConfig().getBlockingConfiguration());
			log.debug("Custom Fields Configuration is: " + configDocument.getMpiConfig().getCustomFields());
			log.debug("File Loader Configuration is: " + configDocument.getMpiConfig().getFileLoaderConfiguration());
			log.debug("Global Identifier Configuration is: " + configDocument.getMpiConfig().getGlobalIdentifier());
			log.debug("Matching Configuration is: " + configDocument.getMpiConfig().getMatchingConfiguration());
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void validateConfiguration(MpiConfigDocument configuration) {

		// Set up the validation error listener.
		ArrayList<XmlError> validationErrors = new ArrayList<XmlError>();
		XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);

		// During validation, errors are added to the ArrayList for
		// retrieval and printing by the printErrors method.
		boolean isValid = configuration.validate(validationOptions);

		// Print the errors if the XML is invalid.
		if (!isValid)
		{
		    java.util.Iterator<XmlError> iter = validationErrors.iterator();
		    StringBuffer sb = new StringBuffer("MPI Configuration validation errors:\n");
		    while (iter.hasNext())
		    {
		    	sb.append(">> ").append(iter.next()).append("\n");
		    }
		    log.error("Invalid configuration file encountered. Errors are: " + sb);
//		    throw new RuntimeException("Unable to process the MPI configuration file: " + sb);
		}
	}

	private MpiConfigDocument loadConfigurationFromSource() throws XmlException, IOException {
		File file = getDefaultConfigurationFile();
		log.debug("Checking for presence of the configuration in file: " + file.getAbsolutePath());
		if (file.exists() && file.isFile()) {
			log.info("Loading configuration from file: " + file.getAbsolutePath());
			return MpiConfigDocument.Factory.parse(file);
		}
		
		URL fileUrl = Configuration.class.getResource("mpi-config.xml");
		if (fileUrl != null) {
			log.info("Loading configuration from URL: " + fileUrl);
			return MpiConfigDocument.Factory.parse(fileUrl);
		}
		return null;
	}
	
	private File getDefaultConfigurationFile() {
		File dir = new File(Context.getOpenEmpiHome() + "/conf");
		System.out.println(System.getProperty("OPENEMPI_HOME"));
		File file = new File(dir, "mpi-config.xml");
		return file;
	}
}
