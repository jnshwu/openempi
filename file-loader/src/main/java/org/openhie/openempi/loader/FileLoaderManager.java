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
package org.openhie.openempi.loader;

import java.io.File;

import org.apache.log4j.Logger;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.profiling.DataProfilerLoader;
import org.openhie.openempi.util.BaseSpringApp;

public class FileLoaderManager extends BaseSpringApp
{
	protected static Logger log = Logger.getLogger(FileLoaderManager.class);
	private PersonLoaderManager personLoaderMgr;
	private FileLoader loader;

	private boolean skipHeaderLine = Boolean.FALSE;
	
	public FileLoaderManager() {
		
	}
	
	public void setUp() {
		setUp(null);
	}
	
	public void setUp(java.util.Map<String,Object> map) {
		startup();
		personLoaderMgr = (PersonLoaderManager) Context.getApplicationContext().getBean("personLoaderManager");
		personLoaderMgr.setupConnection(map);
	}
	
	public String loadFile(String filename, String loaderAlias) {
		 File file = new File(filename);
		 log.debug("Loading file " + file.getAbsolutePath());
		 if (!file.isFile() || !file.canRead()) {
			 log.error("Input file is not available.");
			 throw new RuntimeException("Input file " + filename + " is not readable.");
		 }
		 
		 loader = FileLoaderFactory.getFileLoader(Context.getApplicationContext(), loaderAlias);
		 loader.setPersonLoaderManager(personLoaderMgr);
		 loader.init();
		 return loader.parseFile(skipHeaderLine, file);
	}

	public String dataProfile(String filename, Integer userFileId) {
		 File file = new File(filename);
		 log.debug("Loading file " + file.getAbsolutePath());
		 if (!file.isFile() || !file.canRead()) {
			 log.error("Input file is not available.");
			 throw new RuntimeException("Input file " + filename + " is not readable.");
		 }
		
		 DataProfilerLoader dataProfilerLoader = new DataProfilerLoader();			
		 return dataProfilerLoader.parseFile(file, userFileId);
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			usage();
			System.exit(-1);
		}
		
		long startTime = System.currentTimeMillis();
		String filename = args[0];
		String loaderAlias = args[1];
		Boolean isImport = Boolean.FALSE;
		if (args.length > 2 && args[2] != null && args[2].equalsIgnoreCase("true")) {
			isImport = Boolean.TRUE;
		}
		String mappingFile = "";
		if (args.length > 3 && args[3] != null) {
			mappingFile = args[3];
		}
		Boolean previewOnly = Boolean.FALSE;
		if (args.length > 4 && args[4] != null && args[4].equalsIgnoreCase("true")) {
			previewOnly = Boolean.TRUE;
		}
		log.info("Loading the data file " + filename + " using loader " + loaderAlias);

		java.util.HashMap<String,Object> map = new java.util.HashMap<String,Object>();
		Context.startup();
		Context.authenticate("admin", "admin");
		map.put("context", Context.getApplicationContext());
		map.put("isImport", isImport);
		map.put("mappingFile", mappingFile);
		map.put("previewOnly", previewOnly);
		try {
			FileLoaderManager fileLoaderManager = new FileLoaderManager();
			fileLoaderManager.setUp(map);
			fileLoaderManager.loadFile(filename, loaderAlias);
			fileLoaderManager.shutdown();
			
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
		    log.debug("Elapsed time for file load: " + elapsedTime);
			
		} catch (Throwable t) {
			log.error("Got an exception: " + t);
		}
	}

	public java.lang.Boolean getSkipHeaderLine() {
		return skipHeaderLine;
	}

	public void setSkipHeaderLine(java.lang.Boolean skipHeaderLine) {
		this.skipHeaderLine = skipHeaderLine;
	}	
	
	public void shutdown() {
		loader.shutdown();
		super.shutdown();
	}

	public static void usage() {
		System.out.println("Usage: " + FileLoaderManager.class.getName() + " <file-to-load> <loader-alias> [<import-data-boolean-flag>] [mapping-file] [preview-only]");
	}
}
