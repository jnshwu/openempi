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
 * <p>Java class for Create201302RequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Create201302RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="PRPA201310Patient" type="{urn:hl7-org:v3}PRPA_MT201310UV02.Patient"/>
 *           &lt;element name="PRPA201301Patient" type="{urn:hl7-org:v3}PRPA_MT201301UV02.Patient"/>
 *         &lt;/choice>
 *         &lt;element name="remotePatientId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="remoteDeviceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="senderOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="receiverOID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Create201302RequestType", propOrder = {
    "prpa201310Patient",
    "prpa201301Patient",
    "remotePatientId",
    "remoteDeviceId",
    "senderOID",
    "receiverOID"
})
public class Create201302RequestType {

    @XmlElement(name = "PRPA201310Patient")
    protected PRPAMT201310UV02Patient prpa201310Patient;
    @XmlElement(name = "PRPA201301Patient")
    protected PRPAMT201301UV02Patient prpa201301Patient;
    @XmlElement(required = true)
    protected String remotePatientId;
    @XmlElement(required = true)
    protected String remoteDeviceId;
    @XmlElement(required = true)
    protected String senderOID;
    @XmlElement(required = true)
    protected String receiverOID;

    /**
     * Gets the value of the prpa201310Patient property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAMT201310UV02Patient }
     *     
     */
    public PRPAMT201310UV02Patient getPRPA201310Patient() {
        return prpa201310Patient;
    }

    /**
     * Sets the value of the prpa201310Patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAMT201310UV02Patient }
     *     
     */
    public void setPRPA201310Patient(PRPAMT201310UV02Patient value) {
        this.prpa201310Patient = value;
    }

    /**
     * Gets the value of the prpa201301Patient property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAMT201301UV02Patient }
     *     
     */
    public PRPAMT201301UV02Patient getPRPA201301Patient() {
        return prpa201301Patient;
    }

    /**
     * Sets the value of the prpa201301Patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAMT201301UV02Patient }
     *     
     */
    public void setPRPA201301Patient(PRPAMT201301UV02Patient value) {
        this.prpa201301Patient = value;
    }

    /**
     * Gets the value of the remotePatientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemotePatientId() {
        return remotePatientId;
    }

    /**
     * Sets the value of the remotePatientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemotePatientId(String value) {
        this.remotePatientId = value;
    }

    /**
     * Gets the value of the remoteDeviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteDeviceId() {
        return remoteDeviceId;
    }

    /**
     * Sets the value of the remoteDeviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteDeviceId(String value) {
        this.remoteDeviceId = value;
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

}
