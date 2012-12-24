package com.stresstest.random.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.stresstest.random.ClassReflectionAccessWrapper;
import com.stresstest.random.PropertySetter;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.ValueGeneratorFactory;

abstract public class ClassConstructor<T> {

    abstract public T create();

    final protected static Collection<ValueGenerator<?>> createValueGenerators(Class<?>[] parameters, ValueGeneratorFactory valueGeneratorFactory) {
        Collection<ValueGenerator<?>> valueGenerators = new ArrayList<ValueGenerator<?>>();
        for (Class<?> parameter : parameters) {
            valueGenerators.add(valueGeneratorFactory.getValueGenerator(parameter));
        }
        return valueGenerators;
    }

    public final static class ConstructorBasedConstructor<T> extends ClassConstructor<T> {
        final private Constructor<T> constructor;
        final private Collection<ValueGenerator<?>> constructorValueGenerators;

        public ConstructorBasedConstructor(Constructor<T> constructor, Collection<ValueGenerator<?>> constructorValueGenerators) {
            this.constructor = constructor;
            this.constructorValueGenerators = constructorValueGenerators;
        }

        public Constructor<T> getConstructor() {
            return constructor;
        }

        public Collection<ValueGenerator<?>> getConstructorValueGenerators() {
            return constructorValueGenerators;
        }

        @Override
        public T create() {
            // Step 1. Generate value for Constructor
            Collection values = new ArrayList();
            for (ValueGenerator<?> valueGenerator : getConstructorValueGenerators())
                values.add(valueGenerator.generate());
            // Step 2. Invoke constructor, creating empty Object
            Object generatedObject = null;
            try {
                getConstructor().setAccessible(true);
                generatedObject = values.size() == 0 ? getConstructor().newInstance() : getConstructor().newInstance(values.toArray());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return (T) generatedObject;
        }

        protected static <T, R extends T> ConstructorBasedConstructor<T> check(final ClassReflectionAccessWrapper<?> classToGenerate, final ValueGeneratorFactory valueGeneratorFactory) {
            Constructor<?>[] constructors = classToGenerate.getConstructors();
            // Step 1. Selecting appropriate constructor
            if (constructors.length == 0)
                return null;
            // Step 2. Searching for default Constructor or constructor with least configurations
            Constructor<?> bestCandidate = null;
            // Step 2.1 Filtering classes with parameters that can be cast to constructed class
            Collection<Constructor<?>> filteredConstructors = Collections2.filter(Arrays.asList(constructors), new Predicate<Constructor<?>>() {
                @Override
                public boolean apply(final Constructor<?> input) {
                    for (Class<?> parameter : input.getParameterTypes())
                        if (classToGenerate.canBeReplacedWith(parameter) || classToGenerate.canReplace(parameter))
                            return false;
                    return true;
                }
            });
            // Step 3. Selecting constructor that would best fit for processing
            for (Constructor<?> constructor : filteredConstructors) {
                if (bestCandidate == null || constructor.getParameterTypes().length > bestCandidate.getParameterTypes().length) {
                    bestCandidate = constructor;
                }
            }
            // Step 4. Returning selected constructor
            if (bestCandidate != null) {
                // Step 4.1 Choosing generators for Constructor variable
                return new ConstructorBasedConstructor<T>((Constructor<T>) bestCandidate, createValueGenerators(bestCandidate.getParameterTypes(),
                        valueGeneratorFactory));
            }
            // Step 4.2 Returning default null value
            return null;
        }
    }

    final public static class FactoryMethodBasedConstructor<T> extends ClassConstructor<T> {
        final private Method builder;
        final private Collection<ValueGenerator<?>> constructorValueGenerators;

        public FactoryMethodBasedConstructor(Method builder, Collection<ValueGenerator<?>> constructorValueGenerators) {
            this.builder = builder;
            this.constructorValueGenerators = constructorValueGenerators;
        }

        @Override
        public T create() {
            // Step 1. Generate value for Constructor
            Collection values = new ArrayList();
            for (ValueGenerator<?> valueGenerator : constructorValueGenerators)
                values.add(valueGenerator.generate());
            // Step 2. Invoke constructor, creating empty Object
            Object generatedObject = null;
            try {
                builder.setAccessible(true);
                generatedObject = values.size() == 0 ? builder.invoke(null, ImmutableList.of().toArray()) : builder.invoke(null, values.toArray());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return (T) generatedObject;
        }

        public static <T> FactoryMethodBasedConstructor<T> check(final ClassReflectionAccessWrapper<?> classToGenerate, final ValueGeneratorFactory valueGeneratorFactory) {
            // Step 1. Filter static methods, that return instance of the type as a result
            Collection<Method> possibleBuilders = Collections2.filter(classToGenerate.getMethods(), new Predicate<Method>() {
                @Override
                public boolean apply(final Method method) {
                    if ((method.getModifiers() & Modifier.STATIC) == 0 || !classToGenerate.canBeReplacedWith(method.getReturnType()))
                        return false;
                    for (Class<?> parameter : method.getParameterTypes()) {
                        if (classToGenerate.canBeReplacedWith(parameter) || classToGenerate.canReplace(parameter))
                            return false;
                    }
                    return true;
                }
            });
            // Step 2. If there is no such method return null
            if (possibleBuilders.size() == 0)
                return null;
            // Step 3. Select constructor with most arguments
            Method builder = null;
            for (Method candidate : possibleBuilders)
                if (builder == null || candidate.getParameterTypes().length > builder.getParameterTypes().length)
                    builder = candidate;
            // Step 4. Creating factory method based
            return new FactoryMethodBasedConstructor(builder, createValueGenerators(builder.getParameterTypes(), valueGeneratorFactory));
        }
    }

    public static class BuilderBasedConstructor<T> extends ClassConstructor<T> {

        final private ClassConstructor<?> builderFactoryMethod;
        final private ClassPropertySetter<?> classPropertySetter;
        final private Method valueBuilderMethod;

        private BuilderBasedConstructor(final ClassConstructor<?> builderFactoryMethod, final ClassPropertySetter<?> classPropertySetter,
                final Method valueBuilderMethod) {
            this.builderFactoryMethod = checkNotNull(builderFactoryMethod);
            this.classPropertySetter = checkNotNull(classPropertySetter);
            this.valueBuilderMethod = checkNotNull(valueBuilderMethod);

        }

        @Override
        public T create() {
            try {
                Object builder = builderFactoryMethod.create();
                classPropertySetter.setProperties(builder);
                valueBuilderMethod.setAccessible(true);
                return (T) valueBuilderMethod.invoke(builder, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static <T> BuilderBasedConstructor<T> check(final ClassReflectionAccessWrapper<?> classToGenerate,
                final ValueGeneratorFactory valueGeneratorFactory) {
            // Step 1. Filter static methods, that return instance of the type as a result
            Collection<Method> possibleBuilders = Collections2.filter(classToGenerate.getMethods(), new Predicate<Method>() {
                @Override
                public boolean apply(final Method method) {
                    if ((method.getModifiers() & Modifier.STATIC) == 0)
                        return false;
                    // Checking that returned type has methods, that return instance of target class
                    boolean builder = false;
                    for (Method builderMethod : classToGenerate.wrap(method.getReturnType()).getMethods()) {
                        if (builderMethod.getDeclaringClass() != Object.class)
                            builder = builder || classToGenerate.canBeReplacedWith(builderMethod.getReturnType());
                    }
                    if (!builder)
                        return false;
                    // Checking list of parameters
                    for (Class<?> methodArgument : method.getParameterTypes())
                        if (classToGenerate.canBeReplacedWith(methodArgument) || classToGenerate.canReplace(methodArgument))
                            return false;
                    return true;
                }
            });
            // Step 2. If there is no such method return null
            if (possibleBuilders.size() == 0)
                return null;
            // Step 3. Select constructor with most arguments
            Method builder = null;
            for (Method candidate : possibleBuilders)
                if (builder == null || candidate.getParameterTypes().length > builder.getParameterTypes().length)
                    builder = candidate;
            // Step 4. Selecting most factory method based
            ClassConstructor<T> builderMethod = new FactoryMethodBasedConstructor(builder, createValueGenerators(builder.getParameterTypes(),
                    valueGeneratorFactory));
            ClassPropertySetter<T> builderPropertySetter = new ClassPropertySetter<T>(PropertySetter.extractAvailableProperties(classToGenerate.wrap(builder.getReturnType())));

            Method valueBuilderMethod = null;
            for (Method constructorMethod : builder.getReturnType().getDeclaredMethods()) {
                if (classToGenerate.canBeReplacedWith(constructorMethod.getReturnType())) {
                    valueBuilderMethod = constructorMethod;
                }
            }

            return new BuilderBasedConstructor(builderMethod, builderPropertySetter, valueBuilderMethod);
        }
    }

    public static <T> ClassConstructor<T> construct(final ClassReflectionAccessWrapper<?> classToGenerate, final ValueGeneratorFactory valueGeneratorFactory) {
        ClassConstructor<T> objectConstructor = null;
        if ((objectConstructor = FactoryMethodBasedConstructor.check(classToGenerate, valueGeneratorFactory)) != null)
            return objectConstructor;
        if ((objectConstructor = BuilderBasedConstructor.check(classToGenerate, valueGeneratorFactory)) != null)
            return objectConstructor;
        return (ClassConstructor<T>) ((classToGenerate.getModifiers() & Modifier.ABSTRACT) == 0 ? ConstructorBasedConstructor.check(classToGenerate, valueGeneratorFactory) : null);
    }

}
