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
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;


/**
 * <p>Java class for PIXConsumer_PRPA_IN201310UVRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PIXConsumer_PRPA_IN201310UVRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:hl7-org:v3}PRPA_IN201310UV02"/>
 *         &lt;element name="assertion" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}AssertionType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PIXConsumer_PRPA_IN201310UVRequestType", propOrder = {
    "prpain201310UV02",
    "assertion"
})
public class PIXConsumerPRPAIN201310UVRequestType {

    @XmlElement(name = "PRPA_IN201310UV02", required = true)
    protected PRPAIN201310UV02 prpain201310UV02;
    @XmlElement(required = true)
    protected AssertionType assertion;

    /**
     * Gets the value of the prpain201310UV02 property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAIN201310UV02 }
     *     
     */
    public PRPAIN201310UV02 getPRPAIN201310UV02() {
        return prpain201310UV02;
    }

    /**
     * Sets the value of the prpain201310UV02 property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAIN201310UV02 }
     *     
     */
    public void setPRPAIN201310UV02(PRPAIN201310UV02 value) {
        this.prpain201310UV02 = value;
    }

    /**
     * Gets the value of the assertion property.
     * 
     * @return
     *     possible object is
     *     {@link AssertionType }
     *     
     */
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * Sets the value of the assertion property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssertionType }
     *     
     */
    public void setAssertion(AssertionType value) {
        this.assertion = value;
    }

}
