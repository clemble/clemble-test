package com.clemble.test.runners;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.clemble.test.runners.CheckAfter;
import com.clemble.test.runners.CheckBefore;
import com.clemble.test.runners.FrequentRunner;
import com.clemble.test.runners.RunInParallel;
import com.clemble.test.runners.RunTimes;
import com.clemble.test.runners.SingleRun;

@RunWith(FrequentRunner.class)
@RunInParallel(maxThreads = 50)
@RunTimes(500)
public class FrequentRunnerSingleRunTest {
	
	private AtomicInteger onceCounter = new AtomicInteger(0);
	private AtomicInteger parallelCounter = new AtomicInteger(0);

	@CheckBefore
	public void checkBefore() {
		assertEquals(onceCounter.get(), 0);
		assertEquals(parallelCounter.get(), 0);
	}

	@CheckAfter
	public void checkAfter() {
		assertEquals(onceCounter.get(), 1);
		assertEquals(parallelCounter.get(), 500);
	}

	@SingleRun
	@Test
	public void increaseCounterOnceTest() {
		onceCounter.incrementAndGet();
	}
	
	@Test
	public void increaseCounter() {
		parallelCounter.incrementAndGet();
	}
}
