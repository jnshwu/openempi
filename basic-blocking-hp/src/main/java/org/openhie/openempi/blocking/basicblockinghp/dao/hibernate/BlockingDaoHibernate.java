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
package org.openhie.openempi.blocking.basicblockinghp.dao.hibernate;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openhie.openempi.blocking.basicblockinghp.BlockingKeyValueGenerator;
import org.openhie.openempi.blocking.basicblockinghp.dao.BlockingDao;
import org.openhie.openempi.configuration.BaseField;
import org.openhie.openempi.configuration.BlockingRound;
import org.openhie.openempi.dao.hibernate.UniversalDaoHibernate;
import org.openhie.openempi.model.Criteria;
import org.openhie.openempi.model.Criterion;
import org.openhie.openempi.model.NameValuePair;
import org.openhie.openempi.model.Operation;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.util.ConvertUtil;
import org.springframework.orm.hibernate3.HibernateCallback;

public class BlockingDaoHibernate extends UniversalDaoHibernate implements BlockingDao
{
	private Map<String,Method> methodByFieldName = new HashMap<String,Method>();

	public List<Record> blockRecords(Criteria criteria) {
		List<Person> persons = getPersons(criteria);
		List<Record> records = new java.util.ArrayList<Record>(persons.size());
		for (Person person : persons) {
			Record record = ConvertUtil.getRecordFromPerson(person);
			records.add(record);
		}
		return records;
	}
	
	public String getPrimaryKeyFieldName() {
		return "personId";
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
	
	public Long getRecordPairCount(BlockingRound round) {
		StringBuffer sb = new StringBuffer();
		List<BaseField> fields = round.getFields();
		for (int i=0; i < fields.size(); i++) {
			sb.append("p.").append(fields.get(i).getFieldName());
			if (i < fields.size()-1) {
				sb.append(",");
			}
		}
		final String query = "select count(*) as c, " + sb.toString() + " from Person p group by " + sb.toString() + " having count(*) > 1";
		log.info("Counting block sizes for round " + round.getName() + " using query:\n" + query);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Long> valueCounts = (List<Long>) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<Long> valueCounts = new ArrayList<Long>();
				@SuppressWarnings("rawtypes")
				List list = session.createQuery(query).list();
				if (list == null || list.size() == 0) {
					return valueCounts;
				}
				for (Object value : list) {
					Object[] countPlusFieldValues = (Object[]) value;
					log.trace("Found a value: " + countPlusFieldValues[0]);
					valueCounts.add((Long) countPlusFieldValues[0]);
				}
				return valueCounts;
			}
		});
		long totalRecordPairCount=0;
		for (Long valueCount : valueCounts) {
			totalRecordPairCount += calculateRecordPairsByBlockSize(valueCount.longValue());
		}
		return totalRecordPairCount;
	}
	
	public long calculateRecordPairsByBlockSize(long valueCount) {
		double dValueCount = (double) valueCount;
		return (long) (dValueCount * (dValueCount-1.0) / 2.0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<NameValuePair> getDistinctKeyValuePairs(final List<String> fields) {
		return  (List<NameValuePair>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				ProjectionList projectionList = Projections.projectionList();
				for (String field : fields) {
					projectionList.add(Projections.property(field), field);					
				}
				projectionList.add(Projections.property("personId"), "personId");
				org.hibernate.Criteria criteria = session.createCriteria(Person.class, "person_")
					.setProjection(Projections.distinct(projectionList));
				for (String field : fields) {
					String property = "person_."+field;
					criteria.add(Expression.isNotNull(property));
					criteria.addOrder(Order.asc(property));
				}
				// We don't want to load deleted values
				criteria.add(Expression.isNull("dateVoided"));
				log.debug("Blocking criteria query: " + criteria.toString());
				List list = (List<Object[]>) criteria.list();
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(list.size());
				if (list.size() == 0) {
					return pairs;
				}
				List<Object[]> objectArrayValues = (List<Object[]>) list;
				for (Object[] row : objectArrayValues) {
					// The last entry is always the person ID
					Integer personId = (Integer) row[row.length-1];
					Object[] fields = Arrays.copyOfRange(row, 0, row.length-1);
					String blockingKeyValue = BlockingKeyValueGenerator.generateBlockingKeyValue(fields);
					NameValuePair pair = new NameValuePair(blockingKeyValue, personId);
					pairs.add(pair);
				}
				return pairs;
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Person> getPersons(final Criteria criteria) {
		return  (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				org.hibernate.Criteria criteriaHibernate = buildHibernateCriteria(session, criteria);
				log.debug("Querying by criteria using " + criteriaHibernate.toString());
				List<Person> list = (List<Person>) criteriaHibernate.setResultTransformer(org.hibernate.Criteria.DISTINCT_ROOT_ENTITY).list();
//				log.debug("Query by criteria returned: " + list.size() + " elements.");
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

	@SuppressWarnings("rawtypes")
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
		} else if (criterion.getOperation().equals(Operation.IN)) {
			criteriaHibernate.add(Restrictions.in(criterion.getName(), (Collection) criterion.getValue()));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Record loadRecord(final long recordId, final List<String> fieldNameSet) {
		final String[] fieldNames = fieldNameSet.toArray(new String[]{});
		return (Record) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				try {
					org.hibernate.Criteria criteria = createQueryFromFields(session, fieldNameSet);
					criteria.add(Restrictions.eq("personId", new Integer((int) recordId)));
					criteria.setCacheMode(CacheMode.IGNORE);
					criteria.setFetchSize(10000);
					ScrollableResults iterator = criteria.scroll(ScrollMode.FORWARD_ONLY);
					int count = 0;
					Record record = null;
					while (iterator.next()) {
						Object[] values = iterator.get();
						Person person = populatePersonObject(fieldNames, values);
						Long recordId = person.getPersonId().longValue();
						record = new Record(person);
						record.setRecordId(recordId);
						log.trace("Loaded " + count + " records with id " + recordId + " into the cache.");
						count++;
					}
					return record;
				} catch (Exception e) {
					log.error("Failed while scrolling through the records: " + e, e);
					throw new RuntimeException("Failed while scrolling through the records: " + e.getMessage());
				}
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void loadAllRecords(final Cache recordCache, final List<String> fieldNameSet) {
		final String[] fieldNames = fieldNameSet.toArray(new String[]{});
//		final String queryStr = buildQuery(fieldNames).toString();
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) {
				try {
					org.hibernate.Criteria criteria = createQueryFromFields(session, fieldNameSet);
					criteria.setCacheMode(CacheMode.IGNORE);
					criteria.setFetchSize(10000);
					ScrollableResults iterator = criteria.scroll(ScrollMode.FORWARD_ONLY);
//					Query query = session.createQuery(queryStr);
//					query.setCacheMode(CacheMode.IGNORE);
//					query.setFetchSize(10000);
//					ScrollableResults iterator = query.scroll(ScrollMode.FORWARD_ONLY);
					int count = 0;
					while (iterator.next()) {
						Object[] values = iterator.get();
						Person person = populatePersonObject(fieldNames, values);
						Record record = new Record(person);
						Long recordId = person.getPersonId().longValue();
						record.setRecordId(recordId);
						Element element = new Element(recordId, record);
						recordCache.put(element);
						log.trace("Loaded record " + recordId + " into the cache.");
						count++;
					}
					log.debug("Loaded " + count + " records into the cache.");
				} catch (Exception e) {
					log.error("Failed while scrolling through the records: " + e, e);
				}
				return null;
			}
		});
	}

	private org.hibernate.Criteria createQueryFromFields(Session session, List<String> fieldNameSet) {
		org.hibernate.Criteria criteria = session.createCriteria(Person.class);
		ProjectionList projectionsList = Projections.projectionList();
		for (String fieldName : fieldNameSet) {
			projectionsList.add(Projections.property(fieldName));
		}
		criteria.setProjection(projectionsList);
		criteria.add(Restrictions.isNull("dateVoided"));
		return criteria;
	}

//	private StringBuffer buildQuery(String[] fieldNames) {
//		StringBuffer query = new StringBuffer("select ");
//		int index = 0, totalCount=fieldNames.length;
//		for (String fieldName : fieldNames) {
//			query.append("p.").append(fieldName);
//			index++;
//			if (index < totalCount) {
//				query.append(",");
//			}
//		}
//		query.append(" from Person p where p.dateVoided is null");
//		return query;
//	}

	private Person populatePersonObject(String[] fieldNames, Object[] values) {
		Person person = new Person();
		if (fieldNames.length != values.length) {
			log.error("The list of field names does not match the result set length.");
			return person;
		}
		for (int i=0; i < fieldNames.length; i++) {
			if (values[i] != null) {
				setEntityValue(values[i], fieldNames[i], person);
			}
		}
		return person;
	}
	
	private void setEntityValue(Object value, String fieldName, Object entityInstance) {

		Method method = getMethod(entityInstance.getClass(), fieldName, value.getClass());
		if (method == null) {
			return;
		}
		
		try {
			method.invoke(entityInstance, value);
		} catch (Exception e) {
			log.warn("Unable to set the value of field " + fieldName + " to a value " + value + 
					" of type " + value.getClass());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Method getMethod(Class theClass, String fieldName, Class paramClass) {
		Method method = methodByFieldName.get(fieldName);
		if (method != null) {
			return method;
		}
		String methodName = getMethodName(fieldName);
		try {
			if (paramClass.equals(java.sql.Date.class)) {
				paramClass = java.util.Date.class;
			}
			method = theClass.getMethod(methodName, paramClass);
			methodByFieldName.put(fieldName, method);
		} catch (Exception e) {
			log.warn("Unable to field entity method for setting field " + fieldName + " to a value of type " + paramClass);
			return null;
		}
		return method;
	}

	private String getMethodName(String fieldName) {
		return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getAllRecordIds() {
		List<Integer> personIds = new java.util.ArrayList<Integer>();
		String queryString = "select p.personId from Person p where p.dateVoided is null";
		personIds = getHibernateTemplate().find(queryString);
		List<Long> recordIds = new java.util.ArrayList<Long>();
		for (Integer personId : personIds) {
			recordIds.add(personId.longValue());
		}
		return recordIds;
	}
}
