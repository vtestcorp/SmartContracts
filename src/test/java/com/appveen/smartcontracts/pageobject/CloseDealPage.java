package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CloseDealPage {
	WebDriverFactory webDriverFactory = new WebDriverFactory();
	JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	WebDriver driver = null;
	ReportFactory report = ListenerFactory.reportFactory;
	LocatorFactory locators = ListenerFactory.locators;
	ManageConfigsPage manageConfigsPage = new ManageConfigsPage(locators);
	NewDealPage newDealPage = new NewDealPage(locators);
	LoginPage loginPage = new LoginPage(locators);
	LandingPage landingPage = new LandingPage(locators);
	LiveDealsPage liveDealPage = new LiveDealsPage(locators);
	DealLifecycleMakerPage dealLifecycleMakerPage = new DealLifecycleMakerPage(locators);
	DealLifecycleCheckerPage dealLifecycleCheckerPage = new DealLifecycleCheckerPage(locators);
	
	

}
