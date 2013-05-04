package com.stresstest.jbehave.context.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContextManager;

public class StoryContextListenerProcessingBean implements BeanPostProcessor, Ordered {

    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof TestContextManager) {
            ((TestContextManager) bean).registerTestExecutionListeners(new StoryContextTestExecutionListener());
        }

        return bean;
    }

}
