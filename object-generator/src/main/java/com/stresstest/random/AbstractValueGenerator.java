package com.stresstest.random;

abstract public class AbstractValueGenerator<T> implements ValueGenerator<T>, Cloneable {
	
	public int scope() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	public ValueGenerator<T> clone() {
		try {
			return (ValueGenerator<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
