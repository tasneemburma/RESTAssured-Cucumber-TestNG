package main;

//import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
//import cucumber.api.junit.*;

//@RunWith(Cucumber.class)
@CucumberOptions(
		strict=false,
		features = "src/test/resources/features",
		glue = "stepdefinition",
		plugin = {"pretty", "json:target/cucumber.json"},
		tags={"@APITesting"})

public class CucumberRunner extends AbstractTestNGCucumberTests {
	
}
