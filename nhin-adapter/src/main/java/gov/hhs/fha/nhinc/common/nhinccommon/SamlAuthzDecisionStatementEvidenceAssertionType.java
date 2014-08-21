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
 * <p>Java class for SamlAuthzDecisionStatementEvidenceAssertionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SamlAuthzDecisionStatementEvidenceAssertionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="issueInstant" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="issuer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="issuerFormat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="conditions" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}SamlAuthzDecisionStatementEvidenceConditionsType" minOccurs="0"/>
 *         &lt;element name="accessConsentPolicy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="instanceAccessConsentPolicy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SamlAuthzDecisionStatementEvidenceAssertionType", propOrder = {
    "id",
    "issueInstant",
    "version",
    "issuer",
    "issuerFormat",
    "conditions",
    "accessConsentPolicy",
    "instanceAccessConsentPolicy"
})
public class SamlAuthzDecisionStatementEvidenceAssertionType {

    protected String id;
    protected String issueInstant;
    protected String version;
    protected String issuer;
    protected String issuerFormat;
    protected SamlAuthzDecisionStatementEvidenceConditionsType conditions;
    protected String accessConsentPolicy;
    protected String instanceAccessConsentPolicy;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the issueInstant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueInstant() {
        return issueInstant;
    }

    /**
     * Sets the value of the issueInstant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueInstant(String value) {
        this.issueInstant = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the issuer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuer(String value) {
        this.issuer = value;
    }

    /**
     * Gets the value of the issuerFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuerFormat() {
        return issuerFormat;
    }

    /**
     * Sets the value of the issuerFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuerFormat(String value) {
        this.issuerFormat = value;
    }

    /**
     * Gets the value of the conditions property.
     * 
     * @return
     *     possible object is
     *     {@link SamlAuthzDecisionStatementEvidenceConditionsType }
     *     
     */
    public SamlAuthzDecisionStatementEvidenceConditionsType getConditions() {
        return conditions;
    }

    /**
     * Sets the value of the conditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link SamlAuthzDecisionStatementEvidenceConditionsType }
     *     
     */
    public void setConditions(SamlAuthzDecisionStatementEvidenceConditionsType value) {
        this.conditions = value;
    }

    /**
     * Gets the value of the accessConsentPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessConsentPolicy() {
        return accessConsentPolicy;
    }

    /**
     * Sets the value of the accessConsentPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessConsentPolicy(String value) {
        this.accessConsentPolicy = value;
    }

    /**
     * Gets the value of the instanceAccessConsentPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceAccessConsentPolicy() {
        return instanceAccessConsentPolicy;
    }

    /**
     * Sets the value of the instanceAccessConsentPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceAccessConsentPolicy(String value) {
        this.instanceAccessConsentPolicy = value;
    }

}
