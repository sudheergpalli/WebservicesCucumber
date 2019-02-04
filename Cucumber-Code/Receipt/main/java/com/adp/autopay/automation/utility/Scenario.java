package com.adp.autopay.automation.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Scenario
{
	public static ArrayList<String> GetScenarioFromStoryFile(String Filepath){
		try{
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(Filepath));
			String nextLine;
			int start = 0;
			int length = 0;
			
			ArrayList<String> ScenarioName = new ArrayList<String>();
			  while ((nextLine = br.readLine())!= null){
		        if (nextLine.startsWith("Scenario:")) {
		        	length = length +1 ;
		        	String Line = nextLine;
		        	if(nextLine.startsWith("Scenario:")){
		        		start = "Scenario:".length();
		        	}
//		        	else if(nextLine.startsWith("!--Scenario:")){
//		        		start = "!--Scenario:".length();
//		        	}
//		        	else if(nextLine.startsWith("!-- Scenario:")){
//		        		start = "!-- Scenario:".length();
//		        	}
		        	int end = (Line.length());
		        	String Scenario = Line.substring(start, end).trim();
		        	if(Scenario.length() == 0){
		        		Scenario = Integer.toString(length);
		        	}
		        	ScenarioName.add(Scenario);
		          }
		        }
	         return ScenarioName;
		}catch (FileNotFoundException e1) {
			     e1.printStackTrace();
		} catch (IOException ep) {
			     ep.printStackTrace();
		}
		return null;
	}
	
	public static String GetScenario(ArrayList<String> ScenarioNameList,int ScenarioNumber){
		try{
			ArrayList<String> ScenarioNames = ScenarioNameList;
			return ScenarioNames.get(ScenarioNumber);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
