package com.stresstest.spring.listener;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;

public class TestContextListenerRegistrator implements BeanPostProcessor, Ordered {
	
	final private Collection<TestExecutionListener> testExecutionListeners;
	
	public TestContextListenerRegistrator(TestExecutionListener executionListener) {
		testExecutionListeners = Collections.singleton(executionListener);
	}

    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof TestContextManager) {
       		((TestContextManager) bean).registerTestExecutionListeners(testExecutionListeners.toArray(new TestExecutionListener[0]));
        }

        return bean;
    }

}
