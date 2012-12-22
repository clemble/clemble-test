package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;

public class BooleanValueGenerator implements ValueGenerator<Boolean> {

    @Override
    public Boolean generate() {
        return RANDOM_UTILS.nextBoolean();
    }

}
