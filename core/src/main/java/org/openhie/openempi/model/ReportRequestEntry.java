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
package org.openhie.openempi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ReportRequestEntry entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "report_request")
@GenericGenerator(name = "report_request_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "report_request_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class ReportRequestEntry extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1943429923033311936L;
	public static final String NOT_COMPLETED_PARAMETER = "N";
	public static final String COMPLETED_PARAMETER = "Y";
	
	private Integer reportRequestId;
	private Report report;
	private User userRequested;
	private Date dateRequested;
	private Date dateCompleted;
	private String completed;
	private String reportHandle;
	
	@Id
	@GeneratedValue(generator="report_request_gen")
	@Column(name = "report_request_id", unique = true, nullable = false)
	public Integer getReportRequestId() {
		return reportRequestId;
	}

	public void setReportRequestId(Integer reportRequestId) {
		this.reportRequestId = reportRequestId;
	}

	@ManyToOne
	@JoinColumn(name="report_id")
	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_requested_id", nullable = false)
	public User getUserRequested() {
		return userRequested;
	}

	public void setUserRequested(User userRequested) {
		this.userRequested = userRequested;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_requested", nullable = false, length = 8)
	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_completed", nullable = true, length = 8)
	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	@Column(name="completed")
	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	@Column(name="report_handle")
	public String getReportHandle() {
		return reportHandle;
	}

	public void setReportHandle(String reportHandle) {
		this.reportHandle = reportHandle;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportRequestEntry other = (ReportRequestEntry) obj;
		if (dateRequested == null) {
			if (other.dateRequested != null)
				return false;
		} else if (!dateRequested.equals(other.dateRequested))
			return false;
		if (reportRequestId == null) {
			if (other.reportRequestId != null)
				return false;
		} else if (!reportRequestId.equals(other.reportRequestId))
			return false;
		if (userRequested == null) {
			if (other.userRequested != null)
				return false;
		} else if (!userRequested.equals(other.userRequested))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateRequested == null) ? 0 : dateRequested.hashCode());
		result = prime * result + ((reportRequestId == null) ? 0 : reportRequestId.hashCode());
		result = prime * result + ((userRequested == null) ? 0 : userRequested.hashCode());
		return result;
	}


	@Override
	public String toString() {
		return "ReportRequestEntry [reportRequestId=" + reportRequestId + ", report=" + report + ", userRequested="
				+ userRequested + ", dateRequested=" + dateRequested + ", completed=" + completed + ", reportHandle="
				+ reportHandle + "]";
	}
}
