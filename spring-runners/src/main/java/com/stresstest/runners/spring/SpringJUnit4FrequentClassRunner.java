package com.stresstest.runners.spring;

// This move is needed in order to have access to spring application context

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stresstest.runners.FrequentRunnerUtils;
import com.stresstest.runners.FrequentTestRunner;

public class SpringJUnit4FrequentClassRunner extends SpringJUnit4ClassRunner {

    final private AtomicReference<Object> createdTestRef = new AtomicReference<>();
    final private Class<?> klass;

    public SpringJUnit4FrequentClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.klass = klass;
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return FrequentTestRunner.order(super.getChildren());
    }

    @Override
    public void run(final RunNotifier notifier) {
    	// Step 1. Running actual test
        FrequentTestRunner.run(klass, new Runnable() {
            @Override
            public void run() {
                execute(notifier);
            }
        });
        // Step 2. Running after checks
        FrequentRunnerUtils.runAfterChecks(notifier, createdTestRef.get(), getTestClass());
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
         Object cratedTest = super.createTest();
         createdTestRef.set(cratedTest);
         return cratedTest;
    }

    public void execute(final RunNotifier notifier) {
        super.run(notifier);
    }

    public void executeChild(final FrameworkMethod method, RunNotifier notifier) {
        super.runChild(method, notifier);
    }
}
