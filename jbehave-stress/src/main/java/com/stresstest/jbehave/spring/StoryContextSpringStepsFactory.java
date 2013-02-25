package com.stresstest.jbehave.spring;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.steps.AbstractStepsFactory;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;

/**
 * Override of {@link SpringStepsFactory} adding ability to intercept Advices beans.
 */
public class StoryContextSpringStepsFactory extends AbstractStepsFactory {

	private final ApplicationContext context;

	public StoryContextSpringStepsFactory(Configuration configuration, ApplicationContext context) {
		super(configuration);
		this.context = context;
	}

	@Override
	protected List<Class<?>> stepsTypes() {
		List<Class<?>> types = new ArrayList<Class<?>>();
		for (String name : context.getBeanDefinitionNames()) {
			Object bean = context.getBean(name);
			if (isAllowed(bean)) {
				types.add(bean.getClass());
			}
		}
		return types;
	}

	/**
	 * Checks if type returned from context is allowed, i.e. not null and not
	 * abstract.
	 * 
	 * @param type
	 *            the Class of the bean
	 * @return A boolean, <code>true</code> if allowed
	 */
	protected boolean isAllowed(Object bean) {
		Class<?> targetClass = bean.getClass();
		try {
			while (bean instanceof Advised) {
				targetClass = ((Advised) bean).getTargetClass();
				bean = ((Advised) bean).getTargetSource().getTarget();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return targetClass != null && !Modifier.isAbstract(targetClass.getModifiers()) && hasAnnotatedMethods(targetClass);
	}

	public Object createInstanceOfType(Class<?> type) {
		return context.getBean(type);
	}

}
