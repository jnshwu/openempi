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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.proxy.HibernateProxy;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.dao.PersonDao;
import org.openhie.openempi.model.AddressType;
import org.openhie.openempi.model.Criterion;
import org.openhie.openempi.model.EthnicGroup;
import org.openhie.openempi.model.ExtendedCriterion;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.IdentifierDomainAttribute;
import org.openhie.openempi.model.IdentifierUpdateEvent;
import org.openhie.openempi.model.IdentifierUpdateEntry;
import org.openhie.openempi.model.Language;
import org.openhie.openempi.model.NameType;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Operation;
import org.openhie.openempi.model.OrderCriterion;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.PhoneType;
import org.openhie.openempi.model.Race;
import org.openhie.openempi.model.Record;
import org.openhie.openempi.model.Religion;
import org.openhie.openempi.model.User;
import org.openhie.openempi.util.ConvertUtil;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.orm.hibernate3.HibernateCallback;

public class PersonDaoHibernate extends UniversalDaoHibernate implements PersonDao
{
	public void addPerson(Person person) {
		log.debug("Saving person record: " + person);
		for (PersonIdentifier identifier : person.getPersonIdentifiers()) {
			identifier.setDateCreated(person.getDateCreated());
			identifier.setUserCreatedBy(person.getUserCreatedBy());
			identifier.getIdentifierDomain().setDateCreated(person.getDateCreated());
			identifier.getIdentifierDomain().setUserCreatedBy(person.getUserCreatedBy());
			saveIdentifierDomain(identifier.getIdentifierDomain());
		}
		if (person.getAccountIdentifierDomain() != null) {
			IdentifierDomain accountDomain = person.getAccountIdentifierDomain();
			accountDomain.setDateCreated(person.getDateCreated());
			accountDomain.setUserCreatedBy(person.getUserCreatedBy());
			saveIdentifierDomain(accountDomain);
			log.debug("Account Domain is: " + accountDomain);
		}
		loadAssociations(person);
		getHibernateTemplate().saveOrUpdate(person);
		getHibernateTemplate().flush();
		log.debug("Finished saving the person.");
	}
	
	public void addPersonIdentifier(PersonIdentifier identifier) {
		log.debug("Saving identifier: " + identifier);
		getHibernateTemplate().saveOrUpdate(identifier);
		getHibernateTemplate().flush();
		log.trace("Finished saving identifier.");			
	}

	public void addPersonIdentifiers(Set<PersonIdentifier> identifiers) {
		if (identifiers == null || identifiers.size() == 0) {
			return;
		}
		for (PersonIdentifier identifier : identifiers) {
			log.trace("Saving identifier: " + identifier);
			getHibernateTemplate().merge(identifier);
		}
		getHibernateTemplate().flush();
		log.debug("Finished saving the set of identifiers.");
	}
	
	public void updatePerson(Person person) {
		log.debug("Saving person record: " + person);
		loadAssociations(person);
		getHibernateTemplate().setFlushMode(HibernateAccessor.FLUSH_ALWAYS);
		getHibernateTemplate().merge(person);
		getHibernateTemplate().flush();
		removeDeletedIdentifiers(person);
		log.debug("Finished saving the person.");
	}
	
	@SuppressWarnings("unchecked")
	public void updatePersonIdentifiers(final Person person) {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String query = "from PersonIdentifier as pi where pi.dateVoided is null and pi.person.personId = ?";
				List<PersonIdentifier> curr = (List<PersonIdentifier>) getHibernateTemplate()
						.find(query, person.getPersonId());
				log.debug("Found list of identifiers: " + curr);
				Map<String,PersonIdentifier> currIdMap = buildMap(curr);
				Map<String,PersonIdentifier> newIdMap = buildMap(person.getPersonIdentifiers());
				for (PersonIdentifier id : person.getPersonIdentifiers()) {
					PersonIdentifier found = currIdMap.get(getIdentifierKey(id));
					if (found == null) {
						// The id is the new list but not in the old; it needs to be added
						id.setDateCreated(new Date());
						id.setPerson(person);
						id.setUserCreatedBy(Context.getUserContext().getUser());
//						log.debug("Adding new ID: " + id);
						session.saveOrUpdate(id);
					} else if (id.getDateVoided() != null && found.getDateVoided() == null) {
						// The id has been voided in the new list but was not previously void
						id.setDateVoided(new Date());
						id.setUserVoidedBy(Context.getUserContext().getUser());
						id.setPerson(person);
//						log.debug("ID is explicitly voided: " + id);
						session.merge(id);
					} else if (id.getDateVoided() == null && found.getDateVoided() != null) {
						
						id.setDateCreated(new Date());
						id.setPerson(person);
						id.setUserCreatedBy(Context.getUserContext().getUser());
//						log.debug("Updating ID: " + id);
						session.merge(id);
					}
				}

				// Go through the list of current identifiers
				for (PersonIdentifier id : curr) {
					PersonIdentifier found = newIdMap.get(getIdentifierKey(id));
					if (found == null) {
						if (id.getDateVoided() == null) {
							id.setDateVoided(new Date());
							id.setUserVoidedBy(Context.getUserContext().getUser());
							session.merge(id);
//							log.debug("Old ID is implicitly voided: " + id);
						}
					}
				}
			
				// removed voided identifiers
				removeDeletedIdentifiersBySet(person);
				return null;
			}
		});
	}

	private Map<String, PersonIdentifier> buildMap(Collection<PersonIdentifier> list) {
		Map<String,PersonIdentifier> mapOfIdentifiers = new HashMap<String,PersonIdentifier>();
		for (PersonIdentifier id : list) {
			mapOfIdentifiers.put(getIdentifierKey(id), id);
		}
		return mapOfIdentifiers;
	}

	private String getIdentifierKey(PersonIdentifier id) {
		return id.getIdentifier() + "." + id.getIdentifierDomain().getIdentifierDomainId();
	}
	
	public void updatePersons(List<Person> persons) {
		for (Person person : persons) {
			log.trace("Saving person record: " + person);
			loadAssociations(person);
			getHibernateTemplate().merge(person);
			removeDeletedIdentifiers(person);
		}
		getHibernateTemplate().flush();
		log.debug("Finished saving the list of persons.");
	}
	
	@SuppressWarnings("unchecked")
	public List<Person> getPersonsByIdentifier(final PersonIdentifier personIdentifier) {
		return (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Person.class)
					.add(Restrictions.isNull("dateVoided"))
					.createAlias("personIdentifiers", "pi")
					.createAlias("personIdentifiers.identifierDomain", "id")
					.add(Expression.like("pi.identifier", personIdentifier.getIdentifier()));

				if (personIdentifier.getIdentifierDomain() != null) {
					if (personIdentifier.getIdentifierDomain().getIdentifierDomainName() != null) {
						criteria.add(Restrictions.like("id.identifierDomainName", personIdentifier.getIdentifierDomain().getIdentifierDomainName()));
					}
					if (personIdentifier.getIdentifierDomain().getNamespaceIdentifier() != null) {
						criteria.add(Restrictions.eq("id.namespaceIdentifier", personIdentifier.getIdentifierDomain().getNamespaceIdentifier()));
					}
					if (personIdentifier.getIdentifierDomain().getUniversalIdentifier() != null) {
						criteria.add(Restrictions.eq("id.universalIdentifier", personIdentifier.getIdentifierDomain().getUniversalIdentifier()));
					}
					if (personIdentifier.getIdentifierDomain().getUniversalIdentifierTypeCode() != null) {
						criteria.add(Restrictions.eq("id.universalIdentifierTypeCode", personIdentifier.getIdentifierDomain().getUniversalIdentifierTypeCode()));
					}
				}
				List<Person> list = criteria.list();
				log.debug("Query by partial identifier returned: " + list.size() + " elements.");
				removeDeletedIdentifiers(list);
				return list;
			}
		});		
	}
	
	public Person loadPerson(final Integer personId) {
		return (Person) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				@SuppressWarnings("unchecked")
				List<Person> persons = session.createCriteria(Person.class)
						.add(Expression.idEq(personId))
						.add(Restrictions.isNull("dateVoided"))
						.list();
				if (persons.size() == 0) {
					return null;
				}
				Person p = persons.get(0);
				removeDeletedIdentifiers(p);
				return p;
			}});
	}
	
	public Person loadPersonForUpdate(final Integer personId) {
		return (Person) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				@SuppressWarnings("unchecked")
				List<Person> persons = session.createCriteria(Person.class)
						.add(Expression.idEq(personId))
						.add(Restrictions.isNull("dateVoided"))
						.list();
				if (persons.size() == 0) {
					return null;
				}
				Person p = persons.get(0);
				removeDeletedIdentifiers(p);
				session.evict(p);
				return p;
			}});
	}

	public Person loadPersonComplete(final Integer personId) {
		return (Person) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				@SuppressWarnings("unchecked")
				List<Person> persons = session.createCriteria(Person.class)
						.add(Expression.idEq(personId))
						.add(Restrictions.isNull("dateVoided"))
						.list();
				if (persons.size() == 0) {
					return null;
				}
				Person p = persons.get(0);
				removeDeletedIdentifiers(p);
				initializeAssociations(p);
				return p;
			}});
	}
	
	@SuppressWarnings("unchecked")
	public List<Person> loadPersons(final List<Integer> personIds) {
		return (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<Person> persons = session.createCriteria(Person.class)
						.add(Expression.in("personId", personIds))
						.add(Restrictions.isNull("dateVoided"))
						.list();
				removeDeletedIdentifiers(persons);
				return persons;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<Record> loadRecords(final List<Long> recordIds) {
		return (List<Record>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<Integer> personIds = new java.util.ArrayList<Integer>(recordIds.size());
				for (Long recordId : recordIds) {
					personIds.add(recordId.intValue());
				}
				List<Person> list = session.createCriteria(Person.class)
						.add(Expression.in("personId", personIds))
						.add(Restrictions.isNull("dateVoided"))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
				removeDeletedIdentifiers(list);
				List<Record> records = convertPersonToRecord(list);
				return records;
			}});
	}
	
	public Record loadRecord(final Long recordId) {
		return (Record) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				@SuppressWarnings("unchecked")
				List<Person> persons = session.createCriteria(Person.class)
						.add(Expression.idEq(recordId.intValue()))
						.add(Restrictions.isNull("dateVoided"))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
				if (persons.size() == 0) {
					return null;
				}
				Person person = persons.get(0);
				removeDeletedIdentifiers(person);
				initializeAssociations(person);
				Record record = ConvertUtil.getRecordFromPerson(person);
				return record;
			}});
	}
	
	public Person getPersonById(final PersonIdentifier personIdentifier) {
		return (Person) getHibernateTemplate().execute(new HibernateCallback() {
			@SuppressWarnings("unchecked")
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Person.class)
					.add(Restrictions.isNull("dateVoided"))
					.createAlias("personIdentifiers", "pi")
					.createAlias("personIdentifiers.identifierDomain", "id")
					.add(Expression.eq("pi.identifier", personIdentifier.getIdentifier()));
				/**
				 * A domain identifier uniquely identifies a domain either through the namespace identifier or through the
				 * combination of universalIdentifier and universalIdentifierTypeCode
				 */
				if (personIdentifier.getIdentifierDomain() != null) {			
					if (personIdentifier.getIdentifierDomain().getNamespaceIdentifier() != null) {
						criteria.add(Restrictions.eq("id.namespaceIdentifier", personIdentifier.getIdentifierDomain().getNamespaceIdentifier()));
					} else {
						criteria.add(Restrictions.and(
								Restrictions.eq("id.universalIdentifier", personIdentifier.getIdentifierDomain().getUniversalIdentifier()),
								Restrictions.eq("id.universalIdentifierTypeCode", personIdentifier.getIdentifierDomain().getUniversalIdentifierTypeCode())));
					}
				}
				List<Person> list = criteria.list();
				log.debug("Query by identifier returned: " + list.size() + " elements.");
				if (list.size() == 0) {
					return null;
				}
				Person person = list.get(0);
				removeDeletedIdentifiers(person);
				return person;
			}
		});
	}
	
	public Boolean getPersonByIdHQL(final PersonIdentifier personIdentifier) {
		return (Boolean) getHibernateTemplate().execute(new HibernateCallback() {
			@SuppressWarnings("unchecked")
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Boolean personsFound = false;
				
				String queryStringNameSpace = "select count(*) from Person p inner join p.personIdentifiers as pi inner join pi.identifierDomain as id where pi.identifier = :identifier and id.namespaceIdentifier = :namespaceIdentifier and p.dateVoided is null";
				
				String queryStringUniversalId = "select count(*) from Person p inner join p.personIdentifiers as pi inner join pi.identifierDomain as id where pi.identifier = :identifier and id.universalIdentifier = :universalIdentifier and id.universalIdentifierTypeCode = :universalIdentifierTypeCode and p.dateVoided is null";


				
				if (personIdentifier.getIdentifierDomain().getNamespaceIdentifier() != null) {
					   personsFound = (Long) session.createQuery(queryStringNameSpace).setString("identifier", personIdentifier.getIdentifier())
                              .setString("namespaceIdentifier",personIdentifier.getIdentifierDomain().getNamespaceIdentifier()).uniqueResult() > 0;

					  
			    } 
				else
				{
					 personsFound = (Long) session.createQuery(queryStringUniversalId).setString("identifier", personIdentifier.getIdentifier())
                             .setString("universalIdentifier", personIdentifier.getIdentifierDomain().getUniversalIdentifier())
                             .setString("universalIdentifierTypeCode", personIdentifier.getIdentifierDomain().getUniversalIdentifierTypeCode()).uniqueResult() > 0;
			     }
				
				return personsFound;
			}
		});
		
	}	
	
	
	public List<Person> getPersons(final org.openhie.openempi.model.Criteria criteria) {
		return getPersonsPaged(criteria, false, 0, 0);
	}
	
	public List<Person> getPersons(final org.openhie.openempi.model.Criteria personCriteria,
			final Set<PersonIdentifier> personIdentifiers) {
		return getPersonsPaged(personCriteria, personIdentifiers, false, 0, 0);
	}
	
	public List<Person> getPersonsPaged(final org.openhie.openempi.model.Criteria criteria,
			final int firstResult, final int maxResults) {
		return getPersonsPaged(criteria, true, firstResult, maxResults);
	}
	
	public List<Person> getPersonsPaged(final org.openhie.openempi.model.Criteria personCriteria,
			final Set<PersonIdentifier> personIdentifiers, final int firstResult, final int maxResults,
			final boolean hydrate) {
		return getPersonsPaged(personCriteria, personIdentifiers, true, firstResult, maxResults);
	}

	public List<Person> getPersonsPaged(final int firstResult, final int maxResults) {
		@SuppressWarnings("unchecked")
		List<Person> persons = (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("from Person p where p.dateVoided is null order by p.personId");
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
				log.debug("Querying using " + query.toString());
				List<Person> list = (List<Person>) query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
				removeDeletedIdentifiers(list);
				log.debug("Query by criteria returned: " + list.size() + " elements.");
				return list;
			}
		});
		return persons;
	}

	public List<Record> getRecordsPaged(final int firstResult, final int maxResults) {
		@SuppressWarnings("unchecked")
		List<Person> persons = (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("from Person p where p.dateVoided is null order by p.personId");
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
				log.debug("Querying using " + query.toString());
				List<Person> list = (List<Person>) query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
				removeDeletedIdentifiers(list);
				log.debug("Query by criteria returned: " + list.size() + " elements.");
				return list;
			}
		});
		return convertPersonToRecord(persons);
	}

	public List<Record> getRecordsPaged(final org.openhie.openempi.model.Criteria criteria,
			final int firstResult, final int maxResults) {
		List<Person> persons = getPersonsPaged(criteria, true, firstResult, maxResults);
		return convertPersonToRecord(persons);
	}
	
	@SuppressWarnings("unchecked")
	private List<Person> getPersonsPaged(final org.openhie.openempi.model.Criteria criteria,
			final boolean paging, final int firstResult, final int maxResults) {
		return  (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				org.hibernate.Criteria criteriaHibernate = buildHibernateCriteria(session, criteria);
				if (paging) {
					criteriaHibernate.setFirstResult(firstResult);
					criteriaHibernate.setMaxResults(maxResults);
				}
				log.debug("Querying by criteria using " + criteriaHibernate.toString());
				List<Person> list = (List<Person>) criteriaHibernate
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
				removeDeletedIdentifiers(list);
				log.debug("Query by criteria returned: " + list.size() + " elements.");
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private List<Person> getPersonsPaged(final org.openhie.openempi.model.Criteria personCriteria,
			final Set<PersonIdentifier> personIdentifiers, final boolean paging, final int firstResult,
			final int maxResults) {
		return  (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				org.hibernate.Criteria criteriaHibernate = buildHibernateCriteria(session, personCriteria, personIdentifiers);
				if (paging) {
					criteriaHibernate.setFirstResult(firstResult);
					criteriaHibernate.setMaxResults(maxResults);
				}
				log.debug("Querying by criteria using " + criteriaHibernate.toString());
				List<Person> list = (List<Person>) criteriaHibernate
						.setFetchMode("personIdentifiers", FetchMode.JOIN)
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
				for (Person person : list) {
					removeDeletedIdentifiers(person);
				}
				log.debug("Query by criteria returned: " + list.size() + " elements.");
				return list;
			}
		});
	}
	
	private List<Record> convertPersonToRecord(List<Person> persons) {
		List<Record> records = new java.util.ArrayList<Record>(persons.size());
		for (Person person : persons) {
			Record record = ConvertUtil.getRecordFromPerson(person);
			records.add(record);
		}
		return records;
	}

	@SuppressWarnings("unchecked")
	public List<IdentifierDomain> getIdentifierDomains() {
		List<IdentifierDomain> domains = (List<IdentifierDomain>) getHibernateTemplate().find("from IdentifierDomain");
		log.trace("Obtained the list of identifier domains with " + domains.size() + " entries.");
		return domains;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getIdentifierDomainTypeCodes() {
		String sql = "select distinct i.universalIdentifierTypeCode from IdentifierDomain i where i.universalIdentifierTypeCode is not null order by i.universalIdentifierTypeCode";
		List<String> codes = (List<String>) getHibernateTemplate().find(sql);
		log.trace("Obtained the list of universal identifier type codes of size " + codes.size() + " entries.");
		return codes;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getAllPersons() {
		List<Integer> personIds = new java.util.ArrayList<Integer>();
		String queryString = "select p.personId from Person p where p.dateVoided is null";
		personIds = getHibernateTemplate().find(queryString);
		return personIds;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getPersonsWithoutIdentifierInDomain(IdentifierDomain identifierDomain, boolean hasLinks) {
		List<Integer> personIds = new java.util.ArrayList<Integer>(); 
		if (identifierDomain == null) {
			return personIds;
		}
		Integer identifierDomainId = identifierDomain.getIdentifierDomainId();
		if (identifierDomain.getIdentifierDomainId() == null) {
			IdentifierDomain foundIdentifierDomain = findIdentifierDomain(identifierDomain);
			if (foundIdentifierDomain == null) {
				return personIds;
			}
			identifierDomainId = foundIdentifierDomain.getIdentifierDomainId();
		}
		String queryString = "select p.personId from Person p where not exists " +
				"(select pi.personIdentifierId from PersonIdentifier pi " +
				"where pi.identifierDomain.identifierDomainId = ? and pi.person.personId = p.personId)";
		if (hasLinks) {
			queryString += "and exists (select pl.personLinkId from PersonLink pl where (pl.personLeft.personId = p.personId or pl.personRight.personId = p.personId))";
		} else {
			queryString += "and not exists (select pl.personLinkId from PersonLink pl where (pl.personLeft.personId = p.personId or pl.personRight.personId = p.personId))";
		}
		personIds = (List<Integer>) getHibernateTemplate().find(queryString, identifierDomainId);
		log.debug("Found " + personIds.size() + " person entries without an identifier in identifier domain " + identifierDomainId);
		return personIds;
	}

	public void addIdentifierDomain(IdentifierDomain identifierDomain) {
		getHibernateTemplate().saveOrUpdate(identifierDomain);
		getHibernateTemplate().flush();
		log.debug("Finished saving the identifier domain.");
	}
	
	public void removeIdentifierDomain(IdentifierDomain identifierDomain) {
		getHibernateTemplate().delete(identifierDomain);
		getHibernateTemplate().flush();
		log.debug("Removed an identifier domain instance.");
	}
	
	public void removePerson(final Integer personId) {
		getHibernateTemplate().execute(new HibernateCallback() {  
            public Object doInHibernate(Session session) throws HibernateException, SQLException {  
                String query = "select removepersonbypersonid(" + personId + ");";
                log.debug("Removing a person using statement: " + query);
                Integer affectedRecords = (Integer) session.createSQLQuery(query).uniqueResult();
                log.debug("Removing a person by id affected: " + affectedRecords + " records.");
                return affectedRecords;
            }
		});
	}
	
	@SuppressWarnings("unchecked")
	public boolean isKnownUniversalIdentifierTypeCode(String universalIdentifierTypeCode) {
		String queryString = "from IdentifierDomain i where i.universalIdentifierTypeCode = ?";
		List<IdentifierDomain> domains = getHibernateTemplate().find(queryString, universalIdentifierTypeCode);
		if (domains.size() == 0) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public IdentifierDomainAttribute addIdentifierDomainAttribute(IdentifierDomain identifierDomain, String attributeName, String attributeValue) {
		if (identifierDomain == null || identifierDomain.getIdentifierDomainId() == null) {
			log.debug("User attempted to add identifier domain attribute for an unknown identifier domain: " + identifierDomain);
			return null;
		}
		IdentifierDomain foundIdentifierDomain = (IdentifierDomain) getHibernateTemplate().get(IdentifierDomain.class, identifierDomain.getIdentifierDomainId());
		if (foundIdentifierDomain == null) {
			log.debug("User attempted to add identifier domain attribute for an unknown identifier domain: " + identifierDomain);
			return null;
		}
		// Now check to see if this attribute already exists in which case this should be an update operation
		String queryString = "from IdentifierDomainAttribute i where i.identifierDomainId = ? and i.attributeName = ?";
		List<IdentifierDomainAttribute> attribs = (List<IdentifierDomainAttribute>) getHibernateTemplate().find(queryString,
				new Object[] {foundIdentifierDomain.getIdentifierDomainId(), attributeName});
		if (attribs.size() > 0) {
			IdentifierDomainAttribute attribute = attribs.get(0);
			log.debug("User attempted to add an attribute that already exists in the repository: " + attribute);
			throw new RuntimeException("This attribute already exists in the repository.");
		}
		IdentifierDomainAttribute attribute = new IdentifierDomainAttribute(foundIdentifierDomain.getIdentifierDomainId(), attributeName, attributeValue);
		getHibernateTemplate().saveOrUpdate(attribute);
		getHibernateTemplate().flush();
		log.debug("Finished saving the identifier domain attribute: " + attribute);
		return attribute;
	}
	
	@SuppressWarnings("unchecked")
	public IdentifierDomainAttribute getIdentifierDomainAttribute(IdentifierDomain identifierDomain, String attributeName) {
		if (identifierDomain == null || identifierDomain.getIdentifierDomainId() == null || attributeName == null) {
			log.debug("User attempted to retrieve identifier domain attribute without providing the appropriate query criteria.");
			return null;
		}
		String queryString = "from IdentifierDomainAttribute i where i.identifierDomainId = ? and i.attributeName = ?";
		List<IdentifierDomainAttribute> attribs = (List<IdentifierDomainAttribute>) getHibernateTemplate().find(queryString,
				new Object[] {identifierDomain.getIdentifierDomainId(), attributeName});
		if (attribs.size() == 0) {
			return null;
		}
		IdentifierDomainAttribute attrib = attribs.get(0);
		log.trace("Loaded the identifier domain attribute: " + attrib);
		return attrib;
	}
	
	@SuppressWarnings("unchecked")
	public List<IdentifierDomainAttribute> getIdentifierDomainAttributes(IdentifierDomain identifierDomain) {
		if (identifierDomain == null || identifierDomain.getIdentifierDomainId() == null) {
			log.debug("User attempted to retrieve list of identifier domain attributes without providing the appropriate query criteria.");
			return null;
		}
		String queryString = "from IdentifierDomainAttribute i where i.identifierDomainId = ?";
		List<IdentifierDomainAttribute> attribs = (List<IdentifierDomainAttribute>) getHibernateTemplate().find(queryString,
				new Object[] {identifierDomain.getIdentifierDomainId()});
		log.trace("Loaded list of identifier domain attributes: " + attribs);
		return attribs;
	}
	
	public void updateIdentifierDomainAttribute(IdentifierDomainAttribute identifierDomainAttribute) {
		if (identifierDomainAttribute == null || 
				identifierDomainAttribute.getIdentifierDomainAttributeId() == null) {
			log.debug("User attempted to update identifier domain attribute without providing the appropriate query criteria.");
			return;
		}
		getHibernateTemplate().update(identifierDomainAttribute);
		getHibernateTemplate().flush();
		log.trace("Updated the identifier domain attribute: " + identifierDomainAttribute);
	}
	
	public int getRecordCount() {
		int count = ((Long)getSession().createQuery("select count(*) from Person where dateVoided is null").uniqueResult()).intValue();
		return count;
	}
	
	public void clearCustomFields() {
		String clearStmt = "update Person set " +
				"custom1 = null, custom2 = null, custom3 = null,  custom4 = null, custom5 = null, custom6 = null, " +
				"custom7 = null, custom8 = null, custom9 = null, custom10 = null, custom11 = null, custom12 = null, " +
				"custom13 = null, custom14 = null, custom15 = null, custom16 = null, custom17 = null, custom18 = null, " +
				"custom19 = null, custom20 = null";
		int recordsAffected = getHibernateTemplate().bulkUpdate(clearStmt);
		log.debug("Clearing the custom fields affected " + recordsAffected + " records.");
	}
	
	public void removeIdentifierDomainAttribute(IdentifierDomainAttribute identifierDomainAttribute) {
		if (identifierDomainAttribute == null || identifierDomainAttribute.getIdentifierDomainAttributeId() == null) {
			log.debug("User attempted to delete an identifier domain attribute without providing the appropriate query criteria.");
			return;
		}
		getHibernateTemplate().delete(identifierDomainAttribute);
		getHibernateTemplate().flush();
		log.debug("Removed an identifier domain instance.");
	}
	
	
//	private void initializePersons(List<Person> persons) {
//		for (Person person : persons) {
//			initializeAssociations(person);
//		}
//	}
	
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
		if (person.getUserChangedBy() != null) {
			Hibernate.initialize(person.getUserChangedBy());
		}
		if (person.getUserCreatedBy() != null) {
			Hibernate.initialize(person.getUserCreatedBy());
		}
		if (person.getUserVoidedBy() != null) {
			Hibernate.initialize(person.getUserVoidedBy());
		}
	}
	
	private void loadAssociations(Person person) {
		loadAddressType(person);
		loadEthnicGroup(person);
		loadGender(person);
		loadLanguage(person);
		loadNameType(person);
		loadNationality(person);
		loadPhoneType(person);
		loadRace(person);
		loadReligion(person);
	}

	@SuppressWarnings("unchecked")
	private void loadReligion(Person person) {
		if (person.getReligion() == null || person.getReligion().getReligionCd() != null) {
			return;
		}
		List<Religion> list = (List<Religion>) getHibernateTemplate()
			.find("from Religion as r where r.religionCode = ?",
					person.getReligion().getReligionCode());
		if (list.size() == 0) {
			getHibernateTemplate().save(person.getReligion());
		} else {
			person.setReligion(list.get(0));
		}
	}

	@SuppressWarnings("unchecked")
	private void loadRace(Person person) {
		if (person.getRace() == null || person.getRace().getRaceCd() != null) {
			return;
		}
		List<Race> list = (List<Race>) getHibernateTemplate()
			.find("from Race as r where r.raceCode = ?",
					person.getRace().getRaceCode());
		if (list.size() == 0) {
			getHibernateTemplate().save(person.getRace());
		} else {
			person.setRace(list.get(0));
		}
	}

	@SuppressWarnings("unchecked")
	private void loadNationality(Person person) {
		if (person.getNationality() == null || person.getNationality().getNationalityCd() != null) {
			return;
		}
		List<Nationality> list = (List<Nationality>) getHibernateTemplate()
			.find("from Nationality as n where n.nationalityCode = ?",
					person.getNationality().getNationalityCode());
		if (list.size() == 0) {
			getHibernateTemplate().save(person.getNationality());
		} else {
			person.setNationality(list.get(0));
		}
	}

	@SuppressWarnings("unchecked")
	private void loadNameType(Person person) {
		if (person.getNameType() == null || person.getNameType().getNameTypeCd() != null) {
			return;
		}
		List<NameType> list = (List<NameType>) getHibernateTemplate()
			.find("from NameType as n where n.nameTypeCode = ?",
					person.getNameType().getNameTypeCode());
		if (list.size() == 0) {
			getHibernateTemplate().save(person.getNameType());
		} else {
			person.setNameType(list.get(0));
		}		
	}

	@SuppressWarnings("unchecked")
	private void loadPhoneType(Person person) {
		if (person.getPhoneType() == null || person.getPhoneType().getPhoneTypeCd() != null) {
			return;
		}
		List<PhoneType> list = (List<PhoneType>) getHibernateTemplate()
			.find("from PhoneType as p where p.phoneTypeCode = ?",
					person.getPhoneType().getPhoneTypeCode());
		if (list.size() == 0) {
			getHibernateTemplate().save(person.getPhoneType());
		} else {
			person.setPhoneType(list.get(0));
		}		
	}

	@SuppressWarnings("unchecked")
	private void loadLanguage(Person person) {
		if (person.getLanguage() == null || person.getLanguage().getLanguageCd() != null) {
			return;
		}
		List<Language> list = (List<Language>) getHibernateTemplate()
			.find("from Language as l where l.languageCode = ?",
					person.getLanguage().getLanguageCode());
		if (list.size() == 0) {
			getHibernateTemplate().save(person.getLanguage());
		} else {
			person.setLanguage(list.get(0));
		}
	}

	@SuppressWarnings("unchecked")
	private void loadGender(Person person) {
		if (person.getGender() == null || person.getGender().getGenderCd() != null) {
			return;
		}
		List<Gender> list = (List<Gender>) getHibernateTemplate()
			.find("from Gender as g where g.genderCode = ?",
					person.getGender().getGenderCode());
		if (list.size() == 0) {
			getHibernateTemplate().save(person.getGender());
		} else {
			person.setGender(list.get(0));
		}
	}

	@SuppressWarnings("unchecked")
	private void loadEthnicGroup(Person person) {
		if (person.getEthnicGroup() == null || person.getEthnicGroup().getEthnicGroupCd() != null) {
			return;
		}
		List<EthnicGroup> list = (List<EthnicGroup>) getHibernateTemplate()
			.find("from EthnicGroup as e where e.ethnicGroupCode = ?",
					person.getEthnicGroup().getEthnicGroupCode());
		if (list.size() == 0) {
			getHibernateTemplate().save(person.getEthnicGroup());
		} else {
			person.setEthnicGroup(list.get(0));
		}
	}

	@SuppressWarnings("unchecked")
	private void loadAddressType(Person person) {
		if (person.getAddressType() == null || person.getAddressType().getAddressTypeCd() != null) {
			return;
		}
		List<AddressType> list = (List<AddressType>) getHibernateTemplate()
			.find("from AddressType as n where n.addressTypeCode = ?",
					person.getAddressType().getAddressTypeCode());
		if (list.size() == 0) {
			getHibernateTemplate().save(person.getAddressType());
		} else {
			person.setAddressType(list.get(0));
		}
	}

	private org.hibernate.Criteria buildHibernateCriteria(Session session, org.openhie.openempi.model.Criteria criteria) {
		org.hibernate.Criteria criteriaHibernate = session.createCriteria(Person.class).add(Restrictions.isNull("dateVoided"));
		for (Criterion criterion : criteria.getCriteria()) {
			addCriterion(criteriaHibernate, criterion);
		}
		for (OrderCriterion ord : criteria.getOrderCriteria()) {
			if (ord.getDirection() == OrderCriterion.ASCENDING_DIRECTION) {
				criteriaHibernate.addOrder(Order.asc(ord.getName()));
			} else {
				criteriaHibernate.addOrder(Order.desc(ord.getName()));
			}
		}
		if (!criteria.isLazyIdentifiers()) {
			criteriaHibernate.createCriteria("personIdentifiers").add(Restrictions.isNull("dateVoided"));
		}
		return criteriaHibernate;
	}
	
	private org.hibernate.Criteria buildHibernateCriteria(Session session, org.openhie.openempi.model.Criteria personCriteria, Set<PersonIdentifier> personIdentifiers) {
		Criteria criteria = session.createCriteria(Person.class).add(Restrictions.isNull("dateVoided"));
		if (personIdentifiers.size() > 0) {
			criteria.createAlias("personIdentifiers", "pi");
			criteria.add(Restrictions.isNull("pi.dateVoided"));
		}
		if (hasIdentifierDomainAttributes(personIdentifiers)) {
			criteria.createAlias("personIdentifiers.identifierDomain", "id");
		}
		for (Criterion criterion : personCriteria.getCriteria()) {
			addCriterion(criteria, criterion);
		}
		
		Disjunction disjunction = Restrictions.disjunction();
		for (PersonIdentifier personIdentifier : personIdentifiers) {
			if (personIdentifier.getIdentifier() != null) {
				disjunction.add(Expression.like("pi.identifier", personIdentifier.getIdentifier()));
			}
			if (personIdentifier.getIdentifierDomain() != null) {
				if (personIdentifier.getIdentifierDomain().getNamespaceIdentifier() != null) {
					disjunction.add(Restrictions.like("id.namespaceIdentifier", personIdentifier.getIdentifierDomain().getNamespaceIdentifier()));
				} else {
					disjunction.add(Restrictions.and(
							Restrictions.like("id.universalIdentifier", personIdentifier.getIdentifierDomain().getUniversalIdentifier()),
							Restrictions.like("id.universalIdentifierTypeCode", personIdentifier.getIdentifierDomain().getUniversalIdentifierTypeCode())));
				}
			}
		}
		return criteria.add(disjunction);		
	}

	private boolean hasIdentifierDomainAttributes(Set<PersonIdentifier> personIdentifiers) {
		for (PersonIdentifier pi : personIdentifiers) {
			if (pi.getIdentifierDomain() != null && (pi.getIdentifierDomain().getNamespaceIdentifier() != null ||
					pi.getIdentifierDomain().getUniversalIdentifier() != null ||
					pi.getIdentifierDomain().getUniversalIdentifierTypeCode() != null)) {
				return true;
			}
		}
		return false;
	}

	private void addCriterion(org.hibernate.Criteria criteriaHibernate, org.openhie.openempi.model.Criterion criterion) {
		if (criterion instanceof ExtendedCriterion) {
			ExtendedCriterion extended = (ExtendedCriterion) criterion;
			criteriaHibernate.createAlias(extended.getAssociationPath(), extended.getAlias());
		}
		if (criterion.getOperation().equals(Operation.EQ) || criterion.getValue() instanceof java.util.Date) {
			if (criterion.getValue() instanceof java.util.Date) {
				criteriaHibernate.add(Restrictions.eq(criterion.getName(), criterion.getValue()));
			} else {
				criteriaHibernate.add(Restrictions.eq(criterion.getName(), criterion.getValue()).ignoreCase());
			}
		} else if (criterion.getOperation().equals(Operation.ISNOTNULL)) {
			criteriaHibernate.add(Restrictions.isNotNull(criterion.getName()));
		} else if (criterion.getOperation().equals(Operation.ISNULL)) {
			criteriaHibernate.add(Restrictions.isNull(criterion.getName()));
		} else if (criterion.getOperation().equals(Operation.LIKE)) {
			criteriaHibernate.add(Restrictions.like(criterion.getName(), criterion.getValue()).ignoreCase());
		} else if (criterion.getOperation().equals(Operation.EQ)) {
			criteriaHibernate.add(Restrictions.eq(criterion.getName(), criterion.getValue()).ignoreCase());
		} else if (criterion.getOperation().equals(Operation.NE)) {
			criteriaHibernate.add(Restrictions.ne(criterion.getName(), criterion.getValue()));
		}
	}
	
	public void saveIdentifierDomain(IdentifierDomain identifierDomain) {
		log.debug("Looking for existing identifier domain " + identifierDomain);
		IdentifierDomain idFound = findIdentifierDomain(identifierDomain);
		if (idFound != null) {
			identifierDomain.setIdentifierDomainId(idFound.getIdentifierDomainId());
			identifierDomain.setIdentifierDomainName(idFound.getIdentifierDomainName());
			log.debug("Identifier domain already exists: " + identifierDomain);
			return;
		}
		
		// IdentifierDomainName is required.  if null, set same as NamespaceIdentifier
		if(identifierDomain.getIdentifierDomainName() == null ) {
			String domainName = (identifierDomain.getNamespaceIdentifier() != null) ? identifierDomain.getNamespaceIdentifier() : identifierDomain.getUniversalIdentifier();
		   identifierDomain.setIdentifierDomainName(domainName);
		   identifierDomain.setIdentifierDomainDescription(identifierDomain.getNamespaceIdentifier());
		}
		
		getHibernateTemplate().save(identifierDomain);
		getHibernateTemplate().flush();
	}
	
	public IdentifierDomain findIdentifierDomainByName(final String identifierDomainName) {
		return (IdentifierDomain) getHibernateTemplate().execute(new HibernateCallback() {
			@SuppressWarnings("unchecked")
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				Criteria criteria = session.createCriteria(IdentifierDomain.class);
				criteria.add(Restrictions.eq("identifierDomainName", identifierDomainName));
				List<IdentifierDomain> list = criteria.list();
				log.debug("Query by identifier returned: " + list.size() + " elements.");
				if (list.size() == 0) {
					return null;
				}
				IdentifierDomain entry = list.get(0);
				Hibernate.initialize(entry);
				return entry;
			}
		});
	}
	
	public IdentifierDomain findIdentifierDomain(final IdentifierDomain identifierDomain) {
		return (IdentifierDomain) getHibernateTemplate().execute(new HibernateCallback() {
			@SuppressWarnings("unchecked")
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				if (identifierDomain.getIdentifierDomainId() != null) {
					IdentifierDomain identifierDomainFound = 
							(IdentifierDomain) session.load(IdentifierDomain.class, identifierDomain.getIdentifierDomainId());
					Hibernate.initialize(identifierDomainFound);
					if (identifierDomainFound instanceof HibernateProxy) {
						identifierDomainFound =  (IdentifierDomain) ((HibernateProxy) identifierDomainFound).getHibernateLazyInitializer().getImplementation();
					}
					return identifierDomainFound;
				}
				Criteria criteria = session.createCriteria(IdentifierDomain.class);
				/**
				 * A domain identifier uniquely identifies a domain either through the namespace identifier or through the
				 * combination of universalIdentifier and universalIdentifierTypeCode
				 */
				if (identifierDomain.getNamespaceIdentifier() != null) {
					criteria.add(Restrictions.eq("namespaceIdentifier", identifierDomain.getNamespaceIdentifier()));
				} else {
					criteria.add(Restrictions.and(
							Restrictions.eq("universalIdentifier", identifierDomain.getUniversalIdentifier()),
							Restrictions.eq("universalIdentifierTypeCode", identifierDomain.getUniversalIdentifierTypeCode())));
				}
				List<IdentifierDomain> list = criteria.list();
				log.debug("Query by identifier returned: " + list.size() + " elements.");
				if (list.size() == 0) {
					return null;
				}
				IdentifierDomain entry = list.get(0);
				Hibernate.initialize(entry);
				return entry;
			}
		});		
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

	private void removeDeletedIdentifiersBySet(Person person) {
		Set<PersonIdentifier> notRemoved = new java.util.HashSet<PersonIdentifier>();
		for (PersonIdentifier id : person.getPersonIdentifiers()) {
			if (id.getDateVoided() == null) {
				notRemoved.add(id);
			}
		}
		person.setPersonIdentifiers(notRemoved);
	}
	
	@SuppressWarnings("unchecked")
	public Gender findGenderByCode(String genderCode) {
		List<Gender> list = (List<Gender>) getHibernateTemplate().
			find("from Gender as g where g.genderCode = ?", genderCode);
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public Race findRaceByCode(String raceCode) {
		List<Race> list = (List<Race>) getHibernateTemplate().
			find("from Race as r where r.raceCode = ?", raceCode);
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public Gender findGenderByName(String genderName) {
		List<Gender> list = (List<Gender>) getHibernateTemplate().
			find("from Gender as g where g.genderName = ?", genderName);
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public Race findRaceByName(String raceName) {
		List<Race> list = (List<Race>) getHibernateTemplate().
			find("from Race as r where r.raceName = ?", raceName);
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

	public int getIdentifierUpdateEventCount(User eventRecipient) {
		// TODO Auto-generated method stub

		Query query = getSession().createQuery("select count(*) from IdentifierUpdateEvent as i where i.updateRecipient = :updateRecipient");
		query.setParameter("updateRecipient", eventRecipient);
		
		int count = ((Long)query.uniqueResult()).intValue();
		return count;
	}

	public IdentifierUpdateEvent addIdentifierUpdateEvent(IdentifierUpdateEvent identifierUpdateEvent) {
		// TODO Auto-generated method stub
		getHibernateTemplate().merge(identifierUpdateEvent);
		//getHibernateTemplate().saveOrUpdate(identifierUpdateEvent);
		getHibernateTemplate().flush();
		return null;
	}

	public void removeIdentifierUpdateEvent(IdentifierUpdateEvent identifierUpdateEvent) {
		// TODO Auto-generated method stub
		if (identifierUpdateEvent == null || identifierUpdateEvent.getIdentifierUpdateEventId() == null) {
			log.debug("User attempted to delete an identifier Update Event without providing the appropriate query criteria.");
			return;
		}
		
		getHibernateTemplate().setFlushMode(HibernateAccessor.FLUSH_ALWAYS);
		IdentifierUpdateEvent deleteIdentifierUpdateEvent = findIdentifierUpdateEvent(identifierUpdateEvent.getIdentifierUpdateEventId());

		
		if (deleteIdentifierUpdateEvent == null) {
			return;
		}
/*
		Set<IdentifierUpdateEntry> preUpdateIdentifierEntries = deleteIdentifierUpdateEvent.getPreUpdateIdentifiers();
		Set<IdentifierUpdateEntry> postUpdateIdentifierEntries = deleteIdentifierUpdateEvent.getPostUpdateIdentifiers();


		for(IdentifierUpdateEntry preUpdateIdentifierEntry : preUpdateIdentifierEntries )
		{
			getHibernateTemplate().delete(preUpdateIdentifierEntry );
		}

		for(IdentifierUpdateEntry postUpdateIdentifierEntry : postUpdateIdentifierEntries )
		{
			getHibernateTemplate().delete(postUpdateIdentifierEntry);	
		}
*/
		getHibernateTemplate().delete(deleteIdentifierUpdateEvent);
		getHibernateTemplate().flush();
		log.debug("Removed identifierUpdateEvent instance.");
	}
	
	@SuppressWarnings("unchecked")
	public IdentifierUpdateEvent findIdentifierUpdateEvent(long identifierUpdateEventId) {

       IdentifierUpdateEvent identifierUpdateEvent = (IdentifierUpdateEvent) getHibernateTemplate().get(IdentifierUpdateEvent.class, identifierUpdateEventId);

	   return identifierUpdateEvent;
	}

	public List<IdentifierUpdateEvent> getIdentifierUpdateEvents(final int startIndex,final int maxEvents, final User eventRecipient) {
		// TODO Auto-generated method stub
			@SuppressWarnings("unchecked")
			List<IdentifierUpdateEvent> identifierUpdateEvents = (List<IdentifierUpdateEvent>) getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery("from IdentifierUpdateEvent i where i.updateRecipient = :updateRecipient order by i.identifierUpdateEventId");
					query.setParameter("updateRecipient", eventRecipient);
					query.setFirstResult(startIndex);
					query.setMaxResults(maxEvents);
					log.debug("Querying using " + query.toString());
					List<Person> list = (List<Person>) query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
							.list();
					log.debug("Query returned: " + list.size() + " elements.");
					return list;
				}
			});
			return identifierUpdateEvents;
	}

	public List<IdentifierUpdateEvent> getIdentifierUpdateEvents(final User eventRecipient) {
		// TODO Auto-generated method stub
			@SuppressWarnings("unchecked")
			List<IdentifierUpdateEvent> identifierUpdateEvents = (List<IdentifierUpdateEvent>) getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery("from IdentifierUpdateEvent i where i.updateRecipient = :updateRecipient order by i.identifierUpdateEventId");
					query.setParameter("updateRecipient", eventRecipient);
					log.debug("Querying using " + query.toString());
					List<Person> list = (List<Person>) query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
							.list();
					log.debug("Query returned: " + list.size() + " elements.");
					return list;
				}
			});
			return identifierUpdateEvents;
	}

	public List<IdentifierUpdateEvent> getIdentifierUpdateEventsByDate(final Date startDate, final User eventRecipient) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<IdentifierUpdateEvent> identifierUpdateEvents = (List<IdentifierUpdateEvent>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("from IdentifierUpdateEvent i where i.dateCreated > :startDate and i.updateRecipient = :updateRecipient " +
						"order by i.dateCreated");
				query.setParameter("startDate", startDate);
				query.setParameter("updateRecipient", eventRecipient);
				log.debug("Querying using " + query.toString());
				List<Person> list = (List<Person>) query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
	
				log.debug("Query returned: " + list.size() + " elements.");
				return list;
			}
		});
		return identifierUpdateEvents;
	}
	
	public List<IdentifierUpdateEvent> getIdentifierUpdateEventsBeforeDate(final Date startDate, final User eventRecipient) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<IdentifierUpdateEvent> identifierUpdateEvents = (List<IdentifierUpdateEvent>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("from IdentifierUpdateEvent i where i.dateCreated < :startDate and i.updateRecipient = :updateRecipient " +
						"order by i.dateCreated");
				query.setParameter("startDate", startDate);
				query.setParameter("updateRecipient", eventRecipient);
				log.debug("Querying using " + query.toString());
				List<Person> list = (List<Person>) query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
	
				log.debug("Query returned: " + list.size() + " elements.");
				return list;
			}
		});
		return identifierUpdateEvents;
	}
}
