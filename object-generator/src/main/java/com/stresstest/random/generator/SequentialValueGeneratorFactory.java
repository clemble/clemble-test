package com.stresstest.random.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.stresstest.random.AbstractValueGeneratorFactory;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.ClassPropertySetterManager;

public class SequentialValueGeneratorFactory extends AbstractValueGeneratorFactory {

	public SequentialValueGeneratorFactory() {
		super(new ClassPropertySetterManager(), SequentialValueGenerator.DEFAULT_GENERATORS);
	}

	public SequentialValueGeneratorFactory(ClassPropertySetterManager propertySetterManager) {
		super(propertySetterManager, SequentialValueGenerator.DEFAULT_GENERATORS);
	}

	@Override
	protected <T> ValueGenerator<T> enumValueGenerator(Class<T> klass) {
		return SequentialValueGenerator.enumValueGenerator(klass);
	}

	public <T> ValueGenerator<T> replace(ValueGenerator<T> valueGeneratorToReplace) {
		// TO DO
		return valueGeneratorToReplace;
	}
	
	public List<ValueGenerator<?>> replace(Collection<ValueGenerator<?>> generators) {
		List<ValueGenerator<?>> newGenerators = new ArrayList<ValueGenerator<?>>();
		for(ValueGenerator<?> constructorGenerator: generators) {
			ValueGenerator<?> newConstructorGenerator = replace(constructorGenerator);
			newGenerators.add(newConstructorGenerator);
		}
		return newGenerators;
	}

}
