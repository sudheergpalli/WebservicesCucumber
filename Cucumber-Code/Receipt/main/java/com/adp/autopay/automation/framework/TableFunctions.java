package com.adp.autopay.automation.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TableFunctions {
	public static int RowCount(WebElement table){
		return table.findElements(By.xpath(".//TBODY[1]/TR")).size();
	}
	
	public static int ColumnCount(WebElement table, int iRowNum){
		return table.findElements(By.xpath(".//TBODY[1]/TR[" + iRowNum + "]/TD")).size();		
	}
	
	public static String CellData(WebElement table, int iRow, int iCol){
		
		return table.findElement(By.xpath("TBODY[1]/TR[" + iRow + "]/TD[" + iCol + "]")).getText().trim();
	}
	
	public static WebElement Cell_Link(WebElement table,int iRow,int iCol){
		return table.findElement(By.xpath("TBODY[1]/TR[" + iRow + "]/TD[" + iCol + "]/a"));
	}
	
	public static WebElement Cell_Element(WebElement table,int iRow,int iCol){
		return table.findElement(By.xpath("TBODY[1]/TR[" + iRow + "]/TD[" + iCol + "]//span"));
	}
	
	public static int RowCountTwo(WebElement table){
		return table.findElements(By.xpath(".//TBODY[2]/TR")).size();
	}
	
	public static int ColumnCountTwo(WebElement table, int iRowNum){
		return table.findElements(By.xpath(".//TBODY[2]/TR[" + iRowNum + "]/TD")).size();		
	}
	
	public static String CellDataTwo(WebElement table, int iRow, int iCol){
		
		return table.findElement(By.xpath("TBODY[2]/TR[" + iRow + "]/TD[" + iCol + "]")).getText().trim();
	}
	public static WebElement Cell_Elementtwo(WebElement table,int iRow,int iCol){
		return table.findElement(By.xpath("TBODY[2]/TR[" + iRow + "]/TD[" + iCol + "]"));
	}
}
