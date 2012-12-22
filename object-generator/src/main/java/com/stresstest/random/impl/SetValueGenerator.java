package com.stresstest.random.impl;

import java.util.HashSet;
import java.util.Set;

import com.stresstest.random.ValueGenerator;

public class SetValueGenerator<T> implements ValueGenerator<Set<T>>{

    @Override
    public Set<T> generate() {
        return new HashSet<T>();
    }

}
