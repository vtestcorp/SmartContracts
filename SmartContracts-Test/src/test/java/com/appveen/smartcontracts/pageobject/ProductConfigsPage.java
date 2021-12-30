package com.appveen.smartcontracts.pageobject;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;



public class ProductConfigsPage {
	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private JSONArray products = null;
	private JSONObject productConfigsLocator = null;
	private JSONObject product = null;
	private ReportFactory report = ListenerFactory.reportFactory;
	private AddProductConfigPage addProductConfigPage = null;
	private List<String> productNames = new ArrayList<String>();
	
	private void getProductList(WebDriver driver, ExtentTest step) throws Exception {
		try {
		Thread.sleep(500);
		productNames.clear();
		List<WebElement> productElementList = driver.findElements(By.xpath(productConfigsLocator.getString("table-ProductName")));
		for (WebElement webElement : productElementList) {
			productNames.add(webElement.getText());
		}	
		step.log(Status.INFO, "List of Products Fetched");
	}
	catch (Exception e) {
		step.log(Status.FAIL, "Error while fetching Product Names");
		report.addStepFailScreenshot(driver, step);
		step.log(Status.FAIL, e.getMessage());
		throw new Exception("Error while fetching Product Names");
	}
	}	
	
	private void editProduct(WebDriver driver, ExtentTest step,  String productName) throws Exception {
		try {
			List<WebElement> editProductList = driver.findElements(By.xpath(productConfigsLocator.getString("link-editProduct")));
			int indexOfProduct = productNames.indexOf(productName);
			editProductList.get(indexOfProduct).click();
			step.log(Status.INFO, "Product "+productName+" picked up for Edit");
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while clicking Product Edit");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while clicking Product Edit");
		}
	}
	
	
	public ProductConfigsPage(LocatorFactory locators) {		
		productConfigsLocator = locators.getLocators("ProductConfigsPage");			
		addProductConfigPage = new AddProductConfigPage(locators);			
	}

	public void addNewProduct(WebDriver driver, ExtentTest step) throws Exception {
		products = testData.getJSONArray("products");
		Iterator<Object> productIterator = products.iterator();
		while(productIterator.hasNext()) {
			product = (JSONObject) productIterator.next();	
			try {
				driver.findElement(By.xpath(productConfigsLocator.getString("button-AddNew"))).click();
				step.log(Status.INFO, "Moved to Add New Product Page");
				Thread.sleep(500);
				addProductConfigPage.createProduct(driver, step, product);
			}

			catch (Exception e) {
				step.log(Status.FAIL, "Error while moving to Add New Product");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while moving to Add New Product");
			}
		}
	}
	
	public boolean checkAddedProductList(WebDriver driver, ExtentTest step) throws Exception {
		try {
			products = testData.getJSONArray("products");
			Iterator<Object> productIterator = products.iterator();
			driver.findElement(By.xpath(productConfigsLocator.getString("tab-AdminChecker"))).click();
			Thread.sleep(500);
			getProductList(driver, step);
			while(productIterator.hasNext()) {
				product = (JSONObject) productIterator.next();	
				if(!productNames.contains(product.get("name"))) {
					return false;
				}
			}
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while checking products created");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while checking products created");
		}
		return true;
	}
	
	public boolean checkValidatedProductList(WebDriver driver, ExtentTest step) throws Exception {
		try {
			products = testData.getJSONArray("products");
			Iterator<Object> productIterator = products.iterator();
			driver.findElement(By.xpath(productConfigsLocator.getString("tab-AdminChecker"))).click();
			Thread.sleep(500);
			getProductList(driver, step);			
			while(productIterator.hasNext()) {
				product = (JSONObject) productIterator.next();	
				if(productNames.contains(product.get("name"))) {
					return false;
				}
			}
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while checking products approved");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while checking products approved");
		}
		return true;
	}
	
	public void validateProduct(WebDriver driver, ExtentTest step) throws Exception {		
		products = testData.getJSONArray("products");
		Iterator<Object> productIterator = products.iterator();
		
		while(productIterator.hasNext()) {				
			product = (JSONObject) productIterator.next();
			try {
				step.log(Status.INFO, "Product Name - " + product.getString("name"));
				
				driver.findElement(By.xpath(productConfigsLocator.getString("tab-AdminChecker"))).click();
				step.log(Status.INFO, "Moved to Product Configs -> Admin Checker tab");
				Thread.sleep(500);
				getProductList(driver, step);
				editProduct(driver, step, product.getString("name"));
				step.log(Status.INFO, "Moved to Add Product Config for Product Approval");
				addProductConfigPage.approveProduct(driver, step, product);			
			}	
			catch (Exception e) {
				step.log(Status.FAIL, "Error while moving to Product Config for Product Approval");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while moving to Product Config for Product Approval");
			}
		}
	}
}
