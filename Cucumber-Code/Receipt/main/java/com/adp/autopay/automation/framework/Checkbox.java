package com.adp.autopay.automation.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.utility.ExecutionContext;

public class Checkbox extends CustomWebElement {
	
	public static void Check(ExecutionContext context,By by){		
		if(!isChecked(context, by)){
			FindElement(context,by).click();
		}		
	}
	
	public static void unCheck(ExecutionContext context,By by){
		if(isChecked(context, by)){
			FindElement(context,by).click();
		}
	}
	
	public static boolean isPresent(ExecutionContext context,By by){
		return FindElement(context,by).isDisplayed();
	}
	
	public static boolean isEnabled(ExecutionContext context,By by){
		return FindElement(context,by).isEnabled();
	}
	
	public static boolean isChecked(ExecutionContext context,By by){
		WebElement ele = FindElement(context, by);
		return ele.findElement(By.tagName("input")).getAttribute("checked").equals("true");
	}
}
