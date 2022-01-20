@deal
Feature: Functional tests for deals 

  @regression
  Scenario: TC002_Verify_creation_of_deal
  	Given I login to ODP as "admin" and create account
  	Given I login as "dealmaker" and create deal
  	When I login as "dealchecker" and approve deal
  	Then Deal should be live
  	
  @regression
  Scenario: TC005_Verify_live_deal_edit
  	Given I login as "superadmin" and edit live deal 
  	When I login as "superadmin" and approve updated deal
  	Then Deal should be live
  	
  	@wip1 @regression
  Scenario: Verify_pausing_a_deal_via_deal_workflow
  	Given I login as "superadmin" and pause live deal
  	Given I login as "dealmaker" and send pause deal
  	When I login as "dealchecker" and approve pause deal
  	Then Deal should be pause
  	
  	@wip1 @regression
  	Scenario: Verify_resuming _a_deal_via_deal_workflow
  	Given I login as "superadmin" and resume live deal
  	Given I login as "dealmaker" and send resume deal
  	When I login as "dealchecker" and approve resume deal
  	Then Deal should be resume
  	
  	@wip1 @regression
  	Scenario: Verify_closing _a_deal_via_deal_workflow
  	Given I login as "superadmin" and close live deal
  	Given I login as "dealmaker" and send close deal
  	When I login as "dealchecker" and approve close deal
  	Then Deal should be close