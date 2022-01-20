package com.appveen.smartcontracts.stepdefinitions;

import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.appveen.smartcontracts.pageobject.LandingPage;
import com.appveen.smartcontracts.pageobject.LiveDealsPage;
import com.appveen.smartcontracts.pageobject.LoginPage;
import com.appveen.smartcontracts.pageobject.PartyCheckerPage;
import com.appveen.smartcontracts.pageobject.PartyMakerPage;
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class EditPartySteps {

	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	LocatorFactory locators = ListenerFactory.locators; 
	ReportFactory report = ListenerFactory.reportFactory;
	
	LoginPage loginPage = new LoginPage(locators);
	LandingPage landingPage = new LandingPage(locators);
	LiveDealsPage liveDealsPage = new LiveDealsPage(locators);
	PartyMakerPage partyMakerPage = new PartyMakerPage(locators);
	PartyCheckerPage partyCheckerPage = new PartyCheckerPage(locators);
	
	@Given("^I login as \"([^\"]*)\" and check party is available at party maker queue$")
	public void I_login_as_user_and_check_party_is_available_at_party_maker_queue(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and check party is available at party maker queue");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.checkPartiesAvailable(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@Given("^I edit the party$")
	public void I_edit_the_party() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I edit the party");
		try {
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.editParties(driver, step);
			landingPage.performLogout(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@When("^I login as \"([^\"]*)\" and approve updated party$")
	public void I_login_as_user_and_approve_updated_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve updated party");	
		try {
			loginPage.performLogin(driver, step, user);
			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.approveParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@Then("^Party should be updated$")
	public void Party_should_be_updated() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Party should be updated");	
		try {
			//TO BE DISCUSSED
			landingPage.performLogout(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}
}
