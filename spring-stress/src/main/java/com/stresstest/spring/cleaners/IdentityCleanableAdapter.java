package com.stresstest.spring.cleaners;

public class IdentityCleanableAdapter implements CleanableAdapter {

	private IdentityCleanableAdapter() {
	}

	public static CleanableAdapter INSTANCE = new IdentityCleanableAdapter();

	@Override
	public Cleanable toCleanable(Object cleanable) {
		return (Cleanable) cleanable;
	}

}
