import json
import os

SCENARIO = "Perf3000"
DEALS = 1
OBLIGORS = 1


master = {"test_id" : "TC000_Verify_creation_of_account"}

credentials_list = []
credentials_list.append({})
credentials_list[0]["admin"] = {
                "username" : "admin",
                "password" : "u?5k167v13w5"
            }
master["credentials"] = credentials_list

master_accounts_list = []

for deal_index in range(0,DEALS):
    accounts_list = []
    for account_index in range(0,OBLIGORS):        
        accounts_list.append({})
        accounts_list[account_index]["account_number"] = SCENARIO+"Deal"+str(deal_index+1)+"Obligor"+str(account_index+1)+"Account"+str(account_index+1)
        
        accounts_list[account_index]["available_balance_amount"] = "1000000"
        accounts_list[account_index]["available_balance_currency"] = "INR"
        accounts_list[account_index]["ledger_balance_amount"] = "10000000"
        accounts_list[account_index]["ledger_balance_currency"] = "INR"
        accounts_list[account_index]["currency"] = "INR"
        accounts_list[account_index]["country"] = "IN"
        accounts_list[account_index]["status"] = "A"
        accounts_list[account_index]["short_name"] = SCENARIO
        accounts_list[account_index]["obo_flag"] = "Y"
    master_accounts_list += accounts_list

master["accounts"] = master_accounts_list

final_json = json.loads(json.dumps(master))

if not os.path.exists('testdata'):
    os.makedirs('testdata')
with open('./testdata/TC000_Verify_creation_of_account.json', 'w+') as file:
    json.dump(final_json, file, indent=4)

if not os.path.exists('PTAccountData'):
    os.makedirs('PTAccountData')
with open('./PTAccountData/'+SCENARIO+'.json', 'w+') as file:
    json.dump(final_json, file, indent=4)
