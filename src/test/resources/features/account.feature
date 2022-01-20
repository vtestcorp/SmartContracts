@account
Feature: Functional tests for accounts in a deal

	@regression @smoke 

	Scenario: ACCOUNT_TC001_Verify_addition_of_new_account_to_new_deal
	  Given I login to ODP as "admin" and create account
		When I login as "superadmin" and onboard account to new deal
		And New deal is submitted to deal checker
		Then Deal should be available at deal checker queue
		
	@regression @smoke 
	Scenario: ACCOUNT_TC002_Verify_addition_of_new_account_to_existing_deal
		Given I login to ODP as "admin" and create account
		And A deal is live
		When I login as "superadmin" and onboard account to live deal
		And Deal draft is submitted to deal checker
		Then Deal should be available at deal checker queue
		
	@regression @smoke 
	Scenario: ACCOUNT_TC003_Verify_addition_of_existing_account_to_existing_deal
		Given I login to ODP as "admin" and create account
		And A deal is live with an account onboarded
		When I login as "superadmin" and onboard same account to another live deal
		Then Response message should be displayed at accounts tab
		
	@regression @smoke
	Scenario: ACCOUNT_TC004_Verify_addition_of_existing_account_from_closed_deal_to_new_deal
		Given I login to ODP as "admin" and create account
		And A live deal with an account onboarded is closed
		When I login as "superadmin" and onboard same account to new deal
		And New deal is submitted to deal checker
		Then Deal should be available at deal checker queue
		
	@regression @smoke
	Scenario: ACCOUNT_TC005_Verify_addition_of_existing_account_from_closed_deal_to_existing_deal
		Given I login to ODP as "admin" and create account
		And A live deal with an account onboarded is closed
		And A deal is live
		When I login as "superadmin" and onboard same account to live deal
		And Deal draft is submitted to deal checker
		Then Deal should be available at deal checker queue
		
	@regression @smoke 
	Scenario: ACCOUNT_TC006_Verify_addition_of_existing_account_from_paused_deal_to_new_deal
		Given I login to ODP as "admin" and create account
		And A live deal with an account onboarded is paused
		When I login as "superadmin" and onboard same account to new deal
		Then Response message should be displayed at accounts tab
		
	@regression @smoke  
	Scenario: ACCOUNT_TC007_Verify_addition_of_existing_account_from_closed_deal_to_new_deal_acc_bal_chk_disabled
		Given I login to ODP as "admin" and create account
		And A live deal with an account onboarded is closed
		When I login as "superadmin" and onboard same account with account balance check disabled to new deal
		And New deal is submitted to deal checker
		Then Deal should be available at deal checker queue
		
	@regression @smoke 
	Scenario: ACCOUNT_TC008_Verify_addition_of_existing_account_from_closed_deal_to_existing_deal_and_add_txn
		Given I login to ODP as "admin" and create account
		And A live deal with an account onboarded is closed
		And A deal is live
		When I login as "dealmaker" and onboard same account to live deal
		And An adhoc transaction is created for live deal
		Then Adhoc transaction should be available at execution report
		
	@regression @smoke
	Scenario: ACCOUNT_TC009_Verify_addition_of_same_account_twice_to_new_deal
		Given I login to ODP as "admin" and create account
		When I login as "dealmaker" and onboard account to new deal
		And Same account is again onboarded to new deal
		Then Response message should be displayed at accounts tab
		
	@regression @smoke
	Scenario: ACCOUNT_TC010_Verify_addition_of_same_account_twice_to_live_deal
		Given I login to ODP as "admin" and create account
		And A deal is live with an account onboarded
		When I login as "dealmaker" and onboard same account to same live deal
		Then Response message should be displayed at accounts tab