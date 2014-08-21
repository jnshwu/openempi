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
 * <p>Java class for PIXConsumer_MCCI_IN000002UV01RequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PIXConsumer_MCCI_IN000002UV01RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:hl7-org:v3}MCCI_IN000002UV01"/>
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
@XmlType(name = "PIXConsumer_MCCI_IN000002UV01RequestType", propOrder = {
    "mcciin000002UV01",
    "assertion"
})
public class PIXConsumerMCCIIN000002UV01RequestType {

    @XmlElement(name = "MCCI_IN000002UV01", required = true)
    protected MCCIIN000002UV01 mcciin000002UV01;
    @XmlElement(required = true)
    protected AssertionType assertion;

    /**
     * Gets the value of the mcciin000002UV01 property.
     * 
     * @return
     *     possible object is
     *     {@link MCCIIN000002UV01 }
     *     
     */
    public MCCIIN000002UV01 getMCCIIN000002UV01() {
        return mcciin000002UV01;
    }

    /**
     * Sets the value of the mcciin000002UV01 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MCCIIN000002UV01 }
     *     
     */
    public void setMCCIIN000002UV01(MCCIIN000002UV01 value) {
        this.mcciin000002UV01 = value;
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
