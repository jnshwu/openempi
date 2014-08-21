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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.proxy.HibernateProxy;
import org.openhie.openempi.dao.PersonLinkDao;
import org.openhie.openempi.model.LinkSource;
import org.openhie.openempi.model.LoggedLink;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonLink;
import org.openhie.openempi.model.ReviewRecordPair;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.orm.hibernate3.HibernateCallback;

public class PersonLinkDaoHibernate extends UniversalDaoHibernate implements PersonLinkDao
{
	private DataFieldMaxValueIncrementer incrementer;
	
	public void addPersonLink(PersonLink personLink) {
		log.debug("Storing a person link: " + personLink);
//		personLink.setLinkSource(new LinkSource(Context.getMatchingService().getMatchingServiceId()));
		getHibernateTemplate().saveOrUpdate(personLink);
		getHibernateTemplate().flush();
		log.debug("Finished saving the person link: " + personLink);
	}

	public void addPersonLinks(List<PersonLink> links) {
		for (PersonLink link : links) {
			getHibernateTemplate().saveOrUpdate(link);
		}
		getHibernateTemplate().flush();
		log.debug("Finished saving " + links.size() + " links.");
	}
	
	@SuppressWarnings("unchecked")
	public PersonLink getPersonLink(Person leftPerson, Person rightPerson) {
		Integer[] personIds = { leftPerson.getPersonId(), rightPerson.getPersonId(), leftPerson.getPersonId(), 
				rightPerson.getPersonId()};
		log.trace("Looking for links between person " + leftPerson + " and " + rightPerson);
		List<PersonLink> links = (List<PersonLink>) getHibernateTemplate().find("from PersonLink " +
				"where (personLeft.personId=? and personRight.personId=?) or " +
				"(personRight.personId=? and personLeft.personId=?) ", personIds);
        if (links.isEmpty()) {
        	log.trace("No links found between person " + leftPerson + " and " + rightPerson);
            return null;
        } else {
        	log.trace("Found links between person " + leftPerson + " and " + rightPerson);
            return (PersonLink) links.get(0);
        }
	}

	@SuppressWarnings("unchecked")
	public List<PersonLink> getPersonLinks(Person person) {
		log.trace("Looking for links to this person " + person.getPersonId());
		List<PersonLink> links = (List<PersonLink>) getHibernateTemplate().find("from PersonLink " +
				"where personLeft.personId=? or personRight.personId=?",
				new Integer[] { person.getPersonId(), person.getPersonId() });
		log.trace("Found " + links.size() + " links to person " + person.getPersonId());
		return links;
	}

	@SuppressWarnings("unchecked")
	public List<PersonLink> getPersonLinksByLinkSource(Integer linkSourceId) {
		log.trace("Looking for links for source with id: " + linkSourceId);
		List<PersonLink> links = (List<PersonLink>) getHibernateTemplate().find("from PersonLink " +
				"where linkSource.linkSourceId=?", linkSourceId);
		log.trace("Found " + links.size() + " links for source " + linkSourceId);
		return links;
	}
	
	public Map<Long,Integer> getClusterIdByRecordIdMap(final Integer sourceId) {
		final String queryStr = "select distinct cluster_id, lh_person_id from person_link where link_source_id = ? " +
				"union select distinct cluster_id, rh_person_id from person_link where link_source_id = ? order by cluster_id";
		final Map<Long,Integer> entries = new HashMap<Long,Integer>();
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(queryStr);
				@SuppressWarnings("unchecked")
				List<Object[]> rows = (List<Object[]>) query.setInteger(0, sourceId).setInteger(1, sourceId).list();
				for (Object[] items : rows) {
					log.debug("We have item " + items);
					Integer clusterId = (Integer) items[0];
					Integer personId = (Integer) items[1];
					entries.put(personId.longValue(), clusterId);
				}
				return null;
			}
		});
		return entries;
	}

	
	public Integer getClusterId(final Long[] recordIds, Integer sourceId) {
		final Set<Long> ids = new HashSet<Long>(recordIds.length);
		for (Long recordId : recordIds) {
			ids.add(recordId);
		}
		return getClusterId(ids, sourceId);
	}
	
	@SuppressWarnings("unchecked")
	public Integer getClusterId(final Set<Long> recordIds, final Integer sourceId) {
		final Set<Integer> ids = convertRecordIdsToInts(recordIds);
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<Integer> clusterIds = session.createCriteria(PersonLink.class)
					.setProjection(Projections.distinct(Projections.property("clusterId")))
					.add(Expression.and(
							Expression.or(Expression.in("personLeft.personId", ids), Expression.in("personRight.personId", ids)),
							Expression.eq("linkSource.linkSourceId", sourceId)))
					.createAlias("personLeft", "pl")
					.createAlias("personRight", "pr")
					.add(Restrictions.isNull("pr.dateVoided"))
					.add(Restrictions.isNull("pl.dateVoided"))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
				if (clusterIds.size() > 1) {
					log.error("Encountered an unexpected condition; we have a set of nodes that are not all sharing the same cluster ID: " + recordIds);
					throw new RuntimeException("Unstable condition in linking module; found a set of nodes that don't share the same cluster ID");
				}
				if (clusterIds.size() == 0) {
					return null;
				}
				return clusterIds.get(0);
			}
		});
	}

	/**
	 * This method returns a cluster ID for two person records that are about to be linked together. It is possible that
	 * either one, both or none of the two persons already belong to a cluster. There are three cases that need to be
	 * considered:
	 * <ul>
	 *    <li>Case 1: No clusters associated with either one of the two record. This is the easiest case to handle since
	 *    these two records are not linked to other records, thereby they have no links and no existing cluster ID to 
	 *    deal with. The solution is to simply create and return a new cluster ID.</li>
	 *    <li>Case 2: In this case one of the two records have a cluster associated with them. The way we handle this
	 *    case is we let the record that does not belong to a cluster yet join the existing cluster by assigning the
	 *    existing cluster ID to every edge that is created between the two sets of nodes.
	 *    <li>Case 3: In this case both records belong to separate clusters. This is the most complex of the three
	 *    cases. The way we handle it is we create a new cluster ID and assign it to all the edges, both the existing
	 *    edges in both clusters and the new edges that are created to create a complete graph between the two
	 *    clusters.
	 * </ul>
	 */
	public void convertReviewLinkToLink(ReviewRecordPair recordPair) {
		List<PersonLink> leftLinks = getPersonLinks(recordPair.getPersonLeft());
		List<PersonLink> rightLinks = getPersonLinks(recordPair.getPersonRight());
		// Case 1.
		if (leftLinks.size() == 0 && rightLinks.size() == 0) {
			PersonLink link = buildPersonLinkFromReviewRecordPair(recordPair);
			link.setClusterId(getNextClusterId());
			addPersonLink(link);
			return;
		}
		
		// Case 3.
		if (leftLinks.size() > 0 && rightLinks.size() > 0) {
			log.debug("Handling the merge of two clusters as a result of linkage of review pair: " + recordPair);
			Integer clusterId = getNextClusterId();
			for (PersonLink link : leftLinks) {
				link.setClusterId(clusterId);
				save(link);
			}
			for (PersonLink link : rightLinks) {
				link.setClusterId(clusterId);
				save(link);
			}
			// Make links across clusters;
			Set<Person> leftPersons = getNodesFromLinks(leftLinks);
			Set<Person> rightPersons = getNodesFromLinks(rightLinks);
			for (Person leftPerson : leftPersons) {
				for (Person rightPerson : rightPersons) {
					PersonLink link = buildPersonLinkFromNodes(recordPair, leftPerson, rightPerson);
					if (log.isDebugEnabled()) {
						log.debug("Creating link " + link.getPersonLinkId() + " between nodes " + leftPerson.getPersonId() + " and " + rightPerson.getPersonId());
					}
					link.setClusterId(clusterId);
					addPersonLink(link);
				}
			}
			return;
		}
		
		// Case 2.
		List<PersonLink> links = null;
		Person singleNode=null;
		if (leftLinks.size() > 0) {
			links = leftLinks;
			singleNode = recordPair.getPersonRight();
		} else {
			links = rightLinks;
			singleNode = recordPair.getPersonLeft();
		}
		Set<Person> nodes = getNodesFromLinks(links);
		Integer clusterId = links.get(0).getClusterId();
		for (Person rightNode : nodes) {
			PersonLink link = buildPersonLinkFromNodes(recordPair, singleNode, rightNode);
			link.setClusterId(clusterId);
			addPersonLink(link);
		}
	}
	
	private PersonLink buildPersonLinkFromNodes(ReviewRecordPair recordPair, Person leftPerson, Person rightPerson) {
		PersonLink personLink = new PersonLink();
		personLink.setDateCreated(recordPair.getDateReviewed());
		personLink.setUserCreatedBy(recordPair.getUserCreatedBy());
		personLink.setPersonLeft(leftPerson);
		personLink.setPersonRight(rightPerson);
		//TODO: This needs to be fixed at some point.
		// the weight of this node is not the same as the weight of the original link that caused these two nodes to be clustered
		// but to evaluate the weight we need to make a call into the matching service and that capability does not currently
		// exist separately from the match call.
		personLink.setWeight(recordPair.getWeight());
		LinkSource linkSource = new LinkSource();
		linkSource.setLinkSourceId(LinkSource.MANUAL_MATCHING_SOURCE);
		personLink.setLinkSource(linkSource);
		return personLink;
	}

	private Set<Person> getNodesFromLinks(List<PersonLink> links) {
		Set<Person> nodes = new HashSet<Person>();
		for (PersonLink link : links) {
			nodes.add(link.getPersonLeft());
			nodes.add(link.getPersonRight());
		}
		return nodes;
	}

	private PersonLink buildPersonLinkFromReviewRecordPair(ReviewRecordPair recordPair) {
		PersonLink personLink = new PersonLink();
		personLink.setDateCreated(recordPair.getDateReviewed());
		personLink.setUserCreatedBy(recordPair.getUserCreatedBy());
		personLink.setPersonLeft(recordPair.getPersonLeft());
		personLink.setPersonRight(recordPair.getPersonRight());
		personLink.setWeight(recordPair.getWeight());
		LinkSource linkSource = new LinkSource();
		linkSource.setLinkSourceId(LinkSource.MANUAL_MATCHING_SOURCE);
		personLink.setLinkSource(linkSource);
		return personLink;
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonLink> getPersonLinks(final Integer clusterId) {
		log.trace("Looking for links with cluster ID: " + clusterId);
		List<PersonLink> links = (List<PersonLink>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<PersonLink> links = session.createCriteria(PersonLink.class)
					.add(Expression.eq("clusterId", clusterId))
					.createAlias("personLeft", "pl")
					.createAlias("personRight", "pr")
					.add(Restrictions.isNull("pr.dateVoided"))
					.add(Restrictions.isNull("pl.dateVoided"))
					.addOrder(Order.asc("personLeft.personId"))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
				return links;
			}
		});
		return links;		
	}

	@SuppressWarnings("unchecked")
	public List<PersonLink> getPersonLinks(final Set<Long> recordIds) {
		log.trace("Looking for links to any of the following ids: " + recordIds);
		final Set<Integer> ids = convertRecordIdsToInts(recordIds);
		List<PersonLink> links = (List<PersonLink>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<PersonLink> links = session.createCriteria(PersonLink.class)
					.add(Expression.or(Expression.in("personLeft.personId", ids), Expression.in("personRight.personId", ids)))
					.addOrder(Order.asc("personLeft.personId"))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
				return links;
			}
		});
		return links;
	}
	
	public int removeAllLinks() {
		log.trace("Removing all person links.");
		int deleteCount = getHibernateTemplate().bulkUpdate("delete from PersonLink");
		log.trace("Removed " + deleteCount + " links from system.");
		return deleteCount;
	}
	
	public int removeLinksBySource(LinkSource linkSource) {
		log.trace("Removing all links associated with source: " + linkSource);
		if (linkSource == null || linkSource.getLinkSourceId() == null) {
			return 0;
		}
		int deleteCount = getHibernateTemplate()
				.bulkUpdate("delete from PersonLink where linkSource.linkSourceId = ?", linkSource.getLinkSourceId());
		log.trace("Removed " + deleteCount + " links from system.");
		return deleteCount;
	}

	public void removeLink(PersonLink personLink) {
		log.trace("Removing person link with id " + personLink.getPersonLinkId());
		PersonLink personLinkFound = (PersonLink) getHibernateTemplate().load(PersonLink.class, personLink.getPersonLinkId());
		if (personLinkFound == null) {
			return;
		}
		getHibernateTemplate().delete(personLinkFound);
		log.trace("Removed person link " + personLinkFound);
	}

	public void addReviewRecordPair(ReviewRecordPair personLinkReview) {
		log.debug("Storing a person link review: " + personLinkReview);
		getHibernateTemplate().saveOrUpdate(personLinkReview);
		getHibernateTemplate().flush();
		log.debug("Finished saving the person link: " + personLinkReview);
	}

	public void addReviewRecordPairs(List<ReviewRecordPair> pairs) {
		for (ReviewRecordPair pair : pairs) {
			log.debug("Storing a person link review: " + pair);
			getHibernateTemplate().saveOrUpdate(pair);
		}
		getHibernateTemplate().flush();
		log.debug("Finished saving " + pairs.size() + " review links.");
	}

	@SuppressWarnings("unchecked")
	public List<ReviewRecordPair> getAllUnreviewedReviewRecordPairs() {
		log.trace("Retrieving all person link review entries.");
		List<ReviewRecordPair> links = (List<ReviewRecordPair>)
				getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						List< ReviewRecordPair> list = session
							.createQuery("from ReviewRecordPair where dateReviewed is null order by dateCreated desc")
							.list();
						for (ReviewRecordPair pair : list) {
							hydratePerson(pair.getPersonLeft());
							hydratePerson(pair.getPersonRight());
						}
						return list;
					}
				});
		log.trace("Found " + links.size() + " person link review entries");
		return links;
	}

	@SuppressWarnings("unchecked")
	public List<ReviewRecordPair> getUnreviewedReviewRecordPairs(final int maxResults) {
		log.trace("Retrieving person link review entries.");
		List<ReviewRecordPair> links = (List<ReviewRecordPair>)
				getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						List< ReviewRecordPair> list = session
							.createQuery("from ReviewRecordPair where dateReviewed is null order by dateCreated desc")
							.setMaxResults(maxResults)
							.list();
						for (ReviewRecordPair pair : list) {
							hydratePerson(pair.getPersonLeft());
							hydratePerson(pair.getPersonRight());
						}
						return list;
					}
				});
		log.trace("Found " + links.size() + " person link review entries");
		return links;
	}
	
	public ReviewRecordPair getReviewRecordPair(final int reviewRecordPairId) {
		log.trace("Loading person link review with id " + reviewRecordPairId);
		ReviewRecordPair reviewRecordPair = 
				(ReviewRecordPair) getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						ReviewRecordPair pair = (ReviewRecordPair) session.load(ReviewRecordPair.class, reviewRecordPairId);
						if (pair == null || pair.getPersonLeft() == null || pair.getPersonRight() == null) {
							return pair;
						}
						if (pair instanceof HibernateProxy) {
							pair =  (ReviewRecordPair) ((HibernateProxy) pair).getHibernateLazyInitializer().getImplementation();
						}						
						hydratePerson(pair.getPersonLeft());
						hydratePerson(pair.getPersonRight());
						return pair;
					}
				});
		return reviewRecordPair;
	}

	public ReviewRecordPair getReviewRecordPair(final int leftPersonId, final int rightPersonId) {
		log.trace("Retrieving person link review entry by endpoints.");
		ReviewRecordPair pair = (ReviewRecordPair)
				getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						@SuppressWarnings("unchecked")
						List< ReviewRecordPair> list = session
							.createQuery("from ReviewRecordPair where dateReviewed is null " +
									"and (personLeft.personId = :leftPersonId and personRight.personId = :rightPersonId) " +
									" or (personLeft.personId = :rightPersonId and personRight.personId = :leftPersonId)")
							.setParameter("leftPersonId", leftPersonId)
							.setParameter("rightPersonId", rightPersonId)
							.list();
						if (list.size() == 0) {
							return null;
						}
						return list.get(0);
					}
				});
		log.trace("Found " + pair + " as existing person link review entry");
		return pair;
	}
	
	public void updateReviewRecordPair(ReviewRecordPair reviewRecordPair) {
		log.debug("Updating person link review record: " + reviewRecordPair);
		getHibernateTemplate().merge(reviewRecordPair);
		getHibernateTemplate().flush();
		log.debug("Finished updating the person link review entry.");		
	}

	public void removeAllReviewRecordPairs() {
		log.trace("Removing all person link review entries.");
		getHibernateTemplate().bulkUpdate("delete from ReviewRecordPair");
		log.trace("Removed all person link review entries from system.");
	}
	
	public int removeReviewLinksBySource(LinkSource linkSource) {
		log.trace("Removing all review links associated with source: " + linkSource);
		if (linkSource == null || linkSource.getLinkSourceId() == null) {
			return 0;
		}
		int deleteCount = getHibernateTemplate()
				.bulkUpdate("delete from ReviewRecordPair where linkSource.linkSourceId = ?", linkSource.getLinkSourceId());
		log.trace("Removed " + deleteCount + " review links from system.");
		return deleteCount;
	}

	public void removeReviewRecordPair(ReviewRecordPair reviewRecordPair) {
		log.trace("Removing review record pair with id " + reviewRecordPair.getReviewRecordPairId());
		ReviewRecordPair reviewRecordPairFound = (ReviewRecordPair) 
				getHibernateTemplate().load(ReviewRecordPair.class, reviewRecordPair.getReviewRecordPairId());
		if (reviewRecordPairFound == null) {
			return;
		}
		getHibernateTemplate().delete(reviewRecordPairFound);
		log.trace("Removed review record pair: " + reviewRecordPairFound);		
	}
	
	public void addLoggedLink(LoggedLink loggedLink) {
		log.debug("Storing a logged link: " + loggedLink);
		getHibernateTemplate().saveOrUpdate(loggedLink);
		getHibernateTemplate().flush();
		log.debug("Finished saving the logged link: " + loggedLink);
	}

	public LoggedLink getLoggedLink(final Integer loggedLinkId) {		
		log.trace("Loading Logged link with id " + loggedLinkId);
		LoggedLink loggedLink = 
				(LoggedLink) getHibernateTemplate().execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						LoggedLink pair = (LoggedLink) session.load(LoggedLink.class, loggedLinkId);
						if (pair == null || pair.getRightRecordId() == null || pair.getLeftRecordId() == null) {
							return pair;
						}
						if (pair instanceof HibernateProxy) {
							pair =  (LoggedLink) ((HibernateProxy) pair).getHibernateLazyInitializer().getImplementation();
						}						
						return pair;
					}
				});
		return loggedLink;
	}

	public int getLoggedLinksCount(int vectorValue) {
		int count = ((Long) getSession()
				.createQuery("select count(*) from LoggedLink where vectorValue = " + vectorValue)
				.uniqueResult()).intValue();
		return count;
	}
	
	public List<LoggedLink> getLoggedLinks(final int vectorValue, final int start, final int maxResults) {
		log.trace("Retrieving logged links.");
		@SuppressWarnings("unchecked")
		List<LoggedLink> links = (List<LoggedLink>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						Query query = session.createQuery("from LoggedLink where vectorValue = " + vectorValue);
						query.setFirstResult(start);
						query.setMaxResults(maxResults);
						List<LoggedLink> list = query.list();
						return list;
					}
				});
		log.trace("Found " + links.size() + " logged links.");
		return links;		
	}
	
	protected void hydratePerson(Person person) {
		if (person == null) {
			return;
		}
		person.toStringLong();
	}

	public Integer getNextClusterId() {
		return incrementer.nextIntValue();
	}
	
	public DataFieldMaxValueIncrementer getIncrementer() {
		return incrementer;
	}

	public void setIncrementer(DataFieldMaxValueIncrementer incrementer) {
		this.incrementer = incrementer;
	}	
	
	private Set<Integer> convertRecordIdsToInts(Set<Long> recordIds) {
		Set<Integer> ids = new java.util.HashSet<Integer>(recordIds.size());
		for (Long lvalue : recordIds) {
			ids.add(lvalue.intValue());
		}
		return ids;
	}
}
