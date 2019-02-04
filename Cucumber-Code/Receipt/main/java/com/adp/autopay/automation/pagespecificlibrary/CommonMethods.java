package com.adp.autopay.automation.pagespecificlibrary;

import java.util.ArrayList;
import java.util.List;

import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.framework.TableFunctions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.adp.autopay.automation.commonlibrary.WaitingFunctions;
import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.framework.Element;
import com.adp.autopay.automation.framework.RevitTableFunctions;
import com.adp.autopay.automation.framework.VerifyAndReport;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.Common;
import com.adp.autopay.automation.pagerepository.EnrollmentPage;
import com.adp.autopay.automation.utility.ExecutionContext;
import com.google.common.collect.Ordering;

public class CommonMethods extends CustomWebElement
{
	public static boolean VerifytableRowData(ExecutionContext context,String RowtoVerify,String Data){
		try{

			WebElement EmployeeTable = FindElement(context,Common.EmployeeRowsTable);
			int rows = TableFunctions.RowCount(EmployeeTable);
			List<WebElement> Headers=FindElements(context, Common.EmployeeHeaders);
			for(int colunm=1;colunm<=Headers.size();colunm++){
				WebElement ele=Headers.get(colunm);
				if(ele.getText().trim().equalsIgnoreCase(RowtoVerify)){
					for (int i=2;i<=rows;i++)
					{
						if(!TableFunctions.CellData(EmployeeTable, i, colunm+1).contains(Data.trim()))
						{
							return false;			
						}

					}  
					colunm=Headers.size()+1;
				}
			}
			return true;
		}catch(Exception e){
			context.addResult("Verify Additional Benefits Row", "Verify Additional Benefits row", "Row present", "Exception occured", RunStatus.FAIL);
			return false;
		}	
	}


	public static boolean VerifytableTotalRowData(ExecutionContext context,String RowtoVerify,String Data){
		try{

			WebElement EmployeeTable = FindElement(context,Common.EmployeeRowsTable);
			int rows = TableFunctions.RowCount(EmployeeTable);
			List<WebElement> Headers=FindElements(context, Common.EmployeeHeaders);
			for(int colunm=1;colunm<=Headers.size();colunm++){
				WebElement ele=Headers.get(colunm);
				if(ele.getText().trim().equalsIgnoreCase(RowtoVerify)){  
					for (int i=2;i<=rows;i++)
					{
						if(!TableFunctions.CellData(EmployeeTable, i, colunm+1).equalsIgnoreCase(Data.trim()))
						{
							return false;			
						}

					}  
					colunm=Headers.size()+1;
				}
			} 
			return true;
		}catch(Exception e){
			context.addResult("Verify Additional Benefits Row", "Verify Additional Benefits row", "Row present", "Exception occured", RunStatus.FAIL);
			return false;
		}	
	}


	public static boolean VerifyUnmaskedSNN(ExecutionContext context){
		try{

			WebElement EmployeeTable = FindElement(context,Common.EmployeeRowsTable);
			int rows = TableFunctions.RowCount(EmployeeTable);
			List<WebElement> Headers=FindElements(context, Common.EmployeeHeaders);
			for(int colunm=1;colunm<=Headers.size();colunm++){
				WebElement ele=Headers.get(colunm);
				if(ele.getText().trim().equalsIgnoreCase("SSN")){
					for (int i=2;i<=rows;i++)
					{
						if(TableFunctions.CellData(EmployeeTable,i,colunm+1).equalsIgnoreCase("XXX-XX-XXXX"))
						{
							System.out.println(TableFunctions.CellData(EmployeeTable,i,colunm+1));
							return false;
						}					
					}  
					colunm=Headers.size()+1;
				}

			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			context.addResult("Verify Additional Benefits Row", "Verify Additional Benefits row", "Row present", "Exception occured", RunStatus.FAIL);
			return false;
		}	
	}

	
	public static boolean VerifyUnmaskedSNNdep(ExecutionContext context){
		try{

			WebElement EmployeeTable = FindElement(context,Common.dependentRowTable);
			int rows = TableFunctions.RowCount(EmployeeTable);
			List<WebElement> Headers=FindElements(context, Common.dependentHeaders);
			for(int colunm=1;colunm<=Headers.size();colunm++){
				WebElement ele=Headers.get(colunm);
				if(ele.getText().trim().equalsIgnoreCase("SSN")){
					for (int i=2;i<=rows;i++)
					{
						if(TableFunctions.CellData(EmployeeTable,i,colunm+1).equalsIgnoreCase("XXX-XX-XXXX"))
						{
							System.out.println(TableFunctions.CellData(EmployeeTable,i,colunm+1));
							return false;
						}					
					}  
					colunm=Headers.size()+1;
				}

			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			context.addResult("Verify Additional Benefits Row", "Verify Additional Benefits row", "Row present", "Exception occured", RunStatus.FAIL);
			return false;
		}	
	}

	public static boolean searchandSelect(ExecutionContext context,String ppId){
		try{
			if(context.browser.equalsIgnoreCase("IE") || context.browser.contains("Internet Explorer"))
			{
				new Actions(context.driver).moveToElement(context.driver.findElement(Common.IESearchIcon)).perform();
				FindElement(context,Common.IESearchIcon).click();;
			}
			else
			{
				WaitingFunctions.waitForAnElement(context, Common.SearchIcon);
				FindElement(context, Common.SearchIcon).click();	
			}
			WaitingFunctions.WaitUntilLoadcleared(context,Common.RevitLoading);
			WaitingFunctions.WaitUntilPageLoads(context);
		}catch(Exception e){}
		try{
			if (EnrollmentWindow.SearchPP(context, "Employee ID", ppId))
				{
				WebElement ele=Element.FindElement(context, EnrollmentPage.Search);
				JavascriptExecutor executor = (JavascriptExecutor)context.driver;
				executor.executeScript("arguments[0].click();",ele );
				WaitingFunctions.WaitUntilLoadcleared(context,Common.TableSpinner);
				}
		}catch(Exception e){}
		try{
			WebElement ElementTable = FindElement(context, Common.EnrollmentsTable);
			int rows = RevitTableFunctions.RowCount(ElementTable);
			int columns = RevitTableFunctions.ColumnCount(ElementTable, 2);
			int count = 0;
			for (int i = 1; i <= rows; i++) {
				for (int j = 1; j <= columns; j++) {
					if (RevitTableFunctions.GetCellData(ElementTable, i, j).equalsIgnoreCase(ppId)) {
						if (RevitTableFunctions.ClickCellData(ElementTable, i, 2)) {
							Thread.sleep(3000);
							FindElement(context, By.xpath(".//span[text()='Elections By Event']")).click();
						}
					}
				}
			}
		}catch(Exception e){}
		
		return false;
	}
	
	public static boolean CheckPPWithEmployeeID(ExecutionContext context,String EmployeeID){
		try{
			if(FindElement(context, By.xpath(".//span[text()='"+EmployeeID+"']/ancestor::tr[contains(@id,'Grid')]//div[@class='revitCheckBoxIcon']")).isDisplayed()){
				FindElement(context, By.xpath(".//span[text()='"+EmployeeID+"']/ancestor::tr[contains(@id,'Grid')]//div[@class='revitCheckBoxIcon']")).click();
				return true;
			}
			else{
				System.out.println("*** PPID not found in the employee Table");
				return false;

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public static boolean RevUI_CheckPPWithEmployeeID(ExecutionContext context,String EmployeeID){
		try{
			
			if(context.browser.equalsIgnoreCase("IE") || context.browser.contains("Internet Explorer"))
			{
				new Actions(context.driver).moveToElement(context.driver.findElement(By.xpath("//span[text()='"+EmployeeID+"']/ancestor::tr[contains(@id,'tableGrid')]//td[@id[contains(.,'row')]]//img//following::img[@src[contains(.,'checkbox-unchecked')]]"))).perform();
				if(FindElement(context, By.xpath("//span[text()='"+EmployeeID+"']/ancestor::tr[contains(@id,'tableGrid')]//td[@id[contains(.,'row')]]//img//following::img[@src[contains(.,'checkbox-unchecked')]]")).isDisplayed())
				{
					
					FindElement(context, By.xpath("//span[text()='"+EmployeeID+"']/ancestor::tr[contains(@id,'tableGrid')]//td[@id[contains(.,'row')]]//img//following::img[@src[contains(.,'checkbox-unchecked')]]")).click();
					
					return true;
				}
				else{
					System.out.println("*** PPID not found in the employee Table");
					return false;

				}
			}
			else
			{
				if(FindElement(context, By.xpath(".//span[text()='"+EmployeeID+"']/ancestor::tr[contains(@id,'tableGrid')]//img[@src[contains(.,'checkbox')]]")).isDisplayed()){
					FindElement(context, By.xpath(".//span[text()='"+EmployeeID+"']/ancestor::tr[contains(@id,'tableGrid')]//img[@src[contains(.,'checkbox')]]")).click();
					return true;
				}
				else
				{
					System.out.println("*** PPID not found in the employee Table");
					return false;

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}


	public static boolean CheckMessageWithText(ExecutionContext context,String Text){
		try{
			if(FindElement(context, By.xpath(".//span[text()='"+Text+"']/ancestor::tr[contains(@id,'currentMessageCenterGrid')]//div[@class='revitCheckBox dijitCheckBox']")).isDisplayed()){
				FindElement(context, By.xpath(".//span[text()='"+Text+"']/ancestor::tr[contains(@id,'currentMessageCenterGrid')]//div[@class='revitCheckBox dijitCheckBox']")).click();
				return true;
			}
			else{
				System.out.println("*** Message not found in the message center Table");
				return false;

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}


	public static boolean CheckMessageInCompletedTabWithText(ExecutionContext context,String Text){
		try{
			if(FindElement(context, By.xpath(".//span[text()='"+Text+"']/ancestor::tr[contains(@id,'completedMessageCenterGrid')]//div[@class='revitCheckBox dijitCheckBox']")).isDisplayed()){
				FindElement(context, By.xpath(".//span[text()='"+Text+"']/ancestor::tr[contains(@id,'completedMessageCenterGrid')]//div[@class='revitCheckBox dijitCheckBox']")).click();
				return true;
			}
			else{
				System.out.println("*** Message not found in the message center Table");
				return false;

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public static boolean EmployeeSortedList(ExecutionContext context,By by,int column){
		WebElement EmpTable = FindElement(context,by);
		int rows = TableFunctions.RowCount(EmpTable);
		ArrayList<String> employeeList = new ArrayList<String>();
		for (int i=1;i<=rows;i++){
			employeeList.add(TableFunctions.CellData(EmpTable, i, column));
		}
		boolean sorted = Ordering.natural().reverse().isOrdered(employeeList);
		if(sorted){
			VerifyAndReport.ReportPass("employee list sort order", "verify employee list is in sort oder");
			return true;
		}
		else{
			VerifyAndReport.ReportFail("employee list sort order", "verify employee list is in sort oder");
			return false;
		}
	}


	public static List<String> GetTableTotalRowData(ExecutionContext context,String RowtoVerify){
		try{
			List<String> RowData=new ArrayList<String>();
			WebElement EmployeeTable = FindElement(context,Common.EmployeeRowsTable);
			int rows = TableFunctions.RowCount(EmployeeTable);
			List<WebElement> Headers=FindElements(context, Common.EmployeeHeaders);
			for(int colunm=1;colunm<=Headers.size();colunm++)
			{
				WebElement ele=Headers.get(colunm);
				if(ele.getText().trim().equalsIgnoreCase(RowtoVerify)){  
					for (int i=2;i<=rows;i++)
					{
						RowData.add(TableFunctions.CellData(EmployeeTable, i, colunm+1));

					}  
					colunm=Headers.size()+1;	    	  }
			} 
			return RowData;
		}catch(Exception e)
		{	
			e.printStackTrace();
			context.addResult("Verify Search Grid Table data", "Verify Search Grid Table data", "Row present", "Exception occured", RunStatus.FAIL);
			return null;

		}
	}

	public static List<String> GetTableTotalRowDataUserActivitylogs(ExecutionContext context,String RowtoVerify){
		try{
			List<String> RowData=new ArrayList<String>();
			WebElement EmployeeTable = FindElement(context,Common.EmployeeRowsTable);
			int rows = TableFunctions.RowCount(EmployeeTable);
			List<WebElement> Headers=FindElements(context, Common.EmployeeHeaders);

			for(int colunm=0;colunm<Headers.size();colunm++)
			{
				WebElement ele=Headers.get(colunm);

				if(ele.getText().trim().equalsIgnoreCase(RowtoVerify)){  
					for (int i=2;i<=rows;i++)
					{
						RowData.add(TableFunctions.CellData(EmployeeTable, i, colunm+1));

					}  
					colunm=Headers.size()+1;	    	  }
			} 
			return RowData;
		}catch(Exception e){
			e.printStackTrace();
			context.addResult("Verify Additional Benefits Row", "Verify Additional Benefits row", "Row present", "Exception occured", RunStatus.FAIL);
			return null;
		}	
	}


	public static boolean VerifytableDataFont(ExecutionContext context,String RowtoVerify,String FontSize,String FontColor ,String FontWeight){
		try{

			WebElement EmployeeTable = FindElement(context,Common.EmployeeRowsTable);
			List<WebElement> Headers=FindElements(context, Common.EmployeeHeaders);
			for(int colunm=0;colunm<Headers.size();colunm++){
				WebElement ele=Headers.get(colunm);
				if(ele.getText().trim().contains(RowtoVerify)){  

					WebElement elem=TableFunctions.Cell_Element(EmployeeTable, 2, colunm+1);
					String ActFontSize=elem.getCssValue("font-size");
					String Colour=elem.getCssValue("color").toString();
					String ActFontColour=org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
					String ActFontWeight=elem.getCssValue("font-weight");
					if(ActFontSize.equalsIgnoreCase(FontSize) && ActFontColour.equalsIgnoreCase(FontColor) && ActFontWeight.equalsIgnoreCase(FontWeight) ){
						context.addResult("Verify fontsize,fontcolour,fontweight of  "+RowtoVerify, "Verify fontsize fontcolour fontweight of  "+RowtoVerify, "Font size :"+FontSize+",Font Colour: "+FontColor+",Font Weight:"+FontWeight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.PASS);
						return true;
					}else{
						context.addResult("Verify fontsize,fontcolour,fontweight of  "+RowtoVerify, "Verify fontsize fontcolour fontweight of  "+RowtoVerify, "Font size :"+FontSize+",Font Colour: "+FontColor+",Font Weight:"+FontWeight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, "Labelfailure"));
						return false;
					}			

				}
			} 
			return true;
		}catch(Exception e){
			context.addResult("Verify Additional Benefits Row", "Verify Additional Benefits row", "Row present", "Exception occured", RunStatus.FAIL);
			return false;
		}	
	}
	
	
	public static boolean VerifydeptableDataFont(ExecutionContext context,String RowtoVerify,String FontSize,String FontColor ,String FontWeight){
		try{

			WebElement EmployeeTable = FindElement(context,Common.dependentRowTable);
			List<WebElement> Headers=FindElements(context, Common.dependentHeaders);
			for(int colunm=0;colunm<Headers.size();colunm++){
				WebElement ele=Headers.get(colunm);
				if(ele.getText().trim().contains(RowtoVerify)){  

					WebElement elem=TableFunctions.Cell_Element(EmployeeTable, 2, colunm+1);
					String ActFontSize=elem.getCssValue("font-size");
					String Colour=elem.getCssValue("color").toString();
					String ActFontColour=org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
					String ActFontWeight=elem.getCssValue("font-weight");
					if(ActFontSize.equalsIgnoreCase(FontSize) && ActFontColour.equalsIgnoreCase(FontColor) && ActFontWeight.equalsIgnoreCase(FontWeight) ){
						context.addResult("Verify fontsize,fontcolour,fontweight of  "+RowtoVerify, "Verify fontsize fontcolour fontweight of  "+RowtoVerify, "Font size :"+FontSize+",Font Colour: "+FontColor+",Font Weight:"+FontWeight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.PASS);
						return true;
					}else{
						context.addResult("Verify fontsize,fontcolour,fontweight of  "+RowtoVerify, "Verify fontsize fontcolour fontweight of  "+RowtoVerify, "Font size :"+FontSize+",Font Colour: "+FontColor+",Font Weight:"+FontWeight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.FAIL,Screenshot.getSnap(context, "Labelfailure"));
						return false;
					}			

				}
			} 
			return true;
		}catch(Exception e){
			context.addResult("Verify Additional Benefits Row", "Verify Additional Benefits row", "Row present", "Exception occured", RunStatus.FAIL);
			return false;
		}	
	}




	public static boolean VerifyPDCtableheaderDataFont(ExecutionContext context,String RowtoVerify,String FontSize,String FontColor ,String FontWeight){
		try{

			WebElement EmployeeTable = FindElement(context,Common.pdcEmployeeRowsTable);
			List<WebElement> Headers=FindElements(context, Common.pdcEmployeeHeaders);
			for(int colunm=0;colunm<Headers.size();colunm++){
				WebElement ele=Headers.get(colunm);
				if(ele.getText().trim().contains(RowtoVerify)){  

					WebElement elem=TableFunctions.Cell_Element(EmployeeTable, 2 , colunm+1);
					String ActFontSize=elem.getCssValue("font-size");
					String Colour=elem.getCssValue("color").toString();
					String ActFontColour=org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
					String ActFontWeight=elem.getCssValue("font-weight");
					if(ActFontSize.equalsIgnoreCase(FontSize) && ActFontColour.equalsIgnoreCase(FontColor) && ActFontWeight.equalsIgnoreCase(FontWeight) ){
						context.addResult("Verify fontsize,fontcolour,fontweight of  "+RowtoVerify, "Verify fontsize fontcolour fontweight of  "+RowtoVerify, "Font size :"+FontSize+",Font Colour: "+FontColor+",Font Weight:"+FontWeight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.PASS);
						return true;
					}else{
						context.addResult("Verify fontsize,fontcolour,fontweight of  "+RowtoVerify, "Verify fontsize fontcolour fontweight of  "+RowtoVerify, "Font size :"+FontSize+",Font Colour: "+FontColor+",Font Weight:"+FontWeight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.FAIL,Screenshot.getSnap(context, "Labelfailure"));
						return false;
					}			

				}
			} 
			return true;
		}catch(Exception e){
			context.addResult("Verify Additional Benefits Row", "Verify Additional Benefits row", "Row present", "Exception occured", RunStatus.FAIL);
			return false;
		}	
	}

	public static boolean verifyElementFontsandColours(ExecutionContext context,WebElement elem,String FontSize,String FontColor ,String FontWeight){
		try{
			String ActFontSize=elem.getCssValue("font-size");
			String Colour=elem.getCssValue("color").toString();
			String ActFontColour=org.openqa.selenium.support.Color.fromString(Colour).asHex().toUpperCase();
			String ActFontWeight=elem.getCssValue("font-weight");
			if(ActFontSize.equalsIgnoreCase(FontSize) && ActFontColour.equalsIgnoreCase(FontColor) && ActFontWeight.equalsIgnoreCase(FontWeight) ){
				context.addResult("Verify fontsize,fontcolour,fontweight of Element ", "Verify fontsize fontcolour fontweight of Element ", "Font size :"+FontSize+",Font Colour: "+FontColor+",Font Weight:"+FontWeight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.PASS);
				return true;
			}else{
				context.addResult("Verify fontsize,fontcolour,fontweight of  Element", "Verify fontsize fontcolour fontweight of Element ", "Font size :"+FontSize+",Font Colour: "+FontColor+",Font Weight:"+FontWeight, "Font size :"+ActFontSize+",Font Colour: "+ActFontColour+",Font Weight:"+ActFontWeight, RunStatus.FAIL,Screenshot.getSnap(context, "Labelfailure"));
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
			return false; 
		}
	}

	public static String convert(String Text){
		try{
			String s=Text.toLowerCase();
			int l=s.length();
			char c=Character.toUpperCase(s.charAt(0));
			s=c+s.substring(1);
			for(int i=1; i<l; i++)
			{
				if(Text.charAt(i)==' ')
				{
					c=Character.toUpperCase(s.charAt(i+1));
					s=s.substring(0, i+1) + c + s.substring(i+2);
				}
			}
			return s;
		}catch(Exception e){
			return Text;
		}
	}
		
		public static boolean selectvantageClient(ExecutionContext context, String client){
			try{		
				By clientSelection=By.xpath("//a/span[@title='"+client.toLowerCase()+"' and text()='"+client.toLowerCase()+"']");
				FindElement(context, Common.clientSearchBy).sendKeys(client.toLowerCase());
				WaitingFunctions.waitForAnElement(context, clientSelection);
				
				Actions actions = new Actions(context.driver);
				actions.moveToElement(FindElement(context,clientSelection)).click().build().perform();
				
				WaitingFunctions.waitForAnElement(context, Common.pratClien);
				
				actions.moveToElement(FindElement(context,clientSelection)).moveToElement(FindElement(context,Common.pratClien)).click().build().perform();
				/*WebElement ele1=FindElement(context, clientSelection);
				JavascriptExecutor js=(JavascriptExecutor) context.driver;
				js.executeScript("arguments[0].click();", ele1);
				WebElement ele=FindElement(context, Common.pratClien);
				js.executeScript("arguments[0].click();", ele);
				*/WaitingFunctions.waitForAnElement(context,Common.CompanyEvents);
				Thread.sleep(3000);
				return true;
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Client selection failed");
				System.out.println();
				return false;
				
			}
	}
		
		/**
		 * Ram
		 * This method is used to switch to previous window that is displayed
		 */
		public static void switchtoFont(ExecutionContext context,WebElement ele,String fontSize,String fontWeight,String fontColor){
			String actFontSize=null,colour=null,actFontColour=null,actFontWeight=null,label=null;
			System.out.println("Ele Text: "+ele.getText());
			try{
				actFontSize = ele.getCssValue("font-size");
				colour = ele.getCssValue("color").toString();
				actFontColour = org.openqa.selenium.support.Color.fromString(colour).asHex().toUpperCase();
				actFontWeight = ele.getCssValue("font-weight");
				
				if (actFontSize.equalsIgnoreCase(fontSize) && actFontColour.equalsIgnoreCase(fontColor) && actFontWeight.equalsIgnoreCase(fontWeight)) {
					context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Font size: " + actFontSize + " , Font Colour: " + actFontColour + ", Font Weight: " + actFontWeight, RunStatus.PASS);

				} else {
					context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Font size: " + actFontSize + " , Font Colour: " + actFontColour + ", Font Weight: " + actFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, "Labelfailure"));
				}
			}catch(Exception e){
				context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Font size: " + actFontSize + " , Font Colour: " + actFontColour + ", Font Weight: " + actFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, "Labelfailure"));
			}
		}
		
		/**
		 * Ram
		 * This method is used to verify the field fontsize, fontWeight, fontColor of the label by providing the xPath of the element
		 */
		public static void verifyFieldPrperties(ExecutionContext context,String label,By eleBy,String fontSize,String fontWeight,String fontColor){
			String actFontSize=null,colour=null,actFontColour=null,actFontWeight=null;
			WebElement ele=null;
			
			try{
				ele=FindElement(context, eleBy);
				if(ele!=null)
				{
					System.out.println("Element Text: "+ele.getText());
					actFontSize = ele.getCssValue("font-size");
					colour = ele.getCssValue("color").toString();
					actFontColour = org.openqa.selenium.support.Color.fromString(colour).asHex().toUpperCase();
					actFontWeight = ele.getCssValue("font-weight");
					
					if (actFontSize.equalsIgnoreCase(fontSize) && actFontColour.equalsIgnoreCase(fontColor) && actFontWeight.equalsIgnoreCase(fontWeight)) 
						context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Font size: " + actFontSize + " , Font Colour: " + actFontColour + ", Font Weight: " + actFontWeight, RunStatus.PASS);
						else
						context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Font size: " + actFontSize + " , Font Colour: " + actFontColour + ", Font Weight: " + actFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, label));
				}
				else
					context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Field not found: "+label, RunStatus.FAIL, Screenshot.getSnap(context, label));
			}catch(Exception e){
				context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Exception Occured while verifying field: "+label, RunStatus.FAIL, Screenshot.getSnap(context, label));
			}
		}
		
		/**
		 * Ram
		 * This method is used to verify the field fontsize, fontWeight, fontColor of the label by providing the xPath of the element
		 */
		public static void verifyFieldPrperties(ExecutionContext context,String label,WebElement ele,String fontSize,String fontWeight,String fontColor){
			String actFontSize=null,colour=null,actFontColour=null,actFontWeight=null;			
			try{
				if(ele!=null)
				{
					System.out.println("Element Text: "+ele.getText());
					actFontSize = ele.getCssValue("font-size");
					colour = ele.getCssValue("color").toString();
					actFontColour = org.openqa.selenium.support.Color.fromString(colour).asHex().toUpperCase();
					actFontWeight = ele.getCssValue("font-weight");
					
					if (actFontSize.equalsIgnoreCase(fontSize) && actFontColour.equalsIgnoreCase(fontColor) && actFontWeight.equalsIgnoreCase(fontWeight)) 
						context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Font size: " + actFontSize + " , Font Colour: " + actFontColour + ", Font Weight: " + actFontWeight, RunStatus.PASS);
						else
						context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Font size: " + actFontSize + " , Font Colour: " + actFontColour + ", Font Weight: " + actFontWeight, RunStatus.FAIL, Screenshot.getSnap(context, label.replace(":","")));
				}
				else
					context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Field not found: "+label, RunStatus.FAIL, Screenshot.getSnap(context, label));
			}catch(Exception e){
				context.addResult("Verify fontsize,fontcolour,fontweight of  " + label, "Verify fontsize, fontcolour, fontweight of  " + label, "Font size: " + fontSize + ",Font Colour: " + fontColor + ",Font Weight: " + fontWeight, "Exception Occured while verifying field: "+label, RunStatus.FAIL, Screenshot.getSnap(context, label.replace(":","")));
			}
		}

}
