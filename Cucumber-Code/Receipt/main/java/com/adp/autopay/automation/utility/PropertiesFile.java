package com.adp.autopay.automation.utility;

import java.io.FileReader;
import java.util.Properties;

public class PropertiesFile {
	
	public static String To = GetEnvironmentProperty("MailTo");
	public static String From = GetEnvironmentProperty("MailFrom");
	public static String CC = GetEnvironmentProperty("MailCC");
	public static String SendMail = GetEnvironmentProperty("SendMail");
	public static String HubURL = GetEnvironmentProperty("HubURL");
	public static String RunningHost = GetEnvironmentProperty("RunningHost");
	public static String DatabaseConnectionString = GetEnvironmentProperty("DatabaseConnectionString");
	public static String DBport = GetEnvironmentProperty("DBport");
	public static String DatabaseName = GetEnvironmentProperty("DatabaseName");
	
	
	//public static String RunAgainst = GetEnvironmentProperty("RunAgainst");
	public static String GetProperty(String Key){
		String ProjectPath = GetProjectPath(PropertiesFile.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String RunAgainst = ExecutionContext.SRunAgainst;
			try{		
				if(RunAgainst == null){
					RunAgainst = GetEnvironmentProperty("RunAgainst");
				}
				//Mahesh String envFileName= ProjectPath+"src/main/resources/PropertyFiles/" + RunAgainst.toUpperCase() +  ".automation.properties";
				String envFileName= ProjectPath+"src/main/resources/PropertyFiles/" + "environment.properties";
				FileReader reader = new FileReader(envFileName);
				Properties properties = new Properties();
				properties.load(reader);
				return properties.getProperty(Key);
			}catch(Exception e){
				e.printStackTrace();
			}
		return null;
	  }
	
	
	public static String GetDatabasePeroperty(String Key){
		String ProjectPath = GetProjectPath(PropertiesFile.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String RunAgainst = ExecutionContext.SRunAgainst;
		System.out.println(RunAgainst);
			try{		
				if(RunAgainst == null){
					RunAgainst = GetEnvironmentProperty("RunAgainst");
				}
				String envFileName= ProjectPath+"src/main/resources/PropertyFiles/" + RunAgainst.toUpperCase() +  ".database.properties";
				FileReader reader = new FileReader(envFileName);
				Properties properties = new Properties();
				properties.load(reader);
				return properties.getProperty(Key.toUpperCase());
			}catch(Exception e){
				e.printStackTrace();
			}
		return null;
	  }
	
	
	public static String GetEnvironmentProperty(String Key){
		try{
			
			String ProjectPath = GetProjectPath(PropertiesFile.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			FileReader reader = new FileReader(ProjectPath+"src/main/resources/PropertyFiles/environment.properties");
			Properties properties = new Properties();
			properties.load(reader);
			return properties.getProperty(Key);
		}catch(Exception e){
			e.printStackTrace();
		}
	return null;
  }
	
	public static String GetSQLQuery(String Key){
		try{
			String ProjectPath = GetProjectPath(PropertiesFile.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			if (ProjectPath.substring(0,1).equals("/")) 
			{
				ProjectPath = ProjectPath.subSequence(1, ProjectPath.length()).toString();
			}
			FileReader reader = new FileReader(ProjectPath+"src/main/resources/PropertyFiles/DBQueries.properties");
			Properties properties = new Properties();
			properties.load(reader);
			return properties.getProperty(Key);
		}catch(Exception e){
			e.printStackTrace();
		}
	return null;
  }

	public static String GetBatchJob(String Key){
		try{
			String ProjectPath = GetProjectPath(PropertiesFile.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			FileReader reader = new FileReader(ProjectPath+"src/main/resources/PropertyFiles/BatchCommands.properties");
			Properties properties = new Properties();
			properties.load(reader);
			return properties.getProperty(Key);
		}catch(Exception e){
			e.printStackTrace();
		}
	return null;
  }
	
	public static String GetWebServicesProperty(String Key, String USname){
		try{		
			//FileReader reader = new FileReader(System.getProperty("user.dir")+"/src/main/resources/PropertyFiles/WebServices.properties");
			
			FileReader reader = new FileReader(System.getProperty("user.dir")+"/src/main/java/com/adp/autopay/automation/webservices/story.json/APIcmds/" + USname + ".txt");
			Properties properties = new Properties();
			properties.load(reader);
			return properties.getProperty(Key);
		}catch(Exception e){
			e.printStackTrace();
		}
	return null;
  }
	
	public static String GetProjectPath(String Path){
		try{
			return Path.replace("/target/classes", "");			
		}catch(Exception e){
			e.printStackTrace();
		}
		return Path;
	}
	
	public static String GetEnvironment(String Key){
        String RunAgainst = ExecutionContext.SRunAgainst;
                        try{                         
                                        if(RunAgainst == null){
                                                        RunAgainst = GetEnvironmentProperty("RunAgainst");
                                        }
                                        return RunAgainst;
                        }catch(Exception e){
                                        e.printStackTrace();
                        }
        return null;
}

	
}
