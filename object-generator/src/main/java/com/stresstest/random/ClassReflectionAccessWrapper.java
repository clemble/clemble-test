package com.stresstest.random;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

abstract public class ClassReflectionAccessWrapper<T> {

    abstract protected Class<T> getSourceClass();

    final public int getModifiers() {
        return getSourceClass().getModifiers();
    }

    final public boolean canBeReplacedWith(Class<?> replacementCandidate) {
        return replacementCandidate != null && getSourceClass().isAssignableFrom(replacementCandidate);
    }

    final public boolean canBeReplacedWith(ClassReflectionAccessWrapper<?> replacementCandidate) {
        return replacementCandidate != null && getSourceClass().isAssignableFrom(replacementCandidate.getSourceClass());
    }

    final public boolean canReplace(Class<?> classToReplace) {
        return classToReplace == null || classToReplace.isAssignableFrom(getSourceClass());
    }

    final public Collection<Method> getMethods() {
        Collection<Method> resultMethods = new ArrayList<Method>();
        if (getSourceClass() == Object.class)
            return resultMethods;
        Collection<Method> methods = Collections2.filter(extractMethods(), new Predicate<Method>() {
            @Override
            public boolean apply(Method input) {
                return input.getDeclaringClass() != Object.class;
            }
        });

        resultMethods.addAll(methods);
        if (getSourceClass().getSuperclass() != null)
            resultMethods.addAll(wrap(getSourceClass().getSuperclass()).getMethods());
        return resultMethods;
    }

    abstract protected Collection<Method> extractMethods();

    abstract public Constructor<?>[] getConstructors();

    final public Collection<Field> getFields() {
        Collection<Field> resultFields = new ArrayList<Field>();
        if (getSourceClass() == Object.class)
            return resultFields;
        resultFields.addAll(extractFields());
        resultFields.addAll(wrap(getSourceClass().getSuperclass()).getFields());
        return resultFields;
    }

    abstract protected Collection<Field> extractFields();

    abstract public <S> ClassReflectionAccessWrapper<S> wrap(Class<S> forClass);

    private static class PublicMethodReflectionAccessWrapper<T> extends ClassReflectionAccessWrapper<T> {

        final private Class<T> sourceClass;

        public PublicMethodReflectionAccessWrapper(Class<T> targetClass) {
            this.sourceClass = checkNotNull(targetClass);
        }

        @Override
        public Class<T> getSourceClass() {
            return sourceClass;
        }

        @Override
        protected Collection<Method> extractMethods() {
            return Arrays.asList(sourceClass.getMethods());
        }

        @Override
        public Collection<Field> extractFields() {
            return Arrays.asList(sourceClass.getFields());
        }

        @Override
        public Constructor<?>[] getConstructors() {
            return sourceClass.getConstructors();
        }

        @Override
        public <S> ClassReflectionAccessWrapper<S> wrap(Class<S> forClass) {
            return new PublicMethodReflectionAccessWrapper<S>(forClass);
        }

    }

    private static class AllMethodReflectionAccessWrapper<T> extends ClassReflectionAccessWrapper<T> {

        final private Class<T> sourceClass;

        public AllMethodReflectionAccessWrapper(Class<T> targetClass) {
            this.sourceClass = checkNotNull(targetClass);
        }

        @Override
        protected Class<T> getSourceClass() {
            return sourceClass;
        }

        @Override
        protected Collection<Method> extractMethods() {
            return Arrays.asList(sourceClass.getDeclaredMethods());
        }

        @Override
        protected Collection<Field> extractFields() {
            return Arrays.asList(sourceClass.getDeclaredFields());
        }

        @Override
        public Constructor<?>[] getConstructors() {
            return sourceClass.getDeclaredConstructors();
        }

        @Override
        public <S> ClassReflectionAccessWrapper<S> wrap(Class<S> forClass) {
            return new AllMethodReflectionAccessWrapper<S>(forClass);
        }

    }

    static public <T> ClassReflectionAccessWrapper<T> createPublicAccessor(final Class<T> classToWrap) {
        return new PublicMethodReflectionAccessWrapper<T>(classToWrap);
    }

    static public <T> ClassReflectionAccessWrapper<T> createAllMethodsAccessor(final Class<T> classToWrap) {
        return new AllMethodReflectionAccessWrapper<T>(classToWrap);
    }

}
