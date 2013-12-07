package com.clemble.test.spring.listener;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;

public class TestContextListenerRegistrator implements BeanPostProcessor, Ordered {

    private TestContextManager contextManager;
    final private Set<TestExecutionListener> testExecutionListeners = new HashSet<TestExecutionListener>();

    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof TestContextManager) {
            if (contextManager != null)
                throw new RuntimeException("TestContextManager already defined");
            contextManager = (TestContextManager) bean;
            contextManager.registerTestExecutionListeners(testExecutionListeners.toArray(new TestExecutionListener[0]));
        } else if (bean instanceof TestExecutionListener) {
            if(contextManager != null) {
                contextManager.registerTestExecutionListeners((TestExecutionListener) bean);
            } else {
                testExecutionListeners.add((TestExecutionListener) bean);
            }
        }

        return bean;
    }

}
