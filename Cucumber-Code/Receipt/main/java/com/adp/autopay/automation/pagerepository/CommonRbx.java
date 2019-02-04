package com.adp.autopay.automation.pagerepository;

import org.openqa.selenium.By;

public class CommonRbx
{
	
	public static By Username = By.xpath("//input[@name='user']");
	public static By Password = By.xpath("//input[@name='password']");
	public static By SignIn = By.xpath("//span[contains(.,'Sign In')]//parent::button");
	public static By Remindmelater = By.xpath("//a[contains(.,'Remind me later')]");
	public static By Redbxheader = By.xpath("//span[@class[contains(.,'header')]]");	
	public static By RedbxBenefits = By.xpath("//span[contains(.,'Benefits')]//parent::a");
	public static By Redbxloader = By.xpath("//div[@loading='true']//i[@class[contains(.,'loading-indicator')]]");	
	public static By Saveandcontinue = By.xpath("//span[contains(.,'Save and Continue To Option Benefits')]//parent::button");
	public static By Saveandreturn = By.xpath("//span[contains(.,'Save and Return to Benefits')]//parent::button");
	public static By CompleteEnrollment = By.xpath("//div[@class[contains(.,'index')]]//span[contains(.,'Complete Enrollment')]//parent::button[@data-ng-click[contains(.,'Indexed')]]");
	public static By ConfirmEnrollment = By.xpath("//span[contains(.,'Confirm Enrollment')]//ancestor::button");
	public static By Printconfirmation = By.xpath("//span[contains(.,'PRINT')]//parent::button[@class[contains(.,'right')]]");
	public static By Confrimationstatement = By.xpath("//xhtml:title[contains(.,'confirmation-statement')]");
	public static By Electionconfirmation = By.xpath("//xhtml:div[contains(.,'Your Election Confirmation')]");
	public static By loaderCleared =  By.xpath("//i[@class='loader loading-indicator']//following::div[@class[contains(.,'show')]]");
	public static By NavigateRight = By.xpath("//div[@class='carousel-nav-container right']");
	public static By ViewDetails =By.xpath("//*[@id='detailViewTab']");
	public static By Seeallbenefits =By.xpath("//span[contains(.,'See all benefits')]");
	public static By AvailablePlans =By.xpath("//li[contains(.,'Available Plans')]");
	public static By ViewDeatilsTabledata =By.xpath("//h4[@class='panel-title']/a/span[1][contains(text(),'')]");
	public static By PlanSelection =By.xpath("(//span[contains(.,'SELECT') or contains(.,'Select')]//parent::button)[1]");
	public static By Defaultviewdetails =By.xpath("(//span[contains(.,'View Details')])[1]");
	public static By BenefitsHome =By.xpath("//div[@class='slidein-header' and @ng-show='backButton']/div/button/span/i");
	public static By Collapse =By.xpath("//div[contains(.,'Collapse view') and @data-ng-if[contains(.,'expandWarning')]]");
	
	
	

	
}
