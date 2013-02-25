package com.stresstest.concurrent;

/**
 * ThreadLocal wrapper, needed to optimize Object creation on empty Objects.
 * 
 * @author Anton Oparin
 * 
 * @param <T> Class parameter
 */
public class ThreadLocalFactory<T> extends ThreadLocal<T> {

    /**
     * Value Generator to use
     */
    final private ValueFactory<T> valueFactory;

    /**
     * Default constructor
     * 
     * @param usedValueFactory targetValueGenerator
     */
    public ThreadLocalFactory(ValueFactory<T> usedValueFactory) {
        this.valueFactory = usedValueFactory;
    }

    @Override
    public T initialValue() {
        return valueFactory.create();
    }
}
