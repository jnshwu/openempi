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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;

import org.apache.log4j.Logger;


enum MainTableFields { messageid , ip  , timereceived  , test  ,pass , is_secure } ;  
/**
 *Class representing the main informations to display in the table of messages.
 * 
 * @author <a href="mailto:anilkumar.reddy@misys.com">Anil kumar</a>
 * 
 */
public class MainTable extends AbstractLogTable {

    public static final String MESSAGE_ID = "messageid"    ;
    public static final String IP         = "ip"           ;
    public static final String TIMESTAMP  = "timereceived" ;
    public static final String TEST       = "test"         ;	
    public static final String PASS       = "pass"         ;
    public static final String IS_SECURE  = "is_secure"    ;
    public static String IP_ADDRESS = "ip" ;

    public static final String TABLE_NAME = "main" ;

    public  String writeSqlCommand = null ;
    public  String readSqlCommand  = null ;
    public  String createTableSqlCommand = null ;

    private  PreparedStatement readPreparedStatement   ;
    private  PreparedStatement writePreparedStatement  ;

    private String      messageId =null  ;
    private String ipAddress ;
    private Timestamp   timestamp ;
    private String      test      ;
    private boolean     pass      ;
    private boolean     isSecure  ;
    private final static Logger logger = Logger.getLogger(MainTable.class);

    public MainTable ( Connection c  ) 
    {
	database = c ;
	tableName = TABLE_NAME ;

	readSqlCommand           = "select " + MESSAGE_ID +" , " + IS_SECURE + " , "+ IP +" ," + TIMESTAMP + " , " + TEST + " , " + PASS +" FROM " + TABLE_NAME + " where "+ MESSAGE_ID + " = ? ;"  ;
	writeSqlCommand          = "insert into "+ TABLE_NAME + " values (?,?,?,?,?,?);"   ; 

	try {
	    if ( database== null || database.isClosed() )
		logger.debug("Database null or closed") ;

	    /** TODO PUT THAT ( preparedStatement ) into a static method in order to do it once and not to each constructor call **/
	    if ( readPreparedStatement == null   ) readPreparedStatement = database.prepareStatement(readSqlCommand ) ;
	    if ( writePreparedStatement  == null ) writePreparedStatement = database.prepareStatement(writeSqlCommand ) ;
	    test      = new String() ;	
	} catch (SQLException sqlException) 
	{
	    logger.debug("Database problem (SqlException ) " + sqlException.getMessage()) ;			
	}
    }

    public MainTable ( Connection c , String name , String inSqlCreateTable , String inSqlCommandRead, String inSqlCommandWrite ) throws LoggerException
    {
		readSqlCommand           = inSqlCommandRead  ;
		writeSqlCommand          = inSqlCommandWrite ; 
	
		database = c ;
		try 
		{
		    if ( readPreparedStatement == null )  readPreparedStatement = database.prepareStatement(readSqlCommand ) ;
		    if ( writePreparedStatement  == null ) writePreparedStatement = database.prepareStatement(writeSqlCommand ) ;	
		}
		catch( SQLException sqlException )
		{
			logger.debug("Database problem (SqlException ) " + sqlException.getMessage()) ;
		}
	 }

    /*************GETTERS AND SETTERS*********************/
    public String getIpAddress() {
	return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }
    public String getMessageId() {
	return messageId;
    }
    public void setMessageId(String messageId) {
	this.messageId = messageId;
    }
    public boolean isPass() {
	return pass;
    }
    public void setPass(boolean pass) {
	this.pass = pass;
    }
    public PreparedStatement getReadPreparedStatement() {
	return readPreparedStatement;
    }
    public String getTest() {
	return test;
    }
    public void setTest(String test) {
	this.test = test;
    }
    public Timestamp getTimestamp() {
	return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
	this.timestamp = timestamp;
    }

    public int readFromDB( String inMessageId ) throws LoggerException {

	try {
	    if ( database== null || database.isClosed() )
		throw new LoggerException("Database null or closed") ;

	    if ( readSqlCommand!=null )
	    {
		if ( messageExist( inMessageId , MainTable.TABLE_NAME ) )
		{
		    if ( readPreparedStatement!=null )
		    {
			readPreparedStatement.setString( 1 , inMessageId ) ;
			ResultSet result = readPreparedStatement.executeQuery() ;
			//"select messageid , ip , timereceived , test , pass FROM " + tableName + " where messageid = ? ;"  ;
			result.next() ;
			messageId = result.getString( 1 ) ;
			isSecure  = result.getBoolean( 2 ) ;
//			try 
//			{
			    ipAddress = result.getString( 3 ) ;
//			}
//			catch ( UnknownHostException e) {}

			timestamp = result.getTimestamp( 4 ) ;
			test      = result.getString   ( 5 ) ;
			pass      = result.getBoolean  ( 6 ) ;
		    }
		}
		else return -1 ;
	    }
	    else return -1 ;     

	} catch (SQLException sqlException)
	{ 
		logger.debug("Database problem (SqlException ) " + sqlException.getMessage()) ;
	}
	return 0;
    }



    void setReadSqlCommand(String sqlCommand) {
	readSqlCommand = sqlCommand ;
    }

    void setWriteSqlCommand(String sqlCommand) {
	writeSqlCommand = sqlCommand ;
    }

    public int writeToDB( MainTableFields parameterName, String parameterValue)
    throws LoggerException {

	try {
	    if ( database == null || database.isClosed() )
	    {
	    	logger.debug("Database null or closed") ;
	    }
	} catch (SQLException e1) {
		logger.debug("MainTable:writeToDB( MainTableFields parameterName, String parameterValue) " + e1.getMessage() ) ;
	}

	Statement s;
	try {
	    s = database.createStatement();

	    if ( messageId == null )		
	    	logger.debug( "MaintTable:writeToDB(MainTableFields parameterName, String parameterValue) : messageId is null" ) ;

	    if ( parameterValue!=null && !parameterValue.trim().equals("") )
	    {
		if ( messageExist(messageId , MainTable.MESSAGE_ID ) )
		{
		    if ( parameterName.equals(MainTableFields.messageid))
		    {
			System.out.println( "UPDATE "+ TABLE_NAME +" SET " + MESSAGE_ID +" = '"+ parameterValue +"' WHERE " + MESSAGE_ID +" = '"+ messageId +"';" ) ;
			s.execute( "UPDATE "+ TABLE_NAME +" SET " + MESSAGE_ID +" = '"+ parameterValue +"' WHERE " + MESSAGE_ID +" = '"+ messageId +"';"  ) ;
			// s.execute( "insert into "+ tableName +" values ( '"+ parameterValue.trim() + "' , '' , 'now' , -1 ,false ) ;"  );
			messageId =  parameterValue.trim() ;
		    }
		    if ( parameterName.equals(MainTableFields.ip))
		    {
//			try {
			    ipAddress = parameterValue; //InetAddress.getByName(parameterValue) ;
			    s.execute( "UPDATE "+ TABLE_NAME +" SET " + IP +"  = '"+ parameterValue +"' WHERE " + MESSAGE_ID +" = '"+ messageId +"';"  ) ;
//			} catch (UnknownHostException e) {}
		    }
		    else if ( parameterName.equals( MainTableFields.is_secure ))
		    {
			s.execute( "UPDATE "+ TABLE_NAME +" SET " + IS_SECURE +"  = '"+ parameterValue +"' WHERE " + MESSAGE_ID +" = '"+ messageId +"';"  ) ;	      
			isSecure = new Boolean(parameterValue).booleanValue() ;
		    }
		    else if ( parameterName.equals  ( MainTableFields.timereceived ))
		    {
			s.execute( "UPDATE "+ TABLE_NAME +" SET " + TIMESTAMP +"  = '"+ parameterValue +"' WHERE " + MESSAGE_ID +" = '"+ messageId +"';"  ) ;	      
		    }
		    else if ( parameterName.equals( MainTableFields.test ))
		    {
			s.execute( "UPDATE "+ TABLE_NAME +" SET " + TEST +"  = "+ parameterValue +" WHERE " + MESSAGE_ID +" = '"+ messageId +"';"  ) ;	        	  	        	  
			test = parameterValue  ;
		    }
		    else if ( parameterName.equals( MainTableFields.pass ))
		    {
			s.execute( "UPDATE "+ TABLE_NAME +" SET " + PASS +"  = "+ parameterValue +" WHERE " + MESSAGE_ID +" = '"+ messageId +"';"  ) ;	        	  	        	          	  
			pass = new Boolean(parameterValue).booleanValue() ;
		    }
		}
		else
		{

		    if ( parameterName.equals(MainTableFields.messageid))
		    {
			if ( messageExist(parameterValue.trim() , MainTable.TABLE_NAME) )
			{
			    messageId =  parameterValue.trim() ;
			}
			else if ( !parameterValue.trim().equals("") )
			{
			    s.execute( "insert into "+ TABLE_NAME +" values ( '"+ parameterValue.trim() + "' , '' , 'now' , -1 ,false ) ;"  );
			    messageId =  parameterValue.trim() ;
			}
		    }
		    else return -1 ;

		}
	    }
	}
	catch (SQLException e1)
	{
		logger.debug("MainTable:writeToDB( MainTableFields parameterName, String parameterValue) " + e1.getMessage() ) ; 
	}
	return 0;
    }
    /*int writeToDB() throws LoggerException  {
	//insert into "+ tableName + " values (?,?,?,?,?);"   ;
	// "select messageid , ip , timereceived , test , pass FROM " + tableName + " where messageid = ? ;"  ;
	// Test messageID

	try {
	    if ( tableName!=null && !tableExist() )
		//createTable( createTableSqlCommand ) ;
	    else if ( tableName == null && !tableExist() )
		return -1 ;

	    if ( messageId == null )
		throw new LoggerException( "MaintTable:writeToDB() : messageId is null" ) ;
	    
	    writePreparedStatement.setString( 1 , messageId ) ;

	    writePreparedStatement.setBoolean( 2 , isSecure ) ;
	    // Test Ip
	    if ( ipAddress== null )
	    {
//		try 
//		{
//		    ipAddress = InetAddress.getByName("localhost") ;
//		} catch (UnknownHostException e) 
//		{}
	    	ipAddress = "1.1.1.1";
	    }
	    writePreparedStatement.setString( 3 , ipAddress )  ;
            // Added 11/14/2007
	    if (!ipExist(ipAddress ) ) 
	    {
		CompanyDetails ipCompanyTable = new CompanyDetails( ) ;
		{
		    ipCompanyTable.writeToDB(  ipAddress  ) ;
		}
	    }

	    // timereceived
	    if ( timestamp == null )
	    {
		timestamp = new Timestamp( new GregorianCalendar().getTimeInMillis() ) ;
	    }
	    writePreparedStatement.setTimestamp( 4 , timestamp ) ;
	    // test
	    writePreparedStatement.setString( 5 , test ) ;

	    //pass
	    writePreparedStatement.setBoolean( 6 , pass ) ;
	    System.out.println( writePreparedStatement.toString() ) ;
	    writePreparedStatement.execute() ;

	} catch (SQLException e1) 
	{
	    throw new LoggerException( "Main table:writeToDB() problem : (" + e1.getErrorCode() + " ) " + e1.getMessage()  ) ;
	}
	return 0;
    }*/

    public String toString()
    {
	return "MessageId:"+messageId +
	"\nIP :" + ipAddress + 
	"\nTimestamp:" + timestamp.toString() + 
	"\nPass :" + pass +  
	"\nTest :" + test +"\n" ;
    }

    public String toXml()
    {
	StringBuffer stringBuff = new StringBuffer() ;
	stringBuff.append("<mainMessage>") ;
	stringBuff.append("	<node name=\"MessageId\" value=\""+ messageId + "\" />"  ) ;
	stringBuff.append("	<node name=\"IP\" value=\""+ ipAddress  + "\" />"  ) ;
	stringBuff.append("	<node name=\"Timestamp\" value=\""+ timestamp.toString()  + "\" />"  ) ;
	stringBuff.append("	<node name=\"Pass\" value=\""+  pass + "\" />"  ) ;
	stringBuff.append("	<node name=\"Test\" value=\""+ test + "\" />"  ) ;
	stringBuff.append("</mainMessage>") ;

	return stringBuff.toString() ;
    }
    
    public HashMap<String, Object> toHashMap() {
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	
    	map.put("MessageId", messageId);
    	map.put("IP", ipAddress);
    	map.put("Timestamp", timestamp.toString());
    	map.put("Pass", (pass) ? "Pass" : "Fail");
    	map.put("Test", test);
    	
    	return map;
    }
    
    
    
    public String toJSon()
    {
	StringBuffer stringBuff = new StringBuffer() ;
	stringBuff.append("{ \"name\"  : \"mainMessage\"  , \n" ) ;
	stringBuff.append("  \"values\" : [\n " ) ;        	
	
	stringBuff.append(" [ \"MessageId\" , \""+ messageId  + "\"],\n "  ) ;
	if ( ipAddress!= null )  stringBuff.append("[ \"IP\"        , \""+ ipAddress + "\"] ,\n "  ) ;
	if ( timestamp != null ) stringBuff.append("[ \"Timestamp\" , \""+ timestamp.toString() + "\"],\n "  ) ;
	stringBuff.append("[ \"Pass\"      , \""+ pass + "\" ], \n "  ) ;
	stringBuff.append("[ \"Test\"      , \""+ test + "\" ]\n]} "  ) ;

	return stringBuff.toString() ;
    }
    public boolean isSecure() 
    {
	return isSecure;
    }
    public void setSecure(boolean isSecure) 
    {
	this.isSecure = isSecure;
    }

    
    /** 
	 * Test if the current ipAddress exists
	 * @param ipAddress
	 * @return
	 * @throws LoggerException
	 */
	public boolean ipExist ( String ipAddress   ) throws LoggerException {
	  
			if ( tableName!=null  )
			{
			 /*if ( tableExist( tableName  ) )
			 {*/
				 try {
					if ( database==null || database.isClosed() ) 
							throw new LoggerException("Database null or closed") ;
			
				 
						String sqlRequest = "SELECT count(*) from " + TABLE_NAME + " where  "+ IP_ADDRESS +"='"+ ipAddress +"' ;" ;
		
						Statement st = database.createStatement() ;
					    ResultSet res = st.executeQuery( sqlRequest ) ;
			
					    res.next() ;
					    if ( res.getInt(1) == 0 ) return false ;
					    else if ( res.getInt(1) > 0 ) return true ;   
				} catch (SQLException e) 
				{
					 throw new LoggerException("IPcompanyTable:ipExist( String ipAddress ) problem : (" + e.getErrorCode() + " )" + e.getMessage()  ) ;
				}
				  
			/* }
			 else return false ;*/
			}
			
			return false ;
	  }

}