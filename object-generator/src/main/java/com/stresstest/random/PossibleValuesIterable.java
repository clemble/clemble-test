package com.stresstest.random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
			List<ValueGenerator<?>> constructorValueGenerators = sequentialValueGeneratorFactory.replace(classConstructor
					.getValueGenerators());
			constructorValueGenerators = ValueGeneratorWrapper.convert(constructorValueGenerators);
			classConstructor = classConstructor.clone(new ArrayList<ValueGenerator<?>>(constructorValueGenerators));
			// Step 2. Replacing valueGenerators for properties
			ClassPropertySetter<T> classPropertySetter = classValueGenerator.getPropertySetter();
			List<ValueGenerator<?>> propertyValueGenerator = sequentialValueGeneratorFactory.replace(classPropertySetter.getValueGenerators());
			propertyValueGenerator = ValueGeneratorWrapper.convert(propertyValueGenerator);
			classPropertySetter.clone(new ArrayList<ValueGenerator<?>>(propertyValueGenerator));
			// Step 4. Linking all generator wrappers
			ValueGeneratorWrapper<?> previous = null;
			for(ValueGenerator<?> valueGenerator: constructorValueGenerators) {
				((ValueGeneratorWrapper<?>) valueGenerator).setPrevious(previous);
				previous = (ValueGeneratorWrapper<?>) valueGenerator;
			}
			for(ValueGenerator<?> valueGenerator: propertyValueGenerator) {
				((ValueGeneratorWrapper<?>) valueGenerator).setPrevious(previous);
				previous = (ValueGeneratorWrapper<?>) valueGenerator;
			}
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
	
	public static class ValueGeneratorWrapper<T> implements ValueGenerator<T> {
		final private ValueGenerator<T> delegate;

		private int currentPosition = 0;
		private T currentValue;
		
		private ValueGeneratorWrapper<T> next;
		private ValueGeneratorWrapper<T> previous;
		
		private ValueGeneratorWrapper(ValueGenerator<T> valueGenerator) {
			this.delegate = valueGenerator;
		}

		@SuppressWarnings("unchecked")
		public void setPrevious(ValueGeneratorWrapper<?> previous) {
			this.previous = (ValueGeneratorWrapper<T>) previous;
		}

		public static <T> ValueGeneratorWrapper<T> convert(ValueGenerator<T> valueGenerator) {
			// Step 1. Sanity check
			if(valueGenerator instanceof ValueGeneratorWrapper) {
				return (ValueGeneratorWrapper<T>) valueGenerator;
			}
			// Step 2. Constructing new ValueGeneratorWrapper
			return new ValueGeneratorWrapper<T>(valueGenerator);
		}
		
		public static List<ValueGenerator<?>> convert(Collection<ValueGenerator<?>> valueGenerators) {
			List<ValueGenerator<?>> resultValueGenerators = new ArrayList<ValueGenerator<?>>();
			// Step 1. Converting all value generators to a list of new ValueGenerators
			for(ValueGenerator<?> valueGenerator: valueGenerators)
				resultValueGenerators.add(convert(valueGenerator));
			return resultValueGenerators;
		}

		@Override
		public T generate() {
			if (previous == null) {
				next();
			}
			return currentValue;
		}
		
		public void next(){
			currentPosition++;
			currentValue = delegate.generate();
			if (currentPosition % delegate.scope() == 0) {
				if(next != null)
					next.next();
			}
		}

		@Override
		public int scope() {
			return delegate.scope();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public ValueGeneratorWrapper<T> clone(){
			try {
				return (ValueGeneratorWrapper<T>) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private int currentPosition = 0;
			private ValueGenerator<T> valueGenerator = PossibleValuesIterable.this.valueGenerator
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
