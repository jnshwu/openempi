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

import org.apache.log4j.Logger;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.context.UserContext;
import org.openhie.openempi.model.Person;

public class RecordLoaderTask implements Runnable
{
	private Logger log = Logger.getLogger(getClass());
	protected PersonLoaderManager personLoaderManager;
	private UserContext userContext;
	private Person person;
	
	public RecordLoaderTask(PersonLoaderManager personLoaderManager, Person person, UserContext userContext) {
		this.personLoaderManager = personLoaderManager;
		this.person = person;
		this.userContext = userContext;
	}
	
	public void run() {
		log.debug("With User Context: " + userContext + " attempting to load person entry " + person);
		Context.setUserContext(userContext);
		try {
			personLoaderManager.addPerson(person);
		} catch (Exception e) {
			log.error("Failed while adding person entry to the system. Error: " + e, e);
			if (e.getCause() instanceof org.hibernate.exception.SQLGrammarException) {
				org.hibernate.exception.SQLGrammarException sge = (org.hibernate.exception.SQLGrammarException) e;
				log.error("Cause is: " + sge.getSQL());
			}
		}
	}
}
