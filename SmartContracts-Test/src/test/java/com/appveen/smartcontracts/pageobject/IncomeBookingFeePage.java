package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class IncomeBookingFeePage {
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject incomeBookingFeeLocator = null;
	
	public IncomeBookingFeePage(LocatorFactory locators) {
		incomeBookingFeeLocator = locators.getLocators("IncomeBookingFeePage");	
	}
	
	public void validateScheduledFeeAtTransactionMakerQueue(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();	
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				JSONArray scheduledFees = deal.getJSONArray("scheduled_fees");
				Iterator<Object> scheduledFeeIterator = scheduledFees.iterator();
				
				while(scheduledFeeIterator.hasNext()) {
					JSONObject scheduledFee = (JSONObject) scheduledFeeIterator.next();
					List<WebElement> filterText = driver.findElements(By.xpath(incomeBookingFeeLocator.getString("input-FilterText")));
					utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingFeeLocator.getString("column-InstructionName")), 2, 120);
					filterText.get(1).clear();
					filterText.get(1).sendKeys(deal.getString("name"));
					filterText.get(2).clear();
					filterText.get(2).sendKeys(scheduledFee.getString("scheduled_fee_instruction_name"));
					utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingFeeLocator.getString("column-InstructionName")), 2, 120);
					
					if(driver.findElements(By.xpath(incomeBookingFeeLocator.getString("column-InstructionName"))).size()==1) {
						step.log(Status.FAIL, "Scheduled fees under deal: "+deal.getString("name")+" is not available at Transaction Maker Queue. (See below image)");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Scheduled fees under deal: "+deal.getString("name")+" is not available at Transaction Maker Queue");
					}
					else {
						String resultDeal = driver.findElements(By.xpath(incomeBookingFeeLocator.getString("column-DealName"))).get(1).getText().trim().toLowerCase();
						
						if(resultDeal.equals(deal.getString("name").trim().toLowerCase())) {
							step.log(Status.PASS, "Scheduled fees under deal: "+deal.getString("name")+" available at Transaction Maker Queue. (See below image)");
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Scheduled fees under deal: "+deal.getString("name")+" not filtered at Transaction Maker Queue. (See below image)");
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Scheduled fees under deal: "+deal.getString("name")+" not filtered at Transaction Maker Queue");
						}
					}
				}
								
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while filtering deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while filtering deal: "+deal.getString("name"));
			}
		}
	}
	
	public void validateScheduledFeeAtTransactionCheckerQueue(WebDriver driver, ExtentTest step) throws Exception {
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();	
		driver.findElement(By.xpath(incomeBookingFeeLocator.getString("label-TXNChecker"))).click();
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				JSONArray scheduledFees = deal.getJSONArray("scheduled_fees");
				Iterator<Object> scheduledFeeIterator = scheduledFees.iterator();
				
				while(scheduledFeeIterator.hasNext()) {
					JSONObject scheduledFee = (JSONObject) scheduledFeeIterator.next();
					List<WebElement> filterText = driver.findElements(By.xpath(incomeBookingFeeLocator.getString("input-FilterText")));
					utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingFeeLocator.getString("column-InstructionName")), 2, 120);
					filterText.get(1).clear();
					filterText.get(1).sendKeys(deal.getString("name"));
					filterText.get(2).clear();
					filterText.get(2).sendKeys(scheduledFee.getString("scheduled_fee_instruction_name"));
					utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingFeeLocator.getString("column-InstructionName")), 2, 120);
					
					if(driver.findElements(By.xpath(incomeBookingFeeLocator.getString("column-InstructionName"))).size()==1) {
						step.log(Status.FAIL, "Scheduled fees under deal: "+deal.getString("name")+" is not available at Transaction Maker Queue. (See below image)");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Scheduled fees under deal: "+deal.getString("name")+" is not available at Transaction Maker Queue");
					}
					else {
						String resultDeal = driver.findElements(By.xpath(incomeBookingFeeLocator.getString("column-DealName"))).get(1).getText().trim().toLowerCase();
						
						if(resultDeal.equals(deal.getString("name").trim().toLowerCase())) {
							step.log(Status.PASS, "Scheduled fees under deal: "+deal.getString("name")+" available at Transaction Maker Queue. (See below image)");
							report.addStepPassScreenshot(driver, step);
						}
						else {
							step.log(Status.FAIL, "Scheduled fees under deal: "+deal.getString("name")+" not filtered at Transaction Maker Queue. (See below image)");
							report.addStepFailScreenshot(driver, step);
							throw new Exception("Scheduled fees under deal: "+deal.getString("name")+" not filtered at Transaction Maker Queue");
						}
					}
				}
								
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while filtering deal: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while filtering deal: "+deal.getString("name"));
			}
		}
	}
}
