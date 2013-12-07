package com.clemble.test.spring.cleaners.annotation;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.test.spring.cleaners.configuration.EnableContextCleaner;
import com.clemble.test.spring.cleaners.context.CleanerContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AnnotationCleanableTest.AnnotationConfiguration.class })
public class AnnotationCleanableTest {

	@Configuration
	@EnableContextCleaner("com.clemble.test.spring.cleaners")
	public static class AnnotationConfiguration {

		@Bean
		public AnnotationCleanableService simpleCleanableService() {
			return new AnnotationCleanableService();
		}

	}

	@Autowired
	private AnnotationCleanableService cleanableService;

	@Autowired
	private CleanerContext cleanerContext;

	final static private AtomicReference<AnnotationCleanable> cleanable = new AtomicReference<AnnotationCleanable>();

	private int order = 0;

	@Test
	public void testCleanableMarked() {
		testCleanCalled();
	}

	@Test
	public void testCleanableProcessed() {
		testCleanCalled();
	}

	/**
	 * Can't guarantee order of execution in JUnit so emulating it
	 */
	public void testCleanCalled() {
		if(order == 0) {
			order++;
			actualTestGenerated();
		} else {
			actualtestCleanCalled();
		}
	}
	
	public void actualTestGenerated() {
		cleanable.set(cleanableService.getCleanable());
		Assert.assertNotNull(cleanable.get());
		Assert.assertFalse(cleanable.get().isCleanCalled());
	}
	
	public void actualtestCleanCalled() {
		Assert.assertFalse(cleanerContext.contains(cleanable.get()));
		Assert.assertNotNull(cleanable.get());
		Assert.assertTrue(cleanable.get().isCleanCalled());
	}
}
