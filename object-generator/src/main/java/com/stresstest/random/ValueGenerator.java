package com.stresstest.random;

/**
 * Value generator abstraction generates random T value.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            class parameter type to generate.
 */
public interface ValueGenerator<T> {

	/**
	 * Generates random T value.
	 * 
	 * @return random T value
	 */
	public T generate();
	
	public int scope();

	public ValueGenerator<T> clone();

}
