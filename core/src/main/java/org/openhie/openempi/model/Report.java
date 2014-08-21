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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Report entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "report")
@GenericGenerator(name = "report_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "report_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class Report extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1943429923033311936L;
	private Logger log = Logger.getLogger(getClass());
	
	private Integer reportId;
	private String name;
	private String nameDisplayed;
	private String description;
	private String templateName;
	private String dataGenerator;
	private Set<ReportQuery> reportQueries = new HashSet<ReportQuery>();
	private Set<ReportParameter> reportParameters = new HashSet<ReportParameter>();
	
	public Report() {
	}
	
	@Id
	@GeneratedValue(generator="report_gen")
	@Column(name = "report_id", unique = true, nullable = false)
	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	@Column(name="name", nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="name_displayed", nullable=false)
	public String getNameDisplayed() {
		return nameDisplayed;
	}

	public void setNameDisplayed(String nameDisplayed) {
		this.nameDisplayed = nameDisplayed;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="template_name", nullable=false)
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Column(name="data_generator", nullable=false)
	public String getDataGenerator() {
		return dataGenerator;
	}

	public void setDataGenerator(String dataGenerator) {
		this.dataGenerator = dataGenerator;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			mappedBy = "report", targetEntity = ReportQuery.class)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
        org.hibernate.annotations.CascadeType.DELETE,
        org.hibernate.annotations.CascadeType.MERGE,
        org.hibernate.annotations.CascadeType.PERSIST,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})	
	public Set<ReportQuery> getReportQueries() {
		return reportQueries;
	}

	public void setReportQueries(Set<ReportQuery> reportQueries) {
		this.reportQueries = reportQueries;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			mappedBy = "report", targetEntity = ReportParameter.class)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
	            org.hibernate.annotations.CascadeType.DELETE,
	            org.hibernate.annotations.CascadeType.MERGE,
	            org.hibernate.annotations.CascadeType.PERSIST,
	            org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<ReportParameter> getReportParameters() {
		return reportParameters;
	}

	public void setReportParameters(Set<ReportParameter> reportParameters) {
		this.reportParameters = reportParameters;
	}

	public void addReportParameter(ReportParameter reportParameter) {
		reportParameters.add(reportParameter);
	}
	
	public boolean isRequiredParameter(String name) {
		ReportParameter param = getReportParameterByName(name);
		if (param == null) {
			return false;
		}
		boolean required = false;
		for (ReportQuery query : getReportQueries()) {
			if (query.getReportQueryParameters().size() == 0) {
				continue;
			}
			for (ReportQueryParameter queryParam : query.getReportQueryParameters()) {
				ReportParameter queryParameter = queryParam.getReportParameter(); 
				if (queryParameter.getName().equalsIgnoreCase(name) && Character.toUpperCase(queryParam.getRequired()) == 'Y') {
					log.debug("In report " + getName() + " parameter " + name + " is a required parameter for query " + query.getName());
					required = true;
				}
			}
		}
		return required;
	}
	
	public ReportParameter getReportParameterByName(String name) {
		for (ReportParameter param : reportParameters) {
			if (param.getName().equalsIgnoreCase(name)) {
				return param;
			}
		}
		return null;
	}

	public void removeReportParameter(ReportParameter reportParameter) {
		boolean removed = reportParameters.remove(reportParameter);
		log.debug("Removing parameter from report returned: " + removed);
	}
	
	public void addReportQuery(ReportQuery reportQuery) {
		reportQueries.add(reportQuery);
	}
	
	public ReportQuery getReportQueryByName(String name) {
		for (ReportQuery query : reportQueries) {
			if (query.getName().equalsIgnoreCase(name)) {
				return query;
			}
		}
		return null;
	}
	
	public void removeReportQuery(ReportQuery reportQuery) {
		reportQueries.remove(reportQuery);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Report other = (Report) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reportId == null) {
			if (other.reportId != null)
				return false;
		} else if (!reportId.equals(other.reportId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((reportId == null) ? 0 : reportId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Report [reportId=" + reportId + ", name=" + name + ", nameDisplayed=" + nameDisplayed
				+ ", description=" + description + "]";
	}
}
