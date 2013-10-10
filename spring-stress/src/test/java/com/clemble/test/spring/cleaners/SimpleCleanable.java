package com.clemble.test.spring.cleaners;

import com.clemble.test.spring.cleaners.Cleanable;

public class SimpleCleanable implements Cleanable {

	private boolean cleanCalled = false;

	@Override
	public void clean() {
		setCleanCalled(true);
	}

	public boolean isCleanCalled() {
		return cleanCalled;
	}

	public void setCleanCalled(boolean cleanCalled) {
		this.cleanCalled = cleanCalled;
	}

}
