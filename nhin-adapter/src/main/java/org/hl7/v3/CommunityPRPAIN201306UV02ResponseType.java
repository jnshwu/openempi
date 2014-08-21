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
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;


/**
 * <p>Java class for Community_PRPA_IN201306UV02ResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Community_PRPA_IN201306UV02ResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:hl7-org:v3}PRPA_IN201306UV02"/>
 *         &lt;element name="nhinTargetCommunity" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}NhinTargetCommunityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Community_PRPA_IN201306UV02ResponseType", propOrder = {
    "prpain201306UV02",
    "nhinTargetCommunity"
})
public class CommunityPRPAIN201306UV02ResponseType {

    @XmlElement(name = "PRPA_IN201306UV02", required = true)
    protected PRPAIN201306UV02 prpain201306UV02;
    @XmlElement(required = true)
    protected NhinTargetCommunityType nhinTargetCommunity;

    /**
     * Gets the value of the prpain201306UV02 property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAIN201306UV02 }
     *     
     */
    public PRPAIN201306UV02 getPRPAIN201306UV02() {
        return prpain201306UV02;
    }

    /**
     * Sets the value of the prpain201306UV02 property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAIN201306UV02 }
     *     
     */
    public void setPRPAIN201306UV02(PRPAIN201306UV02 value) {
        this.prpain201306UV02 = value;
    }

    /**
     * Gets the value of the nhinTargetCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link NhinTargetCommunityType }
     *     
     */
    public NhinTargetCommunityType getNhinTargetCommunity() {
        return nhinTargetCommunity;
    }

    /**
     * Sets the value of the nhinTargetCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link NhinTargetCommunityType }
     *     
     */
    public void setNhinTargetCommunity(NhinTargetCommunityType value) {
        this.nhinTargetCommunity = value;
    }

}
