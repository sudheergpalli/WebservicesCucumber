package com.adp.autopay.automation.utility;

import com.adp.autopay.automation.mongodb.MongoDB;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExecutionContext{
	public WebDriver driver;
	public static String SRunAgainst;
	public String SuiteRunId;
	public MongoDB mongoDB;
	public String MachineIP;
	public String browserVersion;
	public String platform;
	public String version;
	public String browser;
	public String strScenarioRunId;
	public String strScenarioName;
	public String strTestRunId;
	public String STestName;
	public String FeatureName;
	public String SuiteName;
	public Date StartTime;
	public int ScenarioNumber = 0;
	public String outputXmlFileName="";
	public List<HashMap<String,List<String>>> datamap;
	public String InputDataFilePath;
	public String InputDataSheet;
	public String ServicesDataFilePath;
	public String ServicesDataSheet;
	public List<String> ignoreTags = new ArrayList<String>();

	public ExecutionContext() throws MalformedURLException, UnknownHostException{
		try{
			System.out.println(" ***** Context Intialization with the Empty Values ***** ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void closeDriver(){
		this.driver.close();
		this.driver.quit();
	}

	public void disconnect(){
		this.mongoDB.disconnectMongoDB();
	}

	public void completeTestExecution(){
		this.mongoDB.endTestExecution(this.strTestRunId);
	}

	public void addResult(String testcase, String desc, String expected, String actual,  byte status){
		System.out.println("*** Test Step PASSED: "+testcase);
		this.mongoDB.addResultToTest(this.strTestRunId, this.strScenarioRunId,GenerateKey.GenerateExecutionId("RESULT"),this.strScenarioName,
				testcase, desc, expected, actual,  status, null);
	}

	public void addResult(String testcase, String desc, int expected, int actual,  byte status){
		System.out.println("*** Test Step PASSED: "+testcase);
		this.mongoDB.addResultToTest(this.strTestRunId,this.strScenarioRunId, GenerateKey.GenerateExecutionId("RESULT"),this.strScenarioName,
				testcase, desc,Integer.toString(expected),Integer.toString(actual),  status, null);
	}

	public void addResult(String testcase, String desc, boolean expected, boolean actual,  byte status){
		System.out.println("*** Test Step PASSED: "+testcase);
		this.mongoDB.addResultToTest(this.strTestRunId,this.strScenarioRunId, GenerateKey.GenerateExecutionId("RESULT"), this.strScenarioName,
				testcase, desc, expected ? "true" : "false", actual ? "true" : "false", status, null);
	}

	public void addResult(String testcase, String desc,  String expected, String actual, byte status, String screenshotFileName){
		System.err.println("*** Test Step FAILED: "+testcase);
		if(!screenshotFileName.equalsIgnoreCase(""))
			this.mongoDB.addResultToTest(this.strTestRunId, this.strScenarioRunId,GenerateKey.GenerateExecutionId("RESULT"), this.strScenarioName,testcase, desc, expected, actual,  status, screenshotFileName);
	}

	public void addResult(String testcase, String desc,  int expected, int actual, byte status, String screenshotFileName){
		System.err.println("*** Test Step FAILED: "+testcase);
		this.mongoDB.addResultToTest(this.strTestRunId,this.strScenarioRunId, GenerateKey.GenerateExecutionId("RESULT"), this.strScenarioName,
				testcase, desc, Integer.toString(expected),  Integer.toString(actual),status, screenshotFileName);
	}

	public void addResult(String testcase, String desc, boolean expected, boolean actual,  byte status, String screenshotFileName){
		System.err.println("*** Test Step FAILED: "+testcase);
		this.mongoDB.addResultToTest(this.strTestRunId,this.strScenarioRunId, GenerateKey.GenerateExecutionId("RESULT"), this.strScenarioName,
				testcase, desc, expected ? "true" : "false", actual ? "true" : "false",status, screenshotFileName);
	}



}

