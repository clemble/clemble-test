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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

abstract public class PropertySetter<T> implements Comparable<PropertySetter<?>> {

    abstract public void apply(Object target);

    abstract protected Class<?> getAffectedClass();

    final public boolean isApplicable(ClassReflectionAccessWrapper<?> applicableToClass) {
        return applicableToClass == null ? false : applicableToClass.canReplace(getAffectedClass());
    }

    final public boolean isMoreSpecific(PropertySetter<?> anotherPropertySelector) {
        return anotherPropertySelector == null ? true : anotherPropertySelector.getAffectedClass().isAssignableFrom(getAffectedClass());
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
        public void apply(Object target) {
            if (target == null)
                return;
            Object valueToSet = valueGenerator.generate();
            // Step 1. Setting method as a regular expression
            try {
                if (method != null) {
                    method.invoke(target, valueToSet);
                } else if (field != null) {
                    field.set(target, valueToSet);
                }
            } catch (Exception methodSetException) {
                // Step 3. Setting method, changing it access level prior to setting
                try {
                    if (method != null) {
                        method.setAccessible(true);
                        method.invoke(target, valueToSet);
                    } else if (field != null) {
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
            final int prime = 31;
            int result = 1;
            result = prime * result + ((field == null) ? 0 : field.hashCode());
            result = prime * result + ((method == null) ? 0 : method.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SimplePropertySetter other = (SimplePropertySetter) obj;
            if (field == null) {
                if (other.field != null)
                    return false;
            } else if (!field.equals(other.field))
                return false;
            if (method == null) {
                if (other.method != null)
                    return false;
            } else if (!method.equals(other.method))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return (field != null ? field.getName() : "-") + " / " + (method != null ? method.getName() : "-");
        }

        @Override
        public int compareTo(PropertySetter<?> anotherPropertySetter) {
            int comparison = 1;
            if (anotherPropertySetter instanceof SimplePropertySetter) {
                Field anotherField = ((SimplePropertySetter<?>) anotherPropertySetter).field;
                Method anotherMethod = ((SimplePropertySetter<?>) anotherPropertySetter).method;
                if (field != null && anotherField != null) {
                    comparison = -field.getName().compareTo(anotherField.getName());
                }
                if (comparison == 0 && method != null && anotherMethod != null) {
                    comparison = -method.getName().compareTo(anotherMethod.getName());
                }
                if (comparison == 0) {
                    comparison = getAffectedClass().isAssignableFrom(anotherPropertySetter.getAffectedClass()) ? 1 : -1;
                }
            }
            return comparison;
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
        public void apply(Object target) {
            if (target == null)
                return;
            initialPropertySetter.apply(target);
            Object valueToSet = valueGenerator.generate();
            // Step 1. Setting method as a regular expression
            try {
                if (method != null) {
                    method.invoke(target, valueToSet);
                }
            } catch (Exception methodSetException) {
                try {
                    if (method != null) {
                        method.setAccessible(true);
                        method.invoke(target, valueToSet);
                    }
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
            final int prime = 31;
            int result = 1;
            result = prime * result + ((initialPropertySetter == null) ? 0 : initialPropertySetter.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CollectionPropertySetter other = (CollectionPropertySetter) obj;
            if (initialPropertySetter == null) {
                if (other.initialPropertySetter != null)
                    return false;
            } else if (!initialPropertySetter.equals(other.initialPropertySetter))
                return false;
            return true;
        }

        @Override
        public int compareTo(PropertySetter<?> o) {
            if (o instanceof CollectionPropertySetter)
                return -initialPropertySetter.compareTo(((CollectionPropertySetter) o).initialPropertySetter);
            return -1;
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

    public static <T> PropertySetter<T> create(final Class<?> searchClass, final String name, final ValueGenerator<T> valueGenerator) {
        final String possibleName = name.toLowerCase();
        final Field possibleField = findField(searchClass, possibleName);
        final Method possibleMethod = findSetMethod(searchClass, possibleName);
        return create(possibleField, possibleMethod, valueGenerator);
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

    public static <T> Collection<PropertySetter<?>> create(final ClassReflectionAccessWrapper<T> searchClass) {
        // Step 1. Create Collection field setters
        final Collection<PropertySetter<?>> propertySetters = new TreeSet<PropertySetter<?>>(new Comparator<PropertySetter<?>>() {
            @Override
            public int compare(PropertySetter<?> o1, PropertySetter<?> o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        propertySetters.addAll(SELECTOR_MANAGER.getApplicableProperties(searchClass));
        for (Field field : searchClass.getFields()) {
            PropertySetter<?> fieldSetter = createFieldSetter(field);
            if (fieldSetter != null) {
                propertySetters.add(fieldSetter);
            }
        }
        // Step 2. Create Collection of method setters
        for (Method method : Collections2.filter(searchClass.getMethods(), EXTRACT_APPLICABLE_METHODS)) {
            PropertySetter<?> propertySetter = createMethodSetter(method);
            if (propertySetter != null) {
                propertySetters.add(propertySetter);
            }
        }

        final List<PropertySetter<?>> resultSetters = new ArrayList<PropertySetter<?>>(propertySetters);
        Collections.sort(resultSetters, new Comparator<PropertySetter<?>>() {
            @Override
            public int compare(PropertySetter<?> o1, PropertySetter<?> o2) {
                boolean firstCollection = o1 instanceof CollectionPropertySetter;
                boolean secondCollection = o2 instanceof CollectionPropertySetter;
                return firstCollection && secondCollection ? 0 : firstCollection ? -1 : 1;
            }
        });
        // Step 3. Returning accumulated result
        return resultSetters;
    }

    final private static SimplePropertySelectorManager SELECTOR_MANAGER = new SimplePropertySelectorManager();

    public static <T> void register(final Class<?> searchClass, final String name, final ValueGenerator<T> valueGenerator) {
        PropertySetter<T> propertySetter = create(searchClass, name, valueGenerator);
        SELECTOR_MANAGER.addSpecificProperties(propertySetter);
    }

    final private static class SimplePropertySelectorManager {

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
            Collections.sort(sortedSelectors);
            return sortedSelectors;
        }

    }

}
