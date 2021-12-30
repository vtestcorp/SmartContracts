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
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class EditDealSteps {
	
	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	LocatorFactory locators = ListenerFactory.locators; 
	ReportFactory report = ListenerFactory.reportFactory;
	LoginPage loginPage = new LoginPage(locators);
	LandingPage landingPage = new LandingPage(locators);
	LiveDealsPage liveDealsPage = new LiveDealsPage(locators);
	DealCheckerPage dealCheckerPage = new DealCheckerPage(locators);
	
	@Given("^I login as \"([^\"]*)\" and edit live deal$")
	  public void I_login_as_user_and_edit_live_deal(String user) throws Throwable {
		  ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and edit live deal");	
		  try {
			  driver = webDriverFactory.goToSmartContracts();
			  loginPage.performLogin(driver, step, user);			  
			  liveDealsPage.editLiveDeals(driver, step);		  
			  landingPage.performLogout(driver, step);	
		  }
		  catch (Exception e) {
				landingPage.performLogout(driver, step);
				throw new Exception(e.getMessage()); 
			}
		}
	
	@When("^I login as \"([^\"]*)\" and approve updated deal$")
	public void I_login_as_user_and_approve_updated_deal(String user) throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve updated deal");	
		try {
			loginPage.performLogin(driver, step, user);			
			landingPage.goToDealChecker(driver, step);		
			dealCheckerPage.approveUpdatedDeals(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}
}
