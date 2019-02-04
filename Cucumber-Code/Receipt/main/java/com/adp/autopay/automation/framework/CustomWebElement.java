package com.adp.autopay.automation.framework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.utility.ExecutionContext;

public class CustomWebElement{
	
	public static WebElement FindElement(ExecutionContext context,By by){
		return context.driver.findElement(by);
	}
	
	public static List<WebElement> FindElements(ExecutionContext context,By by){
		return context.driver.findElements(by);
	}
}
