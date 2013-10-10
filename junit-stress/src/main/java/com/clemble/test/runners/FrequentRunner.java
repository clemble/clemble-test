package com.clemble.test.runners;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.AfterClass;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class FrequentRunner extends BlockJUnit4ClassRunner {

    final private AtomicReference<Object> createdTestRef = new AtomicReference<Object>();

    public FrequentRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(final RunNotifier notifier) {
        // Step 0. Preparing test
        try {
            createdTestRef.set(super.createTest());
        } catch (Exception exception) {
            notifier.fireTestFailure(new Failure(Description.createSuiteDescription(getTestClass().getJavaClass()), exception));
        }
        // Step 1. Invoking CheckBefore
        FrequentRunnerUtils.runChecks(CheckBefore.class, notifier, createdTestRef.get(), getTestClass());
        // Step 2. Executing klassToRun
        FrequentTestRunner.run(getTestClass().getJavaClass(), new Runnable() {
            @Override
            public void run() {
                execute(notifier);
            }
        });
        // Step 3. Invoking CheckAfter
        FrequentRunnerUtils.runChecks(CheckAfter.class, notifier, createdTestRef.get(), getTestClass());
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        List<FrameworkMethod> afters= getTestClass().getAnnotatedMethods(AfterClass.class);
        return afters.isEmpty() ? statement : new RunAfters(statement, afters, null);
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return FrequentTestRunner.order(super.getChildren());
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
