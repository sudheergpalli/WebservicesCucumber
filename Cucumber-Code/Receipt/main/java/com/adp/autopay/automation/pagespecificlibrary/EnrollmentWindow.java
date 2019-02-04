package com.adp.autopay.automation.pagespecificlibrary;

import java.util.List;

import com.adp.autopay.automation.commonlibrary.Screenshot;
import com.adp.autopay.automation.commonlibrary.WaitingFunctions;
import com.adp.autopay.automation.framework.CustomWebElement;
import com.adp.autopay.automation.framework.TableFunctions;
import com.adp.autopay.automation.framework.WebEdit;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.pagerepository.EnrollmentPage;
import com.adp.autopay.automation.utility.ExecutionContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.adp.autopay.automation.framework.Element;
import com.adp.autopay.automation.pagerepository.EnrollmentWindowPage;


public class EnrollmentWindow extends CustomWebElement {

		public static boolean VerifyEnrollmentTable(ExecutionContext context,String EventReason,String EventDate,String O2EStartDate,String O2EEndDate,String Acknowledged,String Confirmed){
		try{
			
			WebElement EmployeeTable = FindElement(context,EnrollmentWindowPage.EnrollmentwindowRows);
			int rows = TableFunctions.RowCount(EmployeeTable);
			List<WebElement> Headers=FindElements(context, EnrollmentWindowPage.Enrollmentwindowheaders);
			int HeaderSize = Headers.size();
	      for(int colunm=0;colunm<=HeaderSize;colunm++){
	    	
	    	  if(Headers.get(colunm).getText().trim().equalsIgnoreCase("Event Reason")){
					for (int i=1;i<=rows;i++)
					{
						if(TableFunctions.CellData(EmployeeTable, i, colunm+1).equalsIgnoreCase(EventReason))
						{
							if(TableFunctions.CellData(EmployeeTable, i, colunm+2).equalsIgnoreCase(EventDate)){
								if(TableFunctions.CellData(EmployeeTable, i, colunm+3).equalsIgnoreCase(O2EStartDate)){
									if(TableFunctions.CellData(EmployeeTable, i, colunm+4).equalsIgnoreCase(O2EEndDate)){
										if(TableFunctions.CellData(EmployeeTable, i, colunm+5).equalsIgnoreCase(Acknowledged)){
											if(TableFunctions.CellData(EmployeeTable, i, colunm+6).equalsIgnoreCase(Confirmed)){
														  return true;
											}
											
										}
									}
								}				
							}					
						}			
					}    	  
	    	  }
	      }
	      return false;
		}catch(Exception e){
			context.addResult("Verify Enrollment Window Rows", "Verify Enrollment Window rows", "Rows not present", "Exception occured", RunStatus.FAIL, Screenshot.getSnap(context, "Exception"));
			return false;
		}	
	}


		 
		    public static void DatePicker(ExecutionContext context, String date) throws Exception{
		         
		    	String[ ] Months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		        
		 
		     
		    //Split the date time to get only the date part
		   String date_dd_MM_yyyy[] = date.split("/");
		   FindElement(context, By.xpath(".//input[@class='dijitReset dijitInputField dijitDateTimeTriggerIcon']")).click();
		   if(Element.IsPresent(context, By.xpath(".//table[contains(@class,'dijitCalendarContainer dijitCalendar')]"))){
			   context.addResult("Verifiy Calender popup displayed", "Verifiy Calender popup displayed", "Calender popup displayed", "Calender popup displayed", RunStatus.PASS);
		   }else{
			   context.addResult("Verifiy Calender popup displayed", "Verifiy Calender popup displayed", "Calender popup displayed", "Calender popup not displayed", RunStatus.FAIL,Screenshot.getSnap(context, "Calender"));
		   }
			   
		   EnrollmentWindow.SelectYear(context, date_dd_MM_yyyy[2]);
		   EnrollmentWindow.SelectMonth(context, Months[fnDate(date_dd_MM_yyyy[0])]);
    	  //Dropdown.Select(context, By.xpath(".//*[@id='opportunityGrid_row_0_cell_3_date_popup_yddb']"), date_dd_MM_yyyy[2]);
          //Dropdown.Select(context, By.xpath(".//*[@id='opportunityGrid_row_0_cell_3_date_popup_mddb_label']"), Months[fnDate(date_dd_MM_yyyy[0])]);
		  //WebElement dateWidget = FindElement(context, By.xpath(".//*[@id='opportunityGrid_row_0_cell_3_date_popup']"));
          FindElement(context, By.xpath(".//td[contains(@class,'dijitCalendarCurrentMonth')]//span[text()='"+date_dd_MM_yyyy[1]+"']")).click();
          
          
          
          
//		         ///FOR TIME
//		        FindElement(context, By.xpath(".//*[@id='opportunityGrid_row_0_cell_3_time']")).click();
//		        FindElement(context, By.xpath(".//*[@id='opportunityGrid_row_0_cell_3_time']")).click();
//		        Dropdown.Select(context, By.xpath(".//*[@id='widget_opportunityGrid_row_0_cell_3_time_dropdown']"), time);
		    }
		 
		

private static int fnDate(String date){
	String s = null;
	int p = 0;
	if(date!=null && date!="10"){
		s = date.replaceFirst("0","");
		p = Integer.parseInt(s);
	}
	return p;
}


public static void SelectYear(ExecutionContext context, String strValueToSelect){
	try{
	 FindElement(context,By.xpath(".//span[@class='dijitReset dijitArrowButtonChar']//parent::span[contains(@id,'opportunityGrid_row_')]")).click();
	 List<WebElement> listElement=FindElements(context,By.xpath(".//div[@class='dijitCalendarMonthLabel' and @year='"+strValueToSelect+"']"));
	 for(WebElement ele:listElement){
			if(Element.IsPresent(context, ele)){
				ele.click();
				break;
			}
		}
//		List<WebElement> listElement = FindElements(context,By.xpath(".//div[@class='dijitCalendarMonthLabel')]"));
//		for(WebElement ele:listElement){
//			if(ele.getText().trim().equals(strValueToSelect)){
//				ele.click();
//				break;
//			}
//		}
}catch(Exception e){
	e.printStackTrace();
	}
}

public static void SelectMonth(ExecutionContext context, String strValueToSelect){
	try{
	 FindElement(context,By.xpath(".//div[@class='dijitCalendarMonthLabel dijitCalendarCurrentMonthLabel']//parent::span[contains(@id,'mddb')]")).click();
		
		List<WebElement> listElement = FindElements(context,By.xpath(".//div[@class='dijitCalendarMonthLabel']"));
		for(WebElement ele:listElement){
			if(ele.getText().trim().equals(strValueToSelect)){
				ele.click();
				break;
			}
		}
}catch(Exception e){
	e.printStackTrace();
	}
}

public static void TimePicker(ExecutionContext context, String time) {
    try{
           FindElement(context,By.xpath(".//div[contains(@id,'time')]//input[@class='dijitReset dijitInputField dijitDateTimeTriggerIcon']")).click();
           WebEdit.SetText(context, By.xpath(".//div[contains(@class,'dijitInputField')]/input[contains(@id,'time')]"), time);
//         List<WebElement> listElement = FindElements(context,By.xpath(".//div[@class='dijitTimePickerItem dijitTimePickerTick']/div"));
//                for(WebElement ele:listElement){
//                      if(ele.getText().trim().equals(time)){
//                             ele.click();
//                             break;
//                      }
//                }      
    }catch(Exception e){
           
           
    }
    
}

	public static boolean SearchPP(ExecutionContext context,String Field,String Value) {
		try {
			//WebElement field = null;
			switch (Field) 
			{
				case "Last Name":
					/*************
					 * Waiting for a specific label
					 */
					WaitingFunctions.waitForAnElement(context, EnrollmentPage.LastName);
					WebEdit.SetText(context, EnrollmentPage.LastName, Value);
					break;
				case "First Name":
					/*************
					 * Waiting for a specific label
					 */
					WaitingFunctions.waitForAnElement(context, EnrollmentPage.FirstName);
					WebEdit.SetText(context, EnrollmentPage.FirstName, Value);
					break;
				case "Employee ID":
					/*************
					 * Waiting for a specific label
					 */
					WaitingFunctions.waitForAnElement(context, EnrollmentPage.EmployeeId);
					WebEdit.SetText(context, EnrollmentPage.EmployeeId, Value);
					break;
				case "Status":
					/*************
					 * Waiting for a specific label
					 */
					WaitingFunctions.waitForAnElement(context, EnrollmentPage.Status);
					WebEdit.SetText(context, EnrollmentPage.Status, Value);
					break;
				case "File Number":
					/*************
					 * Waiting for a specific label
					 */
					WaitingFunctions.waitForAnElement(context, EnrollmentPage.FileNumber);
					WebEdit.SetText(context, EnrollmentPage.FileNumber, Value);
					break;
				case "Pay Group":
					/*************
					 * Waiting for a specific label
					 */
					WaitingFunctions.waitForAnElement(context, EnrollmentPage.PayGroup);
					WebEdit.SetText(context, EnrollmentPage.PayGroup, Value);
					break;
				case "Job":
					/*************
					 * Waiting for a specific label
					 */
					WaitingFunctions.waitForAnElement(context, EnrollmentPage.Job);
					WebEdit.SetText(context, EnrollmentPage.Job, Value);
					break;
				default:
					System.out.println("*** No Element found with the given input data");
					break;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
