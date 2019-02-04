package com.adp.autopay.automation.commonlibrary;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.adp.autopay.automation.framework.Button;
import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.framework.WebEdit;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.Login;
import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.SuiteContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;


import com.adp.autopay.automation.framework.Element;
import com.adp.autopay.automation.pagerepository.Common;

import cucumber.runtime.java.ObjectContainer;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class LoginFunctions extends CustomWebElement {

	public static void UserLogin(ExecutionContext context,String MemberID,String AOID){
		try{
			WaitingFunctions.waitForElement(context, Login.LoginDiv, 10);
			FindElement(context, Login.Practitionertab).click();
			WebEdit.SetText(context, Login.member_id, MemberID);
			Button.Click(context, Login.LogIn);
			WaitingFunctions.WaitUntilPageLoads(context);
			if(Element.IsPresent(context, Login.Version)){
				FindElement(context, Login.Version).click();
				FindElement(context, Login.VersionButton).click();
			}
			//			FindElement(context,By.id("login")).click();
			//			FindElement(context,By.id("login")).clear();
			//			FindElement(context,By.id("login")).sendKeys(username);
			//			FindElement(context,By.id("login")).sendKeys(Keys.TAB);
			//			FindElement(context,By.id("login-pw")).clear();
			//			FindElement(context,By.id("login-pw")).sendKeys(password);
			//			FindElement(context,By.xpath(".//*[@id='Tabgroup1']/form/div/div[2]")).click();
			//            WaitingFunctions.WaitUntilPageLoads(context);

		}catch(Exception e){
			context.addResult("Login", "Login should be successful", "User login has to be successful", "Exception while login", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
			e.printStackTrace();
		}
	}

	public static void ClientSelection_Revit(ExecutionContext context,String strEnvi,String strRole,String strClient){
		try{
			WaitingFunctions.waitForElement(context, Login.ClientSelectPopup, 10);
			clientSelectDropdown(context, "Env", strEnvi);
			clientSelectDropdown(context, "Role", strRole);
			clientSelectDropdown(context, "Client", strClient);
			//		WaitingFunctions.setImplicitWait(context, 2);
			//		FindElement(context, Login.Environment).sendKeys(strEnvi+Keys.TAB);
			//		FindElement(context, Login.Environment).sendKeys(Keys.ENTER);
			//		FindElement(context, Login.Role).clear();
			//		FindElement(context, Login.Role).sendKeys(strRole+Keys.TAB);
			//		FindElement(context, Login.Role).sendKeys(Keys.ENTER);
			//		FindElement(context, Login.Client).sendKeys(strClient+Keys.TAB);
			//		FindElement(context, Login.Client).sendKeys(Keys.ENTER);
			FindElement(context, Login.Done).click();
			//		WaitingFunctions.nullifyImplicitWait(context);
			//		WaitingFunctions.WaitUntilPageLoads(context);

		}catch(Exception e){
			context.addResult("Client Selection", "Client Selection should be successful", "Client Selection has to be successful", "Exception while login", RunStatus.FAIL,Screenshot.getSnap(context, "Client Selection Fail"));
			e.printStackTrace();

			/*************
			 * 
			 * Closing the Test Context If login fails
			 */

			context = ObjectContainer.getInstance(ExecutionContext.class);
			if (!(SuiteContext.SuiteName.equalsIgnoreCase("CommandLineExecutions")))
			{
				context.driver.close();
				context.completeTestExecution();
				context.disconnect();
				ObjectContainer.removeClass(ExecutionContext.class);
			}
		}
	}
	public static void ClientSelection(ExecutionContext context,String strEnvi,String strRole,String strClient){
		try{
			WaitingFunctions.waitForElement(context, Login.ClientSelectPopup, 10);
			clientSelectDropdown(context,"Env",strEnvi);
			clientSelectDropdown(context,"Role",strRole);
			clientSelectDropdown(context,"Client",strClient);
			//		WaitingFunctions.setImplicitWait(context, 2);	
			//		FindElement(context, Login.Environment).sendKeys(strEnvi+Keys.TAB);
			//		FindElement(context, Login.Environment).sendKeys(Keys.ENTER);
			//		FindElement(context, Login.Role).clear();
			//		FindElement(context, Login.Role).sendKeys(strRole+Keys.TAB);
			//		FindElement(context, Login.Role).sendKeys(Keys.ENTER);
			//		FindElement(context, Login.Client).sendKeys(strClient+Keys.TAB);
			//		FindElement(context, Login.Client).sendKeys(Keys.ENTER);
			FindElement(context, Login.Done).click();
			//		WaitingFunctions.nullifyImplicitWait(context);
			//		WaitingFunctions.WaitUntilPageLoads(context);

		}catch(Exception e){
			context.addResult("Client Selection", "Client Selection should be successful", "Client Selection has to be successful", "Exception while login", RunStatus.FAIL,Screenshot.getSnap(context, "Client Selection Fail"));
			e.printStackTrace();
		}
	}

	public static void clientSelectDropdown(ExecutionContext context,String Dropdown,String DrpValue){
		try{

			/****
			 * Waiting till Disabled content is loaded for Client Selection POP UP 
			 */
			WaitingFunctions.WaitUntilLoadcleared(context, By.xpath("//font[contains(text(),'"+Dropdown+"')]//parent::label//following-sibling::div//input[@class='dijitReset dijitInputInner' and @disabled='']"));

			/****
			 * Waiting till loader for Client Selection POP UP is cleared
			 *  
			 ***/			
			WaitingFunctions.WaitUntilLoadcleared(context,Common.ClientSelectionloader);
			
			if(FindElement(context, By.xpath("//input[@id[contains(.,'"+Dropdown.toLowerCase()+"')]]")).getAttribute("value").equalsIgnoreCase(""))
			{
				WaitingFunctions.waitForAnElement(context,By.xpath("//font[contains(text(),'"+Dropdown+"')]//parent::label//following-sibling::div//input[@class='dijitReset dijitInputField dijitArrowButtonInner']"));
				FindElement(context, By.xpath("//font[contains(text(),'"+Dropdown+"')]//parent::label//following-sibling::div//input[@class='dijitReset dijitInputField dijitArrowButtonInner']")).click();

				System.out.println("Drop Down arrow clicked");
				
				Thread.sleep(3000);
				
				WaitingFunctions.waitForElement(context,By.xpath("//div[contains(@id,'"+Dropdown.toLowerCase()+"') and @class='dijitReset dijitMenuItem' and text()='"+DrpValue+"']"),5);
				WebElement elemnet = FindElement(context, By.xpath("//div[contains(@id,'"+Dropdown.toLowerCase()+"') and @class='dijitReset dijitMenuItem' and text()='"+DrpValue+"']"));
				new Actions(context.driver).moveToElement(elemnet).click().build().perform();
				
				JavascriptExecutor executor = (JavascriptExecutor)context.driver;
				executor.executeScript("arguments[0].click();",elemnet);
				
				
				System.out.println(FindElement(context, By.xpath("//label[contains(.,'"+Dropdown+"')]//following::div[@id[contains(.,'"+Dropdown.toLowerCase()+"')]]//input[@role='textbox']")).getAttribute("value"));
			}
			else
			{
				System.out.println("Default value Selected");
			}

			if(FindElement(context, By.xpath("//label[contains(.,'"+Dropdown+"')]//following::div[@id[contains(.,'"+Dropdown.toLowerCase()+"')]]//input[@role='textbox']")).getAttribute("value").equalsIgnoreCase(""))
			{
				/****
				 * Waiting till Disabled content is loaded for Client Selection POP UP 
				 */
				WaitingFunctions.WaitUntilLoadcleared(context, By.xpath("//font[contains(text(),'"+Dropdown+"')]//parent::label//following-sibling::div//input[@class='dijitReset dijitInputInner' and @disabled='']"));

				/****
				 * Waiting till loader for Client Selection POP UP is cleared
				 *  
				 ***/			
				WaitingFunctions.WaitUntilLoadcleared(context,Common.ClientSelectionloader);
				WaitingFunctions.waitForAnElement(context,By.xpath("//font[contains(text(),'"+Dropdown+"')]//parent::label//following-sibling::div//input[@class='dijitReset dijitInputField dijitArrowButtonInner']"));
				FindElement(context, By.xpath("//font[contains(text(),'"+Dropdown+"')]//parent::label//following-sibling::div//input[@class='dijitReset dijitInputField dijitArrowButtonInner']")).click();
				
				Thread.sleep(3000);
				
				WaitingFunctions.waitForElement(context,By.xpath("//div[contains(@id,'"+Dropdown.toLowerCase()+"') and @class='dijitReset dijitMenuItem' and text()='"+DrpValue+"']"),5);
				WebElement elemnet = FindElement(context, By.xpath("//div[contains(@id,'"+Dropdown.toLowerCase()+"') and @class='dijitReset dijitMenuItem' and text()='"+DrpValue+"']"));
				new Actions(context.driver).moveToElement(elemnet).click().build().perform();
				
				JavascriptExecutor executor = (JavascriptExecutor)context.driver;
				executor.executeScript("arguments[0].click();",elemnet);

			}
			else
			{
				System.out.println("Client Selection Success Full");
			}


		}
		catch(Exception e){
			context.addResult(Dropdown+ " dropdown Selection", Dropdown+ " dropdown Selection", Dropdown+" dropdown Selection has to be successful", "Exception while login", RunStatus.FAIL,Screenshot.getSnap(context, "Client Selection Fail"));
			e.printStackTrace();

			/*************
			 * 
			 * Closing the Test Context If login fails
			 */

			context = ObjectContainer.getInstance(ExecutionContext.class);
			if (!(SuiteContext.SuiteName.equalsIgnoreCase("CommandLineExecutions"))) 
			{
				context.driver.close();
				context.completeTestExecution();
				context.disconnect();
				ObjectContainer.removeClass(ExecutionContext.class);
			}
		}

	}
	public static void UserLogin1(ExecutionContext context,String username,String password){
		try{
			FindElement(context,By.id("USER")).click();
			FindElement(context,By.id("USER")).clear();
			FindElement(context,By.id("USER")).sendKeys(username);
			FindElement(context,By.id("USER")).sendKeys(Keys.TAB);
			FindElement(context,By.xpath(".//*[@id='registrationForm']//div[text()='Submit']")).click();
			FindElement(context,By.id("passwordForm:password")).sendKeys(password);
			FindElement(context,By.xpath(".//*[@id='passwordForm']//div[text()='Submit']")).click();
			WaitingFunctions.WaitUntilPageLoads(context);

		}catch(Exception e){
			context.addResult("Login", "Login should be successful", "User login has to be successful", "Exception while login", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
			e.printStackTrace();
		}
	}

	public static boolean EmployeeLoginInVantage(ExecutionContext context,String username,String password){
		try{
			WaitingFunctions.WaitUntilPageLoads(context);
			if(Element.IsPresent(context, Common.VantageUserId))
			{
				By by=Common.VantageUserPaswword;
				WebElement ele = FindElement(context, by);
				ele.clear();
				Actions action = new Actions(context.driver);
				action.sendKeys(password).perform();
				WebEdit.SetText(context, Common.VantageUserId, username);
				Button.Click(context, Common.VantageUserLoginButton);
				Thread.sleep(5000);
				//WaitingFunctions.WaitUntilPageLoads(context);
				return true;
			}
		}catch(Exception e){   
			context.addResult("Login", "Login should be successful", "Admin login has to be successful", "Exception while login and terminating the feature execution", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
			e.printStackTrace();
			System.err.println("Exception Occured while login and terminating the feature execution");
			/*************
			 * 
			 * Closing the Test Context If login fails
			 */
			return false;
		}
		return false;
	}
	
	
	public static boolean EmployeeLoginInVantageNew(ExecutionContext context,String username,String password){
		try{
			WaitingFunctions.WaitUntilPageLoads(context);
			if(Element.IsPresent(context, By.xpath("//form[@class='login-form']")))
			{
				By userBy=By.xpath("//form[@class='login-form']//input[@name='USER']");
				By pwdBy=By.xpath("//form[@class='login-form']//input[@name='PASSWORD']");
				WebEdit.SetText(context, userBy, username);
				WebEdit.SetText(context, pwdBy, password);
				//Button.Click(context, By.xpath("//form[@class='login-form']//button[@id='portal.login.logIn']"));
				Button.Click(context, Common.VantageUserLoginButton);
				Thread.sleep(5000);
				//WaitingFunctions.WaitUntilPageLoads(context);
				return true;
			}
		}catch(Exception e){ 
			context.addResult("Login", "Login should be successful", "Admin login has to be successful", "Exception while login and terminating the feature execution", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
			e.printStackTrace();
			System.err.println("Exception Occured while login and terminating the feature execution");
			/*************
			 * 
			 * Closing the Test Context If login fails
			 */
			return false;
		}
		return false;
	}


	public static void AdminArcotLogin(ExecutionContext context,String username, String password){
		try{
			try{
			if(context.driver.getTitle().equalsIgnoreCase("Self Service Portal"))
			{
				FindElement(context,Common.Adminlogin).click();
			}
			if(ExecutionContext.SRunAgainst.contains("Vantage".toUpperCase())) 
			{
				FindElement(context,Common.Adminlogin).click();
				WebElement ele=FindElement(context, By.xpath(".//*[@id='adminLoginButton']"));
				JavascriptExecutor js= (JavascriptExecutor)context.driver;
				js.executeScript("arguments[0].click();", ele);
			}
			}
			catch(Exception ex){}
			WaitingFunctions.waitForElement(context,By.id("USER"),20);
			FindElement(context, By.id("USER")).sendKeys(username);
			FindElement(context,Common.SubmitRegistrationform).click();
			context.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			try{
				if (FindElement(context,By.id("passwordForm:password")).isDisplayed()){
					FindElement(context,By.id("passwordForm:password")).sendKeys(password);
					Thread.sleep(3000);
					//					FindElement(context,By.xpath(".//div[@class='loginButton']")).click();
					FindElement(context,Common.SubmitPasswordform).click();
					//WaitingFunctions.WaitUntilPageLoads(context);
					//					WaitingFunctions.waitForTextPresent(context, Login.ADP_NavItem, "ADP", 15);
					//					WaitingFunctions.waitForAnElement(context, LoginPage.ClientWebEdit);
				}
			}catch(Exception e){}

			try{
				if(FindElement(context,By.xpath(".//*[@id='passwordForm']/div[1]/table[1]/tbody/tr")).isDisplayed()){
					if(FindElement(context,By.xpath(".//*[@id='passwordForm']/div[1]/table[1]/tbody/tr")).getText().trim().startsWith("Your request cannot be processed at this time")){
						FindElement(context,By.id("passwordForm:computerType:2")).click();
						FindElement(context,By.id("passwordForm:password")).click();
						FindElement(context,By.id("passwordForm:password")).clear();
						FindElement(context,By.id("passwordForm:password")).sendKeys(password);
						Thread.sleep(3000);
						FindElement(context,By.xpath(".//div[@class='loginButton']")).click();
						WaitingFunctions.waitForElement(context, Login.ClientWebEdit, 20);
						//						WaitingFunctions.waitForAnElement(context, LoginPage.ClientWebEdit);
						//						WaitingFunctions.WaitUntilPageLoads(context);
						//					One or more required fields are missing a value.
					}
				}
			}catch(Exception e){}

			try{
				if(FindElement(context,By.xpath(".//*[@id='passwordForm']/div[1]/table[1]/tbody/tr")).isDisplayed()){
					if(FindElement(context,By.xpath(".//*[@id='passwordForm']/div[1]/table[1]/tbody/tr")).getText().trim().startsWith("One or more required fields are missing a value")){
						FindElement(context,By.id("passwordForm:computerType:2")).click();
						FindElement(context,By.id("passwordForm:password")).click();
						FindElement(context,By.id("passwordForm:password")).clear();
						FindElement(context,By.id("passwordForm:password")).sendKeys(password);
						FindElement(context,By.xpath(".//div[@class='loginButton']")).click();
						//						WaitingFunctions.WaitUntilPageLoads(context);
						//						WaitingFunctions.waitForAnElement(context, LoginPage.ClientWebEdit);
						WaitingFunctions.waitForElement(context, Login.ClientWebEdit, 20);
					}
				}
			}catch(Exception e){}

			try{
				if (FindElement(context,By.id("acCode")).isDisplayed()){
					String sActivationCode = FindElement(context,By.xpath(".//*[@id='activationCodeForm']/table/tbody/tr[1]/td")).getText();
					FindElement(context,By.id("acCode")).sendKeys(sActivationCode);
					FindElement(context,By.xpath("//SPAN[@id='revit_form_Button_3']")).click();
					context.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				}
			}catch(Exception e){}

			try{
				if (FindElement(context,By.id("question1")).isDisplayed()){	
					WaitingFunctions.waitForAnElement(context, Login.verifyQuestionsPane);
					FillArcotQuestions (context, By.id("question1"), By.id("answer1")); 
					FillArcotQuestions (context, By.id("question2"), By.id("answer2"));
					FillArcotQuestions (context, By.id("question3"), By.id("answer3"));
					FindElement(context,By.xpath("//SPAN[@id='revit_form_Button_3']")).click();
					context.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				}
			}catch(Exception e){}

			try{
				if (FindElement(context,By.id("widget_password")).isDisplayed()){
					WaitingFunctions.waitForAnElement(context, Login.revit_form_Button_4);
					FindElement(context,By.id("password")).click();
					FindElement(context,By.id("password")).sendKeys(password);
					Thread.sleep(2000);
					FindElement(context,By.xpath("//SPAN[@id='revit_form_Button_4']")).click();
					//					WaitingFunctions.WaitUntilPageLoads(context);
					//					WaitingFunctions.waitForAnElement(context, LoginPage.ClientWebEdit);
					WaitingFunctions.waitForTextPresent(context, Login.ADP_NavItem, "Welcome", 30);
				}
			}catch(Exception e){

			}
			By remindBy=By.xpath("//span[contains(@id,'remindMeLaterButton_label') and contains(.,'Remind Me Later')]");

			System.out.println(" Remind me Later Check");

			if(Element.IsPresent(context, remindBy))
			{
				context.driver.findElement(remindBy).click();
			}
			else
			{
				System.out.println("Element Not present");
			}

			//			context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}catch(Exception e){
			context.addResult("Login", "Login should be successful", "Admin login has to be successful", "Exception while login and terminating the feature execution", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
			e.printStackTrace();
			System.err.println("Exception Occured while login and terminating the feature execution");
			/*************
			 * 
			 * Closing the Test Context If login fails
			 */

			context = ObjectContainer.getInstance(ExecutionContext.class);
			if (!(SuiteContext.SuiteName.equalsIgnoreCase("CommandLineExecutions"))) 
			{
				context.driver.close();
				context.completeTestExecution();
				context.disconnect();
				ObjectContainer.removeClass(ExecutionContext.class);
			}
		}	
	}


	private static void FillArcotQuestions(ExecutionContext context, By byQuestion, By byAnswer){
		try{			
			String strQ = FindElement(context,byQuestion).getText();
			if(strQ.equals("What was your childhood nickname that most people do not know?"))
				FindElement(context,byAnswer).sendKeys("nickname");
			if(strQ.equals("In what city was your father born? (Enter full name of city only)?"))
				FindElement(context,byAnswer).sendKeys("fathercity");
			else if(strQ.equals("In what city was your mother born? (Enter full name of city only)?"))
				FindElement(context,byAnswer).sendKeys("mothercity");

		}catch(Exception e){}

	}

	public static void selectClient(ExecutionContext context,String Client){
		try{
			Actions action = new Actions(context.driver);
			WaitingFunctions.waitForElement(context, Login.ClientWebEdit, 20);
			FindElement(context,Login.ClientWebEdit).click();
			Thread.sleep(5000);
			action.moveToElement(FindElement(context,Login.ClientWebEdit1)).build().perform();
			FindElement(context,Login.ClientWebEdit1).sendKeys(Client);
			WaitingFunctions.waitForAnElement(context, Login.ClientLink);
			if(FindElement(context,Login.ClientLink).isDisplayed()){
				WebElement client = FindElement(context,Login.ClientLink);
				action.moveToElement(client).build().perform();
				client.click();
				WaitingFunctions.waitForAnElement(context, Login.clickPractitionerAcess);
				List<WebElement> list = FindElements(context,Login.clickPractitionerAcess);
				for(WebElement element:list){
					action.moveToElement(element).build().perform();
					if(element.getText().trim().contains("Practitioner Access")){
						element.click();
						break;
					}
				}
				//					WaitingFunctions.waitForAnElement(context, Home_UserHome.HomePage);
				WaitingFunctions.waitForElement(context, Login.Shell, 15);
			}
		}catch(Exception e){
			context.addResult("Client Selection", "Selecting the client", "Client has to be selected", "Client selection Failed" , RunStatus.FAIL,Screenshot.getSnap(context, "SelectClient"));
			e.printStackTrace();
		}
	}
	//	
	//	public static void AdminArcotLogin(ExecutionContext context,String username, String password){
	//		try{
	//			FindElement(context,By.id("TabgroupButton2")).click();
	//			FindElement(context,By.xpath(".//*[@class='adminButton']")).click();
	//			WaitingFunctions.waitForElement(context, LoginPage.UserNameScreen, 20);
	////			WaitingFunctions.waitForAnElement(context, LoginPage.UserNameScreen);
	//			FindElement(context,By.id("USER")).sendKeys(username);
	//			FindElement(context,By.xpath(".//div[@class='loginButton']/div[2]")).click();
	//			
	//			context.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	//			try{
	//				if (FindElement(context,By.id("passwordForm:password")).isDisplayed()){
	//					FindElement(context,By.id("passwordForm:password")).sendKeys(password);
	//					Thread.sleep(3000);
	//					FindElement(context,By.xpath(".//div[@class='loginButton']")).click();
	////					WaitingFunctions.WaitUntilPageLoads(context);
	//					WaitingFunctions.waitForTextPresent(context, LoginPage.ADP_NavItem, "ADP", 15);
	////					WaitingFunctions.waitForAnElement(context, LoginPage.ClientWebEdit);
	//				}
	//			}catch(Exception e){}
	//			
	//			try{
	//				if(FindElement(context,By.xpath(".//*[@id='passwordForm']/div[1]/table[1]/tbody/tr")).isDisplayed()){
	//					if(FindElement(context,By.xpath(".//*[@id='passwordForm']/div[1]/table[1]/tbody/tr")).getText().trim().startsWith("Your request cannot be processed at this time")){
	//						FindElement(context,By.id("passwordForm:computerType:2")).click();
	//						FindElement(context,By.id("passwordForm:password")).click();
	//						FindElement(context,By.id("passwordForm:password")).clear();
	//						FindElement(context,By.id("passwordForm:password")).sendKeys(password);
	//						Thread.sleep(3000);
	//						FindElement(context,By.xpath(".//div[@class='loginButton']")).click();
	//						WaitingFunctions.waitForElement(context, LoginPage.ClientWebEdit, 20);
	////						WaitingFunctions.waitForAnElement(context, LoginPage.ClientWebEdit);
	////						WaitingFunctions.WaitUntilPageLoads(context);
	//	//					One or more required fields are missing a value.
	//					}
	//				}
	//			}catch(Exception e){}
	//			
	//			try{
	//				if(FindElement(context,By.xpath(".//*[@id='passwordForm']/div[1]/table[1]/tbody/tr")).isDisplayed()){
	//					if(FindElement(context,By.xpath(".//*[@id='passwordForm']/div[1]/table[1]/tbody/tr")).getText().trim().startsWith("One or more required fields are missing a value")){
	//						FindElement(context,By.id("passwordForm:computerType:2")).click();
	//						FindElement(context,By.id("passwordForm:password")).click();
	//						FindElement(context,By.id("passwordForm:password")).clear();
	//						FindElement(context,By.id("passwordForm:password")).sendKeys(password);
	//						FindElement(context,By.xpath(".//div[@class='loginButton']")).click();
	////						WaitingFunctions.WaitUntilPageLoads(context);
	////						WaitingFunctions.waitForAnElement(context, LoginPage.ClientWebEdit);
	//						WaitingFunctions.waitForElement(context, LoginPage.ClientWebEdit, 20);
	//					}
	//				}
	//			}catch(Exception e){}
	//			
	//			context.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	//			
	//			try{
	//				if (FindElement(context,By.id("acCode")).isDisplayed()){
	//					String sActivationCode = FindElement(context,By.xpath(".//*[@id='activationCodeForm']/table/tbody/tr[1]/td")).getText();
	//					FindElement(context,By.id("acCode")).sendKeys(sActivationCode);
	//					FindElement(context,By.xpath("//SPAN[@id='revit_form_Button_3']")).click();
	//					context.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	//				}
	//			}catch(Exception e){}
	//			
	//			try{
	//				if (FindElement(context,By.id("question1")).isDisplayed()){	
	//					WaitingFunctions.waitForAnElement(context, LoginPage.verifyQuestionsPane);
	//					FillArcotQuestions (context, By.id("question1"), By.id("answer1")); 
	//					FillArcotQuestions (context, By.id("question2"), By.id("answer2"));
	//					FillArcotQuestions (context, By.id("question3"), By.id("answer3"));
	//					FindElement(context,By.xpath("//SPAN[@id='revit_form_Button_3']")).click();
	//					context.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	//				}
	//			}catch(Exception e){}
	//			
	//			try{
	//				if (FindElement(context,By.id("widget_password")).isDisplayed()){
	//					WaitingFunctions.waitForAnElement(context, LoginPage.revit_form_Button_4);
	//					FindElement(context,By.id("password")).click();
	//					FindElement(context,By.id("password")).sendKeys(password);
	//					Thread.sleep(2000);
	//					FindElement(context,By.xpath("//SPAN[@id='revit_form_Button_4']")).click();
	////					WaitingFunctions.WaitUntilPageLoads(context);
	////					WaitingFunctions.waitForAnElement(context, LoginPage.ClientWebEdit);
	//					WaitingFunctions.waitForTextPresent(context, LoginPage.ADP_NavItem, "ADP", 30);
	//				}
	//			}catch(Exception e){}
	//			
	//			context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	//		}catch(Exception e){
	//			context.addResult("Login", "Login should be successful", "Admin login has to be successful", "Exception while login", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
	//		}	
	//	}
	//	
	//	private static void FillArcotQuestions(ExecutionContext context, By byQuestion, By byAnswer){
	//		try{			
	//			String strQ = FindElement(context,byQuestion).getText();
	//			if(strQ.equals("What was your childhood nickname that most people do not know?"))
	//				FindElement(context,byAnswer).sendKeys("nickname");
	//			if(strQ.equals("In what city was your father born? (Enter full name of city only)?"))
	//				FindElement(context,byAnswer).sendKeys("fathercity");
	//			else if(strQ.equals("In what city was your mother born? (Enter full name of city only)?"))
	//				FindElement(context,byAnswer).sendKeys("mothercity");
	//			
	//		}catch(Exception e){}
	//
	//	}

	public static void ArcotLogin(ExecutionContext context,String UserName,String Password){
		try{
			Actions action = new Actions(context.driver);
			context.driver.findElement(By.id("USER")).sendKeys(UserName);
			context.driver.findElement(By.xpath(".//*[@id='registrationForm']//div[contains(text(),'Submit')]")).click();
			context.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


			try{
				if (context.driver.findElement(By.id("passwordForm:password")).isDisplayed()){
					context.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					Thread.sleep(1000);
					WebEdit.FillWebEdit( context, By.id("passwordForm:password"), Password);
					context.driver.findElement(By.xpath(".//*[text()='Submit']")).click();
				}
			}
			catch(Exception e){}

			try{
				if (context.driver.findElement(By.id("acCode")).isDisplayed()){
					context.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					String sActivationCode = context.driver.findElement(By.xpath(".//*[@id='activationCodeForm']/table/tbody/tr[1]/td")).getText();
					context.driver.findElement(By.id("acCode")).sendKeys(sActivationCode);
					action.moveToElement(context.driver.findElement(By.xpath(".//*[text()='Next']"))).build().perform();
					context.driver.findElement(By.xpath(".//*[text()='Next']")).click();
				}
			}catch(Exception e){}

			try{
				if (context.driver.findElement(By.id("question1")).isDisplayed()){
					FillArcotQuestions (context.driver, By.id("question1"), By.id("answer1"));
					FillArcotQuestions (context.driver, By.id("question2"), By.id("answer2"));
					FillArcotQuestions (context.driver, By.id("question3"), By.id("answer3"));
					context.driver.findElement(By.xpath(".//*[text()='Next']")).click();
				}
			}catch(Exception e){}

			try{
				if (context.driver.findElement(By.id("password")).isDisplayed()){
					context.driver.findElement(By.id("password")).sendKeys(Password);
					Thread.sleep(2000);
					context.driver.findElement(By.xpath(".//*[text()='Done']")).click();
				}

			}catch(Exception e){}

			By remindBy=By.xpath("//span[contains(@id,'remindMeLaterButton_label') and contains(.,'Remind Me Later')]");
			context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
			if(Element.IsPresent(context, remindBy))
			{
				context.driver.findElement(remindBy).click();
			}
			context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS); //Reset wait

		}catch(Exception e){
			e.printStackTrace();
			context.addResult("Login", "Login should be successful", "Admin login has to be successful", "Exception while login", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
			/*************
			 * 
			 * Closing the Test Context If login fails
			 */

			context = ObjectContainer.getInstance(ExecutionContext.class);
			if (!(SuiteContext.SuiteName.equalsIgnoreCase("CommandLineExecutions"))) 
			{
				context.driver.close();
				context.completeTestExecution();
				context.disconnect();
				ObjectContainer.removeClass(ExecutionContext.class);
			}
		}
	}
	private static void FillArcotQuestions(WebDriver driver, By byQuestion, By byAnswer){
		try{
			String strQ = driver.findElement(byQuestion).getText();
			if(strQ.equals("What was your childhood nickname that most people do not know?"))
				driver.findElement(byAnswer).sendKeys("nickname");
			else if(strQ.equals("In what city was your father born? (Enter full name of city only)?"))
				driver.findElement(byAnswer).sendKeys("fathercity");
			else if(strQ.equals("In what city was your mother born? (Enter full name of city only)?"))
				driver.findElement(byAnswer).sendKeys("mothercity");
		}catch(Exception e){

		}
	}

}
