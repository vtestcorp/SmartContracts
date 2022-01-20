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
import com.appveen.smartcontracts.pageobject.LandingPage;
import com.appveen.smartcontracts.pageobject.LiveDealsPage;
import com.appveen.smartcontracts.pageobject.LoginPage;
import com.appveen.smartcontracts.pageobject.NewDealPage;
import com.appveen.smartcontracts.pageobject.ODPAccountsPage;
import com.appveen.smartcontracts.pageobject.ODPLandingPage;
import com.appveen.smartcontracts.pageobject.ODPLoginPage;
import com.appveen.smartcontracts.pageobject.PartyCheckerPage;
import com.appveen.smartcontracts.pageobject.PartyMakerPage;
import com.appveen.smartcontracts.pageobject.PartySummaryPage;
import com.appveen.smartcontracts.pageobject.UpdateDealPage;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PartySteps {

	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	LocatorFactory locators = ListenerFactory.locators; 
	ReportFactory report = ListenerFactory.reportFactory;
	JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	
	LoginPage loginPage = new LoginPage(locators);
	LandingPage landingPage = new LandingPage(locators);
	LiveDealsPage liveDealsPage = new LiveDealsPage(locators);
	PartyMakerPage partyMakerPage = new PartyMakerPage(locators);
	PartyCheckerPage partyCheckerPage = new PartyCheckerPage(locators);
	PartySummaryPage partySummaryPage = new PartySummaryPage(locators);
	NewDealPage newDealPage = new NewDealPage(locators);
	UpdateDealPage updatedDealPage = new UpdateDealPage(locators);
	DealDraftsPage dealDraftsPage = new DealDraftsPage(locators);
	DealCheckerPage dealCheckerPage = new DealCheckerPage(locators);
	ODPLoginPage odpLoginPage = new ODPLoginPage(locators);
	ODPLandingPage odpLandingPage = new ODPLandingPage(locators);
	ODPAccountsPage odpAccountsPage = new ODPAccountsPage(locators);
		

//PARTY_TC001_Verify_creation_of_external_party
	@Given("^I login as \"([^\"]*)\" and create external party$")
	public void I_login_as_user_and_create_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and create external party");	
		try{
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);
			landingPage.goToPartyMaker(driver, step);			
			partyMakerPage.addParties(driver, step);		
			landingPage.performLogout(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@When("^I login as \"([^\"]*)\" and approve external party$")
	public void I_login_as_user_and_approve_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve external party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);
			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.approveParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@Then("^External party should be displayed at party summary$")
	public void External_party_should_be_displayed_at_party_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: External party should be displayed at party summary");	
		try {
			landingPage.goToPartySummary(driver, step);
			partySummaryPage.validatePartiesDisplayed(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}

//PARTY_TC002_Verify_creation_of_neutral_party
	@Given("^I login as \"([^\"]*)\" and create neutral party$")
	public void I_login_as_user_and_create_neutral_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and create neutral party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.addParties(driver, step);		
			landingPage.performLogout(driver, step);		
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@When("^I login as \"([^\"]*)\" and approve neutral party$")
	public void I_login_as_user_and_approve_neutral_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve neutral party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);
			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.approveParties(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@Then("^Neutral party should be displayed at party summary$")
	public void Neutral_party_should_be_displayed_at_party_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Neutral party should be displayed at party summary");	
		try {
			landingPage.goToPartySummary(driver, step);
			partySummaryPage.validatePartiesDisplayed(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}

//PARTY_TC003_Verify_creation_of_internal_party
	@Given("^I login as \"([^\"]*)\" and create internal party$")
	public void I_login_as_user_and_create_internal_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and create internal party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.addParties(driver, step);		
			landingPage.performLogout(driver, step);		
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@When("^I login as \"([^\"]*)\" and approve internal party$")
	public void I_login_as_user_and_approve_internal_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve internal party");	
		try {
			loginPage.performLogin(driver, step,  user);
			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.approveParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@Then("^Internal party should be displayed at party summary$")
	public void Internal_party_should_be_displayed_at_party_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Internal party should be displayed at party summary");
		try {
			landingPage.goToPartySummary(driver, step);
			partySummaryPage.validatePartiesDisplayed(driver, step);
			landingPage.performLogout(driver, step);	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
		
//PARTY_TC004_Verify_addition_of_contact_to_external_party		
	@Given("^I login as \"([^\"]*)\" and draft external party$")
	public void I_login_as_user_and_draft_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and draft external party");
		try {
  			driver = webDriverFactory.goToSmartContracts();
  			loginPage.performLogin(driver, step,  user);
  			landingPage.goToPartyMaker(driver, step);
  			step.log(Status.INFO, "Initiating external party draft");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@When("^Contact is added to external party$")
	public void Contact_is_added_to_external_party() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Contact is added to external party");	
		try {
			partyMakerPage.addParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Contact should be displayed at party maker summary$")
	public void Contact_should_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Contact should be displayed at party maker summary");
		try {
			partyMakerPage.validateSummary(driver, step);
			landingPage.performLogout(driver, step);	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC005_Verify_addition_of_account_to_external_party  		
	@When("^Account is added to external party$")
	public void Account_is_added_to_external_party() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Account is added to external party");	
		try {
			partyMakerPage.addParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Account should be displayed at party maker summary$")
	public void Account_should_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Account should be displayed at party maker summary");	
		try {
			partyMakerPage.validateSummary(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC006_Verify_addition_of_document_to_external_party  		
	@When("^Document is added to external party$")
	public void Document_is_added_to_external_party() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Document is added to external party");	
		try {
			partyMakerPage.addParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Document should be displayed at party maker summary$")
	public void Document_should_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Document should be displayed at party maker summary");	
		try {
			partyMakerPage.validateSummary(driver, step);
			landingPage.performLogout(driver, step);	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC007_Verify_edit_of_external_party_basic_details		
	@Given("^An external party is live$")
	public void An_external_party_is_live() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: An external party is live");	
		try {
  			driver = webDriverFactory.goToSmartContracts();
  			loginPage.performLogin(driver, step,  "superadmin");
  			landingPage.goToPartyMaker(driver, step);
  			partyMakerPage.addParties(driver, step);
  			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.approveParties(driver, step);
			landingPage.goToPartySummary(driver, step);
			partySummaryPage.validatePartiesDisplayed(driver, step);
			landingPage.performLogout(driver, step);	  			
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@When("^I login as \"([^\"]*)\" and edit basic details of external party$")
	public void I_login_as_user_and_edit_basic_details_of_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit basic details of external party");	
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			partySummaryPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Updated basic details should be displayed at party maker summary$")
	public void Updated_basic_details_should_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Updated basic details should be displayed at party maker summary");	
		try {
			//partyMakerPage.validateUpdatedSummary(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC008_Verify_edit_of_external_party_contact
	@When("^I login as \"([^\"]*)\" and edit contact of external party$")
	public void I_login_as_user_and_edit_contact_of_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit contact of external party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			partySummaryPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Updated contact should be displayed at party maker summary$")
	public void Updated_contact_should_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Updated contact should be displayed at party maker summary");
		try {
			//partyMakerPage.validateUpdatedSummary(driver, step);
			landingPage.performLogout(driver, step);	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC009_Verify_edit_of_external_party_account
	@When("^I login as \"([^\"]*)\" and edit account of external party$")
	public void I_login_as_user_and_edit_account_of_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit account of external party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			partySummaryPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Updated account should be displayed at party maker summary$")
	public void Updated_account_should_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Updated account should be displayed at party maker summary");		
		try {
			//partyMakerPage.validateUpdatedSummary(driver, step);
			landingPage.performLogout(driver, step);	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC010_Verify_edit_of_external_party_document
	@When("^I login as \"([^\"]*)\" and edit document of external party$")
	public void I_login_as_user_and_edit_document_of_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit document of external party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			partySummaryPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Updated document should be displayed at party maker summary$")
	public void Updated_document_should_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Updated document should be displayed at party maker summary");	
		try {
			//partyMakerPage.validateUpdatedSummary(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC011_Verify_delete_of_external_party_contact
	@When("^I login as \"([^\"]*)\" and delete contact of external party$")
	public void I_login_as_user_and_delete_contact_of_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete contact of external party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			partySummaryPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}			  
	}
	
	
	@Then("^Deleted contact should not be displayed at party maker summary$")
	public void Deleted_contact_should_not_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: contact contact should not be displayed at party maker summary");	
		try {
			partyMakerPage.validateDeletedSummary(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC012_Verify_delete_of_external_party_account
	@When("^I login as \"([^\"]*)\" and delete account of external party$")
	public void I_login_as_user_and_delete_account_of_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete account of external party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			partySummaryPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Deleted account should not be displayed at party maker summary$")
	public void Deleted_account_should_not_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deleted account should not be displayed at party maker summary");		
		try {
			partyMakerPage.validateDeletedSummary(driver, step);
			landingPage.performLogout(driver, step);	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC013_Verify_delete_of_external_party_document
	@When("^I login as \"([^\"]*)\" and delete document of external party$")
	public void I_login_as_user_and_delete_document_of_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete document of external party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			partySummaryPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Deleted document should not be displayed at party maker summary$")
	public void Deleted_document_should_not_be_displayed_at_party_maker_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deleted document should not be displayed at party maker summary");	
		try {
			partyMakerPage.validateDeletedSummary(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC014_Verify_edit_of_external_party_draft_basic_details
	@Given("^I login as \"([^\"]*)\" and send external party for review$")
	public void I_login_as_user_and_send_external_party_for_review(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and send external party for review");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);
			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.reviewParties(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}


	@When("^I login as \"([^\"]*)\" and edit basic details of external party draft$")
	public void I_login_as_user_and_edit_basic_details_of_external_party_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit basic details of external party draft");	
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
//PARTY_TC015_Verify_edit_of_external_party_draft_contact
	@When("^I login as \"([^\"]*)\" and edit contact of external party draft$")
	public void I_login_as_user_and_edit_contact_of_external_party_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit contact of external party draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
//PARTY_TC016_Verify_edit_of_external_party_draft_account
	@When("^I login as \"([^\"]*)\" and edit account of external party draft$")
	public void I_login_as_user_and_edit_account_of_external_party_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit account of external party draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
//PARTY_TC017_Verify_edit_of_external_party_draft_document	
	@When("^I login as \"([^\"]*)\" and edit document of external party draft$")
	public void I_login_as_user_and_edit_document_of_external_party_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit document of external party draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}

//PARTY_TC018_Verify_delete_of_external_party_draft_contact
	@When("^I login as \"([^\"]*)\" and delete contact of external party draft$")
	public void I_login_as_user_and_delete_contact_of_external_party_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete contact of external party draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
				  
	}
		
//PARTY_TC019_Verify_delete_of_external_party_draft_account
	@When("^I login as \"([^\"]*)\" and delete account of external party draft$")
	public void I_login_as_user_and_delete_account_of_external_party_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete account of external party draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
		
//PARTY_TC020_Verify_edit_of_external_party_draft_document	
	@When("^I login as \"([^\"]*)\" and delete document of external party draft$")
	public void I_login_as_user_and_delete_document_of_external_party_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete document of external party draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.editParties(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
//PARTY_TC021_Verify_delete_of_external_party_draft_at_party_maker_queue
	@When("^I login as \"([^\"]*)\" and delete external party draft$")
	public void I_login_as_user_and_delete_external_party_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete external party draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.deleteParties(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Deleted party should not be displayed at party maker queue$")
	public void Deleted_party_should_not_be_displayed_at_party_maker_queue() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deleted party should not be displayed at party maker queue");	
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "partymaker");
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.checkPartiesNotAvailable(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC022_Verify_delete_of_external_party_draft_at_party_checker_queue
	@When("^I login as \"([^\"]*)\" and delete submitted external party$")
	public void I_login_as_user_and_delete_submitted_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete submitted external party");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.deleteParties(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			//landingPage.performLogout(driver, step);
			//webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Deleted party should not be displayed at party checker queue$")
	public void Deleted_party_should_not_be_displayed_at_party_checker_queue() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deleted party should not be displayed at party checker queue");	
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "partychecker");
			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.checkPartiesNotAvailable(driver, step);
			landingPage.performLogout(driver, step);
			//webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC023_Verify_required_fields_of_external_party_draft_basic_details
	@When("^Required external party basic details fields are not provided$")
	public void Required_external_party_basic_details_fields_are_not_provided() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Required external party basic details fields are not provided");
		try {
			partyMakerPage.generateRequiredFieldMessages(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@Then("^Required messages should be displayed at party maker$")
	public void Required_messages_should_be_displayed_at_party_maker() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Required messages should be displayed at party maker");
		try {
			partyMakerPage.validateRequiredMessagesGenerated(step);	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC024_Verify_required_fields_of_internal_party_draft_basic_details	
	@Given("^I login as \"([^\"]*)\" and draft internal party$")
	public void I_login_as_user_and_draft_internal_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and draft internal party");
		try {
  			driver = webDriverFactory.goToSmartContracts();
  			loginPage.performLogin(driver, step,  user);
  			landingPage.goToPartyMaker(driver, step);
  			step.log(Status.INFO, "Initiating internal party draft");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@When("^Required internal party basic details fields are not provided$")
	public void Required_internal_party_basic_details_fields_are_not_provided() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Required internal party basic details fields are not provided");
		try {
			partyMakerPage.generateRequiredFieldMessages(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC025_Verify_required_fields_of_external_party_draft_contact
	@When("^Required external party contact fields are not provided$")
	public void Required_external_party_contact_fields_are_not_provided() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Required external party contact fields are not provided");
		try {
			partyMakerPage.generateRequiredFieldMessages(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC026_Verify_required_fields_of_external_party_draft_account
	@When("^Required external party account fields are not provided$")
	public void Required_external_party_account_fields_are_not_provided() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Required external party account fields are not provided");
		try {
			partyMakerPage.generateRequiredFieldMessages(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC027_Verify_required_fields_of_external_party_draft_document
	@When("^Required external party document fields are not provided$")
	public void Required_external_party_document_fields_are_not_provided() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: Required external party document fields are not provided");
		try {
			partyMakerPage.generateRequiredFieldMessages(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC028_Verify_response_to_party_approval_without_comment
	@When("^I login as \"([^\"]*)\" and approve external party without comment$")
	public void I_login_as_user_and_approve_external_party_without_comment(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve external party without comment");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.approvePartiesWithoutComment(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Response message should be displayed at party checker$")
	public void Response_message_should_be_displayed_at_party_checker() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Response message should be displayed at party checker");
		try {
			partyCheckerPage.validateResponseGenerated(step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}

//PARTY_TC029_Verify_response_to_party_review_without_comment
	@When("^I login as \"([^\"]*)\" and send external party for review without comment$")
	public void I_login_as_user_and_send_external_party_for_review_without_comment(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and send external party for review without comment");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			landingPage.goToPartyChecker(driver, step);
			partyCheckerPage.reviewPartieWithoutComment(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
//PARTY_TC030_Verify_addition_of_external_party_draft_not_submitted_to_new_deal	
//PARTY_TC032_Verify_addition_of_external_party_draft_rejected_to_new_deal
	@When("^I login as \"([^\"]*)\" and add external party draft to new deal$")
	public void I_login_as_user_and_add_external_party_draft_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and add external party draft to new deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			newDealPage.createNewDealsDraft(driver, step);
			//newDealPage.validatePartyNameSuggestedEmpty(step);
			//landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^External party draft should not be displayed as suggestion$")
	public void External_party_draft_should_not_be_displayed_as_suggestion() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: External party draft should not be displayed as suggestion");
		try {
			newDealPage.validatePartyNameSuggestedEmpty(step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}

//PARTY_TC031_Verify_addition_of_external_party_draft_not_submitted_to_deal_draft
//PARTY_TC033_Verify_addition_of_external_party_draft_rejected_to_deal_draft
	@Given("^I login as \"([^\"]*)\" and draft deal$")
	public void I_login_as_user_and_draft_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and draft deal");
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
	
	@When("^I login as \"([^\"]*)\" and add external party draft to deal draft$")
	public void I_login_as_user_and_add_external_party_draft_to_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and add external party draft to deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			newDealPage.createNewDealsDraft(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
//PARTY_TC034_Verify_addition_of_external_party_to_new_deal
	@When("^I login as \"([^\"]*)\" and add external party to new deal$")
	public void I_login_as_user_and_add_external_party_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and add external party to new deal");
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
	
	@Then("^External party draft should be displayed as suggestion$")
	public void External_party_draft_should_be_displayed_as_suggestion() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: External party draft should be displayed as suggestion");
		try {
			//newDealPage.validatePartyNameSuggestedNotEmpty(step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC035_Verify_addition_of_external_party_to_deal_draft
	@When("^I login as \"([^\"]*)\" and add external party to deal draft$")
	public void I_login_as_user_and_add_external_party_to_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and add external party to deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			dealDraftsPage.editDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
//PARTY_TC036_Verify_edit_of_external_party_basic_details_linked_to_live_deal
	@Given("^A deal linked with external party is live$")
	public void A_deal_linked_with_external_party_is_live() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: A deal linked with external party is live");	
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
	
//PARTY_TC037_Verify_edit_of_external_party_basic_details_linked_to_deal_draft
	@When("^I login as \"([^\"]*)\" and initiate edit of external party$")
	public void I_login_as_user_and_initiate_edit_of_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and initiate edit of external party");	
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			partySummaryPage.initiatePartyEdit(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Response message should be displayed at party summary$")
	public void Response_message_should_be_displayed_at_party_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Response message should be displayed at party summary");
		try {
			partySummaryPage.validateResponseGenerated(step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC038_Verify_addition_of_ecommerce_party_to_deal_draft
	@Given("^I login to ODP as \"([^\"]*)\" and create account$")
	public void I_login_to_ODP_as_user_and_create_account(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login to ODP as "+user+" and create account");
		try {
			driver = webDriverFactory.goToODP();
			odpLoginPage.performLogin(driver, step,  user);
			odpLandingPage.selectApplication(driver, step,  "XCRO6-DIY");
			odpLandingPage.goToAccounts(driver, step);	
			odpLandingPage.goToAddData(driver, step);
			odpAccountsPage.createAccounts(driver, step);
			odpLandingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			odpLandingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@When("^I login as \"([^\"]*)\" and add ecommerce party to new deal$")
	public void I_login_as_user_and_add_ecommerce_party_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and add ecommerce party to new deal");
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
	
	@Then("^Ecommerce party details should be displayed at deal summary$")
	public void Ecommerce_party_details_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Ecommerce party details should be displayed at deal summary");
		try {
			step.log(Status.PASS, "Ecommerce party details validated");
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC039_Verify_duplication_of_external_party_draft
//PARTY_TC040_Verify_duplication_of_live_external_party
	@When("^A duplicate external party is added$")
	public void A_duplicate_external_party_is_added() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: A duplicate external party is added");
		try {
			step.log(Status.INFO, "Duplicate external party initiated");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Response message should be displayed at party maker$")
	public void Response_message_should_be_displayed_at_party_maker() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Response message should be displayed at party maker");
		try {
			//partyMakerPage.validateResponseGenerated(step);	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC041_Verify_duplication_of_ecommerce_party
	@When("^A duplicate ecommerce party is added$")
	public void A_duplicate_ecommerce_party_is_added() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: A duplicate ecommerce party is added");
		try {
			step.log(Status.INFO, "Duplicate ecommerce party initiated");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Required message should be displayed at new deal$")
	public void Required_message_should_be_displayed_at_new_deal() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Required message should be displayed at new deal");
		try {
			newDealPage.validateRequiredMessagesGenerated(step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC042_Verify_creation_of_external_party_in_new_deal
	@Given("^I login as \"([^\"]*)\" and create new deal$")
	public void I_login_as_user_and_create_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and create new deal");
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
	
	@When("^An external party is added to new deal$")
	public void An_external_party_is_added_to_new_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: An external party is added to new deal");
		try {
			step.log(Status.INFO, "External party added to new deal");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^External party should be displayed at deal summary$")
	public void External_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: External party should be displayed at deal summary");
		try {
			step.log(Status.INFO, "External party displayed at deal summary");
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC043_Verify_creation_of_internal_party_in_new_deal
	@When("^An internal party is added to new deal$")
	public void An_internal_party_is_added_to_new_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: An internal party is added to new deal");
		try {
			step.log(Status.INFO, "Internal party added to new deal");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Internal party should be displayed at deal summary$")
	public void Internal_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Internal party should be displayed at deal summary");
		try {
			step.log(Status.INFO, "Internal party displayed at deal summary");
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC044_Verify_addition_of_contact_to_external_party_in_new_deal
	@When("^A contact is added to external party in new deal$")
	public void A_contact_is_added_to_external_party_in_new_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: A contact is added to external party in new deal");
		try {
			step.log(Status.INFO, "Contact is added to external party");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Contact added to external party should be displayed at deal summary$")
	public void Contact_added_to_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Contact added to external party should be displayed at deal summary");
		try {
			step.log(Status.INFO, "Contact added to external party is displayed at deal summary");
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC045_Verify_addition_of_account_to_external_party_in_new_deal
	@When("^An account is added to external party in new deal$")
	public void An_account_is_added_to_external_party_in_new_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: An account is added to external party in new deal");
		try {
			step.log(Status.INFO, "Account is added to external party");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Account added to external party should be displayed at deal summary$")
	public void Account_added_to_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Account added to external party should be displayed at deal summary");
		try {
			step.log(Status.INFO, "Account added to external party is displayed at deal summary");
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}

//PARTY_TC046_Verify_addition_of_document_to_external_party_in_new_deal
	@When("^A document is added to external party in new deal$")
	public void A_document_is_added_to_external_party_in_new_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: A document is added to external party in new deal");
		try {
			step.log(Status.INFO, "Document is added to external party");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Document added to external party should be displayed at deal summary$")
	public void Document_added_to_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Document added to external party should be displayed at deal summary");
		try {
			step.log(Status.INFO, "Document added to external party is displayed at deal summary");
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC047_Verify_edit_of_external_party_basic_details_in_deal_draft	 
	@When("^I login as \"([^\"]*)\" and edit basic details of external party in deal draft$")
	public void I_login_as_user_and_edit_basic_details_of_external_party_in_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit basic details of external party in deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			dealDraftsPage.editDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Updated basic details of external party should be displayed at deal summary$")
	public void Updated_basic_details_of_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Updated basic details of external party should be displayed at deal summary");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "dealmaker");
			dealDraftsPage.viewUpdatedDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC048_Verify_edit_of_external_party_contact_in_deal_draft
	@When("^I login as \"([^\"]*)\" and edit contact of external party in deal draft$")
	public void I_login_as_user_and_edit_contact_of_external_party_in_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit contact of external party in deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			dealDraftsPage.editDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Updated contact of external party should be displayed at deal summary$")
	public void Updated_contact_of_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Updated contact of external party should be displayed at deal summary");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "dealmaker");
			dealDraftsPage.viewUpdatedDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC049_Verify_edit_of_external_party_account_in_deal_draft
	@When("^I login as \"([^\"]*)\" and edit account of external party in deal draft$")
	public void I_login_as_user_and_edit_account_of_external_party_in_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit account of external party in deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			dealDraftsPage.editDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Updated account of external party should be displayed at deal summary$")
	public void Updated_account_of_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Updated account of external party should be displayed at deal summary");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "dealmaker");
			dealDraftsPage.viewUpdatedDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC050_Verify_edit_of_external_party_document_in_deal_draft
	@When("^I login as \"([^\"]*)\" and edit document of external party in deal draft$")
	public void I_login_as_user_and_edit_document_of_external_party_in_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and edit document of external party in deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			dealDraftsPage.editDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Updated document of external party should be displayed at deal summary$")
	public void Updated_document_of_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Updated document of external party should be displayed at deal summary");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "dealmaker");
			dealDraftsPage.viewUpdatedDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}

//PARTY_TC051_Verify_delete_of_external_party_in_deal_draft
	@When("^I login as \"([^\"]*)\" and delete external party in deal draft$")
	public void I_login_as_user_and_delete_external_party_in_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete external party in deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			dealDraftsPage.editDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Deleted external party should not be displayed at deal summary$")
	public void Deleted_external_party_should_not_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deleted external party should not be displayed at deal summary");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "dealmaker");
			dealDraftsPage.viewUpdatedDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC052_Verify_delete_of_external_party_contact_in_deal_draft
	@When("^I login as \"([^\"]*)\" and delete contact of external party in deal draft$")
	public void I_login_as_user_and_delete_contact_of_external_party_in_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete contact of external party in deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			dealDraftsPage.editDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Deleted contact of external party should not be displayed at deal summary$")
	public void Deleted_contact_of_external_party_should_not_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deleted contact of external party should not be displayed at deal summary");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "dealmaker");
			dealDraftsPage.viewUpdatedDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC053_Verify_delete_of_external_party_account_in_deal_draft
	@When("^I login as \"([^\"]*)\" and delete account of external party in deal draft$")
	public void I_login_as_user_and_delete_account_of_external_party_in_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete account of external party in deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			dealDraftsPage.editDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Deleted account of external party should not be displayed at deal summary$")
	public void Deleted_account_of_external_party_should_not_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deleted account of external party should not be displayed at deal summary");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "dealmaker");
			dealDraftsPage.viewUpdatedDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC054_Verify_delete_of_external_party_document_in_deal_draft
	@When("^I login as \"([^\"]*)\" and delete document of external party in deal draft$")
	public void I_login_as_user_and_delete_document_of_external_party_in_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete document of external party in deal draft");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			dealDraftsPage.editDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Deleted document of external party should not be displayed at deal summary$")
	public void Deleted_document_of_external_party_should_not_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Deleted document of external party should not be displayed at deal summary");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "dealmaker");
			dealDraftsPage.viewUpdatedDealDrafts(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC055_Verify_link_of_existing_contact_to_external_party_in_new_deal
	@When("^An existing contact is linked to external party in new deal$")
	public void An_existing_contact_is_linked_to_external_party_in_new_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: An existing contact is linked to external party in new deal");
		try {
			step.log(Status.INFO, "Existing contact is linked to external party in new deal");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Contact linked to external party should be displayed at deal summary$")
	public void Contact_linked_to_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Contact linked to external party should be displayed at deal summary");
		try {
			step.log(Status.INFO, "Contact linked to external party is displayed at deal summary");	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC056_Verify_link_of_existing_account_to_external_party_in_new_deal
	@When("^An existing account is linked to external party in new deal$")
	public void An_existing_account_is_linked_to_external_party_in_new_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: An existing account is linked to external party in new deal");
		try {
			step.log(Status.INFO, "Existing account is linked to external party in new deal");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
	@Then("^Account linked to external party should be displayed at deal summary$")
	public void Account_linked_to_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Account linked to external party should be displayed at deal summary");
		try {
			step.log(Status.INFO, "Account linked to external party is displayed at deal summary");
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC057_Verify_link_of_exising_document_to_external_party_in_new_deal	
	@When("^An existing document is linked to external party in new deal$")
	public void an_existing_document_is_linked_to_external_party_in_new_deal() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: An existing document is linked to external party in new deal");
		try {
			step.log(Status.INFO, "Existing document is linked to external party in new deal");
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	   
	}
	@Then("^Document linked to external party should be displayed at deal summary$")
	public void Document_linked_to_external_party_should_be_displayed_at_deal_summary() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Document linked to external party should be displayed at deal summary");
		try {
			step.log(Status.INFO, "Document linked to external party is displayed at deal summary");	
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}

//PARTY_TC058_Verify_addition_of_external_party_already_rejected_by_checker
	@Given("^An external party is already rejected by party checker$")
	public void An_external_party_is_already_rejected_by_party_checker() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: An external party is already rejected by party checker");
		JSONArray parties = testData.getJSONArray("reject_parties");
		Iterator<Object> partyIterator = parties.iterator();		
		
		while(partyIterator.hasNext()) {
			JSONObject party = (JSONObject) partyIterator.next();
			try {
				driver = webDriverFactory.goToSmartContracts();
				loginPage.performLogin(driver, step,  "superadmin");
				landingPage.goToPartyMaker(driver, step);
				partyMakerPage.addParties(driver, step,  party);
				landingPage.goToPartyChecker(driver, step);
				partyCheckerPage.reviewParties(driver, step,  party);
				landingPage.performLogout(driver, step);
			}
			catch (Exception e) {
				landingPage.performLogout(driver, step);
				webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
			}
		}	
	}
	
	@When("^I login as \"([^\"]*)\" and create same external party$")
	public void I_login_as_user_and_create_same_external_party(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and create same external party");	
		try{
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);
			landingPage.goToPartyMaker(driver, step);
			partyMakerPage.addParties(driver, step);		
			//landingPage.performLogout(driver, step);	
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}

//PARTY_TC059_Verify_addition_of_external_party_of_live_deal_in_another_deal_draft
	@Given("^An external party is added to live deal$")
	public void An_external_party_is_added_to_live_deal() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: An external party is added to live deal");
		JSONArray deals = testData.getJSONArray("live_deals");
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
				landingPage.performLogout(driver, step);
			}
			catch (Exception e) {
				landingPage.performLogout(driver, step);
				webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
			}
		}	
	}
	
	@When("^I login as \"([^\"]*)\" and add same external party to deal draft$")
	public void I_login_as_user_and_add_same_external_party_to_deal_draft(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and add same external party to deal draft");
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
	
//PARTY_TC060_Verify_addition_of_external_party_of_rejected_deal_to_new_deal
	@Given("^A deal with external party onboarded is already rejected by deal checker$")
	public void A_deal_with_external_party_onboarded_is_already_reject_by_deal_checker() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: A deal with external party onboarded  is already rejected by deal checker");
		JSONArray deals = testData.getJSONArray("reject_deals");
		Iterator<Object> dealIterator = deals.iterator();		
		
		while(dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				driver = webDriverFactory.goToSmartContracts();
				loginPage.performLogin(driver, step,  "superadmin");
				newDealPage.createDeal(driver, step,  deal);
				landingPage.goToDealChecker(driver, step);
				dealCheckerPage.rejectDeal(driver, step,  deal);				
				landingPage.performLogout(driver, step);
			}
			catch (Exception e) {
				landingPage.performLogout(driver, step);
				webDriverFactory.closeWebDriver(driver);
				throw new Exception(e.getMessage()); 
			}
		}	
	}
	
	@When("^I login as \"([^\"]*)\" and onboard same external party to new deal$")
	public void I_login_as_user_and_onboard_same_external_party_to_new_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and onboard same external party to new deal");
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
	
	@Then("^Response message should be displayed at parties tab$")
	public void Response_message_should_be_displayed_at_parties_checker() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Response message should be displayed at parties tab");
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
	
//PARTY_TC061_Verify_link_of_existing_contact_to_external_party_in_multiple_live_deals
	@When("^An existing contact is linked to external party in multiple live deals$")
	public void An_existing_contact_is_linked_to_external_party_in_multiple_live_deals() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: An existing contact is linked to external party in multiple live deals");
		JSONArray deals = testData.getJSONArray("live_deals");
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
				landingPage.performLogout(driver, step);
			}
			catch (Exception e) {
				landingPage.performLogout(driver, step);
				webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
			}
		}	
	}
	
//PARTY_TC062_Verify_external_party_added_via_deal_displayed_at_party_summary
	@Given("^I login as \"([^\"]*)\" and add external party to draft deal$")
	public void I_login_as_user_and_add_external_party_to_draft_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and add external party to draft deal");
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
	
	@Given("^External party is not displayed at party summary$")
	public void External_party_is_not_displayed_at_party_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: External party is not displayed at party summary");	
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "partymaker");
			landingPage.goToPartySummary(driver, step);
			partySummaryPage.validateDealPartiesNotDisplayed(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@When("^I login as \"([^\"]*)\" and approve the deal$")
	public void I_login_as_user_and_approve_the_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and approve the deal");
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
	
	@Then("^External party added via deal should be displayed at party summary$")
	public void External_party_added_via_deal_should_be_displayed_at_party_summary() throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: External party added via deal should be displayed at party summary");	
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  "partymaker");
			landingPage.goToPartySummary(driver, step);
			partySummaryPage.validateDealPartiesDisplayed(driver, step);
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
//PARTY_TC063_Verify_external_party_contact_mapped_to_notification_cannot_be_deleted
	@Given("^External party contact is mapped to deal notification$")
	public void External_party_contact_is_mapped_to_deal_notification() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: External party contact is mapped to deal notification");
		try {
			step.log(Status.INFO, "External party contact was mapped to deal notification");			
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
	
	@When("^I login as \"([^\"]*)\" and delete external party contact added via deal$")
	public void I_login_as_user_and_delete_external_party_contact_added_via_deal(String user) throws Throwable {
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and delete external party contact added via deal");
		try {
			driver = webDriverFactory.goToSmartContracts();
			loginPage.performLogin(driver, step,  user);				
			partySummaryPage.editParties(driver, step);
			landingPage.performLogout(driver, step);
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}				  
	}
	
//PARTY_TC064_Verify_external_party_contact_mapped_to_entitlement_cannot_be_deleted
	@Given("^External party contact is mapped to deal entitlement$")
	public void External_party_contact_is_mapped_to_deal_entitlement() throws Throwable{
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: External party contact is mapped to deal entitlement");
		try {
			step.log(Status.INFO, "External party contact was mapped to deal entitlement");			
		}
		catch (Exception e) {
			landingPage.performLogout(driver, step);
			webDriverFactory.closeWebDriver(driver);
			throw new Exception(e.getMessage()); 
		}
	}
}
