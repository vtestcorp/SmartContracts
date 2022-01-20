package com.appveen.smartcontracts.stepdefinitions;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.appveen.smartcontracts.pageobject.LandingPage;
import com.appveen.smartcontracts.pageobject.LoginPage;
import com.appveen.smartcontracts.pageobject.ManageConfigsPage;
import com.appveen.smartcontracts.pageobject.TaxCategriePage;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TaxCategriesSteps {
	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	ReportFactory report = ListenerFactory.reportFactory;
	JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());

	LocatorFactory locators = ListenerFactory.locators;

	LoginPage loginPage = new LoginPage(locators);
	LandingPage landingPage = new LandingPage(locators);
	ManageConfigsPage manageConfigsPage = new ManageConfigsPage(locators);
	TaxCategriePage taxcategriepage = new TaxCategriePage(locators);

	@Given("^I login as \"([^\"]*)\" and create tax category$")
	public void i_login_as_and_create_tax_category(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and create tax category");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			landingPage.goToManageConfigs(driver, step);
			manageConfigsPage.goToTaxCategries(driver, step);
			taxcategriepage.addTaxCategory(driver, step);
			landingPage.performLogout(driver, step);
		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}

	}

	@When("^I login as \"([^\"]*)\" and approve tax_category$")
	public void i_login_as_and_approve_tax_category(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"When: I login as " + user + " and approve tax_category");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			landingPage.goToManageConfigs(driver, step);
			manageConfigsPage.goToTaxCategries(driver, step);
			taxcategriepage.addTaxCategoryAdminCheker(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}

	}

	@Then("Tax category should be displayed at Admin queue")
	public void tax_category_should_be_displayed_at_admin_queue() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Then:Tax category should be displayed at Admin queue");
		try {

			taxcategriepage.TaxategoryShouldDisplayed(driver, step);
			landingPage.performLogout(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}

	}

}
