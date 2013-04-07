package com.stresstest.random.generator;

import java.util.Collection;

import com.stresstest.random.ValueGenerator;

/**
 * 
 * Factory for ValueGenerators
 * 
 * @author Anton Oparin
 * 
 */
public interface ValueGeneratorFactory {

	/**
	 * Produces {@link ValueGenerator} for specified {@link Class}.
	 * 
	 * @param klass
	 *            generated {@ling Class}
	 * @return {@link ValueGenerator} for procided {@link Class}
	 */
	public <T> ValueGenerator<T> getValueGenerator(Class<T> klass);

	/**
	 * Produces {@Collection} of {@link ValueGenerator} for provided {@link Class}es.
	 * 
	 * @param parameters
	 *            Collection of {@link Class}s to generate.
	 * @return {@link Collection} of {@link ValueGenerator} to use.
	 */
	public Collection<ValueGenerator<?>> getValueGenerators(Class<?>[] parameters);

}
