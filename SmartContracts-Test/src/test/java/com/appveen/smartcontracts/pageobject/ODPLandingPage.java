package com.appveen.smartcontracts.pageobject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ODPLandingPage {
	
	private WebDriverFactory webDriverFactory = new WebDriverFactory();
	private JSONObject odpLandingLocator = null;
	private ReportFactory report = ListenerFactory.reportFactory;	
	private UtilityFactory utility = new UtilityFactory();
	
	public ODPLandingPage(LocatorFactory locators) {
		odpLandingLocator = locators.getLocators("ODPLandingPage");
	}
	
	public void selectApplication(WebDriver driver, ExtentTest step,  String applicationName) throws Exception {
		try {
			if(!driver.findElement(By.xpath(odpLandingLocator.getString("select-CurrentApp"))).getText().equals(applicationName)) {
				driver.findElement(By.xpath(odpLandingLocator.getString("select-CurrentApp"))).click();
				
				List<WebElement> applicationElements = driver.findElements(By.xpath(odpLandingLocator.getString("label-AppName")));
				for(WebElement element : applicationElements) {
					if(element.getText().equals(applicationName)) {
						element.click();
						step.log(Status.INFO, "Selected application: "+applicationName);
						return;
					}
				}
			}			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while selecting application: "+applicationName);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while selecting application: "+applicationName);
		}
	}
	
	public void applyFilter(WebDriver driver, ExtentTest step,  JSONObject filters) throws Exception {
		try {
			driver.findElement(By.xpath(odpLandingLocator.getString("button-Filter"))).click();
			
			driver.findElement(By.id(odpLandingLocator.getString("input-FilterColumns"))).sendKeys("ID");
			driver.findElement(By.id(odpLandingLocator.getString("button-FilterColumn"))).click();
			Iterator<String> filterIterator = filters.keys();
			
			int searchForIndex = 0;
			while(filterIterator.hasNext()) {
				String filterKey = filterIterator.next();
				
				driver.findElement(By.id(odpLandingLocator.getString("input-FilterColumns"))).sendKeys(filterKey);
				Thread.sleep(500);
				driver.findElement(By.id(odpLandingLocator.getString("button-FilterColumn"))).click();
				
				driver.findElement(By.xpath(odpLandingLocator.getString("button-FilterSearchForSelectField"))).click();
				Thread.sleep(500);
				Select filterField = new Select(driver.findElements(By.xpath(odpLandingLocator.getString("select-FilterSearchFor"))).get(searchForIndex));
				filterField.selectByVisibleText(filterKey);				
				driver.findElements(By.xpath(odpLandingLocator.getString("input-FilterSearchForInput"))).get(searchForIndex).sendKeys(filters.getString(filterKey));
				searchForIndex++;				
			}
			driver.findElement(By.xpath(odpLandingLocator.getString("button-FilterApply"))).click();
			Thread.sleep(1000);
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while applying filters");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while applying filters");
		}
		
	}	
	
	public List<String> fetchfieldValues(WebDriver driver, ExtentTest step,  String fieldName) throws Exception {
		List<String> fieldValues = new ArrayList<String>();
		try {
			List<WebElement> headerFields = driver.findElements(By.xpath(odpLandingLocator.getString("label-HeaderCell")));
			int filterIndex = -1;
			for(int index = 0; index < headerFields.size(); index++) {
				if(headerFields.get(index).getText() == fieldName) {
					filterIndex = index;
					break;
				}
			}
			
			if(filterIndex != -1) {
				List<WebElement> records = driver.findElement(By.xpath(odpLandingLocator.getString("label-DataBody"))).findElements(By.xpath(odpLandingLocator.getString("label-DataRow")));
				if(records.size()>0) {
					for(WebElement record : records) {
						fieldValues.add(record.findElements(By.xpath(odpLandingLocator.getString("label-DataCell"))).get(filterIndex).getText());
					}
				}
				else {
					step.log(Status.FAIL, "No records present under field: "+fieldName);
					report.addStepFailScreenshot(driver, step);
				}
				
			}
			else {
				step.log(Status.FAIL, "Field: "+fieldName+" not present");	
				report.addStepFailScreenshot(driver, step);
			}
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while fetching values of field: "+fieldName);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while fetching values of field: "+fieldName);
		}
		return fieldValues;
	}
	
	public void gotoRecord(WebDriver driver, ExtentTest step,  String ID) throws Exception {
		try {
			List<WebElement> headerFields = driver.findElements(By.xpath(odpLandingLocator.getString("label-HeaderCell")));
			int filterIndex = -1;
			for(int index = 0; index < headerFields.size(); index++) {
				if(headerFields.get(index).getText() == "ID") {
					filterIndex = index;
					break;
				}
			}
			
			if(filterIndex != -1) {
				List<WebElement> records = driver.findElement(By.xpath(odpLandingLocator.getString("label-DataBody"))).findElements(By.xpath(odpLandingLocator.getString("label-DataRow")));
				if(records.size()>0) {
					for(WebElement record : records) {
						WebElement recordID = record.findElements(By.xpath(odpLandingLocator.getString("label-DataCell"))).get(filterIndex);
						if(recordID.getText() == ID) {
							record.click();
							break;
						}
					}
				}
				else {
					step.log(Status.FAIL, "No records present under field: ID");
					report.addStepFailScreenshot(driver, step);
				}
				
			}
			else {
				step.log(Status.FAIL, "Field: ID not present");	
				report.addStepFailScreenshot(driver, step);
			}
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while fetching values of field: ID");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while fetching values of field: ID");
		}
	}
	
	public void goToAddData(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.id(odpLandingLocator.getString("button-AddData"))).click();
			step.log(Status.INFO, "Moved to Add Data");
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Add Data");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Add Data");
		}
	}
	
	public void goToAccounts(WebDriver driver, ExtentTest step) throws Exception {
		try {			
			Thread.sleep(1000);
			driver.findElement(By.xpath(odpLandingLocator.getString("input-Search"))).clear();
			driver.findElement(By.xpath(odpLandingLocator.getString("input-Search"))).sendKeys("accounts");
			Thread.sleep(2000);
			driver.findElement(By.xpath(odpLandingLocator.getString("sidemenu-Accounts"))).click();
			step.log(Status.INFO, "Moved to Accounts");
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Accounts");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Accounts");
		}
	}
	
	public void goToEmail(WebDriver driver, ExtentTest step) throws Exception {
		try {			
			Thread.sleep(1000);
			driver.findElement(By.xpath(odpLandingLocator.getString("input-Search"))).clear();
			driver.findElement(By.xpath(odpLandingLocator.getString("input-Search"))).sendKeys("email");
			Thread.sleep(2000);
			driver.findElement(By.xpath(odpLandingLocator.getString("sidemenu-Email"))).click();
			step.log(Status.INFO, "Moved to Email");
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Email");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Email");
		}
	}
	
	public void performLogout(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.xpath(odpLandingLocator.getString("button-Username"))).click();
			driver.findElement(By.xpath(odpLandingLocator.getString("button-Logout"))).click();
			if(!utility.isElementHiddenNow(driver, By.xpath(odpLandingLocator.getString("button-UnsavedChangesYes")))) {
				driver.findElement(By.xpath(odpLandingLocator.getString("button-UnsavedChangesYes"))).click();
			}
			driver.manage().deleteAllCookies();
			Thread.sleep(2000);
			step.log(Status.INFO, "Logged out from ODP");
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			driver.navigate().refresh();
			Thread.sleep(2000);
			driver.findElement(By.xpath(odpLandingLocator.getString("button-Username"))).click();
			driver.findElement(By.xpath(odpLandingLocator.getString("button-Logout"))).click();
			Thread.sleep(2000);
			if(!utility.isElementHiddenNow(driver, By.xpath(odpLandingLocator.getString("button-UnsavedChangesYes")))) {
				driver.findElement(By.xpath(odpLandingLocator.getString("button-UnsavedChangesYes"))).click();
			}
			driver.manage().deleteAllCookies();
			step.log(Status.INFO, "Logged out from ODP on second attempt");
			webDriverFactory.closeWebDriver(driver);
		}
	}
}
