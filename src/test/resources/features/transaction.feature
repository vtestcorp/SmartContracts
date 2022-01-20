@transaction
Feature: Functional tests for transaction

  @regression 
  Scenario: TC003_Verify_creation_of_transaction
  	Given I login as "superadmin" and create transaction for live deal
  	And I login as "superadmin" and submit transactions
  	When I login as "superadmin" and approve transactions
  	Then Transactions should be available at execution report
  	
  @regression
  Scenario: TRANSACTION_TCXXX_Verify_bulk_upload_of_transaction
  	Given I login as "transactionmaker" and bulk upload transactions
  	When I login as "transactionchecker" and approve bulk of transactions
  	Then Transactions should be displayed in internal execution report
  	
  	@wip1
  	Scenario: Verify_ecommerce_transaction_creation
  	Given I login as "superadmin" and create transaction for ecommerce
  	When I login as "superadmin" and approve transactions for ecommerce
  	Then I login as "superadmin" and verifiy transactions for ecommerce
  	
  	@wip1
  	Scenario: Verify_ecommerce_party_creation
  	Given I login as "superadmin" and create party for ecommerce
  	