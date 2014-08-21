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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "report_parameter")
@GenericGenerator(name = "report_parameter_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "report_parameter_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class ReportParameter extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1943429923033311936L;
	
	public static final int DATE_DATATYPE = 0;
	public static final int STRING_DATATYPE = 1;
	public static final int NUMERIC_DATATYPE = 2;
	
	private Integer reportParameterId;
	private Report report;
	private String name;
	private String nameDisplayed;
	private String description;
	private int parameterDatatype;
	
	@Id
	@GeneratedValue(generator="report_parameter_gen")
	@Column(name = "report_parameter_id", unique = true, nullable = false)
	public Integer getReportParameterId() {
		return reportParameterId;
	}

	public void setReportParameterId(Integer reportParameterId) {
		this.reportParameterId = reportParameterId;
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

	@Column(name="parameter_datatype")
	public int getParameterDatatype() {
		return parameterDatatype;
	}

	public void setParameterDatatype(int parameterDatatype) {
		this.parameterDatatype = parameterDatatype;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportParameter other = (ReportParameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reportParameterId == null) {
			if (other.reportParameterId != null)
				return false;
		} else if (!reportParameterId.equals(other.reportParameterId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((reportParameterId == null) ? 0 : reportParameterId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ReportParameter [reportParameterId=" + reportParameterId + ", report=" + report + ", name=" + name
				+ ", nameDisplayed=" + nameDisplayed + ", description=" + description + ", parameterDatatype="
				+ parameterDatatype + "]";
	}
}
