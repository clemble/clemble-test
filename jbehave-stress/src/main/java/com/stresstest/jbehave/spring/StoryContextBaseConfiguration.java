package com.stresstest.jbehave.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import com.stresstest.jbehave.context.StoryContext;
import com.stresstest.jbehave.context.StoryContextClassFileTransformer;
import com.stresstest.jbehave.context.aop.StoryContextConverter;
import com.stresstest.jbehave.context.aop.StoryContextSpringAdvisor;

@Configuration
@EnableLoadTimeWeaving(aspectjWeaving = AspectJWeaving.DISABLED)
public class StoryContextBaseConfiguration {

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
