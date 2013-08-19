package com.stresstest.jbehave.test;

import java.util.List;

import javax.inject.Singleton;

import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.io.LoadFromRelativeFile;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.StepFinder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stresstest.jbehave.context.aop.StoryContextConverter;
import com.stresstest.jbehave.context.configuration.EnableStoryContext;
import com.stresstest.jbehave.context.configuration.StoryContextSpringStepsFactory;
import com.stresstest.jbehave.test.SimpleScenario.TestScenarioConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@UsingSteps(instances = { SimpleJbehaveConditions.class })
@ContextConfiguration(classes = { TestScenarioConfiguration.class })
public class SimpleScenario extends JUnitStory {

    @org.springframework.context.annotation.Configuration
    @EnableStoryContext(packages = { "com.stresstest.jbehave.test" })
    public static class TestScenarioConfiguration {

        @Bean
        @Singleton
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

        StoryReporterBuilder storyReporterBuilder = new StoryReporterBuilder().withFailureTrace(true).withReporters(new ConsoleOutput());

        return new MostUsefulConfiguration().usePendingStepStrategy(new FailingUponPendingStep()).useFailureStrategy(new RethrowingFailure())
                .useStoryLoader(storyLoader).useStepFinder(new StepFinder(new StepFinder.ByLevenshteinDistance()))
                .useParameterConverters(new ParameterConverters().addConverters(storyContextConverter)).useStoryReporterBuilder(storyReporterBuilder);
    }

    @Override
    public List<CandidateSteps> candidateSteps() {
        List<CandidateSteps> candidateSteps = new StoryContextSpringStepsFactory(configuration(), applicationContext).createCandidateSteps();
        return candidateSteps;
    }

}
