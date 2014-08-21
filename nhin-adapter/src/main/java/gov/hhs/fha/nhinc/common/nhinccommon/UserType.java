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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="personName" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}PersonNameType"/>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="org" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}HomeCommunityType"/>
 *         &lt;element name="roleCoded" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}CeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserType", propOrder = {
    "personName",
    "userName",
    "org",
    "roleCoded"
})
public class UserType {

    @XmlElement(required = true)
    protected PersonNameType personName;
    @XmlElement(required = true)
    protected String userName;
    @XmlElement(required = true)
    protected HomeCommunityType org;
    protected CeType roleCoded;

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
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the org property.
     * 
     * @return
     *     possible object is
     *     {@link HomeCommunityType }
     *     
     */
    public HomeCommunityType getOrg() {
        return org;
    }

    /**
     * Sets the value of the org property.
     * 
     * @param value
     *     allowed object is
     *     {@link HomeCommunityType }
     *     
     */
    public void setOrg(HomeCommunityType value) {
        this.org = value;
    }

    /**
     * Gets the value of the roleCoded property.
     * 
     * @return
     *     possible object is
     *     {@link CeType }
     *     
     */
    public CeType getRoleCoded() {
        return roleCoded;
    }

    /**
     * Sets the value of the roleCoded property.
     * 
     * @param value
     *     allowed object is
     *     {@link CeType }
     *     
     */
    public void setRoleCoded(CeType value) {
        this.roleCoded = value;
    }

}
