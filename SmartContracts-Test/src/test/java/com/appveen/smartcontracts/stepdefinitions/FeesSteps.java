package com.appveen.smartcontracts.stepdefinitions;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.appveen.smartcontracts.pageobject.DealCheckerPage;
import com.appveen.smartcontracts.pageobject.IncomeBookingFeePage;
import com.appveen.smartcontracts.pageobject.IncomeBookingTransactionMakerPage;
import com.appveen.smartcontracts.pageobject.LandingPage;
import com.appveen.smartcontracts.pageobject.LiveDealsPage;
import com.appveen.smartcontracts.pageobject.LoginPage;
import com.appveen.smartcontracts.pageobject.NewDealPage;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FeesSteps {
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
	IncomeBookingTransactionMakerPage incomeBookingTransactionMakerPage = new IncomeBookingTransactionMakerPage(locators);
	IncomeBookingFeePage incomeBookingFeePage = new IncomeBookingFeePage(locators);
	
//FEES_TC001_Verify_addition_of_scheduled_fees_to_new_deal_with_upfront
	@Given("^I login as \"([^\"]*)\" and add recurring scheduled fee to new deal with upfront fee$")
	public void I_login_as_user_and_add_recurring_scheduled_fee_to_new_deal_with_upfront_fee(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add recurring scheduled fee to new deal with upfront fee");	
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
	
	@When("^I login as \"([^\"]*)\" and approve deal with scheduled fee$")
	public void I_login_as_user_and_approve_deal_with_scheduled_fee(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve deal with scheduled fee");
		try {
			driver = webDriverFactory.goToSmartContracts();
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
	
	@Then("^Deal with scheduled fee should be live$")
	public void Deal_with_scheduled_fee_should_be_live() throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deal with scheduled fee should be live");
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
	
//FEES_TC002_Verify_addition_of_recurring_scheduled_fees_to_new_deal_without_upfront
	@Given("^I login as \"([^\"]*)\" and add recurring scheduled fee to new deal without upfront fee$")
	public void I_login_as_user_and_add_recurring_scheduled_fee_to_new_deal_without_upfront_fee(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add recurring scheduled fee to new deal without upfront fee");	
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
	
//FEES_TC003_Verify_addition_of_eod_balance_scheduled_fees_to_new_deal_with_upfront
	@Given("^I login as \"([^\"]*)\" and add eod balance scheduled fee to new deal with upfront fee$")
	public void I_login_as_user_and_add_eod_balance_scheduled_fee_to_new_deal_with_upfront_fee(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add eod balance scheduled fee to new deal with upfront fee");	
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
	
//FEES_TC004_Verify_addition_of_eod_balance_scheduled_fees_to_new_deal_without_upfront
	@Given("^I login as \"([^\"]*)\" and add eod balance scheduled fee to new deal without upfront fee$")
	public void I_login_as_user_and_add_eod_balance_scheduled_fee_to_new_deal_without_upfront_fee(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add eod balance scheduled fee to new deal without upfront fee");	
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
	
//FEES_TC005_Verify_addition_of_transaction_count_scheduled_fees_to_new_deal_with_upfront	
	@Given("^I login as \"([^\"]*)\" and add transaction count scheduled fee to new deal with upfront fee$")
	public void I_login_as_user_and_add_transaction_count_scheduled_fee_to_new_deal_with_upfront_fee(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and transaction count scheduled fee to new deal with upfront fee");	
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
	
//FEES_TC006_Verify_addition_of_transaction_value_scheduled_fees_to_new_deal_with_upfront
	@Given("^I login as \"([^\"]*)\" and add transaction value scheduled fee to new deal with upfront fee$")
	public void I_login_as_user_and_add_transaction_value_scheduled_fee_to_new_deal_with_upfront_fee(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and transaction value scheduled fee to new deal with upfront fee");	
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
	
//FEES_TC007_Verify_addition_of_transaction_based_scheduled_fees_to_new_deal_without_upfront
	@Given("^I login as \"([^\"]*)\" and add transaction based scheduled fee to new deal without upfront fee$")
	public void I_login_as_user_and_add_transaction_based_scheduled_fee_to_new_deal_without_upfront_fee(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and transaction based scheduled fee to new deal without upfront fee");	
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
	
//FEES_TC008_Verify_addition_of_scheduled_fees_with_party_split_to_new_deal
	@Given("^I login as \"([^\"]*)\" and add scheduled fee with party split to new deal$")
	public void I_login_as_user_and_add_scheduled_fee_with_party_split_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add scheduled fee with party split to new deal");	
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
	
//FEES_TC009_Verify_addition_of_scheduled_fees_without_fee_credit_account_to_new_deal
	@Given("^I login as \"([^\"]*)\" and add scheduled fee without fee credit account to new deal$")
	public void I_login_as_user_and_add_scheduled_fee_without_fee_credit_account_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add scheduled fee without fee credit account to new deal");	
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
	
//FEES_TC010_Verify_addition_of_scheduled_fees_without_tax_credit_account_to_new_deal
	@Given("^I login as \"([^\"]*)\" and add scheduled fee without tax credit account to new deal$")
	public void I_login_as_user_and_add_scheduled_fee_without_tax_credit_account_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add scheduled fee without tax credit account to new deal");	
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
	
//FEES_TC011_Verify_addition_of_scheduled_fees_without_fee_due_reminder_to_new_deal
	@Given("^I login as \"([^\"]*)\" and add scheduled fee without fee due reminder to new deal$")
	public void I_login_as_user_and_add_scheduled_fee_without_fee_due_reminder_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add scheduled fee without fee due reminder to new deal");	
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
	
//FEES_TC012_Verify_addition_of_scheduled_fees_without_fee_overdue_reminder_to_new_deal
	@Given("^I login as \"([^\"]*)\" and add scheduled fee without fee overdue reminder to new deal$")
	public void I_login_as_user_and_add_scheduled_fee_without_fee_overdue_reminder_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add scheduled fee without fee overdue reminder to new deal");	
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
	
//FEES_TC013_Verify_addition_of_scheduled_fees_without_invoice_contacts_to_new_deal
	@Given("^I login as \"([^\"]*)\" and add scheduled fee to new deal$")
	public void I_login_as_user_and_add_scheduled_fee_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add scheduled fee to new deal");	
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
	
	@When("^Invoice contacts are not added to scheduled fee$")
	public void Invoice_contacts_are_not_added_to_scheduled_fee() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Invoice contacts are not added to scheduled fee");
		try {
			step.log(Status.PASS, "Invoice contacts were not added to scheduled fee");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
	
	@Then("^Response message should be displayed at scheduled fee tab$")
	public void Response_message_should_be_displayed_at_scheduled_fee_tab() throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Response message should be displayed at scheduled fee tab");
		try {
			newDealPage.validateResponseGenerated(step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//FEES_TC014_Verify_addition_of_scheduled_fees_without_fee_reminder_contacts_to_new_deal
	@When("^Fee reminder contacts are not added to scheduled fee$")
	public void Fee_reminder_contacts_are_not_added_to_scheduled_fee() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Fee reminder contacts are not added to scheduled fee");
		try {
			step.log(Status.PASS, "Fee reminder contacts were not added to scheduled fee");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage());
		}
	}
}
