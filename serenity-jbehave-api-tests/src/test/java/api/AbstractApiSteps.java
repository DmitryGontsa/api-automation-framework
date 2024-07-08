package com.hillel.ua.api;

import com.hillel.ua.common.EnvironmentProperties;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import net.thucydides.core.steps.ScenarioSteps;

public abstract class AbstractApiSteps extends ScenarioSteps {

    public AbstractApiSteps() {
        RestAssured.baseURI = EnvironmentProperties.LOCAL_API_URL.readProperty();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}
