package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;


import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;


import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class TransactionMakerPage {
	 
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private LandingPage landingPage = null;
	private AdhocTransactionPage adhocTransactionPage = null;
	private JSONObject transactionMakerLocator = null; 
	private JSONArray transactions = null;
	
	
	public TransactionMakerPage(LocatorFactory locators) {
		transactionMakerLocator = locators.getLocators("TransactionMakerPage");	
		landingPage = new LandingPage(locators);
		adhocTransactionPage = new AdhocTransactionPage(locators);
	}
	
	public void submitAllTransactions(WebDriver driver, ExtentTest step) throws Exception {	
		transactions = testData.getJSONArray("transactions");
		Iterator<Object> transactionIterator = transactions.iterator();			
		while(transactionIterator.hasNext()) {
			JSONObject transaction = (JSONObject) transactionIterator.next();
			String dealTitle = transaction.getString("deal_name").toLowerCase().trim();
			String dealID =	(DealCheckerPage.dealDetails.get(dealTitle) == null) ? transaction.getString("deal_id") : DealCheckerPage.dealDetails.get(dealTitle);
			
			try {
				String[] transactionRecordCount = null;	
				Thread.sleep(1000);
				driver.findElement(By.xpath(transactionMakerLocator.getString("input-FilerDealID"))).findElement(By.tagName("div")).findElement(By.tagName("input")).clear();
				driver.findElement(By.xpath(transactionMakerLocator.getString("input-FilerDealID"))).findElement(By.tagName("div")).findElement(By.tagName("input")).sendKeys(dealID);				
				try {
					driver.findElement(By.xpath(transactionMakerLocator.getString("label-TransactionRecordsCount")));
					transactionRecordCount = driver.findElement(By.xpath(transactionMakerLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
					step.log(Status.INFO, transactionRecordCount[3]+" transactions under Deal ID ' "+dealID+"' in Transaction Maker Queue");
					report.addStepInfoScreenshot(driver, step);
					while(!transactionRecordCount[1].equals(transactionRecordCount[3])) {
						try {
							driver.findElements(By.xpath(transactionMakerLocator.getString("button-TransactionDealID"))).get(driver.findElements(By.xpath(transactionMakerLocator.getString("button-TransactionDealID"))).size()-1).sendKeys(Keys.ARROW_DOWN);
							transactionRecordCount = driver.findElement(By.xpath(transactionMakerLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
						}
						catch (Exception e) {							
						}
					}
				}
				catch (Exception e) {
					step.log(Status.INFO, "No transactions under Deal ID '"+dealID+"' in Transaction Maker Queue");
					report.addStepInfoScreenshot(driver, step);
					break;
				}
				
				driver.findElements(By.id(transactionMakerLocator.getString("checkbox-TransactionCheckbox"))).get(1).click();
				driver.findElement(By.xpath(transactionMakerLocator.getString("button-TransactionSubmit"))).click();
				driver.findElement(By.xpath(transactionMakerLocator.getString("button-TransactionSubmitOK")));
				step.log(Status.PASS, "Submitted "+transactionRecordCount[3]+" transactions under Deal ID ' "+dealID+"' to Transaction Checker Queue");
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(transactionMakerLocator.getString("button-TransactionSubmitOK"))).click();
				driver.navigate().refresh();				
			
			}
	
			catch (Exception e) {
				step.log(Status.FAIL, "Error while submitting all transactions to Transaction Checker Queue");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while submitting all transactions to Transaction Checker Queue");
			}
		}
	}
	
	public void gotoBulkUpload(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.xpath(transactionMakerLocator.getString("link-TransactionBulkUpload"))).click();			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Bulk Upload");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Bulk Upload");
		}
	}
	
	public void addTransactions(WebDriver driver, ExtentTest step) throws Exception {
		
		transactions = testData.getJSONArray("transactions");
		Iterator<Object> transactionIterator = transactions.iterator();
		
		while(transactionIterator.hasNext()) {
			JSONObject transaction = (JSONObject) transactionIterator.next();
			try {
				landingPage.goToTransactionMaker(driver, step);
				
				String dealTitle = transaction.getString("deal_name").toLowerCase().trim();
				String expectedDealID = (DealCheckerPage.dealDetails.get(dealTitle) == null) ? transaction.getString("deal_id").toLowerCase().trim(): DealCheckerPage.dealDetails.get(dealTitle).toLowerCase().trim();
				
				driver.findElement(By.xpath(transactionMakerLocator.getString("select-TransactionDealID"))).findElement(By.tagName("input")).sendKeys(expectedDealID);
				utility.selectFromDivMenu(driver, step, driver.findElement(By.xpath(transactionMakerLocator.getString("select-TransactionDealID"))).findElement(By.tagName("ul")), expectedDealID);
				
				driver.findElement(By.xpath(transactionMakerLocator.getString("select-TransactionSourceAccountNumber"))).findElement(By.tagName("input")).click();
				utility.selectFromDivMenu(driver, step, driver.findElement(By.xpath(transactionMakerLocator.getString("select-TransactionSourceAccountNumber"))).findElement(By.tagName("ul")), transaction.getString("transaction_source_account"));
				
				driver.findElement(By.xpath(transactionMakerLocator.getString("button-TransactionSubmit"))).click();
				
				adhocTransactionPage.addTransaction(driver, step, transaction);			
			}		
			catch (Exception e) {
				step.log(Status.FAIL, "Error while creating transaction "+transaction.getString("transaction_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());				
				throw new Exception("Error while creating transaction "+transaction.getString("transaction_name"));
			}	
		}
	}

}
