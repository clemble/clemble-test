package com.stresstest.random.impl;

import java.util.ArrayDeque;
import java.util.Queue;

import com.stresstest.random.ValueGenerator;

public class QueueValueGenerator<T> implements ValueGenerator<Queue<T>>{

    @Override
    public Queue<T> generate() {
        return new ArrayDeque<T>();
    }

}
