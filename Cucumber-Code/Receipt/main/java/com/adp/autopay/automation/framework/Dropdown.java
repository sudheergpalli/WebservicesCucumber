package com.adp.autopay.automation.framework;

import java.util.List;

import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagespecificlibrary.CommonMethods;
import com.adp.autopay.automation.utility.ExecutionContext;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class Dropdown extends CustomWebElement {

	public static void Select(ExecutionContext context,By by, String valueToSelect){
		WebElement ele = FindElement(context, by);
		if(isPresent(context,by)){
			if(IsEnabled(context,by)){
				Select sel = new Select(ele);
				List<WebElement>list = sel.getOptions();
				for(WebElement element:list){
					if ( element.getText().trim().equalsIgnoreCase(valueToSelect)){
						element.click();
						break;
					}
				}
			}
		}
	}

	public static boolean isPresent(ExecutionContext context,By by){
		return FindElement(context,by).isDisplayed();
	}

	public static boolean IsEnabled(ExecutionContext context,By by){
		return FindElement(context,by).isEnabled();
	}

	public static boolean VerifyWebListValue_Revit(ExecutionContext context,String DropDown, String DropDownValue){
		WebElement element = FindElement(context,(By.id("widget_" + DropDown)));
		element.findElement(By.xpath(".//div[1]")).click();
		List<WebElement> listElement = FindElement(context,By.id("widget_" + DropDown + "_dropdown")).findElements(By.tagName("LI"));
		for(WebElement ele:listElement){
			if(ele.getText().trim().equals(DropDownValue)){
				return true;
			}
		}
		return false;
	}

	public static boolean SelectWebList_Revit(ExecutionContext context,String strObjectID, String strValueToSelect){
		try{
			Actions action = new Actions(context.driver);
			WebElement element = FindElement(context,By.id("widget_" + strObjectID));
			action.doubleClick(element.findElement(By.xpath(".//div[1]"))).build().perform();
			Thread.sleep(2000);
			List<WebElement> listElement = FindElement(context,By.id("widget_"+ strObjectID +"_dropdown")).findElements(By.xpath("//div[@class='dijitReset dijitMenuItem']"));
			for(WebElement ele:listElement){
				if(ele.getText().trim().equals(strValueToSelect)){
					action.moveToElement(ele).click().build().perform();
					Thread.sleep(2000);
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}


	public static boolean SelectWebListByPartialSearch_Revit(ExecutionContext context,String strObjectID, String SearchString,String strValueToSelect){
		try{
			Actions action = new Actions(context.driver);
			WebElement element = FindElement(context,By.id("widget_" + strObjectID));
			FindElement(context, By.id(strObjectID)).clear();
			FindElement(context, By.id(strObjectID)).sendKeys(SearchString);
//			action.doubleClick(element.findElement(By.xpath(".//div[1]"))).build().perform();
			Thread.sleep(2000);
			List<WebElement> listElement = FindElement(context,By.id("widget_"+ strObjectID +"_dropdown")).findElements(By.xpath("//div[@class='dijitReset dijitMenuItem']"));
			for(WebElement ele:listElement){
				if(ele.getText().trim().equals(strValueToSelect)){
					action.moveToElement(ele).click().build().perform();
					Thread.sleep(2000);
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public static boolean VerifyWebListValue_Revit_TypeTwo(ExecutionContext context,String DropDown, String DropDownValue){
		WebElement element = FindElement(context,(By.id("widget_" + DropDown)));
		element.findElement(By.xpath(".//div[1]")).click();
		List<WebElement> listElement = FindElement(context,By.id("widget_" + DropDown + "_dropdown")).findElements(By.xpath("//*[@class='dijitReset dijitMenuItem']"));
		for(WebElement ele:listElement){
			if(ele.getText().trim().equals(DropDownValue)){
				return true;
			}
		}
		return false;
	}


	public static void selectMultipelValues(ExecutionContext context,By by,String multipleVals){

		String multipleSel[] = multipleVals.split(",");
		for (String valueToBeSelected : multipleSel) {
			new Select(context.driver.findElement(by)).selectByVisibleText(valueToBeSelected);
			context.driver.findElement(by).sendKeys(Keys.CONTROL);
		}
	}

	public static void select(ExecutionContext context,String DropdownName,String DropdownValue){
		try{
			FindElement(context, By.xpath(".//label[contains(text(),'"+ CommonMethods.convert(DropdownName)+"')]//parent::div//input[@role='button presentation']")).click();
			FindElement(context, By.xpath(".//div[contains(@id,'revit_form_FilteringSelect_') and contains(text(),'"+DropdownValue+"')]")).click();
		}catch(Exception e){
			e.printStackTrace();
			context.addResult("Select dropdown", "select dropdown", "Dropdown should be selected", "Dropdown not selected", RunStatus.FAIL);
		}

	}
}

