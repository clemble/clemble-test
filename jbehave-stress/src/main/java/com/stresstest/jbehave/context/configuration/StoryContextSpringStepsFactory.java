package com.stresstest.jbehave.context.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
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
            Class<?> targetClass = fetchTargetClass(context.getBean(name));
            if (isAllowed(targetClass)) {
                types.add(targetClass);
            }
        }
        return types;
    }

    public Class<?> fetchTargetClass(Object bean) {
        Class<?> targetClass = bean.getClass();
        try {
            while (bean instanceof Advised) {
                targetClass = ((Advised) bean).getTargetClass();
                bean = ((Advised) bean).getTargetSource().getTarget();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return targetClass;
    }

    /**
     * Checks if type returned from context is allowed, i.e. not null and not
     * abstract.
     * 
     * @param type
     *            the Class of the bean
     * @return A boolean, <code>true</code> if allowed
     */
    protected boolean isAllowed(Class<?> targetClass) {
        return targetClass != null && !Modifier.isAbstract(targetClass.getModifiers()) && hasAnnotatedMethods(targetClass);
    }

    protected boolean hasAnnotatedMethods(Type type) {
        if (type instanceof Class<?>) {
            // Step 1. Check all the methods
            for (Method method : ((Class<?>) type).getMethods()) {
                for (Annotation annotation : method.getAnnotations()) {
                    if (annotation.annotationType().getName().startsWith("org.jbehave.core.annotations")) {
                        return true;
                    }
                }
            }
            // Step 2. Check all interfaces
            for (Class<?> inheritedInterface : ((Class<?>) type).getInterfaces()) {
                if (hasAnnotatedMethods(inheritedInterface))
                    return true;
            }
            // Step 3. Check superclass
            if (((Class<?>) type).getSuperclass() != Object.class) {
                return hasAnnotatedMethods(((Class<?>) type).getSuperclass());
            }
        }
        return false;
    }

    public Object createInstanceOfType(Class<?> type) {
        return context.getBean(type);
    }

}
