package com.adp.autopay.automation.commonlibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.adp.autopay.automation.utility.ExecutionContext;
import com.adp.autopay.automation.utility.PropertiesFile;
import org.apache.commons.io.FileUtils;

import com.adp.autopay.automation.mongodb.RunStatus;

public class ExecuteBatchCommand {
	
		public static boolean RemoteExecution(String serverIP,String Username,String Password,String BatchFileName){
		        try {
		        	
		        	String Command_Validation = DateFunctions.ValidateBatchCommand(BatchFileName);
		        	
		        	File dir = new File("\\\\"+serverIP+"\\C$\\CSEC\\Logs");
		        	File files[] = dir.listFiles();
		        	int filecount_Old = files.length;
		        	
		        	Process p = Runtime.getRuntime().exec("cmd /c start C:\\temp\\PSEXEC\\psExec.exe \\\\"+serverIP+" -u "+Username+" -p "+Password+" cmd /c (^cd c:\\csec\\batch ^& c:\\temp\\SKIPPAUSE.bat ^& "+Command_Validation+"^)");
//		        	Process p = Runtime.getRuntime().exec("cmd /c start C:\\temp\\PSEXEC\\psExec.exe \\\\"+serverIP+" -u "+Username+" -p "+Password+" cmd  /c (^cd c:\\csec\\batch ^& "+BatchFileName+"^)");
		        	int exitvalue = p.waitFor();
		        	    	
		        	String line;
		        	int i=0;
  
		        do{
		        	if(exitvalue == 0){
		        			String pidInfo ="";
		        			Process q =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
		        			BufferedReader input =  new BufferedReader(new InputStreamReader(q.getInputStream()));
		        			while ((line = input.readLine()) != null) {
		        					pidInfo+=line; 
		        			}
		        			input.close();
		        	
		        			if(pidInfo.contains("PsExec.exe"))
		        			{
		        				Thread.sleep(2000);
		        			}
		        			else{
		        				Thread.sleep(15000);
		        				i++;
		        			}
		        	}
		        }while(i==0);
		        	
		        	if(exitvalue == 0){
		        		File files1[] = dir.listFiles();
			        	int filecount_New = files1.length;
		        		System.out.println("*** Command processed Successfully");
		        		
			       	if(filecount_Old == filecount_New || filecount_New < 0){
			        		System.err.println("*** Log file is not generated ");
			        	}
			        	else{
			        		System.out.println("*** Log file generated successfully");
			        }
		        		return true;
		        	}else{
		        		System.err.println("*** Command execution failed");
		        		return false;
		        	}   
		        	
		        } catch (Exception e) {
		            e.printStackTrace();
		       }
				return false;      
		     }
		
		/**
		 * This method is used to run DVS import on the given client and it will run/process all the files in that client folder location 
		 * @param clientID
		 */
		
		public static boolean dVSImport(ExecutionContext context,String clientId,String file) {
			
			String filesLocation,targetLocation;
			filesLocation="\\\\"+ PropertiesFile.GetProperty("NASShareIP")+"\\baseclientcontent\\import\\dvs\\"+clientId+"\\processed";
			targetLocation ="\\\\"+PropertiesFile.GetProperty("NASShareIP")+"\\baseclientcontent\\import\\dvs\\"+clientId;
			filesLocation = (file!="")?filesLocation+"\\"+file:filesLocation;
			System.out.println("Copy file(s) Location: "+filesLocation);
			try {
				
				File dvsFiles = new File(filesLocation);
				 if (dvsFiles.isDirectory()) 
				 {
					 if(FileOperations.copyFilesFromSourcerToTargetWithFilter(filesLocation, targetLocation, "*.xml"))
					 {
						 context.addResult("DVS Import XML Files Copy", "DVS Import XML Files should copy from "+filesLocation+" to"+targetLocation,"DVS Import XML Files should be copied","DVS Import XML Files copied", RunStatus.PASS);
						 if(dVSBatchImport(context,clientId))
							 return true;
						 else
							 return false;
					 }
					 else
					 {
						 context.addResult("Terminating Feature Execution", "No Files to copy from "+filesLocation+" to"+targetLocation,"","", RunStatus.FAIL);
						 CommonFunctions.endFeatureExecution();
					 }
				 }
				 else if(dvsFiles.isFile())
				 {
					 try{
						 FileUtils.copyFile(dvsFiles, new File(targetLocation+"\\"+dvsFiles.getName()));
						 context.addResult("DVS Import XML File Copy", "DVS Import XML File should copy from "+dvsFiles.getAbsolutePath()+" to"+targetLocation,"DVS Import XML File should be copied","DVS Import XML File copied", RunStatus.PASS);
						 return true;
					 }
					 catch(Exception e){
		 	    			System.err.println("Error Occured while copying the file "+dvsFiles.getAbsolutePath()+" to"+targetLocation);
		 	    			context.addResult("Terminating Feature Execution", "Error Occured while copying the file "+dvsFiles.getAbsolutePath()+" to"+targetLocation,"","", RunStatus.FAIL);
							CommonFunctions.endFeatureExecution();
		 	    		}
				 }
				 else
				 {
					 context.addResult("Terminating Feature Execution", "No Directory/File exists to copy files from path "+filesLocation,"","", RunStatus.FAIL);
					 CommonFunctions.endFeatureExecution();
				 }
			} catch (Exception e) {
				context.addResult("Terminating Feature Execution", "No Directory/File exists to copy files from path "+filesLocation,"","", RunStatus.FAIL);
				CommonFunctions.endFeatureExecution();
			}
			return false;
		}
		
		public static boolean dVSBatchImport(ExecutionContext context,String clientId) {
			String psExecCmd="cmd /c start C:\\Temp\\PSEXEC\\psExec.exe \\\\"+PropertiesFile.GetProperty("AdminServerIP")+" -u "+PropertiesFile.GetProperty("AdminUsername")+" -p "+PropertiesFile.GetProperty("AdminPassword")+" cmd /c ^cd ";
			String importCmd= "import_DVS.bat "+clientId;
			String importPath= "C:\\Base\\Import ^& "+importCmd;
			//String importSkipPath= "C:\\Base\\Import ^& c:\\Temp\\SKIPPAUSE.bat ^& "+importCmd;
			String psExecStart=psExecCmd+importPath;
			System.out.println(psExecStart);
			try {
				Process p = Runtime.getRuntime().exec(psExecStart);
				//Process p = Runtime.getRuntime().exec("cmd /c start C:\\temp\\PSEXEC\\psExec.exe \\\\"+serverIP+" -u "+Username+" -p "+Password+" cmd /c (^cd c:\\csec\\batch ^& c:\\temp\\SKIPPAUSE.bat ^& "+Command_Validation+"^)");
				int exitValue=p.waitFor();
				checkPSExecProcess(exitValue);
				return true;
			} catch (Exception e) {
				context.addResult("Terminating Feature Execution", "Error while executing the DVS Import through PSEXEC execution command: "+psExecStart,"","", RunStatus.FAIL, Screenshot.getSnap(context, "PSEXEC execution"));
				CommonFunctions.endFeatureExecution();
			}
			return false;
		}
		
		public static void checkPSExecProcess(int value)
		{
			int exitvalue=value;
			try{
	        	String line;
	        	int i=0;
		        do{
		        	if(exitvalue == 0){
		        			String pidInfo ="";
		        			Process q =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
		        			BufferedReader input =  new BufferedReader(new InputStreamReader(q.getInputStream()));
		        			while ((line = input.readLine()) != null) {
		        					pidInfo+=line; 
		        			}
		        			input.close();
		        	
		        			if(pidInfo.contains("PsExec.exe"))
		        			{
		        				Thread.sleep(2000);
		        				i=0;
		        			}
		        			else{
		        				Thread.sleep(5000);
		        				i++;
		        			}
		        	}
		        }while(i==0);
			}
			catch(Exception ex)
			{
				
			}
		}

}

	