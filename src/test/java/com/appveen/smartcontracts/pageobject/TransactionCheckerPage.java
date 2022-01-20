package com.appveen.smartcontracts.pageobject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class TransactionCheckerPage {
	 
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());	
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject transactionCheckerLocator = null;
	private JSONArray bulkTransactions = null;
	
	private void setPreference(WebDriver driver, ExtentTest step) throws Exception {
		try {
		String[] preferences = {"Transaction Name", "File Name"};
		List<String> preferenceList = new ArrayList<String>();
		
		driver.findElement(By.xpath(transactionCheckerLocator.getString("button-SetPreference"))).click();
		
		WebElement checkbox =  driver.findElement(By.xpath(transactionCheckerLocator.getString("checkbox-SelectAll")));
		if(checkbox.isSelected()) {
			checkbox.click();
		}
		else {
			checkbox.click();
			checkbox.click();
		}
		
		List<WebElement> preferenceElements = driver.findElement(By.id(transactionCheckerLocator.getString("list-PreferenceList"))).findElements(By.xpath(transactionCheckerLocator.getString("label-PreferenceName")));
		
		for(WebElement element : preferenceElements) {
			preferenceList.add(element.getText());
		}
		
		for(int index = 0; index < preferences.length; index++) {
			if(preferenceList.contains(preferences[index])) {
				preferenceElements.get(preferenceList.indexOf(preferences[index])).findElement(By.id(transactionCheckerLocator.getString("checkbox-Preference"))).click();
			}
		}
		
		driver.findElement(By.xpath(transactionCheckerLocator.getString("button-PreferenceApply"))).click();
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while setting preferences");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while setting preferences");
		}
	}
	
	public TransactionCheckerPage(LocatorFactory locators) {
		transactionCheckerLocator = locators.getLocators("TransactionCheckerPage");	
	}
	
	public void approveAllBulkTransactions(WebDriver driver, ExtentTest step) throws Exception {	
		bulkTransactions = testData.getJSONArray("bulk_transactions");
		Iterator<Object> bulkTransactionIterator = bulkTransactions.iterator();			
		while(bulkTransactionIterator.hasNext()) {
			setPreference(driver, step);
			JSONObject bulkTransaction = (JSONObject) bulkTransactionIterator.next();
			try {
				String[] transactionRecordCount = null;				
				driver.findElement(By.xpath(transactionCheckerLocator.getString("input-FilerFileName"))).findElement(By.tagName("div")).findElement(By.tagName("input")).clear();
				driver.findElement(By.xpath(transactionCheckerLocator.getString("input-FilerFileName"))).findElement(By.tagName("div")).findElement(By.tagName("input")).sendKeys(bulkTransaction.getString("bulk_upload_file_name"));				
				try {
					utility.setImplicitWaits(driver, 200);
					driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionRecordsCount")));
					transactionRecordCount = driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
					step.log(Status.INFO, transactionRecordCount[3]+" transactions under file ' "+bulkTransaction.getString("bulk_upload_file_name")+"' in Transaction Checker Queue");
					report.addStepInfoScreenshot(driver, step);
					while(!transactionRecordCount[1].equals(transactionRecordCount[3])) {
						try {
							driver.findElements(By.xpath(transactionCheckerLocator.getString("button-TransactionDealID"))).get(driver.findElements(By.xpath(transactionCheckerLocator.getString("button-TransactionDealID"))).size()-1).sendKeys(Keys.PAGE_DOWN);
							transactionRecordCount = driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
						}
						catch (Exception e) {							
						}
					}
				}
				catch (Exception e) {
					step.log(Status.INFO, "No transactions under file '"+bulkTransaction.getString("bulk_upload_file_name")+"' in Transaction Checker Queue");
					report.addStepInfoScreenshot(driver, step);
					break;
				}
				
				driver.findElements(By.id(transactionCheckerLocator.getString("checkbox-TransactionCheckbox"))).get(1).click();
				
				driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionManage"))).findElement(By.tagName("i")).click();
				driver.findElement(By.id(transactionCheckerLocator.getString("input-TransactionNotes"))).sendKeys("Approved");
				driver.findElement(By.id(transactionCheckerLocator.getString("button-AddTransactionNote"))).click();
				
				driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmit"))).click();				
				driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmitYes"))).click();
				driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmitOK")));
				step.log(Status.PASS, "Approved "+transactionRecordCount[3]+" transactions under File ' "+bulkTransaction.getString("bulk_upload_file_name")+"'");
				report.addStepPassScreenshot(driver, step);				
				driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmitOK"))).click();
				utility.turnOnImplicitWaits(driver);
				driver.navigate().refresh();			
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while approving all transactions");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while approving all transactions");
			}
		}
	}
	
	public void approveAllTransactions(WebDriver driver, ExtentTest step) throws Exception {	
		bulkTransactions = testData.getJSONArray("transactions");
		Iterator<Object> transactionIterator = bulkTransactions.iterator();			
		while(transactionIterator.hasNext()) {
			JSONObject transaction = (JSONObject) transactionIterator.next();
			String dealTitle = transaction.getString("deal_name").toLowerCase().trim();
			String dealID =	(DealCheckerPage.dealDetails.get(dealTitle) == null) ? transaction.getString("deal_id") : DealCheckerPage.dealDetails.get(dealTitle);
			try {
				String[] transactionRecordCount = null;				
				utility.turnOnElementHighlight(driver, driver.findElement(By.xpath(transactionCheckerLocator.getString("input-FilerDealID"))));
				driver.findElement(By.xpath(transactionCheckerLocator.getString("input-FilerDealID"))).findElement(By.tagName("div")).findElement(By.tagName("input")).clear();
				driver.findElement(By.xpath(transactionCheckerLocator.getString("input-FilerDealID"))).findElement(By.tagName("div")).findElement(By.tagName("input")).sendKeys(dealID);				
				try {
					utility.setImplicitWaits(driver, 200);
					driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionRecordsCount")));
					transactionRecordCount = driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
					step.log(Status.INFO, transactionRecordCount[3]+" transactions under Deal ID ' "+dealID+"' in Transaction Checker Queue");
					report.addStepInfoScreenshot(driver, step);
					while(!transactionRecordCount[1].equals(transactionRecordCount[3])) {
						try {
							driver.findElements(By.xpath(transactionCheckerLocator.getString("button-TransactionDealID"))).get(driver.findElements(By.xpath(transactionCheckerLocator.getString("button-TransactionDealID"))).size()-1).sendKeys(Keys.PAGE_DOWN);
							transactionRecordCount = driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
						}
						catch (Exception e) {							
						}
					}
				}
				catch (Exception e) {
					step.log(Status.INFO, "No transactions under Deal ID '"+dealID+"' in Transaction Checker Queue");
					report.addStepInfoScreenshot(driver, step);
					break;
				}
				
				driver.findElements(By.id(transactionCheckerLocator.getString("checkbox-TransactionCheckbox"))).get(1).click();
				
				driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionManage"))).findElement(By.tagName("i")).click();
				driver.findElement(By.id(transactionCheckerLocator.getString("input-TransactionNotes"))).sendKeys("Approved");
				driver.findElement(By.id(transactionCheckerLocator.getString("button-AddTransactionNote"))).click();
				
				driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmit"))).click();				
				driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmitYes"))).click();
				driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmitOK")));
				step.log(Status.PASS, "Approved "+transactionRecordCount[3]+" transactions under Deal ID ' "+dealID+"'");
				report.addStepPassScreenshot(driver, step);				
				driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmitOK"))).click();
				utility.turnOnImplicitWaits(driver);
				driver.navigate().refresh();			
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while approving all transactions");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while approving all transactions");
			}
		}
	}
	
	public void approveAllTransactions(WebDriver driver, ExtentTest step,  String dealID) throws Exception {			
		try {			
			String[] transactionRecordCount = null;				
			utility.turnOnElementHighlight(driver, driver.findElement(By.xpath(transactionCheckerLocator.getString("input-FilerDealID"))));
			driver.findElement(By.xpath(transactionCheckerLocator.getString("input-FilerDealID"))).findElement(By.tagName("div")).findElement(By.tagName("input")).clear();
			driver.findElement(By.xpath(transactionCheckerLocator.getString("input-FilerDealID"))).findElement(By.tagName("div")).findElement(By.tagName("input")).sendKeys(dealID);				
			try {
				utility.setImplicitWaits(driver, 200);
				driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionRecordsCount")));
				transactionRecordCount = driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
				step.log(Status.INFO, transactionRecordCount[3]+" transactions under Deal ID ' "+dealID+"' in Transaction Checker Queue");
				report.addStepInfoScreenshot(driver, step);
				while(!transactionRecordCount[1].equals(transactionRecordCount[3])) {
					try {
						driver.findElements(By.xpath(transactionCheckerLocator.getString("button-TransactionDealID"))).get(driver.findElements(By.xpath(transactionCheckerLocator.getString("button-TransactionDealID"))).size()-1).sendKeys(Keys.PAGE_DOWN);
						transactionRecordCount = driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
					}
					catch (Exception e) {							
					}
				}
			}
			catch (Exception e) {
				step.log(Status.INFO, "No transactions under Deal ID '"+dealID+"' in Transaction Checker Queue");
				report.addStepInfoScreenshot(driver, step);				
			}
			
			driver.findElements(By.id(transactionCheckerLocator.getString("checkbox-TransactionCheckbox"))).get(1).click();
			
			driver.findElement(By.xpath(transactionCheckerLocator.getString("label-TransactionManage"))).findElement(By.tagName("i")).click();
			driver.findElement(By.id(transactionCheckerLocator.getString("input-TransactionNotes"))).sendKeys("Approved");
			driver.findElement(By.id(transactionCheckerLocator.getString("button-AddTransactionNote"))).click();
			
			driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmit"))).click();			
			driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmitYes"))).click();
			driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmitOK")));
			step.log(Status.PASS, "Approved "+transactionRecordCount[3]+" transactions under Deal ID ' "+dealID+"'");
			report.addStepPassScreenshot(driver, step);				
			driver.findElement(By.xpath(transactionCheckerLocator.getString("button-TransactionSubmitOK"))).click();
			utility.turnOnImplicitWaits(driver);
			driver.navigate().refresh();			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while approving all transactions");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while approving all transactions");
		}
		
	}
}
