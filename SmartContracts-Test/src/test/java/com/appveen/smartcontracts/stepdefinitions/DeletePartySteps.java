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

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeletePartySteps {
	
	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	LocatorFactory locators = ListenerFactory.locators; 
	ReportFactory report = ListenerFactory.reportFactory;
	
	LoginPage loginPage = new LoginPage(locators);
	LandingPage landingPage = new LandingPage(locators);
	LiveDealsPage liveDealsPage = new LiveDealsPage(locators);
	PartyMakerPage partyMakerPage = new PartyMakerPage(locators);
	PartyCheckerPage partyCheckerPage = new PartyCheckerPage(locators);	

	
	@When("^I delete the party$")
	public void I_delete_the_party() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I delete the party");
		try {
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.deleteParties(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}	
	
	@Then("^Party should not be available at party maker queue$")
	public void Party_should_not_be_available_at_party_maker_queue() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Party should should not be available at party maker queue");
		try {
			partyMakerPage.checkPartiesNotAvailable(driver, step);
			landingPage.performLogout(driver, step);		
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
	}

}
