package com.adp.autopay.automation.pagerepository;

import org.openqa.selenium.By;

/**
 * Created by monagase on 5/29/2015.
 */
public class PortalPage {
    public static By SubmitQuery = By.xpath(".//*[@id='frmCookie']/input");
    public static By UIType = By.xpath(".//*[@id='frmCookie']/select");
    public static By SelectApp = By.xpath(".//a[@title='HWSE Application']");
    public static By ReportsSelection = By.xpath("//td[contains(.,'Report Type')]");
    public static By UserSearchArea = By.xpath(".//*[@id='userSearchArea']/div");
    public static By UserSearchInfo = By.xpath(".//*[@id='searchInfoDialogDiv']//div[contains(@class,'revitTid3Message')]");

    public static By SA_generalSetup = By.xpath("//div[@class='dijitTitlePaneContentOuter dojoxPortletContentOuter' and @role ='presentation']//form//span[contains(.,'Eligibility')]");
    public static By SA_benefitArea = By.xpath(".//*[@id='benefitAreaGrid_table']");
    public static By SA_Options = By.xpath(".//*[@id='widget_benefitOptionListPageBenefitAreaDropDown']");
    public static By SA_CoverageLevels = By.xpath(".//*[@id='tierGroupGrid_table']");
    public static By SA_Prices = By.xpath(".//*[@id='benAreasList']");
    public static By SA_Programs = By.xpath(".//*[@id='programGrid_table']");
    public static By SA_Utilities = By.xpath(".//*[@id='repType']");
    public static By SA_AdditionalBenefits = By.xpath(".//*[@id='voluntaryBenefitGrid_table']");
    public static By SA_Surcharge = By.xpath(".//*[@id='surveyListGrid_table']");
    public static By SA_DocumentLibrary = By.xpath(".//*[@id='doctLibGrid_table']");


}
