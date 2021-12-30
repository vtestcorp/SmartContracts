@entitlement
Feature: Functional tests for deal and account entitlements

	@regression @smoke
	Scenario: ENTITLEMENT_TC001_Verify_addition_of_deal_entitlement_to_new_deal
		Given I login as "dealmaker" and add deal entitlement to new deal
		When I login as "dealchecker" and approve deal with entitlement
		Then Deal with entitlement should be live
		
	@regression @smoke
	Scenario: ENTITLEMENT_TC002_Verify_addition_of_account_entitlement_to_new_deal
		Given I login to ODP as "admin" and create account
		And I login as "dealmaker" and add account entitlement to new deal
		When I login as "dealchecker" and approve deal with entitlement
		Then Deal with entitlement should be live