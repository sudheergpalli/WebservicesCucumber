package com.adp.autopay.automation.pagerepository;

import org.openqa.selenium.By;

public class Common
{
	public static By SuggestedActivitesclose = By.xpath("//*[@id='suggestionBox']//span[contains(@class,'CloseIcon')]");
	public static By IEBackwardButton = By.xpath("//div[@class='revitTaskPanelInner']//span[@id[contains(.,'Button')]]//span[@class[contains(.,'left')] and not(@class[contains(.,'disabled')])]");
	public static By IEForwrdButton = By.xpath("//div[@class='revitTaskPanelInner']//span[@id[contains(.,'Button')]]//span[@class[contains(.,'right')] and not(@class[contains(.,'disabled')])]");
	public static By ClientSelectionloader =  By.xpath(".//span[contains(.,'Loading')]");
	public static By IESelectAllCheckbox = By.xpath("//table[@id='tableGrid_headers_table']//span[@id[contains(.,'selectAll')]]//div[@class[contains(.,'CheckBox')]]//input");
	public static By IESearchIcon = By.xpath("//div[@name[contains(.,'Profile')]]//span[@class[contains(.,'search')] and not(@class[contains(.,'disabled')])]");
	public static By RevitLoading = By.xpath("//div[@class='revitLoadingPortlet']//div[contains(.,'Loading') and @class[contains(.,'revitLoadingContentPane')]]");
	public static By TableGrid = By.xpath(".//*[contains(@id,'tableGrid_rows_table') or substring(@id, string-length(@id) - 9) = 'Grid_table']");
	public static By SubmitPasswordform = By.xpath(".//*[@id='passwordForm']//div[text()='Submit']");
	public static By SubmitRegistrationform = By.xpath(".//*[@id='registrationForm']//div[text()='Submit']");
	public static By Adminlogin = By.xpath(".//*[contains(@id,'admin')]");
	public static By NoResultsSearch = By.xpath("//tr[@id[contains(.,'empty')]]//td[contains(.,'Your search did not result in any matches')]");
	public static By SelectAllbox = By.xpath("//div//input[@id[contains(.,'selectAll_input')]]");
	public static By PageHeadings = By.xpath(".//span[@class='dijitTitlePaneTextNode']");
    public static By SearchButton=By.xpath(".//*[@id='ajaxId']");
    public static By Eobo=By.xpath("//span[@id='employeeSearchDialogOkButton_label']");
    public static By VantageUserId=By.xpath("//input[@id='login']");
    public static By VantageUserPaswword=By.id("PASSWORD");
    public static By VantageUserLoginButton=By.xpath("//input[@id='portal.login.logIn']");
    public static By VantageHomePage=By.xpath("//span[@class='dijitTitlePaneTextNode' and (contains(text(),'Company Spotlight') or contains(text(),'Message Center'))]");
  //span[@class='dijitTitlePaneTextNode' and contains(text(),'Company Spotlight')]
    public static By ActiveTab=By.xpath("//div[@class='revitTab dijitTab dijitTabChecked dijitChecked']");
    //public static By SearchIcon=By.xpath(".//*[@class='fa fa-search fa-stack-1x fa-inverse']");
    public static By SearchIcon=By.xpath("//div[@name[contains(.,'Profile')]]//span[@class[contains(.,'search')]]");
    public static By VDLSearchIcon=By.xpath("//span[@data-dojo-attach-point='iconNode' and @class[contains(.,'search')]]");
	public static By ContinueButton=By.xpath(".//span[@id='gridSearchContinueButton']");
	public static By verifyrowcount=By.xpath("//table[@id[contains(.,'pagination')]]//span[@id[contains(.,'Grid_total_box')]]");
	public static By EmployeeRowsTable = By.xpath(".//*[@id='tableGrid_rows_table']");
	public static By EmployeeHeaders = By.xpath(".//th[contains(@id,'tableGrid_header_')]");
	public static By dependentHeaders = By.xpath(".//th[contains(@id,'dependents_header')]");
	public static By dependentRowTable = By.xpath(".//*[@id='dependents_rows_table']");
	public static By SSNunmasked = By.xpath(".//div[@id='ssnCell']");
	//public static By ClickForwardicon= By.xpath(".//i[contains(@class,'fa fa-chevron-circle-right fa-2x')]");
	public static By ClickForwardicon= By.xpath("//span[@id[contains(.,'Button')]]//span[@class[contains(.,'right')]]");
	//public static By ClickBackwardicon= By.xpath(".//i[contains(@class,'fa fa-chevron-circle-left fa-2x')]");
	public static By ClickBackwardicon= By.xpath("//span[@id[contains(.,'Button')]]//span[@class[contains(.,'left')]]");
	public static By ClickReveallink= By.xpath(".//font[text()='Reveal']/parent::span/parent::span");
	//public static By PPLabels = By.xpath(".//*[@role='presentation']//div[contains(@class,'pp-labels')]");
	public static By PPLabels = By.xpath(".//*[@role='presentation']//div[contains(@id,'participantProfile')]//tbody");
	public static By Norecordsmessage=By.xpath(".//*[@id='tableGrid_emptyRow']/td[text()='Your search did not result in any matches.']");
	public static By SearchRecordsMessage=By.xpath(".//div[contains(text(),'Enter one or more search options')]");
	public static By SelectAllcheckbox=By.xpath(".//*[@id='tableGrid_selectAll_input']");
	public static By ParticipantPanel=By.xpath(".//*[@id='participantProfileTaskPanel']");
	public static By ClickNavigateToPage= By.xpath(".//div[@class='col-md-1 icon-more']//span[@data-dojo-attach-point='iconNode']");
	public static By userProfile__name=By.xpath(".//span[@id='people_userProfile__name']");
	public static By userProfile_id=By.xpath(".//span[@id='userProfile__employeeId']");
	public static By userProfile_status=By.xpath(".//span[@id='people_userProfilePosition__status']");
	public static By userProfile_Emailicon=By.xpath(".//span[@id='people_userProfileImg__email']");
	public static By userProfile_Telephoneicon=By.xpath(".//span[@id='people_userProfileImg__workPhone']");
	public static By LogoutArroe=By.xpath(".//span[contains(@class,'dijitInline dijitIcon fa fa-sign-out')]");
	public static By Logout=By.xpath(".//div[@class='revitMastheadLogoutLabel']");
	//public static By TableRowNext=By.id("tableGrid_next");
	//public static By TableRowPrevious=By.id("tableGrid_previous");
	//public static By TableRowLast=By.id("tableGrid_last");
	
	public static By TableRowPreviousButton=By.id("tableGrid_previous");
	public static By TableRowLastButton=By.id("tableGrid_last");
	public static By TableRowNextButton=By.id("tableGrid_next");
	public static By TableRowFirstButton=By.id("tableGrid_first");
	
	public static By TableRowPrevious=By.xpath("//div[contains(@id,'tableGrid_previous')]//a//div");
	public static By TableRowLast=By.xpath("//div[contains(@id,'tableGrid_last')]//a//div");
	public static By TableRowNext=By.xpath("//div[contains(@id,'tableGrid_next')]//a//div");
	public static By TableRowFirst=By.xpath("//div[contains(@id,'tableGrid_first')]//a//div");
	//public static By TableRowFirst=By.id("tableGrid_first");
	public static By ErrorMessage=By.xpath(".//span[@id='errorMessage']");
	public static By pdcEmployeeRowsTable = By.xpath(".//*[@id='j_id_jsp_1846800914_113_rows_table']");
	public static By pdcEmployeeHeaders = By.xpath(".//th[contains(@id,'header')]");
	public static By ComEmployeeRowsTable = By.xpath(".//*[@id='CommunicationsBeanListGrid_rows_table']");
	public static By EnrollmentsTable = By.id("tableGrid");
	public static By TableSpinner = By.xpath("//tbody[contains(@id,'tableGrid')]//div[@class='gridSpinner']");
	
	public static By VDLuserExperience = By.xpath(".//a[contains(.,'Switch ON ADP Experience')]");
	public static By clientSearchBy = By.xpath("//input[@id='toolbarQuickSearch']");
	public static By pratClien = By.xpath("//div[contains(@id,'revit_TooltipDialog')]/div[@class='dijitReset dijitInline revitDialogContents']//table//td[1]/a[text()='Practitioner Access']");
	public static By CompanyEvents = By.xpath("span[text()='Company Events']");
	//
	
	
}
