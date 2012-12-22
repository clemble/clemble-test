package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;


public class ByteValueGenerator implements ValueGenerator<Byte> {

    @Override
    public Byte generate() {
        return (byte) RANDOM_UTILS.nextInt();
    }

}
