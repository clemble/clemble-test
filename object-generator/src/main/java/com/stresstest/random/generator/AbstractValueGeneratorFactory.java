package com.stresstest.random.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.stresstest.random.ValueGenerator;


abstract public class AbstractValueGeneratorFactory implements ValueGeneratorFactory {

	@Override
	final public Collection<ValueGenerator<?>> getValueGenerators(Class<?>[] parameters) {
		// Step 1. Sanity check
		if (parameters == null || parameters.length == 0)
			return Collections.emptyList();
		// Step 2. Sequential check
		Collection<ValueGenerator<?>> resultGenerators = new ArrayList<ValueGenerator<?>>();
		for (Class<?> parameter : parameters) {
			if (parameter != null)
				resultGenerators.add(getValueGenerator(parameter));
		}
		return resultGenerators;
	}

}
