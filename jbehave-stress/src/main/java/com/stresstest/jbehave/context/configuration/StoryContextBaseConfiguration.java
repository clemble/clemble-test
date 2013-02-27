package com.stresstest.jbehave.context.configuration;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import com.stresstest.jbehave.context.StoryContext;
import com.stresstest.jbehave.context.aop.StoryContextConverter;
import com.stresstest.jbehave.context.aop.StoryContextSpringAdvisor;
import com.stresstest.jbehave.support.internal.StoryContextClassFileTransformer;
import com.stresstest.jbehave.support.internal.startup.Startup;

@Configuration
public class StoryContextBaseConfiguration {

    @PostConstruct
    public void initializeClassFileListenere(){
        Startup.register(new StoryContextClassFileTransformer());
    }

    @Bean
    public StoryContextSpringAdvisor storyContextSpringAdvisor() {
        return new StoryContextSpringAdvisor();
    }

    @Bean
    public StoryContextClassFileTransformer storyContextClassFileTransformer(){
        return new StoryContextClassFileTransformer();
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
