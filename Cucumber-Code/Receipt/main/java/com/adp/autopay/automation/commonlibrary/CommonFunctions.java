package com.adp.autopay.automation.commonlibrary;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.SuiteContext;

import cucumber.runtime.java.ObjectContainer;

public class CommonFunctions extends CustomWebElement{
	
	public static void SelectSearchEmployee(ExecutionContext context,String WeblistName,String Value){
		try{
			//WebElement element = FindElement(context,By.xpath(".//label[text()='"+WeblistName+"']/parent::div/div"));
			FindElement(context,By.xpath(".//label[text()='"+WeblistName+"']/parent::div/div/div")).click();
			Thread.sleep(20);
			//new WebDriverWait(context.driver,100).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[contains(@id,'revit_form_FilteringSelect')]")));
			//List<WebElement> listElement = FindElements(context,By.xpath(".//div[contains(@id,'revit_form_FilteringSelect')]"));
			List<WebElement> listElement = FindElements(context,By.xpath(".//div[@class='dijitReset dijitMenu dijitComboBoxMenu']//div"));
			for(WebElement ele:listElement){
				if(ele.getText().trim().equals(Value)){
					ele.click();
					break;
				}		
			}
			
	}catch(Exception e){
		e.printStackTrace();
		}
	}
	 	
	public static Document getXmlDocument(String fileName){
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
		    builder = builderFactory.newDocumentBuilder();
		    Document document = builder.parse(new FileInputStream(fileName));
		    return document;
		} catch (ParserConfigurationException | SAXException | IOException e) {		
		    e.printStackTrace(); 
		    return null;
		}
	}
	
	public static void verifyXMLData(ExecutionContext context, Document xmlDocument,String xpath,String value){
		try{
			String actualValue = getXmlXpathValue(xmlDocument,xpath);
			if(value.equalsIgnoreCase(actualValue)){
				context.addResult("Xml Node Value Validations for Node :"+xpath.substring(xpath.lastIndexOf("/")).replace("/", ""), "Verifying XML Node Value for Xpath :"+xpath, value, actualValue, RunStatus.PASS);
			}else{
				context.addResult("Xml Node Value Validations for Node :"+xpath.substring(xpath.lastIndexOf("/")).replace("/", ""), "Verifying XML Node Value for Xpath :"+xpath, value, actualValue, RunStatus.FAIL,"");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getXmlXpathValue(Document xmlDocument, String expression){
		XPath xPath =  XPathFactory.newInstance().newXPath();
		//read a string value
		try {
			return xPath.compile(expression).evaluate(xmlDocument);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
   public static void SelectRowsPerPage(ExecutionContext context,String Value){
	   try{
			FindElement(context, By.xpath(".//div[@id='widget_tableGrid_rowsPerPageWidget']//i")).click();
			List<WebElement> listElement = FindElements(context,By.xpath(".//div[contains(@id,'tableGrid_rowsPerPageWidget_popup')]"));
			for(WebElement ele:listElement){
				if(ele.getText().trim().equals(Value)){
					ele.click();
					break;
				}
			}
	}catch(Exception e){
		e.printStackTrace();
		}
   }
	
	public static void RevUI_SelectRowsPerPage(ExecutionContext context,String Value){
		   try{
				FindElement(context, By.xpath(".//div[contains(@id,'rowsPerPageWidget')]//div[@class[contains(.,'Arrow')]]")).click();
				List<WebElement> listElement = FindElements(context,By.xpath(".//div[contains(@id,'rowsPerPageWidget_popup')]//div[contains(@id,'popup')]"));
				for(WebElement ele:listElement){
					if(ele.getText().trim().equals(Value)){
						ele.click();
						break;
					}
				}
		}catch(Exception e){
			e.printStackTrace();
			}
	   }
	
	/**
	 * Ram
	 * This method is used to terminate the feature execution
	 */
	public static void endFeatureExecution(){
		   try{
			 ExecutionContext  context = ObjectContainer.getInstance(ExecutionContext.class);
				if (!(SuiteContext.SuiteName.equalsIgnoreCase("CommandLineExecutions"))) 
				{
					context.driver.close();
					context.completeTestExecution();
					context.disconnect();
					ObjectContainer.removeClass(ExecutionContext.class);
				}
		}catch(Exception e){
			}
	   }
	
	/**
	 * Ram
	 * This method is used to switch to new/latest window that is displayed
	 */
	public static void switchtoNewWindow(ExecutionContext context){
		String winHandleParent= context.driver.getWindowHandle();
		try{
			for (String winHandle : context.driver.getWindowHandles()) {
				if(!winHandle.equals(winHandleParent))
				{
					context.driver.switchTo().window(winHandle);
					context.driver.manage().window().maximize();
					System.out.println("New window Opened: "+context.driver.getTitle());
				}
			}	 
		}catch(Exception e){
			}
	   }
	
	/**
	 * Ram
	 * This method is used to switch to previous window that is displayed
	 */
	public static void switchtoDefaultWindow(ExecutionContext context){
		String winHandleParent= context.driver.getWindowHandle();
		try{
			for (String winHandle : context.driver.getWindowHandles()) {
				if(!winHandle.equals(winHandleParent))
				{
					context.driver.close();
					context.driver.switchTo().defaultContent();
					System.out.println("New window Opened: "+context.driver.getTitle());
				}
			}	 
		}catch(Exception e){
			}
	   }
	

}
