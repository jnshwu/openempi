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
 * <p>Java class for CreateAckMsgRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateAckMsgRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="localDeviceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origMsgId" type="{urn:hl7-org:v3}II"/>
 *         &lt;element name="msgText" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "CreateAckMsgRequestType", propOrder = {
    "localDeviceId",
    "origMsgId",
    "msgText",
    "senderOID",
    "receiverOID"
})
public class CreateAckMsgRequestType {

    @XmlElement(required = true)
    protected String localDeviceId;
    @XmlElement(required = true)
    protected II origMsgId;
    @XmlElement(required = true)
    protected String msgText;
    @XmlElement(required = true)
    protected String senderOID;
    @XmlElement(required = true)
    protected String receiverOID;

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
     * Gets the value of the origMsgId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getOrigMsgId() {
        return origMsgId;
    }

    /**
     * Sets the value of the origMsgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setOrigMsgId(II value) {
        this.origMsgId = value;
    }

    /**
     * Gets the value of the msgText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgText() {
        return msgText;
    }

    /**
     * Sets the value of the msgText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgText(String value) {
        this.msgText = value;
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
