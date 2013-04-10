package com.stresstest.random.generator;

import com.stresstest.random.AbstractValueGeneratorFactory;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.ClassPropertySetterManager;

public class RandomValueGeneratorFactory extends AbstractValueGeneratorFactory {

	public RandomValueGeneratorFactory() {
		super(new ClassPropertySetterManager(), RandomValueGenerator.DEFAULT_GENERATORS);
	}

	public RandomValueGeneratorFactory(ClassPropertySetterManager propertySetterManager) {
		super(propertySetterManager, RandomValueGenerator.DEFAULT_GENERATORS);
	}

	@Override
	public <T> ValueGenerator<T> enumValueGenerator(Class<T> enumClass) {
		return RandomValueGenerator.enumValueGenerator(enumClass);
	}

}
