/*
 * 
 */
package com.adp.autopay.automation.teststeps;

import java.util.ArrayList;
import java.util.List;

import com.adp.autopay.automation.commonlibrary.*;
import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.framework.Element;
import com.adp.autopay.automation.framework.RevitTableFunctions;
import com.adp.autopay.automation.pagerepository.Common;
import com.adp.autopay.automation.pagerepository.EnrollmentPage;
import com.adp.autopay.automation.pagerepository.Login;
import com.adp.autopay.automation.pagerepository.PortalPage;
import com.adp.autopay.automation.pagespecificlibrary.EnrollmentWindow;
import com.adp.autopay.automation.tabularParameters.Tabular_StringNString;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;

import gherkin.formatter.model.DataTableRow;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.ObjectContainer;

import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.PropertiesFile;
import com.adp.autopay.automation.pagespecificlibrary.AutoItLogin;

import static com.adp.autopay.automation.commonlibrary.LoginFunctions.clientSelectDropdown;

public class CommonRevSteps extends CustomWebElement {

	ExecutionContext context;
	public static int emp=0;

	public CommonRevSteps() {
		this.context = ((ExecutionContext) ObjectContainer.getInstance(ExecutionContext.class));

	}
	
	/**
	 * This method will login to the application with the given user
	 * @param - user name : values - ADPAdmin,ClientAdmin  
	 */
	/*Provide user name in properties file*/
	@When("COMMON:I login with user \"(.*)\"")
	@Then("COMMONREV:I login with user \"(.*)\"")
	public void loginToPortalAdmin_ICT(String user) throws Exception {
		if (user.contains("<")) 
			user = PropertiesFile.GetProperty(user.replace("<", "").replace(">", "").trim());
		if(user!=null)
		LoginFunctions.AdminArcotLogin(context, user, "adpadp10");
		System.out.println("ArcotDone*****************");
		this.context.addResult("Admin user login: "+user, "Login to the portal as Admin: "+user, "Login to the portal as Admin: "+user, "Logged to the portal as Admin: "+user, RunStatus.PASS);

	}
	
	/**
	 * This method will login to the application with the given user
	 * @param - Environment : values - CTEST,LIVE  
	 */
	/*Provide user name in properties file*/
	@When("COMMONREV:I select Environment:\"(.*)\"")
	public void SelectClientEnvi(String Environment) {
		String client = null;
		WaitingFunctions.waitForElement(context, Login.ClientSelectPopup, 10);
		clientSelectDropdown(context, "Env", Environment);
		FindElement(context, Login.Done).click();
		this.context.addResult("Client selection", "Client selection with given data", "Client slelection should be successful", "Client slelection successful", RunStatus.PASS);

	}
	

	/**
	 * It is the common method used for page verification
	 * It will verify the user is in the $PageTitle page
	 */
	@When("^COMMON:I should be on homepage$")
	@Given("^COMMONREV:I should be on homepage$")
	@Then("^I should be on homepage$")
	public void verifyUserIsIntheHomePage() {
		try {
			WaitingFunctions.WaitUntilPageLoads(context);
			if (PageVerifications.VerifyPage(context, "Home")) {
				this.context.addResult("Verify User is in Home page", "Verify User is in Home Page", "User should be in Home page", "User is in Home page", RunStatus.PASS);
				System.out.println("*** User is in Home page");
				Thread.sleep(1000);
			}
			else
			{
				this.context.addResult("Verify User is in Home page", "Verify User is in Home Page", "User should be in Home page", "Home page is not loaded", RunStatus.FAIL, Screenshot.getSnap(context, "Home page"));
			}
		} catch (Exception e) {
			this.context.addResult("Verify User is in Home page", "Verify User is in Home Page", "User should be in Home page", "Exception Occured", RunStatus.FAIL, Screenshot.getSnap(context, "Home page"));
			e.printStackTrace();
		}
	}

	/**
	 * This method used to select Environment,Role,Client
	 * @param - Environment : values - Configuration,Test,CTest, Live
	 * @param - Role : values - BSI,BenRep...
	 */
	@Then("^COMMON:I select Env:\"(.*)\",Role:\"(.*)\",Client:\"(.*)\"$")
	@When("^COMMONREV:I select Env:\"(.*)\",Role:\"(.*)\",Client:\"(.*)\"$")
	public void SelectClientEnvi(String Environment, String Role, String Client) {
		String client = null;
		if (Client.contains("<")) {
			client = PropertiesFile.GetProperty(Client.replace("<", "").replace(">", "").trim());
		} else {
			client = Client;
		}
		LoginFunctions.ClientSelection(context, Environment, Role, client);
		this.context.addResult("Client selection", "Client selection with given data", "Client slelection should be successful", "Client slelection successful", RunStatus.PASS);

	}
	
	/**
	 * This method used to verify the page is loaded completely
	 * 
	 */
	@Then("COMMONREV:I have to verify the page got Loaded")
	public void VerifyRevitPageLoading() {
		try {
			WaitingFunctions.waitForElement(context, PortalPage.UserSearchArea, 10);
			if (FindElement(context, PortalPage.UserSearchArea).isDisplayed()) {
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
	
	/**
	 * This method will open the Benefits Portal window from Self Service Portal 
	 * @param - user name : values - ADPAdmin,ClientAdmin  
	 */
	@When("COMMONREV:Select Health & Welfare application from the Client Management application List")
	public void SelectBenefitsApplication() {
		try {
			String parentHandle = this.context.driver.getWindowHandle();
			Actions action = new Actions(context.driver);
			WebElement ClientMgmtList = FindElement(context, By.xpath(".//*[@id='menuBar']//div[@id='menuHit.node2']"));
			Locatable MegamenuItem = (Locatable) ClientMgmtList;
			Mouse mouse = ((HasInputDevices) context.driver).getMouse();
			mouse.mouseMove(MegamenuItem.getCoordinates());
			WebElement Application = FindElement(context, By.xpath(".//*[@id='dropMenu']//a[text()='Health & Welfare']"));
			Locatable App = (Locatable) Application;
			mouse.click(App.getCoordinates());
			Thread.sleep(10000);
			System.out.println(this.context.driver.getTitle());
			for (String winHandle : this.context.driver.getWindowHandles()) {
				this.context.driver.switchTo().window(winHandle);
				if (context.driver.getTitle().startsWith("Portal")) {
					//					JavascriptExecutor executor = (JavascriptExecutor)context.driver;
					//					executor.executeScript("arguments[0].click()",context.driver.findElement(By.partialLinkText("Log Off")));
					context.driver.switchTo().window(winHandle);
					context.driver.manage().window().maximize();
					System.out.println("New window Opened");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will open the Benefits Portal window from Self Service Portal 
	 * @param - MegaMenu : megamenu name
	 * @param - SubMenu :  submnenu name 
	 */
	@When("COMMONREV:I navigate to \"(.*)\" -> \"(.*)\"")
	public void Revit_NavigateTo(String MegaMenu, String SubMenu) {
		try {
			if (Navigate.Revit_NavigateTo(context, MegaMenu, SubMenu))
			{
				WaitingFunctions.WaitUntilPageLoads(context);
				if (PageVerifications.VerifyPage(context, SubMenu)) {
					this.context.addResult("Navigate to " + MegaMenu + "->" + SubMenu, "User should navigate to " + MegaMenu + " -> " + SubMenu, "User should Navigate to " + MegaMenu + " -> " + SubMenu, "User Navigated to " + MegaMenu + " -> " + SubMenu, RunStatus.PASS);
				} else {
					this.context.addResult("Navigate to " + MegaMenu + "->" + SubMenu,  "User should navigate to " + MegaMenu + " -> " + SubMenu, "User should Navigate to " + MegaMenu + " -> " + SubMenu, "User failed to navigate to " + MegaMenu + " -> " + SubMenu, RunStatus.FAIL, Screenshot.getSnap(context, "Navigate"));
				}
			}
			else
			{
				this.context.addResult("Navigate to " + MegaMenu + "->" + SubMenu,  "User should navigate to " + MegaMenu + " -> " + SubMenu, "User should Navigate to " + MegaMenu + " -> " + SubMenu, "Error Occured", RunStatus.FAIL, Screenshot.getSnap(context, "Navigate"));
			}
		} catch (Exception e) {
			this.context.addResult("Navigate to " + MegaMenu + "->" + SubMenu,  "User should navigate to " + MegaMenu + " -> " + SubMenu, "User should Navigate to " + MegaMenu + " -> " + SubMenu, "Error Occured", RunStatus.FAIL, Screenshot.getSnap(context, "Navigate"));
		}
	}
	
	/**
	 * It is the common method used for page verification
	 * It will verify the user is in the $PageTitle page
	 */
	@Then("COMMONREV:I should navigate to page \"(.*)\"$")
	@Given("COMMONREV:I am in the page \"(.*)\"$")
	@When("COMMON:I am in the page \"(.*)\"$")
	public void verifyUserIsIntheBenefitsPage(String PageTitle) {
		try {
			WaitingFunctions.WaitUntilPageLoads(context);
			if (PageVerifications.VerifyPage(context, PageTitle)) {

				this.context.addResult("Verify User is in the page:" + PageTitle, "Verify User is in Page: " + PageTitle, "User should be in page: " + PageTitle, "User is in page: " + PageTitle, RunStatus.PASS);
				System.out.println("*** User is in " + PageTitle + " page");
				Thread.sleep(1000);
			}
			else
				this.context.addResult("Verify User is in the page:" + PageTitle, "Verify User is in Page: " + PageTitle, "User should be in page: " + PageTitle, PageTitle+" titile verification failed", RunStatus.FAIL,Screenshot.getSnap(context, PageTitle));
		} catch (Exception e) {
			this.context.addResult("Verify User is in the page:" + PageTitle, "Verify User is in Page: " + PageTitle, "User should be in page: " + PageTitle, "User is not in the " + PageTitle + "page", RunStatus.FAIL, Screenshot.getSnap(context, PageTitle));
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will osearch the PP form the seacrh  
	 * @param - Datatable : Columns
	 * Search Field
	 * Search Value  
	 */
	@SuppressWarnings("unchecked")
	@When("^COMMONREV:I search PP with the criteria:$")
	public void SearchPP_Revit(DataTable SearchPPTable) {
		try {
			@SuppressWarnings("rawtypes")
			List InputData = new ArrayList<>();
			for (DataTableRow row : SearchPPTable.getGherkinRows()) {
				String Field = row.getCells().get(0);
				String Value = row.getCells().get(1);
				InputData.add(new Tabular_StringNString(Field, Value));
			}

			List<Tabular_StringNString> Input = InputData;
			for (int i = 1; i < Input.size(); i++) {
				String EmpField = Input.get(i).Field_1();
				String EmpValue = Input.get(i).Field_2();
				if(EmpValue.contains("*"))
				{
					EmpValue = (EmpValue.replace("<", "").replace(">", "").trim());
				}
				else if(EmpValue.contains("<")) 
				{
					EmpValue = PropertiesFile.GetProperty(EmpValue.replace("<", "").replace(">", "").trim());
				}

				if (EnrollmentWindow.SearchPP(context, EmpField, EmpValue)) {
					this.context.addResult("Search PP", "User has to fill fields and Search", "User should Filled " + EmpValue + " for Search", "User Filled " + EmpValue + " for Search", RunStatus.PASS);
				} else {
					this.context.addResult("Search PP", "User has to fill fields and Search", "User should Filled " + EmpValue + " for Search", "User Failed to Fill " + EmpValue + " for Search", RunStatus.FAIL,Screenshot.getSnap(context, "Search"));
				}
			}
			WebElement ele= Element.FindElement(context, EnrollmentPage.Search);
			JavascriptExecutor executor = (JavascriptExecutor)context.driver;
			executor.executeScript("arguments[0].click();",ele );
			//Element.Click(context, EnrollmentPage.Search);
			WaitingFunctions.WaitUntilLoadcleared(context, Common.TableSpinner);

		} catch (Exception e) {

			if(FindElements(context,Common.NoResultsSearch).size()>0)
			{
				this.context.addResult("Search PP", "User has filled fields and Search", "User should Search table grid ", "Search Successful : No Results found for the Search" , RunStatus.PASS);
			}
			else
			{
				this.context.addResult("Search PP", "SearchPP with the given Criteria", "Search has to be successful", "Search UnSuccessful", RunStatus.FAIL, Screenshot.getSnap(context, "Search"));	
			}

		}
	}
	
	/**
	 * This method will open the Benefits Portal window from Self Service Portal 
	 * @param - user name : values - ADPAdmin,ClientAdmin  
	 */
	@When("^COMMONREV:I click on the Last name of the employee with ID \"(.*)\"$")
	public void ClickEmployee(String EmployeeId) {
		try {

			if (EmployeeId.contains("<")) {
				EmployeeId = PropertiesFile.GetProperty(EmployeeId.replace("<", "").replace(">", "").trim());
			}
			WebElement ElementTable = FindElement(context, Common.EnrollmentsTable);
			int rows = RevitTableFunctions.RowCount(ElementTable);
			int columns = RevitTableFunctions.ColumnCount(ElementTable, 2);
			int count = 0;
			for (int i = 1; i <= rows; i++) {
				for (int j = 1; j <= columns; j++) {
					if (RevitTableFunctions.GetCellData(ElementTable, i, j).equalsIgnoreCase(EmployeeId)) {
						if (RevitTableFunctions.ClickCellData(ElementTable, i, 2)) {
							Thread.sleep(3000);
							System.out.println("*** Employee Selected Successfully");
							this.context.addResult("Select Employee", "Select Employee from the table", "Employee has to be selected with ID: " + EmployeeId, "Employee Selection Successful", RunStatus.PASS);
							return;
						}
					}
				}
			}
			System.out.println("*** Employee with ID not found");
			this.context.addResult("Select Employee", "Select Employee from the table", "Employee has to be selected with ID: " + EmployeeId, "Failed to fetch Employee", RunStatus.FAIL, Screenshot.getSnap(context, "SelectPP"));
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Select Employee", "Select Employee from the table", "Employee has to be selected with ID: " + EmployeeId, "Failed to fetch Employee", RunStatus.FAIL, Screenshot.getSnap(context, "SelectPP"));
		}
	}
	
	/**
	 * This method will login as Employee to the DirectURL 
	 * @param - URLType - Direct url for employee
	 * @param - username 
	 * @param - podNum
	 */
	@Given("^Login to HWSE Standalone with \"(.*)\" URL as employee \"(.*)\" for Online Pod \"(.*)\"$")
	public void LoginBenefitsUrl(String URLType, String username, String podNum) throws Exception {
		try {

			if (username.equalsIgnoreCase("employee")) {
				username = PropertiesFile.GetProperty("Employee");
			}
			String url = null;
			if (URLType.toLowerCase().contains("Direct".toLowerCase())) {
				url = PropertiesFile.GetProperty("DirectUrl");
			}
			Runtime.getRuntime().exec("taskkill /f /s "+context.MachineIP+" /u Es\\hwseauto /p adpADP123 /im cmd.exe");
			Runtime.getRuntime().exec("taskkill /f /s "+context.MachineIP+" /u Es\\hwseauto /p adpADP123 /im LoginEmployee.exe");
			if(emp==0){
				String s="\\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -u ES\\hwseauto -p adpADP123 XCOPY \\\\cdlbenefitsqcre\\base\\Scripts\\BenefitsAutomation\\AutoIt\\LoginEmployee.exe C:\\Temp /Y";
				System.out.println("Copy Cmd "+s);
				Process copyProcess=Runtime.getRuntime().exec("\\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -u ES\\hwseauto -p adpADP123 XCOPY \\\\cdlbenefitsqcre\\base\\Scripts\\BenefitsAutomation\\AutoIt\\LoginEmployee.exe C:\\Temp /Y");
				Thread.sleep(4000);
				//copyProcess.waitFor();
				copyProcess.destroy();
				System.out.println("Copy Process value "+copyProcess.exitValue());
				System.out.println("Copy Done");
				emp++;
			}
			//String Password="adpadp10";
			//Process executeProcess = null;

			/*	

		String FinalOutput[] = new String [10];
		FileInputStream fis = null;
		File f = new File("C:\\Temp\\Output.txt");
		f.createNewFile();
		fis = new FileInputStream("C:\\Temp\\Output.txt");
		Runtime.getRuntime().exec("taskkill /f /s "+context.MachineIP+"  /u Es\\hwseauto /p adpADP123 /im cmd.exe");
		System.out.println("cmd /c \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+"  -u ES\\hwseauto -p adpADP123 cmd /c quser /SERVER:"+context.MachineIP+"  >C:\\Temp\\Output.txt");
		Process copyProcess=Runtime.getRuntime().exec("cmd /c \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -u ES\\hwseauto -p adpADP123 cmd /c quser /SERVER:"+context.MachineIP+"  >C:\\Temp\\Output.txt ");
		copyProcess.waitFor();
		Runtime.getRuntime().exec("taskkill /f /s "+context.MachineIP+"  /u Es\\hwseauto /p adpADP123 /im cmd.exe");
		StringBuilder sb = new StringBuilder();
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		while ((line = br.readLine()) != null) 
		{
			sb.append(line);
		}
		fis.close();
		//f.delete();
		System.out.println(sb.toString().replaceAll(" ",""));
		if(sb.toString().contains("hwseautoconsole"))
		{
			 FinalOutput = sb.toString().replaceAll(" ","").split("hwseautoconsole");	
		}
		else
		{
			 FinalOutput = sb.toString().replaceAll(" ","").split("hwseauto");	
		}
		String session = FinalOutput[1].charAt(0);

		br.close();
			 */

			String Cmd=null;
			if(PropertiesFile.RunningHost.equalsIgnoreCase("Local")){  
				Cmd="cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 2 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\LoginEmployee.exe "+username;
				//executeProcess = Runtime.getRuntime().exec("cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 2 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\AuthenticationPopup.exe " + "Authentication" +" "+username +" "+ Password);
			}

			if(PropertiesFile.RunningHost.equalsIgnoreCase("Remote")){ 
				Cmd="cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k (^C:\\temp\\LoginEmployee.exe ^"+username+"^)";
				System.out.println("PSEXEC command: "+Cmd);
				//executeProcess=Runtime.getRuntime().exec("cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+context.MachineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\AuthenticationPopup.exe " + "Authentication" +" "+username +" "+ Password);
			}
			System.out.println("AutoIt is about to iniitate");
			AutoItLogin login = new AutoItLogin(Cmd);
			Thread t1 = new Thread(login);
			t1.start();
			Thread.sleep(2000);
			System.out.println("Invoke the URL: "+url);
			context.driver.get(url);
			Runtime.getRuntime().exec("taskkill /f /s "+context.MachineIP+" /u Es\\hwseauto /p adpADP123 /im cmd.exe");
			Runtime.getRuntime().exec("taskkill /f /s "+context.MachineIP+" /u Es\\hwseauto /p adpADP123 /im LoginEmployee.exe");

			By remindBy=By.xpath("//a[@class='ADPUI-hlink1' and text()='Remind me later']");
			if(Element.IsPresent(context, remindBy))
				context.driver.findElement(remindBy).click();
			this.context.addResult("Login to Portal on "+podNum, "Employee: "+username+" should login to Portal successfully", "Employee: "+username+" should login to Portal successfully", "Employee: "+username+" logged into Portal successfully", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Login to Portal on "+podNum, "Employee: "+username+" should login to Portal successfully", "Employee: "+username+" should login to Portal successfully","Employee: "+username+" failed to login to Portal", RunStatus.FAIL, Screenshot.getSnap(context, "GetUrl"));
			e.printStackTrace();
		}
	}
	
	

	/**
	 * This method is used to select the given tab
	 *
	 * @param TabName - Name of the Tab
	 */
	@When("^COMMONREV:I navigate to \"(.*)\" tab$")
	@Given("^I click on left navigation tab \"(.*)\"$")
	@Then("^I navigate to \"(.*)\" tab$")
	public void SelectTab(String TabName) {
		try {
			FindElement(context, By.xpath(".//span[text()='" + TabName + "']")).click();
			this.context.addResult("Tab Selected", "Tab selected", "Tab selected", "Tab selected", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Tab Selected", "Tab selected", "Tab selected", "Tab not selected", RunStatus.FAIL, Screenshot.getSnap(context, "TabSelect"));
			e.printStackTrace();
		}

	}
	
	/**
	 * This method is used to click link in any page
	 *
	 * @param LinkName - Name of the link
	 */
	@When("^I click on the Switch ON ADP Experience link$")
	public void clickSwithchlink() {
		try {
			WebElement ele = FindElement(context, Common.VDLuserExperience);
			((JavascriptExecutor) context.driver).executeScript("arguments[0].scrollIntoView(true);", ele);
			FindElement(context, Common.VDLuserExperience).click();
			this.context.addResult("Click on Switch ON ADP Experience link", "Click on Switch ON ADP Experience link", "Switch ON ADP Experience link is clicked successfully", "Switch ON ADP Experience link is clicked successfully", RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Click on Switch ON ADP Experience link", "Click on Switch ON ADP Experience link", "Switch ON ADP Experience link should clicked successfully", "Switch ON ADP Experience link is not clicked", RunStatus.FAIL, Screenshot.getSnap(context, "LinkError"));
			e.printStackTrace();
		}

	}
}


