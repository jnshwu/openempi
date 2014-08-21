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
package org.openhie.openempi.service;

import java.util.List;

import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;

public class AuditEventServiceTest extends BaseServiceTestCase
{
	public void testAddAuditEvent() {
		try {
			AuditEventService auditService = Context.getAuditEventService();
			AuditEvent event = auditService.saveAuditEvent(AuditEventType.OBTAIN_UNIQUE_IDENTIFIER_DOMAIN_EVENT_TYPE, "Testing the audit event service");
			log.debug("Generated the audit event: " + event.getAuditEventType());
			List<AuditEvent> events = auditService.getAllAuditEvents();
			for (AuditEvent auditEvent : events) {
				log.debug("Retrieved the audit event: " + auditEvent.getAuditEventType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
