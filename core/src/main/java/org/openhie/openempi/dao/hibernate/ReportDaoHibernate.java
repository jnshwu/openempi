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

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.openhie.openempi.dao.ReportDao;
import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportParameter;
import org.openhie.openempi.model.ReportQuery;
import org.openhie.openempi.model.ReportQueryParameter;
import org.openhie.openempi.model.ReportRequestEntry;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class ReportDaoHibernate extends UniversalDaoHibernate implements ReportDao
{
	private Integer maxReportRequestsReturned;
	
	public Report addReport(Report report) {
		log.debug("Storing a report: " + report);
		getHibernateTemplate().saveOrUpdate(report);
		log.debug("Finished saving the report: " + report);
		return report;
	}

	@SuppressWarnings("unchecked")
	public List<Report> getReports() {
		log.trace("Retrieving all reports from the system.");
		return (List<Report>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<Report> reports = (List<Report>) session
						.createQuery("from Report r order by r.name asc")
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
				log.trace("Found " + reports.size() + " reports in the system.");
				for (Report report : reports) {
					session.evict(report);
				}
				return reports;
			}			
		});
	}

	@SuppressWarnings("unchecked")
	public Report getReportByName(String reportName) {
		log.trace("Retrieving report named: " + reportName);
		if (reportName == null || reportName.length() == 0) {
			return null;
		}
		
		List<Report> reports = (List<Report>) getHibernateTemplate().find("from Report r " +
				"where r.name like ?", reportName);
        if (reports.isEmpty()) {
        	log.trace("No report found by the name " + reportName);
            return null;
        }
        Report report = reports.get(0);
        log.trace("Found report " + report);
        return report;
	}

	@SuppressWarnings("unchecked")
	public Report getReportById(Integer reportId) {
		log.trace("Retrieving report by ID: " + reportId);
		if (reportId == null || reportId.intValue() == 0) {
			return null;
		}
		
		List<Report> reports = (List<Report>) getHibernateTemplate().find("from Report r " +
				"where r.reportId = ?", reportId);
        if (reports.isEmpty()) {
        	log.trace("No report found with the ID: " + reportId);
            return null;
        }
        Report report = reports.get(0);
        log.trace("Found report " + report);
        return report;
	}

	public Report updateReport(Report report) {
		if (report == null || report.getReportId() == null) {
			log.warn("User attempted to update a report but provided insufficient information to identify it.");
			return report;
		}
		getHibernateTemplate().update(report);
		getHibernateTemplate().flush();
		log.trace("Updated the report: " + report);
		return report;
	}

	public void removeReport(Report report) {
		if (report == null || report.getReportId() == null) {
			log.warn("User attempted to delete a report but provided insufficient information to identify it.");
			return;
		}
		report = this.getReportById(report.getReportId());
		HibernateTemplate template = getHibernateTemplate();
		for (ReportQuery query : report.getReportQueries()) {
			for (ReportQueryParameter param : query.getReportQueryParameters()) {
				template.delete(param);
			}
			template.delete(query);
		}
		for (ReportParameter param : report.getReportParameters()) {
			template.delete(param);
		}
		template.delete(report);
		template.flush();
		log.debug("Removed an identifier domain instance.");
	}

	@SuppressWarnings("unchecked")
	public List<ReportRequestEntry> getReportRequests() {
		log.trace("Retrieving all reports from the system.");
		return (List<ReportRequestEntry>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<ReportRequestEntry> reports = (List<ReportRequestEntry>) session
						.createQuery("from ReportRequestEntry r order by r.dateRequested desc")
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.setMaxResults(maxReportRequestsReturned)
						.list();
				log.trace("Found " + reports.size() + " report requests in the system.");
				for (ReportRequestEntry entry : reports) {
					session.evict(entry);
				}
				return reports;
			}			
		});
	}
	
	public ReportRequestEntry addReportRequest(ReportRequestEntry reportRequest) {
		log.debug("Storing a report request: " + reportRequest);
		getHibernateTemplate().saveOrUpdate(reportRequest);
		getHibernateTemplate().flush();
		log.debug("Finished saving the report request: " + reportRequest);
		return reportRequest;
	}

	public void reassignReportRequestsToReport(final Integer oldReportId, final Integer newReportId) {
		log.debug("Re-assigning report requests from report " + oldReportId + " to " + newReportId);
		Integer recordsUpdated = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String updateStmt = "update ReportRequestEntry rre SET rre.report.reportId = :newReportId WHERE rre.report.reportId = :oldReportId";
				Query query = session.createQuery(updateStmt)
						.setInteger("oldReportId", oldReportId)
						.setInteger("newReportId", newReportId);
				log.debug("Doing report re-assignment using statement: " + query.getQueryString());
				int recordsUpdated = query.executeUpdate();
				return recordsUpdated;
			}
		});
		log.debug("Completed the reassignment on " + recordsUpdated + " entries.");
	}
	
	public ReportRequestEntry updateReportRequest(ReportRequestEntry reportRequest) {
		if (reportRequest == null || reportRequest.getReportRequestId() == null) {
			log.warn("User attempted to update a report request but provided insufficient information to identify it.");
			return reportRequest;
		}
		getHibernateTemplate().update(reportRequest);
		getHibernateTemplate().flush();
		log.trace("Updated the report request: " + reportRequest);
		return reportRequest;
	}
	
	public Integer getMaxReportRequestsReturned() {
		return maxReportRequestsReturned;
	}

	public void setMaxReportRequestsReturned(Integer maxReportRequestsReturned) {
		this.maxReportRequestsReturned = maxReportRequestsReturned;
	}	
}
