package com.adp.autopay.automation.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.utility.ExecutionContext;


@SuppressWarnings("unused")
public class RevitTableFunctions extends CustomWebElement{
	
	public static int FindRowInMulCol(ExecutionContext context, By by, String arr[], int arrCol[]){
		try
		{
			WebElement element = FindElement(context,by);
			String sTableID = element.getAttribute("id");					
			
			boolean bExit = false;			
			boolean bFirst = true;
			while(!bExit){
				int iRowCount = RowCount(element);
				for(int i=1;i<=iRowCount;i++){
					String strExpected="";
					
					String strActual="";
					for(int k=0;k<arrCol.length;k++){
						strExpected = strExpected + "|" + arr[arrCol[k]-1];
						strActual = strActual + "|" + GetCellData(element, i, arrCol[k]);
					}
					
					if (strExpected.equals(strActual)){
						return i;						
					}					
				}
				if (bFirst){
					if (element.findElement(By.xpath(".//*[@id='"+sTableID + "_first']/a")).isDisplayed())
					{
						element.findElement(By.xpath(".//*[@id='"+sTableID + "_first']/a")).click();
						WaitForTableLoading(element);
						bFirst = false;
					}
				}else{
					if (element.findElement(By.xpath(".//*[@id='"+sTableID + "_next']/a")).isDisplayed()){
						element.findElement(By.xpath(".//*[@id='"+sTableID + "_next']/a")).click();
						WaitForTableLoading(element);					
					}else{
						bExit = true;
					}
				}
			}
		
		}catch(Exception e){
			System.out.println("Caught an exception in Find Row in Multi Column");
		}		
	return 0;		
	}
	
	public static void FillWebEditInTable(WebElement elementTable, int iRow, int iCol, String strData)
	{
		try{
			WebElement element =  elementTable.findElement(By.id(elementTable.getAttribute("id") + "_row_" + (iRow - 1) + "_cell_" + (iCol - 1) + "]"));
			WebElement elem = element.findElement(By.xpath(".//input[@class='dijitReset dijitInputInner']"));
			elem.sendKeys(strData);			
		}catch(Exception e){
			
		}		
	}
	
	public static String GetCellData(WebElement elementTable, int iRow, int iCol){
		try{
//			System.out.println(elementTable.findElement(By.id(elementTable.getAttribute("id") + "_row_" + (iRow-1) + "_cell_" + (iCol-1))).getText().trim());
			return elementTable.findElement(By.id(elementTable.getAttribute("id") + "_row_" + (iRow-1) + "_cell_" + (iCol-1))).getText().trim();
		}catch(Exception e){
			
		}		
		return "ERROR: CELL NOT FOUND";
	}

	public static boolean ClickCellData(WebElement elementTable, int iRow, int iCol){
		try{
//			System.out.println(elementTable.findElement(By.id(elementTable.getAttribute("id") + "_row_" + (iRow-1) + "_cell_" + (iCol-1))).getText().trim());
			elementTable.findElement(By.id(elementTable.getAttribute("id") + "_row_" + (iRow-1) + "_cell_" + (iCol-1))).findElement(By.tagName("a")).click();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static int RowCount(WebElement elementTable){
		try{
			WebElement elem = elementTable.findElement(By.id(elementTable.getAttribute("id") + "_rows_table"));			
			return (elem.findElements(By.xpath(".//tr")).size())-1;
		}catch(Exception e){
			
		}		
		return 0;
	}

	public static int ColumnCount(WebElement elementTable, int iRow){
		try{
			WebElement elem = elementTable.findElement(By.id(elementTable.getAttribute("id") + "_rows_table"));		
//			System.out.println(elem.findElements(By.xpath(".//*[contains(@id," + elementTable.getAttribute("id")+ "_row_" + (iRow-1) + "_cell_)]")).size());
//			int x = elem.findElements(By.xpath(".//*[contains(@id," + elementTable.getAttribute("id")+ "_row_" + (iRow-1) + "_cell_)]")).size()-1;
			int x = elem.findElements(By.xpath(".//tr["+iRow+"]/td")).size()-1;
			return x;
		}catch(Exception e){
			
		}		
		return 0;
	}
	
	public static String GetRowClass(WebElement elementTable, int iRow){
		try{
			
			return elementTable.findElement(By.id(elementTable.getAttribute("id") + "_row_" + (iRow-1))).getAttribute("class");		
			//WebElement elem = elementTable.findElement(By.id(elementTable.getAttribute("id") + "_rows_table"));		
//			System.out.println(elem.findElements(By.xpath(".//*[contains(@id," + elementTable.getAttribute("id")+ "_row_" + (iRow-1) + "_cell_)]")).size());
//			int x = elem.findElements(By.xpath(".//*[contains(@id," + elementTable.getAttribute("id")+ "_row_" + (iRow-1) + "_cell_)]")).size()-1;
			//return elem.findElement(By.xpath(".//tr["+iRow+"]")).getAttribute("class");
			
		}catch(Exception e){
			
		}		
		return "";
	}
	
	public static void VerifyWebEditInTable(WebElement elementTable, int iRow, int iCol, String strData)
	{
		try{
			WebElement element =  elementTable.findElement(By.id(elementTable.getAttribute("id") + "_row_" + (iRow-1) + "_cell_" + (iCol-1) + "]"));
			WebElement elem = element.findElement(By.xpath(".//input[@class='dijitReset dijitInputInner']"));
			VerifyAndReport.VerifyActualExpected(elem.getText(), strData, "Verify Text Field in Table", "Verifying Text Field Value in the Table");			
		}catch(Exception e){
			
		}		
	}
	
	public static void VerifyCheckBoxInTable(WebElement elementTable, int iRow, int iCol, boolean bExpected){
		try{
			WebElement element;
			if (iRow == 0){
				element = elementTable.findElement(By.id(elementTable.getAttribute("id") + "_selectAll_input"));
				VerifyAndReport.VerifyActualExpected(element.getAttribute("checked") != null, bExpected, "Verify RadCheck Box", "Verifying the Check Box value whether it is selected or not");		
				return;
			}
			element =  elementTable.findElement(By.id(elementTable.getAttribute("id") + ".store.rows[" + (iRow-1) + "].cells[" + (iCol-1) + "]_widget"));
			VerifyAndReport.VerifyActualExpected(element.getAttribute("src").endsWith("checkbox-checked.png"), bExpected, "Verify Check Box", "Verifying the Check Box value whether it is selected or not");			
		}catch(Exception e){
			
		}	
	}
	
	public static void VerifyRadioButtonInTable(WebElement elementTable, int iRow, int iCol, boolean bExpected){
		try{
			WebElement element ;			
			element =  elementTable.findElement(By.id(elementTable.getAttribute("id") + ".store.rows[" + (iRow-1) + "].cells[" + (iCol-1) + "]_widget"));
			VerifyAndReport.VerifyActualExpected(element.getAttribute("src").endsWith("radio-checked.png"), bExpected, "Verify Radio Button", "Verifying the Radio Button value whether it is selected or not");			
		}catch(Exception e){
			
		}	
	}
	
	public static void SelectRadioButtonInTable(WebElement elementTable, int iRow, int iCol)
	{
		try{
			WebElement element =  elementTable.findElement(By.id(elementTable.getAttribute("id") + ".store.rows[" + (iRow-1) + "].cells[" + (iCol-1) + "]_widget"));
			if(element.getAttribute("src").endsWith("radio-unchecked.png")){
				element.click();			
			}
		}catch(Exception e){
			
		}		
	}
	
	public static void SelectCheckBoxInTable(WebElement elementTable, int iRow, int iCol, boolean bSelect)
	{
		try{
			WebElement element;
			if (iRow == 0){
				element = elementTable.findElement(By.id(elementTable.getAttribute("id") + "_selectAll_input"));
				if(bSelect){
					if(element.getAttribute("checked") == null){
						element.click();
					}						
				}else{
					if(element.getAttribute("checked") != null){
						element.click();
					}
				}		
				return;
			}
			element =  elementTable.findElement(By.id(elementTable.getAttribute("id") + ".store.rows[" + (iRow-1) + "].cells[" + (iCol-1) + "]_widget"));
			if(bSelect){
				if(element.getAttribute("src").endsWith("checkbox-unchecked.png")){
					element.click();
				}						
			}else{
				if(element.getAttribute("src").endsWith("checkbox-checked.png")){
					element.click();
				}
			}
		}catch(Exception e){
			
		}		
	}
	
	public static int VerifyTableData(ExecutionContext context, By by, String arrExpected[], int arrCol[])
	{
		int iRow = FindRowInMulCol(context, by, arrExpected, arrCol);
		if (iRow <= 0){
			VerifyAndReport.ReportFail("Row Not Found", "Row Not Found to verify the contents of the table");
			return 0;
		}
		WebElement elementTable = FindElement(context,by);
		int iColCount = ColumnCount(elementTable, iRow);
		String[] arrActual = new String[iColCount];
		for(int i=1;i<=iColCount;i++){
			arrActual[i-1] = GetCellData(elementTable, iRow, i);
		}
		VerifyAndReport.VerifyActualExpected(arrActual, arrExpected, "Verify Table Row Data", "Verifying whether the table row data matching with the provided data");
		return 0;
	}

		
//Changes made according to the ACA
//Last column is the image as per this method
	public static void clickImage_Table(ExecutionContext context,By by,String TextToVerify){
		WebElement Table = FindElement(context,by);
		int rows = RevitTableFunctions.RowCount(Table);
		int column = RevitTableFunctions.ColumnCount(Table, 2);
		for (int i=1;i<=rows;i++){
			for(int j=1;j<=column;j++){
				if(RevitTableFunctions.GetCellData(Table, i, j).equalsIgnoreCase(TextToVerify)){
					WebElement ele = Table.findElement(By.xpath(".//*[@id='"+Table.getAttribute("id") + "_row_" + (i-1) + "_cell_" + column+"']//img"));
					if(ele.isDisplayed()){
						ele.click();
						return;
					}
					else{
						ele = Table.findElement(By.xpath(".//*[@id='"+Table.getAttribute("id") + "_row_" + (i) + "_cell_" + column+"']//img"));
						ele.click();
						return;
					}				
				}
			}
		}
	}
	
	
	//Changes made according to the ACA
	//Last column is the span button as per this method
		public static void clickButton_Table(ExecutionContext context,By by,String TextToVerify){
			WebElement Table = FindElement(context,by);
			int rows = RevitTableFunctions.RowCount(Table);
			int column = RevitTableFunctions.ColumnCount(Table, 2);
			for (int i=1;i<=rows;i++){
				for(int j=1;j<=column;j++){
					if(RevitTableFunctions.GetCellData(Table, i, j).equalsIgnoreCase(TextToVerify)){
						WebElement ele = Table.findElement(By.xpath(".//*[@id='"+Table.getAttribute("id") + "_row_" + (i-1) + "_cell_" + column+"']//span"));
						ele.click();
						return;
					}
				}
			}
		}
	
//Changes made according to the ACA	
	public static boolean VerifycellData(ExecutionContext context,By by,String TextToVerify){
		WebElement Table = FindElement(context,by);
		int rows = RevitTableFunctions.RowCount(Table);
		int column = RevitTableFunctions.ColumnCount(Table, 2);
		for (int i=1;i<=rows;i++){
			for (int j=1;j<=column;j++){
				if(RevitTableFunctions.GetCellData(Table, i, j).equalsIgnoreCase(TextToVerify)){
					return true;
				}
			}
		}
		return false;
	}

//Changes made according to the ACA	
	public static void WaitForTableLoading(WebElement elementTable){
		int i=0;
		while(elementTable.getAttribute("innerHTML").contains("gridSpinner") && i<=400){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			i++;			
		}
	}

////The pages like Eligibility , afforability use this type of Table	
//	public static boolean verifyHeader(ExecutionContext context,String HeaderValue){
//		List<WebElement> list = context.driver.findElements(Common.TableHeaders);
//		for(WebElement ele:list){
//			if(ele.getText().trim().startsWith(HeaderValue)){
//				VerifyAndReport.ReportPass("Verify header", "Verify Header has the value: "+HeaderValue);
//				return true;
//			}
//		}
//		return false;
//	}
	
// This method will verify the employee existence in the table
	public static boolean VerifyEmployeeLinkInTable(ExecutionContext context,By by,String EmployeeName){
		
		WebElement EmpTable = FindElement(context,by);
		int rows = TableFunctions.RowCount(EmpTable);
		int column = TableFunctions.ColumnCount(EmpTable, 1);
		for (int i=1;i<=rows;i++){
			for (int j=1;j<=column;j++){
				if(TableFunctions.CellData(EmpTable, i, j).trim().equals(EmployeeName)){
						return true;
				}
			}
		}
		return false;
		
	}
	
}
