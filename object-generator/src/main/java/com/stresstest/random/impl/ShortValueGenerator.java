package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;


public class ShortValueGenerator implements ValueGenerator<Short> {

    @Override
    public Short generate() {
        return (short) RANDOM_UTILS.nextInt();
    }

}
