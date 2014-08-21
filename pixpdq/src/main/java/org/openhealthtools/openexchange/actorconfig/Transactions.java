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

package org.openhealthtools.openexchange.actorconfig;

import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;

/**
 * This class defines a group of related transactions that one actor
 * interacts with another actor. For example, an XCA initiating gateway
 * interacts with a responding gateway with two related transactions, 
 * namely CrossGatewayQuery and CrossGatewayRetrieve. The connection 
 * information for the two transactions can be grouped in one 
 * Transactions definition. 
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class Transactions {
	/* The ID of the entity for the transactions */
	private String id = null;
	/* The submit connection description*/
	private IConnectionDescription submit = null;
	/* The query connection description*/
	private IConnectionDescription query = null;
	/* The retrieve connection description*/
	private IConnectionDescription retrieve = null;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the submit
	 */
	public IConnectionDescription getSubmit() {
		return submit;
	}
	/**
	 * @param submit the submit to set
	 */
	public void setSubmit(IConnectionDescription submit) {
		this.submit = submit;
	}
	/**
	 * @return the query
	 */
	public IConnectionDescription getQuery() {
		return query;
	}
	/**
	 * @param query the query to set
	 */
	public void setQuery(IConnectionDescription query) {
		this.query = query;
	}
	/**
	 * @return the retrieve
	 */
	public IConnectionDescription getRetrieve() {
		return retrieve;
	}
	/**
	 * @param retrieve the retrieve to set
	 */
	public void setRetrieve(IConnectionDescription retrieve) {
		this.retrieve = retrieve;
	}
	
	
}
