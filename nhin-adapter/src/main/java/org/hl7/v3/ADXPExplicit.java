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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * 
 *             A character string that may have a type-tag signifying its
 *             role in the address. Typical parts that exist in about
 *             every address are street, house number, or post box,
 *             postal code, city, country but other roles may be defined
 *             regionally, nationally, or on an enterprise level (e.g. in
 *             military addresses). Addresses are usually broken up into
 *             lines, which are indicated by special line-breaking
 *             delimiter elements (e.g., DEL).
 *             
 * 
 * <p>Java class for ADXP_explicit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ADXP_explicit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="partType" type="{urn:hl7-org:v3}AddressPartType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ADXP_explicit", propOrder = {
    "content"
})
@XmlSeeAlso({
    AdxpExplicitStreetAddressLine.class,
    AdxpExplicitDeliveryInstallationType.class,
    AdxpExplicitDeliveryAddressLine.class,
    AdxpExplicitUnitID.class,
    AdxpExplicitHouseNumberNumeric.class,
    AdxpExplicitPrecinct.class,
    AdxpExplicitState.class,
    AdxpExplicitCareOf.class,
    AdxpExplicitPostBox.class,
    AdxpExplicitAdditionalLocator.class,
    AdxpExplicitPostalCode.class,
    AdxpExplicitDeliveryMode.class,
    AdxpExplicitDeliveryModeIdentifier.class,
    AdxpExplicitCountry.class,
    AdxpExplicitBuildingNumberSuffix.class,
    AdxpExplicitCensusTract.class,
    AdxpExplicitHouseNumber.class,
    AdxpExplicitStreetNameType1 .class,
    AdxpExplicitDirection.class,
    AdxpExplicitUnitType.class,
    AdxpExplicitCounty.class,
    AdxpExplicitDeliveryInstallationArea.class,
    AdxpExplicitCity.class,
    AdxpExplicitStreetNameBase.class,
    AdxpExplicitDelimiter.class,
    AdxpExplicitStreetName.class,
    AdxpExplicitDeliveryInstallationQualifier.class
})
public class ADXPExplicit {

    @XmlValue
    protected String content;
    @XmlAttribute
    protected List<String> partType;

    /**
     * 
     *             A character string that may have a type-tag signifying its
     *             role in the address. Typical parts that exist in about
     *             every address are street, house number, or post box,
     *             postal code, city, country but other roles may be defined
     *             regionally, nationally, or on an enterprise level (e.g. in
     *             military addresses). Addresses are usually broken up into
     *             lines, which are indicated by special line-breaking
     *             delimiter elements (e.g., DEL).
     *             
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the partType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the partType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPartType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPartType() {
        if (partType == null) {
            partType = new ArrayList<String>();
        }
        return this.partType;
    }

}
