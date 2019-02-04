package com.adp.autopay.automation.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.utility.ExecutionContext;

public class WebEdit extends CustomWebElement{		
	
	public static void SetText(ExecutionContext context,By by, String str){
		try{
		WebElement ele = FindElement(context, by);
		if(IsPresent(context,by)){
			if(IsEnabled(context,by)){
				ele.clear();
				ele.sendKeys(str);	
			}
		}
		}
		catch(Exception ex){}
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
	
	public static String GetText(ExecutionContext context,By by){
		WebElement ele = FindElement(context, by);
		if(IsPresent(context, by)){
			if(IsEnabled(context,by)){
				return ele.getText();
			}
		}
		return "";
	}


	public static boolean FillWebEdit(ExecutionContext context, By by, String strFillData){
		try{
			WebElement ele = context.driver.findElement(by);
			ele.clear();
			ele.sendKeys(strFillData);
			return true;
		}catch(Exception e){
			System.out.println("Exception occured while Filling WebEdit. Exception:=" + e.getMessage());
		}
		return false;
	}
}
