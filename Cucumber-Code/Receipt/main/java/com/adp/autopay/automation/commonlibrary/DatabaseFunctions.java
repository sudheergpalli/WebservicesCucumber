package com.adp.autopay.automation.commonlibrary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.PropertiesFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseFunctions
{
public static Connection connection = null;
	
	public static void getConnection(ExecutionContext context,String ClientName){
		try {
					
			String SchemaName =  ClientName; //PropertiesFile.GetDatabasePeroperty(ClientName);
			if(context.SRunAgainst.equalsIgnoreCase("HWSE_DIT")){
				System.out.println("DIT DB");
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				connection = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION=(failover=on) (ADDRESS_LIST=(load_balance=on) (ADDRESS=(PROTOCOL=TCP)(HOST=51.16.79.18)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=51.16.79.19)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=hws15d_svc1)))",SchemaName, SchemaName);
				System.out.println("DataBase Connected");

			}
			if(context.SRunAgainst.equalsIgnoreCase("HWSE_DIT2")){
				System.out.println("DIT DB2");
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				connection = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION=(failover=on) (ADDRESS_LIST=(load_balance=on) (ADDRESS=(PROTOCOL=TCP)(HOST=10.5.127.208)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.5.127.210)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=HWS05D_SVC1)))",SchemaName,SchemaName);
			}
			if(context.SRunAgainst.equalsIgnoreCase("HWSE_FIT1")){
				System.out.println("FIT DB");
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				connection = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION=(failover=on) (ADDRESS_LIST=(load_balance=on) (ADDRESS=(PROTOCOL=TCP)(HOST=10.5.127.208)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.5.127.210)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=hws01d_svc1)))",SchemaName, SchemaName);
				System.out.println("DataBase Connected");
			}
			if(context.SRunAgainst.equalsIgnoreCase("HWSE_FIT2")){
				System.out.println("FIT DB");
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				connection = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION=(failover=on) (ADDRESS_LIST=(load_balance=on) (ADDRESS=(PROTOCOL=TCP)(HOST=10.5.127.208)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.5.127.210)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=hws01d_svc1)))",SchemaName, SchemaName);
				System.out.println("DataBase Connected");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Database Connection Error");
			e.printStackTrace();
		}
	}
	
	public static void getbenefits_CORE_DBConnection(){
		try {
				System.out.println("benefits CORE DB");
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				connection = DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION=(failover=on) (ADDRESS_LIST=(load_balance=on) (ADDRESS=(PROTOCOL=TCP)(HOST=10.117.80.219)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.117.80.220)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=benefitsqa.sdevqashrdb.adp.com)))","benefitsUSER", "benefits13");
				System.out.println("benefits CORE DataBase Connected");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Database Connection Error");
			e.printStackTrace();
		}
	}
	
	public static String getResult(String Query,String Client){
	try {
		String SchemaName = Client; //PropertiesFile.GetDatabasePeroperty(Client);
		String sql_stmt = PropertiesFile.GetSQLQuery(Query);
		String sql_afterReplace = sql_stmt.replaceAll("<DB_SCHEMA>",SchemaName);
		PreparedStatement stmt = connection.prepareStatement(sql_afterReplace);
		ResultSet rs = stmt.executeQuery(sql_afterReplace);
		
		while(rs.next()){
			String result = rs.getString(1);
			return result;
		}
		} catch (SQLException e) {
			e.printStackTrace();
	//		DatabaseFunctions.closeConnection();
			
		}
	return null;
	}
	
	public static String getResult_AfterReplaceConstants(String Query,String Client,String With,String To){
		try {
			String query_stmt = PropertiesFile.GetSQLQuery(Query);
			String sql_stmt = query_stmt.replace(With, To);
			String SchemaName = PropertiesFile.GetDatabasePeroperty(Client);
			String sql_afterReplace = sql_stmt.replaceAll("<DB_SCHEMA>",SchemaName);
			PreparedStatement stmt = connection.prepareStatement(sql_afterReplace);
			ResultSet rs = stmt.executeQuery(sql_afterReplace);
			
			while(rs.next()){
				String result = rs.getString(1);
				return result;
			}
			} catch (SQLException e) {
				e.printStackTrace();
	//			DatabaseFunctions.closeConnection();
				
			}
		return null;
		}
	
	
	
	
	public static  ArrayList<String> getResultArray(String Query,String Client){
		try{
			String sql_stmt = PropertiesFile.GetSQLQuery(Query);
			String SchemaName = PropertiesFile.GetDatabasePeroperty(Client);
			String sql_afterReplace = sql_stmt.replaceAll("<DB_SCHEMA>",SchemaName);
			PreparedStatement stmt = connection.prepareStatement(sql_afterReplace);
			ResultSet rs = stmt.executeQuery(sql_afterReplace);
			
			if(rs == null){
				return null;
			}
						
			int rowCount = rs.last()? rs.getRow() : 0;
			rs.beforeFirst();
			ArrayList<String> arrayList = new ArrayList<String>();
			
			int i = 0;
			while(rs.next()){
					for(int j=1;j<=rowCount;j++){
						arrayList.add(rs.getString(i));
					}
			}
			return arrayList;
			
		}catch(Exception e){
			e.printStackTrace();
			DatabaseFunctions.closeConnection();
		}
		return null;
	}
	
	
	
	public static ArrayList<String[]> getResultArrayList(String Query,String Client){
		try{
			String sql_stmt = PropertiesFile.GetSQLQuery(Query);
			String SchemaName = Client ; // PropertiesFile.GetDatabasePeroperty(Client);
			String sql_afterReplace = sql_stmt.replaceAll("<DB_SCHEMA>",SchemaName);
			PreparedStatement stmt = connection.prepareStatement(sql_afterReplace);
			ResultSet rs = stmt.executeQuery(sql_afterReplace);
			
			if(rs == null){
				return null;
			}
						
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();	
			
			System.out.println(columns);
			System.out.println(rsmd.toString());			
			
			
			ArrayList<String[]> arrayList = new ArrayList<String[]>();
			
			
			while(rs.next()){
				String[] arrRow = new String[columns];
					for(int j=1;j<=columns;j++){
						arrRow[j-1]=rs.getString(j);
					}
				arrayList.add(arrRow);
			}
			return arrayList;
			
		}catch(Exception e){
			e.printStackTrace();
			DatabaseFunctions.closeConnection();
		}
		return null;
	}
	
	
	public static ArrayList<String> getResultArrayStringList(String Query,String Client){
		try{
			String sql_stmt = PropertiesFile.GetSQLQuery(Query);
			String SchemaName = PropertiesFile.GetDatabasePeroperty(Client);
			String sql_afterReplace = sql_stmt.replaceAll("<DB_SCHEMA>",SchemaName);
			PreparedStatement stmt = connection.prepareStatement(sql_afterReplace);
			ResultSet rs = stmt.executeQuery(sql_afterReplace);
			
			if(rs == null){
				return null;
			}
						
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();			
			ArrayList<String> arrayList = new ArrayList<String>();
			
			
			while(rs.next()){
					for(int j=1;j<=columns;j++){
						arrayList.add(rs.getString(j));
					}
			}

			return arrayList;
			
		}catch(Exception e){
			e.printStackTrace();
			DatabaseFunctions.closeConnection();
		}
		return null;
	}
	
	public static void getResult_FirstRow(String Query,String Client){
		try{
			String sql_stmt = PropertiesFile.GetSQLQuery(Query);
			String SchemaName = Client; //PropertiesFile.GetDatabasePeroperty(Client);
			String sql_afterReplace = sql_stmt.replaceAll("<DB_SCHEMA>",SchemaName);
			PreparedStatement stmt = connection.prepareStatement(sql_afterReplace);
			ResultSet rs = stmt.executeQuery(sql_afterReplace);
			
			
			ResultSetMetaData metadata = rs.getMetaData();
		    int columnCount = metadata.getColumnCount();
		    
		    ArrayList<String> columns = new ArrayList<String>();
		    for (int i = 1; i < columnCount; i++) {
			String columnName = metadata.getColumnName(i);
			columns.add(columnName);
		    }
		    
		    while(rs.next()) {
				for(String columnName : columns) {
				    String value = rs.getString(columnName);
				    System.out.println(columnName + " = " + value);
					}
				break;
			   }
			
			}catch(Exception e){
				e.printStackTrace();
				DatabaseFunctions.closeConnection();
			}
		
		
	}
	public static void closeConnection(){
		try {
			connection.close();
			System.out.println("Database conncetion closed");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String args[]){
//		DatabaseFunctions.getConnection();
//		DatabaseFunctions.getResult_FirstRow("TEST1");
//		DatabaseFunctions.closeConnection();
//	}


	public static String ValidateQuery(ExecutionContext context,String Query,String Client){
		String ValidatedQuery = Query;
		String[] Query_Components = Query.split(" ");
		String Client_Schema = null;
		Client_Schema = PropertiesFile.GetDatabasePeroperty(Client);
		if(Query_Components.length >0 ){
			for(String element: Query_Components){
				if(element.matches("<CLIENT_SCHEMA>")){
					ValidatedQuery.replace(element, Client_Schema);
				}
			}
			return ValidatedQuery;
		}
		return ValidatedQuery;
	}
	
	
	public static void GenerateJSONfileFromResultSet(String Query,String Filename,String Client){
		try{
			String sql_stmt = PropertiesFile.GetSQLQuery(Query);
			String SchemaName = PropertiesFile.GetDatabasePeroperty(Client);
			String sql_afterReplace = sql_stmt.replaceAll("<DB_SCHEMA>",SchemaName);
			PreparedStatement stmt = connection.prepareStatement(sql_afterReplace);
			ResultSet rs = stmt.executeQuery(sql_afterReplace);
			
			JSONArray output = convertJSON(rs);
			String FilePath = "C:\\Temp\\QueryResult";
			File Directory = new File(FilePath);
			if(!Directory.exists()){
				Directory.mkdir();
			}
			File Jsonfile = new File(FilePath+"\\"+Filename+".json");
			FileWriter file = new FileWriter(Jsonfile);
			try {
	            file.write(output.toString());
	            System.out.println("Successfully Copied JSON Object to File...");
	            System.out.println("\nJSON Object: " + output.toString());
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	 
	        } finally {
	            file.flush();
	            file.close();
	        }
	}catch(Exception e){
		e.printStackTrace();
		}
	}
	
	
	public static JSONArray convertJSON(ResultSet rs) throws SQLException, JSONException
    {
      JSONArray json = new JSONArray();
      ResultSetMetaData rsmd = rs.getMetaData();
      while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            
            for(int i=1; i<numColumns+1; i++) {

              String column_name = rsmd.getColumnLabel(i);  //Bugfix , works better than getColumnName() /Aries 

              switch( rsmd.getColumnType( i ) ) {
                case java.sql.Types.ARRAY:
                  obj.put(column_name, rs.getArray(column_name));     break;
                case java.sql.Types.BIGINT:
                  obj.put(column_name, rs.getInt(column_name));       break;
                case java.sql.Types.BOOLEAN:
                  obj.put(column_name, rs.getBoolean(column_name));   break;
                case java.sql.Types.BLOB:
                  obj.put(column_name, rs.getBlob(column_name));      break;
                case java.sql.Types.DOUBLE:
                  obj.put(column_name, rs.getDouble(column_name));    break;
                case java.sql.Types.FLOAT:
                  obj.put(column_name, rs.getFloat(column_name));     break;
                case java.sql.Types.INTEGER:
                  obj.put(column_name, rs.getInt(column_name));       break;
                case java.sql.Types.NVARCHAR:
                  obj.put(column_name, rs.getNString(column_name));   break;
                case java.sql.Types.VARCHAR:
                  obj.put(column_name, rs.getString(column_name));    break;
                case java.sql.Types.TINYINT:
                  obj.put(column_name, rs.getInt(column_name));       break;
                case java.sql.Types.SMALLINT:
                  obj.put(column_name, rs.getInt(column_name));       break;
                case java.sql.Types.DATE:
                  obj.put(column_name, DatabaseFunctions.convertDateToString(rs.getDate(column_name)));      break;
                case java.sql.Types.TIMESTAMP:
                  obj.put(column_name, DatabaseFunctions.convertDateToString(rs.getTimestamp(column_name))); break;
                default:
                  obj.put(column_name, rs.getObject(column_name));    break;
              }
            }
            json.put(obj);
      }

      return json;
    }

private static Collection<?> convertDateToString(Timestamp timestamp) {
	// TODO Auto-generated method stub
	return null;
}

private static Collection<?> convertDateToString(Date date) {
	// TODO Auto-generated method stub
	return null;
}
}