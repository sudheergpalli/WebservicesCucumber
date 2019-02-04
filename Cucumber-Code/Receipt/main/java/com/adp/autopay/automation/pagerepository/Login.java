package com.adp.autopay.automation.pagerepository;

import org.openqa.selenium.By;

public class Login
{
	public static By LoginDiv = By.xpath(".//*[@id='loginForm1']");
	
	public static By DataSource = By.xpath(".//*[@class='ng-pristine ng-valid']");
	public static By UserTab = By.xpath(".//span[@class='tabLabel' and text()='User Login']");
	public static By Practitionertab = By.xpath(".//span[@class='tabLabel' and text()='ADP Practitioner Login']");
	public static By member_id = By.xpath(".//*[@id='HWSE.uidU']");
	public static By LogIn = By.xpath(".//*[@id='loginButton2']");
	public static By Client = By.xpath(".//*[@id='clientListSelect']");
	public static By Environment = By.xpath(".//*[@id='envSelect']");
	public static By Role = By.xpath(".//*[@id='roleSelect']");
	public static By Done  = By.xpath(".//*[@id='contextSwitchButton']");
	public static By ClientSelectPopup  = By.xpath(".//*[@id='hwse.contextSwitch']");
	public static By Version=By.xpath(".//input[@id='revit_form_RadioButton_0']");
	public static By VersionButton=By.xpath(".//*[@id='appVersionButton']");
	
	
    

	//failed Scenario
	public static By LoginFailed = By.xpath(".//*[contains(@class,'adp-alert-title')]");
	
	
	public static By SelectClient = By.name("changeClientForm:clientDropDown");
	public static By ChangeClientButton = By.id("changeClientButton");
	public static By Shell = By.id("shell");
	public static By dummy = By.id("dummy");
	public static By UserNameScreen = By.xpath(".//*[@id='USER']");
	public static By activationCodeForm = By.id("activationCodeForm");
	public static By verifyQuestionsPane = By.id("verifyQuestionsPane");
	public static By revit_form_Button_4 = By.id("revit_form_Button_4");
	
	public static By Revit_SelectClient = By.id("clientDropDown");
	public static By ClientWebEdit = By.xpath("//*[@id='revit_form_ValidationTextBox_0' or @id='mastheadSearchForm.input']/following-sibling::span");
	public static By ClientWebEdit1 = By.xpath("//*[@id='revit_form_ValidationTextBox_0' or @id='mastheadSearchForm.input']");
	public static By ClientLink = By.xpath(".//*[@id='mastheadSearchForm.dialog.parent' or @id='revit_TooltipDialog_0']//a");
	public static By clickPractitionerAcess = By.xpath(".//*[@id='detail0_clientUsersTooltipDialog' or @id='revit_TooltipDialog_1']//a");
	
	public static By ADP_NavItem = By.xpath(".//*[contains(@class,'revitMastheadContext')]//h2");
	
}