package com.stresstest.cleaners;

public class CleanableFactory {

	private CleanableFactory() {
		throw new IllegalAccessError("This is utility class");
	}

	public static Cleanable toCleanable(Object cleanable) {
		return (cleanable instanceof Cleanable) ? (Cleanable) cleanable : null;
	}
	
	public static <T> boolean canApply(Class<T> cleanableCandidate) {
		return Cleanable.class.isAssignableFrom(cleanableCandidate);
	}

}
