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
import javax.persistence.FetchType;
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

@Entity
@Table(name="report_query_parameter")
@GenericGenerator(name = "report_query_parameter_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "report_query_parameter_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
public class ReportQueryParameter extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1943429923033311936L;
	
	public static final char NOT_REQUIRED_PARAMETER = 'N';
	public static final char REQUIRED_PARAMETER = 'Y';
	
	private Integer reportQueryParameterId;
	private ReportQuery reportQuery;
	private ReportParameter reportParameter;
	private String parameterName;
	private char required;
	private String substitutionKey;
	
	public ReportQueryParameter() {
		required = REQUIRED_PARAMETER;
	}

	@Id
	@GeneratedValue(generator="report_query_parameter_gen")
	@Column(name = "report_query_parameter_id", unique = true, nullable = false)
	public Integer getReportQueryParameterId() {
		return reportQueryParameterId;
	}

	public void setReportQueryParameterId(Integer reportQueryParameterId) {
		this.reportQueryParameterId = reportQueryParameterId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "report_parameter_id", nullable = false)
	public ReportParameter getReportParameter() {
		return reportParameter;
	}

	public void setReportParameter(ReportParameter reportParameter) {
		this.reportParameter = reportParameter;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "report_query_id", nullable = false)
	public ReportQuery getReportQuery() {
		return reportQuery;
	}

	public void setReportQuery(ReportQuery reportQuery) {
		this.reportQuery = reportQuery;
	}

	@Column(name="parameter_name", nullable=false)
	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	@Column(name="required")
	public char getRequired() {
		return required;
	}

	public void setRequired(char required) {
		this.required = required;
	}
	
	@Column(name="substitution_key", nullable=true)
	public String getSubstitutionKey() {
		return substitutionKey;
	}

	public void setSubstitutionKey(String substitutionKey) {
		this.substitutionKey = substitutionKey;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportQueryParameter other = (ReportQueryParameter) obj;
		if (parameterName == null) {
			if (other.parameterName != null)
				return false;
		} else if (!parameterName.equals(other.parameterName))
			return false;
		if (reportQueryParameterId == null) {
			if (other.reportQueryParameterId != null)
				return false;
		} else if (!reportQueryParameterId.equals(other.reportQueryParameterId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parameterName == null) ? 0 : parameterName.hashCode());
		result = prime * result + ((reportQueryParameterId == null) ? 0 : reportQueryParameterId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "ReportQueryParameter [reportQueryParameterId=" + reportQueryParameterId + ", reportQuery="
				+ reportQuery + ", reportParameter=" + reportParameter + ", parameterName=" + parameterName
				+ ", required=" + required + "]";
	}
}
