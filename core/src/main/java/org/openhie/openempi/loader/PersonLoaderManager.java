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

import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.service.PersonQueryService;

public interface PersonLoaderManager
{
	public void setupConnection(Map<String,Object> properties);
	
	public Person addPerson(Person person) throws ApplicationException;
	
	public PersonQueryService getPersonQueryService();
	
	public Map<String,Object> getPropertyMap();
	
	public void shutdownConnection();
}
