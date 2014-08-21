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
package org.openhie.openempi.dao;

import java.util.Date;
import java.util.List;

import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.User;

import com.ibm.icu.util.Calendar;

public class AuditEventDaoTest extends BaseDaoTestCase
{
	private AuditEventDao auditEventDao;
	private PersonDao personDao;
	private UserDao userDao;
	
	public void testFilterAuditEvents() {
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(2000, 7, 1);
			Date startDate = cal.getTime();
			cal.set(2012, 10, 1);
			Date endDate = cal.getTime();
			List<Integer> auditTypes = new java.util.ArrayList<Integer>();
			auditTypes.add(7);
			auditTypes.add(1);

			int totalCount = auditEventDao.getAuditEventCount(startDate, endDate, auditTypes);
			assertTrue("Must find some events.", totalCount >= 0);
			if (totalCount > 0) {
				int blockSize = 1000;
				int index = 0;
				while (index < totalCount) {
					List<AuditEvent> events = auditEventDao.filterAuditEventsPaged(startDate, endDate, auditTypes, index, blockSize);
//					for (AuditEvent event : events) {
//						System.out.println("Found audit event : " + event);
//					}
					System.out.println("Obtained chunk of audit events of size: " + events.size());
					index += events.size();
				}
			}
			List<AuditEvent> events = auditEventDao.filterAuditEvents(startDate, endDate, auditTypes);
			
			events = auditEventDao.filterAuditEvents(null, null, null);
			assertTrue("Returned an empty list with no criteria successfully.", events.size() == 0);
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void testGetAllAuditEventType() {
		try {
			List<AuditEventType> allTypes = auditEventDao.getAll(AuditEventType.class);
			for (AuditEventType auditEventType : allTypes) {
				System.out.println("Found audit event type: " + auditEventType);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void testGetAuditEventTypeByCode() {
		try {
			List<AuditEventType> allTypes = auditEventDao.getAll(AuditEventType.class);
			for (AuditEventType auditEventType : allTypes) {
				AuditEventType auditEventTypeFound = auditEventDao.getAuditEventTypeByCode(auditEventType.getAuditEventTypeCode());
				assertNotNull(auditEventTypeFound);
				System.out.println("Found audit event type: " + auditEventTypeFound.getAuditEventTypeName() + " searching by code " + 
						auditEventType.getAuditEventTypeCode());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testAddAuditEvent() {
		AuditEventType type = auditEventDao.getAuditEventTypeByCode(AuditEventType.OBTAIN_UNIQUE_IDENTIFIER_DOMAIN_EVENT_TYPE);
		User user = (User) userDao.loadUserByUsername("admin");
		assertNotNull(type);
		assertNotNull(user);
		AuditEvent auditEvent = new AuditEvent(new java.util.Date(), type, "Testing the generation of an audit event", user);
		auditEventDao.save(auditEvent);
		System.out.println("Saved the audit event " + auditEvent);
		List<AuditEvent> auditEvents = (List<AuditEvent>) auditEventDao.getAll(AuditEvent.class);
		for (AuditEvent auditEventFound : auditEvents) {
			System.out.println("Found the audit event: " + auditEventFound.getAuditEventDescription());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testGetAllAuditEvents() {
		List<AuditEvent> auditEvents = auditEventDao.getAll(AuditEvent.class);
		for (AuditEvent auditEvent : auditEvents) {
			System.out.println("Found the audit event: " + auditEvent.getAuditEventDescription());
		}
	}

	public AuditEventDao getAuditEventDao() {
		return auditEventDao;
	}

	public void setAuditEventDao(AuditEventDao auditEventDao) {
		this.auditEventDao = auditEventDao;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
