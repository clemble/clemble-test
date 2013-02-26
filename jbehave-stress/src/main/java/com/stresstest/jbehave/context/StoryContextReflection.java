package com.stresstest.jbehave.context;

import java.lang.reflect.Field;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;

public class StoryContextReflection {
    final private static String FIELD_NAME = "_storyContextId";

    public static String getName(Object bean) {
        if (bean instanceof CharSequence)
            return bean.toString();

        try {
            Field storyContextField = bean.getClass().getField(FIELD_NAME);
            return (String) storyContextField.get(bean);
        } catch (Exception noSuchFieldException) {
            // Ignore no harm done
        }

        return String.valueOf(bean);
    }

    public static<T> void setName(T bean, String name) {
        Field storyContextField = null;
        try {
            storyContextField = bean.getClass().getField(FIELD_NAME);
        } catch (NoSuchFieldException noSuchFieldException) {
            try {
                Class<?> type = bean.getClass();
                CtClass ctClass = ClassPool.getDefault().get(type.getName());
                ctClass.addField(CtField.make("public String " + FIELD_NAME + ";", ctClass));
                ctClass.writeFile();
                storyContextField = bean.getClass().getField(FIELD_NAME);
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
