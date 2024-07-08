package com.hillel.ua;

import net.serenitybdd.jbehave.SerenityStories;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.steps.ParameterControls;

import java.util.Optional;

public class AcceptanceTestSuite extends SerenityStories {

    private static final String STORY_FILE_PATTERN = System.getProperty("story.file.name");

    public AcceptanceTestSuite() {
        super();
        Optional.ofNullable(STORY_FILE_PATTERN).ifPresent(this::findStoriesCalled);
        configuration().useParameterControls(new ParameterControls().useDelimiterNamedParameters(true));
        configuration().usePendingStepStrategy(new FailingUponPendingStep());
    }
}
