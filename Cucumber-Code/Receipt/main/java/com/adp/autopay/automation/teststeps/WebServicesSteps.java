package com.adp.autopay.automation.teststeps;

import com.adp.autopay.automation.commonlibrary.*;
import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.tabularParameters.Tabular_StringNString;
import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.PropertiesFile;
import com.adp.autopay.automation.webservices.WebServiceParameters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.runtime.java.ObjectContainer;
import gherkin.formatter.model.DataTableRow;
import net.javacrumbs.jsonunit.core.Option;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
//import org.apache.xpath.operations.String;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.adp.autopay.automation.commonlibrary.JSONCompareResult;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import static org.junit.Assert.*;
import static com.jayway.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonAssert.*;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_VALUES;

public class WebServicesSteps
{

	public WebServicesSteps()
	{
		this.context = ObjectContainer.getInstance(ExecutionContext.class);
		dataObjects = DataHelper.dataList(System.getProperty("user.dir") + PropertiesFile.GetEnvironmentProperty("TESTDATA_EXCEL_PATH") +  this.context.ServicesDataFilePath ,this.context.ServicesDataSheet);
	}
	ExecutionContext context;
	List<DataObject> dataObjects;

	public static String strJSONFilePath;

	/*
	 * This step will execute api command (GET) from ServicesData.xlsx  file for feature file/scenario 
	 * parameters :
	 * 		propertiesFileKey - API Service Name
	 * 		SuccessorFailure - you trying to see if command is successful or fail for exception testing
	 * 		Status - API command status in response
	 * 		UpdatePayload - If we need to have a input JSON file for API command 
	 */

	@When("^WEBSERVICES:When we run API command for \"(.*)\" for \"(.*)\" it returns \"(.*)\" status, update payload = \"(.*)\"$")
	public void ReadAndProcessJsonInputFile(String propertiesFileKey, String SucessorFailure, String status, String UpdatePayload){
		try{
			DataObject newObj = new DataObject();
			newObj.setServiceName(propertiesFileKey);
			newObj.setEnvironment(ExecutionContext.SRunAgainst);
			int index = dataObjects.indexOf(newObj); 
			
			if(index == -1) {
				this.context.addResult("Reading data file ", "Reading data file ", "Invalid Service Name " + propertiesFileKey, " Serivce Name does not exists in data file ",  RunStatus.FAIL);
				System.exit(0);
			}
			
			int j = context.STestName.lastIndexOf(".");
			if (j == -1)
			{
				this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
			}
			String testname = "";
			String USname = context.STestName.substring(0,j);
			
			for (DataObject obj : dataObjects) {
				
				if(newObj.equals(obj)){
				DataObject actualObj = obj;
				String url =  CommonUtility.buildURL(actualObj);
				List<String> valuesList = new ArrayList<String>();

				valuesList.add(actualObj.getServiceName());
				valuesList.add(url);
				valuesList.add(actualObj.getHttpMethod());

				valuesList.addAll(actualObj.getHeaders());
				String[] Values = new String[valuesList.size()];
				int arrayIndex = 0;
				for (String value: valuesList) {
					Values[arrayIndex] = value;
					arrayIndex++;
				}

				if(index != -1){
				
					WebServiceParameters webServiceParameters = new WebServiceParameters(Values);
					Response res = null;

					testname =  webServiceParameters.getStrTestName();
					if(webServiceParameters.getStrRequestType().equals("GET")){
						if(webServiceParameters.getHeaders() !=null){
							System.out.println("******************* Input URL ********************************************");
							System.out.println(webServiceParameters.getStrURI());
							System.out.println("************** Input URL Header Parameters *******************************");
							System.out.println(webServiceParameters.getHeaders());

							res = given().headers(new Headers(webServiceParameters.getHeaders())).get(webServiceParameters.getStrURI());
						}else
							res =  given().get(webServiceParameters.getStrURI());
					}
					////Code added by Prabhakar////////////////////////////////////////////////
					else if(webServiceParameters.getStrRequestType().equals("DELETE")){
						if(webServiceParameters.getHeaders() !=null){
							//System.out.print(webServiceParameters.getHeaders());
							System.out.print("***************************DELETE Loop*****************");
							System.out.print(webServiceParameters.getStrURI());
							System.out.print("***************************DELETE Loop*****************");

							res = given().headers(new Headers(webServiceParameters.getHeaders())).contentType("application/json").delete(webServiceParameters.getStrURI());
						}else
							res =  given().get(webServiceParameters.getStrURI());
					}
                   else{
						this.context.addResult("Parse input file for webservice request type", "Check input for webservice call type : "+ webServiceParameters.getStrRequestType(), "Webserivce call type not implemented ", "Webservice call type not implemented (ONLY GET, PUT and POST implemented)" , RunStatus.FAIL);

						// continue;
					}

					try {
						Thread.sleep(1300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Headers= "+ res.headers());
					System.out.println("Status Code= "+ res.getStatusCode());
					System.out.println("Status Line= "+res.getStatusLine());
					this.context.addResult("Execute webservice call for "+propertiesFileKey + " For DataRow_" + (actualObj.getRowNum()+1), "Execute webservice call: "+ webServiceParameters.getStrTestName(), "Webserivce call executed ", "Webservice call returned Status Code = " + res.getStatusCode() +  " StatusLine =  " + res.getStatusLine(), RunStatus.PASS);

					String strOutputDirectory = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname ;
					File currentFolder = new File (strOutputDirectory);
					if(!currentFolder.exists()){
						currentFolder.mkdir();
					}
					String strJSONOutputFileName = strOutputDirectory + "\\" +  webServiceParameters.getStrTestName() + "_" + context.strScenarioRunId + "_DataRow_" + (actualObj.getRowNum()+1) + ".json";
					String strJsonOutput = res.asString();
					String[] Invalidcodelist = {"400","404","500","204","409", "403"};
					writeLineToFile(strJSONOutputFileName, strJsonOutput);
					if (SucessorFailure.equalsIgnoreCase("Success"))
					{
						if ((res.getStatusCode() == 200 || res.getStatusCode() == 201) )

						{
							//writeLineToFile(strJSONOutputFileName, strJsonOutput);
							this.context.addResult("Generate json response file: "+propertiesFileKey + " For DataRow_" + (actualObj.getRowNum()+1), "Generate json response file", "Generated json response file " + strJSONOutputFileName, strJsonOutput, RunStatus.PASS);
						}
						else if (res.getStatusCode() == 503){
							this.context.addResult("Execute WebService for "+propertiesFileKey , "To Execute the WebService : "+ testname, " Service Temporarily Unavailable" + res.getStatusLine() ,  " Webservice Temporarily Unavailable" , RunStatus.FAIL);
						}
						else
							this.context.addResult("Execute WebService for "+propertiesFileKey, "To Execute the WebService : "+ testname, "WebService Needs to be executed", "Error occured while execution of webservice. Error code : " + res.getStatusLine() , RunStatus.FAIL);
					}
					else if (SucessorFailure.equalsIgnoreCase("Failure"))  //(SucessorFailure.equalsIgnoreCase("exception"))
					{
						if  (ArrayUtils.contains(Invalidcodelist,""+res.getStatusCode()))
						{
							this.context.addResult("Verify response status code for "+propertiesFileKey + " For DataRow_" + (actualObj.getRowNum()+1), "verify response status code", "Status code for api call =  " + res.getStatusCode() + "\n" + strJSONOutputFileName  , "api call failed as expected" + "\n\n" + strJsonOutput, RunStatus.PASS);
						}
						else
							this.context.addResult("Execute WebService for "+propertiesFileKey, "To Execute the WebService : "+ testname, "WebService Needs to be executed", "Error occured while execution of webservice. Error code : " + res.getStatusLine() , RunStatus.FAIL);
					}

				}
					}
			}
			
		}catch(AssertionError e){
			this.context.addResult("Execute WebService for "+propertiesFileKey, "To Execute the WebService based on the Request and the File", "WebService Needs to be executed", "Assertion error occured", RunStatus.FAIL);
			e.printStackTrace();
		}
	}


	/*Created/Updated By : Mahesh Gadi
	 * Updated below method to write output response file into a JSON File in results folder
	 * I have added a method to format the out JSON string to proper JSON Format
	 * parameters :
	 * 		strFileName - Output File Path
	 * 		strData - JSON output string 
	 */
	public static void writeLineToFile(String strFileName, String strData){
		try {
			File file = new File (strFileName);
			if (file.exists())
				file.delete();
			FileOutputStream fos = new FileOutputStream(strFileName);

			if(strData != ""){
				org.json.JSONObject object = new org.json.JSONObject(strData);
				System.out.println("************** Output JSON body *******************************");
				System.out.println(strData);
				System.out.println("************** Output JSON body *******************************");
				System.out.println(JsonFormatter.format(object));
				IOUtils.write(JsonFormatter.format(object), fos, Charset.defaultCharset());
			}
			else{
				System.out.println("************** No JSON outout to print *******************************");
			}
			fos.close();
		}
		catch (IOException e)
		{}
	}

	public static String formatJsonReadable(String strJsonOutput){
		return strJsonOutput;
	}


	/*
	 *Below step defnition added by Mahesh Gadi to iterate thru excel data and use input JSON parameters from excel sheet
	 * This step will execute api commands (ADD/UPDATE/DELETE) from ServicesData.xlsx  file for feature file/scenario also it used input data from TestDataInput.xlsx for JSON template data
	 * parameters :
	 * 		propertiesFileKey - API Service Name
	 * 		SuccessorFailure - you trying to see if command is successful or fail for exception testing
	 * 		Status - API command status in response
	 * 		UpdatePayload - YES/NO - always YES for ADD/UPDATE/DELETE services
	 *      MyInputDataSheet - Input sheet name from TestDataInput.xlsx file for JSON template data 
	 *      RowIndex - Row number/s to iterate thru from TestDataInput.xlsx file for JSON template data 
	 */


	/*
	 *Below step defnition added by Mahesh Gadi to iterate thru excel data and use input JSON parameters from excel sheet and execute ALL rows from it
	 * This step will execute api commands (ADD/UPDATE/DELETE) from ServicesData.xlsx  file for feature file/scenario also it used input data from TestDataInput.xlsx for JSON template data
	 *  Parameters :
	 * 		propertiesFileKey - API Service Name
	 * 		SuccessorFailure - you trying to see if command is successful or fail for exception testing
	 * 		Status - API command status in response
	 * 		UpdatePayload - YES/NO - always YES for ADD/UPDATE/DELETE services
	 *      MyInputDataSheet - Input sheet name from TestDataInput.xlsx file for JSON template data 
	 *      RowIndex - ALL :  To iterate ALL rows from TestDataInput.xlsx file for JSON template data 
	 */
	public void ProcessJsonInputFileWithExcelALL(String propertiesFileKey, String SucessorFailure, String status, String UpdatePayload ,String MyInputDataSheet , String RowIndex , String[] Values , String ServiceName){
		try{

			int DataRowIndex = Integer.parseInt(RowIndex);
			int j = context.STestName.lastIndexOf(".");
			String USname = context.STestName.substring(0,j);

			String testname = "";

			String Path = System.getProperty("user.dir")+ PropertiesFile.GetEnvironmentProperty("PAYLOAD_PATH");
			String ResponsePath = System.getProperty("user.dir")+ PropertiesFile.GetEnvironmentProperty("RESPONSE_PATH");
			String InputJsonPath = PropertiesFile.GetEnvironmentProperty("TEMP_INPUT_PATH");

			String JSONfilenamestr = "";

			if(j != -1){
				StringBuilder str = new StringBuilder();
				new StringBuilder();
				WebServiceParameters webServiceParameters = new WebServiceParameters(Values);

				JSONfilenamestr = Path + USname + "\\" +  webServiceParameters.getStrTestName() + "_POST.json";
				JSONParser parser = new JSONParser();
				Response res = null;

				testname =  webServiceParameters.getStrTestName();
				if(webServiceParameters.getStrRequestType().equals("GET")){
					if(webServiceParameters.getHeaders() !=null){
						System.out.println("************** Input URL Header Parameters *******************************");
						System.out.print(webServiceParameters.getHeaders());
						System.out.println("******************* Input URL *******************************");
						System.out.print(webServiceParameters.getStrURI());
						res = given().headers(new Headers(webServiceParameters.getHeaders())).get(webServiceParameters.getStrURI());
					}else
						res =  given().get(webServiceParameters.getStrURI());
				}
				////Code added by Prabhakar////////////////////////////////////////////////
				else if(webServiceParameters.getStrRequestType().equals("DELETE")){
					if(webServiceParameters.getHeaders() !=null){
						//System.out.print(webServiceParameters.getHeaders());
						System.out.print("***************************DELETE Loop*****************");
						System.out.print(webServiceParameters.getStrURI());
						System.out.print("***************************DELETE Loop*****************");

						res = given().headers(new Headers(webServiceParameters.getHeaders())).contentType("application/json").delete(webServiceParameters.getStrURI());
					}else
						res =  given().get(webServiceParameters.getStrURI());
				}
				///////////////////////////////////////////////////////////////////////
				if (webServiceParameters.getStrRequestType().equals("POST")) {
					String strBody = "";
					File inputJson = new File(Path + USname + "\\" + webServiceParameters.getStrTestName() + "_POST.json");
					String updatedinputJson = InputJsonPath + USname + "\\" + webServiceParameters.getStrTestName() + "_" + context.strScenarioRunId +  "_Data_Row_" + RowIndex + "_POST.json";
					if (j != -1) {
						try {
							if (new File(updatedinputJson).exists()) 
								{
									strBody = parser.parse(new FileReader(updatedinputJson)).toString();
				
						} 
						}catch (Exception e) {
							e.printStackTrace();
							this.context.addResult("Parse input file for POST api call ", " Check input for POST api call : " + testname, "Verify input parameters are correct", "Error parsing the POST JSON File. Please check the file. File:-" + Path + "\\" + webServiceParameters.getStrTestName() + "_POST.json", RunStatus.FAIL);
							//continue;
						}
					}
					System.out.println("******************* Input URL *******************************");
					System.out.println(webServiceParameters.getStrURI());
					System.out.println("************** Input JSON body *******************************");
					System.out.println(strBody);
					if(webServiceParameters.getHeaders() !=null) {

						System.out.println("************** Input URL Header Parameters *******************************");
						System.out.print(webServiceParameters.getHeaders());
						//System.out.print(webServiceParameters.getStrURI());
						res = given().headers(new Headers(webServiceParameters.getHeaders())).contentType("application/json").body(strBody).post(webServiceParameters.getStrURI());
					}
					else
						res =  given().contentType("application/json").post(webServiceParameters.getStrURI(), strBody);

				}
				else if(webServiceParameters.getStrRequestType().equals("PUT")){
					String strBody = "";
					if(new File(Path +USname + "\\" + webServiceParameters.getStrTestName() + "_PUT.json").exists()){
						try {
							if (UpdatePayload.equalsIgnoreCase("YES")){

								if (propertiesFileKey.contains("Dependent") && !(propertiesFileKey.toLowerCase().contains("invalid"))){
									String strUpdatedpayloadfile = Path + USname + "\\" + webServiceParameters.getStrTestName() + "_PUT.json";
									FileOperations.UpdatePayLoad(context, strUpdatedpayloadfile,"Dependent_PUT");
									//strUpdatedpayloadfile = Path + USname + "\\" + webServiceParameters.getStrTestName() + "_PUT_test.json";
									strUpdatedpayloadfile = Path + USname + "\\" + webServiceParameters.getStrTestName() + "_PUT.json"; // update samefile instead _test

									strBody = parser.parse(new FileReader(strUpdatedpayloadfile)).toString();

								}
								else if (propertiesFileKey.contains("PIE")) {
									String strUpdatedpayloadfile = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname + "\\" + webServiceParameters.getStrTestName() + "_PUT.json";
									FileOperations.UpdatePayLoad(context, JSONfilenamestr,"PIE_PUT");
									//strUpdatedpayloadfile = Path + USname + "\\" + webServiceParameters.getStrTestName() + "_PUT_test.json";
									//strUpdatedpayloadfile = Path + USname + "\\" + webServiceParameters.getStrTestName() + "_PUT.json"; // update samefile instead _test

									strBody = parser.parse(new FileReader(JSONfilenamestr)).toString();

								}
							}
							else {
								strBody = parser.parse(new FileReader(Path + USname + "\\" +  webServiceParameters.getStrTestName() + "_PUT.json")).toString();

							}

						} catch (Exception  e) {
							e.printStackTrace();
							this.context.addResult("Parse input file for PUT api call ", " Check input for PUT api call : "+ testname, "Exception occured while parsing input file", "Error parsing the PUT JSON File. Please check the file. File:-" + Path + "\\" + webServiceParameters.getStrTestName() + "_PUT.json" , RunStatus.FAIL);

							//		continue;
						}
					}

					if(webServiceParameters.getHeaders() !=null)
						res = given().headers(new Headers(webServiceParameters.getHeaders())).contentType("application/json").body(strBody).put(webServiceParameters.getStrURI());

					else
						res =  given().contentType("application/json").put(webServiceParameters.getStrURI(), strBody);

				}else{
					this.context.addResult("Parse input file for webservice request type", "Check input for webservice call type : "+ webServiceParameters.getStrRequestType(), "Webserivce call type not implemented ", "Webservice call type not implemented (ONLY GET, PUT and POST implemented)" , RunStatus.FAIL);

					// continue;
				}

				try {
					Thread.sleep(1300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Headers= "+ res.headers());
				System.out.println("Status Code= "+ res.getStatusCode());
				System.out.println("Status Line= "+res.getStatusLine());
				this.context.addResult("Execute webservice call for "+propertiesFileKey +" For Data Row: "+RowIndex, "Execute webservice call: "+ webServiceParameters.getStrTestName(), "Webserivce call executed ", "Webservice call returned Status Code = " + res.getStatusCode() +  " StatusLine =  " + res.getStatusLine(), RunStatus.PASS);

				//String strOutputDirectory = "c:\\temp\\output" + "\\" + USname ;
				String strOutputDirectory = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname ;
				//String strOutputDirectory = ResponsePath + USname ;
				File currentFolder = new File (strOutputDirectory);
				if(!currentFolder.exists()){
					currentFolder.mkdir();
				}

				String strJSONOutputFileName = strOutputDirectory + "\\" +  webServiceParameters.getStrTestName() + "_" + context.strScenarioRunId + "_Data_Row_" + RowIndex + ".json";
				String strJsonOutput = res.asString();

				String[] Invalidcodelist = {"400","404","500","204","409", "403"};
				writeLineToFile(strJSONOutputFileName, strJsonOutput);
				if (SucessorFailure.equalsIgnoreCase("Success"))
				{
					if ((res.getStatusCode() == 200 || res.getStatusCode() == 201) )

					{
						//writeLineToFile(strJSONOutputFileName, strJsonOutput);
						this.context.addResult("Generate json response file: "+propertiesFileKey  +" For Data Row: "+RowIndex, "Generate json response file", "Generated json response file " + strJSONOutputFileName, strJsonOutput, RunStatus.PASS);
					}
					else if (res.getStatusCode() == 503){
						this.context.addResult("Execute WebService for "+propertiesFileKey, "To Execute the WebService : "+ testname, " Service Temporarily Unavailable" + res.getStatusLine() ,  " Webservice Temporarily Unavailable" , RunStatus.FAIL);
					}
					else
						this.context.addResult("Execute WebService for "+propertiesFileKey +" For Data Row: "+RowIndex, "To Execute the WebService : "+ testname, "WebService Needs to be executed", "Error occured while execution of webservice. Error code : " + res.getStatusLine() , RunStatus.FAIL);
				}
				else if (SucessorFailure.equalsIgnoreCase("Failure"))  //(SucessorFailure.equalsIgnoreCase("exception"))
				{
					if  (ArrayUtils.contains(Invalidcodelist,""+res.getStatusCode()))
					{
						this.context.addResult("Verify response status code for "+propertiesFileKey, "verify response status code", "Status code for api call =  " + res.getStatusCode() + "\n" + strJSONOutputFileName  , "api call failed as expected" + "\n\n" + strJsonOutput, RunStatus.PASS);
					}
					else
						this.context.addResult("Execute WebService for "+propertiesFileKey, "To Execute the WebService : "+ testname, "WebService Needs to be executed", "Error occured while execution of webservice. Error code : " + res.getStatusLine() , RunStatus.FAIL);
				}

			}

		}catch(AssertionError e){
			this.context.addResult("Execute WebService for "+propertiesFileKey, "To Execute the WebService based on the Request and the File", "WebService Needs to be executed", "Assertion error occured", RunStatus.FAIL);
			e.printStackTrace();
		}
	}

	/*************Added below step definition: Prabhakar 05/11/2015*************************/
	@Then("^WEBSERVICES:Retrieve field \"(.*)\" value from Response Json file \"(.*)\" and replace value for command \"(.*)\" in command file \"(.*)\"$")
	public void GetValueFromJsonAndUpdateCommand(String Field, String JSONFile, String command, String commandFile){
		System.out.println("*****************VerifyResponse Method Execution Starts*********");

		String jsonFile="";
		//String expectedValue = "\""+ Field+ "\""+ ":\"" + Value +"\"";

		String element = "";
		String FileName = "";
		String replaceWith = "";
		String stringToReplace = "";

		try {

			int j = context.STestName.lastIndexOf(".");

			if (j == -1)
			{
				this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
			}

			String USname = context.STestName.substring(0,j);
			System.out.println("User Story Name = "+ USname);

			//String ResponsePath = System.getProperty("user.dir")+ "/src/main/java/com/adp/benefits/automation/webservices/story.json/Response/";
			//jsonFile = "C:\\temp\\output\\US215284_Beneficiary_OutsideProcess_UpdateBeneficiary\\POSTNewBeneficiaryFriendOutsideProcess.json";
			jsonFile = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname+ "\\"+JSONFile + ".json";
			System.out.println("File Path = "+ jsonFile);

			// read the json file
			FileReader reader = new FileReader(jsonFile);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);


			// get a String from the JSON object
			element = (String) jsonObject.get(Field);
			System.out.println("Field Value from Json File is: " + element);
			System.out.println("Field Value from Json File is: " + element);
			replaceWith=element;
			//stringToReplace=fieldToReplace;

			FileName = System.getProperty("user.dir")+"/src/main/java/com/adp/autopay/automation/webservices/story.json/APIcmds/" + commandFile + ".txt";
			FileOperations.ReplaceContentInWebservicepropertyFile(context, FileName, command, replaceWith);
			System.out.println("********************After FileOperations Method********************* ");
			System.out.println("********************After FileOperations Method********************* ");
			context.addResult("Update webservice command File with newly generated "+Field+" value", "Update webservice command File with newly generated "+Field+" value", "Update webservice command File with newly generated "+Field+" value", "Webservice command file got updated successfully", RunStatus.PASS);
		}catch(Exception e){
			System.out.println("Exception Occured");
			this.context.addResult("Update webservice command File with newly generated "+Field+" value", "Update webservice command File with newly generated "+Field+" value", "Update webservice command File with newly generated "+Field+" value", "Found an exception in executing step", RunStatus.FAIL);
			e.printStackTrace();
		}

	}


	/*Modified by Mahesh Gadi : To verify the responses for given fields and values 
	 *
	 * This step will verify the responses for given fields and values
	 * parameters :
	 * 		JSONFile - Output response file which will have the output from GET/ADD/UPDATE/DELETE services
	 * 		table - DataTable with field and value combination to verify in response JSON file
	 *              | Field | Value |
     *              | shortName | CKING |
     *              | shortName | MEDCTY |
	 */
	@Then("^WEBSERVICES:Verify Response Fields as Values for JSON \"([^\"]*)\"$")
	public void VerifyResponseJSONForFieldsAndValues(String JSONFile ,DataTable table ) {

		List<Map<String, String>> data = table.asMaps(String.class, String.class);
		String Field = "";
		String Value = "";
		DataObject newObj = new DataObject();
		newObj.setServiceName(JSONFile);
		newObj.setEnvironment(ExecutionContext.SRunAgainst);
		int index = dataObjects.indexOf(newObj);
		
		if(index == -1) {
			this.context.addResult("Reading data file ", "Reading data file ", "Invalid Service Name " + JSONFile, " Serivce Name does not exists in data file ",  RunStatus.FAIL);
			System.exit(0);
		}
		
		try {

			int j = context.STestName.lastIndexOf(".");
			if (j == -1)
			{
				this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
			}
			String USname = context.STestName.substring(0,j);

			//String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\"+JSONFile + "_" + context.strScenarioRunId + ".json";

			//String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\" +"ListService_SCENARIO_20160224_123044_005" + ".json";

			for (DataObject obj : dataObjects) {
				if(newObj.equals(obj)){
					DataObject actualObj = obj;
				
					String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\"+JSONFile + "_" + context.strScenarioRunId  + "_DataRow_" + (actualObj.getRowNum()+1) + ".json";
			
			File file = new File(FilePath);
			
			if(file.exists()){
				System.out.println("\n Verifying Response JSON file for " + FilePath);
				for (int t = 0; t < data.size(); t++) {

						Field = data.get(t).get("Field");
						Value = data.get(t).get("Value");

						@SuppressWarnings("resource")
						int m = 0, n = 0;
						String respoutputValue = "";
					//	BufferedReader br = new BufferedReader(new FileReader(file));

					FileInputStream fis = new FileInputStream(file);
					String content = IOUtils.toString(fis, Charset.defaultCharset());
					content = content.replaceAll("\\r\\n|\\r|\\n", "");
					content = content.replaceAll(" :", ":");
					content = content.replaceAll(" \"", "\"");
					content= content.replaceAll("\\t", "");

					//while ((content != null)) {
							String[] Fields = content.replace("{", "").replace("}", "").split(",");
							for (int i = 0; i < Fields.length; i++) {
								if (Fields[i].contains(Field)) {
									String Line = Fields[i];
									int start = (Line.indexOf(Field) + (Field.length()) + 2);
									int end = Line.length();
									String outputValue = (Line.substring(start, end).trim().replace("\"", ""));
									if (outputValue.equalsIgnoreCase(Value)) {
										//this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, Field +"="+Value, Field +"="+Value, RunStatus.PASS);
										//return;
										m = m + 1;
									} else {
										//this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, Field +"="+Value, Field +"="+outputValue, RunStatus.FAIL);
										//return;
										n = n + 1;
										//respoutputValue=respoutputValue + "," + outputValue;
									}
								}
							}
						//}
					fis.close();
						if (m > 0) {
							System.out.println("Successfully Verified Response JSON file for " + JSONFile + ": " + " for " + Field + "=" + Value + " And" + " There Are " + m + " Occurrences of the  " + "\n" + Field + "=" + Value);
							this.context.addResult("Verify Response for the WebService  " + JSONFile + ": " + "for " + Field + "=" + Value + " for " + "_DataRow_" + (actualObj.getRowNum()+1), "Verify Response for the WebService  " + JSONFile + ": " + "for " + Field + "=" + Value, Field + "=" + Value, "Verified Response For  " + Field + "=" + Value + " And" + " There Are " + m + " Occurrences of the " + Field + "=" + Value, RunStatus.PASS);
						} else {
							System.out.println("Verification of Response JSON file for " + JSONFile + ": " + " for " +"\n" + Field + "=" + Value  +"\n" + " FAILED"  +"\n"+ " But It found " + n + " Occurrences of the  " + "\n" + Field + " and value(s) not matched to " + " = " + Value);
							this.context.addResult("Verify Response for the WebService  " + JSONFile + ": " + "for " + Field + "=" + Value + " for " + "_DataRow_" + (actualObj.getRowNum()+1), "Verify Response for the WebService" + JSONFile, Field + "=" + Value, "Verification of Response JSON file for " + JSONFile + ": " + " for " + Field + "=" + Value + " FAILED" + " And It found " + n + " Occurrences of the  " + "\n" + Field + " but value(s) not matched to " + " = " + Value, RunStatus.FAIL);
						}
					}
							}


			else{
				this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, Field +"="+Value, "File not found", RunStatus.FAIL);
			}
		}
			}
		}
			catch(Exception e){
			this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, Field +"="+Value, "Found an exception in executing step", RunStatus.FAIL);
			e.printStackTrace();
		}

	}

	//@Then("^WEBSERVICES:Verify Error Codes in JSON Response \"([^\"]*)\"$")
	public void VerifyErrorCodesinJSONResp(String JSONFile ) {

//		List<Map<String, String>> data = table.asMaps(String.class, String.class);
		String Field = "";
		String Value = "";
		
		DataObject newObj = new DataObject();
		newObj.setServiceName(JSONFile);
		newObj.setEnvironment(ExecutionContext.SRunAgainst);
		int index = dataObjects.indexOf(newObj);
		
		DataObject actualObj =  dataObjects.get(index);
		
		Map<String, List<String>> errordataMap = new HashMap<String, List<String>>();
		
		List<String> keysTobeVerifiedList = new ArrayList<String>();
		keysTobeVerifiedList.add("processMessages_userMessage_codeValue");
		keysTobeVerifiedList.add("processMessages_userMessage_messageTxt");
		
		try {

			int j = context.STestName.lastIndexOf(".");
			if (j == -1)
			{
				this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
			}
			String USname = context.STestName.substring(0,j);

			String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\"+JSONFile + "_" + context.strScenarioRunId + ".json";

			//String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\" +"ListService_SCENARIO_20160224_123044_005" + ".json";

			File file = new File(FilePath);

			if(file.exists()){
				
				FileInputStream fis = new FileInputStream(FilePath);
				String content = IOUtils.toString(fis, Charset.defaultCharset());
				
				org.json.JSONObject json = new org.json.JSONObject(content);
				getVerifyTheValue(getAllObjs(json.names(), json, keysTobeVerifiedList, errordataMap, actualObj), keysTobeVerifiedList, errordataMap, actualObj);
				
				
				List<String> errorCodesList = errordataMap.get("processMessages_userMessage_codeValue");
				List<String> errorDescList = errordataMap.get("processMessages_userMessage_messageTxt");
				
				List<String> errorCodes = actualObj.getErrorCodes();
				List<String> errorDesc = actualObj.getErrorDescriptions();
				
				//System.out.println("errordataMap : " + errordataMap + " : errorCodes : " + errorCodes + " : errorDesc : " + errorDesc);
				
				if(errorCodesList != null && (errorCodesList.size() == errorCodes.size()) && 
						(errorDescList != null && errorDescList.size() == errorDesc.size())) {
					this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+
							JSONFile, "Error Code" +"="+errorCodes + " Error Desc=" + errorDesc, " Error Code and Description Found ", RunStatus.PASS);
				} else if(errorCodesList != null && (errorCodesList.size() > 0 ) && 
						(errorDescList != null && errorDescList.size() > 0)) {
					this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+
							JSONFile, "Error Code" +"="+errorCodes + " Error Desc=" + errorDesc, " Error Code && Description Not Found ", RunStatus.FAIL);
				} else {
					this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+
							JSONFile, "Error Code" +"="+errorCodes + " Error Desc=" + errorDesc, " Error Code && Description Not Found ", RunStatus.FAIL);
				} 
			} else{
				this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, "Error Code" +"="+Value, "File not found", RunStatus.FAIL);
			}
		}catch(Exception e){
			this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, "Error Code" +"="+Value, "Found an exception in executing step", RunStatus.FAIL);
			e.printStackTrace();
		}

	}
	
	public static List<Object> getAllObjs(JSONArray names, org.json.JSONObject obj, List<String> keysToBeVerifiedList,
			Map<String, List<String>> errordataMap, DataObject actualObj) throws Exception {
		List<Object> listObjs = new ArrayList<Object>();
		if(names !=null && names.length() > 0) {
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);

				boolean isDataMapKey = false;

				for (String keyTobeVerified : keysToBeVerifiedList) {
					System.out.println(" : keyTobeVerified : " + keyTobeVerified);
					if(keyTobeVerified.startsWith(name))
					{
						Object value = obj.get(names.getString(i));
System.out.println("value : " + value );
						if(value instanceof org.json.JSONObject) {
//						List mapValues = (List) dataMap.get(dataMapKey);
							String[] dataMapKeyTokens = keyTobeVerified.split("_");
							List<String> availableTokens = Arrays.asList(dataMapKeyTokens);

							availableTokens.remove(name);
							org.json.JSONObject valueJSON = (org.json.JSONObject) value;
							JSONArray valueNames = valueJSON.names();
							getObject(valueNames, valueJSON, keyTobeVerified, keysToBeVerifiedList, availableTokens, errordataMap, actualObj);
						} else if(value instanceof org.json.JSONArray) {
//							List mapValues = (List) dataMap.get(dataMapKey);
								String[] dataMapKeyTokens = keyTobeVerified.split("_");
								
								List<String> availableTokens = Arrays.asList(dataMapKeyTokens);

//								availableTokens.remove(name);
								org.json.JSONArray jsonArray = (org.json.JSONArray) value;
								for (int j=0; j< jsonArray.length(); j++) {
									Object  innserJson = jsonArray.get(j);
									if(innserJson instanceof org.json.JSONObject) {
										org.json.JSONObject jsonObj = jsonArray.getJSONObject(j);
//										getVerifyTheValue(listObjs, keysToBeVerifiedList, errordataMap, actualObj);
										getObject(jsonObj.names(), jsonObj, keyTobeVerified, keysToBeVerifiedList, availableTokens, errordataMap, actualObj);
									} 
								}
						} else {
							String finalVal = (String)obj.get(name);
							if(actualObj.getErrorCodes().contains(finalVal)) {
								if(errordataMap.get(keyTobeVerified) == null) {
									errordataMap.put(keyTobeVerified, new ArrayList<String>());
								}
								
								if(!errordataMap.get(keyTobeVerified).contains(finalVal))
								errordataMap.get(keyTobeVerified).add(finalVal);
							}
						}

						isDataMapKey = true;
					}
				}

				if(!isDataMapKey) {
					listObjs.add(obj.get(names.getString(i)));
				}

			}
		}


		return listObjs;
	}
	
	public static void getObject(JSONArray names, org.json.JSONObject obj, String dataMapKey, List<String> dataMap,
			List<String> dataMapKeyTokens, Map<String, List<String>> errordataMap, DataObject actualObj) throws Exception {
		
		if(names !=null){
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);
				Object value = obj.get(names.getString(i));

				for (String dataMapKeyToken : dataMapKeyTokens) {
					if(value instanceof org.json.JSONObject) {
						//System.out.println("value: " + value + " : ((JSONObject)value).names() : " + ((JSONObject)value).names());
						getObject(((org.json.JSONObject)value).names(), ((org.json.JSONObject)value), dataMapKey, dataMap, dataMapKeyTokens, errordataMap, actualObj);
					} else if(dataMapKeyToken.equals(name)){
						String finalVal = (String)obj.get(name);
						if(actualObj.getErrorCodes().contains(finalVal)) {
							if(errordataMap.get(dataMapKey) == null) {
								errordataMap.put(dataMapKey, new ArrayList<String>());
							}
							
							if(!errordataMap.get(dataMapKey).contains(finalVal))
							errordataMap.get(dataMapKey).add(finalVal);
							System.out.println("errordataMap : " + errordataMap);
						}
					}
				}

			}
		}

	}

	public static void getVerifyTheValue(List<Object> listObjects, List<String> dataMap, Map<String, List<String>> errordataMap, DataObject actualObj) throws Exception {
		for (Object object : listObjects) {
			if(object instanceof org.json.JSONObject) {
				getVerifyTheValue(getAllObjs(((org.json.JSONObject) object).names(), (org.json.JSONObject) object, dataMap, errordataMap, actualObj),dataMap, errordataMap, actualObj);
			}
		}
	}
	
	public boolean isValueExists(org.json.JSONObject object) {
		
		return false;
	}


	
	//@Then("^WEBSERVICES:Verify Error Codes in JSON Response \"([^\"]*)\"$")
	public void VerifyErrorCodesinJSONResp_Old(String JSONFile ,DataTable table ) {

		List<Map<String, String>> data = table.asMaps(String.class, String.class);
		String Field = "";
		String Value = "";

		try {

			int j = context.STestName.lastIndexOf(".");
			if (j == -1)
			{
				this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
			}
			String USname = context.STestName.substring(0,j);

			String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\"+JSONFile + "_" + context.strScenarioRunId + ".json";

			//String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\" +"ListService_SCENARIO_20160224_123044_005" + ".json";

			File file = new File(FilePath);

			if(file.exists()){

				for (int t = 0; t < data.size(); t++) {

						Field = "codeValue";
						Value = data.get(t).get("Error Code");

						@SuppressWarnings("resource")
						int m = 0, n = 0;
						String respoutputValue = "";
					//	BufferedReader br = new BufferedReader(new FileReader(file));

					FileInputStream fis = new FileInputStream(file);
					String content = IOUtils.toString(fis, Charset.defaultCharset());
					content = content.replaceAll("\\r\\n|\\r|\\n", "");
					content = content.replaceAll(" :", ":");
					content = content.replaceAll(" \"", "\"");
					content= content.replaceAll("\\t", "");

					//while ((content != null)) {
							String[] Fields = content.replace("{", "").replace("}", "").split(",");
							for (int i = 0; i < Fields.length; i++) {
								if (Fields[i].contains(Field)) {
									String Line = Fields[i];
									int start = (Line.indexOf(Field) + (Field.length()) + 2);
									int end = Line.length();
									String outputValue = (Line.substring(start, end).trim().replace("\"", ""));
									if (outputValue.equalsIgnoreCase(Value)) {
										//this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, Field +"="+Value, Field +"="+Value, RunStatus.PASS);
										//return;
										m = m + 1;
									} else {
										//this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, Field +"="+Value, Field +"="+outputValue, RunStatus.FAIL);
										//return;
										n = n + 1;
										//respoutputValue=respoutputValue + "," + outputValue;
									}
								}
							}
						//}
					fis.close();
						if (m > 0) {
							System.out.println("Successfully Verified Response JSON file for " + JSONFile + ": " + " for " + "Error Code" + "=" + Value + " And" + " There Are " + m + " Occurrences of the  " + "\n" + "Error Code" + "=" + Value);
							this.context.addResult("Verify Response for the WebService  " + JSONFile + ": " + "for " + "Error Code" + "=" + Value, "Verify Response for the WebService  " + JSONFile + ": " + "for " + "Error Code" + "=" + Value, "Error Code" + "=" + Value, "Successfully Verified Response For  " + "Error Code" + "=" + Value , RunStatus.PASS);
						} else {
							System.out.println("Verification of Response JSON file for " + JSONFile + ": " + " for " +"\n" + "Error Code" + "=" + Value  +"\n" + " FAILED"  +"\n"+ " But It found " + n + " Occurrences of the  " + "\n" + "Error Code" + " and value(s) not matched to " + " = " + Value);
							this.context.addResult("Verify Response for the WebService  " + JSONFile + ": " + "for " + "Error Code" + "=" + Value, "Verify Response for the WebService" + JSONFile, "Error Code" + "=" + Value, "Verification of Response JSON file for " + JSONFile + ": " + " for " + "Error Code" + "=" + Value + " FAILED" , RunStatus.FAIL);
						}
					}
							}


			else{
				this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, "Error Code" +"="+Value, "File not found", RunStatus.FAIL);
			}
		}catch(Exception e){
			this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, "Error Code" +"="+Value, "Found an exception in executing step", RunStatus.FAIL);
			e.printStackTrace();
		}

	}
	
	@Then("^WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response \"([^\"]*)\"$")
		public void VerifyErrorCodesinJSONResp_New(String JSONFile) {

			//List<Map<String, String>> data = table.asMaps(String.class, String.class);
			String Field = "";
			String Value = "";

			DataObject newObj = new DataObject();
			newObj.setServiceName(JSONFile);
			newObj.setEnvironment(ExecutionContext.SRunAgainst);
			int index = dataObjects.indexOf(newObj);
			
			if(index == -1) {
				this.context.addResult("Reading data file ", "Reading data file ", "Invalid Service Name " + JSONFile, " Serivce Name does not exists in data file ",  RunStatus.FAIL);
				System.exit(0);
			}
			
			//DataObject actualObj =  dataObjects.get(index);
				 						
			try {

				int j = context.STestName.lastIndexOf(".");
				if (j == -1)
				{
					this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
				}
				String USname = context.STestName.substring(0,j);

				for (DataObject obj : dataObjects) {
					
					if(newObj.equals(obj)){
					DataObject actualObj = obj;
					String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\"+JSONFile + "_" + context.strScenarioRunId  + "_DataRow_" + (actualObj.getRowNum()+1) + ".json";
					
					verifyErrorCodesAndDescriptions("User" ,actualObj , JSONFile , FilePath  );
					verifyErrorCodesAndDescriptions("Developer" ,actualObj , JSONFile , FilePath  );
				}
			}
			}catch(Exception e){
		this.context.addResult("Verify Services Data Input File For Error Codes", "Verify Services Data Input File For Error Codes"+JSONFile, "Error Code" +"="+Value, "Found an exception in executing step", RunStatus.FAIL);
		e.printStackTrace();
	}
}
	
	public void verifyErrorCodesAndDescriptions(String ErrorType , DataObject actualObj , String JSONFile , String FilePath  )
	{
		
		try {
		
	if(ErrorType.equalsIgnoreCase("User")) {
		
		if(actualObj.getErrorCodes() != null){
			// iterate the Error Codes list
			System.out.println("\n==> iterate the Error Codes list - Here are the User Error Codes needs to verified : ");
			for (String errorCode : actualObj.getErrorCodes()) {
				System.out.println(errorCode);
			}
			System.out.println("Start Verifying for Error Codes in ===>" +FilePath);
			verifyResponseFile( "User" , JSONFile , FilePath , "codeValue" , actualObj.getErrorCodes() );
		}
		else{
			this.context.addResult("Verify " + ErrorType + " Error Codes for the WebService  ", "Verify Services Data Input File"+JSONFile, "User Error Codes are not provided in Input excel sheet", "No Error codes provided in Input Excel Sheet", RunStatus.FAIL);
		}
						
       if(actualObj.getErrorDescriptions() != null){
   		// iterate the Error Descriptions list
			System.out.println("\n==> iterate the Error Descriptions list - Here are the User Error Descriptions needs to verified : ");
			     for (String errorCode : actualObj.getErrorDescriptions()) {
				System.out.println(errorCode);
			}
			  System.out.println("Start Verifying for Error Descriptions in ===>" +FilePath);
			     verifyResponseFile("User" , JSONFile , FilePath , "messageTxt" , actualObj.getErrorDescriptions() );
		}
		else{
			this.context.addResult("Verify " + ErrorType + " Error Descriptions for the WebService  ", "Verify Services Data Input File"+JSONFile, "User Error Descriptions are not provided in Input excel sheet", "No Error Descriptions provided in Input Excel Sheet", RunStatus.FAIL);
		}
       
	}

	
if(ErrorType.equalsIgnoreCase("Developer")) {
		
		if(actualObj.getdevErrorCodes() != null ){
			// iterate the Error Codes list
			System.out.println("\n==> iterate the Error Codes list - Here are the Developer Error Codes needs to verified : ");
			for (String errorCode : actualObj.getErrorCodes()) {
				System.out.println(errorCode);
			}
			System.out.println("Start Verifying for Error Codes in ===>" +FilePath);
			verifyResponseFile("Developer" , JSONFile , FilePath , "codeValue" , actualObj.getdevErrorCodes() );
		}
		else{
			this.context.addResult("Verify " + ErrorType + " Error Codes for the WebService  ", "Verify Services Data Input File"+JSONFile, "Error Codes are not provided in Input excel sheet", "No Error codes provided in Input Excel Sheet", RunStatus.FAIL);
		}
						
       if(actualObj.getdevErrorDescriptions() != null){
   		// iterate the Error Descriptions list
			System.out.println("\n==> iterate the Error Descriptions list - Here are the Developer Error Descriptions needs to verified : ");
			     for (String errorCode : actualObj.getErrorDescriptions()) {
				System.out.println(errorCode);
			}
			  System.out.println("Start Verifying for Error Descriptions in ===>" +FilePath);
			     verifyResponseFile( "Developer" ,JSONFile , FilePath , "messageTxt" , actualObj.getdevErrorDescriptions() );
		}
		else{
			this.context.addResult("Verify " + ErrorType + " Error Descriptions for the WebService  ", "Verify Services Data Input File"+JSONFile, "Error Descriptions are not provided in Input excel sheet", "No Error Descriptions provided in Input Excel Sheet", RunStatus.FAIL);
		}
       
	}

		}catch(Exception e){
			this.context.addResult("Verify Services Data Input File For Error Codes", "Verify Services Data Input File For Error Codes"+JSONFile, "Unexpected Exception" , "Found an exception in executing step", RunStatus.FAIL);
			e.printStackTrace();
		}
       
	}
			
public void verifyResponseFile( String ErrorType , String JSONFile , String FilePath , String FieldToVerify , List<String> ListToVerify )
{
	String Field = "";
	String Value = "";	
	File file = new File(FilePath);

				try {
				
				if(file.exists()){

					for (String errorCode : ListToVerify) {
						Field = FieldToVerify;
						Value =errorCode;
						
						@SuppressWarnings("resource")
						int m = 0, n = 0;
								
					FileInputStream fis = new FileInputStream(file);
					String content = IOUtils.toString(fis, Charset.defaultCharset());
					content = content.replaceAll("\\r\\n|\\r|\\n", "");
					content = content.replaceAll(" :", ":");
					content = content.replaceAll(" \"", "\"");
					content= content.replaceAll("\\t", "");

					//while ((content != null)) {
							String[] Fields = content.replace("{", "").replace("}", "").split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					
							for (int i = 0; i < Fields.length; i++) {
								if (Fields[i].contains(Field)) {
									String Line = Fields[i];
									int start = (Line.indexOf(Field) + (Field.length()) + 2);
									int end = Line.length();
									String outputValue = (Line.substring(start, end).trim().replace("\"", ""));
									if (outputValue.equalsIgnoreCase(Value)) {
										m = m + 1;
									} else {
										n = n + 1;
										}
								}
							}
						//}
					fis.close();
					if(FieldToVerify.equalsIgnoreCase("codeValue"))
					{
						if (m > 0) {
							System.out.println("Successfully Verified " + ErrorType + " Error Codes in Response JSON file for " + JSONFile + ": " + " for " + " Error Code" + "=" + Value );
							
							this.context.addResult("Verify " + ErrorType + " Error Codes for the WebService  " + JSONFile + ": " + "for " + " " + ErrorType + " Error Code" + "=" + Value, "Verify Response for the WebService  " + JSONFile + ": " + "for " + "Error Code" + "=" + Value, "Error Code" + "=" + Value, "Successfully Verified Response For  " + "Error Code" + "=" + Value , RunStatus.PASS);
						} else {
							System.out.println("Verification of Response JSON file for " + JSONFile + ": " + " for " +"\n" +  ErrorType + " Error Code" + "=" + Value  +"\n" + " FAILED" );
							this.context.addResult("Verify Error Codes for the WebService  " + JSONFile + ": " + "for " + ErrorType + " Error Code" + "=" + Value, "Verify Response for the WebService" + JSONFile, "Error Code" + "=" + Value, "Verification of Response JSON file for " + JSONFile + ": " + " for " + "Error Code" + "=" + Value + " FAILED" , RunStatus.FAIL);
						}
					}
					if(FieldToVerify.equalsIgnoreCase("messageTxt"))
					{
						if (m > 0) {
							System.out.println("Successfully Verified " + ErrorType + " Error Descriptions in Response JSON file for " + JSONFile + ": " + " for " + "Error Description" + "=" + Value );
							
							this.context.addResult("Verify " + ErrorType + " Error Descriptions for the WebService  " + JSONFile + ": " + "for " + ErrorType + " Error Description" + "=" + Value, "Verify Response for the WebService  " + JSONFile + ": " + "for " + "Error Description" + "=" + Value, "Error Description" + "=" + Value, "Successfully Verified Response For  " + "Error Description" + "=" + Value , RunStatus.PASS);
						} else {
							System.out.println("Verification of Response JSON file for " + JSONFile + ": " + " for " +"\n" +  ErrorType +  " Error Description" + "=" + Value  +"\n" + " FAILED" );
							this.context.addResult("Verify Error Descriptions for the WebService  " + JSONFile + ": " + "for " + ErrorType + " Error Description" + "=" + Value, "Verify Response for the WebService" + JSONFile, "Error Description" + "=" + Value, "Verification of Response JSON file for " + JSONFile + ": " + " for " + "Error Description" + "=" + Value + " FAILED" , RunStatus.FAIL);
						}
					}
						
					}
					}

				else{
					this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, "Error Code" +"="+Value, "File not found", RunStatus.FAIL);
				}
				
			}
			catch(Exception e){
				this.context.addResult("Verify Response for the WebService", "Verify Response for the WebService"+JSONFile, "Error Code" +"="+Value, "Found an exception in executing step", RunStatus.FAIL);
				e.printStackTrace();
			}
}
	

@Then("^WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response \"([^\"]*)\" JSON file with input excel sheet = \"([^\"]*)\" for row \"([^\"]*)\" data$")
public void verifyErrorCodes_in_json_response__with_input_excel(String propertiesFileKey ,String MyInputDataSheet , String RowIndex ){
	
	int j = context.STestName.lastIndexOf(".");
	if (j == -1)
	{
		this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
	}
	String USname = context.STestName.substring(0,j);

	this.context.InputDataSheet = MyInputDataSheet;
	this.context.datamap = DataHelper.data(System.getProperty("user.dir") + PropertiesFile.GetEnvironmentProperty("TESTDATA_EXCEL_PATH") +  this.context.InputDataFilePath ,context.InputDataSheet);
     String printErrorCode;
	
     List<String> ErrorCodesListToVerify = new ArrayList<String>();
     List<String> ErrorDescListToVerify = new ArrayList<String>();
     List<String> DevErrorCodesListToVerify = new ArrayList<String>();
     List<String> DevErrorDescListToVerify = new ArrayList<String>();
    
     if(RowIndex.equalsIgnoreCase("ALL"))
	{
	       HashMap datamap1 = context.datamap.get(0);
	       int totalRows = ((List)datamap1.get(datamap1.keySet().toArray()[0])).size();
	          for (int i = 0; i < totalRows; i++) {
		           System.out.println("\n ==> Working with Data Row----:"+(i+2));
                   String rowIndex2 = Integer.toString(i+2);
                   
                   String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\"+propertiesFileKey + "_" + context.strScenarioRunId + "_Data_Row_" + rowIndex2 + ".json";
                   
                   String[] errorCodes = context.datamap.get(0).get("UserErrorCodes").get(i).split("#");
                   String[] errorDesc = context.datamap.get(0).get("UserErrorDesc").get(i).split("#");
                   String[] deverrorCodes = context.datamap.get(0).get("DevErrorCodes").get(i).split("#");
                   String[] deverrorDesc = context.datamap.get(0).get("DevErrorDesc").get(i).split("#");
                                     
                   for (int m=0; m<errorCodes.length; m++)
                   {
                	   ErrorCodesListToVerify.add(errorCodes[m]);
                   }
                   for (int n=0; n<errorDesc.length; n++)
                   {
                	   ErrorDescListToVerify.add(errorDesc[n]);
                   }
                   for (int k=0; k<deverrorCodes.length; k++)
                   {
                	   DevErrorCodesListToVerify.add(deverrorCodes[k]);
                   }
                   for (int l=0; l<deverrorDesc.length; l++)
                   {
                	   DevErrorDescListToVerify.add(deverrorDesc[l]);
                   }
                             				
					if(ErrorCodesListToVerify.size() > 0){
						// iterate the Error Codes list
						System.out.println("\n==> iterate the User Error Codes list - Here are the Error Codes needs to verified : ");
						for (String errorCode : ErrorCodesListToVerify) {
							System.out.println(errorCode);
						}
						System.out.println("Start Verifying for Error Codes in ===>" +FilePath);
						verifyResponseFile( "User" , propertiesFileKey , FilePath , "codeValue" , ErrorCodesListToVerify );
					}
					else{
						this.context.addResult("Verify Data For JSON Files Input File", "Verify Response for the WebService"+propertiesFileKey, " User Error Codes are not provided in Input excel sheet", "No Error codes provided in Input Excel Sheet", RunStatus.FAIL);
						}
									
		           if(ErrorDescListToVerify.size() > 0){
		       		// iterate the Error Descriptions list
		   			System.out.println("\n==> iterate the User Error Descriptions list - Here are the Error Descriptions needs to verified : ");
		   			     for (String errorCode : ErrorDescListToVerify) {
		   				System.out.println(errorCode);
		   			}
		   			  System.out.println("Start Verifying for Error Descriptions in ===>" +FilePath);
		   			     verifyResponseFile( "User" , propertiesFileKey , FilePath , "messageTxt" , ErrorDescListToVerify );
					}
					else{
						this.context.addResult("Verify Data For JSON Files Input File", "Verify Response for the WebService"+propertiesFileKey, " User Error Descriptions are not provided in Input excel sheet", "No Error Descriptions provided in Input Excel Sheet", RunStatus.FAIL);
					}

		       	if(DevErrorCodesListToVerify.size() > 0){
					// iterate the Error Codes list
					System.out.println("\n==> iterate the Developer Error Codes list - Here are the Error Codes needs to verified : ");
					for (String errorCode : ErrorCodesListToVerify) {
						System.out.println(errorCode);
					}
					System.out.println("Start Verifying for Error Codes in ===>" +FilePath);
					verifyResponseFile( "User" , propertiesFileKey , FilePath , "codeValue" , DevErrorCodesListToVerify );
				}
				else{
					this.context.addResult("Verify Data For JSON Files Input File", "Verify Response for the WebService"+propertiesFileKey, "Developer Error Codes are not provided in Input excel sheet", "No Error codes provided in Input Excel Sheet", RunStatus.FAIL);
				}
								
	           if(DevErrorDescListToVerify.size() > 0){
	       		// iterate the Error Descriptions list
	   			System.out.println("\n==> iterate the Developer Error Descriptions list - Here are the Error Descriptions needs to verified : ");
	   			     for (String errorCode : ErrorDescListToVerify) {
	   				System.out.println(errorCode);
	   			}
	   			  System.out.println("Start Verifying for Error Descriptions in ===>" +FilePath);
	   			     verifyResponseFile( "User" , propertiesFileKey , FilePath , "messageTxt" , DevErrorDescListToVerify );
				}
				else{
					this.context.addResult("Verify Data For JSON Files Input File", "Verify Response for the WebService"+propertiesFileKey, "Developer Error Descriptions are not provided in Input excel sheet", "No Error Descriptions provided in Input Excel Sheet", RunStatus.FAIL);
				}
		           
				   ErrorCodesListToVerify.clear();
		           ErrorDescListToVerify.clear();
		           DevErrorCodesListToVerify.clear();
		           DevErrorDescListToVerify.clear();
                   
			  }
	}
	else{
		
		   int DataRowIndex = Integer.parseInt(RowIndex)-2; // Modified after implementation of JSON w/excel data
		   String FilePath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH")+ USname+ "\\"+propertiesFileKey + "_" + context.strScenarioRunId + "_Data_Row_" + RowIndex + ".json";
		   System.out.println("\n ==> Working with Data Row----:"+RowIndex);
           
		   if(context.datamap.get(0).get("UserErrorCodes").get(DataRowIndex) != null){
			   String[] errorCodes = context.datamap.get(0).get("UserErrorCodes").get(DataRowIndex).split("#");
			   for (int m=0; m<errorCodes.length; m++)
	           {
	        	   ErrorCodesListToVerify.add(errorCodes[m]);
	           }
		   }
		   else{
			   ErrorCodesListToVerify = null;
		   }
		   if(context.datamap.get(0).get("UserErrorDesc").get(DataRowIndex) != null){
			   String[] errorDesc = context.datamap.get(0).get("UserErrorDesc").get(DataRowIndex).split("#");
			   for (int n=0; n<errorDesc.length; n++)
	           {
	        	   ErrorDescListToVerify.add(errorDesc[n]);
	           }
		   }
		   else{
			   ErrorDescListToVerify = null;
		   }
		   if(context.datamap.get(0).get("DevErrorCodes").get(DataRowIndex) != null){
			   String[] deverrorCodes = context.datamap.get(0).get("DevErrorCodes").get(DataRowIndex).split("#");
			   for (int k=0; k<deverrorCodes.length; k++)
	           {
	        	   DevErrorCodesListToVerify.add(deverrorCodes[k]);
	           }
		   }
		   else{
			   DevErrorCodesListToVerify = null;
		   }
		  
		   if(context.datamap.get(0).get("DevErrorDesc").get(DataRowIndex) != null ){
			   String[] deverrorDesc = context.datamap.get(0).get("DevErrorDesc").get(DataRowIndex).split("#");
			   for (int l=0; l<deverrorDesc.length; l++)
	           {
	        	   DevErrorDescListToVerify.add(deverrorDesc[l]);
	           }
		   }
		   else{
			   DevErrorDescListToVerify = null;
		   }
                     
                     				
			if(ErrorCodesListToVerify != null){
				// iterate the Error Codes list
				System.out.println("\n==> iterate the User Error Codes list - Here are the Error Codes needs to verified : for Data Row ---"+RowIndex);
				for (String errorCode : ErrorCodesListToVerify) {
					System.out.println(errorCode);
				}
				System.out.println("Start Verifying for Error Codes in ===>" +FilePath);
				verifyResponseFile( "User" , propertiesFileKey , FilePath , "codeValue" , ErrorCodesListToVerify );
			}
			else{
				this.context.addResult("Verify Data For JSON Files Input File", "Verify Response for the WebService"+propertiesFileKey, " User Error Codes are not provided in Input excel sheet", "No Error codes provided in Input Excel Sheet", RunStatus.FAIL);
				}
							
           if(ErrorDescListToVerify != null){
       		// iterate the Error Descriptions list
   			System.out.println("\n==> iterate the User Error Descriptions list - Here are the Error Descriptions needs to verified : for Data Row ---"+RowIndex);
   			     for (String errorCode : ErrorDescListToVerify) {
   				System.out.println(errorCode);
   			}
   			     System.out.println("Start Verifying for Error Descriptions in ===>" +FilePath);
			     verifyResponseFile( "User" , propertiesFileKey , FilePath , "messageTxt" , ErrorDescListToVerify );
		}
		else{
			this.context.addResult("Verify Data For JSON Files Input File", "Verify Response for the WebService"+propertiesFileKey, " User Error Descriptions are not provided in Input excel sheet", "No Error Descriptions provided in Input Excel Sheet", RunStatus.FAIL);
		}

			if(DevErrorCodesListToVerify != null){
				// iterate the Error Codes list
				System.out.println("\n==> iterate the Developer Error Codes list - Here are the Error Codes needs to verified : for Data Row ---"+RowIndex);
				for (String errorCode : DevErrorCodesListToVerify) {
					System.out.println(errorCode);
				}
				System.out.println("Start Verifying for Error Codes in ===>" +FilePath);
				verifyResponseFile( "Developer" , propertiesFileKey , FilePath , "codeValue" , DevErrorCodesListToVerify );
			}
			else{
				this.context.addResult("Verify Data For JSON Files Input File", "Verify Response for the WebService"+propertiesFileKey, " Developer Error Codes are not provided in Input excel sheet", "No Error codes provided in Input Excel Sheet", RunStatus.FAIL);
				}
							
           if(DevErrorDescListToVerify != null){
       		// iterate the Error Descriptions list
   			System.out.println("\n==> iterate the Developer Error Descriptions list - Here are the Error Descriptions needs to verified : for Data Row ---"+RowIndex);
   			     for (String errorCode : DevErrorDescListToVerify) {
   				System.out.println(errorCode);
   			}
   			     System.out.println("Start Verifying for Error Descriptions in ===>" +FilePath);
			     verifyResponseFile( "Developer" , propertiesFileKey , FilePath , "messageTxt" , DevErrorDescListToVerify );
		}
		else{
			this.context.addResult("Verify Data For JSON Files Input File", "Verify Response for the WebService"+propertiesFileKey, " Developer Error Descriptions are not provided in Input excel sheet", "No Error Descriptions provided in Input Excel Sheet", RunStatus.FAIL);
		}
          		
	}
	
}

	/*Created by Mahesh : To compare output JSON file with Baseline JSON file 
	 *
	 * This step will compare output JSON file with Baseline JSON file 
	 * parameters :
	 * 		outputFileName - Output response file which will have the output from GET/ADD/UPDATE/DELETE services
	 * 		Baselinefilename - Baseline response JSON file from baseline folder for given feature 
	 */
	@Then("^WEBSERVICES:Compare \"(.*)\" JSON file with \"(.*)\" JSON file$")
	public void CompareJsonFilewithBaseline(String outputFileName, String Baselinefilename ) {

		List<String> ignoreTags = new ArrayList<String>();
		CompareJsonFilewithBaselineAndIgnoreTags(outputFileName ,Baselinefilename , ignoreTags );

	}

	/*Created by Mahesh : To compare output JSON file with Baseline JSON file and also ignore the tags from comparison
	 *
	 * This step will compare output JSON file with Baseline JSON file and also ignore the tags from comparison
	 * parameters :
	 * 		outputFileName - Output response file which will have the output from GET/ADD/UPDATE/DELETE services
	 * 		Baselinefilename - Baseline response JSON file from baseline folder for given feature 
	 *      ignoreTags - it is list of tags which will be ignored from comparison process
	 *                   |data.output.generalDeductionConfiguration.itemID|
	 *                   |data.output.generalDeductionConfiguration.deductionCode.codeValue|
	 */
	@Then("^WEBSERVICES:Compare \"([^\"]*)\" JSON file with \"([^\"]*)\" JSON file and ignore these tags$")
	public void CompareJsonFilewithBaselineAndIgnoreTags(String outputFileName, String Baselinefilename , List<String> ignoreTags) {

		System.out.println("**************** Tags to be ignored in the comparison of response with baseline **********************");
		for (String tags : ignoreTags) {
				System.out.println(tags.toString());
				this.context.ignoreTags.add(tags.toString());
		}
		System.out.println("**************** Above Tags will be ignored in the comparison of response with baseline **********************");

		DataObject newObj = new DataObject();
		newObj.setServiceName(outputFileName);
		newObj.setEnvironment(ExecutionContext.SRunAgainst);
		int index = dataObjects.indexOf(newObj);
		
		if(index == -1) {
			this.context.addResult("Reading data file ", "Reading data file ", "Invalid Service Name " + outputFileName, " Serivce Name does not exists in data file ",  RunStatus.FAIL);
			System.exit(0);
		}
		
		try{

			
			StringBuilder str = new StringBuilder();
			StringBuilder str2 = new StringBuilder();
			int j = context.STestName.lastIndexOf(".");
			if (j == -1)
			{
				this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
			}
			String USname = context.STestName.substring(0,j);

            for (DataObject obj : dataObjects) {
				
				if(newObj.equals(obj)){
				DataObject actualObj = obj;
			
			String ResponsePath = System.getProperty("user.dir")+ PropertiesFile.GetEnvironmentProperty("RESPONSE_PATH");
			String strJSONBaselineFileName = ResponsePath + USname + "\\Baseline\\" +  Baselinefilename + ".json";
			//String strJSONOutputFileName = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname + "\\" + outputFileName + "_" + context.strScenarioRunId  + "_DataRow_" + (actualObj.getRowNum()+1)  + ".json";
			String strJSONOutputFileName = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname + "\\" + "GetBenefitEvents_SCENARIO_20160405_102741_750"  + ".json";
									
			File file = new File (strJSONBaselineFileName);
			if (!file.exists()){
				this.context.addResult("Compare Baseline File ", "Baseline File Not Found for " + strJSONBaselineFileName , "Baseline File Not Found for " + strJSONBaselineFileName  , "Skipping baseline comparison",  RunStatus.FAIL);
				System.out.println("Baseline File Not Found for " + strJSONBaselineFileName + ". Skipping baseline comparison");
				str.append(strJSONBaselineFileName + "	" + "Baseline File Not Found for " + strJSONOutputFileName + ". Skipping baseline comparison");
				str.append(System.getProperty("line.separator"));
			}

			/* copy baseline and output file to updated folder*/
			String strUpdatedPath = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname + "\\" +"Updated";
			File strUpdatedFolder = new File(strUpdatedPath);

			if(!strUpdatedFolder.exists()){
				strUpdatedFolder.mkdir();
			}

			//String strUpdatedOutputFileName = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname + "\\" +"Updated\\" +  outputFileName  + "_" + context.strScenarioRunId + "_DataRow_" + (actualObj.getRowNum()+1) + ".json";
			String strUpdatedOutputFileName = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname + "\\" +"Updated\\" + "GetBenefitEvents_SCENARIO_20160405_102741_750" + ".json";
			String strUpdatedBaselineFileName = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname + "\\" + "Updated\\" +  Baselinefilename + ".json";

			File strOutUpdatedfile = new File (strUpdatedOutputFileName);
			File strBaselineUpdatedFile = new File (strUpdatedBaselineFileName);

			if (strOutUpdatedfile.exists())
				strOutUpdatedfile.delete();
			if (strBaselineUpdatedFile.exists())
				strBaselineUpdatedFile.delete();

			FileUtils.copyFile(new File(strJSONOutputFileName), strOutUpdatedfile);
			FileUtils.copyFile(new File(strJSONBaselineFileName), strBaselineUpdatedFile);

			System.out.println("\n\n Starting Baseline file comparison for output file : " +strJSONOutputFileName);
			
			AbstractComparator comparator=new AbstractComparator();
			String actual = FileOperations.GetStringContentFromJsonFile(context ,strUpdatedOutputFileName );
			String expected = FileOperations.GetStringContentFromJsonFile(context ,strUpdatedBaselineFileName );
			JSONCompareResult testResult	=	JSONUtility.assertEquals(expected,actual,comparator,context);

			/* ObjectMapper om = new ObjectMapper();
		   
		            Map<String, Object> m1 = (Map<String, Object>)(om.readValue(actual, Map.class));
		            Map<String, Object> m2 = (Map<String, Object>)(om.readValue(expected, Map.class));
		            System.out.println(m1);
		            System.out.println(m2);
		            System.out.println(m1.equals(m2));*/
			
		           // assertEquals(om.readValue(expected, new TypeReference<HashMap<String, Object>>(){}), om.readValue(actual, new TypeReference<HashMap<String, Object>>(){}));
		            //net.javacrumbs.jsonunit.core.Option.IGNORING_VALUES = "";
		          //below is new implementation 
		            //assertJsonEquals(expected , actual );
		         		            
			if(testResult.getMessage() == null || testResult.getMessage().trim().length() == 0)
			{
				System.out.println("Baseline file comparison Passed");
				this.context.addResult("Baseline file comparison ", "Baseline file comparison PASSED", "Baseline file comparison should match", "Output json file matches baseline for api call " + outputFileName, RunStatus.PASS);
			}
			else
			{
				System.out.println("Baseline file comparison FAILED with " + testResult.getFieldFailures().size() + " Mismatches");
				System.out.println(testResult.getMessage());
				this.context.addResult("Baseline file comparison ", "Baseline file comparison FAILED with " + testResult.getFieldFailures().size() + " Mismatches", "Baseline file comparison should match ", "Baseline file compare failed for " + outputFileName + "\t\n\n\n" + testResult.getMessage() , RunStatus.FAIL);
			}

				}
}
		}
		
		catch (Exception|AssertionError e){
			System.out.println("Exception occured while executing file comparison method");
			System.out.println(e.getMessage());
			this.context.addResult("Baseline file comparison ", "Baseline file comparison FAILED", "Baseline file comparison FAILED", "Baseline file compare failed for \n" + outputFileName + "\t\n\n\n" + e.getMessage(), RunStatus.FAIL);
			//e.printStackTrace();

		}

	}
	
	/*public com.google.gson.JsonArray events;
	public com.google.gson.JsonObject eventsObject;*/
	public String returnedEntity;
	public ObjectMapper mapper = new ObjectMapper();
	public boolean firstTime = true;
	private String eventName;
//	public String inputJSONFile;
	private List<JSONData> jsonDataList = new ArrayList<JSONData>();
	
    @Given("^WEBSERVICES:We create a new API request for \"([^\"]*)\" for ClassName \"([^\"]*)\" with input excel sheet = \"([^\"]*)\" for row \"([^\"]*)\" data$")
    public void createAPIrequestForservice(String propertiesFileKey,  String pojoClassName ,String MyInputDataSheet , String RowIndex)  {
    	
    	String ClassName = pojoClassName;
    	try{

			DataObject newObj = new DataObject();
			newObj.setServiceName(propertiesFileKey);
			newObj.setEnvironment(ExecutionContext.SRunAgainst);
			int index = dataObjects.indexOf(newObj);
			this.context.InputDataSheet = MyInputDataSheet;
			this.context.datamap = DataHelper.data(System.getProperty("user.dir") + PropertiesFile.GetEnvironmentProperty("TESTDATA_EXCEL_PATH") +  this.context.InputDataFilePath ,context.InputDataSheet);
			int j = context.STestName.lastIndexOf(".");
			if (j == -1)
			{
				this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
			}
			String USname = context.STestName.substring(0,j);
			String InputJsonPath = PropertiesFile.GetEnvironmentProperty("TEMP_INPUT_PATH");

			if(index == -1) {
				this.context.addResult("Reading data file ", "Reading data file ", "Invalid Service Name " + propertiesFileKey, " Serivce Name does not exists in data file ",  RunStatus.FAIL);
				System.exit(0);
			}

			File tempFolder = new File (PropertiesFile.GetEnvironmentProperty("TEMP_INPUT_PATH") + USname );

			if(!tempFolder.exists()){
				tempFolder.mkdir();
			}


			if(RowIndex.equalsIgnoreCase("ALL"))
			{

				HashMap datamap1 = context.datamap.get(0);
				int totalRows = ((List)datamap1.get(datamap1.keySet().toArray()[0])).size();
				for (int i = 1; i <= totalRows; i++) {
				//	System.out.println(i);
                    String rowIndex2 = Integer.toString(i+1);
                    JSONData jsonData = new JSONData();
					
                     String updatedinputJson = InputJsonPath + USname + "\\" + propertiesFileKey + "_" + context.strScenarioRunId +  "_Data_Row_" + rowIndex2 + "_POST.json";
//     			    inputJSONFile = updatedinputJson;//mahesh
                     jsonData.setInputJSONFile(updatedinputJson);
     			   jsonData.setIndex(i-1);
     			    CreateAJSONWithInputData(context ,updatedinputJson ,i , ClassName, jsonData);
     			    jsonDataList.add(jsonData);
				}

			}
			else
			{
			
		      int DataRowIndex = Integer.parseInt(RowIndex)-1;
				if(index != -1) {
				JSONData jsonData = new JSONData();
			    String updatedinputJson = InputJsonPath + USname + "\\" + propertiesFileKey + "_" + context.strScenarioRunId +  "_Data_Row_" + RowIndex + "_POST.json";
//			    inputJSONFile = updatedinputJson;
			    jsonData.setInputJSONFile(updatedinputJson);
			    jsonData.setIndex(0);
			    CreateAJSONWithInputData(context ,updatedinputJson ,DataRowIndex , ClassName, jsonData);
			    
			    jsonDataList.add(jsonData);
			
			}
    	}
    	}	
		catch (Exception e)
    	{
		   e.printStackTrace();
		}
				
   }

	//@And("^WEBSERVICES:Create an array and add records using excel data for event (\\d+)") 
	//@And("^WEBSERVICES:Create an array and add records using excel data for event (\\d+) for row \"([^\"]*)\" data$")
	
	@And("^WEBSERVICES:Create an array and add records using excel data for event (\\d+) for row \"([^\"]*)\" data$")
	public void addAnArrayElementToAnEventAndRecordsUsingModels(int eventNbr , String RowIndex, DataTable table  ) {

		List<Map<String, String>> data = table.asMaps(String.class, String.class);
		int rowIndexNum = -1 ;
		try {
			rowIndexNum = Integer.valueOf(RowIndex);
		} catch(Exception e) {
		}
		String ArrayName = "";
		String SpreadSheet = "";
		
		for (int t = 0; t < data.size(); t++) {

			ArrayName = data.get(t).get("ArrayName");
			SpreadSheet = data.get(t).get("DataForArray");			
			
			Map<Integer,Map<String, List<DataModel>>> datamap = DataHelper.dataModels(System.getProperty("user.dir") + PropertiesFile.GetEnvironmentProperty("TESTDATA_EXCEL_PATH") +  this.context.InputDataFilePath ,SpreadSheet);
			String arrayNum = "-1";
			try {
				arrayNum = ArrayName.substring(ArrayName.indexOf("[") + 1, ArrayName.indexOf("]"));
			} catch(Exception e) {
//				e.printStackTrace();
			}
			
		//	for (int arrayNum = 0; t < data.size(); t++)
			
			int i = 0;
			for (Map.Entry<Integer, Map<String, List<DataModel>>> entries : datamap.entrySet()) {
				List<DataModel> dataList = null;
				boolean innerArray = false;
				int rowNum = -1;
				Map<String, List<DataModel>> entryMap = entries.getValue();
				for (Map.Entry<String, List<DataModel>> map : entryMap.entrySet()) {
					String key = map.getKey();
					
					String[] tabs = key.split("_"); 
					rowNum = Integer.valueOf(tabs[0]); //existing code
					//rowNum = Integer.valueOf((tabs[0]).split(",")[0]);
					if(tabs != null && tabs.length > 1) {
						innerArray = true;
					}					
					if(!innerArray && entries.getKey() == rowIndexNum) {
						dataList = map.getValue();
					} else if(innerArray) {
						if(tabs.length == 2) {
							String tab1 = tabs[0];
//							String tab2 = tabs[1];
							
							String[] rowIds = tab1.split(",");
							
							for (String rowId : rowIds) {								
								if(rowIndexNum == -1) {
									dataList = map.getValue();
								} else if(Integer.valueOf(rowId) == rowIndexNum) {
									dataList = map.getValue();
								}
							}
							
						} else if(tabs.length == 3) {
							String tab1 = tabs[0];
							String tab2 = tabs[1];
//							String tab3 = tabs[2];
							
							String[] rowIds = tab1.split(",");
							
							String[] arrayIds = tab2.split(",");
							
							for (String rowId : rowIds) {								
								for (String arrayId : arrayIds) {
									if(rowIndexNum == -1 && arrayNum != null && Integer.valueOf(arrayId) == (Integer.valueOf(arrayNum) +1 )) {
										dataList = map.getValue();
									} else if(Integer.valueOf(rowId) == rowIndexNum && arrayNum != null && Integer.valueOf(arrayId) == (Integer.valueOf(arrayNum) +1 )) {
										dataList = map.getValue();
									}
								}
								
							}
							
						}
					}
				}					
				
				
				if(dataList != null) {
					if(rowIndexNum == -1) {
						if(rowNum > -1) {
							addARecordToAnArrayInEventUsingModels(ArrayName, eventNbr, dataList, i, jsonDataList.get(rowNum -2), Integer.valueOf(arrayNum));
						} else {
							addARecordToAnArrayInEventUsingModels(ArrayName, eventNbr, dataList, i, jsonDataList.get(i), Integer.valueOf(arrayNum));
						}
						
					} else {
						addARecordToAnArrayInEventUsingModels(ArrayName, eventNbr, dataList, i, jsonDataList.get(0), Integer.valueOf(arrayNum));
					}
					
				}
				
				i++;
			}
			this.context.addResult("Add :" +ArrayName +" Array to input JSON request ", "Successfully Added Array : " +ArrayName + "to input JSON request", "Add Array to input JSON request ", "Successfully Added Array : " +ArrayName + "to input JSON request " , RunStatus.PASS);	 
		}
	}
	
	
	
	public void addAnArrayElementToAnEventAndRecordsUsingModels_Old(int eventNbr , String RowIndex, DataTable table  ) {

		List<Map<String, String>> data = table.asMaps(String.class, String.class);
		int rowIndexNum = -1 ;
		try {
			rowIndexNum = Integer.valueOf(RowIndex);
		} catch(Exception e) {
		}
		String ArrayName = "";
		String SpreadSheet = "";
		
		for (int t = 0; t < data.size(); t++) {

			ArrayName = data.get(t).get("ArrayName");
			SpreadSheet = data.get(t).get("DataForArray");			
			
			Map<Integer,Map<String, List<DataModel>>> datamap = DataHelper.dataModels(System.getProperty("user.dir") + PropertiesFile.GetEnvironmentProperty("TESTDATA_EXCEL_PATH") +  this.context.InputDataFilePath ,SpreadSheet);
			String arrayNum = "-1";
			try {
				arrayNum = ArrayName.substring(ArrayName.indexOf("[") + 1, ArrayName.indexOf("]"));
			} catch(Exception e) {
//				e.printStackTrace();
			}
			
		//	for (int arrayNum = 0; t < data.size(); t++)
			
			int i = 0;
			for (Map.Entry<Integer, Map<String, List<DataModel>>> entries : datamap.entrySet()) {
				List<DataModel> dataList = null;
				boolean innerArray = false;
				int rowNum = -1;
				Map<String, List<DataModel>> entryMap = entries.getValue();
				for (Map.Entry<String, List<DataModel>> map : entryMap.entrySet()) {
					String key = map.getKey();
					
					String[] tabs = key.split("_"); 
					rowNum = Integer.valueOf(tabs[0]); //existing code
					//rowNum = Integer.valueOf((tabs[0]).split(",")[0]);
					if(tabs != null && tabs.length > 1) {
						innerArray = true;
					}					
					if(!innerArray && entries.getKey() == rowIndexNum) {
						dataList = map.getValue();
					} else if(innerArray) {
						if(tabs.length == 2) {
							String tab1 = tabs[0];
//							String tab2 = tabs[1];
							
							String[] rowIds = tab1.split(",");
							
							for (String rowId : rowIds) {								
								if(rowIndexNum == -1) {
									dataList = map.getValue();
								} else if(Integer.valueOf(rowId) == rowIndexNum) {
									dataList = map.getValue();
								}
							}
							
						} else if(tabs.length == 3) {
							String tab1 = tabs[0];
							String tab2 = tabs[1];
//							String tab3 = tabs[2];
							
							String[] rowIds = tab1.split(",");
							
							String[] arrayIds = tab2.split(",");
							
							for (String rowId : rowIds) {
								for (String arrayId : arrayIds) {
									if(rowIndexNum == -1 && arrayNum != null && Integer.valueOf(arrayId) == (Integer.valueOf(arrayNum) +1 )) {
										dataList = map.getValue();
									} else if(Integer.valueOf(rowId) == rowIndexNum && arrayNum != null && Integer.valueOf(arrayId) == (Integer.valueOf(arrayNum) +1 )) {
										dataList = map.getValue();
									}
								}
								
							}
							
						}
					}
				}					
				
				
				if(dataList != null) {
					if(rowIndexNum == -1) {
						if(rowNum > -1) {
							addARecordToAnArrayInEventUsingModels(ArrayName, eventNbr, dataList, i, jsonDataList.get(rowNum -2), Integer.valueOf(arrayNum));
						} else {
							addARecordToAnArrayInEventUsingModels(ArrayName, eventNbr, dataList, i, jsonDataList.get(i), Integer.valueOf(arrayNum));
						}
						
					} else {
						addARecordToAnArrayInEventUsingModels(ArrayName, eventNbr, dataList, i, jsonDataList.get(0), Integer.valueOf(arrayNum));
					}
					
				}
				
				i++;
			}
			this.context.addResult("Add :" +ArrayName +" Array to input JSON request ", "Successfully Added Array : " +ArrayName + "to input JSON request", "Add Array to input JSON request ", "Successfully Added Array : " +ArrayName + "to input JSON request " , RunStatus.PASS);	 
		}
	}
	
	
	public void CreateAJSONWithInputData(ExecutionContext context, String OutputJsonFileName ,Integer DataRowIndex , String pojoClassName, JSONData jsonData) throws IOException {

		Integer index = DataRowIndex-1;
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String now = new SimpleDateFormat("yyD-HHmm").format(new Date());
        JsonObject event = new JsonObject();
		String eventClassName =  pojoClassName;  //"com.adp.ea.payroll.v1.events.memoconfiguration.add.MemoConfigurationAddEvent";
		
		//this.eventName = eventName.replace("\"", "");
		com.google.gson.JsonArray events = new JsonArray();
		com.google.gson.JsonObject eventsObject = new JsonObject();

        eventsObject.add("events", events);
        
        HashMap datamap = context.datamap.get(0);
        try {
        	 Class<?> clazz = Class.forName(eventClassName.replace("\"", ""));
        	  String val;
              String[] nodes;
              JsonObject currentNode;
           
              // set parameters in the request 
           
          	Set<String> keySet = datamap.keySet();
			boolean isDataMapKey = false;
			currentNode = event;
			for (String entry : keySet) {
				if(entry.toString().contains("TestDescription") ||  entry.toString().contains("UserErrorCodes")  || entry.toString().contains("UserErrorDesc")  || entry.toString().contains("DevErrorCodes") || entry.toString().contains("DevErrorDesc") )
				{
					//Do nothing 
				}
				else{
				currentNode = event;
                  nodes = entry.replace('~', ',').split(",");  // mahesh commented _ with ~
                  for (int x = 0; x < nodes.length - 1; x++) {
                      if (currentNode.has(nodes[x])) {
                          currentNode = currentNode.getAsJsonObject(nodes[x]);
                      } else {
                          currentNode.add(nodes[x], new JsonObject());
                          currentNode = currentNode.getAsJsonObject(nodes[x]);
                      }
                  }
                  val = ((List)datamap.get(entry)).get(index).toString().replace("$today", today).replace("$now", now);
/*                 if(val.equalsIgnoreCase("*BLANK*")){
					 currentNode.addProperty(nodes[nodes.length - 1], "");
				 }else{
					 currentNode.addProperty(nodes[nodes.length - 1], val);
				 }*/

              	String[] Values = val.split(",");
              
                  if (nodes[nodes.length - 1].endsWith("s")) {
                      if (currentNode.has(nodes[nodes.length - 1])) {
                         // currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(val));
                      	for (String str: Values) {
                      		currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(str));
          				}
                      } else {
                          currentNode.add(nodes[nodes.length - 1], new JsonArray());
                         // currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(val));
                          for (String str: Values) {
                          	currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(str));
                          }
                      }
                  } else {
                	  if(val.equalsIgnoreCase("*BLANK*")){
     					 currentNode.addProperty(nodes[nodes.length - 1], "");
     				 }else{
     					 currentNode.addProperty(nodes[nodes.length - 1], val);
     				 }
                  }
                 
                 
                 
              }
			}
			
              //Verify the object generate is a valid string for event type specified
              mapper = new ObjectMapper();
              eventsObject.getAsJsonArray("events").add(event);
//              Object obj = mapper.readValue(eventsObject.toString(), clazz); 
              String Jsonrequest = eventsObject.toString();
              JsonParser parser = new JsonParser();
              String prettyJson;
              JsonObject json = parser.parse(Jsonrequest).getAsJsonObject();
              Gson gson = new GsonBuilder().setPrettyPrinting().create();
              prettyJson = gson.toJson(json);
              jsonData.setEventsObject(eventsObject);
              jsonData.setEvents(events);
             
		
            IOUtils.write(prettyJson, new FileOutputStream(OutputJsonFileName), Charset.defaultCharset());
			
              this.context.addResult("Create API request JSON file ", "Successfully Created API request JSON file", "Create API request JSON file ", "Successfully Created API request JSON file " + OutputJsonFileName, RunStatus.PASS);
		}
		catch (Exception e){
			//this.context.addResult("Create API request JSON file", "Create API request JSON file - Failed", "Create API request JSON file - Failed" ,"Creating JSON request failed with error : " + e.getMessage() , RunStatus.FAIL);
			e.printStackTrace();
		}
	}
	
	public JsonArray addAnArrayElementToAnEvent(String arrayName, int eventNbr, JSONData jsonData) {
      
		if (jsonData.getEventsObject() == null || !jsonData.getEventsObject().has("events") || !jsonData.getEventsObject().get("events").isJsonArray()) {
            fail("An events array has not been created as yet....");
        }
        if (jsonData.getEventsObject().getAsJsonArray("events").size() < eventNbr) {
            fail("The events array does not have " + eventNbr + " events");
        }
        String[] nodes;
        int y, z, rec;
        JsonObject event = (JsonObject) jsonData.getEventsObject().getAsJsonArray("events").get(eventNbr - 1);
        JsonObject currentNode = event;
        nodes = arrayName.replace("\"", "").replace('.', ',').split(",");
        for (int x = 0; x < nodes.length - 1; x++) {
            y = nodes[x].indexOf("[");
            z = nodes[x].indexOf("]");
            if (y > 0 && z > y) {
                rec = Integer.parseInt(nodes[x].substring(y + 1, z));
                nodes[x] = nodes[x].substring(0, y);
            } else {
                rec = -1;
            }
            if (currentNode.has(nodes[x]) && rec > -1) {
                currentNode = (JsonObject) currentNode.getAsJsonArray(nodes[x]).get(rec);
            } else if (currentNode.has(nodes[x])) {
                currentNode = currentNode.getAsJsonObject(nodes[x]);
            } else if (rec > -1) {
                fail("Field " + arrayName + " references an array that does not exist.");
            } else {
                currentNode.add(nodes[x], new JsonObject());
                currentNode = currentNode.getAsJsonObject(nodes[x]);
            }
        }
        if (currentNode.has(nodes[nodes.length - 1]) && !currentNode.get(nodes[nodes.length - 1]).isJsonArray()) {
            fail("The node requested already exists but is not an array.");
        }
        if (!currentNode.has(nodes[nodes.length - 1])) {
            currentNode.add(nodes[nodes.length - 1], new JsonArray());
        }
         return currentNode.getAsJsonArray(nodes[nodes.length - 1]);
    }
	
	public JsonArray addAnArrayElementToAnEvent_Old(String arrayName, int eventNbr, JSONData jsonData) {
	      
		if (jsonData.getEventsObject() == null || !jsonData.getEventsObject().has("events") || !jsonData.getEventsObject().get("events").isJsonArray()) {
            fail("An events array has not been created as yet....");
        }
        if (jsonData.getEventsObject().getAsJsonArray("events").size() < eventNbr) {
            fail("The events array does not have " + eventNbr + " events");
        }
        String[] nodes;
        int y, z, rec;
        JsonObject event = (JsonObject) jsonData.getEventsObject().getAsJsonArray("events").get(eventNbr - 1);
        JsonObject currentNode = event;
        nodes = arrayName.replace("\"", "").replace('.', ',').split(",");
        for (int x = 0; x < nodes.length - 1; x++) {
            y = nodes[x].indexOf("[");
            z = nodes[x].indexOf("]");
            if (y > 0 && z > y) {
                rec = Integer.parseInt(nodes[x].substring(y + 1, z));
                nodes[x] = nodes[x].substring(0, y);
            } else {
                rec = -1;
            }
            if (currentNode.has(nodes[x]) && rec > -1) {
                currentNode = (JsonObject) currentNode.getAsJsonArray(nodes[x]).get(rec);
            } else if (currentNode.has(nodes[x])) {
                currentNode = currentNode.getAsJsonObject(nodes[x]);
            } else if (rec > -1) {
                fail("Field " + arrayName + " references an array that does not exist.");
            } else {
                currentNode.add(nodes[x], new JsonObject());
                currentNode = currentNode.getAsJsonObject(nodes[x]);
            }
        }
        if (currentNode.has(nodes[nodes.length - 1]) && !currentNode.get(nodes[nodes.length - 1]).isJsonArray()) {
            fail("The node requested already exists but is not an array.");
        }
        if (!currentNode.has(nodes[nodes.length - 1])) {
            currentNode.add(nodes[nodes.length - 1], new JsonArray());
        }
         return currentNode.getAsJsonArray(nodes[nodes.length - 1]);
    }
	public void addARecordToAnArrayInEventUsingModels(String arrayName, int eventNbr , List<DataModel> models , int index, JSONData jsonData, int arrayIndex) {
	        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	        String now = new SimpleDateFormat("yyD-HHmm").format(new Date());
	        JsonArray arr = addAnArrayElementToAnEvent(arrayName, eventNbr, jsonData);
	        JsonObject obj = new JsonObject();
	        JsonObject currentNode;
	        String[] nodes = null;
	        String val = null;
	        String[] values;
	       // if (returnedEntity == null || returnedEntity.trim().isEmpty()) {
	         //   fail("You have not identified the type of event as yet...");
	      //  }
         
	        //int index = 0;
//	    	Set<String> keySet = datamap.keySet();
			boolean isDataMapKey = false;
			//int size = datamap.size()
	        // set parameters in the request
	        int y = -1, z = -1, rec =-1;
	        
	        boolean addArray = false;
	     
	         	for (DataModel entry : models) {
		            
		            Object value = entry.getValue();
		            if(value != null && value instanceof String) {
		            	currentNode = obj;
		            	formJson(value, entry.getKey(), currentNode, nodes, y, z, rec, today, now, val);
		            	addArray = true;
		            } else if(value == null && entry.getListModels().size() > 0) {
		            	List<ListModel> listModels = entry.getListModels();
		            	
		            	int arraySize = ((List<String>)listModels.get(0).getValue()).size();
		            	for (int i = 0; i < arraySize; i++) {
		            		JsonObject json = new JsonObject();
		            		currentNode = json;
		            		for (ListModel listModel : listModels) {
		            			int listIndex = 0;
			            		List<String> listValues = (List<String>)listModel.getValue();
			            		
			            		for (String string : listValues) {
			            			if(i == listIndex) {
			            				formJson(string, listModel.getKey(), currentNode, nodes, y, z, rec, today, now, val);
			            			}
			            			
			            			listIndex++;
								}
							}
		            		
		            		arr.add(json);
						}
		            }
		            
		        }
	         	
	         	if(addArray) {
	         		arr.add(obj);
	         	}
	         	
	     
	        try {
	          //  Class<?> clazz = Class.forName("com.adp.ea.payroll.v1.events.memoconfiguration.add.MemoConfigurationAddEvent");
	           // mapper.readValue(eventsObject.toString(), clazz);
	           // ALERTS.debug("Event created <" + eventsObject.toString() + ">");
	            
	            String Jsonrequest = jsonData.getEventsObject().toString();
	             // FileOutputStream fos = new FileOutputStream(OutputJsonFileName);
	             
	              JsonParser parser = new JsonParser();
	              String prettyJson;
	              JsonObject json = parser.parse(Jsonrequest).getAsJsonObject();
	              Gson gson = new GsonBuilder().setPrettyPrinting().create();
	              prettyJson = gson.toJson(json);
	              String FilePath =   jsonData.getInputJSONFile(); 
	              IOUtils.write(prettyJson, new FileOutputStream(FilePath), Charset.defaultCharset());
	            
	             // this.context.addResult("Add Array to input JSON request ", "Successfully Added Array : " +arrayName + "to input JSON request", "Add Array to input JSON request ", "Successfully Added Array : " +arrayName + "to input JSON request and here is the input JSON file " + FilePath, RunStatus.PASS);
	              	              
	        } catch (Exception ex) {
	         //   ALERTS.error("Invalid event <" + eventsObject.toString() + ">");
	            fail("Json generated was not a valid " + returnedEntity + "\n Json generated <" + jsonData.getEventsObject().toString() + ">");
	            ex.printStackTrace();
	        }
	    }
	
	private void formJson(Object value, String key, JsonObject currentNode, String[] nodes, int y, int z, int rec,
			String today, String now, String val) {
		String valueStr = (String) value;
    	nodes = key.replace('~', ',').split(","); //mahesh repaced _ with ~
        for (int x = 0; x < nodes.length - 1; x++) {
            y = nodes[x].indexOf("[");
            z = nodes[x].indexOf("]");
            if (y > 0 && z > y) {
                rec = Integer.parseInt(nodes[x].substring(y + 1, z));			                   
                nodes[x] = nodes[x].substring(0, y);
            } else {
                rec = -1;
            }
            
            if (currentNode.has(nodes[x]) && rec > -1) {
                currentNode = (JsonObject) currentNode.getAsJsonArray(nodes[x]).get(rec);
            } else if (currentNode.has(nodes[x])) {
                currentNode = currentNode.getAsJsonObject(nodes[x]);
            } else if (rec > -1) {
                fail("Field " + valueStr + " references an array that does not exist.");
            } else {
                currentNode.add(nodes[x], new JsonObject());
                currentNode = currentNode.getAsJsonObject(nodes[x]);
            }
        }
       // val =((List)datamap.get(entry)).get(index).toString().replace("$today", today).replace("$now", now);
        val =valueStr.replace("$today", today).replace("$now", now);
    	String[] Values = val.split(",");
    
        if (nodes[nodes.length - 1].endsWith("s")) {
            if (currentNode.has(nodes[nodes.length - 1])) {
               // currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(val));
            	for (String str: Values) {
            		currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(str));
				}
            } else {
                currentNode.add(nodes[nodes.length - 1], new JsonArray());
               // currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(val));
                for (String str: Values) {
                	currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(str));
                }
            }
        } else {
            currentNode.addProperty(nodes[nodes.length - 1], val);
        }
	}
		public void addARecordToAnArrayInEventUsingModels_Old(String arrayName, int eventNbr , List<DataModel> models , int index, JSONData jsonData) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String now = new SimpleDateFormat("yyD-HHmm").format(new Date());
        JsonArray arr = addAnArrayElementToAnEvent(arrayName, eventNbr, jsonData);
        JsonObject obj = new JsonObject();
        JsonObject currentNode;
        String[] nodes;
        String val;
        String[] values;
       // if (returnedEntity == null || returnedEntity.trim().isEmpty()) {
         //   fail("You have not identified the type of event as yet...");
      //  }
     
        //int index = 0;
//    	Set<String> keySet = datamap.keySet();
		boolean isDataMapKey = false;
		//int size = datamap.size()
        // set parameters in the request
        int y, z, rec;
     
         	for (DataModel entry : models) {
	            currentNode = obj;
	            Object value = entry.getValue();
	            if(value instanceof String) {
	            	String valueStr = (String) value;
	            	nodes = entry.getKey().replace('~', ',').split(","); //mahesh repaced _ with ~
		            for (int x = 0; x < nodes.length - 1; x++) {
		                y = nodes[x].indexOf("[");
		                z = nodes[x].indexOf("]");
		                if (y > 0 && z > y) {
		                    rec = Integer.parseInt(nodes[x].substring(y + 1, z));			                   
		                    nodes[x] = nodes[x].substring(0, y);
		                } else {
		                    rec = -1;
		                }
		                
		                if (currentNode.has(nodes[x]) && rec > -1) {
		                    currentNode = (JsonObject) currentNode.getAsJsonArray(nodes[x]).get(rec);
		                } else if (currentNode.has(nodes[x])) {
		                    currentNode = currentNode.getAsJsonObject(nodes[x]);
		                } else if (rec > -1) {
		                    fail("Field " + valueStr + " references an array that does not exist.");
		                } else {
		                    currentNode.add(nodes[x], new JsonObject());
		                    currentNode = currentNode.getAsJsonObject(nodes[x]);
		                }
		            }
		           // val =((List)datamap.get(entry)).get(index).toString().replace("$today", today).replace("$now", now);
		            val =valueStr.replace("$today", today).replace("$now", now);
		        	String[] Values = val.split(",");
		        
		            if (nodes[nodes.length - 1].endsWith("s")) {
		                if (currentNode.has(nodes[nodes.length - 1])) {
		                   // currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(val));
		                	for (String str: Values) {
		                		currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(str));
		    				}
		                } else {
		                    currentNode.add(nodes[nodes.length - 1], new JsonArray());
		                   // currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(val));
		                    for (String str: Values) {
		                    	currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(str));
		                    }
		                }
		            } else {
		                currentNode.addProperty(nodes[nodes.length - 1], val);
		            }
	            }
	            
	        }

        arr.add(obj);
     
        try {
          //  Class<?> clazz = Class.forName("com.adp.ea.payroll.v1.events.memoconfiguration.add.MemoConfigurationAddEvent");
           // mapper.readValue(eventsObject.toString(), clazz);
           // ALERTS.debug("Event created <" + eventsObject.toString() + ">");
            
            String Jsonrequest = jsonData.getEventsObject().toString();
             // FileOutputStream fos = new FileOutputStream(OutputJsonFileName);
             
              JsonParser parser = new JsonParser();
              String prettyJson;
              JsonObject json = parser.parse(Jsonrequest).getAsJsonObject();
              Gson gson = new GsonBuilder().setPrettyPrinting().create();
              prettyJson = gson.toJson(json);
              String FilePath =   jsonData.getInputJSONFile(); 
              IOUtils.write(prettyJson, new FileOutputStream(FilePath), Charset.defaultCharset());
            
             // this.context.addResult("Add Array to input JSON request ", "Successfully Added Array : " +arrayName + "to input JSON request", "Add Array to input JSON request ", "Successfully Added Array : " +arrayName + "to input JSON request and here is the input JSON file " + FilePath, RunStatus.PASS);
              	              
        } catch (Exception ex) {
         //   ALERTS.error("Invalid event <" + eventsObject.toString() + ">");
            fail("Json generated was not a valid " + returnedEntity + "\n Json generated <" + jsonData.getEventsObject().toString() + ">");
            ex.printStackTrace();
        }
    }


	
	
	    @When("^WEBSERVICES:When We run the API request for \"([^\"]*)\" for \"([^\"]*)\" it returns \"([^\"]*)\" status for input excel sheet = \"([^\"]*)\" for row \"([^\"]*)\" data$")
	    public void submitAPIrequestforPOST(String propertiesFileKey, String SucessorFailure, String status, String MyInputDataSheet , String RowIndex)  {
	        
	    	try{

				DataObject newObj = new DataObject();
				newObj.setServiceName(propertiesFileKey);
				newObj.setEnvironment(ExecutionContext.SRunAgainst);
				int index = dataObjects.indexOf(newObj);
				this.context.InputDataSheet = MyInputDataSheet;
				this.context.datamap = DataHelper.data(System.getProperty("user.dir") + PropertiesFile.GetEnvironmentProperty("TESTDATA_EXCEL_PATH") +  this.context.InputDataFilePath ,context.InputDataSheet);

				int j = context.STestName.lastIndexOf(".");
				if (j == -1)
				{
					this.context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
				}
				String USname = context.STestName.substring(0,j);

				String testname = "";

				String Path = System.getProperty("user.dir")+ PropertiesFile.GetEnvironmentProperty("PAYLOAD_PATH");
				String ResponsePath = System.getProperty("user.dir")+ PropertiesFile.GetEnvironmentProperty("RESPONSE_PATH");
				String InputJsonPath = PropertiesFile.GetEnvironmentProperty("TEMP_INPUT_PATH");

				String JSONfilenamestr = "";

				if(index == -1) {
					this.context.addResult("Reading data file ", "Reading data file ", "Invalid Service Name " + propertiesFileKey, " Serivce Name does not exists in data file ",  RunStatus.FAIL);
					System.exit(0);
				}

				DataObject actualObj = dataObjects.get(index);
				String url =  CommonUtility.buildURL(actualObj);
				List<String> valuesList = new ArrayList<String>();

				valuesList.add(actualObj.getServiceName());
				valuesList.add(url);
				valuesList.add(actualObj.getHttpMethod());
				valuesList.addAll(actualObj.getHeaders());
				String[] Values = new String[valuesList.size()];
				int arrayIndex = 0;
				for (String value: valuesList) {
					Values[arrayIndex] = value;
					arrayIndex++;
				}

				if(RowIndex.equalsIgnoreCase("ALL"))
				{

					HashMap datamap1 = context.datamap.get(0);
					int totalRows = ((List)datamap1.get(datamap1.keySet().toArray()[0])).size();
					for (int i = 1; i <= totalRows; i++) {
						//System.out.println(i);
	                     String rowIndex2 = Integer.toString(i+1);
	                    
						ProcessJsonInputFileWithExcelALL(propertiesFileKey ,SucessorFailure , status ,"YES" ,MyInputDataSheet  ,rowIndex2 , Values , actualObj.getServiceName() );
					}

				}
				else
				{
					int DataRowIndex = Integer.parseInt(RowIndex)-1;

					if(index != -1) {
						StringBuilder str = new StringBuilder();
						new StringBuilder();
						WebServiceParameters webServiceParameters = new WebServiceParameters(Values);

						JSONfilenamestr = Path + USname + "\\" + webServiceParameters.getStrTestName() + "_POST.json";
						JSONParser parser = new JSONParser();
						Response res = null;

						testname = webServiceParameters.getStrTestName();
					if (webServiceParameters.getStrRequestType().equals("POST")) {
							String strBody = "";
							File inputJson = new File(Path + USname + "\\" + webServiceParameters.getStrTestName() + "_POST.json");
							String updatedinputJson = InputJsonPath + USname + "\\" + webServiceParameters.getStrTestName() + "_" + context.strScenarioRunId +  "_Data_Row_" + RowIndex + "_POST.json";
							if (index != -1) {
								try {
									if (new File(updatedinputJson).exists()) 
										{
											strBody = parser.parse(new FileReader(updatedinputJson)).toString();
						
								} 
								}catch (Exception e) {
									e.printStackTrace();
									this.context.addResult("Parse input file for POST api call ", " Check input for POST api call : " + testname, "Verify input parameters are correct", "Error parsing the POST JSON File. Please check the file. File:-" + Path + "\\" + webServiceParameters.getStrTestName() + "_POST.json", RunStatus.FAIL);
									//continue;
								}
							}
							System.out.println("******************* Input URL *******************************");
							System.out.println(webServiceParameters.getStrURI());
							System.out.println("************** Input JSON body *******************************");
							System.out.println(strBody);
							if (webServiceParameters.getHeaders() != null) {

								System.out.println("************** Input URL Header Parameters *******************************");
								System.out.print(webServiceParameters.getHeaders());
								//System.out.print(webServiceParameters.getStrURI());
								res = given().headers(new Headers(webServiceParameters.getHeaders())).contentType("application/json").body(strBody).post(webServiceParameters.getStrURI());
							} else
								res = given().contentType("application/json").post(webServiceParameters.getStrURI(), strBody);//check it for validating header parameters

						}
						else {
							this.context.addResult("Parse input file for webservice request type", "Check input for webservice call type : " + webServiceParameters.getStrRequestType(), "Webserivce call type not implemented ", "Webservice call type not implemented (ONLY GET, PUT and POST implemented)", RunStatus.FAIL);

							// continue;
						}

						try {
							Thread.sleep(1300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out.println("Headers= " + res.headers());
						System.out.println("Status Code= " + res.getStatusCode());
						System.out.println("Status Line= " + res.getStatusLine());
						this.context.addResult("Execute webservice call for " + propertiesFileKey, "Execute webservice call: " + webServiceParameters.getStrTestName(), "Webserivce call executed ", "Webservice call returned Status Code = " + res.getStatusCode() + " StatusLine =  " + res.getStatusLine(), RunStatus.PASS);

						//String strOutputDirectory = "c:\\temp\\output" + "\\" + USname ;
						String strOutputDirectory = PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname;
						//String strOutputDirectory = ResponsePath + USname ;
						File currentFolder = new File(strOutputDirectory);
						if (!currentFolder.exists()) {
							currentFolder.mkdir();
						}

						String strJSONOutputFileName = strOutputDirectory + "\\" + webServiceParameters.getStrTestName() + "_" + context.strScenarioRunId + "_Data_Row_" + RowIndex + ".json";
						String strJsonOutput =   res.asString();
					
						if(res.contentType().equalsIgnoreCase("application/zip")) {
						String downloadedFile = strOutputDirectory + "\\" + webServiceParameters.getStrTestName() + "_" + context.strScenarioRunId + "_Data_Row_" + RowIndex + ".zip";
					    File downloadedFile1 = new File(downloadedFile);
					    try (FileOutputStream out = new FileOutputStream(downloadedFile1)) {
					      try (InputStream in = res.asInputStream()) {
					       // LOG.info("Available: " + in.available());
					        IOUtils.copy(in, out);
					      }
					    }
					    catch(IOException exp ){
					    	exp.printStackTrace();
					    }
						}
						else {
					    				
						writeLineToFile(strJSONOutputFileName, strJsonOutput);
						}
						String[] Invalidcodelist = {"400", "404", "500", "204", "409", "403"};

						if (SucessorFailure.equalsIgnoreCase("Success")) {
							if ((res.getStatusCode() == 200 || res.getStatusCode() == 201))

							{
								//writeLineToFile(strJSONOutputFileName, strJsonOutput);
								this.context.addResult("Generate json response file: " + propertiesFileKey, "Generate json response file", "Generated json response file " + strJSONOutputFileName, strJsonOutput, RunStatus.PASS);
							} else if (res.getStatusCode() == 503) {
								this.context.addResult("Execute WebService for " + propertiesFileKey, "To Execute the WebService : " + testname, " Service Temporarily Unavailable" + res.getStatusLine(), " Webservice Temporarily Unavailable", RunStatus.FAIL);
							} else
								this.context.addResult("Execute WebService for " + propertiesFileKey, "To Execute the WebService : " + testname, "WebService Needs to be executed", "Error occured while execution of webservice. Error code : " + res.getStatusLine(), RunStatus.FAIL);
						} else if (SucessorFailure.equalsIgnoreCase("Failure"))  //(SucessorFailure.equalsIgnoreCase("exception"))
						{
							if (ArrayUtils.contains(Invalidcodelist, "" + res.getStatusCode())) {
								this.context.addResult("Verify response status code for " + propertiesFileKey, "verify response status code", "Status code for api call =  " + res.getStatusCode() + "\n" + strJSONOutputFileName, "api call failed as expected" + "\n\n" + strJsonOutput, RunStatus.PASS);
							} else
								this.context.addResult("Execute WebService for " + propertiesFileKey, "To Execute the WebService : " + testname, "WebService Needs to be executed", "Error occured while execution of webservice. Error code : " + res.getStatusLine(), RunStatus.FAIL);
						}
					}
				}


			}
	          catch(AssertionError e){
				this.context.addResult("Execute WebService for "+propertiesFileKey, "To Execute the WebService based on the Request and the File", "WebService Needs to be executed", "Assertion error occured", RunStatus.FAIL);
				e.printStackTrace();
			}
	    	
	    }
	 
	 
}
