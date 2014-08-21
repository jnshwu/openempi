/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
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
package org.openhealthtools.openpixpdq.common;


import java.util.ArrayList;
import java.util.List;

import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.Patient;

/**
 * This class is a container to store the PDQ continuation information that pointer pointers to.
 *
 * @author Wenzhi Li
 * @version 1.0, Apr 25, 2007
 */
public class ContinuationPointer {
    private String pointer;
    private Object queryTag;
    private List<List<Patient>> patients = null;
    private List<Identifier> returnDomains = null;
    private long lastRequestTime = System.currentTimeMillis();
    private int startResultNumber = -1;
    private int totalRecords = -1;
    
    /**
     * Gets the continuation pointer
     * 
     * @return the pointer
     */
    public String getPointer() {
        return pointer;
    }
    
    /**
     * Sets the continuation pointer.
     * 
     * @param pointer the pointer to set
     */
    public void setPointer(String pointer) {
        this.pointer = pointer;
    }
    
    /**
     * Gets the query tag that this ContinuationPointer is 
     * associated with.
     * 
	 * @return the query tag
	 */
	public Object getQueryTag() {
		return queryTag;
	}
	
	/**
	 * Sets the query tag. 
	 * 
	 * @param queryTag the queryTag to set
	 */
	public void setQueryTag(Object queryTag) {
		this.queryTag = queryTag;
	}
	
	/**
	 * Gets the patients stored in this Continuation Pointer.
	 * 
	 * @return a list of list of {@link Patient}s. The first list
	 *         is for different logic patients, while the second list is for
	 *         the same logic patient in different domains. 
	 */
	public List<List<Patient>> getPatients() {
        if (patients == null)
            return new ArrayList<List<Patient>>();
        else
            return patients;
    }
	
	/**
	 * Sets the patients for this Continuation Pointer.
	 * 
	 * @param patients a list of list of {@link Patient}s. The first list
	 *         is for different logic patients, while the second list is for
	 *         the same logic patient in different domains.
	 */
    public void setPatients(List<List<Patient>> patients) {
        this.patients = patients;
    }

    /**
     * Gets the return domains of the PDQ request.
     * 
     * @return a list of return domains
     */
    public List<Identifier> getReturnDomain() {
        if (returnDomains == null)
            return new ArrayList<Identifier>();
        else
            return returnDomains;
    }
    
    /**
     * Sets the return domain of the PDQ request.
     *  
     * @param domains a list of domains to set 
     */
    public void setReturnDomain(List<Identifier> domains) {
        this.returnDomains = domains;
    }

    /**
     * Gets the last continuation request time.
     * 
     * @return the last request time in long
     */
    public long getLastRequestTime() {
        return lastRequestTime;
    }
    
    /**
     * Sets the last continuation request time.
     * 
     * @param time the time to set
     */
    public void setLastRequestTime(long time) {
        this.lastRequestTime = time;
    }
    
	/**
	 * Gets the start result number of matching records for the PDQ request.
	 * 
	 * @return the start result number of matching records
	 */
	public int getstartResultNumber() {
		return startResultNumber;
	}
	
	/**
	 * Sets the start result number of matching records for the PDQ request.
	 * 
	 * @param startResultNumber the start result number of records to set
	 */
	public void setstartResultNumber(int startResultNumber) {
		this.startResultNumber = startResultNumber;
	}
	
	/**
	 * Gets the total number of matching records for the PDQ request.
	 * 
	 * @return the total number of matching records
	 */
	public int getTotalRecords() {
		return totalRecords;
	}
	
	/**
	 * Sets the total number of matching records for the PDQ request.
	 * 
	 * @param totalRecords the total number of records to set
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
    
}
