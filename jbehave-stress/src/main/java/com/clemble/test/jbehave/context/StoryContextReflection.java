package com.clemble.test.jbehave.context;

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
            try {
                ProxyFactory proxyFactory = new ProxyFactory(bean);
                proxyFactory.setProxyTargetClass(true);
                proxyFactory.addInterface(StoryContextAware.class);
                proxyFactory.addAdvice(new StoryContextAwareMethodInterceptor(name));
                proxyFactory.setTargetClass(bean.getClass());
                proxyFactory.setTarget(bean);
                bean = (T) proxyFactory.getProxy(bean.getClass().getClassLoader());
            } catch (Throwable throwable) {
            }
        }
        return bean;
    }

    public static class StoryContextAwareMethodInterceptor implements MethodInterceptor {
        private String stringPresentation;

        public StoryContextAwareMethodInterceptor(String toString) {
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