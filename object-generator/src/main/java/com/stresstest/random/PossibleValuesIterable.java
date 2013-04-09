package com.stresstest.random;

import java.util.Iterator;

import com.stresstest.random.constructor.ClassValueGenerator;

public class PossibleValuesIterable<T> implements Iterable<T> {

	final private ValueGenerator<T> classValueGenerator;

	final private int size;

	public PossibleValuesIterable(ValueGenerator<T> iValueGenerator) {
		this.classValueGenerator = (ClassValueGenerator<T>) iValueGenerator.clone();
		
		if(classValueGenerator instanceof ClassValueGenerator) {
			ClassValueGenerator<T> classValueGenerator = (ClassValueGenerator<T>) iValueGenerator;
			int possibleValues = 1;
			for (ValueGenerator<?> valueGenerator : classValueGenerator.getObjectConstructor().getValueGenerators()) {
				possibleValues = possibleValues * (valueGenerator.scope() != 0 ? valueGenerator.scope() : 1);
			}
			for (ValueGenerator<?> valueGenerator : classValueGenerator.getPropertySetter().getValueGenerators()) {
				possibleValues = possibleValues * (valueGenerator.scope() != 0 ? valueGenerator.scope() : 1);
			}
			
			this.size = possibleValues;
		} else {
			this.size = iValueGenerator.scope();
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private int currentPosition = 0;
			private ClassValueGenerator<T> valueGenerator = (ClassValueGenerator<T>) PossibleValuesIterable.this.classValueGenerator
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
