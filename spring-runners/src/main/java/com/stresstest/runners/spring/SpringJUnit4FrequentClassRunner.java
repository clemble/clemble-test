package com.stresstest.runners.spring;

// This move is needed in order to have access to spring application context

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stresstest.runners.CheckAfter;
import com.stresstest.runners.CheckBefore;
import com.stresstest.runners.FrequentRunnerUtils;
import com.stresstest.runners.FrequentTestRunner;

public class SpringJUnit4FrequentClassRunner extends SpringJUnit4ClassRunner {

    final private AtomicReference<Object> createdTestRef = new AtomicReference<Object>();

    public SpringJUnit4FrequentClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return FrequentTestRunner.order(super.getChildren());
    }

    @Override
    public void run(final RunNotifier notifier) {
        // Step 0. Preparing test
        try {
            createdTestRef.set(super.createTest());
        } catch (Exception exception) {
            notifier.fireTestFailure(new Failure(Description.createSuiteDescription(getTestClass().getClass()), exception));
        }
        // Step 1. Invoking CheckBefore
        FrequentRunnerUtils.runChecks(CheckBefore.class, notifier, createdTestRef.get(), getTestClass());
    	// Step 1. Running actual test
        FrequentTestRunner.run(getTestClass().getJavaClass(), new Runnable() {
            @Override
            public void run() {
                execute(notifier);
            }
        });
        // Step 2. Running after checks
        FrequentRunnerUtils.runChecks(CheckAfter.class, notifier, createdTestRef.get(), getTestClass());
    }

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        FrequentTestRunner.run(method.getMethod(), new Runnable() {
            @Override
            public void run() {
                executeChild(method, notifier);
            }
        });
    }

    @Override
    public Object createTest() throws Exception {
         return createdTestRef.get();
    }

    public void execute(final RunNotifier notifier) {
        super.run(notifier);
    }

    public void executeChild(final FrameworkMethod method, RunNotifier notifier) {
        super.runChild(method, notifier);
    }
}
