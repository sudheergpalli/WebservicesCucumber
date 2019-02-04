package com.adp.autopay.automation.pagespecificlibrary;

import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.commonlibrary.WaitingFunctions;
import com.adp.autopay.automation.framework.Dropdown;
import com.adp.autopay.automation.framework.WebEdit;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.utility.ExecutionContext;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by monagase on 7/13/2015.
 */
public class NewHire {

    public static String NHID;

    public static boolean fillEmployeeIdentification(ExecutionContext context) throws ParseException {
        String Env = ExecutionContext.SRunAgainst.toUpperCase();
        if(Env.contains("PRODUCTION_POD1")){
        try{
            WaitingFunctions.waitForElement(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), 10);
            String date = null;
            date = "01/01/" + Calendar.getInstance().get(Calendar.YEAR);

            WebEdit.SetText(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), date);
//            DateFunctions.FillWebDate(context, By.id("widget_employeeIdentificationHireDate"), date);
            context.driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

            Dropdown.SelectWebListByPartialSearch_Revit(context, "employeeIdentificationReasonSelect", "NPS", "NPS - New Position");
            context.driver.findElement(By.id("employeeIdentificationFirstName")).sendKeys("FirstName");
            context.driver.findElement(By.id("employeeIdentificationLastName")).sendKeys("LastName");

            Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationCompSelect", "MCB", "MCB - MCB - Employee Invst Svc");
            try{
                Thread.sleep(2000);
            }catch (Exception e) {
            }
            Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationPayGroupSelect", "HW1", "HW1 - HWSE non transmit weekly");
            context.driver.findElement(By.id("employeeIdentificationOverrideBtn")).click();
            Thread.sleep(2000);
            context.driver.findElement(By.id("employeeIdentificationEmployeeID")).click();
            try{
                Thread.sleep(2000);
            }catch (Exception e){}
            NHID = generateNHID();
            context.driver.findElement(By.id("employeeIdentificationEmployeeID")).sendKeys(NHID);
            try{
                Thread.sleep(2000);
            }catch (Exception e){}
//			driver.findElement(By.id("employeeIdentificationOverrideFileNum")).click();
            context.driver.findElement(By.id("employeeIdentificationFileNum")).click();
            Thread.sleep(2000);
            context.driver.findElement(By.id("employeeIdentificationFileNum")).sendKeys("" + randInt(100000, 999999));
            try{
                Thread.sleep(2000);
            }catch (Exception e){}
            context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys("" + randInt(100000000, 999999999));
            context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys(Keys.TAB);
            try{
                Thread.sleep(2000);
            }catch (Exception e){}
            context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlNext')]")).click();
            try{
                Thread.sleep(4000);
            }catch (Exception e){}
            WaitingFunctions.WaitUntilPageLoads(context);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            context.addResult("New Hire Fill Employee Information","Fill employee Information for the new hire","First Step of NewHire has to complete","New Fire fisrt step failed", RunStatus.FAIL, Screenshot.getSnap(context, "EmployeeInfo"));
        }
        }
        if(Env.contains("PRODUCTION_POD2")){
            try{
                WaitingFunctions.waitForElement(context,By.xpath(".//*[@id='employeeIdentificationHireDate']"),10);
                String date = null;
                date = "01/01/" + Calendar.getInstance().get(Calendar.YEAR);

                WebEdit.SetText(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), date);
//            DateFunctions.FillWebDate(context, By.id("widget_employeeIdentificationHireDate"), date);
                context.driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

                Dropdown.SelectWebListByPartialSearch_Revit(context, "employeeIdentificationReasonSelect", "NPS", "NPS - New Position");
                context.driver.findElement(By.id("employeeIdentificationFirstName")).sendKeys("FirstName");
                context.driver.findElement(By.id("employeeIdentificationLastName")).sendKeys("LastName");

                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationCompSelect", "MCB", "MCB - MidCountry Bank");
                try{
                    Thread.sleep(2000);
                }catch (Exception e) {
                }
                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationPayGroupSelect", "A-1", "A-1 - EIS two");
                context.driver.findElement(By.id("employeeIdentificationOverrideBtn")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).click();
                try{
                    Thread.sleep(2000);
                }catch (Exception e){}
                NHID = generateNHID();
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).sendKeys(NHID);
                try{
                    Thread.sleep(2000);
                }catch (Exception e){}
//			driver.findElement(By.id("employeeIdentificationOverrideFileNum")).click();
                context.driver.findElement(By.id("employeeIdentificationFileNum")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationFileNum")).sendKeys("" + randInt(100000, 999999));
                try{
                    Thread.sleep(2000);
                }catch (Exception e){}
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys("" + randInt(100000000, 999999999));
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys(Keys.TAB);
                try{
                    Thread.sleep(2000);
                }catch (Exception e){}
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlNext')]")).click();
                try{
                    Thread.sleep(4000);
                }catch (Exception e){}
                WaitingFunctions.WaitUntilPageLoads(context);
                return true;
            }catch(Exception e){
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Information","Fill employee Information for the new hire","First Step of NewHire has to complete","New Fire fisrt step failed", RunStatus.FAIL, Screenshot.getSnap(context,"EmployeeInfo"));
            }
        }
        if(Env.contains("IAT_POD1")) {
            try {
                WaitingFunctions.waitForElement(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), 10);
                String date = null;
                date = "01/01/" + Calendar.getInstance().get(Calendar.YEAR);

                WebEdit.SetText(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), date);
//            DateFunctions.FillWebDate(context, By.id("widget_employeeIdentificationHireDate"), date);
                context.driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

                Dropdown.SelectWebListByPartialSearch_Revit(context, "employeeIdentificationReasonSelect", "NPS", "NPS - New Position");
                context.driver.findElement(By.id("employeeIdentificationFirstName")).sendKeys("FirstName");
                context.driver.findElement(By.id("employeeIdentificationLastName")).sendKeys("LastName");

                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationCompSelect", "MCB", "MCB - MCB - Employee Invst Svc");
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationPayGroupSelect", "HW2", "HW2 - HWSE non transmit biwk");
                context.driver.findElement(By.id("employeeIdentificationOverrideBtn")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).click();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                NHID = generateNHID();
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).sendKeys(NHID);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
//			driver.findElement(By.id("employeeIdentificationOverrideFileNum")).click();
                context.driver.findElement(By.id("employeeIdentificationFileNum")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationFileNum")).sendKeys("" + randInt(100000, 999999));
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys("" + randInt(100000000, 999999999));
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys(Keys.TAB);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlNext')]")).click();
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                }
                WaitingFunctions.WaitUntilPageLoads(context);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Information", "Fill employee Information for the new hire", "First Step of NewHire has to complete", "New Fire fisrt step failed", RunStatus.FAIL, Screenshot.getSnap(context, "EmployeeInfo"));
            }
        }
        if(Env.contains("IAT_POD2")) {
            try {
                WaitingFunctions.waitForElement(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), 10);
                String date = null;
                date = "01/01/" + Calendar.getInstance().get(Calendar.YEAR);

                WebEdit.SetText(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), date);
//            DateFunctions.FillWebDate(context, By.id("widget_employeeIdentificationHireDate"), date);
                context.driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

                Dropdown.SelectWebListByPartialSearch_Revit(context, "employeeIdentificationReasonSelect", "NPS", "NPS - New Position");
                context.driver.findElement(By.id("employeeIdentificationFirstName")).sendKeys("FirstName");
                context.driver.findElement(By.id("employeeIdentificationLastName")).sendKeys("LastName");

                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationCompSelect", "MCB", "MCB - MCB - Employee Invst Svc");
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationPayGroupSelect", "HW2", "HW2 - HWSE non transmit biwk");
                context.driver.findElement(By.id("employeeIdentificationOverrideBtn")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).click();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                NHID = generateNHID();
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).sendKeys(NHID);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
//			driver.findElement(By.id("employeeIdentificationOverrideFileNum")).click();
                context.driver.findElement(By.id("employeeIdentificationFileNum")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationFileNum")).sendKeys("" + randInt(100000, 999999));
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys("" + randInt(100000000, 999999999));
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys(Keys.TAB);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlNext')]")).click();
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                }
                WaitingFunctions.WaitUntilPageLoads(context);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Information", "Fill employee Information for the new hire", "First Step of NewHire has to complete", "New Fire fisrt step failed", RunStatus.FAIL, Screenshot.getSnap(context, "EmployeeInfo"));
            }
        }
        if(Env.contains("UAT_POD1")) {
            try {
                WaitingFunctions.waitForElement(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), 10);
                String date = null;
                date = "01/01/" + Calendar.getInstance().get(Calendar.YEAR);

                WebEdit.SetText(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), date);
//            DateFunctions.FillWebDate(context, By.id("widget_employeeIdentificationHireDate"), date);
                context.driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

                Dropdown.SelectWebListByPartialSearch_Revit(context, "employeeIdentificationReasonSelect", "NPS", "NPS - New Position");
                context.driver.findElement(By.id("employeeIdentificationFirstName")).sendKeys("FirstName");
                context.driver.findElement(By.id("employeeIdentificationLastName")).sendKeys("LastName");

                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationCompSelect", "MCB", "MCB - MidCountry Bank");
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationPayGroupSelect", "A-2", "A-2 - MidCountry Bank");
                context.driver.findElement(By.id("employeeIdentificationOverrideBtn")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).click();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                NHID = generateNHID();
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).sendKeys(NHID);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
//			driver.findElement(By.id("employeeIdentificationOverrideFileNum")).click();
                context.driver.findElement(By.id("employeeIdentificationFileNum")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationFileNum")).sendKeys("" + randInt(100000, 999999));
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys("" + randInt(100000000, 999999999));
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys(Keys.TAB);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlNext')]")).click();
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                }
                WaitingFunctions.WaitUntilPageLoads(context);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Information", "Fill employee Information for the new hire", "First Step of NewHire has to complete", "New Fire fisrt step failed", RunStatus.FAIL, Screenshot.getSnap(context, "EmployeeInfo"));
            }
        }
        if(Env.contains("UAT_POD2")) {
            try {
                WaitingFunctions.waitForElement(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), 10);
                String date = null;
                date = "01/01/" + Calendar.getInstance().get(Calendar.YEAR);

                WebEdit.SetText(context, By.xpath(".//*[@id='employeeIdentificationHireDate']"), date);
//            DateFunctions.FillWebDate(context, By.id("widget_employeeIdentificationHireDate"), date);
                context.driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

                Dropdown.SelectWebListByPartialSearch_Revit(context, "employeeIdentificationReasonSelect", "NPS", "NPS - New Position");
                context.driver.findElement(By.id("employeeIdentificationFirstName")).sendKeys("FirstName");
                context.driver.findElement(By.id("employeeIdentificationLastName")).sendKeys("LastName");

                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationCompSelect", "DSS", "DSS - Consumer Products");
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                Dropdown.SelectWebListByPartialSearch_Revit(context, "empIdentificationPayGroupSelect", "ET1", "ET1 - Enterprise Expanded Fields");
                context.driver.findElement(By.id("employeeIdentificationOverrideBtn")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).click();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                NHID = generateNHID();
                context.driver.findElement(By.id("employeeIdentificationEmployeeID")).sendKeys(NHID);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
//			driver.findElement(By.id("employeeIdentificationOverrideFileNum")).click();
                context.driver.findElement(By.id("employeeIdentificationFileNum")).click();
                Thread.sleep(2000);
                context.driver.findElement(By.id("employeeIdentificationFileNum")).sendKeys("" + randInt(100000, 999999));
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys("" + randInt(100000000, 999999999));
                context.driver.findElement(By.id("employeeIdentificationSsn")).sendKeys(Keys.TAB);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlNext')]")).click();
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                }
                WaitingFunctions.WaitUntilPageLoads(context);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Information", "Fill employee Information for the new hire", "First Step of NewHire has to complete", "New Fire fisrt step failed", RunStatus.FAIL, Screenshot.getSnap(context, "EmployeeInfo"));
            }
        }
        return false;
    }

    public static String getNHID(){
        try{
            return NHID;
        }catch(Exception e){}
        return null;
    }
    public static boolean fillPersonalInformation(ExecutionContext context){
        String Env = ExecutionContext.SRunAgainst.toUpperCase();
        if(Env.contains("PRODUCTION")) {
            try {
                context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                context.driver.findElement(By.id("personalInfohomeStreet1")).sendKeys("2575 Westside Pkwy");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                context.driver.findElement(By.id("personalInfohomeCity")).sendKeys("Roseland");
                Dropdown.SelectWebList_Revit(context, "personalInfohomeStateFilteringSelect", "New Jersey");
                context.driver.findElement(By.id("personalInfohomeZip")).sendKeys("00768");

                context.driver.findElement(By.xpath(".//*[contains(@class,'dijitTabContainerTop-tabs')]//div[3]")).click();

                Dropdown.SelectWebList_Revit(context, "personalInfogender", "Male");
                Dropdown.SelectWebList_Revit(context, "personalInfomaritalStatus", "Single");
                context.driver.findElement(By.id("personalInfonewHirebirthDate")).sendKeys("01/01/1970");

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }

                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlNext')]")).click();

                WaitingFunctions.WaitUntilPageLoads(context);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee personal Information", "Fill employee personal Information for the new hire", "Second Step of NewHire has to complete", "New Fire Second step failed", RunStatus.FAIL, Screenshot.getSnap(context, "PersonalInfo"));
            }
        }

        if(Env.contains("IAT")) {
            try {
                context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                context.driver.findElement(By.id("personalInfohomeStreet1")).sendKeys("2575 Westside Pkwy");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                context.driver.findElement(By.id("personalInfohomeCity")).sendKeys("Roseland");
                Dropdown.SelectWebList_Revit(context, "personalInfohomeStateFilteringSelect", "New Jersey");
                context.driver.findElement(By.id("personalInfohomeZip")).sendKeys("00768");

                context.driver.findElement(By.xpath(".//*[contains(@class,'dijitTabContainerTop-tabs')]//div[3]")).click();

                Dropdown.SelectWebList_Revit(context, "personalInfogender", "Male");
                Dropdown.SelectWebList_Revit(context, "personalInfomaritalStatus", "Single");
                context.driver.findElement(By.id("personalInfonewHirebirthDate")).sendKeys("01/01/1970");

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }

                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlNext')]")).click();

                WaitingFunctions.WaitUntilPageLoads(context);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee personal Information", "Fill employee personal Information for the new hire", "Second Step of NewHire has to complete", "New Fire Second step failed", RunStatus.FAIL, Screenshot.getSnap(context, "PersonalInfo"));
            }
        }

        if(Env.contains("UAT")) {
            try {
                context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                context.driver.findElement(By.id("personalInfohomeStreet1")).sendKeys("2575 Westside Pkwy");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                context.driver.findElement(By.id("personalInfohomeCity")).sendKeys("Roseland");
                Dropdown.SelectWebList_Revit(context, "personalInfohomeStateFilteringSelect", "New Jersey");
                context.driver.findElement(By.id("personalInfohomeZip")).sendKeys("00768");

                context.driver.findElement(By.xpath(".//*[contains(@class,'dijitTabContainerTop-tabs')]//div[3]")).click();

                Dropdown.SelectWebList_Revit(context, "personalInfogender", "Male");
                Dropdown.SelectWebList_Revit(context, "personalInfomaritalStatus", "Separated");
                context.driver.findElement(By.id("personalInfonewHirebirthDate")).sendKeys("01/01/1970");

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }

                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlNext')]")).click();

                WaitingFunctions.WaitUntilPageLoads(context);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee personal Information", "Fill employee personal Information for the new hire", "Second Step of NewHire has to complete", "New Fire Second step failed", RunStatus.FAIL, Screenshot.getSnap(context, "PersonalInfo"));
            }
        }

        return false;
    }


    public static boolean fillJobInformation(ExecutionContext context){
        String Env = ExecutionContext.SRunAgainst.toUpperCase();
        if(Env.contains("PRODUCTION_POD1")) {
            try {
                context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                Dropdown.SelectWebListByPartialSearch_Revit(context, "jobDescription", "B00009", "B00009 - Branch Manager 1");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//            WebEdit.SetText(context,By.xpath(".//*[@id='departmentSelect']"),"ADSFADSF - ADSFADSF");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "departmentSelect", "108", "108 - MCB Executive");
//            WebEdit.SetText(context, By.xpath(".//*[@id='managerSelect']"), """Dara, Rajasekhar - 0000012547");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "managerSelect", "Baker", "Baker, Allen - 12470");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "workLocationSelect", "HOME", "HOME - Works from Home");
//            Dropdown.SelectWebList_Revit(context,"reportingLocationSelect", "001 - MCB-Hutchinson MN");
                Thread.sleep(3000);
                context.driver.findElement(By.xpath(".//*[contains(@class,'dijitTabContainerTop-tabs')]/div[3]")).click();

                if (!(WebEdit.GetText(context, By.xpath(".//*[@id='compensationFrequency']")).equalsIgnoreCase("Hourly"))) {
                    Dropdown.SelectWebListByPartialSearch_Revit(context, "compensationFrequency", "Hour", "Hourly");
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
//            context.driver.findElement(By.id("newPayRate")).clear();
//            context.driver.findElement(By.id("newPayRate")).sendKeys("30");
//            context.driver.findElement(By.id("annualBenefits")).click();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
//			//Click on Next Button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
//			//Click on State tab
//			driver.findElement(By.xpath(".//*[@class='revitTabController dijitTabContainerTop-tabs dijitTabNoLayout']/div[2]")).click();
//			WebElements.selectWebList(driver,"stateWorkedInState", "GA - Georgia");
//			WebElements.selectWebList(driver,"stateSuiSdi", "GA - Georgia");
//
//			//Click on Next button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
                //click on done button
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlDone')]")).click();
                WaitingFunctions.WaitUntilPageLoads(context);

                context.driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
                context.driver.findElement(By.id("submitterComments")).sendKeys("New Hire testing automation");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                context.driver.findElement(By.id("btnDoneTemplate")).click();
                WaitingFunctions.WaitUntilPageLoads(context);
//            context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                WaitingFunctions.nullifyImplicitWait(context);
//			try{
//				driver.findElement(By.id("newHireMessages")).getText();
//			}catch(Exception e){}
//			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Job Information", "Fill employee Job Information for the new hire", "Third Step of NewHire has to complete", "New Fire third step failed", RunStatus.FAIL, Screenshot.getSnap(context, "JobInfo"));
            }
        }
        if(Env.contains("PRODUCTION_POD2")) {
            try {
                context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                Dropdown.SelectWebListByPartialSearch_Revit(context, "jobDescription", "B00006", "B00006 - Personal Banker 1");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//            WebEdit.SetText(context,By.xpath(".//*[@id='departmentSelect']"),"ADSFADSF - ADSFADSF");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "departmentSelect", "108", "108 - MCB Executive");
//            WebEdit.SetText(context, By.xpath(".//*[@id='managerSelect']"), """Dara, Rajasekhar - 0000012547");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "managerSelect", "Adams", "Adams, Alice - 0000014253");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "workLocationSelect", "HOME", "HOME - Works from Home");
//            Dropdown.SelectWebList_Revit(context,"reportingLocationSelect", "001 - MCB-Hutchinson MN");
                Thread.sleep(3000);
                context.driver.findElement(By.xpath(".//*[contains(@class,'dijitTabContainerTop-tabs')]/div[3]")).click();

                if (!(WebEdit.GetText(context, By.xpath(".//*[@id='compensationFrequency']")).equalsIgnoreCase("Hourly"))) {
                    Dropdown.SelectWebListByPartialSearch_Revit(context, "compensationFrequency", "Hour", "Hourly");
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
//            context.driver.findElement(By.id("newPayRate")).clear();
//            context.driver.findElement(By.id("newPayRate")).sendKeys("30");
//            context.driver.findElement(By.id("annualBenefits")).click();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
//			//Click on Next Button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
//			//Click on State tab
//			driver.findElement(By.xpath(".//*[@class='revitTabController dijitTabContainerTop-tabs dijitTabNoLayout']/div[2]")).click();
//			WebElements.selectWebList(driver,"stateWorkedInState", "GA - Georgia");
//			WebElements.selectWebList(driver,"stateSuiSdi", "GA - Georgia");
//
//			//Click on Next button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
                //click on done button
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlDone')]")).click();
                WaitingFunctions.WaitUntilPageLoads(context);

                context.driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
                context.driver.findElement(By.id("submitterComments")).sendKeys("New Hire testing automation");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                context.driver.findElement(By.id("btnDoneTemplate")).click();
                WaitingFunctions.WaitUntilPageLoads(context);
//            context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                WaitingFunctions.nullifyImplicitWait(context);
//			try{
//				driver.findElement(By.id("newHireMessages")).getText();
//			}catch(Exception e){}
//			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Job Information", "Fill employee Job Information for the new hire", "Third Step of NewHire has to complete", "New Fire third step failed", RunStatus.FAIL, Screenshot.getSnap(context, "JobInfo"));
            }
        }
        if(Env.contains("IAT_POD1")) {
            try {
                context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                Dropdown.SelectWebListByPartialSearch_Revit(context, "jobDescription", "INA300", "INA300 - Sales-Europe");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//            WebEdit.SetText(context,By.xpath(".//*[@id='departmentSelect']"),"ADSFADSF - ADSFADSF");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "departmentSelect", "A10151", "A10151 - Security Loan Dept.");
//            WebEdit.SetText(context, By.xpath(".//*[@id='managerSelect']"), """Dara, Rajasekhar - 0000012547");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "managerSelect", "Babs", "Babs, AnitaSHA – 000012638");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "workLocationSelect", "HOME", "HOME - Works from Home");
//            Dropdown.SelectWebList_Revit(context,"reportingLocationSelect", "001 - MCB-Hutchinson MN");
                Thread.sleep(3000);
                context.driver.findElement(By.xpath(".//*[contains(@class,'dijitTabContainerTop-tabs')]/div[3]")).click();

                if (!(WebEdit.GetText(context, By.xpath(".//*[@id='compensationFrequency']")).equalsIgnoreCase("Hourly"))) {
                    Dropdown.SelectWebListByPartialSearch_Revit(context, "compensationFrequency", "Hour", "Hourly");
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
//            context.driver.findElement(By.id("newPayRate")).clear();
//            context.driver.findElement(By.id("newPayRate")).sendKeys("30");
//            context.driver.findElement(By.id("annualBenefits")).click();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
//			//Click on Next Button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
//			//Click on State tab
//			driver.findElement(By.xpath(".//*[@class='revitTabController dijitTabContainerTop-tabs dijitTabNoLayout']/div[2]")).click();
//			WebElements.selectWebList(driver,"stateWorkedInState", "GA - Georgia");
//			WebElements.selectWebList(driver,"stateSuiSdi", "GA - Georgia");
//
//			//Click on Next button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
                //click on done button
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlDone')]")).click();
                WaitingFunctions.WaitUntilPageLoads(context);

                context.driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
                context.driver.findElement(By.id("submitterComments")).sendKeys("New Hire testing automation");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                context.driver.findElement(By.id("btnDoneTemplate")).click();
                WaitingFunctions.WaitUntilPageLoads(context);
//            context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                WaitingFunctions.nullifyImplicitWait(context);
//			try{
//				driver.findElement(By.id("newHireMessages")).getText();
//			}catch(Exception e){}
//			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Job Information", "Fill employee Job Information for the new hire", "Third Step of NewHire has to complete", "New Fire third step failed", RunStatus.FAIL, Screenshot.getSnap(context, "JobInfo"));
            }
        }
        if(Env.contains("IAT_POD2")) {
            try {
                context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                Dropdown.SelectWebListByPartialSearch_Revit(context, "jobDescription", "B00009", "B00009 - Branch Manager 1");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//            WebEdit.SetText(context,By.xpath(".//*[@id='departmentSelect']"),"ADSFADSF - ADSFADSF");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "departmentSelect", "10000", "10000 – Executive");
//            WebEdit.SetText(context, By.xpath(".//*[@id='managerSelect']"), """Dara, Rajasekhar - 0000012547");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "managerSelect", "Burke", "Burke, Timothy – 600605");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "workLocationSelect", "HOME", "HOME - Works from Home");
//            Dropdown.SelectWebList_Revit(context,"reportingLocationSelect", "001 - MCB-Hutchinson MN");
                Thread.sleep(3000);
                context.driver.findElement(By.xpath(".//*[contains(@class,'dijitTabContainerTop-tabs')]/div[3]")).click();

                if (!(WebEdit.GetText(context, By.xpath(".//*[@id='compensationFrequency']")).equalsIgnoreCase("Hourly"))) {
                    Dropdown.SelectWebListByPartialSearch_Revit(context, "compensationFrequency", "Hour", "Hourly");
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
//            context.driver.findElement(By.id("newPayRate")).clear();
//            context.driver.findElement(By.id("newPayRate")).sendKeys("30");
//            context.driver.findElement(By.id("annualBenefits")).click();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
//			//Click on Next Button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
//			//Click on State tab
//			driver.findElement(By.xpath(".//*[@class='revitTabController dijitTabContainerTop-tabs dijitTabNoLayout']/div[2]")).click();
//			WebElements.selectWebList(driver,"stateWorkedInState", "GA - Georgia");
//			WebElements.selectWebList(driver,"stateSuiSdi", "GA - Georgia");
//
//			//Click on Next button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
                //click on done button
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlDone')]")).click();
                WaitingFunctions.WaitUntilPageLoads(context);

                context.driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
                context.driver.findElement(By.id("submitterComments")).sendKeys("New Hire testing automation");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                context.driver.findElement(By.id("btnDoneTemplate")).click();
                WaitingFunctions.WaitUntilPageLoads(context);
//            context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                WaitingFunctions.nullifyImplicitWait(context);
//			try{
//				driver.findElement(By.id("newHireMessages")).getText();
//			}catch(Exception e){}
//			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Job Information", "Fill employee Job Information for the new hire", "Third Step of NewHire has to complete", "New Fire third step failed", RunStatus.FAIL, Screenshot.getSnap(context, "JobInfo"));
            }
        }
        if(Env.contains("UAT_POD1")) {
            try {
                context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                Dropdown.SelectWebListByPartialSearch_Revit(context, "jobDescription", "B00009", "B00009 - Branch Manager 1");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//            WebEdit.SetText(context,By.xpath(".//*[@id='departmentSelect']"),"ADSFADSF - ADSFADSF");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "departmentSelect", "10000", "10000 – Executive");
//            WebEdit.SetText(context, By.xpath(".//*[@id='managerSelect']"), """Dara, Rajasekhar - 0000012547");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "managerSelect", "Burke", "Burke, Timothy – 600605");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "workLocationSelect", "HOME", "HOME - Works from Home");
//            Dropdown.SelectWebList_Revit(context,"reportingLocationSelect", "001 - MCB-Hutchinson MN");
                Thread.sleep(3000);
                context.driver.findElement(By.xpath(".//*[contains(@class,'dijitTabContainerTop-tabs')]/div[3]")).click();

                if (!(WebEdit.GetText(context, By.xpath(".//*[@id='compensationFrequency']")).equalsIgnoreCase("Hourly"))) {
                    Dropdown.SelectWebListByPartialSearch_Revit(context, "compensationFrequency", "Hour", "Hourly");
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
//            context.driver.findElement(By.id("newPayRate")).clear();
//            context.driver.findElement(By.id("newPayRate")).sendKeys("30");
//            context.driver.findElement(By.id("annualBenefits")).click();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
//			//Click on Next Button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
//			//Click on State tab
//			driver.findElement(By.xpath(".//*[@class='revitTabController dijitTabContainerTop-tabs dijitTabNoLayout']/div[2]")).click();
//			WebElements.selectWebList(driver,"stateWorkedInState", "GA - Georgia");
//			WebElements.selectWebList(driver,"stateSuiSdi", "GA - Georgia");
//
//			//Click on Next button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
                //click on done button
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlDone')]")).click();
                WaitingFunctions.WaitUntilPageLoads(context);

                context.driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
                context.driver.findElement(By.id("submitterComments")).sendKeys("New Hire testing automation");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                context.driver.findElement(By.id("btnDoneTemplate")).click();
                WaitingFunctions.WaitUntilPageLoads(context);
//            context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                WaitingFunctions.nullifyImplicitWait(context);
//			try{
//				driver.findElement(By.id("newHireMessages")).getText();
//			}catch(Exception e){}
//			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Job Information", "Fill employee Job Information for the new hire", "Third Step of NewHire has to complete", "New Fire third step failed", RunStatus.FAIL, Screenshot.getSnap(context, "JobInfo"));
            }
        }
        if(Env.contains("UAT_POD2")) {
            try {
                context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                Dropdown.SelectWebListByPartialSearch_Revit(context, "jobDescription", "INA300", "INA300 - Sales-Europe");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
//            WebEdit.SetText(context,By.xpath(".//*[@id='departmentSelect']"),"ADSFADSF - ADSFADSF");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "departmentSelect", "A10151", "A10151 - Security Loan Dept.");
//            WebEdit.SetText(context, By.xpath(".//*[@id='managerSelect']"), """Dara, Rajasekhar - 0000012547");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "managerSelect", "Babs", "Babs, MollyMgr – 000012439");
                Dropdown.SelectWebListByPartialSearch_Revit(context, "workLocationSelect", "DOTH2", "DOTH2 - Dothan Satellite Office");
//            Dropdown.SelectWebList_Revit(context,"reportingLocationSelect", "001 - MCB-Hutchinson MN");
                Thread.sleep(3000);
                context.driver.findElement(By.xpath(".//*[contains(@class,'dijitTabContainerTop-tabs')]/div[3]")).click();

                if (!(WebEdit.GetText(context, By.xpath(".//*[@id='compensationFrequency']")).equalsIgnoreCase("Hourly"))) {
                    Dropdown.SelectWebListByPartialSearch_Revit(context, "compensationFrequency", "Hour", "Hourly");
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
//            context.driver.findElement(By.id("newPayRate")).clear();
//            context.driver.findElement(By.id("newPayRate")).sendKeys("30");
//            context.driver.findElement(By.id("annualBenefits")).click();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
//			//Click on Next Button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
//			//Click on State tab
//			driver.findElement(By.xpath(".//*[@class='revitTabController dijitTabContainerTop-tabs dijitTabNoLayout']/div[2]")).click();
//			WebElements.selectWebList(driver,"stateWorkedInState", "GA - Georgia");
//			WebElements.selectWebList(driver,"stateSuiSdi", "GA - Georgia");
//
//			//Click on Next button
//			driver.findElement(By.xpath(".//*[starts-with(@class,'dijit dijitReset revitButtonHasIcon workflowWizardControlNext')]")).click();
                //click on done button
                context.driver.findElement(By.xpath(".//*[contains(@class,'workflowWizardControlDone')]")).click();
                WaitingFunctions.WaitUntilPageLoads(context);

                context.driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
                context.driver.findElement(By.id("submitterComments")).sendKeys("New Hire testing automation");
                context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                context.driver.findElement(By.id("btnDoneTemplate")).click();
                WaitingFunctions.WaitUntilPageLoads(context);
//            context.driver.manage().timeouts().implicitlyWait(240, TimeUnit.SECONDS);
                WaitingFunctions.nullifyImplicitWait(context);
//			try{
//				driver.findElement(By.id("newHireMessages")).getText();
//			}catch(Exception e){}
//			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                context.addResult("New Hire Fill Employee Job Information", "Fill employee Job Information for the new hire", "Third Step of NewHire has to complete", "New Fire third step failed", RunStatus.FAIL, Screenshot.getSnap(context, "JobInfo"));
            }
        }
        return false;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }


    public static String generateNHID(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd"); // Just the year, with 2 digits
        String formattedDate = sdf.format(Calendar.getInstance().getTime());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NH");
        stringBuilder.append((int)(Math.random()*100));
//      stringBuilder.append(Calendar.getInstance().get(Calendar.DATE));
//      stringBuilder.append(Calendar.getInstance().get(Calendar.MONTH)+1);
        stringBuilder.append(formattedDate);
        stringBuilder.append((int)(Math.random()*100)) ;
        return stringBuilder.toString();
    }

}
