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
package org.openhealthtools.openpixpdq.api;

import java.util.List;

import org.openhealthtools.openexchange.datamodel.Patient;

/**
 * This class holds the Patient Demographics Query Result.
 * 
 * @author Wenzhi Li
 * @version 1.0, Dec 15, 2008
 * @see PdqQuery
 * @see IPdSupplier
 */
public class PdqResult {
	/** A list of list of <code>Patient</code>.  The first list is a list 
        of different logic patients, while the second list is a list of 
        the same patient in different domain systems. */
	private List<List<Patient>> patients;
	
	/** The continue reference number for subsequent PDQ query. */
	private String continuationPointer;
	
	/**
	 * Constructor.
	 * 
	 * @param patients A list of list of <code>Patient</code>.  
	 * The first list is a list of different logic patients, 
	 * while the second list is a list of the same patient in 
	 * different domain systems.
	 */
	public PdqResult(List<List<Patient>> patients) {
		super();
		this.patients = patients;
	}
	
	/**
	 * Gets the list of patients of this search result.
	 * 
	 * @return a list of list of <code>Patient</code>.  
	 * The first list is a list of different logic patients, 
	 * while the second list is a list of the same patient in 
	 * different domain systems.
	 */
	public List<List<Patient>> getPatients() {
		return patients;
	}
	
	/**
	 * Sets a list of patients for the search result.
	 * 
	 * @param patients a list of list of <code>Patient</code> to set.
	 * The first list is a list of different logic patients, 
	 * while the second list is a list of the same patient in 
	 * different domain systems.
	 */
	public void setPatients(List<List<Patient>> patients) {
		this.patients = patients;
	}

	/**
	 * Gets the continuation pointer for this PDQ search.
	 * 
	 * @return the continuationPointer
	 */
	public String getContinuationPointer() {
		return continuationPointer;
	}
	
	/**
	 * Sets the continuation pointer for this PDQ search.
	 * 
	 * @param continuationPointer the continuation pointer to set
	 */
	public void setContinuationPointer(String continuationPointer) {
		this.continuationPointer = continuationPointer;
	} 
	
}
