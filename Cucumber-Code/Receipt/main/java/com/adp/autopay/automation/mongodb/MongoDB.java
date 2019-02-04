package com.adp.autopay.automation.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.testng.Assert;

import com.adp.autopay.automation.utility.PropertiesFile;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoDB {
	
	private MongoClient mongoClient;
	private DB db;
	private String DatabaseConnectionString = PropertiesFile.DatabaseConnectionString;
	private int DBport = Integer.parseInt(PropertiesFile.DBport);
	private String DatabaseName = PropertiesFile.DatabaseName;
	
//*********************************************
//Managing Connection to the MongoDB	
	public void connectMongoDB(){
		try{
			this.mongoClient = new MongoClient(DatabaseConnectionString, DBport);
//			this.mongoClient = new MongoClient("51.16.105.4", 27017);
			this.db = this.mongoClient.getDB( DatabaseName );
		}
		catch(Exception e){
			this.db = null;
			this.mongoClient=null;
			Assert.fail("Failed while initiating DB connection, result will be posted into DB");			
		}
	}	
	
	public void disconnectMongoDB(){
		if(this.mongoClient != null){
			this.mongoClient.close();
		}
	}
//*********************************************
	

//*********************************************
//Create and Update of Suite Id 
	public void startSuiteExecution(String suiteRunId,String SuiteName){
		if(db == null) return;
		
		this.getSuiteRunCollection().insert(MongoDBObject.getSuite(suiteRunId, SuiteName, new Date(), null));				
	}
	
	public void endSuiteExecution(String suiteRunId){
		if(db == null) return;
		
		DBCollection collection = this.getSuiteRunCollection();
		BasicDBObject document =  findSuite(collection,suiteRunId);
		MongoDBObject.appendDate(document, SuiteRun.EndDt);
		collection.update(MongoDBObject.findSuite(suiteRunId), document);				
	}
//*********************************************


//*********************************************
//Creation and Updation of Test Run Id 
	public void startTestExecution(String suiteRunId, String testRunId, String testName,String RunAgainst,String browser,String version){
		if(db == null) return;
		
		this.getTestRunCollection().insert(MongoDBObject.getTest(suiteRunId, testRunId, testName, RunAgainst, browser, version, new Date(), null, null));				
	}
	
	public void updateTestExecution(String testRunId,String version,String MachineIP){
		if(db == null) return;
		
		DBCollection collection = this.getTestRunCollection();
		BasicDBObject document =  findTest(collection,testRunId);
		MongoDBObject.appendversion(document, version);
		MongoDBObject.appendMachineIP(document, MachineIP);
		collection.update(MongoDBObject.findTest(testRunId), document);
						
	}
	public void endTestExecution(String testRunId){
		if(db == null) return;
		
		DBCollection collection = this.getTestRunCollection();
		BasicDBObject document =  findTest(collection,testRunId);
		MongoDBObject.appendDate(document, TestRun.EndDt);
		collection.update(MongoDBObject.findTest(testRunId), document);
		
	}
//*********************************************

//*********************************************
//Adding Result to a Test 
	public void addResultToTest(String testRunId,String scenarioId, String resultId, String scenarioName, String testCase, String description,String expected, String actual,  byte status, String screenshotFileName){
		if(db == null) return;
					
		String imageId = null;
		
		if(screenshotFileName != null){
			try {
				imageId = SaveImage(screenshotFileName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		this.getResultCollection().insert(MongoDBObject.getResult(testRunId, scenarioId, scenarioName, resultId, testCase, description, actual, expected, status, imageId));
		
//		insertObjectIntoList(document, TestRun.Results, MongoDBObject.getResult(scenarioId, scenarioName, resultId, testCase, description, actual, expected, status, imageId));
//		collection.update(MongoDBObject.findTest(testRunId), document);				
	}
//*********************************************
	

//*********************************************
//Private methods for performing actions	
	private DBCollection getSuiteRunCollection(){
		return db.getCollection(MongoDBTable.SuiteRuns);		
	}

	private DBCollection getTestRunCollection(){
		return db.getCollection(MongoDBTable.TestRuns);		
	}

	private DBCollection getResultCollection(){
		return db.getCollection(MongoDBTable.Result);		
	}

	private BasicDBObject findSuite(DBCollection collection, String suiteId){
		return (BasicDBObject)  collection.findOne(MongoDBObject.findSuite(suiteId));
	}
	
	private BasicDBObject findTest(DBCollection collection, String testId){
		return (BasicDBObject)  collection.findOne(MongoDBObject.findTest(testId));
	}
	
	@SuppressWarnings("unused")
	private void insertObjectIntoList(BasicDBObject document, String colName, BasicDBObject object){
		if(document.get(colName) == null){
			BasicDBList list = new BasicDBList();
		    list.put(0, object);
		    document.append(colName, list);
		}else{
			BasicDBList list = (BasicDBList) document.get(colName);	    
		    list.put(list.size(), object);
		}
	}
	
	private static byte[] LoadImage(String filePath) throws Exception {
        File file = new File(filePath);
        int size = (int)file.length();
        byte[] buffer = new byte[size];
        FileInputStream in = new FileInputStream(file);
        in.read(buffer);
        in.close();
        return buffer;
    }
	
	private String SaveImage(String filePath) throws Exception{
		byte[] imageBytes = LoadImage(filePath);
		GridFS fs = new GridFS( db );
        //Save image into database
        GridFSInputFile in = fs.createFile( imageBytes );
        in.save();
//        GridFSDBFile imageForOutput = fs.find((ObjectId) in.getId());
//        imageForOutput.writeTo("\\\\cdlhwseqcdb\\C$\\Test.png");
		return in.getId().toString();
	}


//*********************************************
		

}
