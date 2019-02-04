package com.adp.autopay.automation.webservices;

import com.jayway.restassured.response.Header;

import java.util.ArrayList;

public class WebServiceParameters
{
  String strTestName;
  String strURI;
  String strRequestType;
  ArrayList<Header> headers;
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public WebServiceParameters(String[] arrParameterData)
  {
    this.strTestName = arrParameterData[0];
    this.strURI = arrParameterData[1];
    if (arrParameterData.length == 2)
    {
      this.strRequestType = "GET";
      this.headers = null; return;
    }
    int iTemp;
    
    if (arrParameterData[2].split("=").length == 0)
    {
      this.strRequestType = "GET";
      iTemp = 2;
    }
    else
    {
      this.strRequestType = arrParameterData[2];
      if (arrParameterData.length == 3)
      {
        this.headers = null;
        return;
      }
      iTemp = 3;
    }
    this.headers = new ArrayList();
      for (int j = iTemp; j < arrParameterData.length ; j++)
    {
      String[] arrTemp = arrParameterData[j].split("=");
      this.headers.add(new Header(arrTemp[0], arrTemp[1]));
    }
  }
  
  public String getStrTestName()
  {
    return this.strTestName;
  }
  
  public String getStrURI()
  {
    return this.strURI;
  }
  
  public String getStrRequestType()
  {
    return this.strRequestType;
  }
  
  public ArrayList<Header> getHeaders()
  {
    return this.headers;
  }
}
