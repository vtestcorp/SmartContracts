package com.appveen.smartcontracts.stepdefinitions;


import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.pageobject.AddProductConfigPage;
import com.appveen.smartcontracts.pageobject.LandingPage;
import com.appveen.smartcontracts.pageobject.LoginPage;
import com.appveen.smartcontracts.pageobject.ManageConfigsPage;
import com.appveen.smartcontracts.pageobject.ProductConfigsPage;
import com.aventstack.extentreports.ExtentTest;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateProductSteps {
	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
  
  LocatorFactory locators = ListenerFactory.locators; 
  ReportFactory report = ListenerFactory.reportFactory;

  
  LoginPage loginPage = new LoginPage(locators);
  LandingPage landingPage = new LandingPage(locators);
  ManageConfigsPage manageConfigsPage = new ManageConfigsPage(locators);
  ProductConfigsPage productConfigsPage = new ProductConfigsPage(locators);
  AddProductConfigPage addProductConfigPage = new AddProductConfigPage(locators);

  @Given("^I login as \"([^\"]*)\" and create product$")
  public void I_login_as_user_and_create_product(String user) throws Throwable {  
	  ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: I login as "+user+" and create product");
	  try {		  
		  driver = webDriverFactory.goToSmartContracts();
		  loginPage.performLogin(driver, step, user);
		  landingPage.goToManageConfigs(driver, step);
		  manageConfigsPage.goToProductConfigs(driver, step);
	  }
	  catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
	  }
	  
  }

  @When("^I login as \"([^\"]*)\" and approve product$")
  public void I_login_as_user_and_approve_the_product(String user) throws Throwable {  
	  ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and validate product");
	  try {
		  
//		  webDriverFactory.goToSmartContracts();
//		  loginPage.performLogin(user);
//		  landingPage.goToManageConfigs();
//		  manageConfigsPage.goToProductConfigs();
//		  productConfigsPage.validateProduct();
//		  Assert.assertEquals(true, productConfigsPage.checkValidatedProductList());
//		  step.pass("Product was created");
//		  report.addStepPassScreenshot(driver, step);
//		  landingPage.performLogout();
	  }
	  catch (Exception e) {
			landingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
	  }

  }

  @Then("^Product should be created$")
  public void Product_should_be_created() throws Throwable {
//	  ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Product should be created");
  }


}
