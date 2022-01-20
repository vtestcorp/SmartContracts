package com.appveen.smartcontracts.pageobject;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class AdhocTransactionPage {
	
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject adhocTransactionLocator = null; 
	
	private void addBTSubInstruction(WebDriver driver, ExtentTest step, JSONObject subInstruction) throws Exception {		
		try {			
			driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionDebitAccountBICCode"))).sendKeys(subInstruction.getString("sub_instruction_debit_account_BIC_code"));
			
			Select subInstructionTransactionChargesBearedBy = new Select(driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionChargesBearedBy"))));
			subInstructionTransactionChargesBearedBy.selectByVisibleText(subInstruction.getString("sub_instruction_transaction_charges_beared_by"));
			
			driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionAmount"))).sendKeys(subInstruction.getString("sub_instruction_amount"));
			
			Select subInstructionPaymentCurrency = new Select(driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionCurrency"))));
			subInstructionPaymentCurrency.selectByVisibleText(subInstruction.getString("sub_instruction_currency"));
			
			driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionValueDate"))).sendKeys(subInstruction.getString("sub_instruction_value_date"));
			
			driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionBeneficiaryName"))).sendKeys(subInstruction.getString("sub_instruction_beneficiary_name"));
			
			driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionBeneficiaryAddress"))).sendKeys(subInstruction.getString("sub_instruction_beneficiary_address"));
			
			step.log(Status.INFO, "Added BT sub instruction");
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding BT sub instruction");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding BT sub instruction");
		}
		
				
	}

	private void addDocuments(WebDriver driver, ExtentTest step, JSONArray documents) throws Exception {
		Iterator<Object> documentIterator = documents.iterator();
		
		while(documentIterator.hasNext()) {			
			JSONObject document = (JSONObject) documentIterator.next();
			
			try {				
				Select documentType = new Select(driver.findElements(By.xpath(adhocTransactionLocator.getString("select-TransactionDocument"))).get(0));
				documentType.selectByVisibleText(document.getString("document_type"));
				
				Select FileType = new Select(driver.findElements(By.xpath(adhocTransactionLocator.getString("select-TransactionDocument"))).get(1));
				FileType.selectByVisibleText(document.getString("file_type"));
				
				if(document.getString("file_type").equals("Attachment")) {
					File attachmentFile = new File(document.getString("attachment_upload_url"));
					driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionDocumentUpload"))).sendKeys(attachmentFile.getAbsolutePath());
				}
				
				while(driver.findElements(By.xpath(adhocTransactionLocator.getString("button-TransactionDocumentProgress"))).size()>0) {
					driver.findElement(By.xpath(adhocTransactionLocator.getString("button-AddTransactionDocument"))).click();
					Thread.sleep(500);
				}
				
				step.log(Status.INFO, "Added transaction document of type: "+document.getString("document_type"));
			}
		
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding transaction document of type: "+document.getString("document_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding transaction document of type: "+document.getString("document_type"));
			}
		}
		
	}
	
	private void adhocPaymentTransaction(WebDriver driver, ExtentTest step, JSONObject transaction) throws Exception {
		try {
			driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionName"))).clear();
			driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionName"))).sendKeys(transaction.getString("transaction_name"));
			
			Select transactionPurpose = new Select(driver.findElements(By.id(adhocTransactionLocator.getString("select-TransactionPurpose"))).get(1));
			transactionPurpose.selectByVisibleText(transaction.getString("transaction_purpose"));
			
			Thread.sleep(500);
			driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionSourceAccount"))).findElement(By.tagName("input")).click();
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionSourceAccount"))).findElement(By.tagName("ul")), transaction.getString("transaction_source_account"));
			
			Select transactionBalanceConsideration = new Select(driver.findElements(By.id(adhocTransactionLocator.getString("select-TransactionBalanceConsideration"))).get(1));
			transactionBalanceConsideration.selectByVisibleText(transaction.getString("transaction_balance_consideration"));
			
			List<WebElement> transactionCheckbox = driver.findElements(By.id(adhocTransactionLocator.getString("checkbox-TransactionCheckbox")));
			
			if(transaction.getBoolean("transaction_execute_later")) {
				WebElement executeLater = transactionCheckbox.get(0);
				executeLater.click();
				
				driver.findElements(By.id(adhocTransactionLocator.getString("input-TransactionDatePicker"))).get(0).sendKeys(transaction.getString("transaction_execute_on"));
				
				Select transactionScheduledAt = new Select(driver.findElements(By.id(adhocTransactionLocator.getString("select-TransactionScheduleAt"))).get(1));
				transactionScheduledAt.selectByVisibleText(transaction.getString("transaction_scheduled_at"));
				
				Select transactionHolidayAction = new Select(driver.findElements(By.id(adhocTransactionLocator.getString("select-TransactionHolidayAction"))).get(1));
				transactionHolidayAction.selectByVisibleText(transaction.getString("transaction_holiday_action"));
						
			}
			driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionRemarks"))).sendKeys(transaction.getString("transaction_remarks"));
			
			if(transaction.getBoolean("transaction_split")) {
				driver.findElement(By.id(adhocTransactionLocator.getString("checkbox-TransactionSplit"))).click();
				
				Select transactionSpecifyAmountAs = new Select(driver.findElements(By.id(adhocTransactionLocator.getString("select-TransactionSpecifyAmountAs"))).get(1));
				transactionSpecifyAmountAs.selectByVisibleText(transaction.getString("transaction_specify_amount_as"));
				
				driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionValue"))).sendKeys(transaction.getString("transaction_value"));				
			}
			
			if(transaction.getBoolean("transaction_pending_deferral")) {
				WebElement transactionPendingDeferral = transactionCheckbox.get(1);
				transactionPendingDeferral.click();
				
				driver.findElements(By.id(adhocTransactionLocator.getString("input-TransactionDatePicker"))).get(1).sendKeys(transaction.getString("transaction_deferral_closure_date"));
				
				driver.findElement(By.id(adhocTransactionLocator.getString("input-TransactionDeferralApprovedBy"))).sendKeys(transaction.getString("transaction_deferral_approved_by"));
				
				driver.findElements(By.id(adhocTransactionLocator.getString("input-TransactionDeferralNotes"))).get(0).sendKeys(transaction.getString("transaction_deferral_notes"));
				
			}
			
			if(transaction.getBoolean("transaction_pending_hard_copy")) {
				WebElement transactionPendingHardCopy = transactionCheckbox.get(2);
				transactionPendingHardCopy.click();
				
				driver.findElements(By.id(adhocTransactionLocator.getString("input-TransactionDeferralNotes"))).get(1).sendKeys(transaction.getString("transaction_pending_hard_copy_comments"));
			}
			driver.findElement(By.id(adhocTransactionLocator.getString("button-TransactionBasicNext"))).click();
			
			if(transaction.has("transaction_sub_instructions")) {
				
				Iterator<Object> transactionSubInstructionsIterator = transaction.getJSONArray("transaction_sub_instructions").iterator();
				
				while(transactionSubInstructionsIterator.hasNext()) {
					
					JSONObject transactionSubInstruction = (JSONObject) transactionSubInstructionsIterator.next();
					
					driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionBudget"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionBudget"))).findElement(By.tagName("ul")), transactionSubInstruction.getString("sub_instruction_budget"));
					
					Thread.sleep(500);
					driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionBudgetPurpose"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionBudgetPurpose"))).findElement(By.tagName("ul")), transactionSubInstruction.getString("sub_instruction_budget_purpose"));
					
					driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionInstrument"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(adhocTransactionLocator.getString("select-TransactionInstrument"))).findElement(By.tagName("ul")), transactionSubInstruction.getString("sub_instruction_instrument"));
					
					
					
					switch (transactionSubInstruction.getString("sub_instruction_instrument").toLowerCase()) {
					case "bt":
						addBTSubInstruction(driver, step, transactionSubInstruction);
						break;
					case "emails":
						
						break;
					default:
						break;
					}		
					
					driver.findElement(By.id(adhocTransactionLocator.getString("button-AddTransactionSubInstruction"))).click();
				}
			}
			
			driver.findElement(By.id(adhocTransactionLocator.getString("button-TransactionSubInstructionNext"))).click();
			
			if(transaction.has("transaction_documents")) {
				addDocuments(driver, step, transaction.getJSONArray("transaction_documents"));
			}
			
			driver.findElement(By.id(adhocTransactionLocator.getString("button-TransactionProceedToSummary"))).click();
			driver.findElement(By.xpath(adhocTransactionLocator.getString("label-TransactionNoError")));
			driver.findElement(By.xpath(adhocTransactionLocator.getString("button-TransactionSubmit"))).click();
			driver.findElement(By.xpath(adhocTransactionLocator.getString("button-TransactionSubmitYes"))).click();
			driver.findElement(By.xpath(adhocTransactionLocator.getString("button-TransactionSubmitOK")));			
			step.log(Status.PASS, "Submitted adhoc payment transaction '"+transaction.getString("transaction_name")+"' to Transaction Checker Queue");
			report.addStepPassScreenshot(driver, step);
			driver.findElement(By.xpath(adhocTransactionLocator.getString("button-TransactionSubmitOK"))).click();
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding Adhoc Transaction of type: Payment");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Adhoc Transaction of Type: Payment");
		}
		
	}
	
	public AdhocTransactionPage(LocatorFactory locators){			
		adhocTransactionLocator = locators.getLocators("AdhocTransactionPage");		
	}
	
	public void addTransaction(WebDriver driver, ExtentTest step, JSONObject transaction) throws Exception {
		try {
			switch (transaction.getString("sub_type").toLowerCase().trim()) {
			case "payment":
				adhocPaymentTransaction(driver, step, transaction);
				break;

			default:
				break;
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating Adhoc Transaction");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating Adhoc Transaction");
		}
	}

}
