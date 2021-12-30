package com.appveen.smartcontracts.stepdefinitions;

import org.json.JSONObject;
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

public class BudgetSteps {
	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	LocatorFactory locators = ListenerFactory.locators; 
	ReportFactory report = ListenerFactory.reportFactory;
	JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	
	LoginPage loginPage = new LoginPage(locators);
	LandingPage landingPage = new LandingPage(locators);
	NewDealPage newDealPage = new NewDealPage(locators);
	DealCheckerPage dealCheckerPage = new DealCheckerPage(locators);
	LiveDealsPage liveDealsPage = new LiveDealsPage(locators);
	
//BUDGET_TC001_Verify_addition_of_consolidated_budget_to_new_deal
	@Given("^I login as \"([^\"]*)\" and add consolidated budget to new deal$")
	public void I_login_as_user_and_add_consolidate_budget_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and consolidated budget to new deal");	
		try{
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);			
			newDealPage.createDeals(driver, step);		
			landingPage.performLogout(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
	@When("^I login as \"([^\"]*)\" and approve deal with budget$")
	public void I_login_as_user_and_approve_deal_with_budget(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve deal with budget");
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
	
	@Then("^Deal with budget should be live$")
	public void Deal_with_budget_should_be_live() throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deal with budget should be live");
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
	
//BUDGET_TC002_Verify_addition_of_purpose_budget_to_new_deal
	@Given("^I login as \"([^\"]*)\" and add purpose budget to new deal$")
	public void I_login_as_user_and_add_purpose_budget_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and purpose budget to new deal");	
		try{
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);			
			newDealPage.createDeals(driver, step);		
			landingPage.performLogout(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
//BUDGET_TC003_Verify_addition_of_destination_budget_to_new_deal
	@Given("^I login as \"([^\"]*)\" and add destination budget to new deal$")
	public void I_login_as_user_and_add_destination_budget_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and destination budget to new deal");	
		try{
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);			
			newDealPage.createDeals(driver, step);		
			landingPage.performLogout(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
//
}
