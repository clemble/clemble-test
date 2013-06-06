package com.stresstest.jbehave.test.multilevel;

import org.jbehave.core.annotations.Then;

public interface ThenInterface<T> {

	@Then("$A value is $T")
	public boolean check(T value, T target);

}
