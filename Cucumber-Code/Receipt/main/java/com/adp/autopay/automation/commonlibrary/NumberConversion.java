package com.adp.autopay.automation.commonlibrary;

public class NumberConversion
{
	public static float RoundToOneDecimal(float number){
		
		double RoundedValue = Math.round(number*10.0)/10.0;
		float f = (float)RoundedValue;
		return f;
		
	}
	
	public static float RoundToTwoDecimal(float number){
		
		double RoundedValue = Math.round(number*100.0)/100.0;
		float f = (float)RoundedValue;
		return f;
		
	}
	
	public static double RoundToOneDecimal_double(double number){
		
		double RoundedValue = Math.round(number*10.0)/10.0;
		double f = (double)RoundedValue;
		return f;
		
	}
	
	public static double RoundToTwoDecimal_double(double number){
		
		double RoundedValue = Math.round(number*100.0)/100.0;
		double f = (double)RoundedValue;
		return f;
		
	}
}
