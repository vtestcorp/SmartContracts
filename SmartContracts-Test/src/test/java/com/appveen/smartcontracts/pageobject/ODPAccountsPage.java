package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ODPAccountsPage {
	 
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject odpAccountsLocator = null;
	private ReportFactory report = ListenerFactory.reportFactory;
	private JSONArray accounts = null;
	
	public ODPAccountsPage(LocatorFactory locators) {
		odpAccountsLocator = locators.getLocators("ODPAccountsPage");							
	}
	
	public void createAccounts(WebDriver driver, ExtentTest step) throws Exception {
		try {
			accounts = testData.getJSONArray("accounts");
			Iterator<Object> accountIterator = accounts.iterator();
			
			while(accountIterator.hasNext()) {
				JSONObject account = (JSONObject) accountIterator.next();
				try {
					utility.waitForElementToBeDisplayed(driver, By.id(odpAccountsLocator.getString("input-AccountNumber")), 120);
					driver.findElement(By.id(odpAccountsLocator.getString("input-AccountNumber"))).sendKeys(account.getString("account_number"));
					
					List<WebElement> dateElements = driver.findElements(By.id(odpAccountsLocator.getString("input-SelectDate")));
					dateElements.get(0).click();
					//Select dateYear = new Select (driver.findElement(By.xpath(odpNewAccountsLocators.getString("select-DateYear"))));
					//dateYear.selectByVisibleText("2020");
					driver.findElement(By.id(odpAccountsLocator.getString("button-DateDone"))).click();
					 
					driver.findElement(By.id(odpAccountsLocator.getString("input-AvailableBalanceAmount"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-AvailableBalanceAmount"))).sendKeys(account.getString("available_balance_amount"));
					driver.findElement(By.id(odpAccountsLocator.getString("input-AvailableBalanceCurrency"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-AvailableBalanceCurrency"))).sendKeys(account.getString("available_balance_currency"));
					dateElements.get(1).click();
					//dateYear.selectByVisibleText("2020");
					driver.findElement(By.id(odpAccountsLocator.getString("button-DateDone"))).click();
					
					driver.findElement(By.id(odpAccountsLocator.getString("input-EODBalanceAmount"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-EODBalanceAmount"))).sendKeys(account.getString("ledger_balance_amount"));
					driver.findElement(By.id(odpAccountsLocator.getString("input-EODBalanceCurrency"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-EODBalanceCurrency"))).sendKeys(account.getString("ledger_balance_currency"));
					
					driver.findElement(By.id(odpAccountsLocator.getString("input-LedgerBalanceAmount"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-LedgerBalanceAmount"))).sendKeys(account.getString("ledger_balance_amount"));
					driver.findElement(By.id(odpAccountsLocator.getString("input-LedgerBalanceCurrency"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-LedgerBalanceCurrency"))).sendKeys(account.getString("ledger_balance_currency"));
					dateElements.get(2).click();
					//dateYear.selectByVisibleText("2020");
					driver.findElement(By.id(odpAccountsLocator.getString("button-DateDone"))).click();
					
//					driver.findElement(By.id(odpAccountsLocator.getString("input-LimitBalanceAmount"))).sendKeys(account.getString("limit_balance_amount"));
//					driver.findElement(By.id(odpAccountsLocator.getString("input-LimitBalanceCurrency"))).clear();
//					driver.findElement(By.id(odpAccountsLocator.getString("input-LimitBalanceCurrency"))).sendKeys(account.getString("limit_balance_currency"));
//					
//					driver.findElement(By.id(odpAccountsLocator.getString("input-OverdraftBalanceAmount"))).sendKeys(account.getString("overdraft_balance_amount"));
//					driver.findElement(By.id(odpAccountsLocator.getString("input-OverdraftBalanceCurrency"))).clear();
//					driver.findElement(By.id(odpAccountsLocator.getString("input-OverdraftBalanceCurrency"))).sendKeys(account.getString("overdraft_balance_currency"));
					
					driver.findElement(By.id(odpAccountsLocator.getString("input-Currency"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-Currency"))).sendKeys(account.getString("currency"));
					driver.findElement(By.id(odpAccountsLocator.getString("input-Country"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-Country"))).sendKeys(account.getString("country"));
					driver.findElement(By.id(odpAccountsLocator.getString("input-UCIC"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-UCIC"))).sendKeys(account.getString("ucic"));
					
					driver.findElement(By.id(odpAccountsLocator.getString("input-MothersMaidenName"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-MothersMaidenName"))).sendKeys(account.getString("mothers_maiden_name"));
					driver.findElement(By.id(odpAccountsLocator.getString("input-Name"))).clear();
					driver.findElement(By.id(odpAccountsLocator.getString("input-Name"))).sendKeys(account.getString("name"));
//					driver.findElement(By.id(odpAccountsLocator.getString("input-Status"))).clear();
//					driver.findElement(By.id(odpAccountsLocator.getString("input-Status"))).sendKeys(account.getString("status"));
					Select accountStatus = new Select(driver.findElement(By.id(odpAccountsLocator.getString("input-Status"))));
					accountStatus.selectByVisibleText(account.getString("status"));
					
					Select accountIdentifierKey = new Select(driver.findElement(By.id(odpAccountsLocator.getString("select-AccountIdentifierKey"))));
					accountIdentifierKey.selectByVisibleText(account.getString("account_identifier_key"));
					
					if(ListenerFactory.environment.equals("pt")) {
						accountStatus = new Select(driver.findElement(By.id(odpAccountsLocator.getString("input-Status"))));
						accountStatus.selectByVisibleText(account.getString("status"));						
						
						driver.findElement(By.id(odpAccountsLocator.getString("input-ShortName"))).sendKeys(account.getString("short_name"));
						
						Select OBOFlag = new Select(driver.findElement(By.id(odpAccountsLocator.getString("select-OBOFlag"))));
						OBOFlag.selectByVisibleText(account.getString("obo_flag"));
					}
					
					driver.findElement(By.id(odpAccountsLocator.getString("button-SaveAndCreateAnother"))).click();
					step.log(Status.INFO, "Account# "+account.getString("account_number")+" created.");
				
				}
				catch (Exception e) {
					step.log(Status.FAIL, "Error while creating Account: "+account.getString("account_number"));
					report.addStepFailScreenshot(driver, step);
					step.log(Status.FAIL, e.getMessage());
					throw new Exception("Error while creating Account: "+account.getString("account_number"));
				}
			}
			driver.findElement(By.id(odpAccountsLocator.getString("button-Cancel"))).click();
			driver.findElement(By.xpath(odpAccountsLocator.getString("button-UnsavedChangesYes"))).click();
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while creating Accounts");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while creating Accounts");
		}
	}

}
