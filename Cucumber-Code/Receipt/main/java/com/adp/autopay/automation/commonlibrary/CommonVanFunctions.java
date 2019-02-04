package com.adp.autopay.automation.commonlibrary;

import java.util.List;

import com.adp.autopay.automation.framework.CustomWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;

import com.adp.autopay.automation.utility.ExecutionContext;

public class CommonVanFunctions extends CustomWebElement {
	
	
	
	public static boolean navigateVDL(ExecutionContext context, String strMainMenu, String strSubMenu,String StrMenuItem){		
		try{
			
			Actions builder = new Actions(context.driver); 
			String script = "return navigator.appName;";
			String Temp = (String) ((JavascriptExecutor) context.driver).executeScript(script);
			
			while(!FindElement(context,By.id("mastheadNavBar")).isDisplayed()){
				Thread.sleep(100);
			}
			List<WebElement> menuList = FindElements(context,By.xpath(".//*[@id='mastheadNavBar']/span"));
			for(WebElement element : menuList){				
				if ( element.getText().trim().equalsIgnoreCase(strMainMenu))
				{	
					
//					String sNavItemID = element.getAttribute("id");
					WebElement sNavItem = FindElement(context, By.xpath(".//*[@id='mastheadNavBar']//span[@id='"+strMainMenu+"_navItem']"));
					String sNavItemID = sNavItem.getAttribute("id");
					
					if(Temp.equals("Microsoft Internet Explorer")){
						FindElement(context,By.id(sNavItemID + "_label")).click();				
					}else{
						builder.moveToElement(FindElement(context,By.id(sNavItemID + "_label"))).build().perform();
						Locatable MegamenuItem = (Locatable) FindElement(context,By.id(sNavItemID + "_label"));
						Mouse mouse = ((HasInputDevices) context.driver).getMouse();
						mouse.mouseMove(MegamenuItem.getCoordinates());
					}																		
					
					WebElement menuPopup = FindElement(context,By.xpath(".//div[@dijitpopupparent='" + sNavItemID + "']//div[@aria-labelledby='"+strMainMenu+"']"));					
					List<WebElement> menuItemList = menuPopup.findElements(By.xpath(".//span[@class='tabLabel']"));					
					
					for(int i=0;i<=menuItemList.size()-1;i++)
					{
						if ( menuItemList.get(i).getText().trim().endsWith(strSubMenu.trim())){
							if(Temp.equals("Microsoft Internet Explorer")){
								String strSubdMenuID = menuItemList.get(i).getAttribute("id");
								WebElement ele = FindElement(context,By.id(sNavItemID + "_label"));								  
								builder.moveToElement(ele).perform();
								Thread.sleep(100);
								menuItemList.get(i).click();
								FindElement(context,By.id(strSubdMenuID)).click();
								
							}else{
									if(!menuItemList.get(i).isDisplayed()){
										WebElement Megamenu =  FindElement(context,By.id(sNavItemID + "_label"));
										Locatable MegamenuItem = (Locatable)Megamenu;
										Mouse mouse = ((HasInputDevices) context.driver).getMouse(); 
										mouse.mouseMove(MegamenuItem.getCoordinates());
									}
								builder.moveToElement(menuItemList.get(i)).perform();
								menuItemList.get(i).click();
								WaitingFunctions.WaitUntilPageLoads(context);							
							
						}
					}
					}
					List<WebElement> SubMenuList = menuPopup.findElements(By.xpath(".//div[contains(@class,'dijitVisible')]//div[contains(@id,'"+strMainMenu+"')]"));
					for(int j=0;j<=SubMenuList.size()-1;j++)
					{
						if ( SubMenuList.get(j).getText().trim().endsWith(StrMenuItem.trim())){
							if(Temp.equals("Microsoft Internet Explorer")){
								String strSubMenuID = SubMenuList.get(j).getAttribute("id");
								WebElement ele = FindElement(context,By.id(sNavItemID + "_label"));								  
								builder.moveToElement(ele).perform();
								Thread.sleep(100);
								SubMenuList.get(j).click();
								FindElement(context,By.id(strSubMenuID)).click();
								return true;
							}else{								
								builder.moveToElement(SubMenuList.get(j)).perform();
								SubMenuList.get(j).findElement(By.xpath(".//span[@class='revitMegaItemText']")).click();
								WaitingFunctions.WaitUntilPageLoads(context);							
								return true;
						}
					}
					}
					return false;
				}
			}
		}
		catch (Exception e)		
		{
			System.out.println("Exception:=" + e.getMessage());
			return false;
		}
		return false;
	}
	
	public static boolean navigatetoRev(ExecutionContext context, String strMainMenu, String strSubMenu){
		try{

			Actions builder = new Actions(context.driver);
			String script = "return navigator.appName;";
			String Temp = (String) ((JavascriptExecutor) context.driver).executeScript(script);

			while(!FindElement(context,By.id("mastheadNavBar")).isDisplayed()){
				Thread.sleep(100);
			}
			List<WebElement> menuList = FindElements(context,By.xpath(".//*[@id='mastheadNavBar']/span"));
			for(WebElement element : menuList){
				if (element.getText().trim().equalsIgnoreCase(strMainMenu))
				{
//					String sNavItemID = element.getAttribute("id");
					WebElement sNavItem = FindElement(context, By.xpath(".//*[@id='mastheadNavBar']//span[@id='"+strMainMenu+"_navItem']"));
					String sNavItemID = sNavItem.getAttribute("id");

					if(Temp.equals("Microsoft Internet Explorer") || context.browser.equalsIgnoreCase("ie"))
					{
						System.out.println();
						builder.moveToElement(FindElement(context,By.xpath("//span[contains(.,'Myself') and @id='"+sNavItemID + "_label' and @class[contains(.,'Button')]]"))).build().perform();
						//FindElement(context,By.xpath("//span[contains(.,'Myself') and @id='"+sNavItemID + "_label' and @class[contains(.,'Button')]]")).click();
						
					}else{
						builder.moveToElement(FindElement(context,By.id(sNavItemID + "_label"))).build().perform();
						Locatable MegamenuItem = (Locatable) FindElement(context,By.id(sNavItemID + "_label"));
						Mouse mouse = ((HasInputDevices) context.driver).getMouse();
						mouse.doubleClick(MegamenuItem.getCoordinates());
					}

					WebElement menuPopup = FindElement(context,By.xpath(".//div[@dijitpopupparent='" + sNavItemID + "']"));
					List<WebElement> menuItemList = menuPopup.findElements(By.xpath(".//span[@class[starts-with(.,'dijit dijitReset dijitInline revitMegaItem')] and @id[contains(.,'"+strMainMenu+"')]]"));

					for(int i=0;i<=menuItemList.size()-1;i++)
					{
						if ( menuItemList.get(i).getAttribute("innerHTML").trim().endsWith(strSubMenu.trim())){

							if(Temp.equals("Microsoft Internet Explorer")  || context.browser.equalsIgnoreCase("ie")){
								//String strSubdMenuID = menuItemList.get(i).getAttribute("id");
								WebElement ele = FindElement(context,By.xpath("//span[contains(.,'Myself') and @id='"+sNavItemID + "_label' and @class[contains(.,'Button')]]"));
								builder.moveToElement(ele).moveToElement(menuItemList.get(i)).click().build().perform();
								/*menuItemList.get(i).click();
								FindElement(context,By.id(strSubdMenuID)).click();*/
								return true;
							}else{
								if(!menuItemList.get(i).isDisplayed()){
									WebElement Megamenu =  FindElement(context,By.id(sNavItemID + "_label"));
									Locatable MegamenuItem = (Locatable)Megamenu;
									Mouse mouse = ((HasInputDevices) context.driver).getMouse();
									mouse.doubleClick(MegamenuItem.getCoordinates());
								}
								//WebElement ele = FindElement(context,By.xpath(".//*[@id='People_ttd_Benefits_Enrollments']"));
								//builder.moveToElement(ele).perform();
								JavascriptExecutor executor = (JavascriptExecutor)context.driver;
								executor.executeScript("arguments[0].click();", menuItemList.get(i));
								
								/*Locatable FirstSubmenuItem = (Locatable) menuItemList.get(0);
								Mouse mouse = ((HasInputDevices) context.driver).getMouse();
								mouse.mouseMove(FirstSubmenuItem.getCoordinates());
								builder.moveToElement(menuItemList.get(i)).click().build().perform();*/
								System.out.println("*** Navigation Successful");
								WaitingFunctions.WaitUntilPageLoads(context);
								return true;
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception:=" + e.getMessage());
			return false;
		}
		return false;
	}
	
	}
