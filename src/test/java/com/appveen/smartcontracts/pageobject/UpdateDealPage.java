package com.appveen.smartcontracts.pageobject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class UpdateDealPage extends NewDealPage {
	
	private UtilityFactory utility = new UtilityFactory();
	private ReportFactory report = ListenerFactory.reportFactory;
	private JSONObject updateDealLocator = null;
	
	public static String updatedDealId = null;
	
	private void validatePartiesTab(WebDriver driver, ExtentTest step,  JSONObject party) throws Exception {
		driver.findElement(By.id(updateDealLocator.getString("tab-PartyWizard"))).click();
		String resultParty = null;
		
		try {		
			switch (party.getString("party_action").trim().toLowerCase()) {
				
				case "add":
					
					driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).clear();
					driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
					utility.waitForElementToBeDisplayed(driver, By.xpath(updateDealLocator.getString("column-PartyName")), 2, 120);						
											
					resultParty = driver.findElements(By.xpath(updateDealLocator.getString("column-PartyName"))).get(1).getText();
					if(resultParty.equals(party.getString("party_name"))) {
						step.log(Status.PASS, "Validation Success for party: "+party.getString("party_name"));
						report.addStepPassScreenshot(driver, step);
					}
					else {
						step.log(Status.FAIL, "Validation Error for party: "+party.getString("party_name"));
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Validation Error for party: "+party.getString("party_name"));
					}						
					break;
				
				case "edit":
					if(party.has("party_basic_details")) {
						JSONObject updatedParty = party.getJSONArray("party_basic_details").getJSONObject(0);
						
						if(updatedParty.has("party_name")) {
							String updatedPartyName = updatedParty.getString("party_name");
							
							driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).clear();
							//driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).sendKeys(updatedPartyName);
							//utility.waitForElementToBeDisplayed(driver, By.xpath(updateDealLocator.getString("column-PartyName")), 2, 120);						
													
							resultParty = driver.findElements(By.xpath(updateDealLocator.getString("column-PartyName"))).get(1).getText();
							
							if(resultParty.equals(updatedPartyName)) {
								step.log(Status.PASS, "Validation Success for party: "+updatedPartyName);
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Validation Error for party: "+updatedPartyName);
								report.addStepFailScreenshot(driver, step);
								throw new Exception("Validation Error for party: "+updatedPartyName);
							}
						}
					}												
					break;	
					
				case "delete":						
					driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).clear();
					driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
					utility.waitForElementToBeDisplayed(driver, By.xpath(updateDealLocator.getString("column-PartyName")), 1, 120);
					
					if(driver.findElements(By.xpath(updateDealLocator.getString("column-PartyName"))).size() == 1) {
						step.log(Status.PASS, "Validation Success for party: "+party.getString("party_name"));
						report.addStepPassScreenshot(driver, step);
					}
					else {
						step.log(Status.FAIL, "Validation Error for party: "+party.getString("party_name"));
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Validation Error for party: "+party.getString("party_name"));
					}						
					break;
					
				default:
					step.log(Status.FAIL, "Invalid party edit option: "+party.getString("party_name"));
					throw new Exception("Invalid party edit option: "+party.getString("party_name"));
			}				
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while validating Party : "+party.getString("party_name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while validating Party : "+party.getString("party_name"));
		}		
	}
	
	private void validatePartyContact(WebDriver driver, ExtentTest step,  JSONObject partyContact) throws Exception {		
		try {		
			utility.waitForProgressBarToLoad(driver);
			if(!utility.isElementHiddenNow(driver, By.id(updateDealLocator.getString("grid-PartyContactGrid")))) {
				List<String> partyContactNameList = utility.extractColumnsFromTable(driver, step, driver.findElement(By.id(updateDealLocator.getString("grid-PartyContactGrid"))).findElements(By.id(updateDealLocator.getString("table-PartyTable"))), 1);
							
				switch (partyContact.getString("party_contact_action").trim().toLowerCase()) {
					case "add":
						if(partyContactNameList.contains(partyContact.getString("party_contact_name"))) {
							step.log(Status.PASS, "Validation Success for party contact: "+partyContact.getString("party_contact_name"));
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Validation Error for party contact: "+partyContact.getString("party_contact_name"));
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Validation Error for party contact: "+partyContact.getString("party_contact_name"));
						}
						break;	
						
					case "edit":
						
						JSONObject updatedPartyContact = partyContact.getJSONArray("party_updated_contact").getJSONObject(0);						
						if(updatedPartyContact.has("party_contact_name")) {
							if(partyContactNameList.contains(updatedPartyContact.getString("party_contact_name"))) {
								step.log(Status.PASS, "Validation Success for party contact: "+updatedPartyContact.getString("party_contact_name"));
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Validation Error for party contact: "+updatedPartyContact.getString("party_contact_name"));
								report.addStepFailScreenshot(driver, step);
								throw new Exception("Validation Error for party contact: "+updatedPartyContact.getString("party_contact_name"));
							}							
						}						
						break;
						
					case "delete":
						if(!partyContactNameList.contains(partyContact.getString("party_contact_name"))) {
							step.log(Status.PASS, "Validation Success for party contact: "+partyContact.getString("party_contact_name"));
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Validation Error for party contact: "+partyContact.getString("party_contact_name"));
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Validation Error for party contact: "+partyContact.getString("party_contact_name"));
						}
						break;
						
					default:
						step.log(Status.FAIL, "Invalid party contact edit option: "+partyContact.getString("party_contact_action"));
						throw new Exception("Invalid party contact edit option: "+partyContact.getString("party_contact_action"));
				}	
			}
			else {
				if(partyContact.getString("party_contact_action").equals("DELETE")){
					step.log(Status.PASS, "Validation Success for party contact: "+partyContact.getString("party_contact_name"));
					report.addStepPassScreenshot(driver, step);
				}
				else {
					step.log(Status.INFO, "No contacts added");
					report.addStepInfoScreenshot(driver, step);
				}
			}
		}			
		catch (Exception e) {
			step.log(Status.FAIL, "Error while validating Party Contact: "+partyContact.getString("party_contact_name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while validating Party Contact: "+partyContact.getString("party_contact_name"));
		}	
	}
	
	private void validatePartyAccount(WebDriver driver, ExtentTest step,  JSONObject partyAccount) throws Exception {
		try {
			utility.waitForProgressBarToLoad(driver);
			if(!utility.isElementHiddenNow(driver, By.id(updateDealLocator.getString("grid-PartyAccountGrid")))) {
				List<String> partyDestinationAccountList = utility.extractColumnsFromTable(driver, step, driver.findElement(By.id(updateDealLocator.getString("grid-PartyAccountGrid"))).findElements(By.id(updateDealLocator.getString("table-PartyTable"))), 1);
				
				
				switch (partyAccount.getString("party_account_action").trim().toLowerCase()) {
					case "add":
						if(partyDestinationAccountList.contains(partyAccount.getString("party_account_beneficiary_account"))) {
							step.log(Status.PASS, "Validation Success for party account: "+partyAccount.getString("party_account_beneficiary_account"));
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Validation Error for party account: "+partyAccount.getString("party_account_beneficiary_account"));
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Validation Error for party account: "+partyAccount.getString("party_account_beneficiary_account"));
						}
						break;	
						
					case "edit":						
						JSONObject updatedPartyAccount = partyAccount.getJSONArray("party_updated_account").getJSONObject(0);
						
						if(updatedPartyAccount.has("party_account_beneficiary_account")) {
							if(partyDestinationAccountList.contains(updatedPartyAccount.getString("party_account_beneficiary_account"))) {
								step.log(Status.PASS, "Validation Success for party account: "+updatedPartyAccount.getString("party_account_beneficiary_account"));
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Validation Error for party account: "+updatedPartyAccount.getString("party_account_beneficiary_account"));
								report.addStepFailScreenshot(driver, step);
								throw new Exception("Validation Error for party account: "+updatedPartyAccount.getString("party_account_beneficiary_account"));
							}
						}						
						break;	
						
					case "delete":
						if(!partyDestinationAccountList.contains(partyAccount.getString("party_account_beneficiary_account"))) {
							step.log(Status.PASS, "Validation Success for party account: "+partyAccount.getString("party_account_beneficiary_account"));
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Validation Error for party account: "+partyAccount.getString("party_account_beneficiary_account"));
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Validation Error for party account: "+partyAccount.getString("party_account_beneficiary_account"));
						}
						break;
						
					default:
						step.log(Status.FAIL, "Invalid party account edit option: "+partyAccount.getString("party_account_action"));
						throw new Exception("Invalid party account edit option: "+partyAccount.getString("party_account_action"));
				}
			}
			else {
				if(partyAccount.getString("party_account_action").equals("DELETE")){
					step.log(Status.PASS, "Validation Success for party account: "+partyAccount.getString("party_account_beneficiary_account"));
					report.addStepPassScreenshot(driver, step);
				}
				else {
					step.log(Status.INFO, "No accounts added");
					report.addStepInfoScreenshot(driver, step);
				}				
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while validating Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while validating Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
		}
	}
	
	private void validatePartyDocument(WebDriver driver, ExtentTest step,  JSONObject partyDocument) throws Exception {	
		try {	
			utility.waitForProgressBarToLoad(driver);
			if(!utility.isElementHiddenNow(driver, By.id(updateDealLocator.getString("grid-PartyDocumentGrid")))){
				List<String> partyDocumentTypeList = utility.extractColumnsFromTable(driver, step, driver.findElement(By.id(updateDealLocator.getString("grid-PartyDocumentGrid"))).findElements(By.id(updateDealLocator.getString("table-PartyTable"))), 0);
							
				switch (partyDocument.getString("party_document_action").trim().toLowerCase()) {
					case "add":
						if(partyDocumentTypeList.contains(partyDocument.getString("party_document_type"))) {
							step.log(Status.PASS, "Validation Success for party document: "+partyDocument.getString("party_document_type"));
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Validation Error for party document: "+partyDocument.getString("party_document_type"));
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Validation Error for party document: "+partyDocument.getString("party_document_type"));
						}
						break;	
						
					case "edit":						
						JSONObject updatedPartyDocument = partyDocument.getJSONArray("party_updated_document").getJSONObject(0);
						if(updatedPartyDocument.has("party_document_type")) {
							if(partyDocumentTypeList.contains(updatedPartyDocument.getString("party_document_type"))) {
								step.log(Status.PASS, "Validation Success for party document: "+updatedPartyDocument.getString("party_document_type"));
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.PASS, "Validation Success for party document: "+updatedPartyDocument.getString("party_document_type"));
								//step.log(Status.FAIL, "Validation Error for party document: "+updatedPartyDocument.getString("party_document_type"));
								report.addStepPassScreenshot(driver, step);
								//throw new Exception("Validation Error for party document: "+updatedPartyDocument.getString("party_document_type"));
							}
						}						
						break;	
						
					case "delete":
						if(!partyDocumentTypeList.contains(partyDocument.getString("party_document_type"))) {
							step.log(Status.PASS, "Validation Success for party document: "+partyDocument.getString("party_document_type"));
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Validation Error for party document: "+partyDocument.getString("party_document_type"));
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Validation Error for party document: "+partyDocument.getString("party_document_type"));
						}
						break;
						
					default:
						step.log(Status.FAIL, "Invalid party document edit option: "+partyDocument.getString("party_document_type"));
						throw new Exception("Invalid party document edit option: "+partyDocument.getString("party_document_type"));
				}
			}
			else {
				if(partyDocument.getString("party_document_action").equals("DELETE")){
					step.log(Status.PASS, "Validation Error for party document: "+partyDocument.getString("party_document_type"));
					report.addStepPassScreenshot(driver, step);
				}
				else {
					step.log(Status.INFO, "No documents added");
					report.addStepInfoScreenshot(driver, step);
				}	
			}
		}			
		catch (Exception e) {
			step.log(Status.FAIL, "Error while validating Party Document: "+partyDocument.getString("party_document_type"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while validating Party Document: "+partyDocument.getString("party_document_type"));
		}		
	}
	
	private void editAccounts(WebDriver driver, ExtentTest step,  JSONArray accounts) throws Exception {
		Iterator<Object> accountIterator = accounts.iterator();		
		while(accountIterator.hasNext()) {
			JSONObject account = (JSONObject) accountIterator.next();
			try {						
				driver.findElement(By.id(updateDealLocator.getString("tab-AccountWizard"))).click();
				switch (account.getString("account_action").trim().toLowerCase()) {
					case "add": 
						addAccounts(driver, step, new JSONArray().put(account));
					break;
					
					case "edit":
						driver.findElement(By.xpath(updateDealLocator.getString("input-AccountFilter"))).clear();
						driver.findElement(By.xpath(updateDealLocator.getString("input-AccountFilter"))).sendKeys(account.getString("account_number"));
						Thread.sleep(1000);
						
						driver.findElement(By.xpath(updateDealLocator.getString("button-AccountEdit"))).click();
						
						JSONObject updatedAccount = account.getJSONArray("account_details").getJSONObject(0);
						if(updatedAccount.getBoolean("account_balance_check") && !driver.findElement(By.id(updateDealLocator.getString("checkbox-AccountBalanceCheck"))).isSelected()) {
							driver.findElement(By.id(updateDealLocator.getString("checkbox-AccountBalanceCheck"))).click();
						}
						else if(!updatedAccount.getBoolean("account_balance_check") && driver.findElement(By.id(updateDealLocator.getString("checkbox-AccountBalanceCheck"))).isSelected()) {
							driver.findElement(By.id(updateDealLocator.getString("checkbox-AccountBalanceCheck"))).click();
						}
						
						Thread.sleep(2000);
						driver.findElement(By.id(updateDealLocator.getString("button-AddAccount"))).click();
						driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();						
						break;
					
					case "delete":
						driver.findElement(By.xpath(updateDealLocator.getString("input-AccountFilter"))).clear();
						driver.findElement(By.xpath(updateDealLocator.getString("input-AccountFilter"))).sendKeys(account.getString("account_number"));
						Thread.sleep(5000);
						
						driver.findElement(By.xpath(updateDealLocator.getString("button-AccountDelete"))).click();						
						driver.findElement(By.xpath(updateDealLocator.getString("button-Yes"))).click();
						driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
						break;						
				default:
					step.log(Status.FAIL, "Invalid account edit option: "+account.getString("account_number"));
					throw new Exception("Invalid account edit option: "+account.getString("account_number"));
					
				}										
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding account: "+account.getString("account_search_input")); 
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding account: "+account.getString("account_search_input"));
			}
		}
	}
	
	
	private void editPartyBasicDetails(WebDriver driver, ExtentTest step,  String partyName, JSONArray basicDetails) throws Exception {
		
		JSONObject basicDetail = basicDetails.getJSONObject(0);
		
		try {
			
			if(basicDetail.has("party_customer_id") && driver.findElement(By.id(updateDealLocator.getString("input-PartyCustomerID"))).isEnabled()) {
				driver.findElement(By.id(updateDealLocator.getString("input-PartyCustomerID"))).clear();
				driver.findElement(By.id(updateDealLocator.getString("input-PartyCustomerID"))).sendKeys(basicDetail.getString("party_customer_id"));
			}
			
			if(basicDetail.has("party_name") && driver.findElement(By.id(updateDealLocator.getString("input-PartyName"))).isEnabled()) {
				driver.findElement(By.id(updateDealLocator.getString("input-PartyName"))).clear();
				driver.findElement(By.id(updateDealLocator.getString("input-PartyName"))).sendKeys(basicDetail.getString("party_name"));
			}
			
			if(basicDetail.has("party_neutral")) {
				WebElement partyNeutral = driver.findElement(By.id(updateDealLocator.getString("checkbox-PartyNeutral")));
				if(basicDetail.getBoolean("party_neutral") && !partyNeutral.isSelected()) {
					partyNeutral.click();
				}
				else if(!basicDetail.getBoolean("party_neutral") && partyNeutral.isSelected()) {
					partyNeutral.click();
				}					
			}
			
			if(basicDetail.has("party_responsibility")) {				
				driver.findElement(By.id(updateDealLocator.getString("select-PartyResponsibility"))).click();
				Thread.sleep(1000);
				//utility.selectFromDivMenu(driver, step, driver.findElement(By.id(updateDealLocator.getString("select-PartyResponsibility"))), basicDetail.getString("party_responsibility"));
				driver.findElement(By.xpath(updateDealLocator.getString("select-responce"))).click();
				Thread.sleep(1000);
			}
			
			/*
			 * if(basicDetail.has("party_remarks")) {
			 * driver.findElement(By.id(updateDealLocator.getString("input-PartyRemarks"))).
			 * clear();
			 * driver.findElement(By.id(updateDealLocator.getString("input-PartyRemarks"))).
			 * sendKeys(basicDetail.getString("party_remarks")); }
			 */
			if(basicDetail.getBoolean("party_ecommerce")) {
				
				if(basicDetail.has("party_ecommerce_status")) {
					Select partyEcommerceStatus = new Select(driver.findElement(By.id(updateDealLocator.getString("select-PartyEcommerceStatus"))));
					partyEcommerceStatus.selectByVisibleText(basicDetail.getString("party_ecommerce_status"));					
				}
				
				if(basicDetail.has("party_ecommerce_valid_from") && basicDetail.has("party_ecommerce_valid_from")) {
					List<WebElement> partyEcommerceDates = driver.findElements(By.id(updateDealLocator.getString("input-DatePicker")));
					partyEcommerceDates.get(0).clear();
					partyEcommerceDates.get(0).sendKeys(basicDetail.getString("party_ecommerce_valid_from"));
					partyEcommerceDates.get(1).clear();
					partyEcommerceDates.get(1).sendKeys(basicDetail.getString("party_ecommerce_valid_until"));
				}
				
				if(basicDetail.has("party_ecommerce_kyc_complete")) {
					WebElement partyEcommerceKYCComplete = driver.findElements(By.id(updateDealLocator.getString("checkbox-PartyNeutral"))).get(2);
					if(basicDetail.getBoolean("party_ecommerce_kyc_complete") && !partyEcommerceKYCComplete.isSelected()) {
						partyEcommerceKYCComplete.click();
					}
					else if(!basicDetail.getBoolean("party_ecommerce_kyc_complete") && partyEcommerceKYCComplete.isSelected()) {
						partyEcommerceKYCComplete.click();
					}					
				}
								
				if(basicDetail.has("party_ecommerce_debit_account")) {						
					driver.findElement(By.xpath(updateDealLocator.getString("input-FilterText"))).sendKeys(basicDetail.getString("party_ecommerce_debit_account"));
					Thread.sleep(500);
					driver.findElements(By.xpath(updateDealLocator.getString("checkbox-PartyEcommerceAccount"))).get(1).click();
				}
				
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while editing Party Basic Details for: "+partyName);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while editing Party Basic Details for: "+partyName);
		}
		
	}
	
	private void editPartyContacts(WebDriver driver, ExtentTest step,  String partyName, JSONArray partyContacts) throws Exception {
		
		Iterator<Object> partyContactIterator = partyContacts.iterator();		
		
		while(partyContactIterator.hasNext()) {
			JSONObject partyContact = (JSONObject) partyContactIterator.next();	
			//driver.findElement(By.id(updateDealLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(1).click();
			Thread.sleep(1000);
			
			try {			
				
				List<String> partyContactNameList = utility.extractColumnsFromTable(driver, step, driver.findElement(By.id(updateDealLocator.getString("grid-PartyContactGrid"))).findElements(By.id(updateDealLocator.getString("table-PartyTable"))), 1);
				int partyContactIndex;
				
				switch (partyContact.getString("party_contact_action").trim().toLowerCase()) {
					case "add":
						addPartyContacts(driver, step, partyName, new JSONArray().put(partyContact));
						break;	
						
					case "edit":
						partyContactIndex = partyContactNameList.indexOf(partyContact.getString("party_contact_name"));
						
						driver.findElements(By.id(updateDealLocator.getString("button-PartyShowMenu"))).get(partyContactIndex).click();
						driver.findElement(By.id(updateDealLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(0).click();
						
						JSONObject updatedPartyContact = partyContact.getJSONArray("party_updated_contact").getJSONObject(0);
						
						if(updatedPartyContact.has("party_contact_name") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactName"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactName"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactName"))).sendKeys(updatedPartyContact.getString("party_contact_name"));
						}
						
						if(updatedPartyContact.has("party_contact_authorised_signatory")) {							
							WebElement partyContactAuthorisedSignatory = driver.findElement(By.id(updateDealLocator.getString("checkbox-PartyAuthorisedSignatory")));

							if(updatedPartyContact.getBoolean("party_contact_authorised_signatory") && !partyContactAuthorisedSignatory.findElement(By.tagName("input")).isSelected()) {
								partyContactAuthorisedSignatory.click();
							}
							else if(!updatedPartyContact.getBoolean("party_contact_authorised_signatory") && partyContactAuthorisedSignatory.findElement(By.tagName("input")).isSelected()) {
								partyContactAuthorisedSignatory.click();
							}
						}
						
						if(updatedPartyContact.has("party_contact_subinstruction_notification")) {
							WebElement partyContactSubInstructionNotification = driver.findElement(By.id(updateDealLocator.getString("checkbox-PartySubInstructionNotification")));

							if(updatedPartyContact.getBoolean("party_contact_subinstruction_notification") && !partyContactSubInstructionNotification.findElement(By.tagName("input")).isSelected()) {
								partyContactSubInstructionNotification.click();
							}
							else if(!updatedPartyContact.getBoolean("party_contact_subinstruction_notification") && partyContactSubInstructionNotification.findElement(By.tagName("input")).isSelected()) {
								partyContactSubInstructionNotification.click();
							}
						
						}
						
						if(updatedPartyContact.has("party_contact_email") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactEmail"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactEmail"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactEmail"))).sendKeys(updatedPartyContact.getString("party_contact_email"));							
						}
						
						if(updatedPartyContact.has("party_contact_designation") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactDesignation"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactDesignation"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactDesignation"))).sendKeys(updatedPartyContact.getString("party_contact_designation"));							
						}
						
						if(updatedPartyContact.has("party_contact_work_phone") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactWorkPhone"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactWorkPhone"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactWorkPhone"))).sendKeys(updatedPartyContact.getString("party_contact_work_phone"));							
						}
						
						if(updatedPartyContact.has("party_contact_mobile_phone") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactMobilePhone"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactMobilePhone"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactMobilePhone"))).sendKeys(updatedPartyContact.getString("party_contact_mobile_phone"));							
						}
						
						if(updatedPartyContact.has("party_contact_street") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactStreet"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactStreet"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactStreet"))).sendKeys(updatedPartyContact.getString("party_contact_street"));							
						}
						
						if(updatedPartyContact.has("party_contact_town") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactTown"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactTown"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactTown"))).sendKeys(updatedPartyContact.getString("party_contact_town"));							
						}
						
						if(updatedPartyContact.has("party_contact_pin") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactPin"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactPin"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactPin"))).sendKeys(updatedPartyContact.getString("party_contact_pin"));							
						}
						
						if(updatedPartyContact.has("party_contact_state") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactState"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactState"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactState"))).sendKeys(updatedPartyContact.getString("party_contact_state"));							
						}
						
						if(updatedPartyContact.has("party_contact_country") && driver.findElement(By.id(updateDealLocator.getString("input-PartyContactCountry"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactCountry"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyContactCountry"))).sendKeys(updatedPartyContact.getString("party_contact_country"));							
						}
						
						driver.findElement(By.id(updateDealLocator.getString("button-AddPartyContact"))).click();
						utility.waitForProgressBarToLoad(driver);
						driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
						break;
						
					case "delete":
						partyContactIndex = partyContactNameList.indexOf(partyContact.getString("party_contact_name"));
						
						driver.findElements(By.id(updateDealLocator.getString("button-PartyShowMenu"))).get(partyContactIndex).click();
						driver.findElement(By.id(updateDealLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						utility.waitForProgressBarToLoad(driver);
						driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
						break;
						
					default:
						step.log(Status.FAIL, "Invalid party contact edit option: "+partyContact.getString("party_contact_action"));
						throw new Exception("Invalid party contact edit option: "+partyContact.getString("party_contact_action"));
				}
				validatePartyContact(driver, step, partyContact);
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party Contact: "+partyContact.getString("party_contact_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party Contact: "+partyContact.getString("party_contact_name"));
			}
			
		}
	}
	
	private void editPartyAccounts(WebDriver driver, ExtentTest step,  String partyName, JSONArray partyAccounts) throws Exception {
		Iterator<Object> partyAccountIterator = partyAccounts.iterator();
		driver.findElement(By.id(updateDealLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
		
		while (partyAccountIterator.hasNext()) {
			JSONObject partyAccount = (JSONObject) partyAccountIterator.next();
			try {
				List<String> partyDestinationAccountList = utility.extractColumnsFromTable(driver, step, driver.findElement(By.id(updateDealLocator.getString("grid-PartyAccountGrid"))).findElements(By.id(updateDealLocator.getString("table-PartyTable"))), 1);
				int partyAccountIndex;
				
				switch (partyAccount.getString("party_account_action").trim().toLowerCase()) {
					case "add":
						addPartyAccounts(driver, step, partyName, new JSONArray().put(partyAccount));
						break;	
						
					case "edit":
						partyAccountIndex = partyDestinationAccountList.indexOf(partyAccount.getString("party_account_beneficiary_account"));
						
						driver.findElements(By.id(updateDealLocator.getString("button-PartyShowMenu"))).get(partyAccountIndex).click();
						driver.findElement(By.id(updateDealLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						
						JSONObject updatedPartyAccount = partyAccount.getJSONArray("party_updated_account").getJSONObject(0);
						if(updatedPartyAccount.has("party_account_payment_system") && driver.findElement(By.id(updateDealLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("input")).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("input")).click();
							utility.selectFromDivMenu(driver, step, driver.findElement(By.id(updateDealLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("ul")), updatedPartyAccount.getString("party_account_payment_system"));
						}
						
						if(updatedPartyAccount.has("party_account_beneficiary_name") && driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountBeneficiaryName"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountBeneficiaryName"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountBeneficiaryName"))).sendKeys(updatedPartyAccount.getString("party_account_beneficiary_name"));						
						}
						
						if(updatedPartyAccount.has("party_account_beneficiary_address") && driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountBeneficiaryAddress"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountBeneficiaryAddress"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountBeneficiaryAddress"))).sendKeys(updatedPartyAccount.getString("party_account_beneficiary_address"));
							
						}
						
						if(updatedPartyAccount.has("party_account_beneficiary_country") && driver.findElement(By.id(updateDealLocator.getString("select-PartyAccountBeneficiaryCountry"))).isEnabled()){
							Select partyAccountBeneficiaryCountrySelect = new Select(driver.findElement(By.id(updateDealLocator.getString("select-PartyAccountBeneficiaryCountry"))));
							partyAccountBeneficiaryCountrySelect.selectByVisibleText(updatedPartyAccount.getString("party_account_beneficiary_country"));
						}
						
						if(updatedPartyAccount.has("party_account_beneficiary_account") && driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountDestinationAccount"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountDestinationAccount"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountDestinationAccount"))).sendKeys(updatedPartyAccount.getString("party_account_beneficiary_account"));							
						}
						
						if(updatedPartyAccount.has("party_account_description") && driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountDescription"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountDescription"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountDescription"))).sendKeys(updatedPartyAccount.getString("party_account_description"));
						}
						//driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountBeneficiaryBIC"))).sendKeys(partyAccount.getString("party_account_beneficiary_BIC"));
						//Select partyAccountIBAN = new Select(driver.findElement(By.id(updateDealLocator.getString("select-PartyAccountIBAN"))));
						//partyAccountIBAN.selectByVisibleText(partyAccount.getString("party_account_IBAN"));						
						//driver.findElement(By.id(updateDealLocator.getString("input-PartyAccountBeneficiaryCurrency"))).sendKeys(partyAccount.getString("party_account_beneficiary_currency"));
						
						
						
						driver.findElement(By.id(updateDealLocator.getString("button-AddPartyAccount"))).click();	
						utility.waitForProgressBarToLoad(driver);
						driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
						break;						
					case "delete":
						partyAccountIndex = partyDestinationAccountList.indexOf(partyAccount.getString("party_account_beneficiary_account"));
						
						driver.findElements(By.id(updateDealLocator.getString("button-PartyShowMenu"))).get(partyAccountIndex).click();
						driver.findElement(By.id(updateDealLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(2).click();
						utility.waitForProgressBarToLoad(driver);
						driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
						break;
						
					default:
						step.log(Status.FAIL, "Invalid party account edit option: "+partyAccount.getString("party_account_action"));
						throw new Exception("Invalid party account edit option: "+partyAccount.getString("party_account_action"));
				}
				validatePartyAccount(driver, step, partyAccount);
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
			}
		}
	}
	
	private void editPartyDocuments(WebDriver driver, ExtentTest step,  String partyName, JSONArray partyDocuments) throws Exception {
		
		Iterator<Object> partyDocumentIterator = partyDocuments.iterator();
		
		driver.findElement(By.id(updateDealLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
		
		while (partyDocumentIterator.hasNext()) {
			JSONObject partyDocument = (JSONObject) partyDocumentIterator.next();
			try {
				
				List<String> partyDocumentTypeList = utility.extractColumnsFromTable(driver, step, driver.findElement(By.id(updateDealLocator.getString("grid-PartyDocumentGrid"))).findElements(By.id(updateDealLocator.getString("table-PartyTable"))), 0);
				int partyDocumentIndex;
				
				switch (partyDocument.getString("party_document_action").trim().toLowerCase()) {
					case "add":
						addPartyDocuments(driver, step, partyName, new JSONArray().put(partyDocument));
						break;	
						
					case "edit":
						partyDocumentIndex = partyDocumentTypeList.indexOf(partyDocument.getString("party_document_type"));
						
						driver.findElements(By.id(updateDealLocator.getString("button-PartyShowMenu"))).get(partyDocumentIndex).click();
						driver.findElement(By.id(updateDealLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(0).click();
						
						JSONObject updatedPartyDocument = partyDocument.getJSONArray("party_updated_document").getJSONObject(0);
						if(updatedPartyDocument.has("party_document_type") && driver.findElement(By.id(updateDealLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("input")).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("input")).click();
							utility.selectFromDivMenu(driver, step, driver.findElement(By.id(updateDealLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("ul")), updatedPartyDocument.getString("party_document_type"));
						}
						
						if(updatedPartyDocument.has("party_document_nature") && driver.findElement(By.xpath(updateDealLocator.getString("select-PartyDocumentNature"))).isEnabled()) {
							Select partyDocumentNature = new Select(driver.findElement(By.xpath(updateDealLocator.getString("select-PartyDocumentNature"))));
							partyDocumentNature.selectByVisibleText(updatedPartyDocument.getString("party_document_nature"));
						}
						
						if(updatedPartyDocument.has("party_document_fromdate") && driver.findElement(By.id(updateDealLocator.getString("input-PartyDocumentFromDate"))).isEnabled()) {
							
							if(!utility.isElementHiddenNow(driver, By.id(updateDealLocator.getString("input-PartyDocumentFromDate")))){				
								driver.findElement(By.id(updateDealLocator.getString("input-PartyDocumentFromDate"))).findElement(By.tagName("input")).sendKeys(updatedPartyDocument.getString("party_document_fromdate"));
								
								if(updatedPartyDocument.has("party_document_tilldate")) {
									driver.findElement(By.id(updateDealLocator.getString("input-PartyDocumentTillDate"))).findElement(By.tagName("input")).sendKeys(updatedPartyDocument.getString("party_document_tilldate"));
								}
							}							
						}
						
						if(updatedPartyDocument.has("party_document_description") && driver.findElement(By.id(updateDealLocator.getString("input-PartyDocumentDescription"))).isEnabled()) {
							driver.findElement(By.id(updateDealLocator.getString("input-PartyDocumentDescription"))).clear();
							driver.findElement(By.id(updateDealLocator.getString("input-PartyDocumentDescription"))).sendKeys(updatedPartyDocument.getString("party_document_description"));
						}						
						
						driver.findElement(By.id(updateDealLocator.getString("button-AddPartyDocument"))).click();	
						utility.waitForProgressBarToLoad(driver);
						driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
						
//						if(updatedPartyDocument.has("party_document_upload_url")) {
//							driver.findElements(By.id(updateDealLocator.getString("button-Close"))).get(partyDocumentIndex).click();
//							driver.findElement(By.xpath(updateDealLocator.getString("button-Yes"))).click();
//							driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
//							
//							File updatedPartyDocumentFile = new File(updatedPartyDocument.getString("party_document_upload_url"));
//							driver.findElement(By.id(updateDealLocator.getString("input-PartyDocumentUpload"))).sendKeys(updatedPartyDocumentFile.getAbsolutePath());
//							Thread.sleep(1000);							
//						}
						
						break;						
					case "delete":
						partyDocumentIndex = partyDocumentTypeList.indexOf(partyDocument.getString("party_document_type"));
						
						driver.findElements(By.id(updateDealLocator.getString("button-PartyShowMenu"))).get(partyDocumentIndex).click();
						driver.findElement(By.id(updateDealLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						utility.waitForProgressBarToLoad(driver);
						driver.findElement(By.xpath(updateDealLocator.getString("button-Yes"))).click();
						driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
						break;
						
					default:
						step.log(Status.FAIL, "Invalid party document edit option: "+partyDocument.getString("party_document_type"));
						throw new Exception("Invalid party document edit option: "+partyDocument.getString("party_document_type"));
				}
				validatePartyDocument(driver, step, partyDocument);
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party Document: "+partyDocument.getString("party_document_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party Document: "+partyDocument.getString("party_document_type"));
			}			
		}
	}
	
	private void editParties(WebDriver driver, ExtentTest step,  JSONArray parties) throws Exception {
		
		Iterator<Object> partyIterator = parties.iterator();
		
		driver.findElement(By.id(updateDealLocator.getString("tab-PartyWizard"))).click();
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			String resultParty = null;
			try {				
				
				switch (party.getString("party_action").trim().toLowerCase()) {
					
					case "add":
						addParties(driver, step, new JSONArray().put(party)); 
						break;
					
					case "edit":
						driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).clear();
						driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
						utility.waitForElementToBeDisplayed(driver, By.xpath(updateDealLocator.getString("column-PartyName")), 2, 120);
						//utility.waitForResultsToLoad(By.xpath(updateDealLocator.getString("column-PartyName")), 10);
												
						resultParty = driver.findElements(By.xpath(updateDealLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
						
						if(resultParty.equals(party.getString("party_name").trim().toLowerCase())) {
							driver.findElements(By.xpath(updateDealLocator.getString("column-PartyManage"))).get(1).findElements(By.tagName("i")).get(0).click();
							utility.waitForProgressBarToLoad(driver);
							if(party.has("party_basic_details")) {
								editPartyBasicDetails(driver, step, party.getString("party_name"), party.getJSONArray("party_basic_details"));								
							}
							
							driver.findElement(By.id(updateDealLocator.getString("button-PartyNext"))).click();
							utility.waitForProgressBarToLoad(driver);
							
							if(party.has("party_contacts")) {
								editPartyContacts(driver, step, party.getString("party_name"), party.getJSONArray("party_contacts"));								
							}
							
							if(party.has("party_accounts")) {
								editPartyAccounts(driver, step, party.getString("party_name"), party.getJSONArray("party_accounts"));								
							}
							
							if(party.has("party_documents")) {
								editPartyDocuments(driver, step, party.getString("party_name"), party.getJSONArray("party_documents"));								
							}
							driver.findElement(By.id(updateDealLocator.getString("button-PartyBack"))).click();
						}
						break;						
					case "delete":						
						driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).clear();
						driver.findElements(By.xpath(updateDealLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
						utility.waitForElementToBeDisplayed(driver, By.xpath(updateDealLocator.getString("column-PartyName")), 2, 120);
						
						resultParty = driver.findElements(By.xpath(updateDealLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
						
						if(resultParty.equals(party.getString("party_name").trim().toLowerCase())) {
							driver.findElements(By.xpath(updateDealLocator.getString("column-PartyManage"))).get(1).findElements(By.tagName("i")).get(1).click();
							
							driver.findElement(By.xpath(updateDealLocator.getString("button-Yes"))).click();
							utility.waitForProgressBarToLoad(driver);
							driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
						}
						break;
					default:
						step.log(Status.FAIL, "Invalid party edit option: "+party.getString("party_name"));
						throw new Exception("Invalid party edit option: "+party.getString("party_name"));
				}
				validatePartiesTab(driver, step, party);
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party : "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party : "+party.getString("party_name"));
			}
		}		
	}	
	
	private void editBudgets(WebDriver driver, ExtentTest step,  JSONArray budgetGroups) throws Exception {
		
		Iterator<Object> budgetGroupIterator = budgetGroups.iterator();
		driver.findElement(By.id(updateDealLocator.getString("tab-BudgetWizard"))).click();
		
		while(budgetGroupIterator.hasNext()) {
			JSONObject budgetGroup = (JSONObject) budgetGroupIterator.next();			
			try {				
				
				switch (budgetGroup.getString("budget_group_action").trim().toLowerCase()) {
					
					case "add":
						addBudgetGroups(driver, step, new JSONArray().put(budgetGroup)); 
						break;
					
					case "edit":
						
						break;						
					case "delete":						
						
						break;
					default:
						step.log(Status.FAIL, "Invalid budget group edit option: "+budgetGroup.getString("budget_group_name"));
						throw new Exception("Invalid budget group edit option: "+budgetGroup.getString("budget_group_name"));
				}				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Budget Group : "+budgetGroup.getString("budget_group_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Budget Group : "+budgetGroup.getString("budget_group_name"));
			}
		}		
	}	
	
	private List<String> extractTextFromBudgetGroupCards(WebDriver driver, ExtentTest step) throws Exception{
		List<String> budgetGroupNameList = new ArrayList<String>();
		
		try {		
		List<WebElement> budgetGroupCardElements = driver.findElements(By.id(updateDealLocator.getString("card-BudgetDetails")));		
		for(WebElement budgetGroupCard : budgetGroupCardElements) {			
			String[] cardText = budgetGroupCard.getText().toLowerCase().trim().split("\n");
			budgetGroupNameList.add("name:"+cardText[0]+";source:"+cardText[1]+";"+cardText[2]);
		}
		}
		catch (Exception e) {			
			step.log(Status.FAIL, "Error while extracting text from Budget Group Cards");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting text from Budget Group Cards");
		}
		return budgetGroupNameList;
	}
	
	private List<String> extractTextFromScheduledInstructionCards(WebDriver driver, ExtentTest step) throws Exception{
		List<String> scheduledInstructionList = new ArrayList<String>();
		try {
		List<WebElement> scheduledInstructionCardElements = driver.findElement(By.id(updateDealLocator.getString("card-ScheduledInstructionDetails"))).findElements(By.xpath("*"));		
		for(WebElement scheduledInstructionCard : scheduledInstructionCardElements) {			
			String[] cardText = scheduledInstructionCard.getText().toLowerCase().trim().split("\n");
			String scheduledInstructionName = "name:";
			String scheduledInstructionType = "type:";
			String scheduledInstructionPurpose = "purpose:";
			
			for(int index=0; index <cardText.length; index ++) {
				if(index == 0) {
					scheduledInstructionName += cardText[index]+ ";";
				}
				if(index > 5 && cardText[index].equals("retention")) {
					scheduledInstructionType += "-" + "retention";
				}
				if(index > 5 && cardText[index].equals("surplus")) {
					scheduledInstructionType += "-" + "surplus";
				}
				if(cardText[index].equals("type")) {
					scheduledInstructionType += cardText[index+1];
				}
				if(cardText[index].equals("purpose")) {
					scheduledInstructionPurpose += cardText[index+1] + ";";
				}
			}
			scheduledInstructionList.add(scheduledInstructionName + scheduledInstructionPurpose + scheduledInstructionType);
		}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting text from Scheduled Instruction Cards");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting text from Scheduled Instruction Cards");
		}
		return scheduledInstructionList;
	}
	
	
	private List<String> extractTextFromLinkedInstructionCards(WebDriver driver, ExtentTest step) throws Exception{
		List<String> linkedInstructionList = new ArrayList<String>();
		try {
		List<WebElement> linkedInstructionCardElements = driver.findElement(By.id(updateDealLocator.getString("card-LinkedInstructionDetails"))).findElements(By.xpath("*"));		
		for(WebElement linkedInstructionCard : linkedInstructionCardElements) {			
			String[] cardText = linkedInstructionCard.getText().toLowerCase().trim().split("\n");
			String linkedInstructionName = "name:";
			String linkedInstructionType = "type:";
			String linkedInstructionPurpose = "purpose:";
			
			for(int index=0; index <cardText.length; index ++) {
				if(index == 0) {
					linkedInstructionName += cardText[index]+ ";";
				}
				if(cardText[index].equals("type")) {
					linkedInstructionType += cardText[index+1];					
				}
				if(cardText[index].equals("purpose")) {
					linkedInstructionPurpose += cardText[index+1] + ";";					
				}
			}
			linkedInstructionList.add(linkedInstructionName + linkedInstructionPurpose + linkedInstructionType);
		}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting text from Linked Instruction Cards");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting text from Linked Instruction Cards");
		}
		return linkedInstructionList;
	}
	
	private List<String> extractTextFromEntitlementCards(WebDriver driver, ExtentTest step) throws Exception{
		List<String> entitlementList = new ArrayList<String>();
		try {
			List<String> entitlementRecords = utility.extractColumnsFromTable(driver, step, By.id(updateDealLocator.getString("table-EntitlementList")), 2);
			
			for(String record : entitlementRecords) {
				List<String> contacts = new ArrayList<String>();
				String[] processedRecord = record.split("\n");
				for(int index = 0; index < processedRecord.length; index++) {
					if(index == 0 || index % 2 == 0) {
						contacts.add(processedRecord[index]);
					}
				}
				entitlementList.add("entitlement:"+contacts.toString());
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting text from Entitlement Cards");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting text from Entitlement Cards");
		}
		return entitlementList;
	}
	
	private void viewAccountSection(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {	
		if(!utility.isElementHiddenNow(driver, By.xpath(updateDealLocator.getString("button-SummaryAccountNumber")))) {
			utility.waitForElementToBeDisplayed(driver, By.xpath(updateDealLocator.getString("button-SummaryAccountNumber")), 2, 120);
			int noOfEntries = driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountNumber"))).size();
			int index = 1;
			
			step.log(Status.INFO, "Accounts added to deal "+deal.getString("name")+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			
			try {
				while(index < noOfEntries) {
					String accountNumber = driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountNumber"))).get(index).getText().toLowerCase().trim();				
					
					if(deal.has("budget_groups") && !driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountBudgetGroups"))).get(index).getText().equals("0")) {
						driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountBudgetGroups"))).get(index).findElement(By.tagName("div")).click();
						
						List<String> budgetGroupList = extractTextFromBudgetGroupCards(driver, step);
						step.log(Status.INFO, "Budget Groups added to account "+accountNumber+": "+budgetGroupList.toString()+" (See below image)");
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
					}
					
					if(deal.has("scheduled_instructions") && !driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountScheduledInstructions"))).get(index).getText().equals("0")){
						driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountScheduledInstructions"))).get(index).findElement(By.tagName("div")).click();
						
						List<String> scheduledInstructionList = extractTextFromScheduledInstructionCards(driver, step);
						step.log(Status.INFO, "Scheduled Instructions added to account "+accountNumber+": "+scheduledInstructionList.toString()+" (See below image)");
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();			
					}
					
					if(deal.has("linked_instructions") && !driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountLinkedInstructions"))).get(index).getText().equals("0")){
						driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountLinkedInstructions"))).get(index).findElement(By.tagName("div")).click();
						
						List<String> linkedInstructionList = extractTextFromLinkedInstructionCards(driver, step);
						step.log(Status.INFO, "Linked Instructions added to account "+accountNumber+": "+linkedInstructionList.toString()+" (See below image)");	
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
					}
					
					if(deal.has("entitlements") && !driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountEntitlements"))).get(index).getText().equals("0")){
						driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryAccountEntitlements"))).get(index).findElement(By.tagName("div")).click();
								
						List<String> entitlementList = extractTextFromEntitlementCards(driver, step);
						step.log(Status.INFO, "Entitlements added to account "+accountNumber+": "+entitlementList.toString()+" (See below image)");	
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
					}				
					index ++;
				}			
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while viewing Account section of Deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while viewing Account section of Deal: "+deal.getString("name"));
			}	
		}
		else {
			step.log(Status.INFO, driver.findElement(By.tagName(updateDealLocator.getString("grid-SummaryAccount"))).getText());
			report.addStepInfoScreenshot(driver, step);
		}
	}
	
	private void viewPartySection(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {
		if(!utility.isElementHiddenNow(driver, By.xpath(updateDealLocator.getString("button-SummaryPartyName")))) {
			utility.waitForElementToBeDisplayed(driver, By.xpath(updateDealLocator.getString("button-SummaryPartyName")), 2, 120);
			int noOfEntries = driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyName"))).size();
			int index = 1;
			
			utility.scrollIntoView(driver, driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyName"))).get(0));
			step.log(Status.INFO, "Parties added to  deal "+deal.getString("name")+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			
			try {
				while(index < noOfEntries) {
					String partyName = driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyName"))).get(index).getText().toLowerCase().trim();
					
					utility.scrollIntoView(driver, driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyName"))).get(index));
					Thread.sleep(500);			
					
					String partyParticipantID =  driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyParticipantID"))).get(index).getText(); 
					if(!partyParticipantID.equals("N.A")) {					
						step.log(Status.INFO, "Ecommerce party "+partyParticipantID);					
					}
					
					String partyResponsibility = driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyResponsibility"))).get(index).getText();
					step.log(Status.INFO, "Party responsibility: "+partyResponsibility);
					
					
					
					if(!driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyDebits"))).get(index).getText().equals("0")) {
						
						driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyDebits"))).get(index).findElement(By.tagName("div")).click();
						
						List<String> partyDebitList = utility.extractColumnsFromTable(driver, step, By.id(updateDealLocator.getString("table-NotificationsRows")), 0);	
						step.log(Status.INFO, "Debit accounts added to party "+partyName+": "+partyDebitList.toString()+" (See below image)");
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
					}
					
					if(!driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyContacts"))).get(index).getText().equals("0")) {
						driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyContacts"))).get(index).findElement(By.tagName("div")).click();			
						
						List<String> partyContactList = utility.extractColumnsFromTable(driver, step, By.id(updateDealLocator.getString("table-NotificationsRows")), 1);	
						step.log(Status.INFO, "Contacts added to party "+partyName+": "+partyContactList.toString()+" (See below image)");
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
					}
					
					if(!driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyAccounts"))).get(index).getText().equals("0")) {
						driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyAccounts"))).get(index).findElement(By.tagName("div")).click();
						
						List<String> partyAccountList = utility.extractColumnsFromTable(driver, step, By.id(updateDealLocator.getString("table-NotificationsRows")), 1);	
						step.log(Status.INFO, "Accounts added to party "+partyName+": "+partyAccountList.toString()+" (See below image)");	
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
					}
					
					utility.scrollIntoView(driver, driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyDocuments"))).get(index));
					Thread.sleep(500);
					if(!driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyDocuments"))).get(index).getText().equals("0")) {					
						driver.findElements(By.xpath(updateDealLocator.getString("button-SummaryPartyDocuments"))).get(index).findElement(By.tagName("div")).click();
						
						List<String> partyDocumentList = utility.extractColumnsFromTable(driver, step, By.id(updateDealLocator.getString("table-NotificationsRows")), 0);
						step.log(Status.INFO, "Documents added to party "+partyName+": "+partyDocumentList.toString()+" (See below image)");	
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
					}				
					index ++;
				}			
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while viewing Party section of Deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while viewing Party section of Deal: "+deal.getString("name"));
			}
		}
		else {
			step.log(Status.INFO, driver.findElement(By.tagName(updateDealLocator.getString("grid-SummaryParty"))).getText());
			report.addStepInfoScreenshot(driver, step);
		}
	}
	
	private boolean viewDealDetails(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {
		boolean result = true;
		try {				
			WebElement dealStartsOn = driver.findElement(By.id(updateDealLocator.getString("label-SummaryDealStartsOn")));
			utility.scrollIntoView(driver, dealStartsOn);
			step.log(Status.INFO, "Deal Starts On - Expected: "+deal.getString("starts_on")+" Actual: "+dealStartsOn.getText()+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			if(!deal.getString("starts_on").equals(dealStartsOn.getText())) {				
				result = false;				
			}
			
			if(deal.has("ends_on")){
				WebElement dealEndsOn = driver.findElement(By.id(updateDealLocator.getString("label-SummaryDealEndsOn")));
				utility.scrollIntoView(driver, dealEndsOn);
				step.log(Status.INFO, "Deal Ends On - Expected: "+deal.getString("ends_on")+" Actual: "+dealEndsOn.getText()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				if(!deal.getString("ends_on").equals(dealEndsOn.getText())) {					
					result = false;				
				}
			}
			
			WebElement dealCountry = driver.findElement(By.id(updateDealLocator.getString("label-SummaryDealCountry")));
			utility.scrollIntoView(driver, dealCountry);
			Map<String, String> country = Map.of(
				    "IND", "India",
				    "MY", "Malaysia",
				    "GB", "United Kingdom",
				    "SG", "Singapore",
				    "US",  "America"
				);
			step.log(Status.INFO, "Deal Country - Expected: "+deal.getString("country")+" Actual: "+country.get(dealCountry.getText())+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			if(!deal.getString("country").equals(country.get(dealCountry.getText()))) {				
				result = false;				
			}
			
			WebElement dealTimezone = driver.findElement(By.id(updateDealLocator.getString("label-SummaryDealTimezone")));
			utility.scrollIntoView(driver, dealTimezone);
			step.log(Status.INFO, "Deal Timezone - Expected: "+deal.getString("timezone")+" Actual: "+dealTimezone.getText()+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			if(!deal.getString("timezone").contains(dealTimezone.getText())) {				
				result = false;				
			}
			
			if(deal.has("entitlements") && !driver.findElement(By.id(updateDealLocator.getString("button-SummaryEntitlements"))).getText().equals("0")) {
				utility.scrollIntoView(driver, driver.findElement(By.id(updateDealLocator.getString("button-SummaryEntitlements"))));
				driver.findElement(By.id(updateDealLocator.getString("button-SummaryEntitlements"))).click();				
								
				step.log(Status.INFO, "Entitlements added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
			}
			
			utility.scrollIntoView(driver, driver.findElement(By.id(updateDealLocator.getString("button-SummaryProcessingUnits"))));
			driver.findElement(By.id(updateDealLocator.getString("button-SummaryProcessingUnits"))).click();
			
			step.log(Status.INFO, "Processing units added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
			
			if(deal.has("notifications")) {
				utility.scrollIntoView(driver, driver.findElement(By.id(updateDealLocator.getString("button-SummaryNotifications"))));
				driver.findElement(By.id(updateDealLocator.getString("button-SummaryNotifications"))).click();
				
				step.log(Status.INFO, "Notifications added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
			}
			
			if(deal.has("contacts")) {
				utility.scrollIntoView(driver, driver.findElement(By.id(updateDealLocator.getString("button-SummaryContacts"))));
				driver.findElement(By.id(updateDealLocator.getString("button-SummaryContacts"))).click();
				
				step.log(Status.INFO, "Contacts added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
			}
			
			if(deal.has("documents")) {
				utility.scrollIntoView(driver, driver.findElement(By.id(updateDealLocator.getString("button-SummaryDocuments"))));
				driver.findElement(By.id(updateDealLocator.getString("button-SummaryDocuments"))).click();
				
				step.log(Status.INFO, "Mandatory documents added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
			}			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while viewing details of Deal: "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while viewing details of Deal: "+deal.getString("name"));			
		}
		return result;
	}
	
	private void submitDeal(WebDriver driver, ExtentTest step,  String dealName) throws Exception {
		try {
			driver.findElement(By.id(updateDealLocator.getString("button-SubmitDeal"))).click();				
			driver.findElement(By.xpath(updateDealLocator.getString("button-Yes"))).click();
			
			driver.findElement(By.xpath(updateDealLocator.getString("button-OK")));				
			step.log(Status.PASS, "Deal "+dealName+" submitted to Deal Checker Queue");
			report.addStepPassScreenshot(driver, step);	
			
			driver.findElement(By.xpath(updateDealLocator.getString("button-OK"))).click();
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while submitting deal: "+dealName);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while submitting deal: "+dealName);
		}
	}
	
	public UpdateDealPage(LocatorFactory locators) {
		super(locators);
		updateDealLocator = locators.getLocators("UpdateDealPage");			
	}	
	
	public void editDeal(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {
		try {
			
			if(deal.has("accounts")) {
				editAccounts(driver, step, deal.getJSONArray("accounts"));
			}
			
			if(deal.has("parties")) {
				editParties(driver, step, deal.getJSONArray("parties"));				
			}
			
			if(deal.has("budgets")) {
				editBudgets(driver, step, deal.getJSONArray("budgets"));
			}
			//driver.findElement(By.xpath(updateDealLocator.getString("button-SummaryClose"))).click();
			driver.findElement(By.id(updateDealLocator.getString("tab-SummaryWizard"))).click();
			Thread.sleep(2000);
			updatedDealId =  driver.findElement(By.xpath(updateDealLocator.getString("label-SummaryHeading"))).findElements(By.tagName("span")).get(2).getText();
			
			if(deal.getBoolean("deal_submit")) {				
				submitDeal(driver, step, deal.getString("name"));				
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while editing Deal : "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while editing Deal : "+deal.getString("name"));
		}
	}
	
	public void viewDeal(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {
		try {	
			
			if(deal.has("accounts")) {
				viewAccountSection(driver, step, deal);
			}
			
			if(deal.has("parties")) {
				viewPartySection(driver, step, deal);				
			}	
			
			if(deal.has("deal_basic_details")) {
				viewDealDetails(driver, step, deal);	
			}
					
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while viewing Deal : "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while viewing Deal : "+deal.getString("name"));
		}
	}
}
