package com.stresstest.jbehave.test;

import java.util.List;

import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.LoadFromRelativeFile;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.ParameterConverters;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stresstest.jbehave.context.aop.StoryContextConverter;
import com.stresstest.jbehave.spring.StoryContextBaseConfiguration;
import com.stresstest.jbehave.spring.StoryContextSpringStepsFactory;
import com.stresstest.jbehave.test.TestScenario.TestScenarioConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@UsingSteps(instances = { SimpleJbehaveConditions.class })
@ContextConfiguration(classes = { TestScenarioConfiguration.class })
public class TestScenario extends JUnitStory {

    @org.springframework.context.annotation.Configuration
    public static class TestScenarioConfiguration extends StoryContextBaseConfiguration {

        @Bean
        public SimpleJbehaveConditions simpleJbehaveConditions() {
            return new SimpleJbehaveConditions();
        }
    }

    @Autowired
    public ApplicationContext applicationContext;
    
    @Autowired
    public StoryContextConverter storyContextConverter;

    @Override
    public Configuration configuration() {
        StoryLoader storyLoader = new LoadFromRelativeFile(getClass().getResource("/"));

        StoryReporterBuilder storyReporterBuilder = new StoryReporterBuilder().withFailureTrace(true).withFailureTraceCompression(true)
                .withReporters(new ConsoleOutput());

        return new MostUsefulConfiguration()
            .usePendingStepStrategy(new FailingUponPendingStep())
            .useFailureStrategy(new FailingUponPendingStep())
            .useStoryLoader(storyLoader)
            .useParameterConverters(new ParameterConverters().addConverters(storyContextConverter))
            .useStoryReporterBuilder(storyReporterBuilder);
    }

    @Override
    public List<CandidateSteps> candidateSteps() {
        List<CandidateSteps> candidateSteps = new StoryContextSpringStepsFactory(configuration(), applicationContext).createCandidateSteps();
        return candidateSteps;
    }

}
