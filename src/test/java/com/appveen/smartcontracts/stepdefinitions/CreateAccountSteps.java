package com.appveen.smartcontracts.stepdefinitions;

import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;

import com.appveen.smartcontracts.pageobject.ODPAccountsPage;
import com.appveen.smartcontracts.pageobject.ODPLandingPage;
import com.appveen.smartcontracts.pageobject.ODPLoginPage;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateAccountSteps {
	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	LocatorFactory locators = ListenerFactory.locators; 
	ReportFactory report = ListenerFactory.reportFactory;
	  
	ODPLoginPage odpLoginPage = new ODPLoginPage(locators);
	ODPLandingPage odpLandingPage = new ODPLandingPage(locators);
	ODPAccountsPage odpAccountsPage = new ODPAccountsPage(locators);  
    
	@When("^I login to ODP and create account$")
	public void I_login_to_ODP_and_create_account() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login to ODP and create account");
		try {
			driver = webDriverFactory.goToODP();
			odpLoginPage.performLogin(driver, step, "admin");
			odpLandingPage.selectApplication(driver, step, "acache");
			odpLandingPage.goToAccounts(driver, step);	
			odpLandingPage.goToAddData(driver, step);
			odpAccountsPage.createAccounts(driver, step); 
		}
		catch (Exception e) {
			odpLandingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@Then("^Account should be created$")
	public void Account_should_be_created()	throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Account should be created");
		try {
			odpLandingPage.goToAccounts(driver, step);
			step.log(Status.INFO, "Accounts are created");
			Thread.sleep(2000);
			report.addStepInfoScreenshot(driver, step);
		}
		catch (Exception e) {
			odpLandingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}
	
}
