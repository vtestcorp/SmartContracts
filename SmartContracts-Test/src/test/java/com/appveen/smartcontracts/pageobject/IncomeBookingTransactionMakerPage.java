package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class IncomeBookingTransactionMakerPage {
	 
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject incomeBookingTransactionMakerLocator = null;
	private JSONArray adhocFees = null;
	
	private void setContacts(WebDriver driver, ExtentTest step, JSONArray contactList) throws InterruptedException {
		Iterator<Object> contactIterator = contactList.iterator();
		
		while (contactIterator.hasNext()) {
			String contact = contactIterator.next().toString();
			
			try {				
				driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-ContactSearch"))).clear();
				driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-ContactSearch"))).sendKeys(contact);
				driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-ContactSearch"))).sendKeys(Keys.ENTER);
				Thread.sleep(1000);
				List<WebElement> searchResult = driver.findElements(By.id(incomeBookingTransactionMakerLocator.getString("input-ContactSearchResult")));
				Iterator<WebElement> searchResultIterator = searchResult.iterator();
				while(searchResultIterator.hasNext()) {
					WebElement result = searchResultIterator.next();
					if(!result.isSelected()) {
						result.click();
					}
				}								
				}
				catch (NoSuchElementException e) {
					step.log(Status.FAIL, "Contact: "+contact+" not found!");
					continue;
				}				
		}		
		driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("button-ContactUpdate"))).click();	
	}
	
	public IncomeBookingTransactionMakerPage(LocatorFactory locators) {
		incomeBookingTransactionMakerLocator = locators.getLocators("IncomeBookingTransactionMakerPage");				
	}
	
	public void addAdhocFee(WebDriver driver, ExtentTest step) throws Exception {
		adhocFees = testData.getJSONArray("adhoc_fees");
		Iterator<Object> adhocFeesIterator = adhocFees.iterator();
		while(adhocFeesIterator.hasNext()) {
			JSONObject adhocFee = (JSONObject) adhocFeesIterator.next();
			try {
				driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("button-AddNewIncomeBooking"))).click();
				
				driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingDealId"))).findElement(By.tagName("input")).sendKeys(NewDealPage.dealDetails.get(adhocFee.getString("adhoc_fee_deal_name").toLowerCase().trim()));
				utility.selectFromDivMenu(driver, step, driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingDealId"))).findElement(By.tagName("ul")), NewDealPage.dealDetails.get(adhocFee.getString("adhoc_fee_deal_name").toLowerCase().trim()));
				
				Thread.sleep(500);
				String actualDealName = driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingDealName"))).getText();
				if(adhocFee.getString("adhoc_fee_deal_name").equals(actualDealName)) {
					driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingSubmit"))).click();
					
					Select incomeBookingFeeMode = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingFeeMode"))));
					incomeBookingFeeMode.selectByVisibleText(adhocFee.getString("adhoc_fee_mode"));
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInstructionName"))).clear();
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInstructionName"))).sendKeys(adhocFee.getString("adhoc_fee_instruction_name"));
					
					Select incomeBookingCurrency = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingCurrency"))));
					incomeBookingCurrency.selectByVisibleText(adhocFee.getString("adhoc_fee_currency"));
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingFeeAmount"))).sendKeys(adhocFee.getString("adhoc_fee_amount"));					
					
					Select incomeBookingFeeNature = new Select(driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingFeeNature"))));
					incomeBookingFeeNature.selectByVisibleText(incomeBookingTransactionMakerLocator.getString("adhoc_fee_nature"));
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingRemarks"))).sendKeys(adhocFee.getString("scheduled_fee_remarks"));
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingBasicNext"))).click();
					
					Select incomeBookingTaxCategory = new Select(driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingTaxCategory"))).findElement(By.tagName("select")));
					incomeBookingTaxCategory.selectByVisibleText(adhocFee.getString("adhoc_fee_tax_category"));
					Select incomeBookingContributeAs = new Select(driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingContributeAs"))).findElement(By.tagName("select")));
					incomeBookingContributeAs.selectByVisibleText(adhocFee.getString("adhoc_fee_contribute_as"));				
					
					
					List<WebElement> incomeBookingPartyList =  driver.findElement(By.tagName(incomeBookingTransactionMakerLocator.getString("list-IncomeBookingParty"))).findElement(By.tagName("ul")).findElements(By.tagName("li"));
					incomeBookingPartyList.get(adhocFee.getJSONArray("adhoc_fee_parties").length()).click();
					
					Iterator<Object> incomeBookingParties = adhocFee.getJSONArray("adhoc_fee_parties").iterator();
					int noOfParties = 0;
					while(incomeBookingParties.hasNext()) {
						JSONObject incomeBookingParty = (JSONObject) incomeBookingParties.next();
						
						driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("tab-IncomeBookingParty"))).get(noOfParties).click();
						
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingParty"))).findElement(By.tagName("input")).click();
						utility.selectFromDivMenu(driver, step, driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingParty"))).findElement(By.tagName("ul")), incomeBookingParty.getString("party_name"));
						
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingFeeContribution"))).sendKeys(incomeBookingParty.getString("party_fee_contribution"));
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingTaxReferenceNo"))).sendKeys(incomeBookingParty.getString("party_tax_reference_no"));
						Select incomeBookingPartyInvoiceTo = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInvoiceTo"))).findElement(By.tagName("select")));
						incomeBookingPartyInvoiceTo.selectByVisibleText(incomeBookingParty.getString("party_invoice_to"));
						
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInvoiceDate"))).findElement(By.tagName("input")).sendKeys(incomeBookingParty.getString("party_invoice_date"));
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInvoiceDueDate"))).findElement(By.tagName("input")).sendKeys(incomeBookingParty.getString("party_invoice_due_date"));
						
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingInvoiceContacts"))).click();
						setContacts(driver, step, incomeBookingParty.getJSONArray("party_invoice_contacts"));					
						
						noOfParties++;				
					}
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingBasicNext"))).click();	
					
					Select incomeBookingFeeCreditAccountNo = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingFeeCreditAccountNo"))).findElement(By.tagName("select")));
					incomeBookingFeeCreditAccountNo.selectByVisibleText(adhocFee.getString("adhoc_fee_credit_account"));					
					Select incomeBookingTaxCreditAccountNo = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingTaxCreditAccountN"))).findElement(By.tagName("select")));
					incomeBookingTaxCreditAccountNo.selectByVisibleText(adhocFee.getString("adhoc_fee_tax_credit_account"));					
					List<WebElement> incomeBookingNarration = driver.findElements(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingNarration")));
					incomeBookingNarration.get(0).sendKeys(adhocFee.getString("adhoc_fee_narration"));
					incomeBookingNarration.get(1).sendKeys(adhocFee.getString("adhoc_fee_narration"));
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingBasicNext"))).click();
					
					driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingSubmit"))).click();
					driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("button-SubmitYes"))).click();					
				}
				else {
					step.log(Status.FAIL, "Invalid deal name: "+actualDealName+" .Expected: "+adhocFee.getString("adhoc_fee_deal_name"));
					report.addStepFailScreenshot(driver, step);
					throw new Exception("Invalid deal name: "+actualDealName+" .Expected: "+adhocFee.getString("adhoc_fee_deal_name"));
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while creating adhoc fee: "+adhocFee.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while creating adhoc fee: "+adhocFee.getString("name"));
			}
		}
	}
	
	public void editAdhocFee(WebDriver driver, ExtentTest step) throws Exception {
		adhocFees = testData.getJSONArray("edit_adhoc_fees");
		Iterator<Object> adhocFeesIterator = adhocFees.iterator();
		while(adhocFeesIterator.hasNext()) {
			JSONObject adhocFee = (JSONObject) adhocFeesIterator.next();
			try {
				String incomeBookingInstructionName = adhocFee.getString("adhoc_fee_instruction_name");
				utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingInstructionName")), 2, 120);
				driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("input-FilterText"))).get(1).sendKeys(incomeBookingInstructionName);
				driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("input-FilterText"))).get(4).sendKeys(adhocFee.getString("adhoc_fee_deal_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingInstructionName")), 2, 120);
				
				String resultInstructionName = driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingInstructionName"))).get(1).getText().trim().toLowerCase();
				
				if(resultInstructionName.equals(incomeBookingInstructionName.trim().toLowerCase())) {
					step.log(Status.INFO, "Adhoc fee: "+incomeBookingInstructionName+" available at Income Booking Transaction Maker Queue");
					driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingActions"))).get(1).findElements(By.tagName("i")).get(0).click();
					
								
					step.log(Status.INFO, "Adhoc Fee: "+incomeBookingInstructionName+" deleted");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("button-Yes"))).click();
				}
				else {
					step.log(Status.FAIL, "Adhoc Fee: "+incomeBookingInstructionName+" not available at Income Booking Transaction Maker Queue");
					report.addStepFailScreenshot(driver, step);					
					throw new Exception("Adhoc Fee: "+incomeBookingInstructionName+" not available at Income Booking Transaction Maker Queue");
				}	
					Select incomeBookingFeeMode = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingFeeMode"))));
					incomeBookingFeeMode.selectByVisibleText(adhocFee.getString("adhoc_fee_mode"));
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInstructionName"))).clear();
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInstructionName"))).sendKeys(adhocFee.getString("adhoc_fee_instruction_name"));
					
					Select incomeBookingCurrency = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingCurrency"))));
					incomeBookingCurrency.selectByVisibleText(adhocFee.getString("adhoc_fee_currency"));
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingFeeAmount"))).sendKeys(adhocFee.getString("adhoc_fee_amount"));					
					
					Select incomeBookingFeeNature = new Select(driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingFeeNature"))));
					incomeBookingFeeNature.selectByVisibleText(incomeBookingTransactionMakerLocator.getString("adhoc_fee_nature"));
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingRemarks"))).sendKeys(adhocFee.getString("scheduled_fee_remarks"));
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingBasicNext"))).click();
					
					Select incomeBookingTaxCategory = new Select(driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingTaxCategory"))).findElement(By.tagName("select")));
					incomeBookingTaxCategory.selectByVisibleText(adhocFee.getString("adhoc_fee_tax_category"));
					Select incomeBookingContributeAs = new Select(driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingContributeAs"))).findElement(By.tagName("select")));
					incomeBookingContributeAs.selectByVisibleText(adhocFee.getString("adhoc_fee_contribute_as"));				
					
					
					List<WebElement> incomeBookingPartyList =  driver.findElement(By.tagName(incomeBookingTransactionMakerLocator.getString("list-IncomeBookingParty"))).findElement(By.tagName("ul")).findElements(By.tagName("li"));
					incomeBookingPartyList.get(adhocFee.getJSONArray("adhoc_fee_parties").length()).click();
					
					Iterator<Object> incomeBookingParties = adhocFee.getJSONArray("adhoc_fee_parties").iterator();
					int noOfParties = 0;
					while(incomeBookingParties.hasNext()) {
						JSONObject incomeBookingParty = (JSONObject) incomeBookingParties.next();
						
						driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("tab-IncomeBookingParty"))).get(noOfParties).click();
						
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingParty"))).findElement(By.tagName("input")).click();
						utility.selectFromDivMenu(driver, step, driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingParty"))).findElement(By.tagName("ul")), incomeBookingParty.getString("party_name"));
						
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingFeeContribution"))).sendKeys(incomeBookingParty.getString("party_fee_contribution"));
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingTaxReferenceNo"))).sendKeys(incomeBookingParty.getString("party_tax_reference_no"));
						Select incomeBookingPartyInvoiceTo = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInvoiceTo"))).findElement(By.tagName("select")));
						incomeBookingPartyInvoiceTo.selectByVisibleText(incomeBookingParty.getString("party_invoice_to"));
						
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInvoiceDate"))).findElement(By.tagName("input")).sendKeys(incomeBookingParty.getString("party_invoice_date"));
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingInvoiceDueDate"))).findElement(By.tagName("input")).sendKeys(incomeBookingParty.getString("party_invoice_due_date"));
						
						driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingInvoiceContacts"))).click();
						setContacts(driver, step, incomeBookingParty.getJSONArray("party_invoice_contacts"));					
						
						noOfParties++;				
					}
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingBasicNext"))).click();	
					
					Select incomeBookingFeeCreditAccountNo = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingFeeCreditAccountNo"))).findElement(By.tagName("select")));
					incomeBookingFeeCreditAccountNo.selectByVisibleText(adhocFee.getString("adhoc_fee_credit_account"));					
					Select incomeBookingTaxCreditAccountNo = new Select(driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("select-IncomeBookingTaxCreditAccountN"))).findElement(By.tagName("select")));
					incomeBookingTaxCreditAccountNo.selectByVisibleText(adhocFee.getString("adhoc_fee_tax_credit_account"));					
					List<WebElement> incomeBookingNarration = driver.findElements(By.id(incomeBookingTransactionMakerLocator.getString("input-IncomeBookingNarration")));
					incomeBookingNarration.get(0).sendKeys(adhocFee.getString("adhoc_fee_narration"));
					incomeBookingNarration.get(1).sendKeys(adhocFee.getString("adhoc_fee_narration"));
					
					driver.findElement(By.id(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingBasicNext"))).click();
					
					driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("button-IncomeBookingSubmit"))).click();
					driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("button-SubmitYes"))).click();					
				
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while creating adhoc fee: "+adhocFee.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while creating adhoc fee: "+adhocFee.getString("name"));
			}
		}
	}
	
	
	public void deleteAdhocFee(WebDriver driver, ExtentTest step) throws Exception {
		adhocFees = testData.getJSONArray("delete_adhoc_fees");
		Iterator<Object> adhocFeesIterator = adhocFees.iterator();
		while(adhocFeesIterator.hasNext()) {
			JSONObject adhocFee = (JSONObject) adhocFeesIterator.next();
			try {
				String incomeBookingInstructionName = adhocFee.getString("adhoc_fee_instruction_name");
				utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingInstructionName")), 2, 120);
				driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("input-FilterText"))).get(1).sendKeys(incomeBookingInstructionName);
				driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("input-FilterText"))).get(4).sendKeys(adhocFee.getString("adhoc_fee_deal_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingInstructionName")), 2, 120);
				
				String resultInstructionName = driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingInstructionName"))).get(1).getText().trim().toLowerCase();
				
				if(resultInstructionName.equals(incomeBookingInstructionName.trim().toLowerCase())) {
					step.log(Status.INFO, "Adhoc fee: "+incomeBookingInstructionName+" available at Income Booking Transaction Maker Queue");
					driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingActions"))).get(1).findElements(By.tagName("i")).get(1).click();
													
					step.log(Status.INFO, "Adhoc Fee: "+incomeBookingInstructionName+" deleted");
					report.addStepInfoScreenshot(driver, step);
					driver.findElement(By.xpath(incomeBookingTransactionMakerLocator.getString("button-Yes"))).click();
				}
				else {
					step.log(Status.FAIL, "Adhoc Fee: "+incomeBookingInstructionName+" not available at Income Booking Transaction Maker Queue");
					report.addStepFailScreenshot(driver, step);					
					throw new Exception("Adhoc Fee: "+incomeBookingInstructionName+" not available at Income Booking Transaction Maker Queue");
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while deleting adhoc fee: "+adhocFee.getString("name"));
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while deleting adhoc fee: "+adhocFee.getString("name"));
			}
		}
	}
	
	public void checkFeesAvailable(WebDriver driver, ExtentTest step) throws Exception {
		adhocFees = testData.getJSONArray("adhoc_fees");
		Iterator<Object> adhocFeesIterator = adhocFees.iterator();
		while(adhocFeesIterator.hasNext()) {
			JSONObject adhocFee = (JSONObject) adhocFeesIterator.next();
			try {
				String incomeBookingInstructionName = adhocFee.getString("adhoc_fee_instruction_name");
				utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingInstructionName")), 2, 120);
				driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("input-FilterText"))).get(1).sendKeys(incomeBookingInstructionName);
				driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("input-FilterText"))).get(4).sendKeys(adhocFee.getString("adhoc_fee_deal_name"));
				utility.waitForElementToBeDisplayed(driver, By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingInstructionName")), 2, 120);
				
				String resultInstructionName = driver.findElements(By.xpath(incomeBookingTransactionMakerLocator.getString("column-IncomeBookingInstructionName"))).get(1).getText().trim().toLowerCase();
				
				if(resultInstructionName.equals(incomeBookingInstructionName.trim().toLowerCase())) {
					step.log(Status.PASS, "Adhoc fee: "+incomeBookingInstructionName+" available at Income Booking Transaction Maker Queue");
					report.addStepPassScreenshot(driver, step);
				}
				else {
					step.log(Status.FAIL, "Adhoc Fee: "+incomeBookingInstructionName+" not available at Income Booking Transaction Maker Queue");
					report.addStepFailScreenshot(driver, step);					
					throw new Exception("Adhoc Fee: "+incomeBookingInstructionName+" not available at Income Booking Transaction Maker Queue");
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while checking fee: "+adhocFee.getString("name")+" available");
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while checking fee: "+adhocFee.getString("name")+" available");
			}
		}
	}
}
