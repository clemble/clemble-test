package com.clemble.test.jbehave.context.aop;

import java.lang.reflect.Type;

import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import com.clemble.test.jbehave.context.StoryContext;

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

}