package com.appveen.smartcontracts.stepdefinitions;

import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.appveen.smartcontracts.pageobject.DealLifecycleMakerPage;
import com.appveen.smartcontracts.pageobject.ExecutionReportPage;
import com.appveen.smartcontracts.pageobject.LandingPage;
import com.appveen.smartcontracts.pageobject.LiveDealsPage;
import com.appveen.smartcontracts.pageobject.LoginPage;
import com.appveen.smartcontracts.pageobject.TransactionCheckerPage;
import com.appveen.smartcontracts.pageobject.TransactionMakerPage;
import com.appveen.smartcontracts.pageobject.TransactionsBulkUploadPage;
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TransactionSteps {

	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	LocatorFactory locators = ListenerFactory.locators;
	ReportFactory report = ListenerFactory.reportFactory;

	LoginPage loginPage = new LoginPage(locators);
	LandingPage landingPage = new LandingPage(locators);
	LiveDealsPage liveDealsPage = new LiveDealsPage(locators);
	TransactionMakerPage transactionMakerPage = new TransactionMakerPage(locators);
	TransactionCheckerPage transactionCheckerPage = new TransactionCheckerPage(locators);
	TransactionsBulkUploadPage transactionBulkUploadPage = new TransactionsBulkUploadPage(locators);
	ExecutionReportPage executionReportPage = new ExecutionReportPage(locators);
	DealLifecycleMakerPage dealLifecycleMakerPage = new DealLifecycleMakerPage(locators);

//TRANSACTION_TCXXX_Verify_bulk_upload_of_transaction	  
	@Given("^I login as \"([^\"]*)\" and bulk upload transactions$")
	public void I_login_as_user_and_bulk_upload_transactions(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and bulk upload transactions");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			transactionBulkUploadPage.uploadBulkTransactions(driver, step);
			landingPage.performLogout(driver, step);
		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}
	}

	@When("^I login as \"([^\"]*)\" and approve bulk of transactions$")
	public void I_login_as_user_and_approve_bulk_of_transactions(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"When: I login as " + user + " and approve bulk of transactions");
		try {
			loginPage.performLogin(driver, step, user);
			landingPage.goToTransactionChecker(driver, step);
			transactionCheckerPage.approveAllBulkTransactions(driver, step);
		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}
	}

	@Then("^Transactions should be displayed in internal execution report$")
	public void Transactions_should_be_displayed_at_internal_execution_report() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Then: Transactions should be displayed in internal execution report");
		try {
			// landingPage.goToInternalExecutionReport();
			// executionReportPage.checkExecutionStatus();
		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}

	}

	@Given("^I login as \"([^\"]*)\" and create transaction for live deal$")
	public void I_login_as_user_and_create_transaction_for_live_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and create transaction for live deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			liveDealsPage.addTransactions(driver, step);
			landingPage.performLogout(driver, step);
		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}
	}

	@Given("^I login as \"([^\"]*)\" and submit transactions$")
	public void I_login_as_user_and_submit_transactions(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Given: I login as " + user + " and submit transactions");
		try {
			loginPage.performLogin(driver, step, user);
			landingPage.goToTransactionMaker(driver, step);
			transactionMakerPage.submitAllTransactions(driver, step);
			landingPage.performLogout(driver, step);
		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}
	}

	@When("^I login as \"([^\"]*)\" and approve transactions$")
	public void I_login_as_user_and_approve_transactions(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"When: I login as " + user + " and approve transactions");
		try {
			loginPage.performLogin(driver, step, user);
			landingPage.goToTransactionChecker(driver, step);
			transactionCheckerPage.approveAllTransactions(driver, step);
		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}
	}

	@Then("^Transactions should be available at execution report$")
	public void Transactions_should_be_available_at_execution_report() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"Then: Transactions should be available at execution report");
		try {
			landingPage.goToInternalExecutionReport(driver, step);
			executionReportPage.checkExecutionStatus(driver, step);
		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}

	}

	@Given("^I login as \"([^\"]*)\" and create transaction for ecommerce$")
	public void i_login_as_and_create_transaction_for_ecommerce(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"When: I login as " + user + " and create transaction for ecommerce$");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			landingPage.goToEcommerceTxnMaker(driver, step);
			landingPage.performLogout(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}

	}

	@When("^I login as \"([^\"]*)\" and approve transactions for ecommerce$")
	public void i_login_as_and_approve_transactions_for_ecommerce(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"When: I login as " + user + " and approve transactions for ecommerce");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			landingPage.goToEcommerceTxnChekar(driver, step);
			landingPage.performLogout(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}

	}
	@Then("^I login as \"([^\"]*)\" and verifiy transactions for ecommerce$")
	public void i_login_as_and_verifiy_transactions_for_ecommerce(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"When: I login as " + user + " and verifiy transactions for ecommerce");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			landingPage.goToEcommerceTxnVerifire(driver, step);
			landingPage.performLogout(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}
	    
	}
	
	@Given("^I login as \"([^\"]*)\" and create party for ecommerce$")
	public void i_login_as_and_create_party_for_ecommerce(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(),
				"When: I login as " + user + "  and create party for ecommerce");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step, user);
			liveDealsPage.goToDealDraft(driver, step);

		} catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage());
		}
	    
	}


}
