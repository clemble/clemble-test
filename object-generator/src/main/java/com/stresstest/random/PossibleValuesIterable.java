package com.stresstest.random;

import java.util.Iterator;

import com.stresstest.random.constructor.ClassConstructor;
import com.stresstest.random.constructor.ClassPropertySetter;
import com.stresstest.random.constructor.ClassValueGenerator;
import com.stresstest.random.generator.SequentialValueGeneratorFactory;

public class PossibleValuesIterable<T> implements Iterable<T> {

	final private static SequentialValueGeneratorFactory sequentialValueGeneratorFactory = new SequentialValueGeneratorFactory();

	final private ValueGenerator<T> valueGenerator;

	final private int size;

	public PossibleValuesIterable(ValueGenerator<T> iValueGenerator) {
		if (iValueGenerator instanceof ClassValueGenerator) {
			ClassValueGenerator<T> classValueGenerator = (ClassValueGenerator<T>) iValueGenerator;
			// Step 1. Replace valueGenerators for constructor
			ClassConstructor<T> classConstructor = classValueGenerator.getObjectConstructor();
			classConstructor = classConstructor.clone(sequentialValueGeneratorFactory.replace(classConstructor
					.getValueGenerators()));
			// Step 2. Replacing valueGenerators for properties
			ClassPropertySetter<T> classPropertySetter = classValueGenerator.getPropertySetter();
			classPropertySetter.clone(sequentialValueGeneratorFactory.replace(classPropertySetter.getValueGenerators()));
			// Step 3. Creating new ClassValueGenerator
			classValueGenerator = new ClassValueGenerator<T>(classConstructor, classPropertySetter);
			// Step 4. Calculating possible range
			iValueGenerator = classValueGenerator;
		} else {
			iValueGenerator = sequentialValueGeneratorFactory.replace(iValueGenerator);
		}

		this.valueGenerator = iValueGenerator.clone();
		this.size = iValueGenerator.scope();
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private int currentPosition = 0;
			private ClassValueGenerator<T> valueGenerator = (ClassValueGenerator<T>) PossibleValuesIterable.this.valueGenerator
					.clone();

			@Override
			public boolean hasNext() {
				return currentPosition < size;
			}

			@Override
			public T next() {
				if (hasNext()) {
					currentPosition++;
					return valueGenerator.generate();
				}
				return null;
			}

			@Override
			public void remove() {
				// Do nothing
			}

		};
	}

}
