package com.stresstest.cleaners;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stresstest.cleaners.configuration.EnableContextCleaner;
import com.stresstest.cleaners.context.CleanerContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SimpleCleanableTest.SimpleConfiguration.class })
public class SimpleCleanableTest {

	@Configuration
	@EnableContextCleaner
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

	public SimpleCleanableTest() {

	}

	@Test
	public void testGenerated() {
		cleanable.set(cleanableService.getCleanable());
		Assert.assertNotNull(cleanable.get());
		Assert.assertFalse(cleanable.get().isCleanCalled());
		Assert.assertTrue(cleanerContext.contains(cleanable.get()));
	}

	@Test
	public void testCleanCalled() {
		Assert.assertFalse(cleanerContext.contains(cleanable.get()));
		Assert.assertNotNull(cleanable.get());
		Assert.assertTrue(cleanable.get().isCleanCalled());
	}
}
