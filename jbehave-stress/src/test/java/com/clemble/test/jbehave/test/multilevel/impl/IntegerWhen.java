package com.clemble.test.jbehave.test.multilevel.impl;

import org.jbehave.core.annotations.When;

import com.clemble.test.jbehave.test.multilevel.WhenInterface;

public class IntegerWhen implements WhenInterface<Integer>{

	@Override
	@When("$A do")
	public Integer when(Integer actionItem) {
		return actionItem - 1;
	}

}
