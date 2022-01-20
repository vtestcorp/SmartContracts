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

public class PartyMakerPage {
	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject partyMakerLocator = null;	
	public static ArrayList<String> actualRequiredMessages = new ArrayList<String>();
	public static ArrayList<String> actualResponseMessages = new ArrayList<String>();
	
	private void generatePartyBasicDetailRequiredMessages(WebDriver driver, ExtentTest step,  JSONObject party) throws Exception {
		try {
			if(party.getString("party_type").toLowerCase().equals("external")) {					
				driver.findElement(By.id(partyMakerLocator.getString("checkbox-PartyType"))).findElement(By.tagName("div")).click();
				
				if(party.has("party_customer_id")) {
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyCustomerID"))).sendKeys(party.getString("party_customer_id"));						
				}
				
				if(party.has("party_name")) {
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyName"))).sendKeys(party.getString("party_name"));						
				}					
				
				if(party.getBoolean("party_neutral")) {
					driver.findElement(By.id(partyMakerLocator.getString("checkbox-PartyNeutral"))).click();
				}
				
				if(party.has("party_processing_units")) {
					driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1), party.getJSONArray("party_processing_units"));
				}
				
				if(party.has("party_remarks")) {
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyRemarks"))).sendKeys(party.getString("party_remarks"));
				}				
				
			}
			else if (party.getString("party_type").toLowerCase().equals("internal")) {					
				
				if(party.has("party_customer_id")) {
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyCustomerID"))).sendKeys(party.getString("party_customer_id"));
				}
				
				if(party.has("party_name")) {
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyName"))).sendKeys(party.getString("party_name"));
				}
				
				if(party.has("party_processing_units")) {
					driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1), party.getJSONArray("party_processing_units"));
				}
				
				if(party.has("party_remarks")) {
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyRemarks"))).sendKeys(party.getString("party_remarks"));
				}
			}
			driver.findElement(By.id(partyMakerLocator.getString("button-PartyNext"))).click();			
			actualRequiredMessages.addAll(utility.extractRequiredMessage(driver, step));
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while generating party basic details required field messages");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());				
			throw new Exception("Error while generating party basic details required field messages");
		}
	}
	
	private void generatePartyContactRequiredMessages(WebDriver driver, ExtentTest step,  JSONArray partyContacts) throws Exception {
		Iterator<Object> partyContactIterator = partyContacts.iterator();
		
		while (partyContactIterator.hasNext()) {
			JSONObject partyContact = (JSONObject) partyContactIterator.next();	
			try {			
				driver.findElement(By.id(partyMakerLocator.getString("button-NewPartyContact"))).click();				
				
				if(!utility.isElementHiddenNow(driver, By.id(partyMakerLocator.getString("button-AddLinkContactNewContact")))) {
					driver.findElements(By.id(partyMakerLocator.getString("button-AddLinkContactNewContact"))).get(0).click();
				}
				
				if(partyContact.has("party_contact_name")) {
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactName"))).sendKeys(partyContact.getString("party_contact_name"));
				}
				
				if(partyContact.has("party_contact_email")) {
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactEmail"))).sendKeys(partyContact.getString("party_contact_email"));
				}
				
				driver.findElement(By.id(partyMakerLocator.getString("button-AddPartyContact"))).click();			
				actualRequiredMessages.addAll(utility.extractRequiredMessage(driver, step));
				driver.findElement(By.id(partyMakerLocator.getString("button-ClosePartyContact"))).click();
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while generating party contact required field messages");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while generating party contact required field messages");
			}
		}
	}
	
	private void generatePartyAccountRequiredMessages(WebDriver driver, ExtentTest step,  JSONArray partyAccounts) throws Exception {
		Iterator<Object> partyAccountIterator = partyAccounts.iterator();
		driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
		
		while (partyAccountIterator.hasNext()) {
			JSONObject partyAccount = (JSONObject) partyAccountIterator.next();
			try {				
				driver.findElement(By.id(partyMakerLocator.getString("button-NewPartyAccount"))).click();				
							
				if(!utility.isElementHiddenNow(driver, By.id(partyMakerLocator.getString("button-AddLinkAccountNewAccount")))) {
					List<WebElement> newAccount = driver.findElements(By.id(partyMakerLocator.getString("button-AddLinkAccountNewAccount")));
					newAccount.get(0).click();
				}				
				
				Thread.sleep(1000);				
				if(partyAccount.has("party_account_payment_system")){
					driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("ul")), partyAccount.getString("party_account_payment_system"));
				}
				
				driver.findElement(By.id(partyMakerLocator.getString("button-AddPartyAccount"))).click();
				actualRequiredMessages.addAll(utility.extractRequiredMessage(driver, step));
				driver.findElement(By.id(partyMakerLocator.getString("button-ClosePartyAccount"))).click();	
				
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while generating party account required field messages");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while generating party account required field messages");
			}			
		}
	}
	
	private void generatePartyDocumentRequiredMessages(WebDriver driver, ExtentTest step,  JSONArray partyDocuments) throws Exception {
		Iterator<Object> partyDocumentIterator = partyDocuments.iterator();		
		driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
		
		while (partyDocumentIterator.hasNext()) {
			JSONObject partyDocument = (JSONObject) partyDocumentIterator.next();
			try {				
				driver.findElement(By.id(partyMakerLocator.getString("button-NewPartyDocument"))).click();
				
				
				if(!utility.isElementHiddenNow(driver, By.id(partyMakerLocator.getString("button-AddLinkDocumentNewDocument")))) {
					List<WebElement> newDocument = driver.findElements(By.id(partyMakerLocator.getString("button-AddLinkDocumentNewDocument")));
					newDocument.get(0).click();
				}

				Thread.sleep(500);
				if(partyDocument.has("party_document_type")) {
					driver.findElement(By.id(partyMakerLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("ul")), partyDocument.getString("party_document_type"));
				}
				
				if(partyDocument.has("party_document_nature")) {
					Select partyDocumentNature = new Select(driver.findElement(By.xpath(partyMakerLocator.getString("select-PartyDocumentNature"))));
					partyDocumentNature.selectByVisibleText(partyDocument.getString("party_document_nature"));					
				}				
				
				driver.findElement(By.id(partyMakerLocator.getString("button-AddPartyDocument"))).click();	
				actualRequiredMessages.addAll(utility.extractRequiredMessage(driver, step));
				driver.findElement(By.id(partyMakerLocator.getString("button-ClosePartyDocument"))).click();
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while generating party document required messages");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while generating party document required messages");
			}
			
		}
	}
	
	

	private void addPartyContacts(WebDriver driver, ExtentTest step,  JSONArray partyContacts) throws Exception {
		Thread.sleep(2000);
		Iterator<Object> partyContactIterator = partyContacts.iterator();
		
		while (partyContactIterator.hasNext()) {
			JSONObject partyContact = (JSONObject) partyContactIterator.next();	
			try {				
				
				driver.findElement(By.id(partyMakerLocator.getString("button-NewPartyContact"))).click();				
				
				if(!utility.isElementHiddenNow(driver, By.id(partyMakerLocator.getString("button-AddLinkContactNewContact")))) {
					driver.findElements(By.id(partyMakerLocator.getString("button-AddLinkContactNewContact"))).get(0).click();
				}
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactName"))).sendKeys(partyContact.getString("party_contact_name"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactEmail"))).sendKeys(partyContact.getString("party_contact_email"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactDesignation"))).sendKeys(partyContact.getString("party_contact_designation"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactWorkPhone"))).sendKeys(partyContact.getString("party_contact_work_phone"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactMobilePhone"))).sendKeys(partyContact.getString("party_contact_mobile_phone"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactStreet"))).sendKeys(partyContact.getString("party_contact_street"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactTown"))).sendKeys(partyContact.getString("party_contact_town"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactPin"))).sendKeys(partyContact.getString("party_contact_pin"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactState"))).sendKeys(partyContact.getString("party_contact_state"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactCountry"))).sendKeys(partyContact.getString("party_contact_country"));
				
				driver.findElement(By.id(partyMakerLocator.getString("button-AddPartyContact"))).click();			
				
				step.log(Status.INFO, "Party contact: "+partyContact.getString("party_contact_name")+" added. (See image below)");
				report.addStepInfoScreenshot(driver, step);
				
				driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();
				
				
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Party Contact: "+partyContact.getString("party_contact_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Party Contact: "+partyContact.getString("party_contact_name"));
			}
			
		}
	}
	
	private void addPartyAccounts(WebDriver driver, ExtentTest step,  JSONArray partyAccounts) throws Exception {
		Thread.sleep(2000);
		Iterator<Object> partyAccountIterator = partyAccounts.iterator();
		driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
		
		while (partyAccountIterator.hasNext()) {
			JSONObject partyAccount = (JSONObject) partyAccountIterator.next();
			try {				
				driver.findElement(By.id(partyMakerLocator.getString("button-NewPartyAccount"))).click();
				
							
				if(!utility.isElementHiddenNow(driver, By.id(partyMakerLocator.getString("button-AddLinkAccountNewAccount")))) {
					List<WebElement> newAccount = driver.findElements(By.id(partyMakerLocator.getString("button-AddLinkAccountNewAccount")));
					newAccount.get(0).click();
				}
				
				
				Thread.sleep(1000);
				driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("ul")), partyAccount.getString("party_account_payment_system"));
				
				//driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryBIC"))).sendKeys(partyAccount.getString("party_account_beneficiary_BIC"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryName"))).sendKeys(partyAccount.getString("party_account_beneficiary_name"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryAddress"))).sendKeys(partyAccount.getString("party_account_beneficiary_address"));
								
				//Select partyAccountIBAN = new Select(driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountIBAN"))));
				//partyAccountIBAN.selectByVisibleText(partyAccount.getString("party_account_IBAN"));
				
				Select partyAccountBeneficiaryCountrySelect = new Select(driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountBeneficiaryCountry"))));
				partyAccountBeneficiaryCountrySelect.selectByVisibleText(partyAccount.getString("party_account_beneficiary_country"));
				
				//driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryCurrency"))).sendKeys(partyAccount.getString("party_account_beneficiary_currency"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountDestinationAccount"))).sendKeys(partyAccount.getString("party_account_beneficiary_account"));
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountDescription"))).sendKeys(partyAccount.getString("party_account_description"));
				
				driver.findElement(By.id(partyMakerLocator.getString("button-AddPartyAccount"))).click();
				
				step.log(Status.INFO, "Party account: "+partyAccount.getString("party_account_beneficiary_account")+" added. (See image below)");
				report.addStepInfoScreenshot(driver, step);
				
				driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();			
				
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
			}
			
		}
	}

	
	private void addPartyDocuments(WebDriver driver, ExtentTest step,  JSONArray partyDocuments) throws Exception {
		Thread.sleep(2000);
		Iterator<Object> partyDocumentIterator = partyDocuments.iterator();
		
		driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
		
		while (partyDocumentIterator.hasNext()) {
			JSONObject partyDocument = (JSONObject) partyDocumentIterator.next();
			try {				
				driver.findElement(By.id(partyMakerLocator.getString("button-NewPartyDocument"))).click();
				
				
				if(!utility.isElementHiddenNow(driver, By.id(partyMakerLocator.getString("button-AddLinkDocumentNewDocument")))) {
					List<WebElement> newDocument = driver.findElements(By.id(partyMakerLocator.getString("button-AddLinkDocumentNewDocument")));
					newDocument.get(0).click();
				}

				Thread.sleep(500);
				driver.findElement(By.id(partyMakerLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("ul")), partyDocument.getString("party_document_type"));
				
				Select partyDocumentNature = new Select(driver.findElement(By.xpath(partyMakerLocator.getString("select-PartyDocumentNature"))));
				partyDocumentNature.selectByVisibleText(partyDocument.getString("party_document_nature"));
				
				List<WebElement> partyDocumentDatePicker = driver.findElements(By.id(partyMakerLocator.getString("input-PartyDocumentDatePicker")));
				if(!utility.isElementHiddenNow(driver, By.id(partyMakerLocator.getString("input-PartyDocumentDatePicker")))){
					partyDocumentDatePicker.get(0).sendKeys(partyDocument.getString("party_document_fromdate"));
					
					if(partyDocument.has("party_document_tilldate")) {
						partyDocumentDatePicker.get(1).sendKeys(partyDocument.getString("party_document_tilldate"));
					}
				}
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyDocumentDescription"))).sendKeys(partyDocument.getString("party_document_description"));
				
				driver.findElement(By.id(partyMakerLocator.getString("button-AddPartyDocument"))).click();	
				
				step.log(Status.INFO, "Party document: "+partyDocument.getString("party_document_type")+" added. (See image below)");
				report.addStepInfoScreenshot(driver, step);
				
				driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();	
				
				File partyDocumentFile = new File(partyDocument.getString("party_document_upload_url"));
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyDocumentUpload"))).sendKeys(partyDocumentFile.getAbsolutePath());
				Thread.sleep(2000);	
				
				
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Party Document: "+partyDocument.getString("party_document_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Party Document: "+partyDocument.getString("party_document_type"));
			}
			
		}
	}	
	
	private void editPartyBasicDetails(WebDriver driver, ExtentTest step,  JSONArray basicDetails) throws Exception {
		
		JSONObject basicDetail = basicDetails.getJSONObject(0);
		try {
			
			if(basicDetail.has("party_customer_id")) {
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyCustomerID"))).clear();
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyCustomerID"))).sendKeys(basicDetail.getString("party_customer_id"));
			}
			
			if(basicDetail.has("party_name")) {
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyName"))).clear();
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyName"))).sendKeys(basicDetail.getString("party_name"));
			}
			
			if(basicDetail.has("party_processing_units")) {
				System.out.println("Here");
				Thread.sleep(1000);
				driver.findElement(By.xpath(partyMakerLocator.getString("select-partyPUnit"))).click();
				driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1).findElements(By.tagName("span")).get(1).click();
				driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1).findElements(By.tagName("span")).get(1).click();
				driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1).findElements(By.tagName("span")).get(1).click();
				//driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1).findElements(By.tagName("span")).get(1).click();
				//utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyChildProcessingUnit"))).findElement(By.tagName("div")), basicDetail.getJSONArray("party_processing_units"));				
			}
			
			if(basicDetail.has("party_remarks")) {
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyRemarks"))).clear();
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyRemarks"))).sendKeys(basicDetail.getString("party_remarks"));
			}			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while editing Party Basic Details");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while editing Party Basic Details");
		}
		
	}
	
	private void editPartyContacts(WebDriver driver, ExtentTest step,  JSONArray partyContacts) throws Exception {
		Thread.sleep(2000);
		Iterator<Object> partyContactIterator = partyContacts.iterator();		
		
		while(partyContactIterator.hasNext()) {
			JSONObject partyContact = (JSONObject) partyContactIterator.next();	
			try {							
				List<String> partyContactNameList = utility.extractColumnsFromTable(driver, step, By.id(partyMakerLocator.getString("table-PartyTable")), 1);
				int partyContactIndex;
				
				switch (partyContact.getString("party_contact_action").trim().toLowerCase()) {
					case "add":
						addPartyContacts(driver, step, new JSONArray().put(partyContact));
						break;	
						
					case "edit":
						partyContactIndex = partyContactNameList.indexOf(partyContact.getString("party_contact_name"));
						
						driver.findElements(By.id(partyMakerLocator.getString("button-PartyShowMenu"))).get(partyContactIndex).click();
						driver.findElement(By.id(partyMakerLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(0).click();
						
						JSONObject updatedPartyContact = partyContact.getJSONArray("party_updated_contact").getJSONObject(0);
						if(updatedPartyContact.has("party_contact_name")) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactName"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactName"))).sendKeys(updatedPartyContact.getString("party_contact_name"));
						}
						
						if(updatedPartyContact.has("party_contact_email")) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactEmail"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactEmail"))).sendKeys(updatedPartyContact.getString("party_contact_email"));							
						}
						
						if(updatedPartyContact.has("party_contact_designation") && driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactDesignation"))).isEnabled()) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactDesignation"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactDesignation"))).sendKeys(updatedPartyContact.getString("party_contact_designation"));							
						}
						
						if(updatedPartyContact.has("party_contact_work_phone") && driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactWorkPhone"))).isEnabled()) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactWorkPhone"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactWorkPhone"))).sendKeys(updatedPartyContact.getString("party_contact_work_phone"));							
						}
						
						if(updatedPartyContact.has("party_contact_mobile_phone") && driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactMobilePhone"))).isEnabled()) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactMobilePhone"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactMobilePhone"))).sendKeys(updatedPartyContact.getString("party_contact_mobile_phone"));							
						}
						
						if(updatedPartyContact.has("party_contact_street") && driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactStreet"))).isEnabled()) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactStreet"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactStreet"))).sendKeys(updatedPartyContact.getString("party_contact_street"));							
						}
						
						if(updatedPartyContact.has("party_contact_town") && driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactTown"))).isEnabled()) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactTown"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactTown"))).sendKeys(updatedPartyContact.getString("party_contact_town"));							
						}
						
						if(updatedPartyContact.has("party_contact_pin") && driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactPin"))).isEnabled()) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactPin"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactPin"))).sendKeys(updatedPartyContact.getString("party_contact_pin"));							
						}
						
						if(updatedPartyContact.has("party_contact_state") && driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactState"))).isEnabled()) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactState"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactState"))).sendKeys(updatedPartyContact.getString("party_contact_state"));							
						}
						
						if(updatedPartyContact.has("party_contact_country") && driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactCountry"))).isEnabled()) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactCountry"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyContactCountry"))).sendKeys(updatedPartyContact.getString("party_contact_country"));							
						}
						
						driver.findElement(By.id(partyMakerLocator.getString("button-AddPartyContact"))).click();
						
						step.log(Status.INFO, "Party contact: "+partyContact.getString("party_contact_name")+" updated. (See image below)");
						report.addStepInfoScreenshot(driver, step);
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();						
						break;
						
					case "delete":
						partyContactIndex = partyContactNameList.indexOf(partyContact.getString("party_contact_name"));
						
						driver.findElements(By.id(partyMakerLocator.getString("button-PartyShowMenu"))).get(partyContactIndex).click();
						driver.findElement(By.id(partyMakerLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-Yes"))).click();
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK")));
						String response = utility.extractTitleMessage(driver, step, By.id(partyMakerLocator.getString("label-Title")));	
						
						if(response.toLowerCase().contains("failed")) {
							step.log(Status.INFO, "Party contact: "+partyContact.getString("party_contact_name")+" deletion failed. (See image below)");
							report.addStepInfoScreenshot(driver, step);	
							actualResponseMessages.add(response);		
						}
						else {
							step.log(Status.INFO, "Party contact: "+partyContact.getString("party_contact_name")+" deleted. (See image below)");
							report.addStepInfoScreenshot(driver, step);
						}	
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();						
						break;
						
					default:
						step.log(Status.FAIL, "Invalid party contact edit option: "+partyContact.getString("party_contact_action"));
						throw new Exception("Invalid party contact edit option: "+partyContact.getString("party_contact_action"));
				}
				
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party Contact: "+partyContact.getString("party_contact_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party Contact: "+partyContact.getString("party_contact_name"));
			}
			
		}
	}
	
	private void editPartyAccounts(WebDriver driver, ExtentTest step,  JSONArray partyAccounts) throws Exception {
		Thread.sleep(2000);
		Iterator<Object> partyAccountIterator = partyAccounts.iterator();
		driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
		
		while (partyAccountIterator.hasNext()) {
			JSONObject partyAccount = (JSONObject) partyAccountIterator.next();
			try {
				List<String> partyDestinationAccountList = utility.extractColumnsFromTable(driver, step, By.id(partyMakerLocator.getString("table-PartyTable")), 1);
				int partyAccountIndex;
				
				switch (partyAccount.getString("party_account_action").trim().toLowerCase()) {
					case "add":
						addPartyAccounts(driver, step, new JSONArray().put(partyAccount));
						break;	
						
					case "edit":
						partyAccountIndex = partyDestinationAccountList.indexOf(partyAccount.getString("party_account_beneficiary_account"));
						
						driver.findElements(By.id(partyMakerLocator.getString("button-PartyShowMenu"))).get(partyAccountIndex).click();
						driver.findElement(By.id(partyMakerLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						
						JSONObject updatedPartyAccount = partyAccount.getJSONArray("party_updated_account").getJSONObject(0);
						if(updatedPartyAccount.has("party_account_payment_system")) {
							driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("input")).click();
							utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("ul")), updatedPartyAccount.getString("party_account_payment_system"));
						}
						
						if(updatedPartyAccount.has("party_account_beneficiary_name")) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryName"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryName"))).sendKeys(updatedPartyAccount.getString("party_account_beneficiary_name"));						
						}
						
						if(updatedPartyAccount.has("party_account_beneficiary_address")) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryAddress"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryAddress"))).sendKeys(updatedPartyAccount.getString("party_account_beneficiary_address"));
							
						}
						
						if(updatedPartyAccount.has("party_account_beneficiary_country")){
							Select partyAccountBeneficiaryCountrySelect = new Select(driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountBeneficiaryCountry"))));
							partyAccountBeneficiaryCountrySelect.selectByVisibleText(updatedPartyAccount.getString("party_account_beneficiary_country"));
						}
						
						if(updatedPartyAccount.has("party_account_beneficiary_account")) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountDestinationAccount"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountDestinationAccount"))).sendKeys(updatedPartyAccount.getString("party_account_beneficiary_account"));							
						}
						
						if(updatedPartyAccount.has("party_account_description")) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountDescription"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountDescription"))).sendKeys(updatedPartyAccount.getString("party_account_description"));
						}
						//driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryBIC"))).sendKeys(partyAccount.getString("party_account_beneficiary_BIC"));
						//Select partyAccountIBAN = new Select(driver.findElement(By.id(partyMakerLocator.getString("select-PartyAccountIBAN"))));
						//partyAccountIBAN.selectByVisibleText(partyAccount.getString("party_account_IBAN"));						
						//driver.findElement(By.id(partyMakerLocator.getString("input-PartyAccountBeneficiaryCurrency"))).sendKeys(partyAccount.getString("party_account_beneficiary_currency"));
						
						
						
						driver.findElement(By.id(partyMakerLocator.getString("button-AddPartyAccount"))).click();	
						
						step.log(Status.INFO, "Party account: "+partyAccount.getString("party_account_beneficiary_account")+" updated. (See image below)");
						report.addStepInfoScreenshot(driver, step);
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();						
						break;						
					case "delete":
						partyAccountIndex = partyDestinationAccountList.indexOf(partyAccount.getString("party_account_beneficiary_account"));
						
						driver.findElements(By.id(partyMakerLocator.getString("button-PartyShowMenu"))).get(partyAccountIndex).click();
						driver.findElement(By.id(partyMakerLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(2).click();
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-Yes"))).click();
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK")));
						String response = utility.extractTitleMessage(driver, step, By.id(partyMakerLocator.getString("label-Title")));	
						
						if(response.toLowerCase().contains("failed")) {
							step.log(Status.INFO, "Party account: "+partyAccount.getString("party_account_beneficiary_account")+" deletion failed. (See image below)");
							report.addStepInfoScreenshot(driver, step);	
							actualResponseMessages.add(response);		
						}
						else {
							step.log(Status.INFO, "Party account: "+partyAccount.getString("party_account_beneficiary_account")+" deleted. (See image below)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();						
						break;
						
					default:
						step.log(Status.FAIL, "Invalid party account edit option: "+partyAccount.getString("party_account_action"));
						throw new Exception("Invalid party account edit option: "+partyAccount.getString("party_account_action"));
				}		
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
			}
		}
	}
	
	private void editPartyDocuments(WebDriver driver, ExtentTest step,  JSONArray partyDocuments) throws Exception {
		Thread.sleep(2000);
		Iterator<Object> partyDocumentIterator = partyDocuments.iterator();
		
		driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
		
		while (partyDocumentIterator.hasNext()) {
			JSONObject partyDocument = (JSONObject) partyDocumentIterator.next();
			try {
				List<String> partyDocumentTypeList = utility.extractColumnsFromTable(driver, step, By.id(partyMakerLocator.getString("table-PartyTable")), 0);
				int partyDocumentIndex;
				
				switch (partyDocument.getString("party_document_action").trim().toLowerCase()) {
					case "add":
						addPartyDocuments(driver, step, new JSONArray().put(partyDocument));
						break;	
						
					case "edit":
						partyDocumentIndex = partyDocumentTypeList.indexOf(partyDocument.getString("party_document_type"));
						
						driver.findElements(By.id(partyMakerLocator.getString("button-PartyShowMenu"))).get(partyDocumentIndex).click();
						driver.findElement(By.id(partyMakerLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(0).click();
						
						JSONObject updatedPartyDocument = partyDocument.getJSONArray("party_updated_document").getJSONObject(0);
						if(updatedPartyDocument.has("party_document_type")) {
							driver.findElement(By.id(partyMakerLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("input")).click();
							utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("ul")), updatedPartyDocument.getString("party_document_type"));
						}
						
						if(updatedPartyDocument.has("party_document_nature")) {
							Select partyDocumentNature = new Select(driver.findElement(By.xpath(partyMakerLocator.getString("select-PartyDocumentNature"))));
							partyDocumentNature.selectByVisibleText(updatedPartyDocument.getString("party_document_nature"));
						}
						
						if(updatedPartyDocument.has("party_document_fromdate")) {
							List<WebElement> partyDocumentDatePicker = driver.findElements(By.id(partyMakerLocator.getString("input-PartyDocumentDatePicker")));
							if(!utility.isElementHiddenNow(driver, By.id(partyMakerLocator.getString("input-PartyDocumentDatePicker")))){
								partyDocumentDatePicker.get(0).sendKeys(updatedPartyDocument.getString("party_document_fromdate"));
								
								if(partyDocument.has("party_document_tilldate")) {
									partyDocumentDatePicker.get(1).sendKeys(updatedPartyDocument.getString("party_document_tilldate"));
								}
							}
						}
						
						if(updatedPartyDocument.has("party_document_description")) {
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyDocumentDescription"))).clear();
							driver.findElement(By.id(partyMakerLocator.getString("input-PartyDocumentDescription"))).sendKeys(updatedPartyDocument.getString("party_document_description"));
						}
						
						driver.findElement(By.id(partyMakerLocator.getString("button-AddPartyDocument"))).click();
						
						step.log(Status.INFO, "Party document: "+partyDocument.getString("party_document_type")+" updated. (See image below)");
						report.addStepInfoScreenshot(driver, step);
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();
						
//						if(updatedPartyDocument.has("party_document_upload_url")) {
//							driver.findElements(By.id(partyMakerLocator.getString("button-PartyDocumentUploadClose"))).get(partyDocumentIndex).click();
//							driver.findElement(By.xpath(partyMakerLocator.getString("button-Yes"))).click();
//							driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();
//							
//							File updatedPartyDocumentFile = new File(updatedPartyDocument.getString("party_document_upload_url"));
//							driver.findElement(By.id(partyMakerLocator.getString("input-PartyDocumentUpload"))).sendKeys(updatedPartyDocumentFile.getAbsolutePath());
//							Thread.sleep(2000);							
//						}
						
						
						break;						
					case "delete":
						partyDocumentIndex = partyDocumentTypeList.indexOf(partyDocument.getString("party_document_type"));
						
						driver.findElements(By.id(partyMakerLocator.getString("button-PartyShowMenu"))).get(partyDocumentIndex).click();
						driver.findElement(By.id(partyMakerLocator.getString("button-PartyPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-Yes"))).click();
						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK")));
						String response = utility.extractTitleMessage(driver, step, By.id(partyMakerLocator.getString("label-Title")));	
						
						if(response.toLowerCase().contains("failed")) {
							step.log(Status.INFO, "Party document: "+partyDocument.getString("party_document_type")+" deletion failed. (See image below)");
							report.addStepInfoScreenshot(driver, step);	
							actualResponseMessages.add(response);		
						}
						else {
							step.log(Status.INFO, "Party document: "+partyDocument.getString("party_document_type")+" deleted. (See image below)");
							report.addStepInfoScreenshot(driver, step);
						}
												
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();
						break;
						
					default:
						step.log(Status.FAIL, "Invalid party document edit option: "+partyDocument.getString("party_document_type"));
						throw new Exception("Invalid party document edit option: "+partyDocument.getString("party_document_type"));						
				}				
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party Document: "+partyDocument.getString("party_document_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party Document: "+partyDocument.getString("party_document_type"));
			}			
		}
	}
	
	private void submitParty(WebDriver driver, ExtentTest step,  String partyName) throws Exception {
		try {
		utility.focusElement(driver, driver.findElement(By.xpath(partyMakerLocator.getString("button-Submit"))));
		driver.findElement(By.xpath(partyMakerLocator.getString("button-Submit"))).click();				
		step.log(Status.INFO, "Party: "+partyName+" submitted for approval");
		report.addStepInfoScreenshot(driver, step);
		driver.findElement(By.xpath(partyMakerLocator.getString("button-Yes"))).click();
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while submitting party: "+partyName);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while submitting party: "+partyName);
		}
	}
	
	public PartyMakerPage(LocatorFactory locators) {	
		partyMakerLocator = locators.getLocators("PartyMakerPage");			
	}
	
	public void validateSummary(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {
				
				List<WebElement> partyBasicDetailsLabels = driver.findElements(By.xpath(partyMakerLocator.getString("label-PartyBasicDetails")));
				
				if(partyBasicDetailsLabels.get(0).getText().equals(party.getString("party_customer_id"))) {
					utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(0));
					step.log(Status.PASS, "Party customer ID: "+party.getString("party_customer_id")+" match. (See below image)");
					report.addStepPassScreenshot(driver, step);
					utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(0));
				}
				else {
					utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(0));
					step.log(Status.FAIL, "Party customer ID: "+party.getString("party_customer_id")+" does not match. (See below image)");
					report.addStepFailScreenshot(driver, step);
					utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(0));
					throw new Exception("Party customer ID: "+party.getString("party_customer_id")+" does not match");
				}
				
				if(partyBasicDetailsLabels.get(1).getText().equals(party.getString("party_name"))) {
					utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(1));
					step.log(Status.PASS, "Party name: "+party.getString("party_name")+" match. (See below image)");
					report.addStepPassScreenshot(driver, step);
					utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(1));
				}
				else {
					utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(1));
					step.log(Status.FAIL, "Party name: "+party.getString("party_name")+" does not match. (See below image)");
					report.addStepFailScreenshot(driver, step);
					utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(1));
					throw new Exception("Party name: "+party.getString("party_name")+" does not match");
				}
				
				if(party.getString("party_type").equals("External")) {
					if(party.getBoolean("party_neutral")  ) {
						if(partyBasicDetailsLabels.get(2).getText().equals("Neutral")) {
						utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(2));
						step.log(Status.PASS, "Party type: Neutral match. (See below image)");
						report.addStepPassScreenshot(driver, step);
						utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(2));
						}
						else {
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(2));
							step.log(Status.FAIL, "Party type: Neutral does not match. (See below image)");
							report.addStepFailScreenshot(driver, step);
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(2));
							throw new Exception("Party type: Neutral does not match");
						}
					}
					else {
						if(partyBasicDetailsLabels.get(2).getText().equals("External")) {
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(2));
							step.log(Status.PASS, "Party type: External match. (See below image)");
							report.addStepPassScreenshot(driver, step);
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(2));
						}
						else {
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(2));
							step.log(Status.FAIL, "Party type: External does not match. (See below image)");
							report.addStepFailScreenshot(driver, step);
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(2));
							throw new Exception("Party type: External does not match");
						}
					}
				}
				else {
					if(partyBasicDetailsLabels.get(2).getText().equals("Internal")) {
						utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(2));
						step.log(Status.PASS, "Party type: Internal match. (See below image)");
						report.addStepPassScreenshot(driver, step);
						utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(2));
						}
						else {
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(2));
							step.log(Status.FAIL, "Party type: Internal does not match. (See below image)");
							report.addStepFailScreenshot(driver, step);
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(2));
							throw new Exception("Party type: Internal does not match");
						}
				}
				
				if(partyBasicDetailsLabels.get(3).getText().equals(party.getString("party_remarks"))) {
					utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(3));
					step.log(Status.PASS, "Party remarks: "+party.getString("party_remarks")+" match. (See below image)");
					report.addStepPassScreenshot(driver, step);
					utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(3));
				}
				else {
					utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(3));
					step.log(Status.FAIL, "Party remarks: "+party.getString("party_remarks")+" does not match. (See below image)");
					report.addStepFailScreenshot(driver, step);
					utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(3));
					throw new Exception("Party remarks: "+party.getString("party_remarks")+" does not match");
				}
				
				driver.findElements(By.xpath(partyMakerLocator.getString("icon-PartyBasicDetails"))).get(5).click();				
				List<WebElement> processingUnitElements = driver.findElement(By.id(partyMakerLocator.getString("card-PartyProcessingUnits"))).findElements(By.tagName("div"));
				JSONArray actualProcessingUnits = new JSONArray();
				for(WebElement element: processingUnitElements) {
					String processingUnitText = element.getText();
					if(!actualProcessingUnits.toString().contains(processingUnitText)) {
						actualProcessingUnits.put(processingUnitText);
					}					
				}
				
				if(party.getJSONArray("party_processing_units").toString().equals(actualProcessingUnits.toString())) {
					step.log(Status.PASS, "Party processing units match. (See below image)");
					report.addStepPassScreenshot(driver, step);
				}
				else {
					step.log(Status.FAIL, "Party processing units do not match. (See below image)");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Party processing units do not match");
				}
				driver.findElement(By.xpath(partyMakerLocator.getString("button-Close"))).click();
				
				if(party.has("party_contacts")) {
					utility.focusElement(driver, driver.findElements(By.tagName("tbody")).get(0));
					utility.turnOnElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(0));					
					List<String> contactNameList = utility.extractColumnsFromTable(driver, step, driver.findElements(By.tagName("tbody")).get(0), 1);
					
					Iterator<Object> partyContactIterator = party.getJSONArray("party_contacts").iterator();
					while(partyContactIterator.hasNext()) {
						JSONObject partyContact = (JSONObject) partyContactIterator.next();
						
						if(contactNameList.contains(partyContact.getString("party_contact_name"))) {
							step.log(Status.PASS, "Party contact: "+partyContact.getString("party_contact_name")+" added. (See below image)");
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Party contact: "+partyContact.getString("party_contact_name")+" not added. (See below image)");
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Party contact: "+partyContact.getString("party_contact_name")+" not added");
						}
					}
					utility.turnOffElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(0));
				}
				
				if(party.has("party_accounts")) {
					utility.focusElement(driver, driver.findElements(By.tagName("tbody")).get(1));
					utility.turnOnElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(1));
					List<String> accountNumberList = utility.extractColumnsFromTable(driver, step, driver.findElements(By.tagName("tbody")).get(1), 1);
					
					Iterator<Object> partyAccountIterator = party.getJSONArray("party_accounts").iterator();
					while(partyAccountIterator.hasNext()) {
						JSONObject partyAccount = (JSONObject) partyAccountIterator.next();
						
						if(accountNumberList.contains(partyAccount.getString("party_account_beneficiary_account"))) {
							step.log(Status.PASS, "Party account: "+partyAccount.getString("party_account_beneficiary_account")+" added. (See below image)");
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Party account: "+partyAccount.getString("party_account_beneficiary_account")+" not added. (See below image)");
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Party account: "+partyAccount.getString("party_account_beneficiary_account")+" not added");
						}
						
					}
					utility.turnOffElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(1));
				}
				
				if(party.has("party_documents")) {
					utility.focusElement(driver, driver.findElements(By.tagName("tbody")).get(2));
					utility.turnOnElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(2));
					List<String> documentNameList = utility.extractColumnsFromTable(driver, step, driver.findElements(By.tagName("tbody")).get(2), 1);
					
					Iterator<Object> partyDocumentIterator = party.getJSONArray("party_documents").iterator();
					while(partyDocumentIterator.hasNext()) {
						JSONObject partyDocument = (JSONObject) partyDocumentIterator.next();
						
						if(documentNameList.contains(partyDocument.getString("party_document_type"))) {
							step.log(Status.PASS, "Party document: "+partyDocument.getString("party_document_type")+" added. (See below image)");
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Party document: "+partyDocument.getString("party_document_type")+" not added. (See below image)");
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Party document: "+partyDocument.getString("party_document_type")+" not added");
						}
					}
					utility.turnOffElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(2));
				}				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating Party: "+party.getString("party_name")+" summary screen.");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while validating Party: "+party.getString("party_name")+" summary screen.");
			}			
		}
	}
	
	public void validateUpdatedSummary(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("edit_parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {		
				
				if(party.has("party_basic_details")) {
					List<WebElement> partyBasicDetailsLabels = driver.findElements(By.xpath(partyMakerLocator.getString("label-PartyBasicDetails")));
					
					JSONObject updatedPartyBasicDetails = party.getJSONArray("party_basic_details").getJSONObject(0);
					if(updatedPartyBasicDetails.has("party_customer_id")) {
						System.out.println("details"+updatedPartyBasicDetails);
						if(partyBasicDetailsLabels.get(0).getText().equals(updatedPartyBasicDetails.getString("party_customer_id"))) {
							
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(0));
							step.log(Status.PASS, "Party customer ID: "+updatedPartyBasicDetails.getString("party_customer_id")+" updated. (See below image)");
							report.addStepPassScreenshot(driver, step);
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(0));
						}
						else {
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(0));
							step.log(Status.FAIL, "Party customer ID: "+updatedPartyBasicDetails.getString("party_customer_id")+" not updated. (See below image)");
							report.addStepFailScreenshot(driver, step);
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(0));
							throw new Exception("Party customer ID: "+updatedPartyBasicDetails.getString("party_customer_id")+" not updated");
						}
					}
					
					if(updatedPartyBasicDetails.has("party_name")) {
						if(partyBasicDetailsLabels.get(1).getText().equals(updatedPartyBasicDetails.getString("party_name"))) {
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(1));
							step.log(Status.PASS, "Party name: "+updatedPartyBasicDetails.getString("party_name")+" updated. (See below image)");
							report.addStepPassScreenshot(driver, step);
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(1));
						}
						else {
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(1));
							step.log(Status.FAIL, "Party name: "+updatedPartyBasicDetails.getString("party_name")+" not updated. (See below image)");
							report.addStepFailScreenshot(driver, step);							
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(1));
							throw new Exception("Party name: "+updatedPartyBasicDetails.getString("party_name")+" not updated");
						}
					}
					
					if(updatedPartyBasicDetails.has("party_remarks")) {
						if(partyBasicDetailsLabels.get(3).getText().equals(updatedPartyBasicDetails.getString("party_remarks"))) {
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(3));
							step.log(Status.PASS, "Party remarks: "+updatedPartyBasicDetails.getString("party_remarks")+" updated. (See below image)");
							report.addStepPassScreenshot(driver, step);
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(3));
						}
						else {
							utility.turnOnElementHighlight(driver, partyBasicDetailsLabels.get(3));
							step.log(Status.FAIL, "Party remarks: "+updatedPartyBasicDetails.getString("party_remarks")+" not updated. (See below image)");
							report.addStepFailScreenshot(driver, step);
							utility.turnOffElementHighlight(driver, partyBasicDetailsLabels.get(3));
							throw new Exception("Party remarks: "+updatedPartyBasicDetails.getString("party_remarks")+" not updated");
						}
					}
					
					if(updatedPartyBasicDetails.has("party_processing_units")) {
						driver.findElements(By.xpath(partyMakerLocator.getString("icon-PartyBasicDetails"))).get(5).click();				
						List<WebElement> processingUnitElements = driver.findElement(By.id(partyMakerLocator.getString("card-PartyProcessingUnits"))).findElements(By.tagName("div"));
						JSONArray actualProcessingUnits = new JSONArray();
						for(WebElement element: processingUnitElements) {
							String processingUnitText = element.getText();
							if(!actualProcessingUnits.toString().contains(processingUnitText)) {
								actualProcessingUnits.put(processingUnitText);
							}					
						}
						
						if(updatedPartyBasicDetails.getJSONArray("party_processing_units").toString().equals(actualProcessingUnits.toString())) {
							step.log(Status.PASS, "Party processing units updated. (See below image)");
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Party processing units not updated. (See below image)");
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Party processing units not updated");
						}
						driver.findElement(By.xpath(partyMakerLocator.getString("button-Close"))).click();
					}
				}
				if(party.has("party_contacts")) {
					utility.focusElement(driver, driver.findElements(By.tagName("tbody")).get(0));
					utility.turnOnElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(0));
					List<String> contactNameList = utility.extractColumnsFromTable(driver, step, driver.findElements(By.tagName("tbody")).get(0), 1);
					
					Iterator<Object> partyContactIterator = party.getJSONArray("party_contacts").iterator();
					while(partyContactIterator.hasNext()) {
						JSONObject partyContact = (JSONObject) partyContactIterator.next();
						JSONObject updatedPartyContact = partyContact.getJSONArray("party_updated_contact").getJSONObject(0);
						
						if(contactNameList.contains(updatedPartyContact.getString("party_contact_name"))) {
							step.log(Status.PASS, "Party contact: "+updatedPartyContact.getString("party_contact_name")+" updated. (See below image)");
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Party contact: "+updatedPartyContact.getString("party_contact_name")+" not updated. (See below image)");
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Party contact: "+updatedPartyContact.getString("party_contact_name")+" not updated");
						}
					}
					utility.turnOffElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(0));
				}
				
				if(party.has("party_accounts")) {
					utility.focusElement(driver, driver.findElements(By.tagName("tbody")).get(1));
					utility.turnOnElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(1));
					List<String> accountNumberList = utility.extractColumnsFromTable(driver, step, driver.findElements(By.tagName("tbody")).get(1), 1);
					
					Iterator<Object> partyAccountIterator = party.getJSONArray("party_accounts").iterator();
					while(partyAccountIterator.hasNext()) {
						JSONObject partyAccount = (JSONObject) partyAccountIterator.next();
						JSONObject updatedPartyAccount = partyAccount.getJSONArray("party_updated_account").getJSONObject(0);
						
						if(accountNumberList.contains(updatedPartyAccount.getString("party_account_beneficiary_account"))) {
							step.log(Status.PASS, "Party account: "+updatedPartyAccount.getString("party_account_beneficiary_account")+" updated. (See below image)");
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Party account: "+updatedPartyAccount.getString("party_account_beneficiary_account")+" not updated. (See below image)");
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Party account: "+updatedPartyAccount.getString("party_account_beneficiary_account")+" not updated");
						}
						
					}
					utility.turnOffElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(1));
				}
				
				if(party.has("party_documents")) {
					utility.focusElement(driver, driver.findElements(By.tagName("tbody")).get(2));
					utility.turnOnElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(2));
					List<String> documentNameList = utility.extractColumnsFromTable(driver, step, driver.findElements(By.tagName("tbody")).get(2), 1);
					
					Iterator<Object> partyDocumentIterator = party.getJSONArray("party_documents").iterator();
					while(partyDocumentIterator.hasNext()) {
						JSONObject partyDocument = (JSONObject) partyDocumentIterator.next();
						JSONObject updatedPartyDocument = partyDocument.getJSONArray("party_updated_document").getJSONObject(0);
						if(documentNameList.contains(updatedPartyDocument.getString("party_document_type"))) {
							step.log(Status.PASS, "Party document: "+updatedPartyDocument.getString("party_document_type")+" updated. (See below image)");
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Party document: "+updatedPartyDocument.getString("party_document_type")+" not updated. (See below image)");
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Party document: "+updatedPartyDocument.getString("party_document_type")+" not updated");
						}
					}
					utility.turnOffElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(2));
				}				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating Party: "+party.getString("party_name")+" summary screen.");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while validating Party: "+party.getString("party_name")+" summary screen.");
			}			
		}
	}
	
	public void validateDeletedSummary(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("edit_parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {		
				if(party.has("party_contacts")) {
					utility.focusElement(driver, driver.findElements(By.tagName("tbody")).get(0));
					utility.turnOnElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(0));
					
					if(driver.findElements(By.tagName("tbody")).get(0).getText().equals("There are no contacts added yet.")) {
						step.log(Status.PASS, "Party contact deleted. (See below image)");
						report.addStepPassScreenshot(driver, step);
					}
					else {
						List<String> contactNameList = utility.extractColumnsFromTable(driver, step, driver.findElements(By.tagName("tbody")).get(0), 1);
						
						Iterator<Object> partyContactIterator = party.getJSONArray("party_contacts").iterator();
						while(partyContactIterator.hasNext()) {
							JSONObject partyContact = (JSONObject) partyContactIterator.next();
							
							if(!contactNameList.contains(partyContact.getString("party_contact_name"))) {
								step.log(Status.PASS, "Party contact: "+partyContact.getString("party_contact_name")+" deleted. (See below image)");
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Party contact: "+partyContact.getString("party_contact_name")+" not deleted. (See below image)");
								report.addStepFailScreenshot(driver, step);
								throw new Exception("Party contact: "+partyContact.getString("party_contact_name")+" not deleted");
							}
						}
					}	
					
					utility.turnOffElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(0));
				}
				
				if(party.has("party_accounts")) {
					utility.focusElement(driver, driver.findElements(By.tagName("tbody")).get(1));
					utility.turnOnElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(1));
					if(driver.findElements(By.tagName("tbody")).get(1).getText().equals("There are no accounts added yet.")) {
						step.log(Status.PASS, "Party account deleted. (See below image)");
						report.addStepPassScreenshot(driver, step);
					}
					else {
						List<String> accountNumberList = utility.extractColumnsFromTable(driver, step, driver.findElements(By.tagName("tbody")).get(1), 1);						
						Iterator<Object> partyAccountIterator = party.getJSONArray("party_accounts").iterator();
						while(partyAccountIterator.hasNext()) {
							JSONObject partyAccount = (JSONObject) partyAccountIterator.next();
							
							if(!accountNumberList.contains(partyAccount.getString("party_account_beneficiary_account"))) {
								step.log(Status.PASS, "Party account: "+partyAccount.getString("party_account_beneficiary_account")+" deleted. (See below image)");
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Party account: "+partyAccount.getString("party_account_beneficiary_account")+" not deleted. (See below image)");
								report.addStepFailScreenshot(driver, step);
								throw new Exception("Party account: "+partyAccount.getString("party_account_beneficiary_account")+" not deleted");
							}							
						}
					}					
					utility.turnOffElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(1));
				}
				
				if(party.has("party_documents")) {
					utility.focusElement(driver, driver.findElements(By.tagName("tbody")).get(2));
					utility.turnOnElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(2));
					if(driver.findElements(By.tagName("tbody")).get(2).getText().equals("There are no documents added yet.")) {
						step.log(Status.PASS, "Party document deleted. (See below image)");
						report.addStepPassScreenshot(driver, step);
					}
					else {
						List<String> documentNameList = utility.extractColumnsFromTable(driver, step, driver.findElements(By.tagName("tbody")).get(2), 1);					
						Iterator<Object> partyDocumentIterator = party.getJSONArray("party_documents").iterator();
						while(partyDocumentIterator.hasNext()) {
							JSONObject partyDocument = (JSONObject) partyDocumentIterator.next();
							
							if(!documentNameList.contains(partyDocument.getString("party_document_type"))) {
								step.log(Status.PASS, "Party document: "+partyDocument.getString("party_document_type")+" deleted. (See below image)");
								report.addStepPassScreenshot(driver, step);
							}
							else {
								step.log(Status.FAIL, "Party document: "+party.getString("party_name")+" is not deleted. (See below image)");
								report.addStepFailScreenshot(driver, step);
								throw new Exception("Party: "+party.getString("party_name")+" is not deleted");
							}
						}
					}
					utility.turnOffElementHighlight(driver, driver.findElements(By.tagName("tbody")).get(2));
				}				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating Party: "+party.getString("party_name")+" summary screen.");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while validating Party: "+party.getString("party_name")+" summary screen.");
			}			
		}
	}
	
	public void generateRequiredFieldMessages(WebDriver driver, ExtentTest step) throws Exception {		
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();		
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {
				driver.findElement(By.xpath(partyMakerLocator.getString("button-AddNewParty"))).click();
				
				generatePartyBasicDetailRequiredMessages(driver, step, party);						
				Thread.sleep(2000);
				if(party.has("party_contacts")) {
					generatePartyContactRequiredMessages(driver, step, party.getJSONArray("party_contacts"));
				}
				Thread.sleep(500);
				if(party.has("party_accounts")) {
					generatePartyAccountRequiredMessages(driver, step, party.getJSONArray("party_accounts"));					
				}
				Thread.sleep(500);
				if(party.has("party_documents")) {
					generatePartyDocumentRequiredMessages(driver, step, party.getJSONArray("party_documents"));					
				}				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while generating party required field messages");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while generating party required field messages");
			}
		}	
	}
	
	public void validateRequiredMessagesGenerated(ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();		
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {
				JSONArray requiredMessages = party.getJSONArray("party_required_messages");
				Iterator<Object> messageIterator = requiredMessages.iterator();
				while(messageIterator.hasNext()) {
					String expectedRequiredMessage = (String) messageIterator.next();
					if(actualRequiredMessages.contains(expectedRequiredMessage)) {
						step.log(Status.PASS, "Message: '"+expectedRequiredMessage+"' displayed");
						actualRequiredMessages.remove(expectedRequiredMessage);
					}
					else {
						step.log(Status.FAIL, "Message: '"+expectedRequiredMessage+"' not displayed");
						throw new Exception("Message: '"+expectedRequiredMessage+"' not displayed");
					}
				}
				
				if(!actualRequiredMessages.isEmpty()) {
					step.log(Status.FAIL, "Additional messages: "+actualRequiredMessages.toString()+" displayed");
					throw new Exception("Additional messages: "+actualRequiredMessages.toString()+" displayed");
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating party required field messages");
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while validating party required field messages");
			}
		}
	}	
	
	public void validateResponseGenerated(ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();		
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {
				if(party.has("party_response_messages")) {
					JSONArray responseMessages = party.getJSONArray("party_response_messages");
					Iterator<Object> messageIterator = responseMessages.iterator();
					while(messageIterator.hasNext()) {
						String expectedResponseMessage = (String) messageIterator.next();
						
						if(actualResponseMessages.contains(expectedResponseMessage)) {
							step.log(Status.PASS, "Message: '"+expectedResponseMessage+"' displayed");
							actualResponseMessages.remove(expectedResponseMessage);
						}
						else {
							step.log(Status.FAIL, "Message: '"+expectedResponseMessage+"' not displayed");
							throw new Exception("Message: '"+expectedResponseMessage+"' not displayed");
						}
					}
					
					if(!actualResponseMessages.isEmpty()) {
						step.log(Status.FAIL, "Additional messages: "+actualResponseMessages.toString()+" displayed");
						throw new Exception("Additional messages: "+actualResponseMessages.toString()+" displayed");
					}
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating party response messages");				
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while validating party response messages");
			}
		}
	}
	
	public void addParties(WebDriver driver, ExtentTest step) throws Exception {
		
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {				
				driver.findElement(By.xpath(partyMakerLocator.getString("button-AddNewParty"))).click();
				
				if(party.getString("party_type").toLowerCase().equals("external")) {					
					driver.findElement(By.id(partyMakerLocator.getString("checkbox-PartyType"))).findElement(By.tagName("div")).click();
					
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyCustomerID"))).sendKeys(party.getString("party_customer_id"));
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyName"))).sendKeys(party.getString("party_name"));
					
					driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1), party.getJSONArray("party_processing_units"));
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyRemarks"))).sendKeys(party.getString("party_remarks"));
					
					if(party.getBoolean("party_neutral")) {
						driver.findElement(By.id(partyMakerLocator.getString("checkbox-PartyNeutral"))).click();
					}
					
				}
				else if (party.getString("party_type").toLowerCase().equals("internal")) {					
					
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyCustomerID"))).sendKeys(party.getString("party_customer_id"));
									
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyName"))).sendKeys(party.getString("party_name"));
					
					driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1), party.getJSONArray("party_processing_units"));
					driver.findElement(By.id(partyMakerLocator.getString("input-PartyRemarks"))).sendKeys(party.getString("party_remarks"));
						
				}				
				
				driver.findElement(By.id(partyMakerLocator.getString("button-PartyNext"))).click();
				
				if(!utility.isElementHiddenNow(driver, By.xpath(partyMakerLocator.getString("button-OK")))) {
					String response = utility.extractTitleMessage(driver, step, By.id(partyMakerLocator.getString("label-Title")));
					actualResponseMessages.add(response);					
					driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();
					driver.findElement(By.id(partyMakerLocator.getString("button-Back"))).click();
					continue;					
				}
				
				Thread.sleep(2000);
				if(party.has("party_contacts")) {
					addPartyContacts(driver, step, party.getJSONArray("party_contacts"));					
				}
				Thread.sleep(500);
				if(party.has("party_accounts")) {
					addPartyAccounts(driver, step, party.getJSONArray("party_accounts"));					
				}
				Thread.sleep(500);
				if(party.has("party_documents")) {
					addPartyDocuments(driver, step, party.getJSONArray("party_documents"));					
				}
				
				Thread.sleep(2000);
				driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
				
				step.log(Status.INFO, "Party Summary: (See below image)");
				Thread.sleep(1000);
				report.addStepInfoScreenshot(driver, step);
				
				if(party.getBoolean("party_submit")) {
					submitParty(driver, step, party.getString("party_name"));
				}
				else {
					if(partyIterator.hasNext()) {
						driver.findElement(By.id(partyMakerLocator.getString("button-Back"))).click();
					}
				}
				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Party: "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while adding Party: "+party.getString("party_name"));
			}			
		}
	}
	
	public void addParties(WebDriver driver, ExtentTest step,  JSONObject party) throws Exception {
		
		try {				
			driver.findElement(By.xpath(partyMakerLocator.getString("button-AddNewParty"))).click();
			
			if(party.getString("party_type").toLowerCase().equals("external")) {					
				driver.findElement(By.id(partyMakerLocator.getString("checkbox-PartyType"))).findElement(By.tagName("div")).click();
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyCustomerID"))).sendKeys(party.getString("party_customer_id"));
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyName"))).sendKeys(party.getString("party_name"));
				
				driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1), party.getJSONArray("party_processing_units"));
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyRemarks"))).sendKeys(party.getString("party_remarks"));
				
				if(party.getBoolean("party_neutral")) {
					driver.findElement(By.id(partyMakerLocator.getString("checkbox-PartyNeutral"))).click();
				}
				
			}
			else if (party.getString("party_type").toLowerCase().equals("internal")) {					
				
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyCustomerID"))).sendKeys(party.getString("party_customer_id"));
								
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyName"))).sendKeys(party.getString("party_name"));
				
				driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(partyMakerLocator.getString("select-PartyProcessingUnit"))).findElements(By.tagName("ul")).get(1), party.getJSONArray("party_processing_units"));
				driver.findElement(By.id(partyMakerLocator.getString("input-PartyRemarks"))).sendKeys(party.getString("party_remarks"));
					
			}				
			
			driver.findElement(By.id(partyMakerLocator.getString("button-PartyNext"))).click();
			
			if(!utility.isElementHiddenNow(driver, By.xpath(partyMakerLocator.getString("button-OK")))) {
				String response = utility.extractTitleMessage(driver, step, By.id(partyMakerLocator.getString("label-Title")));
				actualResponseMessages.add(response);					
				driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();
				driver.findElement(By.id(partyMakerLocator.getString("button-Back"))).click();									
			}
			
			Thread.sleep(2000);
			if(party.has("party_contacts")) {
				addPartyContacts(driver, step, party.getJSONArray("party_contacts"));					
			}
			Thread.sleep(500);
			if(party.has("party_accounts")) {
				addPartyAccounts(driver, step, party.getJSONArray("party_accounts"));					
			}
			Thread.sleep(500);
			if(party.has("party_documents")) {
				addPartyDocuments(driver, step, party.getJSONArray("party_documents"));					
			}
			
			Thread.sleep(2000);
			driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
			
			step.log(Status.INFO, "Party Summary: (See below image)");
			Thread.sleep(1000);
			report.addStepInfoScreenshot(driver, step);
			
			if(party.getBoolean("party_submit")) {
				submitParty(driver, step, party.getString("party_name"));
			}			
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding Party: "+party.getString("party_name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());				
			throw new Exception("Error while adding Party: "+party.getString("party_name"));
		}
	}
	
	public void checkPartiesAvailable(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {				
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyMakerLocator.getString("column-PartyName")), 2, 120);
				driver.findElements(By.xpath(partyMakerLocator.getString("input-FilterText"))).get(1).clear();
				driver.findElements(By.xpath(partyMakerLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyMakerLocator.getString("column-PartyName")), 2, 120);
				
				if(driver.findElements(By.xpath(partyMakerLocator.getString("column-PartyName"))).size()==1) {
					step.log(Status.FAIL, "Party: "+party.getString("party_name")+" is not available at Party Maker Queue. (See below image)");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Party: "+party.getString("party_name")+" is not available at Party Maker Queue");
				}
				else {
					String resultParty = driver.findElements(By.xpath(partyMakerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
					
					if(resultParty.equals(party.getString("party_name").trim().toLowerCase())) {
						step.log(Status.PASS, "Party: "+party.getString("party_name")+" available at Party Maker Queue. (See below image)");
						report.addStepPassScreenshot(driver, step);
					}
					else {
						step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not filtered at Party Maker Queue. (See below image)");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Party: "+party.getString("party_name")+" not filtered at Party Maker Queue");
					}
				}				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while filtering Party: "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while filtering Party: "+party.getString("party_name"));
			}
		}
	}
	
	public void checkPartiesNotAvailable(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {	
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyMakerLocator.getString("column-PartyName")), 2, 120);
				driver.findElements(By.xpath(partyMakerLocator.getString("input-FilterText"))).get(1).clear();
				driver.findElements(By.xpath(partyMakerLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyMakerLocator.getString("column-PartyName")), 2, 10);
				
				if(driver.findElements(By.xpath(partyMakerLocator.getString("column-PartyName"))).size()==1) {
					step.log(Status.PASS, "Party: "+party.getString("party_name")+" is not available at Party Maker Queue. (See below image)");
					report.addStepPassScreenshot(driver, step);
				}
				else {
					String resultParty = driver.findElements(By.xpath(partyMakerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
					
					if(resultParty.equals(party.getString("party_name").trim().toLowerCase())) {
						step.log(Status.FAIL, "Party: "+party.getString("party_name")+" available at Party Maker Queue. (See below image)");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Party: "+party.getString("party_name")+" available at Party Maker Queue");
					}
					else {
						step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not filtered at Party Maker Queue. (See below image)");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Party: "+party.getString("party_name")+" not filtered at Party Maker Queue");
					}
				}	
				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while filtering Party: "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while filtering Party: "+party.getString("party_name"));
			}
		}
	}
	
	public void editParties(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("edit_parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {	
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyMakerLocator.getString("column-PartyName")), 2, 120);
				driver.findElements(By.xpath(partyMakerLocator.getString("input-FilterText"))).get(1).clear();
				driver.findElements(By.xpath(partyMakerLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyMakerLocator.getString("column-PartyName")), 2, 120);
				
				String resultParty = driver.findElements(By.xpath(partyMakerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
				
				if(resultParty.equals(party.getString("party_name").trim().toLowerCase())) {
					step.log(Status.INFO, "Party: "+party.getString("party_name")+" available at Party Maker Queue");
					driver.findElements(By.xpath(partyMakerLocator.getString("column-PartyManage"))).get(1).findElements(By.tagName("i")).get(0).click();
					
					utility.waitForProgressBarToLoad(driver);
					
					if(party.has("party_basic_details")) {
						editPartyBasicDetails(driver, step, party.getJSONArray("party_basic_details"));						
					}
					
					Thread.sleep(1000);
					driver.findElement(By.id(partyMakerLocator.getString("button-PartyNext"))).click();
					
					if(!utility.isElementHiddenNow(driver, By.xpath(partyMakerLocator.getString("button-OK")))) {
						String response = utility.extractTitleMessage(driver, step, By.id(partyMakerLocator.getString("label-Title")));;
						actualResponseMessages.add(response);						
						driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();
						driver.findElement(By.id(partyMakerLocator.getString("button-Back"))).click();
						continue;
					}
					
					Thread.sleep(2000);
					if(party.has("party_contacts")) {
						editPartyContacts(driver, step, party.getJSONArray("party_contacts"));						
					}
					
					if(party.has("party_accounts")) {
						editPartyAccounts(driver, step, party.getJSONArray("party_accounts"));						
					}
					
					if(party.has("party_documents")) {
						editPartyDocuments(driver, step, party.getJSONArray("party_documents"));						
					}
					
					Thread.sleep(2000);
					driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
					
					step.log(Status.INFO, "Party Summary: (See below image)");
					Thread.sleep(1000);
					report.addStepInfoScreenshot(driver, step);
					
					if(party.getBoolean("party_submit")) {
						submitParty(driver, step, party.getString("party_name"));
					}
				}	
				else {
					step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not available at Party Maker Queue (See image below)");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Party: "+party.getString("party_name")+" not available at Party Maker Queue");
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party: "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party: "+party.getString("party_name"));
			}
		}
	}
	
	public void editParty(WebDriver driver, ExtentTest step,  JSONObject party) throws Exception {
		
		try {				
			if(party.has("party_basic_details")) {
				editPartyBasicDetails(driver, step, party.getJSONArray("party_basic_details"));				
			}
			
			Thread.sleep(1000);
			driver.findElement(By.id(partyMakerLocator.getString("button-PartyNext"))).click();
			
			if(!utility.isElementHiddenNow(driver, By.xpath(partyMakerLocator.getString("button-OK")))) {
				String response = utility.extractTitleMessage(driver, step, By.id(partyMakerLocator.getString("label-Title")));
				actualResponseMessages.add(response);				
				driver.findElement(By.xpath(partyMakerLocator.getString("button-OK"))).click();
				driver.findElement(By.id(partyMakerLocator.getString("button-Back"))).click();
				return;
			}
			
			Thread.sleep(2000);
			if(party.has("party_contacts")) {
				editPartyContacts(driver, step, party.getJSONArray("party_contacts"));				
			}
			
			if(party.has("party_accounts")) {
				editPartyAccounts(driver, step, party.getJSONArray("party_accounts"));				
			}
			
			if(party.has("party_documents")) {
				editPartyDocuments(driver, step, party.getJSONArray("party_documents"));				
			}
			
			Thread.sleep(2000);
			driver.findElement(By.id(partyMakerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
			
			step.log(Status.INFO, "Party Summary: (See below image)");
			Thread.sleep(1000);
			report.addStepInfoScreenshot(driver, step);
			
			if(party.getBoolean("party_submit")) {
				submitParty(driver, step, party.getString("party_name"));
			}			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while editing Party: "+party.getString("party_name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while editing Party: "+party.getString("party_name"));
		
		}
	}

	
	public void deleteParties(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("delete_parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {			
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyMakerLocator.getString("column-PartyName")), 2, 120);
				driver.findElements(By.xpath(partyMakerLocator.getString("input-FilterText"))).get(1).clear();
				driver.findElements(By.xpath(partyMakerLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyMakerLocator.getString("column-PartyName")), 2, 120);
				
				String resultParty = driver.findElements(By.xpath(partyMakerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
				
				if(resultParty.equals(party.getString("party_name").trim().toLowerCase())) {
					step.log(Status.INFO, "Party: "+party.getString("party_name")+" available at Party Maker Queue");
					driver.findElements(By.xpath(partyMakerLocator.getString("column-PartyManage"))).get(1).findElements(By.tagName("i")).get(1).click();
					
					step.log(Status.INFO, "Party: "+party.getString("party_name")+" deleted");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(partyMakerLocator.getString("button-Yes"))).click();
				}
				else {
					step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not available at Party Maker Queue. (See below image).");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Party: "+party.getString("party_name")+" not available at Party Maker Queue");
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while deleting party: "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while deleting party: "+party.getString("party_name"));
			}
		}
	}
}
