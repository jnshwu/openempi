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

import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Person entity.
 * 
 * @author <a href="mailto:yimin.xie@sysnetint.com">Yimin Xie</a>
 */
@Entity
@Table(name = "person")
@GenericGenerator(name = "person_gen", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
        @Parameter(name = "sequence_name", value = "person_seq"),
        @Parameter(name = "optimizer", value = "hilo")}
    )
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Person extends BaseObject implements java.io.Serializable
{
	private static final long serialVersionUID = -6061320465621019356L;

	private Integer personId;
	private String groupNumber;
	// Name related attributes
	private String givenName;
	private String middleName;
	private String familyName;
	private String familyName2;
	private String motherName;
	private String fatherName;
	private String prefix;
	private String suffix;
	private NameType nameType;
	// Birth related attributes
	private Date dateOfBirth;
	private String birthPlace;
	private String multipleBirthInd;
	private Integer birthOrder;
	private String mothersMaidenName;
	private String ssn;
	// Other attributes
	private Gender gender;
	private EthnicGroup ethnicGroup;
	private Race race;
	private Nationality nationality;
	private Language language;
	private Religion religion;
	private String degree;
	private String maritalStatusCode;
	// Contact info attributes
	private String email;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private String countryCode;
	private String village;
	private String villageId;
	private String sector;
	private String sectorId;
	private String cell;
	private String cellId;
	private String district;
	private String districtId;
	private String province;	
	private AddressType addressType;
	// Phone attributes
	private String phoneCountryCode;
	private String phoneAreaCode;
	private String phoneNumber;
	private String phoneExt;
	private PhoneType phoneType;
	
	// Record management attributes
	private Date dateCreated;
	private User userCreatedBy;
	private Date dateChanged;
	private User userChangedBy;
	private Date dateVoided;
	private User userVoidedBy;
	private String deathInd;
	private Date deathTime;
	// Transformed field attributes
	private String custom1;
	private String custom2;
	private String custom3;
	private String custom4;
	private String custom5;
	private String custom6;
	private String custom7;
	private String custom8;
	private String custom9;
	private String custom10;
	private String custom11;
	private String custom12;
	private String custom13;
	private String custom14;
	private String custom15;
	private String custom16;
	private String custom17;
	private String custom18;
	private String custom19;
	private String custom20;
	
	private String account;
	private IdentifierDomain accountIdentifierDomain;
	private Set<PersonIdentifier> personIdentifiers = new HashSet<PersonIdentifier>();

	/** default constructor */
	public Person() {
	}

	/** minimal constructor */
	public Person(Integer personId, User userByCreatorId, User userByVoidedById, Date dateCreated) {
		this.personId = personId;
		this.userCreatedBy = userByCreatorId;
		this.userVoidedBy = userByVoidedById;
		this.dateCreated = dateCreated;
	}

	/** full constructor */
	public Person(Integer personId,
			String groupNumber,
			String givenName,
			String middleName,
			String familyName,
			String familyName2,
			String motherName,
			String fatherName,
			String prefix,
			String suffix,
			NameType nameType,
			Date dateOfBirth,
			String birthPlace,
			String multipleBirthInd,
			Integer birthOrder,
			String mothersMaidenName,
			String ssn,
			Gender gender,
			EthnicGroup ethnicGroup,
			Race race,
			Nationality nationality,
			Language language,
			Religion religion,
			String degree,
			String maritalStatusInd,
			String email,
			String address1,
			String address2,
			String city,
			String state,
			String postalCode,
			String country,
			String countryCode,
			String province,
			String district,
			String districtId,
			String cell,
			String cellId,
			String sector,
			String sectorId,
			String village,
			String villageId,
			AddressType addressType,
			String phoneCountryCode,
			String phoneAreaCode,
			String phoneNumber,
			String phoneExt,
			PhoneType phoneType,
			Date dateCreated,
			User userByCreatorId,
			Date dateChanged,
			User userByChangedById,
			Date dateVoided,
			User userByVoidedById,
			String deathInd,
			Date deathTime,
			String account,
			IdentifierDomain accountIdentifierDomain,
			Set<PersonIdentifier> personIdentifiers) {
		this.personId = personId;
		this.groupNumber = groupNumber;
		this.givenName = givenName;
		this.middleName = middleName;
		this.familyName = familyName;
		this.familyName2 = familyName2;
		this.motherName = motherName;
		this.fatherName = fatherName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.nameType = nameType;
		this.dateOfBirth = dateOfBirth;
		this.birthPlace = birthPlace;
		this.multipleBirthInd = multipleBirthInd;
		this.birthOrder = birthOrder;
		this.mothersMaidenName = mothersMaidenName;
		this.ssn = ssn;
		this.gender = gender;
		this.ethnicGroup = ethnicGroup;
		this.race = race;
		this.nationality = nationality;
		this.language = language;
		this.religion = religion;
		this.degree = degree;
		this.maritalStatusCode = maritalStatusInd;
		this.email = email;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
		this.countryCode = countryCode;
		this.province = province;
		this.district = district;
		this.districtId = districtId;
		this.cell = cell;
		this.cellId = cellId;
		this.sector = sector;
		this.sectorId = sectorId;
		this.village = village;
		this.villageId = villageId;
		this.addressType = addressType;
		this.phoneCountryCode = phoneCountryCode;
		this.phoneAreaCode = phoneAreaCode;
		this.phoneNumber = phoneNumber;
		this.phoneExt = phoneExt;
		this.phoneType = phoneType;
		this.deathInd = deathInd;
		this.deathTime = deathTime;
		this.dateCreated = dateCreated;
		this.userCreatedBy = userByCreatorId;
		this.dateChanged = dateChanged;
		this.userChangedBy = userByChangedById;
		this.dateVoided = dateVoided;
		this.userVoidedBy = userByVoidedById;
		this.account = account;
		this.accountIdentifierDomain = accountIdentifierDomain;
		this.personIdentifiers = personIdentifiers;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator="person_gen")
	@Column(name = "person_id", unique = true, nullable = false)
	@XmlElement
	public Integer getPersonId() {
		return this.personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	@Column(name = "group_number")
	@XmlElement
	public String getGroupNumber() {
		return this.groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nationality_cd")
	@XmlElement
	public Nationality getNationality() {
		return this.nationality;
	}

	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gender_cd")
	@XmlElement
	public Gender getGender() {
		return this.gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ethnic_group_cd")
	@XmlElement
	public EthnicGroup getEthnicGroup() {
		return this.ethnicGroup;
	}

	public void setEthnicGroup(EthnicGroup ethnicGroup) {
		this.ethnicGroup = ethnicGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_type_cd")
	@XmlElement
	public AddressType getAddressType() {
		return this.addressType;
	}

	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id", nullable = false)
	@XmlElement
	public User getUserCreatedBy() {
		return this.userCreatedBy;
	}

	public void setUserCreatedBy(User userByCreatorId) {
		this.userCreatedBy = userByCreatorId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "race_cd")
	@XmlElement
	public Race getRace() {
		return this.race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voided_by_id")
	@XmlElement
	public User getUserVoidedBy() {
		return this.userVoidedBy;
	}

	public void setUserVoidedBy(User userByVoidedById) {
		this.userVoidedBy = userByVoidedById;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_cd")
	@XmlElement
	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "religion_cd")
	@XmlElement
	public Religion getReligion() {
		return this.religion;
	}

	public void setReligion(Religion religion) {
		this.religion = religion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "changed_by_id")
	@XmlElement
	public User getUserChangedBy() {
		return this.userChangedBy;
	}

	public void setUserChangedBy(User userByChangedById) {
		this.userChangedBy = userByChangedById;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "name_type_cd")
	@XmlElement
	public NameType getNameType() {
		return this.nameType;
	}

	public void setNameType(NameType nameType) {
		this.nameType = nameType;
	}

	@Column(name = "prefix", length = 50)
	@XmlElement
	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Column(name = "suffix", length = 50)
	@XmlElement
	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "given_name", length = 64)
	@XmlElement
	public String getGivenName() {
		return this.givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	@Column(name = "middle_name", length = 64)
	@XmlElement
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "family_name", length = 64)
	@XmlElement
	public String getFamilyName() {
		return this.familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	@Column(name = "family_name2", length = 64)
	@XmlElement
	public String getFamilyName2() {
		return this.familyName2;
	}

	public void setFamilyName2(String familyName2) {
		this.familyName2 = familyName2;
	}

	@Column(name = "mother_name", length = 64)
	@XmlElement
	public String getMotherName() {
		return this.motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	@Column(name = "father_name", length = 64)
	@XmlElement
	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	
	@Column(name = "degree", length = 50)
	@XmlElement
	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	@Column(name = "mothers_maiden_name", length = 50)
	@XmlElement
	public String getMothersMaidenName() {
		return this.mothersMaidenName;
	}

	public void setMothersMaidenName(String mothersMaidenName) {
		this.mothersMaidenName = mothersMaidenName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth", length = 4)
	@XmlElement
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Column(name = "address1", length = 64)
	@XmlElement
	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Column(name = "address2", length = 64)
	@XmlElement
	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Column(name = "city", length = 64)
	@XmlElement
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "state", length = 64)
	@XmlElement
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "postal_code", length = 30)
	@XmlElement
	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name = "country", length = 64)
	@XmlElement
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "country_code", length = 16)
	@XmlElement
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Column(name = "village", length = 255)
	@XmlElement
	public String getVillage() {
		return this.village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	@Column(name = "village_id", length = 64)
	@XmlElement
	public String getVillageId() {
		return this.villageId;
	}

	public void setVillageId(String villageId) {
		this.villageId = villageId;
	}
	
	@Column(name = "sector", length = 255)
	@XmlElement
	public String getSector() {
		return this.sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	@Column(name = "sector_id", length = 64)
	@XmlElement
	public String getSectorId() {
		return this.sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}
	
	@Column(name = "cell", length = 255)
	@XmlElement
	public String getCell() {
		return this.cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	@Column(name = "cell_id", length = 64)
	@XmlElement
	public String getCellId() {
		return this.cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	
	@Column(name = "district", length = 255)
	@XmlElement
	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "district_id", length = 64)
	@XmlElement
	public String getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	@Column(name = "province", length = 255)
	@XmlElement
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@Column(name = "marital_status_code", length = 1)
	@XmlElement
	public String getMaritalStatusCode() {
		return this.maritalStatusCode;
	}

	public void setMaritalStatusCode(String maritalStatusInd) {
		this.maritalStatusCode = maritalStatusInd;
	}

	@Column(name = "phone_country_code", length = 32)
	@XmlElement
	public String getPhoneCountryCode() {
		return this.phoneCountryCode;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	@Column(name = "phone_area_code", length = 32)
	@XmlElement
	public String getPhoneAreaCode() {
		return this.phoneAreaCode;
	}

	public void setPhoneAreaCode(String phoneAreaCode) {
		this.phoneAreaCode = phoneAreaCode;
	}

	@Column(name = "phone_number", length = 64)
	@XmlElement
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "phone_ext", length = 30)
	@XmlElement
	public String getPhoneExt() {
		return this.phoneExt;
	}

	public void setPhoneExt(String phoneExt) {
		this.phoneExt = phoneExt;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "phone_type_cd")
	@XmlElement
	public PhoneType getPhoneType() {
		return this.phoneType;
	}

	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}
	
	@Column(name = "email")
	@XmlElement
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "multiple_birth_ind", length = 1)
	@XmlElement
	public String getMultipleBirthInd() {
		return this.multipleBirthInd;
	}

	public void setMultipleBirthInd(String multipleBirthInd) {
		this.multipleBirthInd = multipleBirthInd;
	}

	@Column(name = "birth_order")
	@XmlElement
	public Integer getBirthOrder() {
		return this.birthOrder;
	}

	public void setBirthOrder(Integer birthOrder) {
		this.birthOrder = birthOrder;
	}

	@Column(name = "birth_place")
	@XmlElement
	public String getBirthPlace() {
		return this.birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	
	@Column(name = "ssn")
	@XmlElement
	public String getSsn() {
		return this.ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	@Column(name = "death_ind", length = 1)
	@XmlElement
	public String getDeathInd() {
		return this.deathInd;
	}

	public void setDeathInd(String dateInd) {
		this.deathInd = dateInd;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "death_time", length = 8)
	@XmlElement
	public Date getDeathTime() {
		return this.deathTime;
	}

	public void setDeathTime(Date deathTime) {
		this.deathTime = deathTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 8)
	@XmlElement
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_changed", length = 8)
	@XmlElement
	public Date getDateChanged() {
		return this.dateChanged;
	}

	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_voided", length = 8)
	@XmlElement
	public Date getDateVoided() {
		return this.dateVoided;
	}

	public void setDateVoided(Date dateVoided) {
		this.dateVoided = dateVoided;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "person")
	@XmlElement
	public Set<PersonIdentifier> getPersonIdentifiers() {
		return this.personIdentifiers;
	}

	public void setPersonIdentifiers(Set<PersonIdentifier> personIdentifiers) {
		this.personIdentifiers = personIdentifiers;
	}	

	public void addPersonIdentifier(PersonIdentifier personIdentifier) {
		personIdentifier.setPerson(this);
		personIdentifiers.add(personIdentifier);
	}
	
	@Column(name = "custom1", length = 255)
	@XmlElement
	public String getCustom1() {
		return custom1;
	}

	public void setCustom1(String custom1) {
		this.custom1 = custom1;
	}

	@Column(name = "custom2", length = 255)
	@XmlElement
	public String getCustom2() {
		return custom2;
	}

	public void setCustom2(String custom2) {
		this.custom2 = custom2;
	}

	@Column(name = "custom3", length = 255)
	@XmlElement
	public String getCustom3() {
		return custom3;
	}

	public void setCustom3(String custom3) {
		this.custom3 = custom3;
	}

	@Column(name = "custom4", length = 255)
	@XmlElement
	public String getCustom4() {
		return custom4;
	}

	public void setCustom4(String custom4) {
		this.custom4 = custom4;
	}

	@Column(name = "custom5", length = 255)
	@XmlElement
	public String getCustom5() {
		return custom5;
	}

	public void setCustom5(String custom5) {
		this.custom5 = custom5;
	}

	@Column(name = "custom6", length = 255)
	@XmlElement
	public String getCustom6() {
		return custom6;
	}

	public void setCustom6(String custom6) {
		this.custom6 = custom6;
	}

	@Column(name = "custom7", length = 255)
	@XmlElement
	public String getCustom7() {
		return custom7;
	}

	public void setCustom7(String custom7) {
		this.custom7 = custom7;
	}

	@Column(name = "custom8", length = 255)
	@XmlElement
	public String getCustom8() {
		return custom8;
	}

	public void setCustom8(String custom8) {
		this.custom8 = custom8;
	}

	@Column(name = "custom9", length = 255)
	@XmlElement
	public String getCustom9() {
		return custom9;
	}

	public void setCustom9(String custom9) {
		this.custom9 = custom9;
	}

	@Column(name = "custom10", length = 255)
	@XmlElement
	public String getCustom10() {
		return custom10;
	}

	public void setCustom10(String custom10) {
		this.custom10 = custom10;
	}

	@Column(name = "custom11", length = 255)
	@XmlElement
	public String getCustom11() {
		return custom11;
	}

	public void setCustom11(String custom11) {
		this.custom11 = custom11;
	}

	@Column(name = "custom12", length = 255)
	@XmlElement
	public String getCustom12() {
		return custom12;
	}

	public void setCustom12(String custom12) {
		this.custom12 = custom12;
	}

	@Column(name = "custom13", length = 255)
	@XmlElement
	public String getCustom13() {
		return custom13;
	}

	public void setCustom13(String custom13) {
		this.custom13 = custom13;
	}

	@Column(name = "custom14", length = 255)
	@XmlElement
	public String getCustom14() {
		return custom14;
	}

	public void setCustom14(String custom14) {
		this.custom14 = custom14;
	}

	@Column(name = "custom15", length = 255)
	@XmlElement
	public String getCustom15() {
		return custom15;
	}

	public void setCustom15(String custom15) {
		this.custom15 = custom15;
	}

	@Column(name = "custom16", length = 255)
	@XmlElement
	public String getCustom16() {
		return custom16;
	}

	public void setCustom16(String custom16) {
		this.custom16 = custom16;
	}

	@Column(name = "custom17", length = 255)
	@XmlElement
	public String getCustom17() {
		return custom17;
	}

	public void setCustom17(String custom17) {
		this.custom17 = custom17;
	}

	@Column(name = "custom18", length = 255)
	@XmlElement
	public String getCustom18() {
		return custom18;
	}

	public void setCustom18(String custom18) {
		this.custom18 = custom18;
	}

	@Column(name = "custom19", length = 255)
	@XmlElement
	public String getCustom19() {
		return custom19;
	}

	public void setCustom19(String custom19) {
		this.custom19 = custom19;
	}

	@Column(name = "custom20", length = 255)
	@XmlElement
	public String getCustom20() {
		return custom20;
	}

	public void setCustom20(String custom20) {
		this.custom20 = custom20;
	}

	@Column(name = "account", length = 255)
	@XmlElement
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_identifier_domain_id", nullable = true)
	@XmlElement
	public IdentifierDomain getAccountIdentifierDomain() {
		return this.accountIdentifierDomain;
	}

	public void setAccountIdentifierDomain(IdentifierDomain accountIdentifierDomain) {
		this.accountIdentifierDomain = accountIdentifierDomain;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Person))
			return false;
		Person castOther = (Person) other;
		return new EqualsBuilder().append(personId, castOther.personId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(personId).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("personId", personId).append("givenName", givenName).append(
				"familyName", familyName).append("personIdentifiers", personIdentifiers).toString();
	}

	public String toStringLong() {
		return new ToStringBuilder(this).append("personId", personId).append(
				"nationality", nationality).append("gender", gender).append(
				"ethnicGroup", ethnicGroup).append("addressType", addressType)
				.append("userCreatedBy", userCreatedBy).append("race", race)
				.append("userVoidedBy", userVoidedBy).append("language",
						language).append("religion", religion).append(
						"userChangedBy", userChangedBy).append("nameType",
						nameType).append("prefix", prefix).append("suffix",
						suffix).append("givenName", givenName).append(
						"middleName", middleName).append("familyName",
						familyName).append("familyName2", familyName2)
						.append("motherName", motherName)
						.append("fatherName", fatherName)
						.append("degree", degree).append("mothersMaidenName",
						mothersMaidenName).append("dateOfBirth", dateOfBirth)
				.append("address1", address1).append("address2", address2)
				.append("city", city).append("state", state).append(
						"postalCode", postalCode).append("country", country)
				.append("countryCode", countryCode)
				.append("province", province)
				.append("district", district).append("districtId", districtId)
				.append("cell", cell).append("cellId", cellId)
				.append("sector", sector).append("sectorId", sectorId)
				.append("village", village).append("villageId", villageId)
				.append("maritalStatusCode",maritalStatusCode).append("phoneCountryCode",
						phoneCountryCode)
				.append("phoneAreaCode", phoneAreaCode).append("phoneNumber",
						phoneNumber).append("phoneExt", phoneExt).append("phoneType", phoneType)
						.append("email", email).append("multipleBirthInd",multipleBirthInd)
				.append("birthOrder", birthOrder)
				.append("birthPlace", birthPlace).append("deathInd", deathInd)
				.append("custom1", custom1).append("custom2", custom2).append(
						"custom3", custom3).append("custom4", custom4).append(
						"custom5", custom5).append("custom6", custom6)
						.append("custom7", custom7).append("custom8", custom8)
						.append("custom9", custom9).append("custom10", custom10)
						.append("custom11", custom11).append("custom12", custom12)
						.append("custom13", custom13).append("custom14", custom14)
						.append("custom15", custom15).append("custom16", custom16)
						.append("custom17", custom17).append("custom18", custom18)
						.append("custom19", custom19).append("custom20", custom20)
						.append("ssn", ssn).append(
						"deathTime", deathTime).append("dateCreated",
						dateCreated).append("dateChanged", dateChanged).append(
						"dateVoided", dateVoided)
						.append("account", account).append("accountIdentifierDomain", accountIdentifierDomain)
						.append("personIdentifiers",
						personIdentifiers).toString();
	}

}