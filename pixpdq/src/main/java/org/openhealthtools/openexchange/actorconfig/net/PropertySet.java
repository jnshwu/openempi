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


/**
 * @author Jim Firby
 */
public class PropertySet {

    private String name = null;
    private Hashtable<String, String> values = new Hashtable<String, String>();

    //private static String HIMSS_CODE = "HIMSS2005&1.3.6.1.4.1.21367.2005.1.1&ISO";

    public PropertySet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean containsValue(String valueName) {
        if (valueName == null) {
            return false;
        }
        return values.containsKey(valueName);
    }

    public String getValue(String valueName) {
        //if(valueName.equals("codingSystem"))
        //    return HIMSS_CODE;

        if (valueName == null) {
            return null;
        }
        return values.get(valueName);
    }

    public void addValue(String valueName, String value) {
        if (valueName == null) {
            return;
        }
        if (value == null) {
            values.remove(valueName);
        } else {
            values.put(valueName, value);
        }
    }

}
