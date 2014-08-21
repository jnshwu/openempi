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
 * <p>Java class for SamlSignatureKeyInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SamlSignatureKeyInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rsaKeyValueModulus" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="rsaKeyValueExponent" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SamlSignatureKeyInfoType", propOrder = {
    "rsaKeyValueModulus",
    "rsaKeyValueExponent"
})
public class SamlSignatureKeyInfoType {

    protected byte[] rsaKeyValueModulus;
    protected byte[] rsaKeyValueExponent;

    /**
     * Gets the value of the rsaKeyValueModulus property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRsaKeyValueModulus() {
        return rsaKeyValueModulus;
    }

    /**
     * Sets the value of the rsaKeyValueModulus property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRsaKeyValueModulus(byte[] value) {
        this.rsaKeyValueModulus = ((byte[]) value);
    }

    /**
     * Gets the value of the rsaKeyValueExponent property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getRsaKeyValueExponent() {
        return rsaKeyValueExponent;
    }

    /**
     * Sets the value of the rsaKeyValueExponent property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setRsaKeyValueExponent(byte[] value) {
        this.rsaKeyValueExponent = ((byte[]) value);
    }

}
