package com.adp.autopay.automation.framework;

import com.adp.autopay.automation.utility.ExecutionContext;
import org.openqa.selenium.By;

public class Link extends CustomWebElement{

	public static void click(ExecutionContext context,By by){
		FindElement(context,by).click();
	}
	
	public static boolean IsPresent(ExecutionContext context,By by){
		return FindElement(context,by).isDisplayed();
	}	
}
