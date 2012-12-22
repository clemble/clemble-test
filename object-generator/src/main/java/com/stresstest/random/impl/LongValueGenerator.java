package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;


public class LongValueGenerator implements ValueGenerator<Long> {

    @Override
    public Long generate() {
        return RANDOM_UTILS.nextLong();
    }

}
