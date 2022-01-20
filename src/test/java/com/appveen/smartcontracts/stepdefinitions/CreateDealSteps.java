package com.appveen.smartcontracts.stepdefinitions;

import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.appveen.smartcontracts.pageobject.DealCheckerPage;
import com.appveen.smartcontracts.pageobject.LandingPage;
import com.appveen.smartcontracts.pageobject.LiveDealsPage;
import com.appveen.smartcontracts.pageobject.LoginPage;
import com.appveen.smartcontracts.pageobject.NewDealPage;
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class CreateDealSteps {
	
	  WebDriverFactory webDriverFactory = new WebDriverFactory();
	  WebDriver driver = null;
	  LocatorFactory locators = ListenerFactory.locators; 
	  ReportFactory report = ListenerFactory.reportFactory;
	  
	  LoginPage loginPage = new LoginPage(locators);
	  LandingPage landingPage = new LandingPage(locators);
	  NewDealPage newDealPage = new NewDealPage(locators);
	  DealCheckerPage dealCheckerPage = new DealCheckerPage(locators);
	  LiveDealsPage liveDealsPage = new LiveDealsPage(locators);

	@Given("^I login as \"([^\"]*)\" and create deal$")
	public void I_login_as_user_and_create_deal(String user) throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and create deal");	
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);		  
			newDealPage.createDeals(driver, step);	
			landingPage.performLogout(driver, step);		
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@When("^I login as \"([^\"]*)\" and approve deal$")
	public void I_login_as_user_and_approve_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve deal");
		try {
			loginPage.performLogin(driver, step,  user);
			landingPage.goToDealChecker(driver, step);
			dealCheckerPage.approveDeals(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
	@Then("^Deal should be live$")
	public void Deal_should_be_live() throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deal should be live");
		try {
			loginPage.performLogin(driver, step, "dealmaker");
			landingPage.goToLiveDeal(driver, step);
			liveDealsPage.checkLiveDealTitles(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	  
	
	
	


}
