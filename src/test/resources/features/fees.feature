@fees
Feature: Functional tests for both scheduled and adhoc fees

	@regression @smoke 
	Scenario: FEES_TC001_Verify_addition_of_scheduled_fees_to_new_deal_with_upfront
		Given I login as "superadmin" and add recurring scheduled fee to new deal with upfront fee
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke 
	Scenario: FEES_TC002_Verify_addition_of_recurring_scheduled_fees_to_new_deal_without_upfront
		Given I login as "superadmin" and add recurring scheduled fee to new deal without upfront fee
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke
	Scenario: FEES_TC003_Verify_addition_of_eod_balance_scheduled_fees_to_new_deal_with_upfront 
		Given I login to ODP as "admin" and create account
		And I login as "superadmin" and add eod balance scheduled fee to new deal with upfront fee
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke 
	Scenario: FEES_TC004_Verify_addition_of_eod_balance_scheduled_fees_to_new_deal_without_upfront
		Given I login to ODP as "admin" and create account
		And I login as "superadmin" and add eod balance scheduled fee to new deal without upfront fee
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke
	Scenario: FEES_TC005_Verify_addition_of_transaction_count_scheduled_fees_to_new_deal_with_upfront
		Given I login to ODP as "admin" and create account
		And I login as "superadmin" and add transaction count scheduled fee to new deal with upfront fee
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke
	Scenario: FEES_TC006_Verify_addition_of_transaction_value_scheduled_fees_to_new_deal_with_upfront
		Given I login to ODP as "admin" and create account
		And I login as "superadmin" and add transaction value scheduled fee to new deal with upfront fee
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke
	Scenario: FEES_TC007_Verify_addition_of_transaction_based_scheduled_fees_to_new_deal_without_upfront
		Given I login to ODP as "admin" and create account
		And I login as "superadmin" and add transaction based scheduled fee to new deal without upfront fee
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke
		Scenario: FEES_TC008_Verify_addition_of_scheduled_fees_with_party_split_to_new_deal
		Given I login as "superadmin" and add scheduled fee with party split to new deal
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke
	Scenario: FEES_TC009_Verify_addition_of_scheduled_fees_without_fee_credit_account_to_new_deal
			Given I login as "superadmin" and add scheduled fee without fee credit account to new deal
			When I login as "superadmin" and approve deal with scheduled fee
			Then Deal with scheduled fee should be live
			
	@regression @smoke
	Scenario: FEES_TC010_Verify_addition_of_scheduled_fees_without_tax_credit_account_to_new_deal
		Given I login as "superadmin" and add scheduled fee without tax credit account to new deal
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke
	Scenario: FEES_TC011_Verify_addition_of_scheduled_fees_without_fee_due_reminder_to_new_deal
		Given I login as "superadmin" and add scheduled fee without fee due reminder to new deal
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke 
	Scenario: FEES_TC012_Verify_addition_of_scheduled_fees_without_fee_overdue_reminder_to_new_deal
		Given I login as "superadmin" and add scheduled fee without fee overdue reminder to new deal
		When I login as "superadmin" and approve deal with scheduled fee
		Then Deal with scheduled fee should be live
		
	@regression @smoke 
	Scenario: FEES_TC013_Verify_addition_of_scheduled_fees_without_invoice_contacts_to_new_deal
		Given I login as "superadmin" and add scheduled fee to new deal
		When Invoice contacts are not added to scheduled fee
		Then Response message should be displayed at scheduled fee tab
		
	@regression @smoke 
	Scenario: FEES_TC014_Verify_addition_of_scheduled_fees_without_fee_reminder_contacts_to_new_deal
		Given I login as "superadmin" and add scheduled fee to new deal
		When Fee reminder contacts are not added to scheduled fee
		#Then Response message should be displayed at scheduled fee tab