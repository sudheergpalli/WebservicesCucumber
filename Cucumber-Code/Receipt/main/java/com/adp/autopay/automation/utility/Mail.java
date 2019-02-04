package com.adp.autopay.automation.utility;

import com.adp.autopay.automation.cucumberRunner.RunCukesByCompositionTest;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Mail {	
	
	@SuppressWarnings("deprecation")
	public static void SendEmail(String To,String From,String CC,String ExecutionSuiteID) {		
		Properties props = new Properties();
		props.put("mail.smtp.host", "mailrelay.nj.adp.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "25");		
		//System.err.println("http://cdlbenefitsqcre/AutomationWeb/Results?suiteId="+ExecutionSuiteID);

		System.err.println( PropertiesFile.GetEnvironmentProperty("RESULTS_APP_SERVER_PATH") + "="+ExecutionSuiteID);
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.INTERNET_EXPLORER_9);
//		FirefoxDriver driver = new FirefoxDriver();
//		driver.get("http://localhost:32323/Results?suiteId=SUITE_20140323_224454_395");	
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(1000, TimeUnit.SECONDS);
		driver.get(PropertiesFile.GetEnvironmentProperty("RESULTS_APP_SERVER_PATH") + "="+ExecutionSuiteID);
		String innerHtml1 = driver.getPageSource();

		innerHtml1 += "<br><br><h2 style=\"color:#DC143C;\">\n\n\n\n\n\n\n\n Thanks,</h2>";
		innerHtml1 += "<b style=\"color:#DC143C;\">Greenbox Automation Team</b><br>";
		//innerHtml1 += "<br><br><b style=\"font-size:125%;color:Red;\">Please reach out Automation team with questions</b><br>";

		//System.out.println(innerHtml1);
		Session session = Session.getInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("username","password");
				}
			}); 
		try { 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(From));			
			if(PropertiesFile.SendMail.equalsIgnoreCase("True")){
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(To));				
				if((PropertiesFile.CC != null)){
					message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(CC));
				}
			}
			String SuiteName = RunCukesByCompositionTest.SuiteName;
			if(SuiteName == null){
				SuiteName = "Execution From Feature";
			}
//			String Environment = RunStory.Environment; +"("+Environment+")";
			message.setSubject("Execution Summary: " + SuiteName);
			//message.setContent(innerHtml1 + "\n\n please find the link for complete results " , "text/html");
			message.setContent(innerHtml1, "text/html");
			Transport.send(message);
			System.err.println("Mail Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	}

