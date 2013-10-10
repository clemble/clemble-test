package com.stresstest.spring.cleaners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.stresstest.spring.cleaners.annotation.Cleaner;

public class CleanableFactory {

	private CleanableFactory() {
		throw new IllegalAccessError("This is utility class");
	}

	private static HashMap<Class<?>, Boolean> PROCESSED_CLASSES = new HashMap<Class<?>, Boolean>();

	public static Cleanable toCleanable(Object cleanable) {
		if(cleanable instanceof Cleanable) {
			return (cleanable instanceof Cleanable) ? (Cleanable) cleanable : null;
		} else {
			Collection<Method> cleaners = new ArrayList<Method>();
			for(Method method: cleanable.getClass().getMethods()) {
				if (method.getAnnotation(Cleaner.class) != null) {
					cleaners.add(method);
				}
			}
			return new MethodCleanableAddapter(cleanable, cleaners);
		}
	}

	public static <T> boolean canApply(Class<T> cleanableCandidate) {
		Boolean canApply = PROCESSED_CLASSES.get(cleanableCandidate);
		if (canApply == null) {
			// Step 1. Check that it inherits from Cleanable
			canApply = Cleanable.class.isAssignableFrom(cleanableCandidate);
			// Step 2. Checking declared methods for @Cleaner
			if(!canApply) {
				for(Method method: cleanableCandidate.getMethods()) {
					canApply = canApply || method.getAnnotation(Cleaner.class) != null;
				}
			}
			// Step 3. Saving for optimization
			PROCESSED_CLASSES.put(cleanableCandidate, canApply);
		}
		return canApply;
	}
	
	public static class MethodCleanableAddapter implements Cleanable {
		
		final private Collection<Method> cleanerMethods;
		final private Object cleanable;
		
		public MethodCleanableAddapter(Object cleanable, Collection<Method> cleanerMethods) {
			this.cleanable = cleanable;
			this.cleanerMethods = cleanerMethods;
		}

		@Override
		public void clean() {
			for(Method method: cleanerMethods) {
				try {
					method.invoke(cleanable);
				} catch (Throwable throwable) {
					// DO NOTHING
				}
			}
		}

	}

}
