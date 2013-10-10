package com.clemble.test.spring.cleaners.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;

import com.clemble.test.spring.cleaners.CleanableFactory;
import com.clemble.test.spring.cleaners.context.CleanerContext;

public class CleanerSpringAdvisor extends ProxyConfig implements BeanPostProcessor, BeanClassLoaderAware, BeanFactoryAware,
		InitializingBean, Ordered {

	/**
	 * Generated 23/02/13
	 */
	private static final long serialVersionUID = -2003741391204658480L;

	final private Advice advice = new MethodInterceptor() {
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			// Step 1. Checking this was applied to appropriate
			Object[] arguments = invocation.getArguments();
			for(Object argument: arguments) {
				if (argument != null && CleanableFactory.canApply(argument.getClass())) {
					cleanerContext.add(argument);
				}
			}
			// Step 2. Invoking underlying class
			Object value = invocation.proceed();
			// Step 2.1. Adding value to the CleanerContext
			if (value != null && CleanableFactory.canApply(value.getClass())) {
				cleanerContext.add(value);
			}
			// Step 2.3. Returning result
			return value;
		}
	};

	final private Advisor cleanerAdvisor = new CleanerPointcutAdvisor(advice);

	final private String[] packages;

	private volatile CleanerContext cleanerContext;

	private volatile BeanFactory beanFactory;

	private volatile ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	public CleanerSpringAdvisor(String[] packages) {
		this.packages = packages == null ? new String[0] : packages;
	}

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
		this.cleanerContext = beanFactory.getBean(CleanerContext.class);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		bean = advice(cleanerAdvisor, bean);
		return bean;
	}

	private Object advice(Advisor advisor, Object bean) {
		Class<?> targetClass = AopUtils.getTargetClass(bean);
		if (targetClass == null) {
			return bean;
		}
		
		bean.getClass().getPackage();

		Class<?> currentClass = bean.getClass();
		while(currentClass != Object.class) {
			if(currentClass.getAnnotation(Configuration.class) != null)
				return bean;
			currentClass = currentClass.getSuperclass();
		}

		boolean matches = packages.length == 0;
		if (!matches) {
			String className = targetClass.getName();
			for (String packageName : packages)
				matches = matches || className.startsWith(packageName);
		}
		if (matches) {
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