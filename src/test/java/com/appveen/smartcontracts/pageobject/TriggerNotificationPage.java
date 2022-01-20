package com.appveen.smartcontracts.pageobject;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.MongoFactory;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class TriggerNotificationPage {
	
	private JSONObject testData = ListenerFactory.dataFactory.getTestData(Thread.currentThread().getName());
	private MongoFactory mongoFactory = null;
	
	private NewDealPage newDealPage = null;
	private LoginPage loginPage = null;
	private LandingPage landingPage = null;
	private LiveDealsPage liveDealsPage = null;
	private TransactionCheckerPage transactionCheckerPage = null;
	
	public static JSONObject notifications = new JSONObject();	
	private JSONArray triggers = null;
	
	private boolean verifyNotificationEnabled(ExtentTest step, JSONObject notificationTemplate, String country) throws Exception {
		try {
			if(!notificationTemplate.getJSONObject("_metadata").getBoolean("deleted")) {
				if(notificationTemplate.getBoolean("enable")) {
					Iterator<Object> templateIterator = notificationTemplate.getJSONArray("templates").iterator();
					
					while(templateIterator.hasNext()) {
						JSONObject template = (JSONObject) templateIterator.next();
						if(template.getString("country").equals(country)) {
							if(template.getBoolean("enable")) {
								JSONObject notification = new JSONObject();							
								notification.put("Subject", notificationTemplate.getJSONArray("templates").getJSONObject(0).getString("subject"));
								
								notifications.put(notificationTemplate.getString("_id"), notification);
								
								return true;
							}
							else {
								return false;
							}
						}						
					}
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}			
		}
		catch (Exception e) {
			step.log(Status.FAIL, "Error while verifying if notification template: "+notificationTemplate.getString("_id")+" is enabled");
			step.log(Status.FAIL, e.getMessage());
			throw new Exception("Error while verifying if notification template: "+notificationTemplate.getString("_id")+" is enabled");
		}
		return false;
	}
	
	private void validateNotificationTemplates(ExtentTest step, JSONArray notificationTemplateIds) throws Exception {
		Iterator<Object> notificationTemplateIdIterator = notificationTemplateIds.iterator();
		JSONObject notificationTemplate = null;
		while (notificationTemplateIdIterator.hasNext()) {
			JSONObject notification = (JSONObject) notificationTemplateIdIterator.next();
			String notificationTemplateId = notification.getString("notification_template_id");
			try {
				notificationTemplate = mongoFactory.findOneFromCollection("notification.template", "_id", notificationTemplateId);
				if(!notificationTemplate.isEmpty()) {					
					if(verifyNotificationEnabled(step, notificationTemplate, notification.getString("notification_template_country"))) {						
						step.log(Status.PASS, "Notification template: "+notificationTemplateId+" is present and enabled in notification.template collection");
					}
					else {
						step.log(Status.FAIL, "Notification template: "+notificationTemplateId+" is disabled");
					}
				}
				else {
					step.log(Status.FAIL, "Notification template: "+notificationTemplateId+" not present in notification.template collection");
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating notification template: "+notificationTemplateId);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while validating notification template: "+notificationTemplateId);
			}			
		}		
	}
	
	private void validateWebHooks(ExtentTest step, JSONArray webhookIds) throws Exception {
		Iterator<Object> webhookIDIterator = webhookIds.iterator();
		JSONObject webhook = null;
		while (webhookIDIterator.hasNext()) {
			String webhookId = (String) webhookIDIterator.next();
			try {
				webhook = mongoFactory.findOneFromCollection("webhook.hook", "_id", webhookId);
				if(!webhook.isEmpty()) {					
					step.log(Status.PASS, "Webhook: "+webhookId+" present in webhook.hook collection");
				}
				else {
					step.log(Status.FAIL, "Webhook: "+webhookId+" not present in webhook.hook collection");
				}
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while validating webhook: "+webhookId);
				step.log(Status.FAIL, e.getMessage());
				throw new Exception("Error while validating webhook: "+webhookId);
			}			
		}
	}	
	
	public TriggerNotificationPage(LocatorFactory locators) {
		loginPage = new LoginPage(locators);
		landingPage = new LandingPage(locators);
		newDealPage = new NewDealPage(locators);
		liveDealsPage = new LiveDealsPage(locators);
		transactionCheckerPage = new TransactionCheckerPage(locators);
	}
	
	public void validateDatabase(ExtentTest step) throws Exception {
		mongoFactory = new MongoFactory();
		if(testData.has("notification_template_ids")) {
			validateNotificationTemplates(step, testData.getJSONArray("notification_template_ids"));
		}
		if(testData.has("webhook_ids")) {
			validateWebHooks(step, testData.getJSONArray("webhook_ids"));
		}
	}
	
	public void triggerNotifications(WebDriver driver, ExtentTest step) throws Exception {
		JSONObject deal = null;
		JSONObject transaction = null;
		String dealId = null;
		String dealName = null;		
		
		triggers = testData.getJSONArray("triggers");		
		Iterator<Object> triggerIterator = triggers.iterator();	
		while(triggerIterator.hasNext()) {
			JSONObject trigger = (JSONObject) triggerIterator.next();	
			try {	
				switch (trigger.getString("notification_template_id")) {
				case "10001":
					loginPage.performLogin(driver, step, "dealmaker");
					deal = trigger.getJSONArray("deals").getJSONObject(0);
					newDealPage.createDeal(driver, step, deal);
					notifications.getJSONObject("10001").put("Ref ID", NewDealPage.dealDetails.get(deal.getString("name").toLowerCase().trim()));
					landingPage.performLogout(driver, step);
					break;
					
				case "100015":
					loginPage.performLogin(driver, step, "transactionmaker");
					transaction = trigger.getJSONArray("transactions").getJSONObject(0);
					liveDealsPage.addTransaction(driver, step, transaction);
					dealName = transaction.getString("deal_name").toLowerCase().trim();
					dealId = (DealCheckerPage.dealDetails.get(dealName) == null) ? transaction.getString("deal_id") : DealCheckerPage.dealDetails.get(dealName);
					notifications.getJSONObject("100015").put("Ref ID", dealId);
					landingPage.performLogout(driver, step);
					
					loginPage.performLogin(driver, step, "transactionchecker");
					landingPage.goToTransactionChecker(driver, step);
					transactionCheckerPage.approveAllTransactions(driver, step, dealId);
					landingPage.performLogout(driver, step);
					break;
					
				case "100017":
					loginPage.performLogin(driver, step, "transactionmaker");
					transaction = trigger.getJSONArray("transactions").getJSONObject(0);
					liveDealsPage.addTransaction(driver, step, transaction);
					dealName = transaction.getString("deal_name").toLowerCase().trim();
					dealId = (DealCheckerPage.dealDetails.get(dealName) == null) ? transaction.getString("deal_id") : DealCheckerPage.dealDetails.get(dealName);
					notifications.getJSONObject("100017").put("Ref ID", dealId);
					landingPage.performLogout(driver, step);
					
					loginPage.performLogin(driver, step, "transactionchecker");
					landingPage.goToTransactionChecker(driver, step);
					transactionCheckerPage.approveAllTransactions(driver, step, dealId);
					landingPage.performLogout(driver, step);
					break;
					
				default:
					break;
				}
			
			}
			catch (Exception e) {
				step.log(Status.FAIL, "Error while triggering notification: "+trigger.getString("notification_template_id"));
				step.log(Status.FAIL, e.getMessage());
				landingPage.performLogout(driver, step);
				throw new Exception("Error while triggering notification: "+trigger.getString("notification_template_id"));				
			}
		}
	}
}
