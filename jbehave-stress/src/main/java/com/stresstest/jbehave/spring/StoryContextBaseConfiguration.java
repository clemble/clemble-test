package com.stresstest.jbehave.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import com.stresstest.jbehave.context.StoryContext;
import com.stresstest.jbehave.context.aop.StoryContextConverter;
import com.stresstest.jbehave.context.aop.StoryContextSpringAdvisor;

@Configuration
public class StoryContextBaseConfiguration {

    @Bean
    public StoryContextSpringAdvisor storyContextSpringAdvisor() {
        return new StoryContextSpringAdvisor();
    }

    @Bean
    public StoryContext storyContext() {
        return new StoryContext();
    }

    @Bean
    public StoryContextConverter storyContextConverter() {
        return new StoryContextConverter();
    }

    @Bean
    public ConversionService conversionService() {
        return new DefaultConversionService();
    }
}
