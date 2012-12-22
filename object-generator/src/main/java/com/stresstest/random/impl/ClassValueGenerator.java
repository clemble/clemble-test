package com.stresstest.random.impl;

import com.stresstest.random.ValueGenerator;

public class ClassValueGenerator<T> implements ValueGenerator<T> {

    final private ClassConstructor<T> objectConstructor;
    final private ClassPropertySetter<T> propertySetter;

    public ClassValueGenerator(final ClassConstructor<T> objectConstructor, final ClassPropertySetter<T> propertySetter) {
        this.objectConstructor = objectConstructor;
        this.propertySetter = propertySetter;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public T generate() {
        // Step 1. Generating random Object
        Object generatedObject = objectConstructor.create();
        // Step 2. Setting properties to the Object
        propertySetter.setProperties(generatedObject);
        // Step 3. Generated Object can be used
        return (T) generatedObject;
    }

    public ClassConstructor<T> getObjectConstructor() {
        return objectConstructor;
    }

    public ClassPropertySetter<T> getPropertySetter() {
        return propertySetter;
    }

}
