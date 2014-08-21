/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openexchange.actorconfig.net;

import java.util.Hashtable;
import java.util.Set;

import org.openhealthtools.openexchange.utils.Pair;

/**
 * This class describes an ebXML coding scheme.
 *
 * @author Jim Firby
 */
public class CodeSet {

    private String name = null;
    private String classificationScheme = null;
    //Pair<String(code), String(codingScheme)>
    //code is required (cannot be null) while codingScheme is optional.
    private Hashtable<Pair<String,String>, CodeSetEntry> entries = null;

    /**
     * Create a new code set with the given name and ebXML
     * classification scheme ID.
     *
     * @param name                 The name of the coding scheme
     * @param classificationScheme The classification scheme ID
     */
    public CodeSet(String name, String classificationScheme) {
        this.name = name;
        this.classificationScheme = classificationScheme;
        entries = new Hashtable<Pair<String,String>, CodeSetEntry>();
    }

    /**
     * @return The number of codes in this code set
     */
    public int size() {
        return entries.size();
    }

    /**
     * Add an entry to this code set.
     *
     * @param code             The code value
     * @param displayName      A human-readable description of the code value
     * @param codingSchemeName The name of the coding scheme this value is taken from
     * @param ext              A mime-type extension
     */
    public void addEntry(String code, String displayName, String codingSchemeName, String ext) {
        CodeSetEntry entry = new CodeSetEntry();
        entry.value = code;
        entry.displayName = displayName;
        entry.codingScheme = codingSchemeName;
        entry.ext = ext;
        entries.put(new Pair(code, codingSchemeName), entry);
    }

    /**
     * @return The name of this code set
     */
    public String getCodeType() {
        return name;
    }

    /**
     * @return The ebXML classification scheme ID for this code set (if there is one)
     */
    public String getClassificationScheme() {
        return classificationScheme;
    }

    /**
     * Check whether this code set contains a particular code value.
     *
     * @param code The code value to check
     * @return True if this code set contains this value as a code
     * @deprecated the new data structure requires a codingScheme,
     *             so use {@link #containsCode(String, String)} instead of this method.
     */
    public boolean containsCode(String code) {
        for (Pair pair : entries.keySet()) {
            if (pair.first == null) {
                continue;
            }
            if (pair.first.equals(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether this code set contains a particular code value.
     *
     * @param code         The code value to check
     * @param codingScheme The codingScheme of the code
     * @return True if this code set contains this value as a code
     */
    public boolean containsCode(String code, String codingScheme) {
        return entries.containsKey(new Pair(code, codingScheme));
    }


    /**
     * Get the display name for a particular code value.
     *
     * @param code The code value
     * @return The display name for this code value
     * @deprecated the new data structure requires a codingScheme,
     *             so use {@link #getDisplayName(String, String)} instead of this method.
     */
    public String getDisplayName(String code) {
        for (Pair<String,String> pair : entries.keySet()) {
            if (pair.first == null) {
                continue;
            }
            if (pair.first.equals(code)) {
                CodeSetEntry entry = entries.get(pair);
                if (entry != null) {
                    return entry.displayName;
                }
            }
        }
        return null;
    }

    /**
     * Get the display name for a particular code value.
     *
     * @param code         The code value
     * @param codingScheme The codingScheme of the code
     * @return The display name for this code value
     */
    public String getDisplayName(String code, String codingScheme) {
        CodeSetEntry entry = entries.get(new Pair(code, codingScheme));
        if (entry != null) {
            return entry.displayName;
        }
        return null;
    }

    /**
     * Get the name of the coding scheme that the code value is taken from.
     *
     * @param code The code value
     * @return The coding scheme this code value is taken from
     * @deprecated the new data structure requires a codingScheme,
     *             so use {@link #getDisplayName(String, String)} instead of this method.
     */
    public String getCodingScheme(String code) {
        for (Pair<String,String> pair : entries.keySet()) {
            if (pair.first == null) {
                continue;
            }
            if (pair.first.equals(code)) {
                CodeSetEntry entry = entries.get(pair);
                if (entry != null) {
                    return entry.codingScheme;
                }
            }
        }
        return null;
    }

    /**
     * Get the extension name of the code type that the code value is taken from.
     *
     * @param code         The code value
     * @param codingScheme The codingScheme of the code
     * @return The code extension this code value is taken from
     */
    public String getExt(String code, String codingScheme) {
        CodeSetEntry entry = entries.get(new Pair(code, codingScheme));
        if (entry != null) {
            return entry.ext;
        }
        return null;
    }

    /**
     * A private class to group a code value with its display name and coding
     * scheme.
     *
     * @author Jim Firby
     * @version 2.0 - Nov 13, 2005
     */
    private class CodeSetEntry {

        String value = null;
        String displayName = null;
        String codingScheme = null;
        String ext = null;
    }

    /**
     * Get a set of CodeSet keys
     *
     * @return the set of codeSet keys.
     */
    public Set<Pair<String,String>> getCodeSetKeys() {
        return entries.keySet();
	}

}
