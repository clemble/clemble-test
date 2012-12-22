package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;


public class CharValueGenerator implements ValueGenerator<Character> {

    @Override
    public Character generate() {
        return (char) RANDOM_UTILS.nextInt((int) Character.MAX_VALUE);
    }

}
