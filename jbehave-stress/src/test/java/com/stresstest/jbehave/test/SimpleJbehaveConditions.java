package com.stresstest.jbehave.test;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

public class SimpleJbehaveConditions {

    public static class InternalObject {
        final private String internalObject;

        public InternalObject(String internalObject) {
            this.internalObject = internalObject;
        }

        public String getInternalObject() {
            return internalObject;
        }

        public String toString() {
            return internalObject;
        }
    }

    public static class InternalError {
    }

    @Given("$A")
    public InternalObject given(String name) {
        return new InternalObject("testMe");
    }

    @When("$A validated")
    public InternalError validate(InternalObject internalObject) {
        Assert.assertEquals(internalObject.getInternalObject(), "testMe");
        return internalObject != null ? new InternalError() : null;
    }

    @Then("$A validation fails")
    public void fail(InternalError internalError) {
        Assert.assertNotNull(internalError);
    }
}
