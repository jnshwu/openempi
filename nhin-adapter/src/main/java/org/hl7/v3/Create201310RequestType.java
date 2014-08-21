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
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Create201310RequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Create201310RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pseudoPatientId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pseudoAssigningAuthorityId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="localDeviceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="senderOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="receiverOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PRPA201307QueryByParameter" type="{urn:hl7-org:v3}PRPA_MT201307UV02.QueryByParameter"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Create201310RequestType", propOrder = {
    "pseudoPatientId",
    "pseudoAssigningAuthorityId",
    "localDeviceId",
    "senderOID",
    "receiverOID",
    "prpa201307QueryByParameter"
})
public class Create201310RequestType {

    @XmlElement(required = true)
    protected String pseudoPatientId;
    @XmlElement(required = true)
    protected String pseudoAssigningAuthorityId;
    @XmlElement(required = true)
    protected String localDeviceId;
    @XmlElement(required = true)
    protected String senderOID;
    @XmlElement(required = true)
    protected String receiverOID;
    @XmlElement(name = "PRPA201307QueryByParameter", required = true)
    protected PRPAMT201307UV02QueryByParameter prpa201307QueryByParameter;

    /**
     * Gets the value of the pseudoPatientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPseudoPatientId() {
        return pseudoPatientId;
    }

    /**
     * Sets the value of the pseudoPatientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPseudoPatientId(String value) {
        this.pseudoPatientId = value;
    }

    /**
     * Gets the value of the pseudoAssigningAuthorityId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPseudoAssigningAuthorityId() {
        return pseudoAssigningAuthorityId;
    }

    /**
     * Sets the value of the pseudoAssigningAuthorityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPseudoAssigningAuthorityId(String value) {
        this.pseudoAssigningAuthorityId = value;
    }

    /**
     * Gets the value of the localDeviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalDeviceId() {
        return localDeviceId;
    }

    /**
     * Sets the value of the localDeviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalDeviceId(String value) {
        this.localDeviceId = value;
    }

    /**
     * Gets the value of the senderOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderOID() {
        return senderOID;
    }

    /**
     * Sets the value of the senderOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderOID(String value) {
        this.senderOID = value;
    }

    /**
     * Gets the value of the receiverOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverOID() {
        return receiverOID;
    }

    /**
     * Sets the value of the receiverOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverOID(String value) {
        this.receiverOID = value;
    }

    /**
     * Gets the value of the prpa201307QueryByParameter property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAMT201307UV02QueryByParameter }
     *     
     */
    public PRPAMT201307UV02QueryByParameter getPRPA201307QueryByParameter() {
        return prpa201307QueryByParameter;
    }

    /**
     * Sets the value of the prpa201307QueryByParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAMT201307UV02QueryByParameter }
     *     
     */
    public void setPRPA201307QueryByParameter(PRPAMT201307UV02QueryByParameter value) {
        this.prpa201307QueryByParameter = value;
    }

}
