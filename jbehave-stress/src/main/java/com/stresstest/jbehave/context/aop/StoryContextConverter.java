package com.stresstest.jbehave.context.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import net.sf.cglib.proxy.MethodProxy;

import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import com.stresstest.jbehave.context.StoryContext;

public class StoryContextConverter implements ParameterConverter {

    @Autowired
    public StoryContext testContext;

    @Autowired
    public ConversionService conversionService;

    @Override
    public boolean accept(Type type) {
        return true;
    }

    @Override
    public Object convertValue(String source, final Type type) {
        if (type instanceof Class) {
            if (type.equals(String.class))
                return source;
            Object bean = testContext.get(source, (Class<?>) type);
            if (bean != null)
                return bean;
            if (conversionService.canConvert(String.class, (Class<?>) type))
                return conversionService.convert(source, (Class<?>) type);
        }
        throw new IllegalArgumentException("Can't find " + type + " for context " + source);
    }

    public static class ToStringInterceptor implements net.sf.cglib.proxy.MethodInterceptor {
        final private String stringPresentation;

        public ToStringInterceptor(final String name) {
            this.stringPresentation = name;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            if (method.getName().equals("toString"))
                return stringPresentation;
            return proxy.invokeSuper(obj, args);
        }

    }
}
