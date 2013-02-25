package com.stresstest.concurrent;

/**
 * Value Generator, that MUST creates new empty value on every invocation.
 * 
 * @author Anton Oparin
 * 
 * @param <T> Class parameter
 */
public interface ValueFactory<T> {

    /**
     * Creates new empty Object
     * 
     * @return new empty Object
     */
    public T create();

}
