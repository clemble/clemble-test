package com.stresstest.jbehave.test;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.stresstest.jbehave.context.StoryContext;
import com.stresstest.jbehave.context.StoryParam;

public class SimpleJbehaveConditions {

    @Autowired
    private StoryContext storyContext;

    public static class InternalObject {
        private String internalObject;

        public InternalObject() {
        }

        public InternalObject(String internalObject) {
            this.internalObject = internalObject;
        }

        public String getInternalObject() {
            return internalObject;
        }

        public String toString() {
            return "Internal - " + internalObject + " - lanretnI";
        }
    }

    public static class InternalError {
    }

    @Given("added $A")
    public InternalObject given(String name) {
        return new InternalObject("testMe");
    }
    
    @Given("$A add short $S to context")
    public Short addShortToContext(@StoryParam String name, Short value) {
        return value;
    }
    
    @Given("$A add long $S to context")
    public Long addLongToContext(String name, Long value) {
        return value;
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

    @Then("$A context size $S")
    public void fail(String name, int size) {
        Assert.assertEquals(storyContext.get(name).size(), size);
    }
}
