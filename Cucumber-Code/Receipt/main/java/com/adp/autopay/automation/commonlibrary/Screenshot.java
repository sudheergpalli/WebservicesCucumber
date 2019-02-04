package com.adp.autopay.automation.commonlibrary;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.PropertiesFile;

public class Screenshot
{
	public static String getSnap(ExecutionContext context,String filename) {
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
    	Date date = new Date();
    	File scrFile= null;
    	String runHost=PropertiesFile.RunningHost;
    	if (runHost.equalsIgnoreCase("local")){   		
    			scrFile = ((TakesScreenshot)context.driver).getScreenshotAs(OutputType.FILE);
    	}else{
    		WebDriver augmentedDriver = new Augmenter().augment(context.driver);
    			scrFile = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE); 			
    	}
	    	// Now you can do whatever you need to do with it, for example copy somewhere
	    	try {
	    		
	    		File file = new File("C:\\Screenshot");
	    		if (!file.exists()) {
	    			if (file.mkdir()) {
	    				System.out.println("Directory is created!");
	    			} else {
	    				System.out.println("Failed to create directory!");
	    			}
	    		}
				File outputFile = new File("C:\\Screenshots\\"+ filename +"-"+dateFormat.format(date) +".jpg");				
				FileUtils.copyFile(scrFile, outputFile);
				System.out.println(outputFile.getAbsolutePath());
				return outputFile.getAbsolutePath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    	
    		
    return null;	
	}
}
