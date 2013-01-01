package com.stresstest.random;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * Abstraction of object property.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            parameterized {@link Class}.
 */
abstract public class PropertySetter<T> {

    /**
     * Sets value for configured field.
     * 
     * @param target
     *            T value to update
     */
    abstract public void setValue(Object target);

    /**
     * Returns affected Class. Supposed to be used primerely inside invocation.
     * 
     * @return affected {@link Class}.
     */
    abstract protected Class<?> getAffectedClass();

    /**
     * Property setter implementation for a plain field.
     * 
     * @author Anton Oparin
     * 
     * @param <T>
     *            parameterized {@link Class}.
     */
    final private static class SimplePropertySetter<T> extends PropertySetter<T> {

        /**
         * Field to reference.
         */
        final private Field field;
        /**
         * Method assuming java bean naming, or field naming.
         */
        final private Method method;
        /**
         * Value Generator to use to set the value property
         */
        final private ValueGenerator<T> valueGenerator;

        /**
         * Default constructor.
         * 
         * @param field
         *            field to use as reference.
         * @param method
         *            method to use as reference.
         * @param valueGenerator
         *            ValueGenerator to use.
         */
        private SimplePropertySetter(final Field field, final Method method, final ValueGenerator<T> valueGenerator) {
            this.field = field;
            this.method = method;
            this.valueGenerator = valueGenerator;
        }

        @Override
        public void setValue(final Object target) {
            Object valueToSet = valueGenerator.generate();
            // Step 1. Setting value, preferring method over field
            try {
                if (method != null) {
                    method.invoke(target, valueToSet);
                } else {
                    field.set(target, valueToSet);
                }
            } catch (Exception methodSetException) {
                // Step 3. Changing access level and making another attempt
                try {
                    if (method != null) {
                        method.setAccessible(true);
                        method.invoke(target, valueToSet);
                    } else {
                        field.setAccessible(true);
                        field.set(target, valueToSet);
                    }
                } catch (Exception anotherMethodSetException) {
                }
            }
        }

        @Override
        protected Class<?> getAffectedClass() {
            return field != null ? field.getDeclaringClass() : method.getDeclaringClass();
        }

        @Override
        public String toString() {
            // DO NOT CHANGE !!! This is import, it will be used for comparison.
            return (field != null ? field.getName() : "-") + " / " + (method != null ? method.getName() : "-");
        }

    }

    /**
     * Property Setter for Collection fields.
     * 
     * @author Anton Oparin
     * 
     * @param <T>
     *            parameterized {@link Class}
     */
    final private static class CollectionValueSetter<T> extends PropertySetter<T> {

        /**
         * SimplePropertySetter to initialize field with empty Collection.
         */
        final private SimplePropertySetter<T> initialPropertySetter;
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
        private CollectionValueSetter(final SimplePropertySetter<T> initialPropertySetter, final Method method, final ValueGenerator<T> valueGenerator) {
            this.initialPropertySetter = initialPropertySetter;
            this.method = method;
            this.valueGenerator = valueGenerator;
        }

        @Override
        public void setValue(Object target) {
            // Step 1. Generating initial empty Collection
            initialPropertySetter.setValue(target);
            Object valueToSet = valueGenerator.generate();
            // Step 2. Setting method as a regular expression
            try {
                if (method != null)
                    method.invoke(target, valueToSet);
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
    }

    /**
     * Filter for applicable methods, uses only set and add methods
     */
    final private static Predicate<Member> FILTER_APPLICABLE_METHODS = new Predicate<Member>() {
        @Override
        public boolean apply(Member input) {
            if ((input.getModifiers() & Modifier.STATIC) != 0)
                return false;
            String name = input.getName().toLowerCase();
            return name.startsWith("set") || name.startsWith("add");
        }
    };

    /**
     * Extracts and normalizes Member name.
     */
    final private static Function<Member, String> EXTRACT_MEMBER_NAME = new Function<Member, String>() {
        @Override
        public String apply(Member member) {
            final String lowerMethodName = member.getName().toLowerCase();
            return lowerMethodName.startsWith("set") || lowerMethodName.startsWith("add") ? lowerMethodName.substring(3) : lowerMethodName;
        }
    };

    /**
     * Extracts and normalizes possible field name for Collection, assuming there can be "s" ending in the field method.
     */
    final private static Function<Member, String> EXTRACT_ADD_METHOD_NAME = new Function<Member, String>() {
        @Override
        public String apply(Member method) {
            return EXTRACT_MEMBER_NAME.apply(method) + "s";
        }
    };

    /**
     * Extracts and normalizes field name.
     */
    final private static Function<Member, String> EXTRACT_FIELD_NAME = new Function<Member, String>() {
        @Override
        public String apply(Member field) {
            return field.getName().toLowerCase();
        }
    };

    /**
     * Comparator based on String presentation, needed to distinguish the same field on the different levels of inheritance.
     */
    final private static Comparator<PropertySetter<?>> COMPARE_STRING_PRESENTATION = new Comparator<PropertySetter<?>>() {
        @Override
        public int compare(final PropertySetter<?> firstPropertySetter, final PropertySetter<?> secondPropertySetter) {
            return firstPropertySetter.toString().compareTo(secondPropertySetter.toString());
        }
    };

    /**
     * Comparator based on Presentation type.
     */
    final private static Comparator<PropertySetter<?>> COMPARE_PRESENTATION_TYPE = new Comparator<PropertySetter<?>>() {
        @Override
        public int compare(final PropertySetter<?> firstPropertySetter, final PropertySetter<?> secondPropertySetter) {
            boolean firstSimpleProperty = firstPropertySetter instanceof SimplePropertySetter;
            boolean secondSimpleProperty = secondPropertySetter instanceof SimplePropertySetter;
            if (firstSimpleProperty && secondSimpleProperty) {
                // Step 1. Check field names
                Field firstField = ((SimplePropertySetter<?>) firstPropertySetter).field;
                Field secondField = ((SimplePropertySetter<?>) secondPropertySetter).field;
                if (firstField != null && secondField != null) {
                    int comparison = secondField.getName().compareTo(firstField.getName());
                    if (comparison != 0)
                        return comparison;
                }
                // Step 2. Check method names
                Method firstMethod = ((SimplePropertySetter<?>) firstPropertySetter).method;
                Method secondMethod = ((SimplePropertySetter<?>) secondPropertySetter).method;
                if (firstMethod != null && secondMethod != null) {
                    int comparison = secondMethod.getName().compareTo(firstMethod.getName());
                    if (comparison != 0)
                        return comparison;
                }
                // Step 2. Check classes
                Class<?> firstClass = ((SimplePropertySetter<?>) firstPropertySetter).getAffectedClass();
                Class<?> secondClass = ((SimplePropertySetter<?>) secondPropertySetter).getAffectedClass();
                if (firstClass != secondClass) {
                    return firstClass.isAssignableFrom(secondClass) ? 1 : -1;
                }
            } else if (!firstSimpleProperty && !secondSimpleProperty) {
                // Comparison of Collections is equivalent to comparison of the types
                return compare(((CollectionValueSetter<?>) firstPropertySetter).initialPropertySetter,
                        ((CollectionValueSetter<?>) secondPropertySetter).initialPropertySetter);
            } else if (firstSimpleProperty) {
                return 1;
            } else if (secondSimpleProperty) {
                return -1;
            }
            return 0;
        }
    };

    /**
     * Finds field for specified field name.
     * 
     * @param searchClass
     *            class to search in.
     * @param fieldName
     *            name of the field.
     * @return Field or null if not found.
     */
    private static Field findField(final ClassReflectionAccessWrapper searchClass, final String fieldName) {
        // Step 1. Filter all field's with specified name
        Collection<Field> fieldCandidates = Collections2.filter(searchClass.getFields(), new Predicate<Field>() {
            @Override
            public boolean apply(Field field) {
                return fieldName.equals(EXTRACT_FIELD_NAME.apply(field));
            }
        });
        // Step 2. Return first field in sorted Collection.
        return fieldCandidates.isEmpty() ? null : fieldCandidates.iterator().next();
    }

    /**
     * Finds possible method for specified field name.
     * 
     * @param searchClass
     *            class to search in.
     * @param methodName
     *            name of the Method
     * @return possible set method for specified field name.
     */
    private static Method findSetMethod(final ClassReflectionAccessWrapper searchClass, final String methodName) {
        // Step 1. Filter method candidates
        Collection<Method> methodCandidates = Collections2.filter(searchClass.getMethods(), new Predicate<Method>() {
            @Override
            public boolean apply(Method method) {
                return method.getParameterTypes().length == 1 && method.getName().toLowerCase().startsWith("set")
                        && EXTRACT_MEMBER_NAME.apply(method).equals(methodName);
            }
        });
        // Step 2. Return first method in the Collection
        return methodCandidates.isEmpty() ? null : methodCandidates.iterator().next();
    }

    /**
     * Finds possible add method for specified field name.
     * 
     * @param searchClass
     *            Class to search for.
     * @param methodName
     *            name of the method.
     * @return possible add method for specified field name.
     */
    private static Method findAddMethod(final ClassReflectionAccessWrapper searchClass, final String methodName) {
        // Step 1. Filter method candidates
        Collection<Method> methodCandidates = Collections2.filter(searchClass.getMethods(), new Predicate<Method>() {
            @Override
            public boolean apply(Method method) {
                return method.getParameterTypes().length == 1 && method.getName().toLowerCase().startsWith("add")
                        && EXTRACT_ADD_METHOD_NAME.apply(method).startsWith(methodName);
            }
        });
        // Step 2. Return first field
        return methodCandidates.isEmpty() ? null : methodCandidates.iterator().next();
    }

    /**
     * Builds property setter for the specified field.
     * 
     * @param field
     *            field to set
     * @param valueGenerator
     *            {@link ValueGenerator} to use.
     * @return PropertySetter for the provided field.
     */
    private static <T> PropertySetter<T> createFieldSetter(final ClassReflectionAccessWrapper sourceClass, final Field field, final ValueGenerator<T> valueGenerator) {
        // Step 1. Sanity check
        if (field == null)
            throw new IllegalArgumentException();
        if(valueGenerator == null)
            throw new IllegalArgumentException();
        // Step 2. Retrieve possible set name for the field
        Method possibleMethods = findSetMethod(sourceClass, EXTRACT_FIELD_NAME.apply(field));
        // Step 3. Create possible field setter.
        return create(sourceClass, field, possibleMethods, valueGenerator);
    }

    /**
     * * Constructs property setter based on provided Method.
     * 
     * @param method target method.
     * @param valueGenerator {@link ValueGenerator} to use.
     * @return constructed PropertySetter for the method, or <code>null</code> if such PropertySetter can't be created.
     */
    public static <T> PropertySetter<T> createMethodSetter(final ClassReflectionAccessWrapper sourceClass, final Method method, final ValueGenerator<T> valueGenerator) {
        if (method == null)
            throw new IllegalArgumentException();
        if(valueGenerator == null)
            throw new IllegalArgumentException();
        if (method.getParameterTypes().length != 1)
            return null;
        Field possibleField = findField(sourceClass, EXTRACT_MEMBER_NAME.apply(method));
        return create(sourceClass, possibleField, method, valueGenerator);
    }

    /**
     * Generates PropertySetter for provided Field, Method and {@link ValueGenerator}.
     * 
     * @param field target field.
     * @param method target set method.
     * @param valueGenerator {@link ValueGenerator} to use.
     * @return constructed PropertySetter.
     */
    public static <T> PropertySetter<T> create(final ClassReflectionAccessWrapper<?> sourceClass, final Field field, final Method method, final ValueGenerator<T> valueGenerator) {
        if (field != null && Collection.class.isAssignableFrom(field.getType())) {
            Method addMethod = findAddMethod(sourceClass, EXTRACT_FIELD_NAME.apply(field));
            Method setMethod = findSetMethod(sourceClass, EXTRACT_FIELD_NAME.apply(field));
            SimplePropertySetter<T> collectionValueInitializer = new SimplePropertySetter<T>(field, setMethod,
                    (ValueGenerator<T>) ObjectGenerator.getValueGenerator(field.getType()));
            return new CollectionValueSetter<T>(collectionValueInitializer, addMethod, valueGenerator);
        } else {
            return new SimplePropertySetter<T>(field, method, valueGenerator);
        }
    }

    /**
     * Extracts all possible PropertySetters with specified access level.
     * 
     * @param searchClass {@link ClassReflectionAccessWrapper} access wrapper to generate properties for.
     * @return list of all PropertySetter it can set, ussing specified field.
     */
    public static <T> Collection<PropertySetter<?>> extractAvailableProperties(final ClassReflectionAccessWrapper<T> searchClass) {
        // Step 1. Create Collection field setters
        final Collection<PropertySetter<?>> propertySetters = new TreeSet<PropertySetter<?>>(COMPARE_STRING_PRESENTATION);
        propertySetters.addAll(SELECTOR_MANAGER.getApplicableProperties(searchClass));
        for (Field field : searchClass.getFields()) {
            propertySetters.add(createFieldSetter(searchClass, field, ObjectGenerator.getValueGenerator(field.getType())));
        }
        // Step 2. Create Collection of method setters
        for (Method method : Collections2.filter(searchClass.getMethods(), FILTER_APPLICABLE_METHODS)) {
            if(method.getParameterTypes().length != 1)
                continue;
            PropertySetter<?> propertySetter = createMethodSetter(searchClass, method, ObjectGenerator.getValueGenerator(method.getParameterTypes()[0]));
            if (propertySetter != null) {
                propertySetters.add(propertySetter);
            }
        }

        final List<PropertySetter<?>> resultSetters = new ArrayList<PropertySetter<?>>(propertySetters);
        Collections.sort(resultSetters, COMPARE_PRESENTATION_TYPE);
        // Step 3. Returning accumulated result
        return resultSetters;
    }

    final private static PropertySetterManager SELECTOR_MANAGER = new PropertySetterManager();

    /**
     * Registers {@link ValueGenerator} for specified field/method in the Class, all subClasses will use this register as well.
     * 
     * @param searchClass Class to search.
     * @param name field/method name
     * @param valueGenerator {@link ValueGenerator} to use.
     */
    public static <T> void register(final Class<?> searchClass, final String name, final ValueGenerator<T> valueGenerator) {
        final String possibleName = name.toLowerCase();
        final ClassReflectionAccessWrapper wrapper = ClassReflectionAccessWrapper.createAllMethodsAccessor(searchClass);
        final Field possibleField = findField(wrapper, possibleName);
        final Method possibleMethod = findSetMethod(wrapper, possibleName);
        PropertySetter<T> propertySetter = create(wrapper, possibleField, possibleMethod, valueGenerator);
        SELECTOR_MANAGER.addSpecificProperties(propertySetter);
    }

    /**
     * Simple abstraction to keep track of registered PropertySetters.
     * 
     * @author Anton Oparin
     *
     */
    final private static class PropertySetterManager {

        /**
         * Registered PropertySetters collection
         */
        final private SortedSet<PropertySetter<?>> propertySelectors = new TreeSet<PropertySetter<?>>(COMPARE_PRESENTATION_TYPE);

        /**
         * Registers specified Property in PropertySelectors list.
         * 
         * @param propertySelector PropertySelector to add.
         */
        public void addSpecificProperties(PropertySetter<?> propertySelector) {
            propertySelectors.remove(propertySelector);
            propertySelectors.add(propertySelector);
        }

        /**
         * Retrieves Collection of applicable PropertySetters from the List.
         * 
         * @param applicableClass Class on which register happens.
         * @return Collection of PropertySetters applicable to the provided Class.
         */
        public Collection<PropertySetter<?>> getApplicableProperties(final ClassReflectionAccessWrapper<?> applicableClass) {
            // Step 1. Filter all properties
            Collection<PropertySetter<?>> applicableSelectors = Collections2.filter(propertySelectors, new Predicate<PropertySetter<?>>() {
                @Override
                public boolean apply(PropertySetter<?> selector) {
                    return applicableClass.canReplace(selector.getAffectedClass());
                }
            });
            // Step 2. Returning result set to the 
            List<PropertySetter<?>> sortedSelectors = new ArrayList<PropertySetter<?>>(applicableSelectors);
            return sortedSelectors;
        }

    }

}
