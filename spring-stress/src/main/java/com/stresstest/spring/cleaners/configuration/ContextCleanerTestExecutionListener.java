package com.stresstest.spring.cleaners.configuration;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import com.stresstest.spring.cleaners.context.CleanerContext;

public class ContextCleanerTestExecutionListener implements TestExecutionListener {

	final private CleanerContext cleanerContext;

	public ContextCleanerTestExecutionListener(CleanerContext cleanerContext) {
		this.cleanerContext = cleanerContext;
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		cleanerContext.clean();
	}

	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		cleanerContext.clean();
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		cleanerContext.clean();
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		cleanerContext.clean();
	}

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		cleanerContext.clean();
	}
}
