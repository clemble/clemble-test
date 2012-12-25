package com.stresstest.utils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;

import org.reflections.Reflections;

public class ReflectionUtils {

    private ReflectionUtils() {
        throw new IllegalAccessError();
    }

    public static <T extends Annotation> T getAnnotation(Class<?> klass, Class<T> annotationClass) {
        if (Object.class == klass)
            return null;
        T result = klass.getAnnotation(annotationClass);
        return result != null ? result : getAnnotation(klass.getSuperclass(), annotationClass);
    }

    public static <T> Collection<Class<? extends T>> getPossibleImplementations(Class<T> classToGenerate) {
        Reflections reflections = new Reflections(classToGenerate.getPackage().toString().replace("package ", ""));
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(classToGenerate);
        return subTypes;
    }
}
