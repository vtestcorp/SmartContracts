package com.appveen.smartcontracts.pageobject;


import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class LoginPage {
	
	private ReportFactory report = ListenerFactory.reportFactory;	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData("credentials");
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject credentials = null;
	private LandingPage landingPage = null;	
	private JSONObject loginLocators = null;
	
	public LoginPage(LocatorFactory locators) {
		loginLocators = locators.getLocators("LoginPage");
		landingPage = new LandingPage(locators);
	}
	
	public void performLogin(WebDriver driver, ExtentTest step,  String role) throws Exception {
		try {	
			
			credentials = testData.getJSONObject(ListenerFactory.environment);
			
			utility.waitForElementToBeDisplayed(driver, By.id(loginLocators.getString("input-Username")), 120);
			driver.findElement(By.id(loginLocators.getString("input-Username"))).sendKeys(credentials.getJSONObject(role).getString("username"));
			driver.findElement(By.id(loginLocators.getString("input-Password"))).sendKeys(credentials.getJSONObject(role).getString("password"));
			driver.findElement(By.id(loginLocators.getString("button-SignIn"))).click();
			
			if(landingPage.verifyLogin(driver, step)) {
				step.log(Status.INFO, "Logged in as "+ credentials.getJSONObject(role).getString("username"));
			}
			else {
				driver.navigate().refresh();
				Thread.sleep(2000);
				utility.waitForElementToBeDisplayed(driver, By.id(loginLocators.getString("input-Username")), 120);
				driver.findElement(By.id(loginLocators.getString("input-Username"))).sendKeys(credentials.getJSONObject(role).getString("username"));
				driver.findElement(By.id(loginLocators.getString("input-Password"))).sendKeys(credentials.getJSONObject(role).getString("password"));
				driver.findElement(By.id(loginLocators.getString("button-SignIn"))).click();
				Thread.sleep(2000);
			}
		}
		catch(Exception e) {
			step.log(Status.FAIL, "Error while performing login");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while performing login");
		}
	}

}
