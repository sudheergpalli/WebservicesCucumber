package com.adp.autopay.automation.framework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.commonlibrary.DateFunctions;
import com.adp.autopay.automation.utility.ExecutionContext;

public class Calendar extends CustomWebElement {
	
	public static boolean FillWebDate(ExecutionContext context, By by, String dateString){
		try{
			
			String date = DateFunctions.GetDateFromString(dateString);
			String dateField = date.substring(0,2);
			if(Integer.parseInt(dateField) < 32){
				dateField = date.substring(0,2);
			}
			else{
				return false;
			}
			String monthField = date.substring(3,6);
			String yearField = date.substring(7, date.length());
			
			WebElement HeadingArea = FindElement(context, By.xpath(".//*[@class='input-group' and @toggle='show']//table[@class='ng-isolate-scope']//th/button[@class='btn btn-default btn-sm btn-block']"));
			WebElement LeftButton = FindElement(context, By.xpath(".//*[@class='input-group' and @toggle='show']//table[@class='ng-isolate-scope']//i[contains(@class,'left')]"));
			WebElement RightButton = FindElement(context, By.xpath(".//*[@class='input-group' and @toggle='show']//table[@class='ng-isolate-scope']//i[contains(@class,'right')]"));
			By Dates= By.xpath(".//*[@class='input-group' and @toggle='show']//table[@class='ng-isolate-scope']//td[contains(@class,'ng-scope')]/button/span[@class='ng-binding']");
			By elements = By.xpath(".//*[@class='input-group' and @toggle='show']//table[@class='ng-isolate-scope']//td[contains(@class,'ng-scope')]/button/span");
			
			WebElement ele = FindElement(context,by);
			if(ele.isDisplayed()){
				ele.click();
				HeadingArea.click();
				HeadingArea.click();
		
		//Selecting Year	
			int count = 0;
		do{	
			List<WebElement> YearNumbers = FindElements(context, elements);
			int yearfield_Story = Integer.parseInt(yearField);
			int minYear = Integer.parseInt(YearNumbers.get(0).getText());
			int maxYear = Integer.parseInt(YearNumbers.get(YearNumbers.size()-1).getText());
			if(yearfield_Story >= minYear && yearfield_Story <= maxYear){
				for(int i=0;i<YearNumbers.size();i++){
					if(YearNumbers.get(i).getText().trim().equalsIgnoreCase(yearField)){
							YearNumbers.get(i).click();
							count = 1;
							break;
					}
				}
			}
			else if(yearfield_Story < minYear){
				LeftButton.click();
				
			}
			else{
				RightButton.click();
			}
		}while(count == 1);
		
		//Selecting Month
		List<WebElement> MonthNames = FindElements(context, elements);
		for(int i=0;i<MonthNames.size();i++){
			if(MonthNames.get(i).getText().trim().toLowerCase().startsWith(monthField.toLowerCase())){
				MonthNames.get(i).click();
				break;
			}
		}
		
		//Selecting Date
		List<WebElement> DateList = FindElements(context, Dates);
		for(int i=0;i<DateList.size();i++){
			if(DateList.get(i).getText().trim().equalsIgnoreCase(dateString)){
				DateList.get(i).click();
				break;
			}
		}
		return true;
	}
			
	}catch(Exception e){
		e.printStackTrace();
	}
	return false;
	}
}
