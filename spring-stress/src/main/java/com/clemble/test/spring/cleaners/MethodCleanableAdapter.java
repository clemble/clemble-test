package com.clemble.test.spring.cleaners;

import java.lang.reflect.Method;
import java.util.Collection;

public class MethodCleanableAdapter implements CleanableAdapter {

	final private Collection<Method> cleanerMethods;

	public MethodCleanableAdapter(Collection<Method> cleanerMethods) {
		this.cleanerMethods = cleanerMethods;
	}

	@Override
	public Cleanable toCleanable(Object cleanable) {
		return new MethodCleanableWrapper(cleanable, cleanerMethods);
	}

	private static class MethodCleanableWrapper implements Cleanable {

		final private Collection<Method> cleanerMethods;
		final private Object cleanable;
		
		public MethodCleanableWrapper(Object cleanable, Collection<Method> cleanerMethod) {
			this.cleanable = cleanable;
			this.cleanerMethods = cleanerMethod;
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
