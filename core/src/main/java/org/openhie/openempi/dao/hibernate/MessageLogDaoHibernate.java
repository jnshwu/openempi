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
import org.hibernate.criterion.Expression;
import org.openhie.openempi.dao.MessageLogDao;
import org.openhie.openempi.model.MessageLogEntry;
import org.openhie.openempi.model.MessageType;
import org.springframework.orm.hibernate3.HibernateCallback;

public class MessageLogDaoHibernate extends UniversalDaoHibernate implements MessageLogDao
{
	@SuppressWarnings("unchecked")
	public List<MessageType> getMessageTypes() {
		return getHibernateTemplate().find("from MessageType m order by upper(m.messageTypeName)");	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MessageLogEntry> filterMessageLogEntries(final Date startDate, final Date endDate, final List<Integer> messageTypeCodes, 
			final int firstResult, final int maxResults) {
		if (startDate == null && endDate == null && messageTypeCodes == null) {
			return new java.util.ArrayList<MessageLogEntry>();
		}

		return (List<MessageLogEntry>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
	
//				String prefix = "select messageLogId, dateReceived, substring(incomingMessage, 1, 10)," +
//						"substring(outgoingMessage, 1, 10), incomingMessageType, outgoingMessageType " +
				String prefix = "from MessageLogEntry where 1=1 ";
				Query query = buildFilterQuery(session, startDate, endDate, messageTypeCodes, prefix, " order by dateReceived desc");
		        List<MessageLogEntry> messages = (List<MessageLogEntry>)
		        		query
		        			.setFirstResult(firstResult)
		        			.setMaxResults(maxResults)
		        			.list();
		        log.trace("Search for message log entries found " + messages.size() + " entries.");       
		        
		        return messages;
			}
		});		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getMessageLogEntryCount(final Date startDate, final Date endDate, final List<Integer> messageTypeCodes) {
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = buildFilterQuery(session, startDate, endDate, messageTypeCodes, "select count(*) from MessageLogEntry where 1=1 ", "");
				int eventCount = ((Long) query.uniqueResult()).intValue();
				return eventCount;
			}
		});	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MessageLogEntry getMessageLogEntry(final Integer messageLogId) {
		if (messageLogId == null) {
			return null;
		}
		
		return (MessageLogEntry) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<MessageLogEntry> entries = session.createCriteria(MessageLogEntry.class)
						.add(Expression.idEq(messageLogId))
						.list();
				if (entries.size() == 0) {
					return null;
				}
				return entries.get(0);
			}});
	}

	public MessageLogEntry saveMessageLogEntry(MessageLogEntry entry) {
        log.debug("Saving the message log entry: " + entry);
        try {
	        getHibernateTemplate().saveOrUpdate(entry);
	        getHibernateTemplate().flush();
        } catch (Exception e) {
        	log.error("Exception while logging message: " + e, e);
        }
        return entry;
	}
	

	private Query buildFilterQuery(Session session, final Date startDate, final Date endDate, final List<Integer> messageTypeCodes, String prefix, String postfix) {
		StringBuffer query = new StringBuffer(prefix);
		if (startDate != null) {
			query.append(" and dateReceived >= :startDate");
		}
		if (endDate != null) {
			query.append(" and dateReceived <= :endDate");
		}
		if (messageTypeCodes != null && messageTypeCodes.size() > 0) {
			query.append(" and incomingMessageType.messageTypeCd IN (:codes)");
		}
		query.append(postfix);
		Query q = session.createQuery(query.toString());
		if (startDate != null) {
			q.setTimestamp("startDate", startDate);
		}
		if (endDate != null) {
			q.setTimestamp("endDate", endDate);
		}
		if (messageTypeCodes != null && messageTypeCodes.size() > 0) {
			q.setParameterList("codes", messageTypeCodes);
		}
		return q;
	}
}
