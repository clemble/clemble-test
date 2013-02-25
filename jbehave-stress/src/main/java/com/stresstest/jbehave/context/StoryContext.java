package com.stresstest.jbehave.context;

import java.util.HashMap;
import java.util.Map;

import com.stresstest.concurrent.ThreadLocalMap;
import com.stresstest.concurrent.ValueFactory;

public class StoryContext extends ThreadLocalMap<String, Map<Class<?>, Object>> {

    public StoryContext() {
        super(new ValueFactory<Map<String, Map<Class<?>, Object>>>() {
            @Override
            public Map<String, Map<Class<?>, Object>> create() {
                return new HashMap<String, Map<Class<?>, Object>>();
            }
        });
    }

    public void put(String name, Object message) {
        if (message == null || name == null || message.getClass() == Void.class)
            return;
        get(name).put(message.getClass(), message);
    }

    public Object get(String name, Class<?> targetClass) {
        Map<Class<?>, Object> valueMap = get(name);
        if (valueMap.containsKey(targetClass))
            return valueMap.get(targetClass);

        for (Class<?> keyClass : valueMap.keySet()) {
            if (targetClass.isAssignableFrom(keyClass))
                return valueMap.get(keyClass);
        }

        return null;
    }

    @Override
    public Map<Class<?>, Object> put(String arg0, Map<Class<?>, Object> arg1) {
        throw new IllegalAccessError("Don't use this");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Map<Class<?>, Object>> arg0) {
        throw new IllegalAccessError("Don't use this");
    }

}
