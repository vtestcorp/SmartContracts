package com.appveen.smartcontracts.pageobject;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ODPLoginPage {
	 
	private JSONObject testData = ListenerFactory.dataFactory.getTestData("credentials");
	private JSONObject credentials = null;
	private JSONObject odpLoginLocator = null;	
	
	public ODPLoginPage(LocatorFactory locators) {
		odpLoginLocator = locators.getLocators("ODPLoginPage");		
	}
	
	public void performLogin(WebDriver driver, ExtentTest step,  String role) throws Exception {
		try {	
			
			credentials = testData.getJSONObject("odp");
			
			driver.findElement(By.id(odpLoginLocator.getString("input-Username"))).sendKeys(credentials.getJSONObject(role).getString("username"));
			driver.findElement(By.xpath(odpLoginLocator.getString("button-UsernameNext"))).click();
			driver.findElement(By.id(odpLoginLocator.getString("input-Password"))).sendKeys(credentials.getJSONObject(role).getString("password"));
			driver.findElement(By.xpath(odpLoginLocator.getString("button-Login"))).click();
			
			step.log(Status.INFO, "Logged in to ODP as "+ credentials.getJSONObject(role).getString("username"));
			
		}
		catch(Exception e) {
			step.log(Status.FAIL, "Error while performing login");
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while performing login");
		}
	}
}
