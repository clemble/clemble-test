package com.stresstest.random.impl;

import java.util.HashMap;
import java.util.Map;

import com.stresstest.random.ValueGenerator;

public class MapValueGenerator<K, V> implements ValueGenerator<Map<K, V>>{

    @Override
    public Map<K, V> generate() {
        return new HashMap<K, V>();
    }

}
