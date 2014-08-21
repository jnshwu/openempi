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

package org.openhealthtools.openexchange.syslog;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.syslog.LoggerException;

/**
 * This is the class for generating the database connection
 * @author <a href="mailto:anilkumar.reddy@misys.com">Anil kumar</a>
 *
 */
public class DBConnection {
	private static String DRIVER = "org.postgresql.Driver"; 
	private static String URL = "jdbc:postgresql://localhost:5432/logs"; 
	private static String USERNAME = "logs"; 
	private static String PASSWORD = "logs"; 
	private static String databaseType = null;
	private static org.apache.commons.logging.Log logger = LogFactory.getLog(DBConnection.class);
	
	private static Connection connection = null ; 
	private DBConnection(){
		
		try{
			if (connection == null || connection.isClosed()) {
				/*String[] propertyFiles = BootStrapProperties.getPropertyFiles(new String[]{"logs.properties"});
				PropertyFacade.loadProperties(propertyFiles);*/
				if(PropertyFacade.getString("logs.db.driver") == null){
					DRIVER = PropertyFacade.getString("logs.db.driver");
				}
				if(PropertyFacade.getString("logs.db.url") == null){
					URL = PropertyFacade.getString("logs.db.url");
				}
				if(PropertyFacade.getString("logs.db.username") == null){
					USERNAME = PropertyFacade.getString("logs.db.username");
				}
				if(PropertyFacade.getString("logs.db.password") == null){
					PASSWORD = PropertyFacade.getString("logs.db.password");
				}
				Class.forName(DRIVER);
				connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); 
           }
		}catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}
	
	public static Connection getConnection(){
		new DBConnection();
		return connection;
	}
	
	public static String getDatabaseType() throws LoggerException {
        if (databaseType == null){
            if (connection == null) {
                getConnection();
            }
            try {
                databaseType = connection.getMetaData().getDatabaseProductName();
            } catch (Exception ex) {
                logger.error(ex);
                throw new LoggerException(ex.getMessage());
            }
        }
        logger.info("Database Type: " + databaseType);
        return databaseType;
    }
}
