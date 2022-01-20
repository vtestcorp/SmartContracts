 package com.appveen.smartcontracts.factory;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class WebDriverFactory {
	
	private WebDriver driver = null;		
	public static ArrayList<WebDriver> webDriverList = new ArrayList<WebDriver>();
	
	private WebDriver getWebDriver(String browserName) {
		try {		
			String driverPath = (System.getProperty("driver") == null) ? "src/test/resources/drivers": System.getProperty("driver").toString();
			boolean headless = (System.getProperty("headless") == null) ? false : Boolean.getBoolean("headless");
			switch (browserName) {
				case "chrome":	
					String chromeDriver = (ListenerFactory.operatingSystem == "linux") ? "/chromedriver" : "/chromedriver.exe";
					System.setProperty("webdriver.chrome.driver",driverPath+chromeDriver);
					if(headless) {
						ChromeOptions options = new ChromeOptions();
						options.addArguments("--headless", "--window-size=1296,696", "--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage");
						//, "--no-proxy-server", "--proxy-server='direct://'", "--proxy-bypass-list=*", "--window-size=1920,1200", "--remote-debugging-port=9222", "--disable-gpu", "--disable-gl-drawing-for-tests", "--no-zygote", "--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage"
						driver = new ChromeDriver(options);
					}
					else {
						driver= new ChromeDriver();
						driver.manage().window().maximize();
					}							
					break;
		
				default:
					throw new IllegalArgumentException("Invalid browser: "+browserName);
			}
		    
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);		    	    	
		}
		catch (Exception e) {	
			System.err.println("Error while initiating driver");
			System.err.println(e.getMessage());			
		}
		webDriverList.add(driver);
		return driver;
				
	}
	
	public WebDriver goToSmartContracts() {
		driver = getWebDriver(ListenerFactory.browserName);
		
		switch (ListenerFactory.environment) {
			case "dev": 
				driver.get("https://dev.xcro.appveen.com/");
				break;
			case "sit6":
				driver.get("https://sit6.xcro.appveen.com/");
				break;
			case "pt":
				driver.get("https://pt.xcro.appveen.com/");
				break;
		default:
			throw new IllegalArgumentException("Invalid enrivonment: "+ListenerFactory.environment);				
		}
		return driver;		
	}
	
	public WebDriver goToODP() {
		driver = getWebDriver(ListenerFactory.browserName);		
		driver.get("https://odp.xcro.appveen.com/appcenter");
		
		return driver;		
	}	
	
	public void closeWebDriver(WebDriver driver) {
		try {
			webDriverList.remove(driver);
			//driver.close();
			driver.quit();
		}
		catch (Exception e) {
			System.out.println("Webdriver already closed.");
		}
	}

}
