package com.adp.autopay.automation.framework;

import java.util.List;

import com.adp.autopay.automation.utility.ExecutionContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RadioButton extends CustomWebElement{
	public static void Select(ExecutionContext context, By by, String strOptionToSelect){
		List<WebElement> radioGroup = context.driver.findElements(by);
		if(IsPresent(context,by)){
			if(IsEnabled(context,by)){
				for (WebElement element : radioGroup){
					if(element.getAttribute("value").trim().equalsIgnoreCase(strOptionToSelect)){
						if(!element.isSelected()){
								element.click();
								break;
						}
						else{
							System.out.println("Radio button selected already");
							break;
						}
					}
				}
			}
		}
	}
	
		public static boolean VerifySelect(ExecutionContext context, By by, String strOptionToSelect){
			List<WebElement> radioGroup = context.driver.findElements(by);
			if(IsPresent(context,by)){
				if(IsEnabled(context,by)){
					for (WebElement element : radioGroup){
						if(element.getAttribute("value").trim().equalsIgnoreCase(strOptionToSelect)){
							if(element.isSelected()){
									return true;
							}
							else{
								System.out.println("Radio button selected already");
								return false;
							}
						}
					}
				}
			}
			return false;
		}
		
	public static boolean IsPresent(ExecutionContext context,By by){
		WebElement ele = FindElement(context, by);
		if(ele.isDisplayed()){
			return true;
		}
		return false;
	}
	
	
	public static boolean IsEnabled(ExecutionContext context,By by){
		WebElement ele = FindElement(context, by);
		if(ele.isEnabled()){
			return true;
		}
		return false;
	}
	
}
