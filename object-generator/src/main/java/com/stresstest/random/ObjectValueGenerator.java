package com.stresstest.random;

import static com.google.common.base.Preconditions.checkNotNull;



/**
 * Generates random value of specified Class.
 * 
 * @author Anton Oparin
 *
 * @param <T> parameterized {@link Class}.
 */
public class ObjectValueGenerator<T> extends ValueGenerator<T> {

    /**
     * {@link ClassConstructor} used for value construction.
     */
    final private ClassConstructor<T> objectConstructor;
    /**
     * {@link CombinedClassPropertySetter} used for properties population.
     */
    final private ClassPropertySetter<T> propertySetter;

    /**
     * Default constructor.
     * 
     * @param objectConstructor {@link ClassConstructor} to use.
     * @param propertySetter {@link CombinedClassPropertySetter} to use.
     */
    public ObjectValueGenerator(final ClassConstructor<T> objectConstructor, final ClassPropertySetter<T> propertySetter) {
        this.objectConstructor = checkNotNull(objectConstructor);
        this.propertySetter = checkNotNull(propertySetter);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public T generate() {
        // Step 1. Generating random Object
        Object generatedObject = objectConstructor.construct();
        // Step 2. Setting properties to the Object
        propertySetter.setProperties(generatedObject);
        // Step 3. Generated Object can be used
        return (T) generatedObject;
    }

    /**
     * Returns associated {@link ClassConstructor}.
     * 
     * @return associated {@link ClassConstructor}.
     */
    public ClassConstructor<T> getObjectConstructor() {
        return objectConstructor;
    }

    /**
     * Returns associated {@link CombinedClassPropertySetter}.
     * 
     * @return associated {@link CombinedClassPropertySetter}.
     */
    public ClassPropertySetter<T> getPropertySetter() {
        return propertySetter;
    }

}
