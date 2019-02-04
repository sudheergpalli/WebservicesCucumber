package com.adp.autopay.automation.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;


public class ReadEmails {

	String strSubject;
	Address[] fromAddresses;
	Address[] toAddresses;
	String strBody;

	public ReadEmails(String strSub, Address[] from, Address[] to, String strBody) {
		this.strSubject = strSub;
		this.fromAddresses = from;
		this.toAddresses = to;
		this.strBody = strBody;
	}

	public static List<ReadEmails> getEmails(String strUserName, String strPassword, String strSubjectContains, String strFromContains, Date minDate){
		if(strUserName == null || strPassword == null){
			System.out.print("Username and Password are required to read the emails.");
			return null;
		}
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", "mailrelay.nj.adp.com");

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties, null);

		// Get a Store object that implements the specified protocol.
		Store store;
		try {
			store = session.getStore("imap");
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			System.out.print("Caught an exception while initiating emails store");
			e.printStackTrace();
			return null;
		}

		//Connect to the current host using the specified username and password.
		try {
			store.connect("webmail1.es.ad.adp.com", strUserName, strPassword);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			System.out.print("Caught an exception while connecting to Email, please check the Username and Password;");
			e.printStackTrace();
			try {
				store.close();
			} catch (MessagingException e1) {}
			return null;
		}

		//Create a Folder object corresponding to the given name.
		Folder folder = null;
		try {
			folder = store.getFolder("inbox");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			System.out.print("Caught an exception while opening the Inbox;");
			e.printStackTrace();
			return null;
		}

		// Open the Folder.
		try {
			folder.open(Folder.READ_ONLY);
			Message[] message = folder.getMessages();

			//Folder copyFolder = store.getFolder("inbox/Processed")
			List<ReadEmails> list = new ArrayList<ReadEmails>();
			// Display message.
			for (int i = 0; i < message.length; i++) {
				if(strFromContains !=null)
					if(!message[i].getFrom()[0].toString().contains(strFromContains))
						continue;
				if(strSubjectContains !=null)
					if(!message[i].getSubject().contains(strSubjectContains))
						continue;
				if(minDate != null)
					if(!message[i].getReceivedDate().after(minDate))
						continue;
				list.add(new ReadEmails(message[i].getSubject(), message[i].getFrom(), message[i].getAllRecipients(), message[i].getContent().toString()));
				//message[i].setFlag(Flags.Flag.DELETED, true); // Needs to removed when you are performing any other functions
			}

			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print("Caught an exception while reading the emails;");
			try {
				folder.close(true);
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				store.close();
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null;
		}
	}

	// Getters, Setters
	public String getStrSubject() {
		return strSubject;
	}

	public void setStrSubject(String strSubject) {
		this.strSubject = strSubject;
	}

	public Address[] getFromAddresses() {
		return fromAddresses;
	}

	public void setFromAddresses(Address[] fromAddresses) {
		this.fromAddresses = fromAddresses;
	}

	public Address[] getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses(Address[] toAddresses) {
		this.toAddresses = toAddresses;
	}

	public String getStrBody() {
		return strBody;
	}

	public void setStrBody(String strBody) {
		this.strBody = strBody;
	}


}
