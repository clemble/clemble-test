package com.stresstest.jbehave.context.configuration;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import com.stresstest.jbehave.context.StoryContext;

public class StoryContextTestExecutionListener implements TestExecutionListener {

    public StoryContext storyContext;

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        storyContext = testContext.getApplicationContext().getBean(StoryContext.class);
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        storyContext.clear();
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        storyContext.clear();
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        storyContext.clear();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
    }

}
