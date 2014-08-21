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

import java.util.Date;

/**
 * This class represent a "Visit"
 * The equals and hashcode methods only use the systemId and the visitId because
 * these two variables taken together uniquely identify the visit.
 *
 * @author : Shantanu Paul
 **/

public class Visit {
    private String          systemId;
    private String          visitId;
    private Date            visitStartTimestamp;
    private Date            visitEndTimestamp;
    private String          reason;
    private boolean         confidential = false;
    private boolean         deleted = false;
    private String          facilityName;

    /**
     * Only constructor to create a Visit object - a visit must have
     * a systemId and a visitId. Hence these are included in the constructor,
     * no default constructor is provided and no setters for these 2 variables is
     * provided
     *
     * @param systemId - the systemId of teh visit
     * @param visitId - the visitId of the visit
     */
    public Visit(String systemId, String visitId) {
        this.systemId = systemId;
        this.visitId = visitId;
    }
    /**
     * Get the systemid of this visit
     * @return
     */
    public String getSystemId() {
        return systemId;
    }
    /**
     * Get the visitId of this visit
     * @return
     */
    public String getVisitId() {
        return visitId;
    }

    /**
     * Get the start date of this visit
     * @return
     */
    public Date getVisitStartTimestamp() {
        return visitStartTimestamp;
    }
    /**
     * Set the start date of this visit
     * @param visitStartTimestamp
     */
    public void setVisitStartTimestamp(Date visitStartTimestamp) {
        this.visitStartTimestamp = visitStartTimestamp;
    }
    /**
     * Get the end date of this visit
     * @return
     */
    public Date getVisitEndTimestamp() {
        return visitEndTimestamp;
    }
    /**
     * Set teh end date of this visit
     * @param visitEndTimestamp
     */
    public void setVisitEndTimestamp(Date visitEndTimestamp) {
        this.visitEndTimestamp = visitEndTimestamp;
    }
    /**
     * Get the reason for this visit
     * @return
     */
    public String getReason() {
        return reason;
    }
    /**
     * Set teh reason for this visit
     * @param reason - the reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    /**
     * Indicates whether this visit is confidential
     * @return
     */
    public boolean isConfidential() {
        return confidential;
    }
    /**
     * Set the confidentiality
     * @param confidential
     */
    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
    }
    /**
     * Indicates whether this visit is marked as deleted
     * @return
     */
    public boolean isDeleted() {
        return deleted;
    }
    /**
     * Set teh delete flag
     * @param deleted
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visit)) return false;

        final Visit visit = (Visit) o;

        if (systemId != null ? !systemId.equals(visit.systemId) : visit.systemId != null) return false;
        if (visitId != null ? !visitId.equals(visit.visitId) : visit.visitId != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (systemId != null ? systemId.hashCode() : 0);
        result = 29 * result + (visitId != null ? visitId.hashCode() : 0);
        return result;
    }

}
