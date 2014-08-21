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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportQuery;
import org.openhie.openempi.model.ReportRequest;
import org.openhie.openempi.model.ReportRequestParameter;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

public class JdbcReportGenerator extends AbstractReportGenerator
{
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * Generates data for a report using one or more JDBC queries
	 * as the source of the data.
	 */
	public JRXmlDataSource generateReportDataSource(ReportRequest reportRequest, Report report) throws ApplicationException {
		Element root = new Element(report.getName());
		Document doc = new Document(root);
		
		File reportDataFile = generateReportDataFile(reportRequest, report);
		
		Set<ReportQuery> queries = report.getReportQueries();
		Map<String,ReportRequestParameter> reqParamByName = generateRequestParameterMap(reportRequest);
		for (ReportQuery query : queries) {
			generateQueryData(reportRequest, report, query, root, reqParamByName);
		}
		try {
			String document = new XMLOutputter().outputString(doc);
			FileWriter writer = new FileWriter(reportDataFile);
			writer.write(document);
			writer.close();
			
			log.debug("Generated XML for report: " + report.getName() + " in file: " + reportDataFile.getAbsolutePath());
			ByteArrayInputStream inputStream = new ByteArrayInputStream(document.getBytes());
			JRXmlDataSource dataSource = new JRXmlDataSource(inputStream).dataSource("/");
			return dataSource;
		} catch (JRException e) {
			log.error("Error while generating report " + report.getName() + ". Error message: " + e.getMessage(), e);
			throw new RuntimeException("Failed while generating report: " + report.getName() + ". Error: " + e.getMessage());
		} catch (IOException e) {
			log.error("Error while generating report " + report.getName() + ". Error message: " + e.getMessage(), e);
			throw new RuntimeException("Failed while generating report: " + report.getName() + ". Error: " + e.getMessage());
		}
	}

	private void generateQueryData(final ReportRequest reportRequest, final Report report, final ReportQuery query, final Element root, Map<String,ReportRequestParameter> reqParamByName) throws ApplicationException {
		log.debug("Generating data for query: " + query.getName());
		String queryStr = generateQueryString(query, reportRequest, reqParamByName);
		final Element queryElem = new Element(query.getName());
		root.addContent(queryElem);
		getJdbcTemplate().execute(queryStr, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				ResultSet rset = pstmt.executeQuery();
				ResultSetMetaData metadata = pstmt.getMetaData();
				int colCount = metadata.getColumnCount();
				while (rset != null && rset.next()) {
					Element rowElem = new Element("row");
					queryElem.addContent(rowElem);
					addRowData(rowElem, colCount, rset, metadata);
				}
				return null;
			}

			private void addRowData(Element rowElem, int colCount, ResultSet rset, ResultSetMetaData metadata) throws SQLException {
				for (int i=1; i <= colCount; i++) {
					Element child;
					if( rset.getObject(i) != null ) {
						child = new Element(metadata.getColumnLabel(i)).setText(rset.getObject(i).toString());
					} else {
						child = new Element(metadata.getColumnLabel(i)).setText("");						
					}
					rowElem.addContent(child);
				}
			}			
		});
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
