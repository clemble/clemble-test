package com.stresstest.random.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.stresstest.random.ValueGenerator;

public class RandomValueSelector<T> implements ValueGenerator<T> {

    final private List<T> values;

    public RandomValueSelector(T[] objectValues) {
        this(Arrays.asList(objectValues));
    }
    
    public RandomValueSelector(Collection<T> values) {
        this.values = new ArrayList<T>(values);
    }

    @Override
    public T generate() {
        return values.get(RANDOM_UTILS.nextInt(values.size()));
    }

}
