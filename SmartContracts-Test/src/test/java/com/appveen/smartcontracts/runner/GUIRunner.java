package com.appveen.smartcontracts.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true,
				plugin = {"com.appveen.smartcontracts.factory.ListenerFactory"},
				features={"src/test/resources/features"},
				glue={"com.appveen.smartcontracts.stepdefinitions"},
				tags= "@nikita"
				)

public class GUIRunner {

}
