package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;

public class ConstantValueGenerator<T> implements ValueGenerator<T> {

    final private T constant;

    public ConstantValueGenerator(T theConstant) {
        this.constant = theConstant;
    }

    @Override
    public T generate() {
        return constant;
    }

}
