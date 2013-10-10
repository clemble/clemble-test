package com.clemble.test.jbehave.test.multilevel.impl;

import org.jbehave.core.annotations.Then;

import com.clemble.test.jbehave.test.multilevel.ThenInterface;

public class ObjectThen<T> implements ThenInterface<T>{

	@Override
	@Then("$A value is $T")
	public boolean check(T value, T target) {
		return (value == null && target == null) || (value != null && value.equals(target));
	}
}
