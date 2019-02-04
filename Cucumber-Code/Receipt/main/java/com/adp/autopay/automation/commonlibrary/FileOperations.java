package com.adp.autopay.automation.commonlibrary;

import com.adp.autopay.automation.mongodb.RunStatus;
import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.PropertiesFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import static org.junit.Assert.fail;


public class FileOperations
{
	ExecutionContext context;
	public static String AdminServer = PropertiesFile.GetProperty("AdminServerIP");

	public FileOperations(ExecutionContext context){
		this.context = context;
	}

	//	According to this method , the last attribute for the batch command is the path up to directory
	public static String getFilePath(String command){
		String Command = command;
		String[] parts = Command.split(" ");
		int size = parts.length;
		for(int i=0;i<size;i++){
			if(parts[i].contains("ClientData")){
				//System.out.println("*** File Path:"+parts[i]);
				return (parts[size-1]);
			}
		}
		return (parts[size-1]);
	}

	public static boolean VerifyFileExistance(String path,String file){
		String Path = path.replace("C:", "\\\\"+AdminServer+"\\C$");
		String File = file;
		String completePath = Path+"\\"+File;
		File ifile = new File(completePath);

		if(ifile.exists()){
			System.out.println("********************");
			System.out.println(completePath);
			System.out.println("*** File Exists in the target directory , You can continue with batch Execution ");
			System.out.println("********************");
			return true;
		}
		else{
			String completePath_processed = "\\\\"+AdminServer+"\\C$\\CSEC\\ClientData\\Automation\\"+File;
			File ifile1 = new File(completePath_processed);
			if(ifile1.exists()){
				System.out.println("********************");
				System.out.println(completePath_processed);
				System.out.println("*** Specified File Exists in the Automation directory , copying is in progress ...........");
				try {
					FileUtils.copyFile(ifile1,ifile);
					if(ifile.exists()){
						System.out.println("*** File has been copied from the Automation Directory to target directory ");
						System.out.println("********************");
						return true;
					}
					else{
						System.err.println("*** Error in copying file");
						System.out.println("********************");
						return false;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				System.out.println("*** The File doesnot exist in the Target folder too , Please Copy the file to Automation Directory :C:/CSEC/ClientData/Automation");
				System.out.println("********************");
				return false;
			}
		}
		return false;
	}


	public static int FileCount(String fileDir,String Prefix,String COID,String Filename){
		try{
			File dir = new File(fileDir);

			//Prefix values : LOAImport,NewHire,PayrollImport,EligibilityCalc,EligibilityExport
			FileFilter fileFilter = new WildcardFileFilter(Prefix+"_"+COID+"_"+Filename+"*.log");

//			//HR data import will follow this kind of format.
//			FileFilter fileFilter = new WildcardFileFilter(Prefix+"_"+COID+"_*.log");
			File files[] = dir.listFiles(fileFilter);
			int filecount = files.length-1;
			return filecount;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}



	public static File FileSearch(String fileDir,String Prefix,String COID,String Filename){
		try{
			File dir = new File(fileDir);

			//Prefix values : LOAImport,NewHire,PayrollImport,EligibilityCalc,EligibilityExport
			FileFilter fileFilter = new WildcardFileFilter(Prefix+"_"+COID+"_"+Filename+"*.log");

//			//HR data import will follow this kind of format.
//			FileFilter fileFilter = new WildcardFileFilter(Prefix+"_"+COID+"_*.log");
			File files[] = dir.listFiles(fileFilter);
			int filecount = files.length-1;
			System.out.println(filecount);
			if(filecount >0){
				System.out.println(files[filecount]);
				return files[filecount];
			}
			else{
				return null;
			}
		}
		catch(ArrayIndexOutOfBoundsException ae){
			return null;
		}
	}


	public static boolean VerifyContentIntheFile(ExecutionContext context,String stringToCheck,String Prefix,String COID,String Filename) throws Exception{
		try {

			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd_hhmmss");

			//Get The command Execution Time is current time - 2 minutes
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			now.add(Calendar.MINUTE, -15);
			String BatchTime = sdfDate.format(now.getTime());
			Date BatchExecutedTime = sdfDate.parse(BatchTime);

			//Get Logfile time from the file generated
			File file = FileSearch("\\\\"+AdminServer+"\\C$\\CSEC\\logs\\",Prefix,COID,Filename);
			String Name = FilenameUtils.removeExtension(file.getName());
			String LogFileTime = Name.substring(Name.length() - 15, Name.length());
			Date LogGeneratedTime = sdfDate.parse(LogFileTime);

			//Compare dates to verify Log file.
			if(BatchExecutedTime.compareTo(LogGeneratedTime)< 0){
				@SuppressWarnings("resource")
				BufferedReader br = new BufferedReader(new FileReader(file));
				String nextLine;
				while ((nextLine = br.readLine())!= null){
					if (nextLine.contains(stringToCheck)){
						System.out.println("batch job status: PASS");
						return true;
					}
				}
				System.out.println("*** "+stringToCheck+" not found in the log file");
				context.addResult("Log File Verification For Batch Status",  "Verify the status of batch job By Verifying Log file", "Log file: (" + Filename + ")should contain Specified Text(" + stringToCheck +")", "Specified Text Not found: "+stringToCheck+" .So, We considered batch as FAIL", RunStatus.FAIL);
				return false;
			}
			else{
				System.out.println("Log File Not generated , Batch Job Failed");
				context.addResult("Log File Verification For Batch Status",  "Verify the status of batch job By Verifying Log file", "Log file: (" + Filename + ")should contain Specified Text(" + stringToCheck +")", "Log file not generated", RunStatus.FAIL);
				return false;
			}
		}catch (FileNotFoundException e1) {
			context.addResult("Log File Verification For Batch Status", "Verify the status of batch job By Verifying Log file", "Log file: (" + Filename + ")should contain Specified Text(" + stringToCheck + ")", "Log file not generated", RunStatus.FAIL);
		} catch (IOException ep) {
			ep.printStackTrace();
		}
		return false;
	}


	public static String GetdatafromFile(String FieldName,String Prefix,String COID,String Filename){
		try{
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(FileSearch("\\\\"+AdminServer+"\\C$\\CSEC\\logs\\",Prefix,COID,Filename)));
			String nextLine;
			while ((nextLine = br.readLine())!= null){
				if (nextLine.contains(FieldName)) {
					String Line = nextLine;
					int start = (Line.indexOf(FieldName)+(FieldName.length())+1);
					int end = (Line.length());
					String outputValue = (Line.substring(start, end).replace("=", "").trim());
					return outputValue;
				}
			}
		}catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException ep) {
			ep.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("resource")
	public static List<String> GetFieldsFromJSON(File file) throws IOException,FileNotFoundException{
		BufferedReader br_ProcessedJSON = new BufferedReader(new FileReader(file));
		String[] Fields = null;
		List<String> output = new ArrayList<String>();
		String nextLine;
		while((nextLine = br_ProcessedJSON.readLine())!= null){
			Fields = nextLine.trim().replace("{","").replace("}", "").replace("]", "").replace("[", "").split(",");
			for(int i=0;i<Fields.length;i++){
				output.add(Fields[i]);
			}
		}
		return output;

	}

	/**
	 * \\hwsenas1\ece\DIT2\CSEC\Logs\HealthConnect\Leave
	 * \\hwsenas1\ece\DIT2\CSEC\Logs\HealthConnect\Leave\latest
	 *
	 * @param SrcLocation
	 * @param TargetLocation
	 * @param FileUniqueId
	 * @return
	 */
	public static boolean CopyGeneratedFileToDestinationLocation(String SrcLocation,String TargetLocation,String FileUniqueId){
		try{
			File dir = new File(SrcLocation);
			File files[] = dir.listFiles();
			for(File file:files){
				if(file.getName().contains(FileUniqueId)){
					String FileName = file.getName();
					String SrcFile = SrcLocation+"\\"+FileName;
					File Source = new File(SrcFile);
					String DestFile = TargetLocation+"\\"+FileName;
					File Destination = new File(DestFile);
					FileUtils.copyFile(Source, Destination);
					if(Destination.exists()){
						return true;
					}
					else{
						System.out.println("*** File Is not Copied to Latest Folder");
						return false;
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();

		}
		return false;
	}

	/* nirali..file search and replace in file baseline compare needs to  be change  */

	public static void ReplaceContentInBaselineFile(ExecutionContext context,String Filename, String stringToCheck,String stringToReplace ) throws IOException {
		try {
		/*String Filenm = Filename.replaceFirst(".json", "_temp.json");

		int index = Filename.indexOf("baseline");
		if (index == -1)
		       return;

		Filenm = Filename.substring(0, index) + "baseline//temp//" + Filenm.substring(index+9) ;


		File tempFolder = new File ("c:\\Temp\\baseline\\temp");
		if(!tempFolder.exists()){
			tempFolder.mkdir();
		} */

			FileInputStream fis = new FileInputStream(Filename);
			String content = IOUtils.toString(fis, Charset.defaultCharset());
			content = content.replaceAll(stringToCheck, stringToReplace);
			FileOutputStream fos = new FileOutputStream(Filename);
			IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
			fis.close();
			fos.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	/* replace payload data in JSON file before load */
	public static void ReplaceContentInJSONFile(ExecutionContext context,String Filename,String Fieldnames, String fieldToCheck,String fieldToReplace ) throws IOException {
		try {
			int ind = Filename.lastIndexOf(".json");
			if (ind == -1){
				context.addResult("Replace data in input JSON file ", "Replace data in input JSON paylaod file ", "Replace data in input JSON file FAILED ", "String to replace not found " , RunStatus.FAIL);
				return;
			}
			// String Filenm = Filename.substring(0, ind) + "_test.json";  //+ Filename.substring(ind);

			String[] arrfieldname,arrfieldcheck,arrfieldreplace;

			arrfieldname = Fieldnames.split(",");
			arrfieldcheck = fieldToCheck.split(",");
			arrfieldreplace= fieldToReplace.split(",");


			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddhhmmss");
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			String BatchDate = sdfDate.format(now.getTime());


			int j = context.STestName.lastIndexOf(".");
			if (j == -1)
			{
				context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
			}
			String USname = context.STestName.substring(0,j);

			//File tempFolder = new File (System.getProperty("user.dir")+ "/src/main/java/com/adp/benefits/automation/webservices/story.json/payload/" + USname + "\\" + "temp");
			File tempFolder = new File (PropertiesFile.GetEnvironmentProperty("TEMP_OUTPUT_PATH") + USname );

			if(!tempFolder.exists()){
				tempFolder.mkdir();
			}

			FileInputStream fis = new FileInputStream(Filename);
			String content = IOUtils.toString(fis, Charset.defaultCharset());

			for (int i=0;i<arrfieldname.length; i++){

				if (arrfieldname[i].contains("deductionCode")) {
					String nCode = "P26";
					content = content.replaceAll(arrfieldcheck[i], nCode);
				}
				if (arrfieldname[i].contains("itemID")) {
					String nItem = "PA05:K45:P26::";
					content = content.replaceAll(arrfieldcheck[i], nItem);
				}
/*
	    	else if (arrfieldname[i].contains("benefitEventOccurDate")){
	    		String Stringtofind = FindrepalceString(context, Filename,Fieldnames) ;
	    		int Noofdays = 0;
	    		if (content.contains("Marriage")){
	    			Noofdays = 10; }
	    			else if (content.contains("Divorce")){
		    			Noofdays = 8; }
		    			else if (content.contains("Adoption")){
			    			Noofdays = 6; }
		    			else if (content.contains("Birth")){
			    			Noofdays = 7; }

	    		SimpleDateFormat EvntDate = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar today = Calendar.getInstance();
	    	    today.setTime(new Date());
		        today.add(Calendar.DAY_OF_MONTH, -Noofdays);
	  		    String Eventdtstr = EvntDate.format(today.getTime());

	    		String datetocheck = "benefitEventOccurDate";
	    		//System.out.println("datetocheck = " + datetocheck);

	    		content = content.replaceAll(Stringtofind, Eventdtstr);
	    	}
	    	*/
			}

//	    content = content.replaceAll(fieldToCheck, fieldToReplace);
			FileOutputStream fos = new FileOutputStream(Filename);
			IOUtils.write(content,fos, Charset.defaultCharset());
			fis.close();
			fos.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public static List<Object> getAllObjs(JSONArray names, JSONObject obj, HashMap dataMap , Integer index) throws Exception {
		List<Object> listObjs = new ArrayList<Object>();
		if(names !=null && names.length() > 0) {
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);

				Set<String> keySet = dataMap.keySet();

				boolean isDataMapKey = false;

				for (String dataMapKey : keySet) {
					if(dataMapKey.startsWith(name))
					{
						Object value = obj.get(names.getString(i));

						if(value instanceof JSONObject) {
//						List mapValues = (List) dataMap.get(dataMapKey);
							String[] dataMapKeyTokens = dataMapKey.split("_");

							JSONObject valueJSON = (JSONObject) value;
							JSONArray valueNames = valueJSON.names();
						/*for (int j = 0; j < valueNames.length(); j++) {
							if(mapValues.get(0).equals(valueJSON.getString(valueNames.getString(j)))) {
								valueJSON.put(valueNames.getString(j), mapValues.get(index));
							}
						}*/
							getObject(valueNames, valueJSON, index, dataMapKey, dataMap, dataMapKeyTokens);
						} else {
							obj.put(name, ((List)dataMap.get(dataMapKey)).get(index));
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
	
	public static void getObject(JSONArray names, JSONObject obj, Integer index, String dataMapKey, HashMap dataMap, String[] dataMapKeyTokens) throws Exception {
		if(names !=null){
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);
				Object value = obj.get(names.getString(i));

				for (String dataMapKeyToken : dataMapKeyTokens) {
					if(value instanceof JSONObject) {
						//System.out.println("value: " + value + " : ((JSONObject)value).names() : " + ((JSONObject)value).names());
						getObject(((JSONObject)value).names(), ((JSONObject)value), index, dataMapKey, dataMap, dataMapKeyTokens);
					} else if(dataMapKeyToken.equals(name)){
						obj.put(name, ((List)dataMap.get(dataMapKey)).get(index));
					}
				}

			}
		}

	}

	public static void getVerifyTheValue(List<Object> listObjects, HashMap dataMap, Integer index) throws Exception {
		for (Object object : listObjects) {
			if(object instanceof JSONObject) {
				getVerifyTheValue(getAllObjs(((JSONObject) object).names(), (JSONObject) object, dataMap, index),dataMap, index);
			}
		}
	}
	
	public static List<Object> getAllObjs_1(JSONArray names, JSONObject obj, HashMap dataMap , Integer index) throws Exception {
		List<Object> listObjs = new ArrayList<Object>();
		if(names !=null && names.length() > 0) {
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);

				Set<String> keySet = dataMap.keySet();

				boolean isDataMapKey = false;

				for (String dataMapKey : keySet) {
					if(dataMapKey.startsWith(name))
					{
						Object value = obj.get(names.getString(i));

						if(value instanceof JSONObject) {
//						List mapValues = (List) dataMap.get(dataMapKey);
							String[] dataMapKeyTokens = dataMapKey.split("_");

							JSONObject valueJSON = (JSONObject) value;
							JSONArray valueNames = valueJSON.names();
						/*for (int j = 0; j < valueNames.length(); j++) {
							if(mapValues.get(0).equals(valueJSON.getString(valueNames.getString(j)))) {
								valueJSON.put(valueNames.getString(j), mapValues.get(index));
							}
						}*/
							getObject_1(valueNames, valueJSON, index, dataMapKey, dataMap, dataMapKeyTokens);
						} else if(value instanceof JSONArray) {
							JSONArray jArray = (JSONArray) value;
							
							for(int j =0; j < jArray.length(); j++) {
								Object jValue = jArray.get(j);
								if(jValue instanceof JSONObject) {
									JSONObject jObject = (JSONObject) jValue;
									String[] dataMapKeyTokens = dataMapKey.split("_");
									getObject_1(jObject.names(), jObject, index, dataMapKey, dataMap, dataMapKeyTokens);
								} else {
									if (((List)dataMap.get(dataMapKey)).get(index).toString().equalsIgnoreCase("*BLANK*")){
										jArray.put(j, "");
										}
									else
									{
										jArray.put(j, ((List)dataMap.get(dataMapKey)).get(index));
									}
								}
							}
						} else {
							if (((List)dataMap.get(dataMapKey)).get(index).toString().equalsIgnoreCase("*BLANK*")){
								obj.put(name, "");
							}
							else
							{
								obj.put(name, ((List)dataMap.get(dataMapKey)).get(index));
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
	
	public static void getObject_1(JSONArray names, JSONObject obj, Integer index, String dataMapKey, HashMap dataMap, String[] dataMapKeyTokens) throws Exception {
		if(names !=null){
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);
				Object value = obj.get(names.getString(i));

				for (String dataMapKeyToken : dataMapKeyTokens) {
					if(value instanceof JSONObject) {
						//System.out.println("value: " + value + " : ((JSONObject)value).names() : " + ((JSONObject)value).names());
						getObject_1(((JSONObject)value).names(), ((JSONObject)value), index, dataMapKey, dataMap, dataMapKeyTokens);
					}  else if(value instanceof JSONArray){
						JSONArray array = (JSONArray) value;
						for (int j = 0; j < array.length(); j++) {
							Object jVal = array.get(j);
							if(jVal instanceof JSONObject) {
								getObject_1(((JSONObject)jVal).names(), ((JSONObject)jVal), index, dataMapKey, dataMap, dataMapKeyTokens);
							} else if(dataMapKeyToken.equals(name)){
								if (((List)dataMap.get(dataMapKey)).get(index).toString().equalsIgnoreCase("*BLANK*")){
									array.put(j, "");
								}
								else
								{
									array.put(j, ((List)dataMap.get(dataMapKey)).get(index));
								}

							}
						}
					} else if(dataMapKeyToken.equals(name)){
						if (((List)dataMap.get(dataMapKey)).get(index).toString().equalsIgnoreCase("*BLANK*")){
							obj.put(name, "");
						}
						else
						{
							obj.put(name, ((List)dataMap.get(dataMapKey)).get(index));
						}

					}
				}

			}
		}

	}

	public static void getVerifyTheValue_1(List<Object> listObjects, HashMap dataMap, Integer index) throws Exception {
		for (Object object : listObjects) {
			if(object instanceof JSONObject) {
				getVerifyTheValue_1(getAllObjs_1(((JSONObject) object).names(), (JSONObject) object, dataMap, index),dataMap, index);
			}
		}
	}
	
	public static void ReplaceContentInJSONFileData(ExecutionContext context,String Filename,String OutputJsonFile,Integer index ) throws IOException {
		try {
			int ind = Filename.lastIndexOf(".json");
			if (ind == -1){
				context.addResult("Replace data in input JSON file ", "Replace data in input JSON paylaod file ", "Replace data in input JSON file FAILED ", "String to replace not found " , RunStatus.FAIL);
				return;
			}

			int j = context.STestName.lastIndexOf(".");
			if (j == -1)
			{
				context.addResult("Reading webservice property file name ", "Reading webservice property file name ", "Invalid user story name " , "Invalid user story name ",  RunStatus.FAIL);
			}

			String USname = context.STestName.substring(0, j);

			//File tempFolder = new File (System.getProperty("user.dir")+ "/src/main/java/com/adp/benefits/automation/webservices/story.json/payload/" + USname + "\\" + "temp");
			File tempFolder = new File (PropertiesFile.GetEnvironmentProperty("TEMP_INPUT_PATH") + USname );

			//FileInputStream fis = new FileInputStream(Filename);
			//String content = IOUtils.toString(fis, Charset.defaultCharset());

			if(!tempFolder.exists()){
				tempFolder.mkdir();
			}
			//File file = new File(Filename);
			File outPutFile = new File(OutputJsonFile);			//String content = IOUtils.toString(fis, Charset.defaultCharset());
			//  File outPutFile = new File("C:\\Users\\gadiv\\workspace\\Autopay-POC\\Autopay-POC\\src\\main\\java\\com\\adp\\autopay\\automation\\webservices\\story.json\\payload\\US100698_POC_generaldeduction\\UpdateService_POST_test.json");			//String content = IOUtils.toString(fis, Charset.defaultCharset());
			try
			{
				HashMap datamap = context.datamap.get(0);
				String currLine = "";
				FileInputStream fis = new FileInputStream(Filename);
				String content = IOUtils.toString(fis, Charset.defaultCharset());
				//BufferedReader br = new BufferedReader(new FileReader(file));
//				BufferedWriter bw = new BufferedWriter(new FileWriter(outPutFile));
				JSONObject object = new JSONObject(content);
				JSONArray listObj = object.getJSONArray(object.names().getString(0));
				for (int i=0; i< listObj.length(); i++) {
					Object  obj = listObj.get(i);
					if(obj instanceof JSONObject) {
						JSONObject jsonObj = listObj.getJSONObject(i);
						List<Object> listObjs= getAllObjs_1(jsonObj.names(), jsonObj, datamap , index);
						getVerifyTheValue_1(listObjs, datamap, index);
					} else if(obj instanceof JSONArray) {

					}
				}

//					bw.write(object.toString());
//				bw.newLine();
				FileOutputStream fos = new FileOutputStream(outPutFile);
				//IOUtils.write(object.toString(), fos, Charset.defaultCharset());
				//System.out.println(JsonFormatter.format(object));
				IOUtils.write(JsonFormatter.format(object), fos, Charset.defaultCharset());
				fis.close();
				fos.close();
//				bw.close();
				//br.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
			//org.json.JSONArray jsonArray = new org.json.JSONArray( IOUtils.toString(fis, Charset.defaultCharset()));
			/*
			for (int i=0;i<arrfieldname.length; i++){

				if (arrfieldname[i].contains("deductionCode")) {
					String nCode = "P26";
					content = content.replaceAll(arrfieldcheck[i], nCode);
				}
				if (arrfieldname[i].contains("itemID")) {
					String nItem = "PA05:K45:P26::";
					content = content.replaceAll(arrfieldcheck[i], nItem);
				}
			}
*/

//	    content = content.replaceAll(fieldToCheck, fieldToReplace);

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}


	public static void UpdatePayLoadWithData(ExecutionContext context, String Filename, String OutputJsonFileName ,Integer DataRowIndex ) throws IOException {


		try {
			ReplaceContentInJSONFileData(context, Filename,OutputJsonFileName, DataRowIndex);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

/*	public com.google.gson.JsonArray events;
	public com.google.gson.JsonObject eventsObject;
	public String returnedEntity;
	public ObjectMapper mapper = new ObjectMapper();
	public boolean firstTime = true;
	private String eventName;

	public void CreateAJSONWithInputData(ExecutionContext context, String OutputJsonFileName ,Integer DataRowIndex ) throws IOException {

		Integer index = DataRowIndex-1;
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String now = new SimpleDateFormat("yyD-HHmm").format(new Date());
        JsonObject event = new JsonObject();
		String eventClassName = "com.adp.ea.payroll.v1.events.memoconfiguration.add.MemoConfigurationAddEvent";

		//this.eventName = eventName.replace("\"", "");
		this.events = new JsonArray();

		if (eventsObject == null) {
            eventsObject = new JsonObject();
            eventsObject.add("events", events);
		  }
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
                  nodes = entry.replace('_', ',').split(",");
                  for (int x = 0; x < nodes.length - 1; x++) {
                      if (currentNode.has(nodes[x])) {
                          currentNode = currentNode.getAsJsonObject(nodes[x]);
                      } else {
                          currentNode.add(nodes[x], new JsonObject());
                          currentNode = currentNode.getAsJsonObject(nodes[x]);
                      }
                  }
                  val = ((List)datamap.get(entry)).get(index).toString().replace("$today", today).replace("$now", now);
                 if(val.equalsIgnoreCase("*BLANK*")){
					 currentNode.addProperty(nodes[nodes.length - 1], "");
				 }else{
					 currentNode.addProperty(nodes[nodes.length - 1], val);
				 }

              }
			}
			
              //Verify the object generate is a valid string for event type specified
              mapper = new ObjectMapper();
              eventsObject.getAsJsonArray("events").add(event);
              Object obj = mapper.readValue(eventsObject.toString(), clazz);
              String Jsonrequest = eventsObject.toString();
             // FileOutputStream fos = new FileOutputStream(OutputJsonFileName);
             
              JsonParser parser = new JsonParser();
              String prettyJson;
              JsonObject json = parser.parse(Jsonrequest).getAsJsonObject();
              Gson gson = new GsonBuilder().setPrettyPrinting().create();
              prettyJson = gson.toJson(json);
              
              IOUtils.write(prettyJson, new FileOutputStream(OutputJsonFileName), Charset.defaultCharset());
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public JsonArray addAnArrayElementToAnEvent(String arrayName, int eventNbr) {
      
		String FilePath =   System.getProperty("user.dir") + "//src//test//resources//Data/MyInputJSONFile.json";
	    File file = new File(FilePath);
		FileInputStream fis;
		String content;
		try {
			fis = new FileInputStream(file);
			content = IOUtils.toString(fis, Charset.defaultCharset());
			String Jsonrequest = content;

	          JsonParser parser = new JsonParser();
	          String prettyJson;
	          eventsObject = parser.parse(Jsonrequest).getAsJsonObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
				
		if (eventsObject == null || !eventsObject.has("events") || !eventsObject.get("events").isJsonArray()) {
            fail("An events array has not been created as yet....");
        }
        if (eventsObject.getAsJsonArray("events").size() < eventNbr) {
            fail("The events array does not have " + eventNbr + " events");
        }
        String[] nodes;
        int y, z, rec;
        JsonObject event = (JsonObject) eventsObject.getAsJsonArray("events").get(eventNbr - 1);
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
	
	 public void addARecordToAnArrayInEvent(String arrayName, int eventNbr , HashMap datamap , int index) {
	        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	        String now = new SimpleDateFormat("yyD-HHmm").format(new Date());
	        JsonArray arr = addAnArrayElementToAnEvent(arrayName, eventNbr);
	        JsonObject obj = new JsonObject();
	        JsonObject currentNode;
	        String[] nodes;
	        String val;
	        String[] values;
	       // if (returnedEntity == null || returnedEntity.trim().isEmpty()) {
	         //   fail("You have not identified the type of event as yet...");
	      //  }
            
	        //int index = 0;
	    	Set<String> keySet = datamap.keySet();
			boolean isDataMapKey = false;
			//int size = datamap.size()
	        // set parameters in the request
	        int y, z, rec;
	     
	         	for (String entry : keySet) {
	            currentNode = obj;
	            nodes = entry.replace('_', ',').split(",");
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
	                    fail("Field " + ((List)datamap.get(entry)).get(index).toString() + " references an array that does not exist.");
	                } else {
	                    currentNode.add(nodes[x], new JsonObject());
	                    currentNode = currentNode.getAsJsonObject(nodes[x]);
	                }
	            }
	           // val =((List)datamap.get(entry)).get(index).toString().replace("$today", today).replace("$now", now);
	            val =((List)datamap.get(entry)).get(index).toString().replace("$today", today).replace("$now", now);
	        	String[] Values = val.split(",");
	        
	            if (nodes[nodes.length - 1].endsWith("s")) {
	                if (currentNode.has(nodes[nodes.length - 1])) {
	                   // currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(val));
	                	for (String value: Values) {
	                		currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(value));
	    				}
	                } else {
	                    currentNode.add(nodes[nodes.length - 1], new JsonArray());
	                   // currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(val));
	                    for (String value: Values) {
	                    	currentNode.getAsJsonArray(nodes[nodes.length - 1]).add(new JsonPrimitive(value));
	                    }
	                }
	            } else {
	                currentNode.addProperty(nodes[nodes.length - 1], val);
	            }
	        }

	        arr.add(obj);
	     
	        try {
	          //  Class<?> clazz = Class.forName("com.adp.ea.payroll.v1.events.memoconfiguration.add.MemoConfigurationAddEvent");
	            //mapper.readValue(eventsObject.toString(), clazz);
	           // ALERTS.debug("Event created <" + eventsObject.toString() + ">");
	            
	            String Jsonrequest = eventsObject.toString();
	             // FileOutputStream fos = new FileOutputStream(OutputJsonFileName);
	             
	              JsonParser parser = new JsonParser();
	              String prettyJson;
	              JsonObject json = parser.parse(Jsonrequest).getAsJsonObject();
	              Gson gson = new GsonBuilder().setPrettyPrinting().create();
	              prettyJson = gson.toJson(json);
	              String FilePath =   System.getProperty("user.dir") + "//src//test//resources//Data/MyInputJSONFile.json"; 
	              IOUtils.write(prettyJson, new FileOutputStream(FilePath), Charset.defaultCharset());
	            
	        } catch (Exception ex) {
	         //   ALERTS.error("Invalid event <" + eventsObject.toString() + ">");
	            fail("Json generated was not a valid " + returnedEntity + "\n Json generated <" + eventsObject.toString() + ">");
	            ex.printStackTrace();
	        }
	    }*/
	
	public static void UpdatePayLoad(ExecutionContext context, String Filename, String PayloadType  ) throws IOException {

		try {
			switch (PayloadType)
			{
				case "AddDepInsideProcess_POST" :
					ReplaceContentInJSONFile(context,Filename, "deductionCode,itemID,DOB","P25,PA05:K45:P25::,1999-07-15", "" );
					//UpdateJSONObject( context,  Filename, "deductionCode","MAHESH");
				case "Dependent_PUT":
					ReplaceContentInJSONFile(context,Filename, "SSN,Name,DOB","123031811,JanetTen,1999-07-15", "" );
				case "PIE_POST":
					ReplaceContentInJSONFile(context,Filename,"benefitEventOccurDate","","");
				case "AddMedElection_POST" :
				case "AddDenElection_POST" :
					ReplaceContentInJSONFile(context,Filename, "deductionCode,itemID,DOB","P25,PA05:K45:P24::,1999-07-15", "" );
			}
		}
		catch (Exception e){
			e.printStackTrace();

		}
	}

/* nirali..file search and replace for webservice.properties file for dynamic data in link like process id  */

	public static void ReplaceContentInWebservicepropertyFile(ExecutionContext context,String Filename, String stringToCheck,String stringToReplace  ) throws IOException {
		try {
			String tofind = "";
			String toreplace = "";


			System.out.println(Filename);
			BufferedReader br_Processed = new BufferedReader(new FileReader(Filename));
			System.out.println("it worked");
			String nextLine;
			int ind = 0;
			while((nextLine = br_Processed.readLine())!= null){
				int index =  nextLine.indexOf(stringToCheck);
				//System.out.println("Index= " + index);
				//System.out.println("Index= " + index);
				if (index != -1){
					System.out.println("nextLine is: " + nextLine);
					//System.out.println("Line is: " + nextLine.substring(0, index));
					ind++;
					if (nextLine.contains("beneficiaries/")){
						int fIndex = nextLine.indexOf("beneficiaries/");
						//System.out.println("fIndex= " + fIndex);
						//System.out.println("Actual found= " + nextLine.substring(0, fIndex));
						tofind = nextLine.substring(fIndex, fIndex+50);
						toreplace = "beneficiaries/"+ stringToReplace;
						//System.out.println("tofind= " + tofind);
						//System.out.println("toreplace= " + toreplace);
						break;
					}

					if (stringToCheck.equals("processes/")){
						tofind = nextLine.substring(index, index+46);
						//toreplace = "processes/"+ stringToReplace + "/";
						toreplace = "processes/"+ stringToReplace ;
						break;
					}
					if (stringToCheck.equals("dependents/") && !(nextLine.substring(index+11,index+17).toLowerCase().contains("manage"))){
						tofind = nextLine.substring(index, index+47);
						toreplace = "dependents/"+ stringToReplace ;
						//toreplace = "dependents/"+ stringToReplace ; // + "/";
						break;
					}

				}
			}
			br_Processed.close();
			if (ind == 0) {
				return;
			}

			FileInputStream fis = new FileInputStream(Filename);
			String content = IOUtils.toString(fis, Charset.defaultCharset());
			content = content.replaceAll(tofind, toreplace);

			FileOutputStream fos = new FileOutputStream(Filename);
			IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
			fis.close();
			fos.close();
			//context.addResult("Update JSONfile from the SQL Query", "Update JSONfile from the SQL Query", "Update webservice command File", "Webservice property file got updated successfully", RunStatus.PASS);
		}
		catch(Exception e){
			context.addResult("Update file", "Update file", "File should be updated", "Found an exception in executing ReplaceContentInWebservicepropertyFile Method", RunStatus.FAIL);
			e.printStackTrace();
		}

	}




	public static void IgnoredataInBaselineFile(ExecutionContext context,String Filename) throws IOException {
		try {

			String[] tofind = {"\"confirmationID\":", "processes/","\"creationDateTime\":","\"lastConfirmedDateTime\":","\"processID\":","\"lastUpdatedDateTime\":","\"startDate\":","\"endDate\":","\"dependentID\":","\"itemID\":"};
			String[] tofindupdated = {"","","","","","","","","",""};
			String[] toreplace = {"","","","","","","","","",""};


			System.out.println("Filename to process= "+Filename);
			BufferedReader br_Processed = new BufferedReader(new FileReader(Filename));
			System.out.println("It worked.Successfully opened the file for reading");

			String nextLine;
			FileInputStream fis;
			String content;
			FileOutputStream fos;
			//final String itemIDCheck= "\"itemID\":\"";

			while((nextLine = br_Processed.readLine()) != null){
				//	System.out.println("value of next line inside While loop= "+nextLine);
				for (int j=0; j<tofind.length ; j++)
				{
					int index =  nextLine.indexOf(tofind[j]);
					if (index != -1){

						System.out.println("tofind("+j+") ="+tofind[j]);
						switch (tofind[j])
						{
							case "\"confirmationID\":" :
							 /*tofindupdated[j] = nextLine.substring(index-1, index+31);
							 toreplace[j] = "";*/

								tofindupdated[j] = nextLine.substring(index+1, index+32);
								System.out.println("value to be replaced= " + tofindupdated[j]);
								toreplace[j] = "CONFIRMATIONID\":\"VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}

								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));
								break;

							case "processes/":
							 /*tofindupdated[j] = nextLine.substring(index-1, index+46);
							 toreplace[j] = "";*/

								tofindupdated[j] = nextLine.substring(index, index+46);
								// System.out.println("value to be replaced= " + tofindupdated[j]);
								toreplace[j] = "PROCESSES/VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}

								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));
								break;

							case "\"creationDateTime\":" :
							 /*tofindupdated[j] = nextLine.substring(index-1, index+94);
							 toreplace[j] = "";*/

								tofindupdated[j] = nextLine.substring(index+1, index+44);
								//System.out.println("value to be replaced= " + tofindupdated[j]);
								toreplace[j] = "CREATIONDATETIME\":\"VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}


								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));
								break;

							case "\"lastConfirmedDateTime\":" :
							 /*tofindupdated[j] = nextLine.substring(index-1, index+49);
							 toreplace[j] = "";*/


								tofindupdated[j] = nextLine.substring(index+1, index+47);
								// System.out.println("value to be replaced= " + tofindupdated[j]);
								toreplace[j] = "LASTCONFIRMEDDATETIME\":\"VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}

								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));
								break;

							case "\"processID\":" :
							 /*tofindupdated[j] = nextLine.substring(index-1, index+47);
							 System.out.println("tofindupdadted for processID  = " + tofindupdated[j] );
							 toreplace[j] = "";*/

								tofindupdated[j] = nextLine.substring(index+1, index+49);
								// System.out.println("value to be replaced= " + tofindupdated[j]);
								toreplace[j] = "PROCESSID\":\"VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}


								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));
								break;

							case "\"lastUpdatedDateTime\":" :
							 /*tofindupdated[j] = nextLine.substring(index-1, index+48);
							 System.out.println("tofindupdadted for lastUpdatedDateTime  = " + tofindupdated[j] );
							 toreplace[j] = "";*/

								tofindupdated[j] = nextLine.substring(index+1, index+47);
								//System.out.println("value to be replaced= " + tofindupdated[j]);
								toreplace[j] = "LASTUPDATEDDATETIME\":\"VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}


								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));
								break;

							case "\"startDate\":" :
							 /*tofindupdated[j] = nextLine.substring(index+12, index+22);
							  System.out.println("tofindupdadted for startDate  = " + tofindupdated[j]);
							 toreplace[j] = "";*/


								tofindupdated[j] = nextLine.substring(index+1, index+23);
								// System.out.println("value to be replaced= " + tofindupdated[j]);
								toreplace[j] = "STARTDATE\":\"VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}

								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));
								break;

							case "\"endDate\":" :
							 /*tofindupdated[j] = nextLine.substring(index+10, index+20);
							  System.out.println("tofindupdadted for endDate  = " + tofindupdated[j] );
							 toreplace[j] = "";*/

								tofindupdated[j] = nextLine.substring(index+1, index+21);
								// System.out.println("value to be replaced= " + tofindupdated[j] );
								toreplace[j] = "ENDDATE\":\"VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}

								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));

								break;

							case "\"dependentID\":" :
							 /*tofindupdated[j] = nextLine.substring(index+14, index+50);
							 System.out.println("tofindupdadted for dependentID  = " + tofindupdated[j] );
							 toreplace[j] = "";*/

								tofindupdated[j] = nextLine.substring(index+1, index+51);
								// System.out.println("value to be replaced= " + tofindupdated[j] );
								toreplace[j] = "DEPENDENTID\":\"VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}


								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));
								break;

							case "\"itemID\":" :
								tofindupdated[j] = nextLine.substring(index+1, index+46);

							  /*System.out.println("length of tofindupdated= " + tofindupdated[j].length());
							  if (tofindupdated[j].length()==45){
								  System.out.println("Condition is TRUE");
								  System.out.println("value to be replaced= " + tofindupdated[j]);
								  toreplace[j] = "ITEMID\":\"VALUE-REPLACED";
							  }else{

							  }*/

								if(tofindupdated[j].contains("BASICLIFE")||tofindupdated[j].contains("BasicTerm"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+19);

								}
								if(tofindupdated[j].contains("SPLIFE"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+16);

								}
								if(tofindupdated[j].contains("BASICADD")||tofindupdated[j].contains("AetnaPPO")||tofindupdated[j].contains("DeltaHMO")||tofindupdated[j].contains("DeltaPPO"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+18);

								}
								if(tofindupdated[j].contains("HCFSA") ||tofindupdated[j].contains("FSA"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+15);

								}
								if(tofindupdated[j].contains("SSN"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+13);

								}
								if(tofindupdated[j].contains("AARP"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+14);

								}

								if(tofindupdated[j].contains("Med_NoCoverage")||tofindupdated[j].contains("Den_NoCoverage"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+24);

								}
								if(tofindupdated[j].contains("UnitedHCHD"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+20);

								}

								if(tofindupdated[j].contains("Vision_No_Coverage"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+28);

								}
								if(tofindupdated[j].contains("Vision Coverage"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+25);

								}
								if(tofindupdated[j].contains("1xPay")||tofindupdated[j].contains("2xPay")||tofindupdated[j].contains("3xPay")||tofindupdated[j].contains("4xPay")||tofindupdated[j].contains("5xPay")||tofindupdated[j].contains("6xPay"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+15);

								}
								if(tofindupdated[j].contains("Basic_Life_NoCoverage")||tofindupdated[j].contains("Basic_Term_NoCoverage"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+31);

								}
								if(tofindupdated[j].contains("Prem_Life_NoCoverage")||tofindupdated[j].contains("Prem_Term_NoCoverage"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+30);

								}
								if(tofindupdated[j].contains("Basic_Travel_NoCoverage"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+33);

								}
								if(tofindupdated[j].contains("BasicTravel")||tofindupdated[j].contains("PremiumTerm"))
								{
									tofindupdated[j] = nextLine.substring(index+1, index+21);

								}

								//System.out.println("value to be replaced= " + tofindupdated[j]);

								toreplace[j] = "ITEMID\":\"VALUE-REPLACED";
								br_Processed.close();
								fis = new FileInputStream(Filename);
								content = IOUtils.toString(fis, Charset.defaultCharset());

								for (int k=0;k<tofind.length ; k++){
									content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
								}

								fos = new FileOutputStream(Filename);
								IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
								fis.close();
								fos.close();
								br_Processed = new BufferedReader(new FileReader(Filename));

								break;

						}
					}
					else
						index = 0;
				}

			}
			//System.out.println("after while loop");
			//////////////////Old Code below. Not using now//////////////////////////////////////
			 /*br_Processed.close();

			 if (ind == 0) {
				 return;
			 }

		    FileInputStream fis = new FileInputStream(Filename);
		    String content = IOUtils.toString(fis, Charset.defaultCharset());

		    for (int k=0;k<tofind.length ; k++){
		    	 content = content.replaceAll(tofindupdated[k].toString(), toreplace[k].toString());
		    }


		    FileOutputStream fos = new FileOutputStream(Filename);
			IOUtils.write(content, new FileOutputStream(Filename), Charset.defaultCharset());
			fis.close();
			fos.close();
			 */
			//////////////////////////////////////////////////////////////////////////////////////////
		}
		catch(Exception e){
			context.addResult("Ignore data in JSON file", "Ignore data in JSONfile ", "erase dynamic content from JSON File", "Found an exception in executing step", RunStatus.FAIL);
			e.printStackTrace();
		}

	}



	//////////////// new method to get replace string

	public static String FindrepalceString(ExecutionContext context,String Filename,String tofind) throws IOException {
		try {

			//String tofind = {"creationDateTime","confirmationID", "processes/","lastConfirmedDateTime","benefitEventOccurDate" };
			//String[] tofindupdated = {"","","","","",""};
			//String[] toreplace = {"","","","","",""};

			String toreplace;
			String returnString = "";
			System.out.println(Filename);
			BufferedReader br_Processed = new BufferedReader(new FileReader(Filename));
			String nextLine;

			int ind = 0;
			while((nextLine = br_Processed.readLine())!= null){
				int index =  nextLine.indexOf(tofind);
				if (index != -1){
					ind++;

					switch (tofind)
					{
						case "confirmationID" :
							returnString = nextLine.substring(index-1, index+31);
							toreplace = "";
							break;
						case "processes/":
							returnString = nextLine.substring(index-1, index+47);
							toreplace = "";
							break;
						case "creationDateTime" :
							returnString = nextLine.substring(index-1, index+94);
							toreplace = "";
							break;
						case "lastConfirmedDateTime" :
							returnString = nextLine.substring(index-1, index+49);
							toreplace = "";
							break;
						case "benefitEventOccurDate" :
							returnString = nextLine.substring(index+25,index+35);
							break;
					}
				}
			}

			br_Processed.close();
			return returnString;
		}
		catch(Exception e){
			context.addResult("Find Replacement string", "Find replacement string", "find replacement string failed ", "Found an exception in find replacement String", RunStatus.FAIL);
			e.printStackTrace();
			return "";
		}

	}


	/**
	 * Ram
	 * This method will copy all files with required extensions from source directory to target directory
	 * @param soucreFile
	 * @param targetFile
	 * @param filter
	 */
	public static boolean copyFilesFromSourcerToTargetWithFilter(String sourceDir,String targetDir,String filter){
		try{
			FileFilter fileFilter = new WildcardFileFilter(filter);
			File files[] =  new File(sourceDir).listFiles(fileFilter);
			if(files.length>0)
			{
				System.out.println("Copying files "+files.length+" from "+sourceDir+" to"+targetDir+" is in progress");
				for(File file:files)
				{
					try{
						FileUtils.copyFile(file, new File(targetDir+"\\"+file.getName()));
					}
					catch(Exception e){
						System.err.println("Error Occured while copying the file "+file.getAbsolutePath()+" to"+targetDir+"\\"+file.getName());
					}
				}
				return true;
			}
			else
			{
				System.err.println("No files "+files.length+" from "+sourceDir+" to copy to"+targetDir);
				return false;
			}

		}catch(Exception e){
			System.err.println("Error Occured while copying files from "+sourceDir+" to"+targetDir);
			return false;
		}
	}

	/**
	 * Ram
	 * This method will copy all files from source directory to target directory
	 * @param soucreFile
	 * @param targetFile
	 */
	public static boolean copyFilesFromSourcerToTarget(String sourceDir,String targetDir){
		try{
			File files[] =  new File(sourceDir).listFiles();
			System.out.println("Copying files "+files.length+" from "+sourceDir+" to"+targetDir+" is in progress");
			if(files.length>0)
			{
				System.out.println("Copying files "+files.length+" from "+sourceDir+" to"+targetDir+" is in progress");
				for(File file:files)
				{
					try{
						FileUtils.copyFile(file, new File(targetDir+"\\"+file.getName()));
					}
					catch(Exception e){
						System.err.println("Error Occured while copying the file "+file.getAbsolutePath()+" to"+targetDir+"\\"+file.getName());
					}
				}
				return false;
			}
			else
			{
				System.err.println("No files "+files.length+" from "+sourceDir+" to copy to"+targetDir);
				return false;
			}

		}catch(Exception e){
			System.err.println("Error Occured while copying files from "+sourceDir+" to"+targetDir);
			return false;
		}

	}

	@SuppressWarnings("resource")
	public static boolean searchString(File logFile,String searchText)
	{
		try{
			BufferedReader br = new BufferedReader(new FileReader(logFile));
			String nextLine;
			while ((nextLine = br.readLine())!= null){
				if (nextLine.contains(searchText)){
					System.out.println("Search text "+searchText+" is found in the file "+logFile.getAbsolutePath());
					return true;
				}
			}
			System.out.println("Search text "+searchText+" not found in the file"+logFile.getAbsolutePath());
		}
		catch(Exception ex)
		{
			System.err.println("Error occired while lookin for Search text "+searchText+" in the file"+logFile.getAbsolutePath());
		}
		return false;
	}


	public static String GetStringContentFromJsonFile(ExecutionContext context,String Filename ) throws IOException {
		String content = "";
		try {

			FileInputStream fis = new FileInputStream(Filename);
			content = IOUtils.toString(fis, Charset.defaultCharset());
			fis.close();

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}

}
