package com.adp.autopay.automation.mongodb;

import java.util.Date;

import com.mongodb.BasicDBObject;

public class MongoDBObject {
	
	public static BasicDBObject getSuite(String suiteRunId, String suiteName,Date startDt, Date endDt){
		BasicDBObject object = new BasicDBObject(SuiteRun.SuiteRunId, suiteRunId)
								.append(SuiteRun.SuiteName, suiteName)
								.append(SuiteRun.StartDt, startDt);
		if(endDt != null){
			object.append(SuiteRun.EndDt, endDt);
		}
		return object;
	}

	public static BasicDBObject getTest(String suiteRunId, String testRunId, String testName, String RunAgainst,String browser,String version,Date startDt, Date endDt, String MachineIP){
		BasicDBObject object = new BasicDBObject(TestRun.TestRunId, testRunId)
								.append(TestRun.TestName, testName)
								.append(TestRun.StartDt, startDt)
								.append(TestRun.SuiteRunId, suiteRunId)
								.append(TestRun.Environment, RunAgainst)
								.append(TestRun.Browser,browser)
								.append(TestRun.Version,version)
								.append(TestRun.MachineIP,MachineIP);
								
		if(endDt != null){
			object.append(TestRun.EndDt, endDt);
		}
		if(MachineIP != null){
			object.append(TestRun.MachineIP, MachineIP);
		}
		if(version != null){
			object.append(TestRun.Version, version);
		}
		return object;
	}
	
	public static BasicDBObject getResult(String testRunId, String scenarioId, String scenarioName, String resultId,String testCase, String description, String actual, String expected, byte status, String imageId){
		BasicDBObject object = new BasicDBObject(Result.ResultId, resultId)
									.append(Result.TestRunId, testRunId)
									.append(Result.Testcase, testCase)
									.append(Result.Description, description)									
									.append(Result.Actual, actual)
									.append(Result.Expected, expected)
									.append(Result.ResultDt, new Date())
									.append(Result.Status, status)
									.append(Result.ScenarioId, scenarioId)
									.append(Result.ScenarioName, scenarioName);
		if(imageId != null){
			object.append(Result.ImageId, imageId);
		}
		return object;
	}

	public static BasicDBObject findSuite(String suiteRunId){
		return new BasicDBObject(SuiteRun.SuiteRunId, suiteRunId);
	}
	
	public static BasicDBObject findTest(String testRunId){
		return new BasicDBObject(TestRun.TestRunId, testRunId);
	}
	
	public static void appendDate(BasicDBObject document, String key){
		document.append(key, new Date());
	}	
	
	public static void appendMachineIP(BasicDBObject document, String MachineIP){
		document.append(TestRun.MachineIP, MachineIP);
	}
	
	public static void appendversion(BasicDBObject document, String version){
		document.append(TestRun.Version, version);
	}
	
	public static BasicDBObject getTestInSuite(String testRunId){
		return new BasicDBObject(TestRun.TestRunId, testRunId);
	}
}
