package com.appveen.smartcontracts.pageobject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class DealDraftsPage {

	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	
	private LandingPage landingPage = null;
	private UpdateDealPage updateDealPage = null;
	private JSONObject dealDraftsLocator = null;
	private JSONArray deals = null;	
	
	private List<String> getDealTitleList(WebDriver driver, ExtentTest step) throws Exception {
		List<String> dealTitleList = new ArrayList<String>();
		try {
			List<WebElement> dealTitles = driver.findElements(By.xpath(dealDraftsLocator.getString("card-DealTitle")));
			
			for(WebElement dealTitle : dealTitles) {
				dealTitleList.add(dealTitle.getText().toLowerCase().trim());
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while fetching deal titles from Deal Drafts page");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while fetching deal titles from Deal Drafts page");
		}
		return dealTitleList;
	}	
	
	public DealDraftsPage(LocatorFactory locators){			
		dealDraftsLocator = locators.getLocators("DealDraftsPage");
		landingPage = new LandingPage(locators);
		updateDealPage = new UpdateDealPage(locators);		
	}
	
	public void checkDealDraftsTitles(WebDriver driver, ExtentTest step) throws Exception {					
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				
				utility.waitForElementToBeDisplayed(driver, By.id(dealDraftsLocator.getString("button-DealMenuButton")), 10);
				
				Select dealSearchType = new Select(driver.findElement(By.id(dealDraftsLocator.getString("select-DealSearchType"))));
				dealSearchType.selectByVisibleText("Deal Name");
				driver.findElement(By.id(dealDraftsLocator.getString("input-DealSearchParameter"))).clear();
				driver.findElement(By.id(dealDraftsLocator.getString("input-DealSearchParameter"))).sendKeys(deal.getString("name"));
				driver.findElement(By.id(dealDraftsLocator.getString("button-DealSearch"))).click();				
				utility.waitForProgressBarToLoad(driver);

				List <String> dealTitleList = getDealTitleList(driver, step);				
				String dealTitle = deal.getString("name").toLowerCase().trim();
				
				if(dealTitleList.contains(dealTitle)) {
					step.pass("Deal "+deal.getString("name")+" available at Deal Drafts page");
					report.addStepPassScreenshot(driver, step);
				}
				else {
					step.fail("Deal "+deal.getString("name")+"  not available at Deal Drafts page");
					report.addStepFailScreenshot(driver, step);
				}
				driver.findElement(By.id(dealDraftsLocator.getString("button-DealSearchClearFiler"))).click();
			}
	
			catch (Exception e) {
				step.log(Status.FAIL, "Error while checking deal title: "+deal.getString("name")+"from Deal Drafts page");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while checking deal title: "+deal.getString("name")+"from Deal Drafts page");
			}
		}
	}	
	
	public void editDealDrafts(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("edit_deals");
		Iterator<Object> dealIterator = deals.iterator();
		
		while(dealIterator.hasNext()) {	
			JSONObject deal = (JSONObject) dealIterator.next();
			try {				
				landingPage.goToDealDrafts(driver, step);
				
				utility.waitForElementToBeDisplayed(driver, By.id(dealDraftsLocator.getString("button-DealMenuButton")), 10);
				
				Select dealSearchType = new Select(driver.findElement(By.id(dealDraftsLocator.getString("select-DealSearchType"))));
				dealSearchType.selectByVisibleText("Deal Name");
				driver.findElement(By.id(dealDraftsLocator.getString("input-DealSearchParameter"))).clear();
				driver.findElement(By.id(dealDraftsLocator.getString("input-DealSearchParameter"))).sendKeys(deal.getString("name"));
				driver.findElement(By.id(dealDraftsLocator.getString("button-DealSearch"))).click();					
				utility.waitForProgressBarToLoad(driver);
				
				List <String> dealTitleList = getDealTitleList(driver, step);				
				String dealTitle = deal.getString("name").toLowerCase().trim();
				
				if(dealTitleList.contains(dealTitle)) {
					String actualdealID = driver.findElements(By.xpath(dealDraftsLocator.getString("card-DealDetail"))).get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText().toLowerCase().trim();
					String expectedDealID = (DealCheckerPage.dealDetails.get(dealTitle) == null) ? NewDealPage.dealDetails.get(dealTitle).toLowerCase().trim() : DealCheckerPage.dealDetails.get(dealTitle).toLowerCase().trim();
					
					if(actualdealID.equals(expectedDealID)) {
						driver.findElements(By.id(dealDraftsLocator.getString("button-DealMenuButton"))).get(dealTitleList.indexOf(dealTitle)).click();
						WebElement editDeal = driver.findElement(By.id(dealDraftsLocator.getString("menu-DealPopupMenu"))).findElements(By.tagName("div")).get(1);
						editDeal.click();				
						step.log(Status.INFO, "Initiated edit for deal draft: "+dealTitle);
						
						updateDealPage.editDeal(driver, step, deal);
					}
					else {
						step.log(Status.FAIL, "Unable to validate Deal ID. (Expected: "+expectedDealID+"/ Actual: "+actualdealID+")");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Unable to validate Deal ID. (Expected: "+expectedDealID+"/ Actual: "+actualdealID+")");
					}
					
				}
				else {
					step.log(Status.FAIL, "Unable to find Deal: "+dealTitle);
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Unable to find Deal: "+dealTitle);
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while initiating edit for deal draft: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while initiating edit for deal draft: "+deal.getString("name"));
			}
		}		
	}
	
	public void viewUpdatedDealDrafts(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("edit_deals");
		Iterator<Object> dealIterator = deals.iterator();
		
		while(dealIterator.hasNext()) {	
			JSONObject deal = (JSONObject) dealIterator.next();
			try {				
				landingPage.goToDealDrafts(driver, step);
				
				utility.waitForElementToBeDisplayed(driver, By.id(dealDraftsLocator.getString("button-DealMenuButton")), 10);
				
				Select dealSearchType = new Select(driver.findElement(By.id(dealDraftsLocator.getString("select-DealSearchType"))));
				dealSearchType.selectByVisibleText("Deal Name");
				driver.findElement(By.id(dealDraftsLocator.getString("input-DealSearchParameter"))).clear();
				driver.findElement(By.id(dealDraftsLocator.getString("input-DealSearchParameter"))).sendKeys(deal.getString("name"));
				driver.findElement(By.id(dealDraftsLocator.getString("button-DealSearch"))).click();					
				utility.waitForProgressBarToLoad(driver);
				
				List <String> dealTitleList = getDealTitleList(driver, step);				
				String dealTitle = deal.getString("name").toLowerCase().trim();
				
				if(dealTitleList.contains(dealTitle)) {
					String actualdealID = driver.findElements(By.xpath(dealDraftsLocator.getString("card-DealDetail"))).get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText().toLowerCase().trim();
					String expectedDealID = (DealCheckerPage.dealDetails.get(dealTitle) == null) ? NewDealPage.dealDetails.get(dealTitle).toLowerCase().trim() : DealCheckerPage.dealDetails.get(dealTitle).toLowerCase().trim();
					
					if(actualdealID.equals(expectedDealID)) {
						driver.findElements(By.id(dealDraftsLocator.getString("button-DealMenuButton"))).get(dealTitleList.indexOf(dealTitle)).click();
						WebElement viewDeal = driver.findElement(By.id(dealDraftsLocator.getString("menu-DealPopupMenu"))).findElements(By.tagName("div")).get(0);
						viewDeal.click();				
						step.log(Status.INFO, "Initiated view for deal draft: "+dealTitle);
						
						updateDealPage.viewDeal(driver, step, deal);
					}
					else {
						step.log(Status.FAIL, "Unable to validate Deal ID. (Expected: "+expectedDealID+"/ Actual: "+actualdealID+")");
						report.addStepFailScreenshot(driver, step);
						throw new Exception("Unable to validate Deal ID. (Expected: "+expectedDealID+"/ Actual: "+actualdealID+")");
					}
					
				}
				else {
					step.log(Status.FAIL, "Unable to find Deal: "+dealTitle);
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Unable to find Deal: "+dealTitle);
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while initiating view for deal draft: "+deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while initiating view for deal draft: "+deal.getString("name"));
			}
		}		
	}
}
