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

	private static HashMap<Class<?>, CleanableAdapter> PROCESSED_CLASSES = new HashMap<Class<?>, CleanableAdapter>();

	public static Cleanable toCleanable(Object cleanable) {
		// Step 1. Looking for annotations in Object methods
		if(cleanable != null && canApply(cleanable.getClass())) {
			CleanableAdapter cleanableAdapter = PROCESSED_CLASSES.get(cleanable.getClass());
			return cleanableAdapter != null ? cleanableAdapter.toCleanable(cleanable) : null;
		}
		return null;
	}

	public static <T> boolean canApply(Class<T> cleanableCandidate) {
		if (!PROCESSED_CLASSES.containsKey(cleanableCandidate)) {
			CleanableAdapter adapter = null;
			// Step 1. Check that it inherits from Cleanable
			if(Cleanable.class.isAssignableFrom(cleanableCandidate)) {
				adapter = IdentityCleanableAdapter.INSTANCE;
			} else {
				// Step 2. Checking declared methods for @Cleaner
				Collection<Method> cleanerMethods = new ArrayList<Method>();
				for(Method method: cleanableCandidate.getMethods()) {
					if (method.getAnnotation(Cleaner.class) != null)
						cleanerMethods.add(method);
				}
				if(cleanerMethods != null)
					adapter = new MethodCleanableAdapter(cleanerMethods);
			}
			// Step 3. Saving for optimization
			PROCESSED_CLASSES.put(cleanableCandidate, adapter);
		}
		return PROCESSED_CLASSES.get(cleanableCandidate) != null;
	}
	
}
