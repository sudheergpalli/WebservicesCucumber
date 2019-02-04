package com.adp.autopay.automation.framework;

import org.testng.Assert;

public class VerifyAndReport {	
	
	public static void VerifyActualExpected(String strActual, String strExpected, String strTestCase, String strDescription){
		try{
			Assert.assertEquals(strActual, strExpected, strTestCase);
			ReportRunResult(true, strTestCase , strDescription);
		}
		catch(AssertionError e)
		{
			ReportRunResult(false, strTestCase , strDescription);
		}	
	}
	
	public static void VerifyActualExpected(int iActual, int iExpected, String strTestCase, String strDescription){
		try{
			Assert.assertEquals(iActual, iExpected, strTestCase);
			ReportRunResult(true, strTestCase , strDescription);
		}
		catch(AssertionError e)
		{
			ReportRunResult(false, strTestCase , strDescription);
		}			
	}
	
	public static void VerifyActualExpected(boolean bActual, boolean bExpected, String strTestCase, String strDescription){
		try{
			Assert.assertEquals(bActual, bExpected, strTestCase);
			ReportRunResult(true, strTestCase , strDescription);
		}
		catch(AssertionError e)
		{
			ReportRunResult(false, strTestCase , strDescription);
		}		
	}
	
	public static void VerifyActualExpected(String arrActual[], String arrExpected[], String strTestCase, String strDescription){
		try{
			Assert.assertEquals(arrActual, arrExpected, strTestCase);
			ReportRunResult(true, strTestCase , strDescription);
		}
		catch(AssertionError e)
		{
			ReportRunResult(false, strTestCase , strDescription);
		}				
	}
	
	private static void ReportRunResult(boolean passRfail, String strTestCase, String strDescription){
		@SuppressWarnings("unused")
		String strPassFailStatus;
		if (passRfail)
		{
			System.out.println("Result Passed");
			strPassFailStatus = "PASSED";
			
		}
		else
		{
			strPassFailStatus = "FAILED";
			System.err.println("Result Failed");			
		}
		try
		{
//			if(CrossBrowserTestCasesMainClass.getExecutionId() > 0)
//				OracleOperations.InsertExecutionResultRecord(CrossBrowserTestCasesMainClass.getExecutionId(), strTestCase, strPassFailStatus, strDescription);
		}catch(Exception e){}
	}
	
	public static void ReportPass(String strTestCase, String strDescription){
		Assert.assertTrue( true, strTestCase);
		ReportRunResult(true, strTestCase , strDescription);		
	}
	
	public static void ReportFail(String strTestCase, String strDescription){		
		try{
			Assert.assertTrue(false, strTestCase);		
			ReportRunResult(false, strTestCase , strDescription);
		}
		catch(AssertionError e)
		{
			ReportRunResult(false, strTestCase , strDescription);
		}					
	}
}
