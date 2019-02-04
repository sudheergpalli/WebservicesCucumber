package com.adp.autopay.automation.pagespecificlibrary;

public class AutoItLogin implements Runnable 
{

	String cmdPath;
	public AutoItLogin(String command)
	{
		cmdPath = command;
		//cmdPath="cmd /c start \\\\cdlbenefitsqcre\\base\\PSEXEC\\PsExec.exe \\\\"+machineIP+" -i 1 -d -u ES\\hwseauto -p adpADP123 cmd /k C:\\temp\\LoginEmployee.exe "+sUserName;
	}
	
	public void run() 
	{
		//String[] cmd = { "C:\\temp\\LoginEmployee.exe", sUserName};
		 String cmd=cmdPath;
        Process p;
		try {
			System.out.println("AutoIT thread for login Started");
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 
}