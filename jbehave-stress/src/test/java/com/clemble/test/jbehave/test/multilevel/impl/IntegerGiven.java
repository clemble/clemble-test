package com.clemble.test.jbehave.test.multilevel.impl;

import com.clemble.test.jbehave.test.multilevel.GivenInterface;

public class IntegerGiven implements GivenInterface<Integer> {

	@Override
	public Integer action() {
		return 1;
	}

}
