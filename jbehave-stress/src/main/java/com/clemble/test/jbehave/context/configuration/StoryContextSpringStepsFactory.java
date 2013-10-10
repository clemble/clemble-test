package com.clemble.test.jbehave.context.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.jbehave.core.annotations.AsParameterConverter;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.steps.AbstractStepsFactory;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.ParameterConverters.MethodReturningConverter;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.context.ApplicationContext;

/**
 * Override of {@link SpringStepsFactory} adding ability to intercept Advices beans.
 */
public class StoryContextSpringStepsFactory extends AbstractStepsFactory {

    private final ApplicationContext context;

    final private Configuration configuration;

    public StoryContextSpringStepsFactory(Configuration configuration, ApplicationContext context) {
        super(configuration);
        this.context = context;
        this.configuration = configuration;
    }

    @Override
    public List<CandidateSteps> createCandidateSteps() {
        List<Class<?>> types = stepsTypes();
        List<CandidateSteps> steps = new ArrayList<CandidateSteps>();
        for (Class<?> type : types) {
            configuration.parameterConverters().addConverters(methodReturningConverters(type));
            steps.add(new StorySteps(configuration, type, this));
        }
        return steps;
    }

    private List<ParameterConverter> methodReturningConverters(Class<?> type) {
        List<ParameterConverter> converters = new ArrayList<ParameterConverter>();

        while (type != Object.class) {
            for (Method method : type.getMethods()) {
                if (method.isAnnotationPresent(AsParameterConverter.class)) {
                    converters.add(new MethodReturningConverter(method, type, this));
                }
            }
            type = type.getSuperclass();
        }

        return converters;
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

    @Override
    public Object createInstanceOfType(Class<?> type) {
        try {
            return context.getBean(type);
        } catch (RuntimeException runtimeException) {
            for(String beanName: context.getBeanDefinitionNames()) {
                Object candidateBean = context.getBean(beanName);
                if(type.isAssignableFrom(fetchTargetClass(candidateBean)))
                    return candidateBean;
            }
        }
        throw new RuntimeException();
    }

}
