package com.adp.autopay.automation.pagerepository;

import org.openqa.selenium.By;

public class BenefitsLandingPage
{
	public static By Events = By.xpath(".//div[contains(@class,'card-small card')]");
	public static By Eventheaders = By.xpath(".//div[contains(@class,'card-header')]");
	public static By EventButtons = By.xpath(".//*[contains(@class,'card-footer')]//span");
//	public static By RestartEnrollment = By.xpath(".//button[contains(@ng-click,'resetEventData')]");
//	public static By EditBenefitsEvent = By.xpath(".//button[contains(@ng-click,'initEventData')]");
	public static By DaysLeft = By.xpath(".//*[@class='dateWhite']");
	
	public static By SurveySection = By.xpath(".//*[@class='survey-head-title ng-binding']");
	public static By Question = By.xpath(".//*[@id='survey-body-main']//h1");
	public static By Answer = By.xpath(".//label[@for='answer.text']");

}
