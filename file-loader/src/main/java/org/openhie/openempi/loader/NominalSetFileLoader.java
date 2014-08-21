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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.FormEntryDisplayType;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.ParameterType;
import org.openhie.openempi.model.Person;

public class NominalSetFileLoader extends AbstractFileLoader
{
	private static final String UNKNOWN_NAMESPACE = "Unknown";
	private final static int MAX_FIELD_COUNT = 18;
	public final static String LOADER_ALIAS = "nominalSetDataLoader";
	public final static String SKIP_HEADER_LINE = "skipHeaderLine";
	public final static String IS_IMPORT = "isImport";
	public final static String SKIP_HEADER_LINE_DISPLAY = "Skip Header Line";
	public final static String IS_IMPORT_DISPLAY = "Is Import";
	
	protected boolean processLine(String line, int lineIndex) {
		// Skip the first line since its a header.
//		if (lineIndex == 0) {
//			return false;
//		}
		log.debug("Needs to parse the line " + line);
		try {
			PersonData p = getPerson(line);
			if( p == null ) {
				return false;
			}
			
			log.debug("Person is:\n" + p);			
			Person person = createPerson(p);
			if (person != null) {
				loadPerson(person);	
				return true;
			}
			return false;
		} catch (ParseException e) {
			log.warn("Failed to parse file line: " + line + " due to " + e);
			return false;
		}
	}

	/**
	 * 0        1        2         3            4         5       6       7       8        9        10       11         12           13				14		   15   16, 16 (^-separated)
	rec_id,given_name,surname,street_number,address_1,address_2,suburb,postcode,state,date_of_birth,age,phone_number,soc_sec_id,blocking_number,gender(M/F/O),race,account,id-seq
	 */
	private PersonData getPerson(String line) throws ParseException {
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
			String fieldValue = line.substring(begin, end);
			if (fieldValue != null && fieldValue.length() > 0) {
				fieldValue = fieldValue.trim();
			}
			fields[fieldIndex++] = fieldValue; 
			end++;
			begin=end;
		}
		fields[fieldIndex] = line.substring(begin, end+1);
		PersonData person = new PersonData();
		person.setId(fields[0]);
		person.setLastName(fields[2]);
		person.setFirstName(fields[1]);
		person.setAddressOne(fields[3] + " " + fields[4]);
		person.setAddressTwo(fields[5]);
		person.setCity(fields[6]);
		person.setState(fields[8]);
		person.setZip(fields[7]);
		if (fields[11] != null && fields[11].length() > 0) {
			if (fields[11].length() == 11) {
				person.setAreaCode(fields[11].substring(0,3));
				person.setPhoneNum(fields[11].substring(4));
			} else if (fields[11].length() == 10) {
				person.setAreaCode(fields[11].substring(0,3));
				person.setPhoneNum(fields[11].substring(3));
			}
		}
		person.setSsn(fields[12]);
		String dob = fields[9];
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date dobDate = format.parse(dob);
		person.setDob(dobDate);
		if (isPopulatedField(fields[14])) {
			person.setGender(fields[14].toUpperCase());
		}
		if (isPopulatedField(fields[15])) {
			person.setRace(fields[15]);
		}
		if (isPopulatedField(fields[16])) {
			extractAccountIdentifier(person, fields[16]);
		}
		if (isPopulatedField(fields[17]) || isPopulatedField(fields[0])) {
			extractSetOfIdentifiers(person, fields[17]);
		}
		return person;
	}

	public ParameterType[] getParameterTypes() {
		List<ParameterType> types = new ArrayList<ParameterType>();
		Boolean[] trueOrFalse = { Boolean.TRUE, Boolean.FALSE };
		types.add(new ParameterType(SKIP_HEADER_LINE, SKIP_HEADER_LINE_DISPLAY, FormEntryDisplayType.CHECK_BOX, trueOrFalse));
		types.add(new ParameterType(IS_IMPORT, IS_IMPORT_DISPLAY, FormEntryDisplayType.CHECK_BOX, trueOrFalse));
		return types.toArray(new ParameterType[]{});
	}
	
	/**
	 * The assumption is that the person account identifier has the following format:
	 * account&namespaceIdentifier&universalIdentifier&universalIdentifierTypeCode
	 * 
	 * Only the account identifier needs to be present and the identifier domain attributes are optional
	 * 
	 * @param idSeq
	 * @return
	 */
	private void extractAccountIdentifier(PersonData person, String accountId) {
		StringTokenizer idTokenizer = new StringTokenizer(accountId, "^");
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
		person.setAccount(id);
	}

	/**
	 * The assumption here is that the person identifier sequence has the following format:
	 * identifier0&namespaceIdentifier0&universalIdentifier0&universalIdentifierTypeCode0^.... ('^' separated sequence of ids.)
	 * 
	 * @param idSeq
	 * @return
	 */
	private void extractSetOfIdentifiers(PersonData person, String idSeq) {
		if (idSeq == null && person.getId() != null) {
			PersonIdentifier id = new PersonIdentifier();
			id.setIdentifier(person.getId().toString());
			id.setIdentifierDomainName(UNKNOWN_NAMESPACE);
			id.setNamespaceIdentifier(UNKNOWN_NAMESPACE);
			id.setUniversalIdentifier(UNKNOWN_NAMESPACE);
			id.setUniversalIdentifierTypeCode(UNKNOWN_NAMESPACE);
			person.addIdentifier(id);
			return;
		}
		
		StringTokenizer idsTokenizer = new StringTokenizer(idSeq, "^");
		while (idsTokenizer.hasMoreTokens()) {
			String idCompound = idsTokenizer.nextToken();
			StringTokenizer idTokenizer = new StringTokenizer(idCompound, "&");
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
			person.addIdentifier(id);
		}
	}

	private boolean isPopulatedField(String field) {
		if (field != null && field.length() > 0) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private Person createPerson(String domain, String pid, String address1, String address2, String city,
			String state, String zip, String lname, String fname, String suffix, String gender, String ssn, Date dateOfBirth,
			String areaCode, String phoneNumber) {
		Person person = new Person();
		createIdentifier(domain, pid, person);
		person.setAddress1(address1);
		person.setAddress2(address2);
		person.setCity(city);
		person.setState(state);
		person.setPostalCode(zip);
		person.setFamilyName(lname);
		person.setGivenName(fname);
		person.setDateOfBirth(dateOfBirth);
		person.setPhoneAreaCode(areaCode);
		person.setPhoneNumber(phoneNumber);
		person.setSsn(ssn);
		return person;
	}

	private Person createPerson(PersonData personData) {
		Person person = new Person();
		setPersonIdentifiers(personData, person);
		person.setAddress1(personData.getAddressOne());
		person.setAddress2(personData.getAddressTwo());
		person.setCity(personData.getCity());
		person.setState(personData.getState());
		person.setPostalCode(personData.getZip());
		person.setFamilyName(personData.getLastName());
		person.setGivenName(personData.getFirstName());
		person.setDateOfBirth(personData.getDob());
		person.setPhoneAreaCode(personData.getAreaCode());
		person.setPhoneNumber(personData.getPhoneNum());
		person.setSsn(personData.getSsn());
		if (personData.getGender() != null) {
			person.setGender(findGenderByCode(personData.getGender()));
		}
		if (personData.getRace() != null) {
			person.setRace(findRaceByName(personData.getRace()));
		}
		return person;
	}

	private void setPersonIdentifiers(PersonData personData, Person person) {
		for (PersonIdentifier pids : personData.getIdentifiers()) {
			org.openhie.openempi.model.PersonIdentifier id = new org.openhie.openempi.model.PersonIdentifier();
			id.setIdentifier(pids.getIdentifier());
			id.setPerson(person);
			IdentifierDomain idDomain = new IdentifierDomain();
			idDomain.setIdentifierDomainName(pids.getIdentifierDomainName());
			idDomain.setNamespaceIdentifier(pids.getNamespaceIdentifier());
			idDomain.setUniversalIdentifier(pids.getUniversalIdentifier());
			idDomain.setUniversalIdentifierTypeCode(pids.getUniversalIdentifierTypeCode());
			id.setIdentifierDomain(idDomain);
			person.addPersonIdentifier(id);
		}
		if (personData.getAccount() != null && personData.getAccount().getIdentifier() != null) {
			person.setAccount(personData.getAccount().getIdentifier());
			PersonIdentifier id = personData.getAccount();
			if (id.getNamespaceIdentifier() != null ||
				id.getUniversalIdentifier() != null ||
				id.getUniversalIdentifierTypeCode() != null) {
				IdentifierDomain accountDomain = new IdentifierDomain();
				accountDomain.setNamespaceIdentifier(id.getNamespaceIdentifier());
				accountDomain.setUniversalIdentifier(id.getUniversalIdentifier());
				accountDomain.setUniversalIdentifierTypeCode(id.getUniversalIdentifierTypeCode());
				person.setAccountIdentifierDomain(accountDomain);
			}
		}
	}

	private void createIdentifier(String domain, String pid, Person person) {
		org.openhie.openempi.model.PersonIdentifier id = new org.openhie.openempi.model.PersonIdentifier();
		id.setIdentifier(pid);
		id.setPerson(person);
		IdentifierDomain idDomain = new IdentifierDomain();
		idDomain.setNamespaceIdentifier(domain);
		id.setIdentifierDomain(idDomain);
		person.addPersonIdentifier(id);
	}
	
	/**
	LNAME,FNAME,SECNAME,ADDRESS_1,ADDRESS_2,CITY,STATE_PROV,ZIP,COUNTRY,PHONE_AREA_CD,PHONE_NUM,GENDER,SSN,DOB
		 */	
	public class PersonData
	{
		private String firstName;
		private String lastName;
		private String addressOne;
		private String addressTwo;
		private String city;
		private String state;
		private String zip;
		private String country;
		private String areaCode;
		private String phoneNum;
		private Date dob;
		private String gender;
		private String race;
		private String ssn;
		private String id;
		private PersonIdentifier account;
		private java.util.Set<PersonIdentifier> identifiers;
		
		public PersonData() {
			identifiers = new java.util.HashSet<PersonIdentifier>();
		}
		public String getId() {
			return id;
		}
		public java.util.Set<PersonIdentifier> getIdentifiers() {
			return identifiers;
		}
		public void setIdentifiers(java.util.Set<PersonIdentifier> identifiers) {
			this.identifiers = identifiers;
		}
		public void addIdentifier(PersonIdentifier identifier) {
			identifiers.add(identifier);
		}
		public String getRace() {
			return race;
		}
		public void setRace(String race) {
			this.race = race;
		}
		public void setId(String id) {
			this.id = id;
		}
		public Date getDob() {
			return dob;
		}
		public void setDob(Date dob) {
			this.dob = dob;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
	
		public int hashCode() {
			final int PRIME = 31;
			int result = super.hashCode();
			result = PRIME * result + ((addressOne == null) ? 0 : addressOne.hashCode());
			result = PRIME * result + ((addressTwo == null) ? 0 : addressTwo.hashCode());
			result = PRIME * result + ((areaCode == null) ? 0 : areaCode.hashCode());
			result = PRIME * result + ((city == null) ? 0 : city.hashCode());
			result = PRIME * result + ((country == null) ? 0 : country.hashCode());
			result = PRIME * result + ((dob == null) ? 0 : dob.hashCode());
			result = PRIME * result + ((firstName == null) ? 0 : firstName.hashCode());
			result = PRIME * result + ((gender == null) ? 0 : gender.hashCode());
			result = PRIME * result + ((lastName == null) ? 0 : lastName.hashCode());
			result = PRIME * result + ((phoneNum == null) ? 0 : phoneNum.hashCode());
			result = PRIME * result + ((state == null) ? 0 : state.hashCode());
			result = PRIME * result + ((zip == null) ? 0 : zip.hashCode());
			return result;
		}
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			final PersonData other = (PersonData) obj;
			if (addressOne == null) {
				if (other.addressOne != null)
					return false;
			} else if (!addressOne.equals(other.addressOne))
				return false;
			if (addressTwo == null) {
				if (other.addressTwo != null)
					return false;
			} else if (!addressTwo.equals(other.addressTwo))
				return false;
			if (areaCode == null) {
				if (other.areaCode != null)
					return false;
			} else if (!areaCode.equals(other.areaCode))
				return false;
			if (city == null) {
				if (other.city != null)
					return false;
			} else if (!city.equals(other.city))
				return false;
			if (country == null) {
				if (other.country != null)
					return false;
			} else if (!country.equals(other.country))
				return false;
			if (dob == null) {
				if (other.dob != null)
					return false;
			} else if (!dob.equals(other.dob))
				return false;
			if (firstName == null) {
				if (other.firstName != null)
					return false;
			} else if (!firstName.equals(other.firstName))
				return false;
			if (gender == null) {
				if (other.gender != null)
					return false;
			} else if (!gender.equals(other.gender))
				return false;
			if (lastName == null) {
				if (other.lastName != null)
					return false;
			} else if (!lastName.equals(other.lastName))
				return false;
			if (phoneNum == null) {
				if (other.phoneNum != null)
					return false;
			} else if (!phoneNum.equals(other.phoneNum))
				return false;
			if (state == null) {
				if (other.state != null)
					return false;
			} else if (!state.equals(other.state))
				return false;
			if (zip == null) {
				if (other.zip != null)
					return false;
			} else if (!zip.equals(other.zip))
				return false;
			return true;
		}
		public String getAddressOne() {
			return addressOne;
		}
		public void setAddressOne(String addressOne) {
			this.addressOne = addressOne;
		}
		public String getAddressTwo() {
			return addressTwo;
		}
		public void setAddressTwo(String addressTwo) {
			this.addressTwo = addressTwo;
		}
		public String getAreaCode() {
			return areaCode;
		}
		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getPhoneNum() {
			return phoneNum;
		}
		public void setPhoneNum(String phoneNum) {
			this.phoneNum = phoneNum;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		public String getSsn() {
			return ssn;
		}
		public void setSsn(String ssn) {
			this.ssn = ssn;
		}
		public void setAccount(PersonIdentifier account) {
			this.account = account;
		}
		public PersonIdentifier getAccount() {
			return account;
		}
		public String toString() {
			return new ToStringBuilder(this).append("firstName", firstName)
					.append("lastName", lastName).append("addressOne",
							addressOne).append("addressTwo", addressTwo)
					.append("city", city).append("state", state).append("zip",
							zip).append("country", country).append("areaCode",
							areaCode).append("phoneNum", phoneNum).append(
							"dob", dob).append("gender", gender).append("ssn",
							ssn).append("account", account).toString();
		}
	}
	
	public class PersonIdentifier
	{
		private String identifier;
		private String identifierDomainName;
		private String namespaceIdentifier;
		private String universalIdentifier;
		private String universalIdentifierTypeCode;
		
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		public String getIdentifierDomainName() {
			return identifierDomainName;
		}
		public void setIdentifierDomainName(String identifierDomainName) {
			this.identifierDomainName = identifierDomainName;
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
					.append(identifierDomainName, castOther.identifierDomainName)
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
			return new ToStringBuilder(this).append("identifier", identifier)
					.append("identifierDomainName", identifierDomainName)
					.append("namespaceIdentifier", namespaceIdentifier).append(
							"universalIdentifier", universalIdentifier).append(
							"universalIdentifierTypeCode",
							universalIdentifierTypeCode).toString();
		}
		
	}
}
