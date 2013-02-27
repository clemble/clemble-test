package com.stresstest.jbehave.test;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.stresstest.jbehave.context.StoryContext;
import com.stresstest.jbehave.context.StoryParam;

public class SimpleJbehaveConditions {

    @Autowired
    private StoryContext storyContext;

    @Given("$A add int $S to context")
    public int addIntToContext(@StoryParam String name, int valueToAdd) {
        return valueToAdd;
    }
    
    @Given("$A add short $S to context")
    public short addShortToContext(@StoryParam String name, short value) {
        return value;
    }
    
    @Given("$A add long $S to context")
    public long addLongToContext(@StoryParam String name, Long value) {
        return value;
    }

    @Then("$A context size $S")
    public void fail(String name, int size) {
        Assert.assertEquals(storyContext.get(name).size(), size);
    }
}
