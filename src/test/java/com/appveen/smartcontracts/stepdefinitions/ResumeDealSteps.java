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

public class ResumeDealSteps {
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

	@Given("^I login as \"([^\"]*)\" and resume live deal$")
	public void i_login_as_and_resume_live_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and resume live deal");
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				driver = webDriverFactory.goToSmartContracts();
				loginPage.performLogin(driver, step, user);
				liveDealPage.resumeNewLiveDeal(driver, step, deal);
				landingPage.performLogout(driver, step);

			} catch (Exception e) {
				landingPage.performLogout(driver, step);
				throw new Exception(e.getMessage());

			}

		}

	}

	@Given("^I login as \"([^\"]*)\" and send resume deal$")
	public void i_login_as_and_send_resume_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and send resume deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			dealLifecycleMakerPage.sendNewDealResumeRequestToChecker(driver, step);
			landingPage.performLogout(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());

		}

	}

	@When("^I login as \"([^\"]*)\" and approve resume deal$")
	public void i_login_as_and_approve_resume_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and approve resume deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			dealLifecycleCheckerPage.approveNewDealResumeRequest(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());

		}

	}

	@Then("Deal should be resume")
	public void deal_should_be_resume() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deal should be resume");
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();

		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				liveDealPage.verifyResumeDeal(driver, step, deal);
				landingPage.performLogout(driver, step);
			} catch (Exception e) {
				landingPage.performLogout(driver, step);
				throw new Exception(e.getMessage());

			}
		}

	}

	@Given("^I login as \"([^\"]*)\" and close live deal$")
	public void i_login_as_and_close_live_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and close live deal");
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				driver = webDriverFactory.goToSmartContracts();
				loginPage.performLogin(driver, step, user);
				liveDealPage.closeNewLiveDeal(driver, step, deal);
				landingPage.performLogout(driver, step);
			} catch (Exception e) {
				landingPage.performLogout(driver, step);
				throw new Exception(e.getMessage());

			}

		}
	}

	@Given("^I login as \"([^\"]*)\" and send close deal$")
	public void i_login_as_and_send_close_deal(String user) throws Throwable {

		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and send close deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			dealLifecycleMakerPage.sendNewDealCloseRequestToChecker(driver, step);
			landingPage.performLogout(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());

		}

	}

	@When("^I login as \"([^\"]*)\" and approve close deal$")
	public void i_login_as_and_approve_close_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and approve close deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			dealLifecycleCheckerPage.approveNewDealCloseRequest(driver, step);
			

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());

		}
		
	}
	@Then("Deal should be close")
	public void deal_should_be_close() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deal should be close");
		JSONArray deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();

		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				liveDealPage.verifyCloseDeal(driver, step, deal);
				landingPage.performLogout(driver, step);
			} catch (Exception e) {
				landingPage.performLogout(driver, step);
				throw new Exception(e.getMessage());

			}
		}
	    
	}

}
