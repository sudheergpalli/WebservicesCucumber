package com.adp.autopay.automation.commonlibrary;

import com.adp.autopay.automation.utility.ExecutionContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateFunctions
{
	/**
	 * this method is used to get the specified date format need.
	 * @return
	 */
	public static String Today(){
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar now = Calendar.getInstance();
    String strDate = sdfDate.format(now.getTime());
	return strDate;
	}
	
	
	public static String TodayMMDDYYYY(){
		SimpleDateFormat sdfDate = new SimpleDateFormat("MMddYYYY");
	    Calendar now = Calendar.getInstance();
	    String strDate = sdfDate.format(now.getTime());
		return strDate;
		}
	
	public static String TodayYYYYMMDD(){
		SimpleDateFormat sdfDate = new SimpleDateFormat("YYYYMMdd");
	    Calendar now = Calendar.getInstance();
	    String strDate = sdfDate.format(now.getTime());
		return strDate;
		}
	
	
	public static String TodayMMDDYYYYWithSlashes(){
		SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/YYYY");
	    Calendar now = Calendar.getInstance();
	    String strDate = sdfDate.format(now.getTime());
		return strDate;
		}
	
	public static String Today_WithTime(){
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
	    Calendar now = Calendar.getInstance();
	    String strDate = sdfDate.format(now.getTime());
		return strDate;
		}
	
	public static String Today_WithTimeDash(){
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar now = Calendar.getInstance();
	    String strDate = sdfDate.format(now.getTime());
		return strDate;
		}
	
	public static Date addMinutesToDate(int minutes, Date beforeTime){
	    final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

	    long curTimeInMs = beforeTime.getTime();
	    Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
	    return afterAddingMins;
	}
	
	
	/**
	 * Validating the command is case Insensitive. Please makesure you have given correct constants at the command.
	 * It will return the validating the batch command that is with the data constant.
	 * 
	 */
	public static String ValidateBatchCommand(String Command){
		String[] commandcomponents = Command.split(" ");
		String NewCommand = Command;
		if(commandcomponents.length >0){
			for(String Element:commandcomponents){
				SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
			 	Calendar now = Calendar.getInstance();
			 	String strDate = sdfDate.format(now.getTime());
				
			 	//TODAY
			 	if(Element.matches("<TODAY>")){
			 		NewCommand = NewCommand.replace(Element, strDate);
				}
			
			 	//ADDTODATE
				if(Element.matches("<ADDTODATE,*-?\\d*,*-?\\d*,*-?\\d*>")){
			 		String input = Element.substring(1, (Element.length()-1));
			 		input = input.substring(10);
			 		String[] dateArray = input.split(",");
			 		ArrayList<Integer> dateList = new ArrayList<Integer>(3);
			 	    for(String element:dateArray){
			 	    	dateList.add(Integer.parseInt(element));
			 	    }
			 	    
			 	    if(dateList.get(0) != 0){
			 	        now.add(Calendar.MONTH, dateList.get(0));
			 	    }
			 	    if(dateList.get(1) != 0){
			 	    	now.add(Calendar.DAY_OF_YEAR, dateList.get(1));
			 	    }
			 	    if(dateList.get(2) != 0){
			 	    	now.add(Calendar.YEAR, dateList.get(2));
			 	    }
			 	    
			 	   String strDate_After = sdfDate.format(now.getTime());
			 	  NewCommand = NewCommand.replace(Element, strDate_After);
			 	}
				
				//FIRSTOFYEAR
				if(Element.matches("<FIRSTOFYEAR,*-?\\d*>")){	 
			 		String input = Element.substring(1, (Element.length()-1));
			 		input = input.substring(12);
			 		int number = Integer.parseInt(input);
		 			int year = now.get(Calendar.YEAR);
		 			now.clear();
		 			now.set(Calendar.YEAR, year);
		 			now.set(Calendar.MONTH, 0);
		 			now.set(Calendar.DATE, 1);
			 		if(number != 0){
			 			now.add(Calendar.YEAR, number);
			 		}
			 		String strDate_After = sdfDate.format(now.getTime());
			 		NewCommand = NewCommand.replace(Element,strDate_After);
			 	}
				
				//LASTOFYEAR
				if(Element.matches("<LASTOFYEAR,*-?\\d*>")){	 
			 		String input = Element.substring(1, (Element.length()-1));
			 		input = input.substring(11);
			 		int number = Integer.parseInt(input);
		 			int year = now.get(Calendar.YEAR);
		 			now.clear();
		 			now.set(Calendar.YEAR, year);
		 			now.set(Calendar.MONTH, 11);
		 			now.set(Calendar.DATE, 31);
			 		if(number != 0){
			 			now.add(Calendar.YEAR, number);
			 		}
			 		String strDate_After = sdfDate.format(now.getTime());
			 		NewCommand = NewCommand.replace(Element,strDate_After);
			 	}
			 	
				//FIRSTOFMONTH
				if(Element.matches("<FIRSTOFMONTH,*-?\\d*>")){	 
			 		String input = Element.substring(1, (Element.length()-1));
			 		input = input.substring(13);
			 		int number = Integer.parseInt(input);
		 			now.set(Calendar.DATE, 1);
			 		if(number != 0){
			 			now.add(Calendar.MONTH, number);
			 		}
			 		String strDate_After = sdfDate.format(now.getTime());
			 		NewCommand = NewCommand.replace(Element,strDate_After);
			 	}
				
				//LASTOFMONTH
				if(Element.matches("<LASTOFMONTH,*-?\\d*>")){	 
			 		String input = Element.substring(1, (Element.length()-1));
			 		input = input.substring(12);
			 		int number = Integer.parseInt(input);
		 			now.set(Calendar.DATE, 1);
		 			now.add(Calendar.MONTH, 1);
		 			now.add(Calendar.DATE, -1);
			 		if(number != 0){
			 			now.add(Calendar.MONTH, number);
			 		}
			 		String strDate_After = sdfDate.format(now.getTime());
			 		NewCommand = NewCommand.replace(Element,strDate_After);
			 	}
			}
		return NewCommand;
		}
		return Command;
	}
	
	/**
	 * This is used to change the String to Date 
	 * For Eg: <ADDTODATE.0,0,1> Will gives you Tommorow Date
	 * 
	 * @param dateString
	 * @return
	 */
	public static String GetDateFromString(String dateString){
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
	 	Calendar now = Calendar.getInstance();
	 	String strDate = sdfDate.format(now.getTime());
	 	
		if(dateString.matches("<TODAY>")){
	 		return strDate;
		}
	
	 	//ADDTODATE
		if(dateString.matches("<ADDTODATE,*-?\\d*,*-?\\d*,*-?\\d*>")){
	 		String input = dateString.substring(1,(dateString.length()-1));
	 		input = input.substring(10);
	 		String[] dateArray = input.split(",");
	 		ArrayList<Integer> dateList = new ArrayList<Integer>(3);
	 	    for(String element:dateArray){
	 	    	dateList.add(Integer.parseInt(element));
	 	    }
	 	    if(dateList.get(0) != 0){
	 	        now.add(Calendar.MONTH, dateList.get(0));
	 	    }
	 	    if(dateList.get(1) != 0){
	 	    	now.add(Calendar.DAY_OF_YEAR, dateList.get(1));
	 	    }
	 	    if(dateList.get(2) != 0){
	 	    	now.add(Calendar.YEAR, dateList.get(2));
	 	    }
	 	    
	 	   return sdfDate.format(now.getTime());
	 	}
		
		//FIRSTOFYEAR
		if(dateString.matches("<FIRSTOFYEAR,*-?\\d*>")){	 
	 		String input = dateString.substring(1, (dateString.length()-1));
	 		input = input.substring(12);
	 		int number = Integer.parseInt(input);
 			int year = now.get(Calendar.YEAR);
 			now.clear();
 			now.set(Calendar.YEAR, year);
 			now.set(Calendar.MONTH, 0);
 			now.set(Calendar.DATE, 1);
	 		if(number != 0){
	 			now.add(Calendar.YEAR, number);
	 		}
	 		return sdfDate.format(now.getTime());
	 	}
		
		//LASTOFYEAR
		if(dateString.matches("<LASTOFYEAR,*-?\\d*>")){	 
	 		String input = dateString.substring(1, (dateString.length()-1));
	 		input = input.substring(11);
	 		int number = Integer.parseInt(input);
 			int year = now.get(Calendar.YEAR);
 			now.clear();
 			now.set(Calendar.YEAR, year);
 			now.set(Calendar.MONTH, 11);
 			now.set(Calendar.DATE, 31);
	 		if(number != 0){
	 			now.add(Calendar.YEAR, number);
	 		}
	 		return sdfDate.format(now.getTime());
	 	}
	 	
		//FIRSTOFMONTH
		if(dateString.matches("<FIRSTOFMONTH,*-?\\d*>")){	 
	 		String input = dateString.substring(1, (dateString.length()-1));
	 		input = input.substring(13);
	 		int number = Integer.parseInt(input);
 			now.set(Calendar.DATE, 1);
	 		if(number != 0){
	 			now.add(Calendar.MONTH, number);
	 		}
	 		return sdfDate.format(now.getTime());
	 	}
		
		//LASTOFMONTH
		if(dateString.matches("<LASTOFMONTH,*-?\\d*>")){	 
	 		String input = dateString.substring(1, (dateString.length()-1));
	 		input = input.substring(12);
	 		int number = Integer.parseInt(input);
 			now.set(Calendar.DATE, 1);
 			now.add(Calendar.MONTH, 1);
 			now.add(Calendar.DATE, -1);
	 		if(number != 0){
	 			now.add(Calendar.MONTH, number);
	 		}
	 		return sdfDate.format(now.getTime());
	 	}
		return strDate;
	}

	@SuppressWarnings("deprecation")
	public static boolean FillWebDate(ExecutionContext context, By by, Date date){
		try{
			WebElement ele = context.driver.findElement(by);
			String strID = ele.getAttribute("id");
			context.driver.findElement(By.xpath(".//*[@id='" + strID + "']/div[3]")).click();
			String arrMonths[] = {"January","Febuary", "March", "April",
					"May","June", "July", "August",
					"September","October", "November","December"};

			String sMonth = arrMonths[date.getMonth()];
			int iDay = date.getDay();
			int iYear = date.getYear() + 1900;

			WebElement datePopup = context.driver.findElement(By.xpath(".//*[@id='" + strID.replace("widget_", "") + "_popup" + "']"));

			WebElement selectedMonth = datePopup.findElement(By.xpath(".//div[@class='dijitCalendarMonthLabel dijitInline dijitVisible']"));

			datePopup = context.driver.findElement(By.xpath(".//*[@id='" + strID.replace("widget_", "") + "_popup" + "']"));

			if (!(selectedMonth.getText().equals(sMonth))){
				selectedMonth.click();
				System.out.println("selecting date");
				datePopup.findElement(By.xpath(".//div[@month='" + date.getMonth() + "']")).click();
			}

			datePopup = context.driver.findElement(By.xpath(".//*[@id='" + strID.replace("widget_", "") + "_popup" + "']"));

			WebElement currentYear = datePopup.findElement(By.xpath(".//span[@dojoattachpoint='currentYearLabelNode']"));
			while(Integer.parseInt(currentYear.getText()) != iYear)
			{
				WebElement tempElement;
				if (Integer.parseInt(currentYear.getText()) > iYear){
					tempElement = datePopup.findElement(By.xpath(".//span[@dojoattachpoint='previousYearLabelNode']"));
				}
				else
				{
					tempElement = datePopup.findElement(By.xpath(".//span[@dojoattachpoint='nextYearLabelNode']"));
				}
				tempElement.click();
				datePopup = context.driver.findElement(By.xpath(".//*[@id='" + strID.replace("widget_", "") + "_popup" + "']"));
				currentYear = datePopup.findElement(By.xpath(".//span[@dojoattachpoint='currentYearLabelNode']"));
			}


			List<WebElement> daysList = datePopup.findElements(By.xpath(".//*[contains(@class,'dijitCalendarCurrentMonth dijitCalendarDateTemplate')]"));
			for(WebElement element : daysList){
				if (Integer.parseInt(element.getText()) == iDay){
					element.click();
					return true;
				}
			}
		}
		catch(Exception e){
			System.out.println("Caught an exception while selectin the Drop Down Item " + e.getMessage());
		}
		return false;
	}

}
