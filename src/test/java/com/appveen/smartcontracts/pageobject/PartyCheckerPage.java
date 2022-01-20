package com.appveen.smartcontracts.pageobject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class PartyCheckerPage {
	 
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject partyCheckerLocator = null;
	public static ArrayList<String> actualResponseMessages = new ArrayList<String>();
	
		
	public PartyCheckerPage(LocatorFactory locators) {
		partyCheckerLocator = locators.getLocators("PartyCheckerPage");			
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
	
	public void approveParties(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			String partyName = null;
			
			if(party.getBoolean("party_submit")) {
				partyName = party.getString("party_name");				
				try {	
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(partyName);
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					
					String resultParty = driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
					
					if(resultParty.equals(partyName.trim().toLowerCase())) {
						step.log(Status.INFO, "Party: "+partyName+" available at Party Checker Queue");
						
						driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyManage"))).get(1).findElement(By.tagName("i")).click();
						
						Thread.sleep(1000);
						String partyCustomerId = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyCustomerID"))).getAttribute("value");
						String Name = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyName"))).getAttribute("value");
						step.log(Status.INFO, "Party customer ID: "+partyCustomerId+", Party name:"+Name+" (See below image)");					
						report.addStepInfoScreenshot(driver, step);
						
						if(party.has("party_contacts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(1).click();
							List<String> partyContacts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party contact(s) added: "+partyContacts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);												
						}
						
						if(party.has("party_accounts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
							List<String> partyAccounts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party account(s) added: "+partyAccounts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						if(party.has("party_documents")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
							List<String> partyDocuments = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 0);
							step.log(Status.INFO, "Party document(s) added: "+partyDocuments.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
						driver.findElement(By.tagName("textarea")).sendKeys("Approved");
						
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-Approve"))).click();				
						step.log(Status.INFO, "Party: "+partyName+" approved");
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-Yes"))).click();
						
					}
					else {
						step.log(Status.FAIL, "Party: "+partyName+" not available at Party Checker Queue");
						report.addStepFailScreenshot(driver, step);					
						throw new Exception("Party: "+partyName+" not available at Party Checker Queue");
					}
				
				}
				catch (Exception e) {
					step.log(Status.FAIL, "Error while approving Party: "+partyName);
					report.addStepFailScreenshot(driver, step);
					step.log(Status.FAIL, e.getMessage());
					throw new Exception("Error while approving Party: "+partyName);
				}
			}
		}
	}
	
	public void approveUpdatedParties(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("edit_parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			String partyName = null;
			
			if(party.getBoolean("party_submit")) {
				if(party.has("party_basic_details")) {
					partyName = party.getJSONArray("party_basic_details").getJSONObject(0).getString("party_name");
				}
				else {
					partyName = party.getString("party_name");
				}			
				try {	
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(partyName);
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					
					String resultParty = driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
					
					if(resultParty.equals(partyName.trim().toLowerCase())) {
						step.log(Status.INFO, "Party: "+partyName+" available at Party Checker Queue");
						
						driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyManage"))).get(1).findElement(By.tagName("i")).click();
						
						Thread.sleep(1000);
						String partyCustomerId = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyCustomerID"))).getAttribute("value");
						String Name = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyName"))).getAttribute("value");
						step.log(Status.INFO, "Party customer ID: "+partyCustomerId+", Party name:"+Name+" (See below image)");					
						report.addStepInfoScreenshot(driver, step);
						
						if(party.has("party_contacts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(1).click();
							List<String> partyContacts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party contact(s) added: "+partyContacts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);												
						}
						
						if(party.has("party_accounts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
							List<String> partyAccounts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party account(s) added: "+partyAccounts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						if(party.has("party_documents")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
							List<String> partyDocuments = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 0);
							step.log(Status.INFO, "Party document(s) added: "+partyDocuments.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
						driver.findElement(By.tagName("textarea")).sendKeys("Approved");
						
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-Approve"))).click();				
						step.log(Status.INFO, "Party: "+partyName+" approved");
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-Yes"))).click();
						
					}
					else {
						step.log(Status.FAIL, "Party: "+partyName+" not available at Party Checker Queue");
						report.addStepFailScreenshot(driver, step);					
						throw new Exception("Party: "+partyName+" not available at Party Checker Queue");
					}
				
				}
				catch (Exception e) {
					step.log(Status.FAIL, "Error while approving Party: "+partyName);
					report.addStepFailScreenshot(driver, step);
					step.log(Status.FAIL, e.getMessage());
					throw new Exception("Error while approving Party: "+partyName);
				}
			}
		}
	}
	
	public void approvePartiesWithoutComment(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			String partyName = null;
			
			if(party.getBoolean("party_submit")) {
				partyName = party.getString("party_name");
				
				try {	
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(partyName);
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					
					String resultParty = driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
					
					if(resultParty.equals(partyName.trim().toLowerCase())) {
						step.log(Status.INFO, "Party: "+partyName+" available at Party Checker Queue");
						
						driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyManage"))).get(1).findElement(By.tagName("i")).click();
						
						Thread.sleep(1000);
						String partyCustomerId = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyCustomerID"))).getAttribute("value");
						String Name = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyName"))).getAttribute("value");
						step.log(Status.INFO, "Party customer ID: "+partyCustomerId+", Party name:"+Name+" (See below image)");					
						report.addStepInfoScreenshot(driver, step);
						
						if(party.has("party_contacts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(1).click();
							List<String> partyContacts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party contact(s) added: "+partyContacts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);												
						}
						
						if(party.has("party_accounts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
							List<String> partyAccounts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party account(s) added: "+partyAccounts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						if(party.has("party_documents")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
							List<String> partyDocuments = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 0);
							step.log(Status.INFO, "Party document(s) added: "+partyDocuments.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
							
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-Approve"))).click();
						
						String responseMessage = utility.extractTitleMessage(driver, step,  By.id(partyCheckerLocator.getString("label-Title")));
						actualResponseMessages.add(responseMessage);						
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-OK"))).click();
						
					}
					else {
						step.log(Status.FAIL, "Party: "+partyName+" not available at Party Checker Queue");
						report.addStepFailScreenshot(driver, step);					
						throw new Exception("Party: "+partyName+" not available at Party Checker Queue");
					}
				}
				catch (Exception e) {
					step.log(Status.FAIL, "Error while approving Party: "+partyName+" without comment");
					report.addStepFailScreenshot(driver, step);
					step.log(Status.FAIL, e.getMessage());
					throw new Exception("Error while approving Party: "+partyName+" without comment");
				}
			}
		}
	}
	
	public void reviewParties(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			String partyName = null;
			
			if(party.getBoolean("party_submit")) {
				partyName = party.getString("party_name");
				
				try {			
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(partyName);
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					
					String resultParty = driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
					
					if(resultParty.equals(partyName.trim().toLowerCase())) {
						step.log(Status.INFO, "Party: "+partyName+" available at Party Checker Queue");
						
						driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyManage"))).get(1).findElement(By.tagName("i")).click();
						
						Thread.sleep(1000);
						String partyCustomerId = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyCustomerID"))).getAttribute("value");
						String Name = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyName"))).getAttribute("value");
						step.log(Status.INFO, "Party customer ID: "+partyCustomerId+", Party name:"+Name+" (See below image)");					
						report.addStepInfoScreenshot(driver, step);
						
						if(party.has("party_contacts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(1).click();
							List<String> partyContacts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party contact(s) added: "+partyContacts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);												
						}
						
						if(party.has("party_accounts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
							List<String> partyAccounts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party account(s) added: "+partyAccounts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						if(party.has("party_documents")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
							List<String> partyDocuments = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 0);
							step.log(Status.INFO, "Party document(s) added: "+partyDocuments.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
						driver.findElement(By.tagName("textarea")).sendKeys("Reviewed");
						
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-SendForReview"))).click();				
						step.log(Status.INFO, "Party: "+partyName+" sent for review");
						report.addStepInfoScreenshot(driver, step);
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-Yes"))).click();
						
					}
					else {
						step.log(Status.FAIL, "Party: "+partyName+" not available at Party Checker Queue");
						report.addStepFailScreenshot(driver, step);					
						throw new Exception("Party: "+partyName+" not available at Party Checker Queue");
					}
				}
				catch (Exception e) {
					step.log(Status.FAIL, "Error while reviewing Party: "+partyName);
					report.addStepFailScreenshot(driver, step);
					step.log(Status.FAIL, e.getMessage());
					throw new Exception("Error while reviewing Party: "+partyName);
				}
			}
		}
	}
	
	public void reviewParties(WebDriver driver, ExtentTest step,  JSONObject party) throws Exception {
		String partyName = null;
		
		if(party.getBoolean("party_submit")) {
			if(party.has("party_basic_details")) {
				partyName = party.getJSONArray("party_basic_details").getJSONObject(0).getString("party_name");
			}
			else {
				partyName = party.getString("party_name");
			}
			try {			
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
				driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(partyName);
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
				
				String resultParty = driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
				
				if(resultParty.equals(partyName.trim().toLowerCase())) {
					step.log(Status.INFO, "Party: "+partyName+" available at Party Checker Queue");
					
					driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyManage"))).get(1).findElement(By.tagName("i")).click();
					
					Thread.sleep(1000);
					String partyCustomerId = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyCustomerID"))).getAttribute("value");
					String Name = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyName"))).getAttribute("value");
					step.log(Status.INFO, "Party customer ID: "+partyCustomerId+", Party name:"+Name+" (See below image)");					
					report.addStepInfoScreenshot(driver, step);
					
					if(party.has("party_contacts")) {
						driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(1).click();
						List<String> partyContacts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
						step.log(Status.INFO, "Party contact(s) added: "+partyContacts.toString()+" (See below image)");
						report.addStepInfoScreenshot(driver, step);												
					}
					
					if(party.has("party_accounts")) {
						driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
						List<String> partyAccounts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
						step.log(Status.INFO, "Party account(s) added: "+partyAccounts.toString()+" (See below image)");
						report.addStepInfoScreenshot(driver, step);
					}
					
					if(party.has("party_documents")) {
						driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
						List<String> partyDocuments = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 0);
						step.log(Status.INFO, "Party document(s) added: "+partyDocuments.toString()+" (See below image)");
						report.addStepInfoScreenshot(driver, step);
					}
					
					driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
					driver.findElement(By.tagName("textarea")).sendKeys("Reviewed");
					
					driver.findElement(By.xpath(partyCheckerLocator.getString("button-SendForReview"))).click();				
					step.log(Status.INFO, "Party: "+partyName+" sent for review");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(partyCheckerLocator.getString("button-Yes"))).click();
					
				}
				else {
					step.log(Status.FAIL, "Party: "+partyName+" not available at Party Checker Queue");
					report.addStepFailScreenshot(driver, step);					
					throw new Exception("Party: "+partyName+" not available at Party Checker Queue");
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while reviewing Party: "+partyName);
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while reviewing Party: "+partyName);
			}
		}		
	}
	
	public void reviewPartieWithoutComment(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			String partyName = null;
			if(party.getBoolean("party_submit")) {
				partyName = party.getString("party_name");
				
				try {			
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(partyName);
					utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
					
					String resultParty = driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
					
					if(resultParty.equals(partyName.trim().toLowerCase())) {
						step.log(Status.INFO, "Party: "+partyName+" available at Party Checker Queue");
						
						driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyManage"))).get(1).findElement(By.tagName("i")).click();
						
						Thread.sleep(1000);
						String partyCustomerId = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyCustomerID"))).getAttribute("value");
						String Name = driver.findElement(By.id(partyCheckerLocator.getString("input-PartyName"))).getAttribute("value");
						step.log(Status.INFO, "Party customer ID: "+partyCustomerId+", Party name:"+Name+" (See below image)");					
						report.addStepInfoScreenshot(driver, step);
						
						if(party.has("party_contacts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(1).click();
							List<String> partyContacts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party contact(s) added: "+partyContacts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);												
						}
						
						if(party.has("party_accounts")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(2).click();
							List<String> partyAccounts = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 1);
							step.log(Status.INFO, "Party account(s) added: "+partyAccounts.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						if(party.has("party_documents")) {
							driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(3).click();
							List<String> partyDocuments = utility.extractColumnsFromTable(driver, step,  By.id(partyCheckerLocator.getString("table-Party")), 0);
							step.log(Status.INFO, "Party document(s) added: "+partyDocuments.toString()+" (See below image)");
							report.addStepInfoScreenshot(driver, step);
						}
						
						driver.findElement(By.id(partyCheckerLocator.getString("tab-PartyTabSet"))).findElements(By.tagName("li")).get(4).click();
											
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-SendForReview"))).click();
						
						String responseMessage = utility.extractTitleMessage(driver, step,  By.id(partyCheckerLocator.getString("label-Title")));
						actualResponseMessages.add(responseMessage);						
						driver.findElement(By.xpath(partyCheckerLocator.getString("button-OK"))).click();
						
					}
					else {
						step.log(Status.FAIL, "Party: "+partyName+" not available at Party Checker Queue");
						report.addStepFailScreenshot(driver, step);					
						throw new Exception("Party: "+partyName+" not available at Party Checker Queue");
					}
				}
				catch (Exception e) {
					step.log(Status.FAIL, "Error while reviewing Party: "+partyName+" without commment");
					report.addStepFailScreenshot(driver, step);
					step.log(Status.FAIL, e.getMessage());
					throw new Exception("Error while reviewing Party: "+partyName+" without commment");
				}
			}
		}
	}
	
	public void checkPartiesAvailable(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {				
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
				driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).clear();
				driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
				
				if(driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).size()==1) {
					step.log(Status.FAIL, "Party: "+party.getString("party_name")+" is not available at Party Checker Queue. (See below image)");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Party: "+party.getString("party_name")+" is not available at Party Checker Queue");
				}
				else {
					String resultParty = driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
					
					if(resultParty.equals(party.getString("party_name").trim().toLowerCase())) {
						step.log(Status.PASS, "Party: "+party.getString("party_name")+" available at Party Checker Queue. (See below image)");
						report.addStepPassScreenshot(driver, step);
					}
					else {
						step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not filtered at Party Checker Queue. (See below image)");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Party: "+party.getString("party_name")+" not filtered at Party Checker Queue");
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
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
				driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).clear();
				driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 10);
				
				if(driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).size()==1) {
					step.log(Status.PASS, "Party: "+party.getString("party_name")+" is not available at Party Checker Queue. (See below image)");
					report.addStepPassScreenshot(driver, step);
				}
				else {
					String resultParty = driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
					
					if(resultParty.equals(party.getString("party_name").trim().toLowerCase())) {
						step.log(Status.FAIL, "Party: "+party.getString("party_name")+" available at Party Checker Queue. (See below image)");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Party: "+party.getString("party_name")+" available at Party Checker Queue");
					}
					else {
						step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not filtered at Party Checker Queue. (See below image)");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Party: "+party.getString("party_name")+" not filtered at Party Checker Queue");
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
	
	public void deleteParties(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray parties = testData.getJSONArray("delete_parties");
		Iterator<Object> partyIterator = parties.iterator();	
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {			
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
				driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).clear();
				driver.findElements(By.xpath(partyCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(party.getString("party_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(partyCheckerLocator.getString("column-PartyName")), 2, 120);
				
				String resultParty = driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyName"))).get(1).getText().trim().toLowerCase();
				
				if(resultParty.equals(party.getString("party_name").trim().toLowerCase())) {
					step.log(Status.INFO, "Party: "+party.getString("party_name")+" available at Party Checker Queue");
					driver.findElements(By.xpath(partyCheckerLocator.getString("column-PartyManage"))).get(1).findElements(By.tagName("i")).get(1).click();
					
					step.log(Status.INFO, "Party: "+party.getString("party_name")+" deleted");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(partyCheckerLocator.getString("button-Yes"))).click();
				}
				else {
					step.log(Status.FAIL, "Party: "+party.getString("party_name")+" not available at Party Checker Queue. (See below image).");
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Party: "+party.getString("party_name")+" not available at Party Checker Queue");
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
