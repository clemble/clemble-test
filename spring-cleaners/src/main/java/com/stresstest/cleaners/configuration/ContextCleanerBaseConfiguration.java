package com.stresstest.cleaners.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.stresstest.cleaners.aop.CleanerSpringAdvisor;
import com.stresstest.cleaners.context.CleanerContext;
import com.stresstest.spring.listener.TestContextListenerRegistrator;

public class ContextCleanerBaseConfiguration implements ImportAware {
	protected AnnotationAttributes enableContextCleaner;

	@Bean
	public CleanerSpringAdvisor cleanerSpringAdvisor() {
		return new CleanerSpringAdvisor();
	}

	@Bean
	public CleanerContext cleanerContext() {
		return new CleanerContext();
	}

	@Bean
	public TestContextListenerRegistrator contextListenerRegistrator() {
		return new TestContextListenerRegistrator(null);
	}

	@Bean
	public ContextCleanerTestExecutionListener cleanerTestExecutionListener() {
		return new ContextCleanerTestExecutionListener(cleanerContext());
	}

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableContextCleaner = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(
				EnableContextCleaner.class.getName(), false));
	}
}
