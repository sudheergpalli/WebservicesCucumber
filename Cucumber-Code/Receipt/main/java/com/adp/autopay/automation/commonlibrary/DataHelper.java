package com.adp.autopay.automation.commonlibrary;

/**
 * Created by gadiv on 2/5/2016.
 */

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHelper {
    public static HashMap<String,String> storeValues = new HashMap();
    public static List<HashMap<String,List<String>>> data(String filepath,String sheetName)
    {

        List<HashMap<String,List<String>>> mydata = new ArrayList<>();
        try
        {
            FileInputStream fs = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(fs);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            Row HeaderRow = sheet.getRow(0);
            HashMap<String,List<String>> currentHash = new HashMap<String,List<String>>();

            for(int i=1;i<sheet.getPhysicalNumberOfRows();i++)
            {
                Row currentRow = sheet.getRow(i);


                for(int j=0;j<currentRow.getPhysicalNumberOfCells();j++)
                {
                    String  header = HeaderRow.getCell(j).getStringCellValue();
                    Cell currentCell = currentRow.getCell(j);
                    switch (currentCell.getCellType())
                    {
                        case Cell.CELL_TYPE_STRING:
                            //Mahesh System.out.print(currentCell.getStringCellValue() + "\t");
                            //currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue());
                            if(currentHash.get(header) == null)
                                currentHash.put(header, new ArrayList<String>());
                            currentHash.get(header).add(currentCell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            //Mahesh System.out.print(currentCell.getStringCellValue() + "\t");
                            //currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue());
                            if(currentHash.get(header) == null)
                                currentHash.put(header, new ArrayList<String>());
                            currentHash.get(header).add(String.valueOf(currentCell.getNumericCellValue()));
                            break;
                    }
                }


            }
            mydata.add(currentHash);
            fs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mydata;
    }

    public static Map<Integer,Map<String, List<DataModel>>> dataModels(String filepath,String sheetName)
    {

    	Map<Integer,Map<String, List<DataModel>>> currentHash = new HashMap<Integer,Map<String, List<DataModel>>>();
        try
        {
            FileInputStream fs = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(fs);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            Row HeaderRow = sheet.getRow(0);
//            Map<Integer, List<DataModel>> innerMap = new HashMap<Integer, List<DataModel>>();

            for(int i=1;i<sheet.getPhysicalNumberOfRows();i++)
            {
                Row currentRow = sheet.getRow(i);

                String arraNo = null;
                for(int j=0;j<currentRow.getPhysicalNumberOfCells();j++)
                {
                    String  header = HeaderRow.getCell(j).getStringCellValue();
                    Cell currentCell = currentRow.getCell(j);
                    DataModel model = new DataModel();
                    switch (currentCell.getCellType())
                    {
                        case Cell.CELL_TYPE_STRING:
                            //Mahesh System.out.print(currentCell.getStringCellValue() + "\t");
                            //currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue());
                        	
                        	if(header.equalsIgnoreCase("ArrayNo")) {
                        		arraNo = currentCell.getStringCellValue();
                        	} 
                        	
                        	if(!header.equalsIgnoreCase("ArrayNo")) {
                        		String value = currentCell.getStringCellValue();
                        		List<String> valuesList = new ArrayList<String>();
                        		if(value.contains(";")) {
                        			String[] valuesArray = value.split(";");                            		
                            		
                            		if(valuesArray != null && valuesArray.length > 0) {                        			
                                		for (String string : valuesArray) {
                                			if(!string.isEmpty())
                                				valuesList.add(string);
        								}
                            		}
                        		}
                        		
                        		
                        		if(currentHash.get(i) == null)
                                    currentHash.put(i, new HashMap<String, List<DataModel>>());
                                model.setKey(header);
                                if(valuesList.size() == 0) {
                                	model.setValue(currentCell.getStringCellValue());
                                }
                                
                                
                                if(currentHash.get(i).get(arraNo) == null) {
                                	currentHash.get(i).put(arraNo, new ArrayList<DataModel>());                                	
                                }
                                
                                if(valuesList.size() > 0) {
                                	List<DataModel> existingModelsList = currentHash.get(i).get(arraNo);
                                	model.setKey(arraNo);
                                	ListModel listModel = new ListModel();
                                	listModel.setKey(header);
                                	listModel.setValue(valuesList);
                                	if(existingModelsList != null && existingModelsList.size() > 0) {
                                		int index = existingModelsList.indexOf(model);
                                    	
                                    	if(index > -1) {
                                    		DataModel existingModel = existingModelsList.get(index);
                                    		existingModel.getListModels().add(listModel);
                                    	}
                                	} else {
                                		model.getListModels().add(listModel);
                                		existingModelsList.add(model);
                                	}
                                	
                            	} else {
                            		currentHash.get(i).get(arraNo).add(model);
                            	}  
                        	}
                            
                            
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            //Mahesh System.out.print(currentCell.getStringCellValue() + "\t");
                            //currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue());
                        	if(header.equalsIgnoreCase("ArrayNo")) {
                        		arraNo = String.valueOf(currentCell.getNumericCellValue());
                        	}
                        	if(!header.equalsIgnoreCase("ArrayNo")) {
                        		if(currentHash.get(i) == null)
                                    currentHash.put(i, new HashMap<String, List<DataModel>>());
                                model.setKey(header);
                                model.setValue(currentCell.getNumericCellValue());
                                
                                if(currentHash.get(i).get(arraNo) == null) {
                                	currentHash.get(i).put(arraNo, new ArrayList<DataModel>());
                                } 
                                
                                currentHash.get(i).get(arraNo).add(model);
                        	}
                            
                            
                            break;
                    }
                }


            }
            fs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return currentHash;
    }
    
    public static List<DataObject> dataList(String filepath,String sheetName)
    {

        List<DataObject> mydata = new ArrayList<DataObject>();
        try
        {
            FileInputStream fs = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(fs);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            Row HeaderRow = sheet.getRow(0);
            HashMap<String,List<String>> currentHash = new HashMap<String,List<String>>();

            for(int i=1;i<sheet.getPhysicalNumberOfRows();i++)
            {
                Row currentRow = sheet.getRow(i);

                DataObject dataObject = new DataObject();
                for(int j=0;j<currentRow.getPhysicalNumberOfCells();j++)
                {
                    String  header = HeaderRow.getCell(j).getStringCellValue();
                    Cell currentCell = currentRow.getCell(j);
                    if(currentCell != null) {
                    	//System.out.println("currentCell : " + currentCell + " : header : " + header + " : currentRow : " + currentRow);
                        int cellType = currentCell.getCellType();
                        switch (cellType)
                        {

                            case Cell.CELL_TYPE_STRING:
                               //Mahesh  System.out.println(currentCell.getStringCellValue() + "\t" + " : currentRow : " + HeaderRow.getCell(j).getStringCellValue());
                                //currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue());

                                dataObject.setRowNum(currentRow.getRowNum());
                                if(currentHash.get(header) == null)
                                    currentHash.put(header, new ArrayList<String>());
                                currentHash.get(header).add(currentCell.getStringCellValue());

                                if(header.equalsIgnoreCase("BaseUrl")) {
                                    dataObject.setBaseUrl(currentCell.getStringCellValue());
                                } else if(header.equalsIgnoreCase("ServiceName")) {
                                    dataObject.setServiceName(currentCell.getStringCellValue());
                                } else if(header.equalsIgnoreCase("serverName")) {
                                    dataObject.setServerName(currentCell.getStringCellValue());
                                } /*else if(header.equalsIgnoreCase("URL_region")) {
                                    dataObject.setRegionId(currentCell.getStringCellValue());
                                }*/else if(header.equalsIgnoreCase("UserStory")) {
                                    dataObject.setUserStory(currentCell.getStringCellValue());
                                }else if(header.equalsIgnoreCase("TestDescription")) {
                                    dataObject.settestDescription(currentCell.getStringCellValue());
                                }/*else if(header.equalsIgnoreCase("URL_orgoid")) {
                                    dataObject.setOrgId(currentCell.getStringCellValue());
                                }else if(header.equalsIgnoreCase("URL_company")) {
                                    dataObject.setCompanyCode(currentCell.getStringCellValue());
                                }*/else if(header.equalsIgnoreCase("HttpMethod")) {
                                    dataObject.setHttpMethod(currentCell.getStringCellValue());
                                }else if(header.equalsIgnoreCase("headerParams")) {
                                    dataObject.setHeaderParams(currentCell.getStringCellValue());
                                }else if(header.equalsIgnoreCase("Environment")) {
                                    dataObject.setEnvironment(currentCell.getStringCellValue());
                                }/*else if(header.equalsIgnoreCase("URL_itemID")) {
                                    dataObject.setItemId(currentCell.getStringCellValue());
                                }*/else if(header.equalsIgnoreCase("ADP-UserID")) {
                                    dataObject.setADP_UserID(currentCell.getStringCellValue());
                                  //  dataObject.setSm_serversessionid(currentCell.getStringCellValue());
                                }else if(header.equalsIgnoreCase("serversessionid")) {
                                    dataObject.setSm_serversessionid(currentCell.getStringCellValue());
                                }else if(header.equalsIgnoreCase("associateOID")) {
                                    dataObject.setAssociateOID(currentCell.getStringCellValue());
                                }else if(header.equalsIgnoreCase("ORGOID")) {
                                    dataObject.setORGOID(currentCell.getStringCellValue());
                                } else if(header.contains("Header")) {
                                	//System.out.println(":header : " + header + " : currentCell.getStringCellValue() : " + currentCell.getStringCellValue());
                                	String[] headersStr = header.split("#");
                                	
                                	if(dataObject.getHeaders() == null) {
                                		dataObject.setHeaders(new ArrayList<String>());
                                	}
                                    if (currentCell.getStringCellValue().equalsIgnoreCase("*BLANK*")){
                                       // dataObject.getHeaders().add(headersStr[1] + "=" + "");
                                    }
                                    else
                                    {
                                        dataObject.getHeaders().add(headersStr[1] + "=" + currentCell.getStringCellValue());
                                    }

                                    //dataObject.getHeaders().add(headersStr[1] + "=" + currentCell.getStringCellValue());
                                } else if(header.contains("URL")) {
                                	//System.out.println(":header : " + header + " : currentCell.getStringCellValue() : " + currentCell.getStringCellValue());
                                	String[] UrlParamNames = header.split("#");
                                	
                                	if(dataObject.getUrlParams() == null) {
                                		dataObject.setUrlParams(new HashMap<String, String>());
                                	}
                                    dataObject.getUrlParams().put(UrlParamNames[1], currentCell.getStringCellValue());
                                } else if(header.equalsIgnoreCase("UserErrorCode")) {
                                	//System.out.println(":header : " + header + " : currentCell.getStringCellValue() : " + currentCell.getStringCellValue());
                                	String[] errorCodes = currentCell.getStringCellValue().split("#");
                                	
                                	if(dataObject.getErrorCodes() == null) {
                                		dataObject.setErrorCodes(new ArrayList<String>());
                                	}
                                	for (String string : errorCodes) {
                                		dataObject.getErrorCodes().add(string);
									}
                                    
                                } else if(header.equalsIgnoreCase("UserErrorDesc")) {
                                	//System.out.println(":header : " + header + " : currentCell.getStringCellValue() : " + currentCell.getStringCellValue());
                                	String[] errorCodes = currentCell.getStringCellValue().split("#");
                                	
                                	if(dataObject.getErrorDescriptions() == null) {
                                		dataObject.setErrorDescriptions(new ArrayList<String>());
                                	}
                                	for (String string : errorCodes) {
                                		dataObject.getErrorDescriptions().add(string);
									}
                                }
                                else if(header.equalsIgnoreCase("DevErrorCode")) {
                                	//System.out.println(":header : " + header + " : currentCell.getStringCellValue() : " + currentCell.getStringCellValue());
                                	String[] deverrorCodes = currentCell.getStringCellValue().split("#");
                                	
                                	if(dataObject.getdevErrorCodes() == null) {
                                		dataObject.setdevErrorCodes(new ArrayList<String>());
                                	}
                                	for (String string : deverrorCodes) {
                                		dataObject.getdevErrorCodes().add(string);
									}
                                    
                                }
                                else if(header.equalsIgnoreCase("DevErrorDesc")) {
                                	//System.out.println(":header : " + header + " : currentCell.getStringCellValue() : " + currentCell.getStringCellValue());
                                	String[] devErrorDescriptions = currentCell.getStringCellValue().split("#");
                                	
                                	if(dataObject.getdevErrorDescriptions() == null) {
                                		dataObject.setdevErrorDescriptions(new ArrayList<String>());
                                	}
                                	for (String string : devErrorDescriptions) {
                                		dataObject.getdevErrorDescriptions().add(string);
									}
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                            	dataObject.setRowNum(currentRow.getRowNum());
                                if(currentHash.get(header) == null)
                                    currentHash.put(header, new ArrayList<String>());
                                currentHash.get(header).add(String.valueOf(currentCell.getNumericCellValue()));

                                if(header.equalsIgnoreCase("URL_region")) {
                                    dataObject.setRegionId(String.valueOf(currentCell.getNumericCellValue()));
                                }else if(header.equalsIgnoreCase("UserStory")) {
                                    dataObject.setUserStory(String.valueOf(currentCell.getNumericCellValue()));
                                }else if(header.equalsIgnoreCase("URL_orgoid")) {
                                    dataObject.setOrgId(String.valueOf(currentCell.getNumericCellValue()));
                                }else if(header.equalsIgnoreCase("URL_company")) {
                                    dataObject.setCompanyCode(String.valueOf(currentCell.getNumericCellValue()));
                                }else if(header.equalsIgnoreCase("Environment")) {
                                    dataObject.setEnvironment(String.valueOf(currentCell.getNumericCellValue()));
                                }else if(header.equalsIgnoreCase("URL_ItemID")) {
                                    dataObject.setItemId(String.valueOf(currentCell.getNumericCellValue()));
                                }else if(header.equalsIgnoreCase("Header_sm_serversessionid")) {
                                    dataObject.setSm_serversessionid(String.valueOf(currentCell.getNumericCellValue()));
                                }else if(header.equalsIgnoreCase("Header_associateOID")) {
                                    dataObject.setAssociateOID(String.valueOf(currentCell.getNumericCellValue()));
                                }else if(header.equalsIgnoreCase("Header_ORGOID")) {
                                    dataObject.setORGOID(String.valueOf(currentCell.getNumericCellValue()));
                                } else if(header.contains("Header")) {
                                	//System.out.println(":header : " + header + " : currentCell.getStringCellValue() : " + currentCell.getNumericCellValue());
                                	String[] headersStr = header.split("#");
                                	
                                	if(dataObject.getHeaders() == null) {
                                		dataObject.setHeaders(new ArrayList<String>());
                                	}
                                    dataObject.getHeaders().add(headersStr[1] + "=" + String.valueOf(currentCell.getNumericCellValue()));
                                }else if(header.contains("URL")) {
                                	//System.out.println(":header : " + header + " : currentCell.getStringCellValue() : " + currentCell.getStringCellValue());
                                	String[] UrlParamNames = header.split("#");
                                	
                                	if(dataObject.getUrlParams() == null) {
                                		dataObject.setUrlParams(new HashMap<String, String>());
                                	}
                                    dataObject.getUrlParams().put(UrlParamNames[1], String.valueOf(currentCell.getNumericCellValue()));
                                }
                                break;
                    }
                    
                }
            }
                mydata.add(dataObject);

        }

            fs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mydata;
    }
}

