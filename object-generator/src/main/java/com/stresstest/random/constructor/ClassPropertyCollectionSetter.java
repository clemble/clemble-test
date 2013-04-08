package com.stresstest.random.constructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import com.stresstest.random.ObjectGenerator;
import com.stresstest.random.ValueGenerator;

/**
 * Property Setter for Collection fields.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            parameterized {@link Class}
 */
final class ClassPropertyCollectionSetter<T> extends ClassPropertySetter<T> {

	/**
	 * SimplePropertySetter to initialize field with empty Collection.
	 */
	final ClassPropertySimpleSetter<T> initialPropertySetter;
	/**
	 * Add method if there is one for the field (It is impossible to identify collection type in runtime),
	 */
	final private Method method;
	/**
	 * ValueGenerator to use for additional value generation.
	 */
	final private ValueGenerator<T> valueGenerator;

	/**
	 * Default constructor.
	 * 
	 * @param initialPropertySetter
	 *            default constructor for the field.
	 * @param method
	 *            add method to use
	 * @param valueGenerator
	 *            value generator to add something to Collection.
	 */
	@SuppressWarnings("unchecked")
	ClassPropertyCollectionSetter(final ClassAccessWrapper<?> sourceClass, final Field field) {
		Method addMethod = findAddMethod(sourceClass, ClassPropertySetter.extractFieldName(field));
		Method setMethod = findSetMethod(sourceClass, ClassPropertySetter.extractFieldName(field));
		
		
		this.initialPropertySetter = new ClassPropertySimpleSetter<T>(field, setMethod, (ValueGenerator<T>) ObjectGenerator.getValueGenerator(field.getType()));;

		if(addMethod != null) {
			this.valueGenerator = (ValueGenerator<T>) ObjectGenerator.getValueGenerator(addMethod.getParameterTypes()[0]);
			this.method = addMethod;
		} else {
			this.valueGenerator= null;
			this.method = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	ClassPropertyCollectionSetter(final ClassAccessWrapper<?> sourceClass, final Method setMethod) {
		Method addMethod = findAddMethod(sourceClass, setMethod.getName().substring(3));
		
		this.initialPropertySetter = new ClassPropertySimpleSetter<T>(null, setMethod, (ValueGenerator<T>) ObjectGenerator.getValueGenerator(setMethod.getParameterTypes()[0]));;

		if(addMethod != null) {
			this.valueGenerator = (ValueGenerator<T>) ObjectGenerator.getValueGenerator(addMethod.getParameterTypes()[0]);
			this.method = addMethod;
		} else {
			this.valueGenerator= null;
			this.method = null;
		}

	}

	@Override
	public void setProperties(Object target) {
		// Step 1. Generating initial empty Collection
		initialPropertySetter.setProperties(target);
		// Step 2. Setting method as a regular expression
		Object valueToSet = null;
		try {
			if (method != null && valueGenerator != null) {
				valueToSet = valueGenerator.generate();
				method.invoke(target, valueToSet);
			}
		} catch (Exception methodSetException) {
			try {
				method.setAccessible(true);
				method.invoke(target, valueToSet);
			} catch (Exception exception) {
			}
		}
	}

	@Override
	protected Class<?> getAffectedClass() {
		return initialPropertySetter.getAffectedClass();
	}

	@Override
	public String toString() {
		return initialPropertySetter.toString();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<ValueGenerator<?>> getValueGenerators() {
		return (Collection<ValueGenerator<?>>)(Collection<?>) Collections.singletonList(valueGenerator);
	}
}