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
package gov.hhs.fha.nhinc.common.nhinccommon;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AssertionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssertionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="address" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}AddressType"/>
 *         &lt;element name="dateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="explanationNonClaimantSignature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="haveSecondWitnessSignature" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="haveSignature" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="haveWitnessSignature" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="homeCommunity" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}HomeCommunityType"/>
 *         &lt;element name="personName" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}PersonNameType"/>
 *         &lt;element name="phoneNumber" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}PhoneType"/>
 *         &lt;element name="secondWitnessAddress" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}AddressType"/>
 *         &lt;element name="secondWitnessName" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}PersonNameType"/>
 *         &lt;element name="secondWitnessPhone" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}PhoneType"/>
 *         &lt;element name="SSN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uniquePatientId" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="witnessAddress" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}AddressType"/>
 *         &lt;element name="witnessName" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}PersonNameType"/>
 *         &lt;element name="witnessPhone" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}PhoneType"/>
 *         &lt;element name="userInfo" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}UserType"/>
 *         &lt;element name="authorized" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="purposeOfDisclosureCoded" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}CeType" minOccurs="0"/>
 *         &lt;element name="samlAuthnStatement" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}SamlAuthnStatementType" minOccurs="0"/>
 *         &lt;element name="samlAuthzDecisionStatement" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}SamlAuthzDecisionStatementType" minOccurs="0"/>
 *         &lt;element name="samlSignature" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}SamlSignatureType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssertionType", propOrder = {
    "address",
    "dateOfBirth",
    "explanationNonClaimantSignature",
    "haveSecondWitnessSignature",
    "haveSignature",
    "haveWitnessSignature",
    "homeCommunity",
    "personName",
    "phoneNumber",
    "secondWitnessAddress",
    "secondWitnessName",
    "secondWitnessPhone",
    "ssn",
    "uniquePatientId",
    "witnessAddress",
    "witnessName",
    "witnessPhone",
    "userInfo",
    "authorized",
    "purposeOfDisclosureCoded",
    "samlAuthnStatement",
    "samlAuthzDecisionStatement",
    "samlSignature"
})
public class AssertionType {

    @XmlElement(required = true)
    protected AddressType address;
    @XmlElement(required = true)
    protected String dateOfBirth;
    @XmlElement(required = true)
    protected String explanationNonClaimantSignature;
    protected boolean haveSecondWitnessSignature;
    protected boolean haveSignature;
    protected boolean haveWitnessSignature;
    @XmlElement(required = true)
    protected HomeCommunityType homeCommunity;
    @XmlElement(required = true)
    protected PersonNameType personName;
    @XmlElement(required = true)
    protected PhoneType phoneNumber;
    @XmlElement(required = true)
    protected AddressType secondWitnessAddress;
    @XmlElement(required = true)
    protected PersonNameType secondWitnessName;
    @XmlElement(required = true)
    protected PhoneType secondWitnessPhone;
    @XmlElement(name = "SSN", required = true)
    protected String ssn;
    @XmlElement(required = true)
    protected List<String> uniquePatientId;
    @XmlElement(required = true)
    protected AddressType witnessAddress;
    @XmlElement(required = true)
    protected PersonNameType witnessName;
    @XmlElement(required = true)
    protected PhoneType witnessPhone;
    @XmlElement(required = true)
    protected UserType userInfo;
    protected boolean authorized;
    protected CeType purposeOfDisclosureCoded;
    protected SamlAuthnStatementType samlAuthnStatement;
    protected SamlAuthzDecisionStatementType samlAuthzDecisionStatement;
    protected SamlSignatureType samlSignature;

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setAddress(AddressType value) {
        this.address = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateOfBirth(String value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the explanationNonClaimantSignature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExplanationNonClaimantSignature() {
        return explanationNonClaimantSignature;
    }

    /**
     * Sets the value of the explanationNonClaimantSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExplanationNonClaimantSignature(String value) {
        this.explanationNonClaimantSignature = value;
    }

    /**
     * Gets the value of the haveSecondWitnessSignature property.
     * 
     */
    public boolean isHaveSecondWitnessSignature() {
        return haveSecondWitnessSignature;
    }

    /**
     * Sets the value of the haveSecondWitnessSignature property.
     * 
     */
    public void setHaveSecondWitnessSignature(boolean value) {
        this.haveSecondWitnessSignature = value;
    }

    /**
     * Gets the value of the haveSignature property.
     * 
     */
    public boolean isHaveSignature() {
        return haveSignature;
    }

    /**
     * Sets the value of the haveSignature property.
     * 
     */
    public void setHaveSignature(boolean value) {
        this.haveSignature = value;
    }

    /**
     * Gets the value of the haveWitnessSignature property.
     * 
     */
    public boolean isHaveWitnessSignature() {
        return haveWitnessSignature;
    }

    /**
     * Sets the value of the haveWitnessSignature property.
     * 
     */
    public void setHaveWitnessSignature(boolean value) {
        this.haveWitnessSignature = value;
    }

    /**
     * Gets the value of the homeCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link HomeCommunityType }
     *     
     */
    public HomeCommunityType getHomeCommunity() {
        return homeCommunity;
    }

    /**
     * Sets the value of the homeCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link HomeCommunityType }
     *     
     */
    public void setHomeCommunity(HomeCommunityType value) {
        this.homeCommunity = value;
    }

    /**
     * Gets the value of the personName property.
     * 
     * @return
     *     possible object is
     *     {@link PersonNameType }
     *     
     */
    public PersonNameType getPersonName() {
        return personName;
    }

    /**
     * Sets the value of the personName property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonNameType }
     *     
     */
    public void setPersonName(PersonNameType value) {
        this.personName = value;
    }

    /**
     * Gets the value of the phoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link PhoneType }
     *     
     */
    public PhoneType getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value of the phoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhoneType }
     *     
     */
    public void setPhoneNumber(PhoneType value) {
        this.phoneNumber = value;
    }

    /**
     * Gets the value of the secondWitnessAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getSecondWitnessAddress() {
        return secondWitnessAddress;
    }

    /**
     * Sets the value of the secondWitnessAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setSecondWitnessAddress(AddressType value) {
        this.secondWitnessAddress = value;
    }

    /**
     * Gets the value of the secondWitnessName property.
     * 
     * @return
     *     possible object is
     *     {@link PersonNameType }
     *     
     */
    public PersonNameType getSecondWitnessName() {
        return secondWitnessName;
    }

    /**
     * Sets the value of the secondWitnessName property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonNameType }
     *     
     */
    public void setSecondWitnessName(PersonNameType value) {
        this.secondWitnessName = value;
    }

    /**
     * Gets the value of the secondWitnessPhone property.
     * 
     * @return
     *     possible object is
     *     {@link PhoneType }
     *     
     */
    public PhoneType getSecondWitnessPhone() {
        return secondWitnessPhone;
    }

    /**
     * Sets the value of the secondWitnessPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhoneType }
     *     
     */
    public void setSecondWitnessPhone(PhoneType value) {
        this.secondWitnessPhone = value;
    }

    /**
     * Gets the value of the ssn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSSN() {
        return ssn;
    }

    /**
     * Sets the value of the ssn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSSN(String value) {
        this.ssn = value;
    }

    /**
     * Gets the value of the uniquePatientId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uniquePatientId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUniquePatientId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUniquePatientId() {
        if (uniquePatientId == null) {
            uniquePatientId = new ArrayList<String>();
        }
        return this.uniquePatientId;
    }

    /**
     * Gets the value of the witnessAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getWitnessAddress() {
        return witnessAddress;
    }

    /**
     * Sets the value of the witnessAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setWitnessAddress(AddressType value) {
        this.witnessAddress = value;
    }

    /**
     * Gets the value of the witnessName property.
     * 
     * @return
     *     possible object is
     *     {@link PersonNameType }
     *     
     */
    public PersonNameType getWitnessName() {
        return witnessName;
    }

    /**
     * Sets the value of the witnessName property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonNameType }
     *     
     */
    public void setWitnessName(PersonNameType value) {
        this.witnessName = value;
    }

    /**
     * Gets the value of the witnessPhone property.
     * 
     * @return
     *     possible object is
     *     {@link PhoneType }
     *     
     */
    public PhoneType getWitnessPhone() {
        return witnessPhone;
    }

    /**
     * Sets the value of the witnessPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhoneType }
     *     
     */
    public void setWitnessPhone(PhoneType value) {
        this.witnessPhone = value;
    }

    /**
     * Gets the value of the userInfo property.
     * 
     * @return
     *     possible object is
     *     {@link UserType }
     *     
     */
    public UserType getUserInfo() {
        return userInfo;
    }

    /**
     * Sets the value of the userInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserType }
     *     
     */
    public void setUserInfo(UserType value) {
        this.userInfo = value;
    }

    /**
     * Gets the value of the authorized property.
     * 
     */
    public boolean isAuthorized() {
        return authorized;
    }

    /**
     * Sets the value of the authorized property.
     * 
     */
    public void setAuthorized(boolean value) {
        this.authorized = value;
    }

    /**
     * Gets the value of the purposeOfDisclosureCoded property.
     * 
     * @return
     *     possible object is
     *     {@link CeType }
     *     
     */
    public CeType getPurposeOfDisclosureCoded() {
        return purposeOfDisclosureCoded;
    }

    /**
     * Sets the value of the purposeOfDisclosureCoded property.
     * 
     * @param value
     *     allowed object is
     *     {@link CeType }
     *     
     */
    public void setPurposeOfDisclosureCoded(CeType value) {
        this.purposeOfDisclosureCoded = value;
    }

    /**
     * Gets the value of the samlAuthnStatement property.
     * 
     * @return
     *     possible object is
     *     {@link SamlAuthnStatementType }
     *     
     */
    public SamlAuthnStatementType getSamlAuthnStatement() {
        return samlAuthnStatement;
    }

    /**
     * Sets the value of the samlAuthnStatement property.
     * 
     * @param value
     *     allowed object is
     *     {@link SamlAuthnStatementType }
     *     
     */
    public void setSamlAuthnStatement(SamlAuthnStatementType value) {
        this.samlAuthnStatement = value;
    }

    /**
     * Gets the value of the samlAuthzDecisionStatement property.
     * 
     * @return
     *     possible object is
     *     {@link SamlAuthzDecisionStatementType }
     *     
     */
    public SamlAuthzDecisionStatementType getSamlAuthzDecisionStatement() {
        return samlAuthzDecisionStatement;
    }

    /**
     * Sets the value of the samlAuthzDecisionStatement property.
     * 
     * @param value
     *     allowed object is
     *     {@link SamlAuthzDecisionStatementType }
     *     
     */
    public void setSamlAuthzDecisionStatement(SamlAuthzDecisionStatementType value) {
        this.samlAuthzDecisionStatement = value;
    }

    /**
     * Gets the value of the samlSignature property.
     * 
     * @return
     *     possible object is
     *     {@link SamlSignatureType }
     *     
     */
    public SamlSignatureType getSamlSignature() {
        return samlSignature;
    }

    /**
     * Sets the value of the samlSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SamlSignatureType }
     *     
     */
    public void setSamlSignature(SamlSignatureType value) {
        this.samlSignature = value;
    }

}
