package com.adp.autopay.automation.teststeps;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.commonlibrary.WaitingFunctions;
import com.adp.autopay.automation.framework.Button;
import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.pagerepository.EnrollmentPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.adp.autopay.automation.commonlibrary.DateFunctions;
import com.adp.autopay.automation.framework.Element;
import com.adp.autopay.automation.framework.WebEdit;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.Common;
import com.adp.autopay.automation.utility.ExecutionContext;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.ObjectContainer;


public class EnrollmentSteps extends CustomWebElement {


	ExecutionContext context;

	public EnrollmentSteps()
	{
		this.context = ((ExecutionContext) ObjectContainer.getInstance(ExecutionContext.class));
	}

	@Then("^Enrollments:I click the text: \"(.*)\" in pop up with font \"(.*)\", Weight \"(.*)\" and Color \"(.*)\"$")
	public void verifytextonpopup(String textmessage, String Font, String weight, String color){
		try{
			List<WebElement> ele = FindElements(context,By.xpath(".//*[@id='employeeSearchModalForm']//div/label"));

			for(int i=0;i<ele.size();i++){
				WebElement ele1 = ele.get(i); 
				if(ele1.getText().trim().toLowerCase().equalsIgnoreCase(textmessage))
				{               
					String ActFontSize=ele1.getCssValue("font-size");
					String Colour=ele1.getCssValue("color").toString();
					String ActFontColour=org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
					String ActFontWeight=ele1.getCssValue("font-weight");
					if(ActFontSize.equalsIgnoreCase(Font) && ActFontColour.equalsIgnoreCase(color) && ActFontWeight.equalsIgnoreCase(weight) ){
						this.context.addResult("Verify fontsize,fontcolour,fontweight of "+textmessage,"Verify fontsize fontcolour fontweight of "+textmessage, "Font size :"+Font+",Font Colour: "+color+",Font Weight:"+weight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.PASS);

						ele1.click();
						break;
					}else{
						this.context.addResult("Verify fontsize,fontcolour,fontweight of  "+textmessage, "Verify fontsize fontcolour fontweight of  "+textmessage, "Font size :"+Font+",Font Colour: "+color+",Font Weight:"+weight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, "Labelfailure"));
						break;
					}
				}
			}    
		}catch(Exception e){
			e.printStackTrace();
			this.context.addResult("Verify fontsize,fontcolour,fontweight of  "+textmessage, "Verify fontsize fontcolour fontweight of  "+textmessage, "Font size :"+Font+",Font Colour: "+color+",Font Weight:"+weight, "", RunStatus.FAIL,Screenshot.getSnap(context, "Labelfailure")); 
		}
	}

	@Then("^Enrollments: I click button:\"(.*)\" and verify font \"(.*)\", Weight $\"(.*)\" and Color \"(.*)\"$")
	public void verifyPrefbuttons(String button, String Font, String weight, String color){
		try{
			List<WebElement> ele = FindElements(context, EnrollmentPage.Popupbuttons);

			for(int i=0;i<ele.size();i++){
				WebElement ele1 = ele.get(i); 
				if(ele1.getText().trim().toLowerCase().equalsIgnoreCase(button))
				{               
					String ActFontSize=ele1.getCssValue("font-size");
					String Colour=ele1.getCssValue("color").toString();
					String ActFontColour=org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
					String ActFontWeight=ele1.getCssValue("font-weight");
					if(ActFontSize.equalsIgnoreCase(Font) && ActFontColour.equalsIgnoreCase(color) && ActFontWeight.equalsIgnoreCase(weight) ){
						this.context.addResult("Verify fontsize,fontcolour,fontweight of "+button,"Verify fontsize fontcolour fontweight of "+button, "Font size :"+Font+",Font Colour: "+color+",Font Weight:"+weight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.PASS);

						ele1.click();
						break;
					}else{
						this.context.addResult("Verify fontsize,fontcolour,fontweight of  "+button, "Verify fontsize fontcolour fontweight of  "+button, "Font size :"+Font+",Font Colour: "+color+",Font Weight:"+weight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.FAIL,Screenshot.getSnap(context, "Labelfailure"));
						break;
					}
				}
			}    
		}catch(Exception e){
			e.printStackTrace();
			this.context.addResult("Verify fontsize,fontcolour,fontweight of  "+button, "Verify fontsize fontcolour fontweight of  "+button, "Font size :"+Font+",Font Colour: "+color+",Font Weight:"+weight, "", RunStatus.FAIL,Screenshot.getSnap(context, "Labelfailure")); 
		}
	}

	@When("^Enrollments:I give the Field: \"(.*)\" with Value:\"(.*)\"$")
	public void SearchEmployees(String Field, String Value){
		try{

			FindElement(context, By.xpath(".//label[contains(text(),'"+Field+"')]//following-sibling::div/div/input[@id='employeeSearchDate']")).clear();
			FindElement(context, By.xpath(".//label[text()='"+Field+"']/parent::div//input[@class='dijitReset dijitInputInner']")).sendKeys(Value);
			WaitingFunctions.WaitUntilPageLoads(context);
			this.context.addResult("Search for employee with "+Field+" as"+Value, "Search for employee with "+Field+" as"+Value, "Search successfull", "Search successfull", RunStatus.PASS);}
		catch(Exception e)
		{
			e.printStackTrace();
			this.context.addResult("Search for employee with "+Field+" as"+Value, "Search for employee with "+Field+" as"+Value, "Search successfull", "Exception occured", RunStatus.FAIL,Screenshot.getSnap(context, "SearchFail"));
		}
	}

	@Then("^Enrollments: I should verify the Table data FontSize:\"(.*)\", FontColor:\"(.*)\" and FontWeight:\"(.*)\" for participant having FieldValue: \"(.*)\"$")
	public void verifyTableheader(String Font, String color, String weight, String Value){
		try{
			List<WebElement> LabelList = FindElements(context,By.xpath(".//th[contains(@id,'tableGrid_header')]"));

			for(int i=0;i<LabelList.size();i++){
				WebElement FilterLabel = LabelList.get(i); 
				if(FilterLabel.getText().trim().toLowerCase().contains(Value.toLowerCase()))
				{

					String ActFontSize=FilterLabel.getCssValue("font-size");
					String Colour=FilterLabel.getCssValue("color").toString();
					String ActFontColour=org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
					String ActFontWeight=FilterLabel.getCssValue("font-weight");
					if(ActFontSize.equalsIgnoreCase(Font) && ActFontColour.equalsIgnoreCase(color) && ActFontWeight.equalsIgnoreCase(weight) ){
						this.context.addResult("Verify fontsize,fontcolour,fontweight of "+Value,"Verify fontsize fontcolour fontweight of "+Value, "Font size :"+Font+",Font Colour: "+color+",Font Weight:"+weight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.PASS);
						break; 
					}else{
						this.context.addResult("Verify fontsize,fontcolour,fontweight of  "+Value, "Verify fontsize fontcolour fontweight of  "+Value, "Font size :"+Font+",Font Colour: "+color+",Font Weight:"+weight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.FAIL,Screenshot.getSnap(context, "Labelfailure"));
						break;
					}
				}
			}    
		}catch(Exception e){
			e.printStackTrace();
			this.context.addResult("Verify fontsize,fontcolour,fontweight of  " + Value, "Verify fontsize fontcolour fontweight of  " + Value, "Font size :" + Font + ",Font Colour: " + color + ",Font Weight:" + weight, "", RunStatus.FAIL, Screenshot.getSnap(context, "Labelfailure"));
		}


	}


	@When("^ENROLLMENTS:I select the EOBO popup Option \"(.*)\" with a date as \"(.*)\"$")
	public void SelectEOBOOption(String Option,String Date){
		try{
			context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); 
			boolean flag = false;
			if(Option.toLowerCase().contains("enroll on behalf of")){
				WebElement element = FindElement(context,EnrollmentPage.EOBO);
				element.findElement(By.tagName("input")).click();
				WaitingFunctions.WaitUntilPageLoads(context);
				//this.context.addResult("EOBo popup", "Select EOBO Poupu Option as: " + Option, "User has to select EOBO Option as : " + Option, "EOBO Option selected successfully", RunStatus.PASS);
				flag = true;
			}
			if(Option.toLowerCase().contains("test as")){
				WebElement element = FindElement(context,EnrollmentPage.TestAs);
				element.findElement(By.tagName("input")).click();
				if(Date.equalsIgnoreCase("TODAY")){
					Date = DateFunctions.TodayMMDDYYYYWithSlashes();
				}
				WebEdit.SetText(context, EnrollmentPage.TestAsDate, Date);
				flag = true;
				//this.context.addResult("EOBo popup", "Select EOBO Poupu Option as: " + Option, "User has to select EOBO Option as : " + Option, "EOBO Option selected successfully", RunStatus.PASS);
			}
			if(flag == true){
				Element.Click(context,EnrollmentPage.EOBO_OK);
				WaitingFunctions.WaitUntilLoadcleared(context, Common.RevitLoading);
			}
			context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS); 
		}catch (Exception e){
			//e.printStackTrace();
		}
	}

	@When("ENROLLMENTSREV:I Select the event \"(.*)\" in the Enrollments Page")
	public void SelectEventInAdminPageRev(String Event) {
		try {
			WaitingFunctions.waitForAnElement(context, By.xpath("//*[@class='welcometext']//td[@class='ADPUI-normalText']//span[contains(.,'"+Event+"') and @id[contains(.,'label')]]"));
			WebElement Eventelement = FindElement(context,By.xpath("//*[@class='welcometext']//td[@class='ADPUI-normalText']//span[contains(.,'"+Event+"') and @id[contains(.,'label')]]"));
			Eventelement.click();
			System.out.println("Event Selected: " + Event);
			this.context.addResult("Select Event From Enrollments Page", "Select Event From Enrollments Page: " + Event, "Event has to be selected", "Event Selected Successfully", RunStatus.PASS);
			WaitingFunctions.WaitUntilPageLoads(context);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Select Event From Enrollments Page", "Select Event From Enrollments Page: " + Event, "Event has to be selected", "Exception in Executing the Step", RunStatus.FAIL, Screenshot.getSnap(context, "EventSelection"));
		}
	}
	
	@When("^ENROLLMENTS:I click on the \"(.*)\" Button on the Enrollments Page$")
	public void SelectEvent(String EventName){
		try {
			WaitingFunctions.waitforPageLoad(context);
			String eventName=Element.getText(context, EnrollmentPage.EnrollmentBanner).trim();
			if (eventName.equalsIgnoreCase(EventName)) {
				Element.Click(context, EnrollmentPage.EnrollToday);
				WaitingFunctions.WaitUntilPageLoads(context);
				this.context.addResult("Click Enroll Button", "Click enroll button for event: " + EventName, "User has to click the enroll button for event: "+EventName, "Enroll Button Selected successfully for event: "+eventName, RunStatus.PASS);
			} else if(Element.IsPresent(context,EnrollmentPage.EnrollEvent)){
				Element.Click(context,EnrollmentPage.EnrollEvent);
				System.out.println("*** Unable to found Annual Enrollment Event , Processing with : " + eventName);
				this.context.addResult("Click Enroll Button", "Click enroll button for event: " + EventName, "User has to click the enroll button for event: "+EventName, "Enroll Button Selected successfully for event: "+eventName, RunStatus.PASS);
			}
			else{
				this.context.addResult("Click Enroll Button", "Click enroll button for event: " + EventName, "User has to click the enroll button for event: "+EventName, "Enroll Button Not Selected for Event: " + eventName, RunStatus.FAIL, Screenshot.getSnap(context, "enroll"));
			}
		}catch (Exception e){
			e.printStackTrace();
			this.context.addResult("Click Enroll Button", "Click enroll button for event: " + EventName, "User has to click the enroll button", "Enroll Button Not Selected for Event: " + EventName, RunStatus.FAIL, Screenshot.getSnap(context, "enroll"));
		}
	}
	
	/* This is to select any benefit from summary page*/
	@When("^ENROLLMENTS:I Click on \"(.*)\" option in summary page$")
	public void SelectBenefit(String SBenefit){
		try{
			if (Element.IsPresent(context, EnrollmentPage.WIPPage))
			{
				List<WebElement> WIPOptions = FindElements(context, EnrollmentPage.OptionsinWIPPage);
				List<WebElement> WIPRadioButtons = FindElements(context, EnrollmentPage.OptRadioButtonsinWIPPage);
				for (int i = 0; i < WIPOptions.size(); i++) {
					if(WIPOptions.get(i).getText().trim().contains("discard")) {
						WIPRadioButtons.get(i).click();
						FindElement(context, EnrollmentPage.WIPContinueButton).click();
						System.out.println("existing elections are discarded");
						this.context.addResult("Existing Elections are discrded in Work In Progress Elections page", "Existing Elections are discrded in Work In Progress Elections page", "Existing Elections has to be discarded", "Existing Elections discarded Successfully", RunStatus.PASS);
						WaitingFunctions.WaitUntilPageLoads(context);
					}
				}
			}
			List<WebElement> Benefits = FindElements(context, EnrollmentPage.SummaryBenefitsLinks);
			for (int i = 0; i < Benefits.size(); i++) {
				if (Benefits.get(i).getText().trim().contains(SBenefit)) {
					Benefits.get(i).click();
					System.out.println("Benefit Selected is: " + SBenefit);
					this.context.addResult("Select any benefit From Summary Page", "Select any benefit From Summary Page: " + SBenefit, "Benefit has to be selected", "Benefit Selected Successfully", RunStatus.PASS);
					WaitingFunctions.WaitUntilPageLoads(context);
					return;
		}
			}
			this.context.addResult("Select any benefit From Summary Page", "Select any benefit From Summary Page: " + SBenefit, "Benefit has to be selected", "Benefit not found", RunStatus.FAIL, Screenshot.getSnap(context, "BenefitSelection"));
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Select any benefit From Summary Page", "Select any benefit From Summary Page: " + SBenefit, "Benefit has to be selected", "Exception in Executing the Step", RunStatus.FAIL, Screenshot.getSnap(context, "BenefitSelection"));
		}
	}
	
	@When("^ENROLLMENTS: I Select \"(.*)\" as Coverage Information for the benefit$")
	public void SelectCoverage(String DName){
		try{
			List<WebElement> DNames = FindElements(context, EnrollmentPage.OptDepCoverage);
			List<WebElement> OptCovCheckBox = FindElements(context, EnrollmentPage.OptDepCoverageCheckBox);
			for (int i=0; i<DNames.size(); i++){
				if (DNames.get(i).getText().trim().contains(DName)){
					if(OptCovCheckBox.get(i).isEnabled() && OptCovCheckBox.get(i).getAttribute("src").trim().contains("unchecked")){
						OptCovCheckBox.get(i).click();
						System.out.println("DependentCovered for Option plus Dep Benefit is: " + DName);
						this.context.addResult("Selected Dependent for Option plus dep type benefits", "Selected Dependent for Option plus dep type benefits is : " + DName, "Dependent has to be selected for opt plus dep type benefits", "Dependent is selected Successfully for opt plus dep type benefits", RunStatus.PASS);
						WaitingFunctions.WaitUntilPageLoads(context);
						return;
					}				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Select Dependent for Option plus dep type benefits", "Select Dependent for Option plus dep type benefits: " + DName, "Dependent has to be selected for Option plus dep type benefits", "Exception in Executing the Step", RunStatus.FAIL, Screenshot.getSnap(context, "DependentSelection"));
		}		
	}
	/********************
	 * Description: This method selects the Options for Benefit ex: Aetna for Medical
	 * We need to pass the Options to be selected as parameter. If "*" is passed as parameter options selection will be either Aetna/Oxford (If Aetna is already selected -> will be changed to Oxford and viceversa)
	 */
	@When("^ENROLLMENTS:I Select \"(.*)\" as option for benefit$")
	public void SelectOption(String SOption){
		try{
			List<WebElement> Options = FindElements(context, EnrollmentPage.BenefitOptions);
			List<WebElement> OptRButtons = FindElements(context, EnrollmentPage.BenefitOptionRadioButtons);
			
			if (SOption.equalsIgnoreCase("*")) {
				for (int i = 0; i < Options.size(); i++) {
					if (Options.get(i).getText().trim().contains("Aetna") && OptRButtons.get(i).getAttribute("src").trim().contains("radio-checked")){
						i++;
						OptRButtons.get(i).click();
						System.out.println("Oxford Option is Selected");
						i--;
						this.context.addResult("Select any option for the benefit", "Select any option for the benefit: " + SOption, "Option has to be selected", "Option Selected Successfully", RunStatus.PASS);
						WaitingFunctions.WaitUntilPageLoads(context);
						break;
						}
					else if(Options.get(i).getText().trim().contains("Oxford") && OptRButtons.get(i).getAttribute("src").trim().contains("radio-checked")){
						i--;
						OptRButtons.get(i).click();
						System.out.println("Aetna Option is Selected");
						i++;
						this.context.addResult("Select any option for the benefit", "Select any option for the benefit: " + SOption, "Option has to be selected", "Option Selected Successfully", RunStatus.PASS);
						WaitingFunctions.WaitUntilPageLoads(context);
						break;
					}
				}
					
			}
			else {
				for (int i = 0; i < Options.size(); i++) {
				if (Options.get(i).getText().trim().contains(SOption)){
					OptRButtons.get(i).click();
					System.out.println("Option Selected is: " + SOption);
					this.context.addResult("Select any option for the benefit", "Select any option for the benefit: " + SOption, "Option has to be selected", "Option Selected Successfully", RunStatus.PASS);
					WaitingFunctions.WaitUntilPageLoads(context);
					return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("Select any option for the benefit", "Select any option for the benefit: " + SOption, "Option has to be selected", "Exception in Executing the Step", RunStatus.FAIL, Screenshot.getSnap(context, "OptionSelection"));
		}
	}
	
	@When("^ENROLLMENTS:I Select waivereason as \"(.*)\" for waivedcoverage option$")
	public void SelectReason(String WReason){
		try{
			Element.Click(context, By.xpath(".//div[contains(@id,'widget_revit_form_FilteringSelect')]/div[contains(@class,'dijitDownArrowButton')]/input"));
			List<WebElement> WReasons = FindElements(context, By.xpath(".//div[contains(@id, 'revit_form_FilteringSelect') and contains(@role, 'option')]"));
			for (int i = 0; i < WReasons.size(); i++) {
				if (WReasons.get(i).getText().trim().contains(WReason)){ 
					WReasons.get(i).click();       
					System.out.println("WaiveReason selected is: " + WReason);
					this.context.addResult("WaiveReason should be selected", "WaiveReason should be selected", "Waive Reason is selected as : " + WReason, "WaiveReason selected suceessfully", RunStatus.PASS);
				}
			}
		}catch(Exception e){
				e.printStackTrace();
				this.context.addResult("WaiveReason should be selected", "WaiveReason should be selected", "WaiveReason is: " + WReason, "Exception in executing the step", RunStatus.FAIL,Screenshot.getSnap(context,"WaiverReasonSelection"));
			}
	}
	
	/* This method is to set the provider ID in the Provider Information Page */	
	@When("^ENROLLMENTS:I provided Provider ID as \"(.*)\" for Name \"(.*)\"$")
	public void ProviderInformation(String ProviderID, String PName){
		try{
			List<WebElement> providerIDs = FindElements(context, EnrollmentPage.Provider);
			List<WebElement> PNames = FindElements(context, EnrollmentPage.ProviderInputFields);
			for (int i = 0; i < PNames.size(); i++){
				if(PNames.get(i).getText().trim().contains(PName)){
					providerIDs.get(i).clear();
					providerIDs.get(i).sendKeys(ProviderID);
					this.context.addResult("provider info", "give provider info as: " + ProviderID, "User has to give provider info as : " + ProviderID, "provider info has given successfully", RunStatus.PASS);
				}		
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.context.addResult("give provider info", "give provider info as: " + ProviderID, "provider info has to be given", "Exception in Executing the Step", RunStatus.FAIL, Screenshot.getSnap(context, "providerInfo"));
		}
	}

	
	@Then("ENROLLMENTSREV:I have to be in the Summary page of Enrollments")
	public void VerifySummaryPage(){
		try{
			WaitingFunctions.waitForElement(context, EnrollmentPage.SummaryPageConfirmation, 10);
			if(FindElement(context,EnrollmentPage.SummaryPageConfirmation).isDisplayed()){
				System.out.println("*** User is in Summary Page");
				this.context.addResult("Verify Summary page","Verify user is in Summary Page","User should be in summary page","User is in summary page",RunStatus.PASS);
			}
		}catch (Exception e){
			e.printStackTrace();
			this.context.addResult("Verify Summary page", "Verify user is in Summary Page", "User should be in summary page", "User is not in summary page", RunStatus.FAIL, Screenshot.getSnap(context, "SummaryPage"));
		}
	}
	
	@Then("ENROLLMENTSVDL:I have to be in the Summary page of Enrollments")
	public void verifyVDLSummaryPage(){
		try{
			WaitingFunctions.waitForElement(context, EnrollmentPage.SummaryPageConfirmationVDL, 10);
			if(FindElement(context,EnrollmentPage.SummaryPageConfirmationVDL).isDisplayed()){
				System.out.println("*** User is in Summary Page");
				this.context.addResult("Verify Summary page","Verify user is in Summary Page","User should be in summary page","User is in summary page",RunStatus.PASS);
			}
		}catch (Exception e){
			e.printStackTrace();
			this.context.addResult("Verify Summary page", "Verify user is in Summary Page", "User should be in summary page", "User is not in summary page", RunStatus.FAIL, Screenshot.getSnap(context, "SummaryPage"));
		}
	}

	@When("ENROLLMENTSREV:I click on \"(.*)\" Button")
	public void ClickEnrollmentsButtonRev(String Button){
		try{
			WaitingFunctions.waitforPageLoad(context);
			WebElement element = FindElement(context, By.xpath(".//*[text()='"+Button+"']"));
			WaitingFunctions.WaitUntilPageLoads(context);
			if(element.isDisplayed()){
				element.click();
				this.context.addResult("Click "+Button,"Click "+Button,"Button has to click","Button Clicked Successfully",RunStatus.PASS);
			}
			else{
				this.context.addResult("Click "+Button,"Click "+Button,"Button has to click","Button not found",RunStatus.FAIL,Screenshot.getSnap(context,"ButtonClick"));
			}
		}catch (Exception e){
			e.printStackTrace();
			this.context.addResult("Click " + Button, "Click " + Button, "Button has to click", "Button not found", RunStatus.FAIL, Screenshot.getSnap(context, "ButtonClick"));
		}
	}
	
	@Then("^ENROLLMENTS:I Should be on Provider Page$")
	public void providerpagevalidation(){
		try{
			WebElement provider = FindElement(context, By.xpath(".//form[contains(@id,'providerDetailsForm')]//b[contains(text(), 'Provider')]"));
			if(provider.isDisplayed()){
				System.out.println("User is on provider Information page");
				this.context.addResult("Should be on Provider Info Page","Should be on Provider Info Page","Should be on Provider Info Page","User is on provider info page successfully",RunStatus.PASS);
			}
			else{
				this.context.addResult("Should be on Provider Info Page","Should be on Provider Info Page","Should be on Provider Info Page","Provider Page not found",RunStatus.FAIL,Screenshot.getSnap(context,"ProviderPage"));
			}
		} catch (Exception e){
			e.printStackTrace();
			this.context.addResult("Should be on Provider Info Page","Should be on Provider Info Page","Should be on Provider Info Page","Exception in executing the steps",RunStatus.FAIL,Screenshot.getSnap(context,"ProviderPage"));
		}
	}

	@When("^ENROLLMENTSREV:I Select E-mail Option \"(.*)\" and give email as \"(.*)\" and i have to click on Submit Button$")
	public  void EnrollmentsMailOptionRev(String OptionNumber,String Mail){
		try{
			if(OptionNumber.equalsIgnoreCase("one"))
			{
				/*****************
				 * 
				 * Checking the radio Button is already checked or not
				 */

				if(FindElement(context,EnrollmentPage.Email_Normal_checked).getAttribute("class").contains("Checked"))
				{
					System.out.println("DefaultEmail Radio Button Alraedy Checked");
				}
				else
				{
					Element.Click(context,EnrollmentPage.Email_Normal);

				}

				Button.Click(context, EnrollmentPage.Submit);
				WaitingFunctions.WaitUntilPageLoads(context);
				this.context.addResult("Enrollments Email confirmation","Enrollment email option selection page verification","user has to select :I would like an email sent to the address shown below","user selected: I would like an email sent to the address shown below", RunStatus.PASS);
			}
			if(OptionNumber.equalsIgnoreCase("two"))
			{
				/*****************
				 * 
				 * Checking the radio Button is already checked or not
				 */
				System.out.println(FindElement(context,EnrollmentPage.Email_Reciepient_checked).getAttribute("class"));

				if(FindElement(context,EnrollmentPage.Email_Reciepient_checked).getAttribute("class").contains("Checked"))
				{
					System.out.println("Email Reciepient Radio Button Alraedy Checked");
				}
				else
				{
					Element.Click(context,EnrollmentPage.Email_Reciepient);	
				}

				WebEdit.SetText(context, EnrollmentPage.Email_input, Mail);
				Button.Click(context, EnrollmentPage.Submit);
				WaitingFunctions.WaitUntilPageLoads(context);
				this.context.addResult("Enrollments Email confirmation", "Enrollment email option selection page verification", "user has to select :I would like an email sent to the following address", "user selected: I would like an email sent to the following address", RunStatus.PASS);
			}
			if(OptionNumber.equalsIgnoreCase("three"))
			{
				/*****************
				 * 
				 * Checking the radio Button is already checked or not
				 */
				if(FindElement(context,EnrollmentPage.No_Email_checked).getAttribute("class").contains("Checked"))
				{
					System.out.println("No Email Radio Button Alraedy Checked");
				}
				else
				{
					Element.Click(context,EnrollmentPage.No_Email);
				}


				Button.Click(context,EnrollmentPage.Submit);
				WaitingFunctions.WaitUntilPageLoads(context);
				this.context.addResult("Enrollments Email confirmation", "Enrollment email option selection page verification", "user has to select :No Email Option", "user selected: No Email Option", RunStatus.PASS);
			}
		}catch (Exception e){
			e.printStackTrace();
			this.context.addResult("Enrollment email option selection page verification","Enrollment email option selection page verification","User has to select any email option","Exception Occured",RunStatus.FAIL,Screenshot.getSnap(context,"email"));
		}
	}

	@Then("^ENROLLMENTSREV:I have to get confirmation on the Enrollment Page as \"(.*)\"$")
	public void VerifyConfirmationRev(String ConfirmationMessage){
		try{
			System.out.println("Waiting for election confirmation message from 0-2 minutes");
			WaitingFunctions.waitForElement(context, EnrollmentPage.ElectionConfirmationMessage, 120);
			if(Element.getText(context,EnrollmentPage.ElectionConfirmationMessage).contains(ConfirmationMessage)){
				System.out.println("*** Elections confirmed successfully");
				this.context.addResult("Election confirmation message verification","Election confirmation message verification","Confirmation Message should be: You have successfully completed your enrollment. Your confirmation number is","Confirmation Message is: You have successfully completed your enrollment. Your confirmation number is",RunStatus.PASS);
			}
			else
			{
				System.out.println("*** Elections confirmed successfully");
				this.context.addResult("Election confirmation message verification","Election confirmation message verification","Confirmation Message should be: You have successfully completed your enrollment. Your confirmation number is","Confirmation Message is: "+Element.getText(context,EnrollmentPage.ElectionConfirmationMessage),RunStatus.FAIL,Screenshot.getSnap(context, "ConfirmationMessage") );
			}
			
		}catch (Exception e){
			e.printStackTrace();
			this.context.addResult("Election confirmation message verification", "Election confirmation message verification", "Confirmation Message should be: You have successfully completed your enrollment. Your confirmation number is", "User is unable to findout the confirmation message", RunStatus.FAIL,Screenshot.getSnap(context,"ConfirmationMessage"));
		}
	}

	@When("^ELECTION CONFIRMATIONREV:I Select the Election as \"(.*)\"")
	public void SelectElectiontoViewConfirmationRev(String ElectionConfirmation){
		try{
			if(context.browser.equalsIgnoreCase("IE"))
			{
				String cmd = "REG ADD \"HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\New Windows\" /F /V \"PopupMgr\" /T REG_SZ /D \"no\"";
				try {
					Runtime.getRuntime().exec(cmd);
				} catch (Exception e) {
					System.out.println("Error ocured!");
				}
			}
			if(FindElement(context, EnrollmentPage.ElectionConfirmation_view).isDisplayed())
			{
				Element.Click(context,EnrollmentPage.ElectionConfirmation_view);
				System.out.println("*** Option Selected to view confirmation report is: "+ElectionConfirmation);
				this.context.addResult("Select Report to view confirmation", "Select Report to view confirmation", "Report should be: " + ElectionConfirmation, "Report selected suceessfully", RunStatus.PASS);
			}
			else
			{
				this.context.addResult("Select Report to view confirmation", "Select Report to view confirmation", "Report should be: " + ElectionConfirmation, "Election type selection failed", RunStatus.FAIL,Screenshot.getSnap(context,"confirmation"));
			}

		}catch(Exception e){
			e.printStackTrace();
			this.context.addResult("Select Report to view confirmation", "Select Report to view confirmation", "Report should be: " + ElectionConfirmation, "Election type selection failed", RunStatus.FAIL,Screenshot.getSnap(context,"confirmation"));
		}
	}
	
	@When("^ELECTION CONFIRMATION:I select event as \"(.*)\" from the dropdown and click on view button$")
	public void SelectEventtoViewConfirmation(String Eventname){
		try{
			Element.Click(context,EnrollmentPage.ElectionConfirm_Dropdown);
			List<WebElement> eventsname = FindElements(context, EnrollmentPage.ElectionConfirm_DropdownList);
			for (int i = 0; i < eventsname.size(); i++) {
				if (eventsname.get(i).getText().trim().contains(Eventname)){ 
					Element.Click(context, By.xpath(".//*[contains(@id,'revit_form_FilteringSelect') and contains(text(), 'Annual Enrollment - 01/01/2016')]"));
				}
			}
	        
			if(FindElement(context, EnrollmentPage.ElectionConfirmation_view).isDisplayed())
				{
					Element.Click(context,EnrollmentPage.ElectionConfirmation_view);
					System.out.println("*** Option Selected to view confirmation report is: "+Eventname);
					this.context.addResult("Select Report to view confirmation", "Select Report to view confirmation", "Report should be: " + Eventname, "Report selected suceessfully", RunStatus.PASS);
				}
				else
				{
					this.context.addResult("Select Report to view confirmation", "Select Report to view confirmation", "Report should be: " + Eventname, "Election type selection failed", RunStatus.FAIL,Screenshot.getSnap(context,"confirmation"));
				} 

			}catch(Exception e){
				e.printStackTrace();
				this.context.addResult("Select Report to view confirmation", "Select Report to view confirmation", "Report should be: " + Eventname, "Election type selection failed", RunStatus.FAIL,Screenshot.getSnap(context,"confirmation"));
			}
	}
	
	
	
	@When("^ELECTION CONFIRMATIONREV:I have to View the Report in a new window and I have to verify the report date as today")
	public void VerifyConfirmationReportRev() {
		try {
			Thread.sleep(10000);
			String parentHandle = this.context.driver.getWindowHandle();

			for (String winHandle : this.context.driver.getWindowHandles()) {
				this.context.driver.switchTo().window(winHandle);
				if(this.context.driver.getTitle().contains("ADP")){
					parentHandle = this.context.driver.getWindowHandle();
				}
				while (!(this.context.driver.getTitle().contains("Portal")|| this.context.driver.getTitle().contains("ADP")))
				{
					if(this.context.browser.equalsIgnoreCase("IE"))
					{
						if(this.context.driver.getCurrentUrl().contains("ElectionSummary"))
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

						WaitingFunctions.waitForAnElement(context, By.xpath(".//*[@id='pageContainer1']//*[contains(.,'Your')]"));
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
	
	@When("^ELECTION CONFIRMATION:I have to \"(.*)\" the new Report window$")
	public void VerifyConfirmationRpt(String window) {
		try {
			Thread.sleep(10000);
			
			if (window.equalsIgnoreCase("view")) {
			for (String winHandle : this.context.driver.getWindowHandles()) {
				this.context.driver.switchTo().window(winHandle);
				
				while (!(this.context.driver.getTitle().contains("Portal")|| this.context.driver.getTitle().contains("ADP")))
				{
					if(this.context.browser.equalsIgnoreCase("IE"))
					{
						if(this.context.driver.getCurrentUrl().contains("ElectionSummary"))
						{
							System.out.println("*** Report Verification Successful");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "PDF Opened Successfully", RunStatus.PASS);
						}
						else
						{
							System.out.println("*** Report Verification failed");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "Report Verification Failed", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyPDF"));
							return;
						}
					}
					else if(context.browser.equalsIgnoreCase("firefox"))
					{

						WaitingFunctions.waitForAnElement(context, By.xpath(".//*[@id='pageContainer1']//*[contains(.,'Your')]"));
						if(this.context.driver.getTitle().trim().contains("Election")) {
							System.out.println("*** Report Verification Successful");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "PDF Opened Successfully", RunStatus.PASS);
							return;
						} else {
							System.out.println("*** Report Verification failed");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "Report Verification Failed", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyPDF"));
							return;
						}
					}

					else 
					{
						if(this.context.driver.getTitle().trim().contains("Election")) {
							System.out.println("*** Report Verification Successful");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "PDF Opened Successfully", RunStatus.PASS);
							return;
						} else {
							System.out.println("*** Report Verification failed");
							this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "Report Verification Failed", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyPDF"));
							return;
						}
					}
				}
			}
			}
			
			else{
				this.context.driver.close();
				for (String winHandle : this.context.driver.getWindowHandles()) {
					this.context.driver.switchTo().window(winHandle);
				}
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
			this.context.addResult("Election Confirmation report Verification", "Verify Election Confirmation Report", "PDF has to be Opened", "Report is unsuccessful", RunStatus.FAIL, Screenshot.getSnap(context, "VerifyPDF"));
		}
	}
	
	@When("^ELECTION CONFIRMATION:I have to verify for benefit \"(.*)\" creditlabel message is \"(.*)\"$")
	public void verifyCreditmessage(String Benefit, String Credmsg){
		try{
			WebElement element = FindElement(context, By.xpath(".//xhtml:div[text() = '"+Benefit+"']/following-sibling::xhtml:div[text() = '"+Credmsg+"'][1]"));
			if(element.isDisplayed()){
				System.out.println("*** Credit message is" + Credmsg + "for Benefit" + Benefit);
				this.context.addResult("credit label verification as" + Credmsg, "credit label verification as" + Credmsg, "credit label should be displayed", "Credit label displayed Successfully",RunStatus.PASS);
			}
			else{
				this.context.addResult("credit label verification as" + Credmsg, "credit label verification as" + Credmsg, "credit label should be displayed","credit label not found",RunStatus.FAIL,Screenshot.getSnap(context,"creditlabelverification"));
			}
		}catch (Exception e){
			e.printStackTrace();
			this.context.addResult("credit label verification as" + Credmsg, "credit label verification as" + Credmsg, "credit label should be displayed","credit label not found", RunStatus.FAIL, Screenshot.getSnap(context, "creditlabelverification"));
		}
		}
	
	@Then("I have to close the Application and Logoff the main Browser")
	public void LogoffTheApplication() {
		try{
			String ParentHandle = null;
			String SubHandle = null;
			for (String winHandle : this.context.driver.getWindowHandles()){
				if(this.context.driver.getTitle().contains("Portal")){
					ParentHandle = context.driver.getWindowHandle();
					break;
				}
				else{
					SubHandle = context.driver.getWindowHandle();
					context.driver.switchTo().window(winHandle);
				}
			}

			context.driver.switchTo().window(SubHandle);
			context.driver.close();
			context.driver.switchTo().window(ParentHandle);
			//		WebElement element = FindElement(context, By.xpath(".//*[@id='headerWrapper']//tr[@valign='top']//td[@class='menulogoff']//a"));
			//		element.click();
			//		Robot robot = new Robot();
			//		robot.keyPress(KeyEvent.VK_ENTER);
			//		Thread.sleep(5000);
			context.driver.manage().deleteAllCookies();
		}catch (Exception e) {
		}
	}
	
	

}