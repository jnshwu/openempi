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
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openhie.openempi.dao.DataProfileAttributeDao;
import org.openhie.openempi.model.DataProfileAttribute;
import org.openhie.openempi.model.DataProfileAttributeValue;
import org.springframework.orm.hibernate3.HibernateCallback;

public class DataProfileAttributeDaoHibernate extends UniversalDaoHibernate implements DataProfileAttributeDao
{
	private static final int MAX_STRING_VALUE_LENGTH = 1024;
	
	public DataProfileAttribute saveDataProfileAttribute(DataProfileAttribute dataProfileAttribute) {
		getHibernateTemplate().saveOrUpdate("org.openhie.openempi.model.DataProfileAttribute", dataProfileAttribute);
		log.debug("Finished saving the data profile attribute: " + dataProfileAttribute);
		return dataProfileAttribute;
	}

	public DataProfileAttributeValue saveDataProfileAttributeValue(DataProfileAttributeValue dataProfileAttributeValue) {
		if (dataProfileAttributeValue != null &&
				dataProfileAttributeValue.getAttributeValue() != null &&
				dataProfileAttributeValue.getAttributeValue().length() > MAX_STRING_VALUE_LENGTH) {
			dataProfileAttributeValue.setAttributeValue(dataProfileAttributeValue.getAttributeValue().substring(0,MAX_STRING_VALUE_LENGTH));
		}
		getHibernateTemplate().saveOrUpdate(dataProfileAttributeValue);
		log.trace("Finished saving the data profile attribute value: " + dataProfileAttributeValue);
		return dataProfileAttributeValue;
	}

	public int removeDataProfileAttribute(DataProfileAttribute dataProfileAttribute) {
		if (dataProfileAttribute == null || dataProfileAttribute.getAttributeId() == null) {
			return 0;
		}
		int deleteCount = getHibernateTemplate().bulkUpdate("delete from DataProfileAttribute where attributeId = ?", 
				dataProfileAttribute.getAttributeId());
		log.trace("Removed " + deleteCount + " data profile attributes from the system system.");
		return deleteCount;
	}

	public int removeAllDataProfileAttributes(int sourceId) {
		log.trace("Removing all data profile attributes for source with id: " + sourceId);
		int deleteCount = getHibernateTemplate().bulkUpdate("delete from DataProfileAttribute where dataSourceId = ?", sourceId);
		log.trace("Removed " + deleteCount + " data profile attributes from the system system.");
		return deleteCount;
	}

	public List<DataProfileAttribute> getDataProfileAttributes(int sourceId) {
		log.trace("Looking for data profile attribute data for source with id " + sourceId);
		@SuppressWarnings("unchecked")
		List<DataProfileAttribute> dataList = (List<DataProfileAttribute>) getHibernateTemplate().find("from DataProfileAttribute " +
				"where dataSourceId=? order by attributeName desc", sourceId);
		log.trace("Found " + dataList.size() + " data profile attribute elements for source " + sourceId);
		return dataList;
	}

	public List<DataProfileAttributeValue> getTopDataProfileAttributeValues(final int attributeId, final int topCount) {
		log.trace("Retrieving top attribute values for attribute " + attributeId);
		@SuppressWarnings("unchecked")
		List<DataProfileAttributeValue> dataList = (List<DataProfileAttributeValue>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List< DataProfileAttributeValue> list = session
					.createQuery("from DataProfileAttributeValue where attributeId = :attributeId order by frequency desc")
					.setInteger("attributeId", attributeId)
					.setMaxResults(topCount)
					.list();
				return list;
			}
		});
		log.trace("Found " + dataList.size() + " data profile attribute values.");
		return dataList;
	}
}
