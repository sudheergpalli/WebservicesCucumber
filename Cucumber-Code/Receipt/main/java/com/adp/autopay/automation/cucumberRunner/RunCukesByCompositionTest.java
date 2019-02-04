package com.adp.autopay.automation.cucumberRunner;

import com.adp.autopay.automation.commonlibrary.DataHelper;
import com.adp.autopay.automation.mongodb.MongoDB;
import com.adp.autopay.automation.utility.*;
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
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * An example of using TestNG when the test class does not inherit from
 * AbstractTestNGCucumberTests.
 */
//@CucumberOptions(plugin = {"html:target/cucumber-html-report"},features={"src/main/java/com/adp/aca/automation/features/production/US125150_TC97499_Home_RequireInformation.feature"},
//				glue = {"com.adp.aca.automation.teststeps","com.adp.aca.automation.cucumberRunner"})
public class RunCukesByCompositionTest extends RunCukes{

	public static String SuiteName;
	public ExecutionContext context;
	public String FeatureName;
	private MongoDB mongoDB;
	public String strTestRunId;
	public ITestContext TestContext;
	public String SRunAgainst;
	public String MachineIP;
	public String browserVersion;
	public static String SuiteRunId;
	public static String mailList="NoMailList";

	public List<HashMap<String,String>> datamap;
	//public List<String> ignoreTags ;

    /* ********************************** Before Suite ********************************* */

	@BeforeSuite
	public void beforeSuite(ITestContext istx) throws Exception {
		try {
			System.out.println(istx);
			istx.getSuite().getParallel();
			SuiteContext.SuiteRunId = GenerateKey.GenerateExecutionId("SUITE");
			SuiteContext.StartTime = new Date(System.currentTimeMillis());
			SuiteRunId = SuiteContext.SuiteRunId;
			SuiteContext.SuiteName = istx.getSuite().getName();
			SuiteName = istx.getSuite().getName();
			istx.getSuite().getParameter("mailList");
			MongoDB mongoDB = new MongoDB();
			mongoDB.connectMongoDB();
			mongoDB.startSuiteExecution(SuiteContext.SuiteRunId, SuiteName);
			mongoDB.disconnectMongoDB();
			System.out.println("Before Suite");
		} catch (Exception e) {
			System.out.println("");
		}
	}

    /* *********************************** Before Test ***************************** */

	@Parameters({"FeatureName","RunAgainst","Client","platform","browser","version"})
	@BeforeTest
	public void beforeTest(ITestContext ctx,String FeatureName,String RunAgainst,String Client,String platform, String browser, String version) throws Exception {
		try {
			SuiteName = ctx.getSuite().getName();
			System.out.println("Before Test Method");
			ObjectContainer.addClass(ExecutionContext.class);
			this.context = ObjectContainer.getInstance(ExecutionContext.class);
			Date date = new Date(System.currentTimeMillis());
			this.context.StartTime = date;
			this.context.SuiteRunId = this.SuiteRunId;
			this.context.SuiteName = SuiteName;
			MongoDB mongoDB = new MongoDB();
			mongoDB.connectMongoDB();

			//Configuration starts Here

			SRunAgainst = ctx.getCurrentXmlTest().getParameter("RunAgainst");
			if(mailList=="NoMailList" || mailList==null||mailList=="")
				mailList=ctx.getCurrentXmlTest().getParameter("mailList");
			this.context.FeatureName = ctx.getCurrentXmlTest().getParameter("FeatureName");
			this.context.browser = ctx.getCurrentXmlTest().getParameter("browser");
			this.context.platform = ctx.getCurrentXmlTest().getParameter("platform");
			this.context.version = ctx.getCurrentXmlTest().getParameter("version");
			this.context.InputDataFilePath = ctx.getCurrentXmlTest().getParameter("InputDataFilePath");
			this.context.ServicesDataFilePath = ctx.getCurrentXmlTest().getParameter("ServicesDataFilePath");
			//this.context.InputDataSheet = ctx.getCurrentXmlTest().getParameter("InputDataSheet");
			this.context.ServicesDataSheet = ctx.getCurrentXmlTest().getParameter("ServicesDataSheet");
            //this.context.ignoreTags=ignoreTags;
			//this.context.datamap = DataHelper.data(System.getProperty("user.dir") + "//src//test//resources//testData/TestDataInput_Greenbox.xlsx",ctx.getCurrentXmlTest().getParameter("InputDataSheet"));
			//datamap = DataHelper.data(System.getProperty("user.dir") + "//src//test//resources//testData/default.xlsx", "Sheet1");

			String runHost=PropertiesFile.RunningHost;


			this.mongoDB = new MongoDB();
			this.mongoDB.connectMongoDB();
			this.context.mongoDB = this.mongoDB;

			this.context.STestName = ctx.getCurrentXmlTest().getName();
			System.out.println("Executing Feature File: "+this.context.STestName);
			System.out.println("Executing Feature file from : "+this.context.FeatureName);

			if(runHost.equalsIgnoreCase("local")){
				if(browser.equalsIgnoreCase("IE")){
					DesiredCapabilities d = DesiredCapabilities.internetExplorer();
					d.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
					d.setCapability("nativeEvents", false);
					d.setCapability("INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS",true);
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
				this.context.MachineIP =MachineIP;
				this.context.browserVersion = browserVersion;
				// this.driver.get(PropertiesFile.HubURL);
			}
			else{
				DesiredCapabilities caps = new DesiredCapabilities();
				//Browsers
				if(browser.equalsIgnoreCase("IE")){
					System.setProperty("webdriver.ie.driver", "CustomizedJar/IEDriverServer.exe");
					caps = DesiredCapabilities.internetExplorer();
					caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
					caps.setBrowserName("internet explorer");
					caps.setCapability("nativeEvents", false);
					caps.setCapability("INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS",true);
//					caps.setCapability("");
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
				if(platform.equalsIgnoreCase("XP"))
					caps.setPlatform(Platform.XP);
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


			this.context.SRunAgainst = SRunAgainst;
			this.strTestRunId = GenerateKey.GenerateExecutionId(this.context.STestName.replace(' ', '_'));
			this.context.strTestRunId = this.strTestRunId;
			this.mongoDB.startTestExecution(SuiteContext.SuiteRunId, strTestRunId, this.context.STestName , RunAgainst,browser, version);
			this.mongoDB.updateTestExecution(this.strTestRunId, this.context.browserVersion, this.context.MachineIP);
		}catch (Exception e){
			System.out.println("Before Test: Exception Occured while executing feature :"+ctx.getCurrentXmlTest().getParameter(FeatureName));
			e.printStackTrace();
		}
	}


	/* ************************************* Cucumber Test Method ******************** */

	/**
	 * Create one test method that will be invoked by TestNG and invoke the
	 * Cucumber runner within that method.
	 * @throws Throwable
	 */
	@Test(groups = "examples-testng", description = "Example of using TestNGCucumberRunner to invoke Cucumber")
	public void runCukes(ITestContext istx) throws Throwable
	{
		System.out.println(Thread.currentThread().getId());
		super.RunningOfCukes(OptionsSpecification(istx));
	}

	public String[] OptionsSpecification(ITestContext istx){
		String[] Options_Runtime = new String[7];
		String ProjectPath = PropertiesFile.GetProjectPath(RunCukesByCompositionTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		//Options_Runtime[0] = ProjectPath+"src/main/resources/com/adp/autopay/automation/features/"+istx.getCurrentXmlTest().getParameter("FeatureName");
		Options_Runtime[0] = ProjectPath+"src/test/java/automation/features/"+istx.getCurrentXmlTest().getParameter("FeatureName");
		Options_Runtime[1] = "--glue";
		Options_Runtime[2] = "com/adp/autopay/automation/teststeps";
		Options_Runtime[3] = "--glue";
		Options_Runtime[4] = "com/adp/autopay/automation/cucumberRunner";
		Options_Runtime[5] = "--tags";
		Options_Runtime[6] = istx.getCurrentXmlTest().getParameter("Tags"); //@Ignore,@smoke
		return Options_Runtime;
	}


	/* *********************************** After Test ***************************** */
	@AfterTest
	public void afterTest(ITestContext ctx) {
		try {
			System.out.println("After Test Method");
			this.context = ObjectContainer.getInstance(ExecutionContext.class);
			if (!(SuiteContext.SuiteName.equalsIgnoreCase("CommandLineExecutions"))) {
				this.context.driver.quit();;
				this.context.completeTestExecution();
				this.context.disconnect();
				ObjectContainer.removeClass(ExecutionContext.class);
				File file = new File("C:/Temp/PerformanceSheet_" + ctx.getCurrentXmlTest().getName() + ".xls");
				if (file.exists()) {
					Date now = Calendar.getInstance().getTime();
					DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
					String theDate = df.format(now);
					String FileName = "PerformanceSheet_" + ctx.getCurrentXmlTest().getName() + theDate + ".xls";
					File file2 = new File("C:/Temp/" + FileName);
					boolean success = file.renameTo(file2);
					if (success) {
						System.out.println("Performance Excel sheet generated: " + file2);
//				try {
//					File file_Remote = new File("//cdlnetappcon2/hwseshare/other/"+FileName);
//				    FileUtils.copyFile(file2,file_Remote);
//				} catch (IOException e) {
//				    e.printStackTrace();
//				}
					}
				}
			} else {
				ObjectContainer.removeClass(ExecutionContext.class);
			}

		}catch (Exception e){
			ObjectContainer.removeClass(ExecutionContext.class);
		}
	}

	/* *********************************** After Scenario ***************************** */

	@AfterSuite
	public void afterSuite(){
		System.err.println("I am in After Suite Method");
		MongoDB mongoDB = new MongoDB();
		mongoDB.connectMongoDB();
		mongoDB.endSuiteExecution(SuiteContext.SuiteRunId);
		mongoDB.disconnectMongoDB();
		if(mailList=="NoMailList" || mailList==null||mailList=="")
			Mail.SendEmail(PropertiesFile.To, PropertiesFile.From, PropertiesFile.CC, SuiteContext.SuiteRunId);
		else
			Mail.SendEmail(mailList,PropertiesFile.From,PropertiesFile.CC,SuiteContext.SuiteRunId);
	}

    /* ************************************ Cucumber Configuration ********************************** */

}
