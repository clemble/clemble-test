package com.clemble.test.spring.cleaners.context;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.clemble.test.spring.cleaners.Cleanable;
import com.clemble.test.spring.cleaners.CleanableFactory;

public class CleanerContext {

	private BlockingQueue<Cleanable> cleanableObjects = new LinkedBlockingQueue<Cleanable>();

	public boolean contains(Object value) {
		return cleanableObjects.contains(value);
	}
	
	public void add(Object cleanableInstance) {
		// Step 0. Sanity check
		if (cleanableInstance == null)
			return;
		// Step 1. Converting to cleanable
		Cleanable convertedCleanable = CleanableFactory.toCleanable(cleanableInstance);
		if (convertedCleanable == null)
			return;
		cleanableObjects.add(convertedCleanable);
	}

	public void clean() {
		Cleanable processed = null;
		while ((processed = cleanableObjects.poll()) != null) {
			try {
				processed.clean();
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}

	}
}
