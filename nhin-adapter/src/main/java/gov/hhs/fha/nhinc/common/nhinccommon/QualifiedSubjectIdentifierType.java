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
 * <p>Java class for QualifiedSubjectIdentifierType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QualifiedSubjectIdentifierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubjectIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AssigningAuthorityIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QualifiedSubjectIdentifierType", propOrder = {
    "subjectIdentifier",
    "assigningAuthorityIdentifier"
})
public class QualifiedSubjectIdentifierType {

    @XmlElement(name = "SubjectIdentifier", required = true)
    protected String subjectIdentifier;
    @XmlElement(name = "AssigningAuthorityIdentifier", required = true)
    protected String assigningAuthorityIdentifier;

    /**
     * Gets the value of the subjectIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectIdentifier() {
        return subjectIdentifier;
    }

    /**
     * Sets the value of the subjectIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectIdentifier(String value) {
        this.subjectIdentifier = value;
    }

    /**
     * Gets the value of the assigningAuthorityIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssigningAuthorityIdentifier() {
        return assigningAuthorityIdentifier;
    }

    /**
     * Sets the value of the assigningAuthorityIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssigningAuthorityIdentifier(String value) {
        this.assigningAuthorityIdentifier = value;
    }

}
