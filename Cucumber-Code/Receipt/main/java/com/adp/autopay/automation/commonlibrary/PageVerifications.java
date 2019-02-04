package com.adp.autopay.automation.commonlibrary;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.pagerepository.Common;
import com.adp.autopay.automation.utility.ExecutionContext;

public class PageVerifications extends CustomWebElement
{
	public static boolean VerifyPage(ExecutionContext context,String page){
		try{
//			if(FindElement(context,By.xpath(".//span[contains(@class,'header-text')]']")).getText().trim().equals(page.trim())){
			List<WebElement> Headings = FindElements(context, Common.PageHeadings);
			for(WebElement ele:Headings){
				
				System.out.println(ele.getText().trim()+"***********");
				
				if(ele.getText().trim().equalsIgnoreCase(page)){
						return true;
				}
			}
		}catch(Exception e){
			System.out.println("Page not found");
		}
		return false;
	}
	
	}
