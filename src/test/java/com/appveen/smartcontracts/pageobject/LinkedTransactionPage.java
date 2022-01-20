package com.appveen.smartcontracts.pageobject;

import java.io.File;
import java.util.ArrayList;
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

public class LinkedTransactionPage {
	 
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	
	private JSONObject linkedTransactionLocator = null; 
	
	protected void addBTSubInstruction(WebDriver driver, ExtentTest step,  JSONObject subInstruction) throws Exception {		
		try {
			
			driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionDebitAccountBICCode"))).sendKeys(subInstruction.getString("sub_instruction_debit_account_BIC_code"));
			
			Select subInstructionTransactionChargesBearedBy = new Select(driver.findElement(By.id(linkedTransactionLocator.getString("select-TransactionChargesBearedBy"))));
			subInstructionTransactionChargesBearedBy.selectByVisibleText(subInstruction.getString("sub_instruction_transaction_charges_beared_by"));
			
			driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionAmount"))).sendKeys(subInstruction.getString("sub_instruction_amount"));
			
			Select subInstructionPaymentCurrency = new Select(driver.findElement(By.id(linkedTransactionLocator.getString("select-TransactionCurrency"))));
			subInstructionPaymentCurrency.selectByVisibleText(subInstruction.getString("sub_instruction_currency"));
			
			driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionValueDate"))).sendKeys(subInstruction.getString("sub_instruction_value_date"));
			
			driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionBeneficiaryName"))).sendKeys(subInstruction.getString("sub_instruction_beneficiary_name"));
			
			driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionBeneficiaryAddress"))).sendKeys(subInstruction.getString("sub_instruction_beneficiary_address"));
			
			step.log(Status.INFO, "Added BT sub instruction");
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding BT sub instruction");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding BT sub instruction");
		}
		
				
	}

	private void addDocuments(WebDriver driver, ExtentTest step,  JSONArray documents) throws Exception {
		Iterator<Object> documentIterator = documents.iterator();
		
		while(documentIterator.hasNext()) {			
			JSONObject document = (JSONObject) documentIterator.next();
			try {
			Select documentType = new Select(driver.findElements(By.xpath(linkedTransactionLocator.getString("select-TransactionDocument"))).get(0));
			documentType.selectByVisibleText(document.getString("document_type"));
			
			Select FileType = new Select(driver.findElements(By.xpath(linkedTransactionLocator.getString("select-TransactionDocument"))).get(1));
			FileType.selectByVisibleText(document.getString("file_type"));
			
			if(document.getString("file_type").equals("Attachment")) {
				File attachmentFile = new File(document.getString("attachment_upload_url"));
				driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionDocumentUpload"))).sendKeys(attachmentFile.getAbsolutePath());
			}				
			
			while(driver.findElements(By.xpath(linkedTransactionLocator.getString("button-TransactionDocumentProgress"))).size()>0) {
				driver.findElement(By.xpath(linkedTransactionLocator.getString("button-AddTransactionDocument"))).click();
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
	
	private void linkedPaymentTransaction(WebDriver driver, ExtentTest step,  JSONObject transaction) throws Exception {
		try {
			List<String> transactionNameList = utility.extractColumnsFromTable(driver, step, By.xpath(linkedTransactionLocator.getString("table-LinkedInstructions")), 1);
			if(transactionNameList.contains(transaction.getString("transaction_name"))) {
				WebElement linkedInstruction = driver.findElements(By.xpath(linkedTransactionLocator.getString("table-LinkedInstructions"))).get(transactionNameList.indexOf(transaction.getString("transaction_name")));
				linkedInstruction.findElements(By.tagName("td")).get(6).findElement(By.tagName("i")).click();				
				List<WebElement> yesButton = driver.findElements(By.xpath(linkedTransactionLocator.getString("button-TransactionSubmitYes")));
				if(!yesButton.isEmpty() && yesButton.get(0).isDisplayed()) {
					yesButton.get(0).click();
				}
				try {
					linkedInstruction.findElements(By.tagName("td")).get(6).findElement(By.tagName("i")).click();				
					List<WebElement> yesButton1 = driver.findElements(By.xpath(linkedTransactionLocator.getString("button-TransactionSubmitYes")));
					if(!yesButton1.isEmpty() && yesButton1.get(0).isDisplayed()) {
						yesButton1.get(0).click();
					}
				}
				catch (Exception e) {
				}
			}
			else {
				step.fail("Transaction "+transaction.getString("transaction_name")+"  not available at Live Deals page");
				report.addStepFailScreenshot(driver, step);
			}
			driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionName"))).clear();
			driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionName"))).sendKeys(transaction.getString("transaction_name"));
			
			Select transactionPurpose = new Select(driver.findElements(By.id(linkedTransactionLocator.getString("select-TransactionPurpose"))).get(1));
			transactionPurpose.selectByVisibleText(transaction.getString("transaction_purpose"));
			
			if(driver.findElement(By.id(linkedTransactionLocator.getString("checkbox-TransactionSplit"))).findElement(By.tagName("input")).isEnabled()) {
				
				step.log(Status.INFO, "Split is configurable for linked transaction");
				report.addStepInfoScreenshot(driver, step);
				
				Thread.sleep(500);
				driver.findElement(By.id(linkedTransactionLocator.getString("select-TransactionSourceAccount"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(linkedTransactionLocator.getString("select-TransactionSourceAccount"))).findElement(By.tagName("ul")), transaction.getString("transaction_source_account"));
				
				Select transactionBalanceConsideration = new Select(driver.findElements(By.id(linkedTransactionLocator.getString("select-TransactionBalanceConsideration"))).get(1));
				transactionBalanceConsideration.selectByVisibleText(transaction.getString("transaction_balance_consideration"));
			
				if(transaction.getBoolean("transaction_split")) {
					driver.findElement(By.id(linkedTransactionLocator.getString("checkbox-TransactionSplit"))).click();
					
					Select transactionSpecifyAmountAs = new Select(driver.findElements(By.id(linkedTransactionLocator.getString("select-TransactionSpecifyAmountAs"))).get(1));
					transactionSpecifyAmountAs.selectByVisibleText(transaction.getString("transaction_specify_amount_as"));
					
					driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionValue"))).sendKeys(transaction.getString("transaction_value"));				
				}
			}
			else {
				step.log(Status.INFO, "Split is not configurable for linked transaction");
				report.addStepInfoScreenshot(driver, step);
			}
			
			
			List<WebElement> transactionCheckbox = driver.findElements(By.id(linkedTransactionLocator.getString("checkbox-TransactionCheckbox")));
			
			driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionRemarks"))).sendKeys(transaction.getString("transaction_remarks"));
			
						
			if(transaction.getBoolean("transaction_pending_deferral")) {
				WebElement transactionPendingDeferral = transactionCheckbox.get(0);	
				transactionPendingDeferral.click();
				
				driver.findElements(By.id(linkedTransactionLocator.getString("input-TransactionDatePicker"))).get(0).sendKeys(transaction.getString("transaction_deferral_closure_date"));
				
				driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionDeferralApprovedBy"))).sendKeys(transaction.getString("transaction_deferral_approved_by"));
				
				driver.findElements(By.id(linkedTransactionLocator.getString("input-TransactionDeferralNotes"))).get(0).sendKeys(transaction.getString("transaction_deferral_notes"));
				
			}
			
			if(transaction.getBoolean("transaction_pending_hard_copy")) {
				WebElement transactionPendingHardCopy = transactionCheckbox.get(1);
				transactionPendingHardCopy.click();
				
				driver.findElements(By.id(linkedTransactionLocator.getString("input-TransactionDeferralNotes"))).get(1).sendKeys(transaction.getString("transaction_pending_hard_copy_comments"));
			}
			driver.findElement(By.id(linkedTransactionLocator.getString("button-TransactionBasicNext"))).click();
			
			List<String> transactionInstrument = utility.extractColumnsFromTable(driver, step, By.id(linkedTransactionLocator.getString("table-TransactionSubInstructions")), 1);
			List<String> transactionBudgetPurpose = utility.extractColumnsFromTable(driver, step, By.id(linkedTransactionLocator.getString("table-TransactionSubInstructions")), 2);
			List<String> transactionAmount = utility.extractColumnsFromTable(driver, step, By.id(linkedTransactionLocator.getString("table-TransactionSubInstructions")), 4);
			
			if(transaction.has("transaction_sub_instructions")) {
				
				Iterator<Object> transactionSubInstructionsIterator = transaction.getJSONArray("transaction_sub_instructions").iterator();
				
				while(transactionSubInstructionsIterator.hasNext()) {
					
					JSONObject transactionSubInstruction = (JSONObject) transactionSubInstructionsIterator.next();
					
					Integer subInstructionIndex = null;
					for(int index = 0; index < transactionInstrument.size(); index++) {
						if(transactionInstrument.get(index).toLowerCase().trim().equals(transactionSubInstruction.getString("sub_instruction_instrument").toLowerCase().trim())) {
							if(transactionBudgetPurpose.get(index).toLowerCase().trim().equals(transactionSubInstruction.getString("sub_instruction_budget_purpose").toLowerCase().trim())) {
								if(transactionAmount.get(index).toLowerCase().trim().replace("₹", "").equals(transactionSubInstruction.getString("sub_instruction_amount").toLowerCase().trim())) {
									subInstructionIndex = index;
									break;
								}
							}
						}
					}
					
					if(subInstructionIndex == null) {
						step.log(Status.INFO, "Unable to find sub Instruction");
						report.addStepInfoScreenshot(driver, step);
					}
					
					utility.scrollIntoView(driver, driver.findElements(By.id(linkedTransactionLocator.getString("table-TransactionSubInstructions"))).get(subInstructionIndex).findElements(By.tagName("td")).get(12));
					driver.findElements(By.id(linkedTransactionLocator.getString("table-TransactionSubInstructions"))).get(subInstructionIndex).findElements(By.tagName("td")).get(12).click();
					
					List<WebElement> popupMenu = driver.findElement(By.id(linkedTransactionLocator.getString("menu-TransactionPopupMenu"))).findElements(By.tagName("div"));
					List<String> popupMenuItems = new ArrayList<String>();
					for(int index = 0; index < popupMenu.size(); index++) {
						popupMenuItems.add(popupMenu.get(index).getText().toLowerCase().trim());
					}
					if(transaction.getBoolean("transaction_amount_configure") || transaction.getBoolean("transaction_destination_configure")) {
						popupMenu.get(1).click();
						if(transaction.getBoolean("transaction_amount_configure")) {
							if(driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionAmount"))).isEnabled()) {
								step.log(Status.PASS, "Amount is configurable for linked transaction");
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Amount should be configurable for linked transaction");
								report.addStepFailScreenshot(driver, step);
							}
						}
						else {
							if(!driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionAmount"))).isEnabled()) {
								step.log(Status.PASS, "Amount is not configurable for linked transaction");
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Amount should not be configurable for linked transaction");
								report.addStepFailScreenshot(driver, step);
							}
						}
						if(transaction.getBoolean("transaction_destination_configure")) {
							utility.scrollIntoView(driver, driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionDestinationAccountEnabled"))));
							if(driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionDestinationAccountEnabled"))).isEnabled()) {
								step.log(Status.PASS, "Destination is configurable for linked transaction");
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Destination should be configurable for linked transaction");
								report.addStepFailScreenshot(driver, step);
							}
						}
						else {
							utility.scrollIntoView(driver, driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionDestinationAccountDisabled"))));
							if(!driver.findElement(By.id(linkedTransactionLocator.getString("input-TransactionDestinationAccountDisabled"))).isEnabled()) {
								step.log(Status.PASS, "Destination is not configurable for linked transaction");
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Destination should not be configurable for linked transaction");
								report.addStepFailScreenshot(driver, step);
							}
						}						
						driver.findElement(By.id(linkedTransactionLocator.getString("button-CloseTransactionSubInstruction"))).click();
					}
					else {
						if(popupMenuItems.contains("edit")) {
							step.log(Status.FAIL, "Both Amount and Destination are not configurable for linked transaction");
							report.addStepFailScreenshot(driver, step);
						}
						else {
							step.log(Status.PASS, "Both Amount and Destination are not configurable for linked transaction");
							report.addStepPassScreenshot(driver, step);
						}
					}					
				}
			}
			
			driver.findElement(By.id(linkedTransactionLocator.getString("button-TransactionSubInstructionNext"))).click();
			
			if(transaction.has("transaction_documents")) {
				addDocuments(driver, step, transaction.getJSONArray("transaction_documents"));
			}
			
			driver.findElement(By.id(linkedTransactionLocator.getString("button-TransactionProceedToSummary"))).click();
			driver.findElement(By.xpath(linkedTransactionLocator.getString("label-TransactionNoError")));
			driver.findElement(By.xpath(linkedTransactionLocator.getString("button-TransactionSubmit"))).click();
			driver.findElement(By.xpath(linkedTransactionLocator.getString("button-TransactionSubmitYes"))).click();
			driver.findElement(By.xpath(linkedTransactionLocator.getString("button-TransactionSubmitOK")));			
			step.log(Status.PASS, "Submitted linked payment transaction '"+transaction.getString("transaction_name")+"' to Transaction Checker Queue");
			report.addStepPassScreenshot(driver, step);
			driver.findElement(By.xpath(linkedTransactionLocator.getString("button-TransactionSubmitOK"))).click();
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding Linked Transaction of Type : Payment");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Linked Transaction of Type : Payment");
		}
		
	}
	
	public LinkedTransactionPage(LocatorFactory locators) {
		linkedTransactionLocator = locators.getLocators("LinkedTransactionPage");			
	}
	
	public void addTransaction(WebDriver driver, ExtentTest step,  JSONObject transaction) throws Exception {
		try {
			switch (transaction.getString("sub_type").toLowerCase().trim()) {
			case "payment":
				linkedPaymentTransaction(driver, step, transaction);
				break;

			default:
				break;
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating Linked Transaction");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating Linked Transaction");
		}
	}


}
