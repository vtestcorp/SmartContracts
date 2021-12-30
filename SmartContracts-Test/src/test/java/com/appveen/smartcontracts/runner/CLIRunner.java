package com.appveen.smartcontracts.runner;

import java.util.stream.Stream;

public final class CLIRunner {
	
	private static String[] defaultOptions = {
            "--glue", "com.appveen.smartcontracts.stepdefinitions",                    
            "--plugin", "com.appveen.smartcontracts.factory.ListenerFactory",
            "--tags", "@smoke"
    };
	
	
	public static void main(String[] args) {
		System.setProperty("testdata","src/test/resources/testdata");
		System.setProperty("driver","src/test/resources/drivers");
		System.setProperty("locator","src/test/resources/locators");
		System.setProperty("os", "linux");
		System.setProperty("browser","chrome");
		System.setProperty("environment","dev");
		System.setProperty("headless","true");
		System.setProperty("notifySlack", "true");
		System.setProperty("recordDataStack", "true");
		System.setProperty("cucumber.features", "src/test/resources/features");
		 Stream<String> cucumberOptions = Stream.concat(Stream.of(defaultOptions), Stream.of(args));
		 io.cucumber.core.cli.Main.main(cucumberOptions.toArray(String[]::new));
	       
    }

}
