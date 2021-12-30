package com.appveen.smartcontracts.stepdefinitions;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.appveen.smartcontracts.pageobject.DealLifecycleCheckerPage;
import com.appveen.smartcontracts.pageobject.DealLifecycleMakerPage;
import com.appveen.smartcontracts.pageobject.LandingPage;
import com.appveen.smartcontracts.pageobject.LiveDealsPage;
import com.appveen.smartcontracts.pageobject.LoginPage;
import com.appveen.smartcontracts.pageobject.ManageConfigsPage;
import com.appveen.smartcontracts.pageobject.NewDealPage;
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PauseDealSteps {
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

	@Given("^I login as \"([^\"]*)\" and pause live deal$")
	public void I_login_as_and_pause_live_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and pause live deal");
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();

		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				driver = webDriverFactory.goToSmartContracts();
				loginPage.performLogin(driver, step, user);
				newDealPage.createNewDeals(driver, step);
				liveDealPage.pauseNewLiveDeal(driver, step, deal);
				landingPage.performLogout(driver, step);

			} catch (Exception e) {
				landingPage.performLogout(driver, step);
				throw new Exception(e.getMessage());

			}

		}
	}

	@Given("^I login as \"([^\"]*)\" and send pause deal$")
	public void i_login_as_and_send_pause_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and send pause deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			dealLifecycleMakerPage.sendNewDealPauseRequestToChecker(driver, step);
			landingPage.performLogout(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());

		}

	}

	@When("^I login as \"([^\"]*)\" and approve pause deal$")
	public void i_login_as_and_approve_pause_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and send pause deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			dealLifecycleCheckerPage.approveNewDealPauseRequest(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());

		}

	}

	@Then("Deal should be pause")
	public void deal_should_be_pause() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Deal should be pause ");
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();

		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				liveDealPage.verifyPauseDeal(driver, step, deal);
				landingPage.performLogout(driver, step);
			} catch (Exception e) {
				landingPage.performLogout(driver, step);
				throw new Exception(e.getMessage());

			}
		}
	}

}
