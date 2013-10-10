package com.stresstest.cleaners;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stresstest.spring.cleaners.configuration.EnableContextCleaner;
import com.stresstest.spring.cleaners.context.CleanerContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SimpleCleanableTest.SimpleConfiguration.class })
public class SimpleCleanableTest {

	@Configuration
	@EnableContextCleaner(packages = "com.stresstest.cleaners")
	public static class SimpleConfiguration {

		@Bean
		public SimpleCleanableService simpleCleanableService() {
			return new SimpleCleanableService();
		}

	}

	@Autowired
	private SimpleCleanableService cleanableService;

	@Autowired
	private CleanerContext cleanerContext;

	final static private AtomicReference<SimpleCleanable> cleanable = new AtomicReference<SimpleCleanable>();

	private int order = 0;

	public SimpleCleanableTest() {

	}

	@Test
	public void testGenerated() {
		testCleanCalled();
	}

	/**
	 * Can't guarantee order of execution in JUnit so emulating it
	 */
	@Test
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
		Assert.assertTrue(cleanerContext.contains(cleanable.get()));
	}
	
	public void actualtestCleanCalled() {
		Assert.assertFalse(cleanerContext.contains(cleanable.get()));
		Assert.assertNotNull(cleanable.get());
		Assert.assertTrue(cleanable.get().isCleanCalled());
	}
}
