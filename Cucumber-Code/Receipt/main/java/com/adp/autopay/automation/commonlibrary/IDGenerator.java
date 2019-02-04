package com.adp.autopay.automation.commonlibrary;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IDGenerator
{
	public static String generateID(){
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd"); // Just the year, with 2 digits
		String formattedDate = sdf.format(Calendar.getInstance().getTime());
		StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Report_Auto_");
        stringBuilder.append((int)(Math.random()*100));
        stringBuilder.append(formattedDate);
        stringBuilder.append((int)(Math.random()*100)) ;
        return stringBuilder.toString();
	}
	
		
}
