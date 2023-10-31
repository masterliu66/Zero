package com.masterliu.zero.common.utils;

import com.masterliu.zero.common.exception.ApplicationException;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJProxyUtils;
import org.springframework.aop.aspectj.annotation.AspectJAdvisorFactory;
import org.springframework.aop.aspectj.annotation.BeanFactoryAspectInstanceFactory;
import org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory;
import org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory;
import org.springframework.aop.framework.autoproxy.ProxyCreationContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AspectSpringProxy extends AbstractSpringProxy {

    AspectJAdvisorFactory advisorFactory;

    List<Class<?>> aspectClass;

    List<Advisor> candidateAdvisors;

    public AspectSpringProxy() {
        super();
        this.advisorFactory = new ReflectiveAspectJAdvisorFactory(beanFactory);
        this.aspectClass = new ArrayList<>();
        this.candidateAdvisors = new ArrayList<>();
    }

    public void addAdvisorClass(Class<?> clz) {
        String name = clz.getSimpleName();
        if (advisorFactory.isAspect(clz) && !beanFactory.containsBeanDefinition(name)) {
            aspectClass.add(clz);
            RootBeanDefinition beanDefinition = new RootBeanDefinition(clz);
            beanFactory.registerBeanDefinition(name, beanDefinition);
            MetadataAwareAspectInstanceFactory factory = new BeanFactoryAspectInstanceFactory(beanFactory, name);
            candidateAdvisors.addAll(advisorFactory.getAdvisors(factory));
        }
    }

    @Override
    protected List<Advisor> getAdvisors(Class<?> beanClass) {
        List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanClass.getSimpleName());
        AspectJProxyUtils.makeAdvisorChainAspectJCapableIfNecessary(eligibleAdvisors);
        if (!eligibleAdvisors.isEmpty()) {
            AnnotationAwareOrderComparator.sort(eligibleAdvisors);
        }
        return eligibleAdvisors;
    }

    @SuppressWarnings("unchecked")
    private List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {

        ThreadLocal<String> currentProxiedBeanName;
        try {
            Field field = ProxyCreationContext.class.getDeclaredField("currentProxiedBeanName");
            field.setAccessible(true);
            currentProxiedBeanName = (ThreadLocal<String>) field.get(null);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }

        currentProxiedBeanName.set(beanName);
        try {
            return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
        }
        finally {
            currentProxiedBeanName.remove();
        }
    }


}
