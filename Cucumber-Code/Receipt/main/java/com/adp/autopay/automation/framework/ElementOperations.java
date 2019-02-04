package com.adp.autopay.automation.framework;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.adp.autopay.automation.commonlibrary.DatabaseFunctions;
import com.adp.autopay.automation.commonlibrary.NumberConversion;
import com.adp.autopay.automation.utility.ExecutionContext;

public class ElementOperations extends CustomWebElement
{
	
	/* ***************************************************************************************************************************************************************************************
	
	VERIFICATIONS BETWEEN STORY VALUE AND UI VALUES
	
	*************************************************************************************************************************************************************************************** */
	
	/**
	 * Common method for verification of non-numeric(String) UI element with the story String. 
	 * requirements : Elements from UI should Give the text.
	 * 
	 * @param context
	 * @param Identifier_UI(String)
	 * @param Verifier_UI(String)
	 * @param Identifier_Story(String)
	 * @param Verifier_Story(String)
	 * @return true/false
	 */
	public static boolean CompareCharacters_Story_UI(ExecutionContext context,By Identifier_UI,By Verifier_UI,String Identifier_Story,String Verifier_Story){
	try{	
		WebElement identification = FindElement(context, Identifier_UI);
		WebElement verification = FindElement(context, Verifier_UI);
		if(identification.getText().trim().equalsIgnoreCase(Identifier_Story)){
			if(verification.getText().trim().equalsIgnoreCase(Verifier_Story)){
				return true;
			}
		}	
	}catch(Exception e){
		e.printStackTrace();
	}
	return false;
	}
	
	
	/**
	 * Common method for verification of Numeric UI element with the story Numeric.
	 * requirements : Elements from UI should Give the text
	 * Note : I have used double for the numeric operations as per widening type casting.(byte->short->int->long->float->double)
	 * 
	 * @param context
	 * @param Identifier_UI(String)
	 * @param Verifier_UI(Numeric) - Incase of Float , specify two digits precision.
	 * @param Identifier_Story(String)
	 * @param Verifier_Story(Numeric)
	 * @return true/false
	 */
	public static boolean CompareNumerics_Story_UI(ExecutionContext context,By Identifier_UI,By Verifier_UI,String Identifier_Story,String Verifier_Story){
	try{	
		WebElement identification = FindElement(context, Identifier_UI);
		WebElement verification = FindElement(context, Verifier_UI);
		if(identification.getText().trim().equalsIgnoreCase(Identifier_Story)){
			double Value_Story = NumberConversion.RoundToTwoDecimal_double(Double.parseDouble(Verifier_Story));
			double Value_UI = NumberConversion.RoundToTwoDecimal_double(Double.parseDouble(verification.getText().trim().replace("%", "")));
			if(Value_Story == Value_UI){
					return true;
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
		return false;
	}
	
	/**
	 * common method for verifying string in the List of elements (Non-Numeric).
	 * Requiement: List of Elements from UI should Give the text.
	 * 
	 * @param context
	 * @param Identifiers_UI(String)
	 * @param Verifiers_UI(String)
	 * @param Identifier_Story(String)
	 * @param Verifier_Story(String)
	 * @return
	 */
	public static boolean CompareCharactersInList_Story_UI(ExecutionContext context,By Identifiers_UI,By Verifiers_UI,String Identifier_Story,String Verifier_Story){
	try{	
		List<WebElement> identificationList = FindElements(context, Identifiers_UI);
		List<WebElement> verificationList = FindElements(context, Verifiers_UI);
		if(identificationList.size() == verificationList.size()){
			for(int iterator=0;iterator<identificationList.size();iterator++){
				if(identificationList.get(iterator).getText().trim().equalsIgnoreCase(Identifier_Story)){
					if(verificationList.get(iterator).getText().trim().equalsIgnoreCase(Verifier_Story)){
						return true;
					}
				}
			}
		}
		else{
			System.err.println("Sizes of Identifiers is not equals to Verifiers from the UI, Please check once");
			return false;
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return false;
	}
	
	/**
	 * Common method for Verifying the story value from the list of Web elements(Numeric)
	 * 
	 * @param context
	 * @param Identifiers_UI
	 * @param Verifiers_UI
	 * @param Identifier_Story
	 * @param Verifier_Story
	 * @return
	 */
	public static boolean CompareNumericsInList_Story_UI(ExecutionContext context,By Identifiers_UI,By Verifiers_UI,String Identifier_Story,String Verifier_Story){
		try{	
			List<WebElement> identificationList = FindElements(context, Identifiers_UI);
			List<WebElement> verificationList = FindElements(context, Verifiers_UI);
			if(identificationList.size() == verificationList.size()){
				for(int iterator=0;iterator<identificationList.size();iterator++){
					if(identificationList.get(iterator).getText().trim().equalsIgnoreCase(Identifier_Story)){
						double Value_Story = NumberConversion.RoundToTwoDecimal_double(Double.parseDouble(Verifier_Story));
						double Value_UI = NumberConversion.RoundToTwoDecimal_double(Double.parseDouble(verificationList.get(iterator).getText().trim().replace("%", "")));
						if(Value_Story == Value_UI){
							return true;
						}
					}
				}
			}
			else{
				System.err.println("Sizes of Identifiers is not equals to Verifiers from the UI, Please check once");
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	
	
/*	*********************************************************************************************************************************************************************************************

	VERIFICATIONS BETWEEN THE UI AND DATABASE (Queries Are Supplied from the story)

	********************************************************************************************************************************************************************************************* */
	
	/**
	 * Common method for verification of non-numeric(String) UI element with the Database String. 
	 * requirements : Elements from UI should Give the text.
	 * 
	 * @param context
	 * @param Identifier_UI(String)
	 * @param Verifier_UI(String)
	 * @param Identifier_Story(String)
	 * @param Verifier_Query(String)
	 * @return true/false
	 */
	public static boolean CompareCharacters_Database_UI(ExecutionContext context,By Identifier_UI,By Verifier_UI,String Identifier_Story,String Verifier_Query,String Client){
	try{	
		WebElement identification = FindElement(context, Identifier_UI);
		WebElement verification = FindElement(context, Verifier_UI);
		if(identification.getText().trim().equalsIgnoreCase(Identifier_Story)){
			DatabaseFunctions.getConnection(context,Client);
			// just plan for validating the query before running.
			String Value_DB = DatabaseFunctions.getResult(Verifier_Query,Client);
			DatabaseFunctions.closeConnection();
			if(verification.getText().trim().equalsIgnoreCase(Value_DB)){
				return true;
			}
		}	
	}catch(Exception e){
		e.printStackTrace();
	}
	return false;
	}
	
	
	/**
	 * Common method for verification of Numeric UI element with the story Numeric.
	 * requirements : Elements from UI should Give the text
	 * Note : I have used double for the numeric operations as per widening type casting.(byte->short->int->long->float->double)
	 * 
	 * @param context
	 * @param Identifier_UI(String)
	 * @param Verifier_UI(Numeric) - Incase of Float , specify two digits precision.
	 * @param Identifier_Story(String)
	 * @param Verifier_Query(Numeric)
	 * @return true/false
	 */
	public static boolean CompareNumerics_Database_UI(ExecutionContext context,By Identifier_UI,By Verifier_UI,String Identifier_Story,String Verifier_Query,String Client){
	try{	
		WebElement identification = FindElement(context, Identifier_UI);
		WebElement verification = FindElement(context, Verifier_UI);
		if(identification.getText().trim().equalsIgnoreCase(Identifier_Story)){
			DatabaseFunctions.getConnection(context,Client);
			//Validating the query before the running.
			double Value_Database = NumberConversion.RoundToTwoDecimal_double(Double.parseDouble(DatabaseFunctions.getResult(Verifier_Query,Client)));
			double Value_UI = NumberConversion.RoundToTwoDecimal_double(Double.parseDouble(verification.getText().trim().replace("%", "")));
			if(Value_Database == Value_UI){
					return true;
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
		return false;
	}
	
	/**
	 * common method for verifying string in the List of elements (Non-Numeric).
	 * Requiement: List of Elements from UI should Give the text.
	 * 
	 * @param context
	 * @param Identifiers_UI(String)
	 * @param Verifiers_UI(String)
	 * @param Identifier_Story(String)
	 * @param Verifier_Query(String)
	 * @return
	 */
	public static boolean CompareCharactersInList_Database_UI(ExecutionContext context,By Identifiers_UI,By Verifiers_UI,String Identifier_Story,String Verifier_Query,String Client){
	try{	
		List<WebElement> identificationList = FindElements(context, Identifiers_UI);
		List<WebElement> verificationList = FindElements(context, Verifiers_UI);
		if(identificationList.size() == verificationList.size()){
			for(int iterator=0;iterator<identificationList.size();iterator++){
				if(identificationList.get(iterator).getText().trim().equalsIgnoreCase(Identifier_Story)){
					DatabaseFunctions.getConnection(context,Client);
					//Validate the query . 
					String Value_Database = DatabaseFunctions.getResult(Verifier_Query,Client); 
					if(verificationList.get(iterator).getText().trim().equalsIgnoreCase(Value_Database)){
						return true;
					}
				}
			}
		}
		else{
			System.err.println("Sizes of Identifiers is not equals to Verifiers from the UI, Please check once");
			return false;
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return false;
	}
	
	/**
	 * Common method for Verifying the story value from the list of Web elements(Numeric)
	 * 
	 * @param context
	 * @param Identifiers_UI(String)
	 * @param Verifiers_UI(Numeric)
	 * @param Identifier_Story(String)
	 * @param Verifier_Query(String)
	 * @return
	 */
	public static boolean CompareNumericsInList_Database_UI(ExecutionContext context,By Identifiers_UI,By Verifiers_UI,String Identifier_Story,String Verifier_Query,String Client){
		try{	
			List<WebElement> identificationList = FindElements(context, Identifiers_UI);
			List<WebElement> verificationList = FindElements(context, Verifiers_UI);
			if(identificationList.size() == verificationList.size()){
				for(int iterator=0;iterator<identificationList.size();iterator++){
					if(identificationList.get(iterator).getText().trim().equalsIgnoreCase(Identifier_Story)){
						DatabaseFunctions.getConnection(context,Client);
						//Validating the database query
						String Value = DatabaseFunctions.getResult(Verifier_Query,Client);
						double Value_Story = NumberConversion.RoundToTwoDecimal_double(Double.parseDouble(Value));
						double Value_UI = NumberConversion.RoundToTwoDecimal_double(Double.parseDouble(verificationList.get(iterator).getText().trim().replace("%", "")));
						if(Value_Story == Value_UI){
							return true;
						}
					}
				}
			}
			else{
				System.err.println("Sizes of Identifiers is not equals to Verifiers from the UI, Please check once");
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/*Ram*/
	/**
	 * Common method to get the UI Value from the list of Web elements. UI Header/Field Names list size should be equal to UI Values List
	 * 
	 * @param List<WebElement> Fields : List of WebElements-UI Header/Field Names list
	 * @param List<WebElement> Values : List of WebElements-UI Values corresponding to UI Header/Field Names list
	 * @param FieldName : UI Header/Field Names to be verified
	 * @return Value : Returns a string value corresponding to the Header/Field Names
	 */
	public static String GetFieldValue_WebElements(List<WebElement> Fields,List<WebElement> Values, String FieldName)
	{
		try
		{
			for(int i=0;i<Fields.size();i++)
			{
				if(Fields.get(i).getText().trim().equalsIgnoreCase(FieldName.trim()))
				{
					 return Values.get(i).getText();
				}
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/*Ram*/
	/**
	 * Common method to get the UI Value from the xPath for list of Web elements. UI Header/Field Names Elements list size should be equal to UI Values Elements List
	 * 
	 * @param By FieldsPath : xPath for List of WebElements-UI Header/Field Names Elements List
	 * @param By ValuesPath : xPath for List of WebElements-UI Values corresponding to UI Header/Field Names Elements List
	 * @param FieldName : UI Header/Field Names to be verified
	 * @return Value : Returns a string value corresponding to the Header/Field Names
	 */
	
	public static String GetFieldValue_xPath(ExecutionContext context,By FieldsPath,By ValuesPath, String FieldName)
	{
		try
		{
			List<WebElement> FieldsList,FieldValues;
			FieldsList= FindElements(context, FieldsPath);
			FieldValues= FindElements(context, ValuesPath);
			for(int i=0;i<FieldsList.size();i++)
			{
				if(FieldsList.get(i).getText().trim().equalsIgnoreCase(FieldName.trim()))
				{
					 return FieldValues.get(i).getText();
				}
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	/*Ram*/
	/**
	 * Common method to find the UI Header/Field Name from the list of Web elements
	 * 
	 * @param By FieldsPath : List of WebElements-UI Header/Field Names Elements List
	 * @param FieldName : UI Header/Field Names to find
	 * @return boolean : Returns true if the UI Header/Field found in the WebElements list
	 */
	
	public static boolean FindFieldName_WebElements(ExecutionContext context,List<WebElement> Fields,String FieldName)
	{
		try
		{
			for(int i=0;i<Fields.size();i++)
			{
				if(Fields.get(i).getText().trim().equalsIgnoreCase(FieldName.trim()))
				{
					 return true;
				}
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/*Ram*/
	/**
	 * Common method to find the UI Header/Field Name from the xPath for list of Web elements
	 * 
	 * @param By FieldsPath : xPath for List of WebElements-UI Header/Field Names Elements List
	 * @param FieldName : UI Header/Field Names to find
	 * @return boolean : Returns true if the UI Header/Field found in the WebElements list
	 */
	
	public static boolean FindFieldName_xPath(ExecutionContext context,By FieldsPath,String FieldName)
	{
		try
		{
			List<WebElement> FieldsList;
			FieldsList= FindElements(context, FieldsPath);
			for(int i=0;i<FieldsList.size();i++)
			{
				if(FieldsList.get(i).getText().trim().equalsIgnoreCase(FieldName.trim()))
				{
					 return true;
				}
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/*Ram*/
	/**
	 * Common method to find the UI Header/Field Name from the list of Strings.
	 * 
	 * @param List<String> FieldsList : List of Strings-UI Header/Field Names list
	 * @param FieldName : UI Header/Field Name to find
	 * @return boolean : Returns true if the UI Header/Field found in the list
	 */
	
	public static boolean FindFieldName_StringList(List<String> FieldsList,String FieldName)
	{
		for(String Field:FieldsList)
		{
			if(Field.trim().equalsIgnoreCase(FieldName.trim()))
			{
				return true;
			}
		}
		return false;
	}
}
