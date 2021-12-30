const excel = require('excel4node');
const uuid = require('uuid');
const fs = require('fs');
const input = require('./inputdata.json');
const excelFileName = "Transaction_Bulk_Upload_File_"+uuid.v4()+".xlsx";

var generateExcel = async function(){
    
    const workbook = new excel.Workbook();
    const worksheet = await workbook.addWorksheet('Sheet');
    const headers = ["transactionName", "sourceAccount", "sourceAccountCountry",
                    "sourceAccountCurrency", "transactionPurpose", "remarks",
                    "sweepinAccount", "sweepinLimit", "budget", "purpose",
                    "balanceConsideration",	"paymentType", "isPendingApproval",
                    "deferralClosureDate", "deferralApprovedBy", "deferralNotes",
                    "isHardCopyRequired", "hardCopyComment", "paymentInstrument",
                    "executeOn", "scheduleAt", "scheduleTime", "holidayAction",
                    "holidayId.0", "ACCOUNTIBAN", "Destination Account",
                    "Amount", "Debit Remittance Information", "Credit Remittance Information",
                    "Beneficiary Name", "Ranjith", "Aadhar Custom label", "Beneficiary Country",
                    "Beneficiary Currency", "Account Number", "Name", "senderPop", "receiver pop"]

    for(index = 0; index < headers.length; index++){
        await worksheet.cell(1, index+1).string(headers[index]);
    }

    for(data of input){
        for(index = 0; index < data.noOfTransactions; index++){
            let transactionName = "Transaction"+uuid.v4();
            await worksheet.cell(index+2, 1).string(transactionName);
            await worksheet.cell(index+2, 2).string(data.sourceAccount);
            await worksheet.cell(index+2, 3).string(data.sourceAccountCountry);
            await worksheet.cell(index+2, 4).string(data.sourceAccountCurrency);
            await worksheet.cell(index+2, 5).string(data.transactionPurpose);
            await worksheet.cell(index+2, 6).string(transactionName);
			await worksheet.cell(index+2, 11).string(data.purpose);
            await worksheet.cell(index+2, 11).string(data.balanceConsideration);
            await worksheet.cell(index+2, 19).string(data.paymentInstrument);
			await worksheet.cell(index+2, 25).string(data.accountIban);
            await worksheet.cell(index+2, 26).string(data.destinationAccount);
            await worksheet.cell(index+2, 27).number(data.amount);
            await worksheet.cell(index+2, 33).string(data.beneficiaryCountry);
            await worksheet.cell(index+2, 34).string(data.beneficiaryCurrency);
			await worksheet.cell(index+2, 37).string(data.senderPop);
			await worksheet.cell(index+2, 38).string(data.receiverpop);
        }
    }

    const path = '../../testdata/'
    let regex = /[.]xlsx$/
    fs.readdirSync(path)
    .filter(f => regex.test(f))
    .map(f => fs.unlinkSync(path + f))
    
    await workbook.write('../../testdata/'+excelFileName);

}

var generateJSON = async function(){

    let input = {    
            "test_id" : "TRANSACTION_TCXXX_Verify_bulk_upload_of_transaction",    
            "bulk_transactions":[
                {
                    "bulk_upload_file_url" : "src/test/resources/testdata/"+excelFileName,
                    "bulk_upload_file_name" : excelFileName,
                    "bulk_upload_file_sheet" : "Sheet"			
                }		
            ]
        }

    let data = JSON.stringify(input, null, 4);
    fs.writeFileSync('../../testdata/TRANSACTION_TCXXX_Verify_bulk_upload_of_transaction.json', data);

}

generateExcel();
generateJSON();
