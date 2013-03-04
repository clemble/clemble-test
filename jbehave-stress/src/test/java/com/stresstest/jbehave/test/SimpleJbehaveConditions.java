package com.stresstest.jbehave.test;

import org.jbehave.core.annotations.Aliases;
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

    @Then("$A int value equals $S")
    @Aliases(values = { "$A int equals $S"})
    public void checkIntValue(int expectedValue, int testValue) {
        Assert.assertEquals(expectedValue, testValue);
    }
    
    @Then("$A char equals $S")
    public void checkByteValue(char expectedValue, char testValue) {
        Assert.assertEquals(expectedValue, testValue);
    }
    
    @Then("$A byte equals $S")
    public void checkByteValue(byte expectedValue, byte testValue) {
        Assert.assertEquals(expectedValue, testValue);
    }
    
    @Then("$A long equals $S")
    public void checkLongValue(long expectedValue, long testValue) {
        Assert.assertEquals(expectedValue, testValue);
    }

    @Then("$A short equals $S")
    public void checkShortValue(short expectedValue, short testValue) {
        Assert.assertEquals(expectedValue, testValue);
    }

    @Given("$A add short $S to context")
    public short addShortToContext(@StoryParam String name, short value) {
        return value;
    }

    @Given("$A add long $S to context")
    public long addLongToContext(@StoryParam String name, Long value) {
        return value;
    }

    @Given("$A add byte $S to context")
    public byte addByteToContext(@StoryParam String name, byte value) {
        return value;
    }
    
    @Given("add char $S to context")
    public char contextCharToContext(@StoryParam String name, char value) {
        return value;
    }

    @Given("$A add char $S to context")
    public char addCharToContext(@StoryParam String name, char value) {
        return value;
    }

    @Given("$A add string $S to context")
    public String addCharToContext(@StoryParam String name, String value) {
        return value;
    }

    @Then("$A context size $S")
    public void fail(String name, int size) {
        Assert.assertEquals(storyContext.get(name).size(), size);
    }
}
