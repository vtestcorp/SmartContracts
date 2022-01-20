package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;

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

public class IncomeBookingTransactionCheckerPage {
	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject incomeBookingTransactionCheckerLocator = null;
	private JSONArray adhocFees = null;
	
	public IncomeBookingTransactionCheckerPage(LocatorFactory locators) {
		incomeBookingTransactionCheckerLocator = locators.getLocators("IncomeBookingTransactionCheckerPage");				
	}
	
	public void approveAdhocFees(WebDriver driver, ExtentTest step) throws Exception {
		adhocFees = testData.getJSONArray("adhoc_fees");
		Iterator<Object> adhocFeesIterator = adhocFees.iterator();
		while(adhocFeesIterator.hasNext()) {
			JSONObject adhocFee = (JSONObject) adhocFeesIterator.next();
			try {
				String incomeBookingInstructionName = adhocFee.getString("adhoc_fee_instruction_name");
				utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingTransactionCheckerLocator.getString("column-IncomeBookingInstructionName")), 2, 120);
				driver.findElements(By.xpath(incomeBookingTransactionCheckerLocator.getString("input-FilterText"))).get(1).sendKeys(incomeBookingInstructionName);
				utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingTransactionCheckerLocator.getString("column-IncomeBookingInstructionName")), 2, 120);
				
				String resultInstructionName = driver.findElements(By.xpath(incomeBookingTransactionCheckerLocator.getString("column-IncomeBookingInstructionName"))).get(1).getText().trim().toLowerCase();
				
				if(resultInstructionName.equals(incomeBookingInstructionName.trim().toLowerCase())) {
					step.log(Status.INFO, "Adhoc fee: "+incomeBookingInstructionName+" available at Income Booking Transaction Checker Queue");
					driver.findElements(By.xpath(incomeBookingTransactionCheckerLocator.getString("column-IncomeBookingActions"))).get(1).findElement(By.tagName("i")).click();
					
					report.getReportStep().log(Status.INFO, "Basic details of adhoc fee: "+incomeBookingInstructionName);
					report.addStepInfoScreenshot(driver, report.getReportStep());
					driver.findElement(By.id(incomeBookingTransactionCheckerLocator.getString("button-IncomeBookingBasicNext"))).click();	
					
					report.getReportStep().log(Status.INFO, "Party details of adhoc fee: "+incomeBookingInstructionName);
					report.addStepInfoScreenshot(driver, report.getReportStep());
					driver.findElement(By.id(incomeBookingTransactionCheckerLocator.getString("button-IncomeBookingBasicNext"))).click();	
					
					report.getReportStep().log(Status.INFO, "Fee details of adhoc fee: "+incomeBookingInstructionName);
					report.addStepInfoScreenshot(driver, report.getReportStep());
					driver.findElement(By.id(incomeBookingTransactionCheckerLocator.getString("button-IncomeBookingBasicNext"))).click();	
					
					driver.findElement(By.xpath(incomeBookingTransactionCheckerLocator.getString("tab-IncomeBookingSummary"))).click();
					driver.findElement(By.tagName("textarea")).sendKeys("Approved");					
					driver.findElement(By.xpath(incomeBookingTransactionCheckerLocator.getString("button-Approve"))).click();				
					step.log(Status.INFO, "Adhoc Fee: "+incomeBookingInstructionName+" approved");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(incomeBookingTransactionCheckerLocator.getString("button-Yes"))).click();
				}
				else {
					step.log(Status.FAIL, "Adhoc Fee: "+incomeBookingInstructionName+" not available at Income Booking Transaction Checker Queue");
					report.addStepFailScreenshot(driver, step);					
					throw new Exception("Adhoc Fee: "+incomeBookingInstructionName+" not available at Income Booking Transaction Checker Queue");
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while approving adhoc fee: "+adhocFee.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while approving adhoc fee: "+adhocFee.getString("name"));
			}
		}
	}
}
