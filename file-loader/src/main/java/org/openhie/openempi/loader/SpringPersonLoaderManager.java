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

import java.util.Map;

import org.apache.log4j.Logger;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;

public class SpringPersonLoaderManager implements PersonLoaderManager
{
	protected Logger log = Logger.getLogger(SpringPersonLoaderManager.class);
	
	private String username;
	private String password;
	private boolean isImport = Boolean.FALSE;
	private boolean previewOnly = Boolean.FALSE;
	private PersonManagerService personManagerService;
	private PersonQueryService personQueryService;
	private Map<String, Object> propertyMap;
	
	public void setupConnection(Map<String, Object> properties) {
		if (personManagerService == null) {
			personManagerService = Context.getPersonManagerService();
		}
		if (personQueryService == null) {
			personQueryService = Context.getPersonQueryService();
		}
		propertyMap = properties;
		if (properties != null) {
			Object previewOnlyFlag = properties.get("previewOnly");
			if (previewOnlyFlag != null  && previewOnlyFlag instanceof Boolean && previewOnlyFlag.equals(Boolean.TRUE)) {
				previewOnly = Boolean.TRUE;
			}
			Object isImportFlag = properties.get("isImport");
			if (isImportFlag != null  && isImportFlag instanceof Boolean && isImportFlag.equals(Boolean.TRUE)) {
				log.info("Will be doing an import instead of an add");
				isImport = true;
			}
		}
	}

	public Map<String,Object> getPropertyMap() {
		return propertyMap;
	}
	
	public void shutdownConnection() {
	}

	public Person addPerson(Person person) throws ApplicationException {
		if (previewOnly) {
			log.debug("Importing record: " + person);
			return person;
		}
		if (isImport) {
			return personManagerService.importPerson(person);
		} else {
			return personManagerService.addPerson(person);
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PersonManagerService getPersonManagerService() {
		return personManagerService;
	}

	public void setPersonManagerService(PersonManagerService personManagerService) {
		this.personManagerService = personManagerService;
	}

	public PersonQueryService getPersonQueryService() {
		return personQueryService;
	}

	public void setPersonQueryService(PersonQueryService personQueryService) {
		this.personQueryService = personQueryService;
	}

}
