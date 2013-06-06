package com.stresstest.jbehave.test.multilevel;

import org.jbehave.core.annotations.When;

public interface WhenInterface<T> {

	@When("$A do")
	public T when(T actionItem);

}
