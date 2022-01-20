package com.appveen.smartcontracts.factory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.zeroturnaround.zip.ZipUtil;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;


public class ReportFactory {
	
	private ExtentSparkReporter spark = null;
	private ExtentReports extent = null;
	private ExtentTest scenario = null;
	private ExtentTest step = null;
	private HashMap<String, ExtentTest> scenarioMap = new HashMap<String, ExtentTest>();

	public ReportFactory() throws Exception {
		try {
			FileUtils.deleteDirectory(new File("./report/"));
			
			spark = new ExtentSparkReporter("./report/index.html").viewConfigurer()
				    .viewOrder()
				    .as(new ViewName[] { 
					   ViewName.DASHBOARD, 
					   ViewName.TEST, 				   
					   ViewName.AUTHOR, 
					   ViewName.DEVICE, 
					   ViewName.EXCEPTION, 
					   ViewName.LOG 
					})
				  .apply();
	        
	        //initialize ExtentReports and attach the HtmlReporter
	        extent = new ExtentReports();
	        extent.attachReporter(spark);
	         
	        //To add system or environment info by using the setSystemInfo method.
	        extent.setSystemInfo("Operating System", ListenerFactory.operatingSystem);
	        extent.setSystemInfo("Browser", ListenerFactory.browserName);
	        extent.setSystemInfo("Environment", ListenerFactory.environment);
	        //configuration items to change the look and feel
	        //add content, manage tests etc
	        //spark.config().set sibilityOnOpen(true);
	        spark.config().setDocumentTitle("Smart Contracts");
	        spark.config().setReportName("Test Report");
	        //spark.config().setTestViewChartLocation();
	        spark.config().setTheme(Theme.DARK);
	        //spark.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");			
		}
		catch (IOException e) {
			throw new Exception(e.getMessage());			
		}
	}
	
	
	public ExtentTest setReportScenario(String scenarioName) {
		scenario = extent.createTest(scenarioName);
		scenarioMap.put(scenarioName, scenario);
		return scenario;
	}
	
	public ExtentTest setReportStep(String scenarioName, String stepName) {		
		step = scenarioMap.get(scenarioName).createNode(stepName);
		return step;
		
	}
	
	public ExtentTest getReportScenario(String scenarioName) {
		return scenarioMap.get(scenarioName);
	}
	public ExtentTest getReportStep() {
		return step;
	}
	
	public void addStepInfoScreenshot(WebDriver driver, ExtentTest step) throws IOException {
		UUID uid = UUID.randomUUID();
		String destinationFile = "./report/screenshots/"+uid.toString()+".png";
		String reportScreenPath = "./screenshots/"+uid.toString()+".png";
	    File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    
	    FileUtils.copyFile(sourceFile, new File(destinationFile));
	    step.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromPath(reportScreenPath).build());
	}
	
	public void addStepWarnScreenshot(WebDriver driver, ExtentTest step) throws IOException {
		UUID uid = UUID.randomUUID();
		String destinationFile = "./report/screenshots/"+uid.toString()+".png";
		String reportScreenPath = "./screenshots/"+uid.toString()+".png";
	    File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    
	    FileUtils.copyFile(sourceFile, new File(destinationFile));
	    step.log(Status.WARNING, MediaEntityBuilder.createScreenCaptureFromPath(reportScreenPath).build());
	}
	
	public void addStepPassScreenshot(WebDriver driver, ExtentTest step) throws IOException {
		UUID uid = UUID.randomUUID();
		String destinationFile = "./report/screenshots/"+uid.toString()+".png";
		String reportScreenPath = "./screenshots/"+uid.toString()+".png";
	    File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    
	    FileUtils.copyFile(sourceFile, new File(destinationFile));
	    step.log(Status.PASS, MediaEntityBuilder.createScreenCaptureFromPath(reportScreenPath).build());
	}
	
	public void addStepFailScreenshot(WebDriver driver, ExtentTest step) throws IOException {
		UUID uid = UUID.randomUUID();
		String destinationFile = "./report/screenshots/"+uid.toString()+".png";
		String reportScreenPath = "./screenshots/"+uid.toString()+".png";
	    File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    
	    FileUtils.copyFile(sourceFile, new File(destinationFile));
	    step.log(Status.FAIL, MediaEntityBuilder.createScreenCaptureFromPath(reportScreenPath).build());
	}
	
	public void prepareReport() {
		extent.flush();
		ZipUtil.pack(new File("./report"), new File("./report.zip"));
	}
	
	
}
