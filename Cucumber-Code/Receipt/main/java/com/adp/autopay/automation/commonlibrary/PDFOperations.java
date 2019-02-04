package com.adp.autopay.automation.commonlibrary;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;

public class PDFOperations{
	
	public static void DeleteFileifExist_PDForCSV(String ReportType,String FileName) throws IOException{
		String Extension = null;
		if(ReportType.equalsIgnoreCase("PDF")){
			Extension = ".pdf";
		}
		if(ReportType.equalsIgnoreCase("EXCEL") || ReportType.equalsIgnoreCase("CSV")){
			Extension = ".csv";
		}
		File file = new File("C:/Users/" + System.getProperty("user.name") + "/Downloads/"+FileName+Extension);
		if(file.exists()){
			file.delete();
			System.out.println("*** previous file deleted");
			return;
		}
		System.out.println("*** file doesnot existed before, you can save the file");
		return;
	}
	
	public static boolean SavePDForCSVToStandardLocation(String FileName,String ReportType) throws IOException{
		String Extension = null;
		if(ReportType.equalsIgnoreCase("PDF")){
			Extension = ".pdf";
		}
		if(ReportType.equalsIgnoreCase("EXCEL") || ReportType.equalsIgnoreCase("CSV")){
			Extension = ".csv";
		}
		File file_src = new File("C:/Users/" + System.getProperty("user.name") + "/Downloads/"+FileName+Extension);
		File file_dest = new File("C:/Temp/"+FileName+Extension);
		if(file_src.exists()){
			FileUtils.copyFile(file_src, file_dest);
			return true;
		}
		return false;
	}
	public static boolean VerifyPDF(String PDFFileName , String Field) throws IOException{
			
			String pdfFilePath = "C:/Temp/"+PDFFileName;
			Document pdf = PDF.open(pdfFilePath);
			StringBuilder text = new StringBuilder(1024);
			pdf.pipe(new OutputTarget(text));
			pdf.close();
			String TextFromFile = text.toString();
			if(TextFromFile.contains(Field)){
				System.out.println("*** "+Field+" Found in the PDF");
				return true;
			}
			else{
				System.out.println("*** "+Field+" Not Found in the PDF");
				return false;
			}
	}
}
