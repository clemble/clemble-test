package com.clemble.test.random.constructor;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.clemble.test.random.ValueGenerator;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * Simple abstraction to keep track of registered PropertySetters.
 * 
 * @author Anton Oparin
 * 
 */
final public class ClassPropertySetterRegistry {

    /**
     * Registered PropertySetters collection
     */
    final private SortedSet<ClassPropertySetter<?>> propertySelectors = new TreeSet<ClassPropertySetter<?>>(ClassPropertySetter.COMPARE_PRESENTATION_TYPE);

    /**
     * Registered abstract PropertySetters collection
     */
    final private Set<AbstractPropertySetter<?>> abstractPropertySelectors = new HashSet<AbstractPropertySetter<?>>();

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
        Collection<ClassPropertySetter<?>> applicableSelectors = Collections2.filter(propertySelectors, new Predicate<ClassPropertySetter<?>>() {
            @Override
            public boolean apply(ClassPropertySetter<?> selector) {
                return applicableClass.canReplace(selector.getAffectedClass());
            }
        });
        // Step 2. Filter all abstract properties
        for (AbstractPropertySetter<?> abstractProperty: abstractPropertySelectors) {
            if(abstractProperty.isApplicable(applicableClass)) {
                ClassPropertySetter<?> propertySetter = constructSetter(applicableClass.getSourceClass(), abstractProperty.getName(), abstractProperty.getValueGenerator());
                if(propertySetter != null)
                    applicableSelectors.add(propertySetter);
            }
        }
        // Step 3. Returning result set to the
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> void register(final Class<?> searchClass, final String name, final ValueGenerator<T> valueGenerator) {
        if ((searchClass.getModifiers() & (Modifier.ABSTRACT | Modifier.INTERFACE)) > 0) {
            abstractPropertySelectors.add(new AbstractPropertySetter(searchClass, name, valueGenerator));
        } else {
            addSpecificProperties(constructSetter(searchClass, name, valueGenerator));
        }
    }
    
    private <T> ClassPropertySetter<T> constructSetter(final Class<?> searchClass, final String name, final ValueGenerator<T> valueGenerator) {
        final String possibleName = name.toLowerCase();
        final ClassAccessWrapper<?> wrapper = ClassAccessWrapper.createAllMethodsAccessor(searchClass);
        final Field possibleField = ClassPropertySetter.findField(wrapper, possibleName);
        final Method possibleMethod = ClassPropertySetter.findSetMethod(wrapper, possibleName);

        ClassPropertySetter<T> propertySetter = ClassPropertySetter.create(wrapper, possibleField, possibleMethod, valueGenerator);

        return propertySetter;
    }

    public static class AbstractPropertySetter<T> {
        final private Class<?> searchClass;
        final private String name;
        final private ValueGenerator<T> valueGenerator;

        public AbstractPropertySetter(Class<?> searchClass, String name, ValueGenerator<T> valueGenerator) {
            assert (searchClass.getModifiers() & (Modifier.ABSTRACT | Modifier.INTERFACE)) > 0;
            this.searchClass = checkNotNull(searchClass);
            this.name = checkNotNull(name);
            this.valueGenerator = checkNotNull(valueGenerator);
        }

        public boolean isApplicable(final ClassAccessWrapper<?> applicableClass) {
            return searchClass.isAssignableFrom(applicableClass.getSourceClass());
        }

        public String getName() {
            return name;
        }

        public ValueGenerator<T> getValueGenerator() {
            return valueGenerator;
        }
    }

}