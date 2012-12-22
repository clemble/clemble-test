package com.stresstest.random;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.reflections.Reflections;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.stresstest.random.impl.BooleanValueGenerator;
import com.stresstest.random.impl.ByteValueGenerator;
import com.stresstest.random.impl.CharValueGenerator;
import com.stresstest.random.impl.ClassConstructor;
import com.stresstest.random.impl.ClassPropertySetter;
import com.stresstest.random.impl.ClassValueGenerator;
import com.stresstest.random.impl.CollectionValueGenerator;
import com.stresstest.random.impl.DequeValueGenerator;
import com.stresstest.random.impl.DoubleValueGenerator;
import com.stresstest.random.impl.FloatValueGenerator;
import com.stresstest.random.impl.IntegerValueGenerator;
import com.stresstest.random.impl.ListValueGenerator;
import com.stresstest.random.impl.LongValueGenerator;
import com.stresstest.random.impl.MapValueGenerator;
import com.stresstest.random.impl.QueueValueGenerator;
import com.stresstest.random.impl.RandomValueSelector;
import com.stresstest.random.impl.SetValueGenerator;
import com.stresstest.random.impl.ShortValueGenerator;
import com.stresstest.random.impl.StringValueGenerator;

public class SimpleValueGeneratorFactory implements ValueGeneratorFactory {

    @SuppressWarnings("rawtypes")
    final private Map<Class, ValueGenerator> standardValueGenerators = new HashMap<Class, ValueGenerator>();
    {
        standardValueGenerators.put(String.class, new StringValueGenerator());

        standardValueGenerators.put(Boolean.class, new BooleanValueGenerator());
        standardValueGenerators.put(boolean.class, new BooleanValueGenerator());

        standardValueGenerators.put(Byte.class, new ByteValueGenerator());
        standardValueGenerators.put(byte.class, new ByteValueGenerator());
        standardValueGenerators.put(Character.class, new CharValueGenerator());
        standardValueGenerators.put(char.class, new CharValueGenerator());
        standardValueGenerators.put(Short.class, new ShortValueGenerator());
        standardValueGenerators.put(short.class, new ShortValueGenerator());
        standardValueGenerators.put(Integer.class, new IntegerValueGenerator());
        standardValueGenerators.put(int.class, new IntegerValueGenerator());
        standardValueGenerators.put(Long.class, new LongValueGenerator());
        standardValueGenerators.put(long.class, new LongValueGenerator());

        standardValueGenerators.put(Float.class, new FloatValueGenerator());
        standardValueGenerators.put(float.class, new FloatValueGenerator());
        standardValueGenerators.put(Double.class, new DoubleValueGenerator());
        standardValueGenerators.put(double.class, new DoubleValueGenerator());

        standardValueGenerators.put(Collection.class, new CollectionValueGenerator());
        standardValueGenerators.put(List.class, new ListValueGenerator());
        standardValueGenerators.put(Set.class, new SetValueGenerator());
        standardValueGenerators.put(Queue.class, new QueueValueGenerator());
        standardValueGenerators.put(Deque.class, new DequeValueGenerator());

        standardValueGenerators.put(Map.class, new MapValueGenerator());
    }

    final private Predicate<Class<?>> INTERFACE = new Predicate<Class<?>>() {
        @Override
        public boolean apply(Class<?> inputClass) {
            return (inputClass.getModifiers() & (Modifier.INTERFACE)) != 0;
        }
    };

    @Override
    public <T> ValueGenerator<T> getValueGenerator(Class<T> klass) {
        if (standardValueGenerators.containsKey(klass))
            return standardValueGenerators.get(klass);
        return initiateValueGenerator(klass);
    }

    public <T> ValueGenerator<T> initiateValueGenerator(Class key) {
        if (key.isEnum()) {
            return new RandomValueSelector(key.getEnumConstants());
        }
        try {
            return constructValueGenerator(key, ImmutableList.<Class<?>> of());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> ValueGenerator<T> constructValueGenerator(Class<T> classToGenerate, Collection<Class<?>> candidates) throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, ExecutionException {
        // Step 1. Trying to generate based on provided class
        ValueGenerator<T> instance = constructValueGenerator(classToGenerate);
        if (instance != null)
            return instance;
        // Step 2. Checking all candidate implementations
        for (Class<?> candidate : candidates) {
            if ((instance = constructValueGenerator(candidate)) != null)
                return instance;
        }
        // Step 3 If class is abstract substitute it with real implementation in the same package or sub packages
        Collection<Class<? extends T>> filteredClasses = getPossibleImplementations(classToGenerate);
        filteredClasses.removeAll(candidates);
        if (filteredClasses.size() > 0) {
            // Step 3.1 Checking extended list of candidates
            Collection<Class<?>> allPossibleCandidates = new ArrayList(candidates);
            allPossibleCandidates.addAll(filteredClasses);
            return constructValueGenerator(classToGenerate, (Collection<Class<?>>) (Collection<?>) allPossibleCandidates);
        } else {
            // Step 3.2 Checking extended list of candidates
            for (Class<?> candidate : candidates) {
                Collection<Class<?>> candidateSubClasses = (Collection<Class<?>>) (Collection<?>) getPossibleImplementations(candidate);
                if (candidateSubClasses.size() > 0)
                    try {
                        constructValueGenerator(candidate, candidateSubClasses);
                    } catch (IllegalArgumentException illegalArgumentException) {
                        // Ignore check next implementation
                    }
            }
        }
        throw new IllegalArgumentException();
    }

    private <T> Collection<Class<? extends T>> getPossibleImplementations(Class<T> classToGenerate) {
        Reflections reflections = new Reflections(classToGenerate.getPackage().toString().replace("package ", ""));
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(classToGenerate);
        return subTypes;
    }

    private <T> ValueGenerator<T> constructValueGenerator(final ClassReflectionAccessWrapper<T> classToGenerate) throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, ExecutionException {
        // Step 1. Selecting appropriate constructor
        ClassConstructor<T> objectConstructor = ClassConstructor.construct(classToGenerate, this);
        if (objectConstructor == null)
            return null;
        // Step 2. Selecting list of applicable specific selectors from specific properties
        Collection<PropertySetter<?>> propertySetters = PropertySetter.create(classToGenerate);
        ClassPropertySetter<T> classPropertySetter = new ClassPropertySetter(propertySetters);
        // Step 3. Generating final ClassGenerator for the type
        return new ClassValueGenerator<T>(objectConstructor, classPropertySetter);
    }

    private <T> ValueGenerator<T> constructValueGenerator(Class<?> classToGenerate) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, ExecutionException {
        // Step 1. Checking abstract class
        if (INTERFACE.apply(classToGenerate))
            return null;
        ValueGenerator<T> resultGenerator = (ValueGenerator<T>) constructValueGenerator(ClassReflectionAccessWrapper.createPublicAccessor(classToGenerate));
        if (resultGenerator != null)
            return resultGenerator;
        return (ValueGenerator<T>) constructValueGenerator(ClassReflectionAccessWrapper.createAllMethodsAccessor(classToGenerate));
    }

}