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
package org.openhealthtools.openpixpdq.integrationtests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhealthtools.openexchange.config.ConfigurationException;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhie.openempi.ApplicationException;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;


/**
 * This class cleans up the test patients from OpenEMPI database. This 
 * is the only class that depends on the underlying EMPI.
 */
public class CleanupTestPatients {
	
  public static void main(String[] args) {
	  Context.startup();
	  Context.authenticate("admin", "admin");
	  deleteAllPatients();
  }
  
  private static void deleteAllPatients() {
	  try {
		  PropertyFacade.loadProperties(new String[]{"persons.to.be.deleted.txt"});
		  for (int i=1; ; i++){
    		  String[] name = PropertyFacade.getStringArray("person" + i);
    		  if (null == name) {
    			  continue;
    		  }
    		  //Stop if both the last name is END
    		  if (name[0].equalsIgnoreCase("END")) {
    			  break;
    		  }
    		  deletePerson(name[0], name[1]);
		  }
	  }catch(ConfigurationException e) {
          e.printStackTrace();
	  }
  }
  
  private static void deletePerson(String lastName, String firstName) {
      try {
          Person person = new Person();
          person.setGivenName(firstName);
          person.setFamilyName(lastName);
          deletePerson(person);
      } catch (Exception e) {
          e.printStackTrace();
      } 
  }
  
  private static void deletePerson(Person person) throws ApplicationException {
    List<Person> persons = Context.getPersonQueryService().findPersonsByAttributes(person);
		
    Map<PersonIdentifier,PersonIdentifier> idsDeleted=new HashMap<PersonIdentifier,PersonIdentifier>();
		
    for (Person personFound : persons) {
        PersonIdentifier id = personFound.getPersonIdentifiers().iterator().next();
            if (id.getDateVoided() == null && idsDeleted.get(id) == null) {
            	Context.getPersonManagerService().deletePerson(id);
 
                for (PersonIdentifier idDeleted : personFound.getPersonIdentifiers()) {
                    idsDeleted.put(idDeleted, idDeleted);
                }
            }
        }
    }	 
}
