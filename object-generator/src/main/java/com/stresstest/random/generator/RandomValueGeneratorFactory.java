package com.stresstest.random.generator;

import com.stresstest.random.AbstractValueGeneratorFactory;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.ClassPropertySetterRegistry;

public class RandomValueGeneratorFactory extends AbstractValueGeneratorFactory {

	public RandomValueGeneratorFactory() {
		super(new ClassPropertySetterRegistry(), RandomValueGenerator.DEFAULT_GENERATORS);
	}

	public RandomValueGeneratorFactory(ClassPropertySetterRegistry propertySetterManager) {
		super(propertySetterManager, RandomValueGenerator.DEFAULT_GENERATORS);
	}

	@Override
	public <T> ValueGenerator<T> enumValueGenerator(Class<T> enumClass) {
		return RandomValueGenerator.enumValueGenerator(enumClass);
	}

}
