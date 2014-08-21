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
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HtmlLinkType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="HtmlLinkType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="alternate"/>
 *     &lt;enumeration value="appendix"/>
 *     &lt;enumeration value="bookmark"/>
 *     &lt;enumeration value="chapter"/>
 *     &lt;enumeration value="contents"/>
 *     &lt;enumeration value="copyright"/>
 *     &lt;enumeration value="glossary"/>
 *     &lt;enumeration value="help"/>
 *     &lt;enumeration value="index"/>
 *     &lt;enumeration value="next"/>
 *     &lt;enumeration value="prev"/>
 *     &lt;enumeration value="section"/>
 *     &lt;enumeration value="start"/>
 *     &lt;enumeration value="stylesheet"/>
 *     &lt;enumeration value="subsection"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "HtmlLinkType")
@XmlEnum
public enum HtmlLinkType {

    @XmlEnumValue("alternate")
    ALTERNATE("alternate"),
    @XmlEnumValue("appendix")
    APPENDIX("appendix"),
    @XmlEnumValue("bookmark")
    BOOKMARK("bookmark"),
    @XmlEnumValue("chapter")
    CHAPTER("chapter"),
    @XmlEnumValue("contents")
    CONTENTS("contents"),
    @XmlEnumValue("copyright")
    COPYRIGHT("copyright"),
    @XmlEnumValue("glossary")
    GLOSSARY("glossary"),
    @XmlEnumValue("help")
    HELP("help"),
    @XmlEnumValue("index")
    INDEX("index"),
    @XmlEnumValue("next")
    NEXT("next"),
    @XmlEnumValue("prev")
    PREV("prev"),
    @XmlEnumValue("section")
    SECTION("section"),
    @XmlEnumValue("start")
    START("start"),
    @XmlEnumValue("stylesheet")
    STYLESHEET("stylesheet"),
    @XmlEnumValue("subsection")
    SUBSECTION("subsection");
    private final String value;

    HtmlLinkType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static HtmlLinkType fromValue(String v) {
        for (HtmlLinkType c: HtmlLinkType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
