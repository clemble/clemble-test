package com.clemble.test.jbehave.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.clemble.test.jbehave.context.StoryContext;
import com.clemble.test.jbehave.context.aop.StoryContextConverter;
import com.clemble.test.jbehave.context.aop.StoryContextSpringAdvisor;
import com.clemble.test.jbehave.context.configuration.StoryContextBaseConfiguration;
import com.clemble.test.jbehave.context.configuration.StoryContextTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = StoryContextBaseConfiguration.class)
@TestExecutionListeners(inheritListeners = true, value = { StoryContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class InitializationTest {

    @Autowired
    public StoryContext storyContext;

    @Autowired
    public StoryContextConverter storyContextConverter;

    @Autowired
    public StoryContextSpringAdvisor contextSpringAdvisor;

    @Test
    public void testInitialized() {
        Assert.assertNotNull(storyContext);
        Assert.assertNotNull(storyContextConverter);
        Assert.assertNotNull(contextSpringAdvisor);
    }

}
