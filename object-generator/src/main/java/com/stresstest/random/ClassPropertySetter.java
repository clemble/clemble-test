package com.stresstest.random;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Combination of {@link ValueSetter} for specified {@link Class}.
 * 
 * @author Anton Oparin
 *
 * @param <T> parameterized {@link Class}.
 */
public class ClassPropertySetter<T> {

    /**
     * Source {@link Colleciton} of {@link ValueSetter}.
     */
    final private Collection<ValueSetter<?>> propertySetters;

    /**
     * Default constructor
     * 
     * @param propertySetters source {@link Collection} of {@link ValueSetter}.
     */
    public ClassPropertySetter(final Collection<ValueSetter<?>> propertySetters) {
        this.propertySetters = ImmutableList.<ValueSetter<?>>copyOf(propertySetters);
    }

    /**
     * Set's all properties from the {@link List}.
     * 
     * @param generatedObject target {@link Object}.
     */
    public void setProperties(Object generatedObject) {
        // Step 1. Call set methods for collections
        for(ValueSetter<?> propertySetter: propertySetters) {
            propertySetter.setValue(generatedObject);
        }
    }

}
