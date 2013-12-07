package com.clemble.test.jbehave.context.configuration;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.type.AnnotationMetadata;

import com.clemble.test.jbehave.context.StoryContext;
import com.clemble.test.jbehave.context.aop.StoryContextConverter;
import com.clemble.test.jbehave.context.aop.StoryContextSpringAdvisor;
import com.clemble.test.jbehave.support.internal.StoryContextClassFileTransformer;
import com.clemble.test.spring.listener.TestContextListenerRegistrator;
import com.clemble.test.support.internal.startup.Startup;

@Configuration
public class StoryContextBaseConfiguration implements ImportAware {
    protected AnnotationAttributes enableStoryContext;

    @PostConstruct
    public void initializeClassFileListenere() {
        if (enableStoryContext != null && enableStoryContext.getStringArray("packages").length > 0)
            Startup.register(new StoryContextClassFileTransformer(enableStoryContext.getStringArray("packages")));
    }

    @Bean
    @Singleton
    public StoryContextSpringAdvisor storyContextSpringAdvisor() {
        return new StoryContextSpringAdvisor();
    }

    @Bean
    @Singleton
    public StoryContext storyContext() {
        return new StoryContext();
    }

    @Bean
    @Singleton
    public StoryContextConverter storyContextConverter() {
        return new StoryContextConverter();
    }

    @Bean
    @Singleton
    public ConversionService conversionService() {
        return new DefaultConversionService();
    }

    @Bean
    @Singleton
    public TestContextListenerRegistrator storyContextListenerProcessingBean() {
        return new TestContextListenerRegistrator();
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableStoryContext = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableStoryContext.class.getName(), false));
    }
}
