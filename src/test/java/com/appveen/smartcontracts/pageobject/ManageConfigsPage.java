package com.appveen.smartcontracts.pageobject;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ManageConfigsPage {
	
	private JSONObject manageConfigsLocator = null;
	private ReportFactory report = ListenerFactory.reportFactory;	
	
	
	public ManageConfigsPage(LocatorFactory locators) {	
		manageConfigsLocator = locators.getLocators("ManageConfigsPage");		
	}
	
	public void goToProductConfigs(WebDriver driver, ExtentTest step) throws Exception {
		
		try {
			driver.findElement(By.xpath(manageConfigsLocator.getString("card-Product"))).click();
			step.log(Status.INFO, "Moved to Product Configs");
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Product Configs");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());			
			throw new Exception("Error while moving to Product Configs");
		}
	}

	public void goToTaxCategries(WebDriver driver, ExtentTest step) throws Exception {
		
		try {
			driver.findElement(By.xpath(manageConfigsLocator.getString("card-TaxCategries"))).click();
			step.log(Status.INFO, "Moved to Tax Categries");
			Thread.sleep(1000);
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Tax Categries");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());			
			throw new Exception("Error while moving to Tax Categries");
		}
	}
	
}
