package com.stresstest.random.impl;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.stresstest.random.PropertySetter;

public class ClassPropertySetter<T> {

    final private Collection<PropertySetter<?>> propertySetters;

    public ClassPropertySetter(final Collection<PropertySetter<?>> propertySetters) {
        this.propertySetters = ImmutableList.<PropertySetter<?>>copyOf(propertySetters);
    }

    public void setProperties(Object generatedObject) {
        // Step 1. Call set methods for collections
        for(PropertySetter<?> propertySetter: propertySetters) {
            propertySetter.apply(generatedObject);
        }
    }

}
