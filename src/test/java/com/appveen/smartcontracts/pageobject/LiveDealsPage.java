package com.appveen.smartcontracts.pageobject;

import java.io.IOException;
import java.util.ArrayList;
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

public class LiveDealsPage {

	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private AdhocTransactionPage adhocTransactionPage = null;
	private LinkedTransactionPage linkedTransactionPage = null;
	private LandingPage landingPage = null;
	private UpdateDealPage updateDealPage = null;
	private DealLifecycleMakerPage dealLifecycleMakerPage = null;
	private DealLifecycleCheckerPage dealLifecycleCheckerPage = null;
	private JSONObject liveDealsLocator = null;
	private JSONArray deals = null;
	private JSONArray transactions = null;

	private List<String> getDealTitleList(WebDriver driver, ExtentTest step) throws Exception {
		List<String> dealTitleList = new ArrayList<String>();
		try {
			List<WebElement> dealTitles = driver.findElements(By.xpath(liveDealsLocator.getString("card-DealTitle")));

			for (WebElement dealTitle : dealTitles) {
				dealTitleList.add(dealTitle.getText().toLowerCase().trim());
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while fetching deal titles from Live Deals page");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while fetching deal titles from Live Deals page");
		}
		return dealTitleList;
	}

	public LiveDealsPage(LocatorFactory locators) {
		liveDealsLocator = locators.getLocators("LiveDealsPage");
		landingPage = new LandingPage(locators);
		adhocTransactionPage = new AdhocTransactionPage(locators);
		linkedTransactionPage = new LinkedTransactionPage(locators);
		updateDealPage = new UpdateDealPage(locators);
		dealLifecycleMakerPage = new DealLifecycleMakerPage(locators);
		dealLifecycleCheckerPage = new DealLifecycleCheckerPage(locators);
	}

	public void checkLiveDealTitles(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();

		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				utility.waitForProgressBarToLoad(driver);

				Select dealSearchType = new Select(
						driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
				dealSearchType.selectByVisibleText("Deal Name");
				driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
				driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter")))
						.sendKeys(deal.getString("name"));
				driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
				utility.waitForProgressBarToLoad(driver);

				List<String> dealTitleList = getDealTitleList(driver, step);
				String dealTitle = deal.getString("name").toLowerCase().trim();

				if (dealTitleList.contains(dealTitle)) {
					step.pass("Deal " + deal.getString("name") + " available at Live Deals page");
					report.addStepPassScreenshot(driver, step);
				} else {
					step.fail("Deal " + deal.getString("name") + "  not available at Live Deals page");
					report.addStepFailScreenshot(driver, step);
				}

				driver.findElement(By.id(liveDealsLocator.getString("button-DealSearchClearFiler"))).click();
			} catch (Exception e) {
				step.log(Status.FAIL,
						"Error while checking deal title: " + deal.getString("name") + " from Live Deals page");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception(
						"Error while checking deal title: " + deal.getString("name") + " from Live Deals page");
			}
		}
	}

	public void checkLiveDealTitle(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {
		try {
			utility.waitForProgressBarToLoad(driver);

			Select dealSearchType = new Select(
					driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
			dealSearchType.selectByVisibleText("Deal Name");
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter")))
					.sendKeys(deal.getString("name"));
			driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
			utility.waitForProgressBarToLoad(driver);
			List<String> dealTitleList = getDealTitleList(driver, step);
			String dealTitle = deal.getString("name").toLowerCase().trim();

			if (dealTitleList.contains(dealTitle)) {
				step.pass("Deal " + deal.getString("name") + " available at Live Deals page");
				report.addStepPassScreenshot(driver, step);
			} else {
				step.fail("Deal " + deal.getString("name") + "  not available at Live Deals page");
				report.addStepFailScreenshot(driver, step);
			}

			driver.findElement(By.id(liveDealsLocator.getString("button-DealSearchClearFiler"))).click();
		} catch (Exception e) {
			step.log(Status.FAIL,
					"Error while checking deal title: " + deal.getString("name") + " from Live Deals page");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while checking deal title: " + deal.getString("name") + " from Live Deals page");
		}
	}

	public void addTransactions(WebDriver driver, ExtentTest step) throws Exception {

		transactions = testData.getJSONArray("transactions");
		Iterator<Object> transactionIterator = transactions.iterator();

		while (transactionIterator.hasNext()) {
			JSONObject transaction = (JSONObject) transactionIterator.next();
			try {
				landingPage.goToLiveDeal(driver, step);

				utility.waitForProgressBarToLoad(driver);

				Select dealSearchType = new Select(
						driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
				dealSearchType.selectByVisibleText("Deal Name");
				driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
				driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter")))
						.sendKeys(transaction.getString("deal_name"));
				driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
				utility.waitForProgressBarToLoad(driver);

				List<String> dealTitleList = getDealTitleList(driver, step);
				String dealTitle = transaction.getString("deal_name").toLowerCase().trim();

				if (dealTitleList.contains(dealTitle)) {
					String actualdealID = driver.findElements(By.xpath(liveDealsLocator.getString("card-DealDetail")))
							.get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText()
							.toLowerCase().trim();
					String expectedDealID = (DealCheckerPage.dealDetails.get(dealTitle) == null)
							? transaction.getString("deal_id").toLowerCase().trim()
							: DealCheckerPage.dealDetails.get(dealTitle).toLowerCase().trim();

					if (actualdealID.equals(expectedDealID)) {
						driver.findElements(By.id(liveDealsLocator.getString("button-DealMenuButton")))
								.get(dealTitleList.indexOf(dealTitle)).click();
						WebElement addTransaction = driver
								.findElement(By.id(liveDealsLocator.getString("menu-DealPopupMenu")))
								.findElements(By.tagName("div")).get(5);
						addTransaction.click();

						WebElement menu = addTransaction.findElement(By.tagName("ul"));
						utility.selectFromDivMenu(driver, step, menu, transaction.getString("type"));
						menu.click();

						//WebElement subMenu = menu.findElement(By.tagName("ul"));
						Thread.sleep(500);
						//utility.selectFromDivMenu(driver, step, subMenu, transaction.getString("sub_type"));
						driver.findElement(By.xpath(liveDealsLocator.getString("select-payment"))).click();
						
						if (transaction.getString("type").toLowerCase().trim().equals("adhoc")) {
							adhocTransactionPage.addTransaction(driver, step, transaction);
						} else if (transaction.getString("type").toLowerCase().trim().equals("linked")) {
							linkedTransactionPage.addTransaction(driver, step, transaction);
						}
					} else {
						step.log(Status.FAIL, "Unable to validate Deal ID. (Expected: " + expectedDealID + "/ Actual: "
								+ actualdealID + ")");
						report.addStepFailScreenshot(driver, step);
					}

				} else {
					step.log(Status.FAIL, "Unable to find Deal: " + dealTitle);
					report.addStepFailScreenshot(driver, step);
				}

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while initiating transaction of type: " + transaction.getString("type")
						+ " for live deal");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while initiating transaction of type: " + transaction.getString("type")
						+ " for live deal");
			}
		}
	}

	public void addTransaction(WebDriver driver, ExtentTest step, JSONObject transaction) throws Exception {
		try {
			landingPage.goToLiveDeal(driver, step);

			utility.waitForProgressBarToLoad(driver);

			Select dealSearchType = new Select(
					driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
			dealSearchType.selectByVisibleText("Deal Name");
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter")))
					.sendKeys(transaction.getString("deal_name"));
			driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
			utility.waitForProgressBarToLoad(driver);

			List<String> dealTitleList = getDealTitleList(driver, step);
			String dealTitle = transaction.getString("deal_name").toLowerCase().trim();

			if (dealTitleList.contains(dealTitle)) {
				String actualdealID = driver.findElements(By.xpath(liveDealsLocator.getString("card-DealDetail")))
						.get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText()
						.toLowerCase().trim();
				String expectedDealID = (DealCheckerPage.dealDetails.get(dealTitle) == null)
						? transaction.getString("deal_id").toLowerCase().trim()
						: DealCheckerPage.dealDetails.get(dealTitle).toLowerCase().trim();

				if (actualdealID.equals(expectedDealID)) {
					driver.findElements(By.id(liveDealsLocator.getString("button-DealMenuButton")))
							.get(dealTitleList.indexOf(dealTitle)).click();
					WebElement addTransaction = driver
							.findElement(By.id(liveDealsLocator.getString("menu-DealPopupMenu")))
							.findElements(By.tagName("div")).get(5);
					addTransaction.click();

					WebElement menu = addTransaction.findElement(By.tagName("ul"));
					utility.selectFromDivMenu(driver, step, menu, transaction.getString("type"));

					WebElement subMenu = menu.findElement(By.tagName("ul"));
					Thread.sleep(500);
					utility.selectFromDivMenu(driver, step, subMenu, transaction.getString("sub_type"));

					if (transaction.getString("type").toLowerCase().trim().equals("adhoc")) {
						adhocTransactionPage.addTransaction(driver, step, transaction);
					} else if (transaction.getString("type").toLowerCase().trim().equals("linked")) {
						linkedTransactionPage.addTransaction(driver, step, transaction);
					}
				} else {
					step.log(Status.FAIL, "Unable to validate Deal ID. (Expected: " + expectedDealID + "/ Actual: "
							+ actualdealID + ")");
					report.addStepFailScreenshot(driver, step);
				}

			} else {
				step.log(Status.FAIL, "Unable to find Deal: " + dealTitle);
				report.addStepFailScreenshot(driver, step);
			}

		} catch (Exception e) {
			step.log(Status.FAIL,
					"Error while initiating transaction of type: " + transaction.getString("type") + "for live deal");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception(
					"Error while initiating transaction of type: " + transaction.getString("type") + "for live deal");
		}
	}

	public void editLiveDeals(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("edit_deals");
		Iterator<Object> dealIterator = deals.iterator();

		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();

			try {
				landingPage.goToLiveDeal(driver, step);

				utility.waitForProgressBarToLoad(driver);

				Select dealSearchType = new Select(
						driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
				dealSearchType.selectByVisibleText("Deal Name");
				driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
				driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter")))
						.sendKeys(deal.getString("name"));
				driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
				utility.waitForProgressBarToLoad(driver);

				List<String> dealTitleList = getDealTitleList(driver, step);
				String dealTitle = deal.getString("name").toLowerCase().trim();

				if (dealTitleList.contains(dealTitle)) {
					String actualdealID = driver.findElements(By.xpath(liveDealsLocator.getString("card-DealDetail")))
							.get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText()
							.toLowerCase().trim();
					String expectedDealID = (DealCheckerPage.dealDetails.get(dealTitle) == null)
							? deal.getString("deal_id").toLowerCase().trim()
							: DealCheckerPage.dealDetails.get(dealTitle).toLowerCase().trim();

					if (actualdealID.equals(expectedDealID)) {
						driver.findElements(By.id(liveDealsLocator.getString("button-DealMenuButton")))
								.get(dealTitleList.indexOf(dealTitle)).click();
						WebElement editDeal = driver
								.findElement(By.id(liveDealsLocator.getString("menu-DealPopupMenu")))
								.findElements(By.tagName("div")).get(2);
						editDeal.click();
						step.log(Status.INFO, "Initiated edit for live deal: " + dealTitle);

						updateDealPage.editDeal(driver, step, deal);
					} else {
						step.log(Status.FAIL, "Unable to validate Deal ID. (Expected: " + expectedDealID + "/ Actual: "
								+ actualdealID + ")");
						report.addStepFailScreenshot(driver, step);
					}

				} else {
					step.log(Status.FAIL, "Unable to find Deal: " + dealTitle);
					report.addStepFailScreenshot(driver, step);
				}
			} catch (Exception e) {
				step.log(Status.FAIL, "Error while initiating edit for a live deal" + deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while initiating edit for a live deal" + deal.getString("name"));
			}
		}
	}

	public void pauseLiveDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {
		try {
			landingPage.goToLiveDeal(driver, step);

			utility.waitForProgressBarToLoad(driver);

			Select dealSearchType = new Select(
					driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
			dealSearchType.selectByVisibleText("Deal Name");
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter")))
					.sendKeys(deal.getString("name"));
			driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
			utility.waitForProgressBarToLoad(driver);

			List<String> dealTitleList = getDealTitleList(driver, step);
			String dealTitle = deal.getString("name").toLowerCase().trim();

			if (dealTitleList.contains(dealTitle)) {
				String actualdealID = driver.findElements(By.xpath(liveDealsLocator.getString("card-DealDetail")))
						.get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText()
						.toLowerCase().trim();
				String expectedDealID = (DealCheckerPage.dealDetails.get(dealTitle) == null)
						? deal.getString("deal_id").toLowerCase().trim()
						: DealCheckerPage.dealDetails.get(dealTitle).toLowerCase().trim();

				if (actualdealID.equals(expectedDealID)) {
					driver.findElements(By.id(liveDealsLocator.getString("button-DealMenuButton")))
							.get(dealTitleList.indexOf(dealTitle)).click();
					WebElement pauseDeal = driver.findElement(By.id(liveDealsLocator.getString("menu-DealPopupMenu")))
							.findElements(By.tagName("div")).get(3);
					pauseDeal.click();
					driver.findElement(By.xpath(liveDealsLocator.getString("button-Yes"))).click();
					step.log(Status.INFO, "Initiated pause for live deal: " + dealTitle);
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(liveDealsLocator.getString("button-OK"))).click();
				} else {
					step.log(Status.FAIL, "Unable to validate Deal ID. (Expected: " + expectedDealID + "/ Actual: "
							+ actualdealID + ")");
					report.addStepFailScreenshot(driver, step);
				}

			} else {
				step.log(Status.FAIL, "Unable to find Deal: " + dealTitle);
				report.addStepFailScreenshot(driver, step);
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating pause for a live deal" + deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating pause for a live deal" + deal.getString("name"));
		}
	}

	public void closeLiveDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {
		try {
			landingPage.goToLiveDeal(driver, step);

			utility.waitForProgressBarToLoad(driver);

			Select dealSearchType = new Select(
					driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
			dealSearchType.selectByVisibleText("Deal Name");
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter")))
					.sendKeys(deal.getString("name"));
			driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
			utility.waitForProgressBarToLoad(driver);

			List<String> dealTitleList = getDealTitleList(driver, step);
			String dealTitle = deal.getString("name").toLowerCase().trim();

			if (dealTitleList.contains(dealTitle)) {
				String actualdealID = driver.findElements(By.xpath(liveDealsLocator.getString("card-DealDetail")))
						.get(dealTitleList.indexOf(dealTitle)).findElements(By.tagName("dd")).get(0).getText()
						.toLowerCase().trim();
				String expectedDealID = (DealCheckerPage.dealDetails.get(dealTitle) == null)
						? deal.getString("deal_id").toLowerCase().trim()
						: DealCheckerPage.dealDetails.get(dealTitle).toLowerCase().trim();

				if (actualdealID.equals(expectedDealID)) {
					driver.findElements(By.id(liveDealsLocator.getString("button-DealMenuButton")))
							.get(dealTitleList.indexOf(dealTitle)).click();
					WebElement closeDeal = driver.findElement(By.id(liveDealsLocator.getString("menu-DealPopupMenu")))
							.findElements(By.tagName("div")).get(4);
					closeDeal.click();
					driver.findElement(By.xpath(liveDealsLocator.getString("button-Yes"))).click();
					step.log(Status.INFO, "Initiated close for live deal: " + dealTitle);
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(liveDealsLocator.getString("button-OK"))).click();

					landingPage.goToDealLifeCycleMaker(driver, step);
					dealLifecycleMakerPage.sendDealCloseRequestToChecker(driver, step, deal);
					landingPage.goToDealLifeCycleChecker(driver, step);
					dealLifecycleCheckerPage.approveDealCloseRequest(driver, step, deal);
				} else {
					step.log(Status.FAIL, "Unable to validate Deal ID. (Expected: " + expectedDealID + "/ Actual: "
							+ actualdealID + ")");
					report.addStepFailScreenshot(driver, step);
				}

			} else {
				step.log(Status.FAIL, "Unable to find Deal: " + dealTitle);
				report.addStepFailScreenshot(driver, step);
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating close for a live deal" + deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating pause for a live deal" + deal.getString("name"));
		}
	}

	public void pauseNewLiveDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {
		try {
			landingPage.goToLiveDeal(driver, step);

			utility.waitForProgressBarToLoad(driver);

			Select dealSearchType = new Select(
					driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
			dealSearchType.selectByVisibleText("Deal Name");
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter")))
					.sendKeys(deal.getString("name"));
			driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
			utility.waitForProgressBarToLoad(driver);
			driver.findElement(By.xpath(liveDealsLocator.getString("button-Ic-Menu"))).click();
			driver.findElement(By.xpath(liveDealsLocator.getString("button-Pause"))).click();
			driver.findElement(By.xpath(liveDealsLocator.getString("button-Yes"))).click();
			driver.findElement(By.xpath(liveDealsLocator.getString("button-OK"))).click();

		} catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating pause for a live deal" + deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating pause for a live deal" + deal.getString("name"));
		}
	}

	public void verifyPauseDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Throwable {
		try {
			landingPage.goToNewLiveDeal(driver, step);

			utility.waitForProgressBarToLoad(driver);

			Select dealSearchType = new Select(
					driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
			dealSearchType.selectByVisibleText("Deal Name");
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter")))
					.sendKeys(deal.getString("name"));
			driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
			utility.waitForProgressBarToLoad(driver);
			report.addStepPassScreenshot(driver, step);

		} catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating pause for a live deal" + deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating pause for a live deal" + deal.getString("name"));

		}

	}

	public void resumeNewLiveDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {
		try {
			landingPage.goToLiveDeal(driver, step);

			utility.waitForProgressBarToLoad(driver);

			Select dealSearchType = new Select(
					driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
			dealSearchType.selectByVisibleText("Status");
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).sendKeys("paused");
			driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
			utility.waitForProgressBarToLoad(driver);
			driver.findElement(By.xpath(liveDealsLocator.getString("button-Ic-Menu"))).click();
			driver.findElement(By.xpath(liveDealsLocator.getString("button-resume"))).click();
			driver.findElement(By.xpath(liveDealsLocator.getString("button-Yes"))).click();
			driver.findElement(By.xpath(liveDealsLocator.getString("button-OK"))).click();

		} catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating pause for a live deal" + deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating pause for a live deal" + deal.getString("name"));
		}
	}

	public void verifyResumeDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Throwable {
		try {
			landingPage.goToNewLiveDeal(driver, step);
			utility.waitForProgressBarToLoad(driver);
			report.addStepPassScreenshot(driver, step);
			driver.findElement(By.xpath(liveDealsLocator.getString("Status-Active"))).isDisplayed();
			driver.findElement(By.xpath(liveDealsLocator.getString("Status-Active"))).getText();

		} catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating pause for a live deal" + deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating pause for a live deal" + deal.getString("name"));

		}

	}

	public void closeNewLiveDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Exception {
		try {
			landingPage.goToLiveDeal(driver, step);

			utility.waitForProgressBarToLoad(driver);
			driver.findElement(By.xpath(liveDealsLocator.getString("button-Ic-Menu"))).click();
			driver.findElement(By.xpath(liveDealsLocator.getString("button-close"))).click();
			driver.findElement(By.xpath(liveDealsLocator.getString("button-Yes"))).click();
			driver.findElement(By.xpath(liveDealsLocator.getString("button-OK"))).click();

		} catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating pause for a live deal" + deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating pause for a live deal" + deal.getString("name"));
		}
	}

	public void verifyCloseDeal(WebDriver driver, ExtentTest step, JSONObject deal) throws Throwable {
		try {
			landingPage.goToNewLiveDeal(driver, step);
			utility.waitForProgressBarToLoad(driver);
			Select dealSearchType = new Select(
					driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
			dealSearchType.selectByVisibleText("Status");
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).clear();
			driver.findElement(By.id(liveDealsLocator.getString("input-DealSearchParameter"))).sendKeys("close");
			driver.findElement(By.id(liveDealsLocator.getString("button-DealSearch"))).click();
			utility.waitForProgressBarToLoad(driver);
			report.addStepPassScreenshot(driver, step);
			driver.findElement(By.xpath(liveDealsLocator.getString("status-close"))).isDisplayed();
			driver.findElement(By.xpath(liveDealsLocator.getString("status-close"))).getText();

		} catch (Exception e) {
			step.log(Status.FAIL, "Error while initiating pause for a live deal" + deal.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while initiating pause for a live deal" + deal.getString("name"));

		}

	}

	public void goToDealDraft(WebDriver driver, ExtentTest step) throws Exception {
		deals = testData.getJSONArray("deals");
		Iterator<Object> dealIterator = deals.iterator();
		while (dealIterator.hasNext()) {
			JSONObject deal = (JSONObject) dealIterator.next();
			try {
				landingPage.goToDealDrafts(driver, step);

				utility.waitForProgressBarToLoad(driver);

				Select dealSearchType = new Select(
						driver.findElement(By.id(liveDealsLocator.getString("select-DealSearchType"))));
				dealSearchType.selectByVisibleText("Deal Id");
				driver.findElement(By.xpath(liveDealsLocator.getString("input-serach"))).sendKeys("REF1633513933283");
				driver.findElement(By.xpath(liveDealsLocator.getString("button-search"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("div-showMenu"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("button-open"))).click();
				utility.waitForProgressBarToLoad(driver);
				Thread.sleep(1000);
				driver.findElement(By.xpath(liveDealsLocator.getString("menu-party"))).click();

				driver.findElement(By.xpath(liveDealsLocator.getString("span-addparty"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("span-addNewparty"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-cutId")))
						.sendKeys(deal.getString("CustomerId"));
				driver.findElement(By.xpath(liveDealsLocator.getString("input-partyName")))
						.sendKeys(deal.getString("PartyName"));
				driver.findElement(By.xpath(liveDealsLocator.getString("input-Responsibility"))).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath(liveDealsLocator.getString("select-Responsibility"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-remark"))).sendKeys("text");
				Thread.sleep(1000);
				driver.findElement(By.xpath(liveDealsLocator.getString("input-commission"))).sendKeys("123");
				driver.findElement(By.xpath(liveDealsLocator.getString("input-CommissionPlan"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("select-CommissionPlan"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-checkbox"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("select-ParticipantId")))
						.sendKeys(deal.getString("CustomerId"));
				driver.findElement(By.xpath(liveDealsLocator.getString("select-DebitAccounts"))).click();
				driver.findElement(By.id(liveDealsLocator.getString("select-nextButton"))).click();
				utility.waitForProgressBarToLoad(driver);
				driver.findElement(By.xpath(liveDealsLocator.getString("select-Account"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("add-account"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("select-paymentSystem"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("select-accountpayment"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-account-number"))).sendKeys("123343211");
				driver.findElement(By.xpath(liveDealsLocator.getString("input-account-name"))).sendKeys("ASDFR");
				driver.findElement(By.xpath(liveDealsLocator.getString("input-addButton"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-back"))).click();
				utility.waitForProgressBarToLoad(driver);
				driver.findElement(By.xpath(liveDealsLocator.getString("span-addparty"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("span-addNewparty"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-cutId")))
						.sendKeys(deal.getString("CustomerId1"));
				driver.findElement(By.xpath(liveDealsLocator.getString("input-partyName")))
						.sendKeys(deal.getString("PartyName1"));
				driver.findElement(By.xpath(liveDealsLocator.getString("input-Responsibility"))).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath(liveDealsLocator.getString("select-platform"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-remark"))).sendKeys("text");
				Thread.sleep(1000);
				driver.findElement(By.xpath(liveDealsLocator.getString("input-pName")))
						.sendKeys(deal.getString("CustomerId1"));
				driver.findElement(By.xpath(liveDealsLocator.getString("input-pName")))
						.sendKeys(deal.getString("PartyName2"));
				Thread.sleep(2000);
				driver.findElement(By.xpath(liveDealsLocator.getString("input-DatePicker"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("select-date"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("button-done"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-checkbox"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("select-ParticipantId")))
						.sendKeys(deal.getString("CustomerId1"));
				driver.findElement(By.xpath(liveDealsLocator.getString("select-DebitAccounts"))).click();
				driver.findElement(By.id(liveDealsLocator.getString("select-nextButton"))).click();
				utility.waitForProgressBarToLoad(driver);
				driver.findElement(By.xpath(liveDealsLocator.getString("select-Account"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("add-account"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("select-paymentSystem"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("select-accountpayment"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-account-number"))).sendKeys("123343211");
				driver.findElement(By.xpath(liveDealsLocator.getString("input-account-name"))).sendKeys("ASDFR");
				driver.findElement(By.xpath(liveDealsLocator.getString("input-addButton"))).click();
				driver.findElement(By.xpath(liveDealsLocator.getString("input-back"))).click();
			} catch (Exception e) {
				step.log(Status.FAIL, "Error while initiating pause for a live deal" + deal.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while initiating pause for a live deal" + deal.getString("name"));
			}
		}
	}
}
