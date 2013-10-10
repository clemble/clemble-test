package com.clemble.test.runners;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.TestClass;

public class FrequentRunnerUtils {

    public static <T extends Annotation> void runChecks(final Class<T> annotation, final RunNotifier notifier, final Object testRef, final TestClass testClass) {
    	List<Throwable> errors = new ArrayList<Throwable>();
    	for (FrameworkMethod each : testClass.getAnnotatedMethods(annotation)) {
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
