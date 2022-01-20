package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class TaxCategriePage {
	JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private JSONObject taxCategrieLocator = null;
	private ReportFactory report = ListenerFactory.reportFactory;
	private JSONArray tax = null;
	private UtilityFactory utility = new UtilityFactory();

	public TaxCategriePage(LocatorFactory locators) {
		taxCategrieLocator = locators.getLocators("TaxCategriePage");
	}

	public void addTaxCategory(WebDriver driver, ExtentTest step) throws Exception {
		tax = testData.getJSONArray("tax");
		Iterator<Object> dealIterator = tax.iterator();
		while (dealIterator.hasNext()) {
			JSONObject tax = (JSONObject) dealIterator.next();
			try {
				driver.findElement(By.xpath(taxCategrieLocator.getString("card-Adminmaker"))).click();
				driver.findElement(By.xpath(taxCategrieLocator.getString("card-AddNew"))).click();
				driver.findElement(By.id(taxCategrieLocator.getString("tax-Country"))).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath(taxCategrieLocator.getString("country-india"))).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-Name"))).sendKeys(tax.getString("Name"));
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-Label")))
						.sendKeys(tax.getString("Label"));
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-Code"))).sendKeys(tax.getString("Code"));
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-Percentage")))
						.sendKeys(tax.getString("Percentage"));
				driver.findElement(By.id(taxCategrieLocator.getString("tax-Accounts"))).findElement(By.tagName("input"))
						.click();
				utility.selectFromDivMenu(driver, step,
						driver.findElement(By.id(taxCategrieLocator.getString("tax-Accounts")))
								.findElements(By.tagName("ul")).get(1),
						tax.getJSONArray("tax_category"));
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-DefaultAccount"))).click();
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-SelectDefaultAccoun"))).click();
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-SubCategories"))).click();
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-SubCategoriesName")))
						.sendKeys(tax.getString("SubCategoriesName"));
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-SubCategoriescode")))
						.sendKeys(tax.getString("SubCategoriesCode"));
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-SubCategoriesPercentage")))
						.sendKeys(tax.getString("SubCategoriesPercentage"));
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-Submit"))).click();

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while creating Accounts");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while creating Accounts");

			}

		}
	}

	public void addTaxCategoryAdminCheker(WebDriver driver, ExtentTest step) throws Exception {
		tax = testData.getJSONArray("tax");
		Iterator<Object> dealIterator = tax.iterator();
		while (dealIterator.hasNext()) {
			JSONObject tax = (JSONObject) dealIterator.next();
			try {
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-card-AdminCheker"))).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-ManageButton"))).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-note"))).sendKeys(tax.getString("note"));
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-Approve"))).click();
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-OkButton"))).click();

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while creating Accounts");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while creating Accounts");

			}

		}
	}

	public void TaxategoryShouldDisplayed(WebDriver driver, ExtentTest step) throws Exception {
		tax = testData.getJSONArray("tax");
		Iterator<Object> dealIterator = tax.iterator();
		while (dealIterator.hasNext()) {
			JSONObject tax = (JSONObject) dealIterator.next();
			try {
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-taxCategoriesName"))).isDisplayed();
				driver.findElement(By.xpath(taxCategrieLocator.getString("tax-taxCategoriesName"))).getText();
				report.addStepPassScreenshot(driver, step);

			} catch (Exception e) {
				step.log(Status.FAIL, "Error while creating Accounts");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while creating Accounts");

			}

		}
	}
}
