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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhie.openempi.model.FormEntryDisplayType;
import org.openhie.openempi.model.ParameterType;
import org.openhie.openempi.model.Person;

public class NCVoterFileLoader extends AbstractFileLoader
{
	public final static String LOADER_ALIAS = "ncVoterDataLoader";
	public final static String SKIP_HEADER_LINE = "skipHeaderLine";
	public final static String IS_IMPORT = "isImport";
	public final static String SKIP_HEADER_LINE_DISPLAY = "Skip Header Line";
	public final static String IS_IMPORT_DISPLAY = "Is Import";
	
	
	protected boolean processLine(String line, int lineIndex) {
		log.debug("Needs to parse the line " + line);
		Person person = null;
		try {
			PersonData p = getPerson(line);
			log.debug("Person is:\n" + p);
			person = createPerson(p);
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

/*	private String getRaceNameForCharacter(String character) {
		String raceString = "N/A";
		String upperChar = character.toUpperCase();
		if (upperChar.equals("A"))
			raceString = "Asian";
		else if (upperChar.equals("B"))
			raceString = "Black";
		else if (upperChar.equals("I"))
			raceString = "Italian";
		else if (upperChar.equals("M"))
			raceString = "Minnesota Chippewa";
		else if (upperChar.equals("O"))
			raceString = "Other Race";
		else if (upperChar.equals("W"))
			raceString = "White";
		else if (upperChar.equals("U"))
			raceString = "United Keetowah Band of Cherokee";
		else
			System.out.println("Gotcha");
		return raceString;
	}

	private String getGenderStringForCharacter(String character) {
		String genderString = "Unknown";
		String upperChar = character.toUpperCase();
		if (upperChar.equals("M"))
			genderString = "Male";
		else if (upperChar.equals("F"))
			genderString = "Female";
		else if (upperChar.equals("O"))
			genderString = "Other";
		else if (!upperChar.equals("U"))
			System.out.println("Gotcha");
		return genderString;
	}*/

	/**
	 * 0        1        2         3            4      5     6        7           8          9       10      11
	rec_id,given_name,surname,middle_name,birth_state,city,state,street_name,street_type,street_dir,race,gender(M/F/O)
	 */
	private PersonData getPerson(String line) throws ParseException {
		String[] fields = line.split("\\,");
		PersonData person = new PersonData();
		Long personId = Long.parseLong(fields[0]);
		person.setId(personId);
		if (fields[1].length() > 0)
			person.setFirstName(fields[1]);
		if (fields[2].length() > 0)
			person.setLastName(fields[2]);
		if (fields[3].length() > 0)
			person.setMiddleName(fields[3]);
		if (fields[4].length() > 0)
			person.setBirthState(fields[4]);
		if (fields[5].length() > 0)
			person.setCity(fields[5]);
		if (fields[6].length() > 0)
			person.setState(fields[6]);
		if (fields[7].length() > 0)
			person.setStreetName(fields[7]);
		if (fields[8].length() > 0)
			person.setStreetType(fields[8]);
		if (fields[9].length() > 0)
			person.setStreetDir(fields[9]);
/*		if (isPopulatedField(fields[10])) {
			String raceString = getRaceNameForCharacter(fields[10]);
			if (!raceString.equals("N/A"))
				person.setRace(raceString);
		}
		String genderString = "Unknown";
		if (isPopulatedField(fields[11])) {
			genderString = getGenderStringForCharacter(fields[11]);
		}
		person.setGender(genderString);*/
		if (fields[10].length() > 0)
			person.setRace(fields[10]);
		if (fields[11].length() > 0)
			person.setGender(fields[11]);
		return person;
	}

	public ParameterType[] getParameterTypes() {
		List<ParameterType> types = new ArrayList<ParameterType>();
		Boolean[] trueOrFalse = { Boolean.TRUE, Boolean.FALSE };
		types.add(new ParameterType(SKIP_HEADER_LINE, SKIP_HEADER_LINE_DISPLAY, FormEntryDisplayType.CHECK_BOX, trueOrFalse));
		types.add(new ParameterType(IS_IMPORT, IS_IMPORT_DISPLAY, FormEntryDisplayType.CHECK_BOX, trueOrFalse));
		return types.toArray(new ParameterType[]{});
	}
/*	private boolean isPopulatedField(String field) {
		if (field != null && field.length() > 0) {
			return true;
		}
		return false;
	}*/
	
	private Person createPerson(PersonData personData) {
		Person person = new Person();
		person.setGivenName(personData.getFirstName());
		person.setFamilyName(personData.getLastName());
		person.setMiddleName(personData.getMiddleName());
		person.setBirthPlace(personData.getBirthState());
		person.setCity(personData.getCity());
		person.setState(personData.getState());
		person.setAddress1(personData.getStreetName());
		person.setAddress2(personData.getStreetType());
		person.setPhoneExt(personData.getStreetDir());	// TODO: resolve hack
		person.setPhoneAreaCode(personData.getRace());	// TODO: resolve hack
		person.setPhoneCountryCode(personData.getGender());	// TODO: resolve hack
/*		if (personData.getRace() != null) {
			person.setRace(findRaceByName(personData.getRace()));
		}
		if (personData.getGender() != null) {
			person.setGender(findGenderByName(personData.getGender()));
		}*/
		person.setPhoneNumber(personData.getId().toString());	// TODO: resolve hack
		return person;
	}

	/**
	LNAME,FNAME,SECNAME,ADDRESS_1,ADDRESS_2,CITY,STATE_PROV,ZIP,COUNTRY,PHONE_AREA_CD,PHONE_NUM,GENDER,SSN,DOB
		 */	
	public class PersonData
	{
		private Long id;
		private String firstName;
		private String lastName;
		private String middleName;
		private String birthState;
		private String city;
		private String state;
		private String streetName;
		private String streetType;
		private String streetDir;
		private String race;
		private String gender;
		
		public PersonData() {
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getMiddleName() {
			return middleName;
		}
		public void setMiddleName(String middleName) {
			this.middleName = middleName;
		}
		public String getBirthState() {
			return birthState;
		}
		public void setBirthState(String birthState) {
			this.birthState = birthState;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getStreetName() {
			return streetName;
		}
		public void setStreetName(String streetName) {
			this.streetName = streetName;
		}
		public String getStreetType() {
			return streetType;
		}
		public void setStreetType(String streetType) {
			this.streetType = streetType;
		}
		public String getStreetDir() {
			return streetDir;
		}
		public void setStreetDir(String streetDir) {
			this.streetDir = streetDir;
		}
		public String getRace() {
			return race;
		}
		public void setRace(String race) {
			this.race = race;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}

		public int hashCode() {
			final int PRIME = 31;
			int result = super.hashCode();
			result = PRIME * result + ((firstName == null) ? 0 : firstName.hashCode());
			result = PRIME * result + ((lastName == null) ? 0 : lastName.hashCode());
			result = PRIME * result + ((middleName == null) ? 0 : middleName.hashCode());
			result = PRIME * result + ((birthState == null) ? 0 : birthState.hashCode());
			result = PRIME * result + ((city == null) ? 0 : city.hashCode());
			result = PRIME * result + ((state == null) ? 0 : state.hashCode());
			result = PRIME * result + ((streetName == null) ? 0 : streetName.hashCode());
			result = PRIME * result + ((streetType == null) ? 0 : streetType.hashCode());
			result = PRIME * result + ((streetDir == null) ? 0 : streetDir.hashCode());
			result = PRIME * result + ((race == null) ? 0 : race.hashCode());
			result = PRIME * result + ((gender == null) ? 0 : gender.hashCode());
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
			if (firstName == null) {
				if (other.firstName != null)
					return false;
			} else if (!firstName.equals(other.firstName))
				return false;
			if (lastName == null) {
				if (other.lastName != null)
					return false;
			} else if (!lastName.equals(other.lastName))
				return false;
			if (middleName == null) {
				if (other.middleName != null)
					return false;
			} else if (!middleName.equals(other.middleName))
				return false;
			if (birthState == null) {
				if (other.birthState != null)
					return false;
			} else if (!birthState.equals(other.birthState))
				return false;
			if (city == null) {
				if (other.city != null)
					return false;
			} else if (!city.equals(other.city))
				return false;
			if (state == null) {
				if (other.state != null)
					return false;
			} else if (!state.equals(other.state))
				return false;
			if (streetName == null) {
				if (other.streetName != null)
					return false;
			} else if (!streetName.equals(other.streetName))
				return false;
			if (streetType == null) {
				if (other.streetType != null)
					return false;
			} else if (!streetType.equals(other.streetType))
				return false;
			if (streetDir == null) {
				if (other.streetDir != null)
					return false;
			} else if (!streetDir.equals(other.streetDir))
				return false;
			if (race == null) {
				if (other.race != null)
					return false;
			} else if (!race.equals(other.race))
				return false;
			if (gender == null) {
				if (other.gender != null)
					return false;
			} else if (!gender.equals(other.gender))
				return false;
			return true;
		}
		public String toString() {
			return new ToStringBuilder(this).append("lastName", lastName)
					.append("firstName", firstName).append("middleName", middleName)
					.append("birthState",birthState).append("city", city).append("state", state)
					.append("streetName",streetName).append("streetType",streetType)
					.append("streetDir",streetDir)
					.append("race",race).append("gender", gender).toString();
		}
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
			return new ToStringBuilder(this).append("identifier", identifier)
					.append("namespaceIdentifier", namespaceIdentifier).append(
							"universalIdentifier", universalIdentifier).append(
							"universalIdentifierTypeCode",
							universalIdentifierTypeCode).toString();
		}
		
	}
}
