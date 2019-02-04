
package com.adp.autopay.automation.pagerepository;

import org.openqa.selenium.By;

public class EnrollmentPage
{

	public static By Popupbuttons = By.xpath(".//form[contains(@id,'employeeSearch')]//div/span");
	public static By EnrollToday = By.xpath(".//*[@id='enrollement_btn1']");
	public static By EnrollEvent = By.xpath(".//*[@id='nonGEenrollement_btn']");
	public static By Enroll = By.xpath(".//*[@id='enroll0']");
	public static By ViewPrintableConfirmation = By.xpath(".//*[@id='pdfLink']");
	public static By EnrollmentBanner = By.xpath(".//*[@id='nonGeBannercell' or @id='bannercell']/table/tbody/tr[1]/td[2]");

	public static By EnrollmentEvents = By.xpath(".//*[@class='welcometext']//td[@class='ADPUI-normalText']//span[contains(@id,'label')]");
	
	public static By SummaryBenefitsLinks = By.xpath(".//a[contains(@id,'benefitElectionsGrid_row')]");
	public static By BenefitOptions = By.xpath(".//img[contains(@id, 'planOptionDataGrid')]//ancestor::tr[contains(@id,'planOptionDataGrid')]");
	public static By BenefitOptionRadioButtons = By.xpath(".//img[contains(@id,'planOptionDataGrid.store.rows')]");
	public static By Provider = By.xpath(".//input[contains(@id,'revit_form_ValidationTextBox')]");
	public static By ProviderInputFields = By.xpath(".//td[contains(@id, 'dataGrid_row') and contains(@id, 'cell_0')]/span");
	public static By OptDepCoverage = By.xpath(".//td[contains(@id,'coverageInfoDataGrid_row') and contains(@id, 'cell_1')]/span");
	public static By OptDepCoverageCheckBox = By.xpath(".//img[contains(@id, 'coverageInfoDataGrid.store.rows')]");
	
	public static By WIPPage = By.xpath(".//h1[contains(text(), 'Work In Progress')]");
	public static By OptionsinWIPPage = By.xpath(".//div[@id='wipDiv']//label");
	public static By OptRadioButtonsinWIPPage = By.xpath(".//input[contains(@id, 'wipRadio')]");
	public static By WIPContinueButton = By.xpath(".//span[contains(@id,'continue_btn_label') and contains(text(), 'Continue')]");

	public static By ConfirmElections = By.xpath(".//*[@id='confirm_btn']");
	public static By IAgree = By.xpath(".//*[@id='continue']");
	public static By Email_Normal_checked = By.xpath("//label[contains(.,'address shown below')]//div[@role='presentation' and @widgetid[contains(.,'defaultEmail')]]");
	public static By Email_Normal = By.xpath("//label[contains(.,'address shown below')]//div[@role='presentation' and @widgetid[contains(.,'defaultEmail')]]//input[@name[contains(.,'defaultEmail')]]");
	public static By Email_Reciepient_checked = By.xpath("//label[contains(.,'following address')]//div[@role='presentation' and @widgetid[contains(.,'participantProvided')]]");
	public static By Email_Reciepient = By.xpath("//label[contains(.,'following address')]//div[@role='presentation' and @widgetid[contains(.,'participantProvided')]]//input[@type='radio']");
	public static By Email_input = By.xpath(".//*[@id='emailInput']");
	public static By No_Email_checked = By.xpath("//label[contains(.,'do not wish to have an email')]//div[@role='presentation' and @widgetid[contains(.,'noEmail')]]");
	public static By No_Email = By.xpath("//label[contains(.,'do not wish to have an email')]//div[@role='presentation' and @widgetid[contains(.,'noEmail')]]//input[@name[contains(.,'noEmail')]]");
	public static By Submit = By.xpath(".//*[@id='continue']");
	public static By ElectionConfirmationMessage = By.xpath("//div[@id='confNotificationDialog']//div[@id[contains(.,'NotificationMsg')]]");
	public static By Done = By.xpath(".//*[@id='submit_btn']");
	public static By SummaryPageConfirmation = By.xpath(".//*[@id='benefitElectionsGrid']");
	public static By SummaryPageConfirmationVDL = By.xpath(".//*[@id='benefitElectionsGrid_table']");
	
	public static By ElectionConfirmation_Dropdown = By.xpath(".//*[@id='revit_form_FilteringSelect_2']");
	public static By ElectionConfirmation_view = By.xpath("//span[contains(.,'view') and @id[contains(.,'viewButton')] and @role[contains(.,'button')]]");
	public static String ElectionConfirmationDropdown = "revit_form_FilteringSelect_2";
	
	public static By ElectionConfirm_Dropdown = By.xpath(".//input[contains(@class, 'dijitArrowButtonInner')]");
	public static By ElectionConfirm_DropdownList = By.xpath(".//div[contains(@id, 'revit_form_FilteringSelect') and contains(@role, 'listbox')]");

	//PP Search
	public static By LastName = By.xpath(".//*[@id='lastName']");
	public static By FirstName = By.xpath(".//*[@id='firstName']");
	public static By EmployeeId = By.xpath(".//*[@id='empId']");
	
	public static By EmpId = By.xpath(".//*[@id='gridSearchEmployeeId']");
	
	//public static By EmployeeId = By.xpath(".//*[@id='gridSearchEmployeeId']");
	public static By Status = By.xpath(".//*[@id='revit_form_FilteringSelect_0']");
	public static By FileNumber = By.xpath(".//*[@id='gridSearchFileNumber']");
	public static By PayGroup = By.xpath(".//*[@id='gridSearchPayGroup']");
	public static By Job = By.xpath(".//*[@id='revit_form_FilteringSelect_1']");
	public static By Search = By.xpath(".//*[@id='ajaxId']");
	public static By Reset = By.xpath(".//*[@id='selectResetButton_label']");


	public static By EOBO = By.xpath(".//*[@for='employeeSearchDialogRadio_0']");
	public static By TestAs = By.xpath(".//*[@for='employeeSearchDialogRadio_1']");
	public static By TestAsDate = By.xpath(".//*[@id='employeeSearchDate']");
	public static By EOBO_OK = By.xpath(".//*[@id='employeeSearchDialogOkButton']");
	public static By EOBO_Cancel = By.xpath(".//*[@id='closeModalLink_label']");
	
}
