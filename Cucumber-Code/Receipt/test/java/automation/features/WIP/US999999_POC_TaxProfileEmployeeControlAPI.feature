#######################################################################
#Webservices test story - US100699_POC_TaxProfileEmployeeControlAPI
#User Story : US100699
#User Sory Description : Cucumber Framework POC for Employee Control APIs
#Author: Mahesh Gadi   # Date : 3/24/2016
#Reviewed By : 
#######################################################################
@TaxProfile
Feature: US100699_POC_TaxProfileEmployeeControlAPI

@Execute
  Scenario: Demo for Employee Control APIs -- Tax Profile GET service
    When WEBSERVICES:When we run API command for "TaxProfile_GET1" for "Success" it returns "200 OK" status, update payload = "NO"
 
 @Execute
  Scenario: Demo for Employee Control APIs -- Tax Profile GET service
    When WEBSERVICES:When we run API command for "TaxProfile_GET2" for "Success" it returns "200 OK" status, update payload = "NO"  