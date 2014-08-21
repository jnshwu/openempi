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
 * <p>Java class for SamlAuthzDecisionStatementEvidenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SamlAuthzDecisionStatementEvidenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="assertion" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}SamlAuthzDecisionStatementEvidenceAssertionType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SamlAuthzDecisionStatementEvidenceType", propOrder = {
    "assertion"
})
public class SamlAuthzDecisionStatementEvidenceType {

    protected SamlAuthzDecisionStatementEvidenceAssertionType assertion;

    /**
     * Gets the value of the assertion property.
     * 
     * @return
     *     possible object is
     *     {@link SamlAuthzDecisionStatementEvidenceAssertionType }
     *     
     */
    public SamlAuthzDecisionStatementEvidenceAssertionType getAssertion() {
        return assertion;
    }

    /**
     * Sets the value of the assertion property.
     * 
     * @param value
     *     allowed object is
     *     {@link SamlAuthzDecisionStatementEvidenceAssertionType }
     *     
     */
    public void setAssertion(SamlAuthzDecisionStatementEvidenceAssertionType value) {
        this.assertion = value;
    }

}
