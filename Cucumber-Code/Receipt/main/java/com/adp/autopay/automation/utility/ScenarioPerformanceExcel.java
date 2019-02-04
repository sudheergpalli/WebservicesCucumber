package com.adp.autopay.automation.utility;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class ScenarioPerformanceExcel
{	
	
	 public static void InsertToExcel(String TestName, String ScenarioName , long ExecutionTime , int ScenarioNumber){
		 try{   
			Date now = Calendar.getInstance().getTime();
		    //DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		    DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
		    String theDate = df.format(now);
		    
		    String stestName= TestName;
		    
		    String Directory = "C:/Temp/";
		    File saveDir = new File(Directory);
		    if(!saveDir.exists())
		    	  saveDir.mkdirs();
		    
		    if(stestName.contains("BVT")){
		    File file = new File(Directory+"_PerformanceSheet_"+ stestName +".xls");
		    if(!file.exists()){
				    WritableWorkbook workbook1 = Workbook.createWorkbook(new File(Directory+"_PerformanceSheet_"+ stestName+".xls"));
				    workbook1.createSheet(theDate, 0);
				    // now access it and do some operations
				    WritableSheet xcelSheet = workbook1.getSheet(theDate);
				    
				    Label label = new Label(0 , 0 , "TestName");
				    xcelSheet.addCell(label);
				    Label label1 = new Label(1, 0 , "Scenario Name");
				    xcelSheet.addCell(label1);
				    Label label2 = new Label(2, 0 , "Execution Time");
				    xcelSheet.addCell(label2); 
				    
		// Adding Scenario Name and Execution Time		    
				    
				    long execActualTime = (ExecutionTime - 90);
				    xcelSheet.addCell(new Label(1, ScenarioNumber+1, ScenarioName));
				    xcelSheet.addCell(new Label(2,ScenarioNumber+1,Long.toString(execActualTime)));
				    workbook1.write();
				    workbook1.close();
		    }
		    else{
		    	Workbook workbook1 = Workbook.getWorkbook(file);
			    WritableWorkbook AppendBook = Workbook.createWorkbook(file, workbook1);
			    WritableSheet AppendTextToSheet = AppendBook.getSheet(0);
			    
			    long execActualTime = 0;
			    if(ExecutionTime == 0 || ExecutionTime < 10){
			    	execActualTime = ExecutionTime;
			    }else if(ExecutionTime > 10 || ExecutionTime < 20){
			    	execActualTime = ExecutionTime - 5;
			    }
			    else{	
			    	execActualTime = (ExecutionTime - 10);
			    }
			    AppendTextToSheet.addCell(new Label(1, ScenarioNumber+1, ScenarioName));
			    AppendTextToSheet.addCell(new Label(2,ScenarioNumber+1,Long.toString(execActualTime)));
			    AppendBook.write();
			    AppendBook.close();
			    
		    	}
		    }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
}
