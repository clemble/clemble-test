package com.clemble.test.spring.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

public class MethodAndClassAnnotationAdvisor extends AbstractPointcutAdvisor {

    /**
     * Generated 1/09/2012
     */
    private static final long serialVersionUID = -5960130626158352051L;

    private Advice advice;

    private Class<? extends Annotation> targetClass;

    public MethodAndClassAnnotationAdvisor(Class<? extends Annotation> targetClass, Advice advice) {
        this.targetClass = targetClass;
        this.advice = advice;
    }

    public Advice getAdvice() {
        return this.advice;
    }

    public Pointcut getPointcut() {
        return this.buildPointcut();
    }

    private Pointcut buildPointcut() {
        Pointcut classPointCut = new MetaAnnotationMatchingPointcut(targetClass, true);
        Pointcut methodPointCut = new MetaAnnotationMatchingPointcut(null, targetClass);
        return new ComposablePointcut(classPointCut).union(methodPointCut);
    }

    private static class MetaAnnotationMatchingPointcut implements Pointcut {

        private final ClassFilter classFilter;

        private final MethodMatcher methodMatcher;

        /**
         * Create a new MetaAnnotationMatchingPointcut for the given annotation type.
         * 
         * @param classAnnotationType the annotation type to look for at the class level
         * @param checkInherited whether to explicitly check the superclasses and interfaces for the annotation type as well (even if the annotation type is not
         *            marked as inherited itself)
         */
        private MetaAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, boolean checkInherited) {
            this.classFilter = new AnnotationClassFilter(classAnnotationType, checkInherited);
            this.methodMatcher = MethodMatcher.TRUE;
        }

        /**
         * Create a new MetaAnnotationMatchingPointcut for the given annotation type.
         * 
         * @param classAnnotationType the annotation type to look for at the class level (can be <code>null</code>)
         * @param methodAnnotationType the annotation type to look for at the method level (can be <code>null</code>)
         */
        private MetaAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, Class<? extends Annotation> methodAnnotationType) {

            Assert.isTrue((classAnnotationType != null || methodAnnotationType != null),
                    "Either Class annotation type or Method annotation type needs to be specified (or both)");

            if (classAnnotationType != null) {
                this.classFilter = new AnnotationClassFilter(classAnnotationType);
            } else {
                this.classFilter = ClassFilter.TRUE;
            }

            if (methodAnnotationType != null) {
                this.methodMatcher = new MetaAnnotationMethodMatcher(methodAnnotationType);
            } else {
                this.methodMatcher = MethodMatcher.TRUE;
            }
        }

        public ClassFilter getClassFilter() {
            return this.classFilter;
        }

        public MethodMatcher getMethodMatcher() {
            return this.methodMatcher;
        }
    }

    private static class MetaAnnotationMethodMatcher extends AnnotationMethodMatcher {

        private final Class<? extends Annotation> annotationType;

        /**
         * Create a new AnnotationClassFilter for the given annotation type.
         * 
         * @param annotationType the annotation type to look for
         */
        private MetaAnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
            super(annotationType);
            this.annotationType = annotationType;
        }

        @Override
        @SuppressWarnings("rawtypes")
        public boolean matches(Method method, Class targetClass) {
            if (AnnotationUtils.getAnnotation(method, this.annotationType) != null) {
                return true;
            }
            // The method may be on an interface, so let's check on the target class as well.
            Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            return (specificMethod != method && (AnnotationUtils.getAnnotation(specificMethod, this.annotationType) != null));
        }
    }
}
