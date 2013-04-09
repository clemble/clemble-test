package com.stresstest.random.constructor;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.stresstest.random.ValueGenerator;
import com.stresstest.random.ValueGeneratorFactory;

/**
 * Generates new {@link Object}, based on Builder.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            {@link Class} to use.
 */
public class ClassConstructorBuilder<T> extends ClassConstructor<T> {

	/**
	 * {@link ClassConstructor} builder method to use.
	 */
	final private ClassConstructorFactory<?> builderFactoryMethod;
	/**
	 * {@link ClassPropertyCombinedSetter} to use in builder class.
	 */
	final private ClassPropertySetter<?> classPropertySetter;
	/**
	 * Method that generates target value
	 */
	final private Method valueBuilderMethod;

	/**
	 * Default constructor.
	 * 
	 * @param builderFactoryMethod
	 *            factory method to construct builder.
	 * @param classPropertySetter
	 *            {@link Collection} of {@link ValueGenerator} to use as property setters.
	 * @param valueBuilderMethod
	 *            method to generate value.
	 */
	private ClassConstructorBuilder(final ClassConstructorFactory<?> builderFactoryMethod,
			final ClassPropertySetter<?> classPropertySetter, final Method valueBuilderMethod) {
		this.builderFactoryMethod = checkNotNull(builderFactoryMethod);
		this.classPropertySetter = checkNotNull(classPropertySetter);
		this.valueBuilderMethod = checkNotNull(valueBuilderMethod);

	}

	@Override
	@SuppressWarnings({"unchecked"})
	public T construct() {
		try {
			Object builder = builderFactoryMethod.construct();
			classPropertySetter.setProperties(builder);
			valueBuilderMethod.setAccessible(true);
			return ((T) valueBuilderMethod.invoke(builder, (Object[]) null));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ValueGenerator<?>> getValueGenerators() {
		return classPropertySetter.getValueGenerators();
	}
	
	public ClassConstructor<T> clone(List<ValueGenerator<?>> generatorsToUse) {
		return new ClassConstructorBuilder<T>(builderFactoryMethod, classPropertySetter.clone(generatorsToUse), valueBuilderMethod);
	}

	/**
	 * Tries to build {@link ClassConstructor} based on Builder class.
	 * 
	 * @param classToGenerate
	 *            {@link Class} to generate.
	 * @param valueGeneratorFactory
	 *            {@link ValueGeneratorFactory} to use.
	 * @return {@link ClassConstructor} if it is possible to generate one, <code>null</code> otherwise.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ClassConstructorBuilder<T> build(final ClassAccessWrapper<?> classToGenerate,
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
		ClassConstructorFactory<T> builderMethod = new ClassConstructorFactory<T>(builder,
				valueGeneratorFactory.getValueGenerators(builder.getParameterTypes()));
		ClassPropertySetter<T> builderPropertySetter = ((ClassPropertySetter<T>) ClassPropertySetter.constructPropertySetter(
				classToGenerate.wrap(builder.getReturnType()), valueGeneratorFactory));

		Method valueBuilderMethod = null;
		for (Method constructorMethod : builder.getReturnType().getDeclaredMethods()) {
			if (classToGenerate.canBeReplacedWith(constructorMethod.getReturnType())) {
				valueBuilderMethod = constructorMethod;
			}
		}

		return new ClassConstructorBuilder<T>(builderMethod, builderPropertySetter, valueBuilderMethod);
	}
}