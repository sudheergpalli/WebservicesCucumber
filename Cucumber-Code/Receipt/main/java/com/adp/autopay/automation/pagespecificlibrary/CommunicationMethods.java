package com.adp.autopay.automation.pagespecificlibrary;

import java.util.List;

import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.framework.TableFunctions;
import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.Common;
import com.adp.autopay.automation.utility.ExecutionContext;

public class CommunicationMethods extends CustomWebElement
{
	
	public static boolean VerifyComtableheaderDataFont(ExecutionContext context,String RowtoVerify,String FontSize,String FontColor ,String FontWeight){
		try{
			
			WebElement EmployeeTable = FindElement(context,Common.ComEmployeeRowsTable);
			List<WebElement> Headers=FindElements(context, Common.pdcEmployeeHeaders);
	      for(int colunm=0;colunm<Headers.size();colunm++){
	    	  WebElement ele=Headers.get(colunm);
	    	  if(ele.getText().trim().contains(RowtoVerify)){  
					
						WebElement elem= TableFunctions.Cell_Element(EmployeeTable, 2, colunm + 1);
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
}
	
	