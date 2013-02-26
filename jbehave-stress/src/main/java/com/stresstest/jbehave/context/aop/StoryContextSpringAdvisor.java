package com.stresstest.jbehave.context.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;

import com.stresstest.jbehave.context.StoryContext;

public class StoryContextSpringAdvisor extends ProxyConfig implements BeanPostProcessor, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, Ordered {

    /**
     * Generated 23/02/13
     */
    private static final long serialVersionUID = -2003741391204658480L;

    private volatile StoryContext testContext;

    final private Advice advice = new MethodInterceptor() {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // Step 1. Checking this was applied to appropriate
            Object[] arguments = invocation.getArguments();
            // Step 2. Invoking underlying class
            Object result = invocation.proceed();
            // Step 2.1. Adding value to the Map
            if (arguments != null && arguments.length == 1)
                testContext.put(arguments[0], result);
            // Step 2.3. Returning result
            return result;
        }
    };

    private volatile Advisor givenAdvisor = new BasicAnnotationAdvisor(Given.class, advice);
    private volatile Advisor whenAdvisor = new BasicAnnotationAdvisor(When.class, advice);
    private volatile Advisor thenAdvisor = new BasicAnnotationAdvisor(Then.class, advice);

    private volatile BeanFactory beanFactory;

    private volatile ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void afterPropertiesSet() {
        this.testContext = beanFactory.getBean(StoryContext.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        bean = advice(givenAdvisor, bean);
        bean = advice(whenAdvisor, bean);
        bean = advice(thenAdvisor, bean);
        return bean;
    }

    private Object advice(Advisor advisor, Object bean) {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (targetClass == null) {
            return bean;
        }

        if (AopUtils.canApply(advisor, targetClass)) {
            if (bean instanceof Advised) {
                ((Advised) bean).addAdvisor(advisor);
            } else {
                ProxyFactory proxyFactory = new ProxyFactory(bean);
                // Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
                proxyFactory.copyFrom(this);
                proxyFactory.addAdvisor(advisor);
                bean = proxyFactory.getProxy(this.beanClassLoader);
            }
        }

        return bean;
    }

}
