package com.stresstest.jbehave.test.multilevel.impl;

import com.stresstest.jbehave.test.multilevel.GivenInterface;

public class IntegerGiven implements GivenInterface<Integer> {

	@Override
	public Integer action() {
		return 1;
	}

}
