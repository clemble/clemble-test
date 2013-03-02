package com.stresstest.jbehave.context;

import java.util.HashMap;
import java.util.Map;

public class StoryContext {

    final private ThreadLocal<Map<String, Map<Class<?>, Object>>> implementation = new ThreadLocal<Map<String, Map<Class<?>, Object>>>() {
        @Override
        public Map<String, Map<Class<?>, Object>> initialValue() {
            return new HashMap<String, Map<Class<?>, Object>>();
        }
    };

    public StoryContext() {
    }

    public void put(Object key, Object value) {
        if (value == null || key == null || value.getClass() == Void.class)
            return;
        String keyString = StoryContextReflection.getName(key);

        Map<String, Map<Class<?>, Object>> objectMap = implementation.get();
        if (objectMap.get(keyString) == null)
            objectMap.put(keyString, new HashMap<Class<?>, Object>());
        objectMap.get(keyString).put(normalize(value.getClass()), value);
    }

    public Object get(String name, Class<?> targetClass) {
        Object value = internalGet(name, targetClass);
        if (value != null)
            value = StoryContextReflection.setName(value, name);
        return value;
    }

    public Map<Class<?>, Object> get(String name) {
        return implementation.get().get(name);
    }

    private Object internalGet(String name, Class<?> targetClass) {
        targetClass = normalize(targetClass);

        Map<Class<?>, Object> valueMap = implementation.get().get(name);
        if (valueMap == null)
            return null;
        if (valueMap.containsKey(targetClass)) {
            return valueMap.get(targetClass);
        }

        for (Class<?> keyClass : valueMap.keySet()) {
            if (targetClass.isAssignableFrom(keyClass))
                return valueMap.get(keyClass);
        }

        return null;
    }

    public void clear() {
        implementation.get().clear();
    }

    private Class<?> normalize(Class<?> targetClass) {
        if (targetClass == byte.class)
            return Byte.class;
        if (targetClass == char.class)
            return Character.class;
        if (targetClass == short.class)
            return Short.class;
        if (targetClass == int.class)
            return Integer.class;
        if (targetClass == long.class)
            return Long.class;
        if (targetClass == double.class)
            return Double.class;
        if (targetClass == float.class)
            return Float.class;
        if (targetClass == boolean.class)
            return Boolean.class;
        return targetClass;
    }

}
