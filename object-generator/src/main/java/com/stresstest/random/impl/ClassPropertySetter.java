package com.stresstest.random.impl;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.stresstest.random.PropertySetter;

/**
 * Combination of {@link PropertySetter} for specified {@link Class}.
 * 
 * @author Anton Oparin
 *
 * @param <T> parameterized {@link Class}.
 */
public class ClassPropertySetter<T> {

    /**
     * Source {@link Colleciton} of {@link PropertySetter}.
     */
    final private Collection<PropertySetter<?>> propertySetters;

    /**
     * Default constructor
     * 
     * @param propertySetters source {@link Collection} of {@link PropertySetter}.
     */
    public ClassPropertySetter(final Collection<PropertySetter<?>> propertySetters) {
        this.propertySetters = ImmutableList.<PropertySetter<?>>copyOf(propertySetters);
    }

    /**
     * Set's all properties from the {@link List}.
     * 
     * @param generatedObject target {@link Object}.
     */
    public void setProperties(Object generatedObject) {
        // Step 1. Call set methods for collections
        for(PropertySetter<?> propertySetter: propertySetters) {
            propertySetter.setProperty(generatedObject);
        }
    }

}
