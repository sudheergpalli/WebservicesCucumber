/*
 * 
 */
package com.adp.autopay.automation.teststeps;

import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.commonlibrary.WaitingFunctions;
import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.utility.PropertiesFile;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.adp.autopay.automation.commonlibrary.LoginFunctions;
import com.adp.autopay.automation.framework.Element;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.Common;
import com.adp.autopay.automation.utility.ExecutionContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.ObjectContainer;

public class CommonVanSteps extends CustomWebElement {


	ExecutionContext context;

	public CommonVanSteps() {
		this.context = ((ExecutionContext) ObjectContainer.getInstance(ExecutionContext.class));
	}
	
	/**
	 * This method will verify the user is in Home Page
	 * @param - user : name of the user
	 */
	/*Provide user name in properties file*/
	
	@Then("^COMMONVAN:I am on vantage \"(.*)\" home page$")
	public void verifyVantageHomePage(String user) {
		try {
			if (user.contains("<")) 
				user = PropertiesFile.GetProperty(user.replace("<", "").replace(">", "").trim());
			else if(!user.contains("<"))
				user = PropertiesFile.GetProperty(user);
			if(user!=null)
			{
				if(WaitingFunctions.waitForAnElement(context, Common.VantageHomePage))
					this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Verified "+user+" is in home page", RunStatus.PASS);
				else
					this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Verified "+user+" home page is not properly loaded", RunStatus.FAIL, Screenshot.getSnap(context, "HomePage"));
			}
			else
				this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Verified "+user+" is null", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
				
		} catch (Exception e) {
			this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Error Occured", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
			e.printStackTrace();
		}
	}
	
	@Then("^COMMONVAN:I am on vantage adp admin \"(.*)\" home page$")
	public void verifyADPVantageHomePage(String user) {
		try {
			if (user.contains("<")) 
				user = PropertiesFile.GetProperty(user.replace("<", "").replace(">", "").trim());
			if(user!=null)
			{
				if(WaitingFunctions.waitForAnElement(context, By.xpath("//span[text()='Company Events' or text()='Company Spotlight']")))
					this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Verified "+user+" is in home page", RunStatus.PASS);
				else
					this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Verified "+user+" home page is not properly loaded", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
			}
			else
			this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Verified "+user+" is null", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));

		} catch (Exception e) {
			this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Error Occured", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will verify the user is in Home Page
	 * @param - user : name of the user
	 */
	/*Provide user name in properties file*/
	
	@Then("^COMMONVAN:I am on vantage VDL \"(.*)\" home page$")
	public void verifyVantageHomePageVDL(String user) {
		try {
			if (user.contains("<")) 
				user = PropertiesFile.GetProperty(user.replace("<", "").replace(">", "").trim());
			WaitingFunctions.WaitUntilPageLoads(context);
			if(user!=null)
			{
				if(WaitingFunctions.waitForAnElement(context, Common.VantageHomePage))
					this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Verified "+user+" is in home page", RunStatus.PASS);
				else
					this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Verified "+user+" home page is not properly loaded", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
			}
			else
				this.context.addResult("Verify user is in home page","Verify user is in home page","Verify user should be in home page", "Verified user is null", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));

		} catch (Exception e) {
			this.context.addResult("Verify "+user+" is in home page","Verify "+user+" is in home page","Verify "+user+" should be in home page", "Error Occured", RunStatus.FAIL,Screenshot.getSnap(context, "HomePage"));
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will login to the Vantage application with the given employee ser
	 * @param - user : employeename  
	 */
	/*Provide user name in properties file*/
	@When("COMMONVAN:I login to vantage as \"(.*)\" employee user")
	public void EmployeeLoginInVantage(String username) {
		if (username.contains("<")) 
			username = PropertiesFile.GetProperty(username.replace("<", "").replace(">", "").trim());
		try {
			if(username!=null)
			{
				if(LoginFunctions.EmployeeLoginInVantage(context, username, "adpadp10"))
					context.addResult("Vantage Employee Login: "+username, "Employee user "+username+" should login to vantage successfully", "Employee user should login to vantage successfully", "Employee user logged into vantage successfully", RunStatus.PASS);
				else if(LoginFunctions.EmployeeLoginInVantageNew(context, username, "adpadp10"))
					context.addResult("Vantage Employee Login: "+username, "Employee user "+username+" should login to vantage successfully", "Employee user should login to vantage successfully", "Employee user logged into vantage successfully", RunStatus.PASS);
				else
					context.addResult("Vantage Employee Login: "+username, "Employee user "+username+" should login to vantage successfully", "Employee user should login to vantage successfully", "Employee user failed to login to vantage successfully", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
			}
			else
				context.addResult("Vantage Employee Login: ", "Employee user should login to vantage successfully", "Employee user should login to vantage successfully", "Verified Employee user is null", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));

		} catch (Exception e) {
			e.printStackTrace();
			context.addResult("Vantage Employee Login: "+username, "Employee user "+username+" should login to vantage successfully", "Employee user should login to vantage successfully", "Exception Occured", RunStatus.FAIL,Screenshot.getSnap(context, "LoginFail"));
		}
	}
	
	/**
	 * This method is used to select the required POD for Vantage Config and OPS tools  
	 *
	 */
	@When("VANTICT:I Select Respective POD from the POD Selection page")
	public void SelectPOD(){
		try{
			String Value = null;
			String Env = ExecutionContext.SRunAgainst.toUpperCase();
			if(Env.contains("PRODUCTION") && Env.contains("POD1")){
				Value = "PRODVANPOD1";
			}
			if(Env.contains("PRODUCTION") && Env.contains("POD2")){
				Value = "PRODVANPOD2";
			}
			if(Env.contains("IAT") && Env.contains("POD1")){
				Value = "IATVANPOD1";
			}
			if(Env.contains("IAT") && Env.contains("POD2")){
				Value = "IATVANPOD2";
			}
			if(Env.contains("UAT") && Env.contains("POD1")){
				Value = "UATVANPOD1";
			}
			if(Env.contains("UAT") && Env.contains("POD2")){
				Value = "UATVANPOD2";
			}
			Select select = new Select(FindElement(context,By.name("selCookie")));
			select.selectByValue(Value);
			Thread.sleep(3000);
			//Element.Click(context, By.xpath(".//*[@id='frmCookie']/input"));
			WebElement ele=Element.FindElement(context, By.xpath("//input[@type='submit']"));
			JavascriptExecutor executor = (JavascriptExecutor)context.driver;
			executor.executeScript("arguments[0].click();",ele );
			Thread.sleep(3000);
			if(this.context.STestName.toLowerCase().contains("config")) {
				Element.Click(context, By.partialLinkText("Vantage Benefits Config"));
			}
			if(this.context.STestName.toLowerCase().contains("ops")) {
				if(Env.contains("PRODUCTION") ||Env.contains("UAT") )
					Element.Click(context, By.partialLinkText("Vantage Benefits Ops"));
				if(Env.contains("IAT"))
					Element.Click(context, By.partialLinkText("Vantage HWSE Ops"));
			}
			WaitingFunctions.waitForElement(context, By.xpath(".//*[@id='USER']"), 10);
			System.out.println("POD Selection Successfull");
			this.context.addResult("POD Selection","Select required POD from available PODS","Select POD: "+Value, "POD Selected Successfully: "+Value ,RunStatus.PASS);
		}catch(Exception e){
			System.out.println("POD Selection Error");
			this.context.addResult("POD Selection","Select POD from available PODS","Select POD Required POD", "POD Selection Failed" ,RunStatus.FAIL,Screenshot.getSnap(context,"POD"));
			e.printStackTrace();
		}
	}
	

	/*******************
	 *	This method is close Suggested Activites
	 */
	@Then("^COMMONVAN:I Close Suggested Activites POPUP on After Data Change$")
	public void CloseSuggestedActivitesPOPUP() {

		try 
		{
			FindElement(context, Common.SuggestedActivitesclose).click();
			this.context.addResult("Suggested Activites Pop UP close", "Suggested Activites Pop UP close", "Suggested Activites Pop UP close", "Suggested Activites Pop UP closed Successfully", 

RunStatus.PASS);
		} catch (Exception e) {
			this.context.addResult("Suggested Activites Pop UP close", "Suggested Activites Pop UP close", "Suggested Activites Pop UP close", "Suggested Activites Pop UP closed Successfully", 

RunStatus.FAIL,Screenshot.getSnap(context, "Suggested Activites"));
		}
	}


}


