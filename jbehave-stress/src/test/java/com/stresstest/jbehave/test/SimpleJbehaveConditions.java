package com.stresstest.jbehave.test;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

public class SimpleJbehaveConditions {

    public static class InternalObject {
    }

    public static class InternalError {
    }

    @Given("$A")
    public InternalObject given(String name) {
        return new InternalObject();
    }

    @When("$A validated")
    public InternalError validate(InternalObject internalObject) {
        return internalObject != null ? new InternalError() : null;
    }

    @Then("validation fails")
    public void fail(InternalError internalError) {
        Assert.assertNotNull(internalError);
    }
}
