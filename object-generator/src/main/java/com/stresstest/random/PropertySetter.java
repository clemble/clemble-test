package com.stresstest.random;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

abstract public class PropertySetter<T> {

    abstract public void setProperty(Object target);

    abstract protected Class<?> getAffectedClass();

    final public boolean isApplicable(ClassReflectionAccessWrapper<?> applicableToClass) {
        return applicableToClass.canReplace(getAffectedClass());
    }

    @Override
    abstract public int hashCode();

    @Override
    abstract public boolean equals(Object obj);

    final private static class SimplePropertySetter<T> extends PropertySetter<T> {

        final private Field field;

        final private Method method;
        final private ValueGenerator<T> valueGenerator;

        private SimplePropertySetter(Field field, Method method, ValueGenerator<T> valueGenerator) {
            this.field = field;
            this.method = method;
            this.valueGenerator = valueGenerator;
        }

        @Override
        public void setProperty(Object target) {
            Object valueToSet = valueGenerator.generate();
            // Step 1. Setting method preferably to field as a regular expression
            try {
                if (method != null) {
                    method.invoke(target, valueToSet);
                } else {
                    field.set(target, valueToSet);
                }
            } catch (Exception methodSetException) {
                // Step 3. Setting method, changing it access level prior to setting
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
        public int hashCode() {
            return new HashCodeBuilder().append(field).append(method).toHashCode();
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SimplePropertySetter<T> other = (SimplePropertySetter<T>) obj;
            return new EqualsBuilder().append(field, other.field).append(method, other.method).isEquals();
        }

        @Override
        public String toString() {
            return (field != null ? field.getName() : "-") + " / " + (method != null ? method.getName() : "-");
        }

    }

    final private static class CollectionPropertySetter<T> extends PropertySetter<T> {

        final private PropertySetter<T> initialPropertySetter;

        final private Method method;
        final private ValueGenerator<T> valueGenerator;

        private CollectionPropertySetter(final PropertySetter<T> initialPropertySetter, final Method method, final ValueGenerator<T> valueGenerator) {
            this.initialPropertySetter = initialPropertySetter;
            this.method = method;
            this.valueGenerator = valueGenerator;
        }

        @Override
        public void setProperty(Object target) {
            initialPropertySetter.setProperty(target);
            Object valueToSet = valueGenerator.generate();
            // Step 1. Setting method as a regular expression
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
        public int hashCode() {
            return new HashCodeBuilder().append(initialPropertySetter).toHashCode();
        }

        @Override
        @SuppressWarnings("rawtypes")
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CollectionPropertySetter other = (CollectionPropertySetter) obj;
            return new EqualsBuilder().append(initialPropertySetter, other.initialPropertySetter).isEquals();
        }

        @Override
        public String toString() {
            return initialPropertySetter.toString();
        }
    }

    final private static Predicate<Method> EXTRACT_APPLICABLE_METHODS = new Predicate<Method>() {
        @Override
        public boolean apply(Method input) {
            if ((input.getModifiers() & Modifier.STATIC) != 0)
                return false;
            String name = input.getName().toLowerCase();
            return name.startsWith("set") || name.startsWith("add");
        }
    };

    final private static Function<Method, String> EXTRACT_POSSIBLE_FIELD_NAME = new Function<Method, String>() {
        @Override
        public String apply(Method method) {
            final String lowerMethodName = method.getName().toLowerCase();
            return lowerMethodName.startsWith("set") || lowerMethodName.startsWith("add") ? lowerMethodName.substring(3) : lowerMethodName;
        }
    };

    final private static Function<Method, String> EXTRACT_ADD_METHOD_NAME = new Function<Method, String>() {
        @Override
        public String apply(Method method) {
            final String lowerMethodName = method.getName().toLowerCase();
            return lowerMethodName.startsWith("add") ? lowerMethodName.substring(3) + "s" : lowerMethodName + "s";
        }
    };

    final private static Function<Field, String> EXTRACT_FIELD_NAME = new Function<Field, String>() {
        @Override
        public String apply(Field field) {
            return field.getName().toLowerCase();
        }
    };
    
    final private static Comparator<PropertySetter<?>> STRING_PRESENTATION_COMPARATOR = new Comparator<PropertySetter<?>>() {
        @Override
        public int compare(final PropertySetter<?> firstPropertySetter, final PropertySetter<?> secondPropertySetter) {
            return firstPropertySetter.toString().compareTo(secondPropertySetter.toString());
        }
    };
    
    final private static Comparator<PropertySetter<?>> PRESENTATION_TYPE_COMPARATOR = new Comparator<PropertySetter<?>>() {
        @Override
        public int compare(final PropertySetter<?> firstPropertySetter, final PropertySetter<?> secondPropertySetter) {
            boolean firstCollection = firstPropertySetter instanceof CollectionPropertySetter;
            boolean secondCollection = secondPropertySetter instanceof CollectionPropertySetter;
            return firstCollection && secondCollection ? 0 : firstCollection ? -1 : 1;
        }
    };
    
    final private static Comparator<PropertySetter<?>> TYPE_COMPARATOR = new Comparator<PropertySetter<?>>() {
        @Override
        public int compare(final PropertySetter<?> firstPropertySetter, final PropertySetter<?> secondPropertySetter) {
            boolean firstSimpleProperty = firstPropertySetter instanceof SimplePropertySetter;
            boolean secondSimpleProperty = secondPropertySetter instanceof SimplePropertySetter;
            if(firstSimpleProperty && secondSimpleProperty) {
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
                    if(comparison != 0)
                        return comparison;
                }
                return ((SimplePropertySetter<?>) firstPropertySetter).getAffectedClass().isAssignableFrom(((SimplePropertySetter<?>) secondPropertySetter).getAffectedClass()) ? 1 : -1;
            } else if(!firstSimpleProperty && !secondSimpleProperty) {
                // Comparison of Collections is equivalent to comparison of the types
                return compare(((CollectionPropertySetter<?>) firstPropertySetter).initialPropertySetter, ((CollectionPropertySetter<?>) secondPropertySetter).initialPropertySetter);
            } else if(firstSimpleProperty){
                return 1;
            } else if(secondSimpleProperty) {
                return -1;
            }
            // This is never reached :)
            return 0;
        }
    };


    private static Field findField(final Class searchClass, final String fieldName) {
        Collection<Field> fieldCandidates = Collections2.filter(Arrays.asList(searchClass.getDeclaredFields()), new Predicate<Field>() {
            @Override
            public boolean apply(Field field) {
                return fieldName.equals(EXTRACT_FIELD_NAME.apply(field));
            }
        });

        return fieldCandidates.isEmpty() ? null : fieldCandidates.iterator().next();
    }

    private static Method findSetMethod(final Class searchClass, final String methodName) {
        Collection<Method> methodCandidates = Collections2.filter(Arrays.asList(searchClass.getMethods()), new Predicate<Method>() {
            @Override
            public boolean apply(Method method) {
                return method.getParameterTypes().length == 1 && method.getName().toLowerCase().startsWith("set")
                        && EXTRACT_POSSIBLE_FIELD_NAME.apply(method).equals(methodName);
            }
        });

        return methodCandidates.isEmpty() ? null : methodCandidates.iterator().next();
    }

    private static Method findAddMethod(final Class searchClass, final String methodName) {
        Collection<Method> methodCandidates = Collections2.filter(Arrays.asList(searchClass.getMethods()), new Predicate<Method>() {
            @Override
            public boolean apply(Method method) {
                return method.getParameterTypes().length == 1 && method.getName().toLowerCase().startsWith("add")
                        && EXTRACT_ADD_METHOD_NAME.apply(method).startsWith(methodName);
            }
        });

        return methodCandidates.isEmpty() ? null : methodCandidates.iterator().next();
    }

    public static <T> PropertySetter<T> createFieldSetter(final Field field) {
        return (PropertySetter<T>) createFieldSetter(field, ObjectGenerator.getValueGenerator(field.getType()));
    }

    public static <T> PropertySetter<T> createFieldSetter(final Field field, final ValueGenerator<T> valueGenerator) {
        Method possibleMethods = findSetMethod(field.getDeclaringClass(), EXTRACT_FIELD_NAME.apply(field));
        return create(field, possibleMethods, valueGenerator);
    }

    public static <T> PropertySetter<T> createMethodSetter(final Method method) {
        if (method.getParameterTypes().length != 1)
            return null;
        return (PropertySetter<T>) createMethodSetter(method, ObjectGenerator.getValueGenerator(method.getParameterTypes()[0]));
    }

    public static <T> PropertySetter<T> createMethodSetter(final Method method, final ValueGenerator<T> valueGenerator) {
        Field possibleField = findField(method.getDeclaringClass(), EXTRACT_POSSIBLE_FIELD_NAME.apply(method));
        return create(possibleField, method, valueGenerator);
    }

    public static <T> PropertySetter<T> create(final Field field, final Method method, final ValueGenerator<T> valueGenerator) {
        if (field != null && Collection.class.isAssignableFrom(field.getType())) {
            Method addMethod = findAddMethod(field.getDeclaringClass(), EXTRACT_FIELD_NAME.apply(field));
            Method setMethod = findSetMethod(field.getDeclaringClass(), EXTRACT_FIELD_NAME.apply(field));
            PropertySetter<T> collectionValueInitializer = new SimplePropertySetter<T>(field, setMethod,
                    (ValueGenerator<T>) ObjectGenerator.getValueGenerator(field.getType()));
            return new CollectionPropertySetter<T>(collectionValueInitializer, addMethod, valueGenerator);
        } else {
            return new SimplePropertySetter<T>(field, method, valueGenerator);
        }
    }

    public static <T> Collection<PropertySetter<?>> extractAvailableProperties(final ClassReflectionAccessWrapper<T> searchClass) {
        // Step 1. Create Collection field setters
        final Collection<PropertySetter<?>> propertySetters = new TreeSet<PropertySetter<?>>(STRING_PRESENTATION_COMPARATOR);
        propertySetters.addAll(SELECTOR_MANAGER.getApplicableProperties(searchClass));
        for (Field field : searchClass.getFields()) {
            propertySetters.add(createFieldSetter(field));
        }
        // Step 2. Create Collection of method setters
        for (Method method : Collections2.filter(searchClass.getMethods(), EXTRACT_APPLICABLE_METHODS)) {
            PropertySetter<?> propertySetter = createMethodSetter(method);
            if (propertySetter != null) {
                propertySetters.add(propertySetter);
            }
        }

        final List<PropertySetter<?>> resultSetters = new ArrayList<PropertySetter<?>>(propertySetters);
        Collections.sort(resultSetters, PRESENTATION_TYPE_COMPARATOR);
        // Step 3. Returning accumulated result
        return resultSetters;
    }

    final private static PropertySetterManager SELECTOR_MANAGER = new PropertySetterManager();

    public static <T> void register(final Class<?> searchClass, final String name, final ValueGenerator<T> valueGenerator) {
        final String possibleName = name.toLowerCase();
        final Field possibleField = findField(searchClass, possibleName);
        final Method possibleMethod = findSetMethod(searchClass, possibleName);
        PropertySetter<T> propertySetter = create(possibleField, possibleMethod, valueGenerator);
        SELECTOR_MANAGER.addSpecificProperties(propertySetter);
    }

    final private static class PropertySetterManager {

        final private Collection<PropertySetter<?>> propertySelectors = new HashSet<PropertySetter<?>>();

        public void addSpecificProperties(PropertySetter<?> propertySelector) {
            propertySelectors.remove(propertySelector);
            propertySelectors.add(propertySelector);
        }

        public Collection<PropertySetter<?>> getApplicableProperties(final ClassReflectionAccessWrapper<?> applicableClass) {
            Collection<PropertySetter<?>> applicableSelectors = Collections2.filter(propertySelectors, new Predicate<PropertySetter<?>>() {
                @Override
                public boolean apply(PropertySetter<?> selector) {
                    return selector.isApplicable(applicableClass);
                }
            });
            List<PropertySetter<?>> sortedSelectors = new ArrayList<PropertySetter<?>>(applicableSelectors);
            Collections.sort(sortedSelectors, TYPE_COMPARATOR);
            return sortedSelectors;
        }

    }

}
