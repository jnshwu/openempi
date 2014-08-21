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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ReportQuery entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "report_query")
@GenericGenerator(name = "report_query_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "report_query_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class ReportQuery extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1943429923033311936L;
	
	private Integer reportQueryId;
	private Report report;
	private String name;
	private String query;
	private Set<ReportQueryParameter> reportQueryParameters  = new HashSet<ReportQueryParameter>();
	
	@Id
	@GeneratedValue(generator="report_query_gen")
	@Column(name = "report_query_id", unique = true, nullable = false)
	public Integer getReportQueryId() {
		return reportQueryId;
	}

	public void setReportQueryId(Integer reportQueryId) {
		this.reportQueryId = reportQueryId;
	}

	@ManyToOne
	@JoinColumn(name="report_id")
	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	@Column(name="name", nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="query", nullable=false)
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			mappedBy = "reportQuery", targetEntity = ReportQueryParameter.class)
	public Set<ReportQueryParameter> getReportQueryParameters() {
		return reportQueryParameters;
	}

	public void setReportQueryParameters(Set<ReportQueryParameter> reportQueryParameters) {
		this.reportQueryParameters = reportQueryParameters;
	}

	public void addReportQueryParameter(ReportQueryParameter reportQueryParameter) {
		reportQueryParameters.add(reportQueryParameter);
	}
	
	public void removeReportQueryParameter(ReportParameter reportParameter) {
		reportQueryParameters.remove(reportParameter);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportQuery other = (ReportQuery) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reportQueryId == null) {
			if (other.reportQueryId != null)
				return false;
		} else if (!reportQueryId.equals(other.reportQueryId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((reportQueryId == null) ? 0 : reportQueryId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ReportQuery [reportQueryId=" + reportQueryId + ", report=" + report + ", name=" + name + ", query="
				+ query + ", reportQueryParameters=" + reportQueryParameters + "]";
	}
}
