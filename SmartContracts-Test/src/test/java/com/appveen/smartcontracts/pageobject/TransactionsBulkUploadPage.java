package com.appveen.smartcontracts.pageobject;

import java.io.File;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class TransactionsBulkUploadPage {
	 
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private Actions action = null;
	private LandingPage landingPage = null;
	private TransactionMakerPage transactionMakerPage = null;
	private JSONObject transactionsBulkUploadLocator = null;
	private JSONArray bulkTransactions = null;
	
	public TransactionsBulkUploadPage(LocatorFactory locators) {
		transactionsBulkUploadLocator = locators.getLocators("TransactionsBulkUploadPage");	
		landingPage = new LandingPage(locators);
		transactionMakerPage = new TransactionMakerPage(locators);
	}
	
	public void uploadBulkTransactions(WebDriver driver, ExtentTest step) throws Exception {
		bulkTransactions = testData.getJSONArray("bulk_transactions");
		Iterator<Object> bulkTransactionIterator = bulkTransactions.iterator();
		action = new Actions(driver);
		while(bulkTransactionIterator.hasNext()) {
			JSONObject bulkTransaction = (JSONObject) bulkTransactionIterator.next();
			try {
				landingPage.goToTransactionMaker(driver, step);
				transactionMakerPage.gotoBulkUpload(driver, step);
				
				File transactionBulkUploadFile = new File(bulkTransaction.getString("bulk_upload_file_url"));
				driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("input-BrowseFile"))).sendKeys(transactionBulkUploadFile.getAbsolutePath());
				Select bulkUploadSheet = new Select(driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("select-SelectSheet"))));
				bulkUploadSheet.selectByVisibleText(bulkTransaction.getString("bulk_upload_file_sheet"));
				driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("button-UploadFile"))).click();
				
				if(driver.findElement(By.id(transactionsBulkUploadLocator.getString("label-ReponseTitle"))).getText().toLowerCase().contains("duplicate")) {
					step.log(Status.FAIL, "Duplicate file: "+bulkTransaction.getString("bulk_upload_file_name")+" OR transactions uploaded");
					report.addStepFailScreenshot(driver, step);					
					throw new Exception("Duplicate file: "+bulkTransaction.getString("bulk_upload_file_name")+" OR transactions uploaded");				
				}
				driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("button-OK"))).click();
				
				long start = System.currentTimeMillis();
				landingPage.reviewNotification(driver, step, bulkTransaction.getString("bulk_upload_file_name"));
				long end = System.currentTimeMillis();
				step.log(Status.INFO, "Time taken to generate review notification: "+(end-start)/1000+" seconds");
				
				String validRecords = driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("card-ValidRecords"))).findElements(By.tagName("div")).get(1).getText();
				step.log(Status.INFO, validRecords+" valid records ready to submit");
				report.addStepInfoScreenshot(driver, step);
				
				driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("button-Next"))).click();
				
				Thread.sleep(2000);
				String[] transactionRecordCount = driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
				
				while(transactionRecordCount[3].equals("0")) {
					transactionRecordCount = driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
				}
				
				while(!transactionRecordCount[1].equals(transactionRecordCount[3])) {
					try {						
						driver.findElements(By.xpath(transactionsBulkUploadLocator.getString("column-TransactionDealID"))).get(driver.findElements(By.xpath(transactionsBulkUploadLocator.getString("column-TransactionDealID"))).size()-1).sendKeys(Keys.PAGE_DOWN);
						action.sendKeys(Keys.PAGE_DOWN).build().perform();
						transactionRecordCount = driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
					}
					catch (Exception e) {							
					}
				}		
				
				driver.findElement(By.id(transactionsBulkUploadLocator.getString("checkbox-TransactionCheckbox"))).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("button-Submit"))).click();
				
				if(driver.findElement(By.id(transactionsBulkUploadLocator.getString("label-ReponseTitle"))).getText().toLowerCase().contains("Select atleast one record")) {
					step.log(Status.FAIL, "Select atleast one record before submitting");
					report.addStepFailScreenshot(driver, step);					
					throw new Exception("Select atleast one record before submitting");				
				}
				
				step.log(Status.PASS, "Submitted "+transactionRecordCount[3]+" transactions to Transaction Checker Queue");
				report.addStepPassScreenshot(driver, step);				
				driver.findElement(By.xpath(transactionsBulkUploadLocator.getString("button-OK"))).click();				
				
				
				start = System.currentTimeMillis();
				landingPage.closeNotification(driver, step, bulkTransaction.getString("bulk_upload_file_name"));	
				end = System.currentTimeMillis();
				step.log(Status.INFO, "Time taken to generate submit notification: "+(end-start)/1000+" seconds");
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while uploading bulk transactions from file: "+bulkTransaction.getString("bulk_upload_file_name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while uploading bulk transactions from file: "+bulkTransaction.getString("bulk_upload_file_name"));
			}			
		}
	}
	

}
