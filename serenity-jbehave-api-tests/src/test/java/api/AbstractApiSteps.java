package api;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import net.thucydides.core.steps.ScenarioSteps;
import org.DmitryGontsa.common.EnvironmentProperties;

public abstract class AbstractApiSteps extends ScenarioSteps {

    public AbstractApiSteps() {
        RestAssured.baseURI = EnvironmentProperties.LOCAL_API_URL.readProperty();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}
