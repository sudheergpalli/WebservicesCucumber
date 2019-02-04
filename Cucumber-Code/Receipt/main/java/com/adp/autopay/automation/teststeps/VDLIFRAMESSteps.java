/*
 * 
 */
package com.adp.autopay.automation.teststeps;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.commonlibrary.WaitingFunctions;
import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.framework.Element;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.Common;
import com.adp.autopay.automation.utility.ExecutionContext;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.ObjectContainer;

public class VDLIFRAMESSteps extends CustomWebElement {


	ExecutionContext context;

	public VDLIFRAMESSteps() {
		this.context = ((ExecutionContext) ObjectContainer.getInstance(ExecutionContext.class));
	}
	
	/**
	 * This method will switch to IFrames (REVUI)
	 */
	@Then("VDLIFRAME:I switch to IFRAME page")
	public void navigatetoIFrame() {
		try {
			//WaitingFunctions.WaitUntilPageLoads(context);
			WaitingFunctions.waitforPageLoad(context);
			context.driver.switchTo().defaultContent();
			By iframeBy=By.xpath("//iframe[@id='portalIFrame_iframe']");
			WebElement iframe=null;
			if(Element.IsPresent(context, iframeBy))
			{
				iframe=Element.FindElement(context,iframeBy );
				context.driver.switchTo().frame(iframe);
				System.out.println("PASS:Switched to IFrame");
			}
			else
			{
				System.err.println("FAIL: Switched to IFrame");
			}

		} catch (Exception e) {
			System.err.println("Error: Switched to IFrame");
			e.printStackTrace();
		}
	}

	/**
	 * This method will switch to VDLFrames (VDL)
	 */
	@Then("VDLIFRAME:I switch to VDL page")
	public void navigatetoVDL() {
		try {
			WaitingFunctions.WaitUntilPageLoads(context);
			context.driver.switchTo().defaultContent();
		} catch (Exception e) {
			System.err.println("Error: Switched to IFrame");
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to navigate to the required Page for Admin Users in Vantage & Standalone
	 * @param menuItem: Page to which user should navigate
	 */
	@Given("COMMONVDL:I Navigate as Admin to \"(.*)\" Page")
	public void navigateToPageAdmin(String menuItem) {
		try {
			JavascriptExecutor js=(JavascriptExecutor) context.driver;
			By navItem=By.xpath(".//div[@class='dijitTabPaneWrapper dijitTabContainerLeft-container']//span[text()='"+menuItem+"']");
			List<WebElement> ele=Element.FindElements(context, navItem);
			if(ele.size()==1)
			{
				js.executeScript("arguments[0].click();", ele.get(0));
				WaitingFunctions.WaitUntilPageLoads(context);
				String header=Element.getText(context, Common.PageHeadings);
				if(menuItem.equalsIgnoreCase(header))
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.PASS);
				else
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.FAIL,Screenshot.getSnap(context, menuItem));
			}
			else if(ele.size()==2)
			{
				js.executeScript("arguments[0].click();", ele.get(1));
				WaitingFunctions.WaitUntilPageLoads(context);
				String header=Element.getText(context, Common.PageHeadings);
				if(menuItem.equalsIgnoreCase(header))
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.PASS);
				else
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.FAIL,Screenshot.getSnap(context, menuItem));
			}
			else if(ele.size()==3)
			{
				js.executeScript("arguments[0].click();", ele.get(2));
				WaitingFunctions.WaitUntilPageLoads(context);
				String header=Element.getText(context, Common.PageHeadings);
				if(menuItem.equalsIgnoreCase(header))
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.PASS);
				else
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.FAIL,Screenshot.getSnap(context, menuItem));
			}
				
		} catch (Exception e) {
			this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "Exception occured", RunStatus.FAIL,Screenshot.getSnap(context, menuItem));
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to navigate to the required Page for Employee Users and CLient Admin as Employee for Vantage and Admin in Standalone
	 * @param menuItem: Page to which user should navigate
	 */
	@When("COMMON:I Navigate to \"(.*)\" Page")
	@Given("COMMONVDL:I Navigate as Employee to \"(.*)\" Page")
	public void navigateToPageEmployee(String menuItem) {
		try {
			JavascriptExecutor js=(JavascriptExecutor) context.driver;
			By navItem=By.xpath(".//div[@class='dijitTabPaneWrapper dijitTabContainerLeft-container']//span[text()='"+menuItem+"']");
			List<WebElement> ele=Element.FindElements(context, navItem);
			if(ele.size()==1)
			{
				js.executeScript("arguments[0].click();", ele.get(0));
				WaitingFunctions.WaitUntilPageLoads(context);
				String header=Element.getText(context, Common.PageHeadings);
				if(menuItem.equalsIgnoreCase(header))
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.PASS);
				else
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.FAIL,Screenshot.getSnap(context, menuItem));
			}
			else if(ele.size()>1)
			{
				js.executeScript("arguments[0].click();", ele.get(0));
				WaitingFunctions.WaitUntilPageLoads(context);
				String header=Element.getText(context, Common.PageHeadings);
				if(menuItem.equalsIgnoreCase(header))
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.PASS);
				else
					this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "User navigated to: " + header + " Page", RunStatus.FAIL,Screenshot.getSnap(context, menuItem));
			}
				
		} catch (Exception e) {
			this.context.addResult("Navigate to: "+ menuItem, "User should navigate to : " + menuItem + " Page", "User should navigate to : " + menuItem + " Page", "Exception occured", RunStatus.FAIL,Screenshot.getSnap(context, menuItem));
			e.printStackTrace();
		}
	}
	
	
	/***********
	 * This method used to verify the page is loaded completely
	 * 
	 */
	@Then("COMMONVDL:I should verify the page \"(.*)\" is loaded$")
	public void VerifyRevitPageLoading(String menuItem) {
		try {
			//WaitingFunctions.waitForElement(context,By.xpath("//span[contains(.,'"+menuItem+"')]//parent::div//following::div[contains(@class,'search-grid')]/div[@name[contains(.,'tableGrid')] or @id='userSearchArea' or @class[contains(.,'search-div')]]"), 10);
			if (FindElements(context,By.xpath("//span[contains(.,'"+menuItem+"')]//parent::div//following::div[contains(@class,'search-grid')]/div[@name[contains(.,'tableGrid')] or @id='userSearchArea' or @class[contains(.,'search-div')]]")).size()>0) {
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
}


