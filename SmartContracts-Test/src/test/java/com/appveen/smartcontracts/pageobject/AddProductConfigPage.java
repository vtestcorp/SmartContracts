package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;
import java.util.List;

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

public class AddProductConfigPage {
	
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject addProductConfigLocator = null;	
	private Integer dropdownIndex = -1;
	private Integer checkboxIndex = -1;

	private void setTransactionLimit(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {
		try {
			int incrementer = 0;
			if(product.has("transaction_limits")) {
				Iterator<Object> transactionLimitIterator = product.getJSONArray("transaction_limits").iterator();
				WebElement buttonAddTransactionLimit = driver.findElements(By.xpath(addProductConfigLocator.getString("button-plusicon"))).get(0);
				
				while(transactionLimitIterator.hasNext()) {
					JSONObject transactionLimit = (JSONObject) transactionLimitIterator.next();
					
					dropdownIndex += 1;
					Select transactionLimitCurrency = new Select(driver.findElements(By.xpath(addProductConfigLocator.getString("select-Dropdown"))).get(dropdownIndex));
					transactionLimitCurrency.selectByVisibleText(transactionLimit.getString("transaction_limit_currency"));
					
					String transactionLimitAmountLocator = addProductConfigLocator.getString("input-TransactionLimitAmount")+incrementer;
					driver.findElement(By.id(transactionLimitAmountLocator)).sendKeys(transactionLimit.getString("transaction_limit_amount"));
					incrementer += 1;
					
					if(transactionLimitIterator.hasNext()) {
						buttonAddTransactionLimit.click();
					}
				}			
			}
			else {
				dropdownIndex += 1;
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while creating transaction limit");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while creating transaction limit");
		}
		
		
	}	
	
	private void setAttributes(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {
		try {
			if(product.has("attributes")) {			
				Iterator<Object> attributeIterator = product.getJSONArray("attributes").iterator();
				while(attributeIterator.hasNext()) {
					JSONObject attribute = (JSONObject) attributeIterator.next();
					
					driver.findElement(By.xpath(addProductConfigLocator.getString("button-CreateAttribute"))).click();
					driver.findElement(By.xpath(addProductConfigLocator.getString("input-AttributeName"))).sendKeys(attribute.getString("attribute_name"));
					driver.findElement(By.xpath(addProductConfigLocator.getString("input-AttributeDescription"))).sendKeys(attribute.getString("attribute_description"));
					Select attributeTypeSelect = new Select(driver.findElement(By.xpath(addProductConfigLocator.getString("select-Attribute"))));
					attributeTypeSelect.selectByVisibleText(attribute.getString("attribute_type"));
					
					if(!attribute.getBoolean("attribute_mandatory")) {					
						driver.findElement(By.id(addProductConfigLocator.getString("checkbox-AttributeMandatory"))).click();
					}
					
					driver.findElements(By.xpath(addProductConfigLocator.getString("button-AttributeSave"))).get(1).click();
				}
			}
		}catch (Exception e) {
			step.log(Status.FAIL, "Error while creating product attribute");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while creating product attribute");
		}
		
		
	}
	
	private void setPurposes(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {
		try {
			if(product.has("purposes")) {			
				Iterator<Object> purposeIterator = product.getJSONArray("purposes").iterator();
				Integer incrementer = 1;
				WebElement buttonAddPurpose = driver.findElements(By.xpath(addProductConfigLocator.getString("button-plusicon"))).get(1);
					
				while(purposeIterator.hasNext()) {
					String purposeLocator = addProductConfigLocator.getString("input-Purpose").substring(0, 29)+incrementer.toString()+addProductConfigLocator.getString("input-Purpose").substring(30);
					driver.findElement(By.xpath(purposeLocator)).sendKeys(purposeIterator.next().toString());	
					Thread.sleep(500);				
					incrementer += 1;
					if(purposeIterator.hasNext()) {
						buttonAddPurpose.click();
					}
				}				
			}
		}catch (Exception e) {
			step.log(Status.FAIL, "Error while creating product purposes");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while creating product purposes");
		}
		
		
	}
	
	private void setDocuments(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {
		try {
		
		if(product.has("documents")) {			
			Iterator<Object> documentIterator = product.getJSONArray("documents").iterator();			
			WebElement buttonAddDocument = driver.findElements(By.xpath(addProductConfigLocator.getString("button-plusicon"))).get(2);
			while(documentIterator.hasNext()) {
				JSONObject document = (JSONObject) documentIterator.next();
				dropdownIndex += 1;
				Select documentName = new Select(driver.findElements(By.xpath(addProductConfigLocator.getString("select-Dropdown"))).get(dropdownIndex));
				dropdownIndex += 1;
				Select documentType = new Select(driver.findElements(By.xpath(addProductConfigLocator.getString("select-Dropdown"))).get(dropdownIndex));
				
				documentName.selectByVisibleText(document.getString("document_name"));
				Thread.sleep(500);
				documentType.selectByVisibleText(document.getString("document_type"));
				
				if(documentIterator.hasNext()) {
					buttonAddDocument.click();
				}
			}
		}
		else {
			dropdownIndex += 2;
		}
		}catch (Exception e) {
			step.log(Status.FAIL, "Error while adding product documents");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding product documents");
		}
				
	}
	
	private void setApprovalDocuments(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {
		try {
		if(product.has("approval_documents")) {			
			Iterator<Object> approvalDocumentIterator = product.getJSONArray("approval_documents").iterator();			
			WebElement buttonAddApprovalDocument = driver.findElements(By.xpath(addProductConfigLocator.getString("button-plusicon"))).get(3);
			while(approvalDocumentIterator.hasNext()) {
				JSONObject approvalDocument = (JSONObject) approvalDocumentIterator.next();
				dropdownIndex += 1;
				Select approvalDocumentName = new Select(driver.findElements(By.xpath(addProductConfigLocator.getString("select-Dropdown"))).get(dropdownIndex));
				dropdownIndex += 1;
				Select approvalDocumentType = new Select(driver.findElements(By.xpath(addProductConfigLocator.getString("select-Dropdown"))).get(dropdownIndex));
				
				approvalDocumentName.selectByVisibleText(approvalDocument.getString("document_name"));
				Thread.sleep(500);
				approvalDocumentType.selectByVisibleText(approvalDocument.getString("document_type"));
				
				if(approvalDocumentIterator.hasNext()) {
					buttonAddApprovalDocument.click();
				}
			}			
		}
		else {
			dropdownIndex += 2;
		}
		}catch (Exception e) {
			step.log(Status.FAIL, "Error while adding product approval documents");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding product approval documents");
		}
				
	}
	
	private void setTransactionChecklist(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {
		try {
		if(product.has("transaction_checklist")) {			
			Iterator<Object> transactionChecklistIterator = product.getJSONArray("transaction_checklist").iterator();
			Integer incrementer = 1;
			WebElement buttonAddTransactionChecklist = driver.findElements(By.xpath(addProductConfigLocator.getString("button-plusicon"))).get(4);
				
			while(transactionChecklistIterator.hasNext()) {
				JSONObject transactionChecklist = (JSONObject) transactionChecklistIterator.next();				
				String transactionChecklistLocator = addProductConfigLocator.getString("input-TransactionChecklist").substring(0, 33)+incrementer.toString()+addProductConfigLocator.getString("input-TransactionChecklist").substring(34);
				
				buttonAddTransactionChecklist.click();
				
				driver.findElement(By.xpath(transactionChecklistLocator)).sendKeys(transactionChecklist.getString("checklist_name"));
				dropdownIndex += 1;
				Select transactionChecklistRole = new Select(driver.findElements(By.xpath(addProductConfigLocator.getString("select-Dropdown"))).get(dropdownIndex));
				transactionChecklistRole.selectByVisibleText(transactionChecklist.getString("checklist_role"));
				checkboxIndex += 1;
				if(transactionChecklist.getBoolean("checklist_mandatory")) {
					driver.findElements(By.name(addProductConfigLocator.getString("checkbox-TransactionChecklistMandatory"))).get(checkboxIndex).click();
				}				
				incrementer += 1;				
			}			
		}
		else {
			dropdownIndex += 1;
			checkboxIndex += 1;
		}
		}catch (Exception e) {
			step.log(Status.FAIL, "Error while creating Product Transaction Checklist");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while creating Product Transaction Checklist");
		}
		
		
	}
	
	private void setDealChecklist(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {
		try {
		if(product.has("deal_checklist")) {			
			Iterator<Object> dealChecklistIterator = product.getJSONArray("deal_checklist").iterator();
			Integer incrementer = 1;
			WebElement buttonAddDealChecklist = driver.findElements(By.xpath(addProductConfigLocator.getString("button-plusicon"))).get(5);
				
			while(dealChecklistIterator.hasNext()) {
				JSONObject dealChecklist = (JSONObject) dealChecklistIterator.next();				
				String dealChecklistLocator = addProductConfigLocator.getString("input-TransactionChecklist").substring(0, 33)+incrementer.toString()+addProductConfigLocator.getString("input-TransactionChecklist").substring(34);
				buttonAddDealChecklist.click();
				
				List<WebElement> checklistElements = driver.findElements(By.xpath(dealChecklistLocator));			
				if(checklistElements.size() == 2) {
					checklistElements.get(1).sendKeys(dealChecklist.getString("checklist_name"));
				}
				else if(checklistElements.size() == 1){
					checklistElements.get(0).sendKeys(dealChecklist.getString("checklist_name"));
				}
				dropdownIndex += 1;
				Select dealChecklistRole = new Select(driver.findElements(By.xpath(addProductConfigLocator.getString("select-Dropdown"))).get(dropdownIndex));
				dealChecklistRole.selectByVisibleText(dealChecklist.getString("checklist_role"));
				checkboxIndex += 1;
				if(dealChecklist.getBoolean("checklist_mandatory")) {
					driver.findElements(By.name(addProductConfigLocator.getString("checkbox-TransactionChecklistMandatory"))).get(checkboxIndex).click();
				}			
				incrementer += 1;
			}			
		}
		else {
			dropdownIndex += 1;
			checkboxIndex += 1;
		}
		}catch (Exception e) {
			step.log(Status.FAIL, "Error while creating Product Deal Checklist");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while creating Product Deal Checklist");
		}
		
		
	}
	
	private void setExpiryDocuments(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {
		try {
		if(product.has("expiry_documents")) {			
			Iterator<Object> expiryDocumentIterator = product.getJSONArray("expiry_documents").iterator();			
			WebElement buttonAddExpiryDocument = driver.findElements(By.xpath(addProductConfigLocator.getString("button-plusicon"))).get(6);
			while(expiryDocumentIterator.hasNext()) {
				JSONObject expiryDocument = (JSONObject) expiryDocumentIterator.next();
				dropdownIndex += 1;
				Select expiryDocumentName = new Select(driver.findElements(By.xpath(addProductConfigLocator.getString("select-Dropdown"))).get(dropdownIndex));
				dropdownIndex += 1;
				Select expiryDocumentType = new Select(driver.findElements(By.xpath(addProductConfigLocator.getString("select-Dropdown"))).get(dropdownIndex));
				
				expiryDocumentName.selectByVisibleText(expiryDocument.getString("document_name"));
				Thread.sleep(500);
				expiryDocumentType.selectByVisibleText(expiryDocument.getString("document_type"));
				
				if(expiryDocumentIterator.hasNext()) {
					buttonAddExpiryDocument.click();
				}
			}			
			step.log(Status.INFO, "Product Expiry Documents added: "+product.getJSONArray("expiry_documents").toString());
		}
		else {
			dropdownIndex += 2;
		}
		}catch (Exception e) {
			step.log(Status.FAIL, "Error while adding Product Expiry Documents");
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while adding Product Expiry Documents");
		}
				
	}
	

	
	public AddProductConfigPage(LocatorFactory locators){				
		addProductConfigLocator = locators.getLocators("AddProductConfigPage");	
	}
	
	public void createProduct(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {		
		try {			
			step.log(Status.INFO, "Product Name - " + product.getString("name"));
			driver.findElement(By.id(addProductConfigLocator.getString("input-Name"))).sendKeys(product.getString("name"));
			driver.findElement(By.id(addProductConfigLocator.getString("input-Description"))).sendKeys(product.getString("description"));
			setTransactionLimit(driver, step, product);
			
			
			driver.findElements(By.id(addProductConfigLocator.getString("input-Dropdown"))).get(0).click();		
			utility.selectFromDivMenu(driver, step, driver.findElements(By.id(addProductConfigLocator.getString("list-Dropdown"))).get(0), product.getJSONArray("select_tabs"));
			
			driver.findElement(By.xpath(addProductConfigLocator.getString("label-SelectTabs"))).click();
			driver.findElements(By.id(addProductConfigLocator.getString("input-Dropdown"))).get(1).click();			
			utility.selectFromDivMenu(driver, step, driver.findElements(By.id(addProductConfigLocator.getString("list-Dropdown"))).get(1), product.getJSONArray("select_scheduled_instructions"));
			
			driver.findElement(By.xpath(addProductConfigLocator.getString("label-ScheduleInstructionTypes"))).click();
			driver.findElements(By.id(addProductConfigLocator.getString("input-Dropdown"))).get(2).click();	
			utility.selectFromDivMenu(driver, step, driver.findElements(By.id(addProductConfigLocator.getString("list-Dropdown"))).get(2), product.getJSONArray("select_linked_instructions"));
			
			driver.findElement(By.xpath(addProductConfigLocator.getString("label-LinkedInstructionTypes"))).click();
			driver.findElements(By.id(addProductConfigLocator.getString("input-Dropdown"))).get(3).click();	
			utility.selectFromDivMenu(driver, step, driver.findElements(By.id(addProductConfigLocator.getString("list-Dropdown"))).get(3), product.getJSONArray("select_fee_instructions"));
			
			driver.findElement(By.xpath(addProductConfigLocator.getString("label-FeeInstructionTypes"))).click();
			driver.findElements(By.id(addProductConfigLocator.getString("input-Dropdown"))).get(4).click();	
			utility.selectFromDivMenu(driver, step, driver.findElements(By.id(addProductConfigLocator.getString("list-Dropdown"))).get(4), product.getJSONArray("select_notifications"));
			
			
			setAttributes(driver, step, product);
			setPurposes(driver, step, product);
			setDocuments(driver, step, product);
			setApprovalDocuments(driver, step, product);
			setTransactionChecklist(driver, step, product);
			setDealChecklist(driver, step, product);
			setExpiryDocuments(driver, step, product);
			
			//if(product.getBoolean("introductory_mail")) {
			//driver.findElement(By.xpath(locatorsResponse.getString("checkbox-IntroductoryMail"))).click();
			//}
			
			driver.findElement(By.xpath(addProductConfigLocator.getString("button-ProductSubmit"))).click();
			Thread.sleep(3000);
			step.log(Status.INFO, "Product "+product.getString("name")+" submitted for validation");
			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while creating product: "+product.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());			
			throw new Exception("Error while creating product: "+product.getString("name"));
		}
	}
	
	public void approveProduct(WebDriver driver, ExtentTest step, JSONObject product) throws Exception {
		try {
			Thread.sleep(500);
			String productName = driver.findElement(By.id(addProductConfigLocator.getString("input-Name"))).getAttribute("value").toString();
			
			if(productName.equals(product.getString("name"))) {			
				driver.findElement(By.xpath(addProductConfigLocator.getString("input-AddComment"))).sendKeys("Approved");
				driver.findElement(By.xpath(addProductConfigLocator.getString("button-ProductApprove"))).click();
				Thread.sleep(500);
				driver.findElement(By.xpath(addProductConfigLocator.getString("button-ProductApproveOK"))).click();
				step.log(Status.INFO, "Product: "+productName+" approved");
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while approving product: "+product.getString("name"));
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());			
			throw new Exception("Error while approving product: "+product.getString("name"));
		}
		
	}

}
