package com.stresstest.reflection;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;

/**
 * Collection of internal Reflection utilities used by stresstest packages.
 * 
 * @author Anton Oparin
 *
 */
public class ReflectionUtils {

    /**
     * Protected constructor
     */
    private ReflectionUtils() {
        throw new IllegalAccessError();
    }

    /**
     * Searches for annotation in provided and all parent Classes.
     * 
     * @param klass source Class
     * @param annotationClass searched annotation
     * @return annotation on this Class or any of it's subclasses
     */
    public static <T extends Annotation> T findAnnotation(Class<?> klass, Class<T> annotationClass) {
        if (Object.class == klass)
            return null;
        T result = klass.getAnnotation(annotationClass);
        return result != null ? result : findAnnotation(klass.getSuperclass(), annotationClass);
    }

    /**
     * Searches for possible implementations of the package in original package of the Class and all underlying packages.
     * 
     * @param klass     source Class
     * @return          all implementations that can be used as Class 
     *                  from original package and all sub packages.
     */
    public static <T> Set<Class<? extends T>> findPossibleImplementations(Class<T> klass) {
        return findPossibleImplementations(klass.getPackage().toString().replace("package ", ""), klass);
    }
    
    /**
     * Searches for possible implementations of the package in provided package and all underlying packages.
     * 
     * @param klass         source Class
     * @param packageName   search start point
     * @return              all implementations that can be used as Class 
     *                      from provided package and all sub packages.
     */
    public static <T> Set<Class<? extends T>> findPossibleImplementations(String packageName,Class<T> klass) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(klass);
        return subTypes;
    }
}
