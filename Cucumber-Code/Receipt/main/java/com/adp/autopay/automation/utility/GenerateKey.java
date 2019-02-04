package com.adp.autopay.automation.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateKey {
	
	public static String GenerateExecutionId(String strPrefix){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
		return strPrefix + "_" + format.format(new Date());
	}
	
}
