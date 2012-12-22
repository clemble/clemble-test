package com.stresstest.random.impl;

import java.util.ArrayList;
import java.util.List;

import com.stresstest.random.ValueGenerator;

public class ListValueGenerator<T> implements ValueGenerator<List<T>>{

    @Override
    public List<T> generate() {
        return new ArrayList<T>();
    }

}
