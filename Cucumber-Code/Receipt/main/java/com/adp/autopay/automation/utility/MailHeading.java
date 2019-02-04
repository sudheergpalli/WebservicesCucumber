package com.adp.autopay.automation.utility;

import java.awt.*;
import java.applet.*;
// import an extra class for the ActionListener
import java.awt.event.*;

// Tells the applet you will be using the ActionListener methods.
//This program is for Customized Header for the mail.
@SuppressWarnings("serial")
public class MailHeading extends Applet implements ActionListener
{

	static String Heading = null;
	
    Button ThisIsMyMailHeading;
    TextField MailHeaderData;
     
     public void init() 
     {
  // Now we will use the Flow Layout
    	setLayout( new FlowLayout()); 
    	this.setSize(500,100);
    	
    	ThisIsMyMailHeading = new Button("SET AS MY MAIL HEADING");
        MailHeaderData = new TextField("",50);
        
        add(MailHeaderData);
        this.MailHeaderData.setLocation(250,75);
        add(ThisIsMyMailHeading); 
        this.MailHeaderData.setLocation(250,150);
        
        
  // Attach actions to the components
          MailHeaderData.addActionListener(this);
          ThisIsMyMailHeading.addActionListener(this);   
     }
     
     

 // Here we will read the results of our actions to pass @ Before Suite
        	 public void actionPerformed(ActionEvent evt) 
        	 {
              if (evt.getSource() == ThisIsMyMailHeading){
            	  String ThisIsMyMailHeader = MailHeaderData.getText();
            	  Heading = ThisIsMyMailHeader;
            	  System.out.println("Entered Mail Heading is : "+ ThisIsMyMailHeader);
            	  System.exit(0);
            	  
              }
           }
 
        	 public static String GetCustomizedHeader(){
        	     	return Heading;
        	     }
        	 
        
     } 

