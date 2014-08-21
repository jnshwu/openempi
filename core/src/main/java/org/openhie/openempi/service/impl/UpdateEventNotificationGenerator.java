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
package org.openhie.openempi.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openhie.openempi.configuration.UpdateNotificationRegistrationEntry;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.model.IdentifierUpdateEntry;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.service.impl.PersonManagerServiceImpl.PersonState;

public class UpdateEventNotificationGenerator
{
	private Logger log = Logger.getLogger(getClass());
	
	private PersonDao personDao;
	List<UpdateNotificationRegistrationEntry> configurationEntries;
	
	public UpdateEventNotificationGenerator(PersonDao personDao, List<UpdateNotificationRegistrationEntry> configurationEntries) {
		this.personDao = personDao;
		this.configurationEntries = configurationEntries;
	}

	public void generateEvents(PersonState state) {
		
		if (configurationEntries == null || configurationEntries.size() == 0) {
			return;
		}
		
		// Add to person record identifiers, identifiers from the linked records
		expandIdentifiersWithLinkData(state);
		log.trace("Updated the state to: " + state);
		
		for (UpdateNotificationRegistrationEntry entry : configurationEntries) {
			// Only need to generate update notifications if there has been a change
			// in the domain of interest of the user expecting notifications.
			if (!changeInDomainOfInterest(entry, state)) {
				continue;
			}
			IdentifierUpdateEvent event = new IdentifierUpdateEvent();
			if (state.getPreIdentifiers().size() > state.getPostIdentifiers().size()) {
				event.setTransition(IdentifierUpdateEvent.LEAVE_TRANSITION);
			} else {
				event.setTransition(IdentifierUpdateEvent.JOIN_TRANSITION);
			}
			event.setSource(state.getSource());
			event.setPreUpdateIdentifiers(generateEntries(state.getPreIdentifiers()));
			event.setPostUpdateIdentifiers(generateEntries(state.getPostIdentifiers()));
			event.setDateCreated(new Date());
			event.setUpdateRecipient(entry.getUser());
			log.debug("Saving identifier update event: " + event);
			personDao.addIdentifierUpdateEvent(event);
		}
	}

	private Set<IdentifierUpdateEntry> generateEntries(Set<PersonIdentifier> identifiers) {
		Set<IdentifierUpdateEntry> entries = new HashSet<IdentifierUpdateEntry>();
		for (PersonIdentifier identifier : identifiers) {
			IdentifierUpdateEntry entry = new IdentifierUpdateEntry();
			entry.setIdentifier(identifier.getIdentifier());
			entry.setIdentifierDomain(identifier.getIdentifierDomain());
			entries.add(entry);
		}
		return entries;
	}

	private void expandIdentifiersWithLinkData(PersonState state) {
		Set<PersonIdentifier> preIdentifiers = expandIdentifiers(state.getPersonId(), state.getPreIdentifiers(), state.getPreLinks());
		state.setPreIdentifiers(preIdentifiers);
		Set<PersonIdentifier> postIdentifiers = expandIdentifiers(state.getPersonId(), state.getPostIdentifiers(), state.getPostLinks());
		state.setPostIdentifiers(postIdentifiers);
	}

	private Set<PersonIdentifier> expandIdentifiers(int personId, Set<PersonIdentifier> ids, Set<PersonLink> links) {
		if (ids == null) {
			ids = new HashSet<PersonIdentifier>();
		}
		if (links == null || links.size() == 0) {
			return ids;
		}
		for (PersonLink link : links) {
			if (link.getPersonLeft().getPersonId() != personId && link.getPersonLeft().getPersonIdentifiers() != null 
					&& link.getPersonLeft().getPersonIdentifiers().size() > 0) {
				for (PersonIdentifier identifier : link.getPersonLeft().getPersonIdentifiers()) {
					if (!containsIdentifier(identifier, ids)) {
						ids.add(identifier);
					}
				}
			}
			if (link.getPersonRight().getPersonId() != personId && link.getPersonRight().getPersonIdentifiers() != null
					&& link.getPersonRight().getPersonIdentifiers().size() > 0) {
				for (PersonIdentifier identifier : link.getPersonRight().getPersonIdentifiers()) {
					if (!containsIdentifier(identifier, ids)) {
						ids.add(identifier);
					}
				}
			}
		}
		return ids;
	}

	private boolean containsIdentifier(PersonIdentifier identifier, Set<PersonIdentifier> ids) {
		if (ids == null || ids.size() == 0) {
			return false;
		}
		for (PersonIdentifier id : ids) {
			if (identifier.getIdentifier().equalsIgnoreCase(id.getIdentifier()) && 
					identifier.getIdentifierDomain().getIdentifierDomainName()
						.equalsIgnoreCase(id.getIdentifierDomain().getIdentifierDomainName())) {
				return true;
			}
		}
		return false;
	}

	private boolean changeInDomainOfInterest(UpdateNotificationRegistrationEntry entry, PersonState state) {
		Integer domainOfInterest = entry.getIdentifierDomain().getIdentifierDomainId();
		String beforeIdentifier = getIdentifierInDomainOfInterest(domainOfInterest, state.getPreIdentifiers());
		String afterIdentifier = getIdentifierInDomainOfInterest(domainOfInterest, state.getPostIdentifiers());
		// There are two cases:
		// 1. no identifier in the domain of interest before but one in it afterwards
		// 2. an identifier in the domain of interest before but different one afterwards
		//
		if (beforeIdentifier == null && afterIdentifier != null) {
			return true;
		}
		
		
		if (beforeIdentifier == null && afterIdentifier == null) {
			return false;
		}
		
		
		if (!beforeIdentifier.equalsIgnoreCase(afterIdentifier)) {
			return true;
		}
		return false;
	}

	private String getIdentifierInDomainOfInterest(Integer domainOfInterest, Set<PersonIdentifier> identifiers) {
		for (PersonIdentifier identifier : identifiers) {
			if (identifier.getIdentifierDomain().getIdentifierDomainId().intValue() == domainOfInterest.intValue()) {
				return identifier.getIdentifier();
			}
		}
		return null;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}
}
