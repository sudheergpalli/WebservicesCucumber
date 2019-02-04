/*******************
 *  Red box related functionalities
 *  @author Bheem
 */
package com.adp.autopay.automation.teststeps;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.commonlibrary.WaitingFunctions;
import com.adp.autopay.automation.framework.CustomWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.adp.autopay.automation.framework.Element;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.CommonRbx;
import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.PropertiesFile;
import com.adp.autopay.automation.utility.SuiteContext;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.ObjectContainer;

public class CommonRbxSteps extends CustomWebElement {

	ExecutionContext context;

	public CommonRbxSteps() 
	{
		this.context = ((ExecutionContext) ObjectContainer.getInstance(ExecutionContext.class));
	}



	/***************
	 * This method used to Employee login for Redbox portal
	 * 
	 */
	/*Provide user name in properties file*/
	@Given("COMMONRBX:I login as \"(.*)\" redbox employee user")
	public void EmloyeeLogin(String username) 
	{
		
		WebDriverWait wait = new WebDriverWait(context.driver, 30);
		
		if (username.contains("<")) {
			username = PropertiesFile.GetProperty(username.replace("<", "").replace(">", "").trim());
		}
		else
		{
			username = PropertiesFile.GetProperty(username);
		}
		
		try {
			if(username!=null)
			{
				context.driver.manage().deleteAllCookies();
				context.driver.get(PropertiesFile.GetProperty("RedboxUrl"));
				
				Thread.sleep(5000);
				
				WaitingFunctions.waitForElement(context, CommonRbx.Username, 10);
				
				WebElement Username = wait.until(
						ExpectedConditions.elementToBeClickable(CommonRbx.Username));
				Username.click();
				Username.sendKeys(username);
				
				WebElement Password = wait.until(
						ExpectedConditions.elementToBeClickable(CommonRbx.Password));
				
				WaitingFunctions.waitForElement(context, CommonRbx.Password, 10);
				Password.click();
				Password.sendKeys("adpadp10");
				
				WebElement SignIn = wait.until(
						ExpectedConditions.elementToBeClickable(CommonRbx.SignIn));
				SignIn.click();

				/*********************
				 * nullify implicitlyWait()
				 */

				context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); 
				if(FindElements(context,CommonRbx.Remindmelater).size()>0)
				{
					context.driver.findElement(CommonRbx.Remindmelater).click();
				}

				/*************
				 *  Reset implicit Wait
				 */
				context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS); //Reset wait
				context.addResult("Redbox Employee Login: "+username, "Redbox Employee user "+username+" should login successfully in "+ExecutionContext.SRunAgainst, "Redbox Employee user should login successfully", "Redbox Employee user logged in successfully", RunStatus.PASS);
			}
			else
				context.addResult("Redbox Employee Login: "+username, "Redbox Employee user "+username+" should login successfully in "+ExecutionContext.SRunAgainst, "Redbox Employee user should login successfully", "Redbox Employee user is null", RunStatus.FAIL, Screenshot.getSnap(context, "LoginFail"));

		} catch (Exception e)
		{
			context.addResult("Redbox Employee Login: "+username, "Redbox Employee user "+username+" should login successfully in "+ExecutionContext.SRunAgainst, "Redbox Employee user should login successfully", "Exception Occured while login and terminating the feature execution", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
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

	/**************
	 * This method will verify the Employee is in Redbox Benefits  Home Page
	 */
	@Then("^COMMONRBX:I am on Redbox benefits home page$")
	public void VerifyRedboxHomePage() 
	{

		int numberofwindows =context.driver.getWindowHandles().size();

		try {


			System.out.println("Number of windows before wait"+context.driver.getWindowHandles().size());

			if(context.driver.getWindowHandles().size() == 2)
			{

				for(int i=0;i<60;i++)
				{

					numberofwindows = context.driver.getWindowHandles().size();

					if(numberofwindows == 3)
					{
						System.out.println("Number of windows after wait"+context.driver.getWindowHandles().size());
						break;
					}
					else
					{
						System.out.println("Waiting for the windows");
						Thread.sleep(1000);
						numberofwindows = context.driver.getWindowHandles().size();
					}
				}
			}

			if(numberofwindows == 1)
			{

				if(WaitingFunctions.waitForAnElement(context, CommonRbx.RedbxBenefits,90))
				{
					if(FindElement(context, CommonRbx.Redbxheader).getText().equalsIgnoreCase("Benefits"))
					{
						this.context.addResult("Verify user is in home page","Verify user is in Redbox benefits home page","Verify user should be in Redbox benefits home page", "Verified user is in Redbox benefits home page", RunStatus.PASS);
					}
					else
					{
						WebElement RedbxBenefits = FindElement(context, CommonRbx.RedbxBenefits);
						JavascriptExecutor executor = (JavascriptExecutor)context.driver;
						executor.executeScript("arguments[0].click()",RedbxBenefits);

						/*************
						 * waiting for the load Cleared
						 */
						try 
						{

							WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
						} catch (Exception e) 
						{
							e.printStackTrace();
						}	
						
						if(FindElement(context, CommonRbx.Redbxheader).getText().equalsIgnoreCase("Benefits"))
						{
							System.out.println("Vantge Benefits tab clicked and verified");
							this.context.addResult("Verify user is in home page","Verify user is in Redbox benefits home page","Verify user should be in Redbox benefits home page", "Verified user is in Redbox benefits home page", RunStatus.PASS);
						}
						else
						{
							System.err.println("Vantge Benefots rtab cliecked and verified");
							this.context.addResult("Verify user is in home page","Verify user is in Redbox benefits home page","Verify user should be in Redbox benefits home page", "Verified user Redbox benefits home page is not properly loaded", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
						}
					}
					/*************
					 * waiting for the load Cleared
					 */
					try 
					{

						WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
					} catch (Exception e) 
					{
						e.printStackTrace();
					}		
				}

				else
				{
					this.context.addResult("Verify user is in home page","Verify user is in Redbox benefits home page","Verify user should be in Redbox benefits home page", "Verified user Redbox benefits home page is not properly loaded", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));

				}

			}

			else if(numberofwindows == 3)
			{
				for (String winHandle : this.context.driver.getWindowHandles()) {
					this.context.driver.switchTo().window(winHandle);
				}

				System.out.println(context.driver.getTitle()+"Title");

				if(WaitingFunctions.waitForAnElement(context, CommonRbx.RedbxBenefits))
				{	
					if(FindElement(context,CommonRbx.Redbxheader).getText().equalsIgnoreCase("Benefits"))
					{
						this.context.addResult("Verify user is in Redbox benefits home page","Verify user is in Redbox benefits home page, User navigated to redbox benefits from VDL","Verify user should be in Redbox benefits home page", "Verified user is in Redbox benefits home page", RunStatus.PASS);
					}
					else
					{
						FindElement(context, CommonRbx.RedbxBenefits).click();

						if(FindElement(context, CommonRbx.Redbxheader).getText().equalsIgnoreCase("Benefits"))
						{
							System.out.println("Vantge Benefits tab clicked and verified");
							this.context.addResult("Verify user is in home page","Verify user is in Redbox benefits home page","Verify user should be in Redbox benefits home page", "Verified user is in Redbox benefits home page", RunStatus.PASS);
						}
						else
						{
							System.err.println("Vantge Benefots rtab cliecked and verified");
							this.context.addResult("Verify user is in home page","Verify user is in Redbox benefits home page","Verify user should be in Redbox benefits home page", "Verified user Redbox benefits home page is not properly loaded", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
						}
					}
					/*************
					 * waiting for the load Cleared
					 */
					try 
					{
						WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
					} catch (Exception e) 
					{
						e.printStackTrace();
					}		
				}
				else
				{
					this.context.addResult("Verify user is in home page","Verify user is in Redbox benefits home page, User navigated to redbox benefits from VDL","Verify user should be in Redbox benefits home page", "Verified user Redbox benefits home page is not properly loaded", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
				}

			}

		} catch (Exception e) {
			this.context.addResult("Verify user is in home page","Verify user is in Redbox benefits home page","Verify user should be in Redbox benefits home page", "Error Occured", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
			e.printStackTrace();

		}
	}

	/**************
	 * This method will verify the user is in Home Page
	 * @param - Event : Event Name
	 */

	//@Then("^COMMONRBX:I Click on button for Corressponding \"(.*)\" Event$")
	@Then("^COMMONRBX:I select the event \"(.*)\"$")
	public void ClickCorresspondingEvent(String Eventname) {
		try 
		{	
			WebDriverWait wait = new WebDriverWait(context.driver, 30);

			if(FindElements(context, By.xpath("//div[contains(.,'"+Eventname+"')]//parent::div[@class[contains(.,'benefits-event')]]//button")).size()>0)
			{
				WebElement element = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(.,'"+Eventname+"')]//parent::div[@class[contains(.,'benefits-event')]]//button")));
				element.click();
				this.context.addResult("Verify user select the event: "+Eventname,"Verify user select the event: "+Eventname,"Verify user select the event: "+Eventname+" should be successfull", "Verified user select the event: "+Eventname+" successfully", RunStatus.PASS);
			}
			else if (FindElements(context, By.xpath("//div[contains(.,'"+Eventname+"') and contains(@class,'card-header')]//parent::adp-card-state//button")).size()>0)
			{
				WebElement element = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(.,'"+Eventname+"') and contains(@class,'card-header')]//parent::adp-card-state//button")));
				element.click();
				this.context.addResult("Verify user select the event: "+Eventname,"Verify user select the event: "+Eventname,"Verify user select the event: "+Eventname+" should be successfull", "Verified user select the event: "+Eventname+" successfully", RunStatus.PASS);
			}
			else
			{
				this.context.addResult("Verify user select the event: "+Eventname,"Verify user select the event: "+Eventname,"Verify user select the event: "+Eventname+" should be successfull", "Verified user event: "+Eventname+" selection is not successfull", RunStatus.FAIL,Screenshot.getSnap(context,Eventname));
			} 

			/******************
			 *  waiting for the Loader Cleared
			 */
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}			
		} catch (Exception e) 
		{
			e.printStackTrace();
			this.context.addResult("Verify user select the event: "+Eventname,"Verify user select the event: "+Eventname,"Verify user select the event: "+Eventname+" should be successfull", "Exception occored while user selecting the event: "+Eventname,RunStatus.FAIL,Screenshot.getSnap(context, Eventname));
		}
	}



	/**************
	 * This method will verify the user is in Home Page
	 * @param - Election Type : Election
	 */
	@Then("^COMMONRBX:I select the benefit area \"(.*)\"$")
	public void clickElectionButton(String Election) {
		try 
		{

			WebElement elec=FindElement(context, By.xpath("//div[@title[contains(.,'"+Election+"')]]//parent::div[@class[contains(.,'election-card election-card')]]//button"));
			if(elec.isDisplayed())
			{
				elec.click();
				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
				this.context.addResult("Verify user select the benefit area: "+Election,"Verify user select the benefit area: "+Election,"Verify user select the benefit area: "+Election+" should be successfull", "Verified user select the benefit area: "+Election+" successfully", RunStatus.PASS);	
			}
			else
				this.context.addResult("Verify user select the benefit area: "+Election,"Verify user select the election: "+Election,"Verify user select the benefit area: "+Election+" should be successfull", "Verified user benefit area: "+Election+" selection is not successfull",RunStatus.FAIL,Screenshot.getSnap(context, Election));
			/******************
			 *  waiting for the Loader Cleared
			 */
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		} 
		catch (Exception e) 
		{
			this.context.addResult("Verify user select the benefit area: "+Election,"Verify user select the election: "+Election,"Verify user select the benefit area: "+Election+" should be successfull", "Exception occored while user selecting the benefit area: "+Election,RunStatus.FAIL,Screenshot.getSnap(context, Election));
		}
	}


	/**************
	 * This method will verify the user is in Home Page
	 * @param - plan Type : planname
	 */

	@Then("^COMMONRBX:I Select a plan with planname \"(.*)\"$")
	public void clickplanButton(String planname) {
		try 
		{
			new Actions(context.driver).moveToElement(FindElement(context, CommonRbx.AvailablePlans)).doubleClick().build().perform();
			WaitingFunctions.waitForAnElement(context,By.xpath("//h4[contains(.,'"+planname+"')]//parent::div[@class[contains(.,'plan-head')]]//parent::div[@class[contains(.,'planColumn')]]//span[contains(.,'View Details')]"));
			FindElement(context, By.xpath("//h4[contains(.,'"+planname+"')]//parent::div[@class[contains(.,'plan-head')]]//parent::div[@class[contains(.,'planColumn')]]//span[contains(.,'SELECT') or contains(.,'Select')]//parent::button")).click();
			this.context.addResult("Verify Button Click for planname:"+planname,"Verify Button Click for planname:"+planname,"Verify Button Click for Election:"+planname+" Should be Successfull", "Verified Button Click for Election:"+planname+" is Successfull", RunStatus.PASS);

			/******************
			 *  waiting for the Loader Cleared
			 */
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		} 
		catch (Exception e) 
		{
			this.context.addResult("Verify Button Click for Election:"+planname,"Verify Button Click for Election:"+planname,"Verify Button Click for Election:"+planname+" Should be Successfull", "Verified Button Click for Event:"+planname+" is not Successfull ended with Exception"+e.getMessage(),RunStatus.FAIL,Screenshot.getSnap(context, "Election Button"));
		}
	}



	/**************
	 * This method will verify the user is in Home Page
	 * @param - plan Type : planname
	 */

	@Then("^COMMONRBX:I Select a plan$")
	public void Selectplan() {
		try 
		{

			WebElement Availableplan = FindElement(context, CommonRbx.AvailablePlans);
			if(Availableplan.isDisplayed())
			{
				Availableplan.click();
			}
			else
			{
				this.context.addResult("Verify Availableplans Selection","Verify Availableplans Selection","Verify Availableplans Selection Should be Successfull", "Verified Availableplans Selection is not Successfull",RunStatus.FAIL,Screenshot.getSnap(context, "Availbaleplans"));
			}
			WaitingFunctions.waitForAnElement(context,CommonRbx.Defaultviewdetails);
			WebElement plan = FindElement(context, CommonRbx.PlanSelection);
			if(plan.isDisplayed())
			{
				plan.click();
				this.context.addResult("Verify plan Selection","Verify plan Selection","Verify plan Selection Should be Successfull", "Verified plan Selection is Successfull", RunStatus.PASS);
			}
			else
			{
				this.context.addResult("Verify plan Selection","Verify plan Selection","Verify plan Selection Should be Successfull", "Verified plan Selection is not Successfull",RunStatus.FAIL,Screenshot.getSnap(context, "planselection"));
			}
			/******************
			 *  waiting for the Loader Cleared
			 */
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		} 
		catch (Exception e) 
		{
			this.context.addResult("Verify Button Click for Election","Verify Button Click for Election","Verify Button Click for Election Should be Successfull", "Verified Button Click for Event is not Successfull ended with Exception",RunStatus.FAIL,Screenshot.getSnap(context, "Election Button"));
		}
	}



	/**************
	 * This method will verify the user is in Home Page
	 *
	 */

	@Then("^COMMONRBX:I Click on Save And Continue$")
	public void ClickSaveandContinue() {
		try 
		{
			
			WebElement SaveandContinue = FindElement(context, CommonRbx.Saveandcontinue);
			if(SaveandContinue.isDisplayed())
			{
				SaveandContinue.click();
				this.context.addResult("Verify Save and Continue to Benefits Page","Verify Save and Continue to Benefits selection","Verify Save and Continue to Benefits selection Should be Successfull", "Verified Save and Continue to Benefits selection is Successfull", RunStatus.PASS);
			}
			else
			{
				this.context.addResult("Verify Save and Continue to benefits","Verify Save and Continue to Benefits selection","Verify Save and Continue to Benefits selection Should be Successfull", "Verified Save and Continue to Benefits selection is not Successfull",RunStatus.FAIL,Screenshot.getSnap(context, "SaveandContinue"));
			}

			/******************
			 *  waiting for the Loader Cleared
			 */
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		} 
		catch (Exception e) 
		{
			this.context.addResult("Verify Save and Continue to Benefits","Verify Save and Continue to Benefits selection","Verify Save and Continue Should be Successfull", "Verified Save and Continue is not Successfull, exception occured",RunStatus.FAIL,Screenshot.getSnap(context, "SaveandContinue"));
		}
	}

	/**************
	 * This method will verify the user is in Home Page
	 *
	 */

	@Then("^COMMONRBX:I Click on Save And Return To Benefits$")
	public void ClickSaveandreturn() {
		try 
		{
			
			
			WebElement SaveandReturn = FindElement(context, CommonRbx.Saveandreturn);
			if(SaveandReturn.isDisplayed())
			{
				SaveandReturn.click();
				this.context.addResult("Verify Save and Return to Benefits Page","Verify Save and Return to Benefits selection","Verify Save and Return to Benefits selection Should be Successfull", "Verified Save and Return to Benefits selection is Successfull", RunStatus.PASS);
			}
			else
			{
				this.context.addResult("Verify Save and Return to Benefits Page","Verify Save and Return to Benefits selection","Verify Save and Return to Benefits selection Should be Successfull", "Verified Save and Return to Benefits selection is not Successfull",RunStatus.FAIL,Screenshot.getSnap(context, "SaveandReturn"));
			}


			/******************
			 *  waiting for the Loader Cleared
			 */	
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}	
		} 
		catch (Exception e) 
		{
			this.context.addResult("Verify Save and Return to Benefits Page","Verify Save and Return to Benefits selection","Verify Save and Return to Benefits selection Should be Successfull", "Verified Save and Return to Benefits selectionis not Successfull, exception occured",RunStatus.FAIL,Screenshot.getSnap(context, "SaveandContinue"));
		}
	}

	/**************
	 * This method will verify the user is in Home Page
	 *
	 */

	@Then("^COMMONRBX:I click on Complete Enrollment$")
	public void ClickCompleteEnrollment() {
		try 
		{
			
			
			/******************
			 *  waiting for the Loader Cleared
			 */	
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}	
			
			
			WebElement CompleteEnrollment = FindElement(context, CommonRbx.CompleteEnrollment);
			if(CompleteEnrollment.isDisplayed())
			{
				CompleteEnrollment.click();
				this.context.addResult("Verify CompleteEnrollment Selection","Verify CompleteEnrollment Selection","Verify CompleteEnrollment Selection Should be Successfull", "Verified CompleteEnrollment Selection is Successfull", RunStatus.PASS);
			}
			else
			{
				this.context.addResult("Verify CompleteEnrollment Selection","Verify CompleteEnrollment Selection","Verify CompleteEnrollment Selection Should be Successfull", "Verified CompleteEnrollment Selection is not Successfull",RunStatus.FAIL,Screenshot.getSnap(context, "SaveandReturn"));
			}

			/******************
			 *  waiting for the Loader Cleared
			 */
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		} 
		catch (Exception e) 
		{
			this.context.addResult("Verify CompleteEnrollment Selection","Verify CompleteEnrollment Selection","Verify CompleteEnrollment Selection Should be Successfull", "Verified CompleteEnrollment Selection is not Successfull, exception occured",RunStatus.FAIL,Screenshot.getSnap(context, "CompleteEnrollment"));
		}
	}


	/**************
	 * This method will verify the user is in Home Page
	 *
	 */

	@Then("^COMMONRBX:I click on Confirm Enrollment$")
	public void ClickConfirmEnrollment() {
		try 
		{
			
			
			WebElement ConfirmEnrollment = FindElement(context, CommonRbx.ConfirmEnrollment);
			if(ConfirmEnrollment.isDisplayed())
			{
				ConfirmEnrollment.click();
				this.context.addResult("Verify ConfirmEnrollment Selection","Verify ConfirmEnrollment Selection","Verify ConfirmEnrollment Selection Should be Successfull", "Verified ConfirmEnrollment Selection is Successfull", RunStatus.PASS);
			}
			else
			{
				this.context.addResult("Verify ConfirmEnrollment Selection","Verify ConfirmEnrollment Selection","Verify ConfirmEnrollment Selection Should be Successfull", "Verified ConfirmEnrollment Selection is not Successfull",RunStatus.FAIL,Screenshot.getSnap(context, "SaveandReturn"));
			}

			/******************
			 *  waiting for the Loader Cleared
			 */
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		} 
		catch (Exception e) 
		{
			this.context.addResult("Verify ConfirmEnrollment Selection","Verify ConfirmEnrollment Selection","Verify ConfirmEnrollment Selection Should be Successfull", "Verified ConfirmEnrollment Selection is not Successfull, exception occured",RunStatus.FAIL,Screenshot.getSnap(context, "ConfirmEnrollment"));
		}
	}


	/**************
	 * This method will verify the user is in Home Page
	 *
	 */

	@Then("^COMMONRBX:I click on Print Confirmation$")
	public void ClickPrintconfirmation() {
		try 
		{
			
			WebElement Printconfirmation = FindElement(context, CommonRbx.Printconfirmation);
			if(Printconfirmation.isDisplayed())
			{
				Printconfirmation.click();
				this.context.addResult("Verify Printconfirmation Selection","Verify Printconfirmation Selection","Verify Printconfirmation Selection Should be Successfull", "Verified Printconfirmation Selection is Successfull", RunStatus.PASS);
			}
			else
			{
				this.context.addResult("Verify Printconfirmation Selection","Verify Printconfirmation Selection","Verify Printconfirmation Selection Should be Successfull", "Verified Printconfirmation Selection is not Successfull",RunStatus.FAIL,Screenshot.getSnap(context, "SaveandReturn"));
			}

			/******************
			 *  waiting for the Loader Cleared
			 */
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		} 
		catch (Exception e) 
		{
			this.context.addResult("Verify Printconfirmation Selection","Verify Printconfirmation Selection","Verify Printconfirmation Selection Should be Successfull", "Verified Printconfirmation Selection is not Successfull, exception occured",RunStatus.FAIL,Screenshot.getSnap(context, "Printconfirmation"));
		}
	}


	@Then("^COMMONRBX:I have to View the Report in a new window and I have to verify the title$")
	public void VerifyConfirmationReportRev() {
		try {
			Thread.sleep(10000);
			String parentHandle = this.context.driver.getWindowHandle();

			for (String winHandle : this.context.driver.getWindowHandles()) {
				this.context.driver.switchTo().window(winHandle);
				if(this.context.driver.getTitle().equalsIgnoreCase("Benefits")){
					parentHandle = this.context.driver.getWindowHandle();
				}
				while (!(this.context.driver.getTitle().contains("Benefits")) && !(this.context.driver.getTitle().contains("Portal")) && !(this.context.driver.getTitle().contains("ADP")))
				{
					if(this.context.browser.equalsIgnoreCase("IE"))
					{
						if(this.context.driver.getCurrentUrl().contains("Election Summary"))
						{
							System.out.println("*** Report Verification Successful");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "PDF Opened Successfully", RunStatus.PASS);
							this.context.driver.close();
							this.context.driver.switchTo().window(parentHandle);
						}
						else
						{
							System.out.println("*** Report Verification failed");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "Report Verification Failed", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyPDF"));
							this.context.driver.close();
							this.context.driver.switchTo().window(parentHandle);
							return;
						}
					}
					else if(context.browser.equalsIgnoreCase("firefox"))
					{

						WaitingFunctions.waitForAnElement(context,CommonRbx.Electionconfirmation);
						if(this.context.driver.getTitle().trim().contains("Election")) {
							System.out.println("*** Report Verification Successful");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "PDF Opened Successfully", RunStatus.PASS);
							this.context.driver.close();
							this.context.driver.switchTo().window(parentHandle);
							return;
						} else {
							System.out.println("*** Report Verification failed");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "Report Verification Failed", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyPDF"));
							this.context.driver.close();
							this.context.driver.switchTo().window(parentHandle);
							return;
						}
					}

					else 
					{
						if(this.context.driver.getTitle().trim().contains("Election")) {
							System.out.println("*** Report Verification Successful");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "PDF Opened Successfully", RunStatus.PASS);
							this.context.driver.close();
							this.context.driver.switchTo().window(parentHandle);
							return;
						} else {
							System.out.println("*** Report Verification failed");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "Report Verification Failed", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyPDF"));
							this.context.driver.close();
							this.context.driver.switchTo().window(parentHandle);
							return;
						}
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "Report is unsuccessful", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyPDF"));
		}
	}


	@Then("^COMMONRBX:I have to verify decision support data$")
	public void VerifydecsionSupprotdata() 
	{
		try {

			WaitingFunctions.waitForAnElement(context,CommonRbx.Defaultviewdetails);
			WebElement ViewDetails = FindElement(context, CommonRbx.ViewDetails);

			if(ViewDetails.isDisplayed())
			{
				ViewDetails.click();
				
				if(FindElements(context,CommonRbx.ViewDeatilsTabledata).size()>0)
				{
					System.out.println("*** Decision Support data validation Successful");
					this.context.addResult("Decision Support data Verification", "Verify Decision Support data is available with different options", "Verify Decision Support data is available with different options", "Verified Decision Support data is available with different options", RunStatus.PASS);
				}
				else
				{
					System.out.println("*** Report Verification failed");
					this.context.addResult("Decision Support data Verification", "Verify Decision Support data is available with different options", "Verify Decision Support data is available with different options", "Verified Decision Support data is not available with different options", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyDSS"));
					return;
				}
			}
			else
			{
				System.out.println("*** Report Verification failed");
				this.context.addResult("Decision Support data Verification", "Verify Decision Support data is available with different options", "Verify Decision Support data is available with different options", "Verified Decision Support data is not available with different options", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyDSS"));
				return;	
			}

			


		} catch (Exception e)
		{
			e.printStackTrace();
			this.context.addResult("Decision Support data Verification", "Verify Decision Support data is available with different options", "Verify Decision Support data is available with different options", "Exception occured whine verifying Decision Support data", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyDSS"));
		}
	}


	/**************
	 * This method will verify the user is in Home Page
	 *
	 */

	@Then("^COMMONRBX:I switch to Admin page$")
	public void SwitchtodefaultContent() {
		try 
		{
			String Adminwindow = "";
			/******************
			 * Switc to admin page
			 */
			for (String winHandle : this.context.driver.getWindowHandles()) 
			{
				this.context.driver.switchTo().window(winHandle);

				System.out.println(this.context.driver.getTitle());

				if(this.context.driver.getTitle().equalsIgnoreCase("ADP Benefits"))
				{
					Adminwindow = winHandle;
				}
				else
				{
					this.context.driver.close();
				}
			}

			this.context.driver.switchTo().window(Adminwindow);
			this.context.addResult("Switch to Admin page","Admin page Navigation Successfull","Navigation to Admin page Should be Successfull", "Navigation to Admin page is Successfull",RunStatus.PASS);

		} 
		catch (Exception e) 
		{
			this.context.addResult("Verify Button Click for Printconfirmation","Verify Button Click for Printconfirmation","Button Click for Printconfirmation Should be Successfull", "Button Click for Printconfirmation is not Successfull, exception occured"+e.getMessage(),RunStatus.FAIL,Screenshot.getSnap(context, "Printconfirmation"));
		}
	}


	@Then("COMMONRBX:I am on Vantage Redbox benefits home page")
	public void VerifyVantageRedboxHomePage() 
	{
		try{
			By benefitsTabBy=By.xpath("//div[@id='framework-container']//span[@class='txt ng-binding' and text()='Benefits']");
			
			WaitingFunctions.waitForElement(context, benefitsTabBy, 30);
			Element.Click(context, benefitsTabBy);
			WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			By benefitsHeaderby=By.xpath("//div[@id='framework-container']//span[@class='header-text-style ng-binding' and text()='Benefits']");
			if(Element.IsPresent(context, benefitsHeaderby))
			{
				System.out.println("Vantge Benefots rtab cliecked and verified");
				this.context.addResult("Switch to Admin page","Admin page Navigation Successfull","Navigation to Admin page Should be Successfull", "Navigation to Admin page is Successfull",RunStatus.PASS);
			}
			else
			{
				System.err.println("Vantge Benefots rtab cliecked and verified");
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

	@Then("^COMMONRBX:I click on the open event to make changes$")
	public void makeChangestoEvent() {
		String eventName=null;
		try 
		{	
			WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			WaitingFunctions.waitForAnElement(context,CommonRbx.RedbxBenefits);
			//By eventBy= By.xpath("//div[@id='framework-container']//feature-event//div[@class='landing-main-img-title ng-binding']");
			By eventBy= By.xpath("//div[@id='framework-container']//feature-event//div[@class='landing-main-img-title ng-binding' or @class='card-header ng-binding']");
			By makehangesby=By.xpath("//div[@id='framework-container']//feature-event//button[contains(@class,'btn btn-secondary')]");
			WaitingFunctions.waitForElement(context, eventBy, 10);
			List<WebElement> eventsList=FindElements(context, eventBy);
			List<WebElement> eventChangesList=FindElements(context, makehangesby);
			
			if(eventsList.size()>0 && eventChangesList.size()>0)
			{
				eventName=eventsList.get(0).getText();
				eventChangesList.get(0).click();
				System.out.println("I click on the event "+eventName+" to make changes ");
				this.context.addResult("Make Changes to Event: "+eventName,"Verify Make Changes to Event: "+eventName+"is clicked","Verify Make Changes to Event: "+eventName+" should be clicked successfully","Verify Make Changes to Event: "+eventName+"is clicked successfully", RunStatus.PASS);
			}
			/*else if(eventsList.size()>1 && eventChangesList.size()>1)
			{
				this.context.addResult("Make Changes to Event: "+eventName,"Verify Make Changes to Event: "+eventName+"is clicked","Verify Make Changes to Event: "+eventName+" should be clicked successfully","Verified Make Changes to Event: "+eventName+"is not clicked successfully because of mutiple events", RunStatus.FAIL,Screenshot.getSnap(context,"Event Button"));
			}*/
			WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
		}
		catch(Exception ex)
		{
			this.context.addResult("Make Changes to Event: "+eventName,"Verify Make Changes to Event: "+eventName+"is clicked","Verify Make Changes to Event: "+eventName+" should be clicked successfully","Exception Occured", RunStatus.FAIL,Screenshot.getSnap(context,"Event Button"));
		}
	}
	
	/**************
	 * This method will verify the user is in Home Page
	 *
	 */

	@Then("^COMONRBX:I navigate back to Benefits page$")
	public void NavigateBenefitsHome() {
		try 
		{
			WebElement BenefitsHome = FindElement(context, CommonRbx.BenefitsHome);
			JavascriptExecutor executor = (JavascriptExecutor)context.driver;
			executor.executeScript("arguments[0].click()",BenefitsHome);
			this.context.addResult("Verify Navigation to Benefits Home Page","Verify Navigation to Benefits Home Page","Verify Navigation to Benefits Home Page Should be Successfull", "Verified Navigation to Benefits Home Page is Successfull", RunStatus.PASS);
			/******************
			 *  waiting for the Loader Cleared
			 */
			try 
			{

				WaitingFunctions.WaitUntilLoadcleared(context, CommonRbx.Redbxloader);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			this.context.addResult("Verify Navigation to Benefits Home Page","Verify Navigation to Benefits Home Page","Verify Navigation to Benefits Home Page Should be Successfull", "Verified Navigation to Benefits Home Page is not Successfull, exception occured",RunStatus.FAIL,Screenshot.getSnap(context, "Printconfirmation"));
		}
	}

}


