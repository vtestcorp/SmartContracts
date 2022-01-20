package com.appveen.smartcontracts.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class UtilityFactory {

	private WebDriverFactory webDriverFactory = new WebDriverFactory();
	private ReportFactory report = ListenerFactory.reportFactory;

	public void addToExpectedDetails(HashMap<String, ArrayList<String>> expectedDetails, String mapKey, String myItem) {
		List<String> itemsList = expectedDetails.get(mapKey);
		if (itemsList == null) {
			itemsList = new ArrayList<String>();
			itemsList.add(myItem);
			expectedDetails.put(mapKey, (ArrayList<String>) itemsList);
		} else {
			itemsList.add(myItem);
			expectedDetails.put(mapKey, (ArrayList<String>) itemsList);
		}
	}

	public void selectFromDivMenu(WebDriver driver, ExtentTest step, WebElement unorderedList, JSONArray selectOptions)
			throws Exception {
		try {
			Iterator<Object> selectIterator = selectOptions.iterator();
			List<WebElement> listItems = unorderedList.findElements(By.tagName("li"));

			while (selectIterator.hasNext()) {
				String option = selectIterator.next().toString();
				Iterator<WebElement> listItemsIterator = listItems.iterator();
				Thread.sleep(500);
				while (listItemsIterator.hasNext()) {
					WebElement listItem = listItemsIterator.next();
					if (option.equals(listItem.getText())) {
						listItem.click();
					}
				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while selecting from " + selectOptions.toString());
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while selecting from " + selectOptions.toString());
		}
	}

	public void selectFromDivMenu(WebDriver driver, ExtentTest step, WebElement unorderedList, String selectOption)
			throws Exception {
		try {
			List<WebElement> listItems = unorderedList.findElements(By.tagName("li"));
			Iterator<WebElement> listItemsIterator = listItems.iterator();

			while (listItemsIterator.hasNext()) {
				WebElement listItem = listItemsIterator.next();
				if (selectOption.equals(listItem.getText())) {
					Thread.sleep(1000);
					listItem.click();

				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while selecting from " + selectOption);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while selecting from " + selectOption);
		}
	}

	public List<String> extractOptionsFromSelect(WebDriver driver, ExtentTest step, WebElement unorderedList)
			throws Exception {
		List<String> options = new ArrayList<String>();
		try {
			List<WebElement> listItems = unorderedList.findElements(By.tagName("li"));
			Iterator<WebElement> listItemsIterator = listItems.iterator();

			while (listItemsIterator.hasNext()) {
				WebElement listItem = listItemsIterator.next();
				options.add(listItem.getText());
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting options from select");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting options from select");
		}
		return options;
	}

	public List<String> extractColumnsFromTable(WebDriver driver, ExtentTest step, List<WebElement> tables,
			Integer columnNumber) throws Exception {
		List<String> columnList = new ArrayList<String>();
		try {
			Thread.sleep(1000);
			for (WebElement element : tables) {
				String columnText = element.findElements(By.tagName("td")).get(columnNumber).getText();
				columnList.add(columnText.trim());

			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting column text from table");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting column text from table");
		}
		return columnList;
	}

	public List<String> extractColumnsFromTable(WebDriver driver, ExtentTest step, By table, Integer columnNumber)
			throws Exception {
		List<String> columnList = new ArrayList<String>();
		try {
			Thread.sleep(1000);
			List<WebElement> tableElements = driver.findElements(table);
			for (WebElement element : tableElements) {
				String columnText = element.findElements(By.tagName("td")).get(columnNumber).getText();
				columnList.add(columnText.trim());

			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting column text from table");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting column text from table");
		}
		return columnList;
	}

	public List<String> extractColumnsFromTable(WebDriver driver, ExtentTest step, WebElement table,
			Integer columnNumber) throws Exception {
		List<String> columnList = new ArrayList<String>();
		try {
			Thread.sleep(1000);
			String columnText = table.findElements(By.tagName("td")).get(columnNumber).getText();
			columnList.add(columnText.trim());
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting column text from table");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting column text from table");
		}
		return columnList;
	}

	public List<String> extractRequiredMessage(WebDriver driver, ExtentTest step) throws Exception {
		List<String> messageList = new ArrayList<String>();
		Actions action = new Actions(driver);
		try {
			Thread.sleep(1000);
			if (!isElementHiddenNow(driver, By.tagName("xcro-input-error"))) {
				List<WebElement> errorElements = driver.findElements(By.tagName("xcro-input-error"));
				for (WebElement element : errorElements) {

					if (element.getAttribute("errmsg") != null) {
						String errorText = element.getAttribute("errmsg");
						messageList.add(errorText.trim());
						action.moveToElement(element.findElement(By.tagName("i"))).perform();
						step.log(Status.INFO, "Error message: '" + errorText + "' displayed");
					}

					String requiredText = element.getAttribute("requiredmsg");
					messageList.add(requiredText.trim());
					action.moveToElement(element.findElement(By.tagName("i"))).perform();
					step.log(Status.INFO, "Required message: '" + requiredText + "' displayed");
					report.addStepInfoScreenshot(driver, step);
				}
			}
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting required messages");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting required messages");
		}
		return messageList;
	}

	public String extractTitleMessage(WebDriver driver, ExtentTest step, By locator) throws Exception {
		String title = null;
		try {
			title = driver.findElement(locator).getText();
			step.log(Status.INFO, "Response: '" + title + "' displayed");
			report.addStepInfoScreenshot(driver, step);
		} catch (Exception e) {
			step.log(Status.FAIL, "Error while extracting title message");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while extracting title message");
		}
		return title;
	}

	public boolean isElementHiddenNow(WebDriver driver, By locator) {
		boolean result = false;
		try {
			Thread.sleep(1000);
			turnOffImplicitWaits(driver);
			result = ExpectedConditions.invisibilityOfElementLocated(locator).apply(driver);
			turnOnImplicitWaits(driver);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void waitForProgressBarToLoad(WebDriver driver) throws InterruptedException {
		turnOffImplicitWaits(driver);
		int noOfTries = 120;
		int tries = 0;
		Thread.sleep(1000);
		while (driver.findElements(By.xpath("//div[contains(@style, 'translate3d(-100%, 0px, 0px);')]")).size() < 1
				&& tries < noOfTries) {
			Thread.sleep(500);
			tries++;
		}
		turnOnImplicitWaits(driver);
	}

	public void waitForElementToBeDisplayed(WebDriver driver, By locator, int noOfTries) throws InterruptedException {
		turnOffImplicitWaits(driver);
		int tries = 0;
		Thread.sleep(1000);
		while (driver.findElements(locator).size() < 1 && tries < noOfTries) {
			Thread.sleep(500);
			tries++;
		}
		turnOnImplicitWaits(driver);
	}

	public void waitForElementToBeDisplayed(WebDriver driver, By locator, int elementSize, int noOfTries)
			throws InterruptedException {
		turnOffImplicitWaits(driver);
		int tries = 0;
		Thread.sleep(1000);
		while (driver.findElements(locator).size() < elementSize && tries < noOfTries) {
			Thread.sleep(500);
			tries++;
		}
		turnOnImplicitWaits(driver);
	}

	public void turnOnElementHighlight(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", element);
	}

	public void turnOffElementHighlight(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid white'", element);
	}

	public void focusElement(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView({behavior: 'auto', block: 'end', inline: 'nearest'});", element);

	}

	public void scrollIntoView(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public void turnOffImplicitWaits(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	public void turnOnImplicitWaits(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	public void setImplicitWaits(WebDriver driver, int timeinSeconds) {
		driver.manage().timeouts().implicitlyWait(timeinSeconds, TimeUnit.SECONDS);
	}

	public void killAllWebDrivers() {
		try {
			String cmd = null;

			switch (ListenerFactory.operatingSystem) {
			case "windows":
				cmd = "taskkill /F /IM chromedriver.exe";
				break;
			case "linux":
				cmd = "pkill -9 chromedriver";
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + ListenerFactory.operatingSystem);
			}

			Runtime runTime = Runtime.getRuntime();
			Process process = runTime.exec(cmd);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				System.err.println("Error while killing web drivers");
				System.err.println(e.getMessage());
			}
			BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = buf.readLine()) != null) {
				System.out.println(line);
			}

			/*
			 * Iterator<WebDriver> webDriversStillActive =
			 * WebDriverFactory.webDriverList.iterator();
			 * while(webDriversStillActive.hasNext()) { WebDriver webDriverToClose =
			 * webDriversStillActive.next();
			 * webDriverFactory.closeWebDriver(webDriverToClose);
			 * //WebDriverFactory.webDriverList.remove(webDriverToClose);
			 * //System.out.println("Headful web driver window "+webDriverToClose.getTitle()
			 * +" closed."); }
			 */

		} catch (IOException e) {
			System.err.println("Error while killing web drivers");
			System.err.println(e.getMessage());
		}
	}

}
