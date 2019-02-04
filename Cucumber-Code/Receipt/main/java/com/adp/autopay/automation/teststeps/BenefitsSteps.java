package com.adp.autopay.automation.teststeps;

import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.commonlibrary.WaitingFunctions;
import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.PortalPage;
import com.adp.autopay.automation.utility.ExecutionContext;

import cucumber.api.java.en.Then;
import cucumber.runtime.java.ObjectContainer;


public class BenefitsSteps extends CustomWebElement
{

	ExecutionContext context;

	public BenefitsSteps()
	{
		this.context = ((ExecutionContext) ObjectContainer.getInstance(ExecutionContext.class));
	}

	@Then("COMMONREV:I have to verify the page \"(.*)\" got Loaded")
	public void VerifySAPages(String SAPage){
		try{
			int flag = 0;
			switch (SAPage)
			{
			case "General Setup"		: 
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context, PortalPage.SA_generalSetup);
				if(FindElement(context, PortalPage.SA_generalSetup).isDisplayed())
				{
					flag =1;break;
				}
			case "Benefit Areas"		: 
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context,PortalPage.SA_benefitArea);
				if(FindElement(context, PortalPage.SA_benefitArea).isDisplayed())
				{
					flag =1;break;
				}
			case "Options"				: 
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context,PortalPage.SA_Options);
				if(FindElement(context, PortalPage.SA_Options).isDisplayed())
				{
					flag =1;break;
				}
			case "Coverage Levels/Tiers"		:
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context,PortalPage.SA_CoverageLevels);
				if(FindElement(context, PortalPage.SA_CoverageLevels).isDisplayed())
				{
					flag =1;break;
				}
			case "Prices"				:
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context,PortalPage.SA_Prices);
				if(FindElement(context, PortalPage.SA_Prices).isDisplayed())
				{
					flag =1;break;
				}
			case "Programs"				:
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context,PortalPage.SA_Programs);
				if(FindElement(context, PortalPage.SA_Programs).isDisplayed())
				{
					flag =1;break;
				}
			case "Utilities"			:
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context,PortalPage.SA_Utilities);
				if(FindElement(context, PortalPage.SA_Utilities).isDisplayed())
				{
					flag =1;break;
				}
			case "Additional Benefits"	:
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context,PortalPage.SA_AdditionalBenefits);
				if(FindElement(context, PortalPage.SA_AdditionalBenefits).isDisplayed())
				{
					flag =1;break;
				}
			case "Questionnaire"		:
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context,PortalPage.SA_Surcharge);
				if(FindElement(context, PortalPage.SA_Surcharge).isDisplayed())
				{
					flag =1;break;
				}
			case "Document Library"		:
				/************
				 * Waiting for Specific Labels
				 */
				WaitingFunctions.waitForAnElement(context,PortalPage.SA_DocumentLibrary);
				if(FindElement(context, PortalPage.SA_DocumentLibrary).isDisplayed())
				{
					flag =1;break;
				}
			}
			if(flag == 1)
			{
				System.out.println("*** SA Page successfully Loaded : "+SAPage);
				this.context.addResult("SA Page Verifications","Verify SA Page got loaded successfully","SA Page "+SAPage+" should be load successfully","SA Page "+SAPage+" loaded successfully", RunStatus.PASS);
			}
			else
			{
				System.out.println("*** Failed to Load SA Page");
				this.context.addResult("SA Page Verifications","Verify SA Page got loaded successfully","SA Page "+SAPage+" should be load successfully","SA Page "+SAPage+" Failed to load", RunStatus.FAIL, Screenshot.getSnap(context, "SAPage"));
			}
		}catch (Exception e)
		{
			this.context.addResult("SA Page Verifications","Verify SA Page got loaded successfully","SA Page "+SAPage+" should be load successfully","SA Page "+SAPage+" Failed to load", RunStatus.FAIL, Screenshot.getSnap(context,"SAPage"));
			e.printStackTrace();
		}
	}
}
