#######################################################################
#Webservices test story - US378794_SpecialEffects
#User Story : US378794
#User Story Description : Special Effects GET , ADD , UPDATE and REMOVE services
#Author: Daniel Hued   # Date : 3/22/2016
#Reviewed By : 
#######################################################################
Feature: US378794_SpecialEffects

   @SE_GET_HOLIDAYS
   	Scenario: Testing Holidays/Special Effects GET feature Select Data - All Data
     When WEBSERVICES:When we run API command for "Holidays_GET" for "Success" it returns "200 OK" status, update payload = "NO"
   
   @SE_GET_SPECIALEFFECTS	
  	Scenario: Testing Special Effects GET feature by PSN & Select Data - All Data
     When WEBSERVICES:When we run API command for "SpecialEffects_GET" for "Success" it returns "200 OK" status, update payload = "NO"
   
  @SE_ADD_MODIFY_REMOVE
  Scenario Outline: Demo for ADD Special Effects API
    Given WEBSERVICES:We create a new API request for "SpecialEffects_ADD" for ClassName "com.adp.ea.payroll.v1.events.payrollprocessingschedule.projectiontemplate.modify.PayrollProcessingScheduleProjectionTemplateModifyEvent" with input excel sheet = "SpecialEffects" for row "<row_index>" data
    And WEBSERVICES:Create an array and add records using excel data for event 1 for row "<row_index>" data
      | ArrayName | DataForArray |
      | data.transform.payrollProcessingScheduleProjectionTemplate.includedConfigurationTags | Array_AddConfigurationTags |
    When WEBSERVICES:When We run the API request for "SpecialEffects_ADD" for "Success" it returns "200 OK" status for input excel sheet = "SpecialEffects" for row "<row_index>" data 
    Examples:
      |row_index|
      |ALL|
    
   @SE_ADD_MODIFY_REMOVE
  Scenario Outline: Demo for CHANGE Special Effects API
    Given WEBSERVICES:We create a new API request for "SpecialEffects_MODIFY" for ClassName "com.adp.ea.payroll.v1.events.payrollprocessingschedule.projectiontemplate.modify.PayrollProcessingScheduleProjectionTemplateModifyEvent" with input excel sheet = "SpecialEffects" for row "<row_index>" data
    And WEBSERVICES:Create an array and add records using excel data for event 1 for row "<row_index>" data
      | ArrayName | DataForArray |
      | data.transform.payrollProcessingScheduleProjectionTemplate.includedConfigurationTags | Array_ModifyConfigurationTags |
    When WEBSERVICES:When We run the API request for "SpecialEffects_MODIFY" for "Success" it returns "200 OK" status for input excel sheet = "SpecialEffects" for row "<row_index>" data 
    Examples:
      |row_index|
      |ALL|
      
  @SE_ADD_MODIFY_REMOVE
  Scenario Outline: Demo for REMOVE Special Effects API
    Given WEBSERVICES:We create a new API request for "SpecialEffects_DELETE" for ClassName "com.adp.ea.payroll.v1.events.payrollprocessingschedule.projectiontemplate.modify.PayrollProcessingScheduleProjectionTemplateModifyEvent" with input excel sheet = "SpecialEffects" for row "<row_index>" data
    And WEBSERVICES:Create an array and add records using excel data for event 1 for row "<row_index>" data
      | ArrayName | DataForArray |
      | data.transform.payrollProcessingScheduleProjectionTemplate.includedConfigurationTags | Array_RemoveConfigurationTags |
    When WEBSERVICES:When We run the API request for "SpecialEffects_DELETE" for "Success" it returns "200 OK" status for input excel sheet = "SpecialEffects" for row "<row_index>" data 
    Examples:
      |row_index|
      |ALL|
            
   @SE_GET_ERROR
	Scenario: Testing Holidays/Special Effects GET - Invalid Payroll Region
     When WEBSERVICES:When we run API command for "Holidays1_Error_GET" for "Failure" it returns "400" status, update payload = "NO"	
     Then WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response "Holidays1_Error_GET"  
     
   @SE_GET_ERROR
	Scenario: Testing Holidays/Special Effects GET - Invalid Requester in Requester Header
     When WEBSERVICES:When we run API command for "Holidays2_Error_GET" for "Failure" it returns "400" status, update payload = "NO"	
     Then WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response "Holidays2_Error_GET"  
     
   @SE_GET_ERROR
	Scenario: Testing Holidays/Special Effects GET - No Matching Records Found
     When WEBSERVICES:When we run API command for "Holidays3_Error_GET" for "Failure" it returns "400" status, update payload = "NO"	
     Then WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response "Holidays3_Error_GET"    
                        
   @SE_GET_ERROR
	Scenario: Testing Holidays/Special Effects GET feature - User Not Authenticated
     When WEBSERVICES:When we run API command for "Holidays4_Error_GET" for "Failure" it returns "400" status, update payload = "NO"	
     Then WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response "Holidays4_Error_GET" 
     
   @SE_GET_ERROR
	Scenario: Testing Holidays/Special Effects GET feature - Invalid Date String
     When WEBSERVICES:When we run API command for "Holidays5_Error_GET" for "Failure" it returns "400" status, update payload = "NO"	
     Then WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response "Holidays5_Error_GET" 
     
   @SE_GET_ERROR
	Scenario: Testing Holidays/Special Effects GET - OID/Company Code Combination Invalid
	 When WEBSERVICES:When we run API command for "Holidays6_Error_GET" for "Failure" it returns "400" status, update payload = "NO"	
     Then WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response "Holidays6_Error_GET"    
    
   @SE_SMOKE
	Scenario: Testing Holidays/Special Effects GET - OID/Company Code Combination Invalid
	 When WEBSERVICES:When we run API command for "Holidays6_Error_GET" for "Failure" it returns "400" status, update payload = "NO"	
     Then WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response "Holidays6_Error_GET"                          
                           
                                              