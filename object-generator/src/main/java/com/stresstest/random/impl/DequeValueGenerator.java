package com.stresstest.random.impl;

import java.util.ArrayDeque;
import java.util.Deque;

import com.stresstest.random.ValueGenerator;

public class DequeValueGenerator<T> implements ValueGenerator<Deque<T>>{

    @Override
    public Deque<T> generate() {
        return new ArrayDeque<T>();
    }

}
