package com.masterliu.zero.common.utils;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

import java.util.List;

public abstract class AbstractSpringProxy {

    protected DefaultListableBeanFactory beanFactory;

    public AbstractSpringProxy() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    protected abstract List<Advisor> getAdvisors(Class<?> beanClass);

    @SuppressWarnings("unchecked")
    public <T> T getProxy(T bean) {

        List<Advisor> advisors = getAdvisors(bean.getClass());
        if (advisors.isEmpty()) {
            return bean;
        }

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvisors(advisors);
        proxyFactory.setTargetSource(new SingletonTargetSource(bean));

        return (T) proxyFactory.getProxy(ClassUtils.getDefaultClassLoader());
    }

}
