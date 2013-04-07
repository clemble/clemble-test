package com.stresstest.random.generator;

import java.lang.reflect.Modifier;
import java.util.Collection;

import com.stresstest.random.ObjectValueGenerator;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.constructor.ClassConstructor;
import com.stresstest.random.constructor.ClassPropertySetter;
import com.stresstest.random.constructor.ClassAccessWrapper;
import com.stresstest.reflection.ReflectionUtils;

public class RandomValueGeneratorFactory extends AbstractValueGeneratorFactory {

    /**
     * Produces {@link ValueGenerator} for specified {@link Class}.
     * 
     * @param klass
     *            generated {@ling Class}
     * @return {@link ValueGenerator} for procided {@link Class}
     */
    @SuppressWarnings("unchecked")
    public <T> ValueGenerator<T> getValueGenerator(Class<T> klass) {
    	ValueGenerator<T> valueGenerator = getDefaultValueGenerator(klass);
    	if(valueGenerator != null)
    		return valueGenerator;
        // Step 1. Checking that it can be replaced with standard constructors
    	valueGenerator = (ValueGenerator<T>) RandomValueGenerator.DEFAULT_GENERATORS.get(klass);
        if (valueGenerator != null)
            return valueGenerator;
        // Step 2. If this is enum replace with Random value generator
        if (klass.isEnum())
            return (ValueGenerator<T>) RandomValueGenerator.enumValueGenerator(klass);
        // Step 3. Initialize value generator with primarily public access
        valueGenerator = construct(ClassAccessWrapper.createPublicAccessor(klass));
        if (valueGenerator != null)
            return valueGenerator;
        // Step 4. Trying to initialize with all available access
        valueGenerator = construct(ClassAccessWrapper.createAllMethodsAccessor(klass));
        if (valueGenerator != null)
            return valueGenerator;
        // Step 5. If there is no result throw IllegalArgumentException
        throw new IllegalArgumentException();
    }

    /**
     * Produces {@link ValueGenerator} for provided {@link ClassAccessWrapper}, returned {@link ValueGenerator} can return any subtype of the target
     * {@link Class}.
     * 
     * @param sourceClass
     *            {@link ClassAccessWrapper} with defined level of access.
     * @return {@link ValueGenerator} for provided class if, there is possible to create one.
     */
    @SuppressWarnings("unchecked")
    private <T> ValueGenerator<T> construct(ClassAccessWrapper<T> sourceClass) {
        ValueGenerator<T> valueGenerator = tryConstruct(sourceClass);
        if (valueGenerator != null)
            return valueGenerator;
        // Step 3.1 Trying to initialize sub classes
        Collection<Class<? extends T>> subClasses = ReflectionUtils.findPossibleImplementations(sourceClass.getSourceClass());
        // Step 3.2 Checking extended list of candidates
        for (Class<?> subClass : subClasses) {
            valueGenerator = (ValueGenerator<T>) tryConstruct(sourceClass.wrap(subClass));
            if (valueGenerator != null)
                return valueGenerator;
        }
        return null;
    }

    /**
     * Produces {@link ValueGenerator} for a {@link Class} with restricted access.
     * 
     * @param classToGenerate
     *            {@link ClassAccessWrapper} for the class.
     * @return {@link ValueGenerator} if it is possible to create one, with defined access level, <code>null</code> otherwise.
     */
    private <T> ValueGenerator<T> tryConstruct(final ClassAccessWrapper<T> classToGenerate) {
        // Step 0. Checking abstract class
        if ((classToGenerate.getModifiers() & (Modifier.INTERFACE)) != 0)
            return null;
        // Step 1. Selecting appropriate constructor
        ClassConstructor<T> objectConstructor = ClassConstructor.construct(classToGenerate, this);
        if (objectConstructor == null)
            return null;
        // Step 2. Selecting list of applicable specific selectors from specific properties
        ClassPropertySetter<T> classPropertySetter = ClassPropertySetter.constructPropertySetter(classToGenerate);
        // Step 3. Generating final ClassGenerator for the type
        return new ObjectValueGenerator<T>(objectConstructor, classPropertySetter);
    }

}
