package com.adp.autopay.automation.framework;

import com.adp.autopay.automation.utility.ExecutionContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class Element extends CustomWebElement
{
	public static void HighlightElement(ExecutionContext context, WebElement element) {

	    for (int i = 0; i < 2; i++) {

	        JavascriptExecutor js = (JavascriptExecutor) context.driver;
	        js.executeScript("arguments[0].setAttribute('style', arguments[1]);",element, "color: yellow; border: 2px solid yellow;");
	        js.executeScript("arguments[0].setAttribute('style', arguments[1]);",element, "");

	    }

	}
	
	public static String getText(ExecutionContext context,By by){
		return FindElement(context, by).getText();
		
	}
	
	public static boolean IsPresent(ExecutionContext context,By by){
		try{
		if(FindElement(context,by).isDisplayed()){
			return true;
		}
		return false;
		}catch(Exception e){
			return false;
		}
		
	}
	
	public static boolean IsPresent(ExecutionContext context,WebElement Element){
		try{
		 if(Element.isDisplayed()){
			 return true;
		 }
		}catch(Exception e){
			return false;
		}
		return false;	
	}
	
	
	public static boolean IsEnabled(ExecutionContext context,By by){
		try{

		return FindElement(context,by).isEnabled();
		
	}catch(Exception e){
	return false;
	}
	}
	
	public static boolean IsEnabled(ExecutionContext context,WebElement Element){
		try{
		 if(Element.isEnabled()){
			 return true;
		 }
		}catch(Exception e){
			return false;
		}
		return false;	
	}
		
	
	public static void Click(ExecutionContext context,By by){
		FindElement(context, by).click();
	}
	
	public static boolean IsPresent_linkText(ExecutionContext context,String LinkText){
		return FindElement(context, By.partialLinkText(LinkText)).isDisplayed();
		
	}
}
