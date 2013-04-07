package com.stresstest.random;

import com.stresstest.random.generator.RandomValueGenerator;

/**
 * Value generator abstraction generates random T value.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            class parameter type to generate.
 */
public abstract class ValueGenerator<T> {

	/**
	 * Generates random T value.
	 * 
	 * @return random T value
	 */
	abstract public T generate();

	/**
	 * Generates constant value generator, which brings no randomness in generation.
	 * 
	 * @param constant
	 *            final variable to generate.
	 * @return ValueGenerator that returns constant value for each call.
	 */
	final public static <T> ValueGenerator<T> constantValueGenerator(final T constant) {
		return new RandomValueGenerator<T>() {
			@Override
			public T generate() {
				return constant;
			}
		};
	}

}
