package com.adp.autopay.automation.cucumberRunner;

import com.adp.autopay.automation.mongodb.MongoDB;
import com.adp.autopay.automation.utility.*;
import cucumber.api.Feature;
import cucumber.api.Scenario;
import cucumber.api.java.*;
import cucumber.runtime.java.ObjectContainer;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FeatureSetup {

	ExecutionContext context;
	public String TestName;	
	public String Environment;
	public String strTestRunId;
	private MongoDB mongoDB;
	public String strScenarioRunId;
	public String strScenarioName;
	public long scenarioStartTime;
	public static String SuiteName;
	public static String SuiteRunId;
	public String SRunAgainst;
	public String MachineIP;
	public String browserVersion;
	public String browser;
	public String platform;
	public String version;
	
	
	/* ************************** Before Suite _Cucumber ****************** */
	@BeforeSuite
	public void beforeSuite_Cucumber() throws Exception{
	try{
	if(SuiteContext.SuiteRunId == null){
		SuiteContext.SuiteRunId = GenerateKey.GenerateExecutionId("SUITE");
		SuiteContext.SuiteName = "TestSuite_IndividualTestExecution";
		SuiteContext.StartTime = new Date(System.currentTimeMillis());
		SuiteName = "TestSuite_IndividualTestExecution";
		ObjectContainer.addClass(ExecutionContext.class);
		this.context = ObjectContainer.getInstance(ExecutionContext.class);
		this.context.StartTime = SuiteContext.StartTime;
		this.context.SuiteName = SuiteContext.SuiteName;
		MongoDB mongoDB = new MongoDB();
		mongoDB.connectMongoDB();
		mongoDB.startSuiteExecution(SuiteContext.SuiteRunId, SuiteName);	
		mongoDB.disconnectMongoDB();
		System.out.println("Before Suite - cucumber");
		}
	}catch(Exception e){
		System.out.println("Before Suite_Cucumber: Exception Occured while Executing Feature");
	}
	}
	
	/* *************************** BeforeFeature  ************************  */
	
	@BeforeFeature
	public void beforeTest_Cucumber(Feature feature){
	try{
	this.context = ObjectContainer.getInstance(ExecutionContext.class);
	if((this.context.SuiteName.equalsIgnoreCase("TestSuite_IndividualTestExecution") || this.context.SuiteName.equalsIgnoreCase("CommandLineExecutions"))){	
		System.out.println("Before Feature - cucumber");
		
	//Configuration starts Here
		
		SRunAgainst = PropertiesFile.GetEnvironmentProperty("RunAgainst");
		browser = PropertiesFile.GetEnvironmentProperty("Browser");
		platform = PropertiesFile.GetEnvironmentProperty("Platform");
		version = PropertiesFile.GetEnvironmentProperty("Version");
		
		String runHost=PropertiesFile.RunningHost;
		
		this.mongoDB = new MongoDB();
		this.mongoDB.connectMongoDB();
		this.context.mongoDB = this.mongoDB;

		// HWSE
		if(SRunAgainst.equalsIgnoreCase("HWSE_DIT"))
			SRunAgainst = "HWSE_DIT";
		if(SRunAgainst.equalsIgnoreCase("HWSE_FIELD"))
			SRunAgainst = "HWSE_FIELD";
		if(SRunAgainst.equalsIgnoreCase("HWSE_FIELD_PLUS_ONE"))
			SRunAgainst = "HWSE_FIELD_PLUS_ONE";
		if(SRunAgainst.equalsIgnoreCase("HWSE_IAT"))
			SRunAgainst = "HWSE_IAT";
		if(SRunAgainst.equalsIgnoreCase("HWSE_PRODUCTION"))
			SRunAgainst = "HWSE_PRODUCTION";

		//Vantage
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_DIT1"))
			SRunAgainst = "VANTAGE_DIT1";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_DIT2"))
			SRunAgainst = "VANTAGE_DIT2";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_DIT3"))
			SRunAgainst = "VANTAGE_DIT3";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_DIT4"))
			SRunAgainst = "VANTAGE_DIT4";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_FIT1"))
			SRunAgainst = "VANTAGE_FIT1";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_FIT2"))
			SRunAgainst = "VANTAGE_FIT2";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_FIT3"))
			SRunAgainst = "VANTAGE_FIT3";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_FIT4"))
			SRunAgainst = "VANTAGE_FIT4";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_IAT_POD1"))
			SRunAgainst = "VANTAGE_IAT_POD1";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_IAT_POD2"))
			SRunAgainst = "VANTAGE_IAT_POD2";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_UAT_POD1"))
			SRunAgainst = "VANTAGE_UAT_POD1";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_UAT_POD2"))
			SRunAgainst = "VANTAGE_UAT_POD2";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_PRODUCTION_POD1"))
			SRunAgainst = "VANTAGE_PRODUCTION_POD1";
		if(SRunAgainst.equalsIgnoreCase("VANTAGE_PRODUCTION_POD2"))
			SRunAgainst = "VANTAGE_PRODUCTION_POD2";

		if(runHost.equalsIgnoreCase("local")){
			if(browser.equalsIgnoreCase("IE")){
				DesiredCapabilities d = DesiredCapabilities.internetExplorer();
				d.setCapability("nativeEvents", false);
				System.setProperty("webdriver.ie.driver", "CustomizedJar/IEDriverServer.exe");
				WebDriver driver = new InternetExplorerDriver(d);
				this.context.driver = driver;
				this.context.driver.manage().window().maximize();
			}
			if(browser.equalsIgnoreCase("Firefox")){
				System.setProperty("webdriver.firefox.profile", "default");
				WebDriver driver = new FirefoxDriver();
				this.context.driver = driver;
				this.context.driver.manage().window().maximize();
			}
			if(browser.equalsIgnoreCase("Chrome")){
				System.setProperty("webdriver.chrome.driver", "CustomizedJar/chromedriver.exe");
				WebDriver driver = new ChromeDriver();
				this.context.driver = driver;
				this.context.driver.manage().window().maximize();
			}
			this.context.SRunAgainst = SRunAgainst;
			InetAddress IP=InetAddress.getLocalHost();
			MachineIP=IP.getHostAddress();
			//code to identify browser version
			String browserVer =((JavascriptExecutor)this.context.driver).executeScript("return navigator.userAgent;").toString().toLowerCase();
			System.out.println(browserVer.toLowerCase());
			System.out.println(browser);
			//String browser = "Chrome"; // We will pass this value from the TestNG i think , Specify the String (Mozilla / IE /Chrome)
			String browsercase = browser.toLowerCase();
			int start = browserVer.indexOf(browsercase)+browsercase.length()+1;
			int End = start+4;
			browserVersion = browserVer.substring(start, End).trim().replace(";", "");
			System.out.println("Version:"+browserVer.substring(start, End).trim().replace(";", ""));
			System.out.println("IP of local system is := "+MachineIP);
			this.context.MachineIP = MachineIP;
			this.context.browserVersion = browserVersion;
			// this.driver.get(PropertiesFile.HubURL);
		}
		else{
			DesiredCapabilities caps = new DesiredCapabilities();
			//Browsers
			if(browser.equalsIgnoreCase("IE")){
				System.setProperty("webdriver.ie.driver", "CustomizedJar/IEDriverServer.exe");
				caps = DesiredCapabilities.internetExplorer();
				caps.setBrowserName("iexplorer");
			}
			if(browser.equalsIgnoreCase("Firefox")){
				caps = DesiredCapabilities.firefox();
				caps.setBrowserName("firefox");
			}
			if(browser.equalsIgnoreCase("Chrome")){
				System.setProperty("webdriver.chrome.driver", "CustomizedJar/chromedriver.exe");
				caps = DesiredCapabilities.chrome();
			}
			if(browser.equalsIgnoreCase("iPad"))
				caps = DesiredCapabilities.ipad();
			if(browser.equalsIgnoreCase("Android"))
				caps = DesiredCapabilities.android();

			//Platforms
			if(platform.equalsIgnoreCase("Windows"))
				caps.setPlatform(Platform.ANY);
			if(platform.equalsIgnoreCase("MAC"))
				caps.setPlatform(Platform.MAC);
			if(platform.equalsIgnoreCase("Andorid"))
				caps.setPlatform(Platform.ANDROID);
			//Version
			caps.setVersion(version);
			RemoteWebDriver driver = new RemoteWebDriver(new URL(PropertiesFile.HubURL), caps);
			this.context.driver = driver;
			this.context.driver.manage().window().maximize();
			caps.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			Capabilities remotecap= ((RemoteWebDriver)this.context.driver).getCapabilities();
			browserVersion = remotecap.getVersion();
			MachineIP= NodeIPAddress.getIPOfNode((RemoteWebDriver) this.context.driver);
			this.context.MachineIP = MachineIP;
			this.context.browserVersion = browserVersion;
			System.out.println("IP of Remote system is := "+MachineIP);
			System.out.println("Version of Browser is := "+browserVersion);
		}
		this.context.driver.manage().deleteAllCookies();
		//this.driver.get("http://www.google.com");
		this.context.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		//Configuration Ends Here

		this.context.STestName = feature.getName();
		this.strTestRunId = GenerateKey.GenerateExecutionId(this.context.STestName.replace(' ', '_'));
		this.context.strTestRunId = this.strTestRunId;
		this.mongoDB.startTestExecution(SuiteContext.SuiteRunId, strTestRunId, this.context.STestName , SRunAgainst,browser, version);
		this.mongoDB.updateTestExecution(this.strTestRunId, this.context.browserVersion, this.context.MachineIP);
	}
	}catch(Exception e){
		System.out.println("Before Test: Exception Occured while executing feature :"+feature.getName());
		e.printStackTrace();
	}
	}
	
	
	/* *************************** Before - Before Scenario  ************************  */
		
	@Before
	public void beforeScenario(Scenario scenario){
	try{
		this.context = ObjectContainer.getInstance(ExecutionContext.class);
		System.out.println("******************** beforeScenario - " + scenario.getName() + "************************************ ");
		this.context.strScenarioRunId = GenerateKey.GenerateExecutionId("SCENARIO");
		scenarioStartTime = new Date().getTime();
		this.context.strScenarioName = scenario.getName();
//		this.mongoDB = new MongoDB();
//		this.mongoDB.connectMongoDB();
//		this.context.mongoDB = this.mongoDB;
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
	/* *************************** After - After Scenario  ************************  */
		
	@After
	public void afterScenario(Scenario scenario) {
		try {
			this.context = ObjectContainer.getInstance(ExecutionContext.class);
			long sStartTime = scenarioStartTime;
			long now = new Date().getTime();
			long scenarioSeconds = (now - sStartTime) / 1000;
			//this.context.addResult("Scenario Completion Time", "Scenario completed in: " + scenarioSeconds + " sec", "Scenario Execution Time", "Scenario Execution Time", RunStatus.PASS);
			this.context.ScenarioNumber = this.context.ScenarioNumber + 1;
			ScenarioPerformanceExcel.InsertToExcel(this.context.STestName, this.context.strScenarioName, scenarioSeconds, this.context.ScenarioNumber);
			System.out.println("After Scenario: " + scenario.getName() + " Executed Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* *************************** AfterFeature ***********************************  */
	
	@AfterFeature
	public void afterFeature(Feature feature){
	try{
	this.context = ObjectContainer.getInstance(ExecutionContext.class);
	if(this.context.SuiteName.equalsIgnoreCase("TestSuite_IndividualTestExecution")|| this.context.SuiteName.equalsIgnoreCase("CommandLineExecutions")){
		System.out.println("After Feature Method");
		this.context.driver.close();
		this.context.completeTestExecution();
		this.context.disconnect();
	if(this.context.SuiteName.equalsIgnoreCase("TestSuite_IndividualTestExecution")){	
		ObjectContainer.removeClass(ExecutionContext.class);
	}
		System.out.println("&&&&&&&&&&&&&&&&&&&&&& -> afterFeature - Feature - " + feature.getName() +"<- &&&&&&&&&&&&&&&&&&&&&&&&&&&");
		File file = new File("C:/Temp/PerformanceSheet_"+ feature.getName() +".xls");
		if(file.exists()){
			Date now = Calendar.getInstance().getTime();
		    DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		    String theDate = df.format(now);
		    String FileName = "PerformanceSheet_"+ feature.getName() +theDate+".xls";
			File file2 = new File("C:/Temp/"+FileName);
			boolean success = file.renameTo(file2);
			if(success){
				System.out.println("Performance Excel sheet generated: "+file2);
//				try {
//					File file_Remote = new File("//cdlnetappcon2/hwseshare/other/"+FileName);
//				    FileUtils.copyFile(file2,file_Remote);
//				} catch (IOException e) {
//				    e.printStackTrace();
//				}
			}
		}
	  }
		
	}catch(Exception e){
		ObjectContainer.removeClass(ExecutionContext.class);
	}
	}
	
	
	/* ***************************** After Suite ******************************** */
	
	@AfterSuite
	public void afterSuite_Cucumber(){
	try{
	if(SuiteContext.SuiteName.equalsIgnoreCase("TestSuite_IndividualTestExecution")){	
		System.err.println("I am in After Suite of cucumber Method");
		MongoDB mongoDB = new MongoDB();
		mongoDB.connectMongoDB();
		mongoDB.endSuiteExecution(SuiteContext.SuiteRunId);
		mongoDB.disconnectMongoDB();
		Mail.SendEmail(PropertiesFile.To, PropertiesFile.From, PropertiesFile.CC, SuiteContext.SuiteRunId);
		}
	}catch(Exception e){
		System.out.println("");
	}
	}

	
}