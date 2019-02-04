package com.adp.autopay.automation.pagespecificlibrary;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.pagerepository.BenefitsLandingPage;
import com.adp.autopay.automation.utility.ExecutionContext;

public class BenefitsLanding extends CustomWebElement
{
	/**
	 * This Method is used to click on the Restart/Edit button on the benefits landing page.
	 * 
	 * @param context
	 * @param Heading
	 * @param Button
	 * @return
	 */
	public static boolean ClickButtonforEvent(ExecutionContext context,String Button,String Heading){
		try{
			List<WebElement> Events = FindElements(context, BenefitsLandingPage.Events);
			List<WebElement> Eventheaders = FindElements(context, BenefitsLandingPage.Eventheaders);
			for(int i=0;i<Events.size();i++){
				if(Eventheaders.get(i).getText().trim().equalsIgnoreCase(Heading)){
					List<WebElement> buttons = Events.get(i).findElements(BenefitsLandingPage.EventButtons);
					for(WebElement button:buttons){
						if(button.getText().trim().equalsIgnoreCase(Button)){
							button.click();
							return true;
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
