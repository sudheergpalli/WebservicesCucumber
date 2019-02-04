package com.adp.autopay.automation.utility;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cucumber.runtime.java.ObjectContainer;

public class XmlParser {



	ExecutionContext context;

	public XmlParser()
	{
		this.context = ((ExecutionContext) ObjectContainer.getInstance(ExecutionContext.class));
	}


	@SuppressWarnings({ "unused", "unchecked" })
	public HashMap<String, String> Xmlparser(String Chain,String PPID,String Elementtype,File FileSrc,String Filename) {

		HashMap<String, String> Tagvalues = new HashMap<>();
		try 
		{
			String Defualtchain = "//ACAEnvelope//ACABody//EmployeeBenefits";
			String Elements[] =Elementtype.split(",");
			FileFilter fileFilter = new WildcardFileFilter(Filename+"*.xml");
			File files[] = FileSrc.listFiles(fileFilter);

			if(files.length == 0)
			{

				int filecount = files.length-1;
				SAXReader reader = new SAXReader();
				Document document = reader.read(files[filecount]);

				System.out.println("Root element :------------------------------------------" + document.getRootElement().getName());

				Element classElement = document.getRootElement();
				List<Node> nodes = document.selectNodes(Defualtchain);
				System.out.println("-----------------------------------------------------------------------------------------------------------");

				for (Node node : nodes) 
				{

					if(PPID.equalsIgnoreCase(node.selectSingleNode("EmployeeID").getText()))
					{

						if(Chain == Defualtchain)
						{


							for(int i=0;i<Elements.length;i++)
							{
								Tagvalues.put(PPID+"_"+Elements[i],node.selectSingleNode(Elements[i]).getText());
								System.out.println("Key :"+PPID+"_"+Elements[i]+" :: Value :"+node.selectSingleNode(Elements[i]).getText());
								System.out.println("-----------------------------------------------------------------------------------------------------------");
							}
							break;

						}

						else
						{
							List<Node> childnodes =	document.selectNodes("//ACAEnvelope//ACABody//EmployeeBenefits"+Chain);

							for (Node Innernodes : childnodes) 
							{

								for(int i=0;i<Elements.length;i++)
								{
									Tagvalues.put(PPID+"_"+Elements[i],Innernodes.selectSingleNode(Elements[i]).getText());
									System.out.println("Key :"+PPID+"_"+Elements[i]+" :: Value :"+Innernodes.selectSingleNode(Elements[i]).getText());
									System.out.println("-----------------------------------------------------------------------------------------------------------");
								}
								break;

							}
						}

					}
					else
					{
						System.out.println("ghii");
					}
				}
			}
			else
			{
				System.err.println("File Not Exits");
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return Tagvalues;

	}

	public static void displayFiles(File[] files) {
		System.out.println(files[0].getName());

		for (File file : files) {
			System.out.println(file.getName());
		}
	}
	public static void main(String args[])
	{
		

	}
}
