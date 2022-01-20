package com.appveen.smartcontracts.pageobject;


import java.util.Iterator;
import java.util.concurrent.Delayed;

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

public class DealLifecycleMakerPage {
	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private JSONObject dealLifecycleMakerLocator = null;
	private JSONArray deals = null;	
	private UtilityFactory utility = new UtilityFactory();
	
		
	public DealLifecycleMakerPage(LocatorFactory locators){
		dealLifecycleMakerLocator = locators.getLocators("DealLifecycleMakerPage");		
	}
	
	public void sendDealPauseRequestToChecker(WebDriver driver, ExtentTest step) throws Exception {				
		deals = testData.getJSONArray("pause_deals");
		Iterator<Object> dealIterator = deals.iterator();			
		while(dealIterator.hasNext()) {				
			JSONObject deal = (JSONObject) dealIterator.next();			
			try {	
				//setPreference();				
				
				utility.waitForElementToBeDisplayed(driver, By.xpath(dealLifecycleMakerLocator.getString("column-DealName")), 2, 120);
				driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("input-FilterText"))).get(1).sendKeys(deal.getString("name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(dealLifecycleMakerLocator.getString("column-DealName")), 2, 120);
				
				Select dealOperation = new Select(driver.findElement(By.id(dealLifecycleMakerLocator.getString("select-OperationFilter"))));
				dealOperation.selectByVisibleText("Pause");
				utility.waitForElementToBeDisplayed(driver, By.xpath(dealLifecycleMakerLocator.getString("column-DealName")), 2, 120);
				
				String resultDeal = driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("column-DealName"))).get(1).getText();
				if(resultDeal.equals(deal.getString("name"))){
					driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("column-Manage"))).get(1).findElements(By.tagName("i")).get(0).click();
					step.log(Status.INFO, "Deal: "+deal.getString("name")+" pause request sent to Deal Lifecycle Maker Queue");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Yes"))).click();
				}
				else {
					step.log(Status.FAIL, "Deal: "+deal.getString("name")+" not available at Deal Lifecycle Maker Queue");
					report.addStepFailScreenshot(driver, step);					
					throw new Exception("Deal: "+deal.getString("name")+" not available at Deal Lifecycle Maker Queue");
				}
				
			}		
			catch (Exception e) {
				step.log(Status.FAIL, "Error while sending deal pause request"+deal.getString("name")+" to lifecycle checker");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while sending deal pause request"+deal.getString("name")+" to lifecycle checker");
			}
			finally {
				driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}
	
	public void sendDealCloseRequestToChecker(WebDriver driver, ExtentTest step) throws Exception {				
		deals = testData.getJSONArray("close_deals");
		Iterator<Object> dealIterator = deals.iterator();			
		while(dealIterator.hasNext()) {				
			JSONObject deal = (JSONObject) dealIterator.next();			
			try {	
				//setPreference();				
				
				utility.waitForElementToBeDisplayed(driver, By.xpath(dealLifecycleMakerLocator.getString("column-DealName")), 2, 120);
				driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("input-FilterText"))).get(1).sendKeys(deal.getString("name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(dealLifecycleMakerLocator.getString("column-DealName")), 2, 120);
				
				Select dealOperation = new Select(driver.findElement(By.id(dealLifecycleMakerLocator.getString("select-OperationFilter"))));
				dealOperation.selectByVisibleText("Close");
				utility.waitForElementToBeDisplayed(driver, By.xpath(dealLifecycleMakerLocator.getString("column-DealName")), 2, 120);
				
				String resultDeal = driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("column-DealName"))).get(1).getText();
				if(resultDeal.equals(deal.getString("name"))){
					driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("column-Manage"))).get(1).findElements(By.tagName("i")).get(0).click();
					driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Submit"))).click();
					step.log(Status.INFO, "Deal: "+deal.getString("name")+" close request sent to Deal Lifecycle Maker Queue");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Yes"))).click();
				}
				else {
					step.log(Status.FAIL, "Deal: "+deal.getString("name")+" not available at Deal Lifecycle Maker Queue");
					report.addStepFailScreenshot(driver, step);					
					throw new Exception("Deal: "+deal.getString("name")+" not available at Deal Lifecycle Maker Queue");
				}
				
			}		
			catch (Exception e) {
				step.log(Status.FAIL, "Error while sending deal close request"+deal.getString("name")+" to lifecycle checker");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while sending deal close request"+deal.getString("name")+" to lifecycle checker");
			}
			finally {
				driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}
	
	public void sendDealCloseRequestToChecker(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {				
		try {	
			//setPreference();				
			
			utility.waitForElementToBeDisplayed(driver, By.xpath(dealLifecycleMakerLocator.getString("column-DealName")), 2, 120);
			driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("input-FilterText"))).get(1).clear();
			driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("input-FilterText"))).get(1).sendKeys(deal.getString("name"));
			Thread.sleep(2000);
			utility.waitForElementToBeDisplayed(driver, By.xpath(dealLifecycleMakerLocator.getString("column-DealName")), 2, 120);
			WebElement selectOperation = driver.findElement(By.id(dealLifecycleMakerLocator.getString("select-OperationFilter")));
			utility.scrollIntoView(driver, selectOperation);
			Select dealOperation = new Select(driver.findElement(By.id(dealLifecycleMakerLocator.getString("select-OperationFilter"))));
			dealOperation.selectByVisibleText("Close");
			utility.waitForElementToBeDisplayed(driver, By.xpath(dealLifecycleMakerLocator.getString("column-DealName")), 2, 120);
			
			String resultDeal = driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("column-DealName"))).get(1).getText();
			if(resultDeal.equals(deal.getString("name"))){
				driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("column-Manage"))).get(1).findElements(By.tagName("i")).get(0).click();
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Submit"))).click();
				step.log(Status.INFO, "Deal: "+deal.getString("name")+" close request sent to Deal Lifecycle Maker Queue");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Yes"))).click();
			}
			else {
				step.log(Status.FAIL, "Deal: "+deal.getString("name")+" not available at Deal Lifecycle Maker Queue");
				report.addStepFailScreenshot(driver, step);					
				throw new Exception("Deal: "+deal.getString("name")+" not available at Deal Lifecycle Maker Queue");
			}
			
		}		
		catch (Exception e) {
			step.log(Status.FAIL, "Error while sending deal close request"+deal.getString("name")+" to lifecycle checker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while sending deal close request"+deal.getString("name")+" to lifecycle checker");
		}			
	}
	public void sendNewDealPauseRequestToChecker(WebDriver driver, ExtentTest step) throws Exception {				
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();			
		while(dealIterator.hasNext()) {				
			JSONObject deal = (JSONObject) dealIterator.next();			
			try {	
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("column-DealLifecycle"))).click();
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("column-LifecycleMaker"))).click();
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("column-Icon-Submit"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Yes"))).click();
			}		
			catch (Exception e) {
				step.log(Status.FAIL, "Error while sending deal pause request"+deal.getString("name")+" to lifecycle checker");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while sending deal pause request"+deal.getString("name")+" to lifecycle checker");
			}
			finally {
				driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}
	public void sendNewDealResumeRequestToChecker(WebDriver driver, ExtentTest step) throws Exception {				
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();			
		while(dealIterator.hasNext()) {				
			JSONObject deal = (JSONObject) dealIterator.next();			
			try {	
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("column-DealLifecycle"))).click();
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("column-LifecycleMaker"))).click();
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("column-Icon-Submit"))).click();
				//driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Submit"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Yes"))).click();
			}		
			catch (Exception e) {
				step.log(Status.FAIL, "Error while sending deal pause request"+deal.getString("name")+" to lifecycle checker");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while sending deal pause request"+deal.getString("name")+" to lifecycle checker");
			}
			finally {
				driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}
	public void sendNewDealCloseRequestToChecker(WebDriver driver, ExtentTest step) throws Exception {				
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();			
		while(dealIterator.hasNext()) {				
			JSONObject deal = (JSONObject) dealIterator.next();			
			try {	
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("column-DealLifecycle"))).click();
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("column-LifecycleMaker"))).click();
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("colum-Icon-Doc-submit"))).click();
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Submit"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(dealLifecycleMakerLocator.getString("button-Yes"))).click();
			}		
			catch (Exception e) {
				step.log(Status.FAIL, "Error while sending deal pause request"+deal.getString("name")+" to lifecycle checker");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while sending deal pause request"+deal.getString("name")+" to lifecycle checker");
			}
			finally {
				driver.findElements(By.xpath(dealLifecycleMakerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}
}
