package com.adp.autopay.automation.framework;

import org.openqa.selenium.By;
import com.adp.autopay.automation.utility.ExecutionContext;

public class Button extends CustomWebElement {		
		
		public static void Click(ExecutionContext context,By by){
			FindElement(context,by).click();			
		}
		
		public static boolean IsPresent(ExecutionContext context,By by){
			return FindElement(context,by).isDisplayed();
		}
		
		public static boolean IsEnabled(ExecutionContext context,By by){
			return FindElement(context,by).isEnabled();
		}
	
				
	}

