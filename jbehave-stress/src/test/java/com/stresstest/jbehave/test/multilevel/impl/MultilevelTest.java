package com.stresstest.jbehave.test.multilevel.impl;

import java.util.List;

import javax.inject.Singleton;

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
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stresstest.jbehave.context.aop.StoryContextConverter;
import com.stresstest.jbehave.context.configuration.EnableStoryContext;
import com.stresstest.jbehave.context.configuration.StoryContextSpringStepsFactory;
import com.stresstest.jbehave.test.multilevel.GivenInterface;
import com.stresstest.jbehave.test.multilevel.ThenInterface;
import com.stresstest.jbehave.test.multilevel.WhenInterface;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MultilevelTest.MultilevelTestConfiguration.class)
public class MultilevelTest extends JUnitStory {

    @org.springframework.context.annotation.Configuration
    @EnableStoryContext(packages = { "com" })
    public static class MultilevelTestConfiguration {

        @Bean
        @Singleton
        public GivenInterface<Integer> givenInterface() {
            return new IntegerGiven();
        }

        @Bean
        @Singleton
        public WhenInterface<Integer> whenInterface() {
            return new IntegerWhen();
        }

        @Bean
        @Singleton
        public ThenInterface<Integer> thenInterface() {
            return new ObjectThen<Integer>();
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
