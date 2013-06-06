package com.stresstest.jbehave.test.multilevel;

import org.jbehave.core.annotations.Given;

public interface GivenInterface<T> {

	@Given("Action $A")
	public T action();

}
