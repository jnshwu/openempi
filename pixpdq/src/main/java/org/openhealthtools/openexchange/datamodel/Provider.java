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

package org.openhealthtools.openexchange.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author : Shantanu Paul
 */
public class Provider {
    private String  providerId;
    private String  provNameFirst;
    private String  provNameLast;
    private String  provNameMiddle;
    private String  provNameSuffix;
    private String  provNameTitle;
    private List<Address> provAddressList = new ArrayList<Address>();
    /**
     * Get the ID of this provider
     * @return
     */
    public String getProviderId() {
        return providerId;
    }
    /**
     * Set the ID of this provider
     * @param providerId
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
    /**
     * Get teh first name of the Provider
     * @return
     */
    public String getProvNameFirst() {
        return provNameFirst;
    }
    /**
     * Set the first name of the Provider
     * @param provNameFirst
     */
    public void setProvNameFirst(String provNameFirst) {
        this.provNameFirst = provNameFirst;
    }
    /**
     * Get the last name of the Provider
     * @return
     */
    public String getProvNameLast() {
        return provNameLast;
    }
    /**
     * Set the last name of the Provider
     * @param provNameLast
     */
    public void setProvNameLast(String provNameLast) {
        this.provNameLast = provNameLast;
    }
    /**
     * Get the middle name of the Provider
     * @return
     */
    public String getProvNameMiddle() {
        return provNameMiddle;
    }
    /**
     * Set the middle name of the Provider
     * @param provNameMiddle
     */
    public void setProvNameMiddle(String provNameMiddle) {
        this.provNameMiddle = provNameMiddle;
    }
    /**
     * Get the name suffix of the Provider
     * @return
     */
    public String getProvNameSuffix() {
        return provNameSuffix;
    }
    /**
     * Set the name suffix of the Provider
     * @param provNameSuffix
     */
    public void setProvNameSuffix(String provNameSuffix) {
        this.provNameSuffix = provNameSuffix;
    }
    /**
     * Get the name title of the Provider
     * @return
     */
    public String getProvNameTitle() {
        return provNameTitle;
    }
    /**
     * Set the name tilte of the Provider
     * @param provNameTitle
     */
    public void setProvNameTitle(String provNameTitle) {
        this.provNameTitle = provNameTitle;
    }

    public List<Address> getProvAddressList() {
        return provAddressList;
    }

    public void setProvAddressList(List<Address> provAddressList) {
        this.provAddressList = provAddressList;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Provider)) return false;

        final Provider provider = (Provider) o;

        if (provNameFirst != null ? !provNameFirst.equals(provider.provNameFirst) : provider.provNameFirst != null) return false;
        if (provNameLast != null ? !provNameLast.equals(provider.provNameLast) : provider.provNameLast != null) return false;
        if (provNameMiddle != null ? !provNameMiddle.equals(provider.provNameMiddle) : provider.provNameMiddle != null) return false;
        if (provNameSuffix != null ? !provNameSuffix.equals(provider.provNameSuffix) : provider.provNameSuffix != null) return false;
        if (provNameTitle != null ? !provNameTitle.equals(provider.provNameTitle) : provider.provNameTitle != null) return false;
        if (providerId != null ? !providerId.equals(provider.providerId) : provider.providerId != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (providerId != null ? providerId.hashCode() : 0);
        result = 29 * result + (provNameFirst != null ? provNameFirst.hashCode() : 0);
        result = 29 * result + (provNameLast != null ? provNameLast.hashCode() : 0);
        result = 29 * result + (provNameMiddle != null ? provNameMiddle.hashCode() : 0);
        result = 29 * result + (provNameSuffix != null ? provNameSuffix.hashCode() : 0);
        result = 29 * result + (provNameTitle != null ? provNameTitle.hashCode() : 0);
        return result;
    }
}
