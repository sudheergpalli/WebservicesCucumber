package com.adp.autopay.automation.commonlibrary;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.adp.autopay.automation.framework.CustomWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.adp.autopay.automation.utility.ExecutionContext;



/**
 * Wait tool class.  Provides Wait methods for an elements, and AJAX elements to load.  
 * It uses WebDriverWait (explicit wait) for waiting an element or javaScript.  
 * 
 * To use implicitlyWait() and WebDriverWait() in the same test, 
 * we would have to nullify implicitlyWait() before calling WebDriverWait(), 
 * and reset after it.  This class takes care of it. 
 * 
 * 
 * Generally relying on implicitlyWait slows things down 
 * so use WaitTool�s explicit wait methods as much as possible.
 * Also, consider (DEFAULT_WAIT_4_PAGE = 0) for not using implicitlyWait 
 * for a certain test.
 * 
 * @author Chon Chung, Mark Collin, Andre, Tarun Kumar 
 * 
 * @todo check FluentWait -- http://seleniumsimplified.com/2012/08/22/fluentwait-with-webelement/
 *
 * Copyright [2012] [Chon Chung]
 * 
 * Licensed under the Apache Open Source License, Version 2.0  
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 */
public class WaitingFunctions extends CustomWebElement {

	/** Default wait time for an element. 7  seconds. */ 
	public static final int DEFAULT_WAIT_4_ELEMENT = 7; 
	/** Default wait time for a page to be displayed.  12 seconds.  
	 * The average webpage load time is 6 seconds in 2012. 
	 * Based on your tests, please set this value. 
	 * "0" will nullify implicitlyWait and speed up a test. */ 
	public static final int DEFAULT_WAIT_4_PAGE = 15; 




	/**
	 * Wait for the element to be present in the DOM, and displayed on the page. 
	 * And returns the first WebElement using the given method.
	 * 
	 * @param WebDriver	The driver object to be used 
	 * @param By	selector to find the element
	 * @param int	The time in seconds to wait until returning a failure
	 *
	 * @return WebElement	the first WebElement using the given method, or null (if the timeout is reached)
	 */
	public static WebElement waitForElement(ExecutionContext context, final By by, int timeOutInSeconds) {
		WebElement element; 
		try{	
			//To use WebDriverWait(), we would have to nullify implicitlyWait(). 
			//Because implicitlyWait time also set "driver.findElement()" wait time.  
			//info from: https://groups.google.com/forum/?fromgroups=#!topic/selenium-users/6VO_7IXylgY
			context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 

			WebDriverWait wait = new WebDriverWait(context.driver, timeOutInSeconds); 
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

			context.driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return element; //return the element	
		} catch (Exception e) {
		} 
		return null; 
	}





	/**
	 * Wait for the element to be present in the DOM, regardless of being displayed or not.
	 * And returns the first WebElement using the given method.
	 *
	 * @param WebDriver	The driver object to be used 
	 * @param By	selector to find the element
	 * @param int	The time in seconds to wait until returning a failure
	 * 
	 * @return WebElement	the first WebElement using the given method, or null (if the timeout is reached)
	 */
	public static WebElement waitForElementPresent(ExecutionContext context, final By by, int timeOutInSeconds) {
		WebElement element; 
		try{
			context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 

			WebDriverWait wait = new WebDriverWait(context.driver, timeOutInSeconds); 
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

			context.driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return element; //return the element
		} catch (Exception e) {
		} 
		return null; 
	}


	/**
	 * Wait for the List<WebElement> to be present in the DOM, regardless of being displayed or not.
	 * Returns all elements within the current page DOM. 
	 * 
	 * @param WebDriver	The driver object to be used 
	 * @param By	selector to find the element
	 * @param int	The time in seconds to wait until returning a failure
	 *
	 * @return List<WebElement> all elements within the current page DOM, or null (if the timeout is reached)
	 */
	public static List<WebElement> waitForListElementsPresent(ExecutionContext context, final By by, int timeOutInSeconds) {
		List<WebElement> elements; 
		try{	
			context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 

			WebDriverWait wait = new WebDriverWait(context.driver, timeOutInSeconds); 
			wait.until((new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driverObject) {
					return areElementsPresent(driverObject, by);
				}
			}));

			elements = context.driver.findElements(by); 
			context.driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return elements; //return the element	
		} catch (Exception e) {
		} 
		return null; 
	}


	/**
	 * Wait for an element to appear on the refreshed web-page.
	 * And returns the first WebElement using the given method.
	 *
	 * This method is to deal with dynamic pages.
	 * 
	 * Some sites I have tested have required a page refresh to add additional elements to the DOM.  
	 * Generally you wouldn't need to do this in a typical AJAX scenario.
	 * 
	 * @param WebDriver	The driver object to use to perform this element search
	 * @param locator	selector to find the element
	 * @param int	The time in seconds to wait until returning a failure
	 * 
	 * @return WebElement	the first WebElement using the given method, or null(if the timeout is reached)
	 * 
	 * @author Mark Collin 
	 */
	public static WebElement waitForElementRefresh(ExecutionContext context, final By by,int timeOutInSeconds){
		WebElement element; 
		try{	
			context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
			new WebDriverWait(context.driver, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {


				public Boolean apply(WebDriver driverObject) {
					driverObject.navigate().refresh(); //refresh the page ****************
					return isElementPresentAndDisplay(driverObject, by);
				}
			});
			element = FindElement(context,by);
			context.driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return element; //return the element
		} catch (Exception e) {

		} 
		return null; 
	}

	/**
	 * Wait for the Text to be present in the given element, regardless of being displayed or not.
	 *
	 * @param WebDriver	The driver object to be used to wait and find the element
	 * @param locator	selector of the given element, which should contain the text
	 * @param String	The text we are looking
	 * @param int	The time in seconds to wait until returning a failure
	 * 
	 * @return boolean
	 */
	public static boolean waitForTextPresent(ExecutionContext context, final By by, final String text, int timeOutInSeconds) {
		boolean isPresent = false; 
		try{	
			//context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
			new WebDriverWait(context.driver, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {


				public Boolean apply(WebDriver driverObject) {
					return isTextPresent(driverObject, by, text); //is the Text in the DOM
				}
			});
			isPresent = isTextPresent(context.driver, by, text);
			context.driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return isPresent; 
		} catch (Exception e) {
		} 
		return false; 
	}




	/** 
	 * Waits for the Condition of JavaScript.  
	 *
	 *
	 * @param WebDriver		The driver object to be used to wait and find the element
	 * @param String	The javaScript condition we are waiting. e.g. "return (xmlhttp.readyState >= 2 && xmlhttp.status == 200)" 
	 * @param int	The time in seconds to wait until returning a failure
	 * 
	 * @return boolean true or false(condition fail, or if the timeout is reached)
	 **/
	public static boolean waitForJavaScriptCondition(ExecutionContext context, final String javaScript, 
			int timeOutInSeconds) {
		boolean jscondition = false; 
		try{	
			context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
			new WebDriverWait(context.driver, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {


				public Boolean apply(WebDriver driverObject) {
					return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
				}
			});
			jscondition =  (Boolean) ((JavascriptExecutor) context.driver).executeScript(javaScript); 
			context.driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return jscondition; 
		} catch (Exception e) {
		} 
		return false; 
	}


	/** Waits for the completion of Ajax jQuery processing by checking "return jQuery.active == 0" condition.  
	 *
	 * @param WebDriver - The driver object to be used to wait and find the element
	 * @param int - The time in seconds to wait until returning a failure
	 * 
	 * @return boolean true or false(condition fail, or if the timeout is reached)
	 * */
	public static boolean waitForJQueryProcessing(ExecutionContext context, int timeOutInSeconds){
		boolean jQcondition = false; 
		try{	
			context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
			new WebDriverWait(context.driver, timeOutInSeconds) {
			}.until(new ExpectedCondition<Boolean>() {


				public Boolean apply(WebDriver driverObject) {
					return (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0");
				}
			});
			jQcondition = (Boolean) ((JavascriptExecutor) context.driver).executeScript("return jQuery.active == 0");
			context.driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return jQcondition; 
		} catch (Exception e) {
		} 
		return jQcondition; 
	}


	/**
	 * Coming to implicit wait, If you have set it once then you would have to explicitly set it to zero to nullify it -
	 */
	public static void nullifyImplicitWait(ExecutionContext context) {
		context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
	} 


	/**
	 * Set driver implicitlyWait() time. 
	 */
	public static void setImplicitWait(ExecutionContext context, int waitTime_InSeconds) {
		context.driver.manage().timeouts().implicitlyWait(waitTime_InSeconds, TimeUnit.SECONDS);  
	} 

	/**
	 * Reset ImplicitWait.  
	 * To reset ImplicitWait time you would have to explicitly 
	 * set it to zero to nullify it before setting it with a new time value. 
	 */
	public static void resetImplicitWait(ExecutionContext context) {
		context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
		context.driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
	} 


	/**
	 * Reset ImplicitWait.  
	 * @param int - a new wait time in seconds
	 */
	public static void resetImplicitWait(ExecutionContext context, int newWaittime_InSeconds) {
		context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
		context.driver.manage().timeouts().implicitlyWait(newWaittime_InSeconds, TimeUnit.SECONDS); //reset implicitlyWait
	} 


	/**
	 * Checks if the text is present in the element. 
	 * 
	 * @param driver - The driver object to use to perform this element search
	 * @param by - selector to find the element that should contain text
	 * @param text - The Text element you are looking for
	 * @return true or false
	 */


	private static boolean isTextPresent(WebDriver driver, By by, String text)
	{
		try {
			return driver.findElement(by).getText().contains(text);
		} catch (NullPointerException e) {
			return false;
		}
	}


	/**
	 * Checks if the elment is in the DOM, regardless of being displayed or not.
	 * 
	 * @param driver - The driver object to use to perform this element search
	 * @param by - selector to find the element
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private static boolean isElementPresent(ExecutionContext context, By by) {
		try {
			FindElement(context,by);//if it does not find the element throw NoSuchElementException, which calls "catch(Exception)" and returns false; 
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}


	/**
	 * Checks if the List<WebElement> are in the DOM, regardless of being displayed or not.
	 * 
	 * @param driver - The driver object to use to perform this element search
	 * @param by - selector to find the element
	 * @return boolean
	 */
	private static boolean areElementsPresent(WebDriver driver, By by) {
		try {
			driver.findElements(by); 
			return true; 
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Checks if the element is in the DOM and displayed. 
	 * 
	 * @param driver - The driver object to use to perform this element search
	 * @param by - selector to find the element
	 * @return boolean
	 */
	private static boolean isElementPresentAndDisplay(WebDriver driver, By by) {
		try {			
			return driver.findElement(by).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}


	//		public static void WaitUntilPageLoads(ExecutionContext context){
	//			int j=0;
	//			context.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	//			while(j<=400){
	//				try{
	//					if(!FindElement(context,)By.xpath(".//div[@class='revitLoadingContentPane']")).isDisplayed())
	//						if(!FindElement(context,)By.xpath(".//div[@class='dijitTitlePaneContentOuter dojoxPortletContentOuter']")).getText().trim().equals(""))
	//							return;
	//					j++;
	//					Thread.sleep(100);
	//				}catch (Exception e)
	//					{
	//						j++;
	//						try {						
	//							Thread.sleep(100);
	//						} catch (InterruptedException e1) {
	//							e1.printStackTrace();
	//						}
	//					}
	//			}
	//			context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	//		}

	//		public static void WaitUntilHomePageLoading(ExecutionContext context){
	//			int j=0;
	//			context.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	//			try{
	//				
	//			}
	//		}

	//		public static void WaitUntilACAHomePageLoading(ExecutionContext context){
	//			WaitingFunctions.nullifyImplicitWait(context);
	//			WebElement element  = WaitingFunctions.waitForElementPresent(context, Home_UserHome.HomePage, 60);
	//			if(element.isDisplayed()){
	//				return;
	//			}else{
	//				try
	//				{
	//					Thread.sleep(30000);
	//				}
	//				catch (InterruptedException e)
	//				{
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
	//			}
	//		}
	//		
	//		
	public static void WaitUntilPageLoads(ExecutionContext context){
		context.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try
		{
			for(int load=0;load<100;load++)
			{
				if(FindElements(context, By.xpath(".//*[@id='loading-indicator']")).size()>0)
				{	
					try{
						Thread.sleep(1000);
					}catch(Exception e){}

				}
				else
				{
					break;
				}
			}
		}
		catch (Exception e1)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		context.driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	public static void WaitUntilLoadcleared(ExecutionContext context,By by){
		WaitingFunctions.nullifyImplicitWait(context);
		try
		{
			for(int load=0;load<100;load++)
			{
				if(FindElements(context,by).size()>0)
				{	
					if(FindElement(context, by).isDisplayed())
					{
						try{
							Thread.sleep(1000);
						}
						catch(Exception e){}
					}
					else
					{
						break;
					}

				}
				else
				{
					break;
				}
			}
		}
		catch (Exception e1)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		WaitingFunctions.setImplicitWait(context,60);
	}


	/*
	 *  This method will wait for the specified element in the page for 30 seconds if it is not found in the page
	 *  If it founds on the page , it will break the loop and go for the next step.
	 */

	public static boolean waitForAnElement(ExecutionContext context,By by,int secs){
		try{
			WebElement element = (new WebDriverWait(context.driver, secs)).until(ExpectedConditions.visibilityOfElementLocated(by));
			if(element != null){
				return true;
			}
			else{
				System.err.println("Your Waiting Function failed to Fetch the element");
				return false;
			}
		}catch(Exception e){}
		return false;
	}

	/*
	 *  This method will wait for the specified element in the page for 30 seconds if it is not found in the page
	 *  If it founds on the page , it will break the loop and go for the next step.
	 */

	public static boolean waitForAnElement(ExecutionContext context,By by){
		try{
			WebElement element = (new WebDriverWait(context.driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(by));
			if(element != null){
				return true;
			}
			else{
				System.err.println("Your Waiting Function failed to Fetch the element");
				return false;
			}
		}catch(Exception e){}
		return false;
	}

	/*
	 * This method will wait until the text to be presented in the page
	 * if it founds , then return true
	 */
	public static boolean waitForTextToPresence(ExecutionContext context,By by,String Text){
		try{
			@SuppressWarnings("deprecation")
			Boolean element = (new WebDriverWait(context.driver, 20)).until(ExpectedConditions.textToBePresentInElement(by, Text));
			if(element){
				return true;
			}
		}catch(Exception e){}
		return false;
	}

	/*
	 * This method is for Table loading 
	 * If the spinning wheel is presented in the table  , It will repeat the loop , if not
	 */
	public static void WaitForTableLoading(WebElement elementTable){
		int i=0;

		while(elementTable.getAttribute("innerHTML").contains("gridSpinner") && i<=400){			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			i++;			
		}
	}

	public static void waitUntilHRCoreTablePresent(ExecutionContext context){
		int i=0;
		WebElement elementTable = (context.driver.findElement(By.id("peopleSearchGrid_table")));
		while(elementTable.getAttribute("innerHTML").contains("gridSpinner") && i<=400){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			i++;
		}
	}

	public static void waitUntilBenefitsTablePresent(ExecutionContext context){
		int i=0;
		WebElement elementTable = (context.driver.findElement(By.id("tableGrid_table")));
		while(elementTable.getAttribute("innerHTML").contains("gridSpinner") && i<=400){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			i++;
		}
	}

	public static void waitforPageLoad(ExecutionContext context){
		WebElement myWait =null;
		try{
			WebDriverWait wait = new WebDriverWait(context.driver, 90);
			wait.pollingEvery(2, TimeUnit.SECONDS);
			wait.ignoring(NoSuchElementException.class);
			wait.ignoring(StaleElementReferenceException.class);
			wait.until(new ExpectedCondition<Boolean>(){
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
				}
			});
		}
		catch (Exception e) {}

	}
}
