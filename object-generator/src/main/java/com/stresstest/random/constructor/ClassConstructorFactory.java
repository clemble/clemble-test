package com.stresstest.random.constructor;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.ValueGeneratorFactory;

/**
 * Generates new {@link Object}, based on factory method.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            {@link Class} parameter.
 */
final public class ClassConstructorFactory<T> extends ClassConstructor<T> {
    /**
     * Factory method to use.
     */
    final private Method builder;
    /**
     * {@link Collection} of {@link ValueGenerator} to use in factory method.
     */
    final private List<ValueGenerator<?>> constructorValueGenerators;

    /**
     * Default constructor.
     * 
     * @param builder
     *            factory method to use.
     * @param constructorValueGenerators
     *            {@link Collection} of {@link ValueGenerator} to use.
     */
    public ClassConstructorFactory(Method builder, Collection<ValueGenerator<?>> constructorValueGenerators) {
        this.builder = checkNotNull(builder);
        this.constructorValueGenerators = ImmutableList.<ValueGenerator<?>>copyOf(checkNotNull(constructorValueGenerators));
    }

    @Override
	@SuppressWarnings({"rawtypes", "unchecked"})
    public T construct() {
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

	@Override
	public List<ValueGenerator<?>> getValueGenerators() {
		return constructorValueGenerators;
	}

	@Override
	public ClassConstructor<T> clone(List<ValueGenerator<?>> generatorsToUse) {
		return new ClassConstructorFactory<T>(builder, generatorsToUse);
	}

    /**
     * Tries to build {@link ClassConstructor} based on factory method.
     * 
     * @param classToGenerate
     *            {@link Class} to generate.
     * @param valueGeneratorFactory
     *            {@link ValueGeneratorFactory} to use.
     * @return {@link ClassConstructor} if it is possible to generate one, <code>null</code> otherwise.
     */
    public static <T> ClassConstructorFactory<T> build(final ClassAccessWrapper<?> classToGenerate,
            final ValueGeneratorFactory valueGeneratorFactory) {
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
        return new ClassConstructorFactory<T>(builder, valueGeneratorFactory.getValueGenerators(builder.getParameterTypes()));
    }
    
}