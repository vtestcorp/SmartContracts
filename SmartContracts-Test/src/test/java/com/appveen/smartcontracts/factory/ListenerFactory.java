package com.appveen.smartcontracts.factory;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;


import com.aventstack.extentreports.Status;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestSourceRead;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;


public class ListenerFactory implements ConcurrentEventListener{
	
	public static DataFactory dataFactory = null;	
	public static LocatorFactory locators = null;
	public static ReportFactory reportFactory = null;
	public static String environment = null;
	public static String operatingSystem = null;
	public static String browserName = null;
	private PublishFactory publishFactory = new PublishFactory();	
	private UtilityFactory utility = new UtilityFactory();
	
	private String scenarioName = null;
	private ArrayList<String> features = new ArrayList<String>();
	
	private ArrayList<String> testsRun = new ArrayList<String>();
	private Integer testsRunCount = null;	
	private ArrayList<String> testsPass = new ArrayList<String>();
	private Integer testsPassCount = null;
	private ArrayList<String> testsFail = new ArrayList<String>();
	private Integer testsFailCount = null;	
	private Date testRunStartedTime = null;
	private Date testRunFinishedTime = null;
	private String reportLink = null;
	
	private JSONObject prepareTestSummary() {
		JSONObject testSummary = new JSONObject();
		
		testSummary.put("executed", testsRunCount);
		testSummary.put("passed", testsPassCount);
		testSummary.put("failed", testsFailCount);
		testSummary.put("report", reportLink);
		
		return testSummary;
	}
	
	private JSONObject prepareDetailedTestSummary() {
		JSONObject testDetailedSummary = new JSONObject();		
		testDetailedSummary.put("testRunStartedAt", testRunStartedTime);
		testDetailedSummary.put("features", new JSONArray(features));
		testDetailedSummary.put("testsExecutedCount", testsRunCount);
		testDetailedSummary.put("testsExecuted", new JSONArray(testsRun));
		testDetailedSummary.put("testsPassedCount", testsPassCount);
		testDetailedSummary.put("testsPassed", new JSONArray(testsPass));
		testDetailedSummary.put("testsFailedCount", testsFailCount);
		testDetailedSummary.put("testsFailed", new JSONArray(testsFail));
		testDetailedSummary.put("testRunFinishedAt", testRunFinishedTime);
		testDetailedSummary.put("report", reportLink);
		
		return testDetailedSummary;		
	}
	
	public void onTestSourceRead(TestSourceRead testSourceRead) {
	
	}
	public void onTestRunStarted(TestRunStarted testRunStarted) throws Exception {	
		
		testRunStartedTime = new Date();		
		operatingSystem = (System.getProperty("os") == null) ? "windows" : System.getProperty("os").toLowerCase();
		browserName = (System.getProperty("browser") == null) ? "chrome": System.getProperty("browser").toLowerCase();
		environment = (System.getProperty("environment") == null) ? "dev" : System.getProperty("environment").toLowerCase();
		reportFactory = new ReportFactory();		  
		dataFactory = new DataFactory();
		
		locators = new LocatorFactory();	
		locators.setLocators();			
		
				
		
		testsRunCount = 0;
		testsPassCount = 0;
		testsFailCount = 0;
		  
	}
	
	public void onTestCaseStarted(TestCaseStarted testCaseStarted) throws Exception {	    
	    					
		reportFactory.setReportScenario(testCaseStarted.getTestCase().getName());
		scenarioName = testCaseStarted.getTestCase().getName();
		testsRun.add(scenarioName);
		testsRunCount++;
		
		String[] featureName = testCaseStarted.getTestCase().getUri().toString().split("/");		  
		reportFactory.getReportScenario(scenarioName).log(Status.INFO, "Feature: "+featureName[featureName.length-1]);		
		if(!features.contains(featureName[featureName.length-1])) {
			features.add(featureName[featureName.length-1]);
		}
		
		Thread.currentThread().setName(testCaseStarted.getTestCase().getName());
	}
	
	public void onTestCaseFinished(TestCaseFinished testCaseFinished) {
		
		String testStatus = testCaseFinished.getResult().getStatus().toString();
		if(testStatus.equals("PASSED")) {
			testsPass.add(scenarioName);
			testsPassCount++;
		}
		else if(testStatus.equals("FAILED")) {
			testsFail.add(scenarioName);
			testsFailCount++;
		}
	}
	
	public void onTestStepStarted(TestStepStarted testStepStarted) {
		//System.out.println("Test Step"+testStepStarted.getClass());
	}
	
	public void onTestStepFinished(TestStepFinished testStepFinished) {
		//System.out.println("Test Step"+testStepFinished.getResult().getStatus());
		
	}
	
	public void onTestRunFinished(TestRunFinished testRunFinished) {
	    reportFactory.prepareReport();	    	    
	    testRunFinishedTime = new Date();
	    
	    reportLink = publishFactory.publishTestReport();		
	    publishFactory.publishSlackNotification(prepareTestSummary());	
	    publishFactory.publishDataStackRecord(prepareDetailedTestSummary());
	    
	    utility.killAllWebDrivers();
	}
	
	
	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {		
			
		    eventPublisher.registerHandlerFor(TestRunStarted.class, event -> {
				try {
					onTestRunStarted(event);
				} catch (Exception e) {					
					e.printStackTrace();
				}
			});
		    eventPublisher.registerHandlerFor(TestRunFinished.class, this::onTestRunFinished);
		    eventPublisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
		    eventPublisher.registerHandlerFor(TestSourceRead.class, this::onTestSourceRead);
		    eventPublisher.registerHandlerFor(TestCaseStarted.class, event -> {
				try {
					onTestCaseStarted(event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		    //eventPublisher.registerHandlerFor(TestStepStarted.class, this::onTestStepStarted);
		    //eventPublisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
	    
	}
	

}
