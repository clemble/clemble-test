package com.stresstest.spring.cleaners;

import java.util.HashMap;

public class CleanableFactory {

	private CleanableFactory() {
		throw new IllegalAccessError("This is utility class");
	}

	private static HashMap<Class<?>, Boolean> PROCESSED_CLASSES = new HashMap<Class<?>, Boolean>();

	public static Cleanable toCleanable(Object cleanable) {
		return (cleanable instanceof Cleanable) ? (Cleanable) cleanable : null;
	}

	public static <T> boolean canApply(Class<T> cleanableCandidate) {
		Boolean canApply = PROCESSED_CLASSES.get(cleanableCandidate);
		if (canApply == null) {
			canApply = Cleanable.class.isAssignableFrom(cleanableCandidate);
			PROCESSED_CLASSES.put(cleanableCandidate, canApply);
		}
		return canApply;
	}

}
