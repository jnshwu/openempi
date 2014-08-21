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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActInvoiceOverrideCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActInvoiceOverrideCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="COVGE"/>
 *     &lt;enumeration value="PYRDELAY"/>
 *     &lt;enumeration value="EFORM"/>
 *     &lt;enumeration value="FAX"/>
 *     &lt;enumeration value="GFTH"/>
 *     &lt;enumeration value="LATE"/>
 *     &lt;enumeration value="MANUAL"/>
 *     &lt;enumeration value="ORTHO"/>
 *     &lt;enumeration value="OOJ"/>
 *     &lt;enumeration value="PAPER"/>
 *     &lt;enumeration value="PIE"/>
 *     &lt;enumeration value="REFNR"/>
 *     &lt;enumeration value="REPSERV"/>
 *     &lt;enumeration value="UNRELAT"/>
 *     &lt;enumeration value="VERBAUTH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActInvoiceOverrideCode")
@XmlEnum
public enum ActInvoiceOverrideCode {

    COVGE,
    PYRDELAY,
    EFORM,
    FAX,
    GFTH,
    LATE,
    MANUAL,
    ORTHO,
    OOJ,
    PAPER,
    PIE,
    REFNR,
    REPSERV,
    UNRELAT,
    VERBAUTH;

    public String value() {
        return name();
    }

    public static ActInvoiceOverrideCode fromValue(String v) {
        return valueOf(v);
    }

}
