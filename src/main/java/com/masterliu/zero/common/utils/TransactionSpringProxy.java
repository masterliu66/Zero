package com.masterliu.zero.common.utils;

import com.masterliu.zero.common.exception.ApplicationException;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionSpringProxy extends AbstractSpringProxy {

    List<Advisor> advisors;

    public TransactionSpringProxy(DataSource dataSource) {
        super();
        this.advisors = new ArrayList<>();
        init(dataSource);
    }

    @SuppressWarnings("unchecked")
    private void init(DataSource dataSource) {
        registerBeanDefinition(ConfigurationClassPostProcessor.class);
        registerBeanDefinition(ProxyTransactionManagementConfiguration.class);
        registerBeanDefinition(DataSourceTransactionManager.class);
        ConfigurationClassPostProcessor classPostProcessor = beanFactory.getBean(ConfigurationClassPostProcessor.class);
        classPostProcessor.postProcessBeanDefinitionRegistry(beanFactory);
        String[] advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                this.beanFactory, Advisor.class, true, false);
        for (String advisorName : advisorNames) {
            advisors.add(this.beanFactory.getBean(advisorName, Advisor.class));
        }

        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        try {
            Field field = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
            field.setAccessible(true);
            Map<String, Object> singletonObjects = (Map<String, Object>) field.get(beanFactory);
            singletonObjects.put(DataSourceTransactionManager.class.getName(), transactionManager);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    protected List<Advisor> getAdvisors(Class<?> beanClass) {
        return advisors;
    }

    private void registerBeanDefinition(Class<?> clz) {
        RootBeanDefinition def = new RootBeanDefinition(clz);
        beanFactory.registerBeanDefinition(def.getBeanClassName(), def);
    }

}
