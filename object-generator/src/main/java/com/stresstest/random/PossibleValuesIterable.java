package com.stresstest.random;

import java.util.Iterator;

import com.stresstest.random.constructor.ClassValueGenerator;

public class PossibleValuesIterable<T> implements Iterable<T> {

	final private ClassValueGenerator<T> classValueGenerator;

	final private int size;

	public PossibleValuesIterable(ClassValueGenerator<T> classValueGenerator) {
		this.classValueGenerator = (ClassValueGenerator<T>) classValueGenerator.clone();

		int possibleValues = 1;
		for (ValueGenerator<?> valueGenerator : classValueGenerator.getObjectConstructor().getValueGenerators()) {
			possibleValues = possibleValues * valueGenerator.scope();
		}
		for (ValueGenerator<?> valueGenerator : classValueGenerator.getPropertySetter().getValueGenerators()) {
			possibleValues = possibleValues * valueGenerator.scope();
		}

		this.size = possibleValues;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private int currentPosition = 0;
			private ClassValueGenerator<T> classValueGenerator = (ClassValueGenerator<T>) PossibleValuesIterable.this.classValueGenerator
					.clone();

			@Override
			public boolean hasNext() {
				return currentPosition < size;
			}

			@Override
			public T next() {
				if (hasNext()) {
					currentPosition++;
					return classValueGenerator.generate();
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
