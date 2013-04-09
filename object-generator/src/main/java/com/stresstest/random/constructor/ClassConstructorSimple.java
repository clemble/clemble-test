package com.stresstest.random.constructor;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.ValueGeneratorFactory;

/**
 * Generates new {@link Object}, based on constructor.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            {@link Class} parameter.
 */
public final class ClassConstructorSimple<T> extends ClassConstructor<T> {
	/**
	 * Constructor to use.
	 */
	final private Constructor<T> constructor;
	/**
	 * Set of values to generate parameters for the constructor.
	 */
	final private Collection<ValueGenerator<?>> constructorValueGenerators;

	/**
	 * Constructor based generation.
	 * 
	 * @param constructor
	 *            constructor to use.
	 * @param constructorValueGenerators
	 *            {@link ValueGenerator} to use.
	 */
	public ClassConstructorSimple(final Constructor<T> constructor, final Collection<ValueGenerator<?>> constructorValueGenerators) {
		this.constructor = checkNotNull(constructor);
		this.constructorValueGenerators = checkNotNull(constructorValueGenerators);
	}

	/**
	 * Getter of constructor.
	 * 
	 * @return constructor.
	 */
	public Constructor<T> getConstructor() {
		return constructor;
	}

	/**
	 * Returns a {@link Collection} of {@link ValueGenerator} to use, while construction.
	 * 
	 * @return {@link Collection} of {@link ValueGenerator} to use, while construction.
	 */
	public Collection<ValueGenerator<?>> getConstructorValueGenerators() {
		return constructorValueGenerators;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public T construct() {
		// Step 1. Generate value for Constructor
		Collection values = new ArrayList();
		for (ValueGenerator<?> valueGenerator : getConstructorValueGenerators())
			values.add(valueGenerator.generate());
		// Step 2. Invoke constructor, creating empty Object
		Object generatedObject = null;
		try {
			getConstructor().setAccessible(true);
			generatedObject = values.size() == 0 ? getConstructor().newInstance() : getConstructor()
					.newInstance(values.toArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return (T) generatedObject;
	}

	@Override
	public Collection<ValueGenerator<?>> getValueGenerators() {
		return constructorValueGenerators;
	}

	/**
	 * Tries to build {@link ClassConstructor} based on constructor.
	 * 
	 * @param classToGenerate
	 *            {@link Class} to generate.
	 * @param valueGeneratorFactory
	 *            {@link ValueGeneratorFactory} to use.
	 * @return {@link ClassConstructor} if it is possible to generate one, <code>null</code> otherwise.
	 */
	public static <T, R extends T> ClassConstructorSimple<T> build(final ClassAccessWrapper<?> classToGenerate,
			final ValueGeneratorFactory valueGeneratorFactory) {
		Constructor<?>[] constructors = classToGenerate.getConstructors();
		// Step 1. Selecting appropriate constructor
		if (constructors.length == 0)
			return null;
		// Step 2. Searching for default Constructor or constructor with least configurations
		Constructor<?> bestCandidate = null;
		// Step 2.1 Filtering classes with parameters that can be cast to constructed class
		Collection<Constructor<?>> filteredConstructors = Collections2.filter(Arrays.asList(constructors),
				new Predicate<Constructor<?>>() {
					@Override
					public boolean apply(final Constructor<?> input) {
						for (Class<?> parameter : input.getParameterTypes())
							if (classToGenerate.canBeReplacedWith(parameter) || classToGenerate.canReplace(parameter))
								return false;
						return true;
					}
				});
		// Step 3. Selecting constructor that would best fit for processing
		for (Constructor<?> constructor : filteredConstructors) {
			if (bestCandidate == null || constructor.getParameterTypes().length > bestCandidate.getParameterTypes().length) {
				bestCandidate = constructor;
			}
		}
		// Step 4. Returning selected constructor
		if (bestCandidate != null) {
			// Step 4.1 Choosing generators for Constructor variable
			return new ClassConstructorSimple<T>((Constructor<T>) bestCandidate,
					valueGeneratorFactory.getValueGenerators(bestCandidate.getParameterTypes()));
		}
		// Step 4.2 Returning default null value
		return null;
	}

}