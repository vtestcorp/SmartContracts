package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class DealLifecycleCheckerPage {

	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private JSONObject dealLifecycleCheckerLocator = null;
	private JSONArray deals = null;
	private UtilityFactory utility = new UtilityFactory();

	public DealLifecycleCheckerPage(LocatorFactory locators) {
		dealLifecycleCheckerLocator = locators.getLocators("DealLifecycleCheckerPage");
	}

	public void approveDealPauseRequest(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("pause_deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				// setPreference();

				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1)
						.sendKeys(deal.getString("name"));
				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

				Select dealOperation = new Select(
						driver.findElement(By.id(dealLifecycleCheckerLocator.getString("select-OperationFilter"))));
				dealOperation.selectByVisibleText("Pause");
				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

				String resultDeal = driver
						.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-DealName"))).get(1)
						.getText();
				if (resultDeal.equals(deal.getString("name"))) {
					driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-Manage"))).get(1)
							.findElements(By.tagName("i")).get(0).click();
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("input-AddComments")))
							.sendKeys("Approved");
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Approve"))).click();

					step.log(Status.INFO, "Deal: " + deal.getString("name") + " pause request approved");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Yes"))).click();
				} else {
					step.log(Status.FAIL,
							"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
					report.addStepFailScreenshot(driver, step);
					throw new Exception(
							"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
				}

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while approving pause request for deal: " + deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while approving pause request for deal: " + deal.getString("name"));
			} finally {
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}

	public void reviewDealPauseRequest(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("pause_deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				// setPreference();

				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1)
						.sendKeys(deal.getString("name"));
				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

				Select dealOperation = new Select(
						driver.findElement(By.id(dealLifecycleCheckerLocator.getString("select-OperationFilter"))));
				dealOperation.selectByVisibleText("Pause");
				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

				String resultDeal = driver
						.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-DealName"))).get(1)
						.getText();
				if (resultDeal.equals(deal.getString("name"))) {
					driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-Manage"))).get(1)
							.findElements(By.tagName("i")).get(0).click();
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("input-AddComments")))
							.sendKeys("Review");
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-SendForReview"))).click();

					step.log(Status.INFO, "Deal: " + deal.getString("name") + " pause request send for review");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Yes"))).click();
				} else {
					step.log(Status.FAIL,
							"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
					report.addStepFailScreenshot(driver, step);
					throw new Exception(
							"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
				}

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while sending back pause request for deal: " + deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while sending back pause request for deal: " + deal.getString("name"));
			} finally {
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}

	public void approveDealCloseRequest(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("close_deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				// setPreference();

				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1)
						.sendKeys(deal.getString("name"));
				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

				Select dealOperation = new Select(
						driver.findElement(By.id(dealLifecycleCheckerLocator.getString("select-OperationFilter"))));
				dealOperation.selectByVisibleText("Close");
				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

				String resultDeal = driver
						.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-DealName"))).get(1)
						.getText();
				if (resultDeal.equals(deal.getString("name"))) {
					driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-Manage"))).get(1)
							.findElements(By.tagName("i")).get(0).click();
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("input-AddComments")))
							.sendKeys("Approved");
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Approve"))).click();

					step.log(Status.INFO, "Deal: " + deal.getString("name") + " close request approved");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Yes"))).click();
				} else {
					step.log(Status.FAIL,
							"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
					report.addStepFailScreenshot(driver, step);
					throw new Exception(
							"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
				}

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while approving close request for deal: " + deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while approving close request for deal: " + deal.getString("name"));
			} finally {
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}

	public void approveDealCloseRequest(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {

		try {
			// setPreference();

			utility.waitForElementToBeDisplayed(driver,
					By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);
			driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(2).clear();
			driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(2)
					.sendKeys(deal.getString("name"));
			utility.waitForElementToBeDisplayed(driver,
					By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

			Select dealOperation = new Select(
					driver.findElement(By.id(dealLifecycleCheckerLocator.getString("select-OperationFilter"))));
			dealOperation.selectByVisibleText("Close");
			utility.waitForElementToBeDisplayed(driver,
					By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

			String resultDeal = driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")))
					.get(1).getText();
			if (resultDeal.equals(deal.getString("name"))) {
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-Manage"))).get(1)
						.findElements(By.tagName("i")).get(0).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("input-AddComments")))
						.sendKeys("Approved");
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Approve"))).click();

				step.log(Status.INFO, "Deal: " + deal.getString("name") + " close request approved");
				report.addStepInfoScreenshot(driver, step);
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Yes"))).click();
			} else {
				step.log(Status.FAIL,
						"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
				report.addStepFailScreenshot(driver, step);
				throw new Exception(
						"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
			}

		} catch (Exception e) {
			step.log(Status.FAIL, "Error while approving close request for deal: " + deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while approving close request for deal: " + deal.getString("name"));
		}
	}

	public void reviewDealCloseRequest(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("close_deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				// setPreference();

				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1)
						.sendKeys(deal.getString("name"));
				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

				Select dealOperation = new Select(
						driver.findElement(By.id(dealLifecycleCheckerLocator.getString("select-OperationFilter"))));
				dealOperation.selectByVisibleText("Close");
				utility.waitForElementToBeDisplayed(driver,
						By.xpath(dealLifecycleCheckerLocator.getString("column-DealName")), 2, 120);

				String resultDeal = driver
						.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-DealName"))).get(1)
						.getText();
				if (resultDeal.equals(deal.getString("name"))) {
					driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("column-Manage"))).get(1)
							.findElements(By.tagName("i")).get(0).click();
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("input-AddComments")))
							.sendKeys("Review");
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-SendForReview"))).click();

					step.log(Status.INFO, "Deal: " + deal.getString("name") + " close request send for review");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Yes"))).click();
				} else {
					step.log(Status.FAIL,
							"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
					report.addStepFailScreenshot(driver, step);
					throw new Exception(
							"Deal: " + deal.getString("name") + " not available at Deal Lifecycle Checker Queue");
				}

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while sending back close request for deal: " + deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while sending back close request for deal: " + deal.getString("name"));
			} finally {
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}

	public void approveNewDealPauseRequest(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {

				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("column-DealLifecycle"))).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("colum-Checker"))).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("Ui-Icon-Submit"))).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("textarea-Comments")))
						.sendKeys("Done");
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Approve"))).click();

				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Yes"))).click();

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while approving pause request for deal: " + deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while approving pause request for deal: " + deal.getString("name"));
			} finally {
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}

	public void approveNewDealResumeRequest(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {

				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("column-DealLifecycle"))).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("colum-Checker"))).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("Ui-Icon-Submit"))).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("textarea-Comments")))
						.sendKeys("Done");
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Approve"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Yes"))).click();

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while approving pause request for deal: " + deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while approving pause request for deal: " + deal.getString("name"));
			} finally {
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}
	public void approveNewDealCloseRequest(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {

				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("column-DealLifecycle"))).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("colum-Checker"))).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("colum-Icon-Doc-submit"))).click();
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("textarea-Comments")))
						.sendKeys("Done");
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Approve"))).click();
				report.addStepPassScreenshot(driver, step);
				driver.findElement(By.xpath(dealLifecycleCheckerLocator.getString("button-Yes"))).click();

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while approving pause request for deal: " + deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while approving pause request for deal: " + deal.getString("name"));
			} finally {
				driver.findElements(By.xpath(dealLifecycleCheckerLocator.getString("input-FilterText"))).get(1).clear();
			}
		}
	}
}
