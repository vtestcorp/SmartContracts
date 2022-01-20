package com.appveen.smartcontracts.stepdefinitions;

import org.openqa.selenium.WebDriver;

import com.appveen.smartcontracts.factory.ListenerFactory;
import com.appveen.smartcontracts.factory.LocatorFactory;
import com.appveen.smartcontracts.factory.ReportFactory;
import com.appveen.smartcontracts.factory.SSHFactory;
import com.appveen.smartcontracts.factory.WebDriverFactory;
import com.appveen.smartcontracts.pageobject.ODPEmailPage;
import com.appveen.smartcontracts.pageobject.ODPLandingPage;
import com.appveen.smartcontracts.pageobject.ODPLoginPage;
import com.appveen.smartcontracts.pageobject.TriggerNotificationPage;
import com.aventstack.extentreports.ExtentTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TriggerNotificationSteps {
	
	WebDriverFactory webDriverFactory = new WebDriverFactory();
	WebDriver driver = null;
	LocatorFactory locators = ListenerFactory.locators; 
	ReportFactory report = ListenerFactory.reportFactory;
	SSHFactory sshFactory = null; 	
	
	TriggerNotificationPage triggerNotificationPage = new TriggerNotificationPage(locators);
	ODPLoginPage odpLoginPage = new ODPLoginPage(locators);
	ODPLandingPage odpLandingPage = new ODPLandingPage(locators);
	ODPEmailPage odpEmailPage = new ODPEmailPage(locators);
	
	@Given("^Hooks and Templates are present in database$")
	public void Hooks_and_Templates_are_present_in_database() throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: Hooks and Templates are present in database");		
		sshFactory = new SSHFactory();
		triggerNotificationPage.validateDatabase(step);
	}
	
	@Given("^Pod logs are monitored$")
	public void Pod_logs_are_monitored() throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Given: Pod logs are monitored");	
		sshFactory.executeCommand(step, "tmux new -s notification -d \"kubectl get pods -n appveen | grep 'notification' | awk '{print \\$1}' | xargs -I{} kubectl logs {} -n appveen --tail 30 -f > notificationLog.txt\"");
		sshFactory.executeCommand(step, "tmux new -s smtp -d \"kubectl get pods -n appveen | grep 'smtp' | awk '{print \\$1}' | xargs -I{} kubectl logs {} -n appveen --tail 30 -f > smtpLog.txt\"");
		  
	}
	  
	@When("^I login as \"([^\"]*)\" and trigger notification$")
	public void I_login_as_user_and_trigger_notification(String user) throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "When: I login as "+user+" and and trigger notification");		
		driver = webDriverFactory.goToSmartContracts();		
		triggerNotificationPage.triggerNotifications(driver, step);		  
	}
	
	@Then("^Notifications should be triggered$")
	public void Notifications_should_be_triggered() throws Throwable {  
		ExtentTest step = report.setReportStep(Thread.currentThread().getName(), "Then: Notifications should be triggered");
		try {
			driver = webDriverFactory.goToODP();
			odpLoginPage.performLogin(driver, step, "admin");
			odpLandingPage.goToEmail(driver, step);
			odpEmailPage.validateEmails(driver, step);
		}
		catch (Exception e) {
			odpLandingPage.performLogout(driver, step);
			throw new Exception(e.getMessage()); 
		}
		
		
		sshFactory.executeCommand(step, "tmux kill-session -t notification");
		sshFactory.executeCommand(step, "tmux kill-session -t smtp");
		sshFactory.fetchFile(step, "notificationLog.txt");
		sshFactory.fetchFile(step, "smtpLog.txt");
		sshFactory.executeCommand(step, "rm -f notificationLog.txt smtpLog.txt");
		sshFactory.closeSSH(step);
	}
	
	

}
