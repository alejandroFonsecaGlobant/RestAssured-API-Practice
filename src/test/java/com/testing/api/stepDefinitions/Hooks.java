package com.testing.api.stepDefinitions;

import com.testing.api.requests.ClientRequest;
import com.testing.api.runner.TestRunner;
import com.testing.api.utils.Constants;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private static final ClientRequest clientRequest = new ClientRequest();

    @Before
    public void testStart(Scenario scenario) {
        logger.info("*****************************************************************************************");
        logger.info("	Scenario: " + scenario.getName());
        logger.info("*****************************************************************************************");
        RestAssured.baseURI = Constants.BASE_URL;
        new TestRunner();
    }
}
