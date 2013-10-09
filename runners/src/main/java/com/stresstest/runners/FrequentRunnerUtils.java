package com.stresstest.runners;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.TestClass;

public class FrequentRunnerUtils {

    public static void runAfterChecks(final RunNotifier notifier, final Object testRef, final TestClass testClass) {
    	List<Throwable> errors = new ArrayList<>();
    	for (FrameworkMethod each : testClass.getAnnotatedMethods(CheckAfter.class)) {
    		try {
    			each.invokeExplosively(testRef);
    		} catch (Throwable e) {
    			errors.add(e);
    		}
    	}
    	if(errors.size() != 0) {
    		notifier.fireTestFailure(new Failure(Description.createSuiteDescription(testClass.getClass()), new MultipleFailureException(errors)));
    	}
    }

}
