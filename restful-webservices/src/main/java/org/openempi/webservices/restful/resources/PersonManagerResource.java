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
package org.openempi.webservices.restful.resources;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openempi.webservices.restful.model.IdentifierDomainAttributeRequest;
import org.openempi.webservices.restful.model.MergePersonsRequest;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.ReviewRecordPair;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;


@Path("/person-manager-resource")
public class PersonManagerResource
{
	@PUT
	@Path("/addPerson")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person addPerson(Person person) {
		PersonManagerService manager = getPersonManagerService();
		setPersonReferencesOnPersonIdentifier(person);
		try {
			person = manager.addPerson(person);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}
		return person;
	}

	@PUT
	@Path("/updatePerson")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void updatePerson(Person person) {
		setPersonReferencesOnPersonIdentifier(person);
		PersonManagerService manager = getPersonManagerService();
		try {
			manager.updatePerson(person);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.NOT_MODIFIED);
		}
	}

	@PUT
	@Path("/updatePersonById")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person updatePersonById(Person person) {
		PersonManagerService manager = getPersonManagerService();
		setPersonReferencesOnPersonIdentifier(person);
		try {
			person = manager.updatePersonById(person);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.NOT_MODIFIED);
		}
		return person;
	}

	@PUT
	@Path("/deletePerson")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void deletePerson(PersonIdentifier personIdentifier) {
		PersonManagerService manager = getPersonManagerService();
		try {
			manager.deletePerson(personIdentifier);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

    @POST
    @Path("/removePersonById")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void removePersonById(@QueryParam("personId") Integer personId) {
        PersonManagerService manager = getPersonManagerService();
        try {
            manager.removePerson(personId);
        } catch (ApplicationException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

	@PUT
	@Path("/mergePersons")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void mergePersons(MergePersonsRequest mergePersonsRequest) {
		PersonManagerService manager = getPersonManagerService();
		try {
			manager.mergePersons(mergePersonsRequest.getRetiredIdentifier(), mergePersonsRequest.getSurvivingIdentifer());
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@PUT
	@Path("/deletePersonById")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void deletePersonById(Person person) {
		setPersonReferencesOnPersonIdentifier(person);
		PersonManagerService manager = getPersonManagerService();
		try {
			manager.deletePersonById(person);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@PUT
	@Path("/importPerson")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person importPerson(Person person) {
		PersonManagerService manager = getPersonManagerService();
		setPersonReferencesOnPersonIdentifier(person);
		try {
			person = manager.importPerson(person);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}
		return person;
	}

	@PUT
	@Path("/addIdentifierDomain")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public IdentifierDomain addIdentifierDomain(IdentifierDomain identifierDomain) {
		PersonManagerService manager = getPersonManagerService();
		try {
			identifierDomain = manager.addIdentifierDomain(identifierDomain);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}
		return identifierDomain;
	}

	@PUT
	@Path("/updateIdentifierDomain")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public IdentifierDomain updateIdentifierDomain(IdentifierDomain identifierDomain) {
		PersonManagerService manager = getPersonManagerService();
		try {
			identifierDomain = manager.updateIdentifierDomain(identifierDomain);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		return identifierDomain;
	}

	@PUT
	@Path("/deleteIdentifierDomain")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void deleteIdentifierDomain(IdentifierDomain identifierDomain) {
		PersonManagerService manager = getPersonManagerService();
		try {
			manager.deleteIdentifierDomain(identifierDomain);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

	@PUT
	@Path("/obtainUniqueIdentifierDomain")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public IdentifierDomain obtainUniqueIdentifierDomain(String universalIdentifierTypeCode) {
		PersonManagerService manager = getPersonManagerService();
		return manager.obtainUniqueIdentifierDomain(universalIdentifierTypeCode);
	}

	@PUT
	@Path("/addIdentifierDomainAttribute")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public IdentifierDomainAttribute addIdentifierDomainAttribute(IdentifierDomainAttributeRequest identifierDomainAttributeRequest ) {
		PersonManagerService manager = getPersonManagerService();
		IdentifierDomainAttribute identifierDomainAttribute = null;
		
		identifierDomainAttribute = manager.addIdentifierDomainAttribute(identifierDomainAttributeRequest.getIdentifierDomain(), 
																		identifierDomainAttributeRequest.getAttributeName(), 
																		identifierDomainAttributeRequest.getAttributeValue());

		return identifierDomainAttribute;
	}

	@PUT
	@Path("/updateIdentifierDomainAttribute")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void updateIdentifierDomainAttribute(IdentifierDomainAttribute identifierDomainAttribute) {
		PersonManagerService manager = getPersonManagerService();

		manager.updateIdentifierDomainAttribute(identifierDomainAttribute);
	}

	@PUT
	@Path("/removeIdentifierDomainAttribute")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void removeIdentifierDomainAttribute(IdentifierDomainAttribute identifierDomainAttribute) {
		PersonManagerService manager = getPersonManagerService();

		manager.removeIdentifierDomainAttribute(identifierDomainAttribute);
	}

	@PUT
	@Path("/addReviewRecordPair")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void addReviewRecordPair(ReviewRecordPair recordPair) {
		PersonManagerService manager = getPersonManagerService();
		try {
			manager.addReviewRecordPair(recordPair);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

	@PUT
	@Path("/matchReviewRecordPair")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void matchReviewRecordPair(ReviewRecordPair recordPair) {
		PersonManagerService manager = getPersonManagerService();
		try {
			manager.matchReviewRecordPair(recordPair);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

	@PUT
	@Path("/deleteReviewRecordPair")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void deleteReviewRecordPair(ReviewRecordPair recordPair) {
		PersonManagerService manager = getPersonManagerService();

		manager.deleteReviewRecordPair(recordPair);
	}

	// This method deletes all the review record pair entries from the repository
	@PUT
	@Path("/deleteReviewRecordPairs")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void deleteReviewRecordPairs() {
		PersonManagerService manager = getPersonManagerService();

		manager.deleteReviewRecordPairs();
	}

	@PUT
	@Path("/linkPersons")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void linkPersons(PersonLink personLink) {
		PersonManagerService manager = getPersonManagerService();
		try {
			manager.linkPersons(personLink);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

	@PUT
	@Path("/unlinkPersons")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public void unlinkPersons(PersonLink personLink) {
		PersonManagerService manager = getPersonManagerService();
		try {
			manager.unlinkPersons(personLink);
		} catch (ApplicationException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
/*
	@POST
    @Path("/removeNotifications")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String removeNotifications(List<IdentifierUpdateEvent> identifierUpdateEvents) {
        PersonManagerService manager = getPersonManagerService();

       Integer count;

        try {
           count =  manager.removeNotifications(identifierUpdateEvents);
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Integer.toString(count);
    }
*/
    @POST
    @Path("/removeNotificationById")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void deleteNotification(
    		@QueryParam("notificationId") List<Long> notificationIds) {

        if (notificationIds == null && notificationIds.size() == 0) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

		PersonQueryService query = getPersonQueryService();
        PersonManagerService manager = getPersonManagerService();

		for (Long id: notificationIds) {
	        try {
	            IdentifierUpdateEvent notification = query.findIdentifierUpdateEvent(id);
	            if (notification == null) {
	                throw new WebApplicationException(Response.Status.NOT_FOUND);
	            }
	            manager.removeNotification(notification);

	        } catch (Exception e) {
	            throw new WebApplicationException(Response.Status.CONFLICT);
	        }
		}
    }

	/**
	 * During marshalling/unmarshalling the reference from PersonIdentifier to Person is lost since
	 * otherwise a cycle is created in the serialization graph that Jaxb doesn't know how to deal with
	 * We need to fix the reference here before we go any deeper.
	 * @param person
	 */
	private void setPersonReferencesOnPersonIdentifier(Person person) {
		if (person != null && person.getPersonIdentifiers() != null) {
			for (PersonIdentifier pi : person.getPersonIdentifiers()) {
				pi.setPerson(person);
			}
		}
	}

	private PersonQueryService getPersonQueryService() {
		return org.openhie.openempi.context.Context.getPersonQueryService();
	}

	private PersonManagerService getPersonManagerService() {
		return org.openhie.openempi.context.Context.getPersonManagerService();
	}
}
