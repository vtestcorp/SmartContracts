package com.appveen.smartcontracts.pageobject;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;

public class PauseDealPage {
	JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject pauseDealLocator = null;
	
	public PauseDealPage(LocatorFactory locators) {
		pauseDealLocator = locators.getLocators("auseDealPage");
	}
	public void liveDeals(WebDriver driver, ExtentTest step) {
		driver.findElement(By.xpath(pauseDealLocator.getString("select-Deals"))).click();

	}

}
