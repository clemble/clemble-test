package com.stresstest.jbehave.context;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

public class StoryContextReflection {
    public static String getName(Object bean) {
        if (bean instanceof StoryContextAware) {
            return ((StoryContextAware) bean).getStoryContextObject();
        }
        return String.valueOf(bean);
    }

    @SuppressWarnings("unchecked")
    public static <T> T setName(T bean, String name) {
        if (bean instanceof StoryContextAware) {
            ((StoryContextAware) bean).setStoryContextObject(name);
        } else {
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.setProxyTargetClass(true);
            proxyFactory.addInterface(StoryContextAware.class);
            proxyFactory.addAdvice(new ToString(name));
            proxyFactory.setTargetClass(bean.getClass());
            proxyFactory.setTarget(bean);
            bean = (T) proxyFactory.getProxy(bean.getClass().getClassLoader());
        }
        return bean;
    }

    public static class ToString implements MethodInterceptor {
        private String stringPresentation;

        public ToString(String toString) {
            this.stringPresentation = toString;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            if (invocation.getMethod().getName().equals("setStoryContextObject")) {
                stringPresentation = String.valueOf(invocation.getArguments()[0]);
                return Void.TYPE;
            }
            if (invocation.getMethod().getName().equals("getStoryContextObject")) {
                return stringPresentation;
            }
            return invocation.proceed();
        }

    }

}

/*
 * final private static String FIELD_NAME = "_storyContextId";
 * public static String getName(Object bean) {
 * if (bean instanceof CharSequence)
 * return bean.toString();
 * try {
 * Field storyContextField = bean.getClass().getField(FIELD_NAME);
 * return (String) storyContextField.get(bean);
 * } catch (Exception noSuchFieldException) {
 * // Ignore no harm done
 * }
 * return String.valueOf(bean);
 * }
 * public static<T> void setName(T bean, String name) {
 * Field storyContextField = null;
 * try {
 * storyContextField = bean.getClass().getField(FIELD_NAME);
 * } catch (NoSuchFieldException noSuchFieldException) {
 * try {
 * Class<?> type = bean.getClass();
 * CtClass ctClass = ClassPool.getDefault().get(type.getName());
 * ctClass.addField(CtField.make("public String " + FIELD_NAME + ";", ctClass));
 * ctClass.writeFile();
 * storyContextField = bean.getClass().getField(FIELD_NAME);
 * } catch (Exception exception) {
 * throw new RuntimeException(exception);
 * }
 * }
 * try {
 * storyContextField.set(bean, name);
 * } catch (Exception exception) {
 * throw new RuntimeException(exception);
 * }
 * }
 */