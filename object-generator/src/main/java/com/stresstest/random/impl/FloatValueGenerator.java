package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;

public class FloatValueGenerator implements ValueGenerator<Float> {

    @Override
    public Float generate() {
        return RANDOM_UTILS.nextFloat();
    }

}
