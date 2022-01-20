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
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class LandingPage {
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private WebDriverFactory webDriverFactory = new WebDriverFactory();
	private UtilityFactory utility = new UtilityFactory();
	private JSONArray deals = null;

	private JSONObject landingLocators = null;

	public LandingPage(LocatorFactory locators) {
		landingLocators = locators.getLocators("LandingPage");
	}

	public boolean verifyLogin(WebDriver driver, ExtentTest step) {
		if (driver.findElements(By.name(landingLocators.getString("sidemenu-Search"))).size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void goToLiveDeal(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("deals");
			WebElement deals = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Deals")));
			deals.click();
			deals.click();
			Thread.sleep(1000);
			driver.findElement(By.id(landingLocators.getString("sidemenu-DealsLiveDeal"))).click();
			step.log(Status.INFO, "Moved to Live Deal");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Live Deal");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Live Deal");
		}
	}

	public void goToNewDeal(WebDriver driver, ExtentTest step) throws Exception {
		try {
			Thread.sleep(2000);
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("deals");
			WebElement deals = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Deals")));
			deals.click();
			deals.click();
			WebElement newDeal = driver.findElement(By.id(landingLocators.getString("sidemenu-DealsNewDeal")));
			newDeal.click();
			step.log(Status.INFO, "Moved to New Deal");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to New Deal");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to New Deal");
		}
	}

	public void goToDealDrafts(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("deals");
			WebElement deals = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Deals")));
			deals.click();
			deals.click();
			WebElement draftDeal = driver.findElement(By.id(landingLocators.getString("sidemenu-DealsDraftDeal")));
			draftDeal.click();
			step.log(Status.INFO, "Moved to Deal Drafts");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Deal Drafts");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Deal Drafts");
		}
	}

	public void goToDealChecker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("deals");
			WebElement deals = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Deals")));
			deals.click();
			deals.click();
			Thread.sleep(1000);
			List<WebElement> dealElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-DealsDealChecker")));
			for (WebElement element : dealElements) {
				if (element.getText().toLowerCase().trim().equals("deal checker")) {
					element.click();
					step.log(Status.INFO, "Moved to Deal Checker");
					return;
				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Deal Checker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Deal Checker");
		}
	}

	public void goToTransactionMaker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("transactions");
			WebElement transactions = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Transactions")));
			transactions.click();
			transactions.click();
			Thread.sleep(1000);
			List<WebElement> transactionElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-TransactionsMaker")));
			for (WebElement element : transactionElements) {
				if (element.getText().toLowerCase().trim().equals("transaction maker")) {
					element.click();
					step.log(Status.INFO, "Moved to Transaction Maker");
					return;
				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Transaction Maker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Transaction Maker");
		}
	}

	public void goToTransactionChecker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("transactions");
			WebElement transactions = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Transactions")));
			transactions.click();
			transactions.click();
			Thread.sleep(1000);
			List<WebElement> transactionElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-TransactionsMaker")));
			for (WebElement element : transactionElements) {
				if (element.getText().toLowerCase().trim().equals("transaction checker")) {
					element.click();
					step.log(Status.INFO, "Moved to Transaction Checker");
					return;
				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Transaction Checker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Transaction Checker");
		}
	}

	public void goToPartySummary(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("parties");
			WebElement parties = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Parties")));
			parties.click();
			parties.click();
			Thread.sleep(1000);
			driver.findElement(By.id(landingLocators.getString("sidemenu-PartySummary"))).click();
			;

			step.log(Status.INFO, "Moved to Party Summary");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Party Summary");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Party Summary");
		}
	}

	public void goToPartyMaker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			Thread.sleep(2000);
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("parties");
			WebElement parties = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Parties")));
			parties.click();
			parties.click();
			Thread.sleep(1000);
			List<WebElement> partyElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-PartyMaker")));
			for (WebElement element : partyElements) {
				if (element.getText().toLowerCase().trim().equals("party maker")) {
					element.click();
					step.log(Status.INFO, "Moved to Party Maker");
					return;
				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Party Maker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Party Maker");
		}
	}
	public void goToNewPartyMaker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			Thread.sleep(2000);
			WebElement parties = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Parties")));
			parties.click();
			Thread.sleep(1000);
			List<WebElement> partyElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-PartyMaker")));
			for (WebElement element : partyElements) {
				if (element.getText().toLowerCase().trim().equals("party maker")) {
					element.click();
					step.log(Status.INFO, "Moved to Party Maker");
					return;
				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Party Maker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Party Maker");
		}
	}

	public void goToPartyChecker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("parties");
			WebElement parties = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Parties")));
			parties.click();
			parties.click();
			Thread.sleep(1000);
			List<WebElement> partyElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-PartyMaker")));
			for (WebElement element : partyElements) {
				if (element.getText().toLowerCase().trim().equals("party checker")) {
					element.click();
					step.log(Status.INFO, "Moved to Party Checker");
					return;
				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Party Checker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Party Checker");
		}
	}

	public void goToInternalExecutionReport(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("reports");
			WebElement reports = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Reports")));
			reports.click();
			// reports.click();
			Thread.sleep(1000);
			List<WebElement> reportElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-ReportsInternal")));
			for (WebElement element : reportElements) {
				if (element.getText().toLowerCase().trim().equals("internal")) {
					element.click();
				}
			}
			driver.findElement(By.id(landingLocators.getString("select-ReportTitle"))).findElement(By.tagName("input"))
					.click();
			utility.selectFromDivMenu(driver, step, driver
					.findElement(By.id(landingLocators.getString("select-ReportTitle"))).findElement(By.tagName("ul")),
					"Execution Report");
			step.log(Status.INFO, "Moved to Internal Execution Report");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Internal Execution Report");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Internal Execution Report");
		}
	}

	public void goToWorkbenchRepair(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("workbench");
			WebElement workbench = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Workbench")));
			workbench.click();
			workbench.click();
			Thread.sleep(1000);
			driver.findElement(By.id(landingLocators.getString("sidemenu-WorkbenchRepair")));
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Workbench Repair");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Workbench Repair");
		}
	}

	public void goToDealLifeCycleMaker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("deal lifecycle");
			WebElement dealLifeCycle = driver
					.findElement(By.xpath(landingLocators.getString("sidemenu-DealLifeCycle")));
			dealLifeCycle.click();
			// dealLifeCycle.click();
			Thread.sleep(1000);
			List<WebElement> dealLifeCycleElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-DealLifeCycleMaker")));
			for (WebElement element : dealLifeCycleElements) {
				if (element.getText().toLowerCase().trim().equals("lifecycle maker")) {
					element.click();
				}
			}
			step.log(Status.INFO, "Moved to Deal Lifecycle Maker");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Deal Lifecycle Maker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Deal Lifecycle Maker");
		}
	}

	public void goToDealLifeCycleChecker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("deal lifecycle");
			WebElement dealLifeCycle = driver
					.findElement(By.xpath(landingLocators.getString("sidemenu-DealLifeCycle")));
			dealLifeCycle.click();
			// dealLifeCycle.click();
			Thread.sleep(1000);
			List<WebElement> dealLifeCycleElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-DealLifeCycleMaker")));
			for (WebElement element : dealLifeCycleElements) {
				if (element.getText().toLowerCase().trim().equals("lifecycle checker")) {
					element.click();
				}
			}
			step.log(Status.INFO, "Moved to Deal Lifecycle Checker");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Deal Lifecycle Checker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Deal Lifecycle Checker");
		}
	}

	public void goToIncomeBookingFee(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("income booking");
			WebElement incomeBooking = driver
					.findElement(By.xpath(landingLocators.getString("sidemenu-IncomeBooking")));
			incomeBooking.click();

			Thread.sleep(1000);
			driver.findElement(By.id(landingLocators.getString("sidemenu-IncomeBookingFee"))).click();
			step.log(Status.INFO, "Moved to Income Booking Fee");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Income Booking Fee");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Income Booking Fee");
		}
	}

	public void goToIncomeBookingTransactionMaker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("income booking");
			WebElement incomeBooking = driver
					.findElement(By.xpath(landingLocators.getString("sidemenu-IncomeBooking")));
			incomeBooking.click();

			Thread.sleep(1000);
			List<WebElement> incomeBookingElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-IncomeBookingMakerChecker")));
			for (WebElement element : incomeBookingElements) {
				if (element.getText().toLowerCase().trim().equals("transaction maker")) {
					element.click();
				}
			}
			step.log(Status.INFO, "Moved to Income Booking Transaction Maker");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Income Booking Transaction Maker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Income Booking Transaction Maker");
		}
	}

	public void goToIncomeBookingTransactionChecker(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("income booking");
			WebElement incomeBooking = driver
					.findElement(By.xpath(landingLocators.getString("sidemenu-IncomeBooking")));
			incomeBooking.click();

			Thread.sleep(1000);
			List<WebElement> incomeBookingElements = driver
					.findElements(By.id(landingLocators.getString("sidemenu-IncomeBookingMakerChecker")));
			for (WebElement element : incomeBookingElements) {
				if (element.getText().toLowerCase().trim().equals("transaction checker")) {
					element.click();
				}
			}
			step.log(Status.INFO, "Moved to Income Booking Transaction Checker");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Income Booking Transaction Checker");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Income Booking Transaction Checker");
		}
	}

	public void goToManageConfigs(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("configuration");

			WebElement configuration = driver
					.findElement(By.xpath(landingLocators.getString("sidemenu-Configuration")));
			configuration.click();
			configuration.click();
			WebElement manageConfigs = driver
					.findElement(By.id(landingLocators.getString("sidemenu-ConfigurationManageConfigs")));
			manageConfigs.click();
			step.log(Status.INFO, "Moved to Managed Configs");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Managed Configs");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Managed Configs");
		}
	}

	public void reviewNotification(WebDriver driver, ExtentTest step, String notificationText) throws Exception {
		try {
			driver.findElement(By.xpath(landingLocators.getString("button-NotificationWithoutRedDot"))).click();
			utility.setImplicitWaits(driver, 200);

			while (true) {
				List<WebElement> notificationList = driver
						.findElements(By.xpath(landingLocators.getString("list-NotificationList")));
				for (WebElement mainElement : notificationList) {
					List<WebElement> notificationListElements = mainElement.findElements(By.tagName("li"));
					for (WebElement subElement : notificationListElements) {
						if (subElement.getText().contains(notificationText)) {
							utility.focusElement(driver, subElement);
							utility.turnOnElementHighlight(driver, subElement);
							step.log(Status.INFO,
									"Notification containing text: " + notificationText + " is available for review");
							report.addStepInfoScreenshot(driver, step);
							subElement.findElement(By.xpath(landingLocators.getString("link-NotificationReview")))
									.click();
							utility.turnOnImplicitWaits(driver);
							return;
						}
					}
				}
			}

		} catch (Exception e) {
			step.log(Status.FAIL, "Error while reviewing notification contaning text: " + notificationText);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while reviewing notification contaning text: " + notificationText);
		}
	}

	public void closeNotification(WebDriver driver, ExtentTest step, String notificationText) throws Exception {
		try {
			driver.findElement(By.xpath(landingLocators.getString("button-NotificationWithoutRedDot"))).click();
			utility.setImplicitWaits(driver, 200);

			while (true) {
				List<WebElement> notificationList = driver
						.findElements(By.xpath(landingLocators.getString("list-NotificationList")));
				for (WebElement mainElement : notificationList) {
					List<WebElement> notificationListElements = mainElement.findElements(By.tagName("li"));
					for (WebElement subElement : notificationListElements) {
						if (subElement.getText().contains(notificationText)) {
							utility.turnOnElementHighlight(driver, subElement);
							step.log(Status.INFO,
									"Notification containing text: " + notificationText + " is available");
							report.addStepInfoScreenshot(driver, step);
							subElement.findElement(By.xpath(landingLocators.getString("button-NotificationClose")))
									.click();
							utility.turnOnImplicitWaits(driver);
							return;
						}
					}
				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while closing notification contaning text: " + notificationText);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while closing notification contaning text: " + notificationText);
		}
	}

	public void performLogout(WebDriver driver, ExtentTest step) throws InterruptedException {
		try {
			driver.findElement(By.id(landingLocators.getString("button-Logout"))).click();
			driver.manage().deleteAllCookies();
			Thread.sleep(2000);
			step.log(Status.INFO, "Logged out from SmartContracts");
			driver.close();
		} catch (Exception e) {
			driver.navigate().refresh();
			Thread.sleep(2000);
			if (!utility.isElementHiddenNow(driver, By.id(landingLocators.getString("button-Logout")))) {
				driver.findElement(By.id(landingLocators.getString("button-Logout"))).click();
				Thread.sleep(2000);
				driver.manage().deleteAllCookies();
				step.log(Status.INFO, "Logged out from SmartContracts on second attempt");
			} else {
				webDriverFactory.closeWebDriver(driver);
			}
		}

	}

	public void goToLiveDealAndPuasedeal(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("deals");
			WebElement deals = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Deals")));
			deals.click();
			deals.click();
			Thread.sleep(1000);
			driver.findElement(By.id(landingLocators.getString("sidemenu-DealsLiveDeal"))).click();
			step.log(Status.INFO, "Moved to Live Deal");
			driver.findElement(By.id(landingLocators.getString("button-searchBox"))).clear();
			driver.findElement(By.xpath(landingLocators.getString("button-IcMenu"))).click();
			driver.findElement(By.xpath(landingLocators.getString("button-pause"))).click();
			driver.findElement(By.xpath(landingLocators.getString("button-Yes"))).click();
			driver.findElement(By.xpath(landingLocators.getString("button-ok"))).click();
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Live Deal");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Live Deal");
		}
	}

	public void goToNewLiveDeal(WebDriver driver, ExtentTest step) throws Exception {
		try {
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).clear();
			driver.findElement(By.name(landingLocators.getString("sidemenu-Search"))).sendKeys("deals");
			WebElement deals = driver.findElement(By.xpath(landingLocators.getString("sidemenu-Deals")));
			deals.click();
			Thread.sleep(1000);
			driver.findElement(By.id(landingLocators.getString("sidemenu-DealsLiveDeal"))).click();
			step.log(Status.INFO, "Moved to Live Deal");
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while moving to Live Deal");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while moving to Live Deal");
		}
	}

	public void goToEcommerceTxnMaker(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				driver.findElement(By.xpath(landingLocators.getString("sidemenu-E-comm"))).click();
				driver.findElement(By.xpath(landingLocators.getString("Link-TxnMaker"))).click();
				driver.findElement(By.xpath(landingLocators.getString("Button-AddNew"))).click();
				driver.findElement(By.xpath(landingLocators.getString("Input-DealId"))).sendKeys("REF1638166706482");
				driver.findElement(By.xpath(landingLocators.getString("Select-dealId"))).click();
				utility.waitForProgressBarToLoad(driver);
				driver.findElement(By.xpath(landingLocators.getString("Input-particepantID"))).click();
				driver.findElement(By.xpath(landingLocators.getString("Select-particepantID"))).click();
				driver.findElement(By.xpath(landingLocators.getString("button-submit"))).click();
				utility.waitForProgressBarToLoad(driver);
				Select purpose = new Select(driver.findElement(By.xpath(landingLocators.getString("click-purpose"))));
				purpose.selectByVisibleText("test");
				driver.findElement(By.xpath(landingLocators.getString("Participant-Account"))).click();
				driver.findElement(By.xpath(landingLocators.getString("select-Participant-Account"))).click();
				Select balanceConsideration = new Select(
						driver.findElement(By.xpath(landingLocators.getString("Balance-Consideration"))));
				balanceConsideration.selectByVisibleText("EOD Balance");
				//driver.findElement(By.xpath(landingLocators.getString("button-ok"))).click();
				Thread.sleep(1000);
				List<WebElement> datePickerList = driver
						.findElements(By.id(landingLocators.getString("input-DatePicker")));
				datePickerList.get(0).clear();
				datePickerList.get(0).sendKeys(deal.getString("starts_on"));
				Select selectScheduleAt = new Select(
						driver.findElement(By.xpath(landingLocators.getString("select-ScheduleAt"))));
				selectScheduleAt.selectByVisibleText("EOD");
				driver.findElement(By.xpath(landingLocators.getString("Select-Save-Continue"))).click();
				utility.waitForProgressBarToLoad(driver);
				driver.findElement(By.xpath(landingLocators.getString("select-PId"))).click();
				driver.findElement(By.xpath(landingLocators.getString("select-Pid"))).click();
				Select selectBeneficiaryCountry = new Select(
						driver.findElement(By.xpath(landingLocators.getString("select-beneficiaryCountry"))));
				selectBeneficiaryCountry.selectByVisibleText("IND");
				Select selectBeneficiaryCurrency = new Select(
						driver.findElement(By.xpath(landingLocators.getString("select-beneficiaryCurrency"))));
				selectBeneficiaryCurrency.selectByVisibleText("INR");
				driver.findElement(By.xpath(landingLocators.getString("Input-CreditorAccount"))).click();
				driver.findElement(By.xpath(landingLocators.getString("select-CreditorAccount"))).click();
				driver.findElement(By.xpath(landingLocators.getString("Input-PlatformParticipantId"))).click();
				driver.findElement(By.xpath(landingLocators.getString("select-Input"))).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath(landingLocators.getString("input-PlatformReferenceNumber")))
						.sendKeys(deal.getString("platform_Reference_Number"));
				driver.findElement(By.xpath(landingLocators.getString("input-FragmentPlatformReferenceNo")))
						.sendKeys(deal.getString("Fragment_Platform_Reference_No"));
				driver.findElement(By.xpath(landingLocators.getString("input-SelectCategory"))).click();
				driver.findElement(By.xpath(landingLocators.getString("select-SelectCategory"))).click();
				driver.findElement(By.xpath(landingLocators.getString("input-Amount"))).clear();
				driver.findElement(By.xpath(landingLocators.getString("input-Amount")))
						.sendKeys(deal.getString("Amount"));
				Thread.sleep(2000);
				driver.findElement(By.xpath(landingLocators.getString("input-Assignment"))).click();
				driver.findElement(By.xpath(landingLocators.getString("input-RKTestName")))
						.sendKeys(deal.getString("RKTestName"));
				driver.findElement(By.xpath(landingLocators.getString("input-RefNo")))
						.sendKeys(deal.getString("RefNo"));
				driver.findElement(By.xpath(landingLocators.getString("input-id"))).sendKeys(deal.getString("Rfid"));
				driver.findElement(By.xpath(landingLocators.getString("button-Sub-instruction"))).click();
				driver.findElement(By.xpath(landingLocators.getString("button-next"))).click();
				driver.findElement(By.xpath(landingLocators.getString("button-processToSummary"))).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath(landingLocators.getString("button-submit-summary"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(landingLocators.getString("button-Yes"))).click();
				Thread.sleep(2000);
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(landingLocators.getString("button-ok"))).click();

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while moving to Live Deal");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while moving to Live Deal");

			}
		}
	}
	public void goToEcommerceTxnChekar(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				driver.findElement(By.xpath(landingLocators.getString("sidemenu-E-comm"))).click();
				driver.findElement(By.xpath(landingLocators.getString("link-ecommtxnChekar"))).click();
				driver.findElement(By.xpath(landingLocators.getString("input-dealId"))).sendKeys("REF1638166706482");
				Thread.sleep(9000);
				driver.findElement(By.xpath(landingLocators.getString("input-Checkbox"))).click();
				driver.findElement(By.xpath(landingLocators.getString("input-note"))).click();
				driver.findElement(By.xpath(landingLocators.getString("textrea-note"))).sendKeys("Done");
				driver.findElement(By.xpath(landingLocators.getString("button-ok1"))).click();
				driver.findElement(By.xpath(landingLocators.getString("button-Submit"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(landingLocators.getString("button-Yes"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(landingLocators.getString("button-ok"))).click();
				

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while moving to Live Deal");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while moving to Live Deal");

			}
		}
	}
	public void goToEcommerceTxnVerifire(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				driver.findElement(By.xpath(landingLocators.getString("sidemenu-E-comm"))).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath(landingLocators.getString("button-ecomTxtVerifier"))).click();
				driver.findElement(By.xpath(landingLocators.getString("input-dealId"))).sendKeys("REF1638166706482");
				Thread.sleep(9000);
				driver.findElement(By.xpath(landingLocators.getString("input-Checkbox"))).click();
				driver.findElement(By.xpath(landingLocators.getString("input-note"))).click();
				driver.findElement(By.xpath(landingLocators.getString("textrea-note"))).sendKeys("Done");
				driver.findElement(By.xpath(landingLocators.getString("button-ok1"))).click();
				driver.findElement(By.xpath(landingLocators.getString("button-Submit"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(landingLocators.getString("button-Yes"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(landingLocators.getString("button-ok"))).click();
				

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while moving to Live Deal");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while moving to Live Deal");

			}
		}
	}
	

}
