package com.stresstest.jbehave.context.aop;

import java.lang.reflect.Type;

import org.jbehave.core.steps.ParameterConverters.ParameterConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

import com.stresstest.jbehave.context.StoryContext;

public class StoryContextConverter implements ParameterConverter  {
    
    @Autowired
    public StoryContext testContext;
    
    @Autowired
    public ConversionService conversionService;

    @Override
    public boolean accept(Type type) {
        return true;
    }

    @Override
    public Object convertValue(String source, Type type) {
        if(conversionService.canConvert(String.class, type.getClass()))
            return conversionService.convert(source, type.getClass());
        return testContext.get(source, type.getClass());
    }
}
