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

import java.util.Collection;
import java.util.Hashtable;

/**
 * Defines a set of transactions of the same type with each transactions
 * interacting with one actor. For example,
 * 
 * <pre>
 *  <TransactionsSet type="Repository">
 *    <Transactions id="1.2.43.545" submit="<submit connection>" retrieve="<retrieve connection>"/>
 *    <Transactions id="31.2.56.21" submit="<submit connection>" retrieve="<retrieve connection>"
 *  </TransactionsSet>
 * </pre>
 *     
 * In the above Repository TransactionsSet configuration, each Transactions 
 * defines both submit (for Document Submission) and retrieve (for Document Retrieve)
 * connections for a particular repository actor whose repository id is specified
 * by the id attribute.   
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class TransactionsSet {
	/* The type of this transactions set. */
    private String type = null;
    /* Hashtable<String(id), Transactions>*/
    private Hashtable<String, Transactions> values = new Hashtable<String, Transactions>();

    public TransactionsSet(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean containsId(String id) {
        if (id == null) {
            return false;
        }
        return values.containsKey(id);
    }

    public Transactions getValue(String id) {
        if (id == null) {
            return null;
        }
        return values.get(id);
    }

    public void addValue(String id, Transactions value) {
        if (id == null) {
            return;
        }
        if (value == null) {
            values.remove(id);
        } else {
            values.put(id, value);
        }
    }

    public Collection<Transactions> getAllTransactions() {
    	return values.values();
    }
}
