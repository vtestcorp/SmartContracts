import json
import os

SCENARIO = "Perf3002"
DEALS = 1
OBLIGORS = 3
INVESTORS = 10

notification_contact_list = []
master = {"test_id" : "TC002_Verify_creation_of_deal"}

credentials_list = []
credentials_list.append({})
credentials_list[0]["superadmin"] = {
                "username" : "superadmin",
                "password" : "$$$xcro$$$"
            }
credentials_list[0]["adminmaker"] = {
                "username" : "adminmaker",
                "password" : "$#$#AS400*0101"
            }
master["credentials"] = credentials_list

deals_list = []
for deal_index in range(0,DEALS):
    deals_list.append({})

    deals_list[deal_index]["name"] = "AutomationDeal"
    deals_list[deal_index]["product"] = "Test"
    deals_list[deal_index]["starts_on"] = "01-Jan-2022"
    deals_list[deal_index]["ends_on"] = "31-Dec-2022"
    deals_list[deal_index]["business_segment"] = "Commercial development"
    deals_list[deal_index]["processing_units"] = ["Select All", "Bangalore CPU", "Mumbai HO"]
    deals_list[deal_index]["country"] = "India"
    deals_list[deal_index]["timezone"] = "Asia/Kolkata (+05:30)"

    accounts_list = []    
    accounts_list.append({})
    accounts_list[account_index]["account_country"] = "India"
    accounts_list[account_index]["account_currency"] = "INR"
    accounts_list[account_index]["account_search_parameter"] = "Account No."
    accounts_list[account_index]["account_search_input"] = SCENARIO+"Obligor"+str(account_index+1)+"Account"+str(account_index+1)
    accounts_list[account_index]["account_balance_check"] = True
    deals_list[deal_index]["accounts"] = accounts_list

    party_obligor_list = []
    for party_obligor_index in range(0,OBLIGORS):
        party_obligor_list.append({})
        party_obligor_list[party_obligor_index]["party_type"] = "External"
        party_obligor_list[party_obligor_index]["party_customer_id"] = "DummyCustomerID"
        if DEALS == 1:
            party_obligor_list[party_obligor_index]["party_name"] = SCENARIO+"Obligor"+str(party_obligor_index+1)
        else:
            party_obligor_list[party_obligor_index]["party_name"] = SCENARIO+"Deal"+str(deal_index+1)+"Obligor"+str(party_obligor_index+1)
        party_obligor_list[party_obligor_index]["party_neutral"] = False
        party_obligor_list[party_obligor_index]["party_responsibility"] ="Obligor"
        if DEALS == 1:
            party_obligor_list[party_obligor_index]["party_attribute_name"] = SCENARIO+"Obligor"+str(party_obligor_index+1)
        else:
            party_obligor_list[party_obligor_index]["party_attribute_name"] = SCENARIO+"Deal"+str(deal_index+1)+"Obligor"+str(party_obligor_index+1)
        party_obligor_list[party_obligor_index]["party_attribute_country"] = "IND"
        party_obligor_list[party_obligor_index]["party_ecommerce"] = True
        party_obligor_list[party_obligor_index]["party_ecommerce_participant_id"] = "DummyParticipantID"
        if DEALS == 1:
            party_obligor_list[party_obligor_index]["party_ecommerce_debit_account"] = SCENARIO+"Obligor"+str(party_obligor_index+1)+"Account"+str(party_obligor_index+1)
        else:
            party_obligor_list[party_obligor_index]["party_ecommerce_debit_account"] = SCENARIO+"Deal"+str(deal_index+1)+"Obligor"+str(party_obligor_index+1)+"Account"+str(party_obligor_index+1)
        party_obligor_contact_list = []
        party_obligor_contact_list.append({})
        if DEALS == 1:
            party_obligor_contact_list[0]["party_contact_name"] = SCENARIO+"Obligor"+str(party_obligor_index+1)+"Contact"+str(party_obligor_index+1)
            notification_contact_list.append(SCENARIO+"Obligor"+str(party_obligor_index+1)+"Contact"+str(party_obligor_index+1))
        else:
            party_obligor_contact_list[0]["party_contact_name"] = SCENARIO+"Deal"+str(deal_index+1)+"Obligor"+str(party_obligor_index+1)+"Contact"+str(party_obligor_index+1)
            notification_contact_list.append(SCENARIO+"Deal"+str(deal_index+1)+"Obligor"+str(party_obligor_index+1)+"Contact"+str(party_obligor_index+1))
        party_obligor_contact_list[0]["party_contact_authorised_signatory"] = False
        party_obligor_contact_list[0]["party_contact_subinstruction_notification"] = True
        party_obligor_contact_list[0]["party_contact_email"] = "contact1@gmail.com"
        party_obligor_list[party_obligor_index]["party_contacts"] = party_obligor_contact_list
    
    party_investor_list = []
    for party_investor_index in range(0,INVESTORS):
        party_investor_list.append({})
        party_investor_list[party_investor_index]["party_type"] = "External"
        party_investor_list[party_investor_index]["party_customer_id"] = "DummyCustomerID"
        if DEALS == 1:
            party_investor_list[party_investor_index]["party_name"] = SCENARIO+"Investor"+str(party_investor_index+1)
        else:
            party_investor_list[party_investor_index]["party_name"] = SCENARIO+"Deal"+str(deal_index+1)+"Investor"+str(party_investor_index+1)
        party_investor_list[party_investor_index]["party_neutral"] = False
        party_investor_list[party_investor_index]["party_responsibility"] = "Investor"
        if DEALS == 1:
            party_investor_list[party_investor_index]["party_attribute_name"] = SCENARIO+"Investor"+str(party_investor_index+1)
        else:
            party_investor_list[party_investor_index]["party_attribute_name"] = SCENARIO+"Deal"+str(deal_index+1)+"Investor"+str(party_investor_index+1) 
        party_investor_list[party_investor_index]["party_attribute_country"] = "IND"
        party_investor_list[party_investor_index]["party_ecommerce"] = True
        party_investor_list[party_investor_index]["party_ecommerce_participant_id"] = "DummyParticipantID"
        party_investor_contact_list = []
        party_investor_contact_list.append({})
        if DEALS == 1:
            party_investor_contact_list[0]["party_contact_name"] = SCENARIO+"Investor"+str(party_investor_index+1)+"Contact"+str(party_investor_index+1)
            notification_contact_list.append(SCENARIO+"Investor"+str(party_investor_index+1)+"Contact"+str(party_investor_index+1))
        else:
            party_investor_contact_list[0]["party_contact_name"] = SCENARIO+"Deal"+str(deal_index+1)+"Investor"+str(party_investor_index+1)+"Contact"+str(party_investor_index+1)
            notification_contact_list.append(SCENARIO+"Deal"+str(deal_index+1)+"Investor"+str(party_investor_index+1)+"Contact"+str(party_investor_index+1))
        party_investor_contact_list[0]["party_contact_authorised_signatory"] = False
        party_investor_contact_list[0]["party_contact_subinstruction_notification"] = True
        party_investor_contact_list[0]["party_contact_email"] = "contact1@gmail.com"
        party_investor_list[party_investor_index]["party_contacts"] = party_investor_contact_list
        party_investor_account_list = []
        party_investor_account_list.append({})
        party_investor_account_list[0]["party_account_payment_system"] = "BT_MY"
        party_investor_account_list[0]["party_account_beneficiary_BIC"] = "ICICINBBNRI"
        party_investor_account_list[0]["party_account_IBAN"] = "ACCOUNTNUMBER"
        if DEALS == 1:
            party_investor_account_list[0]["party_account_beneficiary_name"] = SCENARIO+"Investor"+str(party_investor_index+1)
        else:
            party_investor_account_list[0]["party_account_beneficiary_name"] = SCENARIO+"Deal"+str(deal_index+1)+"Investor"+str(party_investor_index+1)
        party_investor_account_list[0]["party_account_beneficiary_country"] = "IN"
        party_investor_account_list[0]["party_account_beneficiary_address"] = "InvestorAddress"
        if DEALS == 1:
            party_investor_account_list[0]["party_account_beneficiary_account"] = SCENARIO+"Investor"+str(party_investor_index+1)+"Account"+str(party_investor_index+1)
        else:
            party_investor_account_list[0]["party_account_beneficiary_account"] = SCENARIO+"Deal"+str(deal_index+1)+"Investor"+str(party_investor_index+1)+"Account"+str(party_investor_index+1)
        party_investor_list[party_investor_index]["party_accounts"] = party_investor_account_list
    party_platform_list = []
    party_platform_list.append({})
    party_platform_list[0]["party_type"] = "External"
    party_platform_list[0]["party_customer_id"] = "DummyCustomerID"
    if DEALS == 1:
        party_platform_list[0]["party_name"] = SCENARIO+"Platform1"
    else:
        party_platform_list[0]["party_name"] = SCENARIO+"Deal"+str(deal_index+1)+"Platform1"
    party_platform_list[0]["party_neutral"] = False
    party_platform_list[0]["party_responsibility"] = "Platform"
    if DEALS == 1:
        party_platform_list[0]["party_attribute_name"] = SCENARIO+"Platform1"
    else:
        party_platform_list[0]["party_attribute_name"] = SCENARIO+"Deal"+str(deal_index+1)+"Platform1"
    party_platform_list[0]["party_attribute_country"] = "IND"
    party_platform_list[0]["party_ecommerce"] = True
    party_platform_list[0]["party_ecommerce_participant_id"] = "DummyParticipantID"
    parties_list = party_obligor_list+party_investor_list+party_platform_list
    deals_list[deal_index]["parties"] = parties_list

    notification_list = []    
    notifications = ["Successful adhoc Transactions", "Failed adhoc Transactions"]
    for notification_index in range(0, len(notifications)):
        notification_list.append({})
        notification_list[notification_index]["notification_type"] = notifications[notification_index]
        notification_list[notification_index]["notification_mode"] = "E-Mail"
        notification_list[notification_index]["notification_contacts"] = notification_contact_list

    deals_list[deal_index]["notifications"] = notification_list

    

master["deals"] = deals_list

final_json = json.loads(json.dumps(master))

if not os.path.exists('testdata'):
    os.makedirs('testdata')
with open('./testdata/TC002_Verify_creation_of_deal.json', 'w+') as file:
    json.dump(final_json, file, indent=4)

if not os.path.exists('PTDealData'):
    os.makedirs('PTDealData')
with open('./PTDealData/'+SCENARIO+'.json', 'w+') as file:
    json.dump(final_json, file, indent=4)
