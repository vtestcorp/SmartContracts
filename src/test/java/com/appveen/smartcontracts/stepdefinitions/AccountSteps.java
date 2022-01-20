package com.appveen.smartcontracts.stepdefinitions;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.appveen.smartcontracts.pageobject.DealCheckerPage;
import com.appveen.smartcontracts.pageobject.DealDraftsPage;
import com.appveen.smartcontracts.pageobject.ExecutionReportPage;
import com.appveen.smartcontracts.pageobject.LandingPage;
import com.appveen.smartcontracts.pageobject.LiveDealsPage;
import com.appveen.smartcontracts.pageobject.LoginPage;
import com.appveen.smartcontracts.pageobject.NewDealPage;
import com.appveen.smartcontracts.pageobject.ODPAccountsPage;
import com.appveen.smartcontracts.pageobject.ODPLandingPage;
import com.appveen.smartcontracts.pageobject.ODPLoginPage;
import com.appveen.smartcontracts.pageobject.TransactionCheckerPage;
import com.appveen.smartcontracts.pageobject.UpdateDealPage;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountSteps {
	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	LocatorFactory locators = ListenerFactory.locators; 
	ReportFactory report = ListenerFactory.reportFactory;
	JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	
	LoginPage loginPage = new LoginPage(locators);
	LandingPage landingPage = new LandingPage(locators);
	LiveDealsPage liveDealsPage = new LiveDealsPage(locators);
	NewDealPage newDealPage = new NewDealPage(locators);
	UpdateDealPage updatedDealPage = new UpdateDealPage(locators);
	DealDraftsPage dealDraftsPage = new DealDraftsPage(locators);
	DealCheckerPage dealCheckerPage = new DealCheckerPage(locators);
	ODPLoginPage odpLoginPage = new ODPLoginPage(locators);
	ODPLandingPage odpLandingPage = new ODPLandingPage(locators);
	ODPAccountsPage odpAccountsPage = new ODPAccountsPage(locators);
	TransactionCheckerPage transactionCheckerPage = new TransactionCheckerPage(locators);
	ExecutionReportPage executionReportPage = new ExecutionReportPage(locators);


//ACCOUNT_TC001_Verify_addition_of_new_account_to_new_deal	

	@When("^I login as \"([^\"]*)\" and onboard account to new deal$")
	public void I_login_as_user_and_add_onboard_account_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and onboard account to new deal");
		try {
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
	
	@When("^New deal is submitted to deal checker$")
	public void New_deal_is_submitted_to_deal_checker() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: New deal is submitted to deal checker");
		try {
			step.log(Status.PASS, "New deal is submitted to deal checker for approval");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	 
	@Then("^Deal should be available at deal checker queue$")
	public void Deal_should_be_available_at_deal_checker_queue() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deal should be available at deal checker queue");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "dealchecker");	
			landingPage.goToDealChecker(driver, step);
			dealCheckerPage.validateDeals(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
//ACCOUNT_TC002_Verify_addition_of_new_account_to_existing_deal
	@Given("^A deal is live$")
	public void A_deal_is_live() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: A deal is live");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "superadmin");
			newDealPage.createDeals(driver, step);
			landingPage.goToDealChecker(driver, step);
			dealCheckerPage.approveDeals(driver, step);
			landingPage.goToLiveDeal(driver, step);
			liveDealsPage.checkLiveDealTitles(driver, step);	
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
	@When("^I login as \"([^\"]*)\" and onboard account to live deal$")
	public void I_login_as_user_and_add_onboard_account_to_live_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and onboard account to live deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			liveDealsPage.editLiveDeals(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}				  
	}
	
	@When("^Deal draft is submitted to deal checker$")
	public void Deal_draft_is_submitted_to_deal_checker() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Deal draft is submitted to deal checker");
		try {
			step.log(Status.PASS, "Deal draft is submitted to deal checker for approval");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
//ACCOUNT_TC003_Verify_addition_of_existing_account_to_existing_deal
	@Given("^A deal is live with an account onboarded$")
	public void A_deal_is_live_with_an_account_onboraded() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: A deal is live with an account onboarded");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "superadmin");
			newDealPage.createDeals(driver, step);
			landingPage.goToDealChecker(driver, step);
			dealCheckerPage.approveDeals(driver, step);
			landingPage.goToLiveDeal(driver, step);
			liveDealsPage.checkLiveDealTitles(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
	@When("^I login as \"([^\"]*)\" and onboard same account to another live deal$")
	public void I_login_as_user_and_add_onboard_same_account_to_another_live_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and onboard same account to another live deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			liveDealsPage.editLiveDeals(driver, step);
			//landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}				  
	}
	
	@Then("^Response message should be displayed at accounts tab$")
	public void Response_message_should_be_displayed_at_party_checker() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Response message should be displayed at accounts tab");
		try {
			//newDealPage.validateResponseGenerated(step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
//ACCOUNT_TC004_Verify_addition_of_existing_account_from_closed_deal_to_new_deal
	@Given("^A live deal with an account onboarded is closed$")
	public void A_live_deal_with_an_account_onboarded_is_closed() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: A live deal with an account onboarded is closed");
		JSONArray deals = testData.getJSONArray("close_deals");
		Iterator<Object> dealIterator = deals.iterator();		
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				driver = webDriverFactory.goToSmartContracts();
				loginPage.performLogin(driver, step,  "superadmin");
				newDealPage.createDeal(driver, step,  deal);
				landingPage.goToDealChecker(driver, step);
				dealCheckerPage.approveDeal(driver, step,  deal);
				landingPage.goToLiveDeal(driver, step);
				liveDealsPage.checkLiveDealTitle(driver, step,  deal);
				liveDealsPage.closeLiveDeal(driver, step,  deal);
				landingPage.performLogout(driver, step);
			}
			catch (Exception e) {
				landingPage.performLogout(driver, step);
				webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
			}
		}	
	}
	
	@When("^I login as \"([^\"]*)\" and onboard same account to new deal$")
	public void I_login_as_user_and_add_onboard_same_account_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and onboard same account to new deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			newDealPage.createDeals(driver, step);
			//landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}				  
	}
	
//ACCOUNT_TC005_Verify_addition_of_existing_account_from_closed_deal_to_existing_deal
	@When("^I login as \"([^\"]*)\" and onboard same account to live deal$")
	public void I_login_as_user_and_add_onboard_same_account_to_live_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and onboard same account to live deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			liveDealsPage.editLiveDeals(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}				  
	}

//ACCOUNT_TC006_Verify_addition_of_existing_account_from_paused_deal_to_new_deal
	@Given("^A live deal with an account onboarded is paused$")
	public void A_live_deal_with_an_account_onboarded_is_paused() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: A live deal with an account onboarded is paused");
		JSONArray deals = testData.getJSONArray("pause_deals");
		Iterator<Object> dealIterator = deals.iterator();		
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				driver = webDriverFactory.goToSmartContracts();
				loginPage.performLogin(driver, step,  "superadmin");
				newDealPage.createDeal(driver, step,  deal);
				landingPage.goToDealChecker(driver, step);
				dealCheckerPage.approveDeal(driver, step,  deal);
				landingPage.goToLiveDeal(driver, step);
				liveDealsPage.checkLiveDealTitle(driver, step,  deal);
				liveDealsPage.pauseLiveDeal(driver, step,  deal);
				landingPage.performLogout(driver, step);
			}
			catch (Exception e) {
				landingPage.performLogout(driver, step);
				webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
			}
		}	
	}
	
//ACCOUNT_TC007_Verify_addition_of_existing_account_from_closed_deal_to_new_deal_acc_bal_chk_disabled
	@When("^I login as \"([^\"]*)\" and onboard same account with account balance check disabled to new deal$")
	public void I_login_as_user_and_add_onboard_same_account_with_account_balance_check_disabled_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and onboard same account with account balance check disabled to new deal");
		try {
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
	
//ACCOUNT_TC008_Verify_addition_of_existing_account_from_closed_deal_to_existing_deal_and_add_txn
	@When("^An adhoc transaction is created for live deal$")
	public void An_adhoc_transaction_is_create_for_live_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: An adhoc transaction is created for live deal");
		try {	
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "superadmin");				
			liveDealsPage.addTransactions(driver, step);
			landingPage.goToTransactionChecker(driver, step);
			transactionCheckerPage.approveAllTransactions(driver, step);			
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}				  
	}
	
	@Then("^Adhoc transaction should be available at execution report$")
	public void Adhoc_transaction_should_be_available_at_execution_report() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Adhoc transaction should be available at execution report");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "superadmin");
			landingPage.goToInternalExecutionReport(driver, step);
			executionReportPage.checkExecutionStatus(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
//ACCOUNT_TC009_Verify_addition_of_same_account_twice_to_new_deal
	@When("^Same account is again onboarded to new deal$")
	public void Same_account_is_again_onboarded_to_new_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Same account is again onboarded to new deal");
		try {			
			step.log(Status.INFO, "Same account was onboarded to new deal");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}				  
	}
	
//ACCOUNT_TC010_Verify_addition_of_same_account_twice_to_live_deal
	@When("^I login as \"([^\"]*)\" and onboard same account to same live deal$")
	public void I_login_as_user_and_add_onboard_same_account_to_same_live_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and onboard same account to same live deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			liveDealsPage.editLiveDeals(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}				  
	}
	
}
