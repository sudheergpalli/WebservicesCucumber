/*
 * 
 */
package com.adp.autopay.automation.teststeps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.adp.autopay.automation.pagerepository.PortalPage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Document;

import com.adp.autopay.automation.commonlibrary.CommonFunctions;
import com.adp.autopay.automation.commonlibrary.LoginFunctions;
import com.adp.autopay.automation.commonlibrary.Navigate;
import com.adp.autopay.automation.commonlibrary.PageVerifications;
import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.commonlibrary.WaitingFunctions;
import com.adp.autopay.automation.framework.Button;
import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.framework.Dropdown;
import com.adp.autopay.automation.framework.Element;
import com.adp.autopay.automation.framework.Link;
import com.adp.autopay.automation.framework.RevitTableFunctions;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.Common;

import com.adp.autopay.automation.pagerepository.Login;
import com.adp.autopay.automation.pagespecificlibrary.CommonMethods;
import com.adp.autopay.automation.tabularParameters.Tabular_String;
import com.adp.autopay.automation.tabularParameters.Tabular_StringNString;
import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.PropertiesFile;
import com.adp.autopay.automation.utility.ReadEmails;
import com.adp.autopay.automation.utility.SuiteContext;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.ObjectContainer;
import gherkin.formatter.model.DataTableRow;

public class CommonSteps extends CustomWebElement {

	String PortalUrl, CONFIGUrl, OPSUrl, MOBILEURL, DirectUrl, RevUIUrl, RunAgainst,ACAExportMachineAddress,ACAExportPath,AdminUsername,AdminPassword,ACAExportXMLServerIP;;
	public long scenarioStartTime;
	ExecutionContext context;
	int ScenarioNumber = 0;
	public String ADPAdmin;
	public static Date StartDate;
	public static int emp=0;

	public CommonSteps() {
		this.context = ((ExecutionContext) ObjectContainer.getInstance(ExecutionContext.class));
		PortalUrl = PropertiesFile.GetProperty("PortalUrl");
		DirectUrl = PropertiesFile.GetProperty("DirectUrl");
		RevUIUrl = PropertiesFile.GetProperty("RevUIUrl");
		CONFIGUrl = PropertiesFile.GetProperty("CONFIGUrl");
		OPSUrl = PropertiesFile.GetProperty("OPSUrl");
		MOBILEURL = PropertiesFile.GetProperty("MobileUrl");
		RunAgainst= PropertiesFile.GetEnvironment("RunAgainst");
		ACAExportMachineAddress = PropertiesFile.GetProperty("ACAExportMachineAddress");
		ACAExportPath = PropertiesFile.GetProperty("ACAExportPath");
		AdminUsername = PropertiesFile.GetProperty("AdminUsername");
		AdminPassword = PropertiesFile.GetProperty("AdminPassword");
		ACAExportXMLServerIP = PropertiesFile.GetProperty("ACAExportXMLServerIP");

	}

	//	@BeforeScenario
	//	public void beforeScenario(){
	//		this.context.strScenarioRunId = GenerateKey.GenerateExecutionId("SCE");
	//		scejava.lang.ObjectnarioStartTime = new Date().getTime();
	////Scenario Name code
	//		Scenario scenario=new Scenario();
	//		ArrayList<String> ScenarioName = scenario.GetScenarioFromStoryFile(RunStory.strStoryPath);
	//		this.context.strScenarioName = scenario.GetScenario(ScenarioName, ScenarioNumber);
	//		
	////Insert Text in to Excel for performance
	//		
	//	}
	//	
	//	@AfterScenario
	//	public void afterScenario(){
	//		long sStartTime=scenarioStartTime;
	//		long now = new Date().getTime();
	//		long scenarioSeconds = (now - sStartTime) / 1000;
	//		this.context.addResult("Scenario Completion Time", "Scenario completed in: "+ scenarioSeconds +" sec", "", "", RunStatus.PASS);
	////Scenario Name code
	//		ScenarioNumber = ScenarioNumber + 1;
	//		
	////Insert Text in to Excel for performance
	//		ScenarioPerformanceExcel.InsertToExcel(this.context.TestName,this.context.strScenarioName, scenarioSeconds, ScenarioNumber);
	//	}



	/**
	 * This method will navigate to the URL in the browser.
	 * @param - aplication name : values - CONFIGUrl,OPSUrl,PortalUrl, DirectUrl, RevUIUrl  
	 */
	/*Provide URL in properties file*/
	@Given("COMMON:I navigate to benefits \"(.*)\" URL")
	public void getBenefitsUrl(String application) throws Exception {
		String runAgainst= PropertiesFile.GetEnvironment("RunAgainst");
		try {
			application=PropertiesFile.GetProperty(application);
			context.driver.manage().deleteAllCookies();
			context.driver.get(application);
			this.context.addResult("Navigate to "+runAgainst+" application", "User should navigate to "+runAgainst+" application: "+application, "User should navigate to Portal login Page: "+application , "User navigate to Portal login Page: "+application, RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Navigate to "+runAgainst+" application", "User should navigate to "+runAgainst+" application: "+application, "User should navigate to Portal login Page: "+application , "Exception Occured", RunStatus.FAIL, Screenshot.getSnap(context, "GetUrl"));
			e.printStackTrace();
		}
	}


	@When("^COMMON:I Select \"(.*)\" click on submit and HWSE application$")
	public void selectNavigations(String Rev) {
		try{
			Select select = new Select(context.driver.findElement(By.xpath(".//*[@id='frmCookie']/select")));
			select.selectByVisibleText(Rev);

			FindElement(context, By.xpath(".//form[@id='frmCookie']/input")).click();
			FindElement(context, By.xpath(".//a[@title='HWSE Application']")).click();
			this.context.addResult("navigate to UI", " Navigating to Portal Rev UI ","has to be navigated to HWSE Application ","Navigated to HWSE Application Successfully",RunStatus.PASS);			


		}catch(Exception e){
			e.printStackTrace();
			this.context.addResult("unable to navigate" , "Unable to navigate to Rev UI ", "has to be navigated to HWSE Application", "Rev UI HWSE Application not found", RunStatus.FAIL, Screenshot.getSnap(context, "ButtonClick"));

		}
	}



	@Given("^BENEFITS \"(.*)\" URL from properties file with \"(.*)\" and password \"(.*)\"$")
	public void getBenefitsUrl(String URLType, String username, String Password) throws Exception {
		try {
			if (username.equalsIgnoreCase("employee")) {
				username = PropertiesFile.GetProperty("Employee");
			}
			String url = null;
			if (URLType.toLowerCase().contains("Direct".toLowerCase())) {
				url = DirectUrl;
			}
			System.out.println("Login Initiated:");
			Runtime.getRuntime().exec("taskkill /f /s "+context.MachineIP+" /u Es\\hwseauto /p adpADP123 /im cmd.exe");

			if(emp==0)
			{
				Process copyProcess=Runtime.getRuntime().exec("\\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -u ES\\hwseauto -p adpADP123 XCOPY \\\\cdlbenefitsqcre\\base\\Scripts\\BenefitsAutomation\\AutoIt C:\\Temp /Y");
				copyProcess.waitFor();
				emp++;
			}
			Process executeProcess = null;

			if(PropertiesFile.RunningHost.equalsIgnoreCase("Local"))
			{  
				System.out.println("Local PSexec");

				if(context.browser.equalsIgnoreCase("IE"))
				{
					executeProcess = Runtime.getRuntime().exec("cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\AuthenticationPopup.exe " + "Windows" +" "+username +" "+ Password);
				}
				else if(context.browser.equalsIgnoreCase("Chrome"))
				{
					executeProcess = Runtime.getRuntime().exec("cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\AuthenticationPopup.exe " + "https" +" "+username +" "+ Password);	
				}
				else if(context.browser.equalsIgnoreCase("Firefox"))
				{
					executeProcess = Runtime.getRuntime().exec("cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\AuthenticationPopup.exe " + "Authentication" +" "+username +" "+ Password);
				}
			}

			if(PropertiesFile.RunningHost.equalsIgnoreCase("Remote"))
			{ 
				System.out.println("Remote PSexec");

				if(context.browser.equalsIgnoreCase("IE"))
				{
					String command  = "cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\AuthenticationPopup.exe " + "Windows" +" "+username +" "+ Password ;

					System.out.println(command);

					executeProcess = Runtime.getRuntime().exec("cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\AuthenticationPopup.exe " + "Windows" +" "+username +" "+ Password);
				}
				else if(context.browser.equalsIgnoreCase("Chrome"))
				{
					executeProcess = Runtime.getRuntime().exec("cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\AuthenticationPopup.exe " + "https" +" "+username +" "+ Password);	
				}
				else if(context.browser.equalsIgnoreCase("Firefox"))
				{
					executeProcess = Runtime.getRuntime().exec("cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\AuthenticationPopup.exe " + "Authentication" +" "+username +" "+ Password);
				}
			}
			System.out.println("invoke URL");
			context.driver.get(url);
			Thread.sleep(5000);
			executeProcess.waitFor();
			Runtime.getRuntime().exec("taskkill /f /s "+context.MachineIP+" /u Es\\hwseauto /p adpADP123 /im AuthenticationPopup.exe");  
			By remindBy=By.xpath("//a[@class='ADPUI-hlink1' and text()='Remind me later']");
			context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
			if(Element.IsPresent(context, remindBy))
			{
				context.driver.findElement(remindBy).click();
			}
			context.driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS); //Reset wait
			this.context.addResult("Login To the Portal : Given HWSE Url", "It will open the browser and load the page with HWSEUrl and Login: " + username + " and " + Password, "Portal login Page", "Portal login Page", RunStatus.PASS);

		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Login To the Portal : Given HWSE Url", "It will open the browser and load the page with HWSEUrl and Login: " + username + " and " + Password, "Portal login Page", "Portal login Page Not Found", RunStatus.FAIL, Screenshot.getSnap(context, "GetUrl"));
			e.printStackTrace();
		}
	}




	@When("^I Select \"(.*)\" from the Set Transfer Cookie Page$")
	public void SelectAppUI(String UITypeIndication) {
		try {
			WebElement UItypedropdown = FindElement(context, PortalPage.UIType);
			Select select = new Select(UItypedropdown);
			select.selectByVisibleText(UITypeIndication);
			Button.Click(context, PortalPage.SubmitQuery);
			Thread.sleep(4000);
			Link.click(context, PortalPage.SelectApp);
			this.context.addResult("Select App UI type", "Select UI type as: " + UITypeIndication, "User has to select :" + UITypeIndication, "User selected:" + UITypeIndication, RunStatus.PASS);
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Select App UI type", "Select UI type as: " + UITypeIndication, "User has to select :" + UITypeIndication, "User Unable to Select:" + UITypeIndication, RunStatus.FAIL, Screenshot.getSnap(context, "SelectApp"));
		}
	}

	@Given("^COMMON:I enter \"(.*)\" URL from properties file$")
	@Then("^COMMON:I give \"(.*)\" URL from properties file$")
	@When("^COMMON:I navigate to the application \"(.*)\"$")
	public void getCONFIGUrl(String Application) throws Exception {
		try {
			String url = null;
			if (Application.equalsIgnoreCase("CONFIG")) {
				url = CONFIGUrl;
			}
			if (Application.equalsIgnoreCase("OPS")) {
				url = OPSUrl;
			}
			if (Application.equalsIgnoreCase("MOBILE")) {
				url = MOBILEURL;
			}
			context.driver.get(url);
			this.context.addResult("Login To the "+Application+" Tool ", "It will open the browser and load the page with "+Application+" URL", ""+Application+" login Page", ""+Application+" login Page", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Login To the "+Application+" Tool ", "It will open the browser and load the page with "+Application+" URL", ""+Application+" login Page", "Exception Occured", RunStatus.FAIL, Screenshot.getSnap(context, "GetUrl"));
			e.printStackTrace();
		}
	}

	@When("^CONFIG:I entered \"(.*)\" username and password \"(.*)\"$")
	public void loginToCONFIG(String StandardUser, String password) {
		try {
			String UserName = null;
			if (StandardUser.equalsIgnoreCase("BSI")) {
				UserName = PropertiesFile.GetProperty("BSIUser");
			}
			if (StandardUser.equalsIgnoreCase("BSO")) {
				UserName = PropertiesFile.GetProperty("BSOUser");
			}
			System.out.println("About to enter Arcot Admin Login credentials");
			LoginFunctions.ArcotLogin(context, UserName, password);
			System.out.println("Arcot Admin Login credentials - Successfull");
			this.context.addResult("User Login", "User Login with given username and password", "CONFIG Client Selection Page", "CONFIG Client Selection Page", RunStatus.PASS);
		} catch (Exception e) {
			System.out.println("Arcot Admin Login credentials - Error");
			this.context.addResult("User Login", "User Login with given username and password", "CONFIG Client Selection Page", "Exception Occured", RunStatus.FAIL, Screenshot.getSnap(context, "Test"));
			e.printStackTrace();
		}
	}

	/**
	 * user login : It will complete the ARCOT process with the $username and $password parameters
	 */

	@When("^COMMON:I login as a Practioner with member ID \"(.*)\" and password \"(.*)\"$")
	public void loginToPortalClient1(String MemberID, String Password) {
		LoginFunctions.UserLogin(context, MemberID, Password);
		this.context.addResult("User Login", "User Login with given username and password", "ACA Portal Home Page", "ACA Portal Home Page", RunStatus.PASS);
	}


	/**
	 * user login : It will complete the ARCOT process with the $username and $password parameters
	 */
	@When("^COMMON:I entered username \"(.*)\" and password \"(.*)\"$")
	public void loginToPortalClient(String username, String password) {

		try {
			//		LoginFunctions.UserLogin1(context,username,password);
			LoginFunctions.ArcotLogin(context, username, password);
			this.context.addResult("User Login", "User Login with given username and password", "ACA Portal Home Page", "ACA Portal Home Page", RunStatus.PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Admin login: It will complete the ARCOT process with the $username and $password parameters
	 */

	@When("^I entered admin username \"(.*)\" and password \"(.*)\"$")
	@Then("^COMMON:I entered admin username \"(.*)\" and password \"(.*)\"$")
	public void loginToPortalAdmin(String username, String password) throws Exception {
		LoginFunctions.AdminArcotLogin(context, username, password);
		this.context.addResult("Admin Login", "Login to the ACA as Admin", "Client Selection Page", "Client Selection Page", RunStatus.PASS);
	}


	@When("ICT:I entered employee user \"(.*)\" and password \"(.*)\"")
	public void LoginToTheEmployee(String username, String Password) {
		try {
			FindElement(context, By.xpath(".//*[@id='employee']")).click();
			Thread.sleep(3000);

			System.out.println(System.getProperty("user.dir") );

			Process process = new ProcessBuilder(System.getProperty("user.dir") + "/src/test/resources/AutoItScripts/AuthenticationPopup.exe", "Authentication Required", username, Password).start();
			Thread.sleep(5000);
			WaitingFunctions.WaitUntilPageLoads(context);
			WaitingFunctions.waitForElement(context, By.xpath(".//*[@id='menuBar']//div[@id='menuHit.node2']"), 10);
			this.context.addResult("User Login", "User Login for the Employee User: " + username, "User login has to be successful", "user login sucess", RunStatus.PASS);
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("User Login", "User Login for the Employee User: " + username, "User login has to be successful", "user login Failed", RunStatus.FAIL, Screenshot.getSnap(context, "Login"));
		}
	}





	@When("^Select the client \"(.*)\"$")
	@Then("^COMMON:Select the client \"(.*)\"$")
	public void selectClient(String Client){
		try{
			if (Client.contains("<")) {
				Client = PropertiesFile.GetProperty(Client.replace("<", "").replace(">", "").trim());
			}
			//			if(Element.IsPresent_linkText(context, "Return to ADP Support")){
			//				Element.Click(context, By.linkText("Return to ADP Support"));
			//				WaitingFunctions.waitForElement(context, LoginPage.ClientWebEdit, 15);
			//			}
			if(context.driver.findElement(Login.ClientWebEdit).isDisplayed()){
				LoginFunctions.selectClient(context, Client);
				//				this.context.driver.findElement(By.xpath(".//*[@id='clientDropDown']")).click();
				//				this.context.driver.findElement(By.xpath(".//*[@id='clientDropDown']")).clear();
				//				this.context.driver.findElement(By.xpath(".//*[@id='clientDropDown']")).sendKeys(Client);
				//				Button.Click(context, LoginPage.ChangeClientButton);
				//				WaitingFunctions.WaitUntilPageLoads(context);

				//				WaitingFunctions.waitForAnElement(context, Home_UserHome.HomePage);
				//				if(Dropdown.isPresent(context, LoginPage.SelectClient)){
				//					Dropdown.Select(context, LoginPage.SelectClient, Client);
				//					Button.Click(context, LoginPage.ChangeClientButton);
				//			}
				if(context.driver.findElement(Login.Shell).isDisplayed()){
					this.context.addResult("Client Selection", "Selecting the client", "Client Selection should be Successful", "Client Selected Successfully:"+Client,  RunStatus.PASS);
				}
				else{
					this.context.addResult("Client Selection", "Selecting the client","Client Selection should be Successful", "Client Selection Failed "+Client , RunStatus.FAIL,Screenshot.getSnap(context, "SelectClient"));
				}
			}
		}catch(StaleElementReferenceException e){
			this.context.addResult("Client Selection", "Selecting the client", "Client Selection should be Successful", "Client Selection Failed " + Client, RunStatus.FAIL, Screenshot.getSnap(context, "SelectClient"));
			e.printStackTrace();
		}
	}


	/**
	 * It is the common method which is used for Navigating to the OBA pages
	 * here we will navigate to the pages by specifying "Menu"
	 */
	@When("^Navigate To \"(.*)\"$")
	@Then("^COMMON:user navigates to \"(.*)\"$")
	public void NavigateToPage(String Menu) {
		try {
			if (Navigate.NavigateToMenu(context, Menu)) ;
			WaitingFunctions.WaitUntilPageLoads(context);
			if (PageVerifications.VerifyPage(context, Menu)) {
				this.context.addResult("Navigate to: " + Menu, "Navigation Step:" + Menu, "User should Navigate to: " + Menu, "User Navigated To: " + Menu, RunStatus.PASS);
			}
		} catch (Exception e) {
			this.context.addResult("Navigate to: " + Menu, "Navigation Step:" + Menu, "User should Navigate to: " + Menu, "Navigation Failed to: " + Menu, RunStatus.FAIL, Screenshot.getSnap(context, Menu));
			e.printStackTrace();
		}
	}


	@And("COMMON:I have to verify the reports page got Loaded")
	public void VerifyGenerate_ViewReportspage() {
		try {

			if (FindElements(context, PortalPage.ReportsSelection).size()>0) {
				System.out.println("*** Page Loaded Successfully");
				this.context.addResult("Verify Page", "Verify Page got Loaded", "User should be in the page", "User is in the page", RunStatus.PASS);
			} else {
				this.context.addResult("page Loading", "Verify Page got Loaded", "User should be in the page", "Page not found", RunStatus.FAIL, Screenshot.getSnap(context, "PageLoading"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("page Loading", "Verify Page got Loaded", "User should be in the page", "Exception Occured", RunStatus.FAIL, Screenshot.getSnap(context, "PageLoading"));
		}
	}

	@Then("I Logoff from ESS application")
	public void Logoff() {
		try {
			Element.Click(context, By.xpath(".//*[@id='masthead']//*[contains(text(),'Log')]"));
			Thread.sleep(2000);
			Element.Click(context, By.xpath(".//*[text()='OK']"));
			Thread.sleep(6000);
			this.context.driver.manage().deleteAllCookies();
			Thread.sleep(4000);
			System.out.println("*** Application Closed");
			this.context.driver.manage().deleteAllCookies();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * It is the common method which is used for Navigating to the ACA pages
	 * here we will navigate to the pages by specifying "MegaMenu" and "SubMenu" items.
	 */
	@When("^COMMON:Navigate To \"(.*)\"->\"(.*)\"->\"(.*)\" Page$")
	@Given("^COMMON:I Navigate to \"(.*)\"->\"(.*)\"->\"(.*)\" Page$")
	public void NavigateToPage(String MegaMenu, String SubMenu, String MenuItem) {
		try {
			if (Navigate.NavigateTo(context, MegaMenu, SubMenu, MenuItem)) {
				WaitingFunctions.WaitUntilPageLoads(context);
				if (PageVerifications.VerifyPage(context, MenuItem)) {
					this.context.addResult("Navigate to: " + MegaMenu + "->" + SubMenu + "->" + MenuItem, "Navigation Step:" + MegaMenu + " -> " + SubMenu + "->" + MenuItem, "User should Navigate to: ->" + MenuItem, "User Navigated To: " + MenuItem, RunStatus.PASS);
				} else {
					this.context.addResult("Navigate to: " + MegaMenu + "->" + SubMenu + "->" + MenuItem, "Navigation Step:" + MegaMenu + " -> " + SubMenu + "->" + MenuItem, "User should Navigate to: " + MenuItem, "Navigation Failed: " + MegaMenu + "->" + SubMenu + "->" + MenuItem, RunStatus.FAIL, Screenshot.getSnap(context, SubMenu));
				}
			} else {
				this.context.addResult("Navigate to: " + MegaMenu + "->" + SubMenu + "->" + MenuItem, "Navigation Step:" + MegaMenu + " -> " + SubMenu + "->" + MenuItem, "User should Navigate to: " + MenuItem, "Navigation Failed: " + MegaMenu + "->" + SubMenu + "->" + MenuItem, RunStatus.FAIL, Screenshot.getSnap(context, SubMenu));
			}
		} catch (Exception e) {
			this.context.addResult("Navigate to: " + MegaMenu + "->" + SubMenu + "->" + MenuItem, "Navigation Step:" + MegaMenu + " -> " + SubMenu + "->" + MenuItem, "User should Navigate to: " + MenuItem, "Navigation Failed: " + MegaMenu + "->" + SubMenu + "->" + MenuItem, RunStatus.FAIL, Screenshot.getSnap(context, SubMenu));
			e.printStackTrace();
		}
	}


	/**
	 * This method is used to search PP with the given value
	 *
	 * @param Field - Field name for which value has to be entered(Must be same as displayed)
	 * @param Value - Value to be entered
	 */
	@When("^COMMON:I search PP with Field \"(.*)\" with value \"(.*)\"$")
	@Then("^I search PP with Field \"(.*)\" with value \"(.*)\"$")
	public void SearchEmployees(String Field, String Value) {
		try {
			if (Field.equals("Status")) {
				CommonFunctions.SelectSearchEmployee(context, Field, Value);
			} else {
				if (Field.equals("First Name")) {
					FindElement(context, By.xpath(".//label[text()='" + Field + " ']/parent::div//input[contains(@class,'dijitReset dijitInputInner')]")).sendKeys(Value);
				} else
					FindElement(context, By.xpath(".//label[text()='" + Field + "']/parent::div//input[contains(@class,'dijitReset dijitInputInner')]")).sendKeys(Value);
			}
			FindElement(context, Common.SearchButton).click();
			WaitingFunctions.WaitUntilPageLoads(context);
			this.context.addResult("Search for employee with " + Field + " as" + Value, "Search for employee with " + Field + " as" + Value, "Search successfull", "Search successfull", RunStatus.PASS);
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Search for employee with " + Field + " as" + Value, "Search for employee with " + Field + " as" + Value, "Search successfull", "Exception occured", RunStatus.FAIL, Screenshot.getSnap(context, "SearchFail"));
		}
	}

	/**
	 * This method is used to search employee with Employee ID
	 *
	 * @param EmployeeID - Employee Id to be searched
	 */

	@When("^COMMON:I select PP with EmployeeID \"(.*)\"$")
	public void SelectPPWithEmployeeID(String EmployeeID) {
		try {
			if (EmployeeID.contains("<")) {
				EmployeeID = PropertiesFile.GetProperty(EmployeeID.replace("<", "").replace(">", "").trim());
			}
			WaitingFunctions.WaitUntilPageLoads(context);
			//CommonFunctions.SelectRowsPerPage(context,"100");
			if (FindElement(context, By.xpath(".//span[text()='" + EmployeeID + "']/ancestor::tr[contains(@id,'tableGrid')]//a")).isDisplayed()) {
				FindElement(context, By.xpath(".//span[text()='" + EmployeeID + "']/ancestor::tr[contains(@id,'tableGrid')]//a")).click();
				this.context.addResult("Select the Employee having ID:" + EmployeeID, "Select the Employee having ID:" + EmployeeID, "Employee should be selected", "Employee Selected successfully", RunStatus.PASS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Select the Employee having ID:" + EmployeeID, "Select the Employee having ID:" + EmployeeID, "Employee should be selected", "Employee is not Selected", RunStatus.FAIL, Screenshot.getSnap(context, "EmployeeSelectionFailed"));
		}
	}


	//org.openqa.selenium.support

	/**
	 * This method is used to verify whether the tab is active or not
	 *
	 * @param ReportsTab - Name of the Tab to be verified
	 */
	@Given("^COMMON:I am in \"(.*)\" tab$")
	public void VerifyTabActive(String ReportsTab) {
		try {
			WebElement element = context.driver.findElement(Common.ActiveTab);
			if (element.isDisplayed()) {
				if (Element.IsPresent(context, By.xpath("//div[@class='revitTab dijitTab dijitTabChecked dijitChecked']//span[text()='" + ReportsTab + "']"))) {
					System.out.println("*** Tab is Active");
					this.context.addResult("Default Selected Tab", "Verify Default Selected Tab", ReportsTab + " is active by default", ReportsTab + " is active by default", RunStatus.PASS);
				} else {
					System.out.println("*** Tab is Not Active");
					this.context.addResult("Default Selected Tab", "Verify Default Selected Tab", ReportsTab + " is active by default", ReportsTab + " is not active by default", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyReportTab"));
				}
			}
		} catch (Exception e) {
			this.context.addResult("Default Selected Tab", "Verify Default Selected Tab", ReportsTab + " is active by default", "Exception While Executing Step", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyReportTab"));
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to verify Tab present or not
	 *
	 * @param TabName - Name of the tab
	 */
	@Then("^COMMON:I Verified \"(.*)\" Tab$")
	public void VerifyTabPresent(String TabName) {
		try {
			if (Element.IsPresent(context, By.xpath(".//span[text()='" + TabName + "']"))) {
				this.context.addResult("Verify tab present", "Verify tab present", "Verify tab present or not", TabName + " is present", RunStatus.PASS);
			} else {
				this.context.addResult("Verify tab present", "Verify tab present", "Verify tab present or not", TabName + " is not present", RunStatus.FAIL, Screenshot.getSnap(context, "TabNotPresent"));
			}
		} catch (Exception e) {
			this.context.addResult("Verify Tab present or not", "Verify Tab present or not", TabName + " is present", "Exception While Executing Step", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyReportTab"));
			e.printStackTrace();
		}

	}


	/**
	 * This method is used to search PP from participants page
	 *
	 * @param EmployeeID - Employee ID to be sarched
	 */
	@When("^COMMON:I search participant with PPID as \"(.*)\" from Participants page$")
	public void PPSearchFromPersonpage(String EmployeeID) {
		try {
			FindElement(context, Common.SearchIcon).click();
			WaitingFunctions.WaitUntilPageLoads(context);
			FindElement(context, By.xpath(".//label[text()='Employee ID']/parent::div//input[@class='dijitReset dijitInputInner']")).sendKeys(EmployeeID);
			FindElement(context, Common.SearchButton).click();
			this.context.addResult("Search for employee with Employee ID as" + EmployeeID, "Search for employee with Employee ID as" + EmployeeID, "Search successfull", "Search successfull", RunStatus.PASS);
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Search for employee with Employee ID as" + EmployeeID, "Search for employee with Employee ID as" + EmployeeID, "Search successfull", "Search unsuccessfull", RunStatus.FAIL, Screenshot.getSnap(context, "SearchFail"));
		}
	}

	/**
	 * This method is used to verify the first row of the central object
	 *
	 * @param TableLabelNames - List of labels in 1st row in Rev_UI
	 */
	@Then("^I should validate all the labels in search grid:$")
	public void RevUI_CentralPaneRowVerify(DataTable TableLabelNames) {

		try {
			WaitingFunctions.WaitUntilLoadcleared(context, Common.RevitLoading);
			List<String> output = new ArrayList<String>();
			for (DataTableRow row : TableLabelNames.getGherkinRows()) {
				String TableHeader = row.getCells().get(0);
				output.add(TableHeader);
			}
			for (int i = 1; i < output.size(); i++) 
			{
				if(output.get(i).equalsIgnoreCase("Search") || output.get(i).equalsIgnoreCase("Reset"))
				{
					if (Element.IsPresent(context, By.xpath("//div[@class='dijitContentPane']//tbody//tr//td//span[contains(.,'"+output.get(i).trim()+"') and @id[contains(.,'label')]]"))) {
						this.context.addResult("Verify label" + output.get(i) + " is present in the Participant Search Grid", "Verify label" + output.get(i) + " is present in the Participant Search Grid", "Label" + output.get(i) + " should be present in the Participant Search Grid", "Label" + output.get(i) + " is be present in the Participant Search Grid", RunStatus.PASS);
					} 
					else
					{
						this.context.addResult("Verify label" + output.get(i) + " is present in the Participant Search Grid", "Verify label" + output.get(i) + " is present in the Participant Search Grid", "Label" + output.get(i) + " should be present in the Participant Search Grid", "Label" + output.get(i) + " is be present in the Participant Search Grid", RunStatus.FAIL, Screenshot.getSnap(context, "labelExistance"));
					}
				}
				else
				{
					if (Element.IsPresent(context, By.xpath("//div[@class='dijitContentPane']//tbody//tr//td[contains(.,'"+output.get(i)+"')]"))) {
						this.context.addResult("Verify label" + output.get(i) + " is present in the Participant Search Grid", "Verify label" + output.get(i) + " is present in the Participant Search Grid", "Label" + output.get(i) + " should be present in the Participant Search Grid", "Label" + output.get(i) + " is be present in the Participant Search Grid", RunStatus.PASS);
					} else
						this.context.addResult("Verify label" + output.get(i) + " is present in the Participant Search Grid", "Verify label" + output.get(i) + " is present in the Participant Search Grid", "Label" + output.get(i) + " should be present in the Participant Search Grid", "Label" + output.get(i) + " is be present in the Participant Search Grid", RunStatus.FAIL, Screenshot.getSnap(context, "labelExistance"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify label is present in the first row or not", "Verify label is present in the Participant Search Grid", "Label should be present in the Participant Search Grid", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "labelExistance"));

		}
	}





	/**
	 * This method is used to verify the first row of the central object
	 *
	 * @param TableLabelNames - List of labels in 1st row
	 */
	@Then("^I should see the Label: LabelName in first row:$")
	public void CentralPaneRow1Verify(DataTable TableLabelNames) {

		try {
			List<String> output = new ArrayList<String>();
			for (DataTableRow row : TableLabelNames.getGherkinRows()) {
				String TableHeader = row.getCells().get(0);
				output.add(TableHeader);
			}
			for (int i = 0; i < output.size(); i++) {
				if (Element.IsPresent(context, By.xpath(".//div[@class='row'][1]//*[contains(text(),'" + output.get(i) + "')]"))) {
					this.context.addResult("Verify label" + output.get(i) + " is present in the first row or not", "Verify label" + output.get(i) + " is present in the first row or not", "Label" + output.get(i) + " should be present in the first row", "Label" + output.get(i) + " is be present in the first row", RunStatus.PASS);
				} else
					this.context.addResult("Verify label" + output.get(i) + " is present in the first row or not", "Verify label" + output.get(i) + " is present in the first row or not", "Label" + output.get(i) + " should be present in the first row", "Label" + output.get(i) + " is be present in the first row", RunStatus.FAIL, Screenshot.getSnap(context, "labelExistance"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify label is present in the first row or not", "Verify label is present in the first row or not", "Label should be present in the first row", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "labelExistance"));

		}
	}

	/**
	 * This method is used to verify the second row of the central object
	 *
	 * @param TableLabelNames - List of labels in 2nd row
	 */
	@Then("^I should see the Label: LabelName in second row:$")
	public void CentralPaneRow2Verify(DataTable TableLabelNames) {

		try {
			List<String> output = new ArrayList<String>();
			for (DataTableRow row : TableLabelNames.getGherkinRows()) {
				String TableHeader = row.getCells().get(0);
				output.add(TableHeader);
			}
			for (int i = 0; i < output.size(); i++) {
				if (Element.IsPresent(context, By.xpath(".//div[@class='row'][2]//*[contains(text(),'" + output.get(i) + "')]"))) {
					this.context.addResult("Verify label" + output.get(i) + " is present in the first row or not", "Verify label" + output.get(i) + " is present in the first row or not", "Label" + output.get(i) + " should be present in the first row", "Label" + output.get(i) + " is be present in the first row", RunStatus.PASS);
				} else
					this.context.addResult("Verify label" + output.get(i) + " is present in the first row or not", "Verify label" + output.get(i) + " is present in the first row or not", "Label" + output.get(i) + " should be present in the first row", "Label" + output.get(i) + " is be present in the first row", RunStatus.FAIL, Screenshot.getSnap(context, "labelExistance"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify label is present in the first row or not", "Verify label is present in the first row or not", "Label should be present in the first row", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "labelExistance"));

		}
	}

	/**
	 * This method is used to click on a button
	 *
	 * @param ButtonName - Name of the button
	 */
	@When("^COMMON:I click on the \"(.*)\" button$")
	public void clickButton(String ButtonName) {
		try {
			FindElement(context, By.xpath(".//span[text()='" + CommonMethods.convert(ButtonName) + "']/parent::span")).click();
			this.context.addResult("Click on " + ButtonName + " button", "Click on " + ButtonName + " button", "Button is clicked successfully", "Button is clicked successfully", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Click on " + ButtonName + " button", "Click on " + ButtonName + " button", "Button is clicked successfully", "Button is not clicked", RunStatus.FAIL, Screenshot.getSnap(context, "ButtonError"));
			e.printStackTrace();
		}

	}

	@Then("^COMMON:I verify \"(.*)\" button present or not$")
	public void verifyButton(String ButtonName) {
		try {
			if (Element.IsPresent(context, By.xpath(".//span[text()='" + ButtonName + "']/parent::span"))) {
				this.context.addResult("verify " + ButtonName + " button present or not", " Verify" + ButtonName + " button present or not", "Button should be present", "Button should be present", RunStatus.PASS);
			} else
				this.context.addResult("verify " + ButtonName + " button present or not", " Verify" + ButtonName + " button present or not", "Button should be present", "Button should be present", RunStatus.FAIL, Screenshot.getSnap(context, "buttonexistance"));
		} catch (Exception e) {
			this.context.addResult("verify " + ButtonName + " button present or not", " Verify" + ButtonName + " button present or not", "Button should be present", "Exception occured", RunStatus.FAIL, Screenshot.getSnap(context, "buttonexistance"));
			e.printStackTrace();
		}

	}

	

	@Then("^COMMON:I Click on popup \"(.*)\" button$")
	public void clickPopupButton(String ButtonName) {
		try {
			FindElement(context, By.xpath(".//span[text()='" + ButtonName + "']/parent::span")).click();
		} catch (Exception e) {

		}
	}

	/**
	 * This method is used to click on the search icon on the participant page
	 */

	@When("^COMMONREV:I click on the Search Icon$")
	@Then("^I click on the Search Icon$")
	public void click_searchicon() {
		try {
			
			if(context.browser.equalsIgnoreCase("IE") || context.browser.contains("Internet Explorer"))
			{
				new Actions(context.driver).moveToElement(context.driver.findElement(Common.IESearchIcon)).perform();
				FindElement(context,Common.IESearchIcon).click();;
			}
			else
			{
				WaitingFunctions.waitForAnElement(context, Common.SearchIcon);
				FindElement(context, Common.SearchIcon).click();	
			}
			WaitingFunctions.WaitUntilLoadcleared(context,Common.RevitLoading);
			WaitingFunctions.WaitUntilPageLoads(context);
			this.context.addResult("Click on Search icon", "Click on Search icon", "Icon is clicked successfully", "Icon is clicked successfully", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Click on Search icon", "Click on Search icon", "Icon is clicked successfully", "Icon is not clicked", RunStatus.FAIL, Screenshot.getSnap(context, "SearchiconError"));
			e.printStackTrace();
		}

	}

	@When("^COMMONVDL:I click on the Search Icon$")
	public void click_searchiconVDL() {
		try {
			WaitingFunctions.waitforPageLoad(context);
			WaitingFunctions.waitForAnElement(context,Common.VDLSearchIcon);
			WebElement ele=FindElement(context,Common.VDLSearchIcon);	
			JavascriptExecutor js=(JavascriptExecutor)context.driver;
			js.executeScript("arguments[0].click();", ele);
			System.out.println("After Search Icon Click");
			this.context.addResult("Click on Search icon", "Click on Search icon", "Icon is clicked successfully", "Icon is clicked successfully", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Click on Search icon", "Click on Search icon", "Icon is clicked successfully", "Icon is not clicked", RunStatus.FAIL, Screenshot.getSnap(context, "SearchiconError"));
			e.printStackTrace();
		}

	}
	
	/**
	 * This method is used to verify the PP count after search performed
	 *
	 * @param rowcount
	 */
	@Then("^I verify the row count: (.*)$")
	public void verifyrowcount(String rowcount) {
		try {
			if (FindElement(context, Common.verifyrowcount).getText().equalsIgnoreCase(rowcount)) {
				this.context.addResult("verify the row count", "verify the row count", "Row count is verified successfully", "Row count is verified successfully", RunStatus.PASS);
			} else
				this.context.addResult("verify the row count", "verify the row count", "Row count should be equal to input", "Row count doest not equal to input", RunStatus.FAIL, Screenshot.getSnap(context, "Row count error"));

		} catch (Exception e) {
			this.context.addResult("verify the row count", "verify the row count", "Row count should be equal to input", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "Rowcounterror"));
		}
	}

	/**
	 * This method is used to click link in any page
	 *
	 * @param LinkName - Name of the link
	 */
	@When("^COMMON:I click on the \"(.*)\" link$")
	public void clickLink(String LinkName) {
		try {
			WebElement ele = FindElement(context, By.xpath(".//span[text()='" + CommonMethods.convert(LinkName) + "']//ancestor::span[@role='button']"));
			((JavascriptExecutor) context.driver).executeScript("arguments[0].scrollIntoView(true);", ele);
			FindElement(context, By.xpath(".//span[text()='" + LinkName + "']//ancestor::span[@role='button']")).click();
			this.context.addResult("Click on " + LinkName + " link", "Click on " + LinkName + " link", "link is clicked successfully", "link is clicked successfully", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Click on " + LinkName + " link", "Click on " + LinkName + " link", "link is clicked successfully", "link is not clicked", RunStatus.FAIL, Screenshot.getSnap(context, "LinkError"));
			e.printStackTrace();
		}

	}


	@Then("^COMMON:I Verify the link \"(.*)\" link$")
	public void VerifyLink(String LinkName) {
		try {
			if (Element.IsPresent(context, FindElement(context, By.xpath(".//span[text()='" + LinkName + "']/parent::span")))) {
				//FindElement(context,By.xpath(".//span[text()='"+LinkName+"']/parent::span")).click();
				this.context.addResult("Verify " + LinkName + " is displayed", "Verify " + LinkName + " is displayed", "the " + LinkName + " is displayed", "the " + LinkName + " is displayed", RunStatus.PASS);
			}
		} catch (Exception e) {
			this.context.addResult("Verify " + LinkName + " is displayed", "Verify " + LinkName + " is displayed", "the " + LinkName + " is displayed", "the " + LinkName + " is displayed", RunStatus.FAIL, Screenshot.getSnap(context, "sdg"));
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to verify FontSize,Fontcolor,FontWeight of any label
	 *
	 * @param FontSize
	 * @param FontColor
	 * @param FontWeight
	 * @param Label
	 */
	@Then("^COMMON:I should verify the FontSize:(.*), FontColor:(.*) and FontWeight:(.*) for the field (.*)$")
	@When("^I should verify the FontSize:\"(.*)\", FontColor:\"(.*)\" and FontWeight:\"(.*)\" for the field \"(.*)\"$")
	public void verifyFontofLabel(String FontSize, String FontColor, String FontWeight, String Label) {
		try {
			WebElement ele = null;
			if (Label.equalsIgnoreCase("Corrections") || Label.equalsIgnoreCase("Reset") || Label.equalsIgnoreCase("Search") || Label.equalsIgnoreCase("theme")) {
				ele = FindElement(context, By.xpath(".//span[contains(text(),'" + Label + "')]"));
			} else if (Label.equalsIgnoreCase("GENERATE REPORT")) {
				ele = FindElement(context, By.xpath(".//*[@id='genrateReport']"));
			} else if (Label.equalsIgnoreCase("hex")) {
				ele = FindElement(context, By.xpath(".//label[contains(text(),'" + Label + "')]"));
			} else
				ele = FindElement(context, By.xpath(".//*[contains(text(),'" + Label + "')]"));

			String ActFontSize = ele.getCssValue("font-size");
			String Colour = ele.getCssValue("color").toString();
			String ActFontColour = org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
			String ActFontWeight = ele.getCssValue("font-weight");
			if (ActFontSize.equalsIgnoreCase(FontSize) && ActFontColour.equalsIgnoreCase(FontColor) && ActFontWeight.equalsIgnoreCase(FontWeight)) {
				this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + Label, "Verify fontsize fontcolour fontweight of  " + Label, "Font size :" + FontSize + ",Font Colour: " + FontColor + ",Font Weight:" + FontWeight, "Font size :" + ActFontSize + ",Font Colour: " + ActFontColour + ",Font Weight:" + ActFontWeight, RunStatus.PASS);

			} else {
				this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + Label, "Verify fontsize fontcolour fontweight of  " + Label, "Font size :" + FontSize + ",Font Colour: " + FontColor + ",Font Weight:" + FontWeight, "Font size :" + ActFontSize + ",Font Colour: " + ActFontColour + ",Font Weight:" + ActFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, "Labelfailure"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Then("^COMMON:Verify label present or not:$")
	public void verifyLabelPresent(DataTable TableLabelNames) {
		try {
			List Labels = new ArrayList();
			for (DataTableRow row : TableLabelNames.getGherkinRows()) {
				String TableHeader = row.getCells().get(0);
				Labels.add(new Tabular_String(TableHeader));
			}
			List<Tabular_String> LablesTable = Labels;
			for (int i = 0; i < Labels.size(); i++) {
				WebElement ele = null;
				if (LablesTable.get(i).Field_1().equalsIgnoreCase("Corrections") || LablesTable.get(i).Field_1().equalsIgnoreCase("Reset") || LablesTable.get(i).Field_1().equalsIgnoreCase("Search") || LablesTable.get(i).Field_1().equalsIgnoreCase("theme")) {
					ele = FindElement(context, By.xpath(".//span[contains(text(),'" + CommonMethods.convert(LablesTable.get(i).Field_1()) + "')]"));
				} else if (LablesTable.get(i).Field_1().equalsIgnoreCase("GENERATE REPORT")) {
					ele = FindElement(context, By.xpath(".//*[@id='genrateReport']"));
				} else if (LablesTable.get(i).Field_1().equalsIgnoreCase("hex")) {
					ele = FindElement(context, By.xpath(".//label[contains(text(),'" + Labels.get(i) + "')]"));
				} else
					ele = FindElement(context, By.xpath(".//*[contains(text(),'" + Labels.get(i) + "')]"));


				if (Element.IsPresent(context, ele)) {
					this.context.addResult("verify " + Labels.get(i) + " present or not", "verify " + Labels.get(i) + " present or not", Labels.get(i) + " should be present", Labels.get(i) + " is present", RunStatus.PASS);
				} else {
					this.context.addResult("verify " + Labels.get(i) + " present or not", "verify " + Labels.get(i) + " present or not", Labels.get(i) + " should be present", Labels.get(i) + " is present", RunStatus.FAIL, Screenshot.getSnap(context, "label"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("^COMMON:I select \"(.*)\" from \"(.*)\"$")
	public void SelectDropdown(String DropdownValue, String DropdownName) {
		try {
			Dropdown.select(context, DropdownName, DropdownValue);
			this.context.addResult("Select dropdown value from dropdown", "Select dropdown value from dropdown", "Dropdown value should be selected", "dropdown value selected successfullt", RunStatus.PASS);
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Select dropdown value from dropdown", "Select dropdown value from dropdown", "Dropdown value should be selected", "dropdown value selected successfullt", RunStatus.FAIL, Screenshot.getSnap(context, "Dropdown"));
		}
	}

	/**
	 * This method is used to verify Field values in participants page
	 *
	 * @param FieldName     - Name of the Field
	 * @param ValueContains - Value to be verified
	 */
	@Then("^COMMON:I verify \"(.*)\" contains value \"(.*)\"$")
	@When("^I verify (.*) contains value (.*)$")
	public void verifyFieldValues(String FieldName, String ValueContains) {
		try {
			String DataVer = null;
			if (ValueContains.contains("*")) {
				DataVer = ValueContains.replace("*", "");
				if (CommonMethods.VerifytableRowData(context, FieldName, DataVer)) {
					this.context.addResult("Verify search results in accurate", "Verify search results in accurate", "Search data should be accurate", "Saerch data is accurate", RunStatus.PASS);
				} else
					this.context.addResult("Verify search results in accurate", "Verify search results in accurate", "Search data should be accurate", "Saerch data is accurate", RunStatus.FAIL, Screenshot.getSnap(context, "SearchFailure"));
			} else {
				DataVer = ValueContains;
				if (CommonMethods.VerifytableTotalRowData(context, FieldName, DataVer)) {
					this.context.addResult("Verify search results in accurate", "Verify search results in accurate", "Search data should be accurate", "Saerch data is accurate", RunStatus.PASS);
				} else
					this.context.addResult("Verify search results in accurate", "Verify search results in accurate", "Search data should be accurate", "Saerch data is accurate", RunStatus.FAIL, Screenshot.getSnap(context, "SearchFailure"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to verify SSN unmasked in PP Table
	 */
	@Then("^COMMON:I should see SSN in unmasked state$")
	public void verifySSNUnmasked() {
		try {
			if (CommonMethods.VerifyUnmaskedSNN(context)) {
				this.context.addResult("Verify SSN should be unmasked", "Verify SSN should be unmasked", "SSN should be unmasked", "SSN is be unmasked", RunStatus.PASS);
			} else
				this.context.addResult("Verify SSN should be unmasked", "Verify SSN should be unmasked", "SSN should be unmasked", "SSN is be unmasked", RunStatus.FAIL, Screenshot.getSnap(context, "SearchFailure"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method is used to verify SSN unmasked in participant page
	 *
	 * @param SSN
	 */
	@Then("^I verify the SSNunmasked:\"(.*)\"$")
	public void verifyunmaskedSSN(String SSN) {
		try {
			if (FindElement(context, Common.SSNunmasked).getText().equalsIgnoreCase(SSN)) {
				this.context.addResult("verify the SSN", "verify the SSN", "SSN number is unmasked verified successfully", "SSN number is unmasked verified successfully", RunStatus.PASS);
			} else
				this.context.addResult("verify the SSN", "verify the SSN", "SSN number is unmasked verified successfully", "SSN is still masked", RunStatus.FAIL, Screenshot.getSnap(context, "SSN error"));

		} catch (Exception e) {
			this.context.addResult("verify the SSN", "verify the SSN", "SSN number is unmasked verified successfully", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "SSN Error"));
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to Verify the labels in participants page 2nd row
	 *
	 * @param TableLabelNames
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Then("^I should see the Label: LabelName in second row in Participant Profile:$")
	public void ParticipantProfileRow2Verify(DataTable TableLabelNames) {

		try {
			List output = new ArrayList();

			for (DataTableRow row : TableLabelNames.getGherkinRows()) {
				String TableHeader = row.getCells().get(0);
				output.add(new Tabular_String(TableHeader));
			}
			List<Tabular_String> Lables = output;
			for (int i = 0; i < Lables.size(); i++) {
				if (Element.IsPresent(context, By.xpath(".//div[@class='row']//*[contains(text(),'" + Lables.get(i).Field_1() + "')]"))) {
					this.context.addResult("Verify label" + output.get(i) + " is present in the first row or not", "Verify label" + output.get(i) + " is present in the first row or not", "Label" + output.get(i) + " should be present in the first row", "Label" + output.get(i) + " is be present in the first row", RunStatus.PASS);
				} else
					this.context.addResult("Verify label" + output.get(i) + " is present in the first row or not", "Verify label" + output.get(i) + " is present in the first row or not", "Label" + output.get(i) + " should be present in the first row", "Label" + output.get(i) + " is be present in the first row", RunStatus.FAIL, Screenshot.getSnap(context, "labelExistance"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify label is present in the first row or not", "Verify label is present in the first row or not", "Label should be present in the first row", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "labelExistance"));

		}
	}


	/**
	 * This method is used to select multiple PP's from search table
	 *
	 * @param PPID - list of PP ID's separated by comma(,)
	 */
	@When("^COMMON:Select Multiple PP's with Id's as \"(.*)\"$")
	public void SelectMultiplePP(String PPID) {
		try {
			String[] PPList = PPID.split(",");
			int count = 0;
			for (int i = 0; i < PPID.length(); i++) {
				if (CommonMethods.CheckPPWithEmployeeID(context, PPList[i])) {
					count = count + 1;
				}
			}
			if (PPList.length == count) {
				this.context.addResult("Check the given PP's", "Check the given PP's", "PPs should be selected", "PPs selected sucessfully", RunStatus.PASS);
			} else {
				this.context.addResult("Check the given PP's", "Check the given PP's", "PPs should be selected", "PPs selected sucessfully", RunStatus.FAIL,Screenshot.getSnap(context, "Select PP"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method is used to select multiple PP's from search table if present in the same grid select 100 records in the drop down to view all the same grid
	 *
	 * @param PPID - list of PP ID's separated by comma(,)
	 */
	@When("^CB:Select Multiple PP's with Id's as \"(.*)\"$")
	public void RevUI_SelectMultiplePP(String PPID) {
		try {
			String[] PPList = PPID.split(",");
			int count = 0;
			for (int i = 0; i <PPList.length; i++) {
				if (CommonMethods.RevUI_CheckPPWithEmployeeID(context, PPList[i])) {
					count = count + 1;
				}
			}
			if (PPList.length == count) 
			{
				//pass
			} else {
				//fail
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method is used to select multiple Message's from message center table
	 *
	 * @param PPID - list of PP ID's separated by comma(,)
	 */
	@When("^COMMON:Select Multiple Messages's with text as \"(.*)\"$")
	public void SelectMultipleMessages(String PPID) {
		try {
			String[] PPList = PPID.split(",");
			int count = 0;
			for (int i = 0; i < PPID.length(); i++) {
				if (CommonMethods.CheckMessageWithText(context, PPList[i])) {
					count = count + 1;
				}
			}
			if (PPList.length == count) {
				//pass
			} else {
				//fail
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method is clicks on forward icon of partipants page
	 */
	@When("^COMMON:I click on Forward Button$")
	public void ClickForwardicon() {
		try {
			if(context.browser.equalsIgnoreCase("IE") || context.browser.contains("Internet Explorer"))
			{
				new Actions(context.driver).moveToElement(context.driver.findElement(Common.IEForwrdButton)).perform();
				FindElement(context,Common.IEForwrdButton).click();;
			}
			else
			{
				FindElement(context, Common.ClickForwardicon).click();	
			}
			WaitingFunctions.waitForAnElement(context, Common.RevitLoading);
			WaitingFunctions.WaitUntilLoadcleared(context, Common.RevitLoading);
			this.context.addResult("Click Forward Icon", "Click Forward Icon", "User is navigated to next PP", "User is navigated to next PP", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Click Forward Icon", "Click Forward Icon", "User is navigated to next PP", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "Forward icon failed Error"));
		}
	}


	/**
	 * This method is clicks on backward icon of partipants page
	 */
	@When("^COMMON:I click on Backward Button$")
	public void ClickBackwardicon() {
		try {

			if(context.browser.equalsIgnoreCase("IE") || context.browser.contains("Internet Explorer"))
			{
				new Actions(context.driver).moveToElement(context.driver.findElement(Common.IEBackwardButton)).perform();
				FindElement(context,Common.IEBackwardButton).click();;
			}
			else
			{
				FindElement(context, Common.ClickBackwardicon).click();
			}
			WaitingFunctions.waitForAnElement(context, Common.RevitLoading);
			WaitingFunctions.WaitUntilLoadcleared(context, Common.RevitLoading);
			this.context.addResult("Click Backward Icon", "Click Backward Icon", "User is navigated to next PP", "User is navigated to next PP", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Click Backward Icon", "Click Backward Icon", "User is navigated to next PP", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "Backward icon failed Error"));
		}
	}

	/**
	 * This method is click Select All Check Box
	 */
	@When("^CB:I select all PPs$")
	public void RevUI_ClickSelectAllCheckBox() {
		try {


			if(context.browser.equalsIgnoreCase("IE") || context.browser.contains("Internet Explorer"))
			{
				new Actions(context.driver).moveToElement(context.driver.findElement(Common.IESelectAllCheckbox)).perform();
				FindElement(context,Common.IESelectAllCheckbox).click();;
			}
			else
			{
				FindElement(context, Common.SelectAllbox).click();
			}
			this.context.addResult("Click SelectAll Check Box", "Click SelectAll Check Box", "User clicked select all check box", "User clicked select all check box", RunStatus.PASS);

		} catch (Exception e) {
			this.context.addResult("Click SelectAll Check Box", "Click SelectAll Check Box", "User clicked select all check box", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "Backward icon failed Error"));
		}
	}

	/**
	 * This method is used to verify Label and its value
	 *
	 * @param Field - Field Name
	 * @param Value - Value of the field
	 */
	@Given("^COMMON:Verify (.*) and it's value is (.*)$")
	@Then("^Verify \"(.*)\" and it's value is \"(.*)\"$")
	public void VerifyParticipantFieldValue(String Field, String Value)
	{
		String ErrorMessage = "Label Details : ";

		/***********
		 * Waiting for load clearance for data validations 
		 */
		WaitingFunctions.WaitUntilLoadcleared(context, Common.RevitLoading);
		List<WebElement> LabelList = FindElements(context, Common.PPLabels);

		for (int i = 0; i < LabelList.size(); i++) 
		{
			if (LabelList.get(i).getText().trim().toLowerCase().contains(Field.toLowerCase())) 
			{
				if (LabelList.get(i).getText().trim().toLowerCase().contains(Value.toLowerCase())) 
				{
					this.context.addResult("Valid field data is present", "field is present "+"\n"+ErrorMessage, "field is present" +"\n"+ErrorMessage, "field is present" +"\n"+ErrorMessage, RunStatus.PASS);
					return;
				}
				else
				{
					ErrorMessage = ErrorMessage + "Label with name : " + Field.toLowerCase() + " Label with data :" + Value.toLowerCase() + "\n";
				}
			}
		}
		System.out.println("FAIL");
		this.context.addResult("Valid field data is not present", "Valid field data is not present" +"\n"+ErrorMessage, "Valid field data is not present"+"\n"+ErrorMessage, "Exception occured", RunStatus.FAIL, Screenshot.getSnap(context, "Nofield data"));
	}


	/**
	 * This method id used to verify No records found message
	 */
	@Then("^COMMON:I should see a message Your search did not result in any matches$")
	public void verifyNoRecordsmessage() {
		try {
			if (Element.IsPresent(context, Common.Norecordsmessage)) {
				this.context.addResult("Verify no records found message", "Verify no records found message", "Message should exists", "Message exists", RunStatus.PASS);
			} else
				this.context.addResult("Verify no records found message", "Verify no records found message", "Message should exists", "Message exists", RunStatus.FAIL, Screenshot.getSnap(context, "NorecordsMessage"));
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify no records found message", "Verify no records found message", "Message should exists", "Exception occured", RunStatus.FAIL, Screenshot.getSnap(context, "NorecordsMessage"));
		}
	}


	/**
	 * This method is used to click Reveal link
	 */
	@When("^COMMON:I click on the link Reveal$")
	public void ClickReveallink() {
		try {
			FindElement(context, Common.ClickReveallink).click();
			this.context.addResult("Click Reveal link", "Click Reveal link", "User clicks on reveal link", "User clicks on reveal link", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Click Reveal link", "Click Reveal link", "User clicks on reveal lin", "Reveal link is not clicked", RunStatus.FAIL, Screenshot.getSnap(context, "reveal link Error"));
		}
	}

	/**
	 * This method is used to verify Message Enter one or more search options
	 */

	@Then("^COMMON:I should see a message:Enter one or more search options$")
	public void verifySerachoptionsmessage() {
		try {
			if (Element.IsPresent(context, Common.SearchRecordsMessage)) {
				this.context.addResult("Verify Enter search results message", "Verify Enter search results message", "Message should exists", "Message exists", RunStatus.PASS);
			} else
				this.context.addResult("Verify Enter search results message", "Verify Enter search results message", "Message should exists", "Message exists", RunStatus.FAIL, Screenshot.getSnap(context, "NorecordsMessage"));
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify Enter search results message", "Verify Enter search results message", "Message should exists", "Exception occured", RunStatus.FAIL, Screenshot.getSnap(context, "NorecordsMessage"));
		}
	}

	/**
	 * This method is used to check select all checkbox in central object search table
	 */
	@When("^COMMON:I select the 'select all' check box$")
	public void CheckSelectAllcheckbox() {
		try {
			FindElement(context, Common.SelectAllcheckbox).click();
			if (FindElement(context, Common.SelectAllcheckbox).isSelected()) {
				this.context.addResult("Check Select All Checkbox", "Check Select All Checkbox", "CHeckbox should be checked", "Checkbox checked", RunStatus.PASS);
			} else
				this.context.addResult("Check Select All Checkbox", "Check Select All Checkbox", "CHeckbox should be checked", "Checkbox not checked", RunStatus.FAIL, Screenshot.getSnap(context, "SelectAllcheckbox"));
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Check Select All Checkbox", "Check Select All Checkbox", "CHeckbox should be checked", "Exception occured", RunStatus.FAIL, Screenshot.getSnap(context, "SelectAllcheckbox"));
		}
	}

	/**
	 *
	 */
	@Then("^COMMON:I should see the respective participant profile page$")
	public void verifyparticipantpage() {
		try {
			if (Element.IsPresent(context, Common.ParticipantPanel)) {
				this.context.addResult("Verify PP is in participants page", "Verify PP is in participants page", "PP should be in participants page", "PP is in participants page", RunStatus.PASS);
			} else
				this.context.addResult("Verify PP is in participants page", "Verify PP is in participants page", "PP should be in participants page", "PP is in participants page", RunStatus.FAIL, Screenshot.getSnap(context, "participantpage"));
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify PP is in participants page", "Verify PP is in participants page", "PP should be in participants page", "PP is in participants page", RunStatus.FAIL, Screenshot.getSnap(context, "participantpage"));
		}
	}


	@SuppressWarnings({"unchecked", "rawtypes"})
	@When("COMMON:I select Employee value:$")
	public void CheckEmployeechackbox(DataTable EmployeeList) {
		try {
			List output = new ArrayList();

			for (DataTableRow row : EmployeeList.getGherkinRows()) {
				String TableHeader = row.getCells().get(0);
				output.add(new Tabular_String(TableHeader));
			}
			List<Tabular_String> EmpList = output;
			for (int i = 0; i < EmpList.size(); i++) {
				FindElement(context, By.xpath(".//span[text()='" + EmpList.get(i).Field_1() + "']/ancestor::tr[contains(@id,'tableGrid')]//div[@class='revitCheckBoxIcon']")).click();
				if (FindElement(context, By.xpath(".//span[text()='" + EmpList.get(i).Field_1() + "']/ancestor::tr[contains(@id,'tableGrid')]//div[@class='revitCheckBoxIcon']/i")).isEnabled()) {
					this.context.addResult("Check Select All Checkbox", "Check Select All Checkbox", "CHeckbox should be checked", "Checkbox checked", RunStatus.PASS);
				} else
					this.context.addResult("Check Select All Checkbox", "Check Select All Checkbox", "CHeckbox should be checked", "Checkbox not checked", RunStatus.FAIL, Screenshot.getSnap(context, "SelectAllcheckbox"));
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}


	@Then("^COMMON:I should verify the FontSize:(.*), FontColor:(.*) and FontWeight:(.*) for participant the (.*): (.*)$")
	public void VerifyPropertiesOfLabel(String FontSize, String FontColor, String FontWeight, String FieldValue, String FieldLabel) {
		try {
			List<WebElement> LabelList = FindElements(context, Common.PPLabels);
			WebElement Value;
			for (int i = 0; i < LabelList.size(); i++) {
				WebElement Label = LabelList.get(i).findElement(By.tagName("label"));
				if (Label.getText().trim().toLowerCase().contains(FieldLabel.toLowerCase())) {
					List<WebElement> SubEle = LabelList.get(i).findElements(By.xpath("./*"));
					if (SubEle.size() == 2) {
						Value = Label.findElement(By.xpath("./following-sibling::div"));
					} else {
						Value = LabelList.get(i);
					}
					if (Value.getText().trim().toLowerCase().contains(FieldValue.toLowerCase())) {
						this.context.addResult("Valid field data is present", "field is present", "field is present", "field is present", RunStatus.PASS);
						int flag = 0, count = 0;
						//font size
						String ActFontSize = Value.getCssValue("font-size");
						if (ActFontSize.equalsIgnoreCase(FontSize)) {
							System.out.println("*** Font Size Verification Successful");
							count = count + 1;
						} else {
							flag = 1;
						}
						//Font color
						String Colour = Value.getCssValue("color").toString();
						String ActFontColour = Color.fromString(Colour).asHex().toUpperCase();
						if (ActFontColour.equalsIgnoreCase(FontColor)) {
							System.out.println("*** Font Color Verification Successful");
							count = count + 1;
						} else {
							flag = 1;
						}
						//Font Weight
						String ActFontWeight = Value.getCssValue("font-weight");
						if (ActFontWeight.equalsIgnoreCase(FontWeight)) {
							System.out.println("*** Font Weight Verification Successful");
							count = count + 1;
						} else {
							flag = 1;
						}

						if (flag == 0 && count == 3) {
							this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + FieldLabel, "Verify fontsize fontcolour fontweight of  " + FieldValue, "Font size :" + FontSize + ",Font Colour:" + FontColor + ",Font Weight:" + FontWeight, "Font size :" + ActFontSize + ",Font Colour: " + ActFontColour + ",Font Weight:" + ActFontWeight, RunStatus.PASS);

						} else {
							this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + FieldLabel, "Verify fontsize fontcolour fontweight of  " + FieldValue, "Font size :" + FontSize + ",Font Colour: " + FontColor + ",Font Weight:" + FontWeight, "Font size :" + ActFontSize + ",Font Colour: " + ActFontColour + ",Font Weight:" + ActFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, "Labelfailure"));
						}
						return;
					}
				}
			}
			System.out.println("FAIL");
			this.context.addResult("Valid field data is not present", "Valid field data is not present", "Valid field data is not present", "Exception occured", RunStatus.FAIL, Screenshot.getSnap(context, "Nofield data"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("^COMMON:I should logout from Application$")
	public void Logout() {
		try {
			FindElement(context, Common.LogoutArroe).click();
			FindElement(context, By.xpath(".//span[text()='OK']/parent::span")).click();
			this.context.addResult("Perform Logout", "Perform Logout", "Logout should be sucessful", "Logout sucessfull", RunStatus.PASS);

		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Perform Logout", "Perform Logout", "Logout should be sucessful", "Logout sucessfull", RunStatus.FAIL, Screenshot.getSnap(context, "Logout"));
		}
	}


	@Then("^COMMON:I should see the \"(.*)\" records in ascending order$")
	public void sortorder(String Field) {
		try {
			List<WebElement> list = context.driver.findElements(Common.EmployeeHeaders);
			for (WebElement ele : list) {
				int i = 0, j = 0;
				if (ele.getText().trim().equalsIgnoreCase(Field)) {
					ele.click();
					j = i + 1;
					if (CommonMethods.EmployeeSortedList(context, Common.EmployeeRowsTable, j)) {
						this.context.addResult("Verify SortOrder", "Verify sort order based on : " + Field, "Entries should be sort order for : " + Field, "Fields Entries are in sort oder", RunStatus.PASS);
						return;
					} else {

						this.context.addResult("Verify SortOrder", "Verify sort order based on : " + Field, "Entries should be sort order for : " + Field, "Fields Entries are not in sort oder", RunStatus.FAIL, Screenshot.getSnap(context, "Sort"));
						return;
					}
				}
				i = i + 1;
			}
		} catch (Exception e) {
			this.context.addResult("Verify SortOrder", "Verify sort order based on : " + Field, "Entries should be sort order for : " + Field, "Exception While Executing step", RunStatus.FAIL, Screenshot.getSnap(context, "Sort"));
			e.printStackTrace();
		}
	}


	@Then("^I have to see the tab (.*) with font (.*), Weight (.*) and Color (.*)$")
	public void verifyFontofTab(String Tab, String Font, String weight, String color) {
		try {
			WebElement ele = null;
			ele = FindElement(context, By.xpath(".//span[text()='" + Tab + "']"));
			String ActFontSize = ele.getCssValue("font-size");
			String Colour = ele.getCssValue("color").toString();
			String ActFontColour = org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
			String ActFontWeight = ele.getCssValue("font-weight");
			if (ActFontSize.equalsIgnoreCase(Font) && ActFontColour.equalsIgnoreCase(color) && ActFontWeight.equalsIgnoreCase(weight)) {
				this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + Tab, "Verify fontsize fontcolour fontweight of  " + Tab, "Font size :" + Font + ",Font Colour: " + color + ",Font Weight:" + weight, "Font size :" + ActFontSize + ",Font Colour: " + ActFontColour + ",Font Weight:" + ActFontWeight, RunStatus.PASS);

			} else {
				this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + Tab, "Verify fontsize fontcolour fontweight of  " + Tab, "Font size :" + Font + ",Font Colour: " + color + ",Font Weight:" + weight, "Font size :" + ActFontSize + ",Font Colour: " + ActFontColour + ",Font Weight:" + ActFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, "Tabfailure"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + Tab, "Verify fontsize fontcolour fontweight of  " + Tab, "Font size :" + Font + ",Font Colour: " + color + ",Font Weight:" + weight, "", RunStatus.FAIL, Screenshot.getSnap(context, "Tabfailure"));
		}

	}


	@Then("^I have to see the Button \"(.*)\" with font \"(.*)\", Weight \"(.*)\" and Color \"(.*)\"$")
	@And("^I verify for button (.*): Fontsize (.*), Fontweight (.*) and Fontcolor (.*)$")
	public void verifyFontofButton(String Button, String Font, String Weight, String color) {
		try {
			WebElement ele = null;
			ele = FindElement(context, By.xpath(".//*[@id='creditCorrectionsListGrid_toolbar_delete']"));
			String ActFontSize = ele.getCssValue("font-size");
			String Colour = ele.getCssValue("color").toString();
			String ActFontColour = org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
			String ActFontWeight = ele.getCssValue("font-weight");
			if (ActFontSize.equalsIgnoreCase(Font) && ActFontColour.equalsIgnoreCase(color) && ActFontWeight.equalsIgnoreCase(Weight)) {
				this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + Button, "Verify fontsize fontcolour fontweight of  " + Button, "Font size :" + Font + ",Font Colour: " + color + ",Font Weight:" + Weight, "Font size :" + ActFontSize + ",Font Colour: " + ActFontColour + ",Font Weight:" + ActFontWeight, RunStatus.PASS);

			} else {
				this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + Button, "Verify fontsize fontcolour fontweight of  " + Button, "Font size :" + Font + ",Font Colour: " + color + ",Font Weight:" + Weight, "Font size :" + ActFontSize + ",Font Colour: " + ActFontColour + ",Font Weight:" + ActFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, "Tabfailure"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@When("^COMMON:I click on NavigateToPage Icon$")
	public void ClickNavigateToPage() {
		try {
			FindElement(context, Common.ClickNavigateToPage).click();
			this.context.addResult("Click NavigateToPage Icon", "Click NavigateToPage Icon", "Pop Up page is displayed", "Pop Up page is displayed", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Click NavigateToPage Icon", "Click NavigateToPage Icon", "Pop Up page is displayed", "Error occured", RunStatus.FAIL, Screenshot.getSnap(context, "NavigateToPage icon failed Error"));
		}

	}

	@Then("^COMMON:I have to see the PPname:\"(.*)\",PPid:\"(.*)\",Status:\"(.*)\", Email and Telephone Icon")
	public void ViewDetialsPOPup(String PPname, String PPid, String Status) {
		try {

			if (FindElement(context, Common.userProfile__name).getText().equalsIgnoreCase(PPname) && FindElement(context, Common.userProfile_id).getText().equalsIgnoreCase(PPid) && FindElement(context, Common.userProfile_status).getText().equalsIgnoreCase(Status)) {
				this.context.addResult("User Profile is displayed", "User Profile is displayed ", "User Profile is displayed", "User Profile is displayed", RunStatus.PASS);
			} else {
				this.context.addResult("User Profile is displayed", "User Profile is displayed ", "User Profile is displayed", "User Profile is not displayed", RunStatus.FAIL, Screenshot.getSnap(context, "User Profile Error"));
				;

			}

			if (Element.IsPresent(context, Common.userProfile_Emailicon) && Element.IsPresent(context, Common.userProfile_Telephoneicon)) {
				this.context.addResult("Email and Telephone icon is displayed", "Email and Telephone icon is displayed ", "Email and Telephone icon is displayed", "Email and Telephone icon is displayed", RunStatus.PASS);
			} else {
				this.context.addResult("Email and Telephone icon is displayed", "Email and Telephone icon is displayed ", "Email and Telephone icon is displayed", "Email and Telephone icon is not displayed", RunStatus.FAIL, Screenshot.getSnap(context, "Icons not present"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param PageList
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Then("^Common:I have to Verify all the Pages:$")
	public void VerifyPagesinPopup(DataTable PageList) {
		List output = new ArrayList();
		for (DataTableRow row : PageList.getGherkinRows()) {
			String TableHeader = row.getCells().get(0);
			output.add(new Tabular_String(TableHeader));
		}
		List<Tabular_String> PageListTable = output;
		for (int i = 0; i <= PageListTable.size(); i++) {
			if (Element.IsPresent(context, By.xpath(".//a[text()='" + PageListTable.get(i).Field_1() + "']"))) {
				this.context.addResult("Verify Page " + PageListTable.get(i).Field_1() + " is present in the Popup", "Verify Page " + PageListTable.get(i).Field_1() + " is present in the Popup", "Page" + PageListTable.get(i).Field_1() + " should be present in the Popup", "Page" + PageListTable.get(i).Field_1() + " is present in the Popup", RunStatus.PASS);
			} else {
				this.context.addResult("Verify Page " + PageListTable.get(i).Field_1() + " is present in the Popup", "Verify Page " + PageListTable.get(i).Field_1() + " is present in the Popup", "Page" + PageListTable.get(i).Field_1() + " should be present in the Popup", "Page" + PageListTable.get(i).Field_1() + " is not present in the Popup", RunStatus.FAIL, Screenshot.getSnap(context, "Page not present"));

			}
		}
	}


	@Then("^COMMON:I Verified text \"(.*)\"$")
	public void VerifyTextvalue(String TextValue) {
		try {
			if (Element.IsPresent(context, By.xpath(".//span[text()='" + TextValue + "']"))) {

				this.context.addResult("This " + TextValue + " is present", "This " + TextValue + " is present", "TextValue is identified successfully", "TextValue is identified successfully", RunStatus.PASS);
			}
		} catch (Exception e) {
			this.context.addResult("This " + TextValue + " is present", "This " + TextValue + " is present", "TextValue is not identified", "TextValue is not identified", RunStatus.FAIL, Screenshot.getSnap(context, "LinkError"));
			e.printStackTrace();
		}

	}

	/**
	 * This method id used to verify search result grid
	 * This method also verifies the availability of check box at each record
	 */
	@Then("^COMMON:I Verify the search result grid of Undo Events$")
	public void VerifySearchResults() {
		try {
			if (Element.IsPresent(context, By.xpath(".//*[@id='tableGrid_table']"))) {
				this.context.addResult("Verify search results", "Verify serach results", "Search results displayed", "Search results displayed", RunStatus.PASS);
			} else {
				this.context.addResult("Verify search results", "Verify serach results", "Search results displayed", "Search results displayed", RunStatus.FAIL, Screenshot.getSnap(context, "Searchresults"));
			}

			if (Element.IsPresent(context, By.xpath(".//div[contains(@class,'CheckBox')]"))) {
				this.context.addResult("Verify the Check box present at each record", "Verify the Check box not present", "Check box not present", "Check box present", RunStatus.FAIL, Screenshot.getSnap(context, "Checkbox"));
			} else {
				this.context.addResult("Verify the Check box present at each record", "Verify the Check box not present", "Check box not present", "Check box Not present", RunStatus.PASS);
			}


		} catch (Exception e) {
			this.context.addResult("Exception occured", "Verify serach results", "Search results displayed", "Exception occured", RunStatus.FAIL, Screenshot.getSnap(context, "Exception"));
		}
	}

	/**
	 * This method is used to verify the central object pane present or not
	 */
	@Then("^COMMON:I verify control returns to Central Object page$")
	public void VerifyCentralobject() {
		if (Element.IsPresent(context, By.xpath(".//*[@id='userSearchArea']"))) {
			this.context.addResult("Verify central object existance", "Verify central object existance", "Central object should be present", "Central object is present", RunStatus.PASS);
		} else {
			this.context.addResult("Verify central object existance", "Verify central object existance", "Central object should be present", "Central object is not present", RunStatus.FAIL, Screenshot.getSnap(context, "Searchresults"));
		}
	}

	//	/**
	//	 * 
	//	 * Admin login: It will complete the ARCOT process with the $username and $password parameters
	//	 * 
	//	 */
	//	
	////	@When("I entered admin username $username and password $password")
	////	@Alias("COMMON:I entered admin username $username and password $password")
	////	public void loginToPortalAdmin(String username,String password) throws Exception{
	////			LoginFunctions.AdminArcotLogin(context, username, password);
	////			this.context.addResult("Admin Login", "Login to the ACA as Admin", "Client Selection Page", "Client Selection Page", RunStatus.PASS);		
	////	}
	////	
	//	/**
	//	 * 
	//	 * Client selection process
	//	 * This is for admin login : Practitioner Access
	//	 * 
	//	 */
	////	@When("Select the client $Client")
	////	@Aliases(values = {"COMMON:Select the client $Client"})
	////	public void selectClient(String Client ){
	////		try{
	////			
	//////			if(Element.IsPresent_linkText(context, "Return to ADP Support")){
	//////				Element.Click(context, By.linkText("Return to ADP Support"));
	//////				WaitingFunctions.waitForElement(context, LoginPage.ClientWebEdit, 15);
	//////			}
	////			if(context.driver.findElement(LoginPage.ClientWebEdit).isDisplayed()){	
	////				LoginFunctions.selectClient(context, Client);
	////				WaitingFunctions.waitForElement(context, LoginPage.Shell, 15);
	//////				this.context.driver.findElement(By.xpath(".//*[@id='clientDropDown']")).click();
	//////				this.context.driver.findElement(By.xpath(".//*[@id='clientDropDown']")).clear();
	//////				this.context.driver.findElement(By.xpath(".//*[@id='clientDropDown']")).sendKeys(Client);
	//////				Button.Click(context, LoginPage.ChangeClientButton);
	//////				WaitingFunctions.WaitUntilPageLoads(context);
	////
	//////				WaitingFunctions.waitForAnElement(context, Home_UserHome.HomePage);
	//////				if(Dropdown.isPresent(context, LoginPage.SelectClient)){
	//////					Dropdown.Select(context, LoginPage.SelectClient, Client);
	//////					Button.Click(context, LoginPage.ChangeClientButton);
	//////			}
	////			if(context.driver.findElement(LoginPage.Shell).isDisplayed()){
	////				this.context.addResult("Client Selection", "Selecting the client", "Client Selection should be Successful", "Client Selected Successfully",  RunStatus.PASS);
	////			}
	////		}
	////		}catch(StaleElementReferenceException e){
	////			this.context.addResult("Client Selection", "Selecting the client","Client Selection should be Successful", "Client Selection Failed " , RunStatus.FAIL,Screenshot.getSnap(context, "SelectClient"));
	////			e.printStackTrace();
	////		}
	////	}		
	//	/**
	//	 * 
	//	 * Client value should provide from XML
	//	 * This is for admin login : Practitioner Access
	//	 * 
	//	 */
	////	@When("User Selects the client from Test Parameters")
	////	@Aliases(values = {"COMMON:User Selects the client from Test Parameters"})
	////	public void selectClientFromTestParam(){
	////		String Client = this.context.sClient;
	////		System.out.println(Client);
	////		try{
	////			if(context.driver.findElement(LoginPage.ClientWebEdit).isDisplayed()){	
	////				LoginFunctions.selectClient(context, Client);		
	////			if(context.driver.findElement(Home_UserHome.HomePage).isDisplayed()){
	////				this.context.addResult("Client Selection", "Selecting the client", "ACA Portal  Home Page", "ACA Portal Home Page",  RunStatus.PASS);
	////			}
	////		}
	////		}catch(Exception e){
	////			this.context.addResult("Client Selection", "Selecting the client", "ACA Portal Home Page", "ACA Portal Home Page Not found" , RunStatus.FAIL,Screenshot.getSnap(context, "SelectClient"));
	////			e.printStackTrace();
	////		}
	////	}	
	////	/**
	////	 * 
	////	 * It is used for the sorting in Eligibilty / Affordability page
	////	 */
	////	@Then("Verify sort order based on $Field")
	////	@Alias("COMMON:Verify sort order based on $Field")
	////	public void sortorder(String Field){
	////		try{
	////			List<WebElement> list = context.driver.findElements(Common.TableHeaders);
	////			for(WebElement ele:list){
	////				if(ele.getText().trim().equalsIgnoreCase(Field)){
	////					ele.findElement(By.tagName("SPAN")).click();
	////					if(CommonMethods.EmployeeSortedList(context, Common.EmployeeTable)){
	////						this.context.addResult("Verify SortOrder", "Verify sort order based on : "+Field, "Entries should be sort order for : "+Field, "Fields Entries are in sort oder", RunStatus.PASS);
	////						return;
	////					}
	////					else{
	////						this.context.addResult("Verify SortOrder", "Verify sort order based on : "+Field,"Entries should be sort order for : "+Field, "Fields Entries are not in sort oder", RunStatus.FAIL,Screenshot.getSnap(context, "Sort"));
	////						return;
	////					}
	////				}
	////			}
	////		}catch(Exception e){
	////			e.printStackTrace();
	////		}
	////	}




	@When("^COMMON:I click on the Last name of the employee with employee ID \"(.*)\"$")
	public void SelectEmployee(String EmpId) {
		try {

			if (EmpId.contains("<")) {
				EmpId = PropertiesFile.GetProperty(EmpId.replace("<", "").replace(">", "").trim());
			}
			WebElement ElementTable = FindElement(context, Common.EnrollmentsTable);
			int rows = RevitTableFunctions.RowCount(ElementTable);
			int columns = RevitTableFunctions.ColumnCount(ElementTable, 2);
			for (int i = 1; i <= rows; i++) {
				for (int j = 1; j <= columns; j++) {
					if (RevitTableFunctions.GetCellData(ElementTable, i, j).equalsIgnoreCase(EmpId)) {
						if (RevitTableFunctions.ClickCellData(ElementTable, i, 2)) {
							Thread.sleep(3000);
							System.out.println("*** Employee Selected Successfully");
							this.context.addResult("Select Employee", "Select Employee from the table", "Employee has to be selected with ID: " + EmpId, "Employee Selection Successful", RunStatus.PASS);
							return;
						}
					}
				}
			}
			System.out.println("*** Employee with ID not found");
			this.context.addResult("Select Employee", "Select Employee from the table", "Employee has to be selected with ID: " + EmpId, "Failed to fetch Employee", RunStatus.FAIL, Screenshot.getSnap(context, "SelectPP"));
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Select Employee", "Select Employee from the table", "Employee has to be selected with ID: " + EmpId, "Failed to fetch Employee", RunStatus.FAIL, Screenshot.getSnap(context, "SelectPP"));
		}
	}


	@Then("COMMON:Close the Portal Integration Application")
	public void ClosePortalIntegration() {
		try {
			for (String winHandle : this.context.driver.getWindowHandles()) {
				if (!(this.context.driver.getTitle().contains("ADP")))
					this.context.driver.close();
				System.out.println("*** ADP Benefits Aplication closed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Then("Verify Online PODS Mail Verification from \"(.*)\" with mail header contains \"(.*)\"")
	public void OnlinePODVerification(String MailFrom, String MailContent) {
		try {
			List<ReadEmails> readEmailsList = null;
			readEmailsList = ReadEmails.getEmails("exchgen-group11182", "Totalsource1!", MailContent, MailFrom, StartDate);
			if (readEmailsList == null || readEmailsList.size() == 0) {
				this.context.addResult("Online POD E-mail Verifications", "Online POD E-mail Verifications for Election Confirmations", "E-mail Verifications has to happen", "Please check it manually", RunStatus.FAIL);
			}
			if (readEmailsList.size() > 0) {
				for(int i=0;i<readEmailsList.size();i++){
					this.context.addResult("Online POD E-mail Verifications", "Online POD E-mail Verifications for Election Confirmations", "E-mail Verifications has to happen", "E-Mail Verification Passed : "+readEmailsList.get(i), RunStatus.PASS);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Then("COMMON:Scenario Objective: \"(.*)\"")
	@Given("COMMON:Scenario Description: \"(.*)\"")
	public void ScenarioObjective(String objective) {

		this.context.addResult("Objective", "Description: "+objective, "", "", RunStatus.PASS);
	}
	
	@Then("COMMON:Wait the script execution for time: \"(.*)\" mins")
	public void scriptWait(String time) throws NumberFormatException, InterruptedException {

		Thread.sleep(Integer.parseInt(time)*1000);
		System.out.println("Waiting the script execution for "+time+" mins");
	}



	@Then("I have to Verify the Online POD Email verification for:")
	public void VerifyOnlinePODMailVerification(DataTable EmployeeTable){
		try{
			List Labels = new ArrayList();
			for (DataTableRow row : EmployeeTable.getGherkinRows()) {
				String Employee = row.getCells().get(0);
				String POD = row.getCells().get(1);
				Labels.add(new Tabular_StringNString(Employee,POD));
			}
			List<Tabular_StringNString> EmployeePODS = Labels;

			List<ReadEmails> readEmailsList = null;
			readEmailsList = ReadEmails.getEmails("exchgen-group11182", "Totalsource1!", "You have an opportunity to enroll in benefits", "HWSupport@adp.com", SuiteContext.StartTime);

			if (CollectionUtils.isEmpty(readEmailsList)) {
				this.context.addResult("Online POD E-mail Verifications", "Online POD E-mail Verifications for Election Confirmations", "E-mail Verifications has to happen", "Please check it manually", RunStatus.FAIL);
				return;
			}
			for(int i=1;i<EmployeePODS.size();i++){
				String employee = EmployeePODS.get(i).Field_1();
				String podNumber = EmployeePODS.get(i).Field_2();
				String client = employee.substring(employee.indexOf("@") + 1, employee.length());
				int temp = 0;
				int mailsize = readEmailsList.size();
				if (!readEmailsList.isEmpty()) {
					for(ReadEmails mail:readEmailsList){
						if(mail.getStrSubject().contains(client)){
							this.context.addResult("Online POD E-mail Verifications", "Online POD E-mail Verifications for Election Confirmations", "E-mail Verifications has to happen", "E-Mail Verification Passed : "+podNumber, RunStatus.PASS);
							break;
						}
						else{
							temp = temp + 1;
						}
					}
					if(mailsize == temp){
						this.context.addResult("Online POD E-mail Verifications", "Online POD E-mail Verifications for Election Confirmations", "E-mail Verifications has to happen", "E-Mail Verification Failed for : "+podNumber, RunStatus.FAIL);
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/*******************
	 * 
	 *	This method is used to go the specified path in the remote machine and copy the file to the local
	 */

	@Then("^COMMON:User should navigate to the remote machine with IP Address \"(.*)\" using Credentails username \"(.*)\" password \"(.*)\" file path \"(.*)\" and get the file with file name \"(.*)\"")
	public void Copyfilefromremote(String machineIP, String Username, String Password,String Filepath, String Filename) {

		try 
		{


			String localMachineIp = context.MachineIP;
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy.HH") ;
			String curDate =dateFormat.format(date);
			List<String> params = Arrays.asList(new String[] { "cmd", "cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+machineIP+" -u ES\\"+Username+" -p "+Password+" XCOPY "+Filepath+""+Filename+"_"+curDate+"_*.xml"+" \\\\"+localMachineIp+"\\C$\\Temp"});
			ProcessBuilder builder = new ProcessBuilder(params);
			Process p = builder.start();
			p.waitFor();
			Thread.sleep(3000);

		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify Xml values of for the tags", "Verify Xml values of for the tags", "Xml Validations Should Happen Successfully","Xml Validations failed with Exception : "+e.getMessage(),RunStatus.FAIL);
		}

	}

	/*******************
	 *	This method is used to execute batch command in the remote machine
	 *
	 * @param filename
	 * @param filepath
	 * @DataTbale xmlData parameters are below
	 * elementXPath, Value
	 *
	 */


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Then("^COMMON:Verify the xml value for given xml xpath")
	public void verfiyXMLData(DataTable xmlTable)
	{
		Document xmlDocument = CommonFunctions.getXmlDocument(this.context.outputXmlFileName);	
		if(xmlDocument == null) {
			this.context.addResult("Xml Read Failed", "Unable to read the XML with file Name"+this.context.outputXmlFileName, "Xml Read from the Remote machine should be Successfull", "Xml Reading from the Remote machine Failed", RunStatus.FAIL);
			return;
		}
		List Labels = new ArrayList();
		for (DataTableRow row : xmlTable.getGherkinRows()){
			Labels.add(new Tabular_StringNString(row.getCells().get(0), row.getCells().get(1)));
		}
		List<Tabular_StringNString> Xmldetails = Labels;
		for(int tableIndex=1;tableIndex<Xmldetails.size();tableIndex++)
		{
			CommonFunctions.verifyXMLData(context, xmlDocument, Xmldetails.get(tableIndex).Field_1(), Xmldetails.get(tableIndex).Field_2());

		}

	}

	/*******************
	 *	This method is used to execute batch command in the remote machine and adde file name to the Context
	 *
	 *  @param Environment 
	 *  @param Client
	 *  @param Exporttype
	 *
	 */

	@Then("^COMMON:Run ACA Export Job in environment \"(.*)\" with Client \"(.*)\"$")
	public void Executebatchcommand(String Environment,String Client) 
	{
		try 
		{
			/***************
			 *  Validate the log file Existing in the local machine
			 *  
			 *  @if Exits delete
			 *  @else Continue
			 */
			boolean logvalidation = false;
			String localMachineIp = context.MachineIP;
			File Logfile = new File("\\\\"+localMachineIp+"\\C$\\Temp\\ACAEXPORT.log");

			if(Logfile.exists())
			{
				Logfile.delete();
			}



			String Exportpath = ACAExportPath;			

			String cmd = null;
			File directory = new File("\\\\"+ACAExportXMLServerIP+Exportpath);
			FileFilter fileFilter = new WildcardFileFilter(Client+Environment+"_ACA_*.xml");
			File[] files = directory.listFiles(fileFilter);

			System.out.println(files.length+"Number of Files before Job");			
			int numberofFilebeforejob = directory.listFiles(fileFilter).length;


			System.out.println(numberofFilebeforejob+" : Number of files before JOB");

			if(Environment.equalsIgnoreCase("LIVE"))
			{
				cmd = "C:\\Base\\Export\\ACAExport.bat "+Client+" bsi@adp "+Environment+"";
			}
			else 
			{
				cmd = "C:\\Base\\Export\\ACAExport.bat "+Client+""+Environment+" bsi@adp "+Environment+"";
			}

			List<String> params = Arrays.asList(new String[] { "cmd", "cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+ACAExportMachineAddress+" -u ES\\"+AdminUsername+" -p "+AdminPassword+" cmd /c C:\\Base\\Export\\SKIPPAUSE.bat ^& cmd /c \""+cmd+" > \\\\"+localMachineIp+"\\C$\\Temp\\ACAEXPORT.log"});
			ProcessBuilder builder = new ProcessBuilder(params);
			System.out.println(params.get(1));
			Process p = builder.start();
			p.waitFor();
			int exitvalue = p.waitFor();

			String line;
			int i=0;
			int breakpoint =0;

			do{
				if(exitvalue == 0){
					String pidInfo ="";
					Process q =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
					BufferedReader input =  new BufferedReader(new InputStreamReader(q.getInputStream()));
					while ((line = input.readLine()) != null) {
						pidInfo+=line; 
					}
					input.close();

					if(pidInfo.contains("PsExec.exe"))
					{
						Thread.sleep(2000);
						breakpoint +=2000;
						if(breakpoint == 300000)
						{
							break;
						}
					}
					else{
						Thread.sleep(15000);
						i++;
					}
				}
			}while(i==0);

			Thread.sleep(10000);


			/***************
			 * Reading the log file for Text Processing Finished
			 * 
			 ******/
			StringBuilder Builder = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(Logfile));
			String nextLine;
			while ((nextLine = br.readLine())!= null)
			{
				if (nextLine.contains("Processing Finished"))
				{
					System.out.println("batch job status: PASS");
					logvalidation = true;
					while ((nextLine = br.readLine())!= null)
					{
						Builder.append(nextLine);
						Builder.append(System.getProperty("line.separator"));
					}
					break;

				}
			}
			br.close();

			
			/*******
			 * Fetching the Files after the 
			 */
			files = directory.listFiles(fileFilter);
			int numberofFileafterjob = directory.listFiles(fileFilter).length;

			System.out.println(numberofFilebeforejob+"Number of Files After the Job");
			
			if(numberofFileafterjob>numberofFilebeforejob && logvalidation)
			{
				
				System.out.println("**************************");
				
				System.out.println(Builder.toString()+"***************Builder");
				
				System.out.println("**************************");
				
				Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
				System.out.println("\nLast Modified Descending Order (LASTMODIFIED_REVERSE)");
				displayFiles(files);
				System.out.println("Job Successfull");

				try 
				{
					this.context.outputXmlFileName= "\\\\"+ACAExportXMLServerIP+Exportpath+files[0].getName();
					
					System.out.println(this.context.outputXmlFileName+"************** File name");

					this.context.addResult("Running ACA Export batch Command", "Running ACA Export batch Command", "ACA Export batch Job Should Happen Successfully","ACA Export batch Job Completed Successfully, Log data : "+Builder.toString(),RunStatus.PASS);
				} catch (Exception e) {

					this.context.addResult("Running ACA Export batch Command", "Running ACA Export batch Command", "ACA Export batch Job Should Happen Successfully","ACA Export batch Job Completed Successfully, Log data : "+Builder.toString(),RunStatus.PASS);
				}

			}
			else
			{
				this.context.addResult("Running ACA Export batch Command", "Running ACA Export batch Command", "ACA Export batch Job Should Happen Successfully","ACA Export batch Job Failed as number of files before and After Job are Same , Log data : "+Builder.toString(),RunStatus.FAIL,"");
			}



		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Verify Xml values of for the tags", "Verify Xml values of for the tags", "Xml Validations Should Happen Successfully","Xml Validations failed with Exception : "+e.getMessage(),RunStatus.FAIL,"");
		}
	}

	public static void displayFiles(File[] files) {
		for (File file : files) {
			System.out.printf("File: %-20s Last Modified:" + new Date(file.lastModified()) + "\n", file.getName());
		}
	}
	
	
	/**
	 * It is the common method to select the client
	 * 
	 */
	@Then("^COMMON:I select the vantage client \"(.*)\"$")
	public void selectVantageClient(String client){
		try {
			if (client.contains("<")) 
				client = PropertiesFile.GetProperty(client.replace("<", "").replace(">", "").trim());
			else if(!client.contains("<"))
				client = PropertiesFile.GetProperty(client);
			if(client!=null)
			{
				if(CommonMethods.selectvantageClient(context, client))
					this.context.addResult("Client Selection: "+client, "Verify ADP Admin should select the client: "+client, "Verfiy ADP Admin should select the client: "+client,"Verified ADP Admin selected the client: "+client,RunStatus.PASS);
				else
					this.context.addResult("Client Selection: "+client, "Verify ADP Admin should select the client: "+client, "Verfiy ADP Admin should select the client: "+client,"Verified ADP Admin not selected the client: "+client,RunStatus.FAIL,Screenshot.getSnap(context, client));
			}
			else
				this.context.addResult("Client Selection: "+client, "Verify ADP Admin should select the client: "+client, "Verfiy ADP Admin should select the client: "+client,"Client is null",RunStatus.FAIL,Screenshot.getSnap(context, "client"));
			
		} catch (Exception e) {
			this.context.addResult("Client Selection: "+client, "Verify ADP Admin should select the client: "+client, "Verfiy ADP Admin should select the client: "+client,"Exception occured while selecting the client: "+client,RunStatus.FAIL,Screenshot.getSnap(context, "client"));
			e.printStackTrace();
		}
	}
}


