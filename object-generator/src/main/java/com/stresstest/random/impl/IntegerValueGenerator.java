package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;


public class IntegerValueGenerator implements ValueGenerator<Integer> {

    @Override
    public Integer generate() {
        return RANDOM_UTILS.nextInt();
    }

}
