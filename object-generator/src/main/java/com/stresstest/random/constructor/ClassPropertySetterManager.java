package com.stresstest.random.constructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.stresstest.random.ValueGenerator;

/**
 * Simple abstraction to keep track of registered PropertySetters.
 * 
 * @author Anton Oparin
 * 
 */
final public class ClassPropertySetterManager {

	/**
	 * Registered PropertySetters collection
	 */
	final private SortedSet<ClassPropertySetter<?>> propertySelectors = new TreeSet<ClassPropertySetter<?>>(
			ClassPropertySetter.COMPARE_PRESENTATION_TYPE);

	/**
	 * Registers specified Property in PropertySelectors list.
	 * 
	 * @param propertySelector
	 *            PropertySelector to add.
	 */
	public void addSpecificProperties(ClassPropertySetter<?> propertySelector) {
		propertySelectors.remove(propertySelector);
		propertySelectors.add(propertySelector);
	}

	/**
	 * Retrieves Collection of applicable PropertySetters from the List.
	 * 
	 * @param applicableClass
	 *            Class on which register happens.
	 * @return Collection of PropertySetters applicable to the provided Class.
	 */
	public Collection<ClassPropertySetter<?>> getApplicableProperties(final ClassAccessWrapper<?> applicableClass) {
		// Step 1. Filter all properties
		Collection<ClassPropertySetter<?>> applicableSelectors = Collections2.filter(propertySelectors,
				new Predicate<ClassPropertySetter<?>>() {
					@Override
					public boolean apply(ClassPropertySetter<?> selector) {
						return applicableClass.canReplace(selector.getAffectedClass());
					}
				});
		// Step 2. Returning result set to the
		List<ClassPropertySetter<?>> sortedSelectors = new ArrayList<ClassPropertySetter<?>>(applicableSelectors);
		return sortedSelectors;
	}

	/**
	 * Registers {@link ValueGenerator} for specified field/method in the Class, all subClasses will use this register as well.
	 * 
	 * @param searchClass
	 *            Class to search.
	 * @param name
	 *            field/method name
	 * @param valueGenerator
	 *            {@link ValueGenerator} to use.
	 */
	public <T> void register(final Class<?> searchClass, final String name, final ValueGenerator<T> valueGenerator) {
		final String possibleName = name.toLowerCase();
		final ClassAccessWrapper<?> wrapper = ClassAccessWrapper.createAllMethodsAccessor(searchClass);
		final Field possibleField = ClassPropertySetter.findField(wrapper, possibleName);
		final Method possibleMethod = ClassPropertySetter.findSetMethod(wrapper, possibleName);
		
		ClassPropertySetter<T> propertySetter = ClassPropertySetter
				.create(wrapper, possibleField, possibleMethod, valueGenerator);
		
		addSpecificProperties(propertySetter);
	}

}