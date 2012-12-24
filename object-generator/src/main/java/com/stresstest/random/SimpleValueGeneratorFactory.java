package com.stresstest.random;

import static com.stresstest.random.ValueGenerator.BOOLEAN_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.BYTE_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.CHAR_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.COLLECTION_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.DEQUE_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.DOUBLE_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.FLOAT_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.INTEGER_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.LIST_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.LONG_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.MAP_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.QUEUE_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.SET_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.SHORT_VALUE_GENERATOR;
import static com.stresstest.random.ValueGenerator.STRING_VALUE_GENERATOR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.stresstest.random.impl.ClassConstructor;
import com.stresstest.random.impl.ClassPropertySetter;
import com.stresstest.random.impl.ClassValueGenerator;
import com.stresstest.runners.ReflectionUtils;

public class SimpleValueGeneratorFactory implements ValueGeneratorFactory {

    @SuppressWarnings("rawtypes")
    final private Map<Class, ValueGenerator> standardValueGenerators = new HashMap<Class, ValueGenerator>();
    {
        standardValueGenerators.put(String.class, STRING_VALUE_GENERATOR);

        standardValueGenerators.put(Boolean.class, BOOLEAN_VALUE_GENERATOR);
        standardValueGenerators.put(boolean.class, BOOLEAN_VALUE_GENERATOR);

        standardValueGenerators.put(Byte.class, BYTE_VALUE_GENERATOR);
        standardValueGenerators.put(byte.class, BYTE_VALUE_GENERATOR);
        standardValueGenerators.put(Character.class, CHAR_VALUE_GENERATOR);
        standardValueGenerators.put(char.class, CHAR_VALUE_GENERATOR);
        standardValueGenerators.put(Short.class, SHORT_VALUE_GENERATOR);
        standardValueGenerators.put(short.class, SHORT_VALUE_GENERATOR);
        standardValueGenerators.put(Integer.class, INTEGER_VALUE_GENERATOR);
        standardValueGenerators.put(int.class, INTEGER_VALUE_GENERATOR);
        standardValueGenerators.put(Long.class, LONG_VALUE_GENERATOR);
        standardValueGenerators.put(long.class, LONG_VALUE_GENERATOR);

        standardValueGenerators.put(Float.class, FLOAT_VALUE_GENERATOR);
        standardValueGenerators.put(float.class, FLOAT_VALUE_GENERATOR);
        standardValueGenerators.put(Double.class, DOUBLE_VALUE_GENERATOR);
        standardValueGenerators.put(double.class, DOUBLE_VALUE_GENERATOR);

        standardValueGenerators.put(Collection.class, COLLECTION_VALUE_GENERATOR);
        standardValueGenerators.put(List.class, LIST_VALUE_GENERATOR);
        standardValueGenerators.put(Set.class, SET_VALUE_GENERATOR);
        standardValueGenerators.put(Queue.class, QUEUE_VALUE_GENERATOR);
        standardValueGenerators.put(Deque.class, DEQUE_VALUE_GENERATOR);

        standardValueGenerators.put(Map.class, MAP_VALUE_GENERATOR);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ValueGenerator<T> getValueGenerator(Class<T> klass) {
        // Step 1. Checking that it can be replaced with standard constructors
        if (standardValueGenerators.containsKey(klass))
            return standardValueGenerators.get(klass);
        // Step 2. If this is enum replace with Random value generator
        if (klass.isEnum())
            return (ValueGenerator<T>) ValueGenerator.randomValueGenerator(Arrays.asList(klass.getEnumConstants()));
        // Step 3. Initialize value generator
        ValueGenerator<T> valueGenerator = tryConstruct(klass);
        if (valueGenerator != null)
            return valueGenerator;
        // Step 3.1 Trying to initialize sub classes
        Collection<Class<? extends T>> subClasses = ReflectionUtils.getPossibleImplementations(klass);
        // Step 3.2 Checking extended list of candidates
        for (Class<?> subClass : subClasses) {
            valueGenerator = tryConstruct(subClass);
            if (valueGenerator != null)
                return valueGenerator;
        }
        throw new IllegalArgumentException();
    }

    @SuppressWarnings("unchecked")
    private <T> ValueGenerator<T> tryConstruct(Class<?> classToGenerate) {
        // Step 1. Checking abstract class
        if ((classToGenerate.getModifiers() & (Modifier.INTERFACE)) != 0)
            return null;
        // Step 2. Generating Class value generator with public access only
        ClassReflectionAccessWrapper<T> accessWrapper = (ClassReflectionAccessWrapper<T>) ClassReflectionAccessWrapper.createPublicAccessor(classToGenerate);
        ValueGenerator<T> resultGenerator = tryConstruct(accessWrapper);
        if (resultGenerator != null)
            return resultGenerator;
        // Step 3. Generating Class value generator with access to all methods
        accessWrapper = (ClassReflectionAccessWrapper<T>) ClassReflectionAccessWrapper.createAllMethodsAccessor(classToGenerate);
        return tryConstruct(accessWrapper);
    }

    private <T> ValueGenerator<T> tryConstruct(final ClassReflectionAccessWrapper<T> classToGenerate) {
        // Step 1. Selecting appropriate constructor
        ClassConstructor<T> objectConstructor = ClassConstructor.construct(classToGenerate, this);
        if (objectConstructor == null)
            return null;
        // Step 2. Selecting list of applicable specific selectors from specific properties
        Collection<PropertySetter<?>> propertySetters = PropertySetter.extractAvailableProperties(classToGenerate);
        ClassPropertySetter<T> classPropertySetter = new ClassPropertySetter<T>(propertySetters);
        // Step 3. Generating final ClassGenerator for the type
        return new ClassValueGenerator<T>(objectConstructor, classPropertySetter);
    }

}