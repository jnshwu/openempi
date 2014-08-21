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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Race;

public abstract class AbstractFileLoader implements FileLoader
{
	protected Logger log = Logger.getLogger(AbstractFileLoader.class);

	public final static String MAPPING_FILE = "mappingFile";
	public final static String LOADER_ALIAS = "flexibleDataLoader";
	public final static String SKIP_HEADER_LINE = "skipHeaderLine";
	public final static String IS_IMPORT = "isImport";
	public final static String PREVIEW_ONLY = "previewOnly";
	
	public final static String SKIP_HEADER_LINE_DISPLAY = "Skip Header Line";
	public final static String IS_IMPORT_DISPLAY = "Is Import";
	public final static String MAPPING_FILE_DISPLAY = "Mapping File Name";
	public final static String PREVIEW_ONLY_DISPLAY = "Only Preview Import";
	
	private PersonLoaderManager personManager;
	
	private Map<String,Race> raceCacheByName = new HashMap<String,Race>();
	private Map<String,Race> raceCacheByCode = new HashMap<String,Race>();
	private Map<String,Gender> genderCacheByCode = new HashMap<String,Gender>();
	private Map<String,Gender> genderCacheByName = new HashMap<String,Gender>();
	
	public void init() {
		log.debug("Initializing the file loader.");
	}
	
	public void shutdown() {
	}
	
	public void loadPerson(Person person) {
		log.debug("Attempting to load person entry " + person);
		try {
			Boolean previewOnly = (Boolean) getParameter(PREVIEW_ONLY);
			if (previewOnly == null || !previewOnly.booleanValue()) {
				personManager.addPerson(person);
			}
		} catch (Exception e) {
			log.error("Failed while adding person entry to the system. Error: " + e, e);
			if (e.getCause() instanceof org.hibernate.exception.SQLGrammarException) {
				org.hibernate.exception.SQLGrammarException sge = (org.hibernate.exception.SQLGrammarException) e;
				log.error("Cause is: " + sge.getSQL());
			}
		}
	}

	public Race findRaceByCode(String raceCode) {
		Race race = raceCacheByCode.get(raceCode);
		if (race != null) {
			return race;
		}
		race = personManager.getPersonQueryService().findRaceByCode(raceCode);
		if (race != null) {
			raceCacheByCode.put(raceCode, race);
		}
		return race;
	}
	
	public Race findRaceByName(String raceName) {
		log.trace("Looking up race by race name: " + raceName);
		Race race = raceCacheByName.get(raceName);
		if (race != null) {
			log.trace("Looking up race by race name: " + raceName);
			return race;
		}
		race = personManager.getPersonQueryService().findRaceByName(raceName);
		if (race != null) {
			raceCacheByName.put(raceName, race);
		}
		return race;
	}
	
	public Gender findGenderByCode(String genderCode) {
		Gender gender = genderCacheByCode.get(genderCode);
		if (gender != null) {
			return gender;
		}
		gender = personManager.getPersonQueryService().findGenderByCode(genderCode);
		if (gender != null) {
			genderCacheByCode.put(genderCode, gender);
		}
		return gender;
	}

	public Gender findGenderByName(String genderName) {
		Gender gender = genderCacheByName.get(genderName);
		if (gender != null) {
			return gender;
		}
		gender = personManager.getPersonQueryService().findGenderByName(genderName);
		if (gender != null) {
			genderCacheByName.put(genderName, gender);
		}
		return gender;
	}

	public String parseFile(boolean skipHeaderLine, File file) {
		return parseFile(skipHeaderLine, file, true);
	}
	
	public void setParameter(String parameterName, Object value) {
		if (getPersonLoaderManager() == null) {
			log.warn("Unable to set file loader parameter because the loader manager has not been injected yet.");
			return;
		}
		log.info("Setting the file loader parameter: " + parameterName + " to value: " + value);
		getPersonLoaderManager().getPropertyMap().put(parameterName, value);
	}
	
	public String getParameterAsString(String parameterName) {
		Object value = getParameter(parameterName);
		if (value != null) {
			if (value instanceof String) {
				return (String) value;
			}
			return value.toString();
		}
		return "";
	}
	
	public Object getParameter(String parameterName) {
		if (getPersonLoaderManager() == null) {
			log.warn("Unable to get file loader parameter because the loader manager has not been injected yet.");
			return null;
		}
		return getPersonLoaderManager().getPropertyMap().get(parameterName);
	}
	
	public String parseFile(boolean skipHeaderLine, File file, boolean populateCustomFields) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			log.error("Unable to read the input file. Error: " + e);
			throw new RuntimeException("Unable to read the input file.");
		}
		
		if (getParameter(SKIP_HEADER_LINE) != null) {
			skipHeaderLine = (Boolean) getParameter(SKIP_HEADER_LINE);
		}
		try {
			boolean done = false;
			int lineIndex=0;
			int rowsImported = 0;
			while (!done) {
				String line = reader.readLine();
				if (line == null) {					
					if(skipHeaderLine && lineIndex > 0) {
						lineIndex--;
					}
					done = true;
					continue;
				}
				
				// Skip the first line since its a header.
				if (lineIndex == 0 && skipHeaderLine) {
					lineIndex++;
					continue;
				}
				
				boolean imported = processLine(line, lineIndex++, populateCustomFields);
				if (imported) {
					rowsImported++;
				}
			}			
			reader.close();
			return "" + lineIndex + "-" + rowsImported;				
			
		} catch (IOException e) {
			log.error("Failed while loading the input file. Error: " + e);
			throw new RuntimeException("Failed while loading the input file.");
		}
	}

	public PersonLoaderManager getPersonLoaderManager() {
		return this.personManager;
	}
	
	public void setPersonLoaderManager(PersonLoaderManager personManager) {
		this.personManager = personManager; 
	}
	
	protected abstract boolean processLine(String line, int lineIndex);
	
	protected boolean processLine(String line, int lineIndex, boolean populateCustomFields) {
		return processLine(line, lineIndex);
	}
	
}
