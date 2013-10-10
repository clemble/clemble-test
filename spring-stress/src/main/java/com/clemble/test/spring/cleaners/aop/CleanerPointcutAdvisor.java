package com.clemble.test.spring.cleaners.aop;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

import com.clemble.test.spring.cleaners.CleanableFactory;
import com.clemble.test.spring.cleaners.annotation.Cleaner;

public class CleanerPointcutAdvisor extends AbstractPointcutAdvisor {

	/**
	 * Generated 1/09/2012
	 */
	private static final long serialVersionUID = -5960130626158352051L;

	private Advice advice;

	public CleanerPointcutAdvisor(Advice advice) {
		this.advice = advice;
	}

	public Advice getAdvice() {
		return this.advice;
	}

	public Pointcut getPointcut() {
		final MethodMatcher methodMatcher = new MetaAnnotationValueMatcher();
		return new Pointcut() {

			@Override
			public MethodMatcher getMethodMatcher() {
				return methodMatcher;
			}

			@Override
			public ClassFilter getClassFilter() {
				return ClassFilter.TRUE;
			}
		};
	}

	private static class MetaAnnotationValueMatcher extends AnnotationMethodMatcher {

		private MetaAnnotationValueMatcher() {
			super(Cleaner.class);
		}

		@Override
		@SuppressWarnings("rawtypes")
		public boolean matches(Method method, Class targetClass) {
			// Step 1. Getting returned value
			Class<?> value = method.getReturnType();
			// Step 2. Checking if we can clean created CleanableFactory
			return CleanableFactory.canApply(value);
		}
	}
}
