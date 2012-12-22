package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;

public class DoubleValueGenerator implements ValueGenerator<Double>{

    @Override
    public Double generate() {
        return RANDOM_UTILS.nextDouble();
    }

}
