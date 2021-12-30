@party
Feature: Functional tests for party master and also party in a deal

	@regression @smoke
	Scenario: PARTY_TC001_Verify_creation_of_external_party
		Given I login as "partymaker" and create external party
		When I login as "partychecker" and approve external party
		Then External party should be displayed at party summary

	@regression  
	Scenario: PARTY_TC002_Verify_creation_of_neutral_party
		Given I login as "partymaker" and create neutral party
		When I login as "partychecker" and approve neutral party
		Then Neutral party should be displayed at party summary

	@regression 
	Scenario: PARTY_TC003_Verify_creation_of_internal_party
		Given I login as "partymaker" and create internal party
		When I login as "partychecker" and approve internal party
		Then Internal party should be displayed at party summary

	@regression @smoke
	Scenario: PARTY_TC004_Verify_addition_of_contact_to_external_party
		Given I login as "partymaker" and draft external party
		When Contact is added to external party
		Then Contact should be displayed at party maker summary

	@regression @smoke 
	Scenario: PARTY_TC005_Verify_addition_of_account_to_external_party
		Given I login as "partymaker" and draft external party
		When Account is added to external party
		Then Account should be displayed at party maker summary

	@regression @smoke
	Scenario: PARTY_TC006_Verify_addition_of_document_to_external_party
		Given I login as "partymaker" and draft external party
		When Document is added to external party
		Then Document should be displayed at party maker summary

	@regression @smoke
	Scenario: PARTY_TC007_Verify_edit_of_external_party_basic_details
		Given An external party is live
		When I login as "partymaker" and edit basic details of external party
		Then Updated basic details should be displayed at party maker summary

	@regression @smoke 
	Scenario: PARTY_TC008_Verify_edit_of_external_party_contact
		Given An external party is live
		When I login as "partymaker" and edit contact of external party
		Then Updated contact should be displayed at party maker summary

	@regression @smoke 
	Scenario: PARTY_TC009_Verify_edit_of_external_party_account
		Given An external party is live
		When I login as "partymaker" and edit account of external party
		Then Updated account should be displayed at party maker summary

	@regression @smoke  
	Scenario: PARTY_TC010_Verify_edit_of_external_party_document
		Given An external party is live
		When I login as "partymaker" and edit document of external party
		Then Updated document should be displayed at party maker summary
		
	@regression @smoke
	Scenario: PARTY_TC011_Verify_delete_of_external_party_contact
		Given An external party is live
		When I login as "partymaker" and delete contact of external party
		Then Deleted contact should not be displayed at party maker summary

	@regression @smoke
	Scenario: PARTY_TC012_Verify_delete_of_external_party_account
		Given An external party is live
		When I login as "partymaker" and delete account of external party
		Then Deleted account should not be displayed at party maker summary

	@regression @smoke 
	Scenario: PARTY_TC013_Verify_delete_of_external_party_document
		Given An external party is live
		When I login as "partymaker" and delete document of external party
		Then Deleted document should not be displayed at party maker summary
		
	@regression @smoke
	Scenario: PARTY_TC014_Verify_edit_of_external_party_draft_basic_details
		Given I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "partymaker" and edit basic details of external party draft
		Then Updated basic details should be displayed at party maker summary	

	@regression @smoke 
	Scenario: PARTY_TC015_Verify_edit_of_external_party_draft_contact
		Given I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "partymaker" and edit contact of external party draft
		Then Updated contact should be displayed at party maker summary

	@regression @smoke 
	Scenario: PARTY_TC016_Verify_edit_of_external_party_draft_account
		Given I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "partymaker" and edit account of external party draft
		Then Updated account should be displayed at party maker summary
	
	@regression @smoke 
	Scenario: PARTY_TC017_Verify_edit_of_external_party_draft_document
		Given I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "partymaker" and edit document of external party draft
		Then Updated document should be displayed at party maker summary
		
	@regression @smoke 
	Scenario: PARTY_TC018_Verify_delete_of_external_party_draft_contact
		Given I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "partymaker" and delete contact of external party draft
		Then Deleted contact should not be displayed at party maker summary

	@regression @smoke 
	Scenario: PARTY_TC019_Verify_delete_of_external_party_draft_account
		Given I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "partymaker" and delete account of external party draft
		Then Deleted account should not be displayed at party maker summary
	
	@regression @smoke 
	Scenario: PARTY_TC020_Verify_delete_of_external_party_draft_document
		Given I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "partymaker" and delete document of external party draft
		Then Deleted document should not be displayed at party maker summary
	
	@regression @smoke 
	Scenario: PARTY_TC021_Verify_delete_of_external_party_draft_at_party_maker_queue
		Given I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "partymaker" and delete external party draft
		Then Deleted party should not be displayed at party maker queue
	
	@regression @smoke 
	Scenario: PARTY_TC022_Verify_delete_of_external_party_draft_at_party_checker_queue
		Given I login as "partymaker" and create external party
		When I login as "partychecker" and delete submitted external party
		Then Deleted party should not be displayed at party checker queue
	
	@regression @smoke 
	Scenario: PARTY_TC023_Verify_required_fields_of_external_party_draft_basic_details
		Given I login as "partymaker" and draft external party
		When Required external party basic details fields are not provided
		Then Required messages should be displayed at party maker
	
	@regression @smoke 
	Scenario: PARTY_TC024_Verify_required_fields_of_internal_party_draft_basic_details
		Given I login as "partymaker" and draft internal party
		When Required internal party basic details fields are not provided
		Then Required messages should be displayed at party maker
	
	@regression @smoke 
	Scenario: PARTY_TC025_Verify_required_fields_of_external_party_draft_contact
		Given I login as "partymaker" and draft external party
		When Required external party contact fields are not provided
		Then Required messages should be displayed at party maker

	@regression @smoke 
	Scenario: PARTY_TC026_Verify_required_fields_of_external_party_draft_account
		Given I login as "partymaker" and draft external party
		When Required external party account fields are not provided
		Then Required messages should be displayed at party maker

	@regression @smoke 
	Scenario: PARTY_TC027_Verify_required_fields_of_external_party_draft_document
		Given I login as "partymaker" and draft external party
		When Required external party document fields are not provided
		Then Required messages should be displayed at party maker
	
	@regression @smoke
	Scenario: PARTY_TC028_Verify_response_to_party_approval_without_comment
		Given I login as "partymaker" and create external party
		When I login as "partychecker" and approve external party without comment
		Then Response message should be displayed at party checker
	
	@regression @smoke
	Scenario: PARTY_TC029_Verify_response_to_party_review_without_comment
		Given I login as "partymaker" and create external party
		When I login as "partychecker" and send external party for review without comment
		Then Response message should be displayed at party checker
		
	@regression @smoke 
	Scenario: PARTY_TC030_Verify_addition_of_external_party_draft_not_submitted_to_new_deal
		Given I login as "partymaker" and create external party
		When I login as "dealmaker" and add external party draft to new deal
		Then External party draft should not be displayed as suggestion
		
	@regression @smoke 
	Scenario: PARTY_TC031_Verify_addition_of_external_party_draft_not_submitted_to_deal_draft
		Given I login as "dealmaker" and draft deal
		And I login as "partymaker" and create external party
		When I login as "dealmaker" and add external party draft to deal draft
		Then External party draft should not be displayed as suggestion
		
	@regression @smoke 
	Scenario: PARTY_TC032_Verify_addition_of_external_party_draft_rejected_to_new_deal
		Given I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "dealmaker" and add external party draft to new deal
		Then External party draft should not be displayed as suggestion  
		
	@regression @smoke 
	Scenario: PARTY_TC033_Verify_addition_of_external_party_draft_rejected_to_deal_draft
		Given I login as "dealmaker" and draft deal		
		And I login as "partymaker" and create external party
		And I login as "partychecker" and send external party for review
		When I login as "dealmaker" and add external party draft to deal draft
		Then External party draft should not be displayed as suggestion
		
	@regression @smoke  
	Scenario: PARTY_TC034_Verify_addition_of_external_party_to_new_deal
		Given An external party is live
		When I login as "dealmaker" and add external party to new deal
		Then External party draft should be displayed as suggestion
		
	@regression @smoke 
	Scenario: PARTY_TC035_Verify_addition_of_external_party_to_deal_draft
		Given I login as "dealmaker" and draft deal
		And An external party is live
		When I login as "dealmaker" and add external party to deal draft
		Then External party draft should be displayed as suggestion
	
	@regression @smoke
	Scenario: PARTY_TC036_Verify_edit_of_external_party_basic_details_linked_to_live_deal
		Given An external party is live
		And A deal linked with external party is live
		When I login as "partymaker" and edit basic details of external party
		Then Updated basic details should be displayed at party maker summary
		
	@regression @smoke
	Scenario: PARTY_TC037_Verify_edit_of_external_party_basic_details_linked_to_new_deal
		Given An external party is live
		When I login as "dealmaker" and add external party to new deal
		And I login as "partymaker" and initiate edit of external party
		Then Response message should be displayed at party summary
		
	@regression @smoke 
	Scenario: PARTY_TC038_Verify_addition_of_ecommerce_party_to_deal_draft
		Given I login to ODP as "admin" and create account
		And An external party is live
		When I login as "dealmaker" and add ecommerce party to new deal
		Then Ecommerce party details should be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC039_Verify_duplication_of_external_party_draft
		Given I login as "partymaker" and create external party
		When A duplicate external party is added
		Then Response message should be displayed at party maker
		
	@regression @smoke 
	Scenario: PARTY_TC040_Verify_duplication_of_live_external_party
		Given An external party is live
		When A duplicate external party is added
		Then Response message should be displayed at party maker
	
	@regression @smoke 
	Scenario: PARTY_TC041_Verify_duplication_of_ecommerce_party
		Given An external party is live
		When I login as "dealmaker" and add ecommerce party to new deal
		And A duplicate ecommerce party is added
		Then Required message should be displayed at new deal
		
	@regression @smoke
	Scenario: PARTY_TC042_Verify_creation_of_external_party_in_new_deal
		Given I login as "dealmaker" and create new deal
		When An external party is added to new deal
		Then External party should be displayed at deal summary
		
	@regression
	Scenario: PARTY_TC043_Verify_creation_of_internal_party_in_new_deal
		Given I login as "dealmaker" and create new deal
		When An internal party is added to new deal
		Then Internal party should be displayed at deal summary
		
	@regression @smoke
	Scenario: PARTY_TC044_Verify_addition_of_contact_to_external_party_in_new_deal
		Given I login as "dealmaker" and create new deal
		When A contact is added to external party in new deal
		Then Contact added to external party should be displayed at deal summary
		
	@regression @smoke
	Scenario: PARTY_TC045_Verify_addition_of_account_to_external_party_in_new_deal
		Given I login as "dealmaker" and create new deal
		When An account is added to external party in new deal
		Then Account added to external party should be displayed at deal summary
		
	@regression @smoke
	Scenario: PARTY_TC046_Verify_addition_of_document_to_external_party_in_new_deal
		Given I login as "dealmaker" and create new deal
		When A document is added to external party in new deal
		Then Document added to external party should be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC047_Verify_edit_of_external_party_basic_details_in_deal_draft
		Given I login as "dealmaker" and draft deal
		When I login as "dealmaker" and edit basic details of external party in deal draft
		Then Updated basic details of external party should be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC048_Verify_edit_of_external_party_contact_in_deal_draft
		Given I login as "dealmaker" and draft deal
		When I login as "dealmaker" and edit contact of external party in deal draft
		Then Updated contact of external party should be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC049_Verify_edit_of_external_party_account_in_deal_draft
		Given I login as "dealmaker" and draft deal
		When I login as "dealmaker" and edit account of external party in deal draft
		Then Updated account of external party should be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC050_Verify_edit_of_external_party_document_in_deal_draft
		Given I login as "dealmaker" and draft deal
		When I login as "dealmaker" and edit document of external party in deal draft
		Then Updated document of external party should be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC051_Verify_delete_of_external_party_in_deal_draft
		Given I login as "dealmaker" and draft deal
		When I login as "dealmaker" and delete external party in deal draft
		Then Deleted external party should not be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC052_Verify_delete_of_external_party_contact_in_deal_draft
		Given I login as "dealmaker" and draft deal
		When I login as "dealmaker" and delete contact of external party in deal draft
		Then Deleted contact of external party should not be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC053_Verify_delete_of_external_party_account_in_deal_draft
	Given I login as "dealmaker" and draft deal
	When I login as "dealmaker" and delete account of external party in deal draft
	Then Deleted account of external party should not be displayed at deal summary

	@regression @smoke
	Scenario: PARTY_TC054_Verify_delete_of_external_party_document_in_deal_draft
		Given I login as "dealmaker" and draft deal
		When I login as "dealmaker" and delete document of external party in deal draft
		Then Deleted document of external party should not be displayed at deal summary
		
	@regression @smoke
	Scenario: PARTY_TC055_Verify_link_of_existing_contact_to_external_party_in_new_deal
		Given An external party is live
		And I login as "dealmaker" and create new deal
		When An existing contact is linked to external party in new deal
		Then Contact linked to external party should be displayed at deal summary
		
	@regression @smoke
	Scenario: PARTY_TC056_Verify_link_of_existing_account_to_external_party_in_new_deal
		Given An external party is live
		And I login as "dealmaker" and create new deal
		When An existing account is linked to external party in new deal
		Then Account linked to external party should be displayed at deal summary
		
	@regression @smoke
	Scenario: PARTY_TC057_Verify_link_of_exising_document_to_external_party_in_new_deal
		Given An external party is live
		And I login as "dealmaker" and create new deal
		When An existing document is linked to external party in new deal
		Then Document linked to external party should be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC058_Verify_addition_of_external_party_already_rejected_by_checker
		Given An external party is already rejected by party checker
		When I login as "partymaker" and create same external party
		Then Response message should be displayed at party maker
		
	@regression @smoke 
	Scenario: PARTY_TC059_Verify_addition_of_external_party_of_live_deal_in_another_deal_draft
		Given An external party is added to live deal
		When I login as "dealmaker" and add same external party to deal draft
		Then External party should be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC060_Verify_addition_of_external_party_of_rejected_deal_to_new_deal
		Given A deal with external party onboarded is already rejected by deal checker
		When I login as "dealmaker" and onboard same external party to new deal
		Then Response message should be displayed at parties tab
		
	@regression @smoke 
	Scenario: PARTY_TC061_Verify_link_of_existing_contact_to_external_party_in_multiple_live_deals
		Given An external party is live
		When An existing contact is linked to external party in multiple live deals
		Then Contact linked to external party should be displayed at deal summary
		
	@regression @smoke 
	Scenario: PARTY_TC062_Verify_external_party_added_via_deal_displayed_at_party_summary
		Given I login as "dealmaker" and add external party to draft deal
		And External party is not displayed at party summary
		When I login as "dealchecker" and approve the deal
		Then External party added via deal should be displayed at party summary
		
	@regression @smoke 
	Scenario: PARTY_TC063_Verify_external_party_contact_mapped_to_notification_cannot_be_deleted
		Given An external party is added to live deal
		And External party contact is mapped to deal notification
		When I login as "partymaker" and delete external party contact added via deal
		Then Response message should be displayed at party maker
		
	@regression @smoke 
	Scenario: PARTY_TC064_Verify_external_party_contact_mapped_to_entitlement_cannot_be_deleted
		Given An external party is added to live deal
		And External party contact is mapped to deal entitlement
		When I login as "partymaker" and delete external party contact added via deal
		Then Response message should be displayed at party maker