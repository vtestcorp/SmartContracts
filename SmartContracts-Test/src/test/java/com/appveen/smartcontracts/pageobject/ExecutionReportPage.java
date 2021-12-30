package com.appveen.smartcontracts.pageobject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ExecutionReportPage {
	 
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject executionReportLocator = null;
	private JSONArray transactions = null;
	
	private void waitForResultstoLoad(WebDriver driver) {
		while(true) {
			if(utility.isElementHiddenNow(driver, By.xpath(executionReportLocator.getString("label-ResultLoading")))) {			
				break;
			}
		}
	}
	
	private void setPreference(WebDriver driver, ExtentTest step) throws Exception {
		try {
			String[] preferences = {"Deal Id", "SCROE Status", "Instruction Id", "Originally Scheduled On", "Scheduled On"};
			List<String> preferenceList = new ArrayList<String>();
			
			driver.findElement(By.xpath(executionReportLocator.getString("button-SetPreference"))).click();
			
			WebElement checkbox =  driver.findElement(By.xpath(executionReportLocator.getString("checkbox-SelectAll")));
			if(checkbox.isSelected()) {
				checkbox.click();
			}
			else {
				checkbox.click();
				checkbox.click();
			}
			
			List<WebElement> preferenceElements = driver.findElement(By.id(executionReportLocator.getString("list-PreferenceList"))).findElements(By.xpath(executionReportLocator.getString("label-PreferenceName")));
			
			for(WebElement element : preferenceElements) {
				preferenceList.add(element.getText());
			}
			
			for(int index = 0; index < preferences.length; index++) {
				if(preferenceList.contains(preferences[index])) {
					preferenceElements.get(preferenceList.indexOf(preferences[index])).findElement(By.id(executionReportLocator.getString("checkbox-Preference"))).click();
				}
			}
			
			driver.findElement(By.xpath(executionReportLocator.getString("button-PreferenceApply"))).click();	
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while setting preferences");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while setting preferences");
		}
	}
	
	public ExecutionReportPage(LocatorFactory locators){
		executionReportLocator = locators.getLocators("ExecutionReportPage");		
	}
	
	public void checkExecutionStatus(WebDriver driver, ExtentTest step) throws Exception {			
		transactions = testData.getJSONArray("transactions");
		Iterator<Object> transactionIterator = transactions.iterator();			
		while(transactionIterator.hasNext()) {	
			
			JSONObject transaction = (JSONObject) transactionIterator.next();
			String dealTitle = transaction.getString("deal_name");
			
			String dealID = DealCheckerPage.dealDetails.get(dealTitle).toLowerCase().trim();
			
			String[] transactionRecordCount = null;
			try {
				waitForResultstoLoad(driver);
				setPreference(driver, step);
				driver.findElement(By.xpath(executionReportLocator.getString("button-ResetFilters"))).click();
				
				waitForResultstoLoad(driver);
				
				List<WebElement> headerLabelElements = driver.findElement(By.xpath(executionReportLocator.getString("tab-HeaderContainer"))).findElement(By.xpath(executionReportLocator.getString("tab-HeaderLabels"))).findElements(By.xpath(executionReportLocator.getString("label-HeaderLabels")));
				List<String> headerLabels = new ArrayList<String>();
				
				for(WebElement element : headerLabelElements) {
					headerLabels.add(element.getText());
				}
				
				List<WebElement> headerFilterElements = driver.findElement(By.xpath(executionReportLocator.getString("tab-HeaderContainer"))).findElement(By.xpath(executionReportLocator.getString("tab-HeaderFilters"))).findElements(By.xpath(executionReportLocator.getString("input-HeaderFilters")));
				
				((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'", headerFilterElements.get(headerLabels.indexOf("DEAL ID")));
				headerFilterElements.get(headerLabels.indexOf("DEAL ID")).findElement(By.tagName("input")).clear();
				headerFilterElements.get(headerLabels.indexOf("DEAL ID")).findElement(By.tagName("input")).sendKeys(dealID);	
				
				headerFilterElements.get(headerLabels.indexOf("SCHEDULED ON")).findElement(By.tagName("input")).click();
				driver.findElement(By.xpath(executionReportLocator.getString("label-CurrentDate"))).click();
				driver.findElement(By.xpath(executionReportLocator.getString("button-DateSubmit"))).click();
				
				waitForResultstoLoad(driver);
				
				driver.findElement(By.xpath(executionReportLocator.getString("label-TransactionRecordsCount")));
				transactionRecordCount = driver.findElement(By.xpath(executionReportLocator.getString("label-TransactionRecordsCount"))).getText().split(" ");
				
				if(!transactionRecordCount[3].equals("0")) {
					step.log(Status.INFO, transactionRecordCount[3]+" transactions under Deal ID ' "+dealID+"' in Execution Report");
					report.addStepInfoScreenshot(driver, step);					
				}
				else {				
					step.log(Status.INFO, "No transactions under Deal ID '"+dealID+"' in Execution Report");
					report.addStepInfoScreenshot(driver, step);					
				}				
			
			}
	
			catch (Exception e) {
				step.log(Status.FAIL, "Error while checking transactions under Deal ID '"+dealID+"' in Execution Report");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while checking transactions under Deal ID '"+dealID+"' in Execution Report");
			}
		}
	}	

}
