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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NhinTargetSystemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NhinTargetSystemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="epr" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}EPRType"/>
 *         &lt;element name="homeCommunity" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}HomeCommunityType"/>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NhinTargetSystemType", propOrder = {
    "epr",
    "homeCommunity",
    "url"
})
public class NhinTargetSystemType {

    protected EPRType epr;
    protected HomeCommunityType homeCommunity;
    protected String url;

    /**
     * Gets the value of the epr property.
     * 
     * @return
     *     possible object is
     *     {@link EPRType }
     *     
     */
    public EPRType getEpr() {
        return epr;
    }

    /**
     * Sets the value of the epr property.
     * 
     * @param value
     *     allowed object is
     *     {@link EPRType }
     *     
     */
    public void setEpr(EPRType value) {
        this.epr = value;
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
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

}
