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
package org.openhie.openempi.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.openhie.openempi.dao.AuditEventDao;
import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;
import org.springframework.orm.hibernate3.HibernateCallback;

public class AuditEventDaoHibernate extends UniversalDaoHibernate implements AuditEventDao
{
	public List<AuditEvent> getAuditEventByType(AuditEventType auditEventType) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public AuditEventType getAuditEventTypeByCode(String auditEventTypeCode) {
		if (auditEventTypeCode == null || auditEventTypeCode.length() == 0) {
			return null;
		}
		String query = "from AuditEventType aet where aet.auditEventTypeCode = '" + auditEventTypeCode + "'";
        List<AuditEventType> values = getHibernateTemplate().find(query);
        log.trace("Search for audit event types by type: " + auditEventTypeCode + " found " + values.size() + " entries.");
        return values.get(0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getAuditEventCount(final Date startDate, final Date endDate, final List<Integer> auditEventTypeCodes) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = buildFilterQuery(session, startDate, endDate, auditEventTypeCodes, "select count(*) from AuditEvent where 1=1 ", "");
				int eventCount = ((Long) query.uniqueResult()).intValue();
		
				return eventCount;
			}
		});
	}
	
	public List<AuditEvent> filterAuditEvents(final Date startDate, final Date endDate, final List<Integer> auditEventTypeCodes) {
		return this.filterAuditEventsPaged(startDate, endDate, auditEventTypeCodes, 0, 0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AuditEvent> filterAuditEventsPaged(final Date startDate, final Date endDate, final List<Integer> auditEventTypeCodes, final int firstResult, final int maxResults) {
		if (startDate == null && endDate == null && auditEventTypeCodes == null) {
			return new java.util.ArrayList<AuditEvent>();
		}

		return (List<AuditEvent>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
	
				Query query = buildFilterQuery(session, startDate, endDate, auditEventTypeCodes, "from AuditEvent where 1=1 ", " order by dateCreated desc");
		        List<AuditEvent> events = (List<AuditEvent>)
		        		query
		        			.setFirstResult(firstResult)
		        			.setMaxResults(maxResults)
		        			.list();
		        log.trace("Search for audit event found " + events.size() + " entries.");       
		        
		        return events;
			}
		});		
	}

	private Query buildFilterQuery(Session session, final Date startDate, final Date endDate, final List<Integer> auditEventTypeCodes, String prefix, String postfix) {
		StringBuffer query = new StringBuffer(prefix);
		if (startDate != null) {
			query.append(" and dateCreated >= :startDate");
		}
		if (endDate != null) {
			query.append(" and dateCreated <= :endDate");
		}
		if (auditEventTypeCodes != null && auditEventTypeCodes.size() > 0) {
			query.append(" and auditEventType.auditEventTypeCd IN (:codes)");
		}
		query.append(postfix);
		Query q = session.createQuery(query.toString());
		if (startDate != null) {
			q.setTimestamp("startDate", startDate);
		}
		if (endDate != null) {
			q.setTimestamp("endDate", endDate);
		}
		if (auditEventTypeCodes != null && auditEventTypeCodes.size() > 0) {
//			StringBuffer sb = new StringBuffer();
//			for (int i=0; i < auditEventTypeCodes.size(); i++) {
//				sb.append(auditEventTypeCodes.get(i));
//				if (i < auditEventTypeCodes.size()-1) {
//					sb.append(",");
//				}
//			}
			q.setParameterList("codes", auditEventTypeCodes);
		}
		return q;
	}
}
