#######################################################################
#Webservices test story - US999999_POC_generaldeduction
#User Story : US999999
#User Sory Description : Cucumber Framework POC for GET , ADD , UPDATE and REMOVE services
#Author: Mahesh Gadi   # Date : 2/23/2016
#Reviewed By : Mahesh Gadi # Date : 2/24/2016
#######################################################################
@GenDedSmoke
Feature: US999999_POC_generaldeduction

  @List+Ve
    Scenario: Sample for executing GET service and do a compare with baseline -- GET Service
    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"

    @List-Ve
    Scenario: GET service -ve testing and verify error codes for General Deductions API for Smoke Test
    When WEBSERVICES:When we run API command for "Get_GenDeductionsForError" for "Failure" it returns "400" status, update payload = "NO"
    Then WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response "Get_GenDeductionsForError"
   
   @SE_ADD
  Scenario Outline: Demo for ADD Special Effects API
    Given WEBSERVICES:We create a new API request for "SpecialEffects_ADD" for ClassName "com.adp.ea.payroll.v1.events.payrollprocessingschedule.projectiontemplate.modify.PayrollProcessingScheduleProjectionTemplateModifyEvent" with input excel sheet = "SpecialEffects" for row "<row_index>" data
    And WEBSERVICES:Create an array and add records using excel data for event 1 for row "<row_index>" data
      | ArrayName | DataForArray |
      | data.transform.payrollProcessingScheduleProjectionTemplate.includedConfigurationTags | Array_AddConfigurationTags |
    When WEBSERVICES:When We run the API request for "SpecialEffects_ADD" for "Success" it returns "200 OK" status for input excel sheet = "SpecialEffects" for row "<row_index>" data 
    Examples:
      |row_index|
      |2|
    
   @SE_MODIFY
  Scenario Outline: Demo for CHANGE Special Effects API
    Given WEBSERVICES:We create a new API request for "SpecialEffects_MODIFY" for ClassName "com.adp.ea.payroll.v1.events.payrollprocessingschedule.projectiontemplate.modify.PayrollProcessingScheduleProjectionTemplateModifyEvent" with input excel sheet = "SpecialEffects" for row "<row_index>" data
    And WEBSERVICES:Create an array and add records using excel data for event 1 for row "<row_index>" data
      | ArrayName | DataForArray |
      | data.transform.payrollProcessingScheduleProjectionTemplate.includedConfigurationTags | Array_ModifyConfigurationTags |
    When WEBSERVICES:When We run the API request for "SpecialEffects_MODIFY" for "Success" it returns "200 OK" status for input excel sheet = "SpecialEffects" for row "<row_index>" data 
    Examples:
      |row_index|
      |2|
      
  @SE_REMOVE
  Scenario Outline: Demo for REMOVE Special Effects API
    Given WEBSERVICES:We create a new API request for "SpecialEffects_DELETE" for ClassName "com.adp.ea.payroll.v1.events.payrollprocessingschedule.projectiontemplate.modify.PayrollProcessingScheduleProjectionTemplateModifyEvent" with input excel sheet = "SpecialEffects" for row "<row_index>" data
    And WEBSERVICES:Create an array and add records using excel data for event 1 for row "<row_index>" data
      | ArrayName | DataForArray |
      | data.transform.payrollProcessingScheduleProjectionTemplate.includedConfigurationTags | Array_RemoveConfigurationTags |
    When WEBSERVICES:When We run the API request for "SpecialEffects_DELETE" for "Success" it returns "200 OK" status for input excel sheet = "SpecialEffects" for row "<row_index>" data 
    Examples:
      |row_index|
      |2|
         
           
    @GenDed_ADD_UPDATE_REMOVE
    Scenario Outline: General Deductions API - Smoke Test for ADD Service
    Given WEBSERVICES:We create a new API request for "Add_GenDeductions" for ClassName "com.adp.ea.payroll.v1.events.generaldeductionconfiguration.add.GeneralDeductionConfigurationAddEvent" with input excel sheet = "Add_GenDeductions" for row "<row_index>" data  
    When WEBSERVICES:When We run the API request for "Add_GenDeductions" for "Success" it returns "200 OK" status for input excel sheet = "Add_GenDeductions" for row "<row_index>" data
    Examples:
      |row_index|
      |ALL|
         
    @GenDed_ADD_UPDATE_REMOVE
    Scenario Outline: General Deductions API - Smoke Test for Update Service
    Given WEBSERVICES:We create a new API request for "Update_GenDeductions" for ClassName "com.adp.ea.payroll.v1.events.generaldeductionconfiguration.change.GeneralDeductionConfigurationChangeEvent" with input excel sheet = "Change_GenDeductions" for row "<row_index>" data  
    When WEBSERVICES:When We run the API request for "Update_GenDeductions" for "Success" it returns "200 OK" status for input excel sheet = "Change_GenDeductions" for row "<row_index>" data
     Examples:
      |row_index|
      |ALL|
          
    @GenDed_ADD_UPDATE_REMOVE
    Scenario Outline: General Deductions API - Smoke Test for Remove Service
    Given WEBSERVICES:We create a new API request for "Remove_GenDeductions" for ClassName "com.adp.ea.payroll.v1.events.generaldeductionconfiguration.remove.GeneralDeductionConfigurationRemoveEvent" with input excel sheet = "Remove_GenDeductions" for row "<row_index>" data  
    When WEBSERVICES:When We run the API request for "Remove_GenDeductions" for "Success" it returns "200 OK" status for input excel sheet = "Remove_GenDeductions" for row "<row_index>" data
     Examples:
      |row_index|
      |ALL|
      
   
     @AddMemosNew
    Scenario Outline: Add Memos API Testing
    Given WEBSERVICES:We create a new API request for "Add_MyMemos" for ClassName "com.adp.ea.payroll.v1.events.memoconfiguration.add.MemoConfigurationAddEvent" with input excel sheet = "AddMemoSimple" for row "<row_index>" data
    And WEBSERVICES:Create an array and add records using excel data for event 1 for row "<row_index>" data
      | ArrayName | DataForArray |
      | data.transform.memoConfiguration.payrollAccumulators | Array_payrollAccumulators |
      | data.transform.memoConfiguration.payrollAccumulators[0].configurationTags | Array_configurationTags |
      | data.transform.memoConfiguration.payrollAccumulators[1].configurationTags | Array_configurationTags |
    When WEBSERVICES:When We run the API request for "Add_MyMemos" for "Success" it returns "200 OK" status for input excel sheet = "AddMemoSimple" for row "<row_index>" data
     Examples:
      |row_index|
      |2|
      |3|
         
  @CompareGet1
  Scenario: Sample for executing GET service and do a compare with baseline -- GET Service
    When WEBSERVICES:When we run API command for "Get_GenDeductions1" for "Success" it returns "200 OK" status, update payload = "NO"
    Then WEBSERVICES:Compare "Get_GenDeductions1" JSON file with "GetBenefitEvents_baseline" JSON file
    
       
    @IPAY
    Scenario Outline: BulkDownload  API - Smoke Test for bulk download Service
    Given WEBSERVICES:We create a new API request for "BulkDownload" for ClassName "com.adp.ea.payroll.v1.events.generaldeductionconfiguration.add.GeneralDeductionConfigurationAddEvent" with input excel sheet = "BulkDownload" for row "<row_index>" data  
    When WEBSERVICES:When We run the API request for "BulkDownload" for "Success" it returns "200 OK" status for input excel sheet = "BulkDownload" for row "<row_index>" data
    Examples:
      |row_index|
      |2|
      
   
    @Smoke
    Scenario Outline: General Deductions API - Smoke Test for ADD Service
    Given WEBSERVICES:We create a new API request for "Add_GenDeductions" for ClassName "com.adp.ea.payroll.v1.events.generaldeductionconfiguration.add.GeneralDeductionConfigurationAddEvent" with input excel sheet = "Add_GenDeductions" for row "<row_index>" data  
    When WEBSERVICES:When We run the API request for "Add_GenDeductions" for "Failure" it returns "400" status for input excel sheet = "Add_GenDeductions" for row "<row_index>" data
    Then WEBSERVICES:Verify Error Codes and Error Descriptions in JSON Response "Add_GenDeductions" JSON file with input excel sheet = "Add_GenDeductions" for row "<row_index>" data
     Examples:
      |row_index|
      |2|
    
    @Smoke
    Scenario: GET service for General Deductions API for Smoke Test
    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"

   

#  @Smoke
#  Scenario: Sample for executing GET service and do a compare with baseline -- GET Service
#    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"


  @getDedEx
  Scenario: Sample for verifying response JSON for Feild/Values -- for GET service
    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"
    Then WEBSERVICES:Verify Response Fields as Values for JSON "Get_GenDeductions"
      | Field | Value |
      | shortName | CKING |
      | shortName | MEDCTY |
      | shortName | JustForTesting |
      | itemID | MYbX6VNRPkYzjFLT9g6AAYMWLYYobTl3KRT23TJa4wk= |

   @CompareGet
  Scenario: Sample for executing GET service and do a compare with baseline -- GET Service
    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"
    Then WEBSERVICES:Compare "Get_GenDeductions" JSON file with "ListService_baseline" JSON file
    
#  Scenario Outline: Sample for verifying response JSON for Feild/Values -- For ADD/UPDATE/DELETE
#    When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#    Then WEBSERVICES:Verify Response Fields as Values for JSON "Add_GenDeductions"
#      | Field | Value |
#      | longName | maAutoLong |
#      | shortName | maAuto |
#      | codeValue | P18 |
#      | itemID | MYbX6VNRPkYzjFLT9g6AAc2S8+l0QN9mAADHVlSu8UQ= |
#    Examples:
#      |row_index|
#      |2|
#      |3|
#  Scenario: Sample for executing GET service and do a compare with baseline -- GET Service
#    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"
#    Then WEBSERVICES:Compare "Get_GenDeductions" JSON file with "ListService_baseline" JSON file

#  Scenario: Sample  for executing GET service and do a compare with baseline and ignore some tags -- GET Service
#    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"
#    Then WEBSERVICES:Compare "Get_GenDeductions" JSON file with "ListService_baseline" JSON file and ignore these tags
#      |generalDeductionConfigurations[itemID=MYbX6VNRPkYzjFLT9g6AAYzyxktGEzGNrWiQ8yvlVok=].deductionCode.codeValue|

# Scenario Outline: Sample  for compare response JSON with baseline -- ADD/UPDATE/DELETE services
#    When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#    Then WEBSERVICES:Compare "Add_GenDeductions" JSON file with "AddService_baseline" JSON file
#    Examples:
#      |row_index|
#      |2|

#  Scenario Outline: Sample  for compare response JSON with baseline and ignore some keys from comparison
#      When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#      Then WEBSERVICES:Compare "Add_GenDeductions" JSON file with "AddService_baseline" JSON file and ignore these tags
#       |data.output.generalDeductionConfiguration.itemID|
#       |data.output.generalDeductionConfiguration.deductionCode.codeValue|
#       |data.eventContext.generalDeductionConfiguration.itemID|
#      Examples:
#        |row_index|
#        |2|
#
#  Scenario Outline: Sample for Passing Parameters from Excel sheet to ADD/UPDATE/DELETE Services
#    When WEBSERVICES:When we run API command for "Remove_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Remove_GenDeductions" for row "<row_index>" data
#    Examples:
#      |row_index|
#      |2|


#  Scenario Outline: Sample  for compare response JSON with baseline -- ADD/UPDATE/DELETE services
#    When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#    Then WEBSERVICES:Compare "Add_GenDeductions" JSON file with "AddService_baseline" JSON file
#    Examples:
#      |row_index|
#      |2|
#
#  Scenario Outline: Sample  for compare response JSON with baseline and ignore some keys from comparison
#      When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#      Then WEBSERVICES:Compare "Add_GenDeductions" JSON file with "AddService_baseline" JSON file and ignore these tags
#       |data.output.generalDeductionConfiguration.itemID|
#       |data.output.generalDeductionConfiguration.deductionCode.codeValue|
#       |data.eventContext.generalDeductionConfiguration.itemID|
#      Examples:
#        |row_index|
#        |3|

#  Scenario Outline: Sample for Passing Parameters from Excel sheet to ADD/UPDATE/DELETE Services
#    When WEBSERVICES:When we run API command for "Remove_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Remove_GenDeductions" for row "<row_index>" data
#    Examples:
#      |row_index|
#      |ALL|



#  Scenario: Sample for executing GET service and do a compare with baseline -- GET Service
#    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"
#    Then WEBSERVICES:Compare "Get_GenDeductions" JSON file with "ListService_baseline" JSON file
#
#  Scenario: Sample  for executing GET service and do a compare with baseline and ignore some tags -- GET Service
#    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"
#    Then WEBSERVICES:Compare "Get_GenDeductions" JSON file with "ListService_baseline" JSON file and ignore these tags
#      |generalDeductionConfigurations[itemID=MYbX6VNRPkYzjFLT9g6AAYzyxktGEzGNrWiQ8yvlVok=].deductionCode.codeValue|



#  Scenario Outline: Sample for Passing Parameters from Excel sheet to ADD/UPDATE/DELETE Services
#    When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#    Examples:
#      |row_index|
#      |ALL|

#  Scenario Outline: Sample for Passing Parameters from Excel sheet to ADD/UPDATE/DELETE Services
#    When WEBSERVICES:When we run API command for "Remove_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Remove_GenDeductions" for row "<row_index>" data
#    Examples:
#      |row_index|
#      |ALL|


#  Scenario: Sample for executing GET service and do a compare with baseline -- GET Service
#    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"
#    Then WEBSERVICES:Compare "Get_GenDeductions" JSON file with "ListService_baseline" JSON file

#  Scenario: Sample  for executing GET service and do a compare with baseline and ignore some tags -- GET Service
#    When WEBSERVICES:When we run API command for "Get_GenDeductions" for "Success" it returns "200 OK" status, update payload = "NO"
#    Then WEBSERVICES:Compare "Get_GenDeductions" JSON file with "ListService_baseline" JSON file and ignore these tags
#      |data.output.generalDeductionConfiguration.itemID|
#      |data.output.generalDeductionConfiguration.deductionCode.codeValue|
#      |data.eventContext.generalDeductionConfiguration.itemID|

#  Scenario Outline: Sample  for compare response JSON with baseline -- ADD/UPDATE/DELETE services
#    When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#    Then WEBSERVICES:Compare "Add_GenDeductions" JSON file with "AddService_baseline" JSON file
#    Examples:
#      |row_index|
#      |2|
#
#  Scenario Outline: Sample  for compare response JSON with baseline also ignore some keys
#      When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#      Then WEBSERVICES:Compare "Add_GenDeductions" JSON file with "AddService_baseline" JSON file and ignore these tags
#       |data.output.generalDeductionConfiguration.itemID|
#       |data.output.generalDeductionConfiguration.deductionCode.codeValue|
#       |data.eventContext.generalDeductionConfiguration.itemID|
#      Examples:
#        |row_index|
#        |2|
#
#
#  Scenario: Sample  for verifying response JSON for Feild/Values -- for GET service
#    When WEBSERVICES:When we run API command for "Get_GenDeductions_PA05" for "Success" it returns "200 OK" status, update payload = "NO"
#    Then WEBSERVICES:Verify Response Fields as Values for JSON "Get_GenDeductions_PA05"
#      | Field | Value |
#      | shortName | CKING |
#      | shortName | MEDCTY |
#      | shortName | JustForTesting |
#      | itemID | MYbX6VNRPkYzjFLT9g6AAYMWLYYobTl3KRT23TJa4wk= |
#
#
#  Scenario Outline: Sample  for verifying response JSON for Feild/Values -- For ADD/UPDATE/DELETE
#    When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#    Then WEBSERVICES:Verify Response Fields as Values for JSON "Add_GenDeductions"
#      | Field | Value |
#      | longName | maAutoLong |
#      | shortName | maAuto |
#      | codeValue | P18 |
#      | itemID | MYbX6VNRPkYzjFLT9g6AAc2S8+l0QN9mAADHVlSu8UQ= |
#    Examples:
#      |row_index|
#      |2|
#      |3|
#
#
#  Scenario Outline: Sample  to run ADD , UPDATE & DELETE Services at same time for diff iterations
#    When WEBSERVICES:When we run API command for "Add_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Add_GenDeductions" for row "<row_index>" data
#    When WEBSERVICES:When we run API command for "Remove_GenDeductions" for "Success" it returns "200 OK" status, update payload = "YES" with input excel sheet = "US100698_Remove_GenDeductions" for row "<row_index>" data
#    Examples:
#      |row_index|
#      |2|
#      |3|

