package com.stresstest.random.constructor;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Wraps {@link Class} reflection access to limit visibility of methods, constructors and fields, used for initialization.
 * 
 * @author Anton Oparin
 * 
 * @param <T>
 *            {@link Class} parameter.
 */
abstract public class ClassAccessWrapper<T> {

    /**
     * Get's source {@link Class} for transformation.
     * 
     * @return target {@link Class}.
     */
    abstract public Class<T> getSourceClass();

    /**
     * Wrapper for {@link Class.getModifiers()} method.
     * 
     * @return look at {@link Class.getModifiers()}.
     */
    final public int getModifiers() {
        return getSourceClass().getModifiers();
    }

    /**
     * Checks, whether provided {@link Class} can replace source {@link Class}.
     * 
     * @param replacementCandidate
     *            {@link Class} that can replace source {@link Class}.
     * @return <code>true</code> if provided {@link Class} can replace source {@link Class}, <code>false</code> otherwise.
     */
    final public boolean canBeReplacedWith(Class<?> replacementCandidate) {
        return getSourceClass().isAssignableFrom(replacementCandidate);
    }

    /**
     * Checks, whether source {@link Class} can replace provided {@link Class}.
     * 
     * @param classToReplace
     *            {@link Class} that can be replaced by provided {@link Class}.
     * @return <code>true</code> if source {@link Class} can replace provided {@link Class}, <code>false</code> otherwise.
     */
    final public boolean canReplace(Class<?> classToReplace) {
        return classToReplace.isAssignableFrom(getSourceClass());
    }

    /**
     * Checks, whether source class can be constructed
     * 
     * @return true if this class can be constructed (not abstract, or interface), false otherwise
     */
    final public boolean constructable() {
        return (getSourceClass().getModifiers() & Modifier.ABSTRACT) == 0 && !getSourceClass().isInterface();
    }

    /**
     * Returns all including inherited {@link Method}s, available for access with this restrictions.
     * 
     * @return all the {@link Method}s, available for access with this restrictions.
     */
    final public Collection<Method> getMethods() {
        // Step 1. Add all available methods.
        Collection<Method> resultMethods = new ArrayList<Method>(extractMethods());
        // Step 2. Add all methods from superclass
        if (getSourceClass().getSuperclass() != null && getSourceClass().getSuperclass() != Object.class)
            resultMethods.addAll(wrap(getSourceClass().getSuperclass()).getMethods());
        return resultMethods;
    }

    /**
     * {@link Collection} of {@link Methods} available for access with this wrapper.
     * 
     * @return {@link Method}s available for access with this access level.
     */
    abstract protected Collection<Method> extractMethods();

    /**
     * {@link Collection} of {@link Constructor} available for access with this wrapper.
     * 
     * @return {@link Constructor} available for access with this access level.
     */
    abstract public Constructor<?>[] getConstructors();

    /**
     * {@link Collection} of {@link Field} available for access with this wrapper.
     * 
     * @return {@link Field}s available for access with this access level.
     */
    final public Collection<Field> getFields() {
        // Step 1. Generating Collection of extracted Fields
        Collection<Field> resultFields = new ArrayList<Field>(extractFields());
        // Step 2. Add all fields from super Class
        if (getSourceClass().getSuperclass() != Object.class && getSourceClass().getSuperclass() != null)
            resultFields.addAll(wrap(getSourceClass().getSuperclass()).getFields());
        // Step 3. Return result
        return resultFields;
    }

    /**
     * {@link Collection} of {@link Field} available for access with this wrapper.
     * 
     * @return {@link Field}s available for access with this access level.
     */
    abstract protected Collection<Field> extractFields();

    /**
     * Returns wrapper of the {@link Class}, with the same level of access as original wrapper.
     * 
     * @param forClass
     *            {@link Class} to wrap.
     * @return wrapper of the {@link Class}, with the same level of access as original wrapper.
     */
    abstract public <S> ClassAccessWrapper<S> wrap(Class<S> forClass);

    /**
     * Wrapper that provides access only to publicly available fields, methods and constructors.
     * 
     * @author Anton Oparin
     * 
     * @param {@link Class} parameter.
     */
    private static class PublicClassAccessWrapper<T> extends ClassAccessWrapper<T> {

        /**
         * Source {@link Class}.
         */
        final private Class<T> sourceClass;

        /**
         * Default constructor.
         * 
         * @param targetClass
         *            source {@link Class}.
         */
        public PublicClassAccessWrapper(Class<T> targetClass) {
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
        public <S> ClassAccessWrapper<S> wrap(Class<S> forClass) {
            return new PublicClassAccessWrapper<S>(forClass);
        }

    }

    /**
     * Wrapper that provides access only to all (public, protected, private) available fields, methods and constructors.
     * 
     * @author Anton Oparin
     * 
     * @param <T>
     *            {@link Class} parameter.
     */
    private static class FullClassAccessWrapper<T> extends ClassAccessWrapper<T> {

        /**
         * Source {@link Class}.
         */
        final private Class<T> sourceClass;

        /**
         * Default constructor.
         * 
         * @param targetClass
         *            source {@link Class}
         */
        public FullClassAccessWrapper(Class<T> targetClass) {
            this.sourceClass = checkNotNull(targetClass);
        }

        @Override
        public Class<T> getSourceClass() {
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
        public <S> ClassAccessWrapper<S> wrap(Class<S> forClass) {
            return new FullClassAccessWrapper<S>(forClass);
        }

    }

    /**
     * Factory method, that creates wrapper with only public access to variables in Class.
     * 
     * @param classToWrap
     *            Class to wrap.
     * @return {@link ClassAccessWrapper} with only publicly available access.
     */
    static public <T> ClassAccessWrapper<T> createPublicAccessor(final Class<T> classToWrap) {
        return new PublicClassAccessWrapper<T>(classToWrap);
    }

    /**
     * Factory method, that creates wrapper with access to all variables in Class.
     * 
     * @param classToWrap
     *            Class to wrap.
     * @return {@link ClassAccessWrapper} with only publicly available access.
     */
    static public <T> ClassAccessWrapper<T> createAllMethodsAccessor(final Class<T> classToWrap) {
        return new FullClassAccessWrapper<T>(classToWrap);
    }

    @Override
    public String toString() {
        return "ClassAccessWrapper [" + getSourceClass().getSimpleName() + "]";
    }

}
