package com.appveen.smartcontracts.pageobject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class PartySummaryPage {
	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject partySummaryLocator = null;
	private LandingPage landingPage = null;
	private PartyMakerPage partyMakerPage = null;
	private ArrayList<String> actualResponseMessages = new ArrayList<String>();
	
	public PartySummaryPage(LocatorFactory locators) {
		partySummaryLocator = locators.getLocators("PartySummaryPage");
		landingPage = new LandingPage(locators);
		partyMakerPage = new PartyMakerPage(locators);
	}
	
	public void validateResponseGenerated(ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();		
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {
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
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating party response messages");
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while validating party response messages");
			}
		}
	}
	
	public void validatePartiesDisplayed(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();
		
		while(partyIterator.hasNext()) {			
			JSONObject party = (JSONObject) partyIterator.next();
			try {
				if(party.getBoolean("party_submit")) {
					utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120);
					Select partySearchType = new Select(driver.findElement(By.id(partySummaryLocator.getString("select-SearchType"))));
					partySearchType.selectByVisibleText("Name");
					driver.findElement(By.id(partySummaryLocator.getString("input-SearchParameter"))).sendKeys(party.getString("party_name"));
					driver.findElement(By.id(partySummaryLocator.getString("button-Search"))).click();
					utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120);
					
					List<String> partyNameList =  utility.extractColumnsFromTable(driver, step, By.id(partySummaryLocator.getString("table-SearchResult")), 2);
					
					if(partyNameList.contains(party.getString("party_name"))) {
						step.log(Status.PASS, "Party: "+party.getString("party_name")+" available at Party Summary Page (See image below)");
						report.addStepPassScreenshot(driver, step);
					}
					else {
						step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not available at Party Summary Page (See image below)");
						report.addStepFailScreenshot(driver, step);
					}
					driver.findElement(By.id(partySummaryLocator.getString("button-SearchClearFiler"))).click();
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating Party: "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while validating Party: "+party.getString("party_name"));
			}
		}
	}
	
	public void validateDealPartiesDisplayed(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
		
			JSONArray parties = deal.getJSONArray("parties");
			Iterator<Object> partyIterator = parties.iterator();
			
			while(partyIterator.hasNext()) {			
				JSONObject party = (JSONObject) partyIterator.next();
				try {
					utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120);
					Select partySearchType = new Select(driver.findElement(By.id(partySummaryLocator.getString("select-SearchType"))));
					partySearchType.selectByVisibleText("Name");
					driver.findElement(By.id(partySummaryLocator.getString("input-SearchParameter"))).sendKeys(party.getString("party_name"));
					driver.findElement(By.id(partySummaryLocator.getString("button-Search"))).click();
					utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120);
					
					List<String> partyNameList =  utility.extractColumnsFromTable(driver, step, By.id(partySummaryLocator.getString("table-SearchResult")), 2);
					
					if(partyNameList.contains(party.getString("party_name"))) {
						step.log(Status.PASS, "Party: "+party.getString("party_name")+" available at Party Summary Page (See image below)");
						report.addStepPassScreenshot(driver, step);
					}
					else {
						step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not available at Party Summary Page (See image below)");
						report.addStepFailScreenshot(driver, step);
					}
					driver.findElement(By.id(partySummaryLocator.getString("button-SearchClearFiler"))).click();					
				}
				catch (Exception e) {
					step.log(Status.FAIL, "Error while validating Party: "+party.getString("party_name"));
					report.addStepFailScreenshot(driver, step);
					step.log(Status.FAIL, e.getMessage());
					throw new Exception("Error while validating Party: "+party.getString("party_name"));
				}
			}
		}
	}
	
	public void validateDealPartiesNotDisplayed(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
		
			JSONArray parties = deal.getJSONArray("parties");
			Iterator<Object> partyIterator = parties.iterator();
			
			while(partyIterator.hasNext()) {			
				JSONObject party = (JSONObject) partyIterator.next();
				try {
					
					utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120);
					Select partySearchType = new Select(driver.findElement(By.id(partySummaryLocator.getString("select-SearchType"))));
					partySearchType.selectByVisibleText("Name");
					driver.findElement(By.id(partySummaryLocator.getString("input-SearchParameter"))).sendKeys(party.getString("party_name"));
					driver.findElement(By.id(partySummaryLocator.getString("button-Search"))).click();
					utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120);
					
					List<String> partyNameList =  utility.extractColumnsFromTable(driver, step, By.id(partySummaryLocator.getString("table-SearchResult")), 2);
					
					if(!partyNameList.contains(party.getString("party_name"))) {
						step.log(Status.PASS, "Party: "+party.getString("party_name")+" not available at Party Summary Page (See image below)");
						report.addStepPassScreenshot(driver, step);
					}
					else {
						step.log(Status.FAIL, "Party: "+party.getString("party_name")+" available at Party Summary Page (See image below)");
						report.addStepFailScreenshot(driver, step);
					}
					driver.findElement(By.id(partySummaryLocator.getString("button-SearchClearFiler"))).click();
				}				
				catch (Exception e) {
					step.log(Status.FAIL, "Error while validating Party: "+party.getString("party_name"));
					report.addStepFailScreenshot(driver, step);
					step.log(Status.FAIL, e.getMessage());
					throw new Exception("Error while validating Party: "+party.getString("party_name"));
				}
			}
		}
	}
	
	public void editParties(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("edit_parties");
		Iterator<Object> partyIterator = parties.iterator();
		
		while(partyIterator.hasNext()) {
			landingPage.goToPartySummary(driver, step);
			JSONObject party = (JSONObject) partyIterator.next();
			try {
				utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120);
				Select partySearchType = new Select(driver.findElement(By.id(partySummaryLocator.getString("select-SearchType"))));
				partySearchType.selectByVisibleText("Name");
				driver.findElement(By.id(partySummaryLocator.getString("input-SearchParameter"))).sendKeys(party.getString("party_name"));
				driver.findElement(By.id(partySummaryLocator.getString("button-Search"))).click();
				utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120);
				
				List<String> partyNameList =  utility.extractColumnsFromTable(driver, step, By.id(partySummaryLocator.getString("table-SearchResult")), 2);
				int partyIndex;
				if(partyNameList.contains(party.getString("party_name"))) {
					partyIndex = partyNameList.indexOf(party.getString("party_name"));
					driver.findElements(By.id(partySummaryLocator.getString("table-SearchResult"))).get(partyIndex).findElements(By.tagName("td")).get(5).findElement(By.tagName("i")).click();
					partyMakerPage.editParty(driver, step, party);
				}
				else {
					step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not available at Party Summary Page (See image below)");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Party: "+party.getString("party_name")+" not available at Party Summary Page");
				}
				//driver.findElement(By.id(partySummaryLocator.getString("button-SearchClearFiler"))).click();
				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party: "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party: "+party.getString("party_name"));
			}
		}
	}
	
	public void initiatePartyEdit(WebDriver driver, ExtentTest step) throws JSONException, Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();
		
		while(partyIterator.hasNext()) {
			landingPage.goToPartySummary(driver, step);
			JSONObject party = (JSONObject) partyIterator.next();
			try {
				utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120); 
				Select partySearchType = new Select(driver.findElement(By.id(partySummaryLocator.getString("select-SearchType"))));
				partySearchType.selectByVisibleText("Name");
				driver.findElement(By.id(partySummaryLocator.getString("input-SearchParameter"))).sendKeys(party.getString("party_name"));
				driver.findElement(By.id(partySummaryLocator.getString("button-Search"))).click();
				utility.waitForElementToBeDisplayed(driver, By.id(partySummaryLocator.getString("table-SearchResult")), 120);
				
				List<String> partyNameList =  utility.extractColumnsFromTable(driver, step, By.id(partySummaryLocator.getString("table-SearchResult")), 2);
				int partyIndex;
				if(partyNameList.contains(party.getString("party_name"))) {
					partyIndex = partyNameList.indexOf(party.getString("party_name"));
					driver.findElements(By.id(partySummaryLocator.getString("table-SearchResult"))).get(partyIndex).findElements(By.tagName("td")).get(5).findElement(By.tagName("i")).click();
					
					String responseMessage = utility.extractTitleMessage(driver, step, By.id(partySummaryLocator.getString("label-Title")));
					actualResponseMessages.add(responseMessage);					
					driver.findElement(By.xpath(partySummaryLocator.getString("button-OK"))).click();
					
				}
				else {
					step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not available at Party Summary Page (See image below)");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Party: "+party.getString("party_name")+" not available at Party Summary Page");
				}
				//driver.findElement(By.id(partySummaryLocator.getString("button-SearchClearFiler"))).click();
				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while editing Party: "+party.getString("party_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while editing Party: "+party.getString("party_name"));
			}
		}
	}
}
