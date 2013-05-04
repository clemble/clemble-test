package com.stresstest.jbehave.context.configuration;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.type.AnnotationMetadata;

import com.stresstest.jbehave.context.StoryContext;
import com.stresstest.jbehave.context.aop.StoryContextConverter;
import com.stresstest.jbehave.context.aop.StoryContextSpringAdvisor;
import com.stresstest.jbehave.support.internal.StoryContextClassFileTransformer;
import com.stresstest.jbehave.support.internal.startup.Startup;

@Configuration
public class StoryContextBaseConfiguration implements ImportAware {
    protected AnnotationAttributes enableStoryContext;

    @PostConstruct
    public void initializeClassFileListenere() {
        if (enableStoryContext != null && enableStoryContext.getStringArray("packages").length > 0)
            Startup.register(new StoryContextClassFileTransformer(enableStoryContext.getStringArray("packages")));
    }

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

    @Bean
    public StoryContextListenerProcessingBean storyContextListenerProcessingBean() {
        return new StoryContextListenerProcessingBean();
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableStoryContext = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableStoryContext.class.getName(), false));
    }
}
