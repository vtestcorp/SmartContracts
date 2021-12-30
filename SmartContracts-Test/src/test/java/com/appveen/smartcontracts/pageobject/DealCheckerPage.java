package com.appveen.smartcontracts.pageobject;

import java.util.ArrayList;
import java.util.HashMap;
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

public class DealCheckerPage {
	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject dealCheckerLocator = null;	
	private JSONArray deals = null; 
	public static HashMap<String, String> dealDetails = new HashMap<String, String>();
	
	private void setPreference(WebDriver driver, ExtentTest step) throws Exception {
		try {
			String[] preferences = {"Account Number"};
			List<String> preferenceList = new ArrayList<String>();
			
			driver.findElement(By.xpath(dealCheckerLocator.getString("button-SetPreference"))).click();
			
			WebElement checkbox =  driver.findElement(By.xpath(dealCheckerLocator.getString("checkbox-SelectAll")));
			if(checkbox.isSelected()) {
				checkbox.click();
			}
			else {
				checkbox.click();
				checkbox.click();
			}
			
			List<WebElement> preferenceElements = driver.findElements(By.xpath(dealCheckerLocator.getString("label-PreferenceName")));
						
			for(WebElement element : preferenceElements) {				
				preferenceList.add(element.getText());
			}
			
			for(int index = 0; index < preferences.length; index++) {
				if(preferenceList.contains(preferences[index])) {
					preferenceElements.get(preferenceList.indexOf(preferences[index])).findElement(By.id(dealCheckerLocator.getString("checkbox-Preference"))).click();
				}
			}
			
			driver.findElement(By.xpath(dealCheckerLocator.getString("button-PreferenceApply"))).click();
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while setting preferences");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while setting preferences");
		}
	}
	
	private List<String> getDealTitleList(WebDriver driver, ExtentTest step) throws Exception {
		List<String> dealTitleList = new ArrayList<String>();
		try {
			List<WebElement> deals = driver.findElements(By.xpath(dealCheckerLocator.getString("card-DealTitle")));
			
			for(WebElement deal : deals) {
				dealTitleList.add(deal.getText().toLowerCase().trim());
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while fetching deal titles from Deal Checker page");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while fetching deal titles from Deal Checker page");
		}
		return dealTitleList;
	}
	
	private void verifyAccountsTab(WebDriver driver, ExtentTest step, JSONArray accounts) throws Exception {
		try {
			driver.findElement(By.id(dealCheckerLocator.getString("tab-AccountWizard"))).click();
			utility.waitForProgressBarToLoad(driver);
			setPreference(driver, step);
			List<String> accountNumberList = utility.extractColumnsFromTable(driver, step, By.xpath(dealCheckerLocator.getString("table-AccountNumber")), 0);
			Iterator<Object> accountIterator = accounts.iterator();
			
			while(accountIterator.hasNext()) {
				JSONObject account = (JSONObject) accountIterator.next();				
				if(accountNumberList.contains(account.getString("account_search_input"))) {
					step.log(Status.INFO, "Account: "+ account.getString("account_search_input")+ " added to deal");
					report.addStepInfoScreenshot(driver, step);
				}
				else {
					step.log(Status.FAIL, "Account: "+account.getString("account_search_input")+ " not added to deal");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Account: "+account.getString("account_search_input")+ " not added to deal");
					
				}
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while verifying Accounts tab");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while verifying Accounts tab");
		}
		
	}
	
	private void verifyPartiesTab(WebDriver driver, ExtentTest step, JSONArray parties) throws Exception {
		try {
			driver.findElement(By.id(dealCheckerLocator.getString("tab-PartyWizard"))).click();
			utility.waitForProgressBarToLoad(driver);
			
			List<WebElement> partyNameElements = driver.findElements(By.xpath(dealCheckerLocator.getString("column-PartyName")));
			partyNameElements.remove(0);
			List<String> partyNameList = new ArrayList<String>();
			for(int index = 0; index <partyNameElements.size(); index++) {
				partyNameList.add(partyNameElements.get(index).getText());
			}
			
			Iterator<Object> partyIterator = parties.iterator();
			
			while(partyIterator.hasNext()) {
				JSONObject party = (JSONObject) partyIterator.next();
				if(partyNameList.contains(party.getString("party_name"))) {
					step.log(Status.INFO, "Party: "+ party.getString("party_name")+ " added to deal");
					report.addStepInfoScreenshot(driver, step);
				}
				else {
					step.log(Status.FAIL, "Party: "+party.getString("party_name")+ " not added to deal");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Party: "+party.getString("party_name")+ " not added to deal");					
				}
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while verifying Parties tab");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while verifying Parties tab");
		}		
	}
	
	private void verifyScheduledFeesTab(WebDriver driver, ExtentTest step, JSONArray scheduledFees) throws Exception {
		try {
			driver.findElement(By.id(dealCheckerLocator.getString("tab-ScheduledFeesWizard"))).click();
			utility.waitForProgressBarToLoad(driver);
			
			List<WebElement> scheduledFeeCards = driver.findElement(By.id(dealCheckerLocator.getString("card-InstructionList"))).findElements(By.tagName(dealCheckerLocator.getString("card-ScheduledFeesCard")));
			List<String> scheduledFeeInstructionNameList = new ArrayList<String>();
			for(int index = 0; index <scheduledFeeCards.size(); index++) {
				scheduledFeeInstructionNameList.add(scheduledFeeCards.get(index).findElement(By.xpath(dealCheckerLocator.getString("label-CardHeader"))).getText());
			}
			
			Iterator<Object> scheduledFeeIterator = scheduledFees.iterator();
			
			while(scheduledFeeIterator.hasNext()) {
				JSONObject scheduledFee = (JSONObject) scheduledFeeIterator.next();
				if(scheduledFeeInstructionNameList.contains(scheduledFee.getString("scheduled_fee_instruction_name"))) {
					step.log(Status.INFO, "Scheduled Fee: "+ scheduledFee.getString("scheduled_fee_instruction_name")+ " added to deal");
					report.addStepInfoScreenshot(driver, step);
				}
				else {
					step.log(Status.FAIL, "Scheduled Fee: "+scheduledFee.getString("scheduled_fee_instruction_name")+ " not added to deal");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Scheduled Fee: "+scheduledFee.getString("scheduled_fee_instruction_name")+ " not added to deal");					
				}
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while verifying Scheduled Fees tab");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while verifying Scheduled Fees tab");
		}		
	}
	
	private void verifyEntitlmentsTab(WebDriver driver, ExtentTest step, JSONArray entitlements) throws Exception {
		try {
			List<String> dealEntitlementRangeFromList = new ArrayList<String>();
			List<String> accountEntitlementRangeFromList = new ArrayList<String>();
			
			driver.findElement(By.id(dealCheckerLocator.getString("tab-EntitlementWizard"))).click();	
			utility.waitForProgressBarToLoad(driver);			
			
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(dealCheckerLocator.getString("select-EntitlementTab"))), "DEAL");
			if(!utility.isElementHiddenNow(driver, By.id(dealCheckerLocator.getString("grid-EntitlementDeal")))) {
				List<WebElement> dealEntitlementRecords = driver.findElement(By.id(dealCheckerLocator.getString("grid-EntitlementDeal"))).findElements(By.id(dealCheckerLocator.getString("table-GenericTable")));
				
				for(int index = 0; index <dealEntitlementRecords.size(); index++) {
					dealEntitlementRangeFromList.add(dealEntitlementRecords.get(index).findElements(By.tagName("td")).get(0).getText().split("\\.")[0].substring(1));
				}
			}			
			
			utility.selectFromDivMenu(driver, step, driver.findElement(By.id(dealCheckerLocator.getString("select-EntitlementTab"))), "ACCOUNT");			
			if(!utility.isElementHiddenNow(driver, By.id(dealCheckerLocator.getString("grid-EntitlementAccount")))) {
				List<WebElement> accountEntitlementCards = driver.findElement(By.id(dealCheckerLocator.getString("grid-EntitlementAccount"))).findElements(By.xpath(dealCheckerLocator.getString("card-GenericCardList")));
				for(int index=0; index < accountEntitlementCards.size(); index++) {
					accountEntitlementCards.get(index).click();
					utility.waitForProgressBarToLoad(driver);
					List<WebElement> accountEntitlementRecords = driver.findElement(By.id(dealCheckerLocator.getString("grid-EntitlementAccount"))).findElements(By.id(dealCheckerLocator.getString("table-GenericTable")));
					
					for(int innerIndex = 0; innerIndex <accountEntitlementRecords.size(); innerIndex++) {
						accountEntitlementRangeFromList.add(accountEntitlementRecords.get(innerIndex).findElements(By.tagName("td")).get(0).getText().split("\\.")[0].substring(1));
					}
				}
			}		
			
			Iterator<Object> entitlementIterator = entitlements.iterator();			
			while(entitlementIterator.hasNext()) {
				JSONObject entitlement = (JSONObject) entitlementIterator.next();				
				if(entitlement.getString("entitlement_type").equals("Deal")) {
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(dealCheckerLocator.getString("select-EntitlementTab"))), "DEAL");
					utility.waitForProgressBarToLoad(driver);
					if(dealEntitlementRangeFromList.contains(entitlement.getString("entitlement_ranges_from"))) {
						step.log(Status.INFO, "Entitlement of type: "+ entitlement.getString("entitlement_type")+ " added to deal");
						report.addStepInfoScreenshot(driver, step);
					}
					else {
						step.log(Status.FAIL, "Entitlement of type: "+entitlement.getString("entitlement_type")+ " not added to deal");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Entitlement of type: "+entitlement.getString("entitlement_type")+ " not added to deal");					
					}
				}
				else if(entitlement.getString("entitlement_type").equals("Account")) {
					utility.selectFromDivMenu(driver, step, driver.findElement(By.id(dealCheckerLocator.getString("select-EntitlementTab"))), "ACCOUNT");
					utility.waitForProgressBarToLoad(driver);
					if(accountEntitlementRangeFromList.contains(entitlement.getString("entitlement_ranges_from"))) {
						step.log(Status.INFO, "Entitlement of type: "+ entitlement.getString("entitlement_type")+ " added to deal");
						report.addStepInfoScreenshot(driver, step);
					}
					else {
						step.log(Status.FAIL, "Entitlement of type: "+entitlement.getString("entitlement_type")+ " not added to deal");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Entitlement of type: "+entitlement.getString("entitlement_type")+ " not added to deal");					
					}
				}				
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while verifying Entitlements tab");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while verifying Entitlements tab");
		}		
	}
	
	private void verifyBudgetGroupTab(WebDriver driver, ExtentTest step, JSONArray budgetGroups) throws Exception {
		try {
			driver.findElement(By.id(dealCheckerLocator.getString("tab-BudgetWizard"))).click();
			utility.waitForProgressBarToLoad(driver);
			
			List<WebElement> budgetGroupCards = driver.findElement(By.id(dealCheckerLocator.getString("card-BudgetGroupCard"))).findElements(By.xpath(dealCheckerLocator.getString("label-BudgetGroupName")));
			List<String> budgetGroupNameList = new ArrayList<String>();
			for(int index = 0; index <budgetGroupCards.size(); index++) {
				budgetGroupNameList.add(budgetGroupCards.get(index).getText());
			}
			
			Iterator<Object> budgetGroupIterator = budgetGroups.iterator();
			
			while(budgetGroupIterator.hasNext()) {
				JSONObject budgetGroup = (JSONObject) budgetGroupIterator.next();
				if(budgetGroupNameList.contains(budgetGroup.getString("budget_group_name"))) {
					step.log(Status.INFO, "Budget Group: "+ budgetGroup.getString("budget_group_name")+ " added to deal");
					report.addStepInfoScreenshot(driver, step);
				}
				else {
					step.log(Status.FAIL, "Budget Group: "+budgetGroup.getString("budget_group_name")+ " not added to deal");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Budget Group: "+budgetGroup.getString("budget_group_name")+ " not added to deal");					
				}
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while verifying Budget Group tab");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while verifying Budget Group tab");
		}		
	}
	
	private void verifySummaryTab(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {
		boolean result = true;
		try {		
			driver.findElement(By.id(dealCheckerLocator.getString("tab-SummaryWizard"))).click();
			Thread.sleep(2000);
			
			WebElement dealStartsOn = driver.findElement(By.id(dealCheckerLocator.getString("label-SummaryDealStartsOn")));
			utility.scrollIntoView(driver, dealStartsOn);
			step.log(Status.INFO, "Deal Starts On - Expected: "+deal.getString("starts_on")+" Actual: "+dealStartsOn.getText()+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			if(!deal.getString("starts_on").equals(dealStartsOn.getText())) {				
				result = false;				
			}
			
			if(deal.has("ends_on")){
				WebElement dealEndsOn = driver.findElement(By.id(dealCheckerLocator.getString("label-SummaryDealEndsOn")));
				utility.scrollIntoView(driver, dealEndsOn);
				step.log(Status.INFO, "Deal Ends On - Expected: "+deal.getString("ends_on")+" Actual: "+dealEndsOn.getText()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				if(!deal.getString("ends_on").equals(dealEndsOn.getText())) {					
					result = false;				
				}
			}
			
			WebElement dealCountry = driver.findElement(By.id(dealCheckerLocator.getString("label-SummaryDealCountry")));
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
			
			WebElement dealTimezone = driver.findElement(By.id(dealCheckerLocator.getString("label-SummaryDealTimezone")));
			utility.scrollIntoView(driver, dealTimezone);
			step.log(Status.INFO, "Deal Timezone - Expected: "+deal.getString("timezone")+" Actual: "+dealTimezone.getText()+" (See below image)");
			report.addStepInfoScreenshot(driver, step);
			if(!deal.getString("timezone").contains(dealTimezone.getText())) {				
				result = false;				
			}
			
			if(!result) {
				step.log(Status.FAIL, "Deal: "+deal.getString("name")+" assertion failed");
				throw new Exception("Deal: "+deal.getString("name")+" assertion failed");
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while asserting details of Deal");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while asserting details of Deal");
		}		
	}
	
	public DealCheckerPage(LocatorFactory locators){	
		dealCheckerLocator = locators.getLocators("DealCheckerPage");	
	}
	
	public void validateDeals(WebDriver driver, ExtentTest step) throws Exception {		
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				if(deal.getBoolean("deal_submit")) {
					
					utility.waitForElementToBeDisplayed(driver, By.id(dealCheckerLocator.getString("button-DealMenuButton")), 10);
					
					Select dealSearchType = new Select(driver.findElement(By.id(dealCheckerLocator.getString("select-DealSearchType"))));
					dealSearchType.selectByVisibleText("Deal Name");
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).clear();
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).sendKeys(deal.getString("name"));
					driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearch"))).click();
					
					Thread.sleep(2000);

					List <String> dealTitleList = getDealTitleList(driver, step);				
					String dealTitle = deal.getString("name").toLowerCase().trim();
					
					if(dealTitleList.contains(dealTitle)) {
						dealDetails.put(deal.getString("name").toLowerCase().trim(),driver.findElements(By.xpath(dealCheckerLocator.getString("card-DealDetail"))).get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText());
						driver.findElements(By.id(dealCheckerLocator.getString("button-DealMenuButton"))).get(dealTitleList.indexOf(dealTitle)).click();
						driver.findElement(By.id(dealCheckerLocator.getString("menu-DealPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						
						Thread.sleep(1000);
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-DealReviewPanel"))).click();
						
						if(deal.has("accounts")) {
							verifyAccountsTab(driver, step, deal.getJSONArray("accounts"));										
						}
						
						if(deal.has("parties")) {
							verifyPartiesTab(driver, step, deal.getJSONArray("parties"));						
						}
						
						if(deal.has("scheduled_fees")) {
							verifyScheduledFeesTab(driver, step, deal.getJSONArray("scheduled_fees"));
						}
						
						if(deal.has("entitlements")) {
							verifyEntitlmentsTab(driver, step, deal.getJSONArray("entitlements"));
						}
						
						if(deal.has("budget_groups")) {
							verifyBudgetGroupTab(driver, step, deal.getJSONArray("budget_groups"));
						}
						verifySummaryTab(driver, step, deal);						
					}
					else {
						step.log(Status.FAIL, "Deal "+deal.getString("name")+"  not available at Deal Checker page");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Deal "+deal.getString("name")+"  not available at Deal Checker page");
					}					
				}
				else {
					step.log(Status.INFO, "Deal "+deal.getString("name")+" is not submitted to Deal Checker");
					report.addStepInfoScreenshot(driver, step);
				}
				
			}
	
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while validating deal: "+deal.getString("name"));
			}
		}
		
	}
	
	public void approveDeals(WebDriver driver, ExtentTest step) throws Exception {		
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				if(deal.getBoolean("deal_submit")) {
					
					utility.waitForElementToBeDisplayed(driver, By.id(dealCheckerLocator.getString("button-DealMenuButton")), 10);
					
					Select dealSearchType = new Select(driver.findElement(By.id(dealCheckerLocator.getString("select-DealSearchType"))));
					dealSearchType.selectByVisibleText("Deal Name");
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).clear();
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).sendKeys(deal.getString("name"));
					driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearch"))).click();
					
					Thread.sleep(2000);

					List <String> dealTitleList = getDealTitleList(driver, step);				
					String dealTitle = deal.getString("name").toLowerCase().trim();
					
					if(dealTitleList.contains(dealTitle)) {
						dealDetails.put(deal.getString("name").toLowerCase().trim(),driver.findElements(By.xpath(dealCheckerLocator.getString("card-DealDetail"))).get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText());
						driver.findElements(By.id(dealCheckerLocator.getString("button-DealMenuButton"))).get(dealTitleList.indexOf(dealTitle)).click();
						driver.findElement(By.id(dealCheckerLocator.getString("menu-DealPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						
						Thread.sleep(1000);
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-DealReviewPanel"))).click();
						
						if(deal.has("accounts")) {
							verifyAccountsTab(driver, step, deal.getJSONArray("accounts"));										
						}
						
						if(deal.has("parties")) {
							verifyPartiesTab(driver, step, deal.getJSONArray("parties"));						
						}
						
						if(deal.has("scheduled_fees")) {
							verifyScheduledFeesTab(driver, step, deal.getJSONArray("scheduled_fees"));
						}
						
						if(deal.has("entitlements")) {
							verifyEntitlmentsTab(driver, step, deal.getJSONArray("entitlements"));
						}
						
						if(deal.has("budget_groups")) {
							verifyBudgetGroupTab(driver, step, deal.getJSONArray("budget_groups"));
						}
						verifySummaryTab(driver, step, deal);						
						
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-DealReviewPanel"))).click();
						Thread.sleep(500);
						driver.findElement(By.id(dealCheckerLocator.getString("radio-DealApproveReject"))).click();
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-NewDealNote"))).click();
						
						driver.findElement(By.id(dealCheckerLocator.getString("input-DealNote"))).sendKeys("Approved");
						driver.findElement(By.id(dealCheckerLocator.getString("button-AddDealNote"))).click();
						
						driver.findElement(By.id(dealCheckerLocator.getString("button-ApproveDeal"))).click();
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealYes"))).click();
						
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK")));
						String response = utility.extractTitleMessage(driver, step, By.id(dealCheckerLocator.getString("label-Title")));					
						
						if(response.toLowerCase().contains("something went wrong")) {
							step.log(Status.FAIL, "Deal "+deal.getString("name")+" approval failed");
							report.addStepFailScreenshot(driver, step);	
							throw new Exception("Deal "+deal.getString("name")+" approval failed");			
						}
						else {
							step.log(Status.PASS, "Deal "+deal.getString("name")+" approved");
							report.addStepPassScreenshot(driver, step);	
							
							driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK"))).click();
						}
						
					}
					else {
						step.log(Status.FAIL, "Deal "+deal.getString("name")+"  not available at Deal Checker page");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Deal "+deal.getString("name")+"  not available at Deal Checker page");
					}
					driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearchClearFiler"))).click();
				}
				else {
					step.log(Status.INFO, "Deal "+deal.getString("name")+" is not submitted to Deal Checker");
					report.addStepInfoScreenshot(driver, step);
				}
				
			}
	
			catch (Exception e) {
				step.log(Status.FAIL, "Error while approving deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while approving deal: "+deal.getString("name"));
			}
		}
		
	}
	
	public void approveDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {
		try {
			if(deal.getBoolean("deal_submit")) {
				
				utility.waitForElementToBeDisplayed(driver, By.id(dealCheckerLocator.getString("button-DealMenuButton")), 10);
				
				Select dealSearchType = new Select(driver.findElement(By.id(dealCheckerLocator.getString("select-DealSearchType"))));
				dealSearchType.selectByVisibleText("Deal Name");
				driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).clear();
				driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).sendKeys(deal.getString("name"));
				driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearch"))).click();
				
				Thread.sleep(2000);

				List <String> dealTitleList = getDealTitleList(driver, step);				
				String dealTitle = deal.getString("name").toLowerCase().trim();
				
				if(dealTitleList.contains(dealTitle)) {
					dealDetails.put(deal.getString("name").toLowerCase().trim(),driver.findElements(By.xpath(dealCheckerLocator.getString("card-DealDetail"))).get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText());
					driver.findElements(By.id(dealCheckerLocator.getString("button-DealMenuButton"))).get(dealTitleList.indexOf(dealTitle)).click();
					driver.findElement(By.id(dealCheckerLocator.getString("menu-DealPopupMenu"))).findElements(By.tagName("div")).get(1).click();
					
					Thread.sleep(1000);
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-DealReviewPanel"))).click();
					
					if(deal.has("accounts")) {
						verifyAccountsTab(driver, step, deal.getJSONArray("accounts"));										
					}
					
					if(deal.has("parties")) {
						verifyPartiesTab(driver, step, deal.getJSONArray("parties"));						
					}
					
					if(deal.has("scheduled_fees")) {
						verifyScheduledFeesTab(driver, step, deal.getJSONArray("scheduled_fees"));
					}
					
					if(deal.has("entitlements")) {
						verifyEntitlmentsTab(driver, step, deal.getJSONArray("entitlements"));
					}
					
					if(deal.has("budget_groups")) {
						verifyBudgetGroupTab(driver, step, deal.getJSONArray("budget_groups"));
					}
					verifySummaryTab(driver, step, deal);						
					
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-DealReviewPanel"))).click();
					Thread.sleep(500);
					driver.findElement(By.id(dealCheckerLocator.getString("radio-DealApproveReject"))).click();
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-NewDealNote"))).click();
					
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealNote"))).sendKeys("Approved");
					driver.findElement(By.id(dealCheckerLocator.getString("button-AddDealNote"))).click();
					
					driver.findElement(By.id(dealCheckerLocator.getString("button-ApproveDeal"))).click();
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealYes"))).click();
					
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK")));
					String response = utility.extractTitleMessage(driver, step, By.id(dealCheckerLocator.getString("label-Title")));					
					
					if(response.toLowerCase().contains("something went wrong")) {
						step.log(Status.FAIL, "Deal "+deal.getString("name")+" approval failed");
						report.addStepFailScreenshot(driver, step);	
						throw new Exception("Deal "+deal.getString("name")+" approval failed");			
					}
					else {
						step.log(Status.PASS, "Deal "+deal.getString("name")+" approved");
						report.addStepPassScreenshot(driver, step);	
						
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK"))).click();
					}
				}
				else {
					step.log(Status.FAIL, "Deal "+deal.getString("name")+"  not available at Deal Checker page");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Deal "+deal.getString("name")+"  not available at Deal Checker page");
				}
				driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearchClearFiler"))).click();
			}
			else {
				step.log(Status.INFO, "Deal "+deal.getString("name")+" is not submitted to Deal Checker");
				report.addStepInfoScreenshot(driver, step);
			}
			
		}

		catch (Exception e) {
			step.log(Status.FAIL, "Error while approving deal: "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());				
			throw new Exception("Error while approving deal: "+deal.getString("name"));
		}		
	}
	
	public void rejectDeals(WebDriver driver, ExtentTest step) throws Exception {		
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				if(deal.getBoolean("deal_submit")) {
					
					utility.waitForElementToBeDisplayed(driver, By.id(dealCheckerLocator.getString("button-DealMenuButton")), 10);
					
					Select dealSearchType = new Select(driver.findElement(By.id(dealCheckerLocator.getString("select-DealSearchType"))));
					dealSearchType.selectByVisibleText("Deal Name");
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).clear();
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).sendKeys(deal.getString("name"));
					driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearch"))).click();
					
					Thread.sleep(2000);

					List <String> dealTitleList = getDealTitleList(driver, step);				
					String dealTitle = deal.getString("name").toLowerCase().trim();
					
					if(dealTitleList.contains(dealTitle)) {
						dealDetails.put(deal.getString("name").toLowerCase().trim(),driver.findElements(By.xpath(dealCheckerLocator.getString("card-DealDetail"))).get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText());
						driver.findElements(By.id(dealCheckerLocator.getString("button-DealMenuButton"))).get(dealTitleList.indexOf(dealTitle)).click();
						driver.findElement(By.id(dealCheckerLocator.getString("menu-DealPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						
						Thread.sleep(1000);
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-DealReviewPanel"))).click();
						
						if(deal.has("accounts")) {
							verifyAccountsTab(driver, step, deal.getJSONArray("accounts"));										
						}
						
						if(deal.has("parties")) {
							verifyPartiesTab(driver, step, deal.getJSONArray("parties"));						
						}
						
						if(deal.has("scheduled_fees")) {
							verifyScheduledFeesTab(driver, step, deal.getJSONArray("scheduled_fees"));
						}
						
						if(deal.has("entitlements")) {
							verifyEntitlmentsTab(driver, step, deal.getJSONArray("entitlements"));
						}
						
						if(deal.has("budget_groups")) {
							verifyBudgetGroupTab(driver, step, deal.getJSONArray("budget_groups"));
						}
						
						verifySummaryTab(driver, step, deal);						
						
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-DealReviewPanel"))).click();
						Thread.sleep(500);
						driver.findElements(By.id(dealCheckerLocator.getString("radio-DealApproveReject"))).get(1).click();
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-NewDealNote"))).click();
						
						driver.findElement(By.id(dealCheckerLocator.getString("input-DealNote"))).sendKeys("Rejected");
						driver.findElement(By.id(dealCheckerLocator.getString("button-AddDealNote"))).click();
						
						driver.findElement(By.id(dealCheckerLocator.getString("button-RejectDeal"))).click();
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealYes"))).click();
						
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK")));
						step.log(Status.PASS, "Deal "+deal.getString("name")+" approved");
						report.addStepPassScreenshot(driver, step);	
						
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK"))).click();
					}
					else {
						step.log(Status.FAIL, "Deal "+deal.getString("name")+"  not available at Deal Checker page");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Deal "+deal.getString("name")+"  not available at Deal Checker page");
					}
					driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearchClearFiler"))).click();
				}
				else {
					step.log(Status.INFO, "Deal "+deal.getString("name")+" is not submitted to Deal Checker");
					report.addStepInfoScreenshot(driver, step);
				}
				
			}
	
			catch (Exception e) {
				step.log(Status.FAIL, "Error while rejecting deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while rejecting deal: "+deal.getString("name"));
			}
		}
		
	}
	
	public void rejectDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {
		try {
			if(deal.getBoolean("deal_submit")) {
				
				utility.waitForElementToBeDisplayed(driver, By.id(dealCheckerLocator.getString("button-DealMenuButton")), 10);
				
				Select dealSearchType = new Select(driver.findElement(By.id(dealCheckerLocator.getString("select-DealSearchType"))));
				dealSearchType.selectByVisibleText("Deal Name");
				driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).clear();
				driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).sendKeys(deal.getString("name"));
				driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearch"))).click();
				
				Thread.sleep(2000);

				List <String> dealTitleList = getDealTitleList(driver, step);				
				String dealTitle = deal.getString("name").toLowerCase().trim();
				
				if(dealTitleList.contains(dealTitle)) {
					dealDetails.put(deal.getString("name").toLowerCase().trim(),driver.findElements(By.xpath(dealCheckerLocator.getString("card-DealDetail"))).get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText());
					driver.findElements(By.id(dealCheckerLocator.getString("button-DealMenuButton"))).get(dealTitleList.indexOf(dealTitle)).click();
					driver.findElement(By.id(dealCheckerLocator.getString("menu-DealPopupMenu"))).findElements(By.tagName("div")).get(1).click();
					
					Thread.sleep(1000);
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-DealReviewPanel"))).click();
					
					if(deal.has("accounts")) {
						verifyAccountsTab(driver, step, deal.getJSONArray("accounts"));										
					}
					
					if(deal.has("parties")) {
						verifyPartiesTab(driver, step, deal.getJSONArray("parties"));						
					}
					
					if(deal.has("scheduled_fees")) {
						verifyScheduledFeesTab(driver, step, deal.getJSONArray("scheduled_fees"));
					}
					
					if(deal.has("entitlements")) {
						verifyEntitlmentsTab(driver, step, deal.getJSONArray("entitlements"));
					}
					
					if(deal.has("budget_groups")) {
						verifyBudgetGroupTab(driver, step, deal.getJSONArray("budget_groups"));
					}
					
					verifySummaryTab(driver, step, deal);						
					
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-DealReviewPanel"))).click();
					Thread.sleep(500);
					driver.findElements(By.id(dealCheckerLocator.getString("radio-DealApproveReject"))).get(1).click();
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-NewDealNote"))).click();
					
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealNote"))).sendKeys("Rejected");
					driver.findElement(By.id(dealCheckerLocator.getString("button-AddDealNote"))).click();
					
					driver.findElement(By.id(dealCheckerLocator.getString("button-RejectDeal"))).click();
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealYes"))).click();
					
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK")));
					step.log(Status.PASS, "Deal "+deal.getString("name")+" rejected");
					report.addStepPassScreenshot(driver, step);	
					
					driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK"))).click();
				}
				else {
					step.log(Status.FAIL, "Deal "+deal.getString("name")+"  not available at Deal Checker page");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Deal "+deal.getString("name")+"  not available at Deal Checker page");
				}
				driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearchClearFiler"))).click();
			}
			else {
				step.log(Status.INFO, "Deal "+deal.getString("name")+" is not submitted to Deal Checker");
				report.addStepInfoScreenshot(driver, step);
			}
			
		}

		catch (Exception e) {
			step.log(Status.FAIL, "Error while rejecting deal: "+deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());				
			throw new Exception("Error while rejecting deal: "+deal.getString("name"));
		}		
	}
	
	public void approveUpdatedDeals(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("edit_deals");
		Iterator<Object> dealIterator = deals.iterator();
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				if(deal.getBoolean("deal_submit")) {
					
					utility.waitForElementToBeDisplayed(driver, By.id(dealCheckerLocator.getString("button-DealMenuButton")), 10);
					
					Select dealSearchType = new Select(driver.findElement(By.id(dealCheckerLocator.getString("select-DealSearchType"))));
					dealSearchType.selectByVisibleText("Deal Name");
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).clear();
					driver.findElement(By.id(dealCheckerLocator.getString("input-DealSearchParameter"))).sendKeys(deal.getString("name"));
					driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearch"))).click();				
					
					Thread.sleep(2000);
	
					List <String> dealTitleList = getDealTitleList(driver, step);				
					String dealTitle = deal.getString("name").toLowerCase().trim();
					
					if(dealTitleList.contains(dealTitle)) {
						dealDetails.put(deal.getString("name").toLowerCase().trim(),driver.findElements(By.xpath(dealCheckerLocator.getString("card-DealDetail"))).get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText());
						driver.findElements(By.id(dealCheckerLocator.getString("button-DealMenuButton"))).get(dealTitleList.indexOf(dealTitle)).click();
						driver.findElement(By.id(dealCheckerLocator.getString("menu-DealPopupMenu"))).findElements(By.tagName("div")).get(1).click();
						
						Thread.sleep(500);
						driver.findElement(By.id(dealCheckerLocator.getString("radio-DealApproveReject"))).click();
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-NewDealNote"))).click();
						
						driver.findElement(By.id(dealCheckerLocator.getString("input-DealNote"))).sendKeys("Approved");
						driver.findElement(By.id(dealCheckerLocator.getString("button-AddDealNote"))).click();
						
						
						driver.findElement(By.id(dealCheckerLocator.getString("button-ApproveDeal"))).click();
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealYes"))).click();
						
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK")));
						step.log(Status.PASS, "Deal "+deal.getString("name")+" approved");
						report.addStepPassScreenshot(driver, step);
						
						
						driver.findElement(By.xpath(dealCheckerLocator.getString("button-ApproveDealOK"))).click();
					}
					else {
						step.log(Status.FAIL, "Deal "+deal.getString("name")+"  not available at Deal Checker page");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Deal "+deal.getString("name")+"  not available at Deal Checker page");
					}
					driver.findElement(By.id(dealCheckerLocator.getString("button-DealSearchClearFiler"))).click();
				}
				else {
					step.log(Status.INFO, "Deal "+deal.getString("name")+" is not submitted to Deal Checker");
					report.addStepInfoScreenshot(driver, step);
				}
			}
	
			catch (Exception e) {
				step.log(Status.FAIL, "Error while approving deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while approving deal: "+deal.getString("name"));				
			}
		}
	}
	
}
