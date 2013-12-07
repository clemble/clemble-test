package com.clemble.test.spring.cleaners.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.clemble.test.spring.cleaners.aop.CleanerSpringAdvisor;
import com.clemble.test.spring.cleaners.context.CleanerContext;
import com.clemble.test.spring.listener.TestContextListenerRegistrator;

@Configuration
public class ContextCleanerBaseConfiguration implements ImportAware {

    protected AnnotationAttributes enableContextCleaner;

    private CleanerContext cleanerContext = new CleanerContext();

    @Bean
    public CleanerSpringAdvisor cleanerSpringAdvisor() {
        if (enableContextCleaner != null) {
            return new CleanerSpringAdvisor(enableContextCleaner.getStringArray("value"));
        } else {
            return new CleanerSpringAdvisor(new String[0]);
        }
    }

    @Bean
    public CleanerContext cleanerContext() {
        return cleanerContext;
    }

    @Bean
    public TestContextListenerRegistrator contextListenerRegistrator() {
        return new TestContextListenerRegistrator();
    }

    @Bean
    public ContextCleanerTestExecutionListener cleanerTestExecutionListener() {
        return new ContextCleanerTestExecutionListener(cleanerContext);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableContextCleaner = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableContextCleaner.class.getName(), false));
    }
}
