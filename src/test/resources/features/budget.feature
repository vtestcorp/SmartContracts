@budget
Feature: Functional tests for budget in a deal

	@regression 
	Scenario: BUDGET_TC001_Verify_addition_of_consolidated_budget_to_new_deal
		Given I login to ODP as "admin" and create account
		And I login as "dealmaker" and add consolidated budget to new deal
		When I login as "dealchecker" and approve deal with budget
		Then Deal with budget should be live
		
	@regression
	Scenario: BUDGET_TC002_Verify_addition_of_purpose_budget_to_new_deal
		Given I login to ODP as "admin" and create account
		And I login as "dealmaker" and add purpose budget to new deal
		When I login as "dealchecker" and approve deal with budget
		Then Deal with budget should be live
		
	@regression
	Scenario: BUDGET_TC003_Verify_addition_of_destination_budget_to_new_deal
		Given I login to ODP as "admin" and create account
		And I login as "dealmaker" and add destination budget to new deal
		When I login as "dealchecker" and approve deal with budget
		Then Deal with budget should be live
		
	