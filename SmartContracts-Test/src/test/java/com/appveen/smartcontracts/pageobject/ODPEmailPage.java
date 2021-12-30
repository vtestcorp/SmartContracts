package com.appveen.smartcontracts.pageobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.UtilityFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ODPEmailPage {
	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());	 
	private JSONObject odpEmailLocator = null;
	private ReportFactory report = ListenerFactory.reportFactory;
	private UtilityFactory utility = new UtilityFactory();
	private JSONObject notifications = TriggerNotificationPage.notifications;
	private HashMap<String, ArrayList<String>> expectedRecipients = new HashMap<String, ArrayList<String>>();	
	private ODPLandingPage odpLandingPage = null;
	
	private void parseRecipients(ExtentTest step) throws Exception {			
		Iterator<Object> recipientListIterator = testData.getJSONArray("recipient_list").iterator();
		try {
			while(recipientListIterator.hasNext()) {
				JSONObject recipientList = (JSONObject) recipientListIterator.next();
				String notificationId = recipientList.getString("notification_template_id");
				expectedRecipients.put(notificationId, new ArrayList<String>());
				Iterator<Object> recipientIterator = recipientList.getJSONArray("recipients").iterator();
				while(recipientIterator.hasNext()) {
					String recipient = (String) recipientIterator.next();
					expectedRecipients.get(notificationId).add(recipient);
				}			
			}
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while parsing expected email recipients");
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while parsing expected email recipients");
		}
		
	}	
	
	private void validateEmailRecord(WebDriver driver, ExtentTest step,  String notificationId) throws Exception {
		try {
			if(notifications.getJSONObject(notificationId).has("Ref ID")) {
				WebElement RefID = driver.findElements(By.tagName("odp-view-control")).get(2).findElement(By.tagName("odp-view-separator"));
				if(RefID.getText().equals(notifications.getJSONObject(notificationId).getString("Ref ID"))) {
					utility.turnOnElementHighlight(driver, RefID);
					step.log(Status.INFO, "Ref ID - Expected: "+notifications.getJSONObject(notificationId).getString("Ref ID")+" Actual: "+RefID.getText()+" (See below image)");
					report.addStepInfoScreenshot(driver, step);
					utility.turnOffElementHighlight(driver, RefID);
				}
				else {
					utility.turnOnElementHighlight(driver, RefID);
					step.log(Status.FAIL, "Ref ID - Expected: "+notifications.getJSONObject(notificationId).getString("Ref ID")+" Actual: "+RefID.getText()+" (See below image)");
					report.addStepFailScreenshot(driver, step);
					utility.turnOffElementHighlight(driver, RefID);
				}
			}
			
			WebElement recipients = driver.findElements(By.tagName("odp-view-array")).get(0);
			utility.focusElement(driver, recipients);
			utility.turnOnElementHighlight(driver, recipients);
			step.log(Status.INFO, "Email Recipients:  (See below image)");
			report.addStepInfoScreenshot(driver, step);
			utility.turnOffElementHighlight(driver, recipients);
			for(WebElement recipient: recipients.findElements(By.tagName("odp-view-separator"))) {
				if(expectedRecipients.get(notificationId).contains(recipient.getText())) {
					step.log(Status.INFO, "Email sent to: "+recipient.getText());
					expectedRecipients.get(notificationId).remove(recipient.getText());					
				}
				else {
					step.log(Status.FAIL, "Unexpected email sent to: "+recipient.getText());
				}
			}
			
			WebElement subject = driver.findElements(By.tagName("odp-view-control")).get(5).findElement(By.tagName("odp-view-separator"));
			if(subject.getText().equals(notifications.getJSONObject(notificationId).getString("Subject"))) {
				utility.turnOnElementHighlight(driver, subject);
				step.log(Status.INFO, "Email Subject - Expected: "+notifications.getJSONObject(notificationId).getString("Subject")+" Actual: "+subject.getText()+" (See below image)");
				report.addStepInfoScreenshot(driver, step);
				utility.turnOnElementHighlight(driver, subject);
			}
			else {
				utility.turnOnElementHighlight(driver, subject);
				step.log(Status.FAIL, "Email Subject - Expected: "+notifications.getJSONObject(notificationId).getString("Subject")+" Actual: "+subject.getText()+" (See below image)");
				report.addStepFailScreenshot(driver, step);	
				utility.turnOffElementHighlight(driver, subject);
			}
			
			WebElement body = driver.findElements(By.tagName("odp-view-control")).get(6).findElement(By.tagName("odp-view-separator"));
			utility.focusElement(driver, body);
			utility.turnOnElementHighlight(driver, body);
			step.log(Status.INFO, "Email Body:  (See below image)");
			report.addStepInfoScreenshot(driver, step);
			utility.turnOffElementHighlight(driver, body);
			
			driver.findElement(By.xpath(odpEmailLocator.getString("button-Back"))).click();
			Thread.sleep(1000);
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while validating Email record for notification: "+notificationId);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while validating Email record for notification: "+notificationId);
		}
		
	}
	
	private void traverseRecords(WebDriver driver, ExtentTest step,  String notificationId) throws Exception {
		try {
			List<WebElement> results = driver.findElement(By.xpath(odpEmailLocator.getString("label-DataBody"))).findElements(By.xpath(odpEmailLocator.getString("label-DataRow")));
			int noOfRecords = results.size();
			for(int index = 0; index < noOfRecords; index++) {		
				List<WebElement> records = driver.findElement(By.xpath(odpEmailLocator.getString("label-DataBody"))).findElements(By.xpath(odpEmailLocator.getString("label-DataRow")));
				records.get(index).findElements(By.xpath(odpEmailLocator.getString("label-DataCell"))).get(1).findElement(By.tagName("a")).click();
				validateEmailRecord(driver, step, notificationId);				
				driver.findElement(By.xpath(odpEmailLocator.getString("button-FilterApply"))).click();
				Thread.sleep(1000);
			}
			driver.findElement(By.xpath(odpEmailLocator.getString("button-FilterClear"))).click();
			Thread.sleep(1000);
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while traversing Email records for notification: "+notificationId);
			report.addStepFailScreenshot(driver, step);
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while traversing Email records for notification: "+notificationId);
		}
	}
	
	public ODPEmailPage(LocatorFactory locators) {
		odpEmailLocator = locators.getLocators("ODPEmailPage");
		odpLandingPage = new ODPLandingPage(locators);
	}
	
	
	
	public void validateEmails(WebDriver driver, ExtentTest step) throws Exception {
		Iterator<String> notificationIds = notifications.keys();
		parseRecipients(step);
		while(notificationIds.hasNext()) {
			String notificationId = notificationIds.next();
			JSONObject filters = new JSONObject();			
			try {
				switch (notificationId) {
					case "10001":
						filters.put("Ref ID", notifications.getJSONObject(notificationId).getString("Ref ID"));
						filters.put("Subject", notifications.getJSONObject(notificationId).getString("Subject"));					
						break;
						
					case "100015":
						filters.put("Ref ID", notifications.getJSONObject(notificationId).getString("Ref ID"));
						filters.put("Subject", notifications.getJSONObject(notificationId).getString("Subject"));
						break;
					
					case "100017":
						filters.put("Ref ID", notifications.getJSONObject(notificationId).getString("Ref ID"));
						filters.put("Subject", notifications.getJSONObject(notificationId).getString("Subject"));
						break;
					
					default:
						break;
				}
				
				odpLandingPage.applyFilter(driver, step, filters);
				traverseRecords(driver, step, notificationId);
				if(expectedRecipients.get(notificationId).isEmpty()) {
					step.log(Status.PASS, "All emails triggered for notification: "+notificationId);
				}
				else {
					step.log(Status.FAIL, "Emails not triggered for notification: "+notificationId+"to: "+expectedRecipients.get(notificationId).toString());
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating emails for notification: "+notificationId);
				report.addStepFailScreenshot(driver, step);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while validating emails for notification: "+notificationId);
			}
		}		
	}

}
