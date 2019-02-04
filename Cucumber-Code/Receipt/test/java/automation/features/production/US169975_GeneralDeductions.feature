#######################################################################
#US169975
#Webservices test story - US169975_GeneralDeductions
#Created By : Mahesh Gadi
#Created Date : 1/19/2016
#Modified Date : 1/22/2016
#Updates/Modifications :
#######################################################################
Feature: US169975_GeneralDeductions

Scenario: Check webservice : General Deductions API with GB and GBMod for Add , List , Update and Remove Service
   When WEBSERVICES:When we run API command for "ViewGeneralDeductionwithGB" for "Success" it returns "200 OK" status, update payload = "NO"
   When WEBSERVICES:When we run API command for "AddGeneralDeductionsWithGBMod" for "Success" it returns "200 OK" status, update payload = "NO"
   When WEBSERVICES:When we run API command for "UpdateGeneralDeductionsWithGBMod" for "Success" it returns "200 OK" status, update payload = "NO"
   When WEBSERVICES:When we run API command for "DeleteGeneralDeductionsWithGBMod" for "Success" it returns "200 OK" status, update payload = "NO"
   