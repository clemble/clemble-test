package com.stresstest.jbehave.context;

import java.lang.reflect.Field;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;

public class StoryContextReflection {

    public static void setField(Object bean, String name) {
        Field storyContextField = null;
        try {
            storyContextField = bean.getClass().getField("storyContextString");
        } catch (NoSuchFieldException noSuchFieldException) {
            try {
                Class<?> type = bean.getClass();
                CtClass ctClass = ClassPool.getDefault().get(type.getName());
                ctClass.addField(CtField.make("public String storyContextString;", ctClass));
                ctClass.writeFile();
                storyContextField = bean.getClass().getField("storyContextString");
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        
        try {
            storyContextField.set(bean, name);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
