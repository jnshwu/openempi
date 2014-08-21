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
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openhie.openempi.dao.BlockingDao;
import org.openhie.openempi.model.Criteria;
import org.openhie.openempi.model.Criterion;
import org.openhie.openempi.model.NameValuePair;
import org.openhie.openempi.model.Operation;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.util.ConvertUtil;
import org.springframework.orm.hibernate3.HibernateCallback;

public class BlockingDaoHibernate extends UniversalDaoHibernate implements BlockingDao
{
	public List<Record> blockRecords(Criteria criteria) {
		List<Person> persons = getPersons(criteria);
		List<Record> records = new java.util.ArrayList<Record>(persons.size());
		for (Person person : persons) {
			Record record = ConvertUtil.getRecordFromPerson(person);
			records.add(record);
		}
		return records;
	}

	@SuppressWarnings("unchecked")
	public List<NameValuePair> getDistinctValues(String field) {
		if (field == null || field.length() == 0) {
			return new java.util.ArrayList<NameValuePair>();
		}
		String query = "select distinct p." + field + " from Person p";
        List<String> values = getHibernateTemplate().find(query);
        log.trace("Found the following: " + values);
        List<NameValuePair> nameValuePairs = new java.util.ArrayList<NameValuePair>();
        for (String value : values) {
        	if (value != null) {
        		nameValuePairs.add(new NameValuePair(field, value));
        	}
        }
        return nameValuePairs;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getBlockingKeyValues(final List<String> fields) {
		return  (List<String>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<String> values = new java.util.ArrayList<String>();
				ProjectionList projectionList = Projections.projectionList();
				for (String field : fields) {
					projectionList.add(Projections.property(field), field);					
				}
				org.hibernate.Criteria criteria = session.createCriteria(Person.class, "person_")
					.setProjection(Projections.distinct(projectionList));
				for (String field : fields) {
					criteria.add(Expression.isNotNull("person_."+field));
				}
				log.debug("Blocking criteria query: " + criteria.toString());
				@SuppressWarnings("rawtypes")
				List list = (List<Object[]>) criteria.list();
				if (list.size() == 0) {
					return values;
				}
				// Query returns either a list of Object or a list of arrays of objects depending on
				// whether one or more than one blocking fields are used.
				if (!(list.get(0) instanceof Object[])) {
					List<Object> theValues = (List<Object>) list;
					for (Object value : theValues) {
						values.add(value.toString());
					}
					return values;
				}
				List<Object[]> objectArrayValues = (List<Object[]>) list;
				for (Object[] row : objectArrayValues) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < row.length; i++) {
						sb.append(row[i].toString().trim());
					}
					values.add(sb.toString());
				}
				return values;
			}
		});		
	}
	
	@SuppressWarnings("unchecked")
	public List<List<NameValuePair>> getDistinctValues(final List<String> fields) {
		return  (List<List<NameValuePair>>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				ProjectionList projectionList = Projections.projectionList();
				for (String field : fields) {
					projectionList.add(Projections.property(field), field);					
				}
				org.hibernate.Criteria criteria = session.createCriteria(Person.class, "person_")
					.setProjection(Projections.distinct(projectionList));
				for (String field : fields) {
					criteria.add(Expression.isNotNull("person_."+field));
				}
				log.debug("Blocking criteria query: " + criteria.toString());
				@SuppressWarnings("rawtypes")
				List list = (List<Object[]>) criteria.list();
				List<List<NameValuePair>> pairs = new ArrayList<List<NameValuePair>>(list.size());
				if (list.size() == 0) {
					return pairs;
				}
				// Query returns either a list of Object or a list of arrays of objects depending on
				// whether one or more than one blocking fields are used.
				if (!(list.get(0) instanceof Object[])) {
					List<Object> theValues = (List<Object>) list;
					for (Object value : theValues) {
						List<NameValuePair> distinctRowValues = new ArrayList<NameValuePair>(1);
						distinctRowValues.add(new NameValuePair(fields.get(0), value));
						pairs.add(distinctRowValues);
					}
					return pairs;
				}
				List<Object[]> objectArrayValues = (List<Object[]>) list;
				for (Object[] row : objectArrayValues) {
					List<NameValuePair> distinctRowValues = new ArrayList<NameValuePair>(row.length);
					for (int i = 0; i < row.length; i++) {
						NameValuePair pair = new NameValuePair(fields.get(i), row[i]);
						distinctRowValues.add(pair);
					}
					pairs.add(distinctRowValues);
				}
				return pairs;
			}
		});
	}

	@SuppressWarnings("unchecked")
	private List<Person> getPersons(final Criteria criteria) {
		return  (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				org.hibernate.Criteria criteriaHibernate = buildHibernateCriteria(session, criteria);
				log.debug("Querying by criteria using " + criteriaHibernate.toString());
				List<Person> list = (List<Person>) criteriaHibernate.setResultTransformer(org.hibernate.Criteria.DISTINCT_ROOT_ENTITY).list();
				log.debug("Query by criteria returned: " + list.size() + " elements.");
				removeDeletedIdentifiers(list);
				return list;
			}
		});
	}
	
	private org.hibernate.Criteria buildHibernateCriteria(Session session, Criteria criteria) {
		org.hibernate.Criteria criteriaHibernate = session.createCriteria(Person.class);
		for (Criterion criterion : criteria.getCriteria()) {
			addCriterion(criteriaHibernate, criterion);
		}
		addCriterion(criteriaHibernate, new Criterion("dateVoided", Operation.ISNULL, null));
		return criteriaHibernate;
	}

	private void addCriterion(org.hibernate.Criteria criteriaHibernate, Criterion criterion) {
		if (criterion.getOperation().equals(Operation.EQ)) {
			criteriaHibernate.add(Restrictions.eq(criterion.getName(), criterion.getValue()));
		} else if (criterion.getOperation().equals(Operation.ISNOTNULL)) {
			criteriaHibernate.add(Restrictions.isNotNull(criterion.getName()));
		} else if (criterion.getOperation().equals(Operation.ISNULL)) {
			criteriaHibernate.add(Restrictions.isNull(criterion.getName()));
		} else if (criterion.getOperation().equals(Operation.LIKE)) {
			criteriaHibernate.add(Restrictions.like(criterion.getName(), criterion.getValue()));
		} else if (criterion.getOperation().equals(Operation.NE)) {
			criteriaHibernate.add(Restrictions.ne(criterion.getName(), criterion.getValue()));
		}
	}

	private static void removeDeletedIdentifiers(List<Person> list) {
		for (Person person : list) {
			removeDeletedIdentifiers(person);
		}
	}

	private static void removeDeletedIdentifiers(Person person) {
		initializeAssociations(person);
		List<PersonIdentifier> toBeRemoved = new java.util.ArrayList<PersonIdentifier>();
		for (PersonIdentifier id : person.getPersonIdentifiers()) {
			if (id.getDateVoided() != null) {
				toBeRemoved.add(id);
			}
		}
		person.getPersonIdentifiers().removeAll(toBeRemoved);
	}
	
	private static void initializeAssociations(Person person) {
		if (person.getAddressType() != null) {
			Hibernate.initialize(person.getAddressType());
		}
		if (person.getEthnicGroup() != null) {
			Hibernate.initialize(person.getEthnicGroup());
		}
		if (person.getGender() != null) {
			Hibernate.initialize(person.getGender());
		}
		if (person.getLanguage() != null) {
			Hibernate.initialize(person.getLanguage());
		}
		if (person.getNameType() != null) {
			Hibernate.initialize(person.getNameType());
		}
		if (person.getNationality() != null) {
			Hibernate.initialize(person.getNationality());
		}
		if (person.getPhoneType() != null) {
			Hibernate.initialize(person.getPhoneType());
		}
		if (person.getRace() != null) {
			Hibernate.initialize(person.getRace());
		}
		if (person.getReligion() != null) {
			Hibernate.initialize(person.getReligion());
		}
	}
}
