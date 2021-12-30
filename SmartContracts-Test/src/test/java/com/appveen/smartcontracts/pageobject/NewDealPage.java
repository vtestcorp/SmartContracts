
package com.appveen.smartcontracts.pageobject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class NewDealPage {
	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject newDealLocator = null;
	private LandingPage landingPage = null;
	private JSONArray deals = null;
	private boolean preferenceSetOnce = false;
	private List<String> partyNameSuggestions = new ArrayList<String>();
	private List<String> expectedSourceAccounts = new ArrayList<String>();
	private List<String> expectedContactList = new ArrayList<String>();	
	private HashMap<String, ArrayList<String>> expectedDealDetails = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> expectedAccountDetails = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> expectedPartyDetails = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<String>> expectedFeeDetails = new HashMap<String, ArrayList<String>>();
	
	public static HashMap<String, String> dealDetails = new HashMap<String, String>();
	public static List<String> actualRequiredMessages = new ArrayList<String>();
	public static List<String> actualResponseMessages = new ArrayList<String>();
	
	
	private void setPreference(WebDriver driver, ExtentTest step) throws Exception {
		try {			
			String[] preferences = {"Account Number"};
			List<String> preferenceList = new ArrayList<String>();
			
			if(!preferenceSetOnce) {
				
				preferenceSetOnce = true;
				
				driver.findElement(By.xpath(newDealLocator.getString("button-SetPreference"))).click();
				
				WebElement checkbox =  driver.findElement(By.xpath(newDealLocator.getString("checkbox-SelectAll")));
				if(checkbox.isSelected()) {
					checkbox.click();
				}
				else {
					checkbox.click();
					checkbox.click();
				}
				
				List<WebElement> preferenceElements = driver.findElement(By.id(newDealLocator.getString("list-PreferenceList"))).findElements(By.xpath(newDealLocator.getString("label-PreferenceName")));
				
				for(WebElement element : preferenceElements) {
					preferenceList.add(element.getText());
				}
				
				for(int index = 0; index < preferences.length; index++) {
					if(preferenceList.contains(preferences[index])) {
						preferenceElements.get(preferenceList.indexOf(preferences[index])).findElement(By.id(newDealLocator.getString("checkbox-Preference"))).click();
					}
				}
				
				driver.findElement(By.xpath(newDealLocator.getString("button-PreferenceApply"))).click();
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while setting preferences");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while setting preferences");
		}
	}
	
	private void setAttributes(WebDriver driver, ExtentTest step,  JSONArray attributeList) throws Exception {
		try {
			Iterator<Object> attributeIterator = attributeList.iterator();
			
			if(!utility.isElementHiddenNow(driver, By.id(newDealLocator.getString("input-Attributes")))) {
				
				WebElement attributeInputList = driver.findElement(By.id(newDealLocator.getString("input-Attributes")));
				List<WebElement> inputItems = attributeInputList.findElements(By.tagName("input"));		
				Iterator<WebElement> inputItemsIterator = inputItems.iterator();
				while(attributeIterator.hasNext() && inputItemsIterator.hasNext()) {
					String attribute = attributeIterator.next().toString();
					inputItemsIterator.next().sendKeys(attribute);							
				}								
			}
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding attributes "+attributeList.toString());
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding attributes "+attributeList.toString());
		}
	}
	
	private void setContacts(WebDriver driver, ExtentTest step,  JSONArray contactList) throws InterruptedException {
		Iterator<Object> contactIterator = contactList.iterator();
		
		while (contactIterator.hasNext()) {
			String contact = contactIterator.next().toString();
			
			try {				
				driver.findElement(By.id(newDealLocator.getString("input-ContactSearch"))).clear();
				driver.findElement(By.id(newDealLocator.getString("input-ContactSearch"))).sendKeys(contact);
				driver.findElement(By.id(newDealLocator.getString("input-ContactSearch"))).sendKeys(Keys.ENTER);
				Thread.sleep(1000);
				List<WebElement> searchResult = driver.findElements(By.id(newDealLocator.getString("input-ContactSearchResult")));
				Iterator<WebElement> searchResultIterator = searchResult.iterator();
				while(searchResultIterator.hasNext()) {
					WebElement result = searchResultIterator.next();
					if(!result.isSelected()) {
						result.click();
					}
				}								
				}
				catch (NoSuchElementException e) {
					step.log(Status.FAIL, "Contact: "+contact+" not found!");
					continue;
				}				
		}
		
		driver.findElement(By.id(newDealLocator.getString("button-ContactUpdate"))).click();
	
	}
	
	private void setDealContacts(WebDriver driver, ExtentTest step,  String dealName, JSONArray contactList) throws InterruptedException {
		Iterator<Object> contactIterator = contactList.iterator();
		
		while (contactIterator.hasNext()) {
			String contact = contactIterator.next().toString();
			try {				
				utility.addToExpectedDetails(expectedDealDetails,dealName.toLowerCase().trim(), "contact:"+contact.toLowerCase().trim());
				
				driver.findElement(By.id(newDealLocator.getString("input-DealContactSearch"))).clear();
				driver.findElement(By.id(newDealLocator.getString("input-DealContactSearch"))).sendKeys(contact);
				driver.findElement(By.id(newDealLocator.getString("input-DealContactSearch"))).sendKeys(Keys.ENTER);
				Thread.sleep(500);
				List<WebElement> searchResult = driver.findElements(By.id(newDealLocator.getString("input-DealContactSearchResult")));
				Iterator<WebElement> searchResultIterator = searchResult.iterator();
				while(searchResultIterator.hasNext()) {
					WebElement result = searchResultIterator.next();
					if(!result.isSelected()) {
						result.click();
					}
				}					
				expectedContactList.add(contact);
				}
				catch (NoSuchElementException e) {
					step.log(Status.FAIL, "Contact: "+contact+" not found!");
					continue;
				}				
		}
		
		driver.findElement(By.id(newDealLocator.getString("button-DealContactUpdate"))).click();
		
	}	
	
	protected void addAccounts(WebDriver driver, ExtentTest step,  JSONArray accounts) throws Exception {
		Iterator<Object> accountIterator = accounts.iterator();		
		while(accountIterator.hasNext()) {
			JSONObject account = (JSONObject) accountIterator.next();
			try {						
				expectedAccountDetails.put(account.getString("account_search_input").toLowerCase().trim(), new ArrayList<String>());
				Select countrySelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-AccountCountry"))).get(1));
				countrySelect.selectByVisibleText(account.getString("account_country"));
				
				Select currencySelect = new Select(driver.findElement(By.xpath(newDealLocator.getString("select-AccountCurrency"))));
				currencySelect.selectByVisibleText(account.getString("account_currency"));				
				
				Select accountIdentifierSelect = new Select(driver.findElement(By.id(newDealLocator.getString("select-AccountIdentifierKey"))).findElement(By.tagName("select")));
				accountIdentifierSelect.selectByVisibleText(account.getString("account_identifier_key"));
				
				Select searchParameterSelect = new Select(driver.findElement(By.id(newDealLocator.getString("select-AccountSearchParameter"))));
				searchParameterSelect.selectByVisibleText(account.getString("account_search_parameter"));
				
				driver.findElement(By.id(newDealLocator.getString("input-AccountSearchInput"))).sendKeys(account.getString("account_search_input"));
				driver.findElement(By.id(newDealLocator.getString("button-AccountSearch"))).click();
				
				
				//Select accountOBOFlag = new Select(driver.findElement(By.xpath(newDealLocator.getString("select-AccountOBOFlag"))));
				//accountOBOFlag.selectByVisibleText(account.getString("account_OBO_Flag"));
				if(!utility.isElementHiddenNow(driver, By.xpath(newDealLocator.getString("label-AccountAlreadyOnboarded")))) {
					actualResponseMessages.add(driver.findElement(By.xpath(newDealLocator.getString("label-AccountAlreadyOnboarded"))).getText());
					step.log(Status.INFO, "Account: "+ account.getString("account_search_input")+ " already added to deal");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.id(newDealLocator.getString("button-AccountClose"))).click();
					continue;
				}
				
				//driver.findElement(By.tagName("xcro-datatype-input")).findElement(By.tagName("input")).sendKeys("PANNumbers");
				
				if(!account.getBoolean("account_balance_check")) {
					WebElement accountBalanceCheck = driver.findElement(By.id(newDealLocator.getString("checkbox-AccountBalanceCheck")));
					utility.focusElement(driver, accountBalanceCheck);
					accountBalanceCheck.click();
				}
				
				Thread.sleep(2000);
				driver.findElement(By.id(newDealLocator.getString("button-AddAccount"))).click();
				utility.waitForProgressBarToLoad(driver);
				
				if(!utility.isElementHiddenNow(driver, By.id(newDealLocator.getString("label-Title")))) {					
					actualResponseMessages.add(utility.extractTitleMessage(driver, step, By.id(newDealLocator.getString("label-Title"))));
					driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealOK"))).click();
					continue;
				}
				
				setPreference(driver, step);
				if(utility.extractColumnsFromTable(driver, step, By.xpath(newDealLocator.getString("table-AccountNumber")), 0).contains(account.getString("account_search_input"))) {
					step.log(Status.INFO, "Account: "+ account.getString("account_search_input")+ " added to deal");	
					expectedSourceAccounts.add(account.getString("account_search_input"));
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
	
	protected void addPartyContacts(WebDriver driver, ExtentTest step,  String partyName, JSONArray partyContacts) throws Exception {
		
		Iterator<Object> partyContactIterator = partyContacts.iterator();
		
		while (partyContactIterator.hasNext()) {
			JSONObject partyContact = (JSONObject) partyContactIterator.next();
			try {				
				utility.addToExpectedDetails(expectedPartyDetails, partyName.toLowerCase().trim(), "contact:"+partyContact.getString("party_contact_name").toLowerCase().trim());
				
				driver.findElement(By.id(newDealLocator.getString("button-NewPartyContact"))).click();				
				
				if(partyContact.has("party_link_contact")) {
					
					List<WebElement> partyContactTables = driver.findElement(By.id(newDealLocator.getString("grid-PartyContactList"))).findElements(By.id(newDealLocator.getString("table-PartyTable")));
					List<String> partyContactNameList = utility.extractColumnsFromTable(driver, step, partyContactTables, 1);
					int partyContactIndex = partyContactNameList.indexOf(partyContact.getString("party_contact_name"));
					
					driver.findElement(By.id(newDealLocator.getString("grid-PartyContactList"))).findElements(By.id(newDealLocator.getString("checkbox-PartyCheckbox"))).get(partyContactIndex).click();
					driver.findElement(By.id(newDealLocator.getString("button-LinkPartyContact"))).click();	
					utility.waitForProgressBarToLoad(driver);
					
					if(!utility.isElementHiddenNow(driver, By.xpath(newDealLocator.getString("button-SubmitDealOK")))) {
						step.log(Status.FAIL, "Error while adding Party Contact: "+partyContact.getString("party_contact_name"));
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Error while adding Party Contact: "+partyContact.getString("party_contact_name"));				
					}
					expectedContactList.add(partyContact.getString("party_contact_name"));
					continue;
				}									
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactName"))).sendKeys(partyContact.getString("party_contact_name"));
				
				if(partyContact.getBoolean("party_contact_authorised_signatory")) {
					driver.findElement(By.id(newDealLocator.getString("checkbox-PartyAuthorisedSignatory"))).click();
				}
				
				if(partyContact.getBoolean("party_contact_subinstruction_notification")) {
					driver.findElement(By.id(newDealLocator.getString("checkbox-PartySubInstructionNotification"))).click();
				}
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactEmail"))).sendKeys(partyContact.getString("party_contact_email"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactDesignation"))).sendKeys(partyContact.getString("party_contact_designation"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactWorkPhone"))).sendKeys(partyContact.getString("party_contact_work_phone"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactMobilePhone"))).sendKeys(partyContact.getString("party_contact_mobile_phone"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactStreet"))).sendKeys(partyContact.getString("party_contact_street"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactTown"))).sendKeys(partyContact.getString("party_contact_town"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactPin"))).sendKeys(partyContact.getString("party_contact_pin"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactState"))).sendKeys(partyContact.getString("party_contact_state"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyContactCountry"))).sendKeys(partyContact.getString("party_contact_country"));
				
				driver.findElement(By.id(newDealLocator.getString("button-AddPartyContact"))).click();	
				utility.waitForProgressBarToLoad(driver);
				
				if(!utility.isElementHiddenNow(driver, By.xpath(newDealLocator.getString("button-SubmitDealOK")))) {
					step.log(Status.FAIL, "Error while adding Party Contact: "+partyContact.getString("party_contact_name"));
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Error while adding Party Contact: "+partyContact.getString("party_contact_name"));				
				}
				expectedContactList.add(partyContact.getString("party_contact_name"));
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Party Contact: "+partyContact.getString("party_contact_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Party Contact: "+partyContact.getString("party_contact_name"));
			}
			
		}
	}
	
	protected void addPartyAccounts(WebDriver driver, ExtentTest step,  String partyName, JSONArray partyAccounts) throws Exception {
		
		Iterator<Object> partyAccountIterator = partyAccounts.iterator();
		driver.findElement(By.id(newDealLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
		
		while (partyAccountIterator.hasNext()) {
			JSONObject partyAccount = (JSONObject) partyAccountIterator.next();
			try {							
				utility.addToExpectedDetails(expectedPartyDetails, partyName.toLowerCase().trim(), "account:"+partyAccount.getString("party_account_beneficiary_account").toLowerCase().trim());
				
				driver.findElement(By.id(newDealLocator.getString("button-NewPartyAccount"))).click();				
					
				if(partyAccount.has("party_link_account")) {
					
					List<WebElement> partyAccountTables = driver.findElement(By.id(newDealLocator.getString("grid-PartyAccountList"))).findElements(By.id(newDealLocator.getString("table-PartyTable")));
					List<String> partyAccountList = utility.extractColumnsFromTable(driver, step, partyAccountTables, 2);
					Integer partyAccountIndex = null;
					
					for(int index = 0; index < partyAccountList.size(); index++) {
						if(partyAccountList.get(index).contains(partyAccount.getString("party_account_beneficiary_account"))) {
							partyAccountIndex = index;
						}
					}						
					
					driver.findElement(By.id(newDealLocator.getString("grid-PartyAccountList"))).findElements(By.id(newDealLocator.getString("checkbox-PartyCheckbox"))).get(partyAccountIndex).click();
					driver.findElement(By.id(newDealLocator.getString("button-LinkPartyAccount"))).click();	
					utility.waitForProgressBarToLoad(driver);
					
					if(!utility.isElementHiddenNow(driver, By.xpath(newDealLocator.getString("button-SubmitDealOK")))) {
						step.log(Status.FAIL, "Error while adding Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Error while adding Party Account: "+partyAccount.getString("party_account_beneficiary_account"));					
					}
					continue;
				}				
				
				Thread.sleep(1000);
				driver.findElement(By.id(newDealLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-PartyAccountPaymentSystem"))).findElement(By.tagName("ul")), partyAccount.getString("party_account_payment_system"));
				
//				driver.findElement(By.id(newDealLocator.getString("input-PartyAccountBeneficiaryBIC"))).sendKeys(partyAccount.getString("party_account_beneficiary_BIC"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyAccountBeneficiaryName"))).sendKeys(partyAccount.getString("party_account_beneficiary_name"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyAccountBeneficiaryAddress"))).sendKeys(partyAccount.getString("party_account_beneficiary_address"));
								
//				Select partyAccountIBAN = new Select(driver.findElement(By.id(newDealLocator.getString("select-PartyAccountIBAN"))));
//				partyAccountIBAN.selectByVisibleText(partyAccount.getString("party_account_IBAN"));
				
				Select partyAccountBeneficiaryCountrySelect = new Select(driver.findElement(By.id(newDealLocator.getString("select-PartyAccountBeneficiaryCountry"))));
				partyAccountBeneficiaryCountrySelect.selectByVisibleText(partyAccount.getString("party_account_beneficiary_country"));
				
//				driver.findElement(By.id(newDealLocator.getString("input-PartyAccountBeneficiaryCurrency"))).sendKeys(partyAccount.getString("party_account_beneficiary_currency"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyAccountDestinationAccount"))).sendKeys(partyAccount.getString("party_account_beneficiary_account"));
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyAccountDescription"))).sendKeys(partyAccount.getString("party_account_description"));
				
				driver.findElement(By.id(newDealLocator.getString("button-AddPartyAccount"))).click();
				utility.waitForProgressBarToLoad(driver);
				
				if(!utility.isElementHiddenNow(driver, By.xpath(newDealLocator.getString("button-SubmitDealOK")))) {
					step.log(Status.FAIL, "Error while adding Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Error while adding Party Account: "+partyAccount.getString("party_account_beneficiary_account"));					
				}
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Party Account: "+partyAccount.getString("party_account_beneficiary_account"));
			}
			
		}
	}

	
	protected void addPartyDocuments(WebDriver driver, ExtentTest step,  String partyName, JSONArray partyDocuments) throws Exception {
		
		Iterator<Object> partyDocumentIterator = partyDocuments.iterator();
		
		driver.findElement(By.id(newDealLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
		
		while (partyDocumentIterator.hasNext()) {
			JSONObject partyDocument = (JSONObject) partyDocumentIterator.next();
			try {				
				utility.addToExpectedDetails(expectedPartyDetails, partyName.toLowerCase().trim(), "document:"+partyDocument.getString("party_document_type").toLowerCase().trim());
				
				driver.findElement(By.id(newDealLocator.getString("button-NewPartyDocument"))).click();				

				if(partyDocument.has("party_link_document")) {
							
					driver.findElement(By.id(newDealLocator.getString("checkbox-PartyDocumentCheckbox"))).findElement(By.tagName("input")).click();
					driver.findElement(By.id(newDealLocator.getString("button-LinkPartyDocument"))).click();	
					utility.waitForProgressBarToLoad(driver);			
					
					continue;
				}			

				Thread.sleep(500);
				driver.findElement(By.id(newDealLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-PartyDocumentType"))).findElement(By.tagName("ul")), partyDocument.getString("party_document_type"));
				
				Select partyDocumentNature = new Select(driver.findElement(By.xpath(newDealLocator.getString("select-PartyDocumentNature"))));
				partyDocumentNature.selectByVisibleText(partyDocument.getString("party_document_nature"));
				
				if(!utility.isElementHiddenNow(driver, By.id(newDealLocator.getString("input-PartyDocumentFromDate")))){				
					driver.findElement(By.id(newDealLocator.getString("input-PartyDocumentFromDate"))).findElement(By.tagName("input")).sendKeys(partyDocument.getString("party_document_fromdate"));
					
					if(partyDocument.has("party_document_tilldate")) {
						driver.findElement(By.id(newDealLocator.getString("input-PartyDocumentTillDate"))).findElement(By.tagName("input")).sendKeys(partyDocument.getString("party_document_tilldate"));
					}
				}
				
				driver.findElement(By.id(newDealLocator.getString("input-PartyDocumentDescription"))).sendKeys(partyDocument.getString("party_document_description"));
				
				driver.findElement(By.id(newDealLocator.getString("button-AddPartyDocument"))).click();				
				utility.waitForProgressBarToLoad(driver);
				
				File partyDocumentFile = new File(partyDocument.getString("party_document_upload_url"));
				driver.findElement(By.id(newDealLocator.getString("input-PartyDocumentUpload"))).sendKeys(partyDocumentFile.getAbsolutePath());
				Thread.sleep(1000);
			}			
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Party Document: "+partyDocument.getString("party_document_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Party Document: "+partyDocument.getString("party_document_type"));
			}
			
		}
	}

	protected void addParties(WebDriver driver, ExtentTest step,  JSONArray parties) throws Exception {
		
		Iterator<Object> partyIterator = parties.iterator();
		
		driver.findElement(By.id(newDealLocator.getString("tab-PartyWizard"))).click();
		utility.waitForProgressBarToLoad(driver);
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {			
				expectedPartyDetails.put(party.getString("party_name").toLowerCase().trim(), new ArrayList<String>());
				utility.addToExpectedDetails(expectedPartyDetails, party.getString("party_name").toLowerCase().trim(), "responsibility:"+party.getString("party_responsibility").toLowerCase().trim());
				
				driver.findElement(By.id(newDealLocator.getString("button-NewParty"))).click();
				
				if(party.getBoolean("party_live")) {
					driver.findElement(By.id(newDealLocator.getString("button-AddLinkPartyExistingParty"))).click();
					
					if(party.getString("party_type").toLowerCase().equals("external")) {						
						if(party.getBoolean("party_neutral")) {
							driver.findElement(By.id(newDealLocator.getString("checkbox-PartyNeutral"))).click();
						}
					}
					else if(party.getString("party_type").toLowerCase().equals("internal")) {
						driver.findElement(By.id(newDealLocator.getString("checkbox-LinkPartyType"))).findElement(By.tagName("div")).click();
					}
					
					//driver.findElement(By.id(newDealLocator.getString("input-LinkPartyCustomerID"))).sendKeys(party.getString("party_customer_id"));
					driver.findElement(By.id(newDealLocator.getString("input-LinkPartyName"))).sendKeys(party.getString("party_name"));
					
					driver.findElement(By.id(newDealLocator.getString("button-LinkPartySearch"))).click();
					utility.waitForElementToBeDisplayed(driver, By.xpath(newDealLocator.getString("checkbox-LinkPartySelect")), 2, 10);
					
					if(driver.findElements(By.xpath(newDealLocator.getString("checkbox-LinkPartySelect"))).size() > 1) {
						driver.findElements(By.xpath(newDealLocator.getString("checkbox-LinkPartySelect"))).get(1).findElement(By.tagName("input")).click();
						partyNameSuggestions.add(driver.findElements(By.xpath(newDealLocator.getString("column-PartyName"))).get(2).getText());
						driver.findElement(By.id(newDealLocator.getString("button-LinkPartyAdd"))).click();	
					}
					else {
						expectedPartyDetails.remove(party.getString("party_name").toLowerCase().trim());
						driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
						driver.findElement(By.id(newDealLocator.getString("button-PartyBack"))).click();
						continue;
					}
				}
				else {
					driver.findElement(By.id(newDealLocator.getString("button-AddLinkPartyNewParty"))).click();
					
					if(party.getString("party_type").toLowerCase().equals("external")) {						
						if(party.getBoolean("party_neutral")) {
							driver.findElement(By.id(newDealLocator.getString("checkbox-PartyNeutral"))).click();
						}
					}
					else if(party.getString("party_type").toLowerCase().equals("internal")) {
						driver.findElement(By.id(newDealLocator.getString("checkbox-PartyType"))).findElement(By.tagName("div")).click();
					}
					
					driver.findElement(By.id(newDealLocator.getString("select-PartyCustomerID"))).sendKeys(party.getString("party_customer_id"));
					driver.findElement(By.id(newDealLocator.getString("select-PartyName"))).sendKeys(party.getString("party_name"));
					
					driver.findElement(By.id(newDealLocator.getString("input-PartyRemarks"))).sendKeys(party.getString("party_remarks"));
				}			
				
				driver.findElement(By.id(newDealLocator.getString("select-PartyResponsibility"))).findElement(By.tagName("input")).click();
				Thread.sleep(500);
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-PartyResponsibility"))).findElement(By.tagName("ul")), party.getString("party_responsibility"));
				
				List<String> responsibilityList = new ArrayList<String>();
				responsibilityList.add("investor");
				responsibilityList.add("obligor");
				responsibilityList.add("platform");
				if(responsibilityList.contains(party.getString("party_responsibility").toLowerCase().trim())) {
					
					List<WebElement> partyAttributes = driver.findElements(By.id(newDealLocator.getString("input-PartyAttributeValue")));
					partyAttributes.get(0).findElement(By.tagName("input")).sendKeys(party.getString("party_attribute_name"));
					
//					Select partyAttributeCountry = new Select(partyAttributes.get(1).findElement(By.tagName("select")));
//					partyAttributeCountry.selectByVisibleText(party.getString("party_attribute_country"));
					partyAttributes.get(1).findElement(By.tagName("input")).sendKeys(party.getString("party_attribute_country"));
				}				
				
				if(party.getBoolean("party_ecommerce")) {
					utility.addToExpectedDetails(expectedPartyDetails, party.getString("party_name").toLowerCase().trim(), "participant:"+party.getString("party_ecommerce_participant_id").toLowerCase().trim());
					driver.findElements(By.id(newDealLocator.getString("checkbox-PartyNeutral"))).get(1).click();
					
					driver.findElement(By.id(newDealLocator.getString("input-PartyEcommerceParticipantID"))).sendKeys(party.getString("party_ecommerce_participant_id"));
					
					Select partyEcommerceStatus = new Select(driver.findElements(By.id(newDealLocator.getString("select-PartyEcommerceStatus"))).get(1));
					partyEcommerceStatus.selectByVisibleText(party.getString("party_ecommerce_status"));
					
					List<WebElement> partyEcommerceDates = driver.findElements(By.id(newDealLocator.getString("input-DatePicker")));
					partyEcommerceDates.get(0).sendKeys(party.getString("party_ecommerce_valid_from"));
					partyEcommerceDates.get(1).sendKeys(party.getString("party_ecommerce_valid_until"));
					
					if(party.getBoolean("party_ecommerce_kyc_complete")) {
						driver.findElements(By.id(newDealLocator.getString("checkbox-PartyNeutral"))).get(2).click();
					}
					
					if(party.has("party_ecommerce_debit_account")) {	
						utility.addToExpectedDetails(expectedPartyDetails, party.getString("party_name").toLowerCase().trim(), "debit:"+party.getString("party_ecommerce_debit_account").toLowerCase().trim());
						
						driver.findElement(By.xpath(newDealLocator.getString("input-FilterText"))).sendKeys(party.getString("party_ecommerce_debit_account"));
						Thread.sleep(2000);
						driver.findElements(By.xpath(newDealLocator.getString("checkbox-PartyEcommerceAccount"))).get(1).click();
					}
					
				}
				
				driver.findElement(By.id(newDealLocator.getString("button-PartyNext"))).click();
				utility.waitForProgressBarToLoad(driver);
				
				if(!utility.isElementHiddenNow(driver, By.xpath(newDealLocator.getString("button-SubmitDealOK")))) {
					actualResponseMessages.add(utility.extractTitleMessage(driver, step, By.id(newDealLocator.getString("label-Title"))));
					driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealOK"))).click();
					driver.findElement(By.id(newDealLocator.getString("button-PartyBack"))).click();
					continue;
				}
				
				if(!utility.isElementHiddenNow(driver, By.tagName("xcro-input-error"))) {
					actualRequiredMessages.addAll(utility.extractRequiredMessage(driver, step));
					driver.findElement(By.id(newDealLocator.getString("button-PartyBack"))).click();
					continue;
				}
				
				
				
				if(party.has("party_contacts")) {
					addPartyContacts(driver, step, party.getString("party_name"),party.getJSONArray("party_contacts"));
				}
				
				if(party.has("party_accounts")) {
					addPartyAccounts(driver, step, party.getString("party_name"),party.getJSONArray("party_accounts"));
				}
				
				if(party.has("party_documents")) {
					addPartyDocuments(driver, step, party.getString("party_name"),party.getJSONArray("party_documents"));
				}
				step.log(Status.INFO, "Party: "+party.getString("party_name")+" added to deal");
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Party: "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Party: "+party.getString("party_name"));
			}	
			//Thread.sleep(2000);
			driver.findElement(By.id(newDealLocator.getString("button-PartyBack"))).click();
		}
	}
	
	private void addIncomeBooking(WebDriver driver, ExtentTest step,  JSONObject scheduledFee) throws Exception {
		try {
			utility.addToExpectedDetails(expectedFeeDetails,scheduledFee.getString("scheduled_fee_instruction_name").toLowerCase().trim(), "type:income booking");
			
			driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingInstructionName"))).clear();
			driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingInstructionName"))).sendKeys(scheduledFee.getString("scheduled_fee_instruction_name"));
			
			Select incomeBookingCurrency = new Select(driver.findElement(By.xpath(newDealLocator.getString("select-IncomeBookingCurrency"))));
			incomeBookingCurrency.selectByVisibleText(scheduledFee.getString("scheduled_fee_currency"));
			
			if(scheduledFee.getBoolean("scheduled_fee_upfront")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-IncomeBookingUpfront"))).click();
				driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingUpfrontAmount"))).sendKeys(scheduledFee.getString("scheduled_fee_upfront_amount"));
			}
			
			Select incomeBookingFeeMode = new Select(driver.findElement(By.xpath(newDealLocator.getString("select-IncomeBookingFeeMode"))));
			incomeBookingFeeMode.selectByVisibleText(scheduledFee.getString("fee_mode"));
			
			Select incomeBookingFeeType = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingFeeType"))).findElement(By.tagName("select")));
			incomeBookingFeeType.selectByVisibleText(scheduledFee.getString("fee_type"));
			
			switch(scheduledFee.getString("fee_type")){
				case "Recurring":
					driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingRecurringAmount"))).sendKeys(scheduledFee.getString("scheduled_fee_recurring_amount"));
					break;
					
				case "Transaction Based":
					Select incomeBookingTransactionBased = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingTransactionBased"))).findElement(By.tagName("select")));
					incomeBookingTransactionBased.selectByVisibleText(scheduledFee.getString("scheduled_fee_transaction_based"));
					
					driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingFeeReferenceAccount"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingFeeReferenceAccount"))).findElement(By.tagName("ul")), scheduledFee.getString("scheduled_fee_reference_account"));
					break;
					
				case "% of EOD Balance":
					driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingEODBalancePercent"))).sendKeys(scheduledFee.getString("scheduled_fee_eod_balance_percent"));
					
					driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingFeeReferenceAccount"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingFeeReferenceAccount"))).findElement(By.tagName("ul")), scheduledFee.getString("scheduled_fee_reference_account"));
					break;
				
				default:
					step.log(Status.FAIL, "Invalid Income Booking Fee Type: "+scheduledFee.getString("fee_type"));
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Invalid Income Booking Fee Type: "+scheduledFee.getString("fee_type"));					
			}
			
			driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingRemarks"))).sendKeys(scheduledFee.getString("scheduled_fee_remarks"));
			
			Select incomeBookingHolidayAction = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingHolidayAction"))).findElement(By.tagName("select")));
			incomeBookingHolidayAction.selectByVisibleText(scheduledFee.getString("scheduled_fee_holiday_action"));
			
			driver.findElement(By.xpath(newDealLocator.getString("button-IncomeBookingBasicNext"))).click();
			utility.waitForProgressBarToLoad(driver);
			
			if(scheduledFee.getString("fee_type").equals("Transaction Based")) {
				Iterator<Object> scheduledFeeTiers = scheduledFee.getJSONArray("scheduled_fee_tiers").iterator();
				int scheduledFeeTierIndex = 0;
				
				if(scheduledFee.getString("scheduled_fee_transaction_based").equals("Value")) {
					Select scheduledFeeTierFeeAmountType = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingTiersFeeAmountType"))).findElement(By.tagName("select")));
					scheduledFeeTierFeeAmountType.selectByVisibleText(scheduledFee.getString("scheduled_fee_tiers_fee_amount_type"));
				}
				
				while(scheduledFeeTiers.hasNext()) {
					JSONObject scheduledFeeTier = (JSONObject) scheduledFeeTiers.next();
					if(scheduledFeeTier.has("scheduled_fee_tiers_payment_instrument")) {
						driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingTiersPaymentInstrument"))).findElement(By.tagName("input")).click();
						utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingTiersPaymentInstrument"))).findElement(By.tagName("ul")), scheduledFeeTier.getString("scheduled_fee_tiers_payment_instrument"));
						
						driver.findElement(By.xpath(newDealLocator.getString("button-IncomeBookingTiersAddTier"))).click();						
					}
					else {
						driver.findElement(By.xpath(newDealLocator.getString("button-IncomeBookingTiersAddTierWithoutPaymentInstrument"))).click();	
					}
					
					Iterator<Object> scheduledFeeTiersDetails = scheduledFeeTier.getJSONArray("scheduled_fee_tiers_details").iterator();
					int scheduledFeeTiersDetailIndex = 0;
					
					while(scheduledFeeTiersDetails.hasNext()) {
						JSONObject scheduledFeeTiersDetail = (JSONObject) scheduledFeeTiersDetails.next();						
						WebElement scheduledFeeTiersTable = driver.findElements(By.xpath(newDealLocator.getString("table-IncomeBookingTiers"))).get(scheduledFeeTierIndex).findElement(By.tagName("tbody"));
						if(scheduledFeeTiersDetailIndex>0) {							
							scheduledFeeTiersTable.findElement(By.tagName("tr")).findElements(By.tagName("td")).get(4).findElement(By.tagName("span")).click();
						}
						WebElement scheduledFeeTiersTableRow = scheduledFeeTiersTable.findElements(By.tagName("tr")).get(scheduledFeeTiersDetailIndex);
						scheduledFeeTiersTableRow.findElements(By.tagName("td")).get(1).findElement(By.tagName("input")).sendKeys(scheduledFeeTiersDetail.getString("scheduled_fee_tiers_cumulative_from"));
						scheduledFeeTiersTableRow.findElements(By.tagName("td")).get(2).findElement(By.tagName("input")).sendKeys(scheduledFeeTiersDetail.getString("scheduled_fee_tiers_cumulative_to"));
						scheduledFeeTiersTableRow.findElements(By.tagName("td")).get(3).findElement(By.tagName("input")).sendKeys(scheduledFeeTiersDetail.getString("scheduled_fee_tiers_fee"));
						
						scheduledFeeTiersDetailIndex++;
					}
					scheduledFeeTierIndex++;
				}
				driver.findElement(By.xpath(newDealLocator.getString("button-IncomeBookingBasicNext"))).click();
				utility.waitForProgressBarToLoad(driver);
			}
			
			if(scheduledFee.getString("fee_type").equals("Recurring")) {
				Select incomeBookingContributeAs = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingContributeAs"))).findElement(By.tagName("select")));
				incomeBookingContributeAs.selectByVisibleText(scheduledFee.getString("scheduled_fee_contribute_as"));				
			}
			
			Select incomeBookingTaxCategory = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingTaxCategory"))).findElement(By.tagName("select")));
			incomeBookingTaxCategory.selectByVisibleText(scheduledFee.getString("scheduled_fee_tax_category"));
			
			if(scheduledFee.getJSONArray("scheduled_fee_parties").length()>1) {
				List<WebElement> incomeBookingPartyList =  driver.findElement(By.tagName(newDealLocator.getString("list-IncomeBookingParty"))).findElement(By.tagName("ul")).findElements(By.tagName("li"));
				incomeBookingPartyList.get(scheduledFee.getJSONArray("scheduled_fee_parties").length()-1).click();
			}
			
			Iterator<Object> incomeBookingParties = scheduledFee.getJSONArray("scheduled_fee_parties").iterator();
			int noOfParties = 0;
			while(incomeBookingParties.hasNext()) {
				JSONObject incomeBookingParty = (JSONObject) incomeBookingParties.next();
				
				driver.findElements(By.xpath(newDealLocator.getString("tab-IncomeBookingParty"))).get(noOfParties).click();
				
				driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingParty"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingParty"))).findElement(By.tagName("ul")), incomeBookingParty.getString("party_name"));
				Thread.sleep(500);
				
				if(incomeBookingParty.has("party_fee_debit_account")) {
					driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingFeeDebitAccount"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingFeeDebitAccount"))).findElement(By.tagName("ul")), incomeBookingParty.getString("party_fee_debit_account"));
				}
				
				if(scheduledFee.getBoolean("scheduled_fee_upfront")) {
					driver.findElement(By.xpath(newDealLocator.getString("input-IncomeBookingUpfrontContribution"))).sendKeys(incomeBookingParty.getString("party_upfront_contribution"));
				}
				
				driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingFeeContribution"))).sendKeys(incomeBookingParty.getString("party_fee_contribution"));
				driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingTaxReferenceNo"))).sendKeys(incomeBookingParty.getString("party_tax_reference_no"));
				driver.findElement(By.xpath(newDealLocator.getString("input-IncomeBookingInvoiceTo"))).sendKeys(incomeBookingParty.getString("party_invoice_to"));
				
				noOfParties++;				
			}
			
			driver.findElement(By.xpath(newDealLocator.getString("button-IncomeBookingBasicNext"))).click();	
			utility.waitForProgressBarToLoad(driver);
			
			if(scheduledFee.getBoolean("scheduled_fee_upfront")) {
				driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingScheduleExecutionDate"))).findElement(By.tagName("input")).sendKeys(scheduledFee.getString("scheduled_fee_upfront_execution_date"));
				Select incomeBookingScheduledAt = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentScheduledAt"))).get(1));
				incomeBookingScheduledAt.selectByVisibleText(scheduledFee.getString("scheduled_fee_upfront_scheduled_at"));
				
				driver.findElement(By.tagName(newDealLocator.getString("radio-IncomeBookingUpfrontSchedule"))).findElements(By.tagName("label")).get(1).click();
			}
			
			driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingScheduleStartDate"))).findElement(By.tagName("input")).sendKeys(scheduledFee.getString("scheduled_fee_start_date"));
			driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingScheduleEndDate"))).findElement(By.tagName("input")).sendKeys(scheduledFee.getString("scheduled_fee_end_date"));
			
			if(scheduledFee.getBoolean("scheduled_fee_invoice_same_as_scheduled_date")) {
				driver.findElement(By.xpath(newDealLocator.getString("checkbox-IncomeBookingCheckbox"))).click();
			}
			else {
				driver.findElement(By.xpath(newDealLocator.getString("input-IncomeBookingInvoiceDueDate"))).sendKeys(scheduledFee.getString("scheduled_fee_invoice_due_date"));				
			}
			
			driver.findElement(By.xpath(newDealLocator.getString("input-IncomeBookingFeeDueDate"))).sendKeys(scheduledFee.getString("scheduled_fee_due_date"));
			
			Select incomeBookingScheduledAt = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentScheduledAt"))).get(1));
			incomeBookingScheduledAt.selectByVisibleText(scheduledFee.getString("scheduled_fee_scheduled_at"));
			
			Select incomeBookingFrequency = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentFrequency"))).get(1));
			incomeBookingFrequency.selectByVisibleText(scheduledFee.getString("scheduled_fee_frequency"));
			
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRepeatEvery"))).clear();
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRepeatEvery"))).sendKeys(scheduledFee.getString("scheduled_fee_repeat_every"));
			
			switch (scheduledFee.getString("scheduled_fee_frequency").toLowerCase()) {
				case "days":
					break;
					
				case "weekly":
					
					selectDayOfWeek(driver, step, scheduledFee.getJSONArray("scheduled_fee_days_of_week"));
					break;
					
				case "monthly":
					
					Select incomeBookingFrequencySubtype = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentSubType"))).get(1));
					incomeBookingFrequencySubtype.selectByVisibleText(scheduledFee.getString("scheduled_fee_sub_type"));
					
					if(scheduledFee.getString("scheduled_fee_sub_type").toLowerCase().equals("day of month")) {
						
						driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentDays"))).findElement(By.tagName("input")).click();
						utility.selectFromDivMenu(driver, step, driver.findElement(By.xpath(newDealLocator.getString("select-ChildProcessingUnit"))), scheduledFee.getJSONArray("scheduled_fee_days"));
					}
					else if(scheduledFee.getString("scheduled_fee_sub_type").toLowerCase().equals("weekday(s)")) {
						
						Select incomeBookingFrequencyWeek = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentWeek"))).get(1));
						incomeBookingFrequencyWeek.selectByVisibleText(scheduledFee.getString("scheduled_fee_week"));
						
						selectDayOfWeek(driver, step, scheduledFee.getJSONArray("scheduled_fee_days_of_week"));					
					}
					break;
				
				default:
					step.log(Status.FAIL, "Invalid fee frequency: "+scheduledFee.getString("scheduled_fee_frequency"));
					throw new Exception("Invalid fee frequency: "+scheduledFee.getString("scheduled_fee_frequency"));
			}
			
			driver.findElement(By.xpath(newDealLocator.getString("button-IncomeBookingBasicNext"))).click();
			utility.waitForProgressBarToLoad(driver);
			
			List<WebElement> incomeBookingNarration = driver.findElements(By.id(newDealLocator.getString("input-IncomeBookingNarration")));			
			if(scheduledFee.has("scheduled_fee_credit_account")) {
				Select incomeBookingFeeCreditAccountNo = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingFeeCreditAccountNo"))).findElement(By.tagName("select")));
				incomeBookingFeeCreditAccountNo.selectByVisibleText(scheduledFee.getString("scheduled_fee_credit_account"));
				incomeBookingNarration.get(0).sendKeys(scheduledFee.getString("scheduled_fee_narration"));
			}
						
			if(scheduledFee.has("scheduled_fee_tax_credit_account")) {
				Select incomeBookingTaxCreditAccountNo = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingTaxCreditAccountNo"))).findElement(By.tagName("select")));
				incomeBookingTaxCreditAccountNo.selectByVisibleText(scheduledFee.getString("scheduled_fee_tax_credit_account"));
				incomeBookingNarration.get(1).sendKeys(scheduledFee.getString("scheduled_fee_narration"));
			}
			
			driver.findElement(By.xpath(newDealLocator.getString("button-IncomeBookingBasicNext"))).click();
			utility.waitForProgressBarToLoad(driver);
			
			List<WebElement> availableContacts = null;
			List<String> actualContactList = new ArrayList<String>();
			if(scheduledFee.has("scheduled_fee_invoice_contacts")) {
				driver.findElement(By.id(newDealLocator.getString("button-IncomeBookingInvoiceContacts"))).click();
				
				availableContacts = driver.findElements(By.xpath(newDealLocator.getString("label-ContactName")));
				actualContactList = new ArrayList<String>();
				for(WebElement contact : availableContacts) {
					actualContactList.add(contact.getText());
				}
				step.log(Status.INFO, "Contacts available at Income Booking - Expected: "+expectedContactList.toString()+" Actual: "+actualContactList.toString()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);				
				
				setContacts(driver, step, scheduledFee.getJSONArray("scheduled_fee_invoice_contacts"));
			}
			
			if(scheduledFee.getBoolean("scheduled_fee_due_reminder")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-IncomeBookingFeeDueReminders"))).click();
				driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingNoOfDaysBeforeCollecting"))).sendKeys(scheduledFee.getString("scheduled_fee_no_of_days_before_collecting"));
			}
			
			if(scheduledFee.getBoolean("scheduled_fee_overdue_reminder")) {
				utility.focusElement(driver, driver.findElement(By.id(newDealLocator.getString("button-IncomeBookingReminderContacts"))));
				driver.findElement(By.id(newDealLocator.getString("checkbox-IncomeBookingOverdueFeesReminder"))).click();
				
				Select incomeBookingOverdueReminderFrequency = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentFrequency"))).get(1));
				incomeBookingOverdueReminderFrequency.selectByVisibleText(scheduledFee.getString("scheduled_fee_overdue_reminder_frequency"));
				
				if(scheduledFee.getString("scheduled_fee_overdue_type").equals("Forever")) {
					driver.findElement(By.xpath(newDealLocator.getString("label-IncomeBookingOverdueTypeForver"))).click();
				}
				else if(scheduledFee.getString("scheduled_fee_overdue_type").equals("Stop after")) {
					driver.findElement(By.xpath(newDealLocator.getString("label-IncomeBookingOverdueTypeStopAfter"))).click();
					driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingOverdueNoOfReminders"))).sendKeys(scheduledFee.getString("scheduled_fee_no_of_reminders"));
				}
				
				driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingOverdueRepeatEvery"))).clear();
				driver.findElement(By.id(newDealLocator.getString("input-IncomeBookingOverdueRepeatEvery"))).sendKeys(scheduledFee.getString("scheduled_fee_overdue_reminder_repeat_every"));
				
				switch (scheduledFee.getString("scheduled_fee_overdue_reminder_frequency").toLowerCase()) {
					case "days":
						break;
						
					case "weekly":
						
						selectDayOfWeek(driver, step, scheduledFee.getJSONArray("scheduled_fee_overdue_reminder_days_of_week"));
						break;
						
					case "monthly":
						
						Select incomeBookingFrequencySubtype = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentSubType"))).get(1));
						incomeBookingFrequencySubtype.selectByVisibleText(scheduledFee.getString("scheduled_fee_overdue_reminder_sub_type"));
						
						if(scheduledFee.getString("scheduled_fee_overdue_reminder_sub_type").toLowerCase().equals("day of month")) {
							
							driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentDays"))).findElement(By.tagName("input")).click();
							utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentDays"))).findElement(By.tagName("ul")), scheduledFee.getJSONArray("scheduled_fee_overdue_reminder_days"));
						}
						else if(scheduledFee.getString("scheduled_fee_overdue_reminder_sub_type").toLowerCase().equals("weekday(s)")) {
							
							Select incomeBookingFrequencyWeek = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentWeek"))).get(1));
							incomeBookingFrequencyWeek.selectByVisibleText(scheduledFee.getString("scheduled_fee_overdue_reminder_week"));
							
							selectDayOfWeek(driver, step, scheduledFee.getJSONArray("scheduled_fee_overdue_reminder_days_of_week"));					
						}
						break;
						
					case "yearly":
						
						Select incomeBookingFrequencyYearlySubtype = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentSubType"))).get(1));
						incomeBookingFrequencyYearlySubtype.selectByVisibleText(scheduledFee.getString("scheduled_fee_overdue_reminder_sub_type"));
						
						if(scheduledFee.getString("scheduled_fee_overdue_reminder_sub_type").toLowerCase().equals("day of month")) {
							
							Select incomeBookingFrequencyMonth = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingOverdueMonth"))).findElement(By.tagName("select")));
							incomeBookingFrequencyMonth.selectByVisibleText(scheduledFee.getString("scheduled_fee_overdue_reminder_month"));
							
							driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentDays"))).findElement(By.tagName("input")).click();
							utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentDays"))).findElement(By.tagName("ul")), scheduledFee.getJSONArray("scheduled_fee_overdue_reminder_days"));
						}
						else if(scheduledFee.getString("scheduled_fee_overdue_reminder_sub_type").toLowerCase().equals("weekday(s)")) {
							Select incomeBookingFrequencyMonth = new Select(driver.findElement(By.id(newDealLocator.getString("select-IncomeBookingOverdueMonth"))).findElement(By.tagName("select")));
							incomeBookingFrequencyMonth.selectByVisibleText(scheduledFee.getString("scheduled_fee_overdue_reminder_month"));
							
							Select incomeBookingFrequencyWeek = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentWeek"))).get(1));
							incomeBookingFrequencyWeek.selectByVisibleText(scheduledFee.getString("scheduled_fee_overdue_reminder_week"));
							
							selectDayOfWeek(driver, step, scheduledFee.getJSONArray("scheduled_fee_overdue_reminder_days_of_week"));					
						}
						break;
					
					default:
						step.log(Status.FAIL, "Invalid fee frequency: "+scheduledFee.getString("scheduled_fee_frequency"));
						throw new Exception("Invalid fee frequency: "+scheduledFee.getString("scheduled_fee_frequency"));
					}
			}
			
			if(scheduledFee.has("scheduled_fee_reminder_contacts")) {
				driver.findElement(By.id(newDealLocator.getString("button-IncomeBookingReminderContacts"))).click();
				
				availableContacts = driver.findElements(By.xpath(newDealLocator.getString("label-ContactName")));
				actualContactList = new ArrayList<String>();
				for(WebElement contact : availableContacts) {
					actualContactList.add(contact.getText());
				}
				step.log(Status.INFO, "Contacts available at Income Booking - Expected: "+expectedContactList.toString()+" Actual: "+actualContactList.toString()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);				
				
				setContacts(driver, step, scheduledFee.getJSONArray("scheduled_fee_reminder_contacts"));
			}
			
			driver.findElement(By.xpath(newDealLocator.getString("button-IncomeBookingBasicNext"))).click();
			
			if(!utility.isElementHiddenNow(driver, By.id(newDealLocator.getString("label-Title")))) {					
				actualResponseMessages.add(utility.extractTitleMessage(driver, step, By.id(newDealLocator.getString("label-Title"))));
				driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealOK"))).click();				
			}
			utility.waitForProgressBarToLoad(driver);
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding Income booking");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Income booking");
		}
	}
	
	private void addScheduledFees(WebDriver driver, ExtentTest step,  JSONArray scheduledFees) throws Exception {
		
		Iterator<Object> scheduledFeeIterator = scheduledFees.iterator();
		
		driver.findElement(By.id(newDealLocator.getString("tab-ScheduledFeesWizard"))).click();
		
		while(scheduledFeeIterator.hasNext()) {	
			JSONObject scheduledFee = (JSONObject) scheduledFeeIterator.next();
			try {				
				driver.findElement(By.id(newDealLocator.getString("button-NewScheduledFees"))).click();
				
				switch (scheduledFee.getString("scheduled_fees_type").toLowerCase()) {
				case "income booking":
					Thread.sleep(500);
					driver.findElement(By.id(newDealLocator.getString("button-IncomeBooking"))).click();					
					addIncomeBooking(driver, step, scheduledFee);
					break;

				default:
					step.log(Status.FAIL, "Invalid Scheduled Fees Type: "+scheduledFee.getString("scheduled_fees_type"));
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Invalid Scheduled Fees Type: "+scheduledFee.getString("scheduled_fees_type"));					
				}
				
				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding scheduled fees of type: "+scheduledFee.getString("scheduled_fees_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding scheduled fees of type: "+scheduledFee.getString("scheduled_fees_type"));
			}
			driver.findElement(By.xpath(newDealLocator.getString("button-ScheduledBack"))).click();
		}
	}
	
	private void addEntitlementDeal(WebDriver driver, ExtentTest step,  String dealName, JSONObject entitlement) throws Exception {
		
		try {
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-EntitlementTab"))), "DEAL");
			utility.addToExpectedDetails(expectedDealDetails, dealName.toLowerCase().trim(), "entitlement:"+entitlement.getJSONArray("entitlement_initiating_contacts").toList().toString());
			driver.findElement(By.id(newDealLocator.getString("button-NewEntitlement"))).click();
			
			Select entitlementCurrency = new Select(driver.findElement(By.xpath(newDealLocator.getString("select-EntitlementCurrency"))));
			entitlementCurrency.selectByVisibleText(entitlement.getString("entitlement_currency"));
			
			if(entitlement.has("entitlement_ranges_from")) {
				driver.findElement(By.id(newDealLocator.getString("input-EntitlementRangesFrom"))).sendKeys(entitlement.getString("entitlement_ranges_from"));
			}
			
			if(entitlement.has("entitlement_ranges_to")) {
				driver.findElement(By.id(newDealLocator.getString("input-EntitlementRangesTo"))).sendKeys(entitlement.getString("entitlement_ranges_to"));
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-EntitlementAddInitiatingContact"))).click();
			setContacts(driver, step, entitlement.getJSONArray("entitlement_initiating_contacts"));
			
			
			
			if(entitlement.has("entitlement_authorizing_contacts")) {
				driver.findElement(By.id(newDealLocator.getString("button-EntitlementAddAuthorizingContact"))).click();
				setContacts(driver, step, entitlement.getJSONArray("entitlement_authorizing_contacts"));
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-AddEntitlement"))).click();
			step.log(Status.INFO, "Transation Initiating Contacts: "+entitlement.getJSONArray("entitlement_initiating_contacts").toList().toString()+" added to deal");
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding Deal Entitlement");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Deal Entitlement");
		}
	}
	
	private void addEntitlementAccount(WebDriver driver, ExtentTest step,  JSONObject entitlement) throws Exception {
		try {
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-EntitlementTab"))), "ACCOUNT");			
			utility.addToExpectedDetails(expectedAccountDetails,entitlement.getString("entitlement_source_account").split("-")[0].toLowerCase().trim(), "entitlement:"+entitlement.getJSONArray("entitlement_initiating_contacts").toList().toString());
			driver.findElement(By.id(newDealLocator.getString("button-NewEntitlement"))).click();
			
			driver.findElement(By.id(newDealLocator.getString("select-EntitlementSourceAccount"))).findElement(By.tagName("input")).click();
			List<String> actualSourceAccounts = utility.extractOptionsFromSelect(driver, step, driver.findElement(By.id(newDealLocator.getString("select-EntitlementSourceAccount"))).findElement(By.tagName("ul")));
			step.log(Status.INFO, "Source Accounts available at Entitlement Account - Expected: "+expectedSourceAccounts.toString()+" Actual: "+actualSourceAccounts.toString());
			report.addStepInfoScreenshot(driver, step);	
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-EntitlementSourceAccount"))).findElement(By.tagName("ul")), entitlement.getString("entitlement_source_account"));
			
			
			if(entitlement.has("entitlement_ranges_from")) {
				driver.findElement(By.id(newDealLocator.getString("input-EntitlementRangesFrom"))).sendKeys(entitlement.getString("entitlement_ranges_from"));
			}
			
			if(entitlement.has("entitlement_ranges_to")) {
				driver.findElement(By.id(newDealLocator.getString("input-EntitlementRangesTo"))).sendKeys(entitlement.getString("entitlement_ranges_to"));
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-EntitlementAddInitiatingContact"))).click();
			setContacts(driver, step, entitlement.getJSONArray("entitlement_initiating_contacts"));
			
			if(entitlement.has("entitlement_authorizing_contacts")) {
				driver.findElement(By.id(newDealLocator.getString("button-EntitlementAddAuthorizingContact"))).click();
				setContacts(driver, step, entitlement.getJSONArray("entitlement_authorizing_contacts"));
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-AddEntitlement"))).click();
			step.log(Status.INFO, "Transation Initiating Contacts: "+entitlement.getJSONArray("entitlement_initiating_contacts").toList().toString()+" added to account");
			//List<String> entitlementInitiatingContacts = utility.extractColumnsFromTable(By.id(newDealLocator.getString("table-EntitlementList")), 2);
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding Account Entitlement");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Account Entitlement");
		}
	}
	
	private void addEntitlements(WebDriver driver, ExtentTest step,  String dealName, JSONArray entitlements) throws Exception {
		Iterator<Object> entitlementIterator = entitlements.iterator();
		
		driver.findElement(By.id(newDealLocator.getString("tab-EntitlementWizard"))).click();
		
		while(entitlementIterator.hasNext()) {
			JSONObject entitlement = (JSONObject) entitlementIterator.next();
			try {
				if(entitlement.getString("entitlement_type").toLowerCase().equals("deal")) {
					addEntitlementDeal(driver, step, dealName, entitlement);					
				}
				else if(entitlement.getString("entitlement_type").toLowerCase().equals("account")) {
					addEntitlementAccount(driver, step, entitlement);
				}				
			}	
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Entitlements");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Entitlements");
			}
		}
	}
	
	private void addBudgetConsolidated(WebDriver driver, ExtentTest step,  JSONObject budget) throws Exception {
		
		try {
			driver.findElement(By.id(newDealLocator.getString("button-BudgetTypeConsolidated"))).click();
			
			if(budget.getBoolean("budget_carry_forward")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-BudgetCarryForward"))).findElement(By.tagName("span")).click();
			}			
			
			Select budgetInterval = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetInterval"))).get(1));
			budgetInterval.selectByVisibleText(budget.getString("budget_interval"));
			
			Select budgetDuration = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetDuration"))).get(1));
			budgetDuration.selectByVisibleText(budget.getString("budget_duration"));
			
			driver.findElement(By.id(newDealLocator.getString("input-BudgetAllocated"))).sendKeys(budget.getString("budget_allocated_amount"));
			
//			List<WebElement> budgetDatePicker = driver.findElements(By.id(newDealLocator.getString("input-BudgetDatePicker")));
//			
//			budgetDatePicker.get(0).clear();
//			budgetDatePicker.get(0).sendKeys(budget.getString("budget_starts_on"));
//			
//			budgetDatePicker.get(1).clear();
//			budgetDatePicker.get(1).sendKeys(budget.getString("budget_ends_on"));
			
			driver.findElement(By.id(newDealLocator.getString("button-AddBudget"))).click();			
		}
		catch (Exception e) {			
			step.log(Status.FAIL, "Error while adding Budget - Consolidated");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Budget - Consolidated");
		}
	}

	private void addBudgetPurpose(WebDriver driver, ExtentTest step,  JSONObject budget) throws Exception {
		
		try {
			driver.findElement(By.id(newDealLocator.getString("button-BudgetTypePurpose"))).click();
			
			if(budget.getBoolean("budget_carry_forward")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-BudgetCarryForward"))).findElement(By.tagName("span")).click();
			}
			
			driver.findElement(By.id(newDealLocator.getString("input-BudgetPurpose"))).sendKeys(budget.getString("budget_purpose"));
			
			Select budgetInterval = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetInterval"))).get(1));
			budgetInterval.selectByVisibleText(budget.getString("budget_interval"));
			
			Select budgetDuration = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetDuration"))).get(1));
			budgetDuration.selectByVisibleText(budget.getString("budget_duration"));
			
			driver.findElement(By.id(newDealLocator.getString("input-BudgetAllocated"))).sendKeys(budget.getString("budget_allocated_amount"));
			
//			List<WebElement> budgetDatePicker = driver.findElements(By.id(newDealLocator.getString("input-BudgetDatePicker")));
//			
//			budgetDatePicker.get(0).clear();
//			budgetDatePicker.get(0).sendKeys(budget.getString("budget_starts_on"));
//			
//			budgetDatePicker.get(1).clear();
//			budgetDatePicker.get(1).sendKeys(budget.getString("budget_ends_on"));
			
			driver.findElement(By.id(newDealLocator.getString("button-AddBudget"))).click();			
		}
		catch (Exception e) {			
			step.log(Status.FAIL, "Error while adding Budget - Purpose: "+budget.getString("budget_purpose"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Budget - Purpose: "+budget.getString("budget_purpose"));
		}
	}
	
	private void addBudgetDestination(WebDriver driver, ExtentTest step,  JSONObject budget) throws Exception {
		
		try {
			driver.findElement(By.id(newDealLocator.getString("button-BudgetTypeDestination"))).click();
			
			if(budget.getBoolean("budget_carry_forward")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-BudgetCarryForward"))).findElement(By.tagName("span")).click();
			}
			
			driver.findElement(By.id(newDealLocator.getString("input-BudgetDestination"))).sendKeys(budget.getString("budget_destination"));
			List<String> actualSourceAccounts = utility.extractOptionsFromSelect(driver, step, driver.findElement(By.xpath(newDealLocator.getString("select-BudgetDestination"))));
			step.log(Status.INFO, "Source Account available at Budget Destination: "+actualSourceAccounts.toString());
			report.addStepInfoScreenshot(driver, step);	
			
			Select budgetInterval = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetInterval"))).get(1));
			budgetInterval.selectByVisibleText(budget.getString("budget_interval"));
			
			Select budgetDuration = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetDuration"))).get(1));
			budgetDuration.selectByVisibleText(budget.getString("budget_duration"));
			
			driver.findElement(By.id(newDealLocator.getString("input-BudgetAllocated"))).sendKeys(budget.getString("budget_allocated_amount"));
			
//			List<WebElement> budgetDatePicker = driver.findElements(By.id(newDealLocator.getString("input-BudgetDatePicker")));
//			
//			budgetDatePicker.get(0).clear();
//			budgetDatePicker.get(0).sendKeys(budget.getString("budget_starts_on"));
//			
//			budgetDatePicker.get(1).clear();
//			budgetDatePicker.get(1).sendKeys(budget.getString("budget_ends_on"));
			
			driver.findElement(By.id(newDealLocator.getString("button-AddBudget"))).click();			
			
		}
		catch (Exception e) {			
			step.log(Status.FAIL, "Error while adding Budget - Destination: "+budget.getString("budget_destination"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Budget - Destination: "+budget.getString("budget_destination"));
		}
	}
	
	private void addBudgetPurposeAndDestination(WebDriver driver, ExtentTest step,  JSONObject budget) throws Exception {
		try {
			driver.findElement(By.id(newDealLocator.getString("button-BudgetTypePurposeAndDestination"))).click();
			
			if(budget.getBoolean("budget_carry_forward")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-BudgetCarryForward"))).findElement(By.tagName("span")).click();
			}
			
			driver.findElement(By.id(newDealLocator.getString("input-BudgetPurpose"))).sendKeys(budget.getString("budget_purpose"));
						
			driver.findElement(By.id(newDealLocator.getString("input-BudgetDestination"))).sendKeys(budget.getString("budget_destination"));
			List<String> actualSourceAccounts = utility.extractOptionsFromSelect(driver, step, driver.findElement(By.xpath(newDealLocator.getString("select-BudgetDestination"))));
			step.log(Status.INFO, "Source Account available at Budget Purpose and Destination: "+actualSourceAccounts.toString());
			report.addStepInfoScreenshot(driver, step);	
			
			Select budgetInterval = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetInterval"))).get(1));
			budgetInterval.selectByVisibleText(budget.getString("budget_interval"));
			
			Select budgetDuration = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetDuration"))).get(1));
			budgetDuration.selectByVisibleText(budget.getString("budget_duration"));
			
			driver.findElement(By.id(newDealLocator.getString("input-BudgetAllocated"))).sendKeys(budget.getString("budget_allocated_amount"));
			
//			List<WebElement> budgetDatePicker = driver.findElements(By.id(newDealLocator.getString("input-BudgetDatePicker")));
//			
//			budgetDatePicker.get(0).clear();
//			budgetDatePicker.get(0).sendKeys(budget.getString("budget_starts_on"));
//			
//			budgetDatePicker.get(1).clear();
//			budgetDatePicker.get(1).sendKeys(budget.getString("budget_ends_on"));
			
			driver.findElement(By.id(newDealLocator.getString("button-AddBudget"))).click();
			
		}
		catch (Exception e) {			
			step.log(Status.FAIL, "Error while adding Budget - Purpose: "+budget.getString("budget_purpose")+" and Destination: "+budget.getString("budget_destination"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Budget - Purpose: "+budget.getString("budget_purpose")+" and Destination: "+budget.getString("budget_destination"));
		}
	}
	
	protected void addBudgetGroups(WebDriver driver, ExtentTest step,  JSONArray budgetGroups) throws Exception {
		
		Iterator<Object> budgetGroupIterator = budgetGroups.iterator();
		
		driver.findElement(By.id(newDealLocator.getString("tab-BudgetWizard"))).click();
		while(budgetGroupIterator.hasNext()) {
			JSONObject budgetGroup = (JSONObject) budgetGroupIterator.next();
			try {				
				utility.addToExpectedDetails(expectedAccountDetails,budgetGroup.getString("budget_group_source_account").split("-")[0].toLowerCase().trim(), "name:"+budgetGroup.getString("budget_group_name").toLowerCase()+";"+"source:"+budgetGroup.getString("budget_group_source_account").split("-")[0].toLowerCase().trim()+";"+"budgets configured: "+budgetGroup.getJSONArray("budgets").length());
				
				driver.findElement(By.id(newDealLocator.getString("button-NewBudgetGroup"))).click();
				
				driver.findElement(By.id(newDealLocator.getString("input-BudgetGroupName"))).sendKeys(budgetGroup.getString("budget_group_name"));
				
				Thread.sleep(500);
				driver.findElement(By.id(newDealLocator.getString("select-BudgetGroupSourceAccount"))).findElement(By.tagName("input")).click();
				List<String> actualSourceAccounts = utility.extractOptionsFromSelect(driver, step, driver.findElement(By.id(newDealLocator.getString("select-BudgetGroupSourceAccount"))).findElement(By.tagName("ul")));
				step.log(Status.INFO, "Source Accounts available at Budget Group - Expected: "+expectedSourceAccounts.toString()+" Actual: "+actualSourceAccounts.toString());
				report.addStepInfoScreenshot(driver, step);				
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-BudgetGroupSourceAccount"))).findElement(By.tagName("ul")), budgetGroup.getString("budget_group_source_account"));
				
				Select budgetGroupFiscalMonth = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetGroupFiscalMonth"))).get(1));
				budgetGroupFiscalMonth.selectByVisibleText(budgetGroup.getString("budget_group_fiscal_month"));
				
				Select budgetGroupFiscalYear = new Select(driver.findElements(By.id(newDealLocator.getString("select-BudgetGroupFiscalYear"))).get(1));
				budgetGroupFiscalYear.selectByVisibleText(budgetGroup.getString("budget_group_fiscal_year"));
				
				driver.findElement(By.id(newDealLocator.getString("button-AddBudgetGroup"))).click();				
				
				Iterator<Object> budgetIterator = budgetGroup.getJSONArray("budgets").iterator();
				int incrementor = 0;
				while(budgetIterator.hasNext()) {
					JSONObject budget = (JSONObject) budgetIterator.next();
					try {							
						if(incrementor == 0) {
							driver.findElement(By.id(newDealLocator.getString("button-NewBudget"))).findElement(By.tagName("i")).click();
						}
						else {
							driver.findElement(By.id(newDealLocator.getString("button-SecondBudget"))).click();
						}
						incrementor ++;						
						switch (budget.getString("budget_type").toLowerCase()) {
							case "consolidated":
								addBudgetConsolidated(driver, step, budget);
								break;
							case "purpose":
								addBudgetPurpose(driver, step, budget);
								break;
							case "destination":
								addBudgetDestination(driver, step, budget);
								break;
							case "purpose and destination":
								addBudgetPurposeAndDestination(driver, step, budget);
								break;	
							default:
								step.log(Status.FAIL, "Invalid budget type: "+budget.getString("budget_type"));
								throw new Exception("Invalid budget type: "+budget.getString("budget_type"));
						}						
						
					}
					catch (Exception e) {
						step.log(Status.FAIL, "Error while adding Budget: "+budget.getString("budget_type"));
						report.addStepFailScreenshot(driver, step);
						step.log(Status.FAIL, e.getMessage());
						throw new Exception("Error while adding Budget: "+budget.getString("budget_type"));
					}
				}	
				step.log(Status.INFO, "Budget Group: "+budgetGroup.getString("budget_group_name")+" added to deal");	
				
				driver.findElement(By.id(newDealLocator.getString("button-BudgetBack"))).click();
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Budget Group: "+budgetGroup.getString("budget_group_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Budget Group: "+budgetGroup.getString("budget_group_name"));
			}			
			
		}
	}
	
	private void selectDayOfWeek(WebDriver driver, ExtentTest step,  JSONArray daysOfWeek) {
		
		Map<String, Integer> dayOfWeek = Map.of(
			    "Sunday", 0,
			    "Monday", 1,
			    "Tuesday", 2,
			    "Wednesday", 3,
			    "Thursday", 4,
			    "Friday", 5,
			    "Saturday", 6
			);
		
		Iterator<Object> daysOfWeekIterator = daysOfWeek.iterator();
		
		while(daysOfWeekIterator.hasNext()) {
			String day = (String) daysOfWeekIterator.next();
			driver.findElement(By.id(newDealLocator.getString("card-ScheduledPaymentWeekSelect"))).findElement(By.tagName("div")).findElements(By.tagName("div")).get(dayOfWeek.get(day)).click();
		}
		
	}
	
	private void addBTSubInstruction(WebDriver driver, ExtentTest step,  WebElement datePicker, JSONObject subInstruction) throws Exception {
		
		try {
			
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentDebitAccountBICCode"))).sendKeys(subInstruction.getString("sub_instruction_debit_account_BIC_code"));
			
			Select subInstructionTransactionChargesBearedBy = new Select(driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentTransactionChargesBearedBy"))));
			subInstructionTransactionChargesBearedBy.selectByVisibleText(subInstruction.getString("sub_instruction_transaction_charges_beared_by"));
			
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentAmount"))).sendKeys(subInstruction.getString("sub_instruction_amount"));
			
			Select subInstructionPaymentCurrency = new Select(driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentCurrency"))));
			subInstructionPaymentCurrency.selectByVisibleText(subInstruction.getString("sub_instruction_currency"));
			
			datePicker.sendKeys(subInstruction.getString("sub_instruction_value_date"));
			
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentBeneficiaryName"))).sendKeys(subInstruction.getString("sub_instruction_beneficiary_name"));
			
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentBeneficiaryAddress"))).sendKeys(subInstruction.getString("sub_instruction_beneficiary_address"));
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding BT sub instruction");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding BT sub instruction");
		}
		
				
	}
	
	
	private void addScheduledPaymentRetentionSurplus(WebDriver driver, ExtentTest step,  JSONObject scheduledInstruction) throws Exception {
		try {
			
			utility.addToExpectedDetails(expectedAccountDetails,scheduledInstruction.getString("scheduled_payment_source_account").split("-")[0].toLowerCase().trim(), "name:"+scheduledInstruction.getString("scheduled_payment_name").toLowerCase().trim()+";"+"purpose:"+scheduledInstruction.getString("scheduled_payment_purpose").toLowerCase().trim()+";"+"type:payment-retention-surplus");
			
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentName"))).clear();
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentName"))).sendKeys(scheduledInstruction.getString("scheduled_payment_name"));
			
			Select scheduledPaymentPurpose = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentPurpose"))).get(1));
			scheduledPaymentPurpose.selectByVisibleText(scheduledInstruction.getString("scheduled_payment_purpose"));
			
			Thread.sleep(500);
			driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentSourceAccount"))).findElement(By.tagName("input")).click();
			List<String> actualSourceAccounts = utility.extractOptionsFromSelect(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentSourceAccount"))).findElement(By.tagName("ul")));
			step.log(Status.INFO, "Source Accounts available at Scheduled Instruction - Expected: "+expectedSourceAccounts.toString()+" Actual: "+actualSourceAccounts.toString());
			report.addStepInfoScreenshot(driver, step);
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentSourceAccount"))).findElement(By.tagName("ul")), scheduledInstruction.getString("scheduled_payment_source_account"));
			
			Select scheduledPaymentBalanceConsideration = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentBalanceConsideration"))).get(1));
			scheduledPaymentBalanceConsideration.selectByVisibleText(scheduledInstruction.getString("scheduled_payment_balance_consideration"));
			
			if(scheduledInstruction.getBoolean("scheduled_payment_split")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentSplit"))).click();
				
				Select scheduledPaymentSpecifyAmountAs = new Select(driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentSpecifyAmountAs"))).findElement(By.tagName("select")));
				scheduledPaymentSpecifyAmountAs.selectByVisibleText(scheduledInstruction.getString("scheduled_payment_specify_amount_as"));
				
				driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentValue"))).sendKeys(scheduledInstruction.getString("scheduled_payment_value"));
			}
			
			else if(scheduledInstruction.getBoolean("scheduled_payment_partial_payment")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentPartialPayment"))).findElement(By.tagName("span")).click();
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentBasicDetailsNext"))).click();
			
			if(scheduledInstruction.getBoolean("scheduled_payment_sweep_in")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentSweepIn"))).findElement(By.tagName("div")).click();
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentSweepInNext"))).click();
			
			if(scheduledInstruction.getBoolean("scheduled_payment_repeating")) {				
				
				driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentStartDate"))).findElement(By.tagName("input")).sendKeys(scheduledInstruction.getString("scheduled_payment_start_date"));				
				driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentEndDate"))).findElement(By.tagName("input")).sendKeys(scheduledInstruction.getString("scheduled_payment_end_date"));
			
				if(scheduledInstruction.getBoolean("scheduled_payment_all_sub_instructions")) {				
					
					Select scheduledInstructionFrequency = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentFrequency"))).get(1));
					scheduledInstructionFrequency.selectByVisibleText(scheduledInstruction.getString("scheduled_payment_frequency"));
					
					driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRepeatEvery"))).clear();
					driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRepeatEvery"))).sendKeys(scheduledInstruction.getString("scheduled_payment_repeat_every"));
					
					switch (scheduledInstruction.getString("scheduled_payment_frequency").toLowerCase()) {
						case "days":
							break;
							
						case "weekly":
							
							selectDayOfWeek(driver, step, scheduledInstruction.getJSONArray("scheduled_payment_days_of_week"));
							break;
							
						case "monthly":
							
							Select scheduledInstructionFrequencySubtype = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentSubType"))).get(1));
							scheduledInstructionFrequencySubtype.selectByVisibleText(scheduledInstruction.getString("scheduled_payment_sub_type"));
							
							if(scheduledInstruction.getString("scheduled_payment_sub_type").toLowerCase().equals("day of month")) {
								
								driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentDays"))).findElement(By.tagName("input")).click();
								utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentDays"))).findElement(By.tagName("ul")), scheduledInstruction.getJSONArray("scheduled_payment_days"));
							}
							else if(scheduledInstruction.getString("scheduled_payment_sub_type").toLowerCase().equals("weekday(s)")) {
								
								Select scheduledInstructionFrequencyWeek = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentWeek"))).get(1));
								scheduledInstructionFrequencyWeek.selectByVisibleText(scheduledInstruction.getString("scheduled_payment_week"));
								
								selectDayOfWeek(driver, step, scheduledInstruction.getJSONArray("scheduled_payment_days_of_week"));					
							}
							break;
						
						default:
							step.log(Status.FAIL, "Invalid payment frequency: "+scheduledInstruction.getString("scheduled_payment_frequency"));
							throw new Exception("Invalid payment frequency: "+scheduledInstruction.getString("scheduled_payment_frequency"));
					}
				}
				else {
					driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentAllSubInstructions"))).findElement(By.tagName("input")).click();
				}
			}
			else {
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentRepeating"))).findElement(By.tagName("span")).click();
				driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentExecutionDate"))).findElement(By.tagName("span")).sendKeys(scheduledInstruction.getString("scheduled_payment_execution_date"));
			}
			
			Select scheduledInstructionScheduledAt = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentScheduledAt"))).get(1));
			scheduledInstructionScheduledAt.selectByVisibleText(scheduledInstruction.getString("scheduled_payment_scheduled_at"));
			
			Select scheduledInstructionHolidayAction = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentHolidayAction"))).get(1));
			scheduledInstructionHolidayAction.selectByVisibleText(scheduledInstruction.getString("scheduled_payment_holiday_action"));
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentScheduleNext"))).click();
			
			
			if(scheduledInstruction.has("scheduled_payment_sub_instructions")) {
				
				Iterator<Object> paymentSubInstructionsIterator = scheduledInstruction.getJSONArray("scheduled_payment_sub_instructions").iterator();
				
				driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentBudget"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentBudget"))).findElement(By.tagName("ul")), scheduledInstruction.getString("scheduled_instruction_budget"));
				
				if(scheduledInstruction.getBoolean("scheduled_payment_split") && scheduledInstruction.getBoolean("scheduled_instruction_split_by_percentage")) {
					driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentSplitByPercentage"))).findElement(By.tagName("span")).click();
				}
				
				while(paymentSubInstructionsIterator.hasNext()) {
					
					JSONObject paymentSubInstruction = (JSONObject) paymentSubInstructionsIterator.next();
					
					driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentInstrument"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentInstrument"))).findElement(By.tagName("ul")), paymentSubInstruction.getString("sub_instruction_instrument"));
					
					
					Thread.sleep(500);
					if(!utility.isElementHiddenNow(driver, By.id(newDealLocator.getString("select-ScheduledPaymentBudgetPurpose")))){
						driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentBudgetPurpose"))).findElement(By.tagName("input")).click();
						utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentBudgetPurpose"))).findElement(By.tagName("ul")), paymentSubInstruction.getString("sub_instruction_budget_purpose"));
					}
					
					if(!scheduledInstruction.getBoolean("scheduled_payment_all_sub_instructions")) {
						driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentDate"))).findElement(By.tagName("input")).sendKeys("sub_instruction_date");
					}
					
					switch (paymentSubInstruction.getString("sub_instruction_instrument").toLowerCase()) {
					case "bt":
						addBTSubInstruction(driver, step, driver.findElements(By.id(newDealLocator.getString("input-ScheduledPaymentValueDate"))).get(2), paymentSubInstruction);
						break;

					default:
						break;
					}		
					
					driver.findElement(By.id(newDealLocator.getString("button-AddScheduledPaymentSubInstruction"))).click();
				}
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentSubInstructionNext"))).click();
			
			if(scheduledInstruction.getBoolean("scheduled_payment_enable_auto_retry")) {
				
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentEnableAutoRetry"))).findElement(By.tagName("div")).click();
				
				driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentRetryType"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentRetryType"))).findElement(By.tagName("ul")), scheduledInstruction.getString("scheduled_payment_retry_type"));
				
				if(scheduledInstruction.getString("scheduled_payment_retry_type").toLowerCase().equals("same day")) {
					
					driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRetryHours"))).sendKeys(scheduledInstruction.getString("scheduled_payment_retry_hours"));
				}
				
				else if(scheduledInstruction.getString("scheduled_payment_retry_type").toLowerCase().equals("custom")) {
					
					driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRetryDays"))).sendKeys(scheduledInstruction.getString("scheduled_payment_retry_days"));
				}
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentRetryNext"))).click();
			
			if(scheduledInstruction.getBoolean("scheduled_payment_enable_notification_alerts")) {
				
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentNotificationAlerts"))).findElement(By.tagName("div")).click();
				
				driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentNotificationMode"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentNotificationMode"))).findElement(By.tagName("ul")), scheduledInstruction.getString("scheduled_payment_notification_mode"));
				
				driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentNotificationAddContact"))).click();
				
				List<WebElement> availableContacts = driver.findElements(By.xpath(newDealLocator.getString("label-ContactName")));
				List<String> actualContactList = new ArrayList<String>();
				for(WebElement contact : availableContacts) {
					actualContactList.add(contact.getText());
				}
				step.log(Status.INFO, "Contacts available at Scheduled Instruction - Expected: "+expectedContactList.toString()+" Actual: "+actualContactList.toString()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);				
				
				setContacts(driver, step, scheduledInstruction.getJSONArray("scheduled_payment_notification_contacts"));
				
				driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentNotificationNext"))).click();
			}
			
			//Retention TAB
			Thread.sleep(500);
			driver.findElement(By.xpath(newDealLocator.getString("tab-ScheduledRetentionTab"))).click();
			
			Select scheduledRetentionPurpose = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledRetentionPurpose"))).get(1));
			scheduledRetentionPurpose.selectByVisibleText(scheduledInstruction.getString("scheduled_retention_purpose"));
			
			driver.findElement(By.id(newDealLocator.getString("select-ScheduledRetentionExecute"))).findElement(By.tagName("input")).click();
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledRetentionExecute"))).findElement(By.tagName("ul")), scheduledInstruction.getString("scheduled_retention_execute"));
			
			if(scheduledInstruction.getString("scheduled_retention_execute").toLowerCase().equals("a few days prior")) {
				
				driver.findElement(By.id(newDealLocator.getString("input-ScheduledRetentionNoOfDays"))).sendKeys(scheduledInstruction.getString("scheduled_retention_noofdays"));
				}
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledRetentionBasicDetailsNext"))).click();
			Thread.sleep(1000);
			
			// surplus TAB
			
			driver.findElement(By.xpath(newDealLocator.getString("tab-ScheduledSurplusTab"))).click();
			
			Select scheduledSurplusPurpose = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledSurplusPurpose"))).get(1));
			scheduledSurplusPurpose.selectByVisibleText(scheduledInstruction.getString("scheduled_surplus_purpose"));
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledSurplusBasicsNext"))).click();
			
			if(scheduledInstruction.has("scheduled_surplus_sub_instructions")) {
				
				Iterator<Object> surplusSubInstructionsIterator = scheduledInstruction.getJSONArray("scheduled_surplus_sub_instructions").iterator();
				
				while(surplusSubInstructionsIterator.hasNext()) {
					
					JSONObject surplusSubInstruction = (JSONObject) surplusSubInstructionsIterator.next();
					
					driver.findElement(By.id(newDealLocator.getString("select-ScheduledSurplusInstrument"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledSurplusInstrument"))).findElement(By.tagName("ul")), surplusSubInstruction.getString("sub_instruction_instrument"));
					
					driver.findElement(By.id(newDealLocator.getString("select-ScheduledSurplusBudget"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledSurplusBudget"))).findElement(By.tagName("ul")), surplusSubInstruction.getString("sub_instruction_budget"));
					
					Thread.sleep(500);
					driver.findElement(By.id(newDealLocator.getString("select-ScheduledSurplusBudgetPurpose"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledSurplusBudgetPurpose"))).findElement(By.tagName("ul")), surplusSubInstruction.getString("sub_instruction_budget_purpose"));
					
					switch (surplusSubInstruction.getString("sub_instruction_instrument").toLowerCase()) {
					case "bt":
						addBTSubInstruction(driver, step, driver.findElements(By.id(newDealLocator.getString("input-ScheduledPaymentValueDate"))).get(2), surplusSubInstruction);
						break;

					default:
						break;
					}		
					
					driver.findElement(By.id(newDealLocator.getString("button-AddScheduledSurplusSubInstruction"))).click();
				}
			}			
			step.log(Status.INFO, "Scheduled Instruction of Type: Payment-Retention-Surplus added to deal");			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding Scheduled Instruction of Type : Payment-Retention-Surplus");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Scheduled Instruction of Type : Payment-Retention-Surplus");
		}
		
	}
	
	
	private void addScheduledInstructions(WebDriver driver, ExtentTest step,  JSONArray scheduledInstructions) throws Exception {
		
		Iterator<Object> scheduledInstructionIterator = scheduledInstructions.iterator();
		
		driver.findElement(By.id(newDealLocator.getString("tab-ScheduledWizard"))).click();
		
		while(scheduledInstructionIterator.hasNext()) {
			JSONObject scheduledInstruction = (JSONObject) scheduledInstructionIterator.next();
			try {				
				driver.findElement(By.id(newDealLocator.getString("button-NewScheduledInstruction"))).click();
				
				switch (scheduledInstruction.getString("scheduled_type").toLowerCase()) {
				case "payment-retention-surplus":
					Thread.sleep(500);
					driver.findElement(By.id(newDealLocator.getString("card-ScheduledPaymentRetentionSurplus"))).click();
					driver.findElement(By.id(newDealLocator.getString("button-ScheduledTypeProceed"))).click();
					addScheduledPaymentRetentionSurplus(driver, step, scheduledInstruction);
					break;

				default:
					break;
				}				
				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Scheduled Instruction of type: "+scheduledInstruction.getString("scheduled_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Scheduled Instruction of type: "+scheduledInstruction.getString("scheduled_type"));
			}
			driver.findElement(By.xpath(newDealLocator.getString("button-ScheduledBack"))).click();
		}
	}
	
	private void addLinkedPayment(WebDriver driver, ExtentTest step,  JSONObject linkedInstruction) throws Exception {
		try {
			utility.addToExpectedDetails(expectedAccountDetails, linkedInstruction.getString("linked_payment_source_account").split("-")[0].toLowerCase().trim(), "name:"+linkedInstruction.getString("linked_payment_name").toLowerCase().trim()+";"+"purpose:"+linkedInstruction.getString("linked_payment_purpose").toLowerCase().trim()+";"+"type:payment");
			
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentName"))).clear();
			driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentName"))).sendKeys(linkedInstruction.getString("linked_payment_name"));
			
			Select linkedPaymentPurpose = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentPurpose"))).get(1));
			linkedPaymentPurpose.selectByVisibleText(linkedInstruction.getString("linked_payment_purpose"));
			
			Thread.sleep(500);
			driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentSourceAccount"))).findElement(By.tagName("input")).click();
			List<String> actualSourceAccounts = utility.extractOptionsFromSelect(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentSourceAccount"))).findElement(By.tagName("ul")));
			step.log(Status.INFO, "Source Accounts available at Linked Instruction - Expected: "+expectedSourceAccounts.toString()+" Actual: "+actualSourceAccounts.toString());
			report.addStepInfoScreenshot(driver, step);			
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentSourceAccount"))).findElement(By.tagName("ul")), linkedInstruction.getString("linked_payment_source_account"));
			
			Select linkedPaymentBalanceConsideration = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentBalanceConsideration"))).get(1));
			linkedPaymentBalanceConsideration.selectByVisibleText(linkedInstruction.getString("linked_payment_balance_consideration"));
			
			if(linkedInstruction.getBoolean("linked_payment_split")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentSplit"))).click();
			}
			
			else if(linkedInstruction.getBoolean("linked_payment_partial_payment")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentPartialPayment"))).click();
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentBasicDetailsNext"))).click();
			
			if(linkedInstruction.getBoolean("linked_payment_amount_configure")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-LinkedAmountConfigure"))).click();
			}
			if(linkedInstruction.getBoolean("linked_payment_destination_configure")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-LinkedDestinationCOnfigure"))).click();
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-LinkedConfigNext"))).click();
			
			if(linkedInstruction.getBoolean("linked_payment_sweep_in")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentSweepIn"))).findElement(By.tagName("div")).click();
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentSweepInNext"))).click();
			
			if(linkedInstruction.getBoolean("linked_payment_repeating")) {
				
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentRepeating"))).findElement(By.tagName("div")).click();
				
				List<WebElement> linkedInstructionDatePicker = driver.findElements(By.id(newDealLocator.getString("input-ScheduledPaymentDatePicker")));
				linkedInstructionDatePicker.get(0).sendKeys(linkedInstruction.getString("linked_payment_start_date"));				
				linkedInstructionDatePicker.get(1).sendKeys(linkedInstruction.getString("linked_payment_end_date"));
			
				if(linkedInstruction.getBoolean("linked_payment_all_sub_instructions")) {				
					
					Select linkedInstructionFrequency = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentFrequency"))).get(1));
					linkedInstructionFrequency.selectByVisibleText(linkedInstruction.getString("linked_payment_frequency"));
					
					driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRepeatEvery"))).clear();
					driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRepeatEvery"))).sendKeys(linkedInstruction.getString("linked_payment_repeat_every"));
					
					switch (linkedInstruction.getString("linked_payment_frequency").toLowerCase()) {
						case "days":
							break;
							
						case "weekly":
							
							selectDayOfWeek(driver, step, linkedInstruction.getJSONArray("linked_payment_days_of_week"));
							break;
							
						case "monthly":
							
							Select linkedInstructionFrequencySubtype = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentSubType"))).get(1));
							linkedInstructionFrequencySubtype.selectByVisibleText(linkedInstruction.getString("linked_payment_sub_type"));
							
							if(linkedInstruction.getString("linked_payment_sub_type").toLowerCase().equals("day of month")) {
								
								driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentDays"))).findElement(By.tagName("input")).click();
								utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentDays"))).findElement(By.tagName("ul")), linkedInstruction.getJSONArray("linked_payment_days"));
							}
							else if(linkedInstruction.getString("linked_payment_sub_type").toLowerCase().equals("weekday(s)")) {
								
								Select linkedInstructionFrequencyWeek = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentWeek"))).get(1));
								linkedInstructionFrequencyWeek.selectByVisibleText(linkedInstruction.getString("linked_payment_week"));
								
								selectDayOfWeek(driver, step, linkedInstruction.getJSONArray("linked_payment_days_of_week"));					
							}
							break;
						
						default:
							step.log(Status.FAIL, "Invalid payment frequency: "+linkedInstruction.getString("linked_payment_frequency"));
							throw new Exception("Invalid payment frequency: "+linkedInstruction.getString("linked_payment_frequency"));
					}
				}
				else {
					driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentAllSubInstructions"))).findElement(By.tagName("div")).click();
				}
			}
			else {
				driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentDatePicker"))).sendKeys(linkedInstruction.getString("linked_payment_execution_date"));
			}
			
			Select linkedInstructionScheduledAt = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentScheduledAt"))).get(1));
			linkedInstructionScheduledAt.selectByVisibleText(linkedInstruction.getString("linked_payment_scheduled_at"));
			
			Select linkedInstructionHolidayAction = new Select(driver.findElements(By.id(newDealLocator.getString("select-ScheduledPaymentHolidayAction"))).get(1));
			linkedInstructionHolidayAction.selectByVisibleText(linkedInstruction.getString("linked_payment_holiday_action"));
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentScheduleNext"))).click();
			
			
			if(linkedInstruction.has("linked_payment_sub_instructions")) {
				
				Iterator<Object> paymentSubInstructionsIterator = linkedInstruction.getJSONArray("linked_payment_sub_instructions").iterator();
				
				while(paymentSubInstructionsIterator.hasNext()) {
					
					JSONObject paymentSubInstruction = (JSONObject) paymentSubInstructionsIterator.next();
					
					driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentInstrument"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentInstrument"))).findElement(By.tagName("ul")), paymentSubInstruction.getString("sub_instruction_instrument"));
					
					driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentBudget"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentBudget"))).findElement(By.tagName("ul")), paymentSubInstruction.getString("sub_instruction_budget"));
					
					Thread.sleep(500);
					driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentBudgetPurpose"))).findElement(By.tagName("input")).click();
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentBudgetPurpose"))).findElement(By.tagName("ul")), paymentSubInstruction.getString("sub_instruction_budget_purpose"));
					
					
					switch (paymentSubInstruction.getString("sub_instruction_instrument").toLowerCase()) {
					case "bt":
						addBTSubInstruction(driver, step, driver.findElements(By.id(newDealLocator.getString("input-ScheduledPaymentValueDate"))).get(2), paymentSubInstruction);
						break;

					default:
						break;
					}		
					
					driver.findElement(By.id(newDealLocator.getString("button-AddScheduledPaymentSubInstruction"))).click();
				}
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentSubInstructionNext"))).click();
			
			if(linkedInstruction.getBoolean("linked_payment_enable_auto_retry")) {
				
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentEnableAutoRetry"))).findElement(By.tagName("div")).click();
				
				driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentRetryType"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentRetryType"))).findElement(By.tagName("ul")), linkedInstruction.getString("linked_payment_retry_type"));
				
				if(linkedInstruction.getString("linked_payment_retry_type").toLowerCase().equals("same day")) {
					
					driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRetryHours"))).sendKeys(linkedInstruction.getString("linked_payment_retry_hours"));
				}
				
				else if(linkedInstruction.getString("linked_payment_retry_type").toLowerCase().equals("custom")) {
					
					driver.findElement(By.id(newDealLocator.getString("input-ScheduledPaymentRetryDays"))).sendKeys(linkedInstruction.getString("linked_payment_retry_days"));
				}
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentRetryNext"))).click();
			
			if(linkedInstruction.getBoolean("linked_payment_enable_notification_alerts")) {
				
				driver.findElement(By.id(newDealLocator.getString("checkbox-ScheduledPaymentNotificationAlerts"))).findElement(By.tagName("div")).click();
				
				driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentNotificationMode"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-ScheduledPaymentNotificationMode"))).findElement(By.tagName("ul")), linkedInstruction.getString("linked_payment_notification_mode"));
				
				driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentNotificationAddContact"))).click();
				
				List<WebElement> availableContacts = driver.findElements(By.xpath(newDealLocator.getString("label-ContactName")));
				List<String> actualContactList = new ArrayList<String>();
				for(WebElement contact : availableContacts) {
					actualContactList.add(contact.getText());
				}
				step.log(Status.INFO, "Contacts available at Linked Instruction - Expected: "+expectedContactList.toString()+" Actual: "+actualContactList.toString()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);				
				
				setContacts(driver, step, linkedInstruction.getJSONArray("linked_payment_notification_contacts"));
				
				driver.findElement(By.id(newDealLocator.getString("button-ScheduledPaymentNotificationNext"))).click();
			}
			
			
			
			step.log(Status.INFO, "Linked Instruction of Type : Payment added to deal");
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding Linked Instruction of Type : Payment");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Linked Instruction of Type : Payment");
		}
		
	}
	
	private void addLinkedInstructions(WebDriver driver, ExtentTest step,  JSONArray linkedInstructions) throws Exception {
		
		Iterator<Object> linkedInstructionIterator = linkedInstructions.iterator();
		
		driver.findElement(By.id(newDealLocator.getString("tab-LinkedWizard"))).click();
		
		while(linkedInstructionIterator.hasNext()) {	
			JSONObject linkedInstruction = (JSONObject) linkedInstructionIterator.next();
			try {				
				driver.findElement(By.id(newDealLocator.getString("button-NewScheduledInstruction"))).click();
				
				switch (linkedInstruction.getString("linked_type").toLowerCase()) {
				case "payment":
					Thread.sleep(500);
					driver.findElement(By.id(newDealLocator.getString("card-ScheduledPayment"))).click();
					driver.findElement(By.id(newDealLocator.getString("button-ScheduledTypeProceed"))).click();
					addLinkedPayment(driver, step, linkedInstruction);
					break;

				default:
					break;
				}
				
				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Linked Instruction of type: "+linkedInstruction.getString("linked_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Linked Instruction of type: "+linkedInstruction.getString("linked_type"));
			}
			driver.findElement(By.xpath(newDealLocator.getString("button-ScheduledBack"))).click();
		}
	}
	
	private void fillDocument(WebDriver driver, ExtentTest step,  JSONObject document) throws Exception {
		
		try {
			Thread.sleep(500);
			
			driver.findElement(By.id(newDealLocator.getString("select-DocumentType"))).findElement(By.tagName("input")).click();
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-DocumentType"))).findElement(By.tagName("ul")), document.getString("document_type"));
			
			Select documentNature = new Select(driver.findElement(By.xpath(newDealLocator.getString("select-DocumentNature"))));
			documentNature.selectByVisibleText(document.getString("document_nature"));
			
			if(document.has("document_date")) {
				driver.findElement(By.id(newDealLocator.getString("input-DocumentDatePicker"))).sendKeys(document.getString("document_date"));
			}
			
			driver.findElement(By.id(newDealLocator.getString("button-AddDocument"))).click();
		}
		catch (InterruptedException e) {
			step.log(Status.FAIL, "Error while filling Document of type: "+document.getString("document_type"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while filling Document of type: "+document.getString("document_type"));
		}
		
	}
	
	private void addDocuments(WebDriver driver, ExtentTest step,  String dealName, JSONArray documents) throws Exception {
		
		Iterator<Object> documentIterator = documents.iterator();
		
		driver.findElement(By.id(newDealLocator.getString("tab-DocumentWizard"))).click();
		
		while (documentIterator.hasNext()) {
			JSONObject document = (JSONObject) documentIterator.next();
			try {				
				utility.addToExpectedDetails(expectedDealDetails, dealName.toLowerCase().trim(), "document level:"+document.getString("document_level").toLowerCase().trim()+";document type:"+document.getString("document_type").toLowerCase().trim());
				Thread.sleep(1000);
				switch (document.getString("document_level").toLowerCase()) {
				case "mandatory":					
					driver.findElements(By.id(newDealLocator.getString("button-NewDocument"))).get(0).click();
					
					fillDocument(driver, step, document);
					
					if(document.getBoolean("document_deferral")) {
						driver.findElement(By.id(newDealLocator.getString("checkbox-DocumentDeferral"))).click();
						
						driver.findElement(By.id(newDealLocator.getString("input-DocumentDatePicker"))).sendKeys(document.getString("document_deferral_due_date"));
						
						driver.findElement(By.xpath(newDealLocator.getString("button-DocumentDeferralSave"))).click();
					}
					Thread.sleep(1000);
					File documentFile = new File(document.getString("document_upload_url"));
					driver.findElement(By.id(newDealLocator.getString("input-DocumentUpload"))).sendKeys(documentFile.getAbsolutePath());
					break;

				default:
					break;
				}			
				step.log(Status.INFO, "Document: "+document.getString("document_type")+" added to deal");
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while adding Document of type: "+document.getString("document_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while adding Document of type: "+document.getString("document_type"));
			}
		}
		
	}
	

	
	private void addNotifications(WebDriver driver, ExtentTest step,  String dealName, JSONArray notifications) throws Exception {
		Iterator <Object> notificationIterator = notifications.iterator();		
		driver.findElement(By.id(newDealLocator.getString("tab-NotificationsWizard"))).click();			
					
		while(notificationIterator.hasNext()) {
			List<String> notificationList = utility.extractColumnsFromTable(driver, step, By.id(newDealLocator.getString("table-NotificationsRows")), 0);
				
			JSONObject notification = (JSONObject) notificationIterator.next();				
			try {
				utility.addToExpectedDetails(expectedDealDetails, dealName.toLowerCase().trim(), "notification:"+notification.getString("notification_type").toLowerCase().trim());
				
				WebElement notificationElement = driver.findElements(By.id(newDealLocator.getString("table-NotificationsRows"))).get(notificationList.indexOf(notification.getString("notification_type")));
				utility.scrollIntoView(driver, notificationElement.findElements(By.tagName("td")).get(4));
				Thread.sleep(500); 
				notificationElement.findElements(By.tagName("td")).get(4).click();
				driver.findElement(By.id(newDealLocator.getString("menu-NotificationsPopupMenu"))).findElement(By.tagName("div")).click();
				
				driver.findElement(By.id(newDealLocator.getString("checkbox-NotificationsEnable"))).findElement(By.tagName("label")).click();
				
				Thread.sleep(500);
				driver.findElement(By.id(newDealLocator.getString("select-NotificationsMode"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.id(newDealLocator.getString("select-NotificationsMode"))).findElement(By.tagName("ul")), notification.getString("notification_mode"));
				
				driver.findElement(By.id(newDealLocator.getString("button-NotificationsAddContact"))).click();
				
				List<WebElement> availableContacts = driver.findElements(By.xpath(newDealLocator.getString("label-ContactName")));
				List<String> actualContactList = new ArrayList<String>();
				for(WebElement contact : availableContacts) {
					actualContactList.add(contact.getText());
				}
				step.log(Status.INFO, "Contacts available at Notifications - Expected: "+expectedContactList.toString()+" Actual: "+actualContactList.toString()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);				
				
				setContacts(driver, step, notification.getJSONArray("notification_contacts"));
				
				driver.findElement(By.id(newDealLocator.getString("button-AddNotifications"))).click();
				step.log(Status.INFO, "Notification: "+notification.getString("notification_type")+" added to deal");
			}		
			catch (Exception e) {			
				step.log(Status.FAIL, "Error while enabling Notification: "+notification.getString("notification_type"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while enabling Notification: "+notification.getString("notification_type"));
			}
		}
		
	}
	
	private void addExecutionPolicies(WebDriver driver, ExtentTest step,  String dealName, JSONObject executionPolicies) throws Exception {
		try {
			driver.findElement(By.id(newDealLocator.getString("tab-ExecutionPoliciesWizard"))).click();
			utility.waitForProgressBarToLoad(driver);
			
			Select executionPolicyDependencies = new Select(driver.findElement(By.id(newDealLocator.getString("select-ExecutionPoliciesDependencies"))).findElement(By.tagName("select")));
			executionPolicyDependencies.selectByVisibleText(executionPolicies.getString("execution_policy_dependencies"));
			
			Select executionPolicyVerifyBalances = new Select(driver.findElement(By.id(newDealLocator.getString("select-ExecutionPoliciesVerifyBalances"))).findElement(By.tagName("select")));
			executionPolicyVerifyBalances.selectByVisibleText(executionPolicies.getString("execution_policy_verify_balances"));
			
			Select executionPolicyReattempt = new Select(driver.findElement(By.id(newDealLocator.getString("select-ExecutionPoliciesReattempt"))).findElement(By.tagName("select")));
			executionPolicyReattempt.selectByVisibleText(executionPolicies.getString("execution_policy_reattempt"));
			
			Select executionPolicyHolidayAction = new Select(driver.findElement(By.id(newDealLocator.getString("select-ExecutionPoliciesHolidayAction"))).findElement(By.tagName("select")));
			executionPolicyHolidayAction.selectByVisibleText(executionPolicies.getString("execution_policy_holiday_action"));
			
			Select executionPolicyInactiveBehaviour = new Select(driver.findElement(By.id(newDealLocator.getString("select-ExecutionPoliciesInactiveBehaviour"))).findElement(By.tagName("select")));
			executionPolicyInactiveBehaviour.selectByVisibleText(executionPolicies.getString("execution_policy_inactive_behaviour"));
			
			if(executionPolicies.getString("execution_policy_verify_balances").equals("At an Instruction")) {
				Select executionPolicyBalanceConsideration = new Select(driver.findElement(By.id(newDealLocator.getString("select-ExecutionPoliciesBalanceConsideration"))).findElement(By.tagName("select")));
				executionPolicyBalanceConsideration.selectByVisibleText(executionPolicies.getString("execution_policy_balance_consideration"));
			}
			
			List<WebElement> executionPolicyReattemptInterval = driver.findElements(By.xpath(newDealLocator.getString("button-ExecutionPoliciesReattemptInterval"))); 
			switch (executionPolicies.getString("execution_policy_reattempt_interval")){
			case "1 Hour": 
				executionPolicyReattemptInterval.get(0).click();
				break;
			
			case "3 Hours":
				executionPolicyReattemptInterval.get(1).click();
				break;
			
			case "1 Day":
				executionPolicyReattemptInterval.get(2).click();
				break;
				
			default:
				step.log(Status.FAIL, "Invalid Reattempt Interval: "+executionPolicies.getString("execution_policy_reattempt_interval"));
				report.addStepFailScreenshot(driver, step);
				throw new Exception("Invalid Reattempt Interval: "+executionPolicies.getString("execution_policy_reattempt_interval"));
			}
			
			if(executionPolicies.getBoolean("execution_policy_group_payments")) {
				driver.findElement(By.id(newDealLocator.getString("checkbox-ExecutionPoliciesGroupPayments"))).click();
			}
			
						
			driver.findElement(By.id(newDealLocator.getString("button-ExecutionPoliciesNext"))).click();
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while adding execution policies to deal: "+dealName);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding execution policies to deal: "+dealName);
		}
	}
	
	private List<String> extractTextFromBudgetGroupCards(WebDriver driver, ExtentTest step) throws Exception{
		List<String> budgetGroupNameList = new ArrayList<String>();
		
		try {		
		List<WebElement> budgetGroupCardElements = driver.findElements(By.id(newDealLocator.getString("card-BudgetDetails")));		
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
		List<WebElement> scheduledInstructionCardElements = driver.findElement(By.id(newDealLocator.getString("card-ScheduledInstructionDetails"))).findElements(By.xpath("*"));		
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
		List<WebElement> linkedInstructionCardElements = driver.findElement(By.id(newDealLocator.getString("card-LinkedInstructionDetails"))).findElements(By.xpath("*"));		
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
			List<String> entitlementRecords = utility.extractColumnsFromTable(driver, step, By.id(newDealLocator.getString("table-EntitlementList")), 2);
			
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
	
	private void assertAccountSection(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {		
		utility.waitForElementToBeDisplayed(driver, By.xpath(newDealLocator.getString("button-SummaryAccountNumber")), 2, 120);
		int noOfEntries = driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountNumber"))).size();
		int index = 1;
		
		step.log(Status.INFO, "Accounts added to deal "+deal.getString("name")+" (See below image)");
		report.addStepInfoScreenshot(driver, step);
		
		try {
			while(index < noOfEntries) {
				String accountNumber = driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountNumber"))).get(index).getText().toLowerCase().trim();				
				
				if(deal.has("budget_groups") && !driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountBudgetGroups"))).get(index).getText().equals("0")) {
					driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountBudgetGroups"))).get(index).findElement(By.tagName("div")).click();
					
					List<String> budgetGroupList = extractTextFromBudgetGroupCards(driver, step);	
					
					for(String budgetGroup : budgetGroupList) {
						expectedAccountDetails.get(accountNumber).remove(budgetGroup);				
					}
					
					step.log(Status.INFO, "Budget Groups added to account "+accountNumber+" (See below image)");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
				}
				
				if(deal.has("scheduled_instructions") && !driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountScheduledInstructions"))).get(index).getText().equals("0")){
					driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountScheduledInstructions"))).get(index).findElement(By.tagName("div")).click();
					List<String> scheduledInstructionList = extractTextFromScheduledInstructionCards(driver, step);
					
					for(String scheduledInstruction : scheduledInstructionList) {
						expectedAccountDetails.get(accountNumber).remove(scheduledInstruction);					
					}
					
					step.log(Status.INFO, "Scheduled Instructions added to account "+accountNumber+" (See below image)");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();			
				}
				
				if(deal.has("linked_instructions") && !driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountLinkedInstructions"))).get(index).getText().equals("0")){
					driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountLinkedInstructions"))).get(index).findElement(By.tagName("div")).click();
					List<String> linkedInstructionList = extractTextFromLinkedInstructionCards(driver, step);
					
					for(String linkedInstruction : linkedInstructionList) {
						expectedAccountDetails.get(accountNumber).remove(linkedInstruction);				
					}
					
					step.log(Status.INFO, "Linked Instructions added to account "+accountNumber+" (See below image)");	
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
				}
				
				if(deal.has("entitlements") && !driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountEntitlements"))).get(index).getText().equals("0")){
					driver.findElements(By.xpath(newDealLocator.getString("button-SummaryAccountEntitlements"))).get(index).findElement(By.tagName("div")).click();
					List<String> entitlementList = extractTextFromEntitlementCards(driver, step);
					
					for(String entitlement : entitlementList) {
						expectedAccountDetails.get(accountNumber).remove(entitlement);				
					}
					
					step.log(Status.INFO, "Entitlements added to account "+accountNumber+" (See below image)");	
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
				}
				
				if(!expectedAccountDetails.get(accountNumber).isEmpty()) {
					step.log(Status.FAIL, "Validation Error for Account: "+accountNumber);
					step.log(Status.FAIL, "Components not added: "+expectedAccountDetails.get(accountNumber).toString());
				}
				else {
					step.log(Status.PASS, "Validation Success for Account: "+accountNumber);
					expectedAccountDetails.remove(accountNumber);
				}
				index ++;
			}
			if(!expectedAccountDetails.isEmpty()) {
				step.log(Status.INFO, "Account(s) not added: "+expectedAccountDetails.keySet());
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while asserting Account section of Deal: "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while asserting Account section of Deal: "+deal.getString("name"));
		}		
	}
	
	private void assertPartySection(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {				
		utility.waitForElementToBeDisplayed(driver, By.xpath(newDealLocator.getString("button-SummaryPartyName")), 2, 120);
		int noOfEntries = driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyName"))).size();
		int index = 1;
		
		utility.scrollIntoView(driver, driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyName"))).get(0));
		step.log(Status.INFO, "Parties added to  deal "+deal.getString("name")+" (See below image)");
		report.addStepInfoScreenshot(driver, step);
		
		try {
			while(index < noOfEntries) {
				String partyName = driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyName"))).get(index).getText().toLowerCase().trim();
				
				utility.scrollIntoView(driver, driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyName"))).get(index));
				Thread.sleep(500);			
				
				String partyParticipantID =  "participant:"+driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyParticipantID"))).get(index).getText().toLowerCase().trim(); 
				if(!partyParticipantID.equals("participant:n.a")) {					
					expectedPartyDetails.get(partyName).remove(partyParticipantID);
					step.log(Status.INFO, "Ecommerce party "+partyParticipantID);
					
				}
				String partyResponsibility = "responsibility:"+driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyResponsibility"))).get(index).getText().toLowerCase().trim();
				expectedPartyDetails.get(partyName).remove(partyResponsibility);
				step.log(Status.INFO, "Party responsibility: "+partyResponsibility);
				
				
				
				if(expectedPartyDetails.get(partyName).toString().contains("debit:")) {
					driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyDebits"))).get(index).findElement(By.tagName("div")).click();
					
					List<String> partyDebitList = utility.extractColumnsFromTable(driver, step, By.id(newDealLocator.getString("table-NotificationsRows")), 0);	
					
					for(String partyDebit : partyDebitList) {
						expectedPartyDetails.get(partyName).remove("debit:"+partyDebit.toLowerCase().trim());				
					}
					
					step.log(Status.INFO, "Debit accounts added to party "+partyName+" (See below image)");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
				}
				
				if(expectedPartyDetails.get(partyName).toString().contains("contact:")) {
					driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyContacts"))).get(index).findElement(By.tagName("div")).click();			
					
					List<String> partyContactList = utility.extractColumnsFromTable(driver, step, By.id(newDealLocator.getString("table-NotificationsRows")), 1);	
					
					for(String partyContact : partyContactList) {
						expectedPartyDetails.get(partyName).remove("contact:"+partyContact.toLowerCase().trim());				
					}
					
					step.log(Status.INFO, "Contacts added to party "+partyName+" (See below image)");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
				}
				
				if(expectedPartyDetails.get(partyName).toString().contains("account:")) {
					driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyAccounts"))).get(index).findElement(By.tagName("div")).click();
					List<String> partyAccountList = utility.extractColumnsFromTable(driver, step, By.id(newDealLocator.getString("table-NotificationsRows")), 1);	
					
					for(String partyAccount : partyAccountList) {
						expectedPartyDetails.get(partyName).remove("account:"+partyAccount.toLowerCase().trim());					
					}
					
					step.log(Status.INFO, "Accounts added to party "+partyName+" (See below image)");	
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
				}
				
				if(expectedPartyDetails.get(partyName).toString().contains("document:")) {
					utility.scrollIntoView(driver, driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyDocuments"))).get(index));
					Thread.sleep(500);
					driver.findElements(By.xpath(newDealLocator.getString("button-SummaryPartyDocuments"))).get(index).findElement(By.tagName("div")).click();
					List<String> partyDocumentList = utility.extractColumnsFromTable(driver, step, By.id(newDealLocator.getString("table-NotificationsRows")), 0);
					
					for(String partyDocument : partyDocumentList) {
						expectedPartyDetails.get(partyName).remove("document:"+partyDocument.toLowerCase().trim());				
					}
					
					step.log(Status.INFO, "Documents added to party "+partyName+" (See below image)");	
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
				}
				if(!expectedPartyDetails.get(partyName).isEmpty()) {
					step.log(Status.FAIL, "Validation Error for Party: "+partyName);
					step.log(Status.FAIL, "Components not added: "+expectedPartyDetails.get(partyName).toString());
				}
				else {
					step.log(Status.PASS, "Validation Success for Party: "+partyName);
					expectedPartyDetails.remove(partyName);
				}
				index ++;
			}
			if(!expectedPartyDetails.isEmpty()) {
				step.log(Status.INFO, "Party not added: "+expectedPartyDetails.keySet());
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while asserting Party section of Deal: "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while asserting Party section of Deal: "+deal.getString("name"));
		}
	}
	
	private String extractTextFromFeesCards(WebDriver driver, ExtentTest step) throws Exception{
		String feeCardText = null;
		String feeType = "type:";
		try {
			List<WebElement> feeCardElements = driver.findElement(By.id(newDealLocator.getString("card-FeeDetails"))).findElements(By.xpath("*"));		
			for(WebElement feeCard : feeCardElements) {			
				String[] cardText = feeCard.getText().toLowerCase().trim().split("\n");		
				
				for(int index=0; index <cardText.length; index ++) {					
					if(cardText[index].equals("type")) {
						feeType += cardText[index+1];
					}				
				}				
			}
			feeCardText = feeType;
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting text from Fee Cards");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting text from Fee Cards");
		}
		return feeCardText;
	}
	
	private void assertFeeSection(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {	
		
		utility.focusElement(driver, driver.findElement(By.id(newDealLocator.getString("grid-SummaryFee"))));  
		step.log(Status.INFO, "Fees added to  deal "+deal.getString("name")+" (See below image)");
		report.addStepInfoScreenshot(driver, step);
		
		try {
			List<WebElement> feeList = driver.findElement(By.id(newDealLocator.getString("grid-SummaryFee"))).findElements(By.tagName("table"));
			feeList.remove(0);
			
			for(WebElement feeElement : feeList) {
				String feeName = feeElement.findElements(By.tagName("td")).get(0).getText().toLowerCase().trim();
				
				utility.focusElement(driver, feeElement);
				Thread.sleep(500);	
				
				feeElement.findElements(By.tagName("td")).get(1).findElement(By.tagName("i")).click();
				String feeCardText = extractTextFromFeesCards(driver, step);				
				expectedFeeDetails.get(feeName).remove(feeCardText);					
				
				
				step.log(Status.INFO, "Fee: "+feeName+" details. (See below image)");
				report.addStepInfoScreenshot(driver, step);
				Thread.sleep(500);
				driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
				
				if(!expectedFeeDetails.get(feeName).isEmpty()) {
					step.log(Status.FAIL, "Validation Error for Fee: "+feeName);
					step.log(Status.FAIL, "Components not added: "+expectedFeeDetails.get(feeName).toString());
				}
				else {
					step.log(Status.PASS, "Validation Success for Fee: "+feeName);
					expectedFeeDetails.remove(feeName);
				}				
			}
			if(!expectedFeeDetails.isEmpty()) {
				step.log(Status.INFO, "Fees not added: "+expectedFeeDetails.keySet());
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while asserting Fee section of Deal: "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while asserting Fee section of Deal: "+deal.getString("name"));
		}
	}
	
	private boolean assertDealDetails(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {
		boolean result = true;
		try {				
			WebElement dealStartsOn = driver.findElement(By.id(newDealLocator.getString("label-SummaryDealStartsOn")));
			utility.scrollIntoView(driver, dealStartsOn);
			step.log(Status.INFO, "Deal Starts On - Expected: "+deal.getString("starts_on")+" Actual: "+dealStartsOn.getText()+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			if(!deal.getString("starts_on").equals(dealStartsOn.getText())) {				
				result = false;				
			}
			
			if(deal.has("ends_on")){
				WebElement dealEndsOn = driver.findElement(By.id(newDealLocator.getString("label-SummaryDealEndsOn")));
				utility.scrollIntoView(driver, dealEndsOn);
				step.log(Status.INFO, "Deal Ends On - Expected: "+deal.getString("ends_on")+" Actual: "+dealEndsOn.getText()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				if(!deal.getString("ends_on").equals(dealEndsOn.getText())) {					
					result = false;				
				}
			}
			
			WebElement dealCountry = driver.findElement(By.id(newDealLocator.getString("label-SummaryDealCountry")));
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
			
			WebElement dealTimezone = driver.findElement(By.id(newDealLocator.getString("label-SummaryDealTimezone")));
			utility.scrollIntoView(driver, dealTimezone);
			step.log(Status.INFO, "Deal Timezone - Expected: "+deal.getString("timezone")+" Actual: "+dealTimezone.getText()+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			if(!deal.getString("timezone").contains(dealTimezone.getText())) {				
				result = false;				
			}
			
			if(deal.has("entitlements") && !driver.findElement(By.id(newDealLocator.getString("button-SummaryEntitlements"))).getText().equals("0")) {
				utility.scrollIntoView(driver, driver.findElement(By.id(newDealLocator.getString("button-SummaryEntitlements"))));
				driver.findElement(By.id(newDealLocator.getString("button-SummaryEntitlements"))).click();				
				List<String> entitlementList = extractTextFromEntitlementCards(driver, step);
				
				for(String entitlement : entitlementList) {
					
					expectedDealDetails.get(deal.getString("name").toLowerCase().trim()).remove(entitlement);
				}
				
				step.log(Status.INFO, "Entitlements added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
			}
			
			utility.scrollIntoView(driver, driver.findElement(By.id(newDealLocator.getString("button-SummaryProcessingUnits"))));
			driver.findElement(By.id(newDealLocator.getString("button-SummaryProcessingUnits"))).click();
			Thread.sleep(1000);
			String[] dealProcessingUnits = driver.findElement(By.id(newDealLocator.getString("card-SummaryProcessingUnits"))).getText().split("\n");
			expectedDealDetails.get(deal.getString("name").toLowerCase().trim()).remove("processing units:"+Arrays.toString(dealProcessingUnits).toLowerCase().trim());
			
			step.log(Status.INFO, "Processing units added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
			
			if(deal.has("notifications")) {
				utility.scrollIntoView(driver, driver.findElement(By.id(newDealLocator.getString("button-SummaryNotifications"))));
				driver.findElement(By.id(newDealLocator.getString("button-SummaryNotifications"))).click();
				Thread.sleep(2000);
				List<String> notificationTypes = utility.extractColumnsFromTable(driver, step, By.id(newDealLocator.getString("table-NotificationsRows")), 0);
				for(String notification : notificationTypes) {
					
					expectedDealDetails.get(deal.getString("name").toLowerCase().trim()).remove("notification:"+notification.toLowerCase().trim());
				}
				
				step.log(Status.INFO, "Notifications added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
			}
			
			if(deal.has("contacts")) {
				utility.scrollIntoView(driver, driver.findElement(By.id(newDealLocator.getString("button-SummaryContacts"))));
				driver.findElement(By.id(newDealLocator.getString("button-SummaryContacts"))).click();
				List<String> contactList = utility.extractColumnsFromTable(driver, step, By.id(newDealLocator.getString("table-NotificationsRows")), 0);
				for(String contact : contactList) {
					expectedDealDetails.get(deal.getString("name").toLowerCase().trim()).remove("contact:"+contact.toLowerCase().trim());
				}
				
				step.log(Status.INFO, "Contacts added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
			}
			
			if(deal.has("documents")) {
				utility.scrollIntoView(driver, driver.findElement(By.id(newDealLocator.getString("button-SummaryDocuments"))));
				driver.findElement(By.id(newDealLocator.getString("button-SummaryDocuments"))).click();
				Thread.sleep(2000);
				List<String> documentList = utility.extractColumnsFromTable(driver, step, By.xpath(newDealLocator.getString("table-SummaryDocuments")), 2);
				for(String document : documentList) {
					
					expectedDealDetails.get(deal.getString("name").toLowerCase().trim()).remove("document level:mandatory;document type:"+document.toLowerCase().trim());
				}
				
				step.log(Status.INFO, "Mandatory documents added to deal "+deal.getString("name").toLowerCase().trim()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(newDealLocator.getString("button-SummaryClose"))).click();
			}
			if(!expectedDealDetails.get(deal.getString("name").toLowerCase().trim()).isEmpty()) {
				result = false;
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while asserting details of Deal: "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while asserting details of Deal: "+deal.getString("name"));			
		}
		return result;
	}
	
	private void submitDeal(WebDriver driver, ExtentTest step,  String dealName) throws Exception {
		try {
			driver.findElement(By.id(newDealLocator.getString("button-SubmitDeal"))).click();				
			driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealYes"))).click();
			
			driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealOK")));
			String response = utility.extractTitleMessage(driver, step, By.id(newDealLocator.getString("label-Title")));			
			if(response.toLowerCase().contains("something went wrong")) {
				step.log(Status.FAIL, "Deal "+dealName+" submit failed");
				report.addStepFailScreenshot(driver, step);	
				throw new Exception("Deal "+dealName+" submit failed");			
			}
			else {
				step.log(Status.PASS, "Deal "+dealName+" submitted to Deal Checker Queue");
				report.addStepPassScreenshot(driver, step);	
				
				driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealOK"))).click();
			}			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while submitting deal: "+dealName);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while submitting deal: "+dealName);
		}
	}
	
	public NewDealPage(LocatorFactory locators) {	
		newDealLocator = locators.getLocators("NewDealPage");
		landingPage = new LandingPage(locators);
	}
	
	public void validateRequiredMessagesGenerated(ExtentTest step) throws Exception {
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();		
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				JSONArray requiredMessages = deal.getJSONArray("deal_required_messages");
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
				step.log(Status.FAIL, "Error while validating deal required field messages");				
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while validating deal required field messages");
			}
		}
	}
	
	public void validateResponseGenerated(ExtentTest step) throws Exception {
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();		
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				if(deal.has("deal_response_messages")) {
					JSONArray responseMessages = deal.getJSONArray("deal_response_messages");
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
				step.log(Status.FAIL, "Error while validating deal response messages");				
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while validating deal response messages");
			}
		}
	}
	
	public void createDeals(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();	
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();	
			try {
				expectedContactList.clear();
				expectedDealDetails.clear();
				expectedAccountDetails.clear();
				expectedPartyDetails.clear();
				expectedFeeDetails.clear();
				
				landingPage.goToNewDeal(driver, step);				
		
				step.log(Status.INFO, "Deal Name - " + deal.getString("name"));
				
				driver.findElement(By.id(newDealLocator.getString("input-Name"))).sendKeys(deal.getString("name"));
				
				Select productSelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-Product"))).get(1));
				productSelect.selectByVisibleText(deal.getString("product"));
				
				List<WebElement> datePickerList = driver.findElements(By.id(newDealLocator.getString("input-DatePicker")));
				
				datePickerList.get(0).clear();
				datePickerList.get(0).sendKeys(deal.getString("starts_on"));
				
				if(deal.has("ends_on")) {
					datePickerList.get(1).clear();
					datePickerList.get(1).sendKeys(deal.getString("ends_on"));
				}
				
				Select businessSegementSelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-BusinessSegment"))).get(1));
				businessSegementSelect.selectByVisibleText(deal.getString("business_segment"));
				
				driver.findElement(By.id(newDealLocator.getString("select-ProcessingUnit"))).findElement(By.tagName("input")).click();
				Thread.sleep(1000);
				driver.findElement(By.id(newDealLocator.getString("select-ProcessingUnit"))).findElements(By.tagName("ul")).get(1).findElements(By.tagName("span")).get(1).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.xpath(newDealLocator.getString("select-ChildProcessingUnit"))), deal.getJSONArray("processing_units"));
				List<Object> updatedProcessingUnits = deal.getJSONArray("processing_units").toList();
				
				utility.addToExpectedDetails(expectedDealDetails, deal.getString("name").toLowerCase().trim(), "processing units:"+updatedProcessingUnits.toString().toLowerCase().trim());
				
				Select countrySelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-Country"))).get(1));
				countrySelect.selectByVisibleText(deal.getString("country"));
				
				Select timezoneSelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-Timezone"))).get(1));
				timezoneSelect.selectByVisibleText(deal.getString("timezone"));
				
				if(deal.has("attributes")) {
					setAttributes(driver, step, deal.getJSONArray("attributes"));
				}
				
				if(deal.has("contacts")) {
					driver.findElement(By.id(newDealLocator.getString("link-ContactConfigure"))).click();
					setDealContacts(driver, step, deal.getString("name"), deal.getJSONArray("contacts"));						
				}
				
				
				driver.findElement(By.id(newDealLocator.getString("button-DealNext"))).click();
				utility.waitForProgressBarToLoad(driver);
				
				if(deal.has("accounts")) {
					addAccounts(driver, step, deal.getJSONArray("accounts"));
				}
				
				if(deal.has("parties")) {
					addParties(driver, step, deal.getJSONArray("parties"));
				}
				
				if(deal.has("scheduled_fees")) {
					addScheduledFees(driver, step, deal.getJSONArray("scheduled_fees"));
				}
				
				if(deal.has("entitlements")) {
					addEntitlements(driver, step, deal.getString("name"), deal.getJSONArray("entitlements"));
				}
				
				if(deal.has("budget_groups")) {
					addBudgetGroups(driver, step, deal.getJSONArray("budget_groups"));
				}
				
				if(deal.has("scheduled_instructions")){
					addScheduledInstructions(driver, step, deal.getJSONArray("scheduled_instructions"));
				}
				
				if(deal.has("linked_instructions")){
					addLinkedInstructions(driver, step, deal.getJSONArray("linked_instructions"));
				}
				
				if(deal.has("documents")) {
					addDocuments(driver, step, deal.getString("name"), deal.getJSONArray("documents"));
				}
				
				if(deal.has("notifications")) {
					addNotifications(driver, step, deal.getString("name"), deal.getJSONArray("notifications"));
				}
				
				if(deal.has("execution_policies")) {
					addExecutionPolicies(driver, step, deal.getString("name"), deal.getJSONObject("execution_policies"));
				}
				
				Thread.sleep(2000);
				driver.findElement(By.id(newDealLocator.getString("tab-SummaryWizard"))).click();
				
				dealDetails.put(deal.getString("name").toLowerCase().trim(),driver.findElement(By.xpath(newDealLocator.getString("label-SummaryHeading"))).findElements(By.tagName("span")).get(2).getText());
								
				if(deal.has("accounts")) {					
					assertAccountSection(driver, step, deal);
				}
				
				if(deal.has("parties")) {					
					assertPartySection(driver, step, deal);
				}
				
				if(deal.has("scheduled_fees")) {
					assertFeeSection(driver, step, deal);
				}
				
				if(assertDealDetails(driver, step, deal)) {
					 step.log(Status.PASS, "Validation Success for deal: "+deal.getString("name"));
				 }else {
					 step.log(Status.FAIL, "Validation Error for deal: "+deal.getString("name"));
					 throw new Exception("Validation Error for deal: "+deal.getString("name"));
				 }
					 
				Thread.sleep(500);
				if(deal.getBoolean("deal_submit")) {
					submitDeal(driver, step, deal.getString("name"));
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while creating deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while creating deal: "+deal.getString("name"));
			}
		}
		
	}
	
	public void createDeal(WebDriver driver, ExtentTest step,  JSONObject deal) throws Exception {
		try {
			expectedContactList.clear();
			expectedDealDetails.clear();
			expectedAccountDetails.clear();
			expectedPartyDetails.clear();
			
			landingPage.goToNewDeal(driver, step);				
	
			step.log(Status.INFO, "Deal Name - " + deal.getString("name"));
			
			driver.findElement(By.id(newDealLocator.getString("input-Name"))).sendKeys(deal.getString("name"));
			
			Select productSelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-Product"))).get(1));
			productSelect.selectByVisibleText(deal.getString("product"));
			
			List<WebElement> datePickerList = driver.findElements(By.id(newDealLocator.getString("input-DatePicker")));
			
			datePickerList.get(0).clear();
			datePickerList.get(0).sendKeys(deal.getString("starts_on"));
			
			if(deal.has("ends_on")) {
				datePickerList.get(1).clear();
				datePickerList.get(1).sendKeys(deal.getString("ends_on"));				
			}
			
			Select businessSegementSelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-BusinessSegment"))).get(1));
			businessSegementSelect.selectByVisibleText(deal.getString("business_segment"));
			
			driver.findElement(By.id(newDealLocator.getString("select-ProcessingUnit"))).findElement(By.tagName("input")).click();
			Thread.sleep(1000);
			driver.findElement(By.id(newDealLocator.getString("select-ProcessingUnit"))).findElements(By.tagName("ul")).get(1).findElements(By.tagName("span")).get(1).click();
			utility.selectFromDivMenu(driver, step, driver.findElement(By.xpath(newDealLocator.getString("select-ChildProcessingUnit"))), deal.getJSONArray("processing_units"));
			List<Object> updatedProcessingUnits = deal.getJSONArray("processing_units").toList();
			utility.addToExpectedDetails(expectedDealDetails, deal.getString("name").toLowerCase().trim(), "processing units:"+updatedProcessingUnits.toString().toLowerCase().trim());
			
			Select countrySelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-Country"))).get(1));
			countrySelect.selectByVisibleText(deal.getString("country"));
			
			Select timezoneSelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-Timezone"))).get(1));
			timezoneSelect.selectByVisibleText(deal.getString("timezone"));
			
			if(deal.has("attributes")) {
				setAttributes(driver, step, deal.getJSONArray("attributes"));
			}
			
			if(deal.has("contacts")) {
				driver.findElement(By.id(newDealLocator.getString("link-ContactConfigure"))).click();
				setDealContacts(driver, step, deal.getString("name"), deal.getJSONArray("contacts"));						
			}
			
			Thread.sleep(1000);
			driver.findElement(By.id(newDealLocator.getString("button-DealNext"))).click();
			Thread.sleep(1000);
			
			if(deal.has("accounts")) {
				addAccounts(driver, step, deal.getJSONArray("accounts"));
			}
			
			if(deal.has("parties")) {
				addParties(driver, step, deal.getJSONArray("parties"));
			}
			
			if(deal.has("scheduled_fees")) {
				addScheduledFees(driver, step, deal.getJSONArray("scheduled_fees"));
			}
			
			if(deal.has("entitlements")) {
				addEntitlements(driver, step, deal.getString("name"), deal.getJSONArray("entitlements"));
			}
			
			if(deal.has("budget_groups")) {
				addBudgetGroups(driver, step, deal.getJSONArray("budget_groups"));
			}
			
			if(deal.has("scheduled_instructions")){
				addScheduledInstructions(driver, step, deal.getJSONArray("scheduled_instructions"));
			}
			
			if(deal.has("linked_instructions")){
				addLinkedInstructions(driver, step, deal.getJSONArray("linked_instructions"));
			}
			
			if(deal.has("documents")) {
				addDocuments(driver, step, deal.getString("name"), deal.getJSONArray("documents"));
			}
			
			if(deal.has("notifications")) {
				addNotifications(driver, step, deal.getString("name"), deal.getJSONArray("notifications"));
			}
			
			if(deal.has("execution_policies")) {
				addExecutionPolicies(driver, step, deal.getString("name"), deal.getJSONObject("execution_policies"));
			}
			
			Thread.sleep(2000);
			driver.findElement(By.id(newDealLocator.getString("tab-SummaryWizard"))).click();
			Thread.sleep(2000);
			dealDetails.put(deal.getString("name").toLowerCase().trim(),driver.findElement(By.xpath(newDealLocator.getString("label-SummaryHeading"))).findElements(By.tagName("span")).get(2).getText());
			
			if(deal.has("accounts")) {					
				assertAccountSection(driver, step, deal);
			}
			
			if(deal.has("parties")) {					
				assertPartySection(driver, step, deal);
			}
			
			if(deal.has("scheduled_fees")) {
				assertFeeSection(driver, step, deal);
			}
			
			if(assertDealDetails(driver, step, deal)) {
				 step.log(Status.PASS, "Validation Success for deal: "+deal.getString("name"));
			 }else {
				 step.log(Status.FAIL, "Validation Error for deal: "+deal.getString("name"));
				 throw new Exception("Validation Error for deal: "+deal.getString("name"));
			 }
				 
			Thread.sleep(500);
			if(deal.getBoolean("deal_submit")) {
				submitDeal(driver, step, deal.getString("name"));
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while creating deal: "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while creating deal: "+deal.getString("name"));
		}
	}	
	
	public void validatePartyNameSuggestedNotEmpty(ExtentTest step) throws Exception{
		try {
			if(!partyNameSuggestions.isEmpty()) {
				step.log(Status.PASS, "Party name suggested: "+partyNameSuggestions.toString());
			}
			else {
				step.log(Status.FAIL, "No party name suggested");
				throw new Exception("No party name suggested");
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while validating that party name was suggested");			
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while validating that party name was suggested");
		}
	}
	
	public void validatePartyNameSuggestedEmpty(ExtentTest step) throws Exception{
		try {
			if(partyNameSuggestions.isEmpty()) {
				step.log(Status.PASS, "No party name suggested");
			}
			else {
				step.log(Status.FAIL, "Party name suggested: "+partyNameSuggestions.toString());
				throw new Exception("Party name suggested: "+partyNameSuggestions.toString());
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while validating that party name was not suggested");			
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while validating that party name was not suggested");
		}
	}
	public void createNewDeals(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();	
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();	
			try {
				expectedContactList.clear();
				expectedDealDetails.clear();
				expectedAccountDetails.clear();
				expectedPartyDetails.clear();
				expectedFeeDetails.clear();
				
				landingPage.goToNewDeal(driver, step);				
		
				step.log(Status.INFO, "Deal Name - " + deal.getString("name"));
				
				driver.findElement(By.id(newDealLocator.getString("input-Name"))).sendKeys(deal.getString("name"));
				
				Select productSelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-Product"))).get(1));
				productSelect.selectByVisibleText(deal.getString("product"));
				
				List<WebElement> datePickerList = driver.findElements(By.id(newDealLocator.getString("input-DatePicker")));
				
				datePickerList.get(0).clear();
				datePickerList.get(0).sendKeys(deal.getString("starts_on"));
				
				if(deal.has("ends_on")) {
					datePickerList.get(1).clear();
					datePickerList.get(1).sendKeys(deal.getString("ends_on"));
				}
				
				Select businessSegementSelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-BusinessSegment"))).get(1));
				businessSegementSelect.selectByVisibleText(deal.getString("business_segment"));
				
				driver.findElement(By.id(newDealLocator.getString("select-ProcessingUnit"))).findElement(By.tagName("input")).click();
				Thread.sleep(1000);
				driver.findElement(By.id(newDealLocator.getString("select-ProcessingUnit"))).findElements(By.tagName("ul")).get(1).findElements(By.tagName("span")).get(1).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.xpath(newDealLocator.getString("select-ChildProcessingUnit"))), deal.getJSONArray("processing_units"));
				List<Object> updatedProcessingUnits = deal.getJSONArray("processing_units").toList();
				
				utility.addToExpectedDetails(expectedDealDetails, deal.getString("name").toLowerCase().trim(), "processing units:"+updatedProcessingUnits.toString().toLowerCase().trim());
				
				Select countrySelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-Country"))).get(1));
				countrySelect.selectByVisibleText(deal.getString("country"));
				
				Select timezoneSelect = new Select(driver.findElements(By.id(newDealLocator.getString("select-Timezone"))).get(1));
				timezoneSelect.selectByVisibleText(deal.getString("timezone"));
				
				if(deal.has("attributes")) {
					setAttributes(driver, step, deal.getJSONArray("attributes"));
				}
				
				if(deal.has("contacts")) {
					driver.findElement(By.id(newDealLocator.getString("link-ContactConfigure"))).click();
					setDealContacts(driver, step, deal.getString("name"), deal.getJSONArray("contacts"));						
				}
				
				Thread.sleep(1000);
				driver.findElement(By.id(newDealLocator.getString("button-DealNext"))).click();
				Thread.sleep(1000);
				driver.findElement(By.id(newDealLocator.getString("tab-SummaryWizard"))).click();
				Thread.sleep(2000);
				driver.findElement(By.id(newDealLocator.getString("button-SubmitDeal"))).click();
				driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealYes"))).click();
				driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealOK"))).click();
				driver.findElement(By.xpath(newDealLocator.getString("link-dealChecker"))).click();
				utility.waitForProgressBarToLoad(driver);
				driver.findElement(By.xpath(newDealLocator.getString("button-Open"))).click();
				driver.findElement(By.xpath(newDealLocator.getString("button-ClickOnOpen"))).click();
				driver.findElement(By.xpath(newDealLocator.getString("button-RedioSelect"))).click();
				driver.findElement(By.xpath(newDealLocator.getString("button-IconNote"))).click();
				driver.findElement(By.xpath(newDealLocator.getString("textarea-Note"))).sendKeys("Done");
				Thread.sleep(1000);
				driver.findElement(By.xpath(newDealLocator.getString("button-Ok"))).click();
				driver.findElement(By.xpath(newDealLocator.getString("button-Approve"))).click();
				driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealYes"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(newDealLocator.getString("button-SubmitDealOK"))).click();
				utility.waitForProgressBarToLoad(driver);
				 
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while creating deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while creating deal: "+deal.getString("name"));
			}
		}
		
	}
	
	
	
}

