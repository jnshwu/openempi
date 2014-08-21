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
package org.openhie.openempi.report.impl;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportParameter;
import org.openhie.openempi.model.ReportQuery;
import org.openhie.openempi.model.ReportQueryParameter;
import org.openhie.openempi.model.ReportRequest;
import org.openhie.openempi.model.ReportRequestParameter;
import org.openhie.openempi.report.ReportGenerator;

public abstract class AbstractReportGenerator implements ReportGenerator
{
    protected final Log log = LogFactory.getLog(getClass());

	public abstract JRXmlDataSource generateReportDataSource(ReportRequest reportRequest, Report report) throws ApplicationException;

	protected File generateReportDataFile(ReportRequest reportRequest, Report report) {
		File reportDataDirectory = Context.getReportService().getReportDataDirectory();
		long createTime = reportRequest.getRequestDate().getTime();
		String fileName = report.getName() + "-" + createTime + ".xml";
		return new File(reportDataDirectory, fileName);
	}

	protected String generateQueryString(ReportQuery query, ReportRequest reportRequest, Map<String,ReportRequestParameter> reqParamByName) throws ApplicationException {
		StringBuffer queryStr = new StringBuffer(query.getQuery());
		for (ReportQueryParameter queryParam : query.getReportQueryParameters()) {
			ReportRequestParameter reqParam = reqParamByName.get(queryParam.getReportParameter().getName());
			if (reqParam == null && Character.toUpperCase(queryParam.getRequired()) == 'Y') {
				log.error("User requested a report to be generated with a required parameter '" + queryParam.getReportParameter().getName() +
						"' for which no value was provided.");
				throw new ApplicationException("User requested a report to be generated with a required parameter '" + queryParam.getReportParameter().getName() +
						"' for which no value was provided.");
			}
			String paramValue = convertParamValueToString(queryParam.getReportParameter(), reqParam.getParameterValue());
			insertParamValue(queryStr, paramValue, queryParam);
		}
		return queryStr.toString();
	}

	private void insertParamValue(StringBuffer queryStr, String paramValue, ReportQueryParameter queryParam) {
		String clause = queryParam.getParameterName() + paramValue;
		if (queryParam.getSubstitutionKey() != null && queryParam.getSubstitutionKey().length() > 0 &&
				queryStr.indexOf("$" + queryParam.getSubstitutionKey()) >= 0) {
			log.debug("Before replacing substitution key " + queryParam.getSubstitutionKey() + " the query was: " + queryStr);
			int start = queryStr.indexOf("$" + queryParam.getSubstitutionKey());
			int end = start + queryParam.getSubstitutionKey().length() + 2;
			queryStr.replace(start, end, clause);
			log.debug("After replacing substitution key " + queryParam.getSubstitutionKey() + " the query is: " + queryStr);
		} else {
			if (queryStr.indexOf("WHERE") < 0 && queryStr.indexOf("where") < 0) {
				queryStr.append(" WHERE 1=1");
			}
			queryStr.append(" AND ").append(clause);
		}
	}

	private String convertParamValueToString(ReportParameter reportParameter, Serializable value) throws ApplicationException {
		int dataType = reportParameter.getParameterDatatype();
		if (dataType == ReportParameter.STRING_DATATYPE) {
			return "'" + value.toString() + "'";
		} else if (dataType == ReportParameter.NUMERIC_DATATYPE) {
			return value.toString();
		} else if (dataType == ReportParameter.DATE_DATATYPE) {
			return "'" + value.toString() + "'";
		}
		log.error("Unable to convert request parameter value of an unknown data type : " + reportParameter.getName() + ":" + reportParameter.getReport().getName());
		throw new ApplicationException("The request parameter is of an unknown data type.");
	}

	protected Map<String,ReportRequestParameter> generateRequestParameterMap(ReportRequest reportRequest) {
		Map<String, ReportRequestParameter> reqParamByName = new HashMap<String, ReportRequestParameter>();
		for (ReportRequestParameter reqParam : reportRequest.getReportParameters()) {
			reqParamByName.put(reqParam.getParameterName(), reqParam);
		}
		return reqParamByName;
	}
}