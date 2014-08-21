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
package org.openhie.openempi.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

public class ProcessFileDataset
{
	protected Logger log = Logger.getLogger(ProcessFileDataset.class);
	
	private final static int MAX_FIELD_COUNT = 18;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProcessFileDataset pfd = new ProcessFileDataset();
		pfd.parseFile(new File(args[0]));
	}

	public void parseFile(File file) {
		log.debug("Will attempt to process the file: " + file.getAbsolutePath());
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			log.error("Unable to read the input file. Error: " + e);
			throw new RuntimeException("Unable to read the input file.");
		}
		
		try {
			boolean done = false;
			int lineIndex=0;
			while (!done) {
				String line = reader.readLine();
				if (line == null) {
					done = true;
					continue;
				}
				processLine(line, lineIndex++);
			}
		} catch (IOException e) {
			log.error("Failed while loading the input file. Error: " + e);
			throw new RuntimeException("Failed while loading the input file.");
		}
	}

	protected void processLine(String line, int lineIndex) {
		// Skip the first line since its a header.
		if (lineIndex == 0) {
			System.out.println(line);
		}
		log.debug("Needs to parse the line " + line);
		try {
			getPerson(lineIndex, line);
		} catch (ParseException e) {
			log.warn("Failed to parse file line: " + line + " due to " + e);
			return;
		}
	}
	
	/**
	 * 0        1        2         3            4         5       6       7       8        9        10       11         12           13				14		   15   16, 16 (^-separated)
	rec_id,given_name,surname,street_number,address_1,address_2,suburb,postcode,state,date_of_birth,age,phone_number,soc_sec_id,blocking_number,gender(M/F/O),race,account,id-seq
	 */
	private void getPerson(int lineIndex, String line) throws ParseException {
		String[] fields = new String[MAX_FIELD_COUNT];
		int length = line.length();
		int begin=0;
		int end=0;
		int fieldIndex=0;
		while (end < length) {
			while (end < length-1 && line.charAt(end) != ',') {
				end++;
			}
			if (end == length -1 ) {
				break;
			}
			fields[fieldIndex++] = line.substring(begin, end);
			end++;
			begin=end;
		}
		fields[fieldIndex] = line.substring(begin, end+1);
		if (isPopulatedField(fields[16])) {
			log.debug("Extracted an account number of " + fields[16]);
			PersonIdentifier id = extractAccountIdentifier(fields[16]);
			log.debug("We have an ID of " + id);
			log.debug("Will set the account number to: " + String.format("%07d", lineIndex));
			id.setIdentifier(String.format("%07d", lineIndex));
			outputLine(fields, id);
		}
	}
	
	private void outputLine(String[] fields, PersonIdentifier id) {
		for (int i=0; i < fields.length; i++) {
			if (i == 16) {
				System.out.print(id.toString());
			} else {
				System.out.print(fields[i]);
			}
			if (i < fields.length-1) {
				System.out.print(",");
			}
		}
		System.out.println();
	}

	private PersonIdentifier extractAccountIdentifier(String accountId) {
		StringTokenizer idTokenizer = new StringTokenizer(accountId, "&");
		int count=0;
		PersonIdentifier id = new PersonIdentifier();
		while (idTokenizer.hasMoreTokens()) {
			String field = idTokenizer.nextToken();
			switch (count) {
			case 0:
				id.setIdentifier(field);
				break;
			case 1:
				id.setNamespaceIdentifier(field);
				break;
			case 2:
				id.setUniversalIdentifier(field);
				break;
			case 3:
				id.setUniversalIdentifierTypeCode(field);
				break;
			}
			count++;
		}
		return id;
	}
	
	private boolean isPopulatedField(String field) {
		if (field != null && field.length() > 0) {
			return true;
		}
		return false;
	}
	
	public class PersonIdentifier
	{
		private String identifier;
		private String namespaceIdentifier;
		private String universalIdentifier;
		private String universalIdentifierTypeCode;
		
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		public String getNamespaceIdentifier() {
			return namespaceIdentifier;
		}
		public void setNamespaceIdentifier(String namespaceIdentifier) {
			this.namespaceIdentifier = namespaceIdentifier;
		}
		public String getUniversalIdentifier() {
			return universalIdentifier;
		}
		public void setUniversalIdentifier(String universalIdentifier) {
			this.universalIdentifier = universalIdentifier;
		}
		public String getUniversalIdentifierTypeCode() {
			return universalIdentifierTypeCode;
		}
		public void setUniversalIdentifierTypeCode(String universalIdentifierTypeCode) {
			this.universalIdentifierTypeCode = universalIdentifierTypeCode;
		}
		@Override
		public boolean equals(final Object other) {
			if (!(other instanceof PersonIdentifier))
				return false;
			PersonIdentifier castOther = (PersonIdentifier) other;
			return new EqualsBuilder().append(identifier, castOther.identifier)
					.append(namespaceIdentifier, castOther.namespaceIdentifier)
					.append(universalIdentifier, castOther.universalIdentifier)
					.append(universalIdentifierTypeCode,
							castOther.universalIdentifierTypeCode).isEquals();
		}
		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(identifier).append(
					namespaceIdentifier).append(universalIdentifier).append(
					universalIdentifierTypeCode).toHashCode();
		}
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer(identifier);
			if (namespaceIdentifier != null) {
				buffer.append("&").append(namespaceIdentifier);
			}
			if (universalIdentifier != null) {
				buffer.append("&").append(universalIdentifier);
			}
			if (universalIdentifierTypeCode != null) {
				buffer.append("&").append(universalIdentifierTypeCode);
			}
			return buffer.toString();
		}
		
	}	
}
